package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;


public class CustomerGroup {

    //TAG
    public static String TAG_ID = "Id";
    public static String TAG_NAME = "Name";
    public static String TAG_COLOR = "Color";
    public static String TAG_ICON = "Icon";
    public static String TAG_IS_DELETE = "IsDelete";
    public static String TAG_MAHAK_ID = "MahakId";
    public static String TAG_MASTER_ID = "personGroupCode";
    public static String TAG_DATABASE_ID = "DatabaseId";
    public static String TAG_Sell_DefaultCostLevel = "SellPriceLevel";
    //////////////////////////

    private Long Id;
    private Long ModifyDate;
    private long UserId;
    private String MahakId;
    private String DatabaseId;

    @SerializedName("SellPriceLevel")
    @Expose
    private String SellPriceLevel;

    @SerializedName("PersonGroupCode")
    @Expose
    private Long personGroupCode;

    @SerializedName("Deleted")
    @Expose
    private boolean deleted;

    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("Color")
    @Expose
    private int Color;
    @SerializedName("Icon")
    @Expose
    private String Icon;

    public CustomerGroup() {
        setMahakId(BaseActivity.getPrefMahakId());
        setDatabaseId(BaseActivity.getPrefDatabaseId());
        setUserId(BaseActivity.getPrefUserId());
    }

    public CustomerGroup(long id, String name, String icon, int color) {
        this.Id = id;
        this.Name = name;
        this.Icon = icon;
        this.Color = color;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getId() {
        return Id;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getIcon() {
        return Icon;
    }

    public void setColor(int color) {
        Color = color;
    }

    public int getColor() {
        return Color;
    }

    public void setModifyDate(Long modifyDate) {
        ModifyDate = modifyDate;
    }

    public Long getModifyDate() {
        return ModifyDate;
    }

    public void setMahakId(String mahakId) {
        MahakId = mahakId;
    }

    public String getMahakId() {
        return MahakId;
    }

    public void setPersonGroupCode(Long personGroupCode) {
        this.personGroupCode = personGroupCode;
    }

    public Long getPersonGroupCode() {
        return personGroupCode;
    }

    public void setDatabaseId(String databaseId) {
        DatabaseId = databaseId;
    }

    public String getDatabaseId() {
        return DatabaseId;
    }

    public void setSellPriceLevel(String sellPriceLevel) {
        SellPriceLevel = sellPriceLevel;
    }

    public int getDeleted() {
        return deleted ? 1 : 0;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    @SerializedName("PersonGroupId")
    @Expose
    private int personGroupId;
    @SerializedName("PersonGroupClientId")
    @Expose
    private long personGroupClientId;

    @SerializedName("DiscountPercent")
    @Expose
    private double discountPercent;

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

    public int getPersonGroupId() {
        return personGroupId;
    }

    public void setPersonGroupId(int personGroupId) {
        this.personGroupId = personGroupId;
    }

    public long getPersonGroupClientId() {
        return personGroupClientId;
    }

    public void setPersonGroupClientId(long personGroupClientId) {
        this.personGroupClientId = personGroupClientId;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
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

    public String getSellPriceLevel() {
        return SellPriceLevel;
    }
}
