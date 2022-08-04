package com.mahak.order;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mahak.order.common.Customer;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.SharedPreferencesHelper;
import com.mahak.order.mission.Mission;
import com.mahak.order.mission.MissionDetail;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.storage.RadaraDb;
import com.mahak.order.widget.DrawableClickListener;
import com.mahak.order.widget.FontCheckBox;
import com.mahak.order.widget.FontEditText;

import java.util.ArrayList;
import java.util.List;


public class MissionListActivity extends BaseActivity {

    private static final int MENU_SORT = 1;
    private static final int MENU_ADD = 2;
    private Context mContext;
    private Activity mActivty;
    private ExpandableListView ExpandList;
    private ArrayList<Mission> missions;
    private List<MissionDetail> missionDetails;
    private DbAdapter db;
    private ExpandListAdapter expandlistAdapter;
    private FontEditText txtSearch;
    private long lngDate;
    private TextView tvPageTitle;
    int printerBrand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_list);

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
        tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________

        mContext = this;
        mActivty = this;

        printerBrand = SharedPreferencesHelper.getPrefPrinterBrand(mContext);

        initialise();
        FillView();

        txtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence query, int arg1, int arg2, int arg3) {
                txtSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cancel_search, 0, R.drawable.ic_search_set_nav, 0);
                //expandlistAdapter.filterData(query.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

        txtSearch.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case LEFT:
                        txtSearch.setText("");
                        txtSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search_set_nav,0 );
                        break;
                    default:
                        break;
                }
            }
        });

    }//end of onCreate

    /**
     * Initializing Variables
     */
    private void initialise() {
        ExpandList = (ExpandableListView) findViewById(R.id.explistReceipt);
        txtSearch = (FontEditText) findViewById(R.id.txtSearch);
        db = new DbAdapter(mContext);
        tvPageTitle.setText(getString(R.string.str_nav_receipt_list) + "(" + ExpandList.getCount() + ")");
    }

    /**
     * Read Receipts From Database And Fill Adapter
     */
    private void FillView() {
        db.open();
        Customer customer;

        missions = db.getAllMission();
        for (Mission mission : missions) {
            missionDetails = db.getAllMissionDetailWithMissionId(mission.getMissionId());
            mission.setMissionDetails(missionDetails);
        }
        expandlistAdapter = new ExpandListAdapter(mContext, missions);
        ExpandList.setAdapter(expandlistAdapter);
        tvPageTitle.setText("لیست ماموریت ها" + "(" + ExpandList.getCount() + ")");
    }

    public class ExpandListAdapter extends BaseExpandableListAdapter {

        private Context context;
        private List<Mission> missionList;
        private List<Mission> originallist;


        private class HolderChild {
            public TextView tvDate, tvChequeType, tvNumber, tvAmount ,txtStatus, tvBank, tvDescription;
            public FontCheckBox checkListDone;

            HolderChild(View view) {
                tvDate = (TextView) view.findViewById(R.id.tvDate);
                tvNumber = (TextView) view.findViewById(R.id.tvNumber);
                tvAmount = (TextView) view.findViewById(R.id.txtAmount);
                txtStatus = (TextView) view.findViewById(R.id.txtStatus);
                tvBank = (TextView) view.findViewById(R.id.tvBank);
                tvChequeType = (TextView) view.findViewById(R.id.tvChequeType);
                tvDescription = (TextView) view.findViewById(R.id.tvDescription);
                checkListDone = (FontCheckBox) view.findViewById(R.id.checkListDone);
            }

            public void Populate(MissionDetail missionDetail) {
                String type ="";
                Customer customer = db.getCustomerWithPersonId(missionDetail.getPersonId());
                tvNumber.setText(customer.getName());
                tvDate.setText(missionDetail.getDate());
                tvDescription.setText(missionDetail.getDescription());
                switch (missionDetail.getType()){
                    case 1 :
                        type = "دریافت سفارش";
                        break;
                    case 2:
                        type = "تحویل کالا";
                        break;
                    case 3:
                        type = "وصول مطالبات";
                        break;
                    case 4:
                        type = "برداشت کالا";
                        break;
                }

                int status = missionDetail.getStatus();
                String st = "";

                switch (status){
                    case 1:
                        st = "انجام نشده";
                        checkListDone.setChecked(false);
                        break;
                    case 2:
                        st = "شروع شده در راه";
                        break;
                    case 3:
                        st = "لغو شده توسط مدیر";
                        break;
                    case 4:
                        checkListDone.setChecked(true);
                        st = "انجام شده";
                        break;
                }

                txtStatus.setText(st);
                tvAmount.setText(type);
                tvBank.setText(customer.getAddress());
                tvChequeType.setText(customer.getMobile());

                checkListDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(!b)
                            missionDetail.setStatus(1);
                        else
                            missionDetail.setStatus(4);
                        notifyDataSetChanged();
                        db.AddMissionDetail(missionDetail);
                    }
                });
            }
        }

        private class HolderGroup {
            public TextView tvDate, tvMissionStatus, tvNumberOfCheckLists, tvDescription, tvCode;
            ImageView imgExpand, imgSync;

            HolderGroup(View view) {
                tvMissionStatus = (TextView) view.findViewById(R.id.tvMissionStatus);
                tvDate = (TextView) view.findViewById(R.id.tvDate);
                tvDescription = (TextView) view.findViewById(R.id.tvDescription);
                tvCode = (TextView) view.findViewById(R.id.tvCode);
                imgExpand = (ImageView) view.findViewById(R.id.imgExpandedList);
                tvNumberOfCheckLists = (TextView) view.findViewById(R.id.tvNumberOfCheckLists);
                imgSync = (ImageView) view.findViewById(R.id.imgSync);

            }

            public void Populate(final Mission mission, boolean isExpand, final int position) {
                int status = mission.getStatus();
                String st = "";

                switch (status){
                    case 1:
                        st = "انجام نشده";
                        break;
                    case 2:
                        st = "شروع شده در راه";
                        break;
                    case 3:
                        st = "انجام شده";
                        break;
                }

                long format_date =  ServiceTools.getDate(mission.getDate());
                String date = ServiceTools.getDateAndTimeForLong(format_date);

                tvMissionStatus.setText(st);
                /*tvNumberOfCheckLists.setText(String.valueOf(mission.getMissionDetailCount()));
                tvCode.setText(String.valueOf(mission.getAccountId()));*/
                tvDate.setText(date);
                tvDescription.setText(mission.getDescription());

                //______________________________________________________________________________
                if (isExpand)
                    imgExpand.setImageResource(R.drawable.ic_expand_down);
                else
                    imgExpand.setImageResource(R.drawable.ic_expand_up);

                if (mission.getMissionDetails().size() == 0)
                    imgExpand.setVisibility(View.INVISIBLE);
                else
                    imgExpand.setVisibility(View.VISIBLE);


            }
        }


        public ExpandListAdapter(Context context, List<Mission> groups) {
            this.context = context;
            this.missionList = new ArrayList<Mission>();
            this.originallist = new ArrayList<Mission>();

            this.missionList.addAll(groups);
            this.originallist.addAll(groups);
        }


        public Object getChild(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            List<MissionDetail> chList = missionList.get(groupPosition).getMissionDetails();
            return chList.get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return childPosition;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {

            MissionDetail child = (MissionDetail) getChild(groupPosition, childPosition);
            HolderChild holderchild = null;
            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.expandlist_child_mission, null);
                holderchild = new HolderChild(view);
                view.setTag(holderchild);
            } else
                holderchild = (HolderChild) view.getTag();

            holderchild.Populate(child);
            // TODO Auto-generated method stub
            return view;
        }

        public int getChildrenCount(int groupPosition) {
            // TODO Auto-generated method stub
            List<MissionDetail> chList = missionList.get(groupPosition).getMissionDetails();

            return chList.size();

        }

        public Object getGroup(int groupPosition) {
            // TODO Auto-generated method stub
            return missionList.get(groupPosition);
        }

        public int getGroupCount() {
            // TODO Auto-generated method stub
            return missionList.size();
        }

        public long getGroupId(int groupPosition) {
            // TODO Auto-generated method stub
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {

            Mission mission = (Mission) getGroup(groupPosition);
            HolderGroup holdergroup = null;
            if (view == null) {
                LayoutInflater inf = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                view = inf.inflate(R.layout.expandlist_group_mission, null);
                holdergroup = new HolderGroup(view);
                view.setTag(holdergroup);
            } else {
                holdergroup = (HolderGroup) view.getTag();
            }
            holdergroup.Populate(mission, isExpanded, groupPosition);

            return view;
        }

        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return true;
        }

        public boolean isChildSelectable(int arg0, int arg1) {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public void onGroupExpanded(int groupPosition) {
            // TODO Auto-generated method stub
            super.onGroupExpanded(groupPosition);
        }


    }



    public ExpandListAdapter getExpandlistAdapter() {
        return expandlistAdapter;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finishing();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finishing();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    public void finishing(){
        Intent intent2 = new Intent(getApplicationContext(), DashboardActivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent2);
    }
}
