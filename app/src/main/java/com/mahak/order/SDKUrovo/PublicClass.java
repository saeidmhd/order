package com.mahak.order.SDKUrovo;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.BaseActivity;
import com.mahak.order.R;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;

public class PublicClass {


    public static String PrinterId;
    public static String PrinterName;
    public static boolean Inch3;
    public static ProgressDialog waitDialog;
    private BluetoothPrinter zpSDK;
    Context context;

    public void SetPublicClass(Context context) {
        setZpSDK(new BluetoothPrinter());
        this.context = context;

        Hawk.init(context).build();

        String valueName = Hawk.get("PrinterName");
        if (valueName != null) {
            PrinterName = valueName;
        } else {
            PrinterName = "";
        }

        String valueId = Hawk.get("PrinterId");
        if (valueId != null) {
            PrinterId = valueId;
        } else {
            PrinterId = "";
        }

        Boolean valueinch = Hawk.get("Inch3");
        if (valueinch != null) {
            Inch3 = valueinch;
        } else {
            Inch3 = true;
        }
    }

    public void waitDialogShow() {
        waitDialog.show();
    }

    public void waitDialogHide() {
        waitDialog.hide();
    }

    public void SetWaitDialog(Context context) {
        waitDialog = ProgressDialog.show(context, "", context.getString(R.string.waiting), true);
    }

    public String PrintBitmapCPCL(Bitmap bmp, int widthGap, int heightGap) throws IOException {

        if (bmp == null) {
            return context.getString(R.string.urovo_null_bitmap);
        }

        if (getZpSDK() == null) {
            return context.getString(R.string.urovo_isnt_connect);
        }

        if (!getZpSDK().connect(PrinterId)) {
            return context.getString(R.string.urovo_cant_connect);
        }
        getZpSDK().drawGraphic(0, 1, bmp.getWidth(), bmp.getHeight() + 40, bmp);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
//         zpSDK.disconnect();

        return context.getString(R.string.urovo_cant_connect);
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


    public void PrintDisconnect() {

        getZpSDK().disconnect();

    }

    public BluetoothPrinter getZpSDK() {
        return zpSDK;
    }

    public void setZpSDK(BluetoothPrinter zpSDK) {
        this.zpSDK = zpSDK;
    }
}
