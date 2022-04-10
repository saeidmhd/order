package com.mahak.order.log;

import static android.content.Context.LOCATION_SERVICE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.util.Log;

import com.mahak.order.BaseActivity;
import com.mahak.order.apiHelper.ApiClient;
import com.mahak.order.apiHelper.ApiInterface;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.StopLocation.StopLocationResponse;
import com.mahak.order.common.manageLog.RadaraManageLog;
import com.mahak.order.storage.RadaraDb;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogReceiver extends BroadcastReceiver {
    RadaraDb radaraDb;
    RadaraManageLog radaraManageLog;
    Context mContext;

    public LogReceiver(Context context) {
        mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long currentTime = System.currentTimeMillis();
        if(radaraDb == null) radaraDb = new RadaraDb(mContext);
        radaraDb.open();

        ArrayList<RadaraManageLog> radaraManageLogs = new ArrayList<>();
        String action = intent.getAction();
        JSONObject jsonObject = new JSONObject();
        switch (action) {
            case Intent.ACTION_BOOT_COMPLETED:
                if(radaraManageLog == null) radaraManageLog = new RadaraManageLog();
                radaraManageLog.setType(1);
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("device","Boot Complete");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                radaraManageLog.setValue(jsonObject.toString());
                break;
            case ConnectivityManager.CONNECTIVITY_ACTION:
                if(radaraManageLog == null) radaraManageLog = new RadaraManageLog();
                boolean network = ServiceTools.isNetworkAvailable2(context);
                radaraManageLog.setType(2);
                if (!network) {
                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("internet","Off");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    radaraManageLog.setValue(jsonObject.toString());
                } else {
                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("internet","On");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    radaraManageLog.setValue(jsonObject.toString());
                }
                break;
            case LocationManager.PROVIDERS_CHANGED_ACTION:
                if(radaraManageLog == null) radaraManageLog = new RadaraManageLog();
                LocationManager service = (LocationManager) context.getSystemService(LOCATION_SERVICE);
                boolean isGPSPROVIDEREnabled = service != null && service.isProviderEnabled(LocationManager.GPS_PROVIDER);
                radaraManageLog.setType(3);
                if (!isGPSPROVIDEREnabled) {

                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("gps","Off");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    radaraManageLog.setValue(jsonObject.toString());
                } else {
                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("gps","On");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    radaraManageLog.setValue(jsonObject.toString());
                }
                break;
            case Intent.ACTION_BATTERY_LOW:
                if(radaraManageLog == null) radaraManageLog = new RadaraManageLog();
                radaraManageLog.setType(4);
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("battery","Low");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                radaraManageLog.setValue(jsonObject.toString());

                break;
            case Intent.ACTION_BATTERY_OKAY:
                if(radaraManageLog == null) radaraManageLog = new RadaraManageLog();
                radaraManageLog.setType(4);
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("battery","High");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                radaraManageLog.setValue(jsonObject.toString());
                break;
        }
        if(radaraManageLog != null){
            radaraManageLog.setCreated(ServiceTools.getFormattedDate(currentTime));
            radaraManageLog.setVisitorId(BaseActivity.getPrefUserId());

            if(ServiceTools.isOnline(mContext)){
                sendManageLogToServer(radaraManageLog);
            }else {
                radaraManageLog.setSent(-1);
                radaraManageLogs.add(radaraManageLog);
                updateManageLogToDb(radaraManageLogs);
            }
        }
    }

    private void updateManageLogToDb(ArrayList<RadaraManageLog> radaraManageLogs) {
        if (radaraDb == null) radaraDb = new RadaraDb(mContext);
        radaraDb.open();
        try {
            radaraDb.updateManageLogs(radaraManageLogs);
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getMessage() != null)
                Log.e("saveInDb",e.getMessage());
        }
        radaraDb.close();
    }

    public void sendManageLogToServer(RadaraManageLog radaraManageLog) {
        ApiInterface apiService = ApiClient.trackingRetrofitClient().create(ApiInterface.class);
        Call<StopLocationResponse> call = apiService.saveStatusLog(radaraManageLog);
        call.enqueue(new Callback<StopLocationResponse>() {
            @Override
            public void onResponse(Call<StopLocationResponse> call, Response<StopLocationResponse> response) {
                if (response.body() != null) {
                    if (!response.body().isSucceeded()) {
                        radaraManageLog.setSent(-1);
                        //updateManageLogToDb(radaraManageLog);
                    }
                }
            }
            @Override
            public void onFailure(Call<StopLocationResponse> call, Throwable t) {
                Log.d("fail",t.getMessage());
                radaraManageLog.setSent(-1);
            }
        });
    }
}
