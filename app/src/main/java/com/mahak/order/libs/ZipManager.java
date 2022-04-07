package com.mahak.order.libs;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.BaseActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * The Class FileManager.
 */
public class ZipManager extends AsyncTask<Void, Integer, Integer> {

    private final String _zipFile;
    private final String _location;
    private int per = 0;
    private final ProgressBar bar;
    private int totalSize;

    public ZipManager(String zipFile, String location, ProgressBar progressBar) {
        _zipFile = zipFile;
        _location = location;
        bar = progressBar;
        _dirChecker("");
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try {
            ZipFile zip = new ZipFile(_zipFile);
            totalSize = zip.size();
//			if (bar != null)
//				bar.setMax(totalSize);
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                Log.v("Decompress", "Unzipping " + ze.getName());
                if (ze.getName().contains("/")) {
                    String[] dirs = ze.getName().split("/");
                    String root = "";
                    for (int i = 0; i < dirs.length - 1; i++) {
                        _dirChecker(root + "/" + dirs[i]);
                        root = root + "/" + dirs[i];
                    }
                }
                if (ze.isDirectory()) {
                    _dirChecker(ze.getName());
                } else {
                    // Here I am doing the update of my progress bar

                    per++;
                    publishProgress(per);

                    FileOutputStream fout = new FileOutputStream(getOutputLocation() + "/" + ze.getName());
                    BufferedOutputStream bufout = new BufferedOutputStream(fout);
                    byte[] buffer = new byte[1024];
                    int read = 0;
                    while ((read = zin.read(buffer)) != -1) {
                        bufout.write(buffer, 0, read);
                    }

                    zin.closeEntry();
                    bufout.close();
                    fout.close();
                }
            }
            zin.close();
            onPostExecute();
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Decompress", "unzip", e);
            onError(e);
        }
        return totalSize;
    }

    protected void onError(Exception e) {
        FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
        FirebaseCrashlytics.getInstance().recordException(e);
    }

    protected void onProgressUpdate(Integer... progress) {
        if (bar != null)
            bar.setProgress(per);
    }

    protected void onPostExecute(Integer... result) {
        Log.i("Completed. Total size: ", result + "");
    }

    private void _dirChecker(String dir) {
        File f = new File(getOutputLocation() + dir);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }

    public String getOutputLocation() {
        return _location;
    }
}
