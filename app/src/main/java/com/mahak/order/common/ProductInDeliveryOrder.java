package com.mahak.order.common;

import com.mahak.order.BaseActivity;

public class ProductInDeliveryOrder {

    // TAG
    public static String TAG_ID = "Id";
    public static String TAG_PRODUCT_ID = "ProductId";
    public static String TAG_DELIVERY_ID = "DeliveryId";
    public static String TAG_PRICE = "Price";
    public static String TAG_COUNT = "Count";
    public static String TAG_GIFT = "Gift";
    public static String TAG_DESCRIPTION = "Description";
    public static String TAG_MAHAK_ID = "MahakId";
    public static String TAG_DATABASE_ID = "DatabaseId";
    public static String TAG_MASTER_ID = "MasterId";
    public static String TAG_ISDELETE = "IsDelete";
    public static String TAG_TAX_PERCENT = "TaxPercent";
    public static String TAG_CHARGE_PERCENT = "ChargePercent";
    public static String TAG_OFF_PERCENT = "OffPercent";
    // /////////////////////////////////

    private long Id;
    private long ProductId;
    private long DeliveryId;
    private long ModifyDate;
    private long MasterId;
    private int Gift;
    private int Publish;
    private int IsDelete;
    private String Price;
    private String PriceProduct;
    private String FinalPrice;
    private String MahakId;
    private String DatabaseId;
    private String OffPercent;
    private String TaxPercent;
    private String ChargePercent;
    private String Description;
    private String ProductName;
    private double Count;
    private double Min;
    private double Max;


    public ProductInDeliveryOrder() {
        this.setCount(0);
        this.setGift(0);
        this.setMin(0);
        this.setPrice("0");
        this.setPriceProduct("0");
        this.setFinalPrice("0");
        this.setTaxPercent("0");
        this.setChargePercent("0");
        this.setOffPercent("0");
        this.setDescription("");
        this.setMahakId(BaseActivity.getPrefMahakId());
        this.setDatabaseId(BaseActivity.getPrefDatabaseId());
    }


    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getProductId() {
        return ProductId;
    }

    public void setProductId(long productId) {
        ProductId = productId;
    }

    public long getDeliveryId() {
        return DeliveryId;
    }

    public void setDeliveryId(long deliveryId) {
        DeliveryId = deliveryId;
    }

    public long getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(long modifyDate) {
        ModifyDate = modifyDate;
    }

    public long getMasterId() {
        return MasterId;
    }

    public void setMasterId(long masterId) {
        MasterId = masterId;
    }

    public int getGift() {
        return Gift;
    }

    public void setGift(int gift) {
        Gift = gift;
    }

    public int getPublish() {
        return Publish;
    }

    public void setPublish(int publish) {
        Publish = publish;
    }

    public int getIsDelete() {
        return IsDelete;
    }

    public void setIsDelete(int isDelete) {
        IsDelete = isDelete;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPriceProduct() {
        return PriceProduct;
    }

    public void setPriceProduct(String priceProduct) {
        PriceProduct = priceProduct;
    }

    public String getFinalPrice() {
        return FinalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        FinalPrice = finalPrice;
    }

    public String getMahakId() {
        return MahakId;
    }

    public void setMahakId(String mahakId) {
        MahakId = mahakId;
    }

    public String getDatabaseId() {
        return DatabaseId;
    }

    public void setDatabaseId(String databaseId) {
        DatabaseId = databaseId;
    }

    public String getOffPercent() {
        return OffPercent;
    }

    public void setOffPercent(String offPercent) {
        OffPercent = offPercent;
    }

    public String getTaxPercent() {
        return TaxPercent;
    }

    public void setTaxPercent(String taxPercent) {
        TaxPercent = taxPercent;
    }

    public String getChargePercent() {
        return ChargePercent;
    }

    public void setChargePercent(String chargePercent) {
        ChargePercent = chargePercent;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public double getCount() {
        return Count;
    }

    public void setCount(double count) {
        Count = count;
    }

    public double getMin() {
        return Min;
    }

    public void setMin(double min) {
        Min = min;
    }

    public double getMax() {
        return Max;
    }

    public void setMax(double max) {
        Max = max;
    }
}
