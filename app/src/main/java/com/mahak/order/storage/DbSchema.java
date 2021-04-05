package com.mahak.order.storage;

import android.provider.BaseColumns;

public class DbSchema {

    public static final String DATABASE_NAME = "DB_MahakOrder";
    public static final int DATABASE_VERSION = 7;

    public static class Userschema implements BaseColumns {

        public static final String TABLE_NAME = "User";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_USERNAME = "UserName";
        public static final String COLUMN_PASSWORD = "Password";
        public static final String COLUMN_TYPE = "Type";
        public static final String COLUMN_LOGINDATE = "LoginDate";
        public static final String COLUMN_MAHAK_ID = "MahakId";
        public static final String COLUMN_DATABASE_ID = "DatabaseId";
        public static final String COLUMN_MASTER_ID = "MasterId";
        public static final String COLUMN_MODIFYDATE = "ModifyDate";
        public static final String COLUMN_PACKAGE_SERIAL = "PackageSerial";
        public static final String COLUMN_SYNC_ID = "SyncId";
        public static final String COLUMN_DATE_SYNC = "DateSync";
        public static final String COLUMN_StoreCode = "StoreCode";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_UserToken = "UserToken";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY ," +
                COLUMN_NAME + " TEXT," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_PASSWORD + " TEXT," +
                COLUMN_TYPE + " NUMERIC," +
                COLUMN_LOGINDATE + " NUMERIC," +
                COLUMN_MAHAK_ID + " TEXT, " +
                COLUMN_DATABASE_ID + " TEXT, " +
                COLUMN_MASTER_ID + " NUMERIC," +
                COLUMN_MODIFYDATE + " NUMERIC," +
                COLUMN_PACKAGE_SERIAL + " TEXT," +
                COLUMN_SYNC_ID + " TEXT, " +
                COLUMN_DATE_SYNC + " NUMERIC, " +
                COLUMN_StoreCode + " TEXT, " +
                COLUMN_USER_ID + " TEXT, " +
                COLUMN_UserToken + " NUMERIC )";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class Customerschema implements BaseColumns {

        public static final String TABLE_NAME = "Customers";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_PersonId = "PersonId";
        public static final String COLUMN_PersonClientId = "PersonClientId";
        public static final String COLUMN_PersonCode = "PersonCode";
        public static final String COLUMN_PersonGroupId = "PersonGroupId";
        public static final String COLUMN_PersonGroupCode = "PersonGroupCode";
        public static final String COLUMN_PersonType = "PersonType";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_FirstName = "FirstName";
        public static final String COLUMN_LastName = "LastName";
        public static final String COLUMN_ORGANIZATION = "Organization";
        public static final String COLUMN_Gender = "Gender";
        public static final String COLUMN_NationalCode = "NationalCode";
        public static final String COLUMN_CREDIT = "Credit";
        public static final String COLUMN_BALANCE = "Balance";
        public static final String COLUMN_Email = "Email";
        public static final String COLUMN_UserName = "UserName";
        public static final String COLUMN_Password = "Password";
        public static final String COLUMN_DiscountPercent = "DiscountPercent";
        public static final String COLUMN_SellPriceLevel = "SellPriceLevel";
        public static final String COLUMN_STATE = "State";
        public static final String COLUMN_CITY = "City";
        public static final String COLUMN_CityCode = "CityCode";
        public static final String COLUMN_ZONE = "Zone";
        public static final String COLUMN_ADDRESS = "Address";
        public static final String COLUMN_LATITUDE = "Latitude";
        public static final String COLUMN_LONGITUDE = "Longitude";
        public static final String COLUMN_PHONE = "Phone";
        public static final String COLUMN_Fax = "Fax";
        public static final String COLUMN_MOBILE = "Mobile";
        public static final String COLUMN_SHIFT = "Shift";
        public static final String COLUMN_Deleted = "Deleted";

        public static final String COLUMN_MODIFYDATE = "ModifyDate";
        public static final String COLUMN_PUBLISH = "Publish";
        public static final String COLUMN_MAHAK_ID = "MahakId";
        public static final String COLUMN_DATABASE_ID = "DatabaseId";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_HasOrder = "HasOrder";

        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        public static final String INSERT1 = "INSERT INTO  " + TABLE_NAME + "(" + COLUMN_NAME + ") VALUES ('Guest')";
        public static final String DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = 1";
    }

    public static class Visitorschema implements BaseColumns {

