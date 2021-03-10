package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class VisitorProduct {

    @SerializedName("VisitorProductId")
    @Expose
    private int visitorProductId;

    @SerializedName("ProductDetailId")
    @Expose
    private long productDetailId;

    @SerializedName("VisitorId")
    @Expose
    private int visitorId;

    @SerializedName("Count1")
    @Expose
    private double count1;

    @SerializedName("Count2")
    @Expose
    private double count2;

    @SerializedName("Price")
    @Expose
    private int price;

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


    public int getVisitorProductId() {
        return visitorProductId;
    }

    public void setVisitorProductId(int visitorProductId) {
        this.visitorProductId = visitorProductId;
    }

    public long getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(long productDetailId) {
        this.productDetailId = productDetailId;
    }

    public int getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(int visitorId) {
        this.visitorId = visitorId;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSerials() {
        return serials;
    }

    public void setSerials(String serials) {
        this.serials = serials;
    }

    public int getDelete() {
        return deleted ? 1 : 0;
    }

    public void setDelete(int delete) {
        this.deleted = delete == 1;
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
}