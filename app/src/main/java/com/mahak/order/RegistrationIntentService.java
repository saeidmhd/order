
package com.mahak.order;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.webService.RequestSender;
import com.mahak.order.webService.RunInternetService;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String token = "";
        if (intent != null) {
            Bundle bundle =  intent.getExtras();
            if (bundle != null) {
                token = bundle.getString("token");
            }
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            sendRegistrationToServer(token);
            ServiceTools.setKeyInSharedPreferences(getApplicationContext(), ProjectInfo.pre_device_token, token);
            subscribeTopics(token);
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.d(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(String token) {
        Log.d("@TAG", token);
        String url = "http://service.mahaksoft.com/mahakservice.asmx";
        String methodName = "SetDeviceToken";
        HashMap<String, Object> params = new HashMap<>();
        params.put("AppSign", ProjectInfo.APPSIGN);
        params.put("Data", registerRequestPackage(getApplication(), token));
        RequestSender.ResponseReceiver receiver = new RequestSender.ResponseReceiver() {
            @Override
            public void gotResponse(Object sender, StringBuffer result, StringBuffer cookies) {
                if (result != null)
                    Log.d("@TAG-Res", result.toString());
            }
        };
        new RunInternetService().Do(getApplicationContext(), "", url, methodName, params, receiver, null, 14000);
    }


    public String registerRequestPackage(Context ctx, String Token) {
        String rPackage = "{\"deviceId\":\"" + ServiceTools.getDeviceID(ctx) + "\",\"deviceToken\":\"" + Token + "\",\"os\":\"android\",\"osVersion\":\"" + android.os.Build.VERSION.SDK_INT + "\",\"appId\":" + ProjectInfo.App_Id + ",\"appVersion\":\"" + ServiceTools.getVersionCode(ctx) + "\",\"timeZone\":\"" + getTimeZone()+ "\",\"mahakId\":\"" + BaseActivity.getPrefMahakId() + "\"}";
        return rPackage;
    }

    public static String getTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = Calendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());

        String offset = String.format(Locale.US, "%d:%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
        offset = (offsetInMillis >= 0 ? "GMT+" : "GMT-") + offset;

        return offset;
    }

    private void subscribeTopics(String token) throws IOException {

        FirebaseApp.initializeApp(this);
        for (String topic : TOPICS) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
        }
    }

}
