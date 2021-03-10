package com.mahak.order.common.request.SetSign;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("Result")
    @Expose
    private Boolean result;
    @SerializedName("EntityId")
    @Expose
    private Integer entityId;
    @SerializedName("RowVersion")
    @Expose
    private Integer rowVersion;
    @SerializedName("AdditionalData")
    @Expose
    private AdditionalData additionalData;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Integer getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(Integer rowVersion) {
        this.rowVersion = rowVersion;
    }

    public AdditionalData getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(AdditionalData additionalData) {
        this.additionalData = additionalData;
    }

}
