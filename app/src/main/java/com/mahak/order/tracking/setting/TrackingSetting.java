
package com.mahak.order.tracking.setting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackingSetting {

    @SerializedName("Result")
    @Expose
    private boolean result;
    @SerializedName("Data")
    @Expose
    private Data data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
