package com.mahak.order.threeLevelAdapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.R;
import com.mahak.order.common.ProductCategory;
import com.mahak.order.fragment.RecyclerProductAdapter;
import com.mahak.order.storage.DbAdapter;
import com.multilevelview.MultiLevelAdapter;
import com.multilevelview.MultiLevelRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends MultiLevelAdapter {

    private Holder mViewHolder;
    private Context mContext;
    private List<Item> mListItems = new ArrayList<>();
    private Item mItem;
    private MultiLevelRecyclerView mMultiLevelRecyclerView;
    private RecyclerProductAdapter adapter;
    private DbAdapter db;

    public MyAdapter(Context mContext, List<Item> mListItems, MultiLevelRecyclerView mMultiLevelRecyclerView, RecyclerProductAdapter adapter) {
        super(mListItems);
        this.mListItems = mListItems;
        this.mContext = mContext;
        this.mMultiLevelRecyclerView = mMultiLevelRecyclerView;
        this.adapter = adapter;
        db = new DbAdapter(mContext);
    }

    private void setExpandButton(ImageView expandButton, boolean isExpanded) {
        expandButton.setImageResource(isExpanded ? R.drawable.ic_keyboard_arrow_up_black_24dp : R.drawable.ic_keyboard_arrow_down_black_24dp);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mViewHolder = (Holder) holder;
        mItem = mListItems.get(position);

        if (mItem.hasChildren())
            holder.itemView.setBackgroundColor(Color.parseColor("#efefef"));
        else {
            holder.itemView.setBackgroundColor(Color.parseColor("#dedede"));
            mViewHolder.mTitle.setTextSize(16);
        }


        /*switch (getItemViewType(position)) {
            case 1:
                holder.itemView.setBackgroundColor(Color.parseColor("#efefef"));
                break;
            case 2:
                holder.itemView.setBackgroundColor(Color.parseColor("#dedede"));
                break;
            default:
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
        }*/
        mViewHolder.mTitle.setText(mItem.getText());

        if (mItem.hasChildren() && mItem.getChildren().size() > 0) {
            // setExpandButton(mViewHolder.mExpandIcon, mItem.isExpanded());
            mViewHolder.mExpandButton.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.mExpandButton.setVisibility(View.GONE);
        }

        Log.e("MuditLog", mItem.getLevel() + " " + mItem.getPosition() + " " + mItem.isExpanded() + "");

        // indent child items
        // Note: the parent item should start at zero to have no indentation
        // e.g. in populateFakeData(); the very first Item shold be instantiate like this: Item item = new Item(0);
        /*float density = mContext.getResources().getDisplayMetrics().density;
        ((ViewGroup.MarginLayoutParams) mViewHolder.mTextBox.getLayoutParams()).rightMargin = (int) ((getItemViewType(position) * 20) * density + 0.5f);*/
        /*float textSize = mViewHolder.mTitle.getTextSize();
         */
    }


    private class Holder extends RecyclerView.ViewHolder {

        TextView mTitle;
        ImageView mExpandIcon;
        LinearLayout mTextBox, mExpandButton, ll_category;

        Holder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mExpandIcon = (ImageView) itemView.findViewById(R.id.image_view);
            mTextBox = (LinearLayout) itemView.findViewById(R.id.text_box);
            ll_category = (LinearLayout) itemView.findViewById(R.id.ll_category);
            mExpandButton = (LinearLayout) itemView.findViewById(R.id.expand_field);

            // The following code snippets are only necessary if you set multiLevelRecyclerView.removeItemClickListeners(); in MainActivity.java
            // this enables more than one click event on an item (e.g. Click Event on the item itself and click event on the expand button)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //set click event on item here

                    // Toast.makeText(mContext, String.format(Locale.ENGLISH, "Item at position %d was clicked!", getAdapterPosition()), Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext, mListItems.get(getAdapterPosition()).getText(), Toast.LENGTH_SHORT).show();


                }
            });

            ll_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    db.open();
                    ArrayList<ProductCategory> productCategories = db.getAllProductCategoryWithCategoryCode(mListItems.get(getAdapterPosition()).getCategoryCode());
                    Toast.makeText(mContext, mListItems.get(getAdapterPosition()).getText(), Toast.LENGTH_SHORT).show();

                    // rotate the icon based on the current state
                    // but only here because otherwise we'd see the animation on expanded items too while scrolling
                    //

                }
            });

            //set click listener on LinearLayout because the click area is bigger than the ImageView
            mExpandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //adapter.getFilter().filter();

                    // set click event on expand button here
                    mMultiLevelRecyclerView.toggleItemsGroup(getAdapterPosition());
                    mExpandIcon.animate().rotation(mListItems.get(getAdapterPosition()).isExpanded() ? -180 : 0).start();

                    // setExpandButton(mExpandIcon, mListItems.get(getAdapterPosition()).isExpanded());

                    //Toast.makeText(mContext, String.format(Locale.ENGLISH, "Item at position %d is expanded: %s", getAdapterPosition(), mItem.isExpanded()), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
