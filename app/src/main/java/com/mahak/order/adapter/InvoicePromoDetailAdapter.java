package com.mahak.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.PromotionDetailActivity;
import com.mahak.order.R;
import com.mahak.order.common.Promotion;
import com.mahak.order.common.PromotionDetail;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;

import static com.mahak.order.InvoiceDetailActivity.CommitPromoCode;

public class InvoicePromoDetailAdapter extends RecyclerView.Adapter<InvoicePromoDetailAdapter.ViewHolder> {

    private ArrayList<Promotion> promotions = new ArrayList<>();
    private ArrayList<PromotionDetail> promotionDetails = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
    private DbAdapter db;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView howToPromo;
        public TextView AccordingTo;
        public TextView promoTitle;

        ViewHolder(View itemView) {
            super(itemView);
            howToPromo = (TextView) itemView.findViewById(R.id.howToPromo);
            AccordingTo = (TextView) itemView.findViewById(R.id.accordingTo);
            promoTitle = (TextView) itemView.findViewById(R.id.promoTitle);
        }
    }

    public InvoicePromoDetailAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.lst_invoice_promo_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (db == null) db = new DbAdapter(context);
        db.open();
        for (int elem : CommitPromoCode) {
            promotions.add(db.getPromotionByCode(String.valueOf(elem)));
            promotionDetails.addAll(db.getPromotionDetails(String.valueOf(elem)));
        }
        holder.promoTitle.setText(String.format("%s (%s)", promotions.get(position).getNamePromotion(), promotions.get(position).getPromotionCode()));

        switch (promotions.get(position).getAccordingTo()) {
            case Promotion.Mablaghe_kole_Faktor:
                holder.AccordingTo.setText(R.string.total_invoice_amount);
                break;
            case Promotion.Jame_Aghlame_Faktor:
                holder.AccordingTo.setText(R.string.total_invoice_items);
                break;
            case Promotion.Jame_Vazne_Faktor:
                holder.AccordingTo.setText(R.string.total_weight_factor);
                break;
            case Promotion.Jame_anvae_Aghlame_faktor:
                holder.AccordingTo.setText(R.string.total_invoice_types_items);
                break;
            case Promotion.Mablaghe_Satr:
                holder.AccordingTo.setText(R.string.row_amount);
                break;
            case Promotion.Meghdare_Satr:
                holder.AccordingTo.setText(R.string.row_count);
                break;
        }

        switch (promotionDetails.get(position).getHowToPromotion()) {
            case Promotion.takhfif_mablaghe_sabet:
                holder.howToPromo.setText(R.string.fixed_amount_discount);
                break;
            case Promotion.takhfif_darsadi:
                holder.howToPromo.setText(R.string.percent_discount);
                break;
            case Promotion.takhfif_Az_Sotooh:
                holder.howToPromo.setText(R.string.discount_from_levels);
                break;
            case Promotion.eshantion_Az_hamanKala:
                holder.howToPromo.setText(R.string.gift_same_product);
                break;
            case Promotion.eshantion_Az_kalahaye_digar:
                holder.howToPromo.setText(R.string.gift_other_product);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PromotionDetailActivity.class);
                intent.putExtra("CodePromotion", promotions.get(position).getPromotionCode());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return CommitPromoCode.size();
    }
}
