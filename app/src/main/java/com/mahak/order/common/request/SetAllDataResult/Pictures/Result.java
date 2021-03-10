package com.mahak.order.common.request.SetAllDataResult.Pictures;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

    @SerializedName("Result")
    @Expose
    private boolean result;
    @SerializedName("Index")
    @Expose
    private long index;
    @SerializedName("EntityId")
    @Expose
    private int entityID;
    @SerializedName("RowVersion")
    @Expose
    private long rowVersion;
    @SerializedName("Errors")
    @Expose
    private List<Error> errors = null;
    @SerializedName("AdditionalData")
    @Expose
    private AdditionalData additionalData;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public long getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(long rowVersion) {
        this.rowVersion = rowVersion;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public AdditionalData getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(AdditionalData additionalData) {
        this.additionalData = additionalData;
    }

}