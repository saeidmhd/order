package com.mahak.order.PromoFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mahak.order.PromotionDetailActivity;
import com.mahak.order.R;
import com.mahak.order.common.Promotion;


public class PromoGeneralSpec extends Fragment {

    private static final String CODE_PROMOTION = "codePromotion";
    private PromotionDetailActivity promotionDetailActivity;
    private TextView promotionTitle;
    private TextView dateStart;
    private TextView dateEnd;
    private TextView accordingTo;
    private TextView priorityPromotion;
    private TextView aggregateWithOther;
    private CheckBox aggregateBox;
    private Promotion promotion;

    public PromoGeneralSpec() {
        // Required empty public constructor
    }

    public static PromoGeneralSpec newInstance(String codePromotion) {
        PromoGeneralSpec fragment = new PromoGeneralSpec();
        Bundle args = new Bundle();
        args.putString(CODE_PROMOTION, codePromotion);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

        if (promotionDetailActivity != null)
            promotion = PromotionDetailActivity.promotion;
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_general_promo_spec, container, false);
        promotionTitle = (TextView) rootView.findViewById(R.id.promotionTitle);
        dateStart = (TextView) rootView.findViewById(R.id.dateStart);
        dateEnd = (TextView) rootView.findViewById(R.id.dateEnd);
        priorityPromotion = (TextView) rootView.findViewById(R.id.priorityPromotion);
        accordingTo = (TextView) rootView.findViewById(R.id.accordingTo);
        aggregateWithOther = (TextView) rootView.findViewById(R.id.aggregateWithOther);
        aggregateBox = (CheckBox) rootView.findViewById(R.id.aggregateBox);

        promotionTitle.setText(promotion.getNamePromotion());
        dateStart.setText(promotion.getDateStart());
        dateEnd.setText(promotion.getDateEnd());
        priorityPromotion.setText(String.valueOf(promotion.getPriorityPromotion()));
        switch (promotion.getAccordingTo()) {
            case Promotion.Mablaghe_kole_Faktor:
                accordingTo.setText(R.string.total_invoice_amount);
                break;
            case Promotion.Jame_Aghlame_Faktor:
                accordingTo.setText(R.string.total_invoice_items);
                break;
            case Promotion.Jame_Vazne_Faktor:
                accordingTo.setText(R.string.total_weight_factor);
                break;
            case Promotion.Jame_anvae_Aghlame_faktor:
                accordingTo.setText(R.string.total_invoice_types_items);
                break;
            case Promotion.Mablaghe_Satr:
                accordingTo.setText(R.string.row_amount);
                break;
            case Promotion.Meghdare_Satr:
                accordingTo.setText(R.string.row_count);
                break;
        }
        if (promotion.getAggregateWithOther() == 0) {
            aggregateWithOther.setText(R.string.not_aggregated);
            aggregateWithOther.setTextColor(getResources().getColor(R.color.red));
            aggregateBox.setChecked(false);
            aggregateBox.setEnabled(false);
        } else {
            aggregateWithOther.setText(R.string.aggregated);
            aggregateWithOther.setTextColor(getResources().getColor(R.color.accent));
            aggregateBox.setChecked(true);
            aggregateBox.setEnabled(false);
        }

        return rootView;
    }


}
