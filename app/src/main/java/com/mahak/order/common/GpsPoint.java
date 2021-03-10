package com.mahak.order.common;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mostafavi on 7/12/2016.
 * modified by Saeid.mhd@gmail.com
 */
public class GpsPoint {

    private boolean isSend;

    @SerializedName("VisitorLocationId")
    @Expose
    private int visitorLocationId;
    @SerializedName("VisitorId")
    @Expose
    private long visitorId;
    @SerializedName("Latitude")
    @Expose
    private double latitude;
    @SerializedName("Longitude")
    @Expose
    private double longitude;
    @SerializedName("skipCount")
    @Expose
    private int skipCount;
    @SerializedName("CreateDate")
    @Expose
    private String date;
    @SerializedName("CreateSyncId")
    @Expose
    private int createSyncId;
    @SerializedName("RowVersion")
    @Expose
    private long rowVersion;


    public long getDate() {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        try {
            Date today = simpleDateFormat.parse(date);
            return today.getTime();
        } catch (ParseException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return -1;
    }

    public void setDate(long Locationdate) {

        Date today = new Date();
        today.setTime(Locationdate);
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        date = simpleDateFormat.format(today);

    }

    public String getLatitude() {
        return String.valueOf(latitude);
    }

    public void setLatitude(String latitude) {
        this.latitude = ServiceTools.toFloat(latitude);
    }

    public String getLongitude() {
        return String.valueOf(longitude);
    }

    public void setLongitude(String longitude) {
        this.longitude = ServiceTools.toFloat(longitude);
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(ProjectInfo._json_key_date, getDate());
            obj.put(ProjectInfo._json_key_latitude, getLatitude());
            obj.put(ProjectInfo._json_key_longitude, getLongitude());
            return obj;
        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return null;
    }

    public long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(long visitorId) {
        this.visitorId = visitorId;
    }

    public int getVisitorLocationId() {
        return visitorLocationId;
    }

    public void setVisitorLocationId(int visitorLocationId) {
        this.visitorLocationId = visitorLocationId;
    }

    public int getSkipCount() {
        return skipCount;
    }

    public void setSkipCount(int skipCount) {
        this.skipCount = skipCount;
    }

    public int getCreateSyncId() {
        return createSyncId;
    }

    public void setCreateSyncId(int createSyncId) {
        this.createSyncId = createSyncId;
    }

    public long getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(long rowVersion) {
        this.rowVersion = rowVersion;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }
}
