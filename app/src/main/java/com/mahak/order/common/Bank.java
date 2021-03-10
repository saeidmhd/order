package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

public class Bank {

    // TAG
    public static String TAG_ID = "Id";
    public static String TAG_NAME = "Name";
    public static String TAG_DESCRIPTION = "Desc";
    public static String TAG_MAHAK_ID = "MahakId";
    public static String TAG_MASTER_ID = "BankCode";
    public static String TAG_DATABASE_ID = "DatabaseId";
    public static String TAG_CUSTOMER_ID = "CustomerId";
    public static String TAG_IS_DELETE = "IsDelete";
    // //////////////////////////////////////////

    private String MahakId;
    private String DatabaseId;
    private long Id;
    private long ModifyDate;
    private int CustomerId;

    @SerializedName("BankCode")
    @Expose
    private long BankCode;
    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("Description")
    @Expose
    private String Description;
    @SerializedName("Deleted")
    @Expose
    private boolean deleted;

    //new
    @SerializedName("BankId")
    @Expose
    private long bankId;

    @SerializedName("BankClientId")
    @Expose
    private long bankClientId;

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

    public Bank() {
        setId(0L);
        setBankCode(0L);
        setCustomerId(0);
        setModifyDate(0L);
        setName("");
        setDescription("");
        setMahakId(BaseActivity.getPrefMahakId());
        setDatabaseId(BaseActivity.getPrefDatabaseId());
        this.setDeleted(false);
    }

    @Override
    public String toString() {

        return this.getName();
        //return super.toString();
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getBankCode() {
        return BankCode;
    }

    public void setBankCode(long bankCode) {
        BankCode = bankCode;
    }

    public long getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public long getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(long modifyDate) {
        ModifyDate = modifyDate;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public int getDeleted() {
        return deleted ? 1 : 0;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    public long getBankId() {
        return bankId;
    }

    public void setBankId(long bankId) {
        this.bankId = bankId;
    }

    public long getBankClientId() {
        return bankClientId;
    }

    public void setBankClientId(long bankClientId) {
        this.bankClientId = bankClientId;
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


