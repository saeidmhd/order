
package com.mahak.order.tracking.setting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeofencingSetting {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("visitorId")
    @Expose
    private int visitorId;
    @SerializedName("settingId")
    @Expose
    private int settingId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(int visitorId) {
        this.visitorId = visitorId;
    }

    public int getSettingId() {
        return settingId;
    }

    public void setSettingId(int settingId) {
        this.settingId = settingId;
    }

}
