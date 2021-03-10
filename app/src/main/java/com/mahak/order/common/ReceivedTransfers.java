package com.mahak.order.common;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReceivedTransfers {

    //TAG
    public static String TAG_ID = "ID";
    public static String TAG_TransferCode = "transfercode";
    public static String TAG_ModifyDate = "modifieddate";
    public static String TAG_SyncId = "syncid";
    public static String TAG_TransferDate = "transferdate";
    public static String TAG_TransferId = "transferid";
    public static String TAG_IsAccepted = "isaccepted";
    public static String TAG_CreatedBy = "createdby";
    public static String TAG_SenderVisitor = "sendervisitor";
    public static String TAG_ReceiverVisitor = "receivervisitor";
    public static String TAG_ModifiedBy = "modifiedby";
    public static String TAG_Comment = "description";
    //////////////////////////////

    public ReceivedTransfers() {
        this.setMahakId(BaseActivity.getPrefMahakId());
        this.setDatabaseId(BaseActivity.getPrefDatabaseId());
    }

    private String MahakId;
    private String DatabaseId;
    private String ModifyDate;
    private String SyncId;
    private String ModifiedBy;
    private String CreatedBy;

    @SerializedName("TransferStoreId")
    @Expose
    private int transferStoreId;
    @SerializedName("TransferStoreCode")
    @Expose
    private long transferStoreCode;
    @SerializedName("SenderVisitorId")
    @Expose
    private int senderVisitorId;
    @SerializedName("ReceiverVisitorId")
    @Expose
    private int receiverVisitorId;
    @SerializedName("Date")
    @Expose
    private String transferDate;
    @SerializedName("IsAccepted")
    @Expose
    private int isAccepted;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("TransferStoreClientId")
    @Expose
    private long transferStoreClientId;
    @SerializedName("SenderStoreCode")
    @Expose
    private int senderStoreCode;
    @SerializedName("ReceiverStoreCode")
    @Expose
    private int receiverStoreCode;

    @SerializedName("Deleted")
    @Expose
    private boolean deleted;
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

    public String getTransferStoreCode() {
        return String.valueOf(transferStoreCode);
    }

    public void setTransferStoreCode(String transferStoreCode) {
        this.transferStoreCode = ServiceTools.toLong(transferStoreCode);
    }

    public String getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(String modifyDate) {
        ModifyDate = modifyDate;
    }

    public String getSyncId() {
        return SyncId;
    }

    public void setSyncId(String syncId) {
        SyncId = syncId;
    }

    public long getTransferDate() {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        try {
            Date date = simpleDateFormat.parse(transferDate);
            return date.getTime();
        } catch (ParseException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
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


    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getSenderVisitorId() {
        return String.valueOf(senderVisitorId);
    }

    public void setSenderVisitorId(String senderVisitorId) {
        this.senderVisitorId = ServiceTools.toInt(senderVisitorId);
    }

    public String getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMahakId() {
        return MahakId;
    }

    public void setMahakId(String mahakId) {
        MahakId = mahakId;
    }

    public String getDatabaseId() {
        return DatabaseId;
    }

    public void setDatabaseId(String databaseId) {
        DatabaseId = databaseId;
    }

    public String getTransferStoreId() {
        return String.valueOf(transferStoreId);
    }

    public void setTransferStoreId(String transferStoreId) {
        this.transferStoreId = ServiceTools.toInt(transferStoreId);
    }

    public String getReceiverVisitorId() {
        return String.valueOf(receiverVisitorId);
    }

    public void setReceiverVisitorId(String receiverVisitorId) {
        this.receiverVisitorId = ServiceTools.toInt(receiverVisitorId);
    }

    public long getTransferStoreClientId() {
        return transferStoreClientId;
    }

    public void setTransferStoreClientId(long transferStoreClientId) {
        this.transferStoreClientId = transferStoreClientId;
    }

    public int getSenderStoreCode() {
        return senderStoreCode;
    }

    public void setSenderStoreCode(int senderStoreCode) {
        this.senderStoreCode = senderStoreCode;
    }

    public int getReceiverStoreCode() {
        return receiverStoreCode;
    }

    public void setReceiverStoreCode(int receiverStoreCode) {
        this.receiverStoreCode = receiverStoreCode;
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

    public int getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(int isAccepted) {
        this.isAccepted = isAccepted;
    }
}
