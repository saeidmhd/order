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


public class Cheque {

    //TAG
    public static String TAG_ID = "Id";
    public static String TAG_RECEIPT_ID = "ReceiptId";
    public static String TAG_NUMBER = "Number";
    public static String TAG_BANK = "bankName";
    public static String TAG_BANK_ID = "BankId";
    public static String TAG_BRANCH = "Branch";
    public static String TAG_AMOUNT = "Amount";
    public static String TAG_DATE = "Date";
    public static String TAG_TYPE = "Type";
    public static String TAG_DESCRIPTION = "Description";
    public static String TAG_MAHAK_ID = "MahakId";
    public static String TAG_DATABASE_ID = "DatabaseId";
    public static String TAG_MASTER_ID = "chequeCode";

    /////////////////////////////////

    private long Id;
    private long ModifyDate;
    private int Publish;
    private String MahakId;
    private String DatabaseId;

    @SerializedName("ChequeClientId")
    @Expose
    private long chequeClientId;

    @SerializedName("BankClientId")
    @Expose(serialize = false)
    private long bankClientId;

    @SerializedName("ChequeCode")
    private long chequeCode;

    @SerializedName("ReceiptId")
    @Expose(serialize = false)
    private long ReceiptId;

    @SerializedName("ReceiptClientId")
    @Expose
    private long ReceiptClientId;

    @SerializedName("BankId")
    @Expose
    private String BankId;

    @SerializedName("Number")
    @Expose
    private String Number;

    @SerializedName("BankName")
    @Expose
    private String bankName;

    @SerializedName("Branch")
    @Expose
    private String Branch;

    @SerializedName("Amount")
    @Expose
    private BigDecimal Amount;

    @SerializedName("Date")
    @Expose
    private String Date;

    @SerializedName("Type")
    @Expose
    private int Type;

    @SerializedName("Description")
    @Expose
    private String Description;

    @SerializedName("ChequeId")
    @Expose
    private int chequeId;

    @SerializedName("Deleted")
    @Expose
    private boolean deleted;

    @SerializedName("DataHash")
    @Expose(serialize = false)
    private String dataHash;
    @SerializedName("CreateDate")
    @Expose(serialize = false)
    private String createDate;
    @SerializedName("UpdateDate")
    @Expose(serialize = false)
    private String updateDate;
    @SerializedName("CreateSyncId")
    @Expose(serialize = false)
    private int createSyncId;
    @SerializedName("UpdateSyncId")
    @Expose(serialize = false)
    private int updateSyncId;
    @SerializedName("RowVersion")
    @Expose(serialize = false)
    private long rowVersion;

    public Cheque() {

        this.setBranch("");
        this.setAmount(0.0);
        this.setBankName("");
        this.setDescription("");
        this.setNumber("");
        this.setModifyDate(0);
        this.setDate(0);
        this.setPublish(ProjectInfo.DONT_PUBLISH);
        setMahakId(BaseActivity.getPrefMahakId());
        setDatabaseId(BaseActivity.getPrefDatabaseId());
    }

    public Cheque(long id, long receiptid, String bank, String number, String branch, double amount, String description) {
        // TODO Auto-generated constructor stub
        this.setId(id);
        this.setReceiptId(receiptid);
        this.setBankName(bank);
        this.setNumber(number);
        this.setBranch(branch);
        this.setAmount(amount);
        this.setDescription(description);
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getReceiptId() {
        return ReceiptId;
    }

    public void setReceiptId(long receiptId) {
        ReceiptId = receiptId;
    }

    public long getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(long modifyDate) {
        ModifyDate = modifyDate;
    }

    public long getDate() {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        try {
            java.util.Date date = simpleDateFormat.parse(Date);
            return date != null ? date.getTime() : 0;
        } catch (ParseException e) {
            ServiceTools.logToFireBase(e);
            e.printStackTrace();
        }
        return -1;
    }

    public void setDate(long orderDate) {

        Date date = new Date();
        date.setTime(orderDate);
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        Date = simpleDateFormat.format(date);

    }

    public long getChequeCode() {
        return chequeCode;
    }

    public void setChequeCode(long chequeCode) {
        this.chequeCode = chequeCode;
    }

    public String getBankId() {
        return BankId;
    }

    public void setBankId(String bankId) {
        BankId = bankId;
    }

    public int getPublish() {
        return Publish;
    }

    public void setPublish(int publish) {
        Publish = publish;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public double getAmount() {
        return Amount.doubleValue();
    }

    public void setAmount(double amount) {
        Amount = new BigDecimal(amount);
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
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

    //new

    public int getChequeId() {
        return chequeId;
    }

    public void setChequeId(int chequeId) {
        this.chequeId = chequeId;
    }

    public long getChequeClientId() {
        return chequeClientId;
    }

    public void setChequeClientId(long chequeClientId) {
        this.chequeClientId = chequeClientId;
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

    public long getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(long rowVersion) {
        this.rowVersion = rowVersion;
    }

    public long getReceiptClientId() {
        return ReceiptClientId;
    }

    public void setReceiptClientId(long receiptClientId) {
        ReceiptClientId = receiptClientId;
    }

    public long getBankClientId() {
        return bankClientId;
    }

    public void setBankClientId(long bankClientId) {
        this.bankClientId = bankClientId;
    }


}
