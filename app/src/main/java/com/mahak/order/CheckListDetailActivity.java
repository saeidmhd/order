package com.mahak.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class CheckListDetailActivity extends BaseActivity {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    //private CirclePageIndicator mIndicator;
    private Bundle Extras;
    private int PageNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list_detail);

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
        TextView tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        tvPageTitle.setText(getString(R.string.str_detail_checklist));
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________

        Extras = getIntent().getExtras();
        if (Extras != null) {
            PageNumber = Extras.getInt(POSITION_KEY);
        }
        initialise();

        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //mIndicator.setCurrentItem(position);
            }
        });

    }//end of onCreate

    /**
     * Initializing Variables
     */
    private void initialise() {
        mPager = (ViewPager) findViewById(R.id.pager);
        //mIndicator = findViewById(R.id.indicator);
        mPagerAdapter = new CheckListPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        // mIndicator.setViewPager(mPager);
        mPager.setCurrentItem(PageNumber);
        // mIndicator.setCurrentItem(PageNumber);
    }

    private class CheckListPagerAdapter extends FragmentStatePagerAdapter {
        CheckListPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return CheckListDetailFragment.create(position);
        }

        @Override
        public int getCount() {
            return DashboardActivity.arrayChecklist.size();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
