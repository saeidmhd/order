package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

public class Setting {

    @SerializedName("SettingId")
    @Expose
    private int settingId;
    @SerializedName("SettingCode")
    @Expose
    private int settingCode;
    @SerializedName("VisitorId")
    @Expose
    private long visitorId;
    @SerializedName("Value")
    @Expose
    private String value;
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

    public int getSettingId() {
        return settingId;
    }

    public void setSettingId(int settingId) {
        this.settingId = settingId;
    }

    public int getSettingCode() {
        return settingCode;
    }

    public void setSettingCode(int settingCode) {
        this.settingCode = settingCode;
    }

    public long getVisitorId() {
        if(visitorId == 0)
            return BaseActivity.getPrefUserId();
        else
            return visitorId;
    }

    public void setVisitorId(long visitorId) {
        this.visitorId = visitorId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getDeleted() {
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
}