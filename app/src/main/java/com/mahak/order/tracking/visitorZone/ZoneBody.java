package com.mahak.order.tracking.visitorZone;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZoneBody {
    @SerializedName("accountId")
    @Expose
    private long accountId;

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
