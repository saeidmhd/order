package com.mahak.order.common.request.SetAllDataResult.Settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Settings {

    @SerializedName("Results")
    @Expose
    private List<Result> Results = null;

    public List<Result> getSettingsResults() {
        return Results;
    }

    public void setSettingsResults(List<Result> settingsResults) {
        this.Results = settingsResults;
    }

}