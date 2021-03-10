package com.mahak.order.common;

/**
 * Created by mehrdadsml on 26/04/2016.
 */
public class ReportProductDetail {

    private String Name;
    private double Sale;
    private double Discount;
    private double NetSale;
    private double Asset;
    private double Count;

    public ReportProductDetail() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getSale() {
        return Sale;
    }

    public void setSale(double sale) {
        Sale = sale;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }

    public double getNetSale() {
        return NetSale;
    }

    public void setNetSale(double netSale) {
        NetSale = netSale;
    }

    public double getAsset() {
        return Asset;
    }

    public void setAsset(double asset) {
        Asset = asset;
    }

    public double getCount() {
        return Count;
    }

    public void setCount(double count) {
        Count = count;
    }
}
