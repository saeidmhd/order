package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

public class PromotionEntity {

    //tags

    public static String TAG_SyncID = "SyncID";
    public static String TAG_CodePromotion = "CodePromotion";
    public static String TAG_OtherFields = "OtherFields";
    public static String TAG_CreatedBy = "CreatedBy";
    public static String TAG_CreatedDate = "CreatedDate";
    public static String TAG_ModifiedBy = "ModifiedBy";
    public static String TAG_ModifiedDate = "ModifiedDate";

    public static String TAG_CodeEntity = "CodeEntity";
    public static String TAG_CodePromotionEntity = "CodePromotionEntity";
    public static String TAG_EntityType = "EntityType";

    //Entity Type
    public static int Visitor = 1;
    public static int Customer = 2;
    public static int GroupCustomer = 3;
    public static int Good = 4;
    public static int GroupGood = 5;

    private int Id;
    private long ModifyDate;
    private long UserId;
    private String MahakId;
    private String DatabaseId;

    private int codeEntity;
    private int codePromotionEntity;
    private int entityType;

    private String modifiedBy;
    private String syncID;
    private String modifiedDate;
    private String createdBy;
    private String createdDate;

    public PromotionEntity() {
        this.setMahakId(BaseActivity.getPrefMahakId());
        this.setDatabaseId(BaseActivity.getPrefDatabaseId());
        setUserId(BaseActivity.getPrefUserId());
    }


    @SerializedName("OtherFields")
    @Expose
    private String promotionEntityOtherFields;

    @SerializedName("PromotionId")
    @Expose
    private int promotionId;

    @SerializedName("PromotionEntityId")
    @Expose
    private int promotionEntityId;
    @SerializedName("PromotionEntityClientId")
    @Expose
    private int promotionEntityClientId;

    @SerializedName("PromotionCode")
    @Expose
    private int promotionCode;

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


    public String getSyncID() {
        return syncID;
    }

    public void setSyncID(String syncID) {
        this.syncID = syncID;
    }

    public String getPromotionCode() {
        return String.valueOf(promotionCode);
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = ServiceTools.toInt(promotionCode);
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Long getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(Long modifyDate) {
        ModifyDate = modifyDate;
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
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

    public int getCodeEntity() {
        return codeEntity;
    }

    public void setCodeEntity(int codeEntity) {
        this.codeEntity = codeEntity;
    }

    public int getCodePromotionEntity() {
        return codePromotionEntity;
    }

    public void setCodePromotionEntity(int codePromotionEntity) {
        this.codePromotionEntity = codePromotionEntity;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getPromotionEntityId() {
        return promotionEntityId;
    }

    public void setPromotionEntityId(int promotionEntityId) {
        this.promotionEntityId = promotionEntityId;
    }

    public int getPromotionEntityClientId() {
        return promotionEntityClientId;
    }

    public void setPromotionEntityClientId(int promotionEntityClientId) {
        this.promotionEntityClientId = promotionEntityClientId;
    }

    public String getPromotionEntityOtherFields() {
        return promotionEntityOtherFields;
    }

    public void setPromotionEntityOtherFields(String promotionEntityOtherFields) {
        this.promotionEntityOtherFields = promotionEntityOtherFields;
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

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }
}