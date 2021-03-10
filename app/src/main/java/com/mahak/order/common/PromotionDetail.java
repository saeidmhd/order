package com.mahak.order.common;

//created by saeid.mhd@gmail.com

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

public class PromotionDetail {
    //TAG

    public static String TAG_SyncID = "syncID";
    public static String TAG_CodePromotion = "promotionCode";
    public static String TAG_OtherFields = "OtherFields";
    public static String TAG_CodePromotion_Det = "promotionDetailCode";
    public static String TAG_HowToPromotion = "HowToPromotion";
    public static String TAG_IsCalcAdditive = "IsCalcAdditive";
    public static String TAG_ReducedEffectOnPrice = "ReducedEffectOnPrice";
    public static String TAG_ToPayment = "ToPayment";
    public static String TAG_MeghdarPromotion = "MeghdarPromotion";
    public static String TAG_StoreCode = "StoreCode";
    public static String TAG_CodeGood = "CodeGood";
    public static String TAG_Tool = "Tool";
    public static String TAG_Arz = "Arz";
    public static String TAG_Tedad = "Tedad";
    public static String TAG_Meghdar = "Meghdar";
    public static String TAG_Meghdar2 = "Meghdar2";
    public static String TAG_Ghotr = "Ghotr";
    public static String TAG_ToolidCode = "ToolidCode";
    public static String TAG_CreatedBy = "createdBy";
    public static String TAG_CreatedDate = "createdDate";
    public static String TAG_ModifiedBy = "modifiedBy";
    public static String TAG_ModifiedDate = "ModifiedDate";


    ////////////////////////////


    private String createdBy;
    private String createdDate;
    private String modifiedBy;
    private String modifiedDate;
    private String syncID;

    private Long Id;
    private Long modifyDate;
    private long UserId;
    private String MahakId;
    private String DatabaseId;


    private int HowToPromotion;
    private int IsCalcAdditive;
    private int ReducedEffectOnPrice;
    private int MeghdarPromotion;
    private int CodeGood;
    private int tedad;
    private int ToolidCode;
    private int StoreCode;

    private double ToPayment;
    private double tool;
    private double arz;
    private double meghdar2;
    private double ghotr;
    private double meghdar;


    @SerializedName("PromotionCode")
    @Expose
    private int promotionCode;

    @SerializedName("PromotionId")
    @Expose
    private int promotionId;

    @SerializedName("PromotionDetailCode")
    @Expose
    private int promotionDetailCode;

    @SerializedName("PromotionDetailClientId")
    @Expose
    private int promotionDetailClientId;

    @SerializedName("PromotionDetailId")
    @Expose
    private int promotionDetailId;

    @SerializedName("OtherFields")
    @Expose
    private String promotionDetailOtherFields;

    @SerializedName("CreateDate")
    @Expose
    private String createDate;

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


    //How To Promotion
    public static int Takhfif_Mablaghe_Sabet = 1;
    public static int Takhfif_Darsadi = 2;
    public static int Takhfif_As_Sotooh = 3;
    public static int Eshantion_As_Haman_Kala = 4;
    public static int Eshantion_As_kalahaye_digar = 5;

    //default constructor with default values
    public PromotionDetail() {
        this.setMahakId(BaseActivity.getPrefMahakId());
        this.setDatabaseId(BaseActivity.getPrefDatabaseId());
        setUserId(BaseActivity.getPrefUserId());

    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Long modifyDate) {
        this.modifyDate = modifyDate;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
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


    public int getPromotionDetailCode() {
        return promotionDetailCode;
    }

    public void setPromotionDetailCode(int promotionDetailCode) {
        this.promotionDetailCode = promotionDetailCode;
    }

    public String getPromotionCode() {
        return String.valueOf(promotionCode);
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = ServiceTools.toInt(promotionCode);
    }

    public int getHowToPromotion() {
        return HowToPromotion;
    }

    public void setHowToPromotion(int howToPromotion) {
        HowToPromotion = howToPromotion;
    }

    public int getIsCalcAdditive() {
        return IsCalcAdditive;
    }

    public void setIsCalcAdditive(int isCalcAdditive) {
        IsCalcAdditive = isCalcAdditive;
    }

    public int getReducedEffectOnPrice() {
        return ReducedEffectOnPrice;
    }

    public void setReducedEffectOnPrice(int reducedEffectOnPrice) {
        ReducedEffectOnPrice = reducedEffectOnPrice;
    }

    public int getMeghdarPromotion() {
        return MeghdarPromotion;
    }

    public void setMeghdarPromotion(int meghdarPromotion) {
        MeghdarPromotion = meghdarPromotion;
    }

    public int getCodeGood() {
        return CodeGood;
    }

    public void setCodeGood(int codeGood) {
        CodeGood = codeGood;
    }

    public int getTedad() {
        return tedad;
    }

    public void setTedad(int tedad) {
        this.tedad = tedad;
    }

    public int getToolidCode() {
        return ToolidCode;
    }

    public void setToolidCode(int toolidCode) {
        ToolidCode = toolidCode;
    }

    public double getToPayment() {
        return ToPayment;
    }

    public void setToPayment(double toPayment) {
        ToPayment = toPayment;
    }

    public double getTool() {
        return tool;
    }

    public void setTool(double tool) {
        this.tool = tool;
    }

    public double getArz() {
        return arz;
    }

    public void setArz(double arz) {
        this.arz = arz;
    }

    public double getMeghdar2() {
        return meghdar2;
    }

    public void setMeghdar2(double meghdar2) {
        this.meghdar2 = meghdar2;
    }

    public double getGhotr() {
        return ghotr;
    }

    public void setGhotr(double ghotr) {
        this.ghotr = ghotr;
    }

    public double getMeghdar() {
        return meghdar;
    }

    public void setMeghdar(double meghdar) {
        this.meghdar = meghdar;
    }

    public int getStoreCode() {
        return StoreCode;
    }

    public void setStoreCode(int storeCode) {
        StoreCode = storeCode;
    }

    public String getSyncID() {
        return syncID;
    }

    public void setSyncID(String syncID) {
        this.syncID = syncID;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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

    public int getPromotionDetailClientId() {
        return promotionDetailClientId;
    }

    public void setPromotionDetailClientId(int promotionDetailClientId) {
        this.promotionDetailClientId = promotionDetailClientId;
    }

    public int getPromotionDetailId() {
        return promotionDetailId;
    }

    public void setPromotionDetailId(int promotionDetailId) {
        this.promotionDetailId = promotionDetailId;
    }

    public String getPromotionDetailOtherFields() {
        return promotionDetailOtherFields;
    }

    public void setPromotionDetailOtherFields(String promotionDetailOtherFields) {
        this.promotionDetailOtherFields = promotionDetailOtherFields;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }
}
