package com.mahak.order.common.request.GetAllDataResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.common.Bank;
import com.mahak.order.common.CheckList;
import com.mahak.order.common.Cheque;
import com.mahak.order.common.Customer;
import com.mahak.order.common.CustomerGroup;
import com.mahak.order.common.ExtraData;
import com.mahak.order.common.NonRegister;
import com.mahak.order.common.Order;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.PayableTransfer;
import com.mahak.order.common.PicturesProduct;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductDetail;
import com.mahak.order.common.ProductGroup;
import com.mahak.order.common.ProductPriceLevelName;
import com.mahak.order.common.Promotion;
import com.mahak.order.common.PromotionDetail;
import com.mahak.order.common.PromotionEntity;
import com.mahak.order.common.PropertyDescription;
import com.mahak.order.common.Reasons;
import com.mahak.order.common.Receipt;
import com.mahak.order.common.ReceivedTransferProducts;
import com.mahak.order.common.ReceivedTransfers;
import com.mahak.order.common.Setting;
import com.mahak.order.common.TransactionsLog;
import com.mahak.order.common.Visitor;
import com.mahak.order.common.VisitorPeople;
import com.mahak.order.common.VisitorProduct;

import java.util.ArrayList;
import java.util.List;

public class Objects {

    @SerializedName("Banks")
    @Expose
    private List<Bank> banks = new ArrayList<>();
    @SerializedName("Checklists")
    @Expose
    private List<CheckList> checklists = new ArrayList<>();
    @SerializedName("Cheques")
    @Expose
    private List<Cheque> cheques = new ArrayList<>();
    @SerializedName("ExtraDatas")
    @Expose
    private List<ExtraData> extraData = new ArrayList<>();
    @SerializedName("OrderDetails")
    @Expose
    private List<OrderDetail> orderDetails = new ArrayList<>();
    @SerializedName("Orders")
    @Expose
    private List<Order> orders = new ArrayList<>();
    @SerializedName("PersonGroups")
    @Expose
    private List<CustomerGroup> personGroups = new ArrayList<>();
    @SerializedName("People")
    @Expose
    private List<Customer> people = new ArrayList<>();
    @SerializedName("Pictures")
    @Expose
    private List<PicturesProduct> pictures = new ArrayList<>();
    @SerializedName("ProductCategories")
    @Expose
    private List<ProductGroup> productCategories = new ArrayList<>();
    @SerializedName("ProductDetails")
    @Expose
    private List<ProductDetail> productDetails = new ArrayList<>();
    @SerializedName("Products")
    @Expose
    private List<Product> products = new ArrayList<>();
    @SerializedName("Receipts")
    @Expose
    private List<Receipt> receipts = new ArrayList<>();
    @SerializedName("Settings")
    @Expose
    private List<Setting> settings = new ArrayList<>();
    @SerializedName("Transactions")
    @Expose
    private List<TransactionsLog> transactions = new ArrayList<>();
    @SerializedName("Visitors")
    @Expose
    private List<Visitor> visitors = new ArrayList<>();

    @SerializedName("NotRegisters")
    @Expose
    private final List<NonRegister> nonRegisters = new ArrayList<>();
    @SerializedName("Promotions")
    @Expose
    private List<Promotion> promotions = new ArrayList<>();
    @SerializedName("PromotionDetails")
    @Expose
    private List<PromotionDetail> promotionDetails = new ArrayList<>();
    @SerializedName("PromotionEntities")
    @Expose
    private List<PromotionEntity> promotionEntities;
    @SerializedName("ReturnReasons")
    @Expose
    private List<Reasons> returnReasons = new ArrayList<>();
    @SerializedName("TransferAccounts")
    @Expose
    private final List<PayableTransfer> transferAccounts = new ArrayList<>();
    @SerializedName("TransferStores")
    @Expose
    private List<ReceivedTransfers> receivedTransfers;
    @SerializedName("TransferStoreDetails")
    @Expose
    private List<ReceivedTransferProducts> receivedTransferProducts;

    @SerializedName("CostLevelNames")
    @Expose
    private List<ProductPriceLevelName> costLevelNames = new ArrayList<>();

