
package com.mahak.order.mission;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAllMissionDetail {

    @SerializedName("data")
    @Expose
    private List<MissionDetail> data = null;
    @SerializedName("succeeded")
    @Expose
    private Boolean succeeded;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("errors")
    @Expose
    private List<String> errors = null;

    public List<MissionDetail> getData() {
        return data;
    }

    public void setData(List<MissionDetail> data) {
        this.data = data;
    }

    public Boolean getSucceeded() {
        return succeeded;
    }

    public void setSucceeded(Boolean succeeded) {
        this.succeeded = succeeded;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

}
