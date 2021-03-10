package com.mahak.order.common.request.SetSign;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class setSignImage {
    @SerializedName("Result")
    @Expose
    private Boolean result;
    @SerializedName("Data")
    @Expose
    private Data data;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
