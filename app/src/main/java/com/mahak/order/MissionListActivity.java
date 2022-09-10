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


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.adapter.MissionDetailAdapter;
import com.mahak.order.mission.Mission;
import com.mahak.order.mission.MissionDetail;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.widget.FontPopUp;

import java.util.ArrayList;
import java.util.List;


public class MissionListActivity extends BaseActivity {

    private Context mContext;
    private ArrayList<Mission> missions;
    private Mission mission;
    private ArrayList<MissionDetail> missionDetails = new ArrayList<>();
    private DbAdapter db;
    private TextView tvPageTitle;
    private TextView description;
    private ImageView show_mission;
    private RelativeLayout llPageTitle;
    int printerBrand;
    private MissionDetailAdapter missionDetailAdapter;
    private RecyclerView missionDetailList;
    private RecyclerView.LayoutManager mLayoutManager;
    private Bundle Extras;
    private int missionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);

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

        missionIndex = missions.get(0).getMissionId();

        tvPageTitle.setText("شناسه ماموریت : " + missions.get(0).getMissionId());
        description.setText(missions.get(0).getDescription());

        Extras = getIntent().getExtras();

        if (Extras != null) {
            missionIndex = Extras.getInt("missionIndex");
            missionDetails.clear();
            for(Mission mMission : missions)
                if (mMission.getMissionId() == missionIndex)
                    mission = mMission;
            description.setText(mission.getDescription());
            missionDetails.addAll(db.getAllMissionDetailWithMissionId(missionIndex));
            tvPageTitle.setText("شناسه ماموریت : " + missionIndex);
            missionDetailAdapter = new MissionDetailAdapter(missionDetails , mission, mContext);
            missionDetailList.setAdapter(missionDetailAdapter);
        }

        missionDetailList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        missionDetailList.setLayoutManager(mLayoutManager);

        llPageTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(mContext,show_mission);

                for (Mission mission : missions) {
                    popup.getMenu().add(mission.getMissionId(),mission.getMissionId(),mission.getMissionId(), "شناسه ماموریت : " + mission.getMissionId());
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        tvPageTitle.setText(menuItem.getTitle());
                        int missionIndex = menuItem.getItemId();
                        for(Mission mMission : missions)
                            if (mMission.getMissionId() == missionIndex)
                                mission = mMission;
                        description.setText(mission.getDescription());
                        missionDetails.clear();
                        missionDetails.addAll(db.getAllMissionDetailWithMissionId(missionIndex));
                        missionDetailAdapter = new MissionDetailAdapter(missionDetails, mission, mContext);
                        missionDetailList.setAdapter(missionDetailAdapter);
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

    /**
     * Initializing Variables
     */
    private void initialise() {
        missionDetailList = (RecyclerView) findViewById(R.id.missionDetailList);
        description = (TextView) findViewById(R.id.description);
        db = new DbAdapter(mContext);
    }

    /**
     * Read Receipts From Database And Fill Adapter
     */
    private void FillView() {
        db.open();
        missions = db.getAllMission();
        mission = missions.get(0);
        missionDetails.addAll(db.getAllMissionDetailWithMissionId(missions.get(0).getMissionId()));
        missionDetailAdapter = new MissionDetailAdapter(missionDetails, mission, mContext);
        missionDetailList.setAdapter(missionDetailAdapter);
    }

    public MissionDetailAdapter getMissionDetailAdapter() {
        return missionDetailAdapter;
    }

    private void setMissionStatus(Mission mission, List<MissionDetail> missionDetails) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        /*Drawable exit = ContextCompat.getDrawable(mContext, R.drawable.arrow_drop_down);
        menu.add(0, 0, 0, R.string.str_exit_app)
                .setIcon(exit)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
        return super.onCreateOptionsMenu(menu);
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
}
