package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromotionEntityOtherFields {

    @SerializedName("CodeEntity")
    @Expose
    private int codeEntity;
    @SerializedName("CodePromotionEntity")
    @Expose
    private int codePromotionEntity;
    @SerializedName("EntityType")
    @Expose
    private int entityType;

    public int getCodeEntity() {
        return codeEntity;
    }

    public void setCodeEntity(int codeEntity) {
        this.codeEntity = codeEntity;
    }

    public int getCodePromotionEntity() {
        return codePromotionEntity;
    }

    public void setCodePromotionEntity(int codePromotionEntity) {
        this.codePromotionEntity = codePromotionEntity;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }
}
