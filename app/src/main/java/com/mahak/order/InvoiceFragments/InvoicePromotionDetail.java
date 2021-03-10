package com.mahak.order.InvoiceFragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.R;
import com.mahak.order.adapter.InvoicePromoDetailAdapter;
import com.mahak.order.interfaces.FragmentLifecycle;


public class InvoicePromotionDetail extends Fragment implements FragmentLifecycle {

    private static InvoicePromoDetailAdapter invoicePromoDetailAdapter;


    public InvoicePromotionDetail() {
        // Required empty public constructor
    }

    public static InvoicePromotionDetail newInstance(String param1, String param2) {
        InvoicePromotionDetail fragment = new InvoicePromotionDetail();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
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
        RecyclerView promotionDetailRecycler;
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_invoice_promotion_detail, container, false);

        promotionDetailRecycler = (RecyclerView) v.findViewById(R.id.promotionDetailRecycler);
        promotionDetailRecycler.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        promotionDetailRecycler.setLayoutManager(mLayoutManager);

        invoicePromoDetailAdapter = new InvoicePromoDetailAdapter(getActivity());
        promotionDetailRecycler.setAdapter(invoicePromoDetailAdapter);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        invoicePromoDetailAdapter.notifyDataSetChanged();

    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {
        invoicePromoDetailAdapter.notifyDataSetChanged();

    }
}
