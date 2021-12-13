
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
    private int sendPointsEveryMinute;
    @SerializedName("sendPointsPerMeter")
    @Expose
    private int sendPointsPerMeter;
    @SerializedName("radius")
    @Expose
    private int radius;
    @SerializedName("sendPointsBasedMeter")
    @Expose
    private boolean sendPointsBasedMeter;
    @SerializedName("userDemoTitle")
    @Expose
    private String userDemoTitle;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;

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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public boolean isSendPointsBasedMeter() {
        return sendPointsBasedMeter;
    }

    public void setSendPointsBasedMeter(boolean sendPointsBasedMeter) {
        this.sendPointsBasedMeter = sendPointsBasedMeter;
    }

    public String getUserDemoTitle() {
        return userDemoTitle;
    }

    public void setUserDemoTitle(String userDemoTitle) {
        this.userDemoTitle = userDemoTitle;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
