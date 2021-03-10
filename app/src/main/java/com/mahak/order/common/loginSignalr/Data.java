package com.mahak.order.common.loginSignalr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("userName")
    @Expose
    private String UserName;

    @SerializedName("password")
    @Expose
    private String Password;
    @SerializedName("mahakID")
    @Expose
    private String MahakID;
    @SerializedName("createTime")
    @Expose
    private String CreateTime;
    @SerializedName("ip")
    @Expose
    private String IP;
    @SerializedName("language")
    @Expose
    private String Language;
    @SerializedName("databaseId")
    @Expose
    private int DatabaseId;
    @SerializedName("syncId")
    @Expose
    private int SyncId;
    @SerializedName("visitorId")
    @Expose
    private int VisitorId;


    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getMahakID() {
        return MahakID;
    }

    public void setMahakID(String mahakID) {
        MahakID = mahakID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public int getDatabaseId() {
        return DatabaseId;
    }

    public void setDatabaseId(int databaseId) {
        DatabaseId = databaseId;
    }

    public int getSyncId() {
        return SyncId;
    }

    public void setSyncId(int syncId) {
        SyncId = syncId;
    }

    public int getVisitorId() {
        return VisitorId;
    }

    public void setVisitorId(int visitorId) {
        VisitorId = visitorId;
    }


}
