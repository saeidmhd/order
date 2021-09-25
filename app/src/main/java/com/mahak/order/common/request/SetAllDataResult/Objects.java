package com.mahak.order.common.request.SetAllDataResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.common.request.SetAllDataResult.Banks.Banks;
import com.mahak.order.common.request.SetAllDataResult.Checklists.Checklists;
import com.mahak.order.common.request.SetAllDataResult.Cheques.Cheques;
import com.mahak.order.common.request.SetAllDataResult.ExtraDatas.ExtraDatas;
import com.mahak.order.common.request.SetAllDataResult.NonRegisters.NonRegisters;
import com.mahak.order.common.request.SetAllDataResult.OrderDetails.OrderDetails;
import com.mahak.order.common.request.SetAllDataResult.Orders.Orders;
import com.mahak.order.common.request.SetAllDataResult.PayableTransfers.PayableTransfers;
import com.mahak.order.common.request.SetAllDataResult.PersonGroups.PersonGroups;
import com.mahak.order.common.request.SetAllDataResult.Persons.People;
import com.mahak.order.common.request.SetAllDataResult.Pictures.Pictures;
import com.mahak.order.common.request.SetAllDataResult.ProductCategories.ProductCategories;
import com.mahak.order.common.request.SetAllDataResult.ProductDetails.ProductDetails;
import com.mahak.order.common.request.SetAllDataResult.Products.Products;
import com.mahak.order.common.request.SetAllDataResult.Receipts.Receipts;
import com.mahak.order.common.request.SetAllDataResult.Settings.Settings;
import com.mahak.order.common.request.SetAllDataResult.Transactions.Transactions;
import com.mahak.order.common.request.SetAllDataResult.TransferStoreDetails.TransferStoreDetails;
import com.mahak.order.common.request.SetAllDataResult.TransferStores.TransferStores;
import com.mahak.order.common.request.SetAllDataResult.Visitors.Visitors;
import com.mahak.order.common.request.SetAllDataResult.VisitorLocations.VisitorLocations;

public class Objects {

    @SerializedName("Banks")
    @Expose
    private Banks banks;
    @SerializedName("Checklists")
    @Expose
    private Checklists checklists;
    @SerializedName("Cheques")
    @Expose
    private Cheques cheques;
    @SerializedName("ExtraDatas")
    @Expose
    private ExtraDatas extraDatas;
    @SerializedName("OrderDetails")
    @Expose
    private OrderDetails orderDetails;
    @SerializedName("Orders")
    @Expose
    private Orders orders;
    @SerializedName("PersonGroups")
    @Expose
    private PersonGroups personGroups;
    @SerializedName("People")
    @Expose
    private People people;
    @SerializedName("Pictures")
    @Expose
    private Pictures pictures;
    @SerializedName("ProductCategories")
    @Expose
    private ProductCategories productCategories;
    @SerializedName("ProductDetails")
    @Expose
    private ProductDetails productDetails;
    @SerializedName("Products")
    @Expose
    private Products products;
    @SerializedName("Receipts")
    @Expose
    private Receipts receipts;
    @SerializedName("Settings")
    @Expose
    private Settings settings;
    @SerializedName("Transactions")
    @Expose
    private Transactions transactions;
    @SerializedName("Visitors")
    @Expose
    private Visitors visitors;
    @SerializedName("NotRegisters")
    @Expose
    private NonRegisters notRegisters;

    @SerializedName("TransferAccounts")
    @Expose
    private PayableTransfers payableTransfers;

    @SerializedName("TransferStores")
    @Expose
    private TransferStores transferStores;

    @SerializedName("TransferStoreDetails")
    @Expose
    private TransferStoreDetails transferStoreDetails;

    @SerializedName("VisitorLocations")
    @Expose
    private VisitorLocations VisitorLocations;


    public Banks getBanks() {
        return banks;
    }

    public void setBanks(Banks banks) {
        this.banks = banks;
    }

    public Checklists getChecklists() {
        return checklists;
    }

    public void setChecklists(Checklists checklists) {
        this.checklists = checklists;
    }

    public Cheques getCheques() {
        return cheques;
    }

    public void setCheques(Cheques cheques) {
        this.cheques = cheques;
    }

    public ExtraDatas getExtraDatas() {
        return extraDatas;
    }

    public void setExtraDatas(ExtraDatas extraDatas) {
        this.extraDatas = extraDatas;
    }

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public PersonGroups getPersonGroups() {
        return personGroups;
    }

    public void setPersonGroups(PersonGroups personGroups) {
        this.personGroups = personGroups;
    }

    public People getPeople() {
        return people;
    }

    public void setPeople(People people) {
        this.people = people;
    }

    public Pictures getPictures() {
        return pictures;
    }

    public void setPictures(Pictures pictures) {
        this.pictures = pictures;
    }

    public ProductCategories getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(ProductCategories productCategories) {
        this.productCategories = productCategories;
    }

    public ProductDetails getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ProductDetails productDetails) {
        this.productDetails = productDetails;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public Receipts getReceipts() {
        return receipts;
    }

    public void setReceipts(Receipts receipts) {
        this.receipts = receipts;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Transactions getTransactions() {
        return transactions;
    }

    public void setTransactions(Transactions transactions) {
        this.transactions = transactions;
    }

    public Visitors getVisitors() {
        return visitors;
    }

    public void setVisitors(Visitors visitors) {
        this.visitors = visitors;
    }


    public NonRegisters getNotRegisters() {
        return notRegisters;
    }

    public void setNotRegisters(NonRegisters notRegisters) {
        this.notRegisters = notRegisters;
    }

    public PayableTransfers getPayableTransfers() {
        return payableTransfers;
    }

    public void setPayableTransfers(PayableTransfers payableTransfers) {
        this.payableTransfers = payableTransfers;
    }

    public TransferStores getTransferStores() {
        return transferStores;
    }

    public void setTransferStores(TransferStores transferStores) {
        this.transferStores = transferStores;
    }

    public TransferStoreDetails getTransferStoreDetails() {
        return transferStoreDetails;
    }

    public void setTransferStoreDetails(TransferStoreDetails transferStoreDetails) {
        this.transferStoreDetails = transferStoreDetails;
    }

    public com.mahak.order.common.request.SetAllDataResult.VisitorLocations.VisitorLocations getVisitorLocations() {
        return VisitorLocations;
    }

    public void setVisitorLocations(com.mahak.order.common.request.SetAllDataResult.VisitorLocations.VisitorLocations visitorLocations) {
        VisitorLocations = visitorLocations;
    }
}