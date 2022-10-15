package com.mahak.order;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.FirebaseApp;
import com.mahak.order.common.ExceptionLog;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.SharedPreferencesHelper;
import com.mahak.order.storage.DbAdapter;
import com.yariksoffice.lingver.Lingver;

import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;


public final class AnalyticsTrackers extends MultiDexApplication {

    private static Context mContext;

    private Tracker mTracker;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private DbAdapter db;

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
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

        /*Thread.UncaughtExceptionHandler handler = new ExceptionReporter(getDefaultTracker(), Thread.getDefaultUncaughtExceptionHandler(), this) {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                exceptionLog(e);
                ServiceTools.logToFireBase(e);
                logToDbFile logToDbFile = new logToDbFile(e.getMessage());
                logToDbFile.execute();
                super.uncaughtException(t, e);
            }
        };*/

        Thread.setDefaultUncaughtExceptionHandler(new ManageException(mContext));
    }

    public void exceptionLog(Throwable ex) {
        new StringBuffer();
        try {
            Calendar calendar = Calendar.getInstance();
            String path = Environment.getExternalStorageDirectory().getPath() + "/MAHAK_ORDER_LOG.txt";
            File f = new File(path);
            long seek = f.length();
            RandomAccessFile raf = new RandomAccessFile(new File(path), "rw");
            raf.seek(seek);
            Date date = new Date();
            String rafStr= "\n@@@ date: " + ServiceTools.getDate(date)
                    + " time: " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + "\n"
                    + "@@@ version " + ServiceTools.getVersionCode(mContext);

            raf.write(rafStr.getBytes());
            seek += rafStr.getBytes().length;
            raf.seek(seek);

            raf.write("\n----------------------------------\n".getBytes());
            seek +="\n----------------------------------\n".getBytes().length;
            raf.seek(seek);

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            raf.writeUTF(sw.toString());
            raf.close();

        } catch (Exception e) {
            ServiceTools.logToFireBase(e);
            e.printStackTrace();
        }

    }

    public static Context getContext() {
        return mContext;
    }

    private class logToDbFile extends AsyncTask<String, Integer, Boolean> {

        String exception;

        logToDbFile(String exceptionLog){
            exception = exceptionLog;
        }


        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            logToDb(exception);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }

    private void logToDb(String exc) {
        ExceptionLog exceptionLog = new ExceptionLog();
        exceptionLog.setException(exc);
        db = new DbAdapter(getApplicationContext());
        db.open();
        db.AddException(exceptionLog);
    }
}
