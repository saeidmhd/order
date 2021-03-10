package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromotionDetailOtherFields {

    @SerializedName("CodePromotion_Det")
    @Expose
    private int codePromotionDet;
    @SerializedName("HowToPromotion")
    @Expose
    private int howToPromotion;
    @SerializedName("IsCalcAdditive")
    @Expose
    private boolean isCalcAdditive;
    @SerializedName("ReducedEffectOnPrice")
    @Expose
    private boolean reducedEffectOnPrice;
    @SerializedName("ToPayment")
    @Expose
    private int toPayment;
    @SerializedName("MeghdarPromotion")
    @Expose
    private int meghdarPromotion;
    @SerializedName("StoreCode")
    @Expose
    private int storeCode;
    @SerializedName("CodeGood")
    @Expose
    private int codeGood;
    @SerializedName("Tool")
    @Expose
    private int tool;
    @SerializedName("Arz")
    @Expose
    private int arz;
    @SerializedName("Tedad")
    @Expose
    private int tedad;
    @SerializedName("Meghdar")
    @Expose
    private int meghdar;
    @SerializedName("Meghdar2")
    @Expose
    private int meghdar2;
    @SerializedName("Ghotr")
    @Expose
    private int ghotr;
    @SerializedName("ToolidCode")
    @Expose
    private int toolidCode;

    public int getCodePromotionDet() {
        return codePromotionDet;
    }

    public void setCodePromotionDet(int codePromotionDet) {
        this.codePromotionDet = codePromotionDet;
    }

    public int getHowToPromotion() {
        return howToPromotion;
    }

    public void setHowToPromotion(int howToPromotion) {
        this.howToPromotion = howToPromotion;
    }

    public boolean isIsCalcAdditive() {
        return isCalcAdditive;
    }

    public void setIsCalcAdditive(boolean isCalcAdditive) {
        this.isCalcAdditive = isCalcAdditive;
    }

    public boolean isReducedEffectOnPrice() {
        return reducedEffectOnPrice;
    }

    public void setReducedEffectOnPrice(boolean reducedEffectOnPrice) {
        this.reducedEffectOnPrice = reducedEffectOnPrice;
    }

    public int getToPayment() {
        return toPayment;
    }

    public void setToPayment(int toPayment) {
        this.toPayment = toPayment;
    }

    public int getMeghdarPromotion() {
        return meghdarPromotion;
    }

    public void setMeghdarPromotion(int meghdarPromotion) {
        this.meghdarPromotion = meghdarPromotion;
    }

    public int getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(int storeCode) {
        this.storeCode = storeCode;
    }

    public int getCodeGood() {
        return codeGood;
    }

    public void setCodeGood(int codeGood) {
        this.codeGood = codeGood;
    }

    public int getTool() {
        return tool;
    }

    public void setTool(int tool) {
        this.tool = tool;
    }

    public int getArz() {
        return arz;
    }

    public void setArz(int arz) {
        this.arz = arz;
    }

    public int getTedad() {
        return tedad;
    }

    public void setTedad(int tedad) {
        this.tedad = tedad;
    }

    public int getMeghdar() {
        return meghdar;
    }

    public void setMeghdar(int meghdar) {
        this.meghdar = meghdar;
    }

    public int getMeghdar2() {
        return meghdar2;
    }

    public void setMeghdar2(int meghdar2) {
        this.meghdar2 = meghdar2;
    }

    public int getGhotr() {
        return ghotr;
    }

    public void setGhotr(int ghotr) {
        this.ghotr = ghotr;
    }

    public int getToolidCode() {
        return toolidCode;
    }

    public void setToolidCode(int toolidCode) {
        this.toolidCode = toolidCode;
    }
}
