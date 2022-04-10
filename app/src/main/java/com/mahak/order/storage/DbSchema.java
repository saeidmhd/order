package com.mahak.order.storage;

import android.provider.BaseColumns;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BuildConfig;
import com.mahak.order.common.ServiceTools;

public class DbSchema {

    public static final String MAHAK_ORDER_DB = "DB_MahakOrder.db";
    public static final String RADARA_DB = "DB_Radara.db";

    public static final int DATABASE_VERSION = BuildConfig.VERSION_CODE;
    public static final int RADARA_DATABASE_VERSION = BuildConfig.VERSION_CODE;

    public static class UserSchema implements BaseColumns {

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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"User\" (" +
                "`DateSync` NUMERIC, " +
                "`SyncId` TEXT, " +
                "`PackageSerial` TEXT, " +
                "`LoginDate` NUMERIC, " +
                "`DatabaseId` TEXT, " +
                "`MasterId` NUMERIC, " +
                "`MahakId` TEXT, " +
                "`ModifyDate` NUMERIC, " +
                "`Id` INTEGER, " +
                "`Name` TEXT, " +
                "`UserName` TEXT, " +
                "`Password` TEXT, " +
                "`Type` NUMERIC, " +
                "`StoreCode` TEXT, " +
                "`UserId` TEXT, " +
                "`UserToken` NUMERIC, " +
                "PRIMARY KEY(`Id`) )";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class CustomerSchema implements BaseColumns {

        public static final String TABLE_NAME = "Customers";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_PersonId = "PersonId";
        public static final String COLUMN_PersonClientId = "PersonClientId";
        public static final String COLUMN_PersonGroupClientId = "PersonGroupClientId";
        public static final String COLUMN_PersonCode = "PersonCode";
        public static final String COLUMN_PersonGroupId = "PersonGroupId";
        public static final String COLUMN_PersonGroupCode = "PersonGroupCode";
        public static final String COLUMN_PersonType = "PersonType";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_FirstName = "FirstName";
        public static final String COLUMN_LastName = "LastName";
        public static final String COLUMN_Prefix = "Prefix";
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
        public static final String CREATE_TABLE =" CREATE TABLE IF NOT EXISTS \"Customers\" ( " +
                "`Id` INTEGER, " +
                "`PersonId` INTEGER, " +
                "`PersonClientId` NUMERIC, " +
                "`PersonGroupClientId` NUMERIC, " +
                "`PersonCode` NUMERIC, " +
                "`PersonGroupId` NUMERIC, " +
                "`PersonGroupCode` NUMERIC, " +
                "`PersonType` INTEGER, " +
                "`FirstName` TEXT, " +
                "`LastName` TEXT, " +
                "`Prefix` TEXT, " +
                "`Organization` TEXT, " +
                "`Gender` INTEGER, " +
                "`NationalCode` TEXT, " +
                "`Credit` TEXT, " +
                "`Balance` TEXT, " +
                "`Email` TEXT, " +
                "`UserId` NUMERIC, " +
                "`DatabaseId` TEXT, " +
                "`MahakId` TEXT, " +
                "`Publish` NUMERIC, " +
                "`ModifyDate` NUMERIC, " +
                "`Longitude` TEXT, " +
                "`Latitude` TEXT, " +
                "`Shift` TEXT, " +
                "`Name` TEXT, " +
                "`State` TEXT, " +
                "`City` TEXT, " +
                "`Address` TEXT, " +
                "`Zone` TEXT, " +
                "`Phone` TEXT, " +
                "`Fax` TEXT, " +
                "`Mobile` TEXT, " +
                "`DiscountPercent` NUMERIC, " +
                "`SellPriceLevel` TEXT, " +
                "`HasOrder` INTEGER, " +
                "`DataHash` text, " +
                "`CreateDate` text, " +
                "`UpdateDate` text, " +
                "`CreateSyncId` numeric, " +
                "`UpdateSyncId` numeric, " +
                "`RowVersion` numeric, " +
                "`UserName` TEXT, " +
                "`Password` TEXT, " +
                "`CityCode` INTEGER, " +
                "`Deleted` INTEGER, " +
                " PRIMARY KEY(`Id`) )";

    }

    public static class VisitorSchema implements BaseColumns {

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
        public static final String COLUMN_HasRadara = "HasRadara";
        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";

        public static final String CREATE_TABLE =" CREATE TABLE IF NOT EXISTS \"Visitors\" ( " +
                " `ID` INTEGER," +
                " `VisitorCode` NUMERIC," +
                " `ModifyDate` NUMERIC," +
                " `Name` TEXT," +
                " `UserName` TEXT," +
                " `StoreCode` INTEGER," +
                " `Tell` TEXT," +
                " `BankCode` INTEGER," +
                " `CashCode` INTEGER," +
                " `DatabaseId` TEXT," +
                " `MahakId` TEXT," +
                " `PriceAccess` TEXT," +
                " `CostLevelAccess` TEXT," +
                " `Sell_DefaultCostLevel` INTEGER," +
                " `SelectedCostLevels` TEXT," +
                " `TotalCredit` INTEGER," +
                " `ChequeCredit` INTEGER," +
                " `UserId` NUMERIC," +
                " `DataHash` text," +
                " `CreateDate` text, " +
                " `UpdateDate` text," +
                " `CreateSyncId` numeric," +
                " `UpdateSyncId` numeric," +
                " `RowVersion` numeric," +
                " `VisitorId` INTEGER," +
                " `VisitorClientId` INTEGER," +
                " `Password` TEXT, " +
                " `PersonCode` INTEGER, " +
                " `VisitorType` INTEGER," +
                " `DeviceId` TEXT, " +
                " `Active` INTEGER, " +
                " `HasRadara` INTEGER, " +
                " `Color` TEXT," +
                " PRIMARY KEY(`ID`) )";

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

