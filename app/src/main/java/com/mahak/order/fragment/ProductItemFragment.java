package com.mahak.order.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mahak.order.ProductItemInitialize;
import com.mahak.order.ProductPickerListActivity;
import com.mahak.order.ProductsListActivity;
import com.mahak.order.R;
import com.mahak.order.common.Product;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;

/**
 * Created by mostafavi on 7/5/2016.
 */
public class ProductItemFragment extends DialogFragment {

    private Context mContext;
    private long productId;
    private String Barcode;
    private int position;
    private ImageView imgProduct;
    private Button btnCount;
    private DbAdapter db;
    private ProductsListActivity productsListActivity;
    private ProductPickerListActivity productPickerListActivity;
    private ArrayList<Product> products;
    private ProductItemInitialize productItemInitialize;
    private ProductItemInitialize.Holder holder;

    @Override
    public void onAttach(Context context) {
        mContext = context;
        db = new DbAdapter(mContext);
        super.onAttach(context);
        if (mContext instanceof ProductsListActivity) {
            productsListActivity = (ProductsListActivity) mContext;
            products = ProductsListActivity.arrayProductMain;
        } else if (mContext instanceof ProductPickerListActivity) {
            productPickerListActivity = (ProductPickerListActivity) mContext;
            products = ProductPickerListActivity.arrayProductMain;
            calculateSelectedProduct();
        }
    }

    private void calculateSelectedProduct() {
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (ProductPickerListActivity.HashMap_Product != null && ProductPickerListActivity.HashMap_Product.size() > 0 && ProductPickerListActivity.HashMap_Product.containsKey(product.getProductId())) {
                product.setSelectedCount(ProductPickerListActivity.HashMap_Product.get(product.getProductId()).getCount1());
            } else
                product.setSelectedCount(0);
        }
    }

    /*public void refresh() {
        calculateSelectedProduct();
        if (productItemInitialize != null && holder != null)
            productItemInitialize.initHolder(getProduct(), getChildFragmentManager(), holder, position);
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("productId")) {
            productId = bundle.getLong("productId");
        }
        if (bundle != null && bundle.containsKey("barcode")) {
            Barcode = bundle.getString("barcode");
        }
        if (bundle != null && bundle.containsKey("position")) {
            position = bundle.getInt("position");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db.open();
        View view = inflater.inflate(R.layout.fragment_item_product_gallery, container, false);
        productItemInitialize = new ProductItemInitialize(mContext, productsListActivity, productPickerListActivity);
        holder = productItemInitialize.initView(view);
        if (getProduct() != null && (Barcode == null || Barcode.equals("")))
            productItemInitialize.initHolder(position, getProduct(), holder, products);

        else {

            Product product = null;
            for (Product listproduct : products) {
                if (db.getProductDetailWithProductId(listproduct.getProductId()).getBarcode() != null && db.getProductDetailWithProductId(listproduct.getProductId()).getBarcode().equals(Barcode)) {
                    product = listproduct;
                }
            }
            if (product != null) {
                productItemInitialize.initHolder(position, product, holder, products);
            }


        }
        return view;
    }

    private Product getProduct() {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == productId)
                return products.get(i);
        }
        return null;
    }

}
