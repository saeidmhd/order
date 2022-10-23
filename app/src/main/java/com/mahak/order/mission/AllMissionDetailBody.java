package com.mahak.order.mission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllMissionDetailBody {

    @SerializedName("FromMissionDetailVersion")
    @Expose
    private Long fromMissionDetailVersion;

    public Long getFromMissionDetailVersion() {
        return fromMissionDetailVersion;
    }

    public void setFromMissionDetailVersion(Long fromMissionDetailVersion) {
        this.fromMissionDetailVersion = fromMissionDetailVersion;
    }

}
