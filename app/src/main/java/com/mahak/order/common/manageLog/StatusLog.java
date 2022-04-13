package com.mahak.order.common.manageLog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusLog {

    @SerializedName("visitorId")
    @Expose
    private long visitorId;

    @SerializedName("type")
    @Expose
    private int type;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("created")
    @Expose
    private String created;

    private int sent;

    public long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(long visitorId) {
        this.visitorId = visitorId;
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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getSent() {
        return sent;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }
}
