package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

import java.math.BigDecimal;


public class Visitor {
    //TAG
    public static String TAG_ID = "ID";
    public static String TAG_NAME = "Name";
    public static String TAG_UserName = "username";
    public static String TAG_StoreCode = "StoreCode";
    public static String TAG_TELL = "mobile";
    public static String TAG_BankCode = "BankCode";
    public static String TAG_CashCode = "CashCode";
    public static String TAG_MASTER_ID = "MasterID";
    public static String TAG_IsDelete = "IsDelete";
    public static String TAG_PriceAccess = "hasPriceAccess";
    public static String TAG_CostLevelAccess = "hasPriceLevelAccess";
    public static String TAG_SelectedCostLevels = "selectedPriceLevels";
    public static String TAG_Sell_DefaultCostLevel = "sellPriceLevel";
    public static String TAG_ChequeCredit = "ChequeCredit";
    public static String TAG_TotalCredit = "TotalCredit";
    //////////////////////////////

    private long Id;
    private long UserId;
    private long ModifyDate;
    private int Publish;

    @SerializedName("VisitorCode")
    @Expose
    private long visitorCode;

    @SerializedName("Username")
    @Expose
    private String username;

    @SerializedName("Mobile")
    @Expose
    private String mobile;

    @SerializedName("Deleted")
    @Expose
    private boolean deleted;
    @SerializedName("HasPriceAccess")
    @Expose
    private boolean hasPriceAccess;

    @SerializedName("HasPriceLevelAccess")
    @Expose
    private boolean hasPriceLevelAccess;

    @SerializedName("SellPriceLevel")
    @Expose
    private int sellPriceLevel;

    @SerializedName("SelectedPriceLevels")
    @Expose
    private String selectedPriceLevels;

    @SerializedName("StoreCode")
    @Expose
    private long StoreCode;
    @SerializedName("CashCode")
    @Expose
    private long CashCode;
    @SerializedName("BankCode")
    @Expose
    private long BankCode;
    @SerializedName("ChequeCredit")
    @Expose
    private BigDecimal ChequeCredit;
    @SerializedName("TotalCredit")
    @Expose
    private BigDecimal TotalCredit;
    @SerializedName("Name")
    @Expose
    private String Name;
    ///
    @SerializedName("VisitorId")
    @Expose
    private int visitorId;
    @SerializedName("VisitorClientId")
    @Expose
    private long visitorClientId;

    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("PersonCode")
    @Expose
    private int personCode;

    @SerializedName("VisitorType")
    @Expose
    private int visitorType;
    @SerializedName("DeviceId")
    @Expose
    private String deviceId;

    @SerializedName("IsActive")
    @Expose
    private boolean isActive;
    @SerializedName("Color")
    @Expose
    private String color;

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


    public Visitor() {
        this.setName("");
        this.setUsername("");
        this.setMobile("");
        this.setId(0);
        this.setBankCode(0);
        this.setVisitorCode(0);
        this.setCashCode(0);
        this.setModifyDate(0);
        this.setPublish(ProjectInfo.DONT_PUBLISH);
        this.setTotalCredit(0);
        this.setChequeCredit(0);
        setUserId(BaseActivity.getPrefUserId());
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

    public long getVisitorCode() {
        return visitorCode;
    }

    public void setVisitorCode(long visitorCode) {
        this.visitorCode = visitorCode;
    }

    public long getBankCode() {
        return BankCode;
    }

    public void setBankCode(long bankCode) {
        BankCode = bankCode;
    }

    public long getCashCode() {
        return CashCode;
    }

    public void setCashCode(long cashCode) {
        CashCode = cashCode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPublish() {
        return Publish;
    }

    public void setPublish(int publish) {
        Publish = publish;
    }

    public int getDeleted() {
        return deleted ? 1 : 0;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public long getStoreCode() {
        return StoreCode;
    }

    public void setStoreCode(long storeCode) {
        StoreCode = storeCode;
    }

    public String getSelectedPriceLevels() {
        return selectedPriceLevels;
    }

    public void setSelectedPriceLevels(String selectedPriceLevels) {
        this.selectedPriceLevels = selectedPriceLevels;
    }

    public int getSellPriceLevel() {
        return sellPriceLevel;
    }

    public void setSellPriceLevel(int sellPriceLevel) {
        this.sellPriceLevel = sellPriceLevel;
    }

    public double getChequeCredit() {
        if (ChequeCredit != null)
            return ChequeCredit.doubleValue();
        else
            return 0;
    }

    public void setChequeCredit(double chequeCredit) {
        ChequeCredit = new BigDecimal(chequeCredit);
    }

    public double getTotalCredit() {
        if (TotalCredit != null)
            return TotalCredit.doubleValue();
        else
            return 0;
    }

    public void setTotalCredit(double totalCredit) {
        TotalCredit = new BigDecimal(totalCredit);
    }

    public boolean isHasPriceAccess() {
        return hasPriceAccess;
    }

    public void setHasPriceAccess(boolean hasPriceAccess) {
        this.hasPriceAccess = hasPriceAccess;
    }

    public boolean isHasPriceLevelAccess() {
        return hasPriceLevelAccess;
    }

    public void setHasPriceLevelAccess(boolean hasPriceLevelAccess) {
        this.hasPriceLevelAccess = hasPriceLevelAccess;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }


    //new

    public int getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(int visitorId) {
        this.visitorId = visitorId;
    }

    public long getVisitorClientId() {
        return visitorClientId;
    }

    public void setVisitorClientId(long visitorClientId) {
        this.visitorClientId = visitorClientId;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPersonCode() {
        return personCode;
    }

    public void setPersonCode(int personCode) {
        this.personCode = personCode;
    }

    public int getVisitorType() {
        return visitorType;
    }

    public void setVisitorType(int visitorType) {
        this.visitorType = visitorType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public int isIsActive() {
        return isActive ? 0 : 1;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive != 0;

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isDeleted() {
        return deleted;
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
