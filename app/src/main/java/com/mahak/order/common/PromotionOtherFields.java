package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromotionOtherFields {

    /*private String CodePromotion;
    private String NamePromotion;
    private String DateStart;
    private String DateEnd;
    private String TimeStart;
    private String TimeEnd;
    private String DesPromotion;

    private int PriorityPromotion;
    private int LevelPromotion;
    private int AccordingTo;
    private int IsCalcLinear;
    private int TypeTasvieh;
    private int DeadlineTasvieh;
    private int IsActive;
    private int IsAllCustomer;
    private int IsAllGood;
    private int AggregateWithOther;

    private int IsAllVisitor;
    private int isAllService;
    private int isAllStore;*/

    @SerializedName("NamePromotion")
    @Expose
    private String namePromotion;
    @SerializedName("PriorityPromotion")
    @Expose
    private int priorityPromotion;
    @SerializedName("DateStart")
    @Expose
    private String dateStart;
    @SerializedName("DateEnd")
    @Expose
    private String dateEnd;
    @SerializedName("TimeStart")
    @Expose
    private String timeStart;
    @SerializedName("TimeEnd")
    @Expose
    private String timeEnd;
    @SerializedName("LevelPromotion")
    @Expose
    private int levelPromotion;
    @SerializedName("AccordingTo")
    @Expose
    private int accordingTo;
    @SerializedName("IsCalcLinear")
    @Expose
    private boolean isCalcLinear;
    @SerializedName("TypeTasvieh")
    @Expose
    private int typeTasvieh;
    @SerializedName("DeadlineTasvieh")
    @Expose
    private int deadlineTasvieh;
    @SerializedName("DesPromotion")
    @Expose
    private String desPromotion;
    @SerializedName("IsAllCustomer")
    @Expose
    private boolean isAllCustomer;
    @SerializedName("IsAllVisitor")
    @Expose
    private boolean isAllVisitor;
    @SerializedName("IsAllGood")
    @Expose
    private boolean isAllGood;
    @SerializedName("IsAllService")
    @Expose
    private boolean isAllService;
    @SerializedName("IsAllStore")
    @Expose
    private boolean isAllStore;
    @SerializedName("AggregateWithOther")
    @Expose
    private int aggregateWithOther;

    public String getNamePromotion() {
        return namePromotion;
    }

    public void setNamePromotion(String namePromotion) {
        this.namePromotion = namePromotion;
    }

    public int getPriorityPromotion() {
        return priorityPromotion;
    }

    public void setPriorityPromotion(int priorityPromotion) {
        this.priorityPromotion = priorityPromotion;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public int getLevelPromotion() {
        return levelPromotion;
    }

    public void setLevelPromotion(int levelPromotion) {
        this.levelPromotion = levelPromotion;
    }

    public int getAccordingTo() {
        return accordingTo;
    }

    public void setAccordingTo(int accordingTo) {
        this.accordingTo = accordingTo;
    }

    public boolean isIsCalcLinear() {
        return isCalcLinear;
    }

    public void setIsCalcLinear(boolean isCalcLinear) {
        this.isCalcLinear = isCalcLinear;
    }

    public int getTypeTasvieh() {
        return typeTasvieh;
    }

    public void setTypeTasvieh(int typeTasvieh) {
        this.typeTasvieh = typeTasvieh;
    }

    public int getDeadlineTasvieh() {
        return deadlineTasvieh;
    }

    public void setDeadlineTasvieh(int deadlineTasvieh) {
        this.deadlineTasvieh = deadlineTasvieh;
    }

    public String getDesPromotion() {
        return desPromotion;
    }

    public void setDesPromotion(String desPromotion) {
        this.desPromotion = desPromotion;
    }

    public boolean isIsAllCustomer() {
        return isAllCustomer;
    }

    public void setIsAllCustomer(boolean isAllCustomer) {
        this.isAllCustomer = isAllCustomer;
    }

    public boolean isIsAllVisitor() {
        return isAllVisitor;
    }

    public void setIsAllVisitor(boolean isAllVisitor) {
        this.isAllVisitor = isAllVisitor;
    }

    public boolean isIsAllGood() {
        return isAllGood;
    }

    public void setIsAllGood(boolean isAllGood) {
        this.isAllGood = isAllGood;
    }

    public boolean isIsAllService() {
        return isAllService;
    }

    public void setIsAllService(boolean isAllService) {
        this.isAllService = isAllService;
    }

    public boolean isIsAllStore() {
        return isAllStore;
    }

    public void setIsAllStore(boolean isAllStore) {
        this.isAllStore = isAllStore;
    }

    public int getAggregateWithOther() {
        return aggregateWithOther;
    }

    public void setAggregateWithOther(int aggregateWithOther) {
        this.aggregateWithOther = aggregateWithOther;
    }
}
