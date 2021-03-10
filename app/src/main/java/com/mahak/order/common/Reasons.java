package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

public class Reasons {
    // TAG
    public static String TAG_MASTER_ID = "MasterID";
    public static String TAG_TYPE = "Type";
    public static String TAG_COMMENT = "Comment";
    public static String TAG_TITLE = "Title";
    public static String TAG_ModifiedDate = "ModifiedDate";
    public static String TAG_ID = "ID";

    // //////////////////////////////////////////

    public Reasons() {
        ModifyDate = 0L;
        this.type = 0;
        this.description = "";
        this.name = "";
        this.setMahakId(BaseActivity.getPrefMahakId());
        this.setDatabaseId(BaseActivity.getPrefDatabaseId());
    }

    private Long Id;
    private Long ModifyDate;
    private String MahakId;
    private String DatabaseId;

    @SerializedName("ReturnReasonId")
    @Expose
    private int returnReasonId;

    @SerializedName("ReturnReasonClientId")
    @Expose
    private long returnReasonClientId;

    @SerializedName("Description")
    @Expose
    private String description;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("ReturnReasonCode")
    @Expose
    private int returnReasonCode;

    @SerializedName("Type")
    @Expose
    private int type;

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

    public int getReturnReasonCode() {
        return returnReasonCode;
    }

    public void setReturnReasonCode(int returnReasonCode) {
        this.returnReasonCode = returnReasonCode;
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

    public void setModifyDate(Long modifyDate) {
        ModifyDate = modifyDate;
    }

    public Long getModifyDate() {
        return ModifyDate;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getReturnReasonId() {
        return returnReasonId;
    }

    public void setReturnReasonId(int returnReasonId) {
        this.returnReasonId = returnReasonId;
    }

    public long getReturnReasonClientId() {
        return returnReasonClientId;
    }

    public void setReturnReasonClientId(long returnReasonClientId) {
        this.returnReasonClientId = returnReasonClientId;
    }
}
