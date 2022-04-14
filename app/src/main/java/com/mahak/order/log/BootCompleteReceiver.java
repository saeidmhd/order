package com.mahak.order.log;

import static android.content.Context.LOCATION_SERVICE;

import static com.mahak.order.BaseActivity.isRadaraActive;

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
import com.mahak.order.tracking.LocationService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BootCompleteReceiver extends BroadcastReceiver {
    RadaraDb radaraDb;
    StatusLog statusLog;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(isRadaraActive()){
            context.startService(new Intent(context, LocationService.class));
            long currentTime = System.currentTimeMillis();
            if(radaraDb == null) radaraDb = new RadaraDb(context);
            radaraDb.open();
            ArrayList<StatusLog> statusLogs = new ArrayList<>();
            ManageLog manageLog = new ManageLog();
            String action = intent.getAction();
            JSONObject jsonObject = new JSONObject();
            if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
                if (statusLog == null) statusLog = new StatusLog();
                statusLog.setType(1);
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("device", "Boot Complete");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (statusLog.getValue() != null)
                    if (statusLog.getValue().equals(jsonObject.toString()))
                        return;
                statusLog.setValue(jsonObject.toString());
            }
            if(statusLog != null){
                statusLog.setCreated(ServiceTools.getFormattedDate(currentTime));
                statusLog.setVisitorId(BaseActivity.getPrefUserId());
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
