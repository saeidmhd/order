package com.mahak.order.common;

public class PrintReceipt {
    private int receiptType;
    private double amount;
    private String receiptName;

    public int getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(int receiptType) {
        this.receiptType = receiptType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }
}
