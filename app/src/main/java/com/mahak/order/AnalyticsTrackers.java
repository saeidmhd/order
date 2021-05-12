package com.mahak.order;


import android.app.Application;
import android.content.Context;
import android.os.Environment;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.common.SharedPreferencesHelper;
import com.yariksoffice.lingver.Lingver;

import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;


public final class AnalyticsTrackers extends MultiDexApplication {

    private static Context mContext;

    private Tracker mTracker;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.app_tracker);
        }
        return mTracker;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Lingver.init(this, SharedPreferencesHelper.getCurrentLanguage(this));
        mContext = getApplicationContext();

        FirebaseApp.initializeApp(mContext);

        Thread.UncaughtExceptionHandler handler = new ExceptionReporter(getDefaultTracker(), Thread.getDefaultUncaughtExceptionHandler(), this) {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                exceptionLog(e);
                FirebaseCrashlytics.getInstance().setUserId(BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                super.uncaughtException(t, e);
            }
        };
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }

    public void exceptionLog(Throwable ex) {
        // String version = String.format(getString(R.string.))
        new StringBuffer();
        try {
            String path = Environment.getExternalStorageDirectory().getPath() + "/MAHAK_ORDER_LOG.txt";
            File f = new File(path);
            long seek = f.length();
            RandomAccessFile raf = new RandomAccessFile(new File(path), "rw");
            raf.seek(seek);
            raf.write("\n----------------------------------\n".getBytes());
            seek += (long) "\n----------------------------------\n".getBytes().length;
            raf.seek(seek);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            raf.writeUTF(sw.toString());
            raf.close();
        } catch (Exception var10) {
            FirebaseCrashlytics.getInstance().recordException(var10);
            var10.printStackTrace();
        }

    }

    public static Context getContext() {
        return mContext;
    }

}
