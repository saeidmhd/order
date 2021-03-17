
package com.mahak.order.tracking.visitorZone;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Zone {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("mahakId")
    @Expose
    private String mahakId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("zoneLocations")
    @Expose
    private List<ZoneLocation> zoneLocations = null;
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

    public List<ZoneLocation> getZoneLocations() {
        return zoneLocations;
    }

    public void setZoneLocations(List<ZoneLocation> zoneLocations) {
        this.zoneLocations = zoneLocations;
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
