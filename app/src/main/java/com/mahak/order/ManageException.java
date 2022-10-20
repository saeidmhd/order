package com.mahak.order;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.mahak.order.common.ExceptionLog;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;

import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;

public class ManageException implements Thread.UncaughtExceptionHandler{

    private DbAdapter db;
    Context mContext;
    ManageException(Context context){
        mContext = context;
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable e) {
        exceptionLog(e);
       // ServiceTools.logToFireBase(e);
        logToDb("e.getMessage()");
        System.exit(0);
    }


    private class LogToDbFile extends AsyncTask<String, Integer, Boolean> {

        String exception;

        LogToDbFile(String exceptionLog){
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
            System.exit(0);
            super.onPostExecute(result);
        }
    }

    private void logToDb(String exc) {
        ExceptionLog exceptionLog = new ExceptionLog();
        exceptionLog.setException(exc);
        db = new DbAdapter(mContext);
        db.open();
        db.AddException(exceptionLog);
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

}
