package com.mahak.order.tracking;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.mahak.order.BaseActivity;
import com.mahak.order.common.GpsPointSignalR;
import com.mahak.order.common.RecieveSignalR.ReceiveResult;
import com.mahak.order.common.ServiceTools;
import com.microsoft.signalr.Action1;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

import static com.mahak.order.BaseActivity.baseUrlSignalR;
import static com.mahak.order.BaseActivity.getPrefUserId;
import static com.mahak.order.BaseActivity.setPrefSignalUserToken;

import okhttp3.OkHttpClient;

public class RealTimeLocation implements Thread.UncaughtExceptionHandler {

    HubConnection hubConnection;
    Context context;
    Activity activity;
    Location mLocation;
    HubConnection[] hubConnectionArrayList;

    public RealTimeLocation(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    public void sendRealTimeLocation(Location location) {
        sendLocationSignalR(location);
        mLocation = location;
    }

    private void signalSetup() {
        hubConnection = HubConnectionBuilder.create(baseUrlSignalR + BaseActivity.getPrefSignalUserToken()).build();
        hubConnection.setKeepAliveInterval(60000);
        hubConnection.setServerTimeout(60000);
        listenOnConnected();
        listenReceiveAdminConnect();
        listenReceiveDisconnectAdmin();
        listenReceiveNewLocation();
        listenDisconnectVisitor();
        try {
            new HubConnectionTask().execute(hubConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
    }

    public void stopRealTimeSend() {
        if(hubConnection != null)
            try {
                if(hubConnection.getConnectionState().toString().equals("CONNECTED"))
                    hubConnection.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    class HubConnectionTask extends AsyncTask<HubConnection, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(HubConnection... hubConnections) {
            hubConnection = hubConnections[0];
            try {
                hubConnection.start().blockingAwait();

            } catch (Exception e) {
                ServiceTools.writeLog(e.getMessage());
                e.printStackTrace();
            }
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
            if(message == 401 || message == 400){
                setPrefSignalUserToken("");
            }else {
                sendLocationSignalR(mLocation);
            }
            Log.d("SignalRManage", String.valueOf(message));
        }, Integer.class);
    }

    private void listenDisconnectVisitor() {
        hubConnection.on("ReceiveDisconnectVisitor", (ReceiveDisconnectVisitor) -> {
            Log.d("SignalRManage", ReceiveDisconnectVisitor.errorMessage);
        }, ReceiveDisconnectVisitor.class);
    }

    private void sendLocationSignalR(Location location) {
        if(hubConnection == null) {
            signalSetup();
        }else if(hubConnection.getConnectionState().toString().equals("CONNECTED")){
            if(location != null){
                GpsPointSignalR gpsPointSignalR = new GpsPointSignalR();
                gpsPointSignalR.setLatitude(location.getLatitude());
                gpsPointSignalR.setLongitude(location.getLongitude());
                gpsPointSignalR.setUserToken(BaseActivity.getPrefSignalUserToken());
                try {
                    hubConnection.send("SendNewLocation", gpsPointSignalR);
                    Log.d("SendNewLocation", location.getLatitude() + " " + location.getLongitude());
                } catch (Exception e) {
                    setPrefSignalUserToken("");
                    Log.d("SendNewLocation", "SendNewLocation_exception");
                    e.printStackTrace();
                }
            }
        } else {
            signalSetup();
        }
    }
}
