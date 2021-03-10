package com.mahak.order.szzt_pos;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.kishcore.sdk.sepehr.rahyab.api.DataCallback;
import com.mahak.order.R;

public class SelectWayDialog extends Dialog {

    private DataCallback dataCallback;

    public SelectWayDialog(@NonNull Context context, DataCallback dataCallback) {
        super(context);
        this.dataCallback = dataCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_payment_way);
        setCanceledOnTouchOutside(true);
        init();
    }

    private void init() {
        Button btnNormalPurchase = findViewById(R.id.btn_normal_purchase);
        Button btnBonPurchase = findViewById(R.id.btn_bon_purchase);

        btnNormalPurchase.setOnClickListener(arg0 -> {
            dataCallback.onDataInserted("normal");
            dismiss();
        });
        btnBonPurchase.setOnClickListener(arg0 -> {
            dataCallback.onDataInserted("bon");
            dismiss();
        });
    }
}