        public static final String TABLE_NAME = "Visitors";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_VisitorCode = "VisitorCode";
        public static final String COLUMN_MODIFYDATE = "ModifyDate";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_USERNAME = "UserName";
        public static final String COLUMN_STORECODE = "StoreCode";
        public static final String COLUMN_TELL = "Tell";
        public static final String COLUMN_BANKCODE = "BankCode";
        public static final String COLUMN_CASHCODE = "CashCode";
        public static final String COLUMN_PriceAccess = "PriceAccess";
        public static final String COLUMN_CostLevelAccess = "CostLevelAccess";
        public static final String COLUMN_Sell_DefaultCostLevel = "Sell_DefaultCostLevel";
        public static final String COLUMN_SelectedCostLevels = "SelectedCostLevels";
        public static final String COLUMN_TotalCredit = "TotalCredit";
        public static final String COLUMN_ChequeCredit = "ChequeCredit";
        public static final String COLUMN_DatabaseId = "DatabaseId";
        public static final String COLUMN_MahakId = "MahakId";
        public static final String COLUMN_VisitorId = "VisitorId";
        public static final String COLUMN_VisitorClientId = "VisitorClientId";
        public static final String COLUMN_Password = "Password";
        public static final String COLUMN_PersonCode = "PersonCode";
        public static final String COLUMN_VisitorType = "VisitorType";
        public static final String COLUMN_DeviceId = "DeviceId";
        public static final String COLUMN_Active = "Active";
        public static final String COLUMN_Color = "Color";
        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";


        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    public static class VisitorProductSchema implements BaseColumns {

        public static final String TABLE_NAME = "VisitorProduct";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_DATABASE_ID = "DatabaseId";

        public static final String COLUMN_VisitorProductId = "VisitorProductId";
        public static final String COLUMN_ProductDetailId = "ProductDetailId";
        public static final String COLUMN_USER_ID = "UserId";

        public static final String COLUMN_Count1 = "Count1";
        public static final String COLUMN_Count2 = "Count2";
        public static final String COLUMN_Price = "Price";
        public static final String COLUMN_Serials = "Serials";
        public static final String COLUMN_Deleted = "Deleted";

        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class VisitorPeopleSchema implements BaseColumns {

        public static final String TABLE_NAME = "VisitorPeople";
        public static final String COLUMN_ID = "Id";

        public static final String COLUMN_VisitorPersonId = "VisitorPersonId";
        public static final String COLUMN_PersonId = "PersonId";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_Deleted = "Deleted";

        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class PromotionSchema implements BaseColumns {

        public static final String TABLE_NAME = "Promotion";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_DatabaseId = "DatabaseId";
        public static final String COLUMN_MahakId = "MahakId";
        public static final String COLUMN_MODIFYDATE = "ModifyDate";
        public static final String COLUMN_CreatedBy = "CreatedBy";
        public static final String COLUMN_CreatedDate = "CreatedDate";
        public static final String COLUMN_ModifiedBy = "ModifiedBy";
        public static final String COLUMN_PromotionCode = "PromotionCode";
        public static final String COLUMN_NamePromotion = "NamePromotion";
        public static final String COLUMN_PriorityPromotion = "PriorityPromotion";
        public static final String COLUMN_DateStart = "DateStart";
        public static final String COLUMN_DateEnd = "DateEnd";
        public static final String COLUMN_TimeStart = "TimeStart";
        public static final String COLUMN_TimeEnd = "TimeEnd";
        public static final String COLUMN_LevelPromotion = "LevelPromotion";
        public static final String COLUMN_AccordingTo = "AccordingTo";
        public static final String COLUMN_IsCalcLinear = "IsCalcLinear";
        public static final String COLUMN_TypeTasvieh = "TypeTasvieh";
        public static final String COLUMN_DeadlineTasvieh = "DeadlineTasvieh";
        public static final String COLUMN_IsActive = "IsActive";
        public static final String COLUMN_DesPromotion = "DesPromotion";
        public static final String COLUMN_IsAllCustomer = "IsAllCustomer";
        public static final String COLUMN_IsAllVisitor = "IsAllVisitor";
        public static final String COLUMN_IsAllGood = "IsAllGood";
        public static final String COLUMN_IsAllStore = "IsAllStore";
        public static final String COLUMN_AggregateWithOther = "AggregateWithOther";

        public static final String COLUMN_PromotionId = "PromotionId";
        public static final String COLUMN_PromotionClientId = "PromotionClientId";
        public static final String COLUMN_Visitors = "Visitors";
        public static final String COLUMN_Stores = "Stores";
        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";
        public static final String COLUMN_Deleted = "Deleted";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class PromotionDetailSchema implements BaseColumns {

        public static final String TABLE_NAME = "PromotionDetail";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_DatabaseId = "DatabaseId";
        public static final String COLUMN_MahakId = "MahakId";
        public static final String COLUMN_MODIFYDATE = "ModifyDate";
        public static final String COLUMN_CreatedBy = "CreatedBy";
        public static final String COLUMN_CreatedDate = "CreatedDate";
        public static final String COLUMN_ModifiedBy = "ModifiedBy";

        public static final String COLUMN_HowToPromotion = "HowToPromotion";
        public static final String COLUMN_IsCalcAdditive = "IsCalcAdditive";
        public static final String COLUMN_ReducedEffectOnPrice = "ReducedEffectOnPrice";
        public static final String COLUMN_ToPayment = "ToPayment";
        public static final String COLUMN_MeghdarPromotion = "MeghdarPromotion";
        public static final String COLUMN_StoreCode = "StoreCode";
        public static final String COLUMN_CodeGood = "CodeGood";
        public static final String COLUMN_tool = "tool";
        public static final String COLUMN_arz = "arz";
        public static final String COLUMN_tedad = "tedad";
        public static final String COLUMN_meghdar2 = "meghdar2";
        public static final String COLUMN_ghotr = "ghotr";
        public static final String COLUMN_ToolidCode = "ToolidCode";
        public static final String COLUMN_meghdar = "meghdar";
        public static final String COLUMN_SyncID = "SyncID";

        public static final String COLUMN_PromotionDetailCode = "PromotionDetailCode";
        public static final String COLUMN_PromotionDetailId = "PromotionDetailId";
        public static final String COLUMN_PromotionDetailClientId = "PromotionDetailClientId";
        public static final String COLUMN_PromotionCode = "PromotionCode";
        public static final String COLUMN_PromotionId = "PromotionId";
        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    }

    public static class PromotionEntitySchema implements BaseColumns {

        public static final String TABLE_NAME = "PromotionEntity";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_DatabaseId = "DatabaseId";
        public static final String COLUMN_MahakId = "MahakId";
        public static final String COLUMN_MODIFYDATE = "ModifyDate";
        public static final String COLUMN_ModifiedBy = "ModifiedBy";
        public static final String COLUMN_SyncID = "SyncID";

        public static final String COLUMN_CodePromotion = "CodePromotion";
        public static final String COLUMN_PromotionCode = "PromotionCode";

        public static final String COLUMN_CodeEntity = "CodeEntity";
        public static final String COLUMN_CodePromotionEntity = "CodePromotionEntity";
        public static final String COLUMN_EntityType = "EntityType";

        public static final String COLUMN_CreatedBy = "CreatedBy";
        public static final String COLUMN_CreatedDate = "CreatedDate";

        public static final String COLUMN_PromotionEntityId = "PromotionEntityId";
        public static final String COLUMN_PromotionId = "PromotionId";
        public static final String COLUMN_PromotionEntityClientId = "PromotionEntityClientId";

        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class CustomersGroupschema implements BaseColumns {

        public static final String TABLE_NAME = "CustomersGroups";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_COLOR = "Color";
        public static final String COLUMN_ICON = "Icon";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_MODIFYDATE = "ModifyDate";
        public static final String COLUMN_MAHAK_ID = "MahakId";
        public static final String COLUMN_DATABASE_ID = "DatabaseId";
        public static final String COLUMN_PersonGroupCode = "PersonGroupCode";
        public static final String COLUMN_SellPriceLevel = "SellPriceLevel";
        public static final String COLUMN_PersonGroupId = "PersonGroupId";
        public static final String COLUMN_PersonGroupClientId = "PersonGroupClientId";
        public static final String COLUMN_DiscountPercent = "DiscountPercent";
        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    public static class Productschema implements BaseColumns {

        public static final String TABLE_NAME = "Products";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_CATEGORYID = "CategoryId";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_REALPRICE = "RealPrice";
        public static final String COLUMN_TAGS = "Tags";
        public static final String COLUMN_CUSTOMERPRICE = "CustomerPrice";
        public static final String COLUMN_UnitRatio = "UnitRatio";
        public static final String COLUMN_CODE = "Code";
        public static final String COLUMN_IMAGE = "Image";
        public static final String COLUMN_MIN = "Min";
        public static final String COLUMN_MODIFYDATE = "ModifyDate";
        public static final String COLUMN_PUBLISH = "Publish";
        public static final String COLUMN_UNITNAME = "UnitName";
        public static final String COLUMN_UNITNAME2 = "UnitName2";
        public static final String COLUMN_MAHAK_ID = "MahakId";
        public static final String COLUMN_DATABASE_ID = "DatabaseId";
        public static final String COLUMN_PRODUCT_CODE = "ProductCode";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_TAX = "Tax";
        public static final String COLUMN_CHARGE = "Charge";
        public static final String COLUMN_DiscountPercent = "DiscountPercent";
        public static final String COLUMN_Barcode = "Barcode";
        public static final String COLUMN_ProductId = "ProductId";
        public static final String COLUMN_ProductClientId = "ProductClientId";
        public static final String COLUMN_Deleted = "Deleted";

        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";

        public static final String COLUMN_WEIGHT = "Weight";
        public static final String COLUMN_Width = "Width";
        public static final String COLUMN_Height = "Height";
        public static final String COLUMN_Length = "Length";


        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class ProductDetailSchema implements BaseColumns {

        public static final String TABLE_NAME = "ProductDetail";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_ProductDetailId = "ProductDetailId";
        public static final String COLUMN_ProductDetailClientId = "ProductDetailClientId";
        public static final String COLUMN_ProductDetailCode = "ProductDetailCode";
        public static final String COLUMN_ProductId = "ProductId";
        public static final String COLUMN_Properties = "Properties";

        public static final String COLUMN_Count1 = "Count1";
        public static final String COLUMN_Count2 = "Count2";

        public static final String COLUMN_Barcode = "Barcode";

        public static final String COLUMN_USER_ID = "UserId";

        public static final String COLUMN_Price1 = "Price1";
        public static final String COLUMN_Price2 = "Price2";
        public static final String COLUMN_Price3 = "Price3";
        public static final String COLUMN_Price4 = "Price4";
        public static final String COLUMN_Price5 = "Price5";
        public static final String COLUMN_Price6 = "Price6";
        public static final String COLUMN_Price7 = "Price7";
        public static final String COLUMN_Price8 = "Price8";
        public static final String COLUMN_Price9 = "Price9";
        public static final String COLUMN_Price10 = "Price10";
        public static final String COLUMN_DefaultSellPriceLevel = "DefaultSellPriceLevel";

        public static final String COLUMN_Discount = "Discount";
        public static final String COLUMN_Discount1 = "Discount1";
        public static final String COLUMN_Discount2 = "Discount2";
        public static final String COLUMN_Discount3 = "Discount3";
        public static final String COLUMN_Discount4 = "Discount4";
        public static final String COLUMN_DefaultDiscountLevel = "DefaultDiscountLevel";
        public static final String COLUMN_DiscountType = "DiscountType";

        public static final String COLUMN_CustomerPrice = "CustomerPrice";
        public static final String COLUMN_Serials = "Serials";
        public static final String COLUMN_Deleted = "Deleted";

        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class PriceLevelNameSchema implements BaseColumns {
        public static final String TABLE_NAME = "PriceLevelNames";
        public static final String _ID = "Id";
        public static final String _SyncId = "SyncId";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String _MODIFY_DATE = "ModifyDate";
        public static final String PRICE_LEVEL_CODE = "PriceLevelCode";
        public static final String PRICE_LEVEL_NAME = "PriceLevelName";

        public static final String CostLevelNameId = "CostLevelNameId";
        public static final String CostLevelNameClientId = "CostLevelNameClientId";
        public static final String DataHash = "DataHash";
        public static final String UpdateDate = "UpdateDate";
        public static final String CreateDate = "CreateDate";
        public static final String CreateSyncId = "CreateSyncId";
        public static final String UpdateSyncId = "UpdateSyncId";
        public static final String RowVersion = "RowVersion";


        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class ReceivedTransfersschema implements BaseColumns {

        public static final String TABLE_NAME = "ReceivedTransfers";

        public static final String COLUMN_DatabaseId = "DatabaseId";
        public static final String COLUMN_MahakId = "MahakId";
        public static final String COLUMN_TransferStoreCode = "TransferStoreCode";
        public static final String COLUMN_ModifyDate = "ModifyDate";
        public static final String COLUMN_SyncId = "SyncId";
        public static final String COLUMN_TransferDate = "TransferDate";
        public static final String COLUMN_TransferStoreId = "TransferStoreId";
        public static final String COLUMN_IsAccepted = "IsAccepted";
        public static final String COLUMN_CreatedBy = "CreatedBy";
        public static final String COLUMN_SenderVisitorId = "SenderVisitorId";
        public static final String COLUMN_ModifiedBy = "ModifiedBy";
        public static final String COLUMN_Description = "Description";
        public static final String COLUMN_ReceiverVisitorId = "ReceiverVisitorId";

        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";
        public static final String COLUMN_TransferStoreClientId = "TransferStoreClientId";
        public static final String COLUMN_SenderStoreCode = "SenderStoreCode";
        public static final String COLUMN_ReceiverStoreCode = "ReceiverStoreCode";


        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_DatabaseId + " TEXT ," +
                COLUMN_MahakId + " TEXT," +
                COLUMN_TransferStoreCode + " TEXT," +
                COLUMN_ModifyDate + " TEXT," +
                COLUMN_SyncId + " TEXT," +
                COLUMN_TransferDate + " TEXT," +
                COLUMN_TransferStoreId + " TEXT, " +
                COLUMN_IsAccepted + " TEXT, " +
                COLUMN_CreatedBy + " TEXT," +
                COLUMN_SenderVisitorId + " TEXT," +
                COLUMN_ModifiedBy + " TEXT," +
                COLUMN_Description + " TEXT, " +
                COLUMN_ReceiverVisitorId + " TEXT )";


        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class ReceivedTransferProductsschema implements BaseColumns {

        public static final String TABLE_NAME = "ReceivedTransferProducts";
        public static final String COLUMN_Id = "Id";
        public static final String COLUMN_DatabaseId = "DatabaseId";
        public static final String COLUMN_MahakId = "MahakId";
        public static final String COLUMN_ModifyDate = "ModifyDate";

        public static final String COLUMN_ProductId = "ProductId";
        public static final String COLUMN_Count1 = "Count1";
        public static final String COLUMN_Name = "Name";
        public static final String COLUMN_TransferId = "TransferId";
        public static final String COLUMN_CreatedDate = "CreatedDate";
        public static final String COLUMN_CreatedBy = "CreatedBy";
        public static final String COLUMN_Description = "Description";

        public static final String COLUMN_TransferStoreDetailId = "TransferStoreDetailId";
        public static final String COLUMN_TransferStoreDetailClientId = "TransferStoreDetailClientId";
        public static final String COLUMN_TransferStoreId = "TransferStoreId";
        public static final String COLUMN_ProductDetailId = "ProductDetailId";
        public static final String COLUMN_Count2 = "Count2";

        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";


        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_Id + " TEXT ," +
                COLUMN_DatabaseId + " TEXT," +
                COLUMN_MahakId + " TEXT," +
                COLUMN_ModifyDate + " TEXT," +
                COLUMN_ProductId + " TEXT," +
                COLUMN_Count1 + " TEXT," +
                COLUMN_Name + " TEXT, " +
                COLUMN_TransferId + " TEXT, " +
                COLUMN_CreatedDate + " TEXT," +
                COLUMN_CreatedBy + " TEXT," +
                COLUMN_Description + " TEXT )";


        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class ProductGroupSchema implements BaseColumns {
        public static final String TABLE_NAME = "ProductsGroups";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_PARENTID = "ParentId";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_COLOR = "Color";
        public static final String COLUMN_ICON = "Icon";
        public static final String COLUMN_MODIFYDATE = "ModifyDate";
        public static final String COLUMN_MAHAK_ID = "MahakId";
        public static final String COLUMN_DATABASE_ID = "DatabaseId";
        public static final String COLUMN_ProductCategoryCode = "ProductCategoryCode";
        public static final String COLUMN_ProductCategoryId = "ProductCategoryId";
        public static final String COLUMN_ProductCategoryClientId = "ProductCategoryClientId";
        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    }

    public static class Orderschema implements BaseColumns {
        public static final String TABLE_NAME = "Orders";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_PersonId = "PersonId";
        public static final String COLUMN_PersonClientId = "PersonClientId";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_ORDERDATE = "OrderDate";
        public static final String COLUMN_DELIVERYDATE = "DeliveryDate";
        public static final String COLUMN_DISCOUNT = "Discount";
        public static final String COLUMN_DESCRIPTION = "Description";
        public static final String COLUMN_SETTLEMEMNTTYPE = "SettlementType";
        public static final String COLUMN_IMMEDIATE = "Immediate";
        public static final String COLUMN_TYPE = "Type";
        public static final String COLUMN_CODE = "Code";
        public static final String COLUMN_MODIFYDATE = "ModifyDate";
        public static final String COLUMN_PUBLISH = "Publish";
        public static final String COLUMN_MAHAK_ID = "MahakId";
        public static final String COLUMN_DATABASE_ID = "DatabaseId";
        public static final String COLUMN_OrderCode = "OrderCode";
        public static final String COLUMN_GIFT_TYPE = "GiftType";
        public static final String COLUMN_PROMOTION_CODE = "PromotionCode";
        public static final String COLUMN_ISFINAL = "IsFinal";
        public static final String COLUMN_ReturnReasonId = "ReturnReasonId";

        public static final String COLUMN_OrderId = "OrderId";
        public static final String COLUMN_OrderClientId = "OrderClientId";

        public static final String COLUMN_ReceiptId = "ReceiptId";
        public static final String COLUMN_ReceiptClientId = "ReceiptClientId";

        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";

        public static final String COLUMN_SendCost = "SendCost";
        public static final String COLUMN_OtherCost = "OtherCost";
        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class OrderDetailSchema implements BaseColumns {

        public static final String TABLE_NAME = "OrderDetail";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_OrderDetailId = "OrderDetailId";
        public static final String COLUMN_OrderDetailClientId = "OrderDetailClientId";
        public static final String COLUMN_OrderClientId = "OrderClientId";
        public static final String COLUMN_OrderId = "OrderId";
        public static final String COLUMN_ProductDetailId = "ProductDetailId";
        public static final String COLUMN_ProductId = "ProductId";
        public static final String COLUMN_Price = "Price";
        public static final String COLUMN_Count1 = "Count1";
        public static final String COLUMN_Count2 = "Count2";
        public static final String COLUMN_SumCountBaJoz = "SumCountBaJoz";
        public static final String COLUMN_GiftCount1 = "GiftCount1";
        public static final String COLUMN_GiftCount2 = "GiftCount2";
        public static final String COLUMN_GiftType = "GiftType";
        public static final String COLUMN_Description = "Description";
        public static final String COLUMN_TaxPercent = "TaxPercent";
        public static final String COLUMN_ChargePercent = "ChargePercent";
        public static final String COLUMN_Discount = "Discount";
        public static final String COLUMN_DiscountType = "DiscountType";
        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";
        public static final String COLUMN_CostLevel = "CostLevel";
        public static final String COLUMN_PROMOTION_CODE = "PromotionCode";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class OrderDetailPropertySchema implements BaseColumns {

        public static final String TABLE_NAME = "OrderDetailProperty";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_OrderDetailClientId = "OrderDetailClientId";
        public static final String COLUMN_OrderId = "OrderId";
        public static final String COLUMN_ProductDetailId = "ProductDetailId";
        public static final String COLUMN_ProductId = "ProductId";
        public static final String COLUMN_OrderDetailPropertyId = "OrderDetailPropertyId";
        public static final String COLUMN_Count1 = "Count1";
        public static final String COLUMN_Count2 = "Count2";
        public static final String COLUMN_SumCountBaJoz = "SumCountBaJoz";
        public static final String COLUMN_Max = "Max";
        public static final String COLUMN_ProductSpec = "ProductSpec";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    public static class NonRegisterSchema implements BaseColumns {

        public static final String TABLE_NAME = "NonRegisters";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_PersonId = "PersonId";
        public static final String COLUMN_PersonClientId = "PersonClientId";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_NonRegister_DATE = "NonRegisterDate";
        public static final String COLUMN_DESCRIPTION = "Description";
        public static final String COLUMN_MODIFYDATE = "ModifyDate";
        public static final String COLUMN_PUBLISH = "Publish";
        public static final String COLUMN_MAHAK_ID = "MahakId";
        public static final String COLUMN_DATABASE_ID = "DatabaseId";
        public static final String COLUMN_NotRegisterCode = "NotRegisterCode";
        public static final String COLUMN_CODE = "Code";
        public static final String COLUMN_CustomerName = "CustomerName";
        public static final String COLUMN_ReasonCode = "ReasonCode";

        public static final String COLUMN_NotRegisterId = "NotRegisterId";
        public static final String COLUMN_NotRegisterClientId = "NotRegisterClientId";
        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    public static class Receiptschema implements BaseColumns {
        public static final String TABLE_NAME = "Receipts";
        public static final String COLUMN_ID = "Id";
        public static final String PERSON_ID = "PersonId";
        public static final String COLUMN_PersonClientId = "PersonClientId";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_CASHAMOUNT = "CashAmount";
        public static final String COLUMN_DESCRIPTION = "Description";
        public static final String COLUMN_MODIFYDATE = "ModifyDate";
        public static final String COLUMN_DATE = "Date";
        public static final String COLUMN_PUBLISH = "Publish";
        public static final String COLUMN_MAHAK_ID = "MahakId";
        public static final String COLUMN_DATABASE_ID = "DatabaseId";
        public static final String COLUMN_ReceiptCode = "ReceiptCode";
        public static final String COLUMN_CODE = "Code";
        public static final String COLUMN_CashCode = "CashCode";
        public static final String COLUMN_ReceiptId = "ReceiptId";
        public static final String COLUMN_ReceiptClientId = "ReceiptClientId";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class PayableSchema implements BaseColumns {

        public static final String TABLE_NAME = "Payables";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_TransferType = "TransferType";
        public static final String COLUMN_TransferCode = "TransferCode";
        public static final String COLUMN_TransferDate = "TransferDate";
        public static final String COLUMN_Receiverid = "Receiverid";
        public static final String COLUMN_Comment = "Comment";
        public static final String COLUMN_PayerId = "PayerId";
        public static final String COLUMN_VisitorId = "VisitorId";
        public static final String COLUMN_DatabaseId = "DatabaseId";
        public static final String COLUMN_MahakId = "MahakId";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_PUBLISH = "Publish";
        public static final String COLUMN_Price = "Price";

        public static final String COLUMN_TransferAccountId = "TransferAccountId";
        public static final String COLUMN_TransferAccountClientId = "TransferAccountClientId";
        public static final String COLUMN_TransferAccountCode = "TransferAccountCode";
        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";


        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COLUMN_TransferType + " INTEGER," +
                COLUMN_TransferCode + " INTEGER," +
                COLUMN_TransferDate + " INTEGER," +
                COLUMN_Receiverid + " INTEGER," +
                COLUMN_Comment + " TEXT," +
                COLUMN_PayerId + " INTEGER, " +
                COLUMN_VisitorId + " INTEGER, " +
                COLUMN_DatabaseId + " TEXT," +
                COLUMN_MahakId + " TEXT," +
                COLUMN_USER_ID + " NUMERIC," +
                COLUMN_PUBLISH + " NUMERIC, " +
                COLUMN_Price + " INTEGER )";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class Chequeschema implements BaseColumns {
        public static final String TABLE_NAME = "Cheques";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_RECEIPTID = "ReceiptId";
        public static final String COLUMN_NUMBER = "Number";
        public static final String COLUMN_BANK = "Bank";
        public static final String COLUMN_BRANCH = "Branch";
        public static final String COLUMN_AMOUNT = "Amount";
        public static final String COLUMN_DESCRIPTION = "Description";
        public static final String COLUMN_MODIFYDATE = "ModifyDate";
        public static final String COLUMN_DATE = "Date";
        public static final String COLUMN_TYPE = "Type";
        public static final String COLUMN_PUBLISH = "Publish";
        public static final String COLUMN_MAHAK_ID = "MahakId";
        public static final String COLUMN_DATABASE_ID = "DatabaseId";
        public static final String COLUMN_ChequeCode = "ChequeCode";
        public static final String COLUMN_BANK_ID = "BankId";
        public static final String COLUMN_ChequeClientId = "ChequeClientId";
        public static final String COLUMN_ReceiptClientId = "ReceiptClientId";
        public static final String COLUMN_BankClientId = "BankClientId";


        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class CheckListschema implements BaseColumns {
        public static final String TABLE_NAME = "CheckList";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_CUSTOMERID = "CustomerId";
        public static final String COLUMN_STATUS = "Status";
        public static final String COLUMN_TYPE = "Type";
        public static final String COLUMN_DESCRIPTION = "Description";
        public static final String COLUMN_MODIFYDATE = "ModifyDate";
        public static final String COLUMN_PUBLISH = "Publish";
        public static final String COLUMN_MAHAK_ID = "MahakId";
        public static final String COLUMN_DATABASE_ID = "DatabaseId";
        public static final String COLUMN_CHECK_LIST_CODE = "CheckListCode";
        public static final String COLUMN_ChecklistId = "CheckListId";
        public static final String COLUMN_ChecklistClientId = "CheckListClientId";
        public static final String COLUMN_VisitorId = "VisitorId";
        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class Transactionslogschema implements BaseColumns {
        public static final String TABLE_NAME = "TransactionsLog";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_PersonId = "PersonId";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_TRANSACTIONID = "TransactionId";
        public static final String COLUMN_TYPE = "Type";
        public static final String COLUMN_DEBITAMOUNT = "DebitAmount";
        public static final String COLUMN_CREDITAMOUNT = "CreditAmount";
        public static final String COLUMN_Balance = "Balance";
        public static final String COLUMN_STATUS = "Status";
        public static final String COLUMN_DESCRIPTION = "Description";
        public static final String COLUMN_MODIFYDATE = "ModifyDate";
        public static final String COLUMN_MAHAK_ID = "MahakId";
        public static final String COLUMN_DATABASE_ID = "DatabaseId";
        public static final String COLUMN_TransactionCode = "TransactionCode";
        public static final String COLUMN_DATE = "Date";

        public static final String COLUMN_TransactionClientId = "TransactionClientId";
        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";


        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class BanksSchema implements BaseColumns {
        public static final String TABLE_NAME = "Banks";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_MAHAK_ID = "MahakId";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_DATABASE_ID = "DatabaseId";
        public static final String COLUMN_BANK_CODE = "BankCode";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_DESCRIPTION = "Description";
        public static final String COLUMN_MODIFYDATE = "ModifyDate";
        public static final String COLUMN_BankId = "BankId";
        public static final String COLUMN_BankClientId = "BankClientId";
        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class PropertyDescriptionSchema implements BaseColumns {

        public static final String TABLE_NAME = "PropertyDescription";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_DATABASE_ID = "DatabaseId";

        public static final String COLUMN_PropertyDescriptionId = "PropertyDescriptionId";
        public static final String COLUMN_PropertyDescriptionClientId = "PropertyDescriptionClientId";
        public static final String COLUMN_PropertyDescriptionCode = "PropertyDescriptionCode";

        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_Title = "Title";
        public static final String COLUMN_EmptyTitle = "EmptyTitle";
        public static final String COLUMN_DataType = "DataType";
        public static final String COLUMN_DisplayType = "DisplayType";
        public static final String COLUMN_ExtraData = "ExtraData";
        public static final String COLUMN_Description = "Description";

        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class ReasonsSchema implements BaseColumns {
        public static final String TABLE_NAME = "Reasons";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_MAHAK_ID = "MahakId";
        public static final String COLUMN_DATABASE_ID = "DatabaseId";
        public static final String COLUMN_ReturnReasonCode = "ReturnReasonCode";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_TYPE = "Type";
        public static final String COLUMN_DESCRIPTION = "Description";
        public static final String COLUMN_MODIFYDATE = "ModifyDate";

        public static final String COLUMN_ReturnReasonId = "ReturnReasonId";
        public static final String COLUMN_ReturnReasonClientId = "ReturnReasonClientId";
        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";


        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class ExtraDataSchema implements BaseColumns {

        public static final String TABLE_NAME = "ExtraData";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_MAHAK_ID = "MahakId";
        public static final String COLUMN_DATABASE_ID = "DatabaseId";
        public static final String COLUMN_MODIFYDATE = "ModifyDate";
        public static final String COLUMN_USER_ID = "UserId";

        public static final String COLUMN_Data = "Data";
        public static final String COLUMN_ExtraDataId = "ExtraDataId";
        public static final String COLUMN_ItemType = "ItemType";
        public static final String COLUMN_ItemId = "ItemId";
        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class ProductCategorySchema implements BaseColumns {

        public static final String TABLE_NAME = "ProductCategory";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_CategoryCode = "CategoryCode";
        public static final String COLUMN_ProductCode = "ProductCode";
        public static final String COLUMN_USER_ID = "UserId";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class CategorySchema implements BaseColumns {

        public static final String TABLE_NAME = "Category";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_CategoryCode = "CategoryCode";
        public static final String COLUMN_ParentCode = "ParentCode";
        public static final String COLUMN_CategoryName = "CategoryName";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class CityZoneSchema implements BaseColumns {
        public static final String TABLE_NAME = "CityZone";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_ZoneCode = "ZoneCode";
        public static final String COLUMN_ParentCode = "ParentCode";
        public static final String COLUMN_ZoneName = "ZoneName";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class ConfigsSchema implements BaseColumns {

        public static final String TABLE_NAME = "Configs";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_MASTER_ID = "MasterId";
        public static final String COLUMN_MAHAK_ID = "MahakId";
        public static final String COLUMN_DATABASE_ID = "DatabaseId";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_TYPE = "Type";
        public static final String COLUMN_VALUE = "Value";
        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_DESCRIPTION = "Description";
        public static final String COLUMN_MODIFY_DATE = "ModifyDate";
        public static final String COLUMN_CODE = "Code";
    }

    public static final class NotificationSchema implements BaseColumns {
        public static final String TABLE_NAME = "Notifications";
        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_MESSAGE = "Message";
        public static final String COLUMN_FULLMESSAGE = "FullMessage";
        public static final String COLUMN_TYPE = "Type";
        public static final String COLUMN_DATA = "Data";
        public static final String COLUMN_ISREAD = "IsRead";
        public static final String COLUMN_DATE = "DateNotification";
        public static final String COLUMN_USER_ID = "UserId";

        public static int[] COLUMNINDEXES;

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_MESSAGE + " TEXT," +
                COLUMN_FULLMESSAGE + " TEXT," +
                COLUMN_TYPE + " TEXT," +
                COLUMN_DATA + " TEXT," +
                COLUMN_ISREAD + " INTEGER, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_DATE + " INTEGER )";


        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class PicturesProductSchema implements BaseColumns {
        public static final String TABLE_NAME = "PicturesProduct";

        public static final String COLUMN_MAHAK_ID = "mahakId";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_LAST_UPDATE = "lastUpdate";
        public static final String COLUMN_DATABASE_ID = "dataBaseId";

        public static final String COLUMN_ITEM_ID = "ItemId";
        public static final String COLUMN_ITEM_TYPE = "ItemType";

        public static final String COLUMN_PICTURE_ID = "pictureId";
        public static final String COLUMN_PRODUCT_ID = "productId";
        public static final String COLUMN_PictureCode = "PictureCode";
        public static final String COLUMN_FILE_NAME = "fileName";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_FILE_SIZE = "fileSize";

        public static final String COLUMN_PictureClientId = "PictureClientId";
        public static final String COLUMN_DisplayOrder = "DisplayOrder";
        public static final String COLUMN_Width = "Width";
        public static final String COLUMN_Height = "Height";
        public static final String COLUMN_Format = "Format";
        public static final String COLUMN_PictureHash = "PictureHash";
        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";


        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_PICTURE_ID + " INTEGER ," +
                COLUMN_PRODUCT_ID + " INTEGER ," +
                COLUMN_PRODUCT_ID + " INTEGER ," +
                COLUMN_PictureCode + " INTEGER ," +
                COLUMN_FILE_NAME + " TEXT ," +
                COLUMN_TITLE + " TEXT ," +
                COLUMN_URL + " TEXT ," +
                COLUMN_FILE_SIZE + " INTEGER ," +
                COLUMN_DATABASE_ID + " TEXT ," +
                COLUMN_MAHAK_ID + " TEXT ," +
                COLUMN_USER_ID + " TEXT ," +
                COLUMN_LAST_UPDATE + " INTEGER );";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class VisitorLocationSchema implements BaseColumns {

        public static final String TABLE_NAME = "VisitorLocation";
        public static final String COLUMN_uniqueID = "uniqueID";
        public static final String COLUMN_Create_DATE = "CreateDate";
        public static final String COLUMN_DATE = "Date";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_IS_SEND = "isSend";
        public static final String COLUMN_VISITOR_ID = "VisitorId";
        public static final String COLUMN_VisitorLocationId = "VisitorLocationId";
        public static final String COLUMN_RowVersion = "RowVersion";
        public static final String COLUMN_skipCount = "skipCount";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_Create_DATE + " INTEGER ," +
                COLUMN_LONGITUDE + " TEXT ," +
                COLUMN_LATITUDE + " TEXT ," +
                COLUMN_VISITOR_ID + " INTEGER ," +
                COLUMN_IS_SEND + " INTEGER )";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class SettingSchema implements BaseColumns {

        public static final String TABLE_NAME = "Setting";
        public static final String COLUMN_SettingId = "SettingId";
        public static final String COLUMN_SettingCode = "SettingCode";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_Value = "Value";
        public static final String COLUMN_Deleted = "Deleted";
        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";
    }

    public static class ZoneSchema implements BaseColumns {

        public static final String TABLE_NAME = "Zone";
        public static final String COLUMN_zoneId = "zoneId";
        public static final String COLUMN_visitorId = "visitorId";
        public static final String COLUMN_title = "title";
        public static final String COLUMN_createdBy = "createdBy";
        public static final String COLUMN_created = "created";
        public static final String COLUMN_lastModifiedBy = "lastModifiedBy";
        public static final String COLUMN_lastModified = "lastModified";
    }
    public static class ZoneLocationSchema implements BaseColumns {

        public static final String TABLE_NAME = "ZoneLocation";
        public static final String COLUMN_id = "id";
        public static final String COLUMN_zoneId = "zoneId";
        public static final String COLUMN_visitorId = "visitorId";
        public static final String COLUMN_latitude = "latitude";
        public static final String COLUMN_longitude = "longitude";
        public static final String COLUMN_createdBy = "createdBy";
        public static final String COLUMN_created = "created";
        public static final String COLUMN_lastModifiedBy = "lastModifiedBy";
        public static final String COLUMN_lastModified = "lastModified";
    }

}
