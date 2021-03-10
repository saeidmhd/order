package com.mahak.order.PromoFragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.PromotionDetailActivity;
import com.mahak.order.R;
import com.mahak.order.adapter.CustomerEntityAdapter;
import com.mahak.order.adapter.GoodsEntityAdapter;
import com.mahak.order.adapter.StoreEntityAdapter;
import com.mahak.order.adapter.VisitorEntityAdapter;
import com.mahak.order.common.Promotion;
import com.mahak.order.common.PromotionEntity;

import java.util.ArrayList;

public class PromoTerms extends Fragment {

    private PromotionDetailActivity promotionDetailActivity;
    private Promotion promotion;
    private ArrayList<PromotionEntity> entitiesOfPromotions;
    private ArrayList<PromotionEntity> customerEntities = new ArrayList<>();
    private ArrayList<PromotionEntity> visitorEntities = new ArrayList<>();
    private ArrayList<PromotionEntity> goodEntities = new ArrayList<>();
    private ArrayList<PromotionEntity> storeEntities = new ArrayList<>();
    GoodsEntityAdapter goodsEntityAdapter;
    CustomerEntityAdapter customerEntityAdapter;
    StoreEntityAdapter storeEntityAdapter;
    VisitorEntityAdapter visitorEntityAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    LinearLayout customer_spinner, customer_check, specialCustomers, ll_all_customer, nothingTasvieh, HasTasvieh;
    LinearLayout visitor_spinner, visitor_check, specialVisitors, ll_all_visitors;
    LinearLayout goods_spinner, goods_check, specialGoods, ll_all_goods;
    LinearLayout stores_spinner, stores_check, specialStores, ll_all_stores;
    LinearLayout ll_detail_customers, ll_detail_visitors, ll_detail_goods, ll_detail_stores;
    RecyclerView customer_recycler, visitor_recycler, good_recycler, store_recycler;
    LinearLayout ll_customers, ll_goods;
    ImageView img_customer_check, img_goods_check;

    TextView nahve_tasvieh, has_nahve_tasvieh;


