
package com.mahak.order.tracking.visitorZone;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZoneLocation {

    @SerializedName("zoneLocationId")
    @Expose
    private Long zoneLocationId;
    @SerializedName("zoneId")
    @Expose
    private Long zoneId;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("index")
    @Expose
    private Long index;

    public Long getZoneLocationId() {
        return zoneLocationId;
    }

    public void setZoneLocationId(Long zoneLocationId) {
        this.zoneLocationId = zoneLocationId;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

}
