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

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.common.Notification;
import com.mahak.order.service.NotificationService;
import com.mahak.order.storage.DbAdapter;

import java.util.Date;
import java.util.Random;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     * For Set of keys use data.keySet().
     */

    public static View.OnClickListener receiveMessag = null;

    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }
//        sendNotification(message);
        readMessage(data);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    private void readMessage(Bundle bundle) {
        if (!bundle.containsKey("message"))
            return;
        String title = bundle.getString("title");
        String message = bundle.getString("message");
        String fullMessage = bundle.getString("fullmessage");
        String type = bundle.getString("type");
        String data = bundle.getString("data");
        String sound = "";
        if (bundle.containsKey("soundName")) {
            sound = bundle.getString("soundName");
            sound = sound.substring(0, sound.lastIndexOf("."));
        } else {
            sound = "";
        }
        //Save Notification In db////////////////////////////////////
        if (type != null) {
            long userId = BaseActivity.getPrefUserMasterId(getApplicationContext());
            Notification noti = new Notification();
            noti.setTitle(title);
            noti.setMessage(message);
            noti.setFullMessage(fullMessage);
            noti.setType(type);
            noti.setData(data);
            noti.setRead(false);
            noti.setUserId(userId);
            noti.setDate(new Date().getTime());

            DbAdapter db = new DbAdapter(this);
            db.open();
            long notificationId = db.AddNotification(noti);
            int count = db.getCountNotification(String.valueOf(userId));
            if (count > 99) {
                Long Id = db.getMinNotificationId();
                db.DeleteNotification(Id);
            }

            db.close();
            //Send Notification///////////////////////////////////////////
            sendNotification(title, message, fullMessage, type, data, notificationId, sound);
        }

    }

    private void sendNotification(String title, String message, String fullMessage, String type, String data, long id, String sound) {
//        Intent intent = new Intent(this, getApplicationContext().getClass());
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ic_launcher)
//                .setContentTitle(getString(R.string.str_notification_title))
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        int notify_id = new Random().nextInt(9999);
//        notificationManager.notify(notify_id /* ID of notification */, notificationBuilder.build());


        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, SplashActivity.class), 0);

        //int Sound = R.raw.news;
        if (type != null) {
            if (type.equals("main")) {
                Random rand = new Random();
                int INTENT_ID = rand.nextInt(9999);
                Intent intent = new Intent(this, SplashActivity.class);
                intent.putExtra("Id", id);
                contentIntent = PendingIntent.getActivity(this, INTENT_ID, intent, 0);
            } else if (type.equals("web")) {
                Random rand = new Random();
                int INTENT_ID = rand.nextInt(9999);
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", data);
                intent.putExtra("Id", id);
                contentIntent = PendingIntent.getActivity(this, INTENT_ID, intent, 0);
            } else if (type.equals("dialog")) {
                Intent intent = new Intent(this, NotificationDialogActivity.class);
                intent.putExtra("url", data);
                intent.putExtra("title", title);
                intent.putExtra("message", fullMessage);
                intent.putExtra("Id", id);
                Random rand = new Random();
                int INTENT_ID = rand.nextInt(9999);
                contentIntent = PendingIntent.getActivity(this, INTENT_ID, intent, 0);
            } else if (type.equals("empty")) {
                Intent intent = new Intent(this, NotificationService.class);
                intent.putExtra("Id", id);
                Random rand = new Random();
                int INTENT_ID = rand.nextInt(9999);
                contentIntent = PendingIntent.getService(this, INTENT_ID, intent, 0);
            }

            Uri DefaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            boolean ResSound = false;
            int resId = 0;
            try {
                resId = getResources().getIdentifier(sound, "raw", getPackageName());
                if (resId != 0)
                    ResSound = true;
                else if (resId == 0)
                    ResSound = false;
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                ResSound = false;
            }

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(fullMessage))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setLights(0xFFFF9900, 400, 2000)
                    .setVibrate(new long[]{100, 100, 200, 300});
            if (ResSound)
                mBuilder.setSound(Uri.parse("android.resource://" + this.getPackageName() + "/" + resId));
            else if (!ResSound)
                mBuilder.setSound(DefaultSound);

            Random r = new Random();
            int NOTIFICATION_ID = r.nextInt(9999);

            mBuilder.setContentIntent(contentIntent);

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

            if (receiveMessag != null) {
                receiveMessag.onClick(null);
            }
        }

    }
}
