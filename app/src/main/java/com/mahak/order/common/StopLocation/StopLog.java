package com.mahak.order.common.StopLocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StopLog {

    @SerializedName("visitorId")
    @Expose
    private long visitorId;

    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("entryDate")
    @Expose
    private String entryDate;
    @SerializedName("duration")
    @Expose
    private long duration;

    @SerializedName("stoplocationclientid")
    @Expose
    private long stopLocationClientId;

    @SerializedName("latitude")
    @Expose
    private double latitude;

    @SerializedName("longitude")
    @Expose
    private double longitude;

    public long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(long visitorId) {
        this.visitorId = visitorId;
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

    public long getStopLocationClientId() {
        return stopLocationClientId;
    }

    public void setStopLocationClientId(long stopLocationClientId) {
        this.stopLocationClientId = stopLocationClientId;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getLat() {
        return latitude;
    }

    public void setLat(double lat) {
        this.latitude = lat;
    }

    public double getLng() {
        return longitude;
    }

    public void setLng(double lng) {
        this.longitude = lng;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        long HH = duration / 3600;
        long MM = (duration % 3600) / 60;
        long SS = duration % 60;

        if(HH > 0)
            sb.append(HH).append(" : ");

        if(MM > 0)
            sb.append(MM).append(" : ");

        if(SS > 0)
            sb.append(SS).append(" : ");

        String timeInHHMMSS2 = sb.toString();

        return "stopLog{" + "\n" +
                "id=" + stopLocationClientId + "\n" +
                ", lat=" + latitude + "\n" +
                ", lng=" + longitude + "\n" +
                ", entryDate='" + entryDate + '\'' + "\n" +
                ", endDate='" + endDate + '\'' + "\n" +
                ", duration='" + duration + '\'' + "\n" +
                ", duration string=" + timeInHHMMSS2 + "\n" +
                '}';
    }
}
