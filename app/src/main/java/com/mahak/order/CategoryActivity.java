package com.mahak.order;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mahak.order.adapter.CategoryAdapter;
import com.mahak.order.common.Category;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;

public class CategoryActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private ImageView back;
    private ImageView menu;
    private ImageView cancel;
    private CategoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DbAdapter db;
    private Context mContext;
    private ArrayList<Category> categories = new ArrayList<>();
    private TextView tvPageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

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

        categories = db.getAllCategoryWithParentCode(0);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categories = db.getAllCategoryWithParentCode(0);
                mAdapter = new CategoryAdapter(categories, mContext , db);
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categories.clear();
                categories = db.getAllCategoryWithParentCode(CategoryAdapter.lastCategoryCode);
                mAdapter = new CategoryAdapter(categories, mContext , db);
                mRecyclerView.setAdapter(mAdapter);
            }
        });

    }

    private void initialise() {
        db = new DbAdapter(mContext);
        db.open();
        mRecyclerView = (RecyclerView) findViewById(R.id.category_list);
        back = (ImageView) findViewById(R.id.back);
        cancel = (ImageView) findViewById(R.id.cancel);
        menu = (ImageView) findViewById(R.id.menu);
    }
}