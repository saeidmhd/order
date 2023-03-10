package com.mahak.order;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.mahak.order.adapter.MissionDetailAdapter;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.mission.Mission;
import com.mahak.order.mission.MissionDetail;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.widget.FontPopUp;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class MissionListActivity extends BaseActivity implements Observer {

    private Context mContext;
    private ArrayList<Mission> missions;
    private Mission mission;
    private static ArrayList<MissionDetail> missionDetails = new ArrayList<>();
    private DbAdapter db;

    private TextView tvPageTitle;
    private TextView description;

    private static TextView missionDetailStat;

    private static TextView successCount;
    private static TextView failCount;
    private static TextView unDo;
    private static CircularProgressIndicator successProgressBar;
    private static CircularProgressIndicator failProgressBar;

    private TextView getGoodsCountTxT;
    private TextView getOrderCountTxT;
    private TextView getReceiveCountTxT;
    private TextView getDeliverGoodsCountTxT;



    private ImageView show_mission;
    private RelativeLayout llPageTitle;
    private MissionDetailAdapter missionDetailAdapter;
    private RecyclerView missionDetailList;
    private RecyclerView.LayoutManager mLayoutManager;
    private Bundle Extras;
    private int missionIndex;

    static double successMissionDetailCount = 0 ;
    static double failMissionDetailCount = 0;
    double missionDetailCount = 0;

    private int getOrderCount;
    private int delivergoodsCount;
    private int recieveCount;
    private int getgoodsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_list_activity);

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.mission_title, null);
        tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        show_mission = (ImageView) view.findViewById(R.id.show_mission);
        llPageTitle = (RelativeLayout) view.findViewById(R.id.llPageTitle);
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________

        mContext = this;

        initialise();
        FillView();

        missionDetailList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        missionDetailList.setLayoutManager(mLayoutManager);
        missionDetailList.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        Extras = getIntent().getExtras();
        if (Extras != null) {
            missionIndex = Extras.getInt("missionIndex");
            refreshList();
        }

        llPageTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context wrapper = new ContextThemeWrapper(mContext, R.style.MyPopupMenu);
                PopupMenu popup = new PopupMenu(wrapper,show_mission);
                for (int i = 0; i < missions.size(); i++) {
                    Mission mission = missions.get(i);
                    popup.getMenu().add(i,mission.getMissionId(),mission.getMissionId(),    "?????????? ?????????????? :" + mission.getMissionId() + "  |  "  + ServiceTools.getPersianDate(mission.getDate()));
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        tvPageTitle.setText(menuItem.getTitle());
                        missionIndex = menuItem.getItemId();
                        refreshList();
                        return false;
                    }
                });
                popup.show();
                Menu menu = popup.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem mi = menu.getItem(i);
                    FontPopUp.applyFontToMenuItem(mi, mContext);
                }
            }
        });
    }//end of onCreate

    private void refreshList() {
        missionDetails.clear();
        for(Mission mMission : missions)
            if (mMission.getMissionId() == missionIndex)
                mission = mMission;
        description.setText(mission.getDescription());
        missionDetails.addAll(db.getAllMissionDetailWithMissionId(missionIndex));
        missionDetailAdapter = new MissionDetailAdapter(missionDetails , mContext);
        missionDetailList.setAdapter(missionDetailAdapter);
        setMissionStatus();
        calcAndSetCheckListStat();
        tvPageTitle.setText("?????????? ?????????????? :" + mission.getMissionId() + "  |  "  + ServiceTools.getPersianDate(mission.getDate()));
    }

    /**
     * Initializing Variables
     */
    private void initialise() {

        missionDetailList = (RecyclerView) findViewById(R.id.missionDetailList);
        description = (TextView) findViewById(R.id.description);

        missionDetailStat = (TextView) findViewById(R.id.missionDetailStat);

        successCount = (TextView) findViewById(R.id.successCount);
        failCount = (TextView) findViewById(R.id.failCount);
        unDo = (TextView) findViewById(R.id.unDo);
        successProgressBar = (CircularProgressIndicator) findViewById(R.id.successProgressBar);
        failProgressBar = (CircularProgressIndicator) findViewById(R.id.failProgressBar);

        getGoodsCountTxT = (TextView) findViewById(R.id.getGoodsCountTxT);
        getOrderCountTxT = (TextView) findViewById(R.id.getOrderCountTxT);
        getReceiveCountTxT = (TextView) findViewById(R.id.getReceiveCountTxT);
        getDeliverGoodsCountTxT = (TextView) findViewById(R.id.getDeliverGoodsCountTxT);



        db = new DbAdapter(mContext);
    }

    /**
     * Read Receipts From Database And Fill Adapter
     */
    private void FillView() {
        db.open();
        missions = db.getAllMission();
        mission = missions.get(0);
        missionDetails.addAll(db.getAllMissionDetailWithMissionId(mission.getMissionId()));
        missionDetailAdapter = new MissionDetailAdapter(missionDetails, mContext);
        missionDetailList.setAdapter(missionDetailAdapter);
    }

    private void setMissionStatus() {

        successMissionDetailCount = 0;
        failMissionDetailCount = 0;
        double missionDetailCount = missionDetails.size();
        for (MissionDetail missionDetail : missionDetails){
            switch (missionDetail.getStatus()){
                case 3:
                    successMissionDetailCount++;
                    break;
                case 4:
                    failMissionDetailCount++;
                    break;
            }
        }

        double sumDone = successMissionDetailCount + failMissionDetailCount;
        double undo = missionDetailCount - sumDone;
        double successRatio = 70 * ((successMissionDetailCount) / missionDetailCount);
        double failRatio = 70 * ((failMissionDetailCount + successMissionDetailCount) / missionDetailCount);

        successCount.setText((int)successMissionDetailCount + " ????????");
        failCount.setText((int)failMissionDetailCount + " ????????????");
        unDo.setText((int)undo + " ?????????? ????????");

        missionDetailStat.setText( "" + (int)missionDetailCount);
        successProgressBar.setProgress((int) successRatio);
        failProgressBar.setProgress((int) failRatio);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finishing();
        }
        return true;
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

    private void calcAndSetCheckListStat() {
        calcNumberOfChecklist();
        setCountToUi();
    }

    private void setCountToUi() {
        getGoodsCountTxT.setText(String.valueOf(getgoodsCount));
        getOrderCountTxT.setText(String.valueOf(getOrderCount));
        getReceiveCountTxT.setText(String.valueOf(recieveCount));
        getDeliverGoodsCountTxT.setText(String.valueOf(delivergoodsCount));
    }
    private void calcNumberOfChecklist() {
        getOrderCount = delivergoodsCount = recieveCount = getgoodsCount = 0;
        for (MissionDetail missionDetail : missionDetails){
            switch (missionDetail.getType()){
                case 1 :
                    getOrderCount++;
                    break;
                case 2:
                    delivergoodsCount++;
                    break;
                case 3:
                    recieveCount++;
                    break;
                case 4:
                    getgoodsCount++;
                    break;
            }
        }

    }

    @Override
    public void update(Observable observable, Object o) {
        setMissionStatus();
    }

}
