package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StopLog {

    @SerializedName("Id")
    @Expose
    private long id;
    @SerializedName("Lat")
    @Expose
    private double lat;
    @SerializedName("Lng")
    @Expose
    private double lng;
    @SerializedName("EntryDate")
    @Expose
    private String entryDate;
    @SerializedName("Duration")
    @Expose
    private long duration;
    @SerializedName("EndDate")
    @Expose
    private String endDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
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

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "stopLog{" + "\n" +
                "id=" + id + "\n" +
                ", lat=" + lat + "\n" +
                ", lng=" + lng + "\n" +
                ", entryDate='" + entryDate + '\'' + "\n" +
                ", duration=" + duration + "\n" +
                ", endDate='" + endDate + '\'' + "\n" +
                ", duration in minute ='" + (duration / 60) + '\'' + "\n" +
                '}';
    }
}
