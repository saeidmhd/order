package com.mahak.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.R;
import com.mahak.order.common.PromotionEntity;
import com.mahak.order.common.Visitor;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;

public class StoreEntityAdapter extends RecyclerView.Adapter<StoreEntityAdapter.ViewHolder> {

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

    public StoreEntityAdapter(ArrayList<PromotionEntity> entitiesOfPromotions, Context context) {

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
        holder.entity_type.setText(R.string.store);
        Visitor visitor = db.getVisitorWithStoreCode(entitiesOfPromotions.get(position).getCodeEntity());
        holder.entity_name.setText(String.valueOf(visitor.getStoreCode()));
    }

    @Override
    public int getItemCount() {
        return entitiesOfPromotions.size();
    }
}
