package com.mahak.order.gpsTracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by mostafavi on 7/13/2016.
 */
public class OnBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        GpsTracking.setAlarmManager(context);
    }


}