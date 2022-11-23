
package com.mahak.order.mission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mission {

    @SerializedName("MissionId")
    @Expose
    private Integer missionId;

    @SerializedName("DatabaseId")
    @Expose(serialize = false)
    private Integer databaseId;

    @SerializedName("MissionCode")
    @Expose(serialize = false)
    private Integer missionCode;

    @SerializedName("AccountId")
    @Expose(serialize = false)
    private Integer accountId;

    @SerializedName("MissionClientId")
    @Expose(serialize = false)
    private Integer missionClientId;

    @SerializedName("StatusAdmin")
    @Expose(serialize = false)
    private Integer statusAdmin;

    @SerializedName("Date")
    @Expose
    private String date;

    @SerializedName("StatusDate")
    @Expose
    private String statusDate;

    @SerializedName("Deleted")
    @Expose(serialize = false)
    private Boolean deleted;

    @SerializedName("Description")
    @Expose(serialize = false)
    private String description;

    @SerializedName("RowVersion")
    @Expose(serialize = false)
    private Integer rowVersion;

    @SerializedName("CreateDate")
    @Expose(serialize = false)
    private String createDate;

    @SerializedName("UpdateDate")
    @Expose(serialize = false)
    private String updateDate;

    private Integer status;
    private Integer publish;

    public Integer getMissionId() {
        return missionId;
    }

    public void setMissionId(Integer missionId) {
        this.missionId = missionId;
    }

    public Integer getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Integer databaseId) {
        this.databaseId = databaseId;
    }

    public Integer getMissionCode() {
        return missionCode;
    }

    public void setMissionCode(Integer missionCode) {
        this.missionCode = missionCode;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getMissionClientId() {
        return missionClientId;
    }

    public void setMissionClientId(Integer missionClientId) {
        this.missionClientId = missionClientId;
    }

    public Integer getStatusAdmin() {
        return statusAdmin;
    }

    public void setStatusAdmin(Integer statusAdmin) {
        this.statusAdmin = statusAdmin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
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

    public Integer getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(Integer rowVersion) {
        this.rowVersion = rowVersion;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPublish() {
        return publish;
    }

    public void setPublish(Integer publish) {
        this.publish = publish;
    }
}