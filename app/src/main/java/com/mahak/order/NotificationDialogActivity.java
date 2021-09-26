package com.mahak.order;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.common.Notification;
import com.mahak.order.storage.DbAdapter;


public class NotificationDialogActivity extends Activity {

    private TextView tvTitle, tvMessage;
    private Button btnCancel, btnOK;
    private Bundle bundle;
    private Intent intent;
    private long NotifiId;
    private DbAdapter db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(android.R.style.Theme_Holo_Light_Dialog);
        setContentView(R.layout.activity_dialog_notification);

        initialise();

        bundle = this.getIntent().getExtras();
        if (bundle != null) {
            NotifiId = bundle.getLong("Id");
        }
        /////////////////////////////////////////////////////////
        ////////////////////////////////////////////////
        setTitle(bundle.getString("title"));
        tvMessage.setText(bundle.getString("message"));
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
                } catch (ActivityNotFoundException localActivityNotFoundException) {
                    FirebaseCrashlytics.getInstance().recordException(localActivityNotFoundException);
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

        db = new DbAdapter(getApplicationContext());
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
