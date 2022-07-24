package com.mahak.order.tracking;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.BaseActivity;
import com.mahak.order.R;
import com.mahak.order.apiHelper.ApiClient;
import com.mahak.order.apiHelper.ApiInterface;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.StopLocation.StopLocationResponse;
import com.mahak.order.common.StopLocation.StopLog;
import com.mahak.order.common.User;
import com.mahak.order.common.loginSignalr.SignalLoginBody;
import com.mahak.order.common.loginSignalr.SignalLoginResult;
import com.mahak.order.common.manageLog.ManageLog;
import com.mahak.order.common.manageLog.StatusLog;
import com.mahak.order.mission.AllMissionDetailBody;
import com.mahak.order.mission.GetAllMission;
import com.mahak.order.mission.AllMissionBody;
import com.mahak.order.mission.GetAllMissionDetail;
import com.mahak.order.mission.Mission;
import com.mahak.order.mission.MissionDetail;
import com.mahak.order.mission.SendUpdate;
import com.mahak.order.mission.updateResponse.MissionUpdateResponse;
import com.mahak.order.service.DataService;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.storage.RadaraDb;
import com.mahak.order.tracking.setting.SettingBody;
import com.mahak.order.tracking.setting.TrackingSetting;
import com.mahak.order.tracking.visitorZone.Datum;
import com.mahak.order.tracking.visitorZone.VisitorZoneLocation;
import com.mahak.order.tracking.visitorZone.ZoneBody;
import com.mahak.order.widget.FontProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mahak.order.BaseActivity.getPrefSignalUserToken;
import static com.mahak.order.BaseActivity.getPrefUserMasterId;
import static com.mahak.order.BaseActivity.setPrefSignalUserToken;

public class TrackingConfig {

    Context mContext;
    private FontProgressDialog pd;

    private RadaraDb radaraDb;
    JSONObject gpsData = new JSONObject();

