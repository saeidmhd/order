package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

public class PropertyDescription {

    private String DatabaseId;
    private long Id;

    @SerializedName("PropertyDescriptionId")
    @Expose
    private long propertyDescriptionId;

    @SerializedName("PropertyDescriptionClientId")
    @Expose
    private long propertyDescriptionClientId;

    @SerializedName("PropertyDescriptionCode")
    @Expose
    private long propertyDescriptionCode;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("Title")
    @Expose
    private String title;

    @SerializedName("EmptyTitle")
    @Expose
    private String emptyTitle;

    @SerializedName("DataType")
    @Expose
    private int dataType;

    @SerializedName("DisplayType")
    @Expose
    private int displayType;

    @SerializedName("ExtraData")
    @Expose
    private String extraData;

    @SerializedName("Description")
    @Expose
    private String description;

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

    public PropertyDescription() {
        setId(0L);
        setName("");
        setDatabaseId(BaseActivity.getPrefDatabaseId());
        this.setDeleted(false);
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatabaseId() {
        return DatabaseId;
    }

    public void setDatabaseId(String databaseId) {
        DatabaseId = databaseId;
    }

    public int getDeleted() {
        return deleted ? 1 : 0;
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

    public long getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(long rowVersion) {
        this.rowVersion = rowVersion;
    }

    public long getPropertyDescriptionId() {
        return propertyDescriptionId;
    }

    public void setPropertyDescriptionId(long propertyDescriptionId) {
        this.propertyDescriptionId = propertyDescriptionId;
    }

    public long getPropertyDescriptionClientId() {
        return propertyDescriptionClientId;
    }

    public void setPropertyDescriptionClientId(long propertyDescriptionClientId) {
        this.propertyDescriptionClientId = propertyDescriptionClientId;
    }

    public long getPropertyDescriptionCode() {
        return propertyDescriptionCode;
    }

    public void setPropertyDescriptionCode(long propertyDescriptionCode) {
        this.propertyDescriptionCode = propertyDescriptionCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmptyTitle() {
        return emptyTitle;
    }

    public void setEmptyTitle(String emptyTitle) {
        this.emptyTitle = emptyTitle;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


