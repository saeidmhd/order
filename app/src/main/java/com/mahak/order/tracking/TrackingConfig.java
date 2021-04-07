package com.mahak.order.tracking;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.BaseActivity;
import com.mahak.order.R;
import com.mahak.order.apiHelper.ApiClient;
import com.mahak.order.apiHelper.ApiInterface;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.User;
import com.mahak.order.common.loginSignalr.SignalLoginBody;
import com.mahak.order.common.loginSignalr.SignalLoginResult;
import com.mahak.order.service.DataService;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.tracking.setting.SettingBody;
import com.mahak.order.tracking.setting.TrackingSetting;
import com.mahak.order.tracking.visitorZone.Datum;
import com.mahak.order.tracking.visitorZone.VisitorZoneLocation;
import com.mahak.order.tracking.visitorZone.ZoneBody;
import com.mahak.order.widget.FontProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mahak.order.BaseActivity.getPrefSignalUserToken;
import static com.mahak.order.BaseActivity.getPrefUserMasterId;
import static com.mahak.order.BaseActivity.setPrefSignalUserToken;

public class TrackingConfig {
    private final MapPolygon mapPolygon;
    Context mContext;
    private FontProgressDialog pd;
    GoogleMap mGoogleMap;
    private DbAdapter db;

    public TrackingConfig(Context context , GoogleMap googleMap){
        mContext = context;
        mGoogleMap = googleMap;
        mapPolygon = new MapPolygon(mGoogleMap,mContext);
        db = new DbAdapter(mContext);

    }

    public void getSignalTokenAndSetting() {
        DbAdapter mDb = new DbAdapter(mContext);
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
                        getSetting(mContext);
                    }else {
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<SignalLoginResult> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getSetting(Context context) {
        ApiInterface apiService = ApiClient.trackingRetrofitClient().create(ApiInterface.class);
        SettingBody settingBody = new SettingBody();
        settingBody.setVisitorId(0);
        Call<TrackingSetting> call = apiService.GetTrackingSetting(getPrefSignalUserToken(),settingBody);
        pd = new FontProgressDialog(mContext);
        pd.setMessage("در حال دریافت تنظیمات ردیابی ویزیتور");
        pd.setCancelable(false);
        pd.show();
        call.enqueue(new Callback<TrackingSetting>() {
            @Override
            public void onResponse(Call<TrackingSetting> call, Response<TrackingSetting> response) {
                pd.dismiss();
                if (response.body() != null) {
                    if (response.body().isSucceeded()) {
                        int visitorId = 0;
                        boolean isRestricted = false;

                        JSONObject gpsData = new JSONObject();
                        long MIN_DISTANCE_CHANGE_FOR_UPDATES = ServiceTools.toLong(response.body().getData().getSendPointsPerMeter());
                        long MIN_TIME_BW_UPDATES = ServiceTools.toLong(response.body().getData().getSendPointsEveryMinute());
                        int radius = ServiceTools.toInt(response.body().getData().getRadius());
                        boolean sendingPointsByAdmin = response.body().getData().isControlSendingPointsByAdmin();
                        boolean sendingPoints = response.body().getData().isSendingPoints();

                        BaseActivity.setPrefAdminControl(sendingPointsByAdmin);
                        if(sendingPoints)
                            BaseActivity.setPrefTrackingControl(1);

                        if(response.body().getData().getGeofencingSetting() != null)
                            visitorId = response.body().getData().getGeofencingSetting().get(0).getVisitorId();
                        if(visitorId == getPrefUserMasterId())
                            isRestricted = true;
                        try {
                            gpsData.put(ProjectInfo._json_key_mingps_distance_change, MIN_DISTANCE_CHANGE_FOR_UPDATES);
                            gpsData.put(ProjectInfo._json_key_mingps_time_change, MIN_TIME_BW_UPDATES);
                            gpsData.put(ProjectInfo._json_key_sendingPointsByAdmin, sendingPointsByAdmin);
                            gpsData.put(ProjectInfo._json_key_sendingPoints, sendingPoints);
                            gpsData.put(ProjectInfo._json_key_isRestricted, isRestricted);
                            gpsData.put(ProjectInfo._json_key_radius, radius);
                            ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_gps_config, gpsData.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //btnTrackingService.setEnabled(!sendingPointsByAdmin);
                        Toast.makeText(mContext, "تنظیمات دریافت گردید", Toast.LENGTH_LONG).show();

                        new getTrackingZoneAsync().execute();

                    }else {
                        Toast.makeText(context, response.body().getErrors().get(0).toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<TrackingSetting> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    class getTrackingZoneAsync extends AsyncTask<String, String, Integer> {

        long visitorId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            visitorId = getPrefUserMasterId();
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            pd = new FontProgressDialog(mContext);
            final String[] mMsg = {""};

            ZoneBody zoneBody = new ZoneBody();
            zoneBody.setVisitorId(visitorId);
            ApiInterface apiService = ApiClient.trackingRetrofitClient().create(ApiInterface.class);
            pd.setMessage(mContext.getString(R.string.recieiving_info));
            pd.setCancelable(false);
            pd.show();

            Call<VisitorZoneLocation> call = apiService.GetZoneLocation(zoneBody);
            call.enqueue(new Callback<VisitorZoneLocation>() {
                @Override
                public void onResponse(Call<VisitorZoneLocation> call, Response<VisitorZoneLocation> response) {
                    pd.dismiss();
                    if (response.body() != null) {
                        if (response.body().isSucceeded()) {
                            List<Datum> data =  response.body().getData();
                            new SaveTrackingZoneAsyncTask(data).execute();

                        }else {
                            if (response.body() != null) {
                                pd.dismiss();
                                mMsg[0] = response.body().getMessage();
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<VisitorZoneLocation> call, Throwable t) {
                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                    FirebaseCrashlytics.getInstance().log(t.getMessage());
                    pd.dismiss();
                    mMsg[0] = t.toString();
                }
            });
        }
    }

    class SaveTrackingZoneAsyncTask extends AsyncTask<String, String, Integer> {

        List<Datum> trackingZoneData;

        public SaveTrackingZoneAsyncTask(List<Datum> data){
            trackingZoneData = data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new FontProgressDialog(mContext);
            pd.setMessage(mContext.getString(R.string.storing_info));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            db.open();
            if (trackingZoneData != null){
                if (trackingZoneData.size() > 0){
                    db.DeleteAllZone();
                    db.DeleteAllZoneLocation();
                    for (Datum datum : trackingZoneData){
                        DataService.InsertZone(db, datum.getZone());
                        DataService.InsertZoneLocation(db,datum.getZone().getZoneLocations());
                    }
                }
            }
            db.close();

            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            mapPolygon.showPolygon();
            pd.dismiss();
        }

    }

}
