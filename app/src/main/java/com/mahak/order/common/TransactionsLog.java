package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

import java.math.BigDecimal;


public class TransactionsLog {

    //TAG
    public static String TAG_ID = "Id";
    public static String TAG_CUSTOMER_ID = "personId";
    public static String TAG_TRANSACTION_ID = "TransactionId";
    public static String TAG_TYPE = "Type";
    public static String TAG_DEBIT_AMOUNT = "debtAmount";
    public static String TAG_CREDIT_AMOUNT = "CreditAmount";
    public static String TAG_REMAINED_AMOUNT = "balance";
    public static String TAG_STATUSE = "Status";
    public static String TAG_TRANSACTION_DATE = "TransactionDate";
    public static String TAG_DESCRIPTION = "Description";
    public static String TAG_IS_DELETE = "IsDelete";
    public static String TAG_MAHAK_ID = "MahakId";
    public static String TAG_MASTER_ID = "transactionCode";
    public static String TAG_DATABASE_ID = "DatabaseId";
    /////////////////////////////////////////////////////

    private long Id;
    private String MahakId;
    private String DatabaseId;
    private long ModifyDate;


    @SerializedName("Deleted")
    @Expose
    private boolean deleted;

    @SerializedName("DebtAmount")
    @Expose
    private BigDecimal debtAmount;

    @SerializedName("CreditAmount")
    @Expose
    private BigDecimal CreditAmount;

    @SerializedName("Balance")
    @Expose
    private BigDecimal balance;

    @SerializedName("TransactionCode")
    @Expose
    private long transactionCode;

    @SerializedName("PersonId")
    @Expose
    private long personId;

    @SerializedName("Status")
    @Expose
    private int Status;

    @SerializedName("Description")
    @Expose
    private String Description;

    @SerializedName("Type")
    @Expose
    private int Type;

    @SerializedName("Date")
    @Expose
    private String TransactionDate;

    @SerializedName("TransactionId")
    @Expose
    private long TransactionId;
    //new
    @SerializedName("TransactionClientId")
    @Expose
    private long transactionClientId;

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

    public TransactionsLog() {
        this.setMahakId(BaseActivity.getPrefMahakId());
        this.setDatabaseId(BaseActivity.getPrefDatabaseId());
    }

    public void setId(long id) {
        Id = id;
    }

    public long getId() {
        return Id;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public long getPersonId() {
        return personId;
    }

    public void setTransactionId(long transactionId) {
        TransactionId = transactionId;
    }

    public long getTransactionId() {
        return TransactionId;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getType() {
        return Type;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getStatus() {
        return Status;
    }

    public void setDebtAmount(double debtAmount) {
        this.debtAmount = new BigDecimal(debtAmount);
    }

    public double getDebtAmount() {
        return debtAmount.doubleValue();
    }

    public void setCreditAmount(double creditAmount) {
        CreditAmount = new BigDecimal(creditAmount);

    }

    public double getCreditAmount() {
        return CreditAmount.doubleValue();
    }

    public double getBalance() {
        return balance.doubleValue();
    }

    public void setBalance(double mBalance) {
        balance = new BigDecimal(mBalance);
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDescription() {
        return Description;
    }

    public void setModifyDate(long modifyDate) {
        ModifyDate = modifyDate;
    }

    public long getModifyDate() {
        return ModifyDate;
    }

    public int getDeleted() {
        return deleted ? 1 : 0;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setMahakId(String mahakId) {
        MahakId = mahakId;
    }

    public String getMahakId() {
        return MahakId;
    }

    public void setTransactionCode(Long transactionCode) {
        this.transactionCode = transactionCode;
    }

    public Long getTransactionCode() {
        return transactionCode;
    }

    public void setDatabaseId(String databaseId) {
        DatabaseId = databaseId;
    }

    public String getDatabaseId() {
        return DatabaseId;
    }

    public void setTransactionDate(String transactionDate) {
        TransactionDate = transactionDate;
    }

    public String getTransactionDate() {

        return TransactionDate;
    }

    public long getTransactionClientId() {
        return transactionClientId;
    }

    public void setTransactionClientId(long transactionClientId) {
        this.transactionClientId = transactionClientId;
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
