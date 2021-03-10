package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * created by Saeid.mhd@gmail.com
 */
public class GpsPointSignalR {

    @SerializedName("Latitude")
    @Expose
    private double latitude;
    @SerializedName("Longitude")
    @Expose
    private double longitude;

    @SerializedName("UserToken")
    @Expose
    private String UserToken;


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

    public String getUserToken() {
        return UserToken;
    }

    public void setUserToken(String userToken) {
        this.UserToken = userToken;
    }
}
