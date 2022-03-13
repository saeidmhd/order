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
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

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
import com.mahak.order.common.StopLocation.StopLocationResponse;
import com.mahak.order.common.StopLocation.StopLog;
import com.mahak.order.common.User;
import com.mahak.order.common.VisitorLocation;
import com.mahak.order.common.login.LoginBody;
import com.mahak.order.common.login.LoginResult;
import com.mahak.order.common.loginSignalr.SignalLoginBody;
import com.mahak.order.common.loginSignalr.SignalLoginResult;
import com.mahak.order.common.request.SetAllDataBody;
import com.mahak.order.common.request.SetAllDataResult.SaveAllDataResult;
import com.mahak.order.log.GPSReceiver;
import com.mahak.order.log.NetworkReceiver;
import com.mahak.order.storage.DbAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import io.reactivex.annotations.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mahak.order.BaseActivity.getPrefUserId;
import static com.mahak.order.BaseActivity.isRadaraActive;
import static com.mahak.order.BaseActivity.setPrefSignalUserToken;

public class LocationService extends Service  {

    private static final int ID_NOTIFICATION_TRACKING = 1090;

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

    public static RealTimeLocation realTimeLocation;

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

    long tracking_client_id = 0;


    private TimerHelper timerHelper;

    long stop_time;
    Location lastStopLocation;
    NetworkReceiver networkReceiver;
    GPSReceiver gpsReceiver;


    @Override
    public void onCreate() {

        Log.i(TAG, "in onCreate()");

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

        networkReceiver = new NetworkReceiver();
        gpsReceiver = new GPSReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service onStartCommand");
        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,
                false);
        if (startedFromNotification) {
            removeLocationUpdates();
            stopSelf();
        }

        getLastLocation();

        IntentFilter filter = new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        this.registerReceiver(networkReceiver, filter);



