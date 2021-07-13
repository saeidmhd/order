package com.mahak.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.R;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductGroup;
import com.mahak.order.common.Promotion;
import com.mahak.order.common.PromotionEntity;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;

public class GoodsEntityAdapter extends RecyclerView.Adapter<GoodsEntityAdapter.ViewHolder> {

    private ArrayList<PromotionEntity> entitiesOfPromotions;
    private LayoutInflater mInflater;
    private Context context;
    private DbAdapter db;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView entity_type;
        public TextView entity_name;

        ViewHolder(View itemView) {
            super(itemView);
            entity_type = (TextView) itemView.findViewById(R.id.entity_type);
            entity_name = (TextView) itemView.findViewById(R.id.entity_name);
        }
    }

    public GoodsEntityAdapter(ArrayList<PromotionEntity> entitiesOfPromotions, Context context) {

        this.entitiesOfPromotions = entitiesOfPromotions;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = mInflater.inflate(R.layout.lst_entity_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (db == null) db = new DbAdapter(context);
        db.open();

        switch (entitiesOfPromotions.get(position).getEntityType()) {

            case Promotion.EntityGoods:
                holder.entity_type.setText(R.string.product);
                Product product = db.getProductWithProductCode(entitiesOfPromotions.get(position).getCodeEntity());
                holder.entity_name.setText(product.getName());
                break;
            case Promotion.EntityGroupGoods:
                holder.entity_type.setText(R.string.product_group);
                ProductGroup productGroup = db.GetPromoGroup(entitiesOfPromotions.get(position).getCodeEntity());
                holder.entity_name.setText(productGroup.getName());
                break;

        }

    }

    @Override
    public int getItemCount() {
        return entitiesOfPromotions.size();
    }
}
