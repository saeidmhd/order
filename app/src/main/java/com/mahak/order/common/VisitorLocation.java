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
import java.util.UUID;

/**
 * Created by mostafavi on 7/12/2016.
 * modified by Saeid.mhd@gmail.com
 */
public class VisitorLocation {

    private String uniqueID ;

    private long Date;

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
    private String createDate;
    @SerializedName("CreateSyncId")
    @Expose
    private int createSyncId;
    @SerializedName("RowVersion")
    @Expose
    private long rowVersion;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long locationDate) {
        Date today = new Date();
        today.setTime(locationDate);
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        createDate = simpleDateFormat.format(today);
    }


    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(ProjectInfo._json_key_date, getCreateDate());
            obj.put(ProjectInfo._json_key_latitude, getLatitude());
            obj.put(ProjectInfo._json_key_longitude, getLongitude());
            return obj;
        } catch (JSONException e) {
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public long getDate() {
        return Date;
    }

    public void setDate(long date) {
        Date = date;
    }
}
