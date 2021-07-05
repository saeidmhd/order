package com.mahak.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.PromotionDetailActivity;
import com.mahak.order.R;
import com.mahak.order.common.Promotion;
import com.mahak.order.common.ServiceTools;

import java.util.ArrayList;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.ViewHolder> implements Filterable {

    private ArrayList<Promotion> promotionArrayList;
    private LayoutInflater mInflater;
    private ArrayList<Promotion> arrayOriginal = new ArrayList<>();
    private CustomFilterList Filter;
    private Context context;

    @Override
    public Filter getFilter() {
        if (Filter == null)
            Filter = new CustomFilterList();
        return Filter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView promotion_name;
        public TextView from_date;
        public TextView to_date;

        ViewHolder(View itemView) {
            super(itemView);
            promotion_name = (TextView) itemView.findViewById(R.id.promotion_name);
            from_date = (TextView) itemView.findViewById(R.id.from_date);
            to_date = (TextView) itemView.findViewById(R.id.to_date);
        }
    }

    public PromotionAdapter(ArrayList<Promotion> promotionArrayList, Context context) {

        this.promotionArrayList = promotionArrayList;
        this.mInflater = LayoutInflater.from(context);
        arrayOriginal.addAll(promotionArrayList);
        this.context = context;
    }

    public class CustomFilterList extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            String PromotionName;

            if (constraint.toString().length() > 0) {
                ArrayList<Promotion> filterItem = new ArrayList<>();
                for (int i = 0; i < arrayOriginal.size(); i++) {
                    Promotion promotion = arrayOriginal.get(i);
                    PromotionName = promotion.getNamePromotion();
                    boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), PromotionName);
                    if (result_contain) {
                        filterItem.add(promotion);
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
            promotionArrayList = (ArrayList<Promotion>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = mInflater.inflate(R.layout.lst_promo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.promotion_name.setText(String.format("%s (%s)", promotionArrayList.get(position).getNamePromotion(), promotionArrayList.get(position).getPromotionCode()));
        holder.from_date.setText(promotionArrayList.get(position).getDateStart());
        holder.to_date.setText(promotionArrayList.get(position).getDateEnd());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PromotionDetailActivity.class);
                intent.putExtra("CodePromotion", promotionArrayList.get(position).getPromotionCode());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return promotionArrayList.size();
    }
}
