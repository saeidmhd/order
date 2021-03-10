package com.mahak.order.gpsTracking;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.mahak.order.R;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.gpsTracking.wakeful.WakefulIntentService;

import java.util.Calendar;

/**
 * Created by mostafavi on 7/13/2016.
 */
public class GpsService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void doWakefulWork(Intent intent) {

        Log.d("@GPS_TIMER", "GPSStart");
        GpsTracking gpsTracking = new GpsTracking(getApplicationContext());

        String startTime = ServiceTools.getKeyFromSharedPreferences(getApplicationContext(), ProjectInfo.pre_start_time_tracking);
        String endTime = ServiceTools.getKeyFromSharedPreferences(getApplicationContext(), ProjectInfo.pre_end_time_tracking);

        if (!ServiceTools.isNull(startTime) && !gpsTracking.isRunService()) {
            long lastDate = ServiceTools.toLong(ServiceTools.getKeyFromSharedPreferences(getApplicationContext(), ProjectInfo.pre_last_date_notification_tracking));
            Calendar calendar = Calendar.getInstance();
            String[] sTime = startTime.split(":");
            if (calendar.getTimeInMillis() - lastDate > 86400000) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                if (hour > ServiceTools.toInt(sTime[0]) || (hour == ServiceTools.toInt(sTime[0]) && min >= ServiceTools.toInt(sTime[1]))) {
                    gpsTracking.showNotification(getString(R.string.str_msg_notification_start_tracking), GpsTracking.TYPE_START_TRACKING);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    ServiceTools.setKeyInSharedPreferences(getApplicationContext(), ProjectInfo.pre_last_date_notification_tracking, String.valueOf(calendar.getTimeInMillis()));
                }
            }
        }


        if (!ServiceTools.isNull(endTime) && gpsTracking.isRunService()) {
            long lastDate = ServiceTools.toLong(ServiceTools.getKeyFromSharedPreferences(getApplicationContext(), ProjectInfo.pre_last_end_date_notification_tracking));
            Calendar calendar = Calendar.getInstance();
            String[] sTime = endTime.split(":");
            if (calendar.getTimeInMillis() - lastDate > 86400000) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                if (hour > ServiceTools.toInt(sTime[0]) || (hour == ServiceTools.toInt(sTime[0]) && min >= ServiceTools.toInt(sTime[1]))) {
                    gpsTracking.showNotification(getString(R.string.str_msg_notification_end_tracking), GpsTracking.TYPE_END_TRACKING);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    ServiceTools.setKeyInSharedPreferences(getApplicationContext(), ProjectInfo.pre_last_end_date_notification_tracking, String.valueOf(calendar.getTimeInMillis()));
                }
            }
        }



        if (gpsTracking.isRunService()) {
            gpsTracking.showNotificationServiceRun();
            gpsTracking.currentLocation();
        }

        if (gpsTracking.isPauseService()) {
            gpsTracking.showNotificationServiceRun();
        }

        if (ServiceTools.isNetworkAvailable(getApplicationContext())) {
            gpsTracking.sendOldPoints();
        }

    }


}
