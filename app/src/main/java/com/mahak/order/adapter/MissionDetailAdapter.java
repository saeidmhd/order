package com.mahak.order.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.BuildConfig;
import com.mahak.order.PromotionDetailActivity;
import com.mahak.order.R;
import com.mahak.order.common.Customer;
import com.mahak.order.common.Promotion;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.mission.MissionDetail;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.storage.RadaraDb;

import java.util.ArrayList;

public class MissionDetailAdapter extends RecyclerView.Adapter<MissionDetailAdapter.ViewHolder> {

    private ArrayList<MissionDetail> missionDetails;
    private LayoutInflater mInflater;
    private ArrayList<MissionDetail> arrayOriginal = new ArrayList<>();
    private Context mContext;
    private DbAdapter db;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView  customerName , customerAddress , missionDetailType , txtStatus , organization , rowOfMissionDetail;
        public ImageView missionDetailIcon , missionDetailStatusIcon ;
        public LinearLayout changeStatus , status ,changeStatusMenu;
        public Button changeStatusBtn;

        ViewHolder(View view) {
            super(view);
            customerName = (TextView) view.findViewById(R.id.customerName);
            customerAddress = (TextView) view.findViewById(R.id.customerAddress);
            missionDetailType = (TextView) view.findViewById(R.id.missionDetailType);
            rowOfMissionDetail = (TextView) view.findViewById(R.id.rowOfMissionDetail);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            missionDetailIcon = (ImageView) view.findViewById(R.id.missionDetailIcon);
            changeStatusMenu = (LinearLayout) view.findViewById(R.id.changeStatusMenu);
            missionDetailStatusIcon = (ImageView) view.findViewById(R.id.missionDetailStatusIcon);
            organization = (TextView) view.findViewById(R.id.organization);
            changeStatus = (LinearLayout) view.findViewById(R.id.changeStatus);
            status = (LinearLayout) view.findViewById(R.id.status);
            changeStatusBtn = (Button) view.findViewById(R.id.changeStatusBtn);
        }
    }

    public MissionDetailAdapter(ArrayList<MissionDetail> missionDetails, Context context) {
        this.missionDetails = missionDetails;
        this.mInflater = LayoutInflater.from(context);
        arrayOriginal.addAll(missionDetails);
        mContext = context;
        db = new DbAdapter(mContext);
        db.open();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = mInflater.inflate(R.layout.mission_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        MissionDetail  missionDetail = missionDetails.get(position);

        String type ="";
        Customer customer = db.getCustomerWithPersonId(missionDetail.getPersonId());
        holder.customerName.setText(customer.getName());
        holder.organization.setText(customer.getOrganization());
        switch (missionDetail.getType()){
            case 1 :
                type = "دریافت سفارش";
                holder.missionDetailIcon.setImageResource(R.drawable.ic_getorder_ico);
                break;
            case 2:
                type = "تحویل کالا";
                holder.missionDetailIcon.setImageResource(R.drawable.ic_delivergoods_ico);
                break;
            case 3:
                type = "وصول مطالبات";
                holder.missionDetailIcon.setImageResource(R.drawable.ic_recieve_ico);
                break;
            case 4:
                type = "برداشت کالا";
                holder.missionDetailIcon.setImageResource(R.drawable.ic_getgoods_ico);
                break;
        }

        int status = missionDetail.getStatus();
        String st = "";

        switch (status){
            case 1:
                st = "شروع نشده";
                holder.missionDetailStatusIcon.setImageResource(R.drawable.ic_notstarted_ico);
                holder.changeStatus.setVisibility(View.VISIBLE);
                holder.status.setVisibility(View.GONE);
                break;
            case 2:
                st = "در مسیر";
                holder.missionDetailStatusIcon.setImageResource(R.drawable.ic_ontheway_ico);
                holder.changeStatus.setVisibility(View.GONE);
                holder.status.setVisibility(View.VISIBLE);
                break;
            case 3:
                st = "انجام شده موفق";
                holder.missionDetailStatusIcon.setImageResource(R.drawable.ic_done_successful_ico);
                holder.changeStatus.setVisibility(View.GONE);
                holder.status.setVisibility(View.VISIBLE);
                break;
            case 4:
                st = "انجام شده ناموفق";
                holder.missionDetailStatusIcon.setImageResource(R.drawable.ic_done_unsuccessful_ico);
                holder.changeStatus.setVisibility(View.GONE);
                holder.status.setVisibility(View.VISIBLE);
                break;
        }

        holder.txtStatus.setText(st);
        holder.missionDetailType.setText(type);
        holder.customerAddress.setText(customer.getAddress());
        holder.rowOfMissionDetail.setText(String.valueOf(position + 1));

        holder.changeStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeStatusDialog(missionDetail , position);

            }
        });
        holder.changeStatusMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeStatusDialog(missionDetail , position);

            }
        });

    }

    public void showChangeStatusDialog(MissionDetail missionDetail, int position) {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.checklist_status_dialog);

        Button dialog_btn_data = dialog.findViewById(R.id.dialog_btn_ok);
        RadioButton inWay = dialog.findViewById(R.id.inWay);
        RadioButton unsuccessful = dialog.findViewById(R.id.unsuccessful);
        RadioButton successful = dialog.findViewById(R.id.successful);
        RadioGroup checkListGroup = dialog.findViewById(R.id.checkListGroup);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        switch (missionDetail.getStatus()){
            case 2:
                inWay.setChecked(true);
                break;
            case 3:
                successful.setChecked(true);
                break;
            case 4:
                unsuccessful.setChecked(true);
                break;
        }

        inWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                missionDetail.setStatus(2);
                notifyItemChanged(position);
                db.AddMissionDetail(missionDetail);
                dialog.dismiss();
            }
        });
        successful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                missionDetail.setStatus(3);
                notifyItemChanged(position);
                db.AddMissionDetail(missionDetail);
                dialog.dismiss();
            }
        });
        unsuccessful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                missionDetail.setStatus(4);
                notifyItemChanged(position);
                db.AddMissionDetail(missionDetail);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return missionDetails.size();
    }
}
