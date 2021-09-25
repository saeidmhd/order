
package com.mahak.order.tracking.setting;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("controlSendingPointsByAdmin")
    @Expose
    private boolean controlSendingPointsByAdmin;
    @SerializedName("sendingPoints")
    @Expose
    private boolean sendingPoints;
    @SerializedName("sendPointsEveryMinute")
    @Expose
    private String sendPointsEveryMinute;
    @SerializedName("sendPointsPerMeter")
    @Expose
    private String sendPointsPerMeter;
    @SerializedName("radius")
    @Expose
    private String radius;
    @SerializedName("geofencingSetting")
    @Expose
    private List<GeofencingSetting> geofencingSetting = null;

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

    public String getSendPointsEveryMinute() {
        return sendPointsEveryMinute;
    }

    public void setSendPointsEveryMinute(String sendPointsEveryMinute) {
        this.sendPointsEveryMinute = sendPointsEveryMinute;
    }

    public String getSendPointsPerMeter() {
        return sendPointsPerMeter;
    }

    public void setSendPointsPerMeter(String sendPointsPerMeter) {
        this.sendPointsPerMeter = sendPointsPerMeter;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public List<GeofencingSetting> getGeofencingSetting() {
        return geofencingSetting;
    }

    public void setGeofencingSetting(List<GeofencingSetting> geofencingSetting) {
        this.geofencingSetting = geofencingSetting;
    }

}
