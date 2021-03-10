package com.mahak.order.common;

//Create by Saeid.mhd@gmail.com

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

public class ProductPriceLevelName {

    //TAG
    public static String TAG_Sync_ID = "SyncId";
    public static String TAG_PRICE_LEVEL_NAME = "name";
    public static String TAG_PRICE_LEVEL_CODE = "code";

    ////////////////////////////

    public ProductPriceLevelName() {
        setModifyDate(0L);
        setPriceLevelName("");
        PriceLevelCode = 0;
        setUserId(BaseActivity.getPrefUserId());

    }

    private Long Id;
    private Long ModifyDate;
    private long UserId;
    private String SyncID;

    @SerializedName("CostLevelNameId")
    @Expose
    private int costLevelNameId;

    @SerializedName("CostLevelNameClientId")
    @Expose
    private long costLevelNameClientId;

    @SerializedName("CostLevelNameCode")
    @Expose
    private int PriceLevelCode;

    @SerializedName("Name")
    @Expose
    private String PriceLevelName;

    @SerializedName("CreateDate")
    @Expose
    private String createDate;

    @SerializedName("Deleted")
    @Expose
    private boolean deleted;

    @SerializedName("DataHash")
    @Expose
    private String dataHash;

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

    public String getSyncID() {
        return SyncID;
    }

    public void setSyncID(String syncID) {
        SyncID = syncID;
    }

    public String getPriceLevelName() {
        return PriceLevelName;
    }

    public void setPriceLevelName(String priceLevelName) {
        PriceLevelName = priceLevelName;
    }

    public int getPriceLevelCode() {
        return (PriceLevelCode);
    }

    public void setPriceLevelCode(int priceLevelCode) {
        PriceLevelCode = priceLevelCode;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public int getCostLevelNameId() {
        return costLevelNameId;
    }

    public void setCostLevelNameId(int costLevelNameId) {
        this.costLevelNameId = costLevelNameId;
    }

    public long getCostLevelNameClientId() {
        return costLevelNameClientId;
    }

    public void setCostLevelNameClientId(long costLevelNameClientId) {
        this.costLevelNameClientId = costLevelNameClientId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
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
