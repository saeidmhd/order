package com.mahak.order.common;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Receipt {

    //TAG
    public static String TAG_ID = "Id";
    public static String TAG_CUSTOMER_ID = "PersonId";
    public static String TAG_USER_ID = "visitorId";
    public static String TAG_CASH_AMOUNT = "CashAmount";
    public static String TAG_MODIFY_DATE = "ModifyDate";
    public static String TAG_DATE = "Date";
    public static String TAG_CUSTOMER_NAME = "CustomerName";
    public static String TAG_DESCRIPTION = "Description";
    public static String TAG_MAHAK_ID = "MahakId";
    public static String TAG_DATABASE_ID = "DatabaseId";
    public static String TAG_MASTER_ID = "ReceiptCode";
    public static String TAG_CODE = "TrackingCode";
    public static String TAG_CASH_CODE = "CashCode";

    /////////////////////////////////////

    private Long Id;

    private Long ModifyDate;
    private String MahakId;
    private String DatabaseId;
    private String CustomerName;

    private double TotalAmount;
    private double TotalCheque;
    private double TotalCashReceipt;


    @SerializedName("ReceiptId")
    @Expose
    private int receiptId;

    @SerializedName("ReceiptClientId")
    @Expose
    private long receiptClientId;

    @SerializedName("ReceiptCode")
    @Expose
    private Long ReceiptCode;

    @SerializedName("PersonId")
    @Expose
    private int PersonId;

    @SerializedName("PersonClientId")
    @Expose
    private Long personClientId;

    @SerializedName("VisitorId")
    @Expose
    private long visitorId;

    @SerializedName("CashAmount")
    @Expose
    private BigDecimal CashAmount;

    @SerializedName("CashCode")
    @Expose
    private String CashCode;

    @SerializedName("Description")
    @Expose
    private String Description;

    @SerializedName("Date")
    @Expose
    private String Date;

    @SerializedName("TrackingCode")
    @Expose
    private String trackingCode;

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

    private int Publish;

    private List<Cheque> items;

    public Receipt() {
        this.Description = "";
        setCashAmount(0.0);
        this.CustomerName = "";
        this.Publish = ProjectInfo.DONT_PUBLISH;
        this.trackingCode = ProjectInfo.DONT_CODE;
        this.setMahakId(BaseActivity.getPrefMahakId());
        this.setDatabaseId(BaseActivity.getPrefDatabaseId());
        setVisitorId(BaseActivity.getPrefUserId());
    }

    public Receipt(long id, int customerid, int userid, double cashamount) {

        this.setId(id);
        this.setPersonId(customerid);
        this.setVisitorId(userid);
        this.setCashAmount(cashamount);
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getId() {
        return Id;
    }

    public void setPersonId(int personId) {
        PersonId = personId;
    }

    public int getPersonId() {
        return PersonId;
    }

    public void setVisitorId(long visitorId) {
        this.visitorId = visitorId;
    }

    public long getVisitorId() {
        return visitorId;
    }

    public void setCashAmount(double cashAmount) {
        CashAmount = new BigDecimal(cashAmount);
    }

    public double getCashAmount() {
        if (CashAmount != null)
            return CashAmount.doubleValue();
        else
            return 0;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDescription() {
        return Description;
    }

    public void setModifyDate(Long modifyDate) {
        ModifyDate = modifyDate;
    }

    public Long getModifyDate() {
        return ModifyDate;
    }

    public void setPublish(int publish) {
        Publish = publish;
    }

    public int getPublish() {
        return Publish;
    }


    public void setItems(List<Cheque> items) {
        this.items = items;
    }

    public List<Cheque> getItems() {
        return items;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalCashReceipt(double totalCashReceipt) {
        TotalCashReceipt = totalCashReceipt;
    }

    public double getTotalCashReceipt() {
        return TotalCashReceipt;
    }

    public void setTotalCheque(double totalCheque) {
        TotalCheque = totalCheque;
    }

    public double getTotalCheque() {
        return TotalCheque;
    }

    public void setMahakId(String mahakId) {
        MahakId = mahakId;
    }

    public String getMahakId() {
        return MahakId;
    }

    public void setDatabaseId(String databaseId) {
        DatabaseId = databaseId;
    }

    public String getDatabaseId() {
        return DatabaseId;
    }

    public void setReceiptCode(Long materId) {
        ReceiptCode = materId;
    }

    public Long getReceiptCode() {
        return ReceiptCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public String getCashCode() {
        return CashCode;
    }

    public void setCashCode(String cashCode) {
        CashCode = cashCode;
    }

    public long getDate() {

        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        try {
            java.util.Date date = simpleDateFormat.parse(Date);
            return date.getTime();
        } catch (ParseException e) {
            ServiceTools.logToFireBase(e);
            e.printStackTrace();
        }
        return -1;
    }

    public void setDate(long deliveryDate) {

        Date date = new Date();
        date.setTime(deliveryDate);
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        Date = simpleDateFormat.format(date);
    }


    //new

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public long getReceiptClientId() {
        return receiptClientId;
    }

    public void setReceiptClientId(long receiptClientId) {
        this.receiptClientId = receiptClientId;
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

    public Long getPersonClientId() {
        return personClientId;
    }

    public void setPersonClientId(Long personClientId) {
        this.personClientId = personClientId;
    }
}
