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
import java.util.HashSet;
import java.util.Set;

public class CategoryActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private ImageView back;
    private ImageView menu;
    private ImageView cancel;
    private CategoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DbAdapter db;
    private Context mContext;
    private Set<Category> categories = new HashSet<>();
    private Set<Integer> ids = new HashSet<>();
    private ArrayList<Category> existedCategories = new ArrayList<>();
    private ArrayList<Category> rootCategories = new ArrayList<>();
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

        existedCategories = db.getAllExistedCategory();
        categories.addAll(existedCategories);
        for (Category existed_category : existedCategories)
            if(existed_category.getParentCode() != 0){
                addAllParentCategory(existed_category.getParentCode());
            }else
                rootCategories.add(existed_category);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*categories = db.getAllCategoryWithParentCode(0);
                mAdapter = new CategoryAdapter(categories, mContext , db);
                mRecyclerView.setAdapter(mAdapter);*/

                ArrayList<Category> categoryArrayList = new ArrayList<>(rootCategories);
                mAdapter = new CategoryAdapter(categoryArrayList, mContext , db);
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* categories.clear();
                categories = db.getAllCategoryWithParentCode(CategoryAdapter.lastCategoryCode);
                mAdapter = new CategoryAdapter(categories, mContext , db);
                mRecyclerView.setAdapter(mAdapter);*/
            }
        });

    }


    private void addAllParentCategory(int id) {
        if(ids.add(id)){
            ArrayList<Category> parentCategories =  db.getAllParentCategory(id);
            categories.addAll(parentCategories);
            for (Category parentCategory : parentCategories)
                if(parentCategory.getParentCode() != 0){
                    addAllParentCategory(parentCategory.getCategoryCode());
                }else
                    rootCategories.add(parentCategory);
        }
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