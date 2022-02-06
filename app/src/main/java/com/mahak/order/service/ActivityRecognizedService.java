package com.mahak.order.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

public class ActivityRecognizedService extends IntentService {
    private static final String TAG = ActivityRecognizedService.class.getSimpleName();

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            if (result != null) {
                handleDetectedActivities( result.getProbableActivities() );
            }
        }
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        for( DetectedActivity activity : probableActivities ) {
            switch( activity.getType() ) {
                case DetectedActivity.IN_VEHICLE: {
                    Log.e( TAG, "In Vehicle: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    Log.e( TAG, "On Bicycle: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.RUNNING: {
                    Log.e( TAG, "Running: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.STILL: {
                    Log.e( TAG, "Still: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.WALKING: {
                    Log.e( TAG, "Walking: " + activity.getConfidence() );
                    break;
                }
            }
        }
    }
}
