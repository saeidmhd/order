package com.mahak.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.R;
import com.mahak.order.common.Category;
import com.mahak.order.common.Product;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;

public class CategoryAdapter2 extends RecyclerView.Adapter<CategoryAdapter2.ViewHolder> {

    private ArrayList<Category> categories;
    private LayoutInflater mInflater;
    private ArrayList<Category> arrayOriginal = new ArrayList<>();
    private Context context;
    public static int lastCategoryCode;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        ImageView mExpandIcon;
        LinearLayout mTextBox, item_category;

        ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mExpandIcon = (ImageView) itemView.findViewById(R.id.image_view);
            mTextBox = (LinearLayout) itemView.findViewById(R.id.text_box);
            item_category = (LinearLayout) itemView.findViewById(R.id.item_category);
        }
    }

    public CategoryAdapter2( ArrayList<Category> categories, Context context) {

        this.categories = categories;
        this.mInflater = LayoutInflater.from(context);
        arrayOriginal.addAll(categories);
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = mInflater.inflate(R.layout.item_layout2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTitle.setText(categories.get(position).getCategoryName());

        holder.item_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Product> productArrayList;
                /*clickedItemCategoryCode = mListItems.get(getAdapterPosition()).getCategoryCode();
                getProducts();*/
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Category> temp = new ArrayList<>();
                for (Category category : categories){
                    if(category.getParentCode() == categories.get(position).getCategoryCode())
                        temp.add(category);
                }
                if(temp.size() > 0 ){
                    categories = temp;
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
