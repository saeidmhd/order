
package com.mahak.order.mission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Mission {

    @SerializedName("MissionId")
    @Expose
    private Integer missionId;

    @SerializedName("DatabaseId")
    @Expose(serialize = false)
    private Long databaseId;

    @SerializedName("MissionCode")
    @Expose(serialize = false)
    private Long missionCode;

    @SerializedName("VisitorId")
    @Expose(serialize = false)
    private Long visitorId;

    @SerializedName("MissionClientId")
    @Expose(serialize = false)
    private Long missionClientId;

    @SerializedName("Status")
    @Expose
    private int status;

    @SerializedName("Date")
    @Expose
    private String date;

    @SerializedName("EndDate")
    @Expose
    private String endDate;

    @SerializedName("Deleted")
    @Expose(serialize = false)
    private Boolean deleted;

    @SerializedName("Description")
    @Expose(serialize = false)
    private String description;

    @SerializedName("RowVersion")
    @Expose(serialize = false)
    private Long rowVersion;

    @SerializedName("VisitorClientId")
    @Expose(serialize = false)
    private Long visitorClientId;

    @SerializedName("VisitorCode")
    @Expose(serialize = false)
    private Long visitorCode;

    private List<MissionDetail> missionDetails;

    public Integer getMissionId() {
        return missionId;
    }

    public void setMissionId(Integer missionId) {
        this.missionId = missionId;
    }

    public Long getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Long databaseId) {
        this.databaseId = databaseId;
    }

    public Long getMissionCode() {
        return missionCode;
    }

    public void setMissionCode(Long missionCode) {
        this.missionCode = missionCode;
    }

    public Long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Long visitorId) {
        this.visitorId = visitorId;
    }

    public Long getMissionClientId() {
        return missionClientId;
    }

    public void setMissionClientId(Long missionClientId) {
        this.missionClientId = missionClientId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int isDeleted() {
        return deleted ? 1 : 0;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted == 1;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(Long rowVersion) {
        this.rowVersion = rowVersion;
    }

    public Long getVisitorClientId() {
        return visitorClientId;
    }

    public void setVisitorClientId(Long visitorClientId) {
        this.visitorClientId = visitorClientId;
    }

    public Long getVisitorCode() {
        return visitorCode;
    }

    public void setVisitorCode(Long visitorCode) {
        this.visitorCode = visitorCode;
    }

    public List<MissionDetail> getMissionDetails() {
        return missionDetails;
    }

    public void setMissionDetails(List<MissionDetail> missionDetails) {
        this.missionDetails = missionDetails;
    }
}