package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisitorExtraInfo {

    @SerializedName("PersonCode")
    @Expose
    private long personCode;
    @SerializedName("StoreCode")
    @Expose
    private long storeCode;
    @SerializedName("CashCode")
    @Expose
    private long cashCode;
    @SerializedName("BankCode")
    @Expose
    private long bankCode;
    @SerializedName("DeviceId")
    @Expose
    private String deviceId;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("Color")
    @Expose
    private String color;
    @SerializedName("ChequeCredit")
    @Expose
    private double chequeCredit;
    @SerializedName("TotalCredit")
    @Expose
    private double totalCredit;
    @SerializedName("SellPriceLevel")
    @Expose
    private long sellPriceLevel;
    @SerializedName("HasPriceAccess")
    @Expose
    private boolean hasPriceAccess;
    @SerializedName("HasPriceLevelAccess")
    @Expose
    private boolean hasPriceLevelAccess;
    @SerializedName("SelectedPriceLevels")
    @Expose
    private String selectedPriceLevels;
    @SerializedName("HasRadara")
    @Expose
    private boolean hasRadara;

    public long getPersonCode() {
        return personCode;
    }

    public void setPersonCode(long personCode) {
        this.personCode = personCode;
    }

    public long getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(long storeCode) {
        this.storeCode = storeCode;
    }

    public long getCashCode() {
        return cashCode;
    }

    public void setCashCode(long cashCode) {
        this.cashCode = cashCode;
    }

    public long getBankCode() {
        return bankCode;
    }

    public void setBankCode(long bankCode) {
        this.bankCode = bankCode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getChequeCredit() {
        return chequeCredit;
    }

    public void setChequeCredit(double chequeCredit) {
        this.chequeCredit = chequeCredit;
    }

    public double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(double totalCredit) {
        this.totalCredit = totalCredit;
    }

    public long getSellPriceLevel() {
        return sellPriceLevel;
    }

    public void setSellPriceLevel(long sellPriceLevel) {
        this.sellPriceLevel = sellPriceLevel;
    }

    public boolean isHasPriceAccess() {
        return hasPriceAccess;
    }

    public void setHasPriceAccess(boolean hasPriceAccess) {
        this.hasPriceAccess = hasPriceAccess;
    }

    public boolean isHasPriceLevelAccess() {
        return hasPriceLevelAccess;
    }

    public void setHasPriceLevelAccess(boolean hasPriceLevelAccess) {
        this.hasPriceLevelAccess = hasPriceLevelAccess;
    }

    public String getSelectedPriceLevels() {
        return selectedPriceLevels;
    }

    public void setSelectedPriceLevels(String selectedPriceLevels) {
        this.selectedPriceLevels = selectedPriceLevels;
    }

    public boolean isHasRadara() {
        return hasRadara;
    }

    public void setHasRadara(boolean hasRadara) {
        this.hasRadara = hasRadara;
    }

}
