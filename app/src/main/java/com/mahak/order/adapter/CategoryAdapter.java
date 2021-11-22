package com.mahak.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.PromotionDetailActivity;
import com.mahak.order.R;
import com.mahak.order.common.Category;
import com.mahak.order.common.Promotion;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private ArrayList<Category> categories;
    private ArrayList<Category> rootCategories;
    private LayoutInflater mInflater;
    private ArrayList<Category> arrayOriginal = new ArrayList<>();
    private Context context;
    private DbAdapter db;
    public static int lastCategoryCode;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView category_name;

        ViewHolder(View itemView) {
            super(itemView);
            category_name = (TextView) itemView.findViewById(R.id.categoryName);
        }
    }

    public CategoryAdapter(ArrayList<Category> rootCategories, ArrayList<Category> categories, Context context, DbAdapter db) {

        this.categories = categories;
        this.rootCategories = rootCategories;
        this.mInflater = LayoutInflater.from(context);
        arrayOriginal.addAll(categories);
        this.context = context;
        this.db = db;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = mInflater.inflate(R.layout.lst_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.category_name.setText(rootCategories.get(position).getCategoryName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Category> temp = new ArrayList<>();
                for (Category category : categories){
                    if(category.getParentCode() == rootCategories.get(position).getCategoryCode())
                        temp.add(category);
                }
                if(temp.size() > 0 ){
                    rootCategories = temp;
                    notifyDataSetChanged();
                }

                /*if(categories.size() > 0 ){
                    categories = db.getAllCategoryWithParentCode(categories.get(position).getCategoryCode());
                    if(categories.size() > 0 )
                        notifyDataSetChanged();
                    else
                        Toast.makeText(context, "زیر مجموعه ای ندارد!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, "زیر مجموعه ای ندارد!", Toast.LENGTH_SHORT).show();*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return rootCategories.size();
    }
}