    public TrackingConfig(Context context, FontProgressDialog pd){
        mContext = context;
        this.pd = pd;
        radaraDb = new RadaraDb(mContext);
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

        pd.setMessage("در حال دریافت توکن رادارا");
        pd.setCancelable(false);
        pd.show();
        call.enqueue(new Callback<SignalLoginResult>() {
            @Override
            public void onResponse(Call<SignalLoginResult> call, Response<SignalLoginResult> response) {
                dismissProgressDialog();
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
                dismissProgressDialog();
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getSetting(Context context) {
        ApiInterface apiService = ApiClient.trackingRetrofitClient().create(ApiInterface.class);
        SettingBody settingBody = new SettingBody();
        settingBody.setVisitorId(0);
        Call<TrackingSetting> call = apiService.GetTrackingSetting(getPrefSignalUserToken(),settingBody);
        pd.setMessage("در حال دریافت تنظیمات رادارا");
        pd.setCancelable(false);
        pd.show();
        call.enqueue(new Callback<TrackingSetting>() {
            @Override
            public void onResponse(Call<TrackingSetting> call, Response<TrackingSetting> response) {
                dismissProgressDialog();
                if (response.body() != null) {
                    if (response.body().isSucceeded()) {

                        int MIN_DISTANCE_CHANGE_FOR_UPDATES = response.body().getData().getSendPointsPerMeter();
                        int MIN_TIME_BW_UPDATES = response.body().getData().getSendPointsEveryMinute();
                        int radius = response.body().getData().getRadius();
                        int startTime = response.body().getData().getStartTime();
                        int endTime = response.body().getData().getEndTime();
                        boolean sendingPointsByAdmin = response.body().getData().isControlSendingPointsByAdmin();
                        boolean sendingPoints = response.body().getData().isSendingPoints();
                        boolean sendPointsBasedMeter = response.body().getData().isSendPointsBasedMeter();

                        BaseActivity.setPrefAdminControl(sendingPointsByAdmin);
                        if(sendingPoints)
                            BaseActivity.setPrefTrackingControl(1);
                        else
                            BaseActivity.setPrefTrackingControl(0);

                        try {
                            gpsData.put(ProjectInfo._json_key_mingps_distance_change, MIN_DISTANCE_CHANGE_FOR_UPDATES);
                            gpsData.put(ProjectInfo._json_key_mingps_time_change, MIN_TIME_BW_UPDATES);
                            gpsData.put(ProjectInfo._json_key_sendingPointsByAdmin, sendingPointsByAdmin);
                            gpsData.put(ProjectInfo._json_key_sendingPoints, sendingPoints);
                            gpsData.put(ProjectInfo._json_key_radius, radius);
                            gpsData.put(ProjectInfo._json_sendPointsBasedMeter, sendPointsBasedMeter);
                            gpsData.put(ProjectInfo._json_key_startTime, startTime);
                            gpsData.put(ProjectInfo._json_key_endTime, endTime);
                            ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_gps_config, gpsData.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new GetTrackingZoneAsync().execute();

                    }else {
                        Toast.makeText(context, response.body().getErrors().get(0).toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<TrackingSetting> call, Throwable t) {
                dismissProgressDialog();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    class GetTrackingZoneAsync extends AsyncTask<String, String, Integer> {

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
            final String[] mMsg = {""};

            ZoneBody zoneBody = new ZoneBody();
            zoneBody.setVisitorId(visitorId);
            ApiInterface apiService = ApiClient.trackingRetrofitClient().create(ApiInterface.class);
            pd.setMessage("در حال دریافت محدوده ها");
            pd.setCancelable(false);
            pd.show();

            Call<VisitorZoneLocation> call = apiService.GetZoneLocation(zoneBody);
            call.enqueue(new Callback<VisitorZoneLocation>() {
                @Override
                public void onResponse(Call<VisitorZoneLocation> call, Response<VisitorZoneLocation> response) {
                    dismissProgressDialog();
                    if (response.body() != null) {
                        if (response.body().isSucceeded()) {
                            List<Datum> data =  response.body().getData();
                            try {
                                if(data.size()>0){
                                    gpsData.put(ProjectInfo._json_key_isRestricted, data.get(0).isFactorRegistrationOutRange());
                                    ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_gps_config, gpsData.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            new SaveTrackingZoneAsyncTask(data).execute();

                        }else {
                            if (response.body() != null) {
                                mMsg[0] = response.body().getMessage();
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<VisitorZoneLocation> call, Throwable t) {
                    dismissProgressDialog();
                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                    FirebaseCrashlytics.getInstance().log(t.getMessage());
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
            pd.setMessage(mContext.getString(R.string.storing_info));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            radaraDb.open();
            radaraDb.DeleteZoneLocation();
            if (trackingZoneData != null){
                if (trackingZoneData.size() > 0){
                    for (Datum datum : trackingZoneData){
                        DataService.InsertZone(radaraDb, datum);
                        DataService.InsertZoneLocation(radaraDb,datum.getZoneLocations());
                    }
                }
            }
            radaraDb.close();
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            new SendStopLogAsync().execute();
            dismissProgressDialog();
        }

    }

    class SendStopLogAsync extends AsyncTask<String, String, Integer> {

        ArrayList<StopLog> stopLogs = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            getAllStopLogs();
            return 0;
        }
        private void getAllStopLogs() {
            if (radaraDb == null) radaraDb = new RadaraDb(mContext);
            radaraDb.open();
            try {
                stopLogs = radaraDb.getAllStopLogNotSend();
            } catch (Exception e) {
                e.printStackTrace();
                if(e.getMessage() != null)
                    Log.e("saveInDb",e.getMessage());
            }
            radaraDb.close();
        }

        @Override
        protected void onPostExecute(Integer result) {
            final String[] mMsg = {""};
            pd.setMessage("در حال ارسال نقاط توقف");
            pd.setCancelable(false);
            pd.show();
            ApiInterface apiService = ApiClient.trackingRetrofitClient().create(ApiInterface.class);
            Call<StopLocationResponse> call = apiService.SetStopLocation(stopLogs);
            call.enqueue(new Callback<StopLocationResponse>() {
                @Override
                public void onResponse(Call<StopLocationResponse> call, Response<StopLocationResponse> response) {
                    dismissProgressDialog();
                    if (response.body() != null) {
                        if (response.body().isSucceeded()) {
                            for(StopLog stopLog : stopLogs)
                                stopLog.setSent(1);
                            UpdateOrInsertStopLogToDb(stopLogs);
                        }else
                            ServiceTools.writeLog("\n" + "error in sending points");
                    }
                    new SendMissionStatusAsync().execute();
                }
                @Override
                public void onFailure(Call<StopLocationResponse> call, Throwable t) {
                    dismissProgressDialog();
                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                    FirebaseCrashlytics.getInstance().log(t.getMessage());
                    mMsg[0] = t.toString();
                }
            });
        }
    }

    class SendMissionStatusAsync extends AsyncTask<String, String, Integer> {

        ArrayList<Mission> missions = new ArrayList<>();
        ArrayList<MissionDetail> missionDetails = new ArrayList<>();
        SendUpdate sendUpdate = new SendUpdate();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            getAllStatusLogs();
            return 0;
        }
        private void getAllStatusLogs() {
            if (radaraDb == null) radaraDb = new RadaraDb(mContext);
            radaraDb.open();
            try {
                missions = radaraDb.getAllMission();
                for(Mission mission : missions){
                    missionDetails = radaraDb.getAllMissionDetailWithMissionId(mission.getMissionId());
                    mission.setMissionDetails(missionDetails);
                }
                sendUpdate.setMissions(missions);

            } catch (Exception e) {
                e.printStackTrace();
                if(e.getMessage() != null)
                    Log.e("saveInDb",e.getMessage());
            }
            radaraDb.close();
        }

        @Override
        protected void onPostExecute(Integer result) {
            final String[] mMsg = {""};
            pd.setMessage("در حال ارسال آپدیت وضعیت ماموریت ها");
            pd.setCancelable(false);
            pd.show();
            ApiInterface apiService = ApiClient.trackingRetrofitClient().create(ApiInterface.class);
            Call<MissionUpdateResponse> call = apiService.saveMissionStatus(getPrefSignalUserToken(),sendUpdate);
            call.enqueue(new Callback<MissionUpdateResponse>() {
                @Override
                public void onResponse(Call<MissionUpdateResponse> call, Response<MissionUpdateResponse> response) {
                    dismissProgressDialog();
                    if (response.body() != null) {
                        if (response.body().getSucceeded()) {
                            Log.e("send","success");
                        }else
                            Log.e("send","error");
                    }
                    new GetMissionAsync().execute();
                }
                @Override
                public void onFailure(Call<MissionUpdateResponse> call, Throwable t) {
                    dismissProgressDialog();
                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                    FirebaseCrashlytics.getInstance().log(t.getMessage());
                    mMsg[0] = t.toString();
                }
            });
        }
    }

    class GetMissionAsync extends AsyncTask<String, String, Integer> {
        List<Mission> missions = new ArrayList<>();
        long rowVersion = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            rowVersion = getRowVersion();
            return 0;
        }
        private long getRowVersion() {
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            final String[] mMsg = {""};

            AllMissionBody getAllMissionBody = new AllMissionBody();
            getAllMissionBody.setFromMissionVersion(0l);
            ApiInterface apiService = ApiClient.trackingRetrofitClient().create(ApiInterface.class);
            pd.setMessage("در حال دریافت ماموریت ها");
            pd.setCancelable(false);
            pd.show();

            Call<GetAllMission> call = apiService.GetAllMission(getPrefSignalUserToken(),getAllMissionBody);
            call.enqueue(new Callback<GetAllMission>() {
                @Override
                public void onResponse(Call<GetAllMission> call, Response<GetAllMission> response) {
                    dismissProgressDialog();
                    if (response.body() != null) {
                        if (response.body().getSucceeded()) {
                            missions =  response.body().getData();
                            if(missions.size() > 0){
                                new SaveMissionAsyncTask(missions).execute();
                            }
                        }else {
                            if (response.body() != null) {
                                // mMsg[0] = response.body().getMessage();
                            }
                        }
                    }

                }
                @Override
                public void onFailure(Call<GetAllMission> call, Throwable t) {
                    dismissProgressDialog();
                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                    FirebaseCrashlytics.getInstance().log(t.getMessage());
                    mMsg[0] = t.toString();
                }
            });
        }
    }

    class SaveMissionAsyncTask extends AsyncTask<String, String, Integer> {

        List<Mission> missions;

        public SaveMissionAsyncTask(List<Mission> data){
            missions = data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage(mContext.getString(R.string.storing_info));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            radaraDb.open();
            if (missions != null){
                if (missions.size() > 0){
                    for (Mission mission : missions){
                        DataService.InsertMission(radaraDb, mission);
                    }
                }
            }
            radaraDb.close();
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            new GetMissionDetailAsync().execute();
            dismissProgressDialog();
        }

    }

    class GetMissionDetailAsync extends AsyncTask<String, String, Integer> {
        List<MissionDetail> missionDetails = new ArrayList<>();
        long rowVersion = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            rowVersion = getRowVersion();
            return 0;
        }
        private long getRowVersion() {
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            final String[] mMsg = {""};

            AllMissionDetailBody allMissionDetailBody = new AllMissionDetailBody();
            allMissionDetailBody.setFromMissionDetailVersion(rowVersion);
            ApiInterface apiService = ApiClient.trackingRetrofitClient().create(ApiInterface.class);
            pd.setMessage("در حال دریافت جزییات ماموریت ها");
            pd.setCancelable(false);
            pd.show();

            Call<GetAllMissionDetail> call = apiService.GetAllMissionDetail(getPrefSignalUserToken(),allMissionDetailBody);
            call.enqueue(new Callback<GetAllMissionDetail>() {
                @Override
                public void onResponse(Call<GetAllMissionDetail> call, Response<GetAllMissionDetail> response) {
                    dismissProgressDialog();
                    if (response.body() != null) {
                        if (response.body().getSucceeded()) {
                            missionDetails =  response.body().getData();
                            if(missionDetails.size() > 0){
                                new SaveMissionDetailAsyncTask(missionDetails).execute();
                            }
                        }else {
                            if (response.body() != null) {
                                // mMsg[0] = response.body().getMessage();
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<GetAllMissionDetail> call, Throwable t) {
                    dismissProgressDialog();
                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                    FirebaseCrashlytics.getInstance().log(t.getMessage());
                    mMsg[0] = t.toString();
                }
            });
        }
    }

    class SaveMissionDetailAsyncTask extends AsyncTask<String, String, Integer> {

        List<MissionDetail> missionDetails;

        public SaveMissionDetailAsyncTask(List<MissionDetail> data){
            missionDetails = data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage(mContext.getString(R.string.storing_info));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            radaraDb.open();
            if (missionDetails != null){
                if (missionDetails.size() > 0){
                    for (MissionDetail missionDetail : missionDetails){
                        DataService.InsertMissionDetail(radaraDb, missionDetail);
                    }
                }
            }
            radaraDb.close();
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            new SendStatusLogAsync().execute();
            dismissProgressDialog();
        }

    }

    class SendStatusLogAsync extends AsyncTask<String, String, Integer> {

        ArrayList<StatusLog> statusLogs = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            getAllStatusLogs();
            return 0;
        }
        private void getAllStatusLogs() {
            if (radaraDb == null) radaraDb = new RadaraDb(mContext);
            radaraDb.open();
            try {
                statusLogs = radaraDb.getAllStatusLogNotSend();
            } catch (Exception e) {
                e.printStackTrace();
                if(e.getMessage() != null)
                    Log.e("saveInDb",e.getMessage());
            }
            radaraDb.close();
        }

        @Override
        protected void onPostExecute(Integer result) {
            final String[] mMsg = {""};
            pd.setMessage("در حال ارسال لاگ های مدیریت");
            pd.setCancelable(false);
            pd.show();
            ManageLog manageLog = new ManageLog();
            manageLog.setStatusLogs(statusLogs);
            ApiInterface apiService = ApiClient.trackingRetrofitClient().create(ApiInterface.class);
            Call<StopLocationResponse> call = apiService.saveStatusLog(manageLog);
            call.enqueue(new Callback<StopLocationResponse>() {
                @Override
                public void onResponse(Call<StopLocationResponse> call, Response<StopLocationResponse> response) {
                    dismissProgressDialog();
                    if (response.body() != null) {
                        if (response.body().isSucceeded()) {
                            for(StatusLog statusLog : statusLogs)
                                statusLog.setSent(1);
                            updateManageLogToDb(statusLogs,mContext);
                        }else
                            ServiceTools.writeLog("\n" + "error in sending points");
                    }
                }
                @Override
                public void onFailure(Call<StopLocationResponse> call, Throwable t) {
                    dismissProgressDialog();
                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                    FirebaseCrashlytics.getInstance().log(t.getMessage());
                    mMsg[0] = t.toString();
                }
            });
        }
    }

    private void dismissProgressDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    private void updateManageLogToDb(ArrayList<StatusLog> statusLogs, Context context) {
        if (radaraDb == null) radaraDb = new RadaraDb(context);
        radaraDb.open();
        try {
            radaraDb.updateManageLogs(statusLogs);
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getMessage() != null)
                Log.e("saveInDb",e.getMessage());
        }
        radaraDb.close();
    }

    private void UpdateOrInsertStopLogToDb(ArrayList<StopLog> stopLogs) {
        if (radaraDb == null) radaraDb = new RadaraDb(mContext);
        radaraDb.open();
        try {
            radaraDb.updateStopLogs(stopLogs);
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getMessage() != null)
                Log.e("saveInDb",e.getMessage());
        }
        radaraDb.close();
    }
}