        return START_NOT_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(TAG, "Service onConfigurationChanged");
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
        Log.i(TAG, "onUnbind");
        if (!mChangingConfiguration && Utils.requestingLocationUpdates(this)) {
            Log.i(TAG, "Starting foreground service");
            startForeground(NOTIFICATION_ID, getNotification());
        }
        return true;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        mServiceHandler.removeCallbacksAndMessages(null);
    }

    public LocationService(){}

    public LocationService(Context context , Activity activity) {
        this.mContext = context;
        this.activity = activity;
    }

    public interface EventLocation {
        void onReceivePoint(Location location, boolean saveInDb);
    }

    public void startTracking() {
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        startLocationUpdates();
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
                mCurrentLocation = locationResult.getLastLocation();
                executeEventLocations(mCurrentLocation,false);
                updateUI();
            }
        };
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(10 * 1000)
                .setSmallestDisplacement(100);
    }
    private boolean compareWithLastLocation(Location currentLocation) {

        boolean result;
        double mDistance;

        Calendar calLastLocation = Calendar.getInstance();
        Calendar calNow = Calendar.getInstance();

        JSONObject obj = getLastLocationJson(mContext);
        if (obj == null) {
            performSignalOperation();
            saveStopLocationJsonFile(currentLocation);
            saveInJsonFile(currentLocation);
            return true;
        }

        if(lastStopLocation != null){
            calLastLocation.setTimeInMillis(lastStopLocation.getTime());
            boolean check = calLastLocation.get(Calendar.DAY_OF_YEAR) == calNow.get(Calendar.DAY_OF_YEAR);
            if(!check){
                saveStopLocationJsonFile(currentLocation);
            }
        }

        Location lasLocation = new Location("");
        lasLocation.setLatitude(obj.optDouble(ProjectInfo._json_key_latitude));
        lasLocation.setLongitude(obj.optDouble(ProjectInfo._json_key_longitude));
        lasLocation.setTime(obj.optLong(ProjectInfo._json_key_date));
        mDistance = distance(lasLocation.getLatitude(), lasLocation.getLongitude(), currentLocation.getLatitude(), currentLocation.getLongitude(), "K") * 1000;
        boolean hasSpeed = (int)currentLocation.getSpeed() > 0;
        result = mDistance <= 350 && mDistance > 10 && hasSpeed;
        if(result){
            saveAndSendStopLocation();
            ServiceTools.writeLogRadara( "\n" + mDistance + "\n" + currentLocation.getSpeed() + "\n" + currentLocation.getAccuracy() + "\n" + currentLocation.getLatitude() + "\n" + currentLocation.getLongitude());
            saveStopLocationJsonFile(currentLocation);
        }
        saveInJsonFile(currentLocation);
        return result;
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        mLocationSettingsRequest = builder.build();
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
                        mFusedLocationClient.requestLocationUpdates(locationRequest,mLocationCallback, Looper.myLooper());
                        timerHelper =  new TimerHelper();

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
        removeLocationUpdates();
    }

    private void saveAndSendStopLocation() {
        long currentTime = System.currentTimeMillis();
        lastStopLocation = LastStopLocation();
        if(checkStartTimeEndTime(mContext , lastStopLocation)){
            lastStopLocation = LastStopLocation();
            stop_time = currentTime - lastStopLocation.getTime();
            if(stop_time > 5 * 60 * 1000){
                ArrayList<StopLog> stopLogs = new ArrayList<>();
                StopLog stopLog = new StopLog();
                tracking_client_id = ServiceTools.toLong(ServiceTools.getStopLocationId(lastStopLocation.getTime()));
                stopLog.setDuration(stop_time / 1000);
                stopLog.setStopLocationClientId(tracking_client_id);
                stopLog.setEndDate(ServiceTools.getFormattedDate(currentTime));
                stopLog.setEntryDate(ServiceTools.getFormattedDate(lastStopLocation.getTime()));
                stopLog.setLat(lastStopLocation.getLatitude());
                stopLog.setLng(lastStopLocation.getLongitude());
                stopLog.setVisitorId(getPrefUserId());
                stopLogs.add(stopLog);
                if(ServiceTools.isOnline(mContext)){
                    sendStopLocationToServer(stopLogs);
                }else {
                    stopLogs.get(0).setSent(-1);
                    updateStopLogToDb(stopLogs);
                }
            }
        }
    }
    public void sendStopLocationToServer(ArrayList<StopLog> stopLog) {
        ApiInterface apiService = ApiClient.trackingRetrofitClient().create(ApiInterface.class);
        Call<StopLocationResponse> call = apiService.SetStopLocation(stopLog);
        call.enqueue(new Callback<StopLocationResponse>() {
            @Override
            public void onResponse(Call<StopLocationResponse> call, Response<StopLocationResponse> response) {
                if (response.body() != null) {
                    if (!response.body().isSucceeded()) {
                        stopLog.get(0).setSent(-1);
                        updateStopLogToDb(stopLog);
                    }
                }
            }
            @Override
            public void onFailure(Call<StopLocationResponse> call, Throwable t) {
                stopLog.get(0).setSent(-1);
                updateStopLogToDb(stopLog);
            }
        });
    }

    private void InsertStopLogToDb(ArrayList<StopLog> stopLogs) {
        if (dba == null) dba = new DbAdapter(mContext);
        dba.open();
        try {
            dba.InsertStopLogs(stopLogs);
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getMessage() != null)
                Log.e("saveInDb",e.getMessage());
        }
        dba.close();
    }

    private void updateStopLogToDb(ArrayList<StopLog> stopLogs) {
        if (dba == null) dba = new DbAdapter(mContext);
        dba.open();
        try {
            dba.updateStopLogs(stopLogs);
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getMessage() != null)
                Log.e("saveInDb",e.getMessage());
        }
        dba.close();
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
    public JSONObject getLastStopLocationJson(Context context) {
        String lastLocation = ServiceTools.getKeyFromSharedPreferences(context, ProjectInfo.pre_last_stop_location);
        if (ServiceTools.isNull(lastLocation))
            return null;
        try {
            return new JSONObject(lastLocation);
        } catch (JSONException e) {
            return null;
        }
    }

    private Location LastLocation(){
        Location lastLocation = new Location("");
        JSONObject obj = getLastLocationJson(mContext);
        if (obj != null) {
            lastLocation.setLatitude(obj.optDouble(ProjectInfo._json_key_latitude));
            lastLocation.setLongitude(obj.optDouble(ProjectInfo._json_key_longitude));
            lastLocation.setTime(obj.optLong(ProjectInfo._json_key_date));
        }
        return lastLocation;
    }
    private Location LastStopLocation(){
        Location lastLocation = new Location("");
        JSONObject obj = getLastStopLocationJson(mContext);
        if (obj != null) {
            lastLocation.setLatitude(obj.optDouble(ProjectInfo._json_key_stop_latitude));
            lastLocation.setLongitude(obj.optDouble(ProjectInfo._json_key_stop_longitude));
            lastLocation.setTime(obj.optLong(ProjectInfo._json_key_stop_date));
        }
        return lastLocation;
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
    private void saveStopLocationJsonFile(Location location) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(ProjectInfo._json_key_stop_latitude, location.getLatitude());
            obj.put(ProjectInfo._json_key_stop_longitude, location.getLongitude());
            obj.put(ProjectInfo._json_key_stop_date, System.currentTimeMillis());
            ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_last_stop_location, obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunService(Context mContext) {
        long masterUserId = BaseActivity.getPrefUserMasterId(mContext);
        String sharedPreferences = ServiceTools.getKeyFromSharedPreferences(mContext, ProjectInfo.pre_is_tracking + masterUserId);
        return !ServiceTools.isNull(sharedPreferences) && sharedPreferences.equals("1");
    }

    public void setTrackingPrefOff(String s) {
        long masterUserId = BaseActivity.getPrefUserMasterId(mContext);
        ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_is_tracking + masterUserId, s);
    }

    private void sendLocation(Location correctLocation) {
        boolean saveInDb = false;
        long userId = BaseActivity.getPrefUserMasterId(mContext);
        VisitorLocation visitorLocation = new VisitorLocation();
        visitorLocation.setCreateDate(ServiceTools.formattedDate(System.currentTimeMillis()));
        visitorLocation.setDate(System.currentTimeMillis());
        visitorLocation.setLatitude(correctLocation.getLatitude());
        visitorLocation.setLongitude(correctLocation.getLongitude());
        visitorLocation.setVisitorId(userId);
        visitorLocation.setUniqueID(UUID.randomUUID().toString());
        List<VisitorLocation> VisitorLocations = new ArrayList<>();
        VisitorLocations.add(visitorLocation);
        if(ServiceTools.isOnline(mContext)){
            sendToServer(VisitorLocations);
        }
        if(saveInDb(VisitorLocations)){
            ServiceTools.writeLogRadara("\n" + visitorLocation.getUniqueID() +  "saved in database");
            saveInDb = true;
        }
        executeEventLocations(correctLocation,saveInDb);
    }

    private void sendToServer(final List<VisitorLocation> visitorLocations) {
        SetAllDataBody setAllDataBody;
        final ApiInterface apiService;
        Call<SaveAllDataResult> saveAllDataResultCall;
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
        if (dba == null) dba = new DbAdapter(mContext);
        dba.open();
        User user = dba.getUser();
        dba.close();
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
        try {
            result = dba.AddGpsTracking(VisitorLocations);
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getMessage() != null)
                Log.e("saveInDb",e.getMessage());
        }
        dba.close();
        return result;
    }

    private void updateDb(VisitorLocation visitorLocation) {
        if (dba == null) dba = new DbAdapter(mContext);
        dba.open();
        try {
            dba.updateGpsTrackingForSending(visitorLocation);
        } catch (Exception e) {
            Log.e("saveInDb",e.getMessage());
        }
        dba.close();
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

    private void updateUI() {
        if (mCurrentLocation != null) {
            if(isRadaraActive()){
                if((int)mCurrentLocation.getSpeed() > 0)
                    performSignalOperation();
                if(compareWithLastLocation(mCurrentLocation))
                    sendLocation(mCurrentLocation);
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
     * Removes location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void removeLocationUpdates() {
        ServiceTools.writeLogRadara("Removing location updates");
        Log.i(TAG, "Removing location updates");
        timerHelper.stopTimer();
        try {
            if(mLocationCallback != null){
                mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                Utils.setRequestingLocationUpdates(mContext, false);
                stopSelf();
                if(realTimeLocation != null)
                    realTimeLocation.stopRealTimeSend();
            }
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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext,CHANNEL_ID)
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
        return builder.build();
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
            //mNotificationManager.notify(NOTIFICATION_ID, getNotification());
        }
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
                        performSignalOperation();
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


    public boolean checkStartTimeEndTime(Context context , Location lastStopLocation) {
        int StartTime = 8;
        int EndTime = 22;
        String config = ServiceTools.getKeyFromSharedPreferences(context, ProjectInfo.pre_gps_config);
        if (!ServiceTools.isNull(config)) {
            try {
                JSONObject obj = new JSONObject(config);
                StartTime = obj.getInt(ProjectInfo._json_key_startTime);
                EndTime = obj.getInt(ProjectInfo._json_key_endTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Calendar calLastLocation = Calendar.getInstance();
        calLastLocation.setTimeInMillis(lastStopLocation.getTime());
        boolean betweenStartEndTime = calLastLocation.get(Calendar.HOUR_OF_DAY) >= StartTime && calLastLocation.get(Calendar.HOUR_OF_DAY) <= EndTime;
        if(!betweenStartEndTime){
            timerHelper.stopTimer();
            return false;
        }
        return true;
    }

    public class TimerHelper {
        Timer timer;
        long InitialInMillis = 10 * 1000;
        long DelayInMillis = 5 * 60 * 1000;

        public TimerHelper() {
            timer = new Timer();
            timer.schedule(new MyTimerTask(), InitialInMillis, DelayInMillis);
        }

        public void stopTimer() {
            if(timer != null){
                timer.cancel();
            }
        }

        class MyTimerTask extends TimerTask {
            @Override
            public void run() {
                saveAndSendStopLocation();
            }
        }
    }
}

