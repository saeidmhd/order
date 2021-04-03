package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;


public class ProductDetail {

    @SerializedName("ProductDetailId")
    @Expose
    private int productDetailId;
    @SerializedName("ProductDetailClientId")
    @Expose
    private long productDetailClientId;
    @SerializedName("ProductDetailCode")
    @Expose
    private int productDetailCode;
    @SerializedName("ProductId")
    @Expose
    private int productId;
    @SerializedName("Properties")
    @Expose
    private String properties;
    @SerializedName("Count1")
    @Expose
    private BigDecimal count1;
    @SerializedName("Count2")
    @Expose
    private BigDecimal count2;
    @SerializedName("Barcode")
    @Expose
    private String barcode;
    @SerializedName("Price1")
    @Expose
    private BigDecimal price1;
    @SerializedName("Price2")
    @Expose
    private BigDecimal price2;
    @SerializedName("Price3")
    @Expose
    private BigDecimal price3;
    @SerializedName("Price4")
    @Expose
    private BigDecimal price4;
    @SerializedName("Price5")
    @Expose
    private BigDecimal price5;
    @SerializedName("Price6")
    @Expose
    private BigDecimal price6;
    @SerializedName("Price7")
    @Expose
    private BigDecimal price7;
    @SerializedName("Price8")
    @Expose
    private BigDecimal price8;
    @SerializedName("Price9")
    @Expose
    private BigDecimal price9;
    @SerializedName("Price10")
    @Expose
    private BigDecimal price10;
    @SerializedName("DefaultSellPriceLevel")
    @Expose
    private int defaultSellPriceLevel;
    @SerializedName("Discount")
    @Expose
    private BigDecimal discount;
    @SerializedName("Discount1")
    @Expose
    private BigDecimal discount1;
    @SerializedName("Discount2")
    @Expose
    private BigDecimal discount2;
    @SerializedName("Discount3")
    @Expose
    private BigDecimal discount3;
    @SerializedName("Discount4")
    @Expose
    private BigDecimal discount4;
    @SerializedName("DefaultDiscountLevel")
    @Expose
    private int defaultDiscountLevel;
    @SerializedName("DiscountType")
    @Expose
    private int discountType;
    @SerializedName("CustomerPrice")
    @Expose
    private BigDecimal customerPrice;
    @SerializedName("Serials")
    @Expose
    private String serials;
    @SerializedName("Deleted")
    @Expose
    private boolean deleted;
    @SerializedName("DataHash")
    @Expose
    private String dataHash;
    @SerializedName("CreateDate")
    @Expose
    private String createDate;
    @SerializedName("UpdateDate")
    @Expose
    private String updateDate;
    @SerializedName("CreateSyncId")
    @Expose
    private int createSyncId;
    @SerializedName("UpdateSyncId")
    @Expose
    private int updateSyncId;
    @SerializedName("RowVersion")
    @Expose
    private long rowVersion;

    public int getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(int productDetailId) {
        this.productDetailId = productDetailId;
    }

    public long getProductDetailClientId() {
        return productDetailClientId;
    }

    public void setProductDetailClientId(long productDetailClientId) {
        this.productDetailClientId = productDetailClientId;
    }

    public int getProductDetailCode() {
        return productDetailCode;
    }

