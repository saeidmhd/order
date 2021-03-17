
package com.mahak.order.tracking.visitorZone;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZoneLocation {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("zoneId")
    @Expose
    private int zoneId;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("lastModifiedBy")
    @Expose
    private String lastModifiedBy;
    @SerializedName("lastModified")
    @Expose
    private String lastModified;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

}
