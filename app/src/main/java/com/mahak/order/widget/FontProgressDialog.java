package com.mahak.order.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mahak.order.BaseActivity;

public class FontProgressDialog extends ProgressDialog {

    public FontProgressDialog(Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = this.findViewById(android.R.id.message);
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(BaseActivity.font_yekan);
            }
        }
    }
}