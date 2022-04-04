package com.mahak.order.log;

import static android.content.Context.LOCATION_SERVICE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.mahak.order.BaseActivity;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.StopLocation.StopLog;
import com.mahak.order.common.manageLog.ManageLog;
import com.mahak.order.storage.RadaraDb;

import java.util.ArrayList;

public class LogReceiver extends BroadcastReceiver {
    RadaraDb radaraDb;
    ManageLog manageLog;
    Context mContext;

    public LogReceiver(Context context) {
        mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long currentTime = System.currentTimeMillis();
        if(radaraDb == null) radaraDb = new RadaraDb(mContext);
        radaraDb.open();

        ArrayList<ManageLog> manageLogs = new ArrayList<>();
        String action = intent.getAction();
        switch (action) {
            case Intent.ACTION_BOOT_COMPLETED:
                if(manageLog == null) manageLog = new ManageLog();
                manageLog.setLog_type(1);
                manageLog.setLog_value(1);
                ServiceTools.writeLog("BOOT_COMPLETED");
                Log.d("test_log","BOOT_COMPLETED");
                break;
            case ConnectivityManager.CONNECTIVITY_ACTION:
                if(manageLog == null) manageLog = new ManageLog();
                boolean network = ServiceTools.isNetworkAvailable2(context);
                manageLog.setLog_type(2);
                if (!network) {
                    manageLog.setLog_value(-1);
                    ServiceTools.writeLog("no_network");
                    Log.d("test_log","no_network");
                } else {
                    manageLog.setLog_value(1);
                    ServiceTools.writeLog("ok_network");
                    Log.d("test_log","ok_network");
                }
                break;
            case LocationManager.PROVIDERS_CHANGED_ACTION:
                if(manageLog == null) manageLog = new ManageLog();
                LocationManager service = (LocationManager) context.getSystemService(LOCATION_SERVICE);
                boolean isGPSPROVIDEREnabled = service != null && service.isProviderEnabled(LocationManager.GPS_PROVIDER);
                manageLog.setLog_type(3);
                if (!isGPSPROVIDEREnabled) {
                    manageLog.setLog_value(-1);
                    ServiceTools.writeLog("gps_disabled");
                    Log.d("test_log","gps_disabled");
                } else {
                    manageLog.setLog_value(1);
                    ServiceTools.writeLog("gps_enabled");
                    Log.d("test_log","gps_enabled");
                }
                break;
            case Intent.ACTION_BATTERY_LOW:
                if(manageLog == null) manageLog = new ManageLog();
                manageLog.setLog_type(4);
                manageLog.setLog_value(-1);
                ServiceTools.writeLog("BATTERY LOW!!");
                Log.d("test_log","BATTERY LOW!!");
                break;
            case Intent.ACTION_BATTERY_OKAY:
                if(manageLog == null) manageLog = new ManageLog();
                manageLog.setLog_type(4);
                manageLog.setLog_value(1);
                ServiceTools.writeLog("BATTERY ok!!");
                Log.d("test_log","BATTERY ok!!");
                break;
        }
        if(manageLog != null){
            manageLog.setDate_time(ServiceTools.getFormattedDate(currentTime));
            manageLog.setManageLogClientId(ServiceTools.toLong(ServiceTools.getStopLocationId(currentTime)));
            manageLog.setVisitorId(BaseActivity.getPrefUserId());
            manageLogs.add(manageLog);
            radaraDb.updateManageLogs(manageLogs);
        }
    }
}
