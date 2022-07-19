
package com.mahak.order.mission;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAllMission {

    @SerializedName("data")
    @Expose
    private List<Mission> data = null;
    @SerializedName("succeeded")
    @Expose
    private Boolean succeeded;
    @SerializedName("message")
    @Expose
    private Object message;
    @SerializedName("errors")
    @Expose
    private List<Object> errors = null;

    public List<Mission> getData() {
        return data;
    }

    public void setData(List<Mission> data) {
        this.data = data;
    }

    public Boolean getSucceeded() {
        return succeeded;
    }

    public void setSucceeded(Boolean succeeded) {
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
