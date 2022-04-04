package com.mahak.order.common.manageLog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ManageLog {

    @SerializedName("visitorId")
    @Expose
    private long visitorId;

    @SerializedName("Log_type")
    @Expose
    private int Log_type;

    @SerializedName("Log_value")
    @Expose
    private int Log_value;

    @SerializedName("date_time")
    @Expose
    private String date_time;

    @SerializedName("manageLogClientId")
    @Expose
    private long manageLogClientId;

    @SerializedName("sent")
    @Expose
    private int sent;

    public long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(long visitorId) {
        this.visitorId = visitorId;
    }

    public int getLog_type() {
        return Log_type;
    }

    public void setLog_type(int log_type) {
        Log_type = log_type;
    }

    public int getLog_value() {
        return Log_value;
    }

    public void setLog_value(int log_value) {
        Log_value = log_value;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public long getManageLogClientId() {
        return manageLogClientId;
    }

    public void setManageLogClientId(long manageLogClientId) {
        this.manageLogClientId = manageLogClientId;
    }

    public int getSent() {
        return sent;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }
}
