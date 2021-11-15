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

import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.PromotionDetailActivity;
import com.mahak.order.R;
import com.mahak.order.common.Category;
import com.mahak.order.common.Promotion;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements Filterable {

    private ArrayList<Category> categories;
    private LayoutInflater mInflater;
    private ArrayList<Category> arrayOriginal = new ArrayList<>();
    private CustomFilterList Filter;
    private Context context;
    private DbAdapter db;
    public static int lastCategoryCode;

    @Override
    public Filter getFilter() {
        if (Filter == null)
            Filter = new CustomFilterList();
        return Filter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView category_name;

        ViewHolder(View itemView) {
            super(itemView);
            category_name = (TextView) itemView.findViewById(R.id.categoryName);
        }
    }

    public CategoryAdapter(ArrayList<Category> categories, Context context , DbAdapter db) {

        this.categories = categories;
        this.mInflater = LayoutInflater.from(context);
        arrayOriginal.addAll(categories);
        this.context = context;
        this.db = db;
    }

    public class CustomFilterList extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            String CategoryName;

            if (constraint.toString().length() > 0) {
                ArrayList<Category> filterItem = new ArrayList<>();
                for (int i = 0; i < arrayOriginal.size(); i++) {
                    Category category = arrayOriginal.get(i);
                    CategoryName = category.getCategoryName();
                    boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), CategoryName);
                    if (result_contain) {
                        filterItem.add(category);
                    }
                }//End of for
                result.values = filterItem;
                result.count = filterItem.size();
            } else {
                synchronized (this) {
                    result.values = arrayOriginal;
                    result.count = arrayOriginal.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            categories = (ArrayList<Category>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = mInflater.inflate(R.layout.lst_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.category_name.setText(categories.get(position).getCategoryName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastCategoryCode = categories.get(position).getParentCode();
                categories = db.getAllCategoryWithParentCode(categories.get(position).getCategoryCode());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
