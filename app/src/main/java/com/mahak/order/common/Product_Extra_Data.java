package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class Product_Extra_Data {
    @SerializedName("LastBuyPrice")
    @Expose
    private BigDecimal lastBuyPrice;
    @SerializedName("AveragePrice")
    @Expose
    private BigDecimal averagePrice;

    public double getLastBuyPrice() {
        return lastBuyPrice.doubleValue();
    }

    public void setLastBuyPrice(double lastBuyPrice) {
        this.lastBuyPrice = new BigDecimal(lastBuyPrice);
    }

    public double getAveragePrice() {
        return averagePrice.doubleValue();
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = new BigDecimal(averagePrice);
    }
}
