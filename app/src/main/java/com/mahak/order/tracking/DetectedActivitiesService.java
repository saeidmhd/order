package com.mahak.order.tracking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class DetectedActivitiesService extends Service {

    Intent intentService;
    ActivityRecognitionClient activityRecognitionClient;
    PendingIntent mPendingIntent;

    private final IBinder mBinder = new DetectedActivitiesService.LocalBinder();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        activityRecognitionClient  = new ActivityRecognitionClient(this);
        intentService = new Intent(this,ActivityDetectionIntentService.class);
        mPendingIntent = PendingIntent.getService(this,1,intentService,PendingIntent.FLAG_UPDATE_CURRENT);
        requestActivityUpdatesButtonHandler();
        Log.d("asdasd" , "asdasd");
    }

    private void requestActivityUpdatesButtonHandler() {
        @SuppressLint("MissingPermission") Task<Void> task = ActivityRecognition.getClient(getApplicationContext())
                .requestActivityUpdates(Constants.DETECTION_INTERVAL_IN_MILLISECONDS,
                        mPendingIntent);

        task.addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Toast.makeText(DetectedActivitiesService.this, "success add", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        task.addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(DetectedActivitiesService.this, "failure add", Toast.LENGTH_SHORT).show();
                    }

                }
        );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    public class LocalBinder extends Binder {
        public DetectedActivitiesService getService() {
            return DetectedActivitiesService.this;
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        removeActivityUpdatesButtonHandler();
    }

    private void removeActivityUpdatesButtonHandler() {
        @SuppressLint("MissingPermission") Task<Void> task = ActivityRecognition.getClient(getApplicationContext())
                .removeActivityUpdates(mPendingIntent);

        task.addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Toast.makeText(DetectedActivitiesService.this, "success remove", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        task.addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(DetectedActivitiesService.this, "failure remove", Toast.LENGTH_SHORT).show();
                    }

                }
        );

    }
}
