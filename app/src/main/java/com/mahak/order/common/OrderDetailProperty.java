package com.mahak.order.common;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class OrderDetailProperty implements Parcelable {

    private int Id;

    private BigDecimal sumCountBaJoz;

    private long orderDetailClientId;

    private long orderId;

    private int orderDetailPropertyId;

    private int productDetailId;

    private int productId;

    private double count1;

    private double count2;

    private int position;

    private String productSpec;


    public OrderDetailProperty(Parcel in) {
        Id = in.readInt();
        orderDetailClientId = in.readInt();
        orderDetailPropertyId = in.readInt();
        productDetailId = in.readInt();
        productId = in.readInt();
        count1 = in.readDouble();
        count2 = in.readDouble();
        orderId = in.readLong();
    }

    public OrderDetailProperty() {
    }

    public static final Creator<OrderDetailProperty> CREATOR = new Creator<OrderDetailProperty>() {
        @Override
        public OrderDetailProperty createFromParcel(Parcel in) {
            return new OrderDetailProperty(in);
        }

        @Override
        public OrderDetailProperty[] newArray(int size) {
            return new OrderDetailProperty[size];
        }
    };

    public long getOrderDetailClientId() {
        return orderDetailClientId;
    }

    public void setOrderDetailClientId(long orderDetailClientId) {
        this.orderDetailClientId = orderDetailClientId;
    }

    public int getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(int productDetailId) {
        this.productDetailId = productDetailId;
    }


    public double getSumCountBaJoz() {
        return sumCountBaJoz.doubleValue();
    }

    public void setSumCountBaJoz(double sumCountBaJoz) {
        this.sumCountBaJoz = new BigDecimal(sumCountBaJoz);
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }


    public int getOrderDetailPropertyId() {
        return orderDetailPropertyId;
    }

    public void setOrderDetailPropertyId(int orderDetailPropertyId) {
        this.orderDetailPropertyId = orderDetailPropertyId;
    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeLong(orderDetailClientId);
        dest.writeInt(orderDetailPropertyId);
        dest.writeInt(productDetailId);
        dest.writeInt(productId);
        dest.writeDouble(count1);
        dest.writeDouble(count2);
        dest.writeLong(orderId);
    }

    public double getCount1() {
        return count1;
    }

    public void setCount1(double count1) {
        this.count1 = count1;
    }

    public double getCount2() {
        return count2;
    }

    public void setCount2(double count2) {
        this.count2 = count2;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getProductSpec() {
        return productSpec;
    }

    public void setProductSpec(String productSpec) {
        this.productSpec = productSpec;
    }
}