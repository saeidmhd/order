package com.mahak.order.fragment;

import com.mahak.order.common.OrderDetailProperty;
import com.mahak.order.common.ProductDetail;

import java.util.ArrayList;

public interface ReturnToRecycler {
    void return_value_recycler(
            String countPackage,
            String count,
            double price,
            int position,
            String discount,
            String description,
            int selectedItemPosition,
            double sumCountBaJoz, ProductDetail productDetail, ArrayList<OrderDetailProperty> orderDetailProperties);
}
