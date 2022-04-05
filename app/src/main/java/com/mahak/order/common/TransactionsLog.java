package com.mahak.order.common;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


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
        if (debtAmount != null)
            return debtAmount.doubleValue();
        else
            return 0;
    }

    public void setCreditAmount(double creditAmount) {
        CreditAmount = new BigDecimal(creditAmount);

    }

    public double getCreditAmount() {
        if (CreditAmount != null)
            return CreditAmount.doubleValue();
        else
            return 0;
    }

    public double getBalance() {
        if (balance != null)
            return balance.doubleValue();
        else
            return 0;
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

    public String getTransactionDate(){
        return TransactionDate;
    }

    public String getFormattedTransactionDate() {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        try {
            java.util.Date date = simpleDateFormat.parse(TransactionDate);
            return ServiceTools.getDateForLong(date.getTime());
        } catch (ParseException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return "";
    }

    /*public void setTransactionDate(String transactionDate) {
        String pattern = "yyyy-MM-ddTHH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        TransactionDate = simpleDateFormat.format(transactionDate);
    }*/

    public void setTransactionDate(String transactionDate) {
        TransactionDate = transactionDate;
    }

   /* public void setTransactionDate(String transactionDate) {
        TransactionDate = transactionDate;
    }

    public String getTransactionDate() {

        return TransactionDate;
    }*/


}
