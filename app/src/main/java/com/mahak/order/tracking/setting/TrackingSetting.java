
package com.mahak.order.tracking.setting;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackingSetting {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("succeeded")
    @Expose
    private boolean succeeded;
    @SerializedName("message")
    @Expose
    private Object message;
    @SerializedName("errors")
    @Expose
    private List<Object> errors = null;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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
