package com.mahak.order.log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BatteryLevelReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(Intent.ACTION_BATTERY_LOW)){
            Log.d("XYZ", "BATTERY LOW!!");
        }else if(action.equals(Intent.ACTION_BATTERY_OKAY))
            Log.d("XYZ", "BATTERY ok!!");

    }
}
