package com.mahak.order.gpsTracking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.mahak.order.BaseActivity;
import com.mahak.order.LoginActivityRestApi;
import com.mahak.order.common.GpsPointSignalR;
import com.mahak.order.common.RecieveSignalR.ReceiveResult;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

public class RealTimeLocation {

    HubConnection hubConnection;
    Context context;
    Activity activity;
    HubConnection[] hubConnectionArrayList;

    public RealTimeLocation(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
        hubConnection = HubConnectionBuilder.create("https://tracking.mahaksoft.com/TrackingHub?token=" + BaseActivity.getPrefSignalUserToken()).build();
        signalSetup();
    }

    public void sendLocation(Location location) {
        sendLocationSignalR(location);
    }

    private void signalSetup() {
        listenOnConnected();
        listenReceiveAdminConnect();
        listenReceiveDisconnectAdmin();
        listenReceiveNewLocation();
        new HubConnectionTask().execute(hubConnection);
    }

    class HubConnectionTask extends AsyncTask<HubConnection, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(HubConnection... hubConnections) {
            HubConnection hubConnection = hubConnections[0];
            hubConnectionArrayList = hubConnections;
            hubConnection.start().blockingAwait();
            Log.d("SignalRManage",hubConnection.getConnectionState().toString());
            return null;
        }
    }

    private void listenReceiveNewLocation() {
        hubConnection.on("ReceiveNewLocation", (ReceiveResult) -> {
            Log.d("SignalRManage",ReceiveResult.getMessage());
        }, ReceiveResult.class);
    }

    private void listenReceiveAdminConnect() {
        hubConnection.on("ReceiveAdminConnect", (mahakId) -> {
            Log.d("SignalRManage",mahakId);
        }, String.class);
    }
    private void listenReceiveDisconnectAdmin() {
        hubConnection.on("ReceiveDisconnectAdmin", (mahakId) -> {
            Log.d("SignalRManage",mahakId);
        }, String.class);
    }

    private void listenOnConnected() {
        hubConnection.on("OnConnected", (message) -> {
            if(message == 400 || message == 401){
                BaseActivity.setPrefSignalUserToken("");
                Intent intent = new Intent(activity, LoginActivityRestApi.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                activity.finish();
            }
            Log.d("SignalRManage",String.valueOf(message));
        },Integer.class);
    }

    private void sendLocationSignalR(Location location) {
        GpsPointSignalR gpsPointSignalR = new GpsPointSignalR();
        gpsPointSignalR.setLatitude(location.getLatitude());
        gpsPointSignalR.setLongitude(location.getLongitude());
        gpsPointSignalR.setUserToken(BaseActivity.getPrefSignalUserToken());
        try {hubConnection.send("SendNewLocation", gpsPointSignalR);
            Log.d("SendNewLocation", location.getLatitude() + " " + location.getLongitude());
        } catch (Exception e) {
            Log.d("SendNewLocation", "SendNewLocation_exception");
            e.printStackTrace();
        }
    }
}
