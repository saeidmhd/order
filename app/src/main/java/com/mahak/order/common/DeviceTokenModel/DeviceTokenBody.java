package com.mahak.order.common.DeviceTokenModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceTokenBody  {

    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    @SerializedName("appId")
    @Expose
    private String appId;
    @SerializedName("appVersion")
    @Expose
    private String appVersion;
    @SerializedName("gCode")
    @Expose
    private String gCode;
    @SerializedName("deviceToken")
    @Expose
    private String deviceToken;
    @SerializedName("os")
    @Expose
    private String os;
    @SerializedName("osVersion")
    @Expose
    private String osVersion;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("mahakId")
    @Expose
    private String mahakId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getgCode() {
        return gCode;
    }

    public void setgCode(String gCode) {
        this.gCode = gCode;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getMahakId() {
        return mahakId;
    }

    public void setMahakId(String mahakId) {
        this.mahakId = mahakId;
    }
}
