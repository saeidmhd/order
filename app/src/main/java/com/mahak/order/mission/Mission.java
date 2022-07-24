
package com.mahak.order.mission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Mission {

    @SerializedName("missionId")
    @Expose(serialize = false)
    private Long missionId;
    @SerializedName("databaseId")
    @Expose(serialize = false)
    private Long databaseId;
    @SerializedName("missionCode")
    @Expose(serialize = false)
    private Long missionCode;
    @SerializedName("accountId")
    @Expose(serialize = false)
    private Long accountId;
    @SerializedName("missionClientId")
    @Expose(serialize = false)
    private Long missionClientId;
    @SerializedName("status")
    @Expose(serialize = false)
    private Integer status;
    @SerializedName("date")
    @Expose(serialize = false)
    private String date;
    @SerializedName("endDate")
    @Expose(serialize = false)
    private String endDate;
    @SerializedName("deleted")
    @Expose(serialize = false)
    private Boolean deleted;
    @SerializedName("description")
    @Expose(serialize = false)
    private String description;
    @SerializedName("rowVersion")
    @Expose(serialize = false)
    private Long rowVersion;
    @SerializedName("missionDetailCount")
    @Expose(serialize = false)
    private Long missionDetailCount;
    @SerializedName("missionDetails")
    @Expose
    private List<MissionDetail> missionDetails = null;


    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getMissionClientId() {
        return missionClientId;
    }

    public void setMissionClientId(Long missionClientId) {
        this.missionClientId = missionClientId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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

    public void setMissionDetails(List<MissionDetail> items) {
        this.missionDetails = items;
    }

    public List<MissionDetail> getMissionDetails() {
        return missionDetails;
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

    public Long getMissionDetailCount() {
        return missionDetailCount;
    }

    public void setMissionDetailCount(Long missionDetailCount) {
        this.missionDetailCount = missionDetailCount;
    }


}