    public void setProductDetailCode(int productDetailCode) {
        this.productDetailCode = productDetailCode;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public double getCount1() {
        if (count1 != null)
            return count1.doubleValue();
        else
            return 0;

    }

    public void setCount1(double count1) {
        this.count1 = new BigDecimal(count1);
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public double getPrice1() {
        if (price1 != null)
            return price1.doubleValue();
        else
            return 0;
    }

    public void setPrice1(double price1) {
        this.price1 = new BigDecimal(price1);
    }

    public double getPrice2() {
        if (price2 != null)
            return price2.doubleValue();
        else
            return 0;
    }

    public void setPrice2(double price2) {
        this.price2 = new BigDecimal(price2);
    }

    public double getPrice3() {
        if (price3 != null)
            return price3.doubleValue();
        else
            return 0;
    }

    public void setPrice3(double price3) {
        this.price3 = new BigDecimal(price3);
    }

    public double getPrice4() {
        if (price4 != null)
            return price4.doubleValue();
        else
            return 0;
    }

    public void setPrice4(double price4) {
        this.price4 = new BigDecimal(price4);
    }

    public double getPrice5() {
        if (price5 != null)
            return price5.doubleValue();
        else
            return 0;
    }

    public void setPrice5(double price5) {
        this.price5 = new BigDecimal(price5);
    }

    public double getPrice6() {
        if (price6 != null)
            return price6.doubleValue();
        else
            return 0;
    }

    public void setPrice6(double price6) {
        this.price6 = new BigDecimal(price6);
    }

    public double getPrice7() {
        if (price7 != null)
            return price7.doubleValue();
        else
            return 0;
    }

    public void setPrice7(double price7) {
        this.price7 = new BigDecimal(price7);
    }

    public double getPrice8() {
        if (price8 != null)
            return price8.doubleValue();
        else
            return 0;
    }

    public void setPrice8(double price8) {
        this.price8 = new BigDecimal(price8);
    }

    public double getPrice9() {
        if (price9 != null)
            return price9.doubleValue();
        else
            return 0;
    }

    public void setPrice9(double price9) {
        this.price9 = new BigDecimal(price9);
    }

    public double getPrice10() {
        if (price10 != null)
            return price10.doubleValue();
        else
            return 0;
    }

    public void setPrice10(double price10) {
        this.price10 = new BigDecimal(price10);
    }

    public int getDefaultSellPriceLevel() {
        return defaultSellPriceLevel;
    }

    public void setDefaultSellPriceLevel(int defaultSellPriceLevel) {
        this.defaultSellPriceLevel = defaultSellPriceLevel;
    }

    public double getDiscount() {
        if (discount != null)
            return discount.doubleValue();
        else
            return 0;
    }

    public void setDiscount(double discount) {
        this.discount = new BigDecimal(discount);
    }

    public String getSerials() {
        return serials;
    }

    public void setSerials(String serials) {
        this.serials = serials;
    }


    public int isDeleted() {
        return deleted ? 1 : 0;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted == 1;
    }

    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getCreateSyncId() {
        return createSyncId;
    }

    public void setCreateSyncId(int createSyncId) {
        this.createSyncId = createSyncId;
    }

    public int getUpdateSyncId() {
        return updateSyncId;
    }

    public void setUpdateSyncId(int updateSyncId) {
        this.updateSyncId = updateSyncId;
    }

    public long getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(long rowVersion) {
        this.rowVersion = rowVersion;
    }

    public double getCount2() {
        if (count2 != null)
            return count2.doubleValue();
        else
            return 0;

    }

    public void setCount2(double count2) {
        this.count2 = new BigDecimal(count2);
    }

    public double getDiscount1() {
        if (discount1 != null)
            return discount1.doubleValue();
        else
            return 0;
    }

    public void setDiscount1(double discount1) {
        this.discount1 = new BigDecimal(discount1);
    }

    public double getDiscount2() {
        if (discount2 != null)
            return discount2.doubleValue();
        else
            return 0;
    }

    public void setDiscount2(double discount2) {
        this.discount2 = new BigDecimal(discount2);
    }

    public double getDiscount3() {
        if (discount3 != null)
            return discount3.doubleValue();
        else
            return 0;
    }

    public void setDiscount3(double discount3) {
        this.discount3 = new BigDecimal(discount3);
    }

    public double getDiscount4() {
        if (discount4 != null)
            return discount4.doubleValue();
        else
            return 0;
    }

    public void setDiscount4(double discount4) {
        this.discount4 = new BigDecimal(discount4);
    }

    public int getDefaultDiscountLevel() {
        return defaultDiscountLevel;
    }

    public void setDefaultDiscountLevel(int defaultDiscountLevel) {
        this.defaultDiscountLevel = defaultDiscountLevel;
    }

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    public double getCustomerPrice() {
        if(customerPrice != null)
            return customerPrice.doubleValue();
        else
            return 0;

    }
    public void setCustomerPrice(double customerPrice) {
        this.customerPrice = new BigDecimal(customerPrice);
    }
}