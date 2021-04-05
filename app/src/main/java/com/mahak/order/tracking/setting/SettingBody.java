package com.mahak.order.tracking.setting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingBody {
    @SerializedName("visitorId")
    @Expose
    private int visitorId;

    public int getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(int visitorId) {
        this.visitorId = visitorId;
    }
}
