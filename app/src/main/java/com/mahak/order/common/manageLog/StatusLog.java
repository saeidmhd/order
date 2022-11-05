package com.mahak.order.common.manageLog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusLog {

    @SerializedName("accountid")
    @Expose
    private long accountid;

    @SerializedName("type")
    @Expose
    private int type;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("date")
    @Expose
    private String date;

    private int sent;

    public long getAccountid() {
        return accountid;
    }

    public void setAccountid(long accountid) {
        this.accountid = accountid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSent() {
        return sent;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }
}
