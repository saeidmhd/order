
package com.mahak.order.mission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Observable;

public class MissionDetail extends Observable {

    @SerializedName("MissionDetailId")
    @Expose
    private Integer missionDetailId;

    @SerializedName("VisitorId")
    @Expose(serialize = false)
    private Integer visitorId;

    @SerializedName("DatabaseId")
    @Expose(serialize = false)
    private Integer databaseId;

    @SerializedName("MissionDetailCode")
    @Expose(serialize = false)
    private Integer missionDetailCode;

    @SerializedName("MissionDetailClientId")
    @Expose(serialize = false)
    private Integer missionDetailClientId;

    @SerializedName("Status")
    @Expose
    private int status;

    @SerializedName("Priority")
    @Expose(serialize = false)
    private Integer priority;

    @SerializedName("Date")
    @Expose
    private String date;

    @SerializedName("Type")
    @Expose(serialize = false)
    private Integer type;

    @SerializedName("PersonId")
    @Expose(serialize = false)
    private Integer personId;

    @SerializedName("PersonAddressId")
    @Expose(serialize = false)
    private Integer personAddressId;

    @SerializedName("Description")
    @Expose(serialize = false)
    private String description;

    @SerializedName("ActivityID")
    @Expose(serialize = false)
    private String activityID;

    @SerializedName("MissionId")
    @Expose
    private Integer missionId;

    @SerializedName("Deleted")
    @Expose(serialize = false)
    private Boolean deleted;

    @SerializedName("RowVersion")
    @Expose(serialize = false)
    private Integer rowVersion;

    @SerializedName("PersonClientId")
    @Expose(serialize = false)
    private Integer personClientId;

    @SerializedName("PersonCode")
    @Expose(serialize = false)
    private Integer personCode;

    @SerializedName("VisitorClientId")
    @Expose(serialize = false)
    private Integer visitorClientId;

    @SerializedName("VisitorCode")
    @Expose(serialize = false)
    private Integer visitorCode;

    @SerializedName("MissionClientId")
    @Expose(serialize = false)
    private Integer missionClientId;

    @SerializedName("MissionCode")
    @Expose(serialize = false)
    private Integer missionCode;

    public int getMissionDetailId() {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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

    public void setChangeAndNotify()
    {
        setChanged();
        notifyObservers();
    }
}