package com.mahak.order.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class OrderDetail implements Parcelable {

    private int Id;

    private String productName;

    private int min;

    private String priceProduct;

    private String finalPrice;

    private int costLevel;

    private String FixedOff;


    private BigDecimal sumCountBaJoz;

    @SerializedName("OrderDetailId")
    @Expose
    private int orderDetailId;

    @SerializedName("OrderDetailClientId")
    @Expose
    private long orderDetailClientId;

    @SerializedName("OrderId")
    @Expose(serialize = false)
    private long orderId;

    @SerializedName("OrderClientId")
    @Expose
    private long orderClientId;

    @SerializedName("ProductDetailId")
    @Expose
    private int productDetailId;

    @SerializedName("ProductId")
    @Expose
    private int productId;

    @SerializedName("Price")
    @Expose
    private BigDecimal price;

    @SerializedName("Count1")
    @Expose
    private BigDecimal count1;

    @SerializedName("Count2")
    @Expose
    private BigDecimal count2;

    @SerializedName("Gift")
    @Expose
    private int giftType;

    @SerializedName("PromotionCode")
    @Expose
    private int promotionCode;

    @SerializedName("DiscountType")
    @Expose
    private long discountType;

    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("TaxPercent")
    @Expose
    private BigDecimal taxPercent;
    @SerializedName("ChargePercent")
    @Expose
    private BigDecimal chargePercent;
    @SerializedName("Discount")
    @Expose
    private BigDecimal discount;
    @SerializedName("Deleted")
    @Expose
    private boolean deleted;
    @SerializedName("DataHash")
    @Expose(serialize = false)
    private String dataHash;
    @SerializedName("CreateDate")
    @Expose(serialize = false)
    private String createDate;
    @SerializedName("UpdateDate")
    @Expose(serialize = false)
    private String updateDate;
    @SerializedName("CreateSyncId")
    @Expose(serialize = false)
    private int createSyncId;
    @SerializedName("UpdateSyncId")
    @Expose(serialize = false)
    private int updateSyncId;
    @SerializedName("RowVersion")
    @Expose(serialize = false)
    private long rowVersion;


    private BigDecimal giftCount1;

    private BigDecimal giftCount2;


    public static final Creator<OrderDetail> CREATOR = new Creator<OrderDetail>() {
        @Override
        public OrderDetail createFromParcel(Parcel in) {
            return new OrderDetail(in);
        }

        @Override
        public OrderDetail[] newArray(int size) {
            return new OrderDetail[size];
        }
    };

    public OrderDetail() {
        // TODO: 12/29/2018 set count2 should be considered

        this.setCount2(0);
        this.setCount1(0);
        this.setSumCountBaJoz(0);
        this.setOrderDetailId(0);
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public long getOrderDetailClientId() {
        return orderDetailClientId;
    }

    public void setOrderDetailClientId(long orderDetailClientId) {
        this.orderDetailClientId = orderDetailClientId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(int productDetailId) {
        this.productDetailId = productDetailId;
    }

    public double getPrice() {
        return price.doubleValue();
    }

    public void setPrice(String price) {
        this.price = new BigDecimal(price);
    }

    public double getCount1() {
        return count1.doubleValue();
    }

    public void setCount1(double count1) {
        this.count1 = new BigDecimal(count1);
    }

    public double getSumCountBaJoz() {
        return sumCountBaJoz.doubleValue();
    }

    public void setSumCountBaJoz(double sumCountBaJoz) {
        this.sumCountBaJoz = new BigDecimal(sumCountBaJoz);
    }

    public double getCount2() {
        return count2.doubleValue();
    }

    public void setCount2(double count2) {
        this.count2 = new BigDecimal(count2);
    }

    public int getGiftType() {
        return giftType;
    }

    public void setGiftType(int giftType) {
        this.giftType = giftType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTaxPercent() {
        if (taxPercent != null)
            return taxPercent.doubleValue();
        else
            return 0;
    }

    public void setTaxPercent(double taxPercent) {
        this.taxPercent = new BigDecimal(taxPercent);
    }

    public double getChargePercent() {
        if (chargePercent != null)
            return chargePercent.doubleValue();
        else
            return 0;
    }

    public void setChargePercent(double chargePercent) {
        this.chargePercent = new BigDecimal(chargePercent);
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

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        if(getGiftType() == Promotion.Eshantion_Dasti || getGiftType() == Promotion.Eshantion_Tarhi ){
            productName = " *اشانتیون*- " + productName;
        }
        this.productName = productName;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public String getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(String priceProduct) {
        this.priceProduct = priceProduct;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setPromotionCode(int promotionCode) {
        this.promotionCode = promotionCode;
    }

    public int getPromotionCode() {
        return promotionCode;
    }

    public void setCostLevel(int costLevel) {
        this.costLevel = costLevel;
    }

    public int getCostLevel() {
        return costLevel;
    }

    public String getFixedOff() {
        return FixedOff;
    }

    public void setFixedOff(String fixedOff) {
        FixedOff = fixedOff;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeLong(getOrderId());
        dest.writeLong(getProductDetailId());
        dest.writeDouble(getMin());
        dest.writeInt(getGiftType());
        dest.writeDouble(getCount1());
        dest.writeDouble(getPrice());
        dest.writeString(getPriceProduct());
        dest.writeString(getProductName());
        dest.writeString(getDescription());
        dest.writeDouble(getChargePercent());
        dest.writeDouble(getTaxPercent());
        dest.writeDouble(getDiscount());
        dest.writeInt(getProductId());
    }

    public long getOrderClientId() {
        return orderClientId;
    }

    public void setOrderClientId(long orderClientId) {
        this.orderClientId = orderClientId;
    }

    public OrderDetail(Parcel in) {
        Id = in.readInt();
        productName = in.readString();
        min = in.readInt();
        priceProduct = in.readString();
        finalPrice = in.readString();
        promotionCode = in.readInt();
        costLevel = in.readInt();
        FixedOff = in.readString();
        giftType = in.readInt();
        orderDetailId = in.readInt();
        orderDetailClientId = in.readLong();
        orderId = in.readInt();
        productDetailId = in.readInt();
        description = in.readString();
        taxPercent = BigDecimal.valueOf(in.readDouble());
        chargePercent = BigDecimal.valueOf(in.readDouble());
        deleted = in.readByte() != 0;
        dataHash = in.readString();
        createDate = in.readString();
        updateDate = in.readString();
        createSyncId = in.readInt();
        updateSyncId = in.readInt();
        rowVersion = in.readLong();
        productId = in.readInt();
    }

    public long getDiscountType() {
        return discountType;
    }

    public void setDiscountType(long discountType) {
        this.discountType = discountType;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getGiftCount2() {
        return giftCount2.doubleValue();
    }

    public void setGiftCount2(double giftCount2) {
        this.giftCount2 = new BigDecimal(giftCount2);
    }

    public double getGiftCount1() {
        return giftCount1.doubleValue();
    }

    public void setGiftCount1(double giftCount1) {
        this.giftCount1 = new BigDecimal(giftCount1);
    }
}