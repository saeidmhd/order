
package com.mahak.order.mission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Mission {

    @SerializedName("missionId")
    @Expose
    private Long missionId;
    @SerializedName("databaseId")
    @Expose
    private Long databaseId;
    @SerializedName("missionCode")
    @Expose
    private Long missionCode;
    @SerializedName("accountId")
    @Expose
    private Long accountId;
    @SerializedName("missionClientId")
    @Expose
    private Long missionClientId;
    @SerializedName("status")
    @Expose
    private Long status;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("deleted")
    @Expose
    private Boolean deleted;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("rowVersion")
    @Expose
    private Long rowVersion;
    private List<MissionDetail> items;

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

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
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

    public void setItems(List<MissionDetail> items) {
        this.items = items;
    }

    public List<MissionDetail> getItems() {
        return items;
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

}
