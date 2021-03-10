package com.mahak.order.common;

public class User {

    //TAG
    public static String TAG_MAHAKID = "MahakId";
    public static String TAG_DATABASEID = "DatabaseId";
    public static String TAG_MASTERID = "MasterId";
    public static String TAG_PACKAGESERIAL = "PackageSerial";
    public static String TAG_NAME = "Name";
    public static String TAG_USERNAME = "UserName";
    public static String TAG_ISDELETE = "IsDelete";
    public static String TAG_TYPE = "Type";
    public static String TAG_SYNC_ID = "SyncID";
    public static String TAG_DATE_SYNC = "DateSync";
    public static String TAG_StoreCode = "StoreCode";
    public static String TAG_UserID = "UserID";
    //////////////////////////////

    private long Id;
    private long ModifyDate;
    private long LoginDate;
    private long MasterId;
    private long DateSync;
    private String UserToken;
    private String StoreCode;
    private int Type;
    private String Name;
    private String Username;
    private String Password;
    private String MahakId;
    private String PackageSerial;
    private String SyncId;
    private String DatabaseId;
    private String ServerUserID;


    public void setId(long id) {
        Id = id;
    }

    public long getId() {
        return Id;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getType() {
        return Type;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setUsername(String userName) {
        Username = userName;
    }

    public String getUsername() {
        return Username;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPassword() {
        return Password;
    }

    public void setModifyDate(long modifyDate) {
        ModifyDate = modifyDate;
    }

    public long getModifyDate() {
        return ModifyDate;
    }

    public void setLoginDate(long loginDate) {
        LoginDate = loginDate;
    }

    public long getLoginDate() {
        return LoginDate;
    }

    public void setDatabaseId(String databaseId) {
        DatabaseId = databaseId;
    }

    public String getDatabaseId() {
        return DatabaseId;
    }

    public void setMahakId(String mahakId) {
        MahakId = mahakId;
    }

    public String getMahakId() {
        return MahakId;
    }

    public void setMasterId(Long masterId) {
        MasterId = masterId;
    }

    public Long getMasterId() {
        return MasterId;
    }

    public void setPackageSerial(String packageSerial) {
        PackageSerial = packageSerial;
    }

    public String getPackageSerial() {
        return PackageSerial;
    }

    public void setSyncId(String syncId) {
        SyncId = syncId;
    }

    public String getSyncId() {
        return SyncId;
    }

    public void setDateSync(long dateSync) {
        DateSync = dateSync;
    }

    public long getDateSync() {
        return DateSync;
    }

    public String getStoreCode() {
        return StoreCode;
    }

    public void setStoreCode(String storeCode) {
        StoreCode = storeCode;
    }

    public String getServerUserID() {
        return ServerUserID;
    }

    public void setServerUserID(String serverUserID) {
        ServerUserID = serverUserID;
    }

    public String getUserToken() {
        return UserToken;
    }

    public void setUserToken(String userToken) {
        UserToken = userToken;
    }
}
