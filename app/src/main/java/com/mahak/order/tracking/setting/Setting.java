
package com.mahak.order.tracking.setting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Setting {

    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("MahakId")
    @Expose
    private int mahakId;
    @SerializedName("ControlSendingPointsByAdmin")
    @Expose
    private boolean controlSendingPointsByAdmin;
    @SerializedName("SendingPoints")
    @Expose
    private boolean sendingPoints;
    @SerializedName("SendPointsEveryMinute")
    @Expose
    private int sendPointsEveryMinute;
    @SerializedName("SendPointsPerMeter")
    @Expose
    private int sendPointsPerMeter;
    @SerializedName("DefaultLatitude")
    @Expose
    private double defaultLatitude;
    @SerializedName("DefaultLongitude")
    @Expose
    private double defaultLongitude;
    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;
    @SerializedName("Created")
    @Expose
    private String created;
    @SerializedName("LastModifiedBy")
    @Expose
    private String lastModifiedBy;
    @SerializedName("LastModified")
    @Expose
    private String lastModified;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isControlSendingPointsByAdmin() {
        return controlSendingPointsByAdmin;
    }

    public void setControlSendingPointsByAdmin(boolean controlSendingPointsByAdmin) {
        this.controlSendingPointsByAdmin = controlSendingPointsByAdmin;
    }

    public boolean isSendingPoints() {
        return sendingPoints;
    }

    public void setSendingPoints(boolean sendingPoints) {
        this.sendingPoints = sendingPoints;
    }

    public int getSendPointsEveryMinute() {
        return sendPointsEveryMinute;
    }

    public void setSendPointsEveryMinute(int sendPointsEveryMinute) {
        this.sendPointsEveryMinute = sendPointsEveryMinute;
    }

    public int getSendPointsPerMeter() {
        return sendPointsPerMeter;
    }

    public void setSendPointsPerMeter(int sendPointsPerMeter) {
        this.sendPointsPerMeter = sendPointsPerMeter;
    }


    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getMahakId() {
        return mahakId;
    }

    public void setMahakId(int mahakId) {
        this.mahakId = mahakId;
    }

    public double getDefaultLatitude() {
        return defaultLatitude;
    }

    public void setDefaultLatitude(double defaultLatitude) {
        this.defaultLatitude = defaultLatitude;
    }

    public double getDefaultLongitude() {
        return defaultLongitude;
    }

    public void setDefaultLongitude(double defaultLongitude) {
        this.defaultLongitude = defaultLongitude;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
