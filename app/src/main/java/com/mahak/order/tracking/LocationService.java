package com.mahak.order.tracking;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mahak.order.BaseActivity;
import com.mahak.order.DashboardActivity;
import com.mahak.order.R;
import com.mahak.order.apiHelper.ApiClient;
import com.mahak.order.apiHelper.ApiInterface;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.User;
import com.mahak.order.common.VisitorLocation;
import com.mahak.order.common.login.LoginBody;
import com.mahak.order.common.login.LoginResult;
import com.mahak.order.common.loginSignalr.SignalLoginBody;
import com.mahak.order.common.loginSignalr.SignalLoginResult;
import com.mahak.order.common.request.SetAllDataBody;
import com.mahak.order.common.request.SetAllDataResult.SaveAllDataResult;
import com.mahak.order.storage.DbAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mahak.order.BaseActivity.setPrefSignalUserToken;

public class LocationService extends Service {

    private static final int ID_NOTIFICATION_TRACKING = 1090;
    static final int TYPE_START_TRACKING = 0;
    static final int TYPE_END_TRACKING = 1;
    private static final int ID_NOTIFICATION_START_END = 1078;

    private static long MIN_DISPALCEMENT_CHANGE_FOR_UPDATES = 1; // 1 meters
    // The minimum time between updates in milliseconds
    private static long MIN_TIME_INTERVAL_UPDATES = 120000; // 2 min
    private static int radius = 1; // 2 min
    public Context mContext;

    boolean isLogging = false;

    private Activity activity;
    private DbAdapter dba;
    private static Map<String, EventLocation> eventLocations = new HashMap<>();

    private LocationRequest locationRequest;

    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private Location mCurrentLocation;
    private static final int REQUEST_Location_ON = 1200;

    RealTimeLocation realTimeLocation;

    private static final String PACKAGE_NAME =
            "com.mahak.order.gpsTracking";

    private static final String TAG = LocationService.class.getSimpleName();

    /**
     * The name of the channel for notifications.
     */
    private static final String CHANNEL_ID = "channel_01";

    static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";

    static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +
            ".started_from_notification";

    private final IBinder mBinder = new LocalBinder();

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * The identifier for the notification displayed for the foreground service.
     */
    private static final int NOTIFICATION_ID = 12345678;

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private boolean mChangingConfiguration = false;

    private static NotificationManager mNotificationManager;

    /**
     * Contains parameters used by {@link com.google.android.gms.location.FusedLocationProviderApi}.
     */
    private LocationRequest mLocationRequest;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Callback for changes in location.
     */
    private static LocationCallback mLocationCallback;

    private Handler mServiceHandler;

    /**
     * The current location.
     */
    private Location mLocation;


