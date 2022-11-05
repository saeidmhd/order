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
import com.mahak.order.common.manageLog.ManageLog;
import com.mahak.order.common.manageLog.StatusLog;
import com.mahak.order.storage.RadaraDb;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogReceiver extends BroadcastReceiver {
    RadaraDb radaraDb;
    static StatusLog statusLog;
    
    @Override
    public void onReceive(Context context, Intent intent) {
        long startTime = System.currentTimeMillis();
        if(radaraDb == null) radaraDb = new RadaraDb(context);
        radaraDb.open();

        ArrayList<StatusLog> statusLogs = new ArrayList<>();
        ManageLog manageLog = new ManageLog();
        String action = intent.getAction();
        JSONObject jsonObject = new JSONObject();
        switch (action) {
            case ConnectivityManager.CONNECTIVITY_ACTION:
                if(statusLog == null) statusLog = new StatusLog();
                boolean network = ServiceTools.isNetworkAvailable2(context);
                statusLog.setType(2);
                if (!network) {
                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("internet","Off");
                    } catch (JSONException e) {
                        ServiceTools.logToFireBase(e);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("internet","On");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(statusLog.getValue() != null)
                    if(statusLog.getValue().equals(jsonObject.toString()))
                        return;
                statusLog.setValue(jsonObject.toString());
                break;
            case LocationManager.PROVIDERS_CHANGED_ACTION:
                if(statusLog == null) statusLog = new StatusLog();
                LocationManager service = (LocationManager) context.getSystemService(LOCATION_SERVICE);
                boolean isGPSPROVIDEREnabled = service != null && service.isProviderEnabled(LocationManager.GPS_PROVIDER);
                statusLog.setType(3);
                if (!isGPSPROVIDEREnabled) {
                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("gps","Off");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("gps","On");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(statusLog.getValue() != null)
                    if(statusLog.getValue().equals(jsonObject.toString()))
                        return;
                statusLog.setValue(jsonObject.toString());
                break;
            case Intent.ACTION_BATTERY_LOW:
                if(statusLog == null) statusLog = new StatusLog();
                statusLog.setType(4);
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("battery","Low");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(statusLog.getValue() != null)
                    if(statusLog.getValue().equals(jsonObject.toString()))
                        return;
                statusLog.setValue(jsonObject.toString());

                break;
            case Intent.ACTION_BATTERY_OKAY:
                if(statusLog == null) statusLog = new StatusLog();
                statusLog.setType(4);
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("battery","High");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(statusLog.getValue() != null)
                    if(statusLog.getValue().equals(jsonObject.toString()))
                        return;
                statusLog.setValue(jsonObject.toString());
                break;
        }
        if(statusLog != null){
                statusLog.setDate(ServiceTools.getFormattedDateAndTime(startTime));
                statusLog.setAccountid(BaseActivity.getPrefUserId());
                statusLogs.add(statusLog);
                manageLog.setStatusLogs(statusLogs);
                if(ServiceTools.isOnline(context)){
                    sendManageLogToServer(manageLog,context);
                }else {
                    statusLogs.get(0).setSent(-1);
                    updateManageLogToDb(statusLogs, context);
                }
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

    public void sendManageLogToServer(ManageLog manageLog, Context context) {
        ApiInterface apiService = ApiClient.trackingRetrofitClient().create(ApiInterface.class);
        Call<StopLocationResponse> call = apiService.saveStatusLog(manageLog);
        call.enqueue(new Callback<StopLocationResponse>() {
            @Override
            public void onResponse(Call<StopLocationResponse> call, Response<StopLocationResponse> response) {
                if (response.body() != null) {
                    if (!response.body().isSucceeded()) {
                        ArrayList<StatusLog> statusLogs = manageLog.getStatusLogs();
                        statusLogs.get(0).setSent(-1);
                        updateManageLogToDb(statusLogs,context);
                    }
                }
            }
            @Override
            public void onFailure(Call<StopLocationResponse> call, Throwable t) {
                ArrayList<StatusLog> statusLogs = manageLog.getStatusLogs();
                statusLogs.get(0).setSent(-1);
                updateManageLogToDb(statusLogs,context);
            }
        });
    }
}
