package com.mahak.order.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.mahak.order.common.Notification;
import com.mahak.order.storage.DbAdapter;

public class NotificationService extends IntentService {

    public NotificationService() {
        super("NotificationService");
        // TODO Auto-generated constructor stub
    }

    public NotificationService(String name) {
        super("NotificationService");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle bundle = intent.getExtras();
        long Id = bundle.getLong("Id");
        /////////////////////////////////////////////////////
        //Update For read Notification///////////////////////
        DbAdapter db = new DbAdapter(getApplicationContext());
        db.open();
        Notification notification = db.GetNotification(Id);
        if (notification != null) {
            notification.setRead(true);
            db.UpdateNotification(notification);
        }
        db.close();
    }

}