    @Override
    public void onCreate() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service started");
        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,
                false);
        if (startedFromNotification) {
            removeLocationUpdates();
            stopSelf();
        }

        getLastLocation();

        return START_NOT_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "in onBind()");
        stopForeground(true);
        mChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "in onRebind()");
        stopForeground(true);
        mChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Last client unbound from service");
        if (!mChangingConfiguration && Utils.requestingLocationUpdates(this)) {
            Log.i(TAG, "Starting foreground service");
            startForeground(NOTIFICATION_ID, getNotification());
        }
        return true;
    }

    @Override
    public void onDestroy() {
        mServiceHandler.removeCallbacksAndMessages(null);
    }

    public LocationService(){}

    public LocationService(Context context , Activity activity) {
        this.mContext = context;
        this.activity = activity;
        String config = ServiceTools.getKeyFromSharedPreferences(mContext, ProjectInfo.pre_gps_config);
        if (!ServiceTools.isNull(config)) {
            try {
                JSONObject obj = new JSONObject(config);
                MIN_DISPALCEMENT_CHANGE_FOR_UPDATES = obj.getLong(ProjectInfo._json_key_mingps_distance_change);
                MIN_TIME_INTERVAL_UPDATES = obj.getLong(ProjectInfo._json_key_mingps_time_change);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface EventLocation {
        void onReceivePoint(Location location, boolean saveInDb);
    }

    private Location getCorrectLocation(Location location) {
        if(location.getAccuracy() < 20){
            if(checkDistanceSpeed(location))
                saveInJsonFile(location);
            return location;
        }
        return null;
    }

    private boolean checkDistanceSpeed(Location location) {
        JSONObject obj = getLastLocationJson(mContext);
        if (obj == null) {
            saveInJsonFile(location);
            return false;
        }
        Location lasLocation = new Location("");
        lasLocation.setLatitude(obj.optDouble(ProjectInfo._json_key_latitude));
        lasLocation.setLongitude(obj.optDouble(ProjectInfo._json_key_longitude));
        long time = (System.currentTimeMillis() - obj.optLong(ProjectInfo._json_key_date)) / 1000;
        double mDistanse = distance(lasLocation.getLatitude(), lasLocation.getLongitude(), location.getLatitude(), location.getLongitude(), "K") * 1000;
        double speed = mDistanse / time;//m/s
        return mDistanse > 1 && speed < 42;//42m/s = 150km/h
    }

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

    public JSONObject getLastLocationJson(Context context) {
        String lastLocation = ServiceTools.getKeyFromSharedPreferences(context, ProjectInfo.pre_last_location);
        if (ServiceTools.isNull(lastLocation))
            return null;
        try {
            return new JSONObject(lastLocation);
        } catch (JSONException e) {
            return null;
        }
    }

    private void saveInJsonFile(Location location) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(ProjectInfo._json_key_latitude, location.getLatitude());
            obj.put(ProjectInfo._json_key_longitude, location.getLongitude());
            obj.put(ProjectInfo._json_key_date, System.currentTimeMillis());
            ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_last_location, obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void startTracking() {
        long masterUserId = BaseActivity.getPrefUserMasterId(mContext);
        ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_is_tracking + masterUserId, "1");
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        Utils.setRequestingLocationUpdates(mContext, true);
        mContext.startService(new Intent(mContext , LocationService.class));

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        mSettingsClient = LocationServices.getSettingsClient(mContext);

        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(locationRequest,
                                mLocationCallback, Looper.myLooper());
                        updateUI();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " + "location settings ");
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    if(activity != null)
                                        rae.startResolutionForResult(activity, REQUEST_Location_ON);
                                } catch (IntentSender.SendIntentException sie) {
                                    //Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                //Log.e(TAG, errorMessage);
                                Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
                        }
                        updateUI();
                    }
                });
    }

    public void stopTracking() {
        long masterUserId = BaseActivity.getPrefUserMasterId(mContext);
        ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_is_tracking + masterUserId, "0");
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        removeLocationUpdates();
    }

    public boolean isRunService() {
        long masterUserId = BaseActivity.getPrefUserMasterId(mContext);
        String sharedPreferences = ServiceTools.getKeyFromSharedPreferences(mContext, ProjectInfo.pre_is_tracking + masterUserId);
        return !ServiceTools.isNull(sharedPreferences) && sharedPreferences.equals("1");
    }

    private void sendLocation(Location correctLocation) {
        boolean saveInDb = false;
        long userId = BaseActivity.getPrefUserMasterId(mContext);
        VisitorLocation visitorLocation = new VisitorLocation();
        visitorLocation.setCreateDate(System.currentTimeMillis());
        visitorLocation.setDate(System.currentTimeMillis());
        visitorLocation.setLatitude(correctLocation.getLatitude());
        visitorLocation.setLongitude(correctLocation.getLongitude());
        visitorLocation.setVisitorId(userId);
        visitorLocation.setUniqueID(UUID.randomUUID().toString());
        List<VisitorLocation> VisitorLocations = new ArrayList<>();
        VisitorLocations.add(visitorLocation);
        if(saveInDb(VisitorLocations)){
            saveInDb = true;
            if(ServiceTools.isOnline(mContext)){
                sendToServer(VisitorLocations);
            }
        }
        executeEventLocations(correctLocation,saveInDb);
    }

    private void sendToServer(final List<VisitorLocation> visitorLocations) {

        if (dba == null) dba = new DbAdapter(mContext);
        dba.open();

        SetAllDataBody setAllDataBody;
        final ApiInterface apiService;
        Call<SaveAllDataResult> saveAllDataResultCall;
        User user = dba.getUser();
        setAllDataBody = new SetAllDataBody();
        setAllDataBody.setUserToken(BaseActivity.getPrefUserToken());

        apiService = ApiClient.orderRetrofitClient().create(ApiInterface.class);
        setAllDataBody.setVisitorLocations(visitorLocations);
        saveAllDataResultCall = apiService.SaveAllData(setAllDataBody);

        saveAllDataResultCall.enqueue(new Callback<SaveAllDataResult>() {
            @Override
            public void onResponse(Call<SaveAllDataResult> call, Response<SaveAllDataResult> response) {
                if (response.body() != null) {
                    if (response.body().isResult()) {
                        for (int i = 0; i < visitorLocations.size(); i++) {
                            visitorLocations.get(i).setVisitorLocationId(response.body().getData().getObjects().getVisitorLocations().getResults().get(i).getEntityID());
                            visitorLocations.get(i).setRowVersion(response.body().getData().getObjects().getVisitorLocations().getResults().get(i).getRowVersion());
                            updateDb(visitorLocations.get(i));
                        }

                    } else if (response.body().getCode() == 401) {
                        getUserTokenAndSendAgain(visitorLocations);
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

    private void getUserTokenAndSendAgain(List<VisitorLocation> VisitorLocations) {
        DbAdapter db = new DbAdapter(mContext);
        db.open();
        User user = db.getUser();
        LoginBody loginBody = new LoginBody();
        loginBody.setUserName(user.getUsername());
        loginBody.setPassword(user.getPassword());
        ApiInterface apiService = ApiClient.orderRetrofitClient().create(ApiInterface.class);
        Call<LoginResult> call = apiService.Login(loginBody);
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.body() != null) {
                    if (response.body().isResult()) {
                        BaseActivity.setPrefUserToken(response.body().getData().getUserToken());
                        sendToServer(VisitorLocations);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean saveInDb(List<VisitorLocation> VisitorLocations) {
        boolean result = false;
        if (dba == null) dba = new DbAdapter(mContext);
        dba.open();
        result = dba.AddGpsTracking(VisitorLocations);
        dba.close();
        return result;
    }

    private void updateDb(VisitorLocation visitorLocation) {
        if (dba == null) dba = new DbAdapter(mContext);
        dba.open();
        dba.updateGpsTrackingForSending(visitorLocation);
        dba.close();
    }

    void sendOldPoints() {
        if (dba == null) dba = new DbAdapter(mContext);
        try {
            waiter();
            dba.open();
            List<VisitorLocation> VisitorLocations = dba.getAllGpsPointsWithLimit(0, 200);
            dba.close();
            if (VisitorLocations != null && VisitorLocations.size() > 0) {
                sendToServer(VisitorLocations);
            } else {
                ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_waiter, "0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void waiter() {
        String wait = ServiceTools.getKeyFromSharedPreferences(mContext, ProjectInfo.pre_waiter);
        while (wait.equals("1")) {
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_waiter, "1");
    }

    public void stopNotificationServiceTracking() {
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null) {
            mNotificationManager.cancel(ID_NOTIFICATION_TRACKING);
        }
    }

    //region interface Event location
    public static void addEventLocation(String key, EventLocation eventLocation) {
        eventLocations.put(key, eventLocation);
    }

    public static void removeEventLocation(String key) {
        eventLocations.remove(key);
    }

    void executeEventLocations(Location location, boolean saveInDb) {
        if (eventLocations == null)
            eventLocations = new HashMap<>();
        for (Map.Entry<String, EventLocation> entry : eventLocations.entrySet()) {
            EventLocation eventLocation = entry.getValue();
            eventLocation.onReceivePoint(location,saveInDb);
        }
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
                mCurrentLocation = locationResult.getLastLocation();
                updateUI();
            }
        };
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void updateUI() {
        if (mCurrentLocation != null) {
            executeEventLocations(mCurrentLocation,false);
            performSignalOperation();
            Location correctLocation = getCorrectLocation(mCurrentLocation);
            if (correctLocation != null) {
                sendLocation(correctLocation);
            }
        }
    }

    private void performSignalOperation() {
        if(ServiceTools.isOnline(mContext)){
            if(isLogging)
                return;
            if(BaseActivity.getPrefSignalUserToken().isEmpty()){
                getSignalToken(mContext);
                realTimeLocation = null;
                return;
            }
            if(realTimeLocation == null){
                realTimeLocation = new RealTimeLocation(mContext,activity);
            }
            realTimeLocation.sendRealTimeLocation(mCurrentLocation);
        }
    }

    /**
     * Makes a request for location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates");
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        try {
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                    mLocationCallback, Looper.myLooper());
                            updateUI();
                        } catch (SecurityException unlikely) {
                            Utils.setRequestingLocationUpdates(mContext, false);
                            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " + "location settings ");
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    if(activity != null)
                                        rae.startResolutionForResult(activity, REQUEST_Location_ON);
                                } catch (IntentSender.SendIntentException sie) {
                                    //Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                //Log.e(TAG, errorMessage);
                                Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
                        }
                        updateUI();
                    }
                });
    }

    /**
     * Removes location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void removeLocationUpdates() {
        Log.i(TAG, "Removing location updates");
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            Utils.setRequestingLocationUpdates(mContext, false);
            stopSelf();
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(mContext, true);
            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }
    }

    /**
     * Returns the {@link NotificationCompat} used as part of the foreground service.
     */
    private Notification getNotification() {
        Intent intent = new Intent(mContext, LocationService.class);
        CharSequence text = Utils.getLocationText(mLocation);
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);
        PendingIntent activityPendingIntent = PendingIntent.getActivity(mContext, 0,
                new Intent(mContext, DashboardActivity.class), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .addAction(R.drawable.ic_launcher, mContext.getString(R.string.app_name),
                        activityPendingIntent)
                .setContentText(text)
                .setContentTitle(Utils.getLocationTitle(mContext))
                .setOngoing(true)
                .setSilent(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(text)
                .setWhen(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        return builder.build();
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

    private void getLastLocation() {
        if(mFusedLocationClient!=null){
            try {
                mFusedLocationClient.getLastLocation()
                        .addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    mLocation = task.getResult();
                                } else {
                                    Log.w(TAG, "Failed to get location.");
                                }
                            }
                        });
            } catch (SecurityException unlikely) {
                Log.e(TAG, "Lost location permission." + unlikely);
            }
        }
    }

    private void onNewLocation(Location location) {
        Log.i(TAG, "New location: " + location);
        mLocation = location;
        Intent intent = new Intent(ACTION_BROADCAST);
        intent.putExtra(EXTRA_LOCATION, location);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        if (serviceIsRunningInForeground(mContext)) {
            mNotificationManager.notify(NOTIFICATION_ID, getNotification());
        }
    }

    /**
     * Sets the location request parameters.
     */
    private void createLocationRequest() {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationRequest.setFastestInterval(5000);
        locationRequest.setInterval(MIN_TIME_INTERVAL_UPDATES);
        locationRequest.setSmallestDisplacement(MIN_DISPALCEMENT_CHANGE_FOR_UPDATES);

    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public LocationService getService(Context context, Activity mActivity) {
             mContext = context;
             activity = mActivity;
            return LocationService.this;
        }
    }

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The {@link Context}.
     */
    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }

    public void getSignalToken(Context context) {
        isLogging = true;
        DbAdapter mDb = new DbAdapter(context);
        mDb.open();
        User user = mDb.getUser();
        mDb.close();
        SignalLoginBody loginBody = new SignalLoginBody();
        loginBody.setAppId("MahakOrder");
        loginBody.setDatabaseId(0);
        loginBody.setLanguage("en-US");
        loginBody.setDeviceId("");
        loginBody.setDescription("login");
        loginBody.setUserName(user.getUsername());
        loginBody.setPassword(user.getPassword());

        ApiInterface apiService = ApiClient.trackingRetrofitClient().create(ApiInterface.class);
        Call<SignalLoginResult> call = apiService.LoginSignalR(loginBody);
        call.enqueue(new Callback<SignalLoginResult>() {
            @Override
            public void onResponse(Call<SignalLoginResult> call, Response<SignalLoginResult> response) {
                if (response.body() != null) {
                    if (response.body().isResult()) {
                        setPrefSignalUserToken(response.body().getUserToken());
                        isLogging = false;
                    }else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        isLogging = false;
                    }
                }
            }
            @Override
            public void onFailure(Call<SignalLoginResult> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                isLogging = false;
            }
        });
    }
}

