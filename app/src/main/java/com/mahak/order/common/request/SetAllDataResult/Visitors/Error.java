package com.mahak.order.common.request.SetAllDataResult.Visitors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Error {

    @SerializedName("Property")
    @Expose
    private String property;
    @SerializedName("Code")
    @Expose
    private long code;
    @SerializedName("Error")
    @Expose
    private String error;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}