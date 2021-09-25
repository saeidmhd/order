/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mahak.order;

import static com.mahak.order.BaseActivity.setPrefSignalUserToken;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mahak.order.apiHelper.ApiClient;
import com.mahak.order.apiHelper.ApiInterface;
import com.mahak.order.common.DeviceTokenModel.DeviceTokenBody;
import com.mahak.order.common.DeviceTokenModel.DeviceTokenResponse;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;


import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            // [START register_for_gcm]
            // Initially this getDataResultCall goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]

            /*InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);*/

            // [END get_token]
            Log.i(TAG, "FCM Registration Token: " + token);

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(getApplicationContext() , token);

            ServiceTools.setKeyInSharedPreferences(getApplicationContext(), ProjectInfo.pre_device_token, token);

            // Subscribe to topic channels
            subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     * <p>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */


    public void sendRegistrationToServer(Context context , String token) {
        DeviceTokenBody deviceTokenBody = new DeviceTokenBody();
        deviceTokenBody.setDeviceId(ServiceTools.getDeviceID(context));
        deviceTokenBody.setAppId(ProjectInfo.App_Id);
        deviceTokenBody.setDeviceToken(token);
        deviceTokenBody.setOs("android");
        deviceTokenBody.setOsVersion(String.valueOf(android.os.Build.VERSION.SDK_INT));
        deviceTokenBody.setAppVersion(String.valueOf(ServiceTools.getVersionCode(context)));
        deviceTokenBody.setTimezone(getTimeZone());
        deviceTokenBody.setMahakId(BaseActivity.getPrefMahakId());
        ApiInterface apiService = ApiClient.setDeviceTokenRetrofit().create(ApiInterface.class);
        Call<DeviceTokenResponse> call = apiService.SetDeviceToken(deviceTokenBody);
        call.enqueue(new Callback<DeviceTokenResponse>() {
            @Override
            public void onResponse(Call<DeviceTokenResponse> call, Response<DeviceTokenResponse> response) {
                if (response.body() != null) {
                    if (response.body().isSuccessed()) {
                    }else
                        Log.d("@TAG-Res", "error");
                }
            }
            @Override
            public void onFailure(Call<DeviceTokenResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        // Add custom implementation, as needed.
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
*/

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

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {

        FirebaseApp.initializeApp(this);
        for (String topic : TOPICS) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
        }
        /*GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }*/
    }
    // [END subscribe_topics]

}
