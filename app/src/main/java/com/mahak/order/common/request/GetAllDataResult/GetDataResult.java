package com.mahak.order.common.request.GetAllDataResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDataResult {

    @SerializedName("Result")
    @Expose
    private boolean result;
    @SerializedName("Code")
    @Expose
    private long code;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private Data data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}