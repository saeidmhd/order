package com.mahak.order;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.adapter.PromotionAdapter;
import com.mahak.order.common.Promotion;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;

public class PromotionListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private EditText txtSearch;
    private PromotionAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DbAdapter db;
    private Context mContext;
    private ArrayList<Promotion> allPromotion = new ArrayList<>();
    private TextView tvPageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions_list);

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        tvPageTitle.setText(getString(R.string.str_nav_promotion_list));
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________

        mContext = this;
        initialise();

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        allPromotion = db.getAllPromotion();
        mAdapter = new PromotionAdapter(allPromotion, mContext);
        mRecyclerView.setAdapter(mAdapter);


        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (mAdapter != null)
                    mAdapter.getFilter().filter(s, new Filter.FilterListener() {
                        @Override
                        public void onFilterComplete(int count) {
                            tvPageTitle.setText(getString(R.string.str_nav_promotion_list) + "(" + count + ")");
                        }
                    });

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initialise() {
        db = new DbAdapter(mContext);
        db.open();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
    }


}
