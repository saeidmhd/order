package com.mahak.order.gpsTracking;

import android.content.Context;
import android.content.Intent;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.mahak.order.gpsTracking.wakeful.WakefulIntentService;

/**
 * Created by mostafavi on 7/13/2016.
 */
public class OnAlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        WakefulIntentService.sendWakefulWork(context,GpsTrackingService.class);
        Intent service = new Intent(context, GpsTrackingService.class);
        startWakefulService(context, service);

    }
}