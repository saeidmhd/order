package com.mahak.order.common;

//created by saeid.mhd@gmail.com

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

public class Promotion {

    //TAG
    public static String TAG_SyncID = "SyncID";
    public static String TAG_CodePromotion = "CodePromotion";
    public static String TAG_Visitors = "Visitors";
    public static String TAG_Stores = "Stores";
    public static String TAG_OtherFields = "OtherFields";
    public static String TAG_NamePromotion = "namePromotion";
    public static String TAG_PriorityPromotion = "priorityPromotion";
    public static String TAG_DateStart = "dateStart";
    public static String TAG_DateEnd = "dateEnd";
    public static String TAG_TimeStart = "timeStart";
    public static String TAG_TimeEnd = "timeEnd";
    public static String TAG_LevelPromotion = "levelPromotion";
    public static String TAG_AccordingTo = "accordingTo";
    public static String TAG_IsCalcLinear = "isCalcLinear";
    public static String TAG_TypeTasvieh = "typeTasvieh";
    public static String TAG_DeadlineTasvieh = "deadlineTasvieh";
    public static String TAG_DesPromotion = "desPromotion";
    public static String TAG_IsAllCustomer = "isAllCustomer";
    public static String TAG_IsAllVisitor = "isAllVisitor";
    public static String TAG_IsAllGood = "isAllGood";
    public static String TAG_IsAllService = "IsAllService";
    public static String TAG_IsAllStore = "IsAllStore";
    public static String TAG_AggregateWithOther = "aggregateWithOther";
    public static String TAG_CreatedBy = "CreatedBy";
    public static String TAG_CreatedDate = "CreatedDate";
    public static String TAG_ModifiedBy = "ModifiedBy";
    public static String TAG_ModifiedDate = "ModifiedDate";

    ////////////////////////////

    //Entity
    public final static int EntityVisitor = 1;
    public final static int EntityCustomer = 2;
    public final static int EntityGroupCustomer = 3;
    public final static int EntityGoods = 4;
    public final static int EntityGroupGoods = 5;
    public final static int EntityStores = 6;

    //According TO
    public final static int Mablaghe_kole_Faktor = 0;
    public final static int Jame_Aghlame_Faktor = 1;
    public final static int Jame_Hajme_Faktor = 2;
    public final static int Jame_Vazne_Faktor = 3;
    public final static int Jame_anvae_Aghlame_faktor = 4;
    public final static int Mablaghe_Satr = 5;
    public final static int Meghdare_Satr = 6;

    //Level Promotion
    public static int BasedOnGood = 0;
    public static int BasedOnService = 1;
    public static int BasedOnGoodAndService = 2;

    //Calc Linear
    public static int Linear = 0;
    public static int Pellekani = 1;

    //Type Tasvieh
    public final static int Naghd = 1;
    public final static int cheque = 2;
    public final static int NaghdoCheque = 3;
    public final static int Nesieh = 4;

    //isAllCustomer
    public static int AllCustomers = 1;
    public static int SpecificCustomers = 0;

    //isAllVisitor
    public static int AllVisitors = 1;
    public static int SpecificVisitors = 0;

    //IsAllGoods
    public static int AllGoods = 1;
    public static int SpecificGoods = 0;

    //IsAllServices
    public static int AllServices = 1;
    public static int SpecificServices = 0;

    //IsAllServices
    public static int AllStores = 1;
    public static int SpecificStores = 0;

    //How to promotion
    public final static int takhfif_mablaghe_sabet = 1;
    public final static int takhfif_darsadi = 2;
    public final static int takhfif_Az_Sotooh = 3;
    public final static int eshantion_Az_hamanKala = 4;
    public final static int eshantion_Az_kalahaye_digar = 5;

    //isCalcAddictive
    public static String takhfif_mohasebe_soeoodi = "1";

    //GiftType

    public final static int Eshantion_Dasti = 1;
    public final static int Eshantion_Tarhi = 2;
    public final static int Takhfif_Satri = 3;
    public final static int Takhfif_Kole_Sefaresh = 4;

    private String syncID;
    private String createdBy;
    private String createdDate;
    private String modifiedBy;
    private String modifiedDate;

    private Long Id;
    private Long ModifyDate;
    private long UserId;
    private String MahakId;
    private String DatabaseId;

    private String namePromotion;
    private String dateStart;
    private String dateEnd;
    private String timeStart;
    private String timeEnd;
    private String desPromotion;
    private int priorityPromotion;
    private int levelPromotion;
    private int accordingTo;
    private int isCalcLinear;
    private int typeTasvieh;
    private int deadlineTasvieh;
    private int isActive;
    private int isAllCustomer;
    private int isAllGood;
    private int aggregateWithOther;
    private int isAllVisitor;
    private int isAllService;
    private int isAllStore;


    @SerializedName("PromotionId")
    @Expose
    private int promotionId;
    @SerializedName("RowVersion")
    @Expose
    private long rowVersion;
    @SerializedName("PromotionClientId")
    @Expose
    private long promotionClientId;
    @SerializedName("PromotionCode")
    @Expose
    private int promotionCode;
    @SerializedName("Visitors")
    @Expose
    private String visitors;
    @SerializedName("Stores")
    @Expose
    private String stores;

    @SerializedName("Deleted")
    @Expose
    private boolean deleted;
    @SerializedName("DataHash")
    @Expose
    private String dataHash;
    @SerializedName("CreateDate")
    @Expose
    private String createDate;
    @SerializedName("UpdateDate")
    @Expose
    private String updateDate;
    @SerializedName("CreateSyncId")
    @Expose
    private int createSyncId;
    @SerializedName("UpdateSyncId")
    @Expose
    private int updateSyncId;

    @SerializedName("OtherFields")
    @Expose
    private String promotionOtherFields;


    //default constructor with default values
    public Promotion() {
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
        return ModifyDate;
    }

    public void setModifyDate(Long modifyDate) {
        ModifyDate = modifyDate;
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


    public int getPriorityPromotion() {
        return priorityPromotion;
    }

    public void setPriorityPromotion(int priorityPromotion) {
        this.priorityPromotion = priorityPromotion;
    }

    public int getLevelPromotion() {
        return levelPromotion;
    }

    public void setLevelPromotion(int levelPromotion) {
        this.levelPromotion = levelPromotion;
    }

    public int getAccordingTo() {
        return accordingTo;
    }

    public void setAccordingTo(int accordingTo) {
        this.accordingTo = accordingTo;
    }

    public int getIsCalcLinear() {
        return isCalcLinear;
    }

    public void setIsCalcLinear(int isCalcLinear) {
        this.isCalcLinear = isCalcLinear;
    }

    public int getTypeTasvieh() {
        return typeTasvieh;
    }

    public void setTypeTasvieh(int typeTasvieh) {
        this.typeTasvieh = typeTasvieh;
    }

    public int getDeadlineTasvieh() {
        return deadlineTasvieh;
    }

    public void setDeadlineTasvieh(int deadlineTasvieh) {
        this.deadlineTasvieh = deadlineTasvieh;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getIsAllCustomer() {
        return isAllCustomer;
    }

    public void setIsAllCustomer(int isAllCustomer) {
        this.isAllCustomer = isAllCustomer;
    }

    public int getIsAllVisitor() {
        return isAllVisitor;
    }

    public void setIsAllVisitor(int isAllVisitor) {
        this.isAllVisitor = isAllVisitor;
    }

    public int getIsAllGood() {
        return isAllGood;
    }

    public void setIsAllGood(int isAllGood) {
        this.isAllGood = isAllGood;
    }

    public int getAggregateWithOther() {
        return aggregateWithOther;
    }

    public void setAggregateWithOther(int aggregateWithOther) {
        this.aggregateWithOther = aggregateWithOther;
    }

    public String getNamePromotion() {
        return namePromotion;
    }

    public void setNamePromotion(String namePromotion) {
        this.namePromotion = namePromotion;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDesPromotion() {
        return desPromotion;
    }

    public void setDesPromotion(String desPromotion) {
        this.desPromotion = desPromotion;
    }

    public int getIsAllService() {
        return isAllService;
    }

    public void setIsAllService(int isAllService) {
        this.isAllService = isAllService;
    }

    public int getIsAllStore() {
        return isAllStore;
    }

    public void setIsAllStore(int isAllStore) {
        this.isAllStore = isAllStore;
    }


    public String getSyncID() {
        return syncID;
    }

    public void setSyncID(String syncID) {
        this.syncID = syncID;
    }

    public String getVisitors() {
        return visitors;
    }

    public void setVisitors(String visitors) {
        this.visitors = visitors;
    }

    public String getStores() {
        return stores;
    }

    public void setStores(String stores) {
        this.stores = stores;
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

    public int getDeleted() {
        return deleted ? 1 : 0;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted == 1;
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

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public long getPromotionClientId() {
        return promotionClientId;
    }

    public void setPromotionClientId(long promotionClientId) {
        this.promotionClientId = promotionClientId;
    }

    public String getPromotionOtherFields() {
        return promotionOtherFields;
    }

    public void setPromotionOtherFields(String promotionOtherFields) {
        this.promotionOtherFields = promotionOtherFields;
    }

    public String getPromotionCode() {
        return String.valueOf(promotionCode);
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = ServiceTools.toInt(promotionCode);
    }
}
