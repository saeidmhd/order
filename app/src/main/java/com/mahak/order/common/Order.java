package com.mahak.order.common;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Order {

    //TAG
    public static String TAG_ID = "Id";
    public static String TAG_CUSTOMER_ID = "personId";
    public static String TAG_DESCRIPTION = "Description";
    public static String TAG_MAHAK_ID = "MahakId";
    public static String TAG_DATABASE_ID = "DatabaseId";
    public static String TAG_MASTER_ID = "orderCode";

    ///////////////////////////////////////////////////

    private long Id;
    private long ModifyDate;
    private String DatabaseId;
    private String MahakId;

    private String Code;
    private String CustomerName;
    private String MarketName;
    private String Address;

    private BigDecimal FinalPrice;

    private int Publish;
    private int GiftType;
    private int PromotionCode;

    private int IsFinal;
    private long UserId;

    @SerializedName("OrderId")
    @Expose(serialize = false)
    private long orderId;

    @SerializedName("OrderClientId")
    @Expose
    private long orderClientId;

    @SerializedName("OrderCode")
    @Expose(serialize = false)
    private long orderCode;

    @SerializedName("PersonId")
    @Expose
    private int personId;

    @SerializedName("ReturnReasonId")
    @Expose
    private int returnReasonId;

    @SerializedName("PersonClientId")
    @Expose
    private long personClientId;

    @SerializedName("PersonCode")
    @Expose
    private long PersonCode;

    @SerializedName("VisitorClientId")
    @Expose
    private long VisitorClientId;

    @SerializedName("VisitorId")
    @Expose
    private long visitorId;

    @SerializedName("ReceiptId")
    @Expose
    private String receiptId;

    @SerializedName("ReceiptClientId")
    @Expose
    private String ReceiptClientId;

    @SerializedName("OrderType")
    @Expose
    private int orderType;
    @SerializedName("OrderDate")
    @Expose
    private String OrderDate;
    @SerializedName("DeliveryDate")
    @Expose
    private String DeliveryDate;

    @SerializedName("Discount")
    @Expose
    private BigDecimal Discount;

    @SerializedName("SendCost")
    @Expose
    private BigDecimal sendCost;

    @SerializedName("OtherCost")
    @Expose
    private BigDecimal otherCost;

    @SerializedName("SettlementType")
    @Expose
    private int SettlementType;

    @SerializedName("Immediate")
    @Expose
    private boolean Immediate;

    @SerializedName("Description")
    @Expose
    private String Description;

    @SerializedName("Deleted")
    @Expose
    private boolean isDelete;

    @SerializedName("DataHash")
    @Expose(serialize = false)
    private String dataHash;

    @SerializedName("CreateDate")
    @Expose(serialize = false)
    private String createDate;

    @SerializedName("UpdateDate")
    @Expose(serialize = false)
    private String updateDate;

    @SerializedName("CreateSyncId")
    @Expose(serialize = false)
    private int createSyncId;

    @SerializedName("UpdateSyncId")
    @Expose(serialize = false)
    private int updateSyncId;

    @SerializedName("RowVersion")
    @Expose(serialize = false)
    private long rowVersion;

    @SerializedName("Latitude")
    @Expose
    private BigDecimal Latitude;

    @SerializedName("Longitude")
    @Expose
    private BigDecimal Longitude;


    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public long getVisitorId() {
        return visitorId;
    }


    public long getOrderDate() {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        try {
            Date date = simpleDateFormat.parse(OrderDate);
            return date.getTime();
        } catch (ParseException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return -1;
    }

    public void setOrderDate(long orderDate) {

        Date date = new Date();
        date.setTime(orderDate);
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        OrderDate = simpleDateFormat.format(date);

    }

    public long getDeliveryDate() {

        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        try {
            Date date = simpleDateFormat.parse(DeliveryDate);
            return date.getTime();
        } catch (ParseException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return -1;
    }

    public void setDeliveryDate(long deliveryDate) {

        Date date = new Date();
        date.setTime(deliveryDate);
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        DeliveryDate = simpleDateFormat.format(date);
    }

    public long getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(long modifyDate) {
        ModifyDate = modifyDate;
    }

    public long getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(long orderCode) {
        this.orderCode = orderCode;
    }

    public double getDiscount() {
        return Discount.doubleValue();
    }

    public void setDiscount(double discount) {
        Discount = new BigDecimal(discount);
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getMarketName() {
        return MarketName;
    }

    public void setMarketName(String marketName) {
        MarketName = marketName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getMahakId() {
        return MahakId;
    }

    public void setMahakId(String mahakId) {
        MahakId = mahakId;
    }

    public double getFinalPrice() {
        return FinalPrice.doubleValue();
    }

    public void setFinalPrice(double finalPrice) {
        FinalPrice = new BigDecimal(finalPrice);
    }

    public String getDatabaseId() {
        return DatabaseId;
    }

    public void setDatabaseId(String databaseId) {
        DatabaseId = databaseId;
    }

    public int getSettlementType() {
        return SettlementType;
    }

    public void setSettlementType(int settlementType) {
        SettlementType = settlementType;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getPublish() {
        return Publish;
    }

    public void setPublish(int publish) {
        Publish = publish;
    }

    public int getGiftType() {
        return GiftType;
    }

    public void setGiftType(int giftType) {
        GiftType = giftType;
    }

    public int getPromotionCode() {
        return PromotionCode;
    }

    public void setPromotionCode(int promotionCode) {
        PromotionCode = promotionCode;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getOrderClientId() {
        return orderClientId;
    }

    public void setOrderClientId(long orderClientId) {
        this.orderClientId = orderClientId;
    }

    public void setOrderCode(int orderCode) {
        this.orderCode = orderCode;
    }

    public void setVisitorId(long visitorId) {
        this.visitorId = visitorId;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getOtherCost() {
        return otherCost.toBigInteger().toString();
    }

    public void setOtherCost(String otherCost) {
        this.otherCost = new BigDecimal(otherCost);
    }

    public String getSendCost() {
        return sendCost.toBigInteger().toString();
    }

    public void setSendCost(String sendCost) {
        this.sendCost = new BigDecimal(sendCost);
    }

    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getCreateSyncId() {
        return createSyncId;
    }

    public void setCreateSyncId(int createSyncId) {
        this.createSyncId = createSyncId;
    }

    public int getUpdateSyncId() {
        return updateSyncId;
    }

    public void setUpdateSyncId(int updateSyncId) {
        this.updateSyncId = updateSyncId;
    }

    public long getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(long rowVersion) {
        this.rowVersion = rowVersion;
    }

    public int getDelete() {
        return isDelete ? 1 : 0;
    }

    public void setDelete(boolean delete) {
        this.isDelete = delete;
    }

    public int getImmediate() {
        return Immediate ? 1 : 0;
    }

    public void setImmediate(int immediate) {
        Immediate = immediate == 1;
    }

    public Order() {
        this.setDescription("");
        this.setCode("");
        this.setModifyDate(0);
        this.setPublish(ProjectInfo.DONT_PUBLISH);
        this.setMarketName("");
        this.setAddress("");
        this.setFinalPrice(0);
        this.setMahakId(BaseActivity.getPrefMahakId());
        this.setDatabaseId(BaseActivity.getPrefDatabaseId());
        this.setIsFinal(ProjectInfo.NOt_FINAL);
        this.setSendCost("0");
        this.setOtherCost("0");
        setMahakId(BaseActivity.getPrefMahakId());
        setDatabaseId(BaseActivity.getPrefDatabaseId());
        setUserId(BaseActivity.getPrefUserId());
    }

    public int getIsFinal() {
        return IsFinal;
    }

    public void setIsFinal(int isFinal) {
        IsFinal = isFinal;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public long getPersonCode() {
        return PersonCode;
    }

    public void setPersonCode(long personCode) {
        PersonCode = personCode;
    }

    public long getVisitorClientId() {
        return VisitorClientId;
    }

    public void setVisitorClientId(long visitorClientId) {
        VisitorClientId = visitorClientId;
    }

    public String getReceiptClientId() {
        return ReceiptClientId;
    }

    public void setReceiptClientId(String receiptClientId) {
        ReceiptClientId = receiptClientId;
    }

    public long getPersonClientId() {
        return personClientId;
    }

    public void setPersonClientId(long personClientId) {
        this.personClientId = personClientId;
    }

    public int getReturnReasonId() {
        return returnReasonId;
    }

    public void setReturnReasonId(int returnReasonId) {
        this.returnReasonId = returnReasonId;
    }

    public double getLatitude() {
        return Latitude.doubleValue();
    }

    public void setLatitude(double latitude) {
        Latitude = new BigDecimal(latitude);
    }

    public double getLongitude() {
        return Longitude.doubleValue();
    }

    public void setLongitude(double longitude) {
        Longitude = new BigDecimal(longitude);
    }
}
