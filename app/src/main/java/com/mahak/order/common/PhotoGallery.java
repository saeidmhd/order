package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotoGallery {

    @SerializedName("PhotoGalleryId")
    @Expose
    private int photoGalleryId;
    @SerializedName("PictureId")
    @Expose
    private long pictureId;
    @SerializedName("EntityType")
    @Expose
    private int entityType;
    @SerializedName("ItemCode")
    @Expose
    private int itemCode;
    @SerializedName("ItemType")
    @Expose
    private int itemType;
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
    private int rowVersion;

    public int getPhotoGalleryId() {
        return photoGalleryId;
    }

    public void setPhotoGalleryId(int photoGalleryId) {
        this.photoGalleryId = photoGalleryId;
    }

    public long getPictureId() {
        return pictureId;
    }

    public void setPictureId(long pictureId) {
        this.pictureId = pictureId;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
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

    public int getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(int rowVersion) {
        this.rowVersion = rowVersion;
    }

}
