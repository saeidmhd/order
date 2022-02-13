package com.mahak.order.common.StopLocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StopLocationResponse {

    @SerializedName("data")
    @Expose
    private Object data;
    @SerializedName("succeeded")
    @Expose
    private boolean succeeded;
    @SerializedName("message")
    @Expose
    private Object message;
    @SerializedName("errors")
    @Expose
    private List<Object> errors = null;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public List<Object> getErrors() {
        return errors;
    }

    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }
}
