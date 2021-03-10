package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;


public class ProductProductDetail {

    @SerializedName("UnitRatio")
    @Expose
    private double unitRatio;

    @SerializedName("ProductCode")
    @Expose
    private long productCode;

    @SerializedName("Name")
    @Expose
    private String Name;

    @SerializedName("UnitName")
    @Expose
    private String UnitName;

    @SerializedName("UnitName2")
    @Expose
    private String UnitName2;

    @SerializedName("ProductId")
    @Expose
    private int productId;

    private ArrayList<ProductDetail> productDetails = new ArrayList<>();

    public ArrayList<ProductDetail> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ArrayList<ProductDetail> productDetails) {
        this.productDetails = productDetails;
    }
}
