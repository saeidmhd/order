package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;


/**
 * Created by mostafavi on 7/2/2016.
 */
public class PicturesProduct {

    public static final String TAG_PICTUTE_ID = "PictureID";
    public static final String TAG_PRODUCT_ID = "ProductCode";
    public static final String TAG_MASTER_ID = "MasterID";
    public static final String TAG_FILE_NAME = "FileName";
    public static final String TAG_TITLE = "Title";
    public static final String TAG_URL = "Url";
    public static final String TAG_FILE_SIZE = "FileSize";
    public static final String TAG_IS_DELETED = "Deleted";
    public static final String TAG_RowVersion = "RowVersion";

    public PicturesProduct() {
        this.setMahakId(BaseActivity.getPrefMahakId());
        this.setDataBaseId(BaseActivity.getPrefDatabaseId());
        setUserId(BaseActivity.getPrefUserId());
    }

    private long _id;
    private long lastUpdate;
    private String mahakId;
    private String dataBaseId;
    private long userId;
    private boolean isLoaded;


    @SerializedName("ItemId")
    @Expose
    private long itemId;

    @SerializedName("ItemType")
    @Expose
    private long itemType;

    @SerializedName("PictureCode")
    @Expose
    private long pictureCode;

    @SerializedName("PictureId")
    @Expose
    private long pictureId;

    @SerializedName("ProductId")
    @Expose
    private long productId;

    @SerializedName("ProductCode")
    @Expose
    private long productCode;

    @SerializedName("FileName")
    @Expose
    private String fileName;

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Url")
    @Expose
    private String url;
    @SerializedName("FileSize")
    @Expose
    private long fileSize;
    @SerializedName("Width")
    @Expose
    private int width;
    @SerializedName("Height")
    @Expose
    private int height;

    @SerializedName("PictureClientId")
    @Expose
    private long pictureClientId;

    @SerializedName("DisplayOrder")
    @Expose
    private int displayOrder;

    @SerializedName("Format")
    @Expose
    private String format;
    @SerializedName("PictureHash")
    @Expose
    private String pictureHash;

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

    @SerializedName("Deleted")
    @Expose
    private boolean deleted;


    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getPictureId() {
        return pictureId;
    }

    public void setPictureId(long pictureId) {
        this.pictureId = pictureId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getPictureCode() {
        return pictureCode;
    }

    public void setPictureCode(long pictureCode) {
        this.pictureCode = pictureCode;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getMahakId() {
        return mahakId;
    }

    public void setMahakId(String mahakId) {
        this.mahakId = mahakId;
    }

    public String getDataBaseId() {
        return dataBaseId;
    }

    public void setDataBaseId(String dataBaseId) {
        this.dataBaseId = dataBaseId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }


    public long getPictureClientId() {
        return pictureClientId;
    }

    public void setPictureClientId(long pictureClientId) {
        this.pictureClientId = pictureClientId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPictureHash() {
        return pictureHash;
    }

    public void setPictureHash(String pictureHash) {
        this.pictureHash = pictureHash;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getProductCode() {
        return productCode;
    }

    public void setProductCode(long productCode) {
        this.productCode = productCode;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getItemType() {
        return itemType;
    }

    public void setItemType(long itemType) {
        this.itemType = itemType;
    }
}
