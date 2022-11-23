package com.mahak.order;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.service.quicksettings.Tile;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.common.Notification;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.storage.RadaraDb;


public class NotificationDialogActivity extends BaseActivity {

    private TextView tvTitle, tvMessage;
    private Button btnCancel, btnOK;
    private Bundle bundle;
    private Intent intent;
    private long NotifiId;
    private RadaraDb db;
    String title,message;
    Context mContext;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dialog_notification);
        mContext = this;

        initialise();

        bundle = this.getIntent().getExtras();
        if (bundle != null) {
            NotifiId = bundle.getLong("Id");
            title = bundle.getString("title");
            message = bundle.getString("message");
        }
        /////////////////////////////////////////////////////////
        ////////////////////////////////////////////////
        tvMessage.setText(message);
        tvTitle.setText(title);
        if (bundle.getString("url") == null)
            btnOK.setVisibility(View.GONE);
        else
            btnOK.setVisibility(View.VISIBLE);
        ////////////////////////////////////////////////
        //Update read////////////////////////////////////
        db.open();
        Notification notification = db.GetNotification(NotifiId);
        if (notification != null) {
            notification.setRead(true);
            db.UpdateNotification(notification);
        }
        db.close();
        //////////////////////////////////////////////////
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent("android.intent.action.VIEW");
                try {
                    intent.setData(Uri.parse(bundle.getString("url")));
                    startActivity(intent);
                    finish();
                } catch (ActivityNotFoundException e) {
                    ServiceTools.logToFireBase(e);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initialise() {
        tvTitle = (TextView) findViewById(R.id.alertTitle);
        tvMessage = (TextView) findViewById(R.id.message);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnOK = (Button) findViewById(R.id.btnOK);

        db = new RadaraDb(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }
}
