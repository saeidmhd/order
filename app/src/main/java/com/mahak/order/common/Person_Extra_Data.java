package com.mahak.order.common;

import android.os.Build;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

public class Person_Extra_Data {

    @SerializedName("FullName")
    @Expose
    private String fullName;
    @SerializedName("StorePhone")
    @Expose
    private String storePhone;
    @SerializedName("RemainAmount")
    @Expose
    private BigDecimal remainAmount;
    @SerializedName("RemainStatus")
    @Expose
    private int remainStatus;
    @SerializedName("LastInvoiceAmount")
    @Expose
    private BigDecimal lastInvoiceAmount;
    @SerializedName("LastInvoiceDate")
    @Expose
    private String lastInvoiceDate;
    @SerializedName("InvoiceAVGAmount")
    @Expose
    private BigDecimal invoiceAVGAmount;
    @SerializedName("InvoiceAVGDiscount")
    @Expose
    private BigDecimal invoiceAVGDiscount;
    @SerializedName("StoreAddress")
    @Expose
    private String storeAddress;
    @SerializedName("AvailableChequeAmount")
    @Expose
    private BigDecimal availableChequeAmount;
    @SerializedName("AvailableChequeCount")
    @Expose
    private int availableChequeCount;
    @SerializedName("InReceiptChequeAmount")
    @Expose
    private BigDecimal inReceiptChequeAmount;
    @SerializedName("InReceiptChequeCount")
    @Expose
    private int inReceiptChequeCount;
    @SerializedName("SpentChequeAmount")
    @Expose
    private BigDecimal spentChequeAmount;
    @SerializedName("SpentChequeCount")
    @Expose
    private int spentChequeCount;
    @SerializedName("ReturnChequeAmount")
    @Expose
    private BigDecimal returnChequeAmount;
    @SerializedName("ReturnChequeCount")
    @Expose
    private int returnChequeCount;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public double getRemainAmount() {
        if (remainAmount != null)
            return remainAmount.doubleValue();
        else
            return 0;
    }

    public void setRemainAmount(int remainAmount) {
        this.remainAmount = new BigDecimal(remainAmount);
    }

    public int getRemainStatus() {
        return remainStatus;
    }

    public void setRemainStatus(int remainStatus) {
        this.remainStatus = remainStatus;
    }

    public double getLastInvoiceAmount() {
        if(lastInvoiceAmount != null)
            return lastInvoiceAmount.doubleValue();
        else
            return 0;
    }

    public void setLastInvoiceAmount(double lastInvoiceAmount) {
        this.lastInvoiceAmount = new BigDecimal(lastInvoiceAmount);
    }

    public String getLastInvoiceDate() {
        return lastInvoiceDate;
    }

    public void setLastInvoiceDate(String lastInvoiceDate) {
        this.lastInvoiceDate = lastInvoiceDate;
    }

    public double getInvoiceAVGAmount() {
        if(invoiceAVGAmount != null)
            return invoiceAVGAmount.doubleValue();
        else
            return 0;
    }

    public void setInvoiceAVGAmount(double invoiceAVGAmount) {
        this.invoiceAVGAmount = new BigDecimal(invoiceAVGAmount);
    }

    public double getInvoiceAVGDiscount() {
        if(invoiceAVGDiscount != null)
            return invoiceAVGDiscount.doubleValue();
        else
            return 0;
    }

    public void setInvoiceAVGDiscount(double invoiceAVGDiscount) {
        this.invoiceAVGDiscount = new BigDecimal(invoiceAVGDiscount);
    }

    /*public String getStoreAddress() {
        return storeAddress;
    }*/

    public String getStoreAddress() {
        String zoneNameStr = "";
        byte[] utf8Bytes = new byte[0];
        if (storeAddress != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                utf8Bytes = storeAddress.getBytes(StandardCharsets.UTF_8);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                zoneNameStr = new String(utf8Bytes, StandardCharsets.UTF_8);
            }
        }
        return zoneNameStr;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public double getAvailableChequeAmount() {
        if(availableChequeAmount != null)
            return availableChequeAmount.doubleValue();
        else
            return 0;
    }

    public void setAvailableChequeAmount(double availableChequeAmount) {
        this.availableChequeAmount = new BigDecimal(availableChequeAmount);
    }

    public int getAvailableChequeCount() {
        return availableChequeCount;
    }

    public void setAvailableChequeCount(int availableChequeCount) {
        this.availableChequeCount = availableChequeCount;
    }

    public double getInReceiptChequeAmount() {
        if(inReceiptChequeAmount != null)
            return inReceiptChequeAmount.doubleValue();
        else
            return 0;
    }

    public void setInReceiptChequeAmount(double inReceiptChequeAmount) {
        this.inReceiptChequeAmount = new BigDecimal(inReceiptChequeAmount);
    }

    public int getInReceiptChequeCount() {
        return inReceiptChequeCount;
    }

    public void setInReceiptChequeCount(int inReceiptChequeCount) {
        this.inReceiptChequeCount = inReceiptChequeCount;
    }

    public double getSpentChequeAmount() {
        if(spentChequeAmount != null)
            return spentChequeAmount.doubleValue();
        else
            return 0;
    }

    public void setSpentChequeAmount(double spentChequeAmount) {
        this.spentChequeAmount = new BigDecimal(spentChequeAmount);
    }

    public int getSpentChequeCount() {
        return spentChequeCount;
    }

    public void setSpentChequeCount(int spentChequeCount) {
        this.spentChequeCount = spentChequeCount;
    }

    public double getReturnChequeAmount() {
        if(returnChequeAmount != null)
            return returnChequeAmount.doubleValue();
        else
            return 0;
    }

    public void setReturnChequeAmount(double returnChequeAmount) {
        this.returnChequeAmount = new BigDecimal(returnChequeAmount);
    }

    public int getReturnChequeCount() {
        return returnChequeCount;
    }

    public void setReturnChequeCount(int returnChequeCount) {
        this.returnChequeCount = returnChequeCount;
    }
}

