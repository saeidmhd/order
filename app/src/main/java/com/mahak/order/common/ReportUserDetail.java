package com.mahak.order.common;

/**
 * Created by mehrdadsml on 24/04/2016.
 */
public class ReportUserDetail {

    private int Id;
    private double Sale;
    private double Discount;
    private double NetSale;
    private double TaxCharge;
    private double FinalSum;
    private double CashAmount;
    private double ChequeAmount;
    private double CashReceiptAmount;
    private String Name;
    private double CountOfProduct;
    private double ReturnOfSale;
    private double CountOfReturn;

    public ReportUserDetail() {

    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public double getSale() {
        return Sale;
    }

    public void setSale(double sale) {
        Sale = sale;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }

    public double getNetSale() {
        return NetSale;
    }

    public void setNetSale(double netSale) {
        NetSale = netSale;
    }

    public double getTaxCharge() {
        return TaxCharge;
    }

    public void setTaxCharge(double taxCharge) {
        TaxCharge = taxCharge;
    }

    public double getFinalSum() {
        return FinalSum;
    }

    public void setFinalSum(double finalSum) {
        FinalSum = finalSum;
    }

    public double getCashAmount() {
        return CashAmount;
    }

    public void setCashAmount(double cashAmount) {
        CashAmount = cashAmount;
    }

    public double getChequeAmount() {
        return ChequeAmount;
    }

    public void setChequeAmount(double chequeAmount) {
        ChequeAmount = chequeAmount;
    }

    public double getCashReceiptAmount() {
        return CashReceiptAmount;
    }

    public void setCashReceiptAmount(double cashReceiptAmount) {
        CashReceiptAmount = cashReceiptAmount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getCountOfProduct() {
        return CountOfProduct;
    }

    public void setCountOfProduct(double countOfProduct) {
        CountOfProduct = countOfProduct;
    }

    public double getReturnOfSale() {
        return ReturnOfSale;
    }

    public void setReturnOfSale(long returnOfSale) {
        ReturnOfSale = returnOfSale;
    }

    public double getCountOfReturn() {
        return CountOfReturn;
    }

    public void setCountOfReturn(double countOfReturn) {
        CountOfReturn = countOfReturn;
    }
}
