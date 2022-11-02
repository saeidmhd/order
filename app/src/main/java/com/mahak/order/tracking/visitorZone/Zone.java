
package com.mahak.order.tracking.visitorZone;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Zone {

    @SerializedName("zoneId")
    @Expose
    private Long zoneId;
    @SerializedName("mahakId")
    @Expose
    private String mahakId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("accountZoneLocations")
    @Expose
    private List<Object> accountZoneLocations = null;
    @SerializedName("zoneLocations")
    @Expose
    private List<ZoneLocation> zoneLocations = null;
    @SerializedName("factorRegistrationOutRange")
    @Expose
    private Boolean factorRegistrationOutRange;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("modifiedBy")
    @Expose
    private String modifiedBy;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public String getMahakId() {
        return mahakId;
    }

    public void setMahakId(String mahakId) {
        this.mahakId = mahakId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Object> getAccountZoneLocations() {
        return accountZoneLocations;
    }

    public void setAccountZoneLocations(List<Object> accountZoneLocations) {
        this.accountZoneLocations = accountZoneLocations;
    }

    public List<ZoneLocation> getZoneLocations() {
        return zoneLocations;
    }

    public void setZoneLocations(List<ZoneLocation> zoneLocations) {
        this.zoneLocations = zoneLocations;
    }

    public Boolean getFactorRegistrationOutRange() {
        return factorRegistrationOutRange;
    }

    public void setFactorRegistrationOutRange(Boolean factorRegistrationOutRange) {
        this.factorRegistrationOutRange = factorRegistrationOutRange;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

}
