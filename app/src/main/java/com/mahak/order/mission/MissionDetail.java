
package com.mahak.order.mission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MissionDetail {

    @SerializedName("missionDetailId")
    @Expose
    private Long missionDetailId;
    @SerializedName("databaseId")
    @Expose
    private Long databaseId;
    @SerializedName("missionDetailCode")
    @Expose
    private Long missionDetailCode;
    @SerializedName("status")
    @Expose
    private Long status;
    @SerializedName("priority")
    @Expose
    private Long priority;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("type")
    @Expose
    private Long type;
    @SerializedName("personId")
    @Expose
    private Long personId;
    @SerializedName("accountId")
    @Expose
    private Long accountId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("activityID")
    @Expose
    private String activityID;
    @SerializedName("missionId")
    @Expose
    private Long missionId;
    @SerializedName("deleted")
    @Expose
    private Boolean deleted;
    @SerializedName("dataHash")
    @Expose
    private String dataHash;
    @SerializedName("createDate")
    @Expose
    private String createDate;
    @SerializedName("rowVersion")
    @Expose
    private Long rowVersion;
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

    public Long getMissionDetailId() {
        return missionDetailId;
    }

    public void setMissionDetailId(Long missionDetailId) {
        this.missionDetailId = missionDetailId;
    }

    public Long getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Long databaseId) {
        this.databaseId = databaseId;
    }

    public Long getMissionDetailCode() {
        return missionDetailCode;
    }

    public void setMissionDetailCode(Long missionDetailCode) {
        this.missionDetailCode = missionDetailCode;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
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

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

    public int isDeleted() {
        return deleted ? 1 : 0;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted == 1;
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

    public Long getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(Long rowVersion) {
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
