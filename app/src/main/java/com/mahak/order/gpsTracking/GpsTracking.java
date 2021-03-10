package com.mahak.order.gpsTracking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.BaseActivity;
import com.mahak.order.R;
import com.mahak.order.apiHelper.ApiClient;
import com.mahak.order.apiHelper.ApiInterface;
import com.mahak.order.common.GpsPoint;
import com.mahak.order.common.GpsPointSignalR;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.User;
import com.mahak.order.common.login.LoginBody;
import com.mahak.order.common.login.LoginResult;
import com.mahak.order.common.request.SetAllDataBody;
import com.mahak.order.common.request.SetAllDataResult.SaveAllDataResult;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.webService.RequestSender;
import com.mahak.order.webService.RunInternetService;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mostafavi on 7/12/2016.
 * Edited to new Version Compatibility by saeid mohammadi
 */
public class GpsTracking implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int ID_NOTIFICATION_TRACKING = 1090;
    static final int TYPE_START_TRACKING = 0;
    static final int TYPE_END_TRACKING = 1;
    private static final int ID_NOTIFICATION_START_END = 1078;
    private final Context mContext;
    private DbAdapter dba;
    private static LocationManager locationManager;
    private static LocationListener trackingListener;
    private static Map<String, EventLocation> eventLocations = new HashMap<>();

    // The minimum distance to change Updates in meters
    private static long MIN_DISTANCE_CHANGE_FOR_UPDATES = 15; // 15 meters
    // The minimum time between updates in milliseconds
    private static long MIN_TIME_BW_UPDATES = 120000; // 2 min
    private static GoogleApiClient mGoogleApiClient;
    private static LocationRequest mLocationRequest;
    private static com.google.android.gms.location.LocationListener clocationListener;
    private String provider;

    HubConnection hubConnection;
    ArrayAdapter<String> arrayAdapter;
    HubConnectionTask hubConnectionTask;

    public static final String CHANNEL_ID = "ForegroundServiceChannel";


    private FusedLocationProviderClient fusedLocationClient;
    private boolean signalrConnected = false;

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strings, @NonNull int[] ints) {
    }

    public interface EventLocation {
        void onReceivePoint(Location location);
    }

    public GpsTracking(Context mContext) {
        this.mContext = mContext;
        String config = ServiceTools.getKeyFromSharedPreferences(mContext, ProjectInfo.pre_gps_config);
        if (!ServiceTools.isNull(config)) {
            try {
                JSONObject obj = new JSONObject(config);
                MIN_DISTANCE_CHANGE_FOR_UPDATES = obj.getLong(ProjectInfo._json_key_mingps_distance_change);
                MIN_TIME_BW_UPDATES = obj.getLong(ProjectInfo._json_key_mingps_time_change) * 60000;
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
        }
    }

    @TargetApi(23)
    public void setupSignalR(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Network network = cm.getActiveNetwork();
            NetworkCapabilities capabilities = cm
                    .getNetworkCapabilities(network);
            NetworkRequest request = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    .build();
            cm.registerNetworkCallback(request, new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    signalrConnectionSetup();
                }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    hubConnectionTask.cancel(true);
                    Toast.makeText(context, "ارتباط با سرور قطع شد!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            registerNetworkStateReciever();
        }
    }

    private void registerNetworkStateReciever() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(networkStateReciever, filter);

    }

    private BroadcastReceiver networkStateReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (wifi.isConnected()) {
                    signalrConnectionSetup();
                } else {
                    Toast.makeText(context, "ارسال نقاط به صورت آنلاین امکان پذیر نیست", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };

    private void signalrConnectionSetup() {
        hubConnection = HubConnectionBuilder.create("http://192.168.0.19/chatHub").build();
        // hubConnection = HubConnectionBuilder.create("http://tracking.mahaksoft.com/trackinghub?usertoken = " + BaseActivity.getPrefUserToken()).build();
        hubConnectionTask = new HubConnectionTask();
        hubConnectionTask.execute(hubConnection);
        getMessage();
    }

    private void getMessage() {
        hubConnection.on("ReceiveMessage", (user, message) -> {
            arrayAdapter.add(user + " : " + message);
            arrayAdapter.notifyDataSetChanged();
        }, String.class, String.class);
    }

    class HubConnectionTask extends AsyncTask<HubConnection, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(HubConnection... hubConnections) {
            HubConnection hubConnection = hubConnections[0];
            try {
                hubConnection.start().blockingAwait();
                signalrConnected = true;
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                signalrConnected = false;
                e.printStackTrace();
            }
            return null;
        }
    }


    //region calculate current location
    void currentLocation() {
        googleApiLocationStart();
    }

    private Location getCorrectLocation(Location location) {
        JSONObject obj = getLastLocation();
        if (obj == null) {
            saveLastLocation(location);
            return location;
        }
        Location lasLocation = new Location("");
        lasLocation.setLatitude(obj.optDouble(ProjectInfo._json_key_latitude));
        lasLocation.setLongitude(obj.optDouble(ProjectInfo._json_key_longitude));
        long time = (System.currentTimeMillis() - obj.optLong(ProjectInfo._json_key_date)) / 1000;
        double mDistanse = distance(lasLocation.getLatitude(), lasLocation.getLongitude(), location.getLatitude(), location.getLongitude(), "K") * 1000;
        double speed = mDistanse / time;//m/s
        Log.d("@Location", "d = " + String.format("%.2f", mDistanse) + " - " + new SimpleDateFormat("hh:mm:ss", Locale.US).format(new Date()));
        if (mDistanse > MIN_DISTANCE_CHANGE_FOR_UPDATES && speed < 42) {//42m/s = 150km/h
            saveLastLocation(location);
            return location;
        } else
            saveLastLocation(lasLocation);
        return null;

        /*saveLastLocation(location);
        return location;*/
    }

    private JSONObject getLastLocation() {
        String lastLocation = ServiceTools.getKeyFromSharedPreferences(mContext, ProjectInfo.pre_last_location);
        if (ServiceTools.isNull(lastLocation))
            return null;
        try {
            return new JSONObject(lastLocation);
        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            return null;
        }
    }

    private void saveLastLocation(Location location) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(ProjectInfo._json_key_latitude, location.getLatitude());
            obj.put(ProjectInfo._json_key_longitude, location.getLongitude());
            obj.put(ProjectInfo._json_key_date, System.currentTimeMillis());
            ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_last_location, obj.toString());
        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }
//endregion

    //region start & stop & check Service
    public void startTracking() {
        long masterUserId = BaseActivity.getPrefUserMasterId(mContext);
        ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_is_tracking + masterUserId, "1");
        currentLocation();
    }

    public void stopTracking() {
        long masterUserId = BaseActivity.getPrefUserMasterId(mContext);
        ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_is_tracking + masterUserId, "0");
        stopTrackingListener();
    }

    public boolean isRunService() {
        long masterUserId = BaseActivity.getPrefUserMasterId(mContext);
        String sharedPreferences = ServiceTools.getKeyFromSharedPreferences(mContext, ProjectInfo.pre_is_tracking + masterUserId);
        return !ServiceTools.isNull(sharedPreferences) && sharedPreferences.equals("1");
    }

    boolean isPauseService() {
        String sharedPreferences = ServiceTools.getKeyFromSharedPreferences(mContext, ProjectInfo.pre_is_tracking_pause);
        return !ServiceTools.isNull(sharedPreferences) && sharedPreferences.equals("1");
    }

    //endregion
    public Location getLocation() {
        Location location = null;
        try {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            // Getting GPS status
            boolean isGPSEnabled = false;
            if (locationManager != null) {
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }

            // Getting network status
            boolean isNetworkEnabled = false;
            if (locationManager != null) {
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }

            if (!isGPSEnabled && !isNetworkEnabled) {
                // No network provider is enabled
                turnGPSOn();
                locationManager = null;
            } else {

                // If GPS enabled, get latitude/longitude using GPS Services
                trackingListener = getTrackingListener();
                HandlerThread t = new HandlerThread("my handlerthread");
                t.start();
                if (isGPSEnabled) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, trackingListener, t.getLooper());
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }
                if (isNetworkEnabled && location == null) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, trackingListener, t.getLooper());
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }

            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
            locationManager = null;
        }

        return location;
    }

    @NonNull
    private LocationListener getTrackingListener() {
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location.getLatitude() == 0 || location.getLongitude() == 0) {
                    return;
                }
                sendLocation(location);
                saveLastLocation(location);

                if (signalrConnected)
                    sendLocationSignalR(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("ss", "ss");
            }

            @Override
            public void onProviderEnabled(String provider) {
            }


            @Override
            public void onProviderDisabled(String provider) {
            }
        };
    }

    private void stopTrackingListener() {
        if (locationManager != null && trackingListener != null) {
            locationManager.removeUpdates(trackingListener);
            locationManager = null;
        }
        stopLocationUpdates();//TODO @Change
    }

    private void turnGPSOn() {
        Intent gpsOptionsIntent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        gpsOptionsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(gpsOptionsIntent);
    }
//endregion

    //region send data to server
    private void sendLocation(Location correctLocation) {
        long userId = BaseActivity.getPrefUserMasterId(mContext);
        GpsPoint gpsPoint = new GpsPoint();
        gpsPoint.setDate(System.currentTimeMillis());
        gpsPoint.setLatitude(String.valueOf(correctLocation.getLatitude()));
        gpsPoint.setLongitude(String.valueOf(correctLocation.getLongitude()));
        gpsPoint.setSend(false);
        gpsPoint.setVisitorId(userId);
        List<GpsPoint> gpsPoints = new ArrayList<>();
        gpsPoints.add(gpsPoint);
        saveInDb(gpsPoints);
        sendToServer(gpsPoints, true);
        executeEventLocations(correctLocation);
    }

    private void sendLocationSignalR(Location location) {
        GpsPointSignalR gpsPointSignalR = new GpsPointSignalR();
        gpsPointSignalR.setLatitude(location.getLatitude());
        gpsPointSignalR.setLongitude(location.getLongitude());
        try {
            //hubConnection.send("SendNewLocation", gpsPointSignalR);
            hubConnection.send("SendMessage", String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }


    private void sendToServer(final List<GpsPoint> gpsPoints, final boolean addToDb) {

        if (dba == null) dba = new DbAdapter(mContext);
        dba.open();

        SetAllDataBody setAllDataBody;
        final ApiInterface apiService;
        Call<SaveAllDataResult> saveAllDataResultCall;
        User user = dba.getUser();
        setAllDataBody = new SetAllDataBody();
        setAllDataBody.setUserToken(BaseActivity.getPrefUserToken());

        apiService = ApiClient.getClient().create(ApiInterface.class);
        setAllDataBody.setGpsPoints(gpsPoints);
        saveAllDataResultCall = apiService.SaveAllData(setAllDataBody);

        saveAllDataResultCall.enqueue(new Callback<SaveAllDataResult>() {
            @Override
            public void onResponse(Call<SaveAllDataResult> call, Response<SaveAllDataResult> response) {
                if (response.body() != null) {
                    if (response.body().isResult()) {
                        if (addToDb)
                            saveInDb(gpsPoints);

                        updateDb(gpsPoints);
                        if (!addToDb)
                            ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_waiter, "0");
                    } else if (response.body().getCode() == 401) {
                        getUserTokenAndSendAgain(gpsPoints, addToDb);
                    }
                } else
                    Toast.makeText(mContext, R.string.send_error, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<SaveAllDataResult> call, Throwable t) {
                Toast.makeText(mContext, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserTokenAndSendAgain(List<GpsPoint> gpsPoints, boolean addToDb) {
        DbAdapter db = new DbAdapter(mContext);
        db.open();
        User user = db.getUser();
        LoginBody loginBody = new LoginBody();
        loginBody.setUserName(user.getUsername());
        loginBody.setPassword(user.getPassword());
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResult> call = apiService.Login(loginBody);
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.body() != null) {
                    if (response.body().isResult()) {
                        BaseActivity.setPrefUserToken(response.body().getData().getUserToken());
                        sendToServer(gpsPoints, addToDb);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sendPointToServer(final List<GpsPoint> gpsPoints, final boolean addToDb) {
        String url = ProjectInfo.SOAP_ADDRESS;
        String methodName = "SetLocationVisitor";
        HashMap<String, Object> params = new HashMap<>();
        final JSONObject obj = new JSONObject();
        try {
            if (gpsPoints.size() == 0)
                return;
            obj.put(ProjectInfo._json_key_user_id, gpsPoints.get(0).getVisitorId());
            JSONArray array = new JSONArray();
            for (int i = 0; i < gpsPoints.size(); i++) {
                array.put(i, gpsPoints.get(i).toJson());
            }
            obj.put(ProjectInfo._json_key_points, array);
            obj.put("MahakId", BaseActivity.getPrefMahakId(mContext));
            obj.put("DatabaseId", BaseActivity.getPrefDatabaseId(mContext));
            params.put("Data", obj.toString());
        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        params.put("AppSign", ProjectInfo.APPSIGN);
        RequestSender.ResponseReceiver receiver = new RequestSender.ResponseReceiver() {
            @Override
            public void gotResponse(Object sender, StringBuffer result, StringBuffer cookies) {
                if (addToDb)
                    saveInDb(gpsPoints);
                if (result != null && !result.toString().equals("null") && !result.toString().equals("invalid")) {
                    try {
                        JSONObject object = new JSONObject(result.toString());
                        if (object.optBoolean("result")) {
                            updateDb(gpsPoints);
                        }
                    } catch (JSONException e) {
                        FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                        FirebaseCrashlytics.getInstance().recordException(e);
                        e.printStackTrace();
                    }
                }
                if (!addToDb)
                    ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_waiter, "0");

            }
        };
        new RunInternetService().Do(mContext, "", url, methodName, params, receiver, null, 14000);
    }
//endregion

    //region db method
    private void saveInDb(List<GpsPoint> gpsPoints) {
        if (dba == null) dba = new DbAdapter(mContext);
        dba.open();
        for (int i = 0; i < gpsPoints.size(); i++) {
            dba.AddGpsTracking(gpsPoints.get(i));
        }
        dba.close();
    }

    private void updateDb(List<GpsPoint> gpsPoints) {
        if (dba == null) dba = new DbAdapter(mContext);
        dba.open();
        for (int i = 0; i < gpsPoints.size(); i++) {
            dba.updateGpsTrackingForSending(gpsPoints.get(i).getDate());
        }
        dba.close();
    }

    private void removePointFromDb() {
        if (dba == null) dba = new DbAdapter(mContext);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        dba.open();
        dba.DeleteGpsTrackingToDateSending(calendar.getTimeInMillis() - 1);
        dba.close();
    }

    void sendOldPoints() {
        if (dba == null) dba = new DbAdapter(mContext);

        try {
            waiter();
            dba.open();
            List<GpsPoint> gpsPoints = dba.getAllGpsPointsWithLimit(0, 200);
            dba.close();
            if (gpsPoints != null && gpsPoints.size() > 0) {
                sendToServer(gpsPoints, false);
            } else {
                removePointFromDb();
                ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_waiter, "0");
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }
//endregion

    private void waiter() {
        String wait = ServiceTools.getKeyFromSharedPreferences(mContext, ProjectInfo.pre_waiter);
        while (wait.equals("1")) {
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
        }
        ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_waiter, "1");
    }

    //region set alarm manager
    @SuppressLint("ShortAlarm")
    public static void setAlarmManager(Context context) {
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, OnAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

        try {
            if (mgr != null) {
                mgr.cancel(pi);
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }

        if (mgr != null) {
            mgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 45000, pi);
        }
    }
//endregion

    //region show Notification
    void showNotification(String message, int type) {
        createNotificationChannel(mContext);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, "default")
                .setContentText(message);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(R.drawable.ic_launcher_noti);
            mBuilder.setColor(mContext.getResources().getColor(R.color.notification_color));
        } else {
            mBuilder.setSmallIcon(R.drawable.ic_launcher);
        }
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent intent = new Intent();
        intent.setAction(ProjectInfo._notification_action_yes);
        intent.putExtra(ProjectInfo._TAG_TYPE, type);
        intent.setPackage(mContext.getPackageName());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 12345, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.addAction(R.drawable.ic_accept, mContext.getString(R.string.str_yes), pendingIntent);

        intent = new Intent();
        intent.setAction(ProjectInfo._notification_action_no);
        intent.putExtra(ProjectInfo._TAG_TYPE, type);
        intent.setPackage(mContext.getPackageName());
        pendingIntent = PendingIntent.getBroadcast(mContext, 12345, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.addAction(R.drawable.ic_cancel, mContext.getString(R.string.str_no), pendingIntent);


        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        if (mNotificationManager != null) {
            mNotificationManager.notify(ID_NOTIFICATION_START_END, mBuilder.build());
        }
    }

    public void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Channel description");

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }


    public Notification showNotificationServiceRun() {

        createNotificationChannel(mContext);
        String string = mContext.getString(R.string.str_pause);
        String header = mContext.getString(R.string.str_msg_notification_active_tracking);
        if (isPauseService()) {
            string = mContext.getString(R.string.str_play);
            header = mContext.getString(R.string.str_tracking_pause_header);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, "default")
                .setContentText(header);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(R.drawable.ic_launcher_noti);
            mBuilder.setColor(mContext.getResources().getColor(R.color.notification_color));
        } else {
            mBuilder.setSmallIcon(R.drawable.ic_launcher);
        }

        mBuilder.setAutoCancel(false);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);

        if (!(BaseActivity.getPrefAdminControl(mContext) == 1 && BaseActivity.getPrefTrackingControl(mContext) == 1)) {

            Intent intent = new Intent();
            intent.setAction(ProjectInfo._notification_action_stop);
            intent.setPackage(mContext.getPackageName());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 12345, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.addAction(0, mContext.getString(R.string.str_stop), pendingIntent);

            intent = new Intent();
            intent.setAction(ProjectInfo._notification_action_pause);
            intent.setPackage(mContext.getPackageName());
            pendingIntent = PendingIntent.getBroadcast(mContext, 12345, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.addAction(0, string, pendingIntent);

        }

        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

// mId allows you to update the notification later on.
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        if (mNotificationManager != null) {
            mNotificationManager.notify(ID_NOTIFICATION_TRACKING, notification);
        }

        return notification;

    }

    public void stopNotificationServiceTracking() {
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null) {
            mNotificationManager.cancel(ID_NOTIFICATION_TRACKING);
        }
    }

    void stopNotificationTracking() {
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null) {
            mNotificationManager.cancel(ID_NOTIFICATION_START_END);
        }
    }

    //endregion

    //region interface Event location
    public static void addEventLocation(String key, EventLocation eventLocation) {
        eventLocations.put(key, eventLocation);
    }

    public static void removeEventLocation(String key) {
        eventLocations.remove(key);
    }

    void executeEventLocations(Location location) {
        if (eventLocations == null)
            eventLocations = new HashMap<>();
        for (Map.Entry<String, EventLocation> entry : eventLocations.entrySet()) {
            EventLocation eventLocation = entry.getValue();
            eventLocation.onReceivePoint(location);

        }
    }
//endregion

    //region draw marker and route in map
    public Marker drawGoogleMap(GoogleMap googleMap, Marker marker, Polyline polyline) {
        if (dba == null) dba = new DbAdapter(mContext);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        long userId = BaseActivity.getPrefUserMasterId(mContext);
        dba.open();
        List<GpsPoint> gpsPoints = dba.getAllGpsPointsFromDate(calendar.getTimeInMillis(), userId);
        dba.close();

        if (gpsPoints.size() > 0) {
            List<LatLng> points = polyline.getPoints();
            for (int i = 0; i < gpsPoints.size(); i++) {
                GpsPoint gpsPoint = gpsPoints.get(i);
                LatLng point = new LatLng(ServiceTools.RegulartoDouble(gpsPoint.getLatitude()), ServiceTools.RegulartoDouble(gpsPoint.getLongitude()));
                points.add(point);
            }
            polyline.setPoints(points);
            LatLng point = new LatLng(ServiceTools.RegulartoDouble(gpsPoints.get(gpsPoints.size() - 1).getLatitude()), ServiceTools.RegulartoDouble(gpsPoints.get(gpsPoints.size() - 1).getLongitude()));
            if (marker != null)
                marker.remove();
            marker = googleMap.addMarker(new MarkerOptions().position(point));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_visitor_3));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(point.latitude, point.longitude), 15));
            return marker;
        }
        return null;
    }
//endregion

    //region distance
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit.equals("K")) {
            dist = dist * 1.609344;
        } else if (unit.equals("N")) {
            dist = dist * 0.8684;
        }

        if (String.valueOf(dist).equals("NaN"))
            return 0;

        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
//endregion

    //region Google API Location

    private void googleApiLocationStart() {
        if (clocationListener == null) {
            clocationListener = new com.google.android.gms.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (signalrConnected)
                        sendLocationSignalR(location);
                    Location correctLocation = getCorrectLocation(location);
                    if (correctLocation == null || correctLocation.getLatitude() == 0 || correctLocation.getLongitude() == 0)
                        return;
                    sendLocation(correctLocation);

                }
            };
        }
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            try {
                                startLocationUpdates();
                            } catch (Exception e) {
                                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                                FirebaseCrashlytics.getInstance().recordException(e);
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                        }
                    })
                    .addApi(LocationServices.API)
                    .build();
        } else
            getCorrectLocation();

        if (mLocationRequest == null) {
            createLocationRequest();
        }
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }

    private void stopLocationUpdates() {
        if (mGoogleApiClient != null && clocationListener != null && mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, clocationListener);
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private void getCorrectLocation() {
        List<Location> locations = new ArrayList<>();
        int maxIndex = 0, max = 0;
        List<Integer> maxList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            boolean isAdd = false;
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation == null) {
                continue;
            }
            if (locations.isEmpty()) {
                locations.add(mLastLocation);
                maxList.add(1);
                max = 1;
            } else {
                for (int i1 = 0; i1 < locations.size(); i1++) {
                    Location location = locations.get(i1);
                    if (Math.abs(location.getLatitude() - mLastLocation.getLatitude()) <= 0.01 && Math.abs(location.getLatitude() - mLastLocation.getLatitude()) <= 0.01) {
                        maxList.set(i1, maxList.get(i1) + 1);
                        isAdd = true;
                        if (maxList.get(i1) > max) {
                            max = maxList.get(i1);
                            maxIndex = i1;
                        }
                    }
                }
                if (!isAdd) {
                    locations.add(mLastLocation);
                    maxList.add(1);
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
        }
        if (!locations.isEmpty()) {
            Location location = locations.get(maxIndex);
            Location correctLocation = getCorrectLocation(location);
            if (correctLocation == null || correctLocation.getLatitude() == 0 || correctLocation.getLongitude() == 0)
                return;
            sendLocation(correctLocation);
            if (signalrConnected)
                sendLocationSignalR(location);
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, clocationListener);
    }
    //endregion
}
