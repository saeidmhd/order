package com.mahak.order.common;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class Printer {


    public static Bitmap CreateBitmap(View layout) {

        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth(), layout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        layout.draw(canvas);
        return bitmap;
    }

    public static Boolean CreateFile(Bitmap bitmap, String name, String path) {
        boolean success = false;
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            // bitmap = Bitmap.createScaledBitmap(bitmap, 525, 477, false);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            String FilePath = Environment.getExternalStorageDirectory().toString();
            File Directory = new File(FilePath, path);
            if (!Directory.exists())
                Directory.mkdirs();
            File file = new File(Directory, name);
            //if(!file.exists())
            //{
            file.createNewFile();
            FileOutputStream fileout = new FileOutputStream(file);
            fileout.write(bytes.toByteArray());
            //}
            success = true;
        } catch (Exception e) {
            ServiceTools.logToFireBase(e);
            Log.e("Error Create File", e.toString());
            success = false;
        }
        return success;
    }

    public static void ShareImage(Activity context, String name) {
        Intent IntentShare = new Intent(Intent.ACTION_SEND);
        IntentShare.setType("image/jpg");
        IntentShare.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/" + ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_IMAGES + "/" + ProjectInfo.DIRECTORY_INVOICES + "/" + name));
        context.startActivity(Intent.createChooser(IntentShare, "Share Image"));
    }

    public static Boolean deleteFile(String name, String path) {
        boolean success = false;
        try {
            String FilePath = Environment.getExternalStorageDirectory().toString();
            File Directory = new File(FilePath, path);
            if (!Directory.exists())
                Directory.mkdirs();
            File file = new File(Directory, name);
            file.delete();
            success = true;
        } catch (Exception e) {
            ServiceTools.logToFireBase(e);
            Log.e("Error Create File", e.toString());
            success = false;
        }
        return success;
    }

}
