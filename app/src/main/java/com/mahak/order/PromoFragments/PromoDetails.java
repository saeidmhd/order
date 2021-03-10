package com.mahak.order.PromoFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.PromotionDetailActivity;
import com.mahak.order.R;
import com.mahak.order.adapter.PromotionDetailAdapter;
import com.mahak.order.common.Promotion;
import com.mahak.order.common.PromotionDetail;

import java.util.ArrayList;


public class PromoDetails extends Fragment {


    private PromotionDetailActivity promotionDetailActivity;
    private Promotion promotion;
    private ArrayList<PromotionDetail> promotionDetail;


    public PromoDetails() {
        // Required empty public constructor
    }

    public static PromoDetails newInstance(String param1, String param2) {
        PromoDetails fragment = new PromoDetails();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PromotionDetailActivity) {
            promotionDetailActivity = (PromotionDetailActivity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        TextView stair_linear;
        TextView promotion_type;
        RecyclerView promotionDetailRecycler;
        PromotionDetailAdapter promotionDetailAdapter;

        if (promotionDetailActivity != null) {
            promotionDetail = PromotionDetailActivity.promotionDetail;
            promotion = PromotionDetailActivity.promotion;
        }
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_promo_details, container, false);
        stair_linear = (TextView) rootView.findViewById(R.id.stair_linear);
        promotion_type = (TextView) rootView.findViewById(R.id.promotion_type);

        if (promotion.getIsCalcLinear() == 1)
            stair_linear.setText(R.string.linear);
        else
            stair_linear.setText(R.string.stair);

        switch (promotion.getAccordingTo()) {
            case Promotion.Mablaghe_kole_Faktor:
                promotion_type.setText(R.string.total_invoice_than);
                break;
            case Promotion.Jame_Aghlame_Faktor:
                promotion_type.setText(R.string.total_invoice_items_than);
                break;
            case Promotion.Jame_Vazne_Faktor:
                promotion_type.setText(R.string.total_weight_factor_than);
                break;
            case Promotion.Jame_anvae_Aghlame_faktor:
                promotion_type.setText(R.string.total_invoice_types_items_than);
                break;
            case Promotion.Mablaghe_Satr:
                promotion_type.setText(R.string.row_amount_than);
                break;
            case Promotion.Meghdare_Satr:
                promotion_type.setText(R.string.row_count_than);
                break;
        }

        promotionDetailRecycler = (RecyclerView) rootView.findViewById(R.id.promotionDetailRecycler);

        promotionDetailRecycler.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        promotionDetailRecycler.setLayoutManager(mLayoutManager);

        promotionDetailAdapter = new PromotionDetailAdapter(promotionDetail, getActivity());
        promotionDetailRecycler.setAdapter(promotionDetailAdapter);

        return rootView;


    }

}
