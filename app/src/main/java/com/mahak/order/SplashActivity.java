package com.mahak.order;


import static com.mahak.order.common.ServiceTools.writeLog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.SharedPreferencesHelper;
import com.mahak.order.libs.ZipManager;
import com.mahak.order.service.ReadOfflinePicturesProducts;
import com.mahak.order.webService.RequestSender;
import com.mahak.order.webService.RunInternetService;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

public class SplashActivity extends BaseActivity {

    int delayMillis = 500;
    private final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ServiceTools.scheduleAlarm(this);

        getSupportActionBar().hide();
        if (checkPlayServices()) {
            registerInBackground();
        }

       // GpsTracking.setAlarmManager(getApplicationContext());
        ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_waiter, "0");
        initImageLoader(getApplicationContext());

        try {
            String fPath = ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_IMAGES + "/" + ProjectInfo.DIRECTORY_PRODUCTS;
            String FilePath = Environment.getExternalStorageDirectory().toString();
            File Directory = new File(FilePath, fPath);
            if (!Directory.exists())
                Directory.mkdirs();
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }

        if (getIntent() != null)
            readZipFile(getIntent());

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = null;
                if (SharedPreferencesHelper.getCurrentLanguage(SplashActivity.this).equals("null")) {
                    intent = new Intent(SplashActivity.this, SelectLanguage.class);
                    startActivityForResult(intent, 1000);
                } else if (ServiceTools.checkUserID(getBaseContext())) {
                    intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    intent.putExtra(Type_Login, bnd_Login_Splash);
                    startActivity(intent);
                    finish();
                } else {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    RefreshPreferenceUser();
                    intent = new Intent(SplashActivity.this, LoginActivityRestApi.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, delayMillis);
        //getConfigGpsAndBanks();

        //   throw new RuntimeException("Test Crash");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1000) {
                Intent intent = null;
                if (ServiceTools.checkUserID(getBaseContext())) {
                    intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    intent.putExtra(Type_Login, bnd_Login_Splash);
                } else {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    BaseActivity.RefreshPreferenceUser();
                    intent = new Intent(SplashActivity.this, LoginActivityRestApi.class);
                }
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        } else {
            finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // check for gcm
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("GCM", "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    public static void initImageLoader(Context context) {

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
    }

    //region read offline pictures products
    private void readZipFile(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            try {

                final String path = uri.getPath();
                File file = new File(path);

                if (file.getName().contains(".zip")) {

                    String location = ServiceTools.getKeyFromSharedPreferences(SplashActivity.this, getPrefUserMasterId(SplashActivity.this) + "");
                    if (location.equals("")) {
                        Toast.makeText(getApplicationContext(), getString(R.string.str_msg_error_dont_choose_folder_picture), Toast.LENGTH_LONG).show();
                        return;
                    }
                    ZipManager zipManager = new ZipManager(file.getPath(), location, null) {
                        @Override
                        protected void onPostExecute(Integer... result) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new ReadOfflinePicturesProducts(getApplicationContext()).readAllImages();
                                    Toast.makeText(getApplicationContext(), getString(R.string.str_read_file_ok), Toast.LENGTH_LONG).show();
                                }
                            });

                        }

                        @Override
                        protected void onError(Exception e) {

                            Toast.makeText(getApplicationContext(), getString(R.string.str_error_in_read_pictures), Toast.LENGTH_LONG).show();
                        }
                    };
                    zipManager.execute();

                }
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
        }
    }

    private void registerInBackground() {
        FirebaseApp.initializeApp(SplashActivity.this);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                String token = task.getResult();
                writeLog(token);
                Intent intent = new Intent(SplashActivity.this, RegistrationIntentService.class);
                intent.putExtra("token",token);
                startService(intent);
            }
        });
    }

    //endregion

    // Setup a recurring alarm every half hour

}
