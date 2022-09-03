package com.mahak.order.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.R;
import com.mahak.order.common.Customer;
import com.mahak.order.mission.Mission;
import com.mahak.order.mission.MissionDetail;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;
import java.util.List;

public class MissionDetailAdapter extends RecyclerView.Adapter<MissionDetailAdapter.ViewHolder> {

    private ArrayList<MissionDetail> missionDetails;
    private Mission mission;
    private LayoutInflater mInflater;
    private ArrayList<MissionDetail> arrayOriginal = new ArrayList<>();
    private Context mContext;
    private DbAdapter db;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView  customerName , customerAddress , missionDetailType , txtStatus , organization , rowOfMissionDetail;
        public ImageView missionDetailIcon , missionDetailStatusIcon ;
        public LinearLayout changeStatus , status ,changeStatusMenu , mission_item;
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
            mission_item = (LinearLayout) view.findViewById(R.id.mission_item);
            changeStatusBtn = (Button) view.findViewById(R.id.changeStatusBtn);
        }
    }

    public MissionDetailAdapter(ArrayList<MissionDetail> missionDetails, Mission mission, Context context) {
        this.missionDetails = missionDetails;
        this.mission = mission;
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
        holder.mission_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeStatusDialog(missionDetail , position);

            }
        });
        holder.changeStatusMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                /*PopupMenu popup = new PopupMenu(mContext,holder.changeStatusMenu);
                try {
                    Field[] fields = popup.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popup);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon",boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                popup.inflate(R.menu.change_missiondetail_status);

                *//*MenuPopupHelper menuPopupHelper = new MenuPopupHelper(mContext, (MenuBuilder) popup.getMenu() , holder.changeStatusMenu);
                menuPopupHelper.setForceShowIcon(true);*//*

                *//*@SuppressLint("RestrictedApi") MenuPopupHelper menuHelper = new MenuPopupHelper(mContext, (MenuBuilder) popup.getMenu(), holder.changeStatusMenu);
                menuHelper.setForceShowIcon(true);*//*

                *//*popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        missionTitle.setText(menuItem.getTitle());
                        missionIndex = menuItem.getItemId();
                        missionDetails.clear();
                        missionDetails.addAll(db.getAllMissionDetailWithMissionId(missionIndex));
                        calcAndSetCheckListStat();
                        return false;
                    }
                });*//*

                popup.show();
                Menu menu = popup.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem mi = menu.getItem(i);
                    FontPopUp.applyFontToMenuItem(mi, mContext);
                }*/

                showChangeStatusDialog(missionDetail , position);

            }
        });

    }

    public void showChangeStatusDialog(MissionDetail missionDetail, int position) {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.mission_detail_dialog);

        LinearLayout notـstarted = dialog.findViewById(R.id.notـstarted);
        LinearLayout inWay = dialog.findViewById(R.id.inWay);
        LinearLayout unsuccessful = dialog.findViewById(R.id.unsuccessful);
        LinearLayout successful = dialog.findViewById(R.id.successful);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;


        notـstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                missionDetail.setStatus(1);
                notifyItemChanged(position);
                db.AddMissionDetail(missionDetail);
                setMissionStatus();
                dialog.dismiss();
            }
        });
        inWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                missionDetail.setStatus(2);
                notifyItemChanged(position);
                db.AddMissionDetail(missionDetail);
                setMissionStatus();
                dialog.dismiss();
            }
        });
        successful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                missionDetail.setStatus(3);
                notifyItemChanged(position);
                db.AddMissionDetail(missionDetail);
                setMissionStatus();
                dialog.dismiss();
            }
        });
        unsuccessful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                missionDetail.setStatus(4);
                notifyItemChanged(position);
                db.AddMissionDetail(missionDetail);
                setMissionStatus();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setMissionStatus() {
        for (MissionDetail missionDetail : missionDetails){
            switch (missionDetail.getStatus()){
                case 1:
                    // st = "شروع نشده";
                    mission.setStatus(1);
                    break;
                case 2:
                    // st = "در مسیر";
                    //st = "در جریان";
                    mission.setStatus(2);
                    break;
                case 3:
                case 4:
                    // st = "انجام شده ناموفق";
                    // st = "انجام شده موفق";
                    mission.setStatus(3);
                    break;
            }
        }
        db.AddMission(mission);
    }

    @Override
    public int getItemCount() {
        return missionDetails.size();
    }
}
