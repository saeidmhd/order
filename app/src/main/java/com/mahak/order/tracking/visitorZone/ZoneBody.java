package com.mahak.order.tracking.visitorZone;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZoneBody {
    @SerializedName("visitorId")
    @Expose
    private long visitorId;

    public long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(long visitorId) {
        this.visitorId = visitorId;
    }
}
