
package com.mahak.order.tracking.setting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Objects {

    @SerializedName("Settings")
    @Expose
    private List<Setting> settings = null;
    @SerializedName("GeofencingSettings")
    @Expose
    private Object geofencingSettings;
    @SerializedName("VisitorBanks")
    @Expose
    private Object visitorBanks;

    public List<Setting> getSettings() {
        return settings;
    }

    public void setSettings(List<Setting> settings) {
        this.settings = settings;
    }

    public Object getGeofencingSettings() {
        return geofencingSettings;
    }

    public void setGeofencingSettings(Object geofencingSettings) {
        this.geofencingSettings = geofencingSettings;
    }

    public Object getVisitorBanks() {
        return visitorBanks;
    }

    public void setVisitorBanks(Object visitorBanks) {
        this.visitorBanks = visitorBanks;
    }

}
