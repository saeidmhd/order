package com.mahak.order.log;

import static android.content.Context.LOCATION_SERVICE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import com.mahak.order.common.ServiceTools;

public class GPSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
            LocationManager service = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                service = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            }
            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                boolean isGPSPROVIDEREnabled = service != null && service.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (!isGPSPROVIDEREnabled) {
                    Log.d("cahnage", "gps_disabled");
                }else {
                    Log.d("cahnage", "gps_enabled");
                }
            }
        }
    }
}