    public PromoTerms() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PromotionDetailActivity) {
            promotionDetailActivity = (PromotionDetailActivity) context;
        }
    }

    public static PromoTerms newInstance(String param1, String param2) {
        PromoTerms fragment = new PromoTerms();
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

        if (promotionDetailActivity != null) {
            promotion = PromotionDetailActivity.promotion;
            entitiesOfPromotions = PromotionDetailActivity.entitiesOfPromotions;
        }

        for (PromotionEntity promotionEntity : entitiesOfPromotions) {
            switch (promotionEntity.getEntityType()) {
                case Promotion.EntityCustomer:
                    customerEntities.add(promotionEntity);
                    break;
                case Promotion.EntityGroupCustomer:
                    customerEntities.add(promotionEntity);
                    break;
                case Promotion.EntityVisitor:
                    visitorEntities.add(promotionEntity);
                    break;
                case Promotion.EntityGoods:
                    goodEntities.add(promotionEntity);
                    break;
                case Promotion.EntityGroupGoods:
                    goodEntities.add(promotionEntity);
                    break;
                case Promotion.EntityStores:
                    storeEntities.add(promotionEntity);
                    break;

            }
        }


        View rootView = inflater.inflate(R.layout.fragment_promo_terms, container, false);
        initView(rootView);

        ll_customers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_detail_customers.getVisibility() == View.VISIBLE) {
                    ll_detail_customers.setVisibility(View.GONE);
                    img_customer_check.setImageResource(R.drawable.ic_chevron_down);
                } else {
                    ll_detail_customers.setVisibility(View.VISIBLE);
                    img_customer_check.setImageResource(R.drawable.ic_chevron_up);
                }
            }
        });

        ll_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_detail_goods.getVisibility() == View.VISIBLE) {
                    ll_detail_goods.setVisibility(View.GONE);
                    img_goods_check.setImageResource(R.drawable.ic_chevron_down);
                } else {
                    ll_detail_goods.setVisibility(View.VISIBLE);
                    img_goods_check.setImageResource(R.drawable.ic_chevron_up);
                }
            }
        });

        if (promotion.getTypeTasvieh() != 0) {
            nothingTasvieh.setVisibility(View.GONE);
            HasTasvieh.setVisibility(View.VISIBLE);
            switch (promotion.getTypeTasvieh()) {
                case Promotion.Naghd:
                    has_nahve_tasvieh.setText(R.string.chash);
                    break;
                case Promotion.NaghdoCheque:
                    has_nahve_tasvieh.setText(R.string.cheque_cash);
                    break;
                case Promotion.Nesieh:
                    has_nahve_tasvieh.setText(R.string.credit);
                    break;
                case Promotion.cheque:
                    has_nahve_tasvieh.setText(R.string.cheque);
                    break;
            }
        }

        if (promotion.getIsAllCustomer() == Promotion.AllCustomers) {
            changeToAllCustomer();
            ll_detail_customers.setVisibility(View.GONE);
            ll_customers.setClickable(false);
        } else {
            changeToSpecialCustomer();
            customerEntityAdapter = new CustomerEntityAdapter(customerEntities, getActivity());
            customer_recycler.setAdapter(customerEntityAdapter);
            ll_customers.setClickable(true);
        }

        if (promotion.getIsAllVisitor() == Promotion.AllVisitors) {
            changeToAllVisitor();
        } else {
            changeToSpecialVisitor();
            visitorEntityAdapter = new VisitorEntityAdapter(visitorEntities, getActivity());
            visitor_recycler.setAdapter(visitorEntityAdapter);
            ll_detail_visitors.setVisibility(View.VISIBLE);
        }

        if (promotion.getIsAllGood() == Promotion.AllGoods) {
            changeToAllGoods();
            ll_detail_goods.setVisibility(View.GONE);
            ll_goods.setClickable(false);
        } else {
            changeToSpecialGoods();
            goodsEntityAdapter = new GoodsEntityAdapter(goodEntities, getActivity());
            good_recycler.setAdapter(goodsEntityAdapter);
            ll_goods.setClickable(true);
        }

        if (promotion.getIsAllStore() == Promotion.AllStores) {
            changeToAllStores();
        } else {
            changeToSpecialStores();
            storeEntityAdapter = new StoreEntityAdapter(storeEntities, getActivity());
            store_recycler.setAdapter(storeEntityAdapter);
            ll_detail_stores.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void initView(View rootView) {

        nahve_tasvieh = (TextView) rootView.findViewById(R.id.nahve_tasvieh);
        has_nahve_tasvieh = (TextView) rootView.findViewById(R.id.has_nahve_tasvieh);

        customer_spinner = (LinearLayout) rootView.findViewById(R.id.customer_spinner);
        customer_check = (LinearLayout) rootView.findViewById(R.id.customer_check);
        specialCustomers = (LinearLayout) rootView.findViewById(R.id.specialCustomers);
        ll_all_customer = (LinearLayout) rootView.findViewById(R.id.ll_all_customer);

        nothingTasvieh = (LinearLayout) rootView.findViewById(R.id.nothingTasvieh);
        HasTasvieh = (LinearLayout) rootView.findViewById(R.id.HasTasvieh);

        img_customer_check = (ImageView) rootView.findViewById(R.id.img_customer_check);
        img_goods_check = (ImageView) rootView.findViewById(R.id.img_goods_check);

        ll_customers = (LinearLayout) rootView.findViewById(R.id.ll_customers);
        ll_goods = (LinearLayout) rootView.findViewById(R.id.ll_goods);

        visitor_spinner = (LinearLayout) rootView.findViewById(R.id.visitor_spinner);
        visitor_check = (LinearLayout) rootView.findViewById(R.id.visitor_check);
        specialVisitors = (LinearLayout) rootView.findViewById(R.id.specialVisitors);
        ll_all_visitors = (LinearLayout) rootView.findViewById(R.id.ll_all_visitors);

        goods_spinner = (LinearLayout) rootView.findViewById(R.id.goods_spinner);
        goods_check = (LinearLayout) rootView.findViewById(R.id.goods_check);
        specialGoods = (LinearLayout) rootView.findViewById(R.id.specialGoods);
        ll_all_goods = (LinearLayout) rootView.findViewById(R.id.ll_all_goods);

        stores_spinner = (LinearLayout) rootView.findViewById(R.id.stores_spinner);
        stores_check = (LinearLayout) rootView.findViewById(R.id.stores_check);
        specialStores = (LinearLayout) rootView.findViewById(R.id.specialStores);
        ll_all_stores = (LinearLayout) rootView.findViewById(R.id.ll_all_stores);

        ll_detail_customers = (LinearLayout) rootView.findViewById(R.id.ll_detail_customers);
        ll_detail_visitors = (LinearLayout) rootView.findViewById(R.id.ll_detail_visitors);
        ll_detail_goods = (LinearLayout) rootView.findViewById(R.id.ll_detail_goods);
        ll_detail_stores = (LinearLayout) rootView.findViewById(R.id.ll_detail_stores);

        customer_recycler = (RecyclerView) rootView.findViewById(R.id.customer_recycler);
        visitor_recycler = (RecyclerView) rootView.findViewById(R.id.visitor_recycler);
        good_recycler = (RecyclerView) rootView.findViewById(R.id.good_recycler);
        store_recycler = (RecyclerView) rootView.findViewById(R.id.store_recycler);


        customer_recycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        customer_recycler.setLayoutManager(mLayoutManager);

        visitor_recycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        visitor_recycler.setLayoutManager(mLayoutManager);

        good_recycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        good_recycler.setLayoutManager(mLayoutManager);

        store_recycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        store_recycler.setLayoutManager(mLayoutManager);

    }

    private void changeToAllCustomer() {
        customer_spinner.setVisibility(View.GONE);
        customer_check.setVisibility(View.VISIBLE);
        specialCustomers.setVisibility(View.GONE);
        ll_all_customer.setVisibility(View.VISIBLE);

    }

    private void changeToSpecialCustomer() {
        customer_spinner.setVisibility(View.VISIBLE);
        customer_check.setVisibility(View.GONE);
        specialCustomers.setVisibility(View.VISIBLE);
        ll_all_customer.setVisibility(View.GONE);
    }

    private void changeToAllVisitor() {
        visitor_spinner.setVisibility(View.GONE);
        visitor_check.setVisibility(View.VISIBLE);
        specialVisitors.setVisibility(View.GONE);
        ll_all_visitors.setVisibility(View.VISIBLE);
    }

    private void changeToSpecialVisitor() {
        visitor_spinner.setVisibility(View.VISIBLE);
        visitor_check.setVisibility(View.GONE);
        specialVisitors.setVisibility(View.VISIBLE);
        ll_all_visitors.setVisibility(View.GONE);
    }

    private void changeToAllGoods() {
        goods_spinner.setVisibility(View.GONE);
        goods_check.setVisibility(View.VISIBLE);
        specialGoods.setVisibility(View.GONE);
        ll_all_goods.setVisibility(View.VISIBLE);
    }

    private void changeToSpecialGoods() {
        goods_spinner.setVisibility(View.VISIBLE);
        goods_check.setVisibility(View.GONE);
        specialGoods.setVisibility(View.VISIBLE);
        ll_all_goods.setVisibility(View.GONE);
    }

    private void changeToAllStores() {
        stores_spinner.setVisibility(View.GONE);
        stores_check.setVisibility(View.VISIBLE);
        specialStores.setVisibility(View.GONE);
        ll_all_stores.setVisibility(View.VISIBLE);
    }

    private void changeToSpecialStores() {
        stores_spinner.setVisibility(View.VISIBLE);
        stores_check.setVisibility(View.GONE);
        specialStores.setVisibility(View.VISIBLE);
        ll_all_stores.setVisibility(View.GONE);
    }


}
