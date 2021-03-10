package com.mahak.order.common;

public class ReportMonth {

    private long Id;
    private String Name;
    private double Percent;
    private double Price;
    private double CashAmount;
    private double Cheque;
    private double CashReceipt;
    private boolean StatusAnimation = false;

    public ReportMonth() {
        setPrice(0);
        setCashAmount(0);
        setCheque(0);
        setCashReceipt(0);
        setStatusAnimation(false);
    }


    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getPercent() {
        return Percent;
    }

    public void setPercent(double percent) {
        Percent = percent;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getCashAmount() {
        return CashAmount;
    }

    public void setCashAmount(double cashAmount) {
        CashAmount = cashAmount;
    }

    public double getCheque() {
        return Cheque;
    }

    public void setCheque(double cheque) {
        Cheque = cheque;
    }

    public double getCashReceipt() {
        return CashReceipt;
    }

    public void setCashReceipt(double cashReceipt) {
        CashReceipt = cashReceipt;
    }

    public boolean isStatusAnimation() {
        return StatusAnimation;
    }

    public void setStatusAnimation(boolean statusAnimation) {
        StatusAnimation = statusAnimation;
    }
}