        public static final String CREATE_TABLE =" CREATE TABLE IF NOT EXISTS \"VisitorProduct\" ( " +
                " `Id` INTEGER," +
                " `DatabaseId` TEXT," +
                " `VisitorProductId` INTEGER," +
                " `ProductDetailId` NUMERIC," +
                " `UserId` INTEGER, " +
                " `Count1` NUMERIC," +
                " `Count2` NUMERIC," +
                " `Price` INTEGER," +
                " `Serials` TEXT," +
                " `Deleted` INTEGER," +
                " `DataHash` text," +
                " `CreateDate` text," +
                " `UpdateDate` text," +
                " `CreateSyncId` numeric," +
                " `UpdateSyncId` numeric," +
                " `RowVersion` numeric," +
                "  PRIMARY KEY(`Id`) )";

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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"VisitorPeople\" (" +
                " `Id` INTEGER," +
                " `VisitorPersonId` INTEGER," +
                " `PersonId` INTEGER, `UserId` INTEGER," +
                " `Deleted` INTEGER, `DataHash` text," +
                " `CreateDate` text, `UpdateDate` text," +
                " `CreateSyncId` numeric," +
                " `UpdateSyncId` numeric," +
                " `RowVersion` numeric," +
                "  PRIMARY KEY(`Id`) )";

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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"Promotion\" ( \"Id\" INTEGER," +
                " \"DatabaseId\" TEXT," +
                " \"MahakId\" TEXT," +
                " \"UserId\" NUMERIC," +
                " \"ModifyDate\" NUMERIC," +
                " \"NamePromotion\" TEXT NOT NULL," +
                " \"PriorityPromotion\" INTEGER NOT NULL," +
                " \"DateStart\" TEXT NOT NULL," +
                " \"DateEnd\" TEXT NOT NULL," +
                " \"TimeStart\" TEXT NOT NULL," +
                " \"TimeEnd\" TEXT NOT NULL," +
                " \"LevelPromotion\" INTEGER NOT NULL," +
                " \"AccordingTo\" INTEGER NOT NULL," +
                " \"IsCalcLinear\" INTEGER NOT NULL," +
                " \"TypeTasvieh\" INTEGER," +
                " \"DeadlineTasvieh\" INTEGER," +
                " \"IsActive\" INTEGER NOT NULL," +
                " \"DesPromotion\" TEXT," +
                " \"IsAllCustomer\" INTEGER NOT NULL," +
                " \"IsAllVisitor\" INTEGER NOT NULL, " +
                "\"IsAllGood\" INTEGER NOT NULL," +
                " \"IsAllStore\" INTEGER NOT NULL, " +
                "\"AggregateWithOther\" INTEGER, " +
                "\"CreatedBy\" TEXT, " +
                "\"CreatedDate\" TEXT, " +
                "\"ModifiedBy\" TEXT, " +
                "\"PromotionId\" INTEGER, " +
                "\"PromotionClientId\" NUMERIC, " +
                "\"PromotionCode\" INTEGER, " +
                "\"Visitors\" TEXT, " +
                "\"Stores\" TEXT, " +
                "\"DataHash\" TEXT, " +
                "\"CreateDate\" TEXT, " +
                "\"UpdateDate\" TEXT, " +
                "\"CreateSyncId\" INTEGER, " +
                "\"UpdateSyncId\" INTEGER, " +
                "\"RowVersion\" NUMERIC, " +
                "\"Deleted\" INTEGER, " +
                "PRIMARY KEY(\"Id\") )";

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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"PromotionDetail\" ( " +
                "`Id` INTEGER, " +
                "`DatabaseId` TEXT, " +
                "`MahakId` TEXT, " +
                "`UserId` NUMERIC, " +
                "`ModifyDate` NUMERIC, " +
                "`PromotionDetailCode` INTEGER NOT NULL, " +
                "`PromotionCode` INTEGER NOT NULL, " +
                "`PromotionDetailId` INTEGER, " +
                "`PromotionDetailClientId` NUMERIC, " +
                "`PromotionId` INTEGER, " +
                "`HowToPromotion` INTEGER NOT NULL, " +
                "`IsCalcAdditive` INTEGER NOT NULL, " +
                "`ReducedEffectOnPrice` INTEGER NOT NULL, " +
                "`ToPayment` NUMERIC NOT NULL, " +
                "`MeghdarPromotion` INTEGER NOT NULL, " +
                "`StoreCode` INTEGER, " +
                "`CodeGood` INTEGER, " +
                "`tool` NUMERIC, " +
                "`arz` NUMERIC, " +
                "`tedad` INTEGER, " +
                "`meghdar2` NUMERIC, " +
                "`ghotr` NUMERIC, " +
                "`ToolidCode` INTEGER, " +
                "`meghdar` NUMERIC, " +
                "`SyncID` TEXT, " +
                "`CreatedBy` TEXT, " +
                "`CreatedDate` TEXT, " +
                "`ModifiedBy` TEXT, " +
                "`DataHash` TEXT, " +
                "`CreateDate` TEXT, " +
                "`UpdateDate` TEXT, " +
                "`CreateSyncId` INTEGER, " +
                "`UpdateSyncId` INTEGER, " +
                "`RowVersion` NUMERIC, " +
                "PRIMARY KEY(`Id`) )";
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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"PromotionEntity\" (" +
                " \"Id\" INTEGER," +
                " \"DatabaseId\" TEXT," +
                "\"MahakId\" TEXT," +
                " \"UserId\" NUMERIC," +
                " \"SyncID\" TEXT," +
                " \"ModifyDate\" NUMERIC," +
                " \"CodeEntity\" NUMERIC," +
                " \"CodePromotionEntity\" NUMERIC," +
                " \"EntityType\" NUMERIC," +
                " \"CodePromotion\" TEXT," +
                " \"CreatedBy\" TEXT," +
                " \"CreatedDate\" TEXT," +
                " \"ModifiedBy\" TEXT," +
                " \"DataHash\" TEXT," +
                " \"CreateDate\" TEXT," +
                " \"UpdateDate\" TEXT," +
                " \"CreateSyncId\" INTEGER," +
                " \"UpdateSyncId\" INTEGER," +
                " \"RowVersion\" NUMERIC, " +
                "\"PromotionEntityId\" INTEGER," +
                " \"PromotionEntityClientId\" NUMERIC," +
                " \"PromotionCode\" INTEGER," +
                " \"PromotionId\" INTEGER," +
                " PRIMARY KEY(\"Id\") )";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class CustomersGroupSchema implements BaseColumns {

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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"CustomersGroups\" ( " +
                "`DatabaseId` TEXT, " +
                "`MahakId` TEXT, " +
                "`PersonGroupCode` NUMERIC," +
                " `PersonGroupId` INTEGER," +
                " `PersonGroupClientId` INTEGER, " +
                "`ModifyDate` NUMERIC," +
                " `UserId` NUMERIC, " +
                "`Id` INTEGER, " +
                "`Name` TEXT, " +
                "`Color` NUMERIC, " +
                "`Icon` TEXT, " +
                "`SellPriceLevel` TEXT," +
                " `DataHash` text, " +
                "`CreateDate` text, " +
                "`UpdateDate` text, " +
                "`CreateSyncId` numeric, " +
                "`UpdateSyncId` numeric, " +
                "`RowVersion` numeric, " +
                "`DiscountPercent` NUMERIC," +
                " PRIMARY KEY(`Id`) )";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    public static class ProductSchema implements BaseColumns {

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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"Products\" ( " +
                "`Charge` TEXT, " +
                "`Tax` TEXT, " +
                "`UnitName2` TEXT, " +
                "`UnitName` TEXT, " +
                "`DatabaseId` TEXT, " +
                "`ProductCode` NUMERIC, " +
                "`MahakId` TEXT," +
                " `Asset2` NUMERIC, " +
                "`UserId` NUMERIC, " +
                "`Publish` NUMERIC, " +
                "`ModifyDate` NUMERIC, " +
                "`Min` NUMERIC, " +
                "`Code` TEXT, " +
                "`Image` TEXT, " +
                "`CustomerPrice` TEXT, " +
                "`Id` INTEGER, " +
                "`CategoryId` NUMERIC, " +
                "`Name` TEXT, " +
                "`Asset` NUMERIC, " +
                "`RealPrice` TEXT, " +
                "`Tags` TEXT, " +
                "`DiscountPercent` TEXT, " +
                "`Barcode` TEXT, " +
                "`Weight` NUMERIC, " +
                "`Deleted` INTEGER, " +
                "`DataHash` text, " +
                "`CreateDate` text, " +
                "`UpdateDate` text, " +
                "`CreateSyncId` numeric, " +
                "`UpdateSyncId` numeric, " +
                "`RowVersion` numeric, " +
                "`ProductId` INTEGER, " +
                "`ProductClientId` NUMERIC, " +
                "`ProductDetailId` NUMERIC, " +
                "`UnitRatio` NUMERIC," +
                " `Width` NUMERIC, " +
                "`Height` NUMERIC, " +
                "`Length` NUMERIC, " +
                "PRIMARY KEY(`Id`) )";
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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"ProductDetail\" ( " +
                "`Id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "`ProductDetailId` INTEGER, " +
                "`ProductDetailClientId` INTEGER, " +
                "`ProductDetailCode` INTEGER, " +
                "`ProductId` INTEGER, " +
                "`Properties` TEXT, " +
                "`Count1` TEXT," +
                " `Count2` TEXT, " +
                "`Barcode` TEXT, " +
                "`Price1` TEXT, " +
                "`Price2` TEXT, " +
                "`Price3` TEXT, " +
                "`Price4` TEXT, " +
                "`Price5` TEXT, " +
                "`Price6` TEXT," +
                " `Price7` TEXT, " +
                "`Price8` TEXT, " +
                "`Price9` TEXT, " +
                "`Price10` TEXT, " +
                "`DefaultSellPriceLevel` INTEGER, " +
                "`Discount` TEXT, " +
                "`Serials` TEXT, " +
                "`DataHash` TEXT, " +
                "`CreateDate` TEXT, " +
                "`UpdateDate` TEXT, " +
                "`CreateSyncId` INTEGER, " +
                "`UpdateSyncId` INTEGER, " +
                "`Deleted` INTEGER, " +
                "`UserId` NUMERIC, " +
                "`RowVersion` NUMERIC, " +
                "`Discount1` TEXT, " +
                "`Discount2` TEXT, " +
                "`Discount3` TEXT, " +
                "`Discount4` TEXT, " +
                "`DefaultDiscountLevel` INTEGER, " +
                "`DiscountType` INTEGER, " +
                "`CustomerPrice` TEXT )";
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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"PriceLevelNames\" ( " +
                "`PriceLevelCode` TEXT, " +
                "`PriceLevelName` TEXT, " +
                "`SyncId` TEXT, " +
                "`ModifyDate` NUMERIC, " +
                "`UserId` NUMERIC, " +
                "`Id` INTEGER," +
                "`CostLevelNameId` INTEGER," +
                "`CostLevelNameClientId` NUMERIC ," +
                "`DataHash` text, " +
                "`CreateDate` text, " +
                "`UpdateDate` text, " +
                "`CreateSyncId` numeric, " +
                "`UpdateSyncId` numeric, " +
                "`RowVersion` numeric," +
                " PRIMARY KEY(`Id`) )";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class ReceivedTransfersSchema implements BaseColumns {

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


        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"ReceivedTransfers\" ( " +
                "`DatabaseId` TEXT, " +
                "`MahakId` TEXT, " +
                "`TransferStoreCode` TEXT, " +
                "`ModifyDate` TEXT, " +
                "`SyncId` TEXT, " +
                "`TransferDate` TEXT, " +
                "`TransferStoreId` TEXT, " +
                "`IsAccepted` INTEGER, " +
                "`CreatedBy` TEXT, " +
                "`SenderVisitorId` TEXT, " +
                "`ModifiedBy` TEXT, " +
                "`Description` TEXT, " +
                "`ReceiverVisitorId` TEXT, " +
                "`DataHash` text, " +
                "`CreateDate` text, " +
                "`UpdateDate` text, " +
                "`CreateSyncId` numeric, " +
                "`UpdateSyncId` numeric, " +
                "`RowVersion` numeric, " +
                "`TransferStoreClientId` NUMERIC, " +
                "`SenderStoreCode` INTEGER, " +
                "`ReceiverStoreCode` INTEGER )";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class ReceivedTransferProductsSchema implements BaseColumns {

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


        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"ReceivedTransferProducts\" ( " +
                "`DatabaseId` TEXT, " +
                "`MahakId` TEXT, " +
                "`Id` TEXT, " +
                "`ModifyDate` TEXT, " +
                "`ProductId` TEXT, " +
                "`Name` TEXT, " +
                "`TransferId` TEXT, " +
                "`CreatedDate` TEXT, " +
                "`CreatedBy` TEXT, " +
                "`Description` TEXT, " +
                "`TransferStoreDetailId` INTEGER, " +
                "`TransferStoreDetailClientId` NUMERIC, " +
                "`TransferStoreId` INTEGER, " +
                "`ProductDetailId` INTEGER, " +
                "`Count1` TEXT, " +
                "`Count2` TEXT, " +
                "`DataHash` text, " +
                "`CreateDate` text, " +
                "`UpdateDate` text, " +
                "`CreateSyncId` numeric," +
                " `UpdateSyncId` numeric, " +
                "`RowVersion` numeric )";


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
        public static final String COLUMN_ProductGroupCode = "ProductCategoryCode";
        public static final String COLUMN_ProductGroupId = "ProductCategoryId";
        public static final String COLUMN_ProductGroupClientId = "ProductCategoryClientId";
        public static final String COLUMN_DataHash = "DataHash";
        public static final String COLUMN_CreateDate = "CreateDate";
        public static final String COLUMN_UpdateDate = "UpdateDate";
        public static final String COLUMN_CreateSyncId = "CreateSyncId";
        public static final String COLUMN_UpdateSyncId = "UpdateSyncId";
        public static final String COLUMN_RowVersion = "RowVersion";

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"ProductsGroups\" ( " +
                "`Id` INTEGER, " +
                "`DatabaseId` TEXT," +
                " `ProductCategoryCode` NUMERIC," +
                " `ProductCategoryId` INTEGER, " +
                "`ProductCategoryClientId` NUMERIC, " +
                "`MahakId` TEXT, `UserId` NUMERIC, " +
                "`ModifyDate` NUMERIC, " +
                "`Name` TEXT, " +
                "`ParentId` NUMERIC, " +
                "`Color` TEXT, " +
                "`Icon` TEXT, " +
                "`DataHash` text," +
                " `CreateDate` text, " +
                "`UpdateDate` text, " +
                "`CreateSyncId` numeric," +
                " `UpdateSyncId` numeric," +
                " `RowVersion` numeric, " +
                "PRIMARY KEY(`Id`) )";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    public static class OrderSchema implements BaseColumns {
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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"Orders\" ( " +
                "`Id` INTEGER," +
                " `DatabaseId` TEXT," +
                " `MahakId` TEXT," +
                " `Publish` NUMERIC," +
                " `ModifyDate` NUMERIC," +
                " `Code` TEXT," +
                " `Type` INTEGER," +
                " `Immediate` NUMERIC," +
                " `SettlementType` NUMERIC," +
                " `Description` TEXT," +
                " `Discount` TEXT, " +
                "`DeliveryDate` NUMERIC," +
                " `OrderDate` NUMERIC," +
                " `UserId` NUMERIC," +
                " `PersonId` NUMERIC," +
                " `PromotionCode` INTEGER," +
                " `GiftType` INTEGER," +
                " `IsFinal` NUMERIC, " +
                "`OrderCode` NUMERIC," +
                " `OrderId` INTEGER," +
                " `OrderClientId` NUMERIC," +
                " `ReceiptId` INTEGER," +
                " `ReturnReasonId` INTEGER," +
                " `ReceiptClientId` NUMERIC," +
                " `SendCost` NUMERIC," +
                " `OtherCost` NUMERIC, " +
                "`DataHash` TEXT, " +
                "`CreateDate` TEXT," +
                " `UpdateDate` TEXT, " +
                "`CreateSyncId` INTEGER, " +
                "`UpdateSyncId` INTEGER, " +
                "`RowVersion` NUMERIC, " +
                "`PersonClientId` NUMERIC, " +
                "`latitude` NUMERIC, " +
                "`longitude` NUMERIC, " +
                "PRIMARY KEY(`Id`) )";
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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"OrderDetail\" ( " +
                "`Id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                " `OrderDetailId` INTEGER," +
                " `OrderDetailClientId` INTEGER," +
                " `OrderId` INTEGER," +
                " `ProductDetailId` INTEGER," +
                " `ProductId` INTEGER," +
                " `Price` TEXT," +
                " `Count1` TEXT, " +
                "`Count2` TEXT," +
                " `SumCountBaJoz` TEXT," +
                " `Description` TEXT," +
                " `TaxPercent` TEXT," +
                " `ChargePercent` TEXT," +
                " `Discount` TEXT, " +
                "`DiscountType` INTEGER," +
                " `GiftCount1` NUMERIC," +
                " `GiftCount2` NUMERIC," +
                " `GiftType` INTEGER," +
                " `DataHash` TEXT," +
                " `CreateDate` TEXT, " +
                "`UpdateDate` TEXT," +
                " `CreateSyncId` INTEGER," +
                " `UpdateSyncId` INTEGER," +
                " `RowVersion` NUMERIC," +
                " `OrderClientId` NUMERIC," +
                " `CostLevel` INTEGER," +
                " `PromotionCode` INTEGER, " +
                "`UserId` NUMERIC )";
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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"OrderDetailProperty\" (" +
                " `Id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "`OrderDetailClientId` NUMERIC," +
                " `OrderId` INTEGER," +
                " `ProductDetailId` INTEGER," +
                " `ProductId` INTEGER," +
                " `OrderDetailPropertyId` INTEGER," +
                " `Count1` BLOB," +
                " `Count2` TEXT," +
                " `SumCountBaJoz` TEXT," +
                " `Max` NUMERIC," +
                " `ProductSpec` TEXT )";
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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"NonRegisters\" ( " +
                "`DatabaseId` TEXT," +
                " `NotRegisterCode` NUMERIC," +
                " `MahakId` TEXT," +
                " `Publish` NUMERIC," +
                " `ModifyDate` NUMERIC," +
                " `Description` TEXT," +
                " `NonRegisterDate` NUMERIC," +
                " `UserId` NUMERIC," +
                " `PersonId` NUMERIC," +
                " `PersonClientId` NUMERIC," +
                " `Id` INTEGER," +
                " `Code` TEXT," +
                " `CustomerName` TEXT," +
                " `ReasonCode` INTEGER," +
                " `NotRegisterId` INTEGER," +
                " `NotRegisterClientId` NUMERIC," +
                " `DataHash` text," +
                " `CreateDate` text," +
                " `UpdateDate` text," +
                " `CreateSyncId` numeric," +
                " `UpdateSyncId` numeric," +
                " `RowVersion` numeric," +
                " PRIMARY KEY(`Id`) )";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    public static class ReceiptSchema implements BaseColumns {
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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"Receipts\" ( " +
                "`Code` TEXT," +
                " `DatabaseId` TEXT," +
                " `ReceiptCode` NUMERIC," +
                " `MahakId` TEXT," +
                " `PersonClientId` NUMERIC," +
                " `PersonId` NUMERIC," +
                " `Publish` NUMERIC," +
                " `Date` NUMERIC," +
                " `ReceiptId` INTEGER," +
                " `ReceiptClientId` NUMERIC," +
                " `ModifyDate` NUMERIC," +
                " `Id` INTEGER," +
                " `UserId` NUMERIC," +
                " `CashAmount` TEXT," +
                " `Description` TEXT," +
                " `CashCode` TEXT," +
                " `DataHash` TEXT," +
                " `CreateDate` TEXT," +
                " `UpdateDate` TEXT," +
                " `CreateSyncId` NUMERIC," +
                " `UpdateSyncId` NUMERIC," +
                " `RowVersion` NUMERIC," +
                " PRIMARY KEY(`Id`) )";
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


        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"Payables\" ( `TransferType` INTEGER, " +
                "`TransferCode` INTEGER, " +
                "`TransferDate` INTEGER, " +
                "`Receiverid` INTEGER, " +
                "`Comment` TEXT, " +
                "`PayerId` INTEGER, " +
                "`VisitorId` INTEGER, " +
                "`DatabaseId` TEXT, " +
                "`MahakId` TEXT," +
                " `UserId` NUMERIC," +
                " `Publish` NUMERIC," +
                " `Id` INTEGER," +
                " `Price` INTEGER," +
                " `DataHash` text," +
                " `CreateDate` text," +
                " `UpdateDate` text," +
                " `CreateSyncId` numeric," +
                " `UpdateSyncId` numeric, " +
                "`RowVersion` numeric," +
                " `TransferAccountId` INTEGER," +
                " `TransferAccountClientId` NUMERIC," +
                " `TransferAccountCode` INTEGER," +
                " PRIMARY KEY(`Id`) )";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class ChequeSchema implements BaseColumns {
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


        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"Cheques\" ( " +
                "`BankId` NUMERIC, " +
                "`DatabaseId` TEXT, " +
                "`ChequeCode` NUMERIC, " +
                "`MahakId` TEXT, " +
                "`Publish` NUMERIC, " +
                "`Type` NUMERIC, " +
                "`Date` NUMERIC, " +
                "`ModifyDate` NUMERIC, " +
                "`Description` TEXT, " +
                "`Id` INTEGER, " +
                "`ReceiptId` NUMERIC, " +
                "`ChequeClientId` NUMERIC," +
                "`ReceiptClientId` NUMERIC, " +
                "`BankClientId` NUMERIC, " +
                "`Number` NUMERIC, " +
                "`Bank` TEXT, " +
                "`Branch` TEXT, " +
                "`Amount` TEXT, " +
                "`DataHash` text, " +
                "`CreateDate` text, " +
                "`UpdateDate` text, " +
                "`CreateSyncId` numeric, " +
                "`UpdateSyncId` numeric, " +
                "`RowVersion` numeric, " +
                "PRIMARY KEY(`Id`) )";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class CheckListSchema implements BaseColumns {
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

        static final String CREATE_TABLE =" CREATE TABLE IF NOT EXISTS \"CheckList\" ( " +
                "`DatabaseId` TEXT, " +
                "`CheckListCode` NUMERIC, " +
                "`MahakId` TEXT, " +
                "`Publish` NUMERIC, " +
                "`ModifyDate` NUMERIC, " +
                "`Description` TEXT, " +
                "`Id` INTEGER, " +
                "`UserId` NUMERIC, " +
                "`Status` NUMERIC, " +
                "`CustomerId` NUMERIC, " +
                "`Type` NUMERIC, " +
                "`DataHash` text, " +
                "`CreateDate` text, " +
                "`UpdateDate` text, " +
                "`CreateSyncId` numeric, " +
                "`UpdateSyncId` numeric, " +
                "`RowVersion` numeric, " +
                "`CheckListId` INTEGER, " +
                "`CheckListClientId` NUMERIC, " +
                "`VisitorId` INTEGER, " +
                "PRIMARY KEY(`Id`) )";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class TransactionsLogSchema implements BaseColumns {
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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"TransactionsLog\" ( " +
                "`Id` INTEGER," +
                " `PersonId` NUMERIC, " +
                "`TransactionId` NUMERIC," +
                " `Type` NUMERIC," +
                " `DebitAmount` TEXT," +
                " `CreditAmount` TEXT," +
                " `Balance` TEXT," +
                " `Status` TEXT," +
                " `Date` TEXT," +
                " `Description` TEXT," +
                " `ModifyDate` NUMERIC," +
                " `MahakId` TEXT, " +
                "`DatabaseId` TEXT, " +
                "`TransactionCode` NUMERIC, " +
                "`TransactionClientId` NUMERIC, " +
                "`DataHash` text, " +
                "`CreateDate` text, " +
                "`UpdateDate` text, " +
                "`CreateSyncId` numeric, " +
                "`UpdateSyncId` numeric, " +
                "`RowVersion` numeric, " +
                "`UserId` NUMERIC, " +
                "PRIMARY KEY(`Id`) )";
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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS Banks" +
                "( `Id` INTEGER," +
                " `MahakId` TEXT," +
                " `DatabaseId` TEXT," +
                " `Name` TEXT," +
                " `Description` TEXT," +
                " `UserId` NUMERIC," +
                " `ModifyDate` NUMERIC," +
                " `DataHash` text," +
                " `CreateDate` text," +
                " `UpdateDate` text," +
                " `CreateSyncId` numeric," +
                " `UpdateSyncId` numeric," +
                " `RowVersion` numeric," +
                " `BankId` INTEGER," +
                " `BankClientId` NUMERIC," +
                " `BankCode` INTEGER," +
                " PRIMARY KEY(`Id`) )";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class PropertyDescriptionSchema implements BaseColumns {

        public static final String TABLE_NAME = "PropertyDescription";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_DATABASE_ID = "DatabaseId";
        public static final String COLUMN_UserId = "UserId";

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

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS PropertyDescription" +
                "( `Id` INTEGER," +
                " `DatabaseId` TEXT," +
                " `UserId` NUMERIC," +
                " `PropertyDescriptionId` INTEGER," +
                " `PropertyDescriptionClientId` NUMERIC," +
                " `PropertyDescriptionCode` INTEGER," +
                " `Name` TEXT, `Title` TEXT," +
                " `EmptyTitle` TEXT," +
                " `Description` TEXT," +
                " `ExtraData` TEXT," +
                " `DisplayType` INTEGER," +
                " `DataType` INTEGER," +
                " `DataHash` text," +
                " `CreateDate` text," +
                " `UpdateDate` text," +
                " `CreateSyncId` numeric," +
                " `UpdateSyncId` numeric," +
                " `RowVersion` numeric," +
                " PRIMARY KEY(`Id`) )";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class ReasonsSchema implements BaseColumns {
        public static final String TABLE_NAME = "Reasons";

        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_MAHAK_ID = "MahakId";
        public static final String COLUMN_DATABASE_ID = "DatabaseId";
        public static final String COLUMN_UserId = "UserId";

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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"Reasons\" ( " +
                " `Id` INTEGER," +
                " `MahakId` TEXT," +
                " `UserId` NUMERIC," +
                " `ReturnReasonCode` NUMERIC," +
                " `ReturnReasonId` INTEGER," +
                " `ReturnReasonClientId` NUMERIC," +
                " `DatabaseId` TEXT, " +
                " `Name` TEXT," +
                " `Type` INTEGER, " +
                " `Description` TEXT," +
                " `ModifyDate` NUMERIC, " +
                " `DataHash` text," +
                " `CreateDate` text," +
                " `UpdateDate` text," +
                " `CreateSyncId` numeric," +
                " `UpdateSyncId` numeric," +
                " `RowVersion` numeric," +
                " PRIMARY KEY(`Id`) )";
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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"ExtraData\" ( " +
                " `Id` INTEGER," +
                " `DatabaseId` TEXT," +
                " `MahakId` TEXT," +
                " `ModifyDate` NUMERIC," +
                " `UserId` NUMERIC," +
                " `Data` TEXT," +
                " `DataHash` text," +
                " `CreateDate` text," +
                " `UpdateDate` text," +
                " `CreateSyncId` numeric," +
                " `UpdateSyncId` numeric," +
                " `RowVersion` numeric," +
                " `ExtraDataId` INTEGER," +
                " `ItemType` INTEGER," +
                " `ItemId` INTEGER," +
                " PRIMARY KEY(`Id`) )";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class ProductCategorySchema implements BaseColumns {

        public static final String TABLE_NAME = "ProductCategory";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_CategoryCode = "CategoryCode";
        public static final String COLUMN_ProductCode = "ProductCode";
        public static final String COLUMN_USER_ID = "UserId";

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"ProductCategory\" ( " +
                " `Id` INTEGER," +
                " `CategoryCode` NUMERIC," +
                " `UserId` NUMERIC ," +
                " `ProductCode` NUMERIC," +
                " PRIMARY KEY(`Id`) )";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class CategorySchema implements BaseColumns {

        public static final String TABLE_NAME = "Category";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_CategoryCode = "CategoryCode";
        public static final String COLUMN_ParentCode = "ParentCode";
        public static final String COLUMN_CategoryName = "CategoryName";
        public static final String COLUMN_USER_ID = "UserId";

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"Category\" ( " +
                " `Id` INTEGER," +
                " `CategoryCode` NUMERIC," +
                " `UserId` NUMERIC," +
                " `ParentCode` NUMERIC, " +
                " `CategoryName` text," +
                "  PRIMARY KEY(`Id`) )";


        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class CityZoneSchema implements BaseColumns {
        public static final String TABLE_NAME = "CityZone";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_ZoneCode = "ZoneCode";
        public static final String COLUMN_ParentCode = "ParentCode";
        public static final String COLUMN_ZoneName = "ZoneName";

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"CityZone\" (" +
                " `Id` INTEGER," +
                " `ZoneCode` NUMERIC," +
                " `ParentCode` NUMERIC," +
                " `ZoneName` text," +
                " PRIMARY KEY(`Id`) )";
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

        public static final String CREATE_TABLE =" CREATE TABLE IF NOT EXISTS Configs (Value TEXT," +
                " Title TEXT," +
                " Description TEXT," +
                " Type TEXT," +
                " Code TEXT," +
                " MasterId NUMERIC," +
                " UserId NUMERIC," +
                " ModifyDate NUMERIC," +
                " DatabaseId TEXT," +
                " MahakId TEXT," +
                " Id INTEGER PRIMARY KEY," +
                " DataHash text null," +
                " CreateDate text null," +
                " UpdateDate text null, " +
                " CreateSyncId numeric null," +
                " UpdateSyncId numeric null, " +
                " RowVersion numeric null)";
    }

    public static class NotificationSchema implements BaseColumns {
        public static final String TABLE_NAME = "Notifications";
        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_MESSAGE = "Message";
        public static final String COLUMN_FULLMESSAGE = "FullMessage";
        public static final String COLUMN_TYPE = "Type";
        public static final String COLUMN_DATA = "Data";
        public static final String COLUMN_ISREAD = "IsRead";
        public static final String COLUMN_DATE = "DateNotification";
        public static final String COLUMN_USER_ID = "UserId";

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"Notifications\" ( " +
                "`_ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                " `Title` TEXT, " +
                " `Message` TEXT, " +
                " `FullMessage` TEXT, " +
                " `Type` TEXT, " +
                " `Data` TEXT, " +
                " `IsRead` INTEGER," +
                " `DateNotification` INTEGER," +
                " `UserId` INTEGER )";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class PicturesProductSchema implements BaseColumns {
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


        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"PicturesProduct\" ( " +
                " `_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                " `pictureId` INTEGER," +
                " `productId` INTEGER, `ItemId` INTEGER, " +
                " `ItemType` INTEGER, `PictureCode` INTEGER," +
                " `PictureClientId` NUMERIC," +
                " `fileName` TEXT," +
                " `url` TEXT, " +
                " `title` TEXT," +
                " `fileSize` INTEGER, " +
                " `lastUpdate` INTEGER," +
                " `dataBaseId` TEXT," +
                " `mahakId` TEXT, " +
                " `UserId` INTEGER," +
                " `DataHash` text, " +
                " `Width` INTEGER, " +
                " `Height` INTEGER, " +
                " `DisplayOrder` INTEGER, " +
                " `PictureHash` TEXT, " +
                " `Format` TEXT, " +
                " `CreateDate` text, " +
                " `UpdateDate` text, " +
                " `CreateSyncId` numeric, " +
                " `UpdateSyncId` numeric, " +
                " `RowVersion` numeric )";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class VisitorLocationSchema implements BaseColumns {

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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"VisitorLocation\" ( " +
                "\"_id\" INTEGER NOT NULL," +
                " \"uniqueID\" TEXT, " +
                "\"Date\" NUMERIC, " +
                "\"CreateDate\" INTEGER, " +
                "\"latitude\" TEXT, " +
                "\"longitude\" TEXT, " +
                "\"VisitorId\" INTEGER, " +
                "\"VisitorLocationId\" INTEGER, " +
                "\"RowVersion\" NUMERIC, " +
                "\"skipCount\" INTEGER, " +
                "\"CreateSyncId\" INTEGER, UNIQUE(\"VisitorId\",\"latitude\",\"longitude\")," +
                " PRIMARY KEY(\"_id\" AUTOINCREMENT) )";

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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"Setting\" ( " +
                "`Id` INTEGER, " +
                "`SettingId` NUMERIC, " +
                "`SettingCode` NUMERIC, " +
                "`UserId` NUMERIC, " +
                "`Value` TEXT, " +
                "`Deleted` TEXT, " +
                "`DataHash` text, " +
                "`CreateDate` text, " +
                "`UpdateDate` text, " +
                "`CreateSyncId` numeric, " +
                "`UpdateSyncId` numeric, " +
                "`RowVersion` numeric, " +
                "PRIMARY KEY(`Id`) )";
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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"Zone\" ( " +
                "\"zoneId\" INTEGER," +
                " \"visitorId\" INTEGER," +
                " \"title\" TEXT," +
                " \"createdBy\" TEXT," +
                " \"created\" TEXT," +
                " \"lastModifiedBy\" TEXT," +
                " \"lastModified\" TEXT," +
                " PRIMARY KEY(zoneId , createdBy) )";
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

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"ZoneLocation\" ( " +
                "\"id\" INTEGER," +
                " \"zoneId\" INTEGER," +
                " \"visitorId\" INTEGER," +
                " \"latitude\" TEXT," +
                " \"longitude\" TEXT," +
                " \"createdBy\" TEXT," +
                " \"created\" TEXT," +
                " \"lastModifiedBy\" TEXT," +
                " \"lastModified\" TEXT," +
                " PRIMARY KEY(id , createdBy) )";
    }

    public static class PhotoGallerySchema implements BaseColumns {

        public static final String TABLE_NAME = "PhotoGallery";
        public static final String COLUMN_id = "id";
        public static final String COLUMN_userId = "userId";
        public static final String COLUMN_photoGalleryId = "photoGalleryId";
        public static final String COLUMN_pictureId = "pictureId";
        public static final String COLUMN_entityType = "entityType";
        public static final String COLUMN_itemCode = "itemCode";
        public static final String COLUMN_itemType = "itemType";
        public static final String COLUMN_deleted = "deleted";
        public static final String COLUMN_createDate = "createDate";
        public static final String COLUMN_updateDate = "updateDate";
        public static final String COLUMN_createSyncId = "createSyncId";
        public static final String COLUMN_updateSyncId = "updateSyncId";
        public static final String COLUMN_rowVersion = "rowVersion";
        public static final String COLUMN_dataHash = "dataHash";

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"PhotoGallery\" ( " +
                " \"id\" INTEGER," +
                " \"photoGalleryId\" INTEGER," +
                " \"pictureId\" INTEGER," +
                " \"entityType\" INTEGER," +
                " \"itemCode\" INTEGER," +
                " \"itemType\" INTEGER," +
                " \"deleted\" TEXT," +
                " \"dataHash\" TEXT," +
                " \"createDate\" TEXT," +
                " \"updateDate\" TEXT," +
                " \"createSyncId\" INTEGER," +
                " \"updateSyncId\" INTEGER," +
                " \"userId\" NUMERIC," +
                " \"rowVersion\" INTEGER," +
                " PRIMARY KEY(id))";

    }

    public static class RegionSchema implements BaseColumns {

        public static final String TABLE_NAME = "Region";
        public static final String COLUMN_id = "id";
        public static final String COLUMN_userId = "userId";
        public static final String COLUMN_CityID = "CityID";
        public static final String COLUMN_CityName = "CityName";
        public static final String COLUMN_ProvinceID = "ProvinceID";
        public static final String COLUMN_ProvinceName = "ProvinceName";
        public static final String COLUMN_MapCode = "MapCode";
        public static final String COLUMN_RowVersion = "RowVersion";

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"Region\" ( " +
                " \"id\" INTEGER," +
                " \"userId\" NUMERIC," +
                " \"CityID\" number," +
                " \"CityName\" TEXT," +
                " \"ProvinceID\" INTEGER," +
                " \"ProvinceName\" TEXT," +
                " \"RowVersion\" INTEGER," +
                " \"MapCode\" INTEGER," +
                " PRIMARY KEY(id))";
    }

    public static class StopLogSchema implements BaseColumns {

        public static final String TABLE_NAME = "StopLog";
        public static final String COLUMN_stopLocationClientId = "stopLocationClientId";
        public static final String COLUMN_lat = "lat";
        public static final String COLUMN_lng = "lng";
        public static final String COLUMN_sent = "sent";
        public static final String COLUMN_entryDate = "entryDate";
        public static final String COLUMN_duration = "duration";
        public static final String COLUMN_endDate = "endDate";
        public static final String COLUMN_UserId = "UserId";

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"StopLog\" ( " +
                " `lat` TEXT," +
                " `lng` TEXT," +
                " `sent` INTEGER," +
                " `entryDate` TEXT," +
                " `duration` NUMERIC," +
                " `endDate` TEXT," +
                " `_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                " `stopLocationClientId` NUMERIC," +
                " `UserId` NUMERIC )";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
    public static class ManageLogSchema implements BaseColumns {

        public static final String TABLE_NAME = "ManageLog";
        public static final String COLUMN_Log_type = "Log_type";
        public static final String COLUMN_Log_value = "Log_value";
        public static final String COLUMN_sent = "sent";
        public static final String COLUMN_Date_time = "Date_time";
        public static final String COLUMN_UserId = "UserId";

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS \"ManageLog\" ( " +
                " `_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                " `Log_type` INTEGER," +
                " `Log_value` INTEGER," +
                " `sent` INTEGER," +
                " `Date_time` TEXT," +
                " `UserId` NUMERIC )";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
