package com.mahak.order.common;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NonRegister {

    //TAG
    public static String TAG_ID = "Id";
    public static String TAG_CUSTOMER_ID = "personId";
    public static String TAG_USER_ID = "visitorId";
    public static String TAG_NonRegister_DATE = "RegisterDate";
    public static String TAG_COMMENT = "description";
    public static String TAG_MAHAK_ID = "MahakId";
    public static String TAG_DATABASE_ID = "DatabaseId";
    public static String TAG_MASTER_ID = "notRegisterCode";
    public static String TAG_CODE = "Code";
    public static String TAG_REASON_CODE = "reasonCode";

    ///////////////////////////////////////////////////

    private long Id;
    private String MahakId;
    private String DatabaseId;
    private long ModifyDate;

    private String CustomerName;
    private String MarketName;
    private String Address;
    private String Code;
    private int Publish;


    @SerializedName("NotRegisterId")
    @Expose
    private int notRegisterId;
    @SerializedName("NotRegisterClientId")
    @Expose
    private long notRegisterClientId;
    @SerializedName("notRegisterCode")
    @Expose
    private int notRegisterCode;
    @SerializedName("PersonId")
    @Expose
    private int personId;
    @SerializedName("PersonClientId")
    @Expose
    private Long personClientId;
    @SerializedName("VisitorId")
    @Expose
    private int visitorId;
    @SerializedName("Date")
    @Expose
    private String notRegisterDate;
    @SerializedName("reasonCode")
    @Expose
    private int reasonCode;
    @SerializedName("Description")
    @Expose
    private String description;

    @SerializedName("CreateDate")
    @Expose
    private String createDate;

    @SerializedName("Deleted")
    @Expose
    private boolean deleted;
    @SerializedName("DataHash")
    @Expose
    private String dataHash;

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


    public NonRegister(long id, int personId, int visitorId, long notRegisterDate, long modifyDate, long notRegisterCode, String description, String customerName, String marketName, String address, String mahakId, String databaseId, String code, int publish, int reasonCode) {
        this.personId = 0;
        this.visitorId = visitorId;
        ModifyDate = 0L;
        this.description = "";
        CustomerName = "";
        MarketName = "";
        Address = "";
        Code = "";
        Publish = ProjectInfo.DONT_PUBLISH;
        this.reasonCode = 0;
        this.setMahakId(BaseActivity.getPrefMahakId());
        this.setDatabaseId(BaseActivity.getPrefDatabaseId());
        setVisitorId((int) BaseActivity.getPrefUserId());
    }


    public NonRegister() {
        this.setDescription("");
        this.setModifyDate(0);
        this.setPublish(ProjectInfo.DONT_PUBLISH);
        this.setMarketName("");
        this.setAddress("");
        this.setMahakId(BaseActivity.getPrefMahakId());
        this.setDatabaseId(BaseActivity.getPrefDatabaseId());

    }

    public NonRegister(long id, int customerid, int userid, long orderdate, long deliverydate, String discount, String description, int settlementtype, int immediate, int type) {

        this.setId(id);
        this.setPersonId(customerid);
        this.setVisitorId(userid);
        this.setNotRegisterDate(orderdate);
        this.setDescription(description);

    }

    public long getNotRegisterDate() {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        try {
            Date date = simpleDateFormat.parse(notRegisterDate);
            return date.getTime();
        } catch (ParseException e) {
            ServiceTools.logToFireBase(e);
            e.printStackTrace();
        }
        return -1;
    }

    public void setNotRegisterDate(long orderDate) {

        Date date = new Date();
        date.setTime(orderDate);
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        notRegisterDate = simpleDateFormat.format(date);

    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(int visitorId) {
        this.visitorId = visitorId;
    }

    public long getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(long modifyDate) {
        ModifyDate = modifyDate;
    }

    public int getNotRegisterCode() {
        return notRegisterCode;
    }

    public void setNotRegisterCode(int notRegisterCode) {
        this.notRegisterCode = notRegisterCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getMarketName() {
        return MarketName;
    }

    public void setMarketName(String marketName) {
        MarketName = marketName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
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

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public int getPublish() {
        return Publish;
    }

    public void setPublish(int publish) {
        Publish = publish;
    }

    public int getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(int reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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

    public int getNotRegisterId() {
        return notRegisterId;
    }

    public void setNotRegisterId(int notRegisterId) {
        this.notRegisterId = notRegisterId;
    }

    public long getNotRegisterClientId() {
        return notRegisterClientId;
    }

    public void setNotRegisterClientId(long notRegisterClientId) {
        this.notRegisterClientId = notRegisterClientId;
    }

    public Long getPersonClientId() {
        return personClientId;
    }

    public void setPersonClientId(Long personClientId) {
        this.personClientId = personClientId;
    }
}
