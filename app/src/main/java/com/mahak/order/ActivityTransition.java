package com.mahak.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.DetectedActivity;
import com.mahak.order.tracking.Constants;
import com.mahak.order.tracking.DetectedActivitiesService;

public class ActivityTransition extends AppCompatActivity {

    BroadcastReceiver broadcastReceiver;
    Button btn1,btn2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        requestActivityRecognitionPermission();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Constants.BROADCAST_DETECTED_ACTIVITY)){
                    int type = intent.getIntExtra("type", -1);
                    int confidence = intent.getIntExtra("confidence", 0);
                    provideUserStateOutput(type, confidence);
                }
            }
        };

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startService(new Intent( ActivityTransition.this, DetectedActivitiesService.class ) );

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopService(new Intent( ActivityTransition.this, DetectedActivitiesService.class ));

            }
        });
    }

    private void provideUserStateOutput(int type, int confidence) {
        String label = "";
        switch (type){
            case DetectedActivity.IN_VEHICLE:
                Toast.makeText(this, "IN_VEHICLE", Toast.LENGTH_SHORT).show();
                label = "IN_VEHICLE";
                Log.d("DetectedActivity" , "IN_VEHICLE");
                break;
            case DetectedActivity.ON_BICYCLE:
                Toast.makeText(this, "ON_BICYCLE", Toast.LENGTH_SHORT).show();
                label = "ON_BICYCLE";
                Log.d("DetectedActivity" , "ON_BICYCLE");
                break;
            case DetectedActivity.ON_FOOT:
                Toast.makeText(this, "ON_FOOT", Toast.LENGTH_SHORT).show();
                label = "ON_FOOT";
                Log.d("DetectedActivity" , "ON_FOOT");
                break;
            case DetectedActivity.RUNNING:
                Toast.makeText(this, "RUNNING", Toast.LENGTH_SHORT).show();
                label = "RUNNING";
                Log.d("DetectedActivity" , "RUNNING");
                break;
            case DetectedActivity.STILL:
                Toast.makeText(this, "STILL", Toast.LENGTH_SHORT).show();
                label = "STILL";
                Log.d("DetectedActivity" , "STILL");
                break;
            case DetectedActivity.TILTING:
                Toast.makeText(this, "TILTING", Toast.LENGTH_SHORT).show();
                label = "TILTING";
                Log.d("DetectedActivity" , "TILTING");
                break;
            case DetectedActivity.WALKING:
                Toast.makeText(this, "WALKING", Toast.LENGTH_SHORT).show();
                label = "WALKING";
                Log.d("DetectedActivity" , "WALKING");
                break;
            case DetectedActivity.UNKNOWN:
                Toast.makeText(this, "UNKNOWN", Toast.LENGTH_SHORT).show();
                label = "UNKNOWN";
                Log.d("DetectedActivity" , "UNKNOWN");
                break;
        }

        Log.d("act_rect" , label);
    }

    private void requestActivityRecognitionPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACTIVITY_RECOGNITION},101);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Constants.BROADCAST_DETECTED_ACTIVITY));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);

    }
}