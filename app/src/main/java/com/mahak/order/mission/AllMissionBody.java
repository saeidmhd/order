package com.mahak.order.mission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllMissionBody {

    @SerializedName("fromMissionVersion")
    @Expose
    private Long fromMissionVersion;

    public Long getFromMissionVersion() {
        return fromMissionVersion;
    }

    public void setFromMissionVersion(Long fromMissionVersion) {
        this.fromMissionVersion = fromMissionVersion;
    }

}
