package com.mahak.order.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.R;

public class ProductHolder extends RecyclerView.ViewHolder {

    public TextView txtCount, txtTotalCount, tvPrice, tvAsset, tvAsset2, tvInbox, tvProductName, tvUnit, tvUnit2, txtTotalGift,txtTotalCount1,txtTotalCount2;
    public ImageView imgProduct , imgGift;
    public TextView tvName;
    public TextView tvProductCode;
    public TextView tvCustomerPrice;
    public LinearLayout panelCount , inboxLayout;
    public Button plus , minus , plus_count2 , minus_count2;
    public View panelTotalCount, panelTotalAsset;

    ProductHolder(View view) {
        super(view);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvName.setSelected(true);
        tvProductCode = (TextView) view.findViewById(R.id.tvProductCode);
        imgGift = (ImageView) view.findViewById(R.id.imgGift);
        panelCount = (LinearLayout) view.findViewById(R.id.panelCount);
        tvCustomerPrice = (TextView) view.findViewById(R.id.tvCustomerPrice);
        tvAsset = (TextView) view.findViewById(R.id.tvAsset);
        tvAsset2 = (TextView) view.findViewById(R.id.tvAsset2);
        tvInbox = (TextView) view.findViewById(R.id.tvInbox);
        imgProduct = (ImageView) view.findViewById(R.id.imgProduct);
        tvUnit = (TextView) view.findViewById(R.id.tvUnit);
        tvUnit2 = (TextView) view.findViewById(R.id.tvUnit2);
        txtCount = (TextView) view.findViewById(R.id.txtCount);
        tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        txtTotalCount = (TextView) view.findViewById(R.id.txtTotalCount);
        txtTotalGift = (TextView) view.findViewById(R.id.txtTotalGift);
        txtTotalCount2 = (TextView) view.findViewById(R.id.txtTotalCount2);
        txtTotalCount1 = (TextView) view.findViewById(R.id.txtTotalCount1);
        panelTotalAsset = view.findViewById(R.id.panelTotalAsset);
        panelTotalCount = view.findViewById(R.id.panelTotalCount);
        inboxLayout = view.findViewById(R.id.inboxLayout);

        plus = view.findViewById(R.id.plus);
        plus_count2 = view.findViewById(R.id.plus_count2);

        minus = view.findViewById(R.id.minus);
        minus_count2 = view.findViewById(R.id.minus_count2);
    }
}
