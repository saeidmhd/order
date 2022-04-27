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

/**
 * Created by Saeid.mhd-at-Gmail.com on 12/1/17.
 */

public class PayableTransfer {

    public static String TAG_transferType = "transferType";
    public static String TAG_transferCode = "transferCode";
    public static String TAG_transferDate = "transferDate";
    public static String TAG_price = "price";
    public static String TAG_receiverid = "receiverid";
    public static String TAG_comment = "description";
    public static String TAG_payerId = "payerId";
    public static String TAG_visitorId = "visitorId";


    private long transferCode;
    private String DataBaseId;
    private String MahakId;
    private int Id;
    private int Publish;

    private long UserId;


    @SerializedName("TransferAccountId")
    @Expose
    private int transferAccountId;

    @SerializedName("TransferAccountClientId")
    @Expose
    private long transferAccountClientId;

    @SerializedName("TransferAccountCode")
    @Expose
    private int transferAccountCode;

    @SerializedName("Date")
    @Expose
    private String transferDate;

    @SerializedName("Type")
    @Expose
    private int transferType;

    @SerializedName("Price")
    @Expose
    private BigDecimal price;

    @SerializedName("ReceiverId")
    @Expose
    private long receiverId;

    @SerializedName("PayerId")
    @Expose
    private int payerId;

    @SerializedName("VisitorId")
    @Expose
    private int visitorId;

    @SerializedName("Description")
    @Expose
    private String description;

    @SerializedName("Deleted")
    @Expose
    private boolean deleted;
    @SerializedName("DataHash")
    @Expose
    private String dataHash;

    @SerializedName("UpdateDate")
    @Expose
    private String updateDate;
    @SerializedName("CreateSyncId")
    @Expose
    private int createSyncId;
    @SerializedName("UpdateSyncId")
    @Expose
    private int updateSyncId;
    @SerializedName("RowVersion")
    @Expose
    private long rowVersion;
    @SerializedName("CreateDate")
    @Expose
    private String createDate;

    public PayableTransfer() {
        this.description = "";
        this.setPublish(ProjectInfo.DONT_PUBLISH);
        this.setMahakId(BaseActivity.getPrefMahakId());
        this.setDataBaseId(BaseActivity.getPrefDatabaseId());
        setUserId(BaseActivity.getPrefUserId());
    }

    public long getTransferDate() {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        try {
            Date date = simpleDateFormat.parse(transferDate);
            return date.getTime();
        } catch (ParseException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return -1;
    }

    public void setTransferDate(long orderDate) {

        Date date = new Date();
        date.setTime(orderDate);
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        transferDate = simpleDateFormat.format(date);

    }


    public int getTransferType() {
        return transferType;
    }

    public void setTransferType(int transferType) {
        this.transferType = transferType;
    }

    public long getTransferCode() {
        return transferCode;
    }

    public void setTransferCode(long transferCode) {
        this.transferCode = transferCode;
    }


    public int getPrice() {
        return price.intValue();
    }

    public void setPrice(int price) {
        this.price = new BigDecimal(price);
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPayerId() {
        return payerId;
    }

    public void setPayerId(int payerId) {
        this.payerId = payerId;
    }

    public int getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(int visitorId) {
        this.visitorId = visitorId;
    }

    public int getPublish() {
        return Publish;
    }

    public void setPublish(int publish) {
        Publish = publish;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDataBaseId() {
        return DataBaseId;
    }

    public void setDataBaseId(String dataBaseId) {
        DataBaseId = dataBaseId;
    }

    public String getMahakId() {
        return MahakId;
    }

    public void setMahakId(String mahakId) {
        MahakId = mahakId;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public int getTransferAccountId() {
        return transferAccountId;
    }

    public void setTransferAccountId(int transferAccountId) {
        this.transferAccountId = transferAccountId;
    }

    public long getTransferAccountClientId() {
        return transferAccountClientId;
    }

    public void setTransferAccountClientId(long transferAccountClientId) {
        this.transferAccountClientId = transferAccountClientId;
    }

    public int getTransferAccountCode() {
        return transferAccountCode;
    }

    public void setTransferAccountCode(int transferAccountCode) {
        this.transferAccountCode = transferAccountCode;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
