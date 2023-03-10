package com.mahak.order.Woosim;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.BaseActivity;
import com.mahak.order.common.ServiceTools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;


public class BluetoothPrintService {
    // Debugging
    private static final String TAG = "BluetoothPrintService";
    private static final boolean D = true;

    // Unique UUID for this application
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Member fields
    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private int mState;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    private static final int STATE_LISTEN = 1;     // now listening for incoming connections
    private static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    static final int STATE_CONNECTED = 3;  // now connected to a remote device
    static final int STATE_failed = 5;  // now connected to a remote device


    /**
     * Constructor. Prepares a new Bluetooth session.
     *
     * @param handler A Handler to send messages back to the UI Activity
     */
    public BluetoothPrintService(Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mHandler = handler;
    }

    /**
     * Set the current state of the connection
     *
     * @param state An integer defining the current connection state
     */
    private synchronized void setState(int state) {
        if (D) Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;
    }

    /**
     * Return the current connection state.
     */
    public synchronized int getState() {
        return mState;
    }

    /**
     * Start the print service. Called by onResume() of the Activity
     */
    public synchronized void start() {
        if (D) Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        setState(STATE_LISTEN);
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     * @param secure Socket Security type - Secure(true) or Insecure(false)
     */
    public synchronized void connect(BluetoothDevice device, boolean secure) {
        if (D) Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device, secure);
        mConnectThread.start();

        Message msg = mHandler.obtainMessage(STATE_CONNECTING);
        mHandler.sendMessage(msg);

        setState(STATE_CONNECTING);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket     The BluetoothSocket on which the connection was made
     * @param device     The BluetoothDevice that has been connected
     * @param socketType "Secure" or "Insecure"
     */
    private synchronized void connected(BluetoothSocket socket, BluetoothDevice device, final String socketType) {
        if (D) Log.d(TAG, "connected, Socket Type:" + socketType);

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket, socketType);
        mConnectedThread.start();

        // Send the name of the connected device back to the UI Activity
        //  Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        //bundle.putString(MainActivity.DEVICE_NAME, device.getName());
        //msg.setData(bundle);
        // mHandler.sendMessage(msg);

        Message msg = mHandler.obtainMessage(STATE_CONNECTED);
        mHandler.sendMessage(msg);

        setState(STATE_CONNECTED);
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        if (D) Log.d(TAG, "stop");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        Message msg = mHandler.obtainMessage(STATE_NONE);
        mHandler.sendMessage(msg);

        setState(STATE_NONE);
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;

        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) {
                if (D) Log.w(TAG, "BT session is not connected state");

              /*  Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.TOAST, R.string.not_connected);
                msg.setData(bundle);
                mHandler.sendMessage(msg);*/

                return;
            }
            r = mConnectedThread;
        }

        // Perform the write unsynchronized
        r.write(out);
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        // Send a failure message back to the Activity
        /*Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.TOAST, R.string.connect_fail);
        msg.setData(bundle);
        mHandler.sendMessage(msg);*/

        // Start the service over to restart listening mode

        Message msg = mHandler.obtainMessage(STATE_failed);
        mHandler.sendMessage(msg);
        BluetoothPrintService.this.start();
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        // When the application is destroyed, just return
        if (mState == STATE_NONE) return;

        // Send a failure message back to the Activity
      /*  Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.TOAST, R.string.connect_lost);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
*/
        // Start the service over to restart listening mode
        BluetoothPrintService.this.start();
    }

    /**
     * This thread runs while attempting to make an outgoing connection with a device.
     * It runs straight through the connection either succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mSocketType;

        ConnectThread(BluetoothDevice device, boolean secure) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";

            // Get a BluetoothSocket for a connection with the given BluetoothDevice
            try {
                if (secure)
                    tmp = device.createRfcommSocketToServiceRecord(SPP_UUID);
                else
                    tmp = device.createInsecureRfcommSocketToServiceRecord(SPP_UUID);

            } catch (IOException e) {
                ServiceTools.logToFireBase(e);
                Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e);
            }

            mmSocket = tmp;
        }

        public void run() {
            if (D) Log.i(TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
            setName("ConnectThread" + mSocketType);

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                ServiceTools.logToFireBase(e);
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    ServiceTools.logToFireBase(e2);
                    Log.e(TAG, "unable to close() " + mSocketType + " socket during connection failure", e2);
                }

                connectionFailed();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothPrintService.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice, mSocketType);
        }

        void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                ServiceTools.logToFireBase(e);
                Log.e(TAG, "unable to close() " + mSocketType + " socket", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        ConnectedThread(BluetoothSocket socket, String socketType) {
            if (D) Log.d(TAG, "create ConnectedThread: " + socketType);

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                ServiceTools.logToFireBase(e);
                Log.e(TAG, "temp sockets are not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            if (D) Log.d(TAG, "BEGIN mConnectedThread");

            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // buffer can be over-written by next input stream data, so it should be copied
                    byte[] rcvData = Arrays.copyOf(buffer, bytes);
                    // Send the obtained bytes to the UI Activity
                    // mHandler.obtainMessage(MainActivity.MESSAGE_READ, bytes, -1, rcvData).sendToTarget();
                } catch (IOException e) {
                    ServiceTools.logToFireBase(e);
                    if (D) Log.w(TAG, "disconnected");
                    connectionLost();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                ServiceTools.logToFireBase(e);
                Log.e(TAG, "Exception during write", e);
            }
        }

        void cancel() {
            try {
                mmInStream.close();
                mmOutStream.close();
                mmSocket.close();
            } catch (IOException e) {
                ServiceTools.logToFireBase(e);
                Log.e(TAG, "close() of socket failed", e);
            }
        }
    }
}
