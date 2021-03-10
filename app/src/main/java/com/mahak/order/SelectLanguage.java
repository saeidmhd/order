package com.mahak.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.mahak.order.common.SharedPreferencesHelper;
import com.yariksoffice.lingver.Lingver;

import java.util.Locale;

public class SelectLanguage extends BaseActivity {

    private RadioButton persian, deutsch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_language);

        persian = (RadioButton) findViewById(R.id.persian);
        deutsch = (RadioButton) findViewById(R.id.deutsch);

        persian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deutsch.setChecked(false);
            }
        });

        deutsch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                persian.setChecked(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void ok(View view) {

        if (deutsch.isChecked()) {
            Lingver.getInstance().setLocale(SelectLanguage.this, Locale.GERMANY);
            SharedPreferencesHelper.setCurrentLanguage(SelectLanguage.this, "de_DE");
        } else if (persian.isChecked()) {
            Lingver.getInstance().setLocale(SelectLanguage.this, "fa");
            SharedPreferencesHelper.setCurrentLanguage(SelectLanguage.this, "fa");
        }
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
