
package com.mahak.order.tracking.visitorZone;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Datum {

    @SerializedName("accountZoneLocationId")
    @Expose
    private Long accountZoneLocationId;
    @SerializedName("databaseId")
    @Expose
    private Long databaseId;
    @SerializedName("accountId")
    @Expose
    private Long accountId;
    @SerializedName("zoneId")
    @Expose
    private Long zoneId;
    @SerializedName("factorRegistrationOutRange")
    @Expose
    private Boolean factorRegistrationOutRange;
    @SerializedName("zone")
    @Expose
    private Zone zone;
    @SerializedName("visitor")
    @Expose
    private String visitor;
    @SerializedName("database")
    @Expose
    private String database;
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

    public Long getAccountZoneLocationId() {
        return accountZoneLocationId;
    }

    public void setAccountZoneLocationId(Long accountZoneLocationId) {
        this.accountZoneLocationId = accountZoneLocationId;
    }

    public Long getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Long databaseId) {
        this.databaseId = databaseId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public Boolean getFactorRegistrationOutRange() {
        return factorRegistrationOutRange;
    }

    public void setFactorRegistrationOutRange(Boolean factorRegistrationOutRange) {
        this.factorRegistrationOutRange = factorRegistrationOutRange;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public String getVisitor() {
        return visitor;
    }

    public void setVisitor(String visitor) {
        this.visitor = visitor;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
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
