package com.mahak.order.gpsTracking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mahak.order.BaseActivity;
import com.mahak.order.R;
import com.mahak.order.apiHelper.ApiClient;
import com.mahak.order.apiHelper.ApiInterface;
import com.mahak.order.common.GpsPoint;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.User;
import com.mahak.order.common.login.LoginBody;
import com.mahak.order.common.login.LoginResult;
import com.mahak.order.common.request.SetAllDataBody;
import com.mahak.order.common.request.SetAllDataResult.SaveAllDataResult;
import com.mahak.order.storage.DbAdapter;

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
public class GpsTrackingNewApi implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int ID_NOTIFICATION_TRACKING = 1090;
    static final int TYPE_START_TRACKING = 0;
    static final int TYPE_END_TRACKING = 1;
    private static final int ID_NOTIFICATION_START_END = 1078;

    private final Context mContext;
    private  Activity activity;
    private DbAdapter dba;
    private static Map<String, EventLocation> eventLocations = new HashMap<>();

    private static LocationRequest mLocationRequestHighAccuracy;

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private Boolean mRequestingLocationUpdates;

    RealTimeLocation realTimeLocation;
    List<GpsPoint> gpsPoints;
    List<LatLng> latLngpoints;


    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strings, @NonNull int[] ints) {
    }

    public interface EventLocation {
        void onReceivePoint(Location location);
    }

    public GpsTrackingNewApi(Context context) {
        this.mContext = context;
        setup();
    }

    public GpsTrackingNewApi(Context context , Activity activity) {
        this.mContext = context;
        this.activity = activity;
        setup();
    }

    private void setup() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        mSettingsClient = LocationServices.getSettingsClient(mContext);

        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();

        mRequestingLocationUpdates = false;

        realTimeLocation = new RealTimeLocation(mContext,activity);
    }



    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequestHighAccuracy);
        mLocationSettingsRequest = builder.build();
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                updateUI();
            }
        };
    }

    void currentLocation() {
        startLocationUpdates();
    }

    private Location getCorrectLocation(Location location) {

        JSONObject obj = getLastLocation();
        if (obj == null) {
            saveCorrectLocation(location);
            return location;
        }
        Location lasLocation = new Location("");
        lasLocation.setLatitude(obj.optDouble(ProjectInfo._json_key_latitude));
        lasLocation.setLongitude(obj.optDouble(ProjectInfo._json_key_longitude));
        long time = (System.currentTimeMillis() - obj.optLong(ProjectInfo._json_key_date)) / 1000;
        double mDistanse = distance(lasLocation.getLatitude(), lasLocation.getLongitude(), location.getLatitude(), location.getLongitude(), "K") * 1000;
        double speed = mDistanse / time;//m/s
        Log.d("@Location", "d = " + String.format("%.2f", mDistanse) + " - " + new SimpleDateFormat("hh:mm:ss", Locale.US).format(new Date()));
        if (location.getAccuracy() < 16) {//42m/s = 150km/h
            saveCorrectLocation(location);
            return location;
        } else
            saveCorrectLocation(lasLocation);
        return null;
    }

    private JSONObject getLastLocation() {
        String lastLocation = ServiceTools.getKeyFromSharedPreferences(mContext, ProjectInfo.pre_last_location);
        if (ServiceTools.isNull(lastLocation))
            return null;
        try {
            return new JSONObject(lastLocation);
        } catch (JSONException e) {
            return null;
        }
    }

    private void saveCorrectLocation(Location location) {
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

    private void stopTrackingListener() {
        stopLocationUpdates();
    }



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
                        updateDb(gpsPoints);
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
            e.printStackTrace();
        }

        if (mgr != null) {
            mgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 45000, pi);
        }
    }


    //region show Notification
    void showNotification(String message, int type) {
        initChannels(mContext);
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

    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "mahakorder",
                NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);
    }


    public void showNotificationServiceRun() {
        initChannels(mContext);
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


    public void drawGoogleMap2(GoogleMap googleMap, Marker marker, Polyline polyline) {
        if (mCurrentLocation != null) {
            if (marker != null)
                marker.remove();
            LatLng point = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            marker = googleMap.addMarker(new MarkerOptions().position(point));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_visitor_3));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(point.latitude, point.longitude), 15));
        }
        new loadingGpsLocation(googleMap,marker,polyline).execute();
    }
    public void drawGoogleMapDashboard(GoogleMap googleMap, Marker marker) {
        if (mCurrentLocation != null) {
            if (marker != null)
                marker.remove();
            LatLng point = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            marker = googleMap.addMarker(new MarkerOptions().position(point));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_visitor_3));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(point.latitude, point.longitude), 15));
        }
    }

    class loadingGpsLocation extends AsyncTask<String, String, Boolean>{

        GoogleMap googleMap;
        Marker marker;
        Polyline polyline;

        public loadingGpsLocation(GoogleMap googleMap, Marker marker, Polyline polyline){
            this.googleMap = googleMap;
            this.marker = marker;
            this.polyline = polyline;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            if (dba == null) dba = new DbAdapter(mContext);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            long userId = BaseActivity.getPrefUserMasterId(mContext);
            dba.open();
            latLngpoints = dba.getAllLatLngPointsFromDate(calendar.getTimeInMillis(), userId);
            dba.close();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                LatLng point;
                List<LatLng> points = polyline.getPoints();
                if (latLngpoints.size() > 0) {
                    points.addAll(latLngpoints);
                    polyline.setPoints(points);
                    if (marker != null)
                        marker.remove();
                    marker = googleMap.addMarker(new MarkerOptions().position(points.get(0)));
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_visitor_3));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngpoints.get(0), 15));
                }
            }
            super.onPostExecute(result);
        }
    }

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

    //region Google API Location

    private void createLocationRequest() {
        mLocationRequestHighAccuracy = new LocationRequest();
        //مکان با دقت بالا
        mLocationRequestHighAccuracy.setInterval(10000);
        mLocationRequestHighAccuracy.setFastestInterval(5000);
        mLocationRequestHighAccuracy.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //حداقل تغییر مکان بین به روزرسانی های مکان را بر حسب متر
        //mLocationRequest.setSmallestDisplacement(1);

    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                    }
                });
    }

    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //Log.i(TAG, "All location settings are satisfied.");

                        //noinspection MissingPermission
                        if (checkLocationPermission()) return;
                        mFusedLocationClient.requestLocationUpdates(mLocationRequestHighAccuracy,
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
                                    rae.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    //Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                //Log.e(TAG, errorMessage);
                                Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }
                        updateUI();
                    }
                });
    }

    private boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return true;
        }
        return false;
    }

    private void updateUI() {
        if (mCurrentLocation != null) {
            realTimeLocation.sendLocation(mCurrentLocation);
            Location correctLocation = getCorrectLocation(mCurrentLocation);
            if (correctLocation != null) {
                sendLocation(correctLocation);
            }
        }
    }
}
