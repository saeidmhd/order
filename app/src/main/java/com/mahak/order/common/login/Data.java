package com.mahak.order.common.login;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Data {

    @SerializedName("UserToken")
    @Expose
    private String userToken;
    @SerializedName("SyncId")
    @Expose
    private int syncId;
    @SerializedName("VisitorId")
    @Expose
    private int visitorId;
    @SerializedName("DatabaseId")
    @Expose
    private int databaseId;
    @SerializedName("UserTitle")
    @Expose
    private String userTitle;
    @SerializedName("ServerTime")
    @Expose
    private String serverTime;
    @SerializedName("MahakId")
    @Expose
    private String mahakId;
    @SerializedName("CreditDay")
    @Expose
    private int creditDay;
    @SerializedName("HasRadara")
    @Expose
    private boolean hasRadara;
    @SerializedName("WithDataTransfer")
    @Expose
    private boolean withDataTransfer;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getSyncId() {
        return String.valueOf(syncId);
    }

    public void setSyncId(int syncId) {
        this.syncId = syncId;
    }

    public int getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(int visitorId) {
        this.visitorId = visitorId;
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public long getServerTime() {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        try {
            TimeZone timeZone = TimeZone.getTimeZone("Asia/Tehran");
            simpleDateFormat.setTimeZone(timeZone);
            Date date = simpleDateFormat.parse(serverTime);
            return date.getTime();
        } catch (ParseException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return -1;
    }

    public void setServerTime(long serverTime) {

        Date date = new Date();
        date.setTime(serverTime);
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSz";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        this.serverTime = simpleDateFormat.format(date);

    }

    public String getMahakId() {
        return mahakId;
    }

    public void setMahakId(String mahakId) {
        this.mahakId = mahakId;
    }

    public int getCreditDay() {
        return creditDay;
    }

    public void setCreditDay(int creditDay) {
        this.creditDay = creditDay;
    }

    public boolean isHasRadara() {
        return hasRadara;
    }

    public void setHasRadara(boolean hasRadara) {
        this.hasRadara = hasRadara;
    }

    public boolean isWithDataTransfer() {
        return withDataTransfer;
    }

    public void setWithDataTransfer(boolean withDataTransfer) {
        this.withDataTransfer = withDataTransfer;
    }
}