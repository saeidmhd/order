package com.mahak.order;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.mahak.order.adapter.PromotionPagerAdapter;
import com.mahak.order.common.Promotion;
import com.mahak.order.common.PromotionDetail;
import com.mahak.order.common.PromotionEntity;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;

public class PromotionDetailActivity extends BaseActivity {
    private TextView tvPageTitle;
    private int[] tabIcons = {
            R.drawable.ic_promotions,
            R.drawable.ic_conditins,
            R.drawable.ic_base_info
    };

    private int[] navLabels = {
            R.string.str_promo_details,
            R.string.str_promo_terms,
            R.string.str_general_promo_spec

    };
    private Context mContext;

    private TabLayout tabLayout;
    private String codePromotion;
    private DbAdapter db;
    public static Promotion promotion;
    public static ArrayList<PromotionDetail> promotionDetail;
    public static ArrayList<PromotionEntity> entitiesOfPromotions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if (getIntent() != null)
            codePromotion = getIntent().getStringExtra("CodePromotion");
        db = new DbAdapter(mContext);

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        tvPageTitle.setText(getString(R.string.str_nav_promotion_list));
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________

        setContentView(R.layout.activity_promotion_detail);

        db.open();
        promotion = db.getPromotionByCode(codePromotion);
        promotionDetail = db.getPromotionDetails(codePromotion);
        entitiesOfPromotions = db.getEntitiesOfPromotions(promotion.getPromotionId());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PromotionPagerAdapter promotionPagerAdapter = new PromotionPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(promotionPagerAdapter);
        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
        setupTabIcons();

        viewPager.setCurrentItem(3);
    }

    private void setupTabIcons() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            ConstraintLayout tab = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.nav_tab, null);
            TextView tab_label = (TextView) tab.findViewById(R.id.lable_tab);
            ImageView tab_icon = (ImageView) tab.findViewById(R.id.icon_tab);
            tab_label.setText(getResources().getString(navLabels[i]));
            tab_icon.setImageResource(tabIcons[i]);
            tabLayout.getTabAt(i).setCustomView(tab);
        }
    }

}
