package com.mahak.order.common.manageLog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ManageLog {
    @SerializedName("statuslogs")
    @Expose
    private ArrayList<StatusLog> statusLogs = null;

    public ArrayList<StatusLog> getStatusLogs() {
        return statusLogs;
    }

    public void setStatusLogs(ArrayList<StatusLog> statuslogs) {
        this.statusLogs = statuslogs;
    }
}
