package com.mahak.order.common;

//Create by Saeid.mhd@gmail.com

import com.mahak.order.BaseActivity;

public class ProductPriceLevel {

    //TAG
    public static String TAG_PRODUCT_ID = "ProductId";
    public static String TAG_PRODUCT_PRICE_LEVEL_1 = "CostLevel_Sell_1";
    public static String TAG_PRODUCT_PRICE_LEVEL_2 = "CostLevel_Sell_2";
    public static String TAG_PRODUCT_PRICE_LEVEL_3 = "CostLevel_Sell_3";
    public static String TAG_PRODUCT_PRICE_LEVEL_4 = "CostLevel_Sell_4";
    public static String TAG_PRODUCT_PRICE_LEVEL_5 = "CostLevel_Sell_5";
    public static String TAG_PRODUCT_PRICE_LEVEL_6 = "CostLevel_Sell_6";
    public static String TAG_PRODUCT_PRICE_LEVEL_7 = "CostLevel_Sell_7";
    public static String TAG_PRODUCT_PRICE_LEVEL_8 = "CostLevel_Sell_8";
    public static String TAG_PRODUCT_PRICE_LEVEL_9 = "CostLevel_Sell_9";
    public static String TAG_PRODUCT_PRICE_LEVEL_10 = "CostLevel_Sell_10";
    public static String TAG_Discount_Sell_1 = "Discount_Sell_1";
    public static String TAG_Discount_Sell_2 = "Discount_Sell_2";
    public static String TAG_Discount_Sell_3 = "Discount_Sell_3";
    public static String TAG_Discount_Sell_4 = "Discount_Sell_4";
    public static String TAG_DEFAULT_PRICE_LEVEL = "Default_CostLevel_Sell";
    public static String TAG_Last_Price_Buy = "LastPrice_Buy";
    public static String TAG_Price_Average = "PriceAverage";
    public static String TAG_modified_date = "modifieddate";
    public static String TAG_Comment = "Comment";

    ////////////////////////////

    public ProductPriceLevel() {
        ModifyDate = 0L;
        this.costLevelSell1 = 0;
        this.costLevelSell2 = 0;
        this.costLevelSell3 = 0;
        this.costLevelSell4 = 0;
        this.costLevelSell5 = 0;
        this.costLevelSell6 = 0;
        this.costLevelSell7 = 0;
        this.costLevelSell8 = 0;
        this.costLevelSell9 = 0;
        this.costLevelSell10 = 0;
        this.setDiscount_Sell_1(0);
        this.setDiscount_Sell_2(0);
        this.setDiscount_Sell_3(0);
        this.setDiscount_Sell_4(0);
        this.defaultCostLevelSell = 0;
        this.lastPriceBuy = 0;
        this.priceAverage = 0;
        this.setLastPriceBuy(0);
        this.setPriceAverage(0);
        setUserId(BaseActivity.getPrefUserId());

    }

    private Long Id;
    private Long ModifyDate;
    private long UserId;
    private String ProductId;

    private int costLevelSell1;
    private int costLevelSell2;
    private int costLevelSell3;
    private int costLevelSell4;
    private int costLevelSell5;
    private int costLevelSell6;
    private int costLevelSell7;
    private int costLevelSell8;
    private int costLevelSell9;
    private int costLevelSell10;

    private double Discount_Sell_1;
    private double Discount_Sell_2;
    private double Discount_Sell_3;
    private double Discount_Sell_4;

    private String SyncID;
    private int defaultCostLevelSell;

    private int lastPriceBuy;
    private double priceAverage;


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(Long modifyDate) {
        ModifyDate = modifyDate;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public int getCostLevelSell1() {
        return costLevelSell1;
    }

    public void setCostLevelSell1(int costLevelSell1) {
        this.costLevelSell1 = costLevelSell1;
    }

    public int getCostLevelSell2() {
        return costLevelSell2;
    }

    public void setCostLevelSell2(int costLevelSell2) {
        this.costLevelSell2 = costLevelSell2;
    }

    public int getCostLevelSell3() {
        return costLevelSell3;
    }

    public void setCostLevelSell3(int costLevelSell3) {
        this.costLevelSell3 = costLevelSell3;
    }

    public int getCostLevelSell4() {
        return costLevelSell4;
    }

    public void setCostLevelSell4(int costLevelSell4) {
        this.costLevelSell4 = costLevelSell4;
    }

    public int getCostLevelSell5() {
        return costLevelSell5;
    }

    public void setCostLevelSell5(int costLevelSell5) {
        this.costLevelSell5 = costLevelSell5;
    }

    public int getCostLevelSell6() {
        return costLevelSell6;
    }

    public void setCostLevelSell6(int costLevelSell6) {
        this.costLevelSell6 = costLevelSell6;
    }

    public int getCostLevelSell7() {
        return costLevelSell7;
    }

    public void setCostLevelSell7(int costLevelSell7) {
        this.costLevelSell7 = costLevelSell7;
    }

    public int getCostLevelSell8() {
        return costLevelSell8;
    }

    public void setCostLevelSell8(int costLevelSell8) {
        this.costLevelSell8 = costLevelSell8;
    }

    public int getCostLevelSell9() {
        return costLevelSell9;
    }

    public void setCostLevelSell9(int costLevelSell9) {
        this.costLevelSell9 = costLevelSell9;
    }

    public int getCostLevelSell10() {
        return costLevelSell10;
    }

    public void setCostLevelSell10(int costLevelSell10) {
        this.costLevelSell10 = costLevelSell10;
    }

    public String getSyncID() {
        return SyncID;
    }

    public void setSyncID(String syncID) {
        SyncID = syncID;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public int getDefaultCostLevelSell() {
        return defaultCostLevelSell;
    }

    public void setDefaultCostLevelSell(int defaultCostLevelSell) {
        this.defaultCostLevelSell = defaultCostLevelSell;
    }

    public int getLastPriceBuy() {
        return lastPriceBuy;
    }

    public void setLastPriceBuy(int lastPriceBuy) {
        this.lastPriceBuy = lastPriceBuy;
    }

    public double getPriceAverage() {
        return priceAverage;
    }

    public void setPriceAverage(double priceAverage) {
        this.priceAverage = priceAverage;
    }


    public double getDiscount_Sell_1() {
        return Discount_Sell_1;
    }

    public void setDiscount_Sell_1(double discount_Sell_1) {
        Discount_Sell_1 = discount_Sell_1;
    }

    public double getDiscount_Sell_2() {
        return Discount_Sell_2;
    }

    public void setDiscount_Sell_2(double discount_Sell_2) {
        Discount_Sell_2 = discount_Sell_2;
    }

    public double getDiscount_Sell_3() {
        return Discount_Sell_3;
    }

    public void setDiscount_Sell_3(double discount_Sell_3) {
        Discount_Sell_3 = discount_Sell_3;
    }

    public double getDiscount_Sell_4() {
        return Discount_Sell_4;
    }

    public void setDiscount_Sell_4(double discount_Sell_4) {
        Discount_Sell_4 = discount_Sell_4;
    }
}
