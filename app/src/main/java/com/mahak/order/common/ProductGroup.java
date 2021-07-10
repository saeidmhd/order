package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

public class ProductGroup {

    //TAG
    public static String TAG_ID = "Id";
    public static String TAG_NAME = "Name";
    public static String TAG_PARENT_ID = "ParentId";
    public static String TAG_COLOR = "Color";
    public static String TAG_ICON = "Icon";
    public static String TAG_IS_DELETE = "IsDelete";
    public static String TAG_MAHAK_ID = "MahakId";
    public static String TAG_MASTER_ID = "productCategoryCode";
    public static String TAG_DATABASE_ID = "DatabaseId";
    /////////////////////////////

    private Long Id;
    private Long ParentId;
    private String MahakId;
    private String DatabaseId;
    private long ModifyDate;

    @SerializedName("ProductCategoryCode")
    @Expose
    private Long productGroupCode;

    @SerializedName("Color")
    @Expose
    private String Color;

    @SerializedName("Name")
    @Expose
    private String Name;

    @SerializedName("Icon")
    @Expose
    private String Icon;

    @SerializedName("Deleted")
    @Expose
    private boolean deleted;

    @SerializedName("ProductCategoryId")
    @Expose
    private int productCategoryId;

    @SerializedName("ProductCategoryClientId")
    @Expose
    private long productCategoryClientId;

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


    public ProductGroup() {
        setMahakId(BaseActivity.getPrefMahakId());
        setDatabaseId(BaseActivity.getPrefDatabaseId());
    }

    public ProductGroup(long id, long parentid, String name, String icon, String color) {
        this.setId(id);
        this.setParentId(parentid);
        this.setName(name);
        this.setIcon(icon);
        this.setColor(color);
    }

    @Override
    public String toString() {

        return getName();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getParentId() {
        return ParentId;
    }

    public void setParentId(Long parentId) {
        ParentId = parentId;
    }

    public Long getProductGroupCode() {
        return productGroupCode;
    }

    public void setProductGroupCode(Long productGroupCode) {
        this.productGroupCode = productGroupCode;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
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

    public long getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(long modifyDate) {
        ModifyDate = modifyDate;
    }

    public int getDeleted() {
        return deleted ? 1 : 0;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    //new

    public int getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(int productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public long getProductCategoryClientId() {
        return productCategoryClientId;
    }

    public void setProductCategoryClientId(long productCategoryClientId) {
        this.productCategoryClientId = productCategoryClientId;
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
