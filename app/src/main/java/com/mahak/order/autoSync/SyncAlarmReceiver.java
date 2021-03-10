package com.mahak.order.autoSync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SyncAlarmReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.mahak.order.recievers.alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AutoSyncService.class);
        i.putExtra("foo", "bar");
        context.startService(i);
    }
}
