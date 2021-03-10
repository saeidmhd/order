package com.mahak.order.SDKUrovo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class BluetoothPrinter implements PrinterInterface {

    private static OutputStream myOutStream = null;
    private static InputStream myInStream = null;
    private static BluetoothSocket mySocket = null;
    private static BluetoothAdapter myBluetoothAdapter;
    private static BluetoothDevice myDevice;
    private static Bitmap myBitmap = null;
    private static Canvas myCanvas = null;
    private static Paint myPaint = null;
    private static int myBitmapHeight = 0;
    private static int myBitmapWidth = 0;
    private static int PrinterDotWidth = 800;
    private UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothPrinter() {

    }

    public static int zp_printer_status_get(int timeout) {
        byte[] readata = new byte[4];
        int a = 0;
        if (!SPPReadTimeout(readata, 4, timeout)) {
            return -1;
        } else if (readata[0] != 29) {
            return -1;
        } else if (readata[1] != -103) {
            return -1;
        } else if (readata[3] != -1) {
            return -1;
        } else {
            byte status = readata[2];
            if ((status & 1) != 0) {
                a = 1;
            }

            if ((status & 2) != 0) {
                a = 2;
            }

            return a;
        }
    }

    public boolean connect(String address) {
        if (address == "") {
            return false;
        } else if (mySocket == null || !mySocket.isConnected()) {
            myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (myBluetoothAdapter == null) {
                return false;
            } else {
                myDevice = myBluetoothAdapter.getRemoteDevice(address);
                if (myDevice == null) {
                    return false;
                } else {
                    return this.SPPOpen(myBluetoothAdapter, myDevice);
                }
            }
        }
        return true;
    }

    public boolean check_connect() {
        return mySocket != null;
    }

    public void disconnect() {
        this.SPPClose();
    }

    public void zp_page_print() {
        if (myBitmapWidth > PrinterDotWidth) {
            myBitmapWidth = PrinterDotWidth;
        }

        int len = (myBitmapWidth + 7) / 8;
        byte[] data = new byte[(len + 4) * myBitmapHeight];
        int ndata = 0;
        int[] RowData = new int[myBitmapWidth * myBitmapHeight];
        myBitmap.getPixels(RowData, 0, myBitmapWidth, 0, 0, myBitmapWidth, myBitmapHeight);

        for (int i = 0; i < myBitmapHeight; ++i) {
            data[ndata + 0] = 31;
            data[ndata + 1] = 16;
            data[ndata + 2] = (byte) (len % 256);
            data[ndata + 3] = (byte) (len / 256);

            int j;
            for (j = 0; j < len; ++j) {
                data[ndata + 4 + j] = 0;
            }

            int size;
            for (j = 0; j < myBitmapWidth; ++j) {
                size = RowData[i * myBitmapWidth + j];
                int b = size >> 0 & 255;
                int g = size >> 8 & 255;
                int r = size >> 16 & 255;
                int grey = (r + g + b) / 3;
                if (grey < 153) {
                    int tmp222_221 = ndata + 4 + j / 8;
                    data[tmp222_221] |= (byte) (128 >> j % 8);
                }
            }

            for (size = len - 1; size >= 0 && data[ndata + 4 + size] == 0; --size) {
            }

            data[ndata + 2] = (byte) (len % 256);
            data[ndata + 3] = (byte) (len / 256);
            ndata += 4 + len;
        }

        data = GZIPFrame.codec(data, ndata);
        SPPWrite(data, data.length);
        zp_page_free();
        zp_page_clear();
    }

    public static void zp_page_free() {
        myBitmap.recycle();
        myBitmap = null;
        myCanvas = null;
        myPaint = null;
        myBitmapHeight = 0;
        myBitmapWidth = 0;
    }

    public static void zp_page_clear() {
        if (myCanvas != null) {
            myCanvas.drawColor(-1);
        }

    }

    public static boolean zp_page_create(double pagewidth, double pageheight) {
        myBitmapWidth = (int) pagewidth;
        myBitmapHeight = (int) pageheight;
        myBitmap = Bitmap.createBitmap(myBitmapWidth, myBitmapHeight, Bitmap.Config.RGB_565);
        myCanvas = new Canvas(myBitmap);
        myPaint = new Paint();
        myPaint.setColor(-16777216);
        myPaint.setTextSize(36.0F);
        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setStrokeWidth(1.0F);
        myCanvas.drawColor(-1);
        return true;
    }

    public void drawGraphic(int start_x, int start_y, int width, int height, Bitmap bmp) {
        zp_page_create((double) width, (double) height);
        myCanvas.drawBitmap(bmp, (float) start_x, (float) start_y, myPaint);
        this.zp_page_print();
    }

    public String printerStatus() {
        zp_printer_status_detect();
        return null;
    }

    public int GetStatus() {
        int a = zp_printer_status_get(1000);
        return a;
    }

    public void Write(byte[] Data) {
        SPPWrite(Data);
    }

    public static boolean SPPWrite(byte[] Data) {
        try {
            myOutStream.write(Data);
            return true;
        } catch (IOException var2) {
            return false;
        }
    }

    public void Read(byte[] Data, int len, int timeout) {
        SPPReadTimeout(Data, len, timeout);
    }


    private static void zp_printer_status_detect() {
        byte[] data = new byte[]{29, -103, 0, 0};
        SPPWrite(data, 4);
    }

    private static boolean SPPReadTimeout(byte[] Data, int DataLen, int Timeout) {
        for (int i = 0; i < Timeout / 5; ++i) {
            try {
                if (myInStream.available() >= DataLen) {
                    try {
                        myInStream.read(Data, 0, DataLen);
                        return true;
                    } catch (IOException var5) {
                        return false;
                    }
                }

                try {
                    Thread.sleep(5L);
                } catch (InterruptedException var6) {
                    return false;
                }
            } catch (IOException var7) {
                return false;
            }
        }

        return false;
    }

    private boolean SPPOpen(BluetoothAdapter BluetoothAdapter, BluetoothDevice btDevice) {
        Log.e("a", "SPPOpen");
        myBluetoothAdapter = BluetoothAdapter;
        myDevice = btDevice;
        if (!myBluetoothAdapter.isEnabled()) {
            return false;
        } else {
            myBluetoothAdapter.cancelDiscovery();

            try {
                mySocket = myDevice.createRfcommSocketToServiceRecord(this.SPP_UUID);
            } catch (IOException var11) {
                var11.printStackTrace();
                return false;
            }

            try {
                mySocket.connect();
            } catch (IOException var10) {
                return false;
            }

            try {
                myOutStream = mySocket.getOutputStream();
            } catch (IOException var9) {
                try {
                    mySocket.close();
                } catch (IOException var6) {
                    var6.printStackTrace();
                }

                return false;
            }

            try {
                myInStream = mySocket.getInputStream();
            } catch (IOException var8) {
                try {
                    mySocket.close();
                } catch (IOException var5) {
                    var5.printStackTrace();
                }

                return false;
            }

            try {
                Thread.sleep(100L);
            } catch (InterruptedException var7) {
            }

            Log.e("a", "SPPOpen OK");
            return true;
        }
    }

    private void SPPClose() {
        try {
            if (mySocket != null) {
                mySocket.close();
            }
        } catch (IOException var2) {
        }

    }

    private static boolean SPPWrite(byte[] Data, int DataLen) {
        try {
            myOutStream.write(Data, 0, DataLen);
            return true;
        } catch (IOException var3) {
            return false;
        }
    }


}
