package com.mahak.order.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.MissionDetailMoreInfo;
import com.mahak.order.MissionListActivity;
import com.mahak.order.R;
import com.mahak.order.common.Customer;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.mission.MissionDetail;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;
import java.util.Date;

public class MissionDetailAdapter extends RecyclerView.Adapter<MissionDetailAdapter.ViewHolder> {

    private ArrayList<MissionDetail> missionDetails;
    private LayoutInflater mInflater;
    private MissionListActivity missionListActivity;
    private ArrayList<MissionDetail> arrayOriginal = new ArrayList<>();
    private Context mContext;
    private DbAdapter db;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView  customerName , customerAddress , missionDetailType , txtStatus , organization , rowOfMissionDetail;
        public ImageView missionDetailIcon , missionDetailStatusIcon ;
        public LinearLayout changeStatus , status ,changeStatusMenu , mission_item;
        public Button changeStatusBtn;
        public ImageButton more_info;

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
            mission_item = (LinearLayout) view.findViewById(R.id.mission_item);
            changeStatusBtn = (Button) view.findViewById(R.id.changeStatusBtn);
            more_info = (ImageButton) view.findViewById(R.id.more_info);
        }
    }

    public MissionDetailAdapter(ArrayList<MissionDetail> missionDetails, Context context) {
        this.missionDetails = missionDetails;
        this.mInflater = LayoutInflater.from(context);
        missionListActivity = (MissionListActivity) context;
        arrayOriginal.addAll(missionDetails);
        mContext = context;
        db = new DbAdapter(mContext);
        db.open();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = mInflater.inflate(R.layout.mission_detail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        int mPosition = holder.getAbsoluteAdapterPosition();

        MissionDetail missionDetail = missionDetails.get(mPosition);

        String type ="";
        Customer customer = db.getCustomerWithPersonId(missionDetail.getPersonId());
        holder.customerName.setText(customer.getName());
        if(customer.getOrganization() != null)
            holder.organization.setText( "(" + customer.getOrganization() + ")");
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
        holder.rowOfMissionDetail.setText(String.valueOf(mPosition + 1));

        holder.changeStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeStatusDialog(missionDetail , mPosition);
            }
        });
        holder.mission_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MissionDetailMoreInfo.class);
                intent.putExtra("missionDetailId",missionDetail.getMissionDetailId());
                mContext.startActivity(intent);
                //showChangeStatusDialog(missionDetail , mPosition);

            }
        });
        holder.more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MissionDetailMoreInfo.class);
                intent.putExtra("missionDetailId",missionDetail.getMissionDetailId());
                mContext.startActivity(intent);
                //showChangeStatusDialog(missionDetail , mPosition);

            }
        });
        holder.changeStatusMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showChangeStatusDialog(missionDetail , mPosition);
            }
        });
        holder.status.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showChangeStatusDialog(missionDetail , mPosition);
            }
        });

    }

    public void showChangeStatusDialog(MissionDetail missionDetail, int position) {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.mission_detail_dialog);

        LinearLayout not_started = dialog.findViewById(R.id.notـstarted);
        LinearLayout inWay = dialog.findViewById(R.id.inWay);
        LinearLayout unsuccessful = dialog.findViewById(R.id.unsuccessful);
        LinearLayout successful = dialog.findViewById(R.id.successful);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;


        not_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                missionDetail.setStatus(1);
                missionDetail.setDate(ServiceTools.getFormattedDateAndTime(new Date().getTime()));
                missionDetail.addObserver(missionListActivity);
                missionDetail.setChangeAndNotify();
                notifyItemChanged(position);
                db.AddMissionDetail(missionDetail);
                dialog.dismiss();
            }
        });
        inWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                missionDetail.setStatus(2);
                missionDetail.setDate(ServiceTools.getFormattedDateAndTime(new Date().getTime()));
                missionDetail.addObserver(missionListActivity);
                missionDetail.setChangeAndNotify();
                notifyItemChanged(position);
                db.AddMissionDetail(missionDetail);
                dialog.dismiss();
            }
        });
        successful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                missionDetail.setStatus(3);
                missionDetail.setDate(ServiceTools.getFormattedDateAndTime(new Date().getTime()));
                missionDetail.addObserver(missionListActivity);
                missionDetail.setChangeAndNotify();
                notifyItemChanged(position);
                db.AddMissionDetail(missionDetail);
                dialog.dismiss();
            }
        });
        unsuccessful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                missionDetail.setStatus(4);
                missionDetail.setDate(ServiceTools.getFormattedDateAndTime(new Date().getTime()));
                missionDetail.addObserver(missionListActivity);
                missionDetail.setChangeAndNotify();
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
