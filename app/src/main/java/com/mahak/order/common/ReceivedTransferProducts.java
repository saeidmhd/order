package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

import java.math.BigDecimal;

public class ReceivedTransferProducts {

    //TAG
    public static String TAG_ID = "id";
    public static String TAG_ModifyDate = "modifieddate";
    public static String TAG_Count = "count";
    public static String TAG_TransferId = "transferid";
    public static String TAG_ProductId = "productid";
    public static String TAG_CreatedDate = "createddate";
    public static String TAG_CreatedBy = "createdby";
    public static String TAG_Comment = "description";
    public static String TAG_ProductName = "productname";
    public static String TAG_categoryid = "categoryid";
    public static String TAG_asset = "asset";
    public static String TAG_unitname = "unitname";
    public static String TAG_asset2 = "asset2";
    public static String TAG_unitname2 = "unitname2";
    public static String TAG_price = "price";
    public static String TAG_tags = "tags";
    public static String TAG_customerprice = "customerprice";
    public static String TAG_inbox = "inbox";
    public static String TAG_code = "code";
    public static String TAG_image = "image";
    public static String TAG_min = "min";
    public static String TAG_tax = "tax";
    public static String TAG_charge = "charge";
    public static String TAG_discountpercent = "discountpercent";
    //////////////////////////////

    public ReceivedTransferProducts() {

        ModifyDate = "";
        CreatedDate = "";
        CreatedBy = "";
        description = "";
        ProductName = "";
        this.categoryid = "";
        this.asset = "";
        this.unitname = "";
        this.asset2 = "";
        this.unitname2 = "";
        this.price = "";
        this.tags = "";
        this.customerprice = "";
        this.inbox = "";
        this.code = "";
        this.image = "";
        this.min = "";
        this.tax = "";
        this.charge = "";
        this.discountpercent = "";
        this.setMahakId(BaseActivity.getPrefMahakId());
        this.setDatabaseId(BaseActivity.getPrefDatabaseId());
    }

    @SerializedName("TransferStoreId")
    @Expose
    private int transferStoreId;

    @SerializedName("ProductDetailId")
    @Expose
    private int productDetailId;

    @SerializedName("Count1")
    @Expose
    private BigDecimal count1;

    @SerializedName("Description")
    @Expose
    private String description;

    @SerializedName("TransferStoreDetailId")
    @Expose
    private int transferStoreDetailId;

    @SerializedName("TransferStoreDetailClientId")
    @Expose
    private long transferStoreDetailClientId;

    @SerializedName("TransferStoreClientId")
    @Expose
    private long transferStoreClientId;

    @SerializedName("Count2")
    @Expose
    private BigDecimal count2;

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

    private String Id;
    private String ModifyDate;
    private String CreatedDate;
    private String CreatedBy;
    private String MahakId;
    private String DatabaseId;

    private String ProductName;
    private String categoryid;
    private String asset;
    private String unitname;
    private String asset2;
    private String unitname2;
    private String price;
    private String tags;
    private String customerprice;
    private String inbox;
    private String code;
    private String image;
    private String min;
    private String tax;
    private String charge;
    private String discountpercent;


    public String getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(String modifyDate) {
        ModifyDate = modifyDate;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public double getCount1() {
        return count1.doubleValue();
    }

    public void setCount1(double count) {
        count1 = new BigDecimal(count);
    }

    public double getCount2() {
        return count2.doubleValue();
    }

    public void setCount2(double count2) {
        this.count2 = new BigDecimal(count2);
    }

    public String getTransferStoreId() {
        return String.valueOf(transferStoreId);
    }

    public void setTransferStoreId(String transferStoreId) {
        this.transferStoreId = ServiceTools.toInt(transferStoreId);
    }

    public String getProductDetailId() {
        return String.valueOf(productDetailId);
    }

    public void setProductDetailId(String productDetailId) {
        this.productDetailId = ServiceTools.toInt(productDetailId);
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
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

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public String getAsset2() {
        return asset2;
    }

    public void setAsset2(String asset2) {
        this.asset2 = asset2;
    }

    public String getUnitname2() {
        return unitname2;
    }

    public void setUnitname2(String unitname2) {
        this.unitname2 = unitname2;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCustomerprice() {
        return customerprice;
    }

    public void setCustomerprice(String customerprice) {
        this.customerprice = customerprice;
    }

    public String getInbox() {
        return inbox;
    }

    public void setInbox(String inbox) {
        this.inbox = inbox;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getDiscountpercent() {
        return discountpercent;
    }

    public void setDiscountpercent(String discountpercent) {
        this.discountpercent = discountpercent;
    }

    public int getTransferStoreDetailId() {
        return transferStoreDetailId;
    }

    public void setTransferStoreDetailId(int transferStoreDetailId) {
        this.transferStoreDetailId = transferStoreDetailId;
    }

    public long getTransferStoreDetailClientId() {
        return transferStoreDetailClientId;
    }

    public void setTransferStoreDetailClientId(long transferStoreDetailClientId) {
        this.transferStoreDetailClientId = transferStoreDetailClientId;
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

    public long getTransferStoreClientId() {
        return transferStoreClientId;
    }

    public void setTransferStoreClientId(long transferStoreClientId) {
        this.transferStoreClientId = transferStoreClientId;
    }
}
