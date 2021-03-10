package com.mahak.order.gpsTracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;

/**
 * Created by mostafavi on 7/16/2016.
 */
public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("@GPS_Notification", "Event");
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();
        GpsTracking gpsTracking = new GpsTracking(context);
        int type = -1;
        if (bundle != null && bundle.containsKey(ProjectInfo._TAG_TYPE))
            type = bundle.getInt(ProjectInfo._TAG_TYPE);
        if (action.equals(ProjectInfo._notification_action_yes)) {
            if (type == GpsTracking.TYPE_START_TRACKING) {
                gpsTracking.showNotificationServiceRun();
                gpsTracking.stopNotificationTracking();
                gpsTracking.startTracking();
                gpsTracking.executeEventLocations(null);
            } else if (type == GpsTracking.TYPE_END_TRACKING) {
                gpsTracking.stopNotificationServiceTracking();
                gpsTracking.stopNotificationTracking();
                gpsTracking.executeEventLocations(null);
            }
        } else if (action.equals(ProjectInfo._notification_action_stop)) {
            gpsTracking.stopNotificationServiceTracking();
            gpsTracking.stopTracking();
            gpsTracking.executeEventLocations(null);
        } else if (action.equals(ProjectInfo._notification_action_pause)) {
            if (gpsTracking.isRunService()) {
                gpsTracking.stopTracking();
                ServiceTools.setKeyInSharedPreferences(context, ProjectInfo.pre_is_tracking_pause, "1");
                gpsTracking.showNotificationServiceRun();
            } else {
                gpsTracking.startTracking();
                ServiceTools.setKeyInSharedPreferences(context, ProjectInfo.pre_is_tracking_pause, "0");
                gpsTracking.showNotificationServiceRun();
            }
            gpsTracking.executeEventLocations(null);
        } else if (action.equals(ProjectInfo._notification_action_no)) {
            gpsTracking.stopNotificationTracking();
        }

    }
}
