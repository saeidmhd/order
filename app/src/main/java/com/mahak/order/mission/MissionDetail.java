
package com.mahak.order.mission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MissionDetail {

    @SerializedName("missionDetailId")
    @Expose
    private int missionDetailId;
    @SerializedName("databaseId")
    @Expose
    private int databaseId;
    @SerializedName("missionDetailCode")
    @Expose
    private int missionDetailCode;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("priority")
    @Expose
    private int priority;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("personId")
    @Expose
    private int personId;
    @SerializedName("accountId")
    @Expose
    private int accountId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("activityID")
    @Expose
    private String activityID;
    @SerializedName("missionId")
    @Expose
    private int missionId;
    @SerializedName("deleted")
    @Expose
    private boolean deleted;
    @SerializedName("dataHash")
    @Expose
    private String dataHash;
    @SerializedName("createDate")
    @Expose
    private String createDate;
    @SerializedName("rowVersion")
    @Expose
    private int rowVersion;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("personLatitude")
    @Expose
    private Double personLatitude;
    @SerializedName("personLongitude")
    @Expose
    private Double personLongitude;

    public int getMissionDetailId() {
        return missionDetailId;
    }

    public void setMissionDetailId(int missionDetailId) {
        this.missionDetailId = missionDetailId;
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public int getMissionDetailCode() {
        return missionDetailCode;
    }

    public void setMissionDetailCode(int missionDetailCode) {
        this.missionDetailCode = missionDetailCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
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

    public int getMissionId() {
        return missionId;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(int rowVersion) {
        this.rowVersion = rowVersion;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getPersonLatitude() {
        return personLatitude;
    }

    public void setPersonLatitude(Double personLatitude) {
        this.personLatitude = personLatitude;
    }

    public Double getPersonLongitude() {
        return personLongitude;
    }

    public void setPersonLongitude(Double personLongitude) {
        this.personLongitude = personLongitude;
    }

}