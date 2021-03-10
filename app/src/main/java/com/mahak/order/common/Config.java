package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

public class Config {

    public static String TAG_MAHAK_ID = "MahakId";
    public static String TAG_DATABASE_ID = "DatabaseId";
    public static String TAG_USER_ID = "visitorId";
    public static String TAG_MASTER_ID = "MasterId";
    public static String TAG_TYPE = "Type";
    public static String TAG_VALUE = "value";
    public static String TAG_TITLE = "Title";
    public static String TAG_DESCRIPTION = "Description";
    public static String TAG_CODE = "settingCode";
    public static String TAG_ISDELETE = "deleted";

    public static String TYPE_TAX = "Tax";
    public static String TYPE_CHARGE = "Charge";
    public static String TYPE_DISCOUNT = "SaleRowDiscount";
    public static String TYPE_ChargeTax = "ChargeTax";

    private long Id;
    private long ModifyDate;
    private String DatabaseId;
    private String MahakId;
    private String Title;
    private String Description;

    private String TaxPercent;
    private String ChargePercent;
    private int MasterId;
    private String Type;


    @SerializedName("SettingCode")
    @Expose
    private int settingCode;

    @SerializedName("SettingId")
    @Expose
    private int settingId;

    @SerializedName("VisitorId")
    @Expose
    private long visitorId;
    @SerializedName("value")
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

    public Config() {
        setTaxPercent("0");
        setChargePercent("0");
        setMahakId(BaseActivity.getPrefMahakId());
        setDatabaseId(BaseActivity.getPrefDatabaseId());
        setVisitorId(BaseActivity.getPrefUserId());
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(long modifyDate) {
        ModifyDate = modifyDate;
    }

    public long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(long visitorId) {
        this.visitorId = visitorId;
    }

    public long getMasterId() {
        return MasterId;
    }

    public void setMasterId(int masterId) {
        MasterId = masterId;
    }

    public String getTaxPercent() {
        return TaxPercent;
    }

    public void setTaxPercent(String taxPercent) {
        TaxPercent = taxPercent;
    }

    public String getChargePercent() {
        return ChargePercent;
    }

    public void setChargePercent(String chargePercent) {
        ChargePercent = chargePercent;
    }

    public String getDatabaseId() {
        return DatabaseId;
    }

    public void setDatabaseId(String databaseId) {
        DatabaseId = databaseId;
    }

    public String getMahakId() {
        return MahakId;
    }

    public void setMahakId(String mahakId) {
        MahakId = mahakId;
    }

    public String getSettingCode() {
        return String.valueOf(settingCode);
    }

    public void setSettingCode(String settingCode) {
        this.settingCode = ServiceTools.toInt(settingCode);
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getDelete() {
        return deleted ? 1 : 0;
    }

    public void setDelete(boolean delete) {
        this.deleted = delete;
    }
}