    @SerializedName("PropertyDescriptions")
    @Expose
    private List<PropertyDescription> propertyDescriptions = new ArrayList<>();

    @SerializedName("VisitorProducts")
    @Expose
    private List<VisitorProduct> visitorProducts = new ArrayList<>();

    @SerializedName("VisitorPeople")
    @Expose
    private List<VisitorPeople> visitorPeople = new ArrayList<>();


    public List<Bank> getBanks() {
        return banks;
    }

    public void setBanks(List<Bank> banks) {
        this.banks = banks;
    }

    public List<CheckList> getChecklists() {
        return checklists;
    }

    public void setChecklists(List<CheckList> checklists) {
        this.checklists = checklists;
    }

    public List<Cheque> getCheques() {
        return cheques;
    }

    public void setCheques(List<Cheque> cheques) {
        this.cheques = cheques;
    }

    public List<ExtraData> getExtraData() {
        return extraData;
    }

    public void setExtraData(List<ExtraData> extraData) {
        this.extraData = extraData;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<CustomerGroup> getPersonGroups() {
        return personGroups;
    }

    public void setPersonGroups(List<CustomerGroup> personGroups) {
        this.personGroups = personGroups;
    }

    public List<Customer> getPeople() {
        return people;
    }

    public void setPeople(List<Customer> people) {
        this.people = people;
    }

    public List<PicturesProduct> getPictures() {
        return pictures;
    }

    public void setPictures(List<PicturesProduct> pictures) {
        this.pictures = pictures;
    }

    public List<ProductGroup> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(List<ProductGroup> productCategories) {
        this.productCategories = productCategories;
    }

    public List<ProductDetail> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<ProductDetail> productDetails) {
        this.productDetails = productDetails;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public void setSettings(List<Setting> settings) {
        this.settings = settings;
    }

    public List<TransactionsLog> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionsLog> transactions) {
        this.transactions = transactions;
    }

    public List<Visitor> getVisitors() {
        return visitors;
    }

    public void setVisitors(List<Visitor> visitors) {
        this.visitors = visitors;
    }


    public List<Reasons> getReturnReasons() {
        return returnReasons;
    }

    public void setReturnReasons(List<Reasons> returnReasons) {
        this.returnReasons = returnReasons;
    }

    public List<ProductPriceLevelName> getCostLevelNames() {
        return costLevelNames;
    }

    public void setCostLevelNames(List<ProductPriceLevelName> costLevelNames) {
        this.costLevelNames = costLevelNames;
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public List<PromotionDetail> getPromotionDetails() {
        return promotionDetails;
    }

    public void setPromotionDetails(List<PromotionDetail> promotionDetails) {
        this.promotionDetails = promotionDetails;
    }


    public List<PromotionEntity> getPromotionEntities() {
        return promotionEntities;
    }

    public void setPromotionEntities(List<PromotionEntity> promotionEntities) {
        this.promotionEntities = promotionEntities;
    }

    public List<ReceivedTransfers> getReceivedTransfers() {
        return receivedTransfers;
    }

    public void setReceivedTransfers(List<ReceivedTransfers> receivedTransfers) {
        this.receivedTransfers = receivedTransfers;
    }

    public List<ReceivedTransferProducts> getReceivedTransferProducts() {
        return receivedTransferProducts;
    }

    public void setReceivedTransferProducts(List<ReceivedTransferProducts> receivedTransferProducts) {
        this.receivedTransferProducts = receivedTransferProducts;
    }

    public List<PropertyDescription> getPropertyDescriptions() {
        return propertyDescriptions;
    }

    public void setPropertyDescriptions(List<PropertyDescription> propertyDescriptions) {
        this.propertyDescriptions = propertyDescriptions;
    }

    public List<VisitorProduct> getVisitorProducts() {
        return visitorProducts;
    }

    public void setVisitorProducts(List<VisitorProduct> visitorProducts) {
        this.visitorProducts = visitorProducts;
    }

    public List<VisitorPeople> getVisitorPeople() {
        return visitorPeople;
    }

    public void setVisitorPeople(List<VisitorPeople> visitorPeople) {
        this.visitorPeople = visitorPeople;
    }
}