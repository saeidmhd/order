package com.mahak.order.widget;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mahak.order.R;

public class FontDialog {
    private View dialog;

    public AlertDialog CustomeDialog(Context mContext, String string) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (layoutInflater != null) {
            dialog = layoutInflater.inflate(R.layout.alert_dialog, null);
        }
        getMessage().setText(string);
        return builder.setView(getDialog()).create();
    }

    public Button getPositive() {
        return (Button) dialog.findViewById(R.id.positive);
    }

    public Button getNegative() {
        return (Button) dialog.findViewById(R.id.negative);
    }

    public View getDialog() {
        return dialog;
    }

    public TextView getMessage() {
        return (TextView) dialog.findViewById(R.id.message);
    }
}
