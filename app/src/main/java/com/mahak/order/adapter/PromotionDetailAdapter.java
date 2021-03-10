package com.mahak.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.R;
import com.mahak.order.common.Product;
import com.mahak.order.common.Promotion;
import com.mahak.order.common.PromotionDetail;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;

import static com.mahak.order.common.ServiceTools.formatCount;

public class PromotionDetailAdapter extends RecyclerView.Adapter<PromotionDetailAdapter.ViewHolder> {

    private ArrayList<PromotionDetail> promotionDetails;
    private LayoutInflater mInflater;
    private ArrayList<PromotionDetail> promotionDetailArrayList = new ArrayList<>();
    private Context context;
    private DbAdapter db;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView goodGift;
        public TextView meghdar_promotion;
        public TextView how_to_promotion;
        public TextView to_payment;

        ViewHolder(View itemView) {
            super(itemView);
            goodGift = (TextView) itemView.findViewById(R.id.goodGift);
            meghdar_promotion = (TextView) itemView.findViewById(R.id.meghdar_promotion);
            how_to_promotion = (TextView) itemView.findViewById(R.id.how_to_promotion);
            to_payment = (TextView) itemView.findViewById(R.id.to_payment);
        }
    }

    public PromotionDetailAdapter(ArrayList<PromotionDetail> promotionArrayList, Context context) {

        this.promotionDetails = promotionArrayList;
        this.mInflater = LayoutInflater.from(context);
        promotionDetailArrayList.addAll(promotionArrayList);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = mInflater.inflate(R.layout.lst_promo_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (db == null) db = new DbAdapter(context);
        db.open();
        Product product;
        holder.meghdar_promotion.setText(formatCount(promotionDetails.get(position).getMeghdarPromotion()));
        switch (promotionDetails.get(position).getHowToPromotion()) {
            case Promotion.takhfif_mablaghe_sabet:
                holder.how_to_promotion.setText(R.string.fixed_amount_discount);
                holder.goodGift.setText("-------");
                break;
            case Promotion.takhfif_darsadi:
                holder.how_to_promotion.setText(R.string.percent_discount);
                holder.goodGift.setText("-------");
                break;
            case Promotion.takhfif_Az_Sotooh:
                holder.how_to_promotion.setText(R.string.discount_from_levels);
                holder.goodGift.setText("-------");
                holder.meghdar_promotion.setText(context.getString(R.string.discount_level) + formatCount(promotionDetails.get(position).getMeghdarPromotion()));
                break;
            case Promotion.eshantion_Az_hamanKala:
                holder.how_to_promotion.setText(R.string.gift_same_product);
                holder.goodGift.setText("-------");
                holder.meghdar_promotion.setText(formatCount(promotionDetails.get(position).getMeghdar()));
                break;
            case Promotion.eshantion_Az_kalahaye_digar:
                holder.how_to_promotion.setText(R.string.gift_other_product);
                product = db.getProductWithProductCode(promotionDetails.get(position).getCodeGood());
                holder.goodGift.setText(product.getName());
                holder.meghdar_promotion.setText(formatCount(promotionDetails.get(position).getMeghdar()));
                break;

        }
        holder.to_payment.setText(formatCount(promotionDetails.get(position).getToPayment()));

    }

    @Override
    public int getItemCount() {
        return promotionDetails.size();
    }
}
