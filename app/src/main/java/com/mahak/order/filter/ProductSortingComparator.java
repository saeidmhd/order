package com.mahak.order.filter;

import com.mahak.order.common.Product;

import java.util.Comparator;

public class ProductSortingComparator implements Comparator<Product> {

    @Override
    public int compare(Product product1, Product product2) {
        if (product1.getProductCode() > product2.getProductCode())
            return 1;
        else
            return 0;
    }

    @Override
    public Comparator<Product> reversed() {
        return null;
    }
}
