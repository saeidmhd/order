package com.mahak.order.log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mahak.order.common.ServiceTools;

public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkStatus.getConnectivityStatusString(context);
        boolean con = ServiceTools.isNetworkAvailable2(context);
        Log.d("cahnage", String.valueOf(con));
        intent.getAction();
    }
}
