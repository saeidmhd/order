package com.mahak.order.common;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;


public class CheckList {

    //TAG
    public static String TAG_ID = "Id";
    public static String TAG_USER_ID = "UserId";
    public static String TAG_STATUS = "Status";
    public static String TAG_TYPE = "Type";
    public static String TAG_CUSTOMER_ID = "personId";
    public static String TAG_DESCRIPTION = "Description";
    public static String TAG_IS_DELETE = "IsDelete";
    public static String TAG_MAHAK_ID = "MahakId";
    public static String TAG_MASTER_ID = "checklistCode";
    public static String TAG_DATABASE_ID = "DatabaseId";
    ///////////////////////

    private long Id;
    private String MahakId;
    private String DatabaseId;
    private long UserId;

    private long ModifyDate;
    private String Name;
    private String MarketName;
    private String Address;
    private double Latitude;
    private double Longitude;

    @SerializedName("PersonId")
    @Expose
    private int personId;
    @SerializedName("ChecklistCode")
    @Expose
    private long checklistCode;
    @SerializedName("Status")
    @Expose
    private int Status;
    @SerializedName("Type")
    @Expose
    private int Type;
    @SerializedName("Description")
    @Expose
    private String Description;

    @SerializedName("Deleted")
    @Expose
    private boolean deleted;

    //new
    @SerializedName("ChecklistId")
    @Expose
    private int checklistId;
    @SerializedName("ChecklistClientId")
    @Expose
    private long checklistClientId;

    @SerializedName("VisitorId")
    @Expose
    private int visitorId;

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
    @SerializedName("PersonClientId")
    @Expose
    private long personClientId;

    @SerializedName("VisitorCode")
    @Expose
    private long visitorCode;

    private long Publish;


    public CheckList() {

        this.setName("");
        this.setMarketName("");
        this.setAddress("");
        this.setLatitude(0);
        this.setLongitude(0);
        this.setDescription("");
        this.setModifyDate(0);
        this.setPublish(ProjectInfo.DONT_PUBLISH);
        this.setStatus(ProjectInfo.STATUS_NOT_DO);
        this.setMarketName("");
        setMahakId(BaseActivity.getPrefMahakId());
        setDatabaseId(BaseActivity.getPrefDatabaseId());
        setUserId(BaseActivity.getPrefUserId());
    }


    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public long getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(long modifyDate) {
        ModifyDate = modifyDate;
    }

    public long getPublish() {
        return Publish;
    }

    public void setPublish(long publish) {
        Publish = publish;
    }

    public long getChecklistCode() {
        return checklistCode;
    }

    public void setChecklistCode(long checklistCode) {
        this.checklistCode = checklistCode;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getDeleted() {
        return deleted ? 1 : 0;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
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


    public int getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(int checklistId) {
        this.checklistId = checklistId;
    }

    public long getChecklistClientId() {
        return checklistClientId;
    }

    public void setChecklistClientId(long checklistClientId) {
        this.checklistClientId = checklistClientId;
    }

    public void setChecklistCode(int checklistCode) {
        this.checklistCode = checklistCode;
    }

    public int getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(int visitorId) {
        this.visitorId = visitorId;
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

    public long getPersonClientId() {
        return personClientId;
    }

    public void setPersonClientId(long personClientId) {
        this.personClientId = personClientId;
    }

    public long getVisitorCode() {
        return visitorCode;
    }

    public void setVisitorCode(long visitorCode) {
        this.visitorCode = visitorCode;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
