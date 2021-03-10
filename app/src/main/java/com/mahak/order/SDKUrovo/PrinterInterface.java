package com.mahak.order.SDKUrovo;

import android.graphics.Bitmap;

public interface PrinterInterface {
    boolean connect(String var1);

    void disconnect();

    void drawGraphic(int var1, int var2, int var3, int var4, Bitmap var5);

    String printerStatus();

    int GetStatus();

    void Write(byte[] var1);

    void Read(byte[] var1, int var2, int var3);
}
