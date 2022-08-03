
package com.mahak.order.mission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MissionDetail {

    @SerializedName("MissionDetailId")
    @Expose
    private Integer missionDetailId;
    @SerializedName("VisitorId")
    @Expose
    private Integer visitorId;
    @SerializedName("DatabaseId")
    @Expose
    private Integer databaseId;
    @SerializedName("MissionDetailCode")
    @Expose
    private Integer missionDetailCode;
    @SerializedName("MissionDetailClientId")
    @Expose
    private Integer missionDetailClientId;
    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("Priority")
    @Expose
    private Integer priority;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Type")
    @Expose
    private Integer type;
    @SerializedName("PersonId")
    @Expose
    private Integer personId;
    @SerializedName("PersonAddressId")
    @Expose
    private Integer personAddressId;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("ActivityID")
    @Expose
    private String activityID;
    @SerializedName("MissionId")
    @Expose
    private Integer missionId;
    @SerializedName("Deleted")
    @Expose
    private Boolean deleted;
    @SerializedName("RowVersion")
    @Expose
    private Integer rowVersion;
    @SerializedName("PersonClientId")
    @Expose
    private Integer personClientId;
    @SerializedName("PersonCode")
    @Expose
    private Integer personCode;
    @SerializedName("VisitorClientId")
    @Expose
    private Integer visitorClientId;
    @SerializedName("VisitorCode")
    @Expose
    private Integer visitorCode;
    @SerializedName("MissionClientId")
    @Expose
    private Integer missionClientId;
    @SerializedName("MissionCode")
    @Expose
    private Integer missionCode;

    public Integer getMissionDetailId() {
        return missionDetailId;
    }

    public void setMissionDetailId(Integer missionDetailId) {
        this.missionDetailId = missionDetailId;
    }

    public Integer getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Integer visitorId) {
        this.visitorId = visitorId;
    }

    public Integer getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Integer databaseId) {
        this.databaseId = databaseId;
    }

    public Integer getMissionDetailCode() {
        return missionDetailCode;
    }

    public void setMissionDetailCode(Integer missionDetailCode) {
        this.missionDetailCode = missionDetailCode;
    }

    public Integer getMissionDetailClientId() {
        return missionDetailClientId;
    }

    public void setMissionDetailClientId(Integer missionDetailClientId) {
        this.missionDetailClientId = missionDetailClientId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Integer getPersonAddressId() {
        return personAddressId;
    }

    public void setPersonAddressId(Integer personAddressId) {
        this.personAddressId = personAddressId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    public Integer getMissionId() {
        return missionId;
    }

    public void setMissionId(Integer missionId) {
        this.missionId = missionId;
    }

    public int isDeleted() {
        return deleted ? 1 : 0;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted == 1;
    }

    public Integer getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(Integer rowVersion) {
        this.rowVersion = rowVersion;
    }

    public Integer getPersonClientId() {
        return personClientId;
    }

    public void setPersonClientId(Integer personClientId) {
        this.personClientId = personClientId;
    }

    public Integer getPersonCode() {
        return personCode;
    }

    public void setPersonCode(Integer personCode) {
        this.personCode = personCode;
    }

    public Integer getVisitorClientId() {
        return visitorClientId;
    }

    public void setVisitorClientId(Integer visitorClientId) {
        this.visitorClientId = visitorClientId;
    }

    public Integer getVisitorCode() {
        return visitorCode;
    }

    public void setVisitorCode(Integer visitorCode) {
        this.visitorCode = visitorCode;
    }

    public Integer getMissionClientId() {
        return missionClientId;
    }

    public void setMissionClientId(Integer missionClientId) {
        this.missionClientId = missionClientId;
    }

    public Integer getMissionCode() {
        return missionCode;
    }

    public void setMissionCode(Integer missionCode) {
        this.missionCode = missionCode;
    }

}