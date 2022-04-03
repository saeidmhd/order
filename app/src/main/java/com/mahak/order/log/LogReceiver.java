package com.mahak.order.log;

import static android.content.Context.LOCATION_SERVICE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.mahak.order.common.ServiceTools;

public class LogReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("test_log",action);
        switch (action) {
            case Intent.ACTION_BOOT_COMPLETED:
                ServiceTools.writeLog("BOOT_COMPLETED");
                Log.d("test_log","BOOT_COMPLETED");
                break;
            case ConnectivityManager.CONNECTIVITY_ACTION:
                boolean network = ServiceTools.isNetworkAvailable2(context);
                if (!network) {
                    ServiceTools.writeLog("mobile_data_disabled");
                    Log.d("test_log","mobile_data_disabled");
                } else {
                    ServiceTools.writeLog("mobile_data_enabled");
                    Log.d("test_log","mobile_data_enabled");
                }
                break;
            case WifiManager.WIFI_STATE_CHANGED_ACTION:
                boolean con = ServiceTools.isNetworkAvailable2(context);
                if (!con) {
                    ServiceTools.writeLog("wifi_disabled");
                    Log.d("test_log","wifi_disabled");
                } else {
                    ServiceTools.writeLog("wifi_enabled");
                    Log.d("test_log","wifi_enabled");
                }
                break;
            case LocationManager.PROVIDERS_CHANGED_ACTION:
                LocationManager service = (LocationManager) context.getSystemService(LOCATION_SERVICE);
                boolean isGPSPROVIDEREnabled = service != null && service.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (!isGPSPROVIDEREnabled) {
                    ServiceTools.writeLog("gps_disabled");
                    Log.d("test_log","gps_disabled");
                } else {
                    ServiceTools.writeLog("gps_enabled");
                    Log.d("test_log","gps_enabled");
                }
                break;
            case Intent.ACTION_BATTERY_LOW:
                ServiceTools.writeLog("BATTERY LOW!!");
                Log.d("test_log","BATTERY LOW!!");
                break;
            case Intent.ACTION_BATTERY_OKAY:
                ServiceTools.writeLog("BATTERY ok!!");
                Log.d("test_log","BATTERY ok!!");
                break;

        }

    }
}
