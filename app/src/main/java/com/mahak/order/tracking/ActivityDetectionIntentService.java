package com.mahak.order.tracking;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

public class ActivityDetectionIntentService extends IntentService {
    private static final String TAG = "ActRecoResult";

    public ActivityDetectionIntentService() {
        super(ActivityDetectionIntentService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            List<DetectedActivity> detectedActivities = null;
            if (result != null) {
                detectedActivities = result.getProbableActivities();
                Log.d(TAG, "Detected activities:");
                for (DetectedActivity da: detectedActivities) {
                    Log.d(TAG, (da.getType()) + " (" + da.getConfidence() + "%)");
                    broadcastActivity(da);
                }
            }
        }
    }

    private void broadcastActivity(DetectedActivity da) {
        Intent intent = new Intent(Constants.BROADCAST_DETECTED_ACTIVITY);
        intent.putExtra("type", da.getType());
        intent.putExtra("confidence", da.getConfidence());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


}
