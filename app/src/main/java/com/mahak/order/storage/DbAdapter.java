package com.mahak.order.storage;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;


import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mahak.order.BaseActivity;
import com.mahak.order.BuildConfig;
import com.mahak.order.common.Bank;
import com.mahak.order.common.Category;
import com.mahak.order.common.CheckList;
import com.mahak.order.common.Cheque;
import com.mahak.order.common.CityZone_Extra_Data;
import com.mahak.order.common.Customer;
import com.mahak.order.common.CustomerGroup;
import com.mahak.order.common.ExtraData;
import com.mahak.order.common.PersonCategory;
import com.mahak.order.common.PhotoGallery;
import com.mahak.order.common.Region;
import com.mahak.order.common.GroupedTax;
import com.mahak.order.common.NonRegister;
import com.mahak.order.common.Order;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.OrderDetailProperty;
import com.mahak.order.common.PayableTransfer;
import com.mahak.order.common.Person_Extra_Data;
import com.mahak.order.common.PicturesProduct;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductCategory;
import com.mahak.order.common.ProductDetail;
import com.mahak.order.common.ProductGroup;
import com.mahak.order.common.ProductPriceLevelName;
import com.mahak.order.common.Product_Extra_Data;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.Promotion;
import com.mahak.order.common.PromotionDetail;
import com.mahak.order.common.PromotionDetailOtherFields;
import com.mahak.order.common.PromotionEntity;
import com.mahak.order.common.PromotionEntityOtherFields;
import com.mahak.order.common.PromotionOtherFields;
import com.mahak.order.common.PropertyDescription;
import com.mahak.order.common.Reasons;
import com.mahak.order.common.Receipt;
import com.mahak.order.common.ReceivedTransferProducts;
import com.mahak.order.common.ReceivedTransfers;
import com.mahak.order.common.ReportMonth;
import com.mahak.order.common.ReportProductDetail;
import com.mahak.order.common.ReportUserDetail;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.Setting;
import com.mahak.order.common.TaxInSell_Extra_Data;
import com.mahak.order.common.TransactionsLog;
import com.mahak.order.common.User;
import com.mahak.order.common.Visitor;
import com.mahak.order.common.VisitorPeople;
import com.mahak.order.common.VisitorProduct;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.annotations.NonNull;

import static com.mahak.order.BaseActivity.MODE_MeghdarJoz;
import static com.mahak.order.BaseActivity.baseUrlImage;
import static com.mahak.order.BaseActivity.getPrefUserId;
import static com.mahak.order.BaseActivity.getPrefUserMasterId;
import static com.mahak.order.BaseActivity.mContext;
import static com.mahak.order.common.ServiceTools.RegulartoDouble;
import static com.mahak.order.common.ServiceTools.getLikeString;

public class DbAdapter {

    private static String DB_PATH;
    private final Context mCtx;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    public DbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter open() {
        this.mDbHelper = new DatabaseHelper(mCtx,DbSchema.MAHAK_ORDER_DB);
        this.mDb = mDbHelper.openDataBase(DbSchema.MAHAK_ORDER_DB);
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    //QUERIES ADD
    public void AddOrUpdateCustomer(Customer customer) {
        if (customer.getFirstName() == null)
            customer.setFirstName("");
        if (customer.getLastName() == null)
            customer.setLastName("");
        if (customer.getPrefix() == null)
            customer.setPrefix("");
        customer.setName(customer.getPrefix() + " " + customer.getFirstName() + " " + customer.getLastName());
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_PersonGroupId, customer.getPersonGroupId());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_PersonGroupCode, customer.getPersonGroupCode());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_NAME, customer.getName());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_FirstName, customer.getFirstName());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_LastName, customer.getLastName());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_Prefix, customer.getPrefix());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_ORGANIZATION, customer.getOrganization());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_CREDIT, customer.getCredit());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_BALANCE, customer.getBalance());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_STATE, customer.getState());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_CITY, customer.getCity());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_ADDRESS, customer.getAddress());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_ZONE, customer.getZone());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_PHONE, customer.getTell());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_MOBILE, customer.getMobile());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_LATITUDE, customer.getLatitude());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_LONGITUDE, customer.getLongitude());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_SHIFT, customer.getShift());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_MODIFYDATE, customer.getModifyDate());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_PUBLISH, customer.getPublish());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_MAHAK_ID, customer.getMahakId());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_PersonCode, customer.getPersonCode());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_DATABASE_ID, customer.getDatabaseId());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_USER_ID, customer.getUserId());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_DiscountPercent, customer.getDiscountPercent());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_SellPriceLevel, customer.getSellPriceLevel());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_DataHash, customer.getDataHash());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_CreateDate, customer.getCreateDate());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_UpdateDate, customer.getUpdateDate());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_CreateSyncId, customer.getCreateSyncId());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_UpdateSyncId, customer.getUpdateSyncId());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_RowVersion, customer.getRowVersion());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_PersonId, customer.getPersonId());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_HasOrder, customer.getOrderCount());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_PersonClientId, customer.getPersonClientId());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_PersonType, customer.getPersonType());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_Gender, customer.getGender());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_NationalCode, customer.getNationalCode());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_Email, customer.getEmail());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_UserName, customer.getUserName());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_Password, customer.getPassword());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_CityCode, customer.getCityCode());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_Fax, customer.getFax());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_Deleted, customer.getDeleted());

        boolean result = (mDb.update(DbSchema.CustomerSchema.TABLE_NAME, initialvalue, DbSchema.CustomerSchema.COLUMN_PersonClientId + "=?", new String[]{String.valueOf(customer.getPersonClientId())})) > 0;
        if(!result) {
            mDb.insert(DbSchema.CustomerSchema.TABLE_NAME, null, initialvalue);
        }
    }

    //__________________________QUERIES__________________________________________________

    public long addPicturesProductOffline(PicturesProduct picturesProduct) {
        ContentValues initialvalue = getOfflinePicturesProduct(picturesProduct);
        return mDb.insert(DbSchema.PicturesProductSchema.TABLE_NAME, null, initialvalue);
    }

    public long addPicturesProductFromWeb(PicturesProduct picturesProduct) {
        ContentValues initialvalue = getOnlinePictureProduct(picturesProduct);
        return mDb.insert(DbSchema.PicturesProductSchema.TABLE_NAME, null, initialvalue);
    }

    public long AddPropertyDescription(PropertyDescription propertyDescription) {
        boolean result;

        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_PropertyDescriptionId, propertyDescription.getPropertyDescriptionId());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_DATABASE_ID, propertyDescription.getDatabaseId());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_UserId, getPrefUserId());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_PropertyDescriptionClientId, propertyDescription.getPropertyDescriptionClientId());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_PropertyDescriptionCode, propertyDescription.getPropertyDescriptionCode());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_NAME, propertyDescription.getName());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_Title, propertyDescription.getTitle());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_EmptyTitle, propertyDescription.getEmptyTitle());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_DataType, propertyDescription.getDescription());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_DisplayType, propertyDescription.getDataType());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_ExtraData, propertyDescription.getExtraData());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_Description, propertyDescription.getDescription());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_DataHash, propertyDescription.getDataHash());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_CreateDate, propertyDescription.getCreateDate());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_UpdateDate, propertyDescription.getUpdateDate());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_CreateSyncId, propertyDescription.getCreateSyncId());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_UpdateSyncId, propertyDescription.getUpdateSyncId());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_RowVersion, propertyDescription.getRowVersion());

        return mDb.insert(DbSchema.PropertyDescriptionSchema.TABLE_NAME, null, initialvalue);

    }

    public long AddProductGroup(ProductGroup productGroup) {
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_USER_ID, getPrefUserId());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_NAME, productGroup.getName());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_PARENTID, productGroup.getParentId());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_ICON, productGroup.getIcon());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_COLOR, productGroup.getColor());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_MODIFYDATE, productGroup.getModifyDate());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_MAHAK_ID, productGroup.getMahakId());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_ProductGroupCode, productGroup.getProductGroupCode());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_DATABASE_ID, productGroup.getDatabaseId());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_ProductGroupClientId, productGroup.getProductCategoryClientId());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_ProductGroupId, productGroup.getProductCategoryId());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_DataHash, productGroup.getDataHash());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_CreateDate, productGroup.getCreateDate());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_UpdateDate, productGroup.getUpdateDate());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_CreateSyncId, productGroup.getCreateSyncId());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_UpdateSyncId, productGroup.getUpdateSyncId());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_RowVersion, productGroup.getRowVersion());
        return mDb.insert(DbSchema.ProductGroupSchema.TABLE_NAME, null, initialvalue);
    }

    public long AddCustomerGroup(CustomerGroup customergroup) {
        ContentValues initialvalue = new ContentValues();

        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_NAME, customergroup.getName());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_DATABASE_ID, customergroup.getDatabaseId());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_MAHAK_ID, customergroup.getMahakId());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_PersonGroupCode, customergroup.getPersonGroupCode());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_PersonGroupId, customergroup.getPersonGroupId());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_PersonGroupClientId, customergroup.getPersonGroupClientId());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_MODIFYDATE, customergroup.getModifyDate());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_COLOR, customergroup.getColor());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_ICON, customergroup.getIcon());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_USER_ID, customergroup.getUserId());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_SellPriceLevel, customergroup.getSellPriceLevel());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_DiscountPercent, customergroup.getDiscountPercent());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_DataHash, customergroup.getDataHash());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_CreateDate, customergroup.getCreateDate());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_UpdateDate, customergroup.getUpdateDate());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_CreateSyncId, customergroup.getCreateSyncId());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_UpdateSyncId, customergroup.getUpdateSyncId());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_RowVersion, customergroup.getRowVersion());

        return mDb.insert(DbSchema.CustomersGroupSchema.TABLE_NAME, null, initialvalue);
    }

    public void AddSetting(Setting setting) {
        boolean result;

        ContentValues initialvalue = new ContentValues();

        initialvalue.put(DbSchema.SettingSchema.COLUMN_SettingId, setting.getSettingId());
        initialvalue.put(DbSchema.SettingSchema.COLUMN_SettingCode, setting.getSettingCode());
        initialvalue.put(DbSchema.SettingSchema.COLUMN_USER_ID, setting.getVisitorId());
        initialvalue.put(DbSchema.SettingSchema.COLUMN_Value, setting.getValue());
        initialvalue.put(DbSchema.SettingSchema.COLUMN_Deleted, setting.getDeleted());
        initialvalue.put(DbSchema.SettingSchema.COLUMN_DataHash, setting.getDataHash());
        initialvalue.put(DbSchema.SettingSchema.COLUMN_CreateDate, setting.getCreateDate());
        initialvalue.put(DbSchema.SettingSchema.COLUMN_UpdateDate, setting.getUpdateDate());
        initialvalue.put(DbSchema.SettingSchema.COLUMN_CreateSyncId, setting.getCreateSyncId());
        initialvalue.put(DbSchema.SettingSchema.COLUMN_UpdateSyncId, setting.getUpdateSyncId());
        initialvalue.put(DbSchema.SettingSchema.COLUMN_RowVersion, setting.getRowVersion());

        result = (mDb.update(DbSchema.SettingSchema.TABLE_NAME, initialvalue, DbSchema.SettingSchema.COLUMN_SettingId + "=? and " + DbSchema.SettingSchema.COLUMN_USER_ID + "=? " , new String[]{String.valueOf(setting.getSettingId()) , String.valueOf(setting.getVisitorId())})) > 0;
        if(!result)
            mDb.insert(DbSchema.SettingSchema.TABLE_NAME, null, initialvalue);
    }

    public long AddOrder(Order order) {
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.OrderSchema.COLUMN_USER_ID, order.getVisitorId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_PersonId, order.getPersonId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_LATITUDE, order.getLatitude());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_LONGITUDE, order.getLongitude());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_ReturnReasonId, order.getReturnReasonId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_PersonClientId, order.getPersonClientId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_MAHAK_ID, order.getMahakId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_DATABASE_ID, order.getDatabaseId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_OrderCode, order.getOrderCode());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_DELIVERYDATE, order.getDeliveryDate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_ORDERDATE, order.getOrderDate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_DISCOUNT, order.getDiscount());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_DESCRIPTION, order.getDescription());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_CODE, order.getCode());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_SETTLEMEMNTTYPE, order.getSettlementType());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_TYPE, order.getOrderType());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_IMMEDIATE, order.getImmediate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_ReceiptClientId, order.getReceiptClientId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_MODIFYDATE, order.getModifyDate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_PUBLISH, order.getPublish());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_PROMOTION_CODE, order.getPromotionCode());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_GIFT_TYPE, order.getGiftType());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_OrderId, order.getOrderId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_OrderClientId, order.getOrderClientId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_ReceiptId, order.getReceiptId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_SendCost, order.getSendCost());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_OtherCost, order.getOtherCost());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_DataHash, order.getDataHash());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_CreateDate, order.getCreateDate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_UpdateDate, order.getUpdateDate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_CreateSyncId, order.getCreateSyncId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_UpdateSyncId, order.getUpdateSyncId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_RowVersion, order.getRowVersion());

        return mDb.insert(DbSchema.OrderSchema.TABLE_NAME, null, initialvalue);
    }

    public long AddReceipt(Receipt receipt) {
        ContentValues initialvalue = new ContentValues();
       // initialvalue.put(DbSchema.Receiptschema.COLUMN_ID, receipt.getVisitorId() + ServiceTools.createID());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_USER_ID, receipt.getVisitorId());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_MAHAK_ID, receipt.getMahakId());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_DATABASE_ID, receipt.getDatabaseId());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_ReceiptCode, receipt.getReceiptCode());
        initialvalue.put(DbSchema.ReceiptSchema.PERSON_ID, receipt.getPersonId());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_PersonClientId, receipt.getPersonClientId());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_CASHAMOUNT, receipt.getCashAmount());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_DESCRIPTION, receipt.getDescription());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_MODIFYDATE, receipt.getModifyDate());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_DATE, receipt.getDate());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_PUBLISH, receipt.getPublish());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_CODE, receipt.getTrackingCode());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_ReceiptClientId, receipt.getReceiptClientId());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_ReceiptId, receipt.getReceiptId());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_CashCode, receipt.getCashCode());

        return mDb.insert(DbSchema.ReceiptSchema.TABLE_NAME, null, initialvalue);
    }

    public long AddNonRegister(NonRegister nonRegister) {
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_USER_ID, nonRegister.getVisitorId());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_MAHAK_ID, nonRegister.getMahakId());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_DATABASE_ID, nonRegister.getDatabaseId());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_NotRegisterCode, nonRegister.getNotRegisterCode());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_PersonId, nonRegister.getPersonId());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_PersonClientId, nonRegister.getPersonClientId());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_DESCRIPTION, nonRegister.getDescription());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_MODIFYDATE, nonRegister.getModifyDate());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_NonRegister_DATE, nonRegister.getNotRegisterDate());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_PUBLISH, nonRegister.getPublish());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_CODE, nonRegister.getCode());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_ReasonCode, nonRegister.getReasonCode());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_CustomerName, nonRegister.getCustomerName());

        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_NotRegisterId, nonRegister.getNotRegisterId());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_NotRegisterClientId, nonRegister.getNotRegisterClientId());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_DataHash, nonRegister.getDataHash());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_CreateDate, nonRegister.getCreateDate());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_UpdateDate, nonRegister.getUpdateDate());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_CreateSyncId, nonRegister.getCreateSyncId());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_UpdateSyncId, nonRegister.getUpdateSyncId());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_RowVersion, nonRegister.getRowVersion());


        return mDb.insert(DbSchema.NonRegisterSchema.TABLE_NAME, null, initialvalue);
    }

    public long AddPayable(PayableTransfer payableTransfer) {

        ContentValues initialvalue = new ContentValues();

        initialvalue.put(DbSchema.PayableSchema.COLUMN_USER_ID, payableTransfer.getUserId());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_MahakId, payableTransfer.getMahakId());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_DatabaseId, payableTransfer.getDataBaseId());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_TransferCode, payableTransfer.getTransferCode());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_TransferDate, payableTransfer.getTransferDate());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_TransferType, payableTransfer.getTransferType());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_Receiverid, payableTransfer.getReceiverId());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_Comment, payableTransfer.getDescription());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_PayerId, payableTransfer.getPayerId());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_PUBLISH, payableTransfer.getPublish());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_VisitorId, payableTransfer.getVisitorId());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_Price, payableTransfer.getPrice());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_TransferAccountId, payableTransfer.getTransferAccountId());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_TransferAccountClientId, payableTransfer.getTransferAccountClientId());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_TransferAccountCode, payableTransfer.getTransferAccountCode());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_DataHash, payableTransfer.getDataHash());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_CreateDate, payableTransfer.getCreateDate());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_UpdateDate, payableTransfer.getUpdateDate());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_CreateSyncId, payableTransfer.getCreateSyncId());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_UpdateSyncId, payableTransfer.getUpdateSyncId());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_RowVersion, payableTransfer.getRowVersion());


        return mDb.insert(DbSchema.PayableSchema.TABLE_NAME, null, initialvalue);
    }

    public long AddOrderDetail(OrderDetail orderDetail) {
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_USER_ID, BaseActivity.getPrefUserId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_OrderId, orderDetail.getOrderId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_ProductDetailId, orderDetail.getProductDetailId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_ProductId, orderDetail.getProductId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Price, orderDetail.getPrice());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Count1, orderDetail.getCount1());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz, orderDetail.getSumCountBaJoz());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Count2, orderDetail.getCount2());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_OrderDetailClientId, orderDetail.getOrderDetailClientId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_OrderClientId, orderDetail.getOrderClientId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_GiftType, orderDetail.getGiftType());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Description, orderDetail.getDescription());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_TaxPercent, orderDetail.getTaxPercent());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_ChargePercent, orderDetail.getChargePercent());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Discount, orderDetail.getDiscount());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_DiscountType, orderDetail.getDiscountType());
        //initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Fixed_Off, orderDetail.getFixedOff());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_CostLevel, orderDetail.getCostLevel());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_PROMOTION_CODE, orderDetail.getPromotionCode());
        //initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_GIFT_TYPE, orderDetail.getGiftType());

        return mDb.insert(DbSchema.OrderDetailSchema.TABLE_NAME, null, initialvalue);
    }

    public long AddOrderDetailProperty(OrderDetailProperty orderDetailProperty) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbSchema.OrderDetailPropertySchema.COLUMN_OrderId, orderDetailProperty.getOrderId());
        contentValues.put(DbSchema.OrderDetailPropertySchema.COLUMN_OrderDetailClientId, orderDetailProperty.getOrderDetailClientId());
        contentValues.put(DbSchema.OrderDetailPropertySchema.COLUMN_ProductDetailId, orderDetailProperty.getProductDetailId());
        contentValues.put(DbSchema.OrderDetailPropertySchema.COLUMN_ProductId, orderDetailProperty.getProductId());
        contentValues.put(DbSchema.OrderDetailPropertySchema.COLUMN_OrderDetailPropertyId, orderDetailProperty.getOrderDetailPropertyId());
        contentValues.put(DbSchema.OrderDetailPropertySchema.COLUMN_Count1, orderDetailProperty.getCount1());
        contentValues.put(DbSchema.OrderDetailPropertySchema.COLUMN_Count2, orderDetailProperty.getCount2());
        contentValues.put(DbSchema.OrderDetailPropertySchema.COLUMN_ProductSpec, orderDetailProperty.getProductSpec());
        contentValues.put(DbSchema.OrderDetailPropertySchema.COLUMN_SumCountBaJoz, orderDetailProperty.getSumCountBaJoz());

        return mDb.insert(DbSchema.OrderDetailPropertySchema.TABLE_NAME, null, contentValues);
    }

    public long AddReceivedTransfers(ReceivedTransfers receivedTransfers) {
        ContentValues initialvalue = new ContentValues();

        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_TransferStoreCode, receivedTransfers.getTransferStoreCode());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_TransferDate, receivedTransfers.getTransferDate());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_DatabaseId, receivedTransfers.getDatabaseId());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_MahakId, receivedTransfers.getMahakId());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_Description, receivedTransfers.getDescription());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_CreatedBy, receivedTransfers.getCreatedBy());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_IsAccepted, receivedTransfers.getIsAccepted());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_ModifiedBy, receivedTransfers.getModifiedBy());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_SenderVisitorId, receivedTransfers.getSenderVisitorId());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_ReceiverVisitorId, receivedTransfers.getReceiverVisitorId());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_SyncId, receivedTransfers.getSyncId());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_ModifyDate, receivedTransfers.getModifyDate());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_TransferStoreId, receivedTransfers.getTransferStoreId());

        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_DataHash, receivedTransfers.getDataHash());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_CreateDate, receivedTransfers.getCreateDate());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_UpdateDate, receivedTransfers.getUpdateDate());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_CreateSyncId, receivedTransfers.getCreateSyncId());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_UpdateSyncId, receivedTransfers.getUpdateSyncId());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_RowVersion, receivedTransfers.getRowVersion());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_TransferStoreClientId, receivedTransfers.getTransferStoreClientId());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_SenderStoreCode, receivedTransfers.getSenderStoreCode());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_ReceiverStoreCode, receivedTransfers.getReceiverStoreCode());

        return mDb.insert(DbSchema.ReceivedTransfersSchema.TABLE_NAME, null, initialvalue);
    }

    public void AddCheque(Cheque cheque) {
        long row = 0;
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_RECEIPTID, cheque.getReceiptId());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_MAHAK_ID, cheque.getMahakId());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_DATABASE_ID, cheque.getDatabaseId());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_ChequeCode, cheque.getChequeCode());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_BANK_ID, cheque.getBankId());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_BANK, cheque.getBankName());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_NUMBER, cheque.getNumber());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_BRANCH, cheque.getBranch());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_DESCRIPTION, cheque.getDescription());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_MODIFYDATE, cheque.getModifyDate());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_DATE, cheque.getDate());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_TYPE, cheque.getType());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_PUBLISH, cheque.getPublish());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_AMOUNT, cheque.getAmount());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_ChequeClientId, cheque.getChequeClientId());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_ReceiptClientId, cheque.getReceiptClientId());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_BankClientId, cheque.getBankClientId());

        row = mDb.insert(DbSchema.ChequeSchema.TABLE_NAME, null, initialvalue);
        row = row + 1;
    }

    public long AddTransactionsLog(TransactionsLog transactionlog) {
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_PersonId, transactionlog.getPersonId());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_USER_ID, BaseActivity.getPrefUserId());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_TYPE, transactionlog.getType());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_DEBITAMOUNT, transactionlog.getDebtAmount());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_CREDITAMOUNT, transactionlog.getCreditAmount());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_Balance, transactionlog.getBalance());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_STATUS, transactionlog.getStatus());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_DESCRIPTION, transactionlog.getDescription());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_MODIFYDATE, transactionlog.getModifyDate());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_TransactionCode, transactionlog.getTransactionCode());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_MAHAK_ID, transactionlog.getMahakId());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_DATABASE_ID, transactionlog.getDatabaseId());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_TRANSACTIONID, transactionlog.getTransactionId());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_DATE, transactionlog.getFormattedTransactionDate());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_TransactionClientId, transactionlog.getTransactionClientId());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_DataHash, transactionlog.getDataHash());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_CreateDate, transactionlog.getCreateDate());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_UpdateDate, transactionlog.getUpdateDate());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_CreateSyncId, transactionlog.getCreateSyncId());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_UpdateSyncId, transactionlog.getUpdateSyncId());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_RowVersion, transactionlog.getRowVersion());

        return mDb.insert(DbSchema.TransactionsLogSchema.TABLE_NAME, null, initialvalue);

    }

    public long AddCheckList(CheckList checklist) {
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_CUSTOMERID, checklist.getPersonId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_USER_ID, checklist.getUserId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_STATUS, checklist.getStatus());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_TYPE, checklist.getType());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_DESCRIPTION, checklist.getDescription());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_MODIFYDATE, checklist.getModifyDate());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_PUBLISH, checklist.getPublish());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_CHECK_LIST_CODE, checklist.getChecklistCode());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_MAHAK_ID, checklist.getMahakId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_DATABASE_ID, checklist.getDatabaseId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_ChecklistId, checklist.getChecklistId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_ChecklistClientId, checklist.getChecklistClientId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_VisitorId, checklist.getVisitorId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_DataHash, checklist.getDataHash());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_CreateDate, checklist.getCreateDate());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_UpdateDate, checklist.getUpdateDate());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_CreateSyncId, checklist.getCreateSyncId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_UpdateSyncId, checklist.getUpdateSyncId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_RowVersion, checklist.getRowVersion());

        return mDb.insert(DbSchema.CheckListSchema.TABLE_NAME, null, initialvalue);
    }

    public long AddUser(User user) {

        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.UserSchema.COLUMN_NAME, user.getName());
        initialvalue.put(DbSchema.UserSchema.COLUMN_PASSWORD, user.getPassword());
        initialvalue.put(DbSchema.UserSchema.COLUMN_USERNAME, user.getUsername());
        initialvalue.put(DbSchema.UserSchema.COLUMN_TYPE, user.getType());
        initialvalue.put(DbSchema.UserSchema.COLUMN_MODIFYDATE, user.getModifyDate());
        initialvalue.put(DbSchema.UserSchema.COLUMN_LOGINDATE, user.getLoginDate());
        initialvalue.put(DbSchema.UserSchema.COLUMN_MAHAK_ID, user.getMahakId());
        initialvalue.put(DbSchema.UserSchema.COLUMN_MASTER_ID, user.getMasterId());
        initialvalue.put(DbSchema.UserSchema.COLUMN_DATABASE_ID, user.getDatabaseId());
        initialvalue.put(DbSchema.UserSchema.COLUMN_PACKAGE_SERIAL, user.getPackageSerial());
        initialvalue.put(DbSchema.UserSchema.COLUMN_SYNC_ID, user.getSyncId());
        initialvalue.put(DbSchema.UserSchema.COLUMN_DATE_SYNC, user.getDateSync());
        initialvalue.put(DbSchema.UserSchema.COLUMN_StoreCode, 1);
        initialvalue.put(DbSchema.UserSchema.COLUMN_USER_ID, user.getServerUserID());
        initialvalue.put(DbSchema.UserSchema.COLUMN_UserToken, user.getUserToken());

        return mDb.insert(DbSchema.UserSchema.TABLE_NAME, null, initialvalue);
    }

    public long AddDeliveryOrderDetail(OrderDetail orderDetail) {
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_OrderDetailId, orderDetail.getOrderDetailId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_OrderId, orderDetail.getOrderId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_USER_ID, BaseActivity.getPrefUserId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_OrderDetailClientId, orderDetail.getOrderDetailClientId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_OrderClientId, orderDetail.getOrderClientId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_ProductDetailId, orderDetail.getProductDetailId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_ProductId, orderDetail.getProductId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Price, orderDetail.getPrice());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Count1, orderDetail.getCount1());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Count2, orderDetail.getCount2());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz, orderDetail.getSumCountBaJoz());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_GiftType, orderDetail.getGiftType());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Description, orderDetail.getDescription());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_TaxPercent, orderDetail.getTaxPercent());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_ChargePercent, orderDetail.getChargePercent());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Discount, orderDetail.getDiscount());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_DiscountType, orderDetail.getDiscountType());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_DataHash, orderDetail.getDataHash());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_CreateDate, orderDetail.getCreateDate());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_UpdateDate, orderDetail.getUpdateDate());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_CreateSyncId, orderDetail.getCreateSyncId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_UpdateSyncId, orderDetail.getUpdateSyncId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_RowVersion, orderDetail.getRowVersion());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_CostLevel, orderDetail.getCostLevel());
        return mDb.insert(DbSchema.OrderDetailSchema.TABLE_NAME, null, initialvalue);
    }

    public long AddDeliveryOrder(Order deliveryorder) {
        ContentValues initialvalue = new ContentValues();

        initialvalue.put(DbSchema.OrderSchema.COLUMN_PersonId, deliveryorder.getPersonId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_LATITUDE, deliveryorder.getLatitude());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_LONGITUDE, deliveryorder.getLongitude());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_ReturnReasonId, deliveryorder.getReturnReasonId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_PersonClientId, deliveryorder.getPersonClientId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_USER_ID, deliveryorder.getUserId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_DELIVERYDATE, deliveryorder.getDeliveryDate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_DESCRIPTION, deliveryorder.getDescription());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_DISCOUNT, deliveryorder.getDiscount());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_IMMEDIATE, deliveryorder.getImmediate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_MODIFYDATE, deliveryorder.getModifyDate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_PUBLISH, deliveryorder.getPublish());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_SETTLEMEMNTTYPE, deliveryorder.getSettlementType());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_CODE, deliveryorder.getCode());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_MAHAK_ID, deliveryorder.getMahakId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_OrderCode, deliveryorder.getOrderCode());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_DATABASE_ID, deliveryorder.getDatabaseId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_ISFINAL, deliveryorder.getIsFinal());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_TYPE, deliveryorder.getOrderType());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_OrderId, deliveryorder.getOrderId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_OrderClientId, deliveryorder.getOrderClientId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_ReceiptId, deliveryorder.getReceiptId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_SendCost, deliveryorder.getSendCost());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_OtherCost, deliveryorder.getOtherCost());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_DataHash, deliveryorder.getDataHash());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_CreateDate, deliveryorder.getCreateDate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_UpdateDate, deliveryorder.getUpdateDate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_CreateSyncId, deliveryorder.getCreateSyncId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_UpdateSyncId, deliveryorder.getUpdateSyncId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_RowVersion, deliveryorder.getRowVersion());

        return mDb.insert(DbSchema.OrderSchema.TABLE_NAME, null, initialvalue);
    }

    public long AddBank(Bank bank) {
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.BanksSchema.COLUMN_NAME, bank.getName());
        initialvalue.put(DbSchema.BanksSchema.COLUMN_USER_ID, BaseActivity.getPrefUserId());
        initialvalue.put(DbSchema.BanksSchema.COLUMN_DESCRIPTION, bank.getDescription());
        initialvalue.put(DbSchema.BanksSchema.COLUMN_MAHAK_ID, bank.getMahakId());
        initialvalue.put(DbSchema.BanksSchema.COLUMN_BANK_CODE, bank.getBankCode());
        initialvalue.put(DbSchema.BanksSchema.COLUMN_DATABASE_ID, bank.getDatabaseId());
        initialvalue.put(DbSchema.BanksSchema.COLUMN_MODIFYDATE, bank.getModifyDate());
        initialvalue.put(DbSchema.BanksSchema.COLUMN_BankId, bank.getBankId());
        initialvalue.put(DbSchema.BanksSchema.COLUMN_BankClientId, bank.getBankClientId());
        initialvalue.put(DbSchema.BanksSchema.COLUMN_DataHash, bank.getDataHash());
        initialvalue.put(DbSchema.BanksSchema.COLUMN_CreateDate, bank.getCreateDate());
        initialvalue.put(DbSchema.BanksSchema.COLUMN_UpdateDate, bank.getUpdateDate());
        initialvalue.put(DbSchema.BanksSchema.COLUMN_CreateSyncId, bank.getCreateSyncId());
        initialvalue.put(DbSchema.BanksSchema.COLUMN_UpdateSyncId, bank.getUpdateSyncId());
        initialvalue.put(DbSchema.BanksSchema.COLUMN_RowVersion, bank.getRowVersion());

        return mDb.insert(DbSchema.BanksSchema.TABLE_NAME, null, initialvalue);
    }

    public long AddReasons(Reasons reasons) {

        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_NAME, reasons.getName());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_DESCRIPTION, reasons.getDescription());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_TYPE, reasons.getType());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_MAHAK_ID, reasons.getMahakId());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_ReturnReasonCode, reasons.getReturnReasonCode());

        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_ReturnReasonId, reasons.getReturnReasonId());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_ReturnReasonClientId, reasons.getReturnReasonClientId());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_DataHash, reasons.getDataHash());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_UpdateDate, reasons.getUpdateDate());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_CreateDate, reasons.getCreateDate());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_CreateSyncId, reasons.getCreateSyncId());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_UpdateSyncId, reasons.getUpdateSyncId());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_RowVersion, reasons.getRowVersion());

        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_DATABASE_ID, reasons.getDatabaseId());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_UserId, getPrefUserId());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_MODIFYDATE, reasons.getModifyDate());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_ID, reasons.getId());


        return mDb.insert(DbSchema.ReasonsSchema.TABLE_NAME, null, initialvalue);
    }

    public long AddPromotionDetail(PromotionDetail promotionDetail, PromotionDetailOtherFields promotionDetailOtherFields) {

        ContentValues initialvalue = new ContentValues();

        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_DatabaseId, promotionDetail.getDatabaseId());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_MahakId, promotionDetail.getMahakId());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_USER_ID, promotionDetail.getUserId());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_MODIFYDATE, promotionDetail.getModifyDate());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_CreatedBy, promotionDetail.getCreatedBy());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_CreatedDate, promotionDetail.getCreatedDate());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_ModifiedBy, promotionDetail.getModifiedBy());

        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_IsCalcAdditive, promotionDetailOtherFields.isIsCalcAdditive());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_ReducedEffectOnPrice, promotionDetailOtherFields.isReducedEffectOnPrice());

        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_ToPayment, promotionDetailOtherFields.getToPayment());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_HowToPromotion, promotionDetailOtherFields.getHowToPromotion());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_MeghdarPromotion, promotionDetailOtherFields.getMeghdarPromotion());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_StoreCode, promotionDetailOtherFields.getStoreCode());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_CodeGood, promotionDetailOtherFields.getCodeGood());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_tool, promotionDetailOtherFields.getTool());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_arz, promotionDetailOtherFields.getArz());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_tedad, promotionDetailOtherFields.getTedad());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_meghdar2, promotionDetailOtherFields.getMeghdar2());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_ghotr, promotionDetailOtherFields.getGhotr());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_ToolidCode, promotionDetailOtherFields.getToolidCode());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_meghdar, promotionDetailOtherFields.getMeghdar());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_PromotionCode, promotionDetailOtherFields.getCodePromotionDet());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_meghdar, promotionDetailOtherFields.getMeghdar());

        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_SyncID, promotionDetail.getSyncID());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_PromotionDetailId, promotionDetail.getPromotionDetailId());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_PromotionDetailCode, promotionDetail.getPromotionDetailCode());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_PromotionDetailClientId, promotionDetail.getPromotionDetailClientId());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_DataHash, promotionDetail.getDataHash());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_CreateDate, promotionDetail.getCreateDate());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_UpdateDate, promotionDetail.getUpdateDate());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_CreateSyncId, promotionDetail.getCreateSyncId());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_UpdateSyncId, promotionDetail.getUpdateSyncId());
        initialvalue.put(DbSchema.PromotionDetailSchema.COLUMN_RowVersion, promotionDetail.getRowVersion());


        return mDb.insert(DbSchema.PromotionDetailSchema.TABLE_NAME, null, initialvalue);
    }

    public long AddEntitiesOfPromotions(PromotionEntity promotionEntity, PromotionEntityOtherFields promotionEntityOtherFields) {

        ContentValues initialvalue = new ContentValues();

        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_DatabaseId, promotionEntity.getDatabaseId());
        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_MahakId, promotionEntity.getMahakId());
        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_USER_ID, promotionEntity.getUserId());
        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_MODIFYDATE, promotionEntity.getModifyDate());
        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_CreatedBy, promotionEntity.getCreatedBy());
        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_CreatedDate, promotionEntity.getCreatedDate());
        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_ModifiedBy, promotionEntity.getModifiedBy());

        //initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_CodePromotion, promotionEntity.getPromotionCode());

        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_SyncID, promotionEntity.getSyncID());

        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_CodeEntity, promotionEntityOtherFields.getCodeEntity());
        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_CodePromotionEntity, promotionEntityOtherFields.getCodePromotionEntity());
        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_EntityType, promotionEntityOtherFields.getEntityType());

        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_PromotionEntityId, promotionEntity.getPromotionEntityId());
        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_PromotionId, promotionEntity.getPromotionId());
        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_PromotionEntityClientId, promotionEntity.getPromotionEntityClientId());

        //initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_PromotionCode, promotionEntity.getPromotionCode());

        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_DataHash, promotionEntity.getDataHash());
        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_CreateDate, promotionEntity.getCreateDate());
        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_UpdateDate, promotionEntity.getUpdateDate());
        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_CreateSyncId, promotionEntity.getCreateSyncId());
        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_UpdateSyncId, promotionEntity.getUpdateSyncId());
        initialvalue.put(DbSchema.PromotionEntitySchema.COLUMN_RowVersion, promotionEntity.getRowVersion());


        return mDb.insert(DbSchema.PromotionEntitySchema.TABLE_NAME, null, initialvalue);
    }

    public long AddPromotion(Promotion promotion, PromotionOtherFields promotionOtherFields) {

        ContentValues initialvalue = new ContentValues();

        initialvalue.put(DbSchema.PromotionSchema.COLUMN_DatabaseId, promotion.getDatabaseId());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_MahakId, promotion.getMahakId());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_USER_ID, promotion.getUserId());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_MODIFYDATE, promotion.getModifyDate());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_CreatedBy, promotion.getCreatedBy());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_CreatedDate, promotion.getCreatedDate());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_ModifiedBy, promotion.getModifiedBy());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_PromotionCode, promotion.getPromotionCode());

        initialvalue.put(DbSchema.PromotionSchema.COLUMN_NamePromotion, promotionOtherFields.getNamePromotion());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_PriorityPromotion, promotionOtherFields.getPriorityPromotion());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_DateStart, promotionOtherFields.getDateStart());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_DateEnd, promotionOtherFields.getDateEnd());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_TimeStart, promotionOtherFields.getTimeStart());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_TimeEnd, promotionOtherFields.getTimeEnd());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_LevelPromotion, promotionOtherFields.getLevelPromotion());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_AccordingTo, promotionOtherFields.getAccordingTo());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_IsCalcLinear, promotionOtherFields.isIsCalcLinear());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_TypeTasvieh, promotionOtherFields.getTypeTasvieh());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_DeadlineTasvieh, promotionOtherFields.getDeadlineTasvieh());

        initialvalue.put(DbSchema.PromotionSchema.COLUMN_IsActive, 1);
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_DesPromotion, promotionOtherFields.getDesPromotion());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_IsAllCustomer, promotionOtherFields.isIsAllCustomer());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_IsAllVisitor, promotionOtherFields.isIsAllVisitor());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_IsAllGood, promotionOtherFields.isIsAllGood());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_IsAllStore, promotionOtherFields.isIsAllStore());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_AggregateWithOther, promotionOtherFields.getAggregateWithOther());

        initialvalue.put(DbSchema.PromotionSchema.COLUMN_PromotionId, promotion.getPromotionId());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_PromotionClientId, promotion.getPromotionClientId());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_PromotionCode, promotion.getPromotionCode());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_Visitors, promotion.getVisitors());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_Stores, promotion.getStores());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_DataHash, promotion.getDataHash());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_CreateDate, promotion.getCreateDate());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_UpdateDate, promotion.getUpdateDate());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_CreateSyncId, promotion.getCreateSyncId());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_UpdateSyncId, promotion.getUpdateSyncId());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_RowVersion, promotion.getRowVersion());
        initialvalue.put(DbSchema.PromotionSchema.COLUMN_Deleted, promotion.getDeleted());


        return mDb.insert(DbSchema.PromotionSchema.TABLE_NAME, null, initialvalue);
    }

    public void AddCityZone(CityZone_Extra_Data cityZone_extra_data) {
        ContentValues initialvalue = new ContentValues();
        if (!TextUtils.isEmpty(cityZone_extra_data.getZoneName())) {
            initialvalue.put(DbSchema.CityZoneSchema.COLUMN_ZoneCode, cityZone_extra_data.getZoneCode());
            initialvalue.put(DbSchema.CityZoneSchema.COLUMN_ParentCode, cityZone_extra_data.getParentCode());
            initialvalue.put(DbSchema.CityZoneSchema.COLUMN_ZoneName, cityZone_extra_data.getZoneName());
            mDb.insert(DbSchema.CityZoneSchema.TABLE_NAME, null, initialvalue);
        }
    }

    public void AddCategory(Category category) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbSchema.CategorySchema.COLUMN_CategoryCode, category.getCategoryCode());
        contentValues.put(DbSchema.CategorySchema.COLUMN_ParentCode, category.getParentCode());
        contentValues.put(DbSchema.CategorySchema.COLUMN_CategoryName, category.getCategoryName());
        contentValues.put(DbSchema.CategorySchema.COLUMN_USER_ID, getPrefUserId());
        mDb.insert(DbSchema.CategorySchema.TABLE_NAME, null, contentValues);
    }

    public void AddProductCategory(ProductCategory productCategory) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbSchema.ProductCategorySchema.COLUMN_CategoryCode, productCategory.getCategoryCode());
        contentValues.put(DbSchema.ProductCategorySchema.COLUMN_ProductCode, productCategory.getProductCode());
        contentValues.put(DbSchema.ProductCategorySchema.COLUMN_USER_ID, BaseActivity.getPrefUserId());
        mDb.insert(DbSchema.ProductCategorySchema.TABLE_NAME, null, contentValues);
    }

    public void AddPersonCategory(PersonCategory personCategory) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbSchema.PersonCategorySchema.COLUMN_CategoryCode, personCategory.getCategoryCode());
        contentValues.put(DbSchema.PersonCategorySchema.COLUMN_PersonCode, personCategory.getPersonCode());
        contentValues.put(DbSchema.PersonCategorySchema.COLUMN_USER_ID, BaseActivity.getPrefUserId());
        mDb.insert(DbSchema.PersonCategorySchema.TABLE_NAME, null, contentValues);
    }

    public long AddPriceLevelName(ProductPriceLevelName productPriceLevelName) {

        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.PriceLevelNameSchema.PRICE_LEVEL_NAME, productPriceLevelName.getPriceLevelName());
        initialvalue.put(DbSchema.PriceLevelNameSchema.PRICE_LEVEL_CODE, productPriceLevelName.getPriceLevelCode());
        initialvalue.put(DbSchema.PriceLevelNameSchema._SyncId, productPriceLevelName.getSyncID());
        initialvalue.put(DbSchema.PriceLevelNameSchema.COLUMN_USER_ID, productPriceLevelName.getUserId());
        initialvalue.put(DbSchema.PriceLevelNameSchema._MODIFY_DATE, productPriceLevelName.getModifyDate());

        initialvalue.put(DbSchema.PriceLevelNameSchema.CostLevelNameId, productPriceLevelName.getCostLevelNameId());
        initialvalue.put(DbSchema.PriceLevelNameSchema.CostLevelNameClientId, productPriceLevelName.getCostLevelNameClientId());
        initialvalue.put(DbSchema.PriceLevelNameSchema.DataHash, productPriceLevelName.getDataHash());
        initialvalue.put(DbSchema.PriceLevelNameSchema.UpdateDate, productPriceLevelName.getUpdateDate());
        initialvalue.put(DbSchema.PriceLevelNameSchema.CreateDate, productPriceLevelName.getCreateDate());
        initialvalue.put(DbSchema.PriceLevelNameSchema.CreateSyncId, productPriceLevelName.getCreateSyncId());
        initialvalue.put(DbSchema.PriceLevelNameSchema.UpdateSyncId, productPriceLevelName.getUpdateSyncId());
        initialvalue.put(DbSchema.PriceLevelNameSchema.RowVersion, productPriceLevelName.getRowVersion());

        return mDb.insert(DbSchema.PriceLevelNameSchema.TABLE_NAME, null, initialvalue);
    }

    public long AddReceivedTransferProducts(ReceivedTransferProducts receivedTransferProducts, Product product) {
        ContentValues initialvalue = new ContentValues();

        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_Name, product.getName());

        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_DatabaseId, receivedTransferProducts.getDatabaseId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_MahakId, receivedTransferProducts.getMahakId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_ModifyDate, receivedTransferProducts.getModifyDate());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_ProductId, receivedTransferProducts.getProductDetailId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_Count1, receivedTransferProducts.getCount1());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_TransferId, receivedTransferProducts.getTransferStoreId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_CreatedDate, receivedTransferProducts.getCreatedDate());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_CreatedBy, receivedTransferProducts.getCreatedBy());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_Description, receivedTransferProducts.getDescription());

        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_TransferStoreDetailId, receivedTransferProducts.getTransferStoreDetailId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_TransferStoreDetailClientId, receivedTransferProducts.getTransferStoreDetailClientId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_TransferStoreId, receivedTransferProducts.getTransferStoreId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_ProductDetailId, receivedTransferProducts.getProductDetailId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_Count2, receivedTransferProducts.getCount2());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_DataHash, receivedTransferProducts.getDataHash());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_CreateDate, receivedTransferProducts.getCreateDate());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_UpdateDate, receivedTransferProducts.getUpdateDate());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_CreateSyncId, receivedTransferProducts.getCreateSyncId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_UpdateSyncId, receivedTransferProducts.getUpdateSyncId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_RowVersion, receivedTransferProducts.getRowVersion());


        return mDb.insert(DbSchema.ReceivedTransferProductsSchema.TABLE_NAME, null, initialvalue);
    }


    //QUERIES GET--------------------------------------------------------------------------------------------

    public String getBankNameFromBankID(String bankId) {

        Cursor cursor;
        String BankName = "";
        try {

            cursor = mDb.query(DbSchema.BanksSchema.TABLE_NAME, null, DbSchema.BanksSchema.COLUMN_BankId + "=? ", new String[]{bankId}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {

                    BankName = (cursor.getString(cursor.getColumnIndex(DbSchema.BanksSchema.COLUMN_NAME)));

                    cursor.close();
                }
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getBankNameFromBankID", e.getMessage());
        }
        return BankName;
    }

    public long GetgroupIdFromCustomer(Customer customer) {
        long groupId = 0L;
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.CustomerSchema.TABLE_NAME, null, DbSchema.CustomerSchema.COLUMN_USER_ID + " =? and " + DbSchema.CustomerSchema.COLUMN_MAHAK_ID + "=? and " + DbSchema.CustomerSchema.COLUMN_PersonCode + "=? and " + DbSchema.CustomerSchema.COLUMN_DATABASE_ID + " =? ", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), String.valueOf(customer.getPersonCode()), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    groupId = cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonGroupId));
                    cursor.close();
                }
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("GetgroupIdFromCustomer", e.getMessage());
        }
        return groupId;
    }

    @NonNull
    private ContentValues getOfflinePicturesProduct(PicturesProduct picturesProduct) {
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_PICTURE_ID, picturesProduct.getPictureId());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_PRODUCT_ID, picturesProduct.getProductId());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_ITEM_ID, picturesProduct.getItemId());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_ITEM_TYPE, picturesProduct.getItemType());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_PictureCode, picturesProduct.getPictureCode());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_FILE_NAME, picturesProduct.getFileName());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_FILE_SIZE, picturesProduct.getFileSize());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_TITLE, picturesProduct.getTitle());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_URL, picturesProduct.getUrl());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_DATABASE_ID, picturesProduct.getDataBaseId());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_MAHAK_ID, picturesProduct.getMahakId());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_USER_ID, picturesProduct.getUserId());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_LAST_UPDATE, picturesProduct.getLastUpdate());

        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_PictureClientId, picturesProduct.getPictureClientId());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_DisplayOrder, picturesProduct.getDisplayOrder());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_Width, picturesProduct.getWidth());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_Height, picturesProduct.getHeight());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_Format, picturesProduct.getFormat());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_PictureHash, picturesProduct.getPictureHash());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_DataHash, picturesProduct.getDataHash());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_CreateDate, picturesProduct.getCreateDate());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_UpdateDate, picturesProduct.getUpdateDate());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_CreateSyncId, picturesProduct.getCreateSyncId());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_UpdateSyncId, picturesProduct.getUpdateSyncId());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_RowVersion, picturesProduct.getRowVersion());


        return initialvalue;
    }

    private ContentValues getOnlinePictureProduct(PicturesProduct picturesProduct) {
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_PICTURE_ID, picturesProduct.getPictureId());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_PRODUCT_ID, picturesProduct.getProductId());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_ITEM_TYPE, picturesProduct.getItemType());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_ITEM_ID, picturesProduct.getItemId());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_PictureCode, picturesProduct.getPictureCode());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_FILE_NAME, picturesProduct.getFileName());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_FILE_SIZE, picturesProduct.getFileSize());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_TITLE, picturesProduct.getTitle());
        if (picturesProduct.getUrl() != null)
            initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_URL, baseUrlImage + picturesProduct.getUrl());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_DATABASE_ID, picturesProduct.getDataBaseId());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_MAHAK_ID, picturesProduct.getMahakId());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_USER_ID, picturesProduct.getUserId());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_LAST_UPDATE, picturesProduct.getLastUpdate());

        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_PictureClientId, picturesProduct.getPictureClientId());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_DisplayOrder, picturesProduct.getDisplayOrder());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_Width, picturesProduct.getWidth());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_Height, picturesProduct.getHeight());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_Format, picturesProduct.getFormat());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_PictureHash, picturesProduct.getPictureHash());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_DataHash, picturesProduct.getDataHash());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_CreateDate, picturesProduct.getCreateDate());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_UpdateDate, picturesProduct.getUpdateDate());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_CreateSyncId, picturesProduct.getCreateSyncId());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_UpdateSyncId, picturesProduct.getUpdateSyncId());
        initialvalue.put(DbSchema.PicturesProductSchema.COLUMN_RowVersion, picturesProduct.getRowVersion());


        return initialvalue;
    }

    public Person_Extra_Data getMoreCustomerInfo(long id) {

        Person_Extra_Data person_extra_data = null;
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.ExtraDataSchema.TABLE_NAME, null, DbSchema.ExtraDataSchema.COLUMN_ItemId + " =? and " + DbSchema.ExtraDataSchema.COLUMN_ItemType + " =? and " + DbSchema.ExtraDataSchema.COLUMN_USER_ID + "=?", new String[]{String.valueOf(id), String.valueOf(BaseActivity.person_extra_info), String.valueOf(getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    person_extra_data = getMoreCustomerInfo(cursor);
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getMoreCustomerInfo", e.getMessage());
        }
        return person_extra_data;

    }

    public Product_Extra_Data getProductExtraInfo(long id) {

        Product_Extra_Data product_extra_data = new Product_Extra_Data();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.ExtraDataSchema.TABLE_NAME, null, DbSchema.ExtraDataSchema.COLUMN_ItemId + " =? and " + DbSchema.ExtraDataSchema.COLUMN_ItemType + " =? and " + DbSchema.ExtraDataSchema.COLUMN_USER_ID + "=?", new String[]{String.valueOf(id), String.valueOf(BaseActivity.average_last_price), String.valueOf(getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    product_extra_data = getProductExtraInfo(cursor);
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getProductExtraInfo", e.getMessage());
        }
        return product_extra_data;
    }

    @NonNull
    private CityZone_Extra_Data getCityZone_extra_data(Cursor cursor) {
        CityZone_Extra_Data cityZone_extra_data = new CityZone_Extra_Data();
        cityZone_extra_data.setZoneCode(cursor.getLong(cursor.getColumnIndex(DbSchema.CityZoneSchema.COLUMN_ZoneCode)));
        cityZone_extra_data.setZoneName(cursor.getString(cursor.getColumnIndex(DbSchema.CityZoneSchema.COLUMN_ZoneName)));
        cityZone_extra_data.setParentCode(cursor.getInt(cursor.getColumnIndex(DbSchema.CityZoneSchema.COLUMN_ParentCode)));
        return cityZone_extra_data;
    }

    private Region getRegionFromCursor(Cursor cursor) {
        Region region = new Region();
        region.setCityName(cursor.getString(cursor.getColumnIndex(DbSchema.RegionSchema.COLUMN_CityName)));
        region.setCityID(cursor.getInt(cursor.getColumnIndex(DbSchema.RegionSchema.COLUMN_CityID)));
        region.setProvinceName(cursor.getString(cursor.getColumnIndex(DbSchema.RegionSchema.COLUMN_ProvinceName)));
        region.setProvinceID(cursor.getInt(cursor.getColumnIndex(DbSchema.RegionSchema.COLUMN_ProvinceID)));
        region.setRowVersion(cursor.getInt(cursor.getColumnIndex(DbSchema.RegionSchema.COLUMN_RowVersion)));

        return region;
    }

    public TaxInSell_Extra_Data getTaxInSellExtra(long id) {

        TaxInSell_Extra_Data taxInSell_extra_data = new TaxInSell_Extra_Data();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.ExtraDataSchema.TABLE_NAME, null, DbSchema.ExtraDataSchema.COLUMN_ItemId + " =? and " + DbSchema.ExtraDataSchema.COLUMN_ItemType + " =? and " + DbSchema.ExtraDataSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(id), String.valueOf(BaseActivity.tax_sell_in_cost), String.valueOf(getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    taxInSell_extra_data = getTaxInSellExtra(cursor);
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getTaxInSellExtra", e.getMessage());
        }
        return taxInSell_extra_data;
    }

    public Customer getCustomerWithPersonId(long id) {
        Customer customer = new Customer();
        Cursor cursor;
        String Columns = DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_ID + " as " + DbSchema.CustomerSchema.COLUMN_ID +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_NAME + " as " + DbSchema.CustomerSchema.COLUMN_NAME +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_FirstName + " as " + DbSchema.CustomerSchema.COLUMN_FirstName +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_LastName + " as " + DbSchema.CustomerSchema.COLUMN_LastName +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_Prefix + " as " + DbSchema.CustomerSchema.COLUMN_Prefix +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_MAHAK_ID + " as " + DbSchema.CustomerSchema.COLUMN_MAHAK_ID +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_DATABASE_ID + " as " + DbSchema.CustomerSchema.COLUMN_DATABASE_ID +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonCode + " as " + DbSchema.CustomerSchema.COLUMN_PersonCode +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonId + " as " + DbSchema.CustomerSchema.COLUMN_PersonId +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_HasOrder + " as " + DbSchema.CustomerSchema.COLUMN_HasOrder +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonClientId + " as " + DbSchema.CustomerSchema.COLUMN_PersonClientId +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_USER_ID + " as " + DbSchema.CustomerSchema.COLUMN_USER_ID +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonGroupId + " as " + DbSchema.CustomerSchema.COLUMN_PersonGroupId +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonGroupCode + " as " + DbSchema.CustomerSchema.COLUMN_PersonGroupCode +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_DiscountPercent + " as " + DbSchema.CustomerSchema.COLUMN_DiscountPercent +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_ORGANIZATION + " as " + DbSchema.CustomerSchema.COLUMN_ORGANIZATION +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_CREDIT + " as " + DbSchema.CustomerSchema.COLUMN_CREDIT +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_BALANCE + " as " + DbSchema.CustomerSchema.COLUMN_BALANCE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_STATE + " as " + DbSchema.CustomerSchema.COLUMN_STATE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_CITY + " as " + DbSchema.CustomerSchema.COLUMN_CITY +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_ADDRESS + " as " + DbSchema.CustomerSchema.COLUMN_ADDRESS +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_ZONE + " as " + DbSchema.CustomerSchema.COLUMN_ZONE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_CityCode + " as " + DbSchema.CustomerSchema.COLUMN_CityCode +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PHONE + " as " + DbSchema.CustomerSchema.COLUMN_PHONE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_MOBILE + " as " + DbSchema.CustomerSchema.COLUMN_MOBILE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_LATITUDE + " as " + DbSchema.CustomerSchema.COLUMN_LATITUDE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_LONGITUDE + " as " + DbSchema.CustomerSchema.COLUMN_LONGITUDE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_SHIFT + " as " + DbSchema.CustomerSchema.COLUMN_SHIFT +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_SellPriceLevel + " as " + DbSchema.CustomerSchema.COLUMN_SellPriceLevel +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_MODIFYDATE + " as " + DbSchema.CustomerSchema.COLUMN_MODIFYDATE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PUBLISH + " as " + DbSchema.CustomerSchema.COLUMN_PUBLISH +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_RowVersion + " as " + DbSchema.CustomerSchema.COLUMN_RowVersion +
                "," + DbSchema.CustomersGroupSchema.TABLE_NAME + "." + DbSchema.CustomersGroupSchema.COLUMN_NAME + " as " + "GroupName";

        try {
            //cursor = mDb.query(DbSchema.Customerschema.TABLE_NAME, null, DbSchema.Customerschema.COLUMN_ID + "=?",new String[]{String.valueOf(id)}, null, null, null);
            cursor = mDb.rawQuery("select " + Columns + " from  " + DbSchema.CustomerSchema.TABLE_NAME + " inner join " + DbSchema.CustomersGroupSchema.TABLE_NAME + " on " + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonGroupId + " = " + DbSchema.CustomersGroupSchema.TABLE_NAME + "." + DbSchema.CustomersGroupSchema.COLUMN_PersonGroupId + " where " + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonId + " = " + id + " order by " + DbSchema.CustomerSchema.COLUMN_PersonCode, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                customer = new Customer();
                customer.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ID)));
                customer.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_MAHAK_ID)));
                customer.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_DATABASE_ID)));
                customer.setPersonCode(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonCode)));
                customer.setPersonId(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonId)));
                customer.setOrderCount(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_HasOrder)));
                customer.setPersonClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonClientId)));
                customer.setUserId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_USER_ID)));
                customer.setPersonGroupId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonGroupId)));
                customer.setPersonGroupCode(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonGroupCode)));
                customer.setName(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_NAME)));
                customer.setFirstName(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_FirstName)));
                customer.setLastName(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_LastName)));
                customer.setPrefix(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_Prefix)));
                customer.setOrganization(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ORGANIZATION)));
                customer.setCredit(cursor.getDouble(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_CREDIT)));
                customer.setCityCode(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_CityCode)));
                customer.setBalance(cursor.getDouble(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_BALANCE)));
                customer.setState(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_STATE)));
                customer.setCity(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_CITY)));
                customer.setAddress(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ADDRESS)));
                customer.setZone(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ZONE)));
                customer.setTell(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PHONE)));
                customer.setMobile(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_MOBILE)));
                customer.setLatitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_LATITUDE)));
                customer.setLongitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_LONGITUDE)));
                customer.setShift(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_SHIFT)));
                customer.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_MODIFYDATE)));
                customer.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PUBLISH)));
                customer.setDiscountPercent(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_DiscountPercent)));
                customer.setSellPriceLevel(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_SellPriceLevel)));
                customer.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_RowVersion)));
                customer.setGroup(cursor.getString(cursor.getColumnIndex("GroupName")));
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getCustomerWithPersonId", e.getMessage());
        }

        return customer;
    }

    public Customer getCustomer(long id) {
        Customer customer = new Customer();
        Cursor cursor;
        String Columns = DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_ID + " as " + DbSchema.CustomerSchema.COLUMN_ID +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_NAME + " as " + DbSchema.CustomerSchema.COLUMN_NAME +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_FirstName + " as " + DbSchema.CustomerSchema.COLUMN_FirstName +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_LastName + " as " + DbSchema.CustomerSchema.COLUMN_LastName +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_Prefix + " as " + DbSchema.CustomerSchema.COLUMN_Prefix +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_MAHAK_ID + " as " + DbSchema.CustomerSchema.COLUMN_MAHAK_ID +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_DATABASE_ID + " as " + DbSchema.CustomerSchema.COLUMN_DATABASE_ID +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonCode + " as " + DbSchema.CustomerSchema.COLUMN_PersonCode +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonId + " as " + DbSchema.CustomerSchema.COLUMN_PersonId +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_HasOrder + " as " + DbSchema.CustomerSchema.COLUMN_HasOrder +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonClientId + " as " + DbSchema.CustomerSchema.COLUMN_PersonClientId +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_USER_ID + " as " + DbSchema.CustomerSchema.COLUMN_USER_ID +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonGroupId + " as " + DbSchema.CustomerSchema.COLUMN_PersonGroupId +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonGroupCode + " as " + DbSchema.CustomerSchema.COLUMN_PersonGroupCode +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_DiscountPercent + " as " + DbSchema.CustomerSchema.COLUMN_DiscountPercent +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_ORGANIZATION + " as " + DbSchema.CustomerSchema.COLUMN_ORGANIZATION +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_CREDIT + " as " + DbSchema.CustomerSchema.COLUMN_CREDIT +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_CityCode + " as " + DbSchema.CustomerSchema.COLUMN_CityCode +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_BALANCE + " as " + DbSchema.CustomerSchema.COLUMN_BALANCE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_STATE + " as " + DbSchema.CustomerSchema.COLUMN_STATE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_CITY + " as " + DbSchema.CustomerSchema.COLUMN_CITY +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_ADDRESS + " as " + DbSchema.CustomerSchema.COLUMN_ADDRESS +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_ZONE + " as " + DbSchema.CustomerSchema.COLUMN_ZONE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PHONE + " as " + DbSchema.CustomerSchema.COLUMN_PHONE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_MOBILE + " as " + DbSchema.CustomerSchema.COLUMN_MOBILE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_LATITUDE + " as " + DbSchema.CustomerSchema.COLUMN_LATITUDE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_LONGITUDE + " as " + DbSchema.CustomerSchema.COLUMN_LONGITUDE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_SHIFT + " as " + DbSchema.CustomerSchema.COLUMN_SHIFT +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_SellPriceLevel + " as " + DbSchema.CustomerSchema.COLUMN_SellPriceLevel +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_MODIFYDATE + " as " + DbSchema.CustomerSchema.COLUMN_MODIFYDATE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PUBLISH + " as " + DbSchema.CustomerSchema.COLUMN_PUBLISH +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_RowVersion + " as " + DbSchema.CustomerSchema.COLUMN_RowVersion +
                "," + DbSchema.CustomersGroupSchema.TABLE_NAME + "." + DbSchema.CustomersGroupSchema.COLUMN_NAME + " as " + "GroupName" +
                "," + DbSchema.PromotionEntitySchema.TABLE_NAME + "." + DbSchema.PromotionEntitySchema.COLUMN_PromotionId;

        try {
            //cursor = mDb.query(DbSchema.Customerschema.TABLE_NAME, null, DbSchema.Customerschema.COLUMN_ID + "=?",new String[]{String.valueOf(id)}, null, null, null);
            cursor = mDb.rawQuery("select " + Columns + " from  " + DbSchema.CustomerSchema.TABLE_NAME + " inner join " + DbSchema.CustomersGroupSchema.TABLE_NAME + " on " + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonGroupId + " = " + DbSchema.CustomersGroupSchema.TABLE_NAME + "." + DbSchema.CustomersGroupSchema.COLUMN_PersonGroupId + " LEFT join PromotionEntity  on PromotionEntity.CodeEntity = Customers.PersonCode and EntityType = 2 " + " where " + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_ID + " = " + id + " order by " + DbSchema.CustomerSchema.COLUMN_PersonCode, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                customer = new Customer();
                customer.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ID)));
                customer.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_MAHAK_ID)));
                customer.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_DATABASE_ID)));
                customer.setPersonCode(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonCode)));
                customer.setPersonId(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonId)));
                customer.setOrderCount(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_HasOrder)));
                customer.setPersonClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonClientId)));
                customer.setUserId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_USER_ID)));
                customer.setPersonGroupId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonGroupId)));
                customer.setPersonGroupCode(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonGroupCode)));
                customer.setName(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_NAME)));
                customer.setFirstName(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_FirstName)));
                customer.setLastName(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_LastName)));
                customer.setPrefix(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_Prefix)));
                customer.setOrganization(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ORGANIZATION)));
                customer.setCredit(cursor.getDouble(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_CREDIT)));
                customer.setCityCode(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_CityCode)));
                customer.setBalance(cursor.getDouble(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_BALANCE)));
                customer.setState(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_STATE)));
                customer.setCity(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_CITY)));
                customer.setAddress(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ADDRESS)));
                customer.setZone(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ZONE)));
                customer.setTell(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PHONE)));
                customer.setMobile(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_MOBILE)));
                customer.setLatitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_LATITUDE)));
                customer.setLongitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_LONGITUDE)));
                customer.setShift(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_SHIFT)));
                customer.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_MODIFYDATE)));
                customer.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PUBLISH)));
                customer.setDiscountPercent(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_DiscountPercent)));
                customer.setSellPriceLevel(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_SellPriceLevel)));
                customer.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_RowVersion)));
                customer.setGroup(cursor.getString(cursor.getColumnIndex("GroupName")));
                customer.setPromotionId(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_PromotionId)));
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getCustomer", e.getMessage());
        }

        return customer;
    }

    public Customer getCustomerWithPersonClientId(long id) {
        Customer customer = new Customer();
        Cursor cursor;
        String Columns = DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_ID + " as " + DbSchema.CustomerSchema.COLUMN_ID +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_NAME + " as " + DbSchema.CustomerSchema.COLUMN_NAME +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_FirstName + " as " + DbSchema.CustomerSchema.COLUMN_FirstName +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_LastName + " as " + DbSchema.CustomerSchema.COLUMN_LastName +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_Prefix + " as " + DbSchema.CustomerSchema.COLUMN_Prefix +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_MAHAK_ID + " as " + DbSchema.CustomerSchema.COLUMN_MAHAK_ID +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_DATABASE_ID + " as " + DbSchema.CustomerSchema.COLUMN_DATABASE_ID +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonCode + " as " + DbSchema.CustomerSchema.COLUMN_PersonCode +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonId + " as " + DbSchema.CustomerSchema.COLUMN_PersonId +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_HasOrder + " as " + DbSchema.CustomerSchema.COLUMN_HasOrder +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonClientId + " as " + DbSchema.CustomerSchema.COLUMN_PersonClientId +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_USER_ID + " as " + DbSchema.CustomerSchema.COLUMN_USER_ID +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonGroupId + " as " + DbSchema.CustomerSchema.COLUMN_PersonGroupId +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonGroupCode + " as " + DbSchema.CustomerSchema.COLUMN_PersonGroupCode +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_DiscountPercent + " as " + DbSchema.CustomerSchema.COLUMN_DiscountPercent +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_ORGANIZATION + " as " + DbSchema.CustomerSchema.COLUMN_ORGANIZATION +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_CREDIT + " as " + DbSchema.CustomerSchema.COLUMN_CREDIT +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_CityCode + " as " + DbSchema.CustomerSchema.COLUMN_CityCode +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_BALANCE + " as " + DbSchema.CustomerSchema.COLUMN_BALANCE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_STATE + " as " + DbSchema.CustomerSchema.COLUMN_STATE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_CITY + " as " + DbSchema.CustomerSchema.COLUMN_CITY +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_ADDRESS + " as " + DbSchema.CustomerSchema.COLUMN_ADDRESS +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_ZONE + " as " + DbSchema.CustomerSchema.COLUMN_ZONE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PHONE + " as " + DbSchema.CustomerSchema.COLUMN_PHONE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_MOBILE + " as " + DbSchema.CustomerSchema.COLUMN_MOBILE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_LATITUDE + " as " + DbSchema.CustomerSchema.COLUMN_LATITUDE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_LONGITUDE + " as " + DbSchema.CustomerSchema.COLUMN_LONGITUDE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_SHIFT + " as " + DbSchema.CustomerSchema.COLUMN_SHIFT +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_SellPriceLevel + " as " + DbSchema.CustomerSchema.COLUMN_SellPriceLevel +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_MODIFYDATE + " as " + DbSchema.CustomerSchema.COLUMN_MODIFYDATE +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PUBLISH + " as " + DbSchema.CustomerSchema.COLUMN_PUBLISH +
                "," + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_RowVersion + " as " + DbSchema.CustomerSchema.COLUMN_RowVersion +
                "," + DbSchema.CustomersGroupSchema.TABLE_NAME + "." + DbSchema.CustomersGroupSchema.COLUMN_NAME + " as " + "GroupName";

        try {
            //cursor = mDb.query(DbSchema.Customerschema.TABLE_NAME, null, DbSchema.Customerschema.COLUMN_ID + "=?",new String[]{String.valueOf(id)}, null, null, null);
            cursor = mDb.rawQuery("select " + Columns + " from  " + DbSchema.CustomerSchema.TABLE_NAME + " inner join " + DbSchema.CustomersGroupSchema.TABLE_NAME + " on " + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonGroupId + " = " + DbSchema.CustomersGroupSchema.TABLE_NAME + "." + DbSchema.CustomersGroupSchema.COLUMN_PersonGroupId + " where " + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonClientId + " = " + id + " order by " + DbSchema.CustomerSchema.COLUMN_PersonCode, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                customer = new Customer();
                customer.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ID)));
                customer.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_MAHAK_ID)));
                customer.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_DATABASE_ID)));
                customer.setPersonCode(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonCode)));
                customer.setPersonId(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonId)));
                customer.setOrderCount(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_HasOrder)));
                customer.setPersonClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonClientId)));
                customer.setUserId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_USER_ID)));
                customer.setPersonGroupId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonGroupId)));
                customer.setPersonGroupCode(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonGroupCode)));
                customer.setName(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_NAME)));
                customer.setFirstName(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_FirstName)));
                customer.setLastName(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_LastName)));
                customer.setPrefix(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_Prefix)));
                customer.setOrganization(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ORGANIZATION)));
                customer.setCredit(cursor.getDouble(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_CREDIT)));
                customer.setCityCode(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_CityCode)));
                customer.setBalance(cursor.getDouble(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_BALANCE)));
                customer.setState(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_STATE)));
                customer.setCity(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_CITY)));
                customer.setAddress(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ADDRESS)));
                customer.setZone(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ZONE)));
                customer.setTell(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PHONE)));
                customer.setMobile(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_MOBILE)));
                customer.setLatitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_LATITUDE)));
                customer.setLongitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_LONGITUDE)));
                customer.setShift(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_SHIFT)));
                customer.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_MODIFYDATE)));
                customer.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PUBLISH)));
                customer.setDiscountPercent(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_DiscountPercent)));
                customer.setSellPriceLevel(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_SellPriceLevel)));
                customer.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_RowVersion)));
                customer.setGroup(cursor.getString(cursor.getColumnIndex("GroupName")));
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getCustomerWr", e.getMessage());
        }

        return customer;
    }

    public Customer getCustomerByBarcode(String mBarcode) {
        Customer customer = new Customer();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.CustomerSchema.TABLE_NAME, null, DbSchema.CustomerSchema.COLUMN_PersonCode + "=? And " + DbSchema.CustomerSchema.COLUMN_USER_ID + " =? ", new String[]{mBarcode, String.valueOf(getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    customer = getCustomerFromCursor(cursor);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getCustomerByBarcode", e.getMessage());
        }
        return customer;
    }

    public Customer getCustomerWithPersonId(int personId) {
        Customer customer = new Customer();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.CustomerSchema.TABLE_NAME, null, DbSchema.CustomerSchema.COLUMN_PersonId + "=? And " + DbSchema.CustomerSchema.COLUMN_USER_ID + " =? " , new String[]{String.valueOf(personId), String.valueOf(getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    customer = getCustomerFromCursor(cursor);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getCustomerByBarcode", e.getMessage());
        }
        return customer;
    }

    public Product GetProductWithProductId(long id) {
        Product product = new Product();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.ProductSchema.TABLE_NAME, null, DbSchema.ProductSchema.COLUMN_ProductId + "=? And " + DbSchema.ProductSchema.COLUMN_DATABASE_ID + "=? And " + DbSchema.ProductSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(id), BaseActivity.getPrefDatabaseId(), String.valueOf(getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    product = getProductFromCursor(cursor);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("GetProductWithProductId", e.getMessage());
        }
        return product;
    }

    public Customer GetPromoCustomer(int masterId) {
        Customer customer = new Customer();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.CustomerSchema.TABLE_NAME, null, DbSchema.CustomerSchema.COLUMN_PersonCode + " =? ", new String[]{String.valueOf(masterId)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    customer = getCustomerFromCursor(cursor);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("GetPromoCustomer", e.getMessage());
        }
        return customer;
    }

    public Product getProductByMasterIdAndDataBaseId(String masterId, String dataBaseId) {
        Product product = new Product();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.ProductSchema.TABLE_NAME, null, DbSchema.ProductSchema.COLUMN_PRODUCT_CODE + "=? And " + DbSchema.ProductSchema.COLUMN_DATABASE_ID + " =? ", new String[]{masterId, dataBaseId}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    product = getProductFromCursor(cursor);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getProductByMer", e.getMessage());
        }
        return product;
    }

    public Product getProductWithProductCode(int productCode) {
        Product product = new Product();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.ProductSchema.TABLE_NAME, null, DbSchema.ProductSchema.COLUMN_PRODUCT_CODE + "=? And " + DbSchema.ProductSchema.COLUMN_DATABASE_ID + "=? And " + DbSchema.ProductSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(productCode), BaseActivity.getPrefDatabaseId(), String.valueOf(getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    product = getProductFromCursor(cursor);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getProductWi", e.getMessage());
        }
        return product;
    }

    private double getProductAsset1(String masterId) {
        double asset1 = 0;
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.VisitorProductSchema.TABLE_NAME, null, DbSchema.VisitorProductSchema.COLUMN_ProductDetailId + " =? and " + DbSchema.VisitorProductSchema.COLUMN_USER_ID + " =? ", new String[]{masterId , String.valueOf(getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    asset1 = cursor.getDouble(cursor.getColumnIndex(DbSchema.VisitorProductSchema.COLUMN_Count1));
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getProductAsset1", e.getMessage());
        }
        return asset1;
    }

    private double getProductAsset2(String masterId) {
        double asset2 = 0;
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.VisitorProductSchema.TABLE_NAME, null, DbSchema.VisitorProductSchema.COLUMN_ProductDetailId  + " =? and " + DbSchema.VisitorProductSchema.COLUMN_USER_ID + " =? ", new String[]{masterId , String.valueOf(getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    asset2 = cursor.getDouble(cursor.getColumnIndex(DbSchema.VisitorProductSchema.COLUMN_Count2));
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getProductAsset2", e.getMessage());
        }
        return asset2;
    }

    public ProductDetail getProductDetailForState(long masterId) {

        ProductDetail productDetail = new ProductDetail();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.ProductDetailSchema.TABLE_NAME, null, DbSchema.ProductDetailSchema.COLUMN_ProductDetailId + " =? and " + DbSchema.ProductDetailSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(masterId),String.valueOf(getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    productDetail = getProductDetailForUpdate(cursor);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorProductDetail11", e.getMessage());
        }
        return productDetail;
    }

    public ProductDetail getProductDetail(long masterId) {

        ProductDetail productDetail = new ProductDetail();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.ProductDetailSchema.TABLE_NAME, null, DbSchema.ProductDetailSchema.COLUMN_ProductDetailId  + " =? and " + DbSchema.ProductDetailSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(masterId) , String.valueOf(getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    productDetail = getProductDetailFromCursor(cursor);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getProductDetail", e.getMessage());
        }
        return productDetail;
    }
    public VisitorProduct getVisitorProduct(long masterId) {

        VisitorProduct visitorProduct = new VisitorProduct();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.VisitorProductSchema.TABLE_NAME, null, DbSchema.VisitorProductSchema.COLUMN_ProductDetailId + " =? and " + DbSchema.VisitorProductSchema.COLUMN_Deleted + " =? and " + DbSchema.VisitorProductSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(masterId), String.valueOf(0) , String.valueOf(getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    visitorProduct = getVisitorProductFromCursor(cursor);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getProductDetail", e.getMessage());
        }
        return visitorProduct;
    }

    public ProductDetail getProductDetailWithProductId(long productId) {

        ProductDetail productDetail = new ProductDetail();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.ProductDetailSchema.TABLE_NAME, null, DbSchema.ProductDetailSchema.COLUMN_ProductId  + " =? and " + DbSchema.ProductDetailSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(productId), String.valueOf(getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    productDetail = getProductDetailFromCursor(cursor);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getProductDeilWi", e.getMessage());
        }
        return productDetail;
    }

    @NonNull
    private Product getProductFromCursor(Cursor cursor) {
        Product product;
        product = new Product();
        product.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_ID)));
        product.setProductCategoryId(cursor.getLong(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_CATEGORYID)));
        product.setProductCode(cursor.getLong(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_PRODUCT_CODE)));
        product.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_MAHAK_ID)));
        product.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_DATABASE_ID)));
        product.setUserId(cursor.getLong(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_USER_ID)));
        product.setName(cursor.getString(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_NAME)));
        product.setUnitRatio(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_UnitRatio)));
        product.setRealPrice(cursor.getString(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_REALPRICE)));
        product.setTags(cursor.getString(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_TAGS)));

        product.setMin(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_MIN)));
        product.setCode(cursor.getString(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_CODE)));
        product.setWeight(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_WEIGHT)));

        product.setWidth(cursor.getFloat(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_Width)));
        product.setHeight(cursor.getFloat(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_Height)));
        product.setLength(cursor.getFloat(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_Length)));

        product.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_MODIFYDATE)));
        product.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_PUBLISH)));
        product.setUnitName(cursor.getString(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_UNITNAME)));
        product.setUnitName2(cursor.getString(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_UNITNAME2)));
        product.setTaxPercent(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_TAX)));
        product.setChargePercent(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_CHARGE)));
        product.setDiscountPercent(cursor.getString(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_DiscountPercent)));
        product.setBarcode(cursor.getString(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_Barcode)));
        product.setProductId(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_ProductId)));
        product.setProductClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_ProductClientId)));
        product.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_DataHash)));
        product.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_CreateDate)));
        product.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_UpdateDate)));
        product.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_CreateSyncId)));
        product.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_UpdateSyncId)));
        product.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_RowVersion)));
        product.setDeleted(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_Deleted)));

        //price = ServiceTools.getPriceFromPriceLevel2(productDetails.get(0));

        return product;
    }

    private Product getProductFromCursor2(Cursor cursor) {
        Product product;
        product = new Product();

        product.setProductCode(cursor.getLong(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_PRODUCT_CODE)));
        product.setUnitRatio(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_UnitRatio)));
        product.setProductId(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_ProductId)));
        product.setName(cursor.getString(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_NAME)));
        product.setUnitName(cursor.getString(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_UNITNAME)));
        product.setUnitName2(cursor.getString(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_UNITNAME2)));
        product.setCustomerPrice(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_CustomerPrice)));
        product.setSumCount1(cursor.getDouble(cursor.getColumnIndex("sumcount1")));
        product.setSumCount2(cursor.getDouble(cursor.getColumnIndex("sumcount2")));
        product.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
        product.setPromotionId(cursor.getInt(cursor.getColumnIndex("PromotionId")));
        product.setProductDetailId(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_ProductDetailId)));
        product.setDiscount(cursor.getDouble(cursor.getColumnIndex("discount")));

        return product;
    }

    @NonNull
    private Setting getSettingFromCursor(Cursor cursor) {
        Setting setting = new Setting();

        setting.setSettingId(cursor.getInt(cursor.getColumnIndex(DbSchema.SettingSchema.COLUMN_SettingId)));
        setting.setSettingCode(cursor.getInt(cursor.getColumnIndex(DbSchema.SettingSchema.COLUMN_SettingCode)));
        setting.setVisitorId(cursor.getLong(cursor.getColumnIndex(DbSchema.SettingSchema.COLUMN_USER_ID)));
        setting.setValue(cursor.getString(cursor.getColumnIndex(DbSchema.SettingSchema.COLUMN_Value)));
        setting.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.SettingSchema.COLUMN_DataHash)));
        setting.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.SettingSchema.COLUMN_CreateDate)));
        setting.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.SettingSchema.COLUMN_UpdateDate)));
        setting.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.SettingSchema.COLUMN_CreateSyncId)));
        setting.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.SettingSchema.COLUMN_UpdateSyncId)));
        setting.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.SettingSchema.COLUMN_RowVersion)));
        setting.setDeleted(cursor.getInt(cursor.getColumnIndex(DbSchema.SettingSchema.COLUMN_Deleted)));

        return setting;
    }

    @NonNull
    private Customer getCustomerFromCursor(Cursor cursor) {
        Customer customer;
        customer = new Customer();
        customer.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ID)));
        customer.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_MAHAK_ID)));
        customer.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_DATABASE_ID)));
        customer.setPersonCode(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonCode)));
        customer.setPersonId(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonId)));
        customer.setOrderCount(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_HasOrder)));
        customer.setPersonClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonClientId)));
        customer.setUserId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_USER_ID)));
        customer.setPersonGroupId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonGroupId)));
        customer.setPersonGroupCode(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonGroupCode)));
        customer.setName(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_NAME)));
        customer.setFirstName(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_FirstName)));
        customer.setLastName(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_LastName)));
        customer.setPrefix(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_Prefix)));
        customer.setOrganization(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ORGANIZATION)));
        customer.setCredit(cursor.getDouble(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_CREDIT)));
        customer.setCityCode(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_CityCode)));
        customer.setBalance(cursor.getDouble(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_BALANCE)));
        customer.setState(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_STATE)));
        customer.setCity(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_CITY)));
        customer.setAddress(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ADDRESS)));
        customer.setZone(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ZONE)));
        customer.setTell(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PHONE)));
        customer.setMobile(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_MOBILE)));
        customer.setLatitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_LATITUDE)));
        customer.setLongitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_LONGITUDE)));
        customer.setShift(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_SHIFT)));
        customer.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_MODIFYDATE)));
        customer.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PUBLISH)));
        customer.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_RowVersion)));
        customer.setDiscountPercent(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_DiscountPercent)));
        customer.setSellPriceLevel(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_SellPriceLevel)));
        return customer;
    }

    private Customer getCustomerFromCursor2(Cursor cursor) {
        Customer customer = new Customer();
        customer.setPersonCode(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonCode)));
        customer.setName(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_NAME)));
        customer.setOrganization(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ORGANIZATION)));
        customer.setAddress(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ADDRESS)));
        customer.setPromotionId(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_PromotionId)));
        customer.setPersonId(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonId)));
        customer.setPersonClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonClientId)));
        customer.setPersonGroupId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PersonGroupId)));
        customer.setTell(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_PHONE)));
        customer.setMobile(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_MOBILE)));
        customer.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ID)));
        customer.setBalance(cursor.getDouble(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_BALANCE)));
        return customer;
    }

    @NonNull
    private Person_Extra_Data getMoreCustomerInfo(Cursor cursor) {
        Person_Extra_Data person_extra_data = new Person_Extra_Data();
        Gson gson = new Gson();

        try {
            person_extra_data = gson.fromJson(cursor.getString(cursor.getColumnIndex(DbSchema.ExtraDataSchema.COLUMN_Data)), Person_Extra_Data.class);
        } catch (JsonSyntaxException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }

        return person_extra_data;
    }

    private Product_Extra_Data getProductExtraInfo(Cursor cursor) {
        Gson gson = new Gson();
        Product_Extra_Data product_extra_data = new Product_Extra_Data();
        try {
            product_extra_data = gson.fromJson(cursor.getString(cursor.getColumnIndex(DbSchema.ExtraDataSchema.COLUMN_Data)), Product_Extra_Data.class);
        } catch (JsonSyntaxException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return product_extra_data;
    }

    private TaxInSell_Extra_Data getTaxInSellExtra(Cursor cursor) {
        Gson gson = new Gson();
        TaxInSell_Extra_Data taxInSell_extra_data = new TaxInSell_Extra_Data();
        try {
            taxInSell_extra_data = gson.fromJson(cursor.getString(cursor.getColumnIndex(DbSchema.ExtraDataSchema.COLUMN_Data)), TaxInSell_Extra_Data.class);
        } catch (JsonSyntaxException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return taxInSell_extra_data;
    }

    @NonNull
    private Reasons getReasonFromCursor(Cursor cursor) {
        Reasons reason = new Reasons();
        reason.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_ID)));
        reason.setReturnReasonCode(cursor.getInt(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_ReturnReasonCode)));
        reason.setReturnReasonId(cursor.getInt(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_ReturnReasonId)));
        reason.setReturnReasonClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_ReturnReasonClientId)));
        reason.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_DataHash)));
        reason.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_UpdateDate)));
        reason.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_CreateDate)));
        reason.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_CreateSyncId)));
        reason.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_UpdateSyncId)));
        reason.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_RowVersion)));
        reason.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_MAHAK_ID)));
        reason.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_DATABASE_ID)));
        reason.setType(cursor.getInt(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_TYPE)));
        reason.setDescription(cursor.getString(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_DESCRIPTION)));
        reason.setName(cursor.getString(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_NAME)));
        return reason;
    }

    private ProductCategory productCategoryFromCursor(Cursor cursor) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.ProductCategorySchema.COLUMN_ID)));
        productCategory.setCategoryCode(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductCategorySchema.COLUMN_CategoryCode)));
        productCategory.setProductCode(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductCategorySchema.COLUMN_ProductCode)));
        productCategory.setUserId(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductCategorySchema.COLUMN_USER_ID)));
        return productCategory;
    }

    private PersonCategory personCategoryFromCursor(Cursor cursor) {
        PersonCategory personCategory = new PersonCategory();
        personCategory.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.PersonCategorySchema.COLUMN_ID)));
        personCategory.setCategoryCode(cursor.getInt(cursor.getColumnIndex(DbSchema.PersonCategorySchema.COLUMN_CategoryCode)));
        personCategory.setPersonCode(cursor.getInt(cursor.getColumnIndex(DbSchema.PersonCategorySchema.COLUMN_PersonCode)));
        personCategory.setUserId(cursor.getInt(cursor.getColumnIndex(DbSchema.PersonCategorySchema.COLUMN_USER_ID)));
        return personCategory;
    }

    private Category categoryFromCursor(Cursor cursor) {
        Category category = new Category();
        category.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.CategorySchema.COLUMN_ID)));
        category.setCategoryCode(cursor.getInt(cursor.getColumnIndex(DbSchema.CategorySchema.COLUMN_CategoryCode)));
        category.setParentCode(cursor.getInt(cursor.getColumnIndex(DbSchema.CategorySchema.COLUMN_ParentCode)));
        category.setCategoryName(cursor.getString(cursor.getColumnIndex(DbSchema.CategorySchema.COLUMN_CategoryName)));
        return category;
    }

    @NonNull
    private ProductDetail getProductDetailFromCursor(Cursor cursor) {
        ProductDetail productDetail = new ProductDetail();
        productDetail.setProductDetailId(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_ProductDetailId)));
        productDetail.setProductDetailClientId(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_ProductDetailClientId)));
        productDetail.setProductId(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_ProductId)));
        productDetail.setProperties(cursor.getString(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Properties)));
        productDetail.setCount1(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Count1)));
        productDetail.setCount2(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Count2)));
        productDetail.setBarcode(cursor.getString(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Barcode)));

        productDetail.setDefaultSellPriceLevel(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_DefaultSellPriceLevel)));
        productDetail.setDiscount(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Discount)));
        productDetail.setSerials(cursor.getString(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Serials)));
        productDetail.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_DataHash)));
        productDetail.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_CreateDate)));
        productDetail.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_UpdateDate)));
        productDetail.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_CreateSyncId)));
        productDetail.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_UpdateSyncId)));
        productDetail.setRowVersion(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_RowVersion)));

        productDetail.setDiscount1(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Discount1)));
        productDetail.setDiscount2(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Discount2)));
        productDetail.setDiscount3(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Discount3)));
        productDetail.setDiscount4(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Discount4)));

        productDetail.setDefaultDiscountLevel(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_DefaultDiscountLevel)));
        productDetail.setDiscountType(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_DiscountType)));
        productDetail.setCustomerPrice(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_CustomerPrice)));
        productDetail.setDeleted(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Deleted)));

        double price1 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price1));
        double price2 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price2));
        double price3 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price3));
        double price4 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price4));
        double price5 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price5));
        double price6 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price6));
        double price7 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price7));
        double price8 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price8));
        double price9 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price9));
        double price10 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price10));

        productDetail.setPrice1(price1);
        productDetail.setPrice2(price2);
        productDetail.setPrice3(price3);
        productDetail.setPrice4(price4);
        productDetail.setPrice5(price5);
        productDetail.setPrice6(price6);
        productDetail.setPrice7(price7);
        productDetail.setPrice8(price8);
        productDetail.setPrice9(price9);
        productDetail.setPrice10(price10);

        Product product = GetProductWithProductId(productDetail.getProductId());
        if (getTaxInSellExtra(product.getProductCode()).getTaxInSellCost()) {
            setProductDetailPrice(cursor, productDetail);
        }

        return productDetail;
    }

    private VisitorProduct getVisitorProductFromCursor(Cursor cursor) {
        VisitorProduct visitorProduct = new VisitorProduct();
        visitorProduct.setProductDetailId(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorProductSchema.COLUMN_ProductDetailId)));
        visitorProduct.setVisitorProductId(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorProductSchema.COLUMN_VisitorProductId)));
        visitorProduct.setVisitorId(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorProductSchema.COLUMN_USER_ID)));
        visitorProduct.setCount1(cursor.getDouble(cursor.getColumnIndex(DbSchema.VisitorProductSchema.COLUMN_Count1)));
        visitorProduct.setCount2(cursor.getDouble(cursor.getColumnIndex(DbSchema.VisitorProductSchema.COLUMN_Count2)));
        visitorProduct.setPrice(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorProductSchema.COLUMN_Price)));
        visitorProduct.setSerials(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorProductSchema.COLUMN_Serials)));
        visitorProduct.setDelete(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorProductSchema.COLUMN_Deleted)));
        visitorProduct.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorProductSchema.COLUMN_DataHash)));
        visitorProduct.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorProductSchema.COLUMN_CreateDate)));
        visitorProduct.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorProductSchema.COLUMN_UpdateDate)));
        visitorProduct.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorProductSchema.COLUMN_CreateSyncId)));
        visitorProduct.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorProductSchema.COLUMN_UpdateSyncId)));
        visitorProduct.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorProductSchema.COLUMN_RowVersion)));
        return visitorProduct;
    }

    private void setProductDetailPrice(Cursor cursor, ProductDetail productDetail) {

        double taxPercent = GetTaxPercentWithProductId(productDetail.getProductId());
        double chargePercent = GetChargePercentWithProductId(productDetail.getProductId());
        double discountPercent = getDiscountFromDiscountLevel(productDetail.getDefaultDiscountLevel(), productDetail);
        Product product = GetProductWithProductId(productDetail.getProductId());

        double price1 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price1));
        double price2 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price2));
        double price3 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price3));
        double price4 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price4));
        double price5 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price5));
        double price6 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price6));
        double price7 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price7));
        double price8 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price8));
        double price9 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price9));
        double price10 = cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price10));


        double roundPrice1 = Math.round(price1 / ((1 + taxPercent + chargePercent) * (1 - discountPercent)));
        double roundPrice2 = Math.round(price2 / ((1 + taxPercent + chargePercent) * (1 - discountPercent)));
        double roundPrice3 = Math.round(price3 / ((1 + taxPercent + chargePercent) * (1 - discountPercent)));
        double roundPrice4 = Math.round(price4 / ((1 + taxPercent + chargePercent) * (1 - discountPercent)));
        double roundPrice5 = Math.round(price5 / ((1 + taxPercent + chargePercent) * (1 - discountPercent)));
        double roundPrice6 = Math.round(price6 / ((1 + taxPercent + chargePercent) * (1 - discountPercent)));
        double roundPrice7 = Math.round(price7 / ((1 + taxPercent + chargePercent) * (1 - discountPercent)));
        double roundPrice8 = Math.round(price8 / ((1 + taxPercent + chargePercent) * (1 - discountPercent)));
        double roundPrice9 = Math.round(price9 / ((1 + taxPercent + chargePercent) * (1 - discountPercent)));
        double roundPrice10 = Math.round(price10 / ((1 + taxPercent + chargePercent) * (1 - discountPercent)));

        if (productDetail.getDiscount() > 0) {
            if ((roundPrice1 - productDetail.getDiscount()) * (1 + taxPercent + chargePercent) < price1)
                roundPrice1 = roundPrice1 + 1;
            if ((roundPrice2 - productDetail.getDiscount()) * (1 + taxPercent + chargePercent) < price2)
                roundPrice2 = roundPrice2 + 1;
            if ((roundPrice3 - productDetail.getDiscount()) * (1 + taxPercent + chargePercent) < price3)
                roundPrice3 = roundPrice3 + 1;
            if ((roundPrice4 - productDetail.getDiscount()) * (1 + taxPercent + chargePercent) < price4)
                roundPrice4 = roundPrice4 + 1;
            if ((roundPrice5 - productDetail.getDiscount()) * (1 + taxPercent + chargePercent) < price5)
                roundPrice5 = roundPrice5 + 1;
            if ((roundPrice6 - productDetail.getDiscount()) * (1 + taxPercent + chargePercent) < price6)
                roundPrice6 = roundPrice6 + 1;
            if ((roundPrice7 - productDetail.getDiscount()) * (1 + taxPercent + chargePercent) < price7)
                roundPrice7 = roundPrice7 + 1;
            if ((roundPrice8 - productDetail.getDiscount()) * (1 + taxPercent + chargePercent) < price8)
                roundPrice8 = roundPrice8 + 1;
            if ((roundPrice9 - productDetail.getDiscount()) * (1 + taxPercent + chargePercent) < price9)
                roundPrice9 = roundPrice9 + 1;
            if ((roundPrice10 - productDetail.getDiscount()) * (1 + taxPercent + chargePercent) < price10)
                roundPrice10 = roundPrice10 + 1;
        } else {

            if ((roundPrice1 * (1 - discountPercent)) * (1 + taxPercent + chargePercent) < price1)
                roundPrice1 = roundPrice1 + 1;
            if ((roundPrice2 * (1 - discountPercent)) * (1 + taxPercent + chargePercent) < price2)
                roundPrice2 = roundPrice2 + 1;
            if ((roundPrice3 * (1 - discountPercent)) * (1 + taxPercent + chargePercent) < price3)
                roundPrice3 = roundPrice3 + 1;
            if ((roundPrice4 * (1 - discountPercent)) * (1 + taxPercent + chargePercent) < price4)
                roundPrice4 = roundPrice4 + 1;
            if ((roundPrice5 * (1 - discountPercent)) * (1 + taxPercent + chargePercent) < price5)
                roundPrice5 = roundPrice5 + 1;
            if ((roundPrice6 * (1 - discountPercent)) * (1 + taxPercent + chargePercent) < price6)
                roundPrice6 = roundPrice6 + 1;
            if ((roundPrice7 * (1 - discountPercent)) * (1 + taxPercent + chargePercent) < price7)
                roundPrice7 = roundPrice7 + 1;
            if ((roundPrice8 * (1 - discountPercent)) * (1 + taxPercent + chargePercent) < price8)
                roundPrice8 = roundPrice8 + 1;
            if ((roundPrice9 * (1 - discountPercent)) * (1 + taxPercent + chargePercent) < price9)
                roundPrice9 = roundPrice9 + 1;
            if ((roundPrice10 * (1 - discountPercent)) * (1 + taxPercent + chargePercent) < price10)
                roundPrice10 = roundPrice10 + 1;
        }

        productDetail.setPrice1(roundPrice1);
        productDetail.setPrice2(roundPrice2);
        productDetail.setPrice3(roundPrice3);
        productDetail.setPrice4(roundPrice4);
        productDetail.setPrice5(roundPrice5);
        productDetail.setPrice6(roundPrice6);
        productDetail.setPrice7(roundPrice7);
        productDetail.setPrice8(roundPrice8);
        productDetail.setPrice9(roundPrice9);
        productDetail.setPrice10(roundPrice10);
    }

    private ProductDetail getProductDetailForUpdate(Cursor cursor) {
        ProductDetail productDetail = new ProductDetail();
        productDetail.setProductDetailId(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_ProductDetailId)));
        productDetail.setProductDetailClientId(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_ProductDetailClientId)));
        productDetail.setProductId(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_ProductId)));
        productDetail.setProperties(cursor.getString(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Properties)));
        productDetail.setCount1(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Count1)));
        productDetail.setCount2(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Count2)));
        productDetail.setBarcode(cursor.getString(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Barcode)));

        productDetail.setPrice1(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price1)));
        productDetail.setPrice2(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price2)));
        productDetail.setPrice3(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price3)));
        productDetail.setPrice4(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price4)));
        productDetail.setPrice5(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price5)));
        productDetail.setPrice6(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price6)));
        productDetail.setPrice7(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price7)));
        productDetail.setPrice8(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price8)));
        productDetail.setPrice9(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price9)));
        productDetail.setPrice10(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Price10)));

        productDetail.setDefaultSellPriceLevel(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_DefaultSellPriceLevel)));
        productDetail.setDiscount(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Discount)));
        productDetail.setSerials(cursor.getString(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Serials)));
        productDetail.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_DataHash)));
        productDetail.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_CreateDate)));
        productDetail.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_UpdateDate)));
        productDetail.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_CreateSyncId)));
        productDetail.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_UpdateSyncId)));
        productDetail.setRowVersion(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_RowVersion)));

        productDetail.setDiscount1(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Discount1)));
        productDetail.setDiscount2(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Discount2)));
        productDetail.setDiscount3(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Discount3)));
        productDetail.setDiscount4(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Discount4)));

        productDetail.setDefaultDiscountLevel(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_DefaultDiscountLevel)));
        productDetail.setDiscountType(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_DiscountType)));
        productDetail.setCustomerPrice(cursor.getDouble(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_CustomerPrice)));
        productDetail.setDeleted(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Deleted)));


        return productDetail;
    }

    private double getDiscountFromDiscountLevel(int defaultLevel, ProductDetail productDetail) {
        switch (defaultLevel) {
            case 1:
                return (productDetail.getDiscount1() / 100);
            case 2:
                return (productDetail.getDiscount2() / 100);
            case 3:
                return (productDetail.getDiscount3() / 100);
            case 4:
                return (productDetail.getDiscount4() / 100);
        }
        return 0;
    }

    private double GetTaxPercentWithProductId(int productId) {

        Product product = GetProductWithProductId(productId);

        if (product.getTaxPercent() == -1)
            return 0;
        else if (product.getTaxPercent() == 0)
            return (RegulartoDouble(BaseActivity.getPrefTaxPercent()) / 100);
        else
            return (product.getTaxPercent() / 100);

    }

    private double GetChargePercentWithProductId(int productId) {
        Product product = GetProductWithProductId(productId);
        if (product.getChargePercent() == -1)
            return 0;
        else if (product.getChargePercent() == 0)
            return (RegulartoDouble(BaseActivity.getPrefChargePercent()) / 100);
        else
            return (product.getChargePercent() / 100);

    }

    public PicturesProduct getPictureProductByMasterIdAndDataBaseIdAndPictureId(String masterId, String dataBaseId , String fileName) {
        PicturesProduct picturesProduct = new PicturesProduct();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.PicturesProductSchema.TABLE_NAME, null, DbSchema.PicturesProductSchema.COLUMN_PictureCode + "=? And " + DbSchema.PicturesProductSchema.COLUMN_DATABASE_ID + " =? And " + DbSchema.PicturesProductSchema.COLUMN_FILE_NAME + " =? " , new String[]{masterId, dataBaseId , fileName}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    picturesProduct = getPictureProductFromCursor(cursor);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getPictureProductB", e.getMessage());
        }
        return picturesProduct;
    }

    public PicturesProduct getPictureWithPictureId(long pictureId) {
        PicturesProduct picturesProduct = new PicturesProduct();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.PicturesProductSchema.TABLE_NAME, null, DbSchema.PicturesProductSchema.COLUMN_PICTURE_ID + " =? ", new String[]{String.valueOf(pictureId)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    picturesProduct = getPictureProductFromCursor(cursor);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getPictureWithPictureId", e.getMessage());
        }
        return picturesProduct;
    }

    public long getPictureIdWithFileName(String fileName) {
        PicturesProduct picturesProduct = new PicturesProduct();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.PicturesProductSchema.TABLE_NAME, null, DbSchema.PicturesProductSchema.COLUMN_FILE_NAME + " =? ", new String[]{fileName}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    picturesProduct = getPictureProductFromCursor(cursor);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getPictureIdWith", e.getMessage());
        }
        return picturesProduct.getPictureId();
    }

    private ProductGroup getProductGroupFromCursor(Cursor cursor) {
        ProductGroup productGroup = new ProductGroup();
        try {
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    productGroup.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.ProductGroupSchema.COLUMN_ID)));
                    productGroup.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.ProductGroupSchema.COLUMN_MAHAK_ID)));
                    productGroup.setProductGroupCode(cursor.getLong(cursor.getColumnIndex(DbSchema.ProductGroupSchema.COLUMN_ProductGroupCode)));
                    productGroup.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.ProductGroupSchema.COLUMN_DATABASE_ID)));
                    productGroup.setParentId(cursor.getLong(cursor.getColumnIndex(DbSchema.ProductGroupSchema.COLUMN_PARENTID)));
                    productGroup.setName(cursor.getString(cursor.getColumnIndex(DbSchema.ProductGroupSchema.COLUMN_NAME)));
                    productGroup.setColor(cursor.getString(cursor.getColumnIndex(DbSchema.ProductGroupSchema.COLUMN_COLOR)));
                    productGroup.setIcon(cursor.getString(cursor.getColumnIndex(DbSchema.ProductGroupSchema.COLUMN_ICON)));
                    productGroup.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.ProductGroupSchema.COLUMN_MODIFYDATE)));
                    productGroup.setProductCategoryClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.ProductGroupSchema.COLUMN_ProductGroupClientId)));
                    productGroup.setProductCategoryId(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductGroupSchema.COLUMN_ProductGroupId)));
                }
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getProductGror", e.getMessage());
        }
        return productGroup;
    }

    public ProductGroup GetPromoGroup(long GroupCode) {
        ProductGroup productGroup = new ProductGroup();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.ProductGroupSchema.TABLE_NAME, null, DbSchema.ProductGroupSchema.COLUMN_ProductGroupCode + "=?", new String[]{String.valueOf(GroupCode)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    productGroup = getProductGroupFromCursor(cursor);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("GetPromoGroup", e.getMessage());
        }
        return productGroup;
    }

    public ProductGroup getGroup(long GroupId) {
        ProductGroup productGroup = new ProductGroup();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.ProductGroupSchema.TABLE_NAME, null, DbSchema.ProductGroupSchema.COLUMN_ProductGroupId + "=?", new String[]{String.valueOf(GroupId)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    productGroup = getProductGroupFromCursor(cursor);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getGroup", e.getMessage());
        }
        return productGroup;
    }

    private ProductPriceLevelName GetPriceLevelName(long id) {
        ProductPriceLevelName productPriceLevelName = new ProductPriceLevelName();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.PriceLevelNameSchema.TABLE_NAME, null, DbSchema.PriceLevelNameSchema._ID + "=? And " + DbSchema.PriceLevelNameSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(id), String.valueOf(getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    productPriceLevelName.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.PriceLevelNameSchema._ID)));
                    productPriceLevelName.setSyncID(cursor.getString(cursor.getColumnIndex(DbSchema.PriceLevelNameSchema._SyncId)));
                    productPriceLevelName.setUserId(cursor.getLong(cursor.getColumnIndex(DbSchema.PriceLevelNameSchema.COLUMN_USER_ID)));
                    productPriceLevelName.setPriceLevelName(cursor.getString(cursor.getColumnIndex(DbSchema.PriceLevelNameSchema.PRICE_LEVEL_NAME)));
                    productPriceLevelName.setPriceLevelCode(cursor.getInt(cursor.getColumnIndex(DbSchema.PriceLevelNameSchema.PRICE_LEVEL_CODE)));
                    productPriceLevelName.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.PriceLevelNameSchema._MODIFY_DATE)));

                    productPriceLevelName.setCostLevelNameId(cursor.getInt(cursor.getColumnIndex(DbSchema.PriceLevelNameSchema.CostLevelNameId)));
                    productPriceLevelName.setCostLevelNameClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.PriceLevelNameSchema.CostLevelNameClientId)));
                    productPriceLevelName.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.PriceLevelNameSchema.DataHash)));
                    productPriceLevelName.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.PriceLevelNameSchema.UpdateDate)));
                    productPriceLevelName.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.PriceLevelNameSchema.CreateDate)));
                    productPriceLevelName.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.PriceLevelNameSchema.CreateSyncId)));
                    productPriceLevelName.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.PriceLevelNameSchema.UpdateSyncId)));
                    productPriceLevelName.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.PriceLevelNameSchema.RowVersion)));

                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("productPriceLevelName", e.getMessage());
        }
        return productPriceLevelName;
    }

    public Reasons GetReason(int reasonCode) {
        Reasons reason = new Reasons();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.ReasonsSchema.TABLE_NAME, null, DbSchema.ReasonsSchema.COLUMN_ReturnReasonCode + "=? ", new String[]{String.valueOf(reasonCode)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    reason.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_ID)));
                    reason.setReturnReasonCode(cursor.getInt(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_ReturnReasonCode)));
                    reason.setReturnReasonId(cursor.getInt(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_ReturnReasonId)));
                    reason.setReturnReasonClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_ReturnReasonClientId)));
                    reason.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_DataHash)));
                    reason.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_UpdateDate)));
                    reason.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_CreateDate)));
                    reason.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_CreateSyncId)));
                    reason.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_UpdateSyncId)));
                    reason.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_RowVersion)));
                    reason.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_MAHAK_ID)));
                    reason.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_DATABASE_ID)));
                    reason.setType(cursor.getInt(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_TYPE)));
                    reason.setDescription(cursor.getString(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_DESCRIPTION)));
                    reason.setName(cursor.getString(cursor.getColumnIndex(DbSchema.ReasonsSchema.COLUMN_NAME)));
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetCategory", e.getMessage());
        }
        return reason;
    }

    public CustomerGroup GetCustomerGroup(long id) {
        CustomerGroup customergroup = new CustomerGroup();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.CustomersGroupSchema.TABLE_NAME, null, DbSchema.CustomersGroupSchema.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {

                    customergroup.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_ID)));
                    customergroup.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_MAHAK_ID)));
                    customergroup.setPersonGroupCode(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_PersonGroupCode)));
                    customergroup.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_DATABASE_ID)));
                    customergroup.setName(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_NAME)));
                    customergroup.setIcon(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_ICON)));
                    customergroup.setColor(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_COLOR)));
                    customergroup.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_MODIFYDATE)));
                    customergroup.setPersonGroupCode(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_PersonGroupCode)));
                    customergroup.setPersonGroupId(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_PersonGroupId)));
                    customergroup.setPersonGroupClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_PersonGroupClientId)));
                    customergroup.setDiscountPercent(cursor.getDouble(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_DiscountPercent)));
                    customergroup.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_DataHash)));
                    customergroup.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_CreateDate)));
                    customergroup.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_UpdateDate)));
                    customergroup.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_CreateSyncId)));
                    customergroup.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_UpdateSyncId)));
                    customergroup.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_RowVersion)));
                    customergroup.setSellPriceLevel(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_SellPriceLevel)));

                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetCustomerGroup", e.getMessage());
        }
        return customergroup;
    }

    public CustomerGroup GetCustomerGroupWithGroupId(long id) {
        CustomerGroup customergroup = new CustomerGroup();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.CustomersGroupSchema.TABLE_NAME, null, DbSchema.CustomersGroupSchema.COLUMN_PersonGroupId + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {

                    customergroup.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_ID)));
                    customergroup.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_MAHAK_ID)));
                    customergroup.setPersonGroupCode(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_PersonGroupCode)));
                    customergroup.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_DATABASE_ID)));
                    customergroup.setName(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_NAME)));
                    customergroup.setIcon(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_ICON)));
                    customergroup.setColor(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_COLOR)));
                    customergroup.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_MODIFYDATE)));
                    customergroup.setPersonGroupCode(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_PersonGroupCode)));
                    customergroup.setPersonGroupId(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_PersonGroupId)));
                    customergroup.setPersonGroupClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_PersonGroupClientId)));
                    customergroup.setDiscountPercent(cursor.getDouble(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_DiscountPercent)));
                    customergroup.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_DataHash)));
                    customergroup.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_CreateDate)));
                    customergroup.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_UpdateDate)));
                    customergroup.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_CreateSyncId)));
                    customergroup.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_UpdateSyncId)));
                    customergroup.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_RowVersion)));
                    customergroup.setSellPriceLevel(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_SellPriceLevel)));

                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetCustomerGroup", e.getMessage());
        }
        return customergroup;
    }

    public CustomerGroup GetPromoCustomerGroup(long id) {
        CustomerGroup customergroup = new CustomerGroup();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.CustomersGroupSchema.TABLE_NAME, null, DbSchema.CustomersGroupSchema.COLUMN_PersonGroupCode + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    customergroup.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_ID)));
                    customergroup.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_MAHAK_ID)));
                    customergroup.setPersonGroupCode(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_PersonGroupCode)));
                    customergroup.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_DATABASE_ID)));
                    customergroup.setName(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_NAME)));
                    customergroup.setIcon(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_ICON)));
                    customergroup.setColor(cursor.getInt(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_COLOR)));
                    customergroup.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_MODIFYDATE)));
                    customergroup.setSellPriceLevel(cursor.getString(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_SellPriceLevel)));
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetCustomerGroup", e.getMessage());
        }
        return customergroup;
    }

    public Receipt GetReceipt(long id) {
        Receipt receipt = new Receipt();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.ReceiptSchema.TABLE_NAME, null, DbSchema.ReceiptSchema.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    receipt.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_ID)));
                    receipt.setPersonId(cursor.getInt(cursor.getColumnIndex(DbSchema.ReceiptSchema.PERSON_ID)));
                    receipt.setPersonClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_PersonClientId)));
                    receipt.setVisitorId(cursor.getLong(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_USER_ID)));
                    receipt.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_MAHAK_ID)));
                    receipt.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_DATABASE_ID)));
                    receipt.setReceiptCode(cursor.getLong(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_ReceiptCode)));
                    receipt.setCashAmount(cursor.getDouble(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_CASHAMOUNT)));
                    receipt.setDescription(cursor.getString(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_DESCRIPTION)));
                    receipt.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_MODIFYDATE)));
                    receipt.setDate(cursor.getLong(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_DATE)));
                    receipt.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_PUBLISH)));
                    receipt.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_MAHAK_ID)));
                    receipt.setTrackingCode(cursor.getString(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_CODE)));
                    receipt.setReceiptClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_ReceiptClientId)));
                    receipt.setReceiptId(cursor.getInt(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_ReceiptId)));
                    receipt.setCashCode(cursor.getString(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_CashCode)));

                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetReceipt", e.getMessage());
        }
        return receipt;
    }

    public PayableTransfer GetPayable(long id) {
        PayableTransfer payableTransfer = new PayableTransfer();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.PayableSchema.TABLE_NAME, null, DbSchema.PayableSchema.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {

                    payableTransfer.setId(cursor.getInt(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_ID)));
                    payableTransfer.setTransferCode(cursor.getLong(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_TransferCode)));
                    payableTransfer.setPayerId(cursor.getInt(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_PayerId)));
                    payableTransfer.setPrice(cursor.getInt(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_Price)));
                    payableTransfer.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_PUBLISH)));
                    payableTransfer.setTransferType(cursor.getInt(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_TransferType)));
                    payableTransfer.setTransferDate(cursor.getLong(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_TransferDate)));
                    payableTransfer.setReceiverId(cursor.getInt(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_Receiverid)));
                    payableTransfer.setDescription(cursor.getString(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_Comment)));
                    payableTransfer.setVisitorId(cursor.getInt(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_VisitorId)));
                    payableTransfer.setDataBaseId(cursor.getString(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_DatabaseId)));
                    payableTransfer.setUserId(cursor.getLong(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_USER_ID)));
                    payableTransfer.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_MahakId)));

                    payableTransfer.setTransferAccountId(cursor.getInt(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_TransferAccountId)));
                    payableTransfer.setTransferAccountClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_TransferAccountClientId)));
                    payableTransfer.setTransferAccountCode(cursor.getInt(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_TransferAccountCode)));
                    payableTransfer.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_DataHash)));
                    payableTransfer.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_CreateDate)));
                    payableTransfer.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_UpdateDate)));
                    payableTransfer.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_CreateSyncId)));
                    payableTransfer.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_UpdateSyncId)));
                    payableTransfer.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_RowVersion)));


                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetReceipt", e.getMessage());
        }
        return payableTransfer;
    }

    private OrderDetail GetProductInOrder(long id) {
        OrderDetail orderDetail = new OrderDetail();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.OrderDetailSchema.TABLE_NAME, null, DbSchema.OrderDetailSchema.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    orderDetail.setId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ID)));
                    orderDetail.setOrderId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_OrderId)));
                    orderDetail.setProductDetailId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ProductDetailId)));
                    orderDetail.setProductId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ProductId)));
                    orderDetail.setCount1(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Count1)));
                    orderDetail.setCount2(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Count2)));
                    orderDetail.setSumCountBaJoz(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz)));
                    // orderDetail.setCount2(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_PackageCount)));
                    orderDetail.setPrice(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Price)));
                    orderDetail.setGiftType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_GiftType)));
                    orderDetail.setDescription(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Description)));
                    //orderDetail.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_PUBLISH)));
                    orderDetail.setTaxPercent(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_TaxPercent)));
                    orderDetail.setChargePercent(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ChargePercent)));
                    orderDetail.setDiscount(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Discount)));
                    orderDetail.setDiscountType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_DiscountType)));
                    orderDetail.setOrderDetailClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_OrderDetailClientId)));
                    orderDetail.setOrderClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_OrderClientId)));
                    orderDetail.setCostLevel(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_CostLevel)));
                    // orderDetail.setFixedOff(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Fixed_Off)));
                    // orderDetail.setCostLevel(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Price_LEVEL)));
                    orderDetail.setPromotionCode(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_PROMOTION_CODE)));
                    //   orderDetail.setGiftType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_GIFT_TYPE)));
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetProductInOrder", e.getMessage());
        }
        return orderDetail;
    }

    public OrderDetail GetOrderDetailWithId(long id) {

        double d = ServiceTools.RegulartoDouble(BaseActivity.getPrefRowDiscountIsActive());

        OrderDetail orderDetail = new OrderDetail();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.OrderDetailSchema.TABLE_NAME, null, DbSchema.OrderDetailSchema.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    orderDetail.setId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ID)));
                    orderDetail.setOrderId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_OrderId)));
                    orderDetail.setOrderDetailId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_OrderDetailId)));
                    orderDetail.setOrderDetailClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_OrderDetailClientId)));
                    orderDetail.setOrderClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_OrderClientId)));
                    orderDetail.setProductDetailId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ProductDetailId)));
                    orderDetail.setProductId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ProductId)));
                    if (BaseActivity.getPrefUnit2Setting(mContext) == MODE_MeghdarJoz)
                        orderDetail.setCount1(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz)));
                    else
                        orderDetail.setCount1(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Count1)));
                    orderDetail.setCount2(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Count2)));
                    orderDetail.setSumCountBaJoz(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz)));
                    // orderDetail.setCount2(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_PackageCount)));
                    orderDetail.setPrice(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Price)));
                    orderDetail.setGiftType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_GiftType)));
                    orderDetail.setDescription(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Description)));
                    //orderDetail.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_PUBLISH)));
                    orderDetail.setTaxPercent(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_TaxPercent)));
                    orderDetail.setChargePercent(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ChargePercent)));
                    orderDetail.setDiscount(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Discount)));
                    if (d == 1)
                        orderDetail.setDiscount(ServiceTools.getRowOffPercent(orderDetail.getDiscount(), orderDetail.getPrice(), orderDetail.getSumCountBaJoz()));
                    orderDetail.setDiscountType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_DiscountType)));
                    orderDetail.setCostLevel(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_CostLevel)));
                    // orderDetail.setFixedOff(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Fixed_Off)));
                    // orderDetail.setCostLevel(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Price_LEVEL)));
                    orderDetail.setPromotionCode(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_PROMOTION_CODE)));
                    //   orderDetail.setGiftType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_GIFT_TYPE)));
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetProductInOrder", e.getMessage());
        }
        return orderDetail;
    }

    public OrderDetailProperty GetOrderDetailProperty(long id) {
        OrderDetailProperty orderDetailProperty = new OrderDetailProperty();
        Cursor cursor;
        int position = 0;
        try {
            cursor = mDb.query(DbSchema.OrderDetailPropertySchema.TABLE_NAME, null, DbSchema.OrderDetailPropertySchema.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    position++;
                    orderDetailProperty.setId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailPropertySchema.COLUMN_ID)));
                    orderDetailProperty.setOrderId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailPropertySchema.COLUMN_OrderId)));
                    orderDetailProperty.setProductDetailId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailPropertySchema.COLUMN_ProductDetailId)));
                    orderDetailProperty.setOrderDetailClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderDetailPropertySchema.COLUMN_OrderDetailClientId)));
                    orderDetailProperty.setProductId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailPropertySchema.COLUMN_ProductId)));
                    orderDetailProperty.setCount1(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailPropertySchema.COLUMN_Count1)));
                    orderDetailProperty.setCount2(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailPropertySchema.COLUMN_Count2)));
                    orderDetailProperty.setProductSpec(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailPropertySchema.COLUMN_ProductSpec)));
                    orderDetailProperty.setSumCountBaJoz(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailPropertySchema.COLUMN_SumCountBaJoz)));
                    orderDetailProperty.setPosition(position);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorOrdrDetProp", e.getMessage());
        }
        return orderDetailProperty;
    }

    public OrderDetail OrderdetailWithproductDetailId(long id, int productId) {
        OrderDetail orderDetail = new OrderDetail();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.OrderDetailSchema.TABLE_NAME, null, DbSchema.OrderDetailSchema.COLUMN_OrderId + "=? And " + DbSchema.OrderDetailSchema.COLUMN_ProductId + " =? ", new String[]{String.valueOf(id), String.valueOf(productId)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    orderDetail.setId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ID)));
                    orderDetail.setOrderId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_OrderId)));
                    orderDetail.setOrderDetailClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_OrderDetailClientId)));
                    orderDetail.setOrderClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_OrderClientId)));
                    orderDetail.setProductDetailId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ProductDetailId)));
                    orderDetail.setProductId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ProductId)));
                    orderDetail.setCount1(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Count1)));
                    orderDetail.setCount2(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Count2)));
                    orderDetail.setSumCountBaJoz(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz)));
                    // orderDetail.setCount2(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_PackageCount)));
                    orderDetail.setPrice(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Price)));
                    orderDetail.setGiftType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_GiftType)));
                    orderDetail.setDescription(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Description)));
                    //orderDetail.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_PUBLISH)));
                    orderDetail.setTaxPercent(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_TaxPercent)));
                    orderDetail.setChargePercent(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ChargePercent)));
                    orderDetail.setDiscount(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Discount)));
                    orderDetail.setDiscountType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_DiscountType)));
                    orderDetail.setCostLevel(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_CostLevel)));
                    // orderDetail.setFixedOff(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Fixed_Off)));
                    // orderDetail.setCostLevel(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Price_LEVEL)));
                    orderDetail.setPromotionCode(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_PROMOTION_CODE)));
                    //   orderDetail.setGiftType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_GIFT_TYPE)));
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorOrderdetail", e.getMessage());
        }
        return orderDetail;
    }

    public Order GetOrder(long id) {
        Order order = new Order();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null, DbSchema.OrderSchema.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    order.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID)));
                    order.setPersonId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PersonId)));

                    order.setLatitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_LATITUDE)));
                    order.setLongitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_LONGITUDE)));

                    order.setReturnReasonId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ReturnReasonId)));
                    order.setPersonClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PersonClientId)));
                    order.setVisitorId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_USER_ID)));
                    order.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_MAHAK_ID)));
                    order.setOrderCode(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OrderCode)));
                    order.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DATABASE_ID)));
                    order.setDeliveryDate(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DELIVERYDATE)));
                    order.setOrderDate(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ORDERDATE)));
                    order.setDiscount(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DISCOUNT)));
                    order.setDescription((cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DESCRIPTION))));
                    order.setImmediate(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_IMMEDIATE)));
                    order.setSettlementType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_SETTLEMEMNTTYPE)));
                    order.setOrderType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_TYPE)));
                    order.setCode(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_CODE)));
                    order.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_MODIFYDATE)));
                    order.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PUBLISH)));
                    order.setPromotionCode(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PROMOTION_CODE)));
                    order.setGiftType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_GIFT_TYPE)));
                    order.setOrderId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OrderId)));
                    order.setOrderClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OrderClientId)));
                    order.setReceiptId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ReceiptId)));
                    order.setReceiptClientId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ReceiptId)));
                    order.setSendCost(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_SendCost)));
                    order.setOtherCost(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OtherCost)));
                    order.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DataHash)));
                    order.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_CreateDate)));
                    order.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_UpdateDate)));
                    order.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_CreateSyncId)));
                    order.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_UpdateSyncId)));
                    order.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_RowVersion)));

                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetOrder", e.getMessage());
        }
        return order;
    }

    public Order getOrderFromCursor(Cursor cursor) {
        Order order = new Order();
        try {
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    order.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID)));
                    order.setPersonId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PersonId)));

                    order.setLatitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_LATITUDE)));
                    order.setLongitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_LONGITUDE)));

                    order.setReturnReasonId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ReturnReasonId)));
                    order.setPersonClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PersonClientId)));
                    order.setVisitorId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_USER_ID)));
                    order.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_MAHAK_ID)));
                    order.setOrderCode(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OrderCode)));
                    order.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DATABASE_ID)));
                    order.setDeliveryDate(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DELIVERYDATE)));
                    order.setOrderDate(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ORDERDATE)));
                    order.setDiscount(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DISCOUNT)));
                    order.setDescription((cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DESCRIPTION))));
                    order.setImmediate(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_IMMEDIATE)));
                    order.setSettlementType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_SETTLEMEMNTTYPE)));
                    order.setOrderType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_TYPE)));
                    order.setCode(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_CODE)));
                    order.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_MODIFYDATE)));
                    order.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PUBLISH)));
                    order.setPromotionCode(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PROMOTION_CODE)));
                    order.setGiftType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_GIFT_TYPE)));
                    order.setOrderId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OrderId)));
                    order.setOrderClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OrderClientId)));
                    order.setReceiptId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ReceiptId)));
                    order.setReceiptClientId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ReceiptId)));
                    order.setSendCost(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_SendCost)));
                    order.setOtherCost(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OtherCost)));
                }
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetOrder", e.getMessage());
        }
        return order;
    }

    public Order getOrderFromCursor2(Cursor cursor) {
        Order order = new Order();
        try {
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    order.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID)));
                    order.setCode(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_CODE)));
                    order.setOrderDate(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ORDERDATE)));
                    order.setCustomerName(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_NAME)));
                    order.setAddress(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ADDRESS)));
                    order.setMarketName(cursor.getString(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ORGANIZATION)));
                    order.setPersonClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PersonClientId)));
                    order.setPersonId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PersonId)));
                    order.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PUBLISH)));
                    order.setDiscount(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DISCOUNT)));
                    order.setOrderType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_TYPE)));
                }
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetOrder", e.getMessage());
        }
        return order;
    }

    public Order GetOrderWithOrderClientId(long clientId) {
        Order order = new Order();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null, DbSchema.OrderSchema.COLUMN_OrderClientId + "=?", new String[]{String.valueOf(clientId)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    order.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID)));
                    order.setPersonId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PersonId)));

                    order.setLatitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_LATITUDE)));
                    order.setLongitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_LONGITUDE)));

                    order.setReturnReasonId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ReturnReasonId)));
                    order.setPersonClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PersonClientId)));
                    order.setVisitorId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_USER_ID)));
                    order.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_MAHAK_ID)));
                    order.setOrderCode(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OrderCode)));
                    order.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DATABASE_ID)));
                    order.setDeliveryDate(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DELIVERYDATE)));
                    order.setOrderDate(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ORDERDATE)));
                    order.setDiscount(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DISCOUNT)));
                    order.setDescription((cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DESCRIPTION))));
                    order.setImmediate(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_IMMEDIATE)));
                    order.setSettlementType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_SETTLEMEMNTTYPE)));
                    order.setOrderType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_TYPE)));
                    order.setCode(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_CODE)));
                    order.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_MODIFYDATE)));
                    order.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PUBLISH)));
                    order.setPromotionCode(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PROMOTION_CODE)));
                    order.setGiftType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_GIFT_TYPE)));
                    order.setOrderId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OrderId)));
                    order.setOrderClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OrderClientId)));
                    order.setReceiptId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ReceiptId)));
                    order.setReceiptClientId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ReceiptId)));
                    order.setSendCost(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_SendCost)));
                    order.setOtherCost(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OtherCost)));
                    order.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DataHash)));
                    order.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_CreateDate)));
                    order.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_UpdateDate)));
                    order.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_CreateSyncId)));
                    order.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_UpdateSyncId)));
                    order.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_RowVersion)));

                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetOrder", e.getMessage());
        }
        return order;
    }

    public NonRegister GetNonRegister(long id) {
        NonRegister nonRegister = new NonRegister();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.NonRegisterSchema.TABLE_NAME, null, DbSchema.NonRegisterSchema.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    nonRegister.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_ID)));
                    nonRegister.setPersonId(cursor.getInt(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_PersonId)));
                    nonRegister.setPersonClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_PersonClientId)));
                    nonRegister.setVisitorId(cursor.getInt(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_USER_ID)));
                    nonRegister.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_MAHAK_ID)));
                    nonRegister.setNotRegisterCode(cursor.getInt(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_NotRegisterCode)));
                    nonRegister.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_DATABASE_ID)));
                    nonRegister.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_MODIFYDATE)));
                    nonRegister.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_PUBLISH)));
                    nonRegister.setCode(cursor.getString(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_CODE)));
                    nonRegister.setCustomerName(cursor.getString(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_CustomerName)));
                    nonRegister.setNotRegisterDate(cursor.getLong(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_NonRegister_DATE)));
                    nonRegister.setDescription(cursor.getString(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_DESCRIPTION)));
                    nonRegister.setReasonCode(cursor.getInt(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_ReasonCode)));

                    nonRegister.setNotRegisterId(cursor.getInt(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_NotRegisterId)));
                    nonRegister.setNotRegisterClientId(cursor.getInt(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_NotRegisterClientId)));
                    nonRegister.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_DataHash)));
                    nonRegister.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_CreateDate)));
                    nonRegister.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_UpdateDate)));
                    nonRegister.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_CreateSyncId)));
                    nonRegister.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_UpdateSyncId)));
                    nonRegister.setRowVersion(cursor.getInt(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_RowVersion)));


                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetOrder", e.getMessage());
        }
        return nonRegister;
    }

    public ReceivedTransfers GetReceivedTransfer(String TransferId) {
        ReceivedTransfers receivedTransfers = new ReceivedTransfers();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.ReceivedTransfersSchema.TABLE_NAME, null, DbSchema.ReceivedTransfersSchema.COLUMN_TransferStoreId + "=?", new String[]{TransferId}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {

                    receivedTransfers.setTransferStoreId(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_TransferStoreId)));
                    receivedTransfers.setTransferStoreCode(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_TransferStoreCode)));
                    receivedTransfers.setTransferDate(cursor.getLong(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_TransferDate)));
                    receivedTransfers.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_DatabaseId)));
                    receivedTransfers.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_MahakId)));
                    receivedTransfers.setDescription(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_Description)));
                    receivedTransfers.setCreatedBy(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_CreatedBy)));
                    receivedTransfers.setIsAccepted(cursor.getInt(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_IsAccepted)));
                    receivedTransfers.setModifiedBy(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_ModifiedBy)));
                    receivedTransfers.setSenderVisitorId(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_SenderVisitorId)));
                    receivedTransfers.setReceiverVisitorId(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_ReceiverVisitorId)));
                    receivedTransfers.setSyncId(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_SyncId)));
                    receivedTransfers.setModifyDate(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_ModifyDate)));

                    receivedTransfers.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_DataHash)));
                    receivedTransfers.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_CreateDate)));
                    receivedTransfers.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_UpdateDate)));
                    receivedTransfers.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_CreateSyncId)));
                    receivedTransfers.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_UpdateSyncId)));
                    receivedTransfers.setRowVersion(cursor.getInt(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_RowVersion)));
                    receivedTransfers.setTransferStoreClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_TransferStoreClientId)));
                    receivedTransfers.setSenderStoreCode(cursor.getInt(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_SenderStoreCode)));
                    receivedTransfers.setReceiverStoreCode(cursor.getInt(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_ReceiverStoreCode)));

                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetOrder", e.getMessage());
        }
        return receivedTransfers;
    }

    public ReceivedTransferProducts GetReceivedTransferProduct(String Id) {
        ReceivedTransferProducts receivedTransferProducts = new ReceivedTransferProducts();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.ReceivedTransferProductsSchema.TABLE_NAME, null, DbSchema.ReceivedTransferProductsSchema.COLUMN_TransferStoreDetailId + "=?", new String[]{Id}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {

                    receivedTransferProducts.setCount1(cursor.getDouble(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_Count1)));
                    receivedTransferProducts.setProductName(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_Name)));
                    receivedTransferProducts.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_DatabaseId)));
                    receivedTransferProducts.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_MahakId)));
                    receivedTransferProducts.setDescription(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_Description)));
                    receivedTransferProducts.setCreatedBy(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_CreatedBy)));
                    receivedTransferProducts.setCreatedDate(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_CreatedDate)));
                    receivedTransferProducts.setId(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_Id)));

                    receivedTransferProducts.setModifyDate(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_ModifyDate)));

                    receivedTransferProducts.setTransferStoreDetailId(cursor.getInt(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_TransferStoreDetailId)));
                    receivedTransferProducts.setTransferStoreDetailClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_TransferStoreDetailClientId)));
                    receivedTransferProducts.setTransferStoreId(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_TransferStoreId)));
                    receivedTransferProducts.setProductDetailId(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_ProductDetailId)));
                    receivedTransferProducts.setCount2(cursor.getDouble(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_Count2)));

                    receivedTransferProducts.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_DataHash)));
                    receivedTransferProducts.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_CreateDate)));
                    receivedTransferProducts.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_UpdateDate)));
                    receivedTransferProducts.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_CreateSyncId)));
                    receivedTransferProducts.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_UpdateSyncId)));
                    receivedTransferProducts.setRowVersion(cursor.getInt(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_RowVersion)));


                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetOrder", e.getMessage());
        }
        return receivedTransferProducts;
    }

    public Order GetOrder(String code) {
        Order order = new Order();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null, DbSchema.OrderSchema.COLUMN_CODE + "=?", new String[]{code}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    order.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID)));
                    order.setPersonId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PersonId)));

                    order.setLatitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_LATITUDE)));
                    order.setLongitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_LONGITUDE)));

                    order.setReturnReasonId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ReturnReasonId)));
                    order.setPersonClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PersonClientId)));
                    order.setVisitorId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_USER_ID)));
                    order.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_MAHAK_ID)));
                    order.setOrderCode(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OrderCode)));
                    order.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DATABASE_ID)));
                    order.setDeliveryDate(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DELIVERYDATE)));
                    order.setOrderDate(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ORDERDATE)));
                    order.setDiscount(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DISCOUNT)));
                    order.setDescription((cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DESCRIPTION))));
                    order.setImmediate(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_IMMEDIATE)));
                    order.setSettlementType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_SETTLEMEMNTTYPE)));
                    order.setOrderType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_TYPE)));
                    order.setCode(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_CODE)));
                    order.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_MODIFYDATE)));
                    order.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PUBLISH)));
                    order.setPromotionCode(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PROMOTION_CODE)));
                    order.setGiftType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_GIFT_TYPE)));
                    order.setOrderId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OrderId)));
                    order.setOrderClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OrderClientId)));
                    order.setReceiptId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ReceiptId)));
                    order.setSendCost(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_SendCost)));
                    order.setOtherCost(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OtherCost)));
                    order.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DataHash)));
                    order.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_CreateDate)));
                    order.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_UpdateDate)));
                    order.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_CreateSyncId)));
                    order.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_UpdateSyncId)));
                    order.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_RowVersion)));

                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetOrder", e.getMessage());
        }
        return order;
    }

    private CheckList GetCheckList(long id) {
        CheckList checklist = new CheckList();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.CheckListSchema.TABLE_NAME, null, DbSchema.CheckListSchema.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    checklist.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_ID)));
                    checklist.setPersonId(cursor.getInt(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_CUSTOMERID)));
                    checklist.setUserId(cursor.getLong(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_USER_ID)));
                    checklist.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_MAHAK_ID)));
                    checklist.setChecklistCode(cursor.getLong(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_CHECK_LIST_CODE)));
                    checklist.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_DATABASE_ID)));
                    checklist.setStatus(cursor.getInt(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_STATUS)));
                    checklist.setType(cursor.getInt(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_TYPE)));
                    checklist.setDescription(cursor.getString(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_DESCRIPTION)));
                    checklist.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_MODIFYDATE)));
                    checklist.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_PUBLISH)));
                    checklist.setChecklistId(cursor.getInt(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_ChecklistId)));
                    checklist.setChecklistClientId(cursor.getInt(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_ChecklistClientId)));
                    checklist.setVisitorId(cursor.getInt(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_VisitorId)));
                    checklist.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_DataHash)));
                    checklist.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_CreateDate)));
                    checklist.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_UpdateDate)));
                    checklist.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_CreateSyncId)));
                    checklist.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_UpdateSyncId)));
                    checklist.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_RowVersion)));

                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetCheckList", e.getMessage());
        }
        return checklist;
    }

    public PropertyDescription getPropertyDescription(String code) {
        PropertyDescription propertyDescription = new PropertyDescription();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.PropertyDescriptionSchema.TABLE_NAME, null, DbSchema.PropertyDescriptionSchema.COLUMN_PropertyDescriptionCode + "=?", new String[]{code}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {

                    propertyDescription.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.PropertyDescriptionSchema.COLUMN_ID)));
                    propertyDescription.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.PropertyDescriptionSchema.COLUMN_DATABASE_ID)));
                    propertyDescription.setPropertyDescriptionId(cursor.getLong(cursor.getColumnIndex(DbSchema.PropertyDescriptionSchema.COLUMN_PropertyDescriptionId)));
                    propertyDescription.setPropertyDescriptionClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.PropertyDescriptionSchema.COLUMN_PropertyDescriptionClientId)));
                    propertyDescription.setPropertyDescriptionCode(cursor.getLong(cursor.getColumnIndex(DbSchema.PropertyDescriptionSchema.COLUMN_PropertyDescriptionCode)));
                    propertyDescription.setName(cursor.getString(cursor.getColumnIndex(DbSchema.PropertyDescriptionSchema.COLUMN_NAME)));
                    propertyDescription.setTitle(cursor.getString(cursor.getColumnIndex(DbSchema.PropertyDescriptionSchema.COLUMN_Title)));
                    propertyDescription.setEmptyTitle(cursor.getString(cursor.getColumnIndex(DbSchema.PropertyDescriptionSchema.COLUMN_EmptyTitle)));
                    propertyDescription.setDataType(cursor.getInt(cursor.getColumnIndex(DbSchema.PropertyDescriptionSchema.COLUMN_DataType)));
                    propertyDescription.setDisplayType(cursor.getInt(cursor.getColumnIndex(DbSchema.PropertyDescriptionSchema.COLUMN_DisplayType)));
                    propertyDescription.setExtraData(cursor.getString(cursor.getColumnIndex(DbSchema.PropertyDescriptionSchema.COLUMN_ExtraData)));
                    propertyDescription.setDescription(cursor.getString(cursor.getColumnIndex(DbSchema.PropertyDescriptionSchema.COLUMN_Description)));

                    propertyDescription.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.PropertyDescriptionSchema.COLUMN_DataHash)));
                    propertyDescription.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.PropertyDescriptionSchema.COLUMN_CreateDate)));
                    propertyDescription.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.PropertyDescriptionSchema.COLUMN_UpdateDate)));
                    propertyDescription.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.PropertyDescriptionSchema.COLUMN_CreateSyncId)));
                    propertyDescription.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.PropertyDescriptionSchema.COLUMN_UpdateSyncId)));
                    propertyDescription.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.PropertyDescriptionSchema.COLUMN_RowVersion)));

                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetCheckList", e.getMessage());
        }
        return propertyDescription;

    }

    public Cheque GetCheque(long id) {
        Cheque cheque = new Cheque();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.ChequeSchema.TABLE_NAME, null, DbSchema.ChequeSchema.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    cheque.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.ChequeSchema.COLUMN_ID)));
                    cheque.setReceiptId(cursor.getLong(cursor.getColumnIndex(DbSchema.ChequeSchema.COLUMN_RECEIPTID)));
                    cheque.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.ChequeSchema.COLUMN_MAHAK_ID)));
                    cheque.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.ChequeSchema.COLUMN_DATABASE_ID)));
                    cheque.setChequeCode(cursor.getLong(cursor.getColumnIndex(DbSchema.ChequeSchema.COLUMN_ChequeCode)));
                    cheque.setBranch(cursor.getString(cursor.getColumnIndex(DbSchema.ChequeSchema.COLUMN_BRANCH)));
                    cheque.setNumber(cursor.getString(cursor.getColumnIndex(DbSchema.ChequeSchema.COLUMN_NUMBER)));
                    cheque.setAmount(cursor.getDouble(cursor.getColumnIndex(DbSchema.ChequeSchema.COLUMN_AMOUNT)));
                    cheque.setChequeClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.ChequeSchema.COLUMN_ChequeClientId)));
                    cheque.setReceiptClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.ChequeSchema.COLUMN_ReceiptClientId)));
                    cheque.setBankClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.ChequeSchema.COLUMN_BankClientId)));
                    cheque.setBankName(cursor.getString(cursor.getColumnIndex(DbSchema.ChequeSchema.COLUMN_BANK)));
                    cheque.setBankId(cursor.getString(cursor.getColumnIndex(DbSchema.ChequeSchema.COLUMN_BANK_ID)));
                    cheque.setDescription(cursor.getString(cursor.getColumnIndex(DbSchema.ChequeSchema.COLUMN_DESCRIPTION)));
                    cheque.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.ChequeSchema.COLUMN_MODIFYDATE)));
                    cheque.setDate(cursor.getLong(cursor.getColumnIndex(DbSchema.ChequeSchema.COLUMN_DATE)));
                    cheque.setType(cursor.getInt(cursor.getColumnIndex(DbSchema.ChequeSchema.COLUMN_TYPE)));
                    cheque.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.ChequeSchema.COLUMN_PUBLISH)));

                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetCheque", e.getMessage());
        }
        return cheque;
    }

    public User getUser() {
        User user = new User();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.UserSchema.TABLE_NAME, null, DbSchema.UserSchema.COLUMN_MASTER_ID + "=? and " + DbSchema.UserSchema.COLUMN_DATABASE_ID + "=? ", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    user.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_ID)));
                    user.setName(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_NAME)));
                    user.setUsername(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_USERNAME)));
                    user.setPassword(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_PASSWORD)));
                    user.setType(cursor.getInt(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_TYPE)));
                    user.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_MODIFYDATE)));
                    user.setLoginDate(cursor.getLong(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_LOGINDATE)));
                    user.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_MAHAK_ID)));
                    user.setMasterId(cursor.getLong(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_MASTER_ID)));
                    user.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_DATABASE_ID)));
                    user.setPackageSerial(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_PACKAGE_SERIAL)));
                    user.setDateSync(cursor.getLong(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_DATE_SYNC)));
                    user.setSyncId(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_SYNC_ID)));
                    user.setStoreCode(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_StoreCode)));
                    user.setServerUserID(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_USER_ID)));
                    user.setUserToken(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_UserToken)));
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetUser", e.getMessage());
        }
        return user;
    }

    private Bank getBank(long id) {
        Bank bank = new Bank();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.BanksSchema.TABLE_NAME, null, DbSchema.BanksSchema.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    bank.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.BanksSchema.COLUMN_ID)));
                    bank.setName(cursor.getString(cursor.getColumnIndex(DbSchema.BanksSchema.COLUMN_NAME)));
                    bank.setDescription(cursor.getString(cursor.getColumnIndex(DbSchema.BanksSchema.COLUMN_DESCRIPTION)));
                    bank.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.BanksSchema.COLUMN_MAHAK_ID)));
                    bank.setBankCode(cursor.getLong(cursor.getColumnIndex(DbSchema.BanksSchema.COLUMN_BANK_CODE)));
                    bank.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.BanksSchema.COLUMN_DATABASE_ID)));
                    bank.setBankId(cursor.getLong(cursor.getColumnIndex(DbSchema.BanksSchema.COLUMN_BankId)));

                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetBank", e.getMessage());
        }
        return bank;

    }

    public User getUser(String username) {
        User user = new User();
        Cursor cursor;
        try {
            //cursor = mDb.query(DbSchema.Userschema.TABLE_NAME,null,new String[] {DbSchema.Userschema.COLUMN_USERNAME , DbSchema.Userschema.COLUMN_MAHAK_ID , DbSchema.Userschema.COLUMN_DATABASE_ID }, new String[]{username ,mahakId ,databaseId}, null, null, null);
            cursor = mDb.query(DbSchema.UserSchema.TABLE_NAME, null, DbSchema.UserSchema.COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    user.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_ID)));
                    user.setName(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_NAME)));
                    user.setUsername(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_USERNAME)));
                    user.setPassword(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_PASSWORD)));
                    user.setType(cursor.getInt(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_TYPE)));
                    user.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_MODIFYDATE)));
                    user.setLoginDate(cursor.getLong(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_LOGINDATE)));
                    user.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_MAHAK_ID)));
                    user.setMasterId(cursor.getLong(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_MASTER_ID)));
                    user.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_DATABASE_ID)));
                    user.setPackageSerial(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_PACKAGE_SERIAL)));
                    user.setDateSync(cursor.getLong(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_DATE_SYNC)));
                    user.setSyncId(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_SYNC_ID)));
                    user.setStoreCode(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_StoreCode)));
                    user.setServerUserID(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_USER_ID)));
                    user.setUserToken(cursor.getString(cursor.getColumnIndex(DbSchema.UserSchema.COLUMN_UserToken)));

                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetUser", e.getMessage());
        }
        return user;
    }

    private TransactionsLog getTransactionslog(long id) {
        TransactionsLog transactionlog = new TransactionsLog();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.TransactionsLogSchema.TABLE_NAME, null, DbSchema.TransactionsLogSchema.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {

                    transactionlog.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_ID)));
                    transactionlog.setPersonId(cursor.getLong(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_PersonId)));
                    transactionlog.setTransactionId(cursor.getLong(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_TRANSACTIONID)));
                    transactionlog.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_MAHAK_ID)));
                    transactionlog.setTransactionCode(cursor.getLong(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_TransactionCode)));
                    transactionlog.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_DATABASE_ID)));
                    transactionlog.setDebtAmount(cursor.getDouble(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_DEBITAMOUNT)));
                    transactionlog.setBalance(cursor.getDouble(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_Balance)));
                    transactionlog.setCreditAmount(cursor.getDouble(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_CREDITAMOUNT)));
                    transactionlog.setType(cursor.getInt(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_TYPE)));
                    transactionlog.setStatus(cursor.getInt(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_STATUS)));
                    transactionlog.setDescription(cursor.getString(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_DESCRIPTION)));
                    transactionlog.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_MODIFYDATE)));
                    transactionlog.setTransactionDate(cursor.getString(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_DATE)));
                    transactionlog.setTransactionClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_TransactionClientId)));
                    transactionlog.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_DataHash)));
                    transactionlog.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_CreateDate)));
                    transactionlog.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_UpdateDate)));
                    transactionlog.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_CreateSyncId)));
                    transactionlog.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_UpdateSyncId)));
                    transactionlog.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_RowVersion)));

                }
                cursor.close();
            }


        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Error GetTransactionLog", e.getMessage());
        }
        return transactionlog;
    }

    public Order getDeliveryOrder(long id) {
        Order order = new Order();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null, DbSchema.OrderSchema.COLUMN_ID + "=? and " + DbSchema.OrderSchema.COLUMN_TYPE + "=? ", new String[]{String.valueOf(id), String.valueOf(ProjectInfo.TYPE_Delivery)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    order.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID)));
                    order.setPersonId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PersonId)));

                    order.setLatitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_LATITUDE)));
                    order.setLongitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_LONGITUDE)));

                    order.setPersonClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PersonClientId)));
                    order.setUserId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_USER_ID)));
                    order.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_MAHAK_ID)));
                    order.setOrderCode(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OrderCode)));
                    order.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DATABASE_ID)));
                    order.setDeliveryDate(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DELIVERYDATE)));
                    order.setDiscount(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DISCOUNT)));
                    order.setDescription((cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DESCRIPTION))));
                    order.setImmediate(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_IMMEDIATE)));
                    order.setSettlementType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_SETTLEMEMNTTYPE)));
                    order.setCode(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_CODE)));
                    order.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_MODIFYDATE)));
                    order.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PUBLISH)));
                    order.setIsFinal(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ISFINAL)));
                    order.setOrderId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OrderId)));
                    order.setOrderClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OrderClientId)));
                    order.setReceiptId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ReceiptId)));
                    order.setSendCost(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_SendCost)));
                    order.setOtherCost(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OtherCost)));
                    order.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DataHash)));
                    order.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_CreateDate)));
                    order.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_UpdateDate)));
                    order.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_CreateSyncId)));
                    order.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_UpdateSyncId)));
                    order.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_RowVersion)));
                    order.setOrderType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_TYPE)));
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetDeliveryOrder", e.getMessage());
        }
        return order;

    }

    public Order getDeliveryOrderCustomr(int customerid) {
        Order deliveryorder = new Order();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null, DbSchema.OrderSchema.COLUMN_PersonId + " =? AND " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + " =? AND " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + " =? AND " + DbSchema.OrderSchema.COLUMN_USER_ID + " =? AND " + DbSchema.OrderSchema.COLUMN_ISFINAL + " =?", new String[]{String.valueOf(customerid), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId(), String.valueOf(getPrefUserId()), String.valueOf(ProjectInfo.NOt_FINAL)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    deliveryorder.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID)));
                    deliveryorder.setPersonId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PersonId)));

                    deliveryorder.setLatitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_LATITUDE)));
                    deliveryorder.setLongitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_LONGITUDE)));

                    deliveryorder.setReturnReasonId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ReturnReasonId)));
                    deliveryorder.setPersonClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PersonClientId)));
                    deliveryorder.setUserId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_USER_ID)));
                    deliveryorder.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_MAHAK_ID)));
                    deliveryorder.setOrderCode(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OrderCode)));
                    deliveryorder.setDatabaseId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DATABASE_ID)));
                    deliveryorder.setDeliveryDate(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DELIVERYDATE)));
                    deliveryorder.setDiscount(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DISCOUNT)));
                    deliveryorder.setDescription((cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DESCRIPTION))));
                    deliveryorder.setImmediate(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_IMMEDIATE)));
                    deliveryorder.setSettlementType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_SETTLEMEMNTTYPE)));
                    deliveryorder.setCode(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_CODE)));
                    deliveryorder.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_MODIFYDATE)));
                    deliveryorder.setPublish(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_PUBLISH)));
                    deliveryorder.setOrderId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OrderId)));
                    deliveryorder.setOrderClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OrderClientId)));
                    deliveryorder.setReceiptId(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ReceiptId)));
                    deliveryorder.setSendCost(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_SendCost)));
                    deliveryorder.setOtherCost(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_OtherCost)));
                    deliveryorder.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_DataHash)));
                    deliveryorder.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_CreateDate)));
                    deliveryorder.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_UpdateDate)));
                    deliveryorder.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_CreateSyncId)));
                    deliveryorder.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_UpdateSyncId)));
                    deliveryorder.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_RowVersion)));
                    deliveryorder.setIsFinal(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ISFINAL)));
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetDeliveryOrder", e.getMessage());
        }
        return deliveryorder;

    }

    private OrderDetail getDeliveryOrderDetail(long id) {
        OrderDetail orderDetail = new OrderDetail();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.OrderDetailSchema.TABLE_NAME, null, DbSchema.OrderDetailSchema.COLUMN_ID + "=? ", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    orderDetail.setId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ID)));
                    orderDetail.setOrderDetailId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_OrderDetailId)));
                    orderDetail.setOrderDetailClientId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_OrderDetailClientId)));
                    orderDetail.setOrderClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_OrderClientId)));
                    orderDetail.setOrderId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_OrderId)));
                    orderDetail.setProductDetailId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ProductDetailId)));
                    orderDetail.setProductId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ProductId)));
                    orderDetail.setPrice(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Price)));
                    orderDetail.setCount1(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Count1)));
                    orderDetail.setCount2(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Count2)));
                    orderDetail.setSumCountBaJoz(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz)));
                    orderDetail.setGiftType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_GiftType)));
                    orderDetail.setDescription(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Description)));
                    orderDetail.setTaxPercent(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_TaxPercent)));
                    orderDetail.setChargePercent(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ChargePercent)));
                    orderDetail.setDiscount(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Discount)));
                    orderDetail.setDiscountType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_DiscountType)));
                    orderDetail.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_DataHash)));
                    orderDetail.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_CreateDate)));
                    orderDetail.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_UpdateDate)));
                    orderDetail.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_CreateSyncId)));
                    orderDetail.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_CreateSyncId)));
                    orderDetail.setRowVersion(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_RowVersion)));
                    orderDetail.setCostLevel(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_CostLevel)));
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGet", e.getMessage());
        }
        return orderDetail;
    }

    private OrderDetail GetOrderWithProduct(long id) {
        OrderDetail orderDetail = new OrderDetail();
        Cursor cursor;
        try {
            String Columns = DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_ID + " as " + DbSchema.OrderDetailSchema.COLUMN_ID +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_ProductDetailId + " as " + DbSchema.OrderDetailSchema.COLUMN_ProductDetailId +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_ProductId + " as " + DbSchema.OrderDetailSchema.COLUMN_ProductId +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_PROMOTION_CODE + " as " + DbSchema.OrderDetailSchema.COLUMN_PROMOTION_CODE +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_OrderDetailId + " as " + DbSchema.OrderDetailSchema.COLUMN_OrderDetailId +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_OrderDetailClientId + " as " + DbSchema.OrderDetailSchema.COLUMN_OrderDetailClientId +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_OrderClientId + " as " + DbSchema.OrderDetailSchema.COLUMN_OrderClientId +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_OrderId + " as " + DbSchema.OrderDetailSchema.COLUMN_OrderId +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_Price + " as " + DbSchema.OrderDetailSchema.COLUMN_Price +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_Count1 + " as " + DbSchema.OrderDetailSchema.COLUMN_Count1 +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz + " as " + DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_Count2 + " as " + DbSchema.OrderDetailSchema.COLUMN_Count2 +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_GiftCount1 + " as " + DbSchema.OrderDetailSchema.COLUMN_GiftCount1 +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_GiftCount2 + " as " + DbSchema.OrderDetailSchema.COLUMN_GiftCount2 +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_GiftType + " as " + DbSchema.OrderDetailSchema.COLUMN_GiftType +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_Description + " as " + DbSchema.OrderDetailSchema.COLUMN_Description +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_TaxPercent + " as " + DbSchema.OrderDetailSchema.COLUMN_TaxPercent +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_ChargePercent + " as " + DbSchema.OrderDetailSchema.COLUMN_ChargePercent +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_Discount + " as " + DbSchema.OrderDetailSchema.COLUMN_Discount +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_DiscountType + " as " + DbSchema.OrderDetailSchema.COLUMN_DiscountType +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_DataHash + " as " + DbSchema.OrderDetailSchema.COLUMN_DataHash +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_CreateDate + " as " + DbSchema.OrderDetailSchema.COLUMN_CreateDate +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_UpdateDate + " as " + DbSchema.OrderDetailSchema.COLUMN_UpdateDate +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_UpdateSyncId + " as " + DbSchema.OrderDetailSchema.COLUMN_UpdateSyncId +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_CreateSyncId + " as " + DbSchema.OrderDetailSchema.COLUMN_CreateSyncId +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_RowVersion + " as " + DbSchema.OrderDetailSchema.COLUMN_RowVersion +
                    "," + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_CostLevel + " as " + DbSchema.OrderDetailSchema.COLUMN_CostLevel +
                    "," + DbSchema.ProductSchema.COLUMN_MIN + " as " + DbSchema.ProductSchema.COLUMN_MIN +
                    "," + DbSchema.ProductSchema.TABLE_NAME + "." + DbSchema.ProductSchema.COLUMN_REALPRICE + " as " + DbSchema.ProductSchema.COLUMN_REALPRICE +
                    "," + DbSchema.ProductSchema.COLUMN_NAME + " as " + DbSchema.ProductSchema.COLUMN_NAME;

            cursor = mDb.rawQuery(
                    "select  " + Columns + "  from " + DbSchema.OrderDetailSchema.TABLE_NAME
                            + " inner join " + DbSchema.ProductDetailSchema.TABLE_NAME + " on " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_ProductDetailId + "=" + DbSchema.ProductDetailSchema.TABLE_NAME + "." + DbSchema.ProductDetailSchema.COLUMN_ProductDetailId
                            + " inner join " + DbSchema.ProductSchema.TABLE_NAME + " on " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_ProductId + "=" + DbSchema.ProductSchema.TABLE_NAME + "." + DbSchema.ProductSchema.COLUMN_ProductId
                            + " where " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_ID + "=" + id, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    orderDetail.setId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ID)));
                    orderDetail.setProductDetailId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ProductDetailId)));
                    orderDetail.setProductId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ProductId)));
                    orderDetail.setPromotionCode(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_PROMOTION_CODE)));
                    orderDetail.setOrderDetailId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_OrderDetailId)));
                    orderDetail.setOrderDetailClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_OrderDetailClientId)));
                    orderDetail.setOrderClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_OrderClientId)));
                    orderDetail.setOrderId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_OrderId)));
                    orderDetail.setPrice(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Price)));
                    orderDetail.setCount1(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Count1)));
                    orderDetail.setCount2(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Count2)));
                    orderDetail.setSumCountBaJoz(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz)));
                    orderDetail.setGiftType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_GiftType)));
                    orderDetail.setDescription(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Description)));
                    orderDetail.setTaxPercent(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_TaxPercent)));
                    orderDetail.setChargePercent(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ChargePercent)));
                    orderDetail.setDiscount(cursor.getDouble(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Discount)));
                    orderDetail.setDiscountType(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_DiscountType)));
                    orderDetail.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_DataHash)));
                    orderDetail.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_CreateDate)));
                    orderDetail.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_UpdateDate)));
                    orderDetail.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_CreateSyncId)));
                    orderDetail.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_UpdateSyncId)));
                    orderDetail.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_RowVersion)));
                    orderDetail.setCostLevel(cursor.getInt(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_CostLevel)));
                    orderDetail.setProductName(cursor.getString(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_NAME)));
                    orderDetail.setMin(cursor.getInt(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_MIN)));
                    orderDetail.setPriceProduct(cursor.getString(cursor.getColumnIndex(DbSchema.ProductSchema.COLUMN_REALPRICE)));
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGet", e.getMessage());
        }
        return orderDetail;
    }

    public long getMaxRowVersion(String tableName) {
        Cursor cursor = null;
        long RowVersion = 0;
        try {
            cursor = mDb.rawQuery("select max(RowVersion) from " + tableName + " where " + DbSchema.OrderSchema.COLUMN_USER_ID + " = " + getPrefUserId(), null);

            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    RowVersion = cursor.getLong(0);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Error RowVersion", e.getMessage());
        }
        return RowVersion;
    }

    private PicturesProduct getPictureProductFromCursor(Cursor cursor) {
        PicturesProduct picturesProduct = new PicturesProduct();
        picturesProduct.set_id(cursor.getLong(cursor.getColumnIndex(DbSchema.PicturesProductSchema._ID)));
        picturesProduct.setPictureId(cursor.getLong(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_PICTURE_ID)));
        picturesProduct.setProductId(cursor.getLong(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_PRODUCT_ID)));
        picturesProduct.setItemId(cursor.getLong(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_ITEM_ID)));
        picturesProduct.setItemType(cursor.getLong(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_ITEM_TYPE)));
        picturesProduct.setFileSize(cursor.getLong(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_FILE_SIZE)));
        picturesProduct.setLastUpdate(cursor.getLong(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_LAST_UPDATE)));
        picturesProduct.setPictureCode(cursor.getLong(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_PictureCode)));
        picturesProduct.setTitle(cursor.getString(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_TITLE)));
        picturesProduct.setFileName(cursor.getString(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_FILE_NAME)));
        picturesProduct.setUrl(cursor.getString(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_URL)));
        picturesProduct.setMahakId(cursor.getString(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_MAHAK_ID)));
        picturesProduct.setDataBaseId(cursor.getString(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_DATABASE_ID)));
        picturesProduct.setUserId(cursor.getLong(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_USER_ID)));

        picturesProduct.setPictureClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_PictureClientId)));
        picturesProduct.setDisplayOrder(cursor.getInt(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_DisplayOrder)));
        picturesProduct.setWidth(cursor.getInt(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_Width)));
        picturesProduct.setHeight(cursor.getInt(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_Height)));
        picturesProduct.setFormat(cursor.getString(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_Format)));
        picturesProduct.setPictureHash(cursor.getString(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_PictureHash)));
        picturesProduct.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_DataHash)));
        picturesProduct.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_CreateDate)));
        picturesProduct.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_UpdateDate)));
        picturesProduct.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_CreateSyncId)));
        picturesProduct.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_UpdateSyncId)));
        picturesProduct.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_RowVersion)));


        return picturesProduct;
    }

    public int getMax(String tablename, String column) {
        Cursor cursor;
        int Maxid = 0;
        try {
            cursor = mDb.rawQuery("select max(" + column + ") from " + tablename, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    Maxid = cursor.getInt(0);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return Maxid;
    }

    public double getTotalPriceOrder() {
        Cursor cursor1, cursor2;
        double TotalPrice = 0;
        long id;
        try {
            cursor1 = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null, DbSchema.OrderSchema.COLUMN_USER_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_TYPE + " =?", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId(), String.valueOf(ProjectInfo.TYPE_ORDER)}, null, null, null);
            if (cursor1 != null) {
                cursor1.moveToFirst();
                while (!cursor1.isAfterLast()) {
                    id = cursor1.getLong(cursor1.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID));
                    cursor2 = mDb.rawQuery("select  sum(" + DbSchema.OrderDetailSchema.COLUMN_Price + "*" + DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz + ")  from " + DbSchema.OrderDetailSchema.TABLE_NAME + " where " + DbSchema.OrderDetailSchema.COLUMN_OrderId + "=" + id, null);
                    if (cursor2 != null) {
                        cursor2.moveToFirst();
                        if (cursor2.getCount() > 0) {
                            TotalPrice = TotalPrice + cursor2.getDouble(0);
                        }
                        cursor2.close();
                    }
                    cursor1.moveToNext();
                }// End of While
                cursor1.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetTotalPriceOrder", e.getMessage());
        }
        return TotalPrice;
    }

    public double getTotalPriceInvoice() {
        Cursor cursor1, cursor2;
        double TotalPrice = 0;
        long id;
        try {
            cursor1 = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null, DbSchema.OrderSchema.COLUMN_USER_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_TYPE + " =?", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId(), String.valueOf(ProjectInfo.TYPE_INVOCIE)}, null, null, null);
            if (cursor1 != null) {
                cursor1.moveToFirst();
                while (!cursor1.isAfterLast()) {
                    id = cursor1.getLong(cursor1.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID));
                    cursor2 = mDb.rawQuery("select  sum(" + DbSchema.OrderDetailSchema.COLUMN_Price + "*" + DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz + ")  from " + DbSchema.OrderDetailSchema.TABLE_NAME + " where " + DbSchema.OrderDetailSchema.COLUMN_OrderId + "=" + id, null);
                    if (cursor2 != null) {
                        cursor2.moveToFirst();
                        if (cursor2.getCount() > 0) {
                            TotalPrice = TotalPrice + cursor2.getDouble(0);
                        }
                        cursor2.close();
                    }
                    cursor1.moveToNext();
                }// End of While
                cursor1.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetTotalPriceOrder", e.getMessage());
        }
        return TotalPrice;
    }

    public double getTotalPriceInvoicePerPerson(long personId) {
        Cursor cursor1, cursor2;
        double TotalPrice = 0;
        long id;
        try {
            cursor1 = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null, DbSchema.OrderSchema.COLUMN_USER_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_TYPE + "=? AND " + DbSchema.OrderSchema.COLUMN_PersonId + " =? ", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId(), String.valueOf(ProjectInfo.TYPE_INVOCIE) , String.valueOf(personId)}, null, null, null);
            if (cursor1 != null) {
                cursor1.moveToFirst();
                while (!cursor1.isAfterLast()) {
                    id = cursor1.getLong(cursor1.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID));
                    cursor2 = mDb.rawQuery("select  sum(" + DbSchema.OrderDetailSchema.COLUMN_Price + "*" + DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz + ")  from " + DbSchema.OrderDetailSchema.TABLE_NAME + " where " + DbSchema.OrderDetailSchema.COLUMN_OrderId + "=" + id, null);
                    if (cursor2 != null) {
                        cursor2.moveToFirst();
                        if (cursor2.getCount() > 0) {
                            TotalPrice = TotalPrice + cursor2.getDouble(0);
                        }
                        cursor2.close();
                    }
                    cursor1.moveToNext();
                }// End of While
                cursor1.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetTotalPriceOrder", e.getMessage());
        }
        return TotalPrice;
    }

    public double getTotalReceiveTransfer() {
        Cursor cursor;
        double TotalCount = 0;
        try {
            cursor = mDb.rawQuery("select count(*) as COUNT from " + DbSchema.ReceivedTransfersSchema.TABLE_NAME +
                            " where " + DbSchema.ReceivedTransfersSchema.COLUMN_ReceiverVisitorId + " = " + BaseActivity.getPrefUserMasterId()
                    , null);

            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    TotalCount = TotalCount + cursor.getDouble(cursor.getColumnIndex("COUNT"));
                    cursor.moveToNext();
                }// End of While
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Error Query", e.getMessage());
        }
        return TotalCount;
    }

    public double getTotalReceivedTransferById(String id) {
        Cursor cursor1;
        double TotalCount = 0;
        try {
            cursor1 = mDb.rawQuery("select " + DbSchema.ReceivedTransferProductsSchema.COLUMN_Count1 + " from " + DbSchema.ReceivedTransferProductsSchema.TABLE_NAME + " where " + DbSchema.ReceivedTransferProductsSchema.COLUMN_TransferId + "=" + id, null);
            if (cursor1 != null) {
                cursor1.moveToFirst();
                while (!cursor1.isAfterLast()) {
                    if (cursor1.getCount() > 0) {
                        TotalCount = TotalCount + cursor1.getDouble(0);
                    }
                    cursor1.moveToNext();
                }// End of While
                cursor1.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Error Query", e.getMessage());
        }
        return TotalCount;
    }

    public double getTotalProductById(long id) {
        Cursor cursor1;
        double TotalCount = 0;
        try {
            cursor1 = mDb.rawQuery("select " + DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz + " from " + DbSchema.OrderDetailSchema.TABLE_NAME + " where " + DbSchema.OrderDetailSchema.COLUMN_ID + "=" + id, null);
            if (cursor1 != null) {
                cursor1.moveToFirst();
                while (!cursor1.isAfterLast()) {

                    if (cursor1.getCount() > 0) {
                        TotalCount = TotalCount + (cursor1.getInt(0));
                    }
                    cursor1.moveToNext();
                }// End of While
                cursor1.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Error Query", e.getMessage());
        }
        return TotalCount;
    }

    public double getTotalPriceReceipt() {
        Cursor cursor1;
        Cursor cursor2;
        double CashAmount;
        double TotalPrice = 0;
        long Id;
        try {
            cursor1 = mDb.query(DbSchema.ReceiptSchema.TABLE_NAME, null, DbSchema.ReceiptSchema.COLUMN_USER_ID + "=? AND " + DbSchema.ReceiptSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.ReceiptSchema.COLUMN_DATABASE_ID + "=?", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor1 != null) {
                cursor1.moveToFirst();
                while (!cursor1.isAfterLast()) {
                    Id = cursor1.getLong(cursor1.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_ID));
                    CashAmount = cursor1.getDouble(cursor1.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_CASHAMOUNT));
                    cursor2 = mDb.rawQuery("select  sum(" + DbSchema.ChequeSchema.COLUMN_AMOUNT + ")  from " + DbSchema.ReceiptSchema.TABLE_NAME + " inner join " + DbSchema.ChequeSchema.TABLE_NAME + " on " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_RECEIPTID + "=" + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_ID + " where " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_ID + "=" + Id, null);
                    if (cursor2 != null) {
                        cursor2.moveToFirst();
                        if (cursor2.getCount() > 0) {
                            TotalPrice = TotalPrice + cursor2.getDouble(0);
                            cursor2.close();
                        }
                    }
                    TotalPrice = TotalPrice + CashAmount;
                    cursor1.moveToNext();
                }
                cursor1.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }
        return TotalPrice;

    }

    public double getTotalReceiptForInvoiceNotPublished() {

        Cursor cursor1;
        Cursor cursor2;
        double CashAmount;
        double TotalPrice = 0;
        long Id;
        try {
            cursor1 = mDb.query(DbSchema.ReceiptSchema.TABLE_NAME, null, DbSchema.ReceiptSchema.COLUMN_USER_ID + "=? AND " + DbSchema.ReceiptSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.ReceiptSchema.COLUMN_PUBLISH + "=? AND " + DbSchema.ReceiptSchema.COLUMN_CODE + "!= ? AND " + DbSchema.ReceiptSchema.COLUMN_DATABASE_ID + "=?", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), String.valueOf(ProjectInfo.DONT_PUBLISH), String.valueOf(ProjectInfo.DONT_CODE), BaseActivity.getPrefDatabaseId()}, null, null, null);

            if (cursor1 != null) {
                cursor1.moveToFirst();
                while (!cursor1.isAfterLast()) {
                    Id = cursor1.getLong(cursor1.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_ID));
                    CashAmount = cursor1.getDouble(cursor1.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_CASHAMOUNT));
                    cursor2 = mDb.rawQuery("select  sum(" + DbSchema.ChequeSchema.COLUMN_AMOUNT + ")  from " + DbSchema.ReceiptSchema.TABLE_NAME + " inner join " + DbSchema.ChequeSchema.TABLE_NAME + " on " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_RECEIPTID + "=" + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_ID + " where " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_ID + "=" + Id, null);
                    if (cursor2 != null) {
                        cursor2.moveToFirst();
                        if (cursor2.getCount() > 0) {
                            TotalPrice = TotalPrice + cursor2.getDouble(0);
                            cursor2.close();
                        }
                    }
                    TotalPrice = TotalPrice + CashAmount;
                    cursor1.moveToNext();
                }
                cursor1.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }

        return TotalPrice;

    }

    public double getTotalCashAmountReceipt() {
        Cursor cursor;
        double TotalPrice = 0;
        try {
            cursor = mDb.rawQuery("select  sum(" + DbSchema.ReceiptSchema.COLUMN_CASHAMOUNT + ")  from " + DbSchema.ReceiptSchema.TABLE_NAME + " where " + DbSchema.ReceiptSchema.COLUMN_MAHAK_ID + "='" + BaseActivity.getPrefMahakId() + "' and " + DbSchema.ReceiptSchema.COLUMN_DATABASE_ID + "='" + BaseActivity.getPrefDatabaseId() + "' and " + DbSchema.ReceiptSchema.COLUMN_USER_ID + "=" + getPrefUserId(), null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    TotalPrice = cursor.getDouble(0);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }

        return TotalPrice;
    }

    public double getTotalChequeReceipt() {
        Cursor cursor1;
        Cursor cursor2;
        double TotalPrice = 0;
        long Id;
        try {
            cursor1 = mDb.query(DbSchema.ReceiptSchema.TABLE_NAME, null, DbSchema.ReceiptSchema.COLUMN_USER_ID + "=? AND " + DbSchema.ReceiptSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.ReceiptSchema.COLUMN_DATABASE_ID + "=?", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor1 != null) {
                cursor1.moveToFirst();

                while (!cursor1.isAfterLast()) {
                    Id = cursor1.getLong(cursor1.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_ID));
                    cursor2 = mDb.rawQuery("select  sum(" + DbSchema.ChequeSchema.COLUMN_AMOUNT + ")  from " + DbSchema.ReceiptSchema.TABLE_NAME + " inner join " + DbSchema.ChequeSchema.TABLE_NAME + " on " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_RECEIPTID + "=" + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_ID + " where " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_ID + "=" + Id + " And " + DbSchema.ChequeSchema.COLUMN_TYPE + "=" + ProjectInfo.CHEQUE_TYPE, null);
                    if (cursor2 != null) {
                        cursor2.moveToFirst();
                        if (cursor2.getCount() > 0) {
                            TotalPrice = TotalPrice + cursor2.getDouble(0);
                            cursor2.close();
                        }
                    }
                    cursor1.moveToNext();
                }
                cursor1.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }

        return TotalPrice;

    }

    public double getTotalCashReceipt() {
        Cursor cursor1;
        Cursor cursor2;
        double TotalPrice = 0;
        long Id;
        try {
            cursor1 = mDb.query(DbSchema.ReceiptSchema.TABLE_NAME, null, DbSchema.ReceiptSchema.COLUMN_USER_ID + "=? AND " + DbSchema.ReceiptSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.ReceiptSchema.COLUMN_DATABASE_ID + "=?", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor1 != null) {
                cursor1.moveToFirst();
                while (!cursor1.isAfterLast()) {
                    Id = cursor1.getLong(cursor1.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_ID));
                    cursor2 = mDb.rawQuery("select  sum(" + DbSchema.ChequeSchema.COLUMN_AMOUNT + ")  from " + DbSchema.ReceiptSchema.TABLE_NAME + " inner join " + DbSchema.ChequeSchema.TABLE_NAME + " on " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_RECEIPTID + "=" + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_ID + " where " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_ID + "=" + Id + " And " + DbSchema.ChequeSchema.COLUMN_TYPE + "=" + ProjectInfo.CASHRECEIPT_TYPE, null);
                    if (cursor2 != null) {
                        cursor2.moveToFirst();
                        if (cursor2.getCount() > 0) {
                            TotalPrice = TotalPrice + cursor2.getDouble(0);
                            cursor2.close();
                        }
                    }
                    cursor1.moveToNext();
                }
                cursor1.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }

        return TotalPrice;

    }

    public double getTotalDiscountOrder() {
        Cursor cursor1;
        Cursor cursor2;
        long Id;
        double count;
        int discountType;
        double offValue, off = 0;
        double Price, TotalOff = 0, Discount, fixedOff;
        double TotalDiscount = 0;
        try {
            //cursor = mDb.rawQuery("select  sum("+DbSchema.Orderschema.COLUMN_DISCOUNT+")  from "+DbSchema.Orderschema.TABLE_NAME+ " where "+DbSchema.Orderschema.COLUMN_MAHAK_ID+"='" + BaseActivity.getPrefMahakId()+"' and "+DbSchema.Orderschema.COLUMN_DATABASE_ID+"='"+BaseActivity.getPrefDatabaseId()+"' and "+DbSchema.Orderschema.COLUMN_USER_ID+"="+BaseActivity.getPrefUserId()+" and "+DbSchema.Orderschema.COLUMN_TYPE + "="+ProjectInfo.TYPE_ORDER, null);
            cursor1 = mDb.rawQuery("select * from " + DbSchema.OrderSchema.TABLE_NAME + " where " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "='" + BaseActivity.getPrefMahakId() + "' and " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "='" + BaseActivity.getPrefDatabaseId() + "' and " + DbSchema.OrderSchema.COLUMN_USER_ID + "=" + getPrefUserId() + " and " + DbSchema.OrderSchema.COLUMN_TYPE + "=" + ProjectInfo.TYPE_ORDER, null);
            if (cursor1 != null) {
                cursor1.moveToFirst();
                while (!cursor1.isAfterLast()) {
                    Id = cursor1.getLong(cursor1.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID));
                    cursor2 = mDb.query(DbSchema.OrderDetailSchema.TABLE_NAME, null, DbSchema.OrderDetailSchema.COLUMN_OrderId + " =? ", new String[]{String.valueOf(Id)}, null, null, null);
                    if (cursor2 != null) {
                        cursor2.moveToFirst();
                        TotalOff = 0;
                        while (!cursor2.isAfterLast()) {
                            count = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz));
                            Price = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Price));
                            offValue = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Discount));
                            discountType = ServiceTools.toInt(cursor2.getString(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_DiscountType)));
                            Price = Price * count;
                            TotalOff = (TotalOff + offValue);
                            cursor2.moveToNext();
                        }//End of while
                        cursor2.close();
                    }
                    Discount = cursor1.getDouble(cursor1.getColumnIndex(DbSchema.OrderSchema.COLUMN_DISCOUNT));
                    TotalDiscount += Discount + TotalOff;
                    cursor1.moveToNext();
                }//End of while
                cursor1.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }
        return TotalDiscount;
    }

    public double getTotalDiscountInvoice() {
        Cursor cursor1;
        Cursor cursor2;
        long Id;
        double count;
        int discountType;
        double offValue, Off = 0, TotalOff = 0;
        double Price, Discount, fixedOff = 0;
        double TotalDiscount = 0;
        try {
            cursor1 = mDb.rawQuery("select * from " + DbSchema.OrderSchema.TABLE_NAME + " where " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "='" + BaseActivity.getPrefMahakId() + "' and " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "='" + BaseActivity.getPrefDatabaseId() + "' and " + DbSchema.OrderSchema.COLUMN_USER_ID + "=" + getPrefUserId() + " and " + DbSchema.OrderSchema.COLUMN_TYPE + "=" + ProjectInfo.TYPE_INVOCIE, null);
            if (cursor1 != null) {
                cursor1.moveToFirst();
                while (!cursor1.isAfterLast()) {
                    Id = cursor1.getLong(cursor1.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID));
                    cursor2 = mDb.query(DbSchema.OrderDetailSchema.TABLE_NAME, null, DbSchema.OrderDetailSchema.COLUMN_OrderId + " =? ", new String[]{String.valueOf(Id)}, null, null, null);
                    if (cursor2 != null) {
                        cursor2.moveToFirst();
                        TotalOff = 0;
                        while (!cursor2.isAfterLast()) {
                            count = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz));
                            Price = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Price));
                            offValue = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Discount));
                            discountType = ServiceTools.toInt(cursor2.getString(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_DiscountType)));
                            Price = Price * count;
                            TotalOff = (TotalOff + offValue);

                            cursor2.moveToNext();
                        }//End of while
                        cursor2.close();
                    }
                    Discount = cursor1.getDouble(cursor1.getColumnIndex(DbSchema.OrderSchema.COLUMN_DISCOUNT));
                    TotalDiscount += Discount + TotalOff;
                    cursor1.moveToNext();
                }//End of while
                cursor1.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }

        return TotalDiscount;
    }

    public double getTotalChargeAndTaxOrder() {

        Cursor cursor1;
        Cursor cursor2;
        long Id;
        double Tax;
        double Charge;
        double count;
        double offValue, TaxPercent, ChargePercent, Off = 0;
        double Price, TotalTaxAndCharge = 0;
        try {
            cursor1 = mDb.rawQuery("select " + DbSchema.OrderSchema.COLUMN_ID + " from " + DbSchema.OrderSchema.TABLE_NAME + " where " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "='" + BaseActivity.getPrefMahakId() + "' and " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "='" + BaseActivity.getPrefDatabaseId() + "' and " + DbSchema.OrderSchema.COLUMN_USER_ID + "=" + getPrefUserId() + " and " + DbSchema.OrderSchema.COLUMN_TYPE + "=" + ProjectInfo.TYPE_ORDER, null);
            if (cursor1 != null) {
                cursor1.moveToFirst();
                while (!cursor1.isAfterLast()) {
                    Id = cursor1.getLong(cursor1.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID));
                    cursor2 = mDb.query(DbSchema.OrderDetailSchema.TABLE_NAME, null, DbSchema.OrderDetailSchema.COLUMN_OrderId + " =? ", new String[]{String.valueOf(Id)}, null, null, null);
                    if (cursor2 != null) {
                        cursor2.moveToFirst();
                        while (!cursor2.isAfterLast()) {
                            count = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz));
                            Price = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Price));
                            offValue = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Discount));
                            TaxPercent = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_TaxPercent));
                            ChargePercent = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ChargePercent));
                            int discountType = ServiceTools.toInt(cursor2.getString(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_DiscountType)));
                            Price = Price * count;
                            Price = (Price - offValue);
                            Tax = ((Price * TaxPercent) / 100);
                            Charge = ((Price * ChargePercent) / 100);
                            TotalTaxAndCharge += Tax + Charge;
                            cursor2.moveToNext();
                        }//End of while
                        cursor2.close();
                    }
                    cursor1.moveToNext();
                }//End of while
                cursor1.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }
        return TotalTaxAndCharge;
    }

    public double getTotalChargeAndTaxInvoice() {

        Cursor cursor1;
        Cursor cursor2;
        long Id;
        double Tax;
        double Charge;
        double count;
        double offValue, TaxPercent, ChargePercent, Off = 0, TaxAndChargePercent;
        double Price, TotalTaxAndCharge = 0, fixedOff = 0, TaxAndCharge = 0;
        try {
            cursor1 = mDb.rawQuery("select " + DbSchema.OrderSchema.COLUMN_ID + " from " + DbSchema.OrderSchema.TABLE_NAME + " where " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "='" + BaseActivity.getPrefMahakId() + "' and " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "='" + BaseActivity.getPrefDatabaseId() + "' and " + DbSchema.OrderSchema.COLUMN_USER_ID + "=" + getPrefUserId() + " and " + DbSchema.OrderSchema.COLUMN_TYPE + "=" + ProjectInfo.TYPE_INVOCIE, null);
            if (cursor1 != null) {
                cursor1.moveToFirst();
                while (!cursor1.isAfterLast()) {
                    Id = cursor1.getLong(cursor1.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID));
                    cursor2 = mDb.query(DbSchema.OrderDetailSchema.TABLE_NAME, null, DbSchema.OrderDetailSchema.COLUMN_OrderId + " =? ", new String[]{String.valueOf(Id)}, null, null, null);
                    if (cursor2 != null) {
                        cursor2.moveToFirst();
                        while (!cursor2.isAfterLast()) {
                            count = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz));
                            Price = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Price));
                            offValue = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Discount));
                            TaxPercent = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_TaxPercent));
                            ChargePercent = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ChargePercent));
                            int discountType = ServiceTools.toInt(cursor2.getString(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_DiscountType)));
                            Price = Price * count;
                            Price = (Price - offValue);
                            Tax = ((Price * TaxPercent) / 100);
                            Charge = ((Price * ChargePercent) / 100);
                            TotalTaxAndCharge += Tax + Charge;
                            cursor2.moveToNext();
                        }//End of while
                        cursor2.close();
                    }
                    cursor1.moveToNext();
                }//End of while
                cursor1.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }
        /*if(AvarezMaliat().equals("0"))
            TotalTaxAndCharge = 0;*/
        return TotalTaxAndCharge;
    }

    public double getPurePriceOrder() {

        Cursor cursor1;
        Cursor cursor2;
        long Id;
        double Tax;
        double Charge;
        double TotalTaxAndCharge;
        double count;
        double offValue, TaxPercent, ChargePercent, Off = 0, TaxAndCharge, TaxAndChargePercent;
        double Price, TotalPrice = 0, Discount, FinalPrice = 0, fixedOff = 0;
        try {
            cursor1 = mDb.rawQuery("select * from " + DbSchema.OrderSchema.TABLE_NAME + " where " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "='" + BaseActivity.getPrefMahakId() + "' and " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "='" + BaseActivity.getPrefDatabaseId() + "' and " + DbSchema.OrderSchema.COLUMN_USER_ID + "=" + getPrefUserId() + " and " + DbSchema.OrderSchema.COLUMN_TYPE + "=" + ProjectInfo.TYPE_ORDER, null);
            if (cursor1 != null) {
                cursor1.moveToFirst();
                while (!cursor1.isAfterLast()) {
                    Id = cursor1.getLong(cursor1.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID));
                    cursor2 = mDb.query(DbSchema.OrderDetailSchema.TABLE_NAME, null, DbSchema.OrderDetailSchema.COLUMN_OrderId + " =? ", new String[]{String.valueOf(Id)}, null, null, null);
                    if (cursor2 != null) {
                        cursor2.moveToFirst();
                        FinalPrice = 0;
                        while (!cursor2.isAfterLast()) {
                            count = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz));
                            Price = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Price));
                            offValue = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Discount));
                            TaxPercent = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_TaxPercent));
                            ChargePercent = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ChargePercent));
                            int discountType = ServiceTools.toInt(cursor2.getString(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_DiscountType)));
                            Price = Price * count;
                            Price = (Price - offValue);
                            Tax = ((Price * TaxPercent) / 100);
                            Charge = ((Price * ChargePercent) / 100);
                            TotalTaxAndCharge = Tax + Charge;
                            Price = (Price + TotalTaxAndCharge);
                            FinalPrice += Price;
                            cursor2.moveToNext();
                        }//End of while
                        cursor2.close();
                    }
                    Discount = cursor1.getDouble(cursor1.getColumnIndex(DbSchema.OrderSchema.COLUMN_DISCOUNT));
                    FinalPrice = FinalPrice - Discount;
                    TotalPrice = TotalPrice + FinalPrice;
                    cursor1.moveToNext();
                }//End of while
                cursor1.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorgetPurePriceOrder", e.getMessage());
        }
        return TotalPrice;
    }

    public double getPurePriceInvoice() {

        Cursor cursor1;
        Cursor cursor2;
        long Id;
        double Tax;
        double Charge;
        double TotalTaxAndCharge;
        double count;
        double offValue, TaxPercent, ChargePercent, Off = 0, TaxAndCharge, TaxAndChargePercent;
        double Price, FinalPrice = 0, TotalPrice = 0, Discount;
        try {
            cursor1 = mDb.rawQuery("select * from " + DbSchema.OrderSchema.TABLE_NAME + " where " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "='" + BaseActivity.getPrefMahakId() + "' and " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "='" + BaseActivity.getPrefDatabaseId() + "' and " + DbSchema.OrderSchema.COLUMN_USER_ID + "=" + getPrefUserId() + " and " + DbSchema.OrderSchema.COLUMN_TYPE + "=" + ProjectInfo.TYPE_INVOCIE, null);
            if (cursor1 != null) {
                cursor1.moveToFirst();
                while (!cursor1.isAfterLast()) {
                    Id = cursor1.getLong(cursor1.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID));
                    cursor2 = mDb.query(DbSchema.OrderDetailSchema.TABLE_NAME, null, DbSchema.OrderDetailSchema.COLUMN_OrderId + " =? ", new String[]{String.valueOf(Id)}, null, null, null);
                    if (cursor2 != null) {
                        cursor2.moveToFirst();
                        FinalPrice = 0;
                        while (!cursor2.isAfterLast()) {
                            count = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz));
                            Price = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Price));
                            offValue = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Discount));
                            TaxPercent = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_TaxPercent));
                            ChargePercent = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ChargePercent));
                            int discountType = ServiceTools.toInt(cursor2.getString(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_DiscountType)));
                            Price = Price * count;
                            Price = (Price - offValue);
                            Tax = ((Price * TaxPercent) / 100);
                            Charge = ((Price * ChargePercent) / 100);
                            TotalTaxAndCharge = Tax + Charge;
                            Price = (Price + TotalTaxAndCharge);
                            FinalPrice += Price;
                            cursor2.moveToNext();
                        }//End of while
                        cursor2.close();
                    }

                    Discount = cursor1.getDouble(cursor1.getColumnIndex(DbSchema.OrderSchema.COLUMN_DISCOUNT));
                    FinalPrice = FinalPrice - Discount;
                    TotalPrice = TotalPrice + FinalPrice;
                    cursor1.moveToNext();
                }//End of while
                cursor1.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }
        return TotalPrice;
    }

    public int getTotalCountPeople() {
        Cursor cursor;
        int TotalCount = 0;
        try {
            cursor = mDb.rawQuery("select  count(*)  from " + DbSchema.CustomerSchema.TABLE_NAME + " where " + DbSchema.CustomerSchema.COLUMN_MAHAK_ID + "='" + BaseActivity.getPrefMahakId() + "' and " + DbSchema.CustomerSchema.COLUMN_DATABASE_ID + "='" + BaseActivity.getPrefDatabaseId() + "' and " + DbSchema.CustomerSchema.COLUMN_USER_ID + "=" + getPrefUserId() , null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    TotalCount = cursor.getInt(0);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }

        return TotalCount;
    }

    public int getTotalCountProduct() {
        Cursor cursor;
        int TotalCount = 0;
        try {
            cursor = mDb.rawQuery("SELECT count(*) from VisitorProduct where deleted = 0 and userId = ?", new String[]{String.valueOf(getPrefUserId())});
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    TotalCount = cursor.getInt(0);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }

        return TotalCount;
    }

    public int getTotalCountProduct(String searchStr, long categoryCode , long CategoryId, int MODE_ASSET) {
        Cursor cursor;
        String orderBy = BaseActivity.getPrefSortBase_product() + " " + BaseActivity.getPrefSortDirection();
        if (ServiceTools.checkArabic(searchStr)) {
            searchStr = ServiceTools.replaceWithEnglish(searchStr);
        }
        String LikeStr = ServiceTools.getLikeString(searchStr);
        int TotalCount = 0;
        try {
            cursor = mDb.rawQuery(" select count(*) from Products " +
                    " inner join ProductDetail on Products.productId = ProductDetail.productId and Products.UserId = ProductDetail.UserId " +
                    " left join visitorproduct on visitorproduct.productdetailid = productdetail.productdetailid and visitorproduct.userId = ProductDetail.UserId " +
                    " LEFT join ProductCategory on products.ProductCode = ProductCategory.ProductCode and ProductCategory.UserId" + " = " + getPrefUserId() +
                    " where ( " + LikeStr + " or " + DbSchema.ProductSchema.TABLE_NAME + "." + DbSchema.ProductSchema.COLUMN_PRODUCT_CODE + " LIKE " + "'%" + searchStr + "%'" + " ) " +
                    " and visitorproduct.deleted = 0 "  +
                    " and " + DbSchema.ProductSchema.TABLE_NAME + "." + DbSchema.ProductSchema.COLUMN_USER_ID + " = " + getPrefUserId() +
                    getProductCategoryString(CategoryId) + getProductAssetStrnig(MODE_ASSET) + getCategoryString(categoryCode) +
                    " order by " + orderBy, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    TotalCount = cursor.getInt(0);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }

        return TotalCount;
    }

    public int getTotalCountPeople(int categoryCode, long groupId, String searchString) {
        Cursor cursor;
        if (ServiceTools.checkArabic(searchString)) {
            searchString = ServiceTools.replaceWithEnglish(searchString);
        }
        String orderBy = BaseActivity.getPrefSortBase_customer() + " " + BaseActivity.getPrefSortDirection();
        String LikeStr = ServiceTools.anyPartOfPersonNameLikeString(searchString);

        int TotalCount = 0;
        try {
            cursor = mDb.rawQuery(
                    "select count(*) from Customers " +
                            " INNER join CustomersGroups on Customers.PersonGroupId = CustomersGroups.PersonGroupId and Customers.UserId = CustomersGroups.UserId " +
                            " left join visitorpeople on visitorpeople.personid = customers.personid and VisitorPeople.userId = Customers.userId " +
                            " LEFT  join ( SELECT DISTINCT userid , Personcode , CategoryCode from PersonCategory ) as pc on  Customers.PersonCode = pc.PersonCode "  +
                            " where ( " + LikeStr +
                            " or " + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonCode + " LIKE " + "'%" + searchString + "%'" +
                            " or " + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_ADDRESS + " LIKE " + "'%" + searchString + "%'" +
                            " ) and " + groupIdScript(groupId) + " visitorpeople.deleted = 0 " + getCategoryString2(categoryCode)
                            + " and " + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_MAHAK_ID + " = '" + BaseActivity.getPrefMahakId()
                            + "' and " + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_DATABASE_ID + " = " + BaseActivity.getPrefDatabaseId()
                            + " and " + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                            + " order by " + orderBy, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    TotalCount = cursor.getInt(0);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }

        return TotalCount;
    }

    public int getTotalCountReceipt() {
        Cursor cursor;
        int TotalCount = 0;
        try {
            cursor = mDb.rawQuery("select  count(*)  from " + DbSchema.ReceiptSchema.TABLE_NAME + " where " + DbSchema.ReceiptSchema.COLUMN_MAHAK_ID + "='" + BaseActivity.getPrefMahakId() + "' and " + DbSchema.ReceiptSchema.COLUMN_DATABASE_ID + "='" + BaseActivity.getPrefDatabaseId() + "' and " + DbSchema.ReceiptSchema.COLUMN_USER_ID + "=" + getPrefUserId(), null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    TotalCount = cursor.getInt(0);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }

        return TotalCount;
    }

    public int getTotalCountPayable() {
        Cursor cursor;
        int TotalCount = 0;
        try {
            cursor = mDb.rawQuery("select  count(*)  from " + DbSchema.PayableSchema.TABLE_NAME + " where " + DbSchema.PayableSchema.COLUMN_MahakId + "='" + BaseActivity.getPrefMahakId() + "' and " + DbSchema.PayableSchema.COLUMN_DatabaseId + "='" + BaseActivity.getPrefDatabaseId() + "' and " + DbSchema.PayableSchema.COLUMN_USER_ID + "=" + getPrefUserId(), null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    TotalCount = cursor.getInt(0);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }

        return TotalCount;
    }

    public int getTotalCountNonRegister() {
        Cursor cursor;
        int TotalCount = 0;
        try {
            cursor = mDb.rawQuery("select  count(*)  from " + DbSchema.NonRegisterSchema.TABLE_NAME + " where " + DbSchema.NonRegisterSchema.COLUMN_MAHAK_ID + "='" + BaseActivity.getPrefMahakId() + "' and " + DbSchema.NonRegisterSchema.COLUMN_DATABASE_ID + "='" + BaseActivity.getPrefDatabaseId() + "' and " + DbSchema.NonRegisterSchema.COLUMN_USER_ID + "=" + getPrefUserId(), null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    TotalCount = cursor.getInt(0);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("CountNonRegister", e.getMessage());
        }

        return TotalCount;
    }

    public int getTotalCountPromotion() {
        Cursor cursor;
        int TotalCount = 0;
        try {
            cursor = mDb.rawQuery("select  count(*)  from " + DbSchema.PromotionSchema.TABLE_NAME + " where " + DbSchema.PromotionSchema.COLUMN_MahakId + "='" + BaseActivity.getPrefMahakId() + "' and " + DbSchema.PromotionSchema.COLUMN_DatabaseId + "='" + BaseActivity.getPrefDatabaseId() + "' and " + DbSchema.PromotionSchema.COLUMN_Deleted + "='" + 0 + "' and " + DbSchema.PromotionSchema.COLUMN_USER_ID + "=" + getPrefUserId(), null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    TotalCount = cursor.getInt(0);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("CountNonRegister", e.getMessage());
        }

        return TotalCount;
    }

    public int getTotalCountOrder() {
        Cursor cursor;
        int TotalCount = 0;
        try {
            cursor = mDb.rawQuery("select  count(*)  from " + DbSchema.OrderSchema.TABLE_NAME + " where " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "='" + BaseActivity.getPrefMahakId() + "' and " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "='" + BaseActivity.getPrefDatabaseId() + "' and " + DbSchema.OrderSchema.COLUMN_USER_ID + "=" + getPrefUserId() + " and " + DbSchema.OrderSchema.COLUMN_TYPE + "=" + ProjectInfo.TYPE_ORDER, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    TotalCount = cursor.getInt(0);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorgetTotalCountOrder", e.getMessage());
        }

        return TotalCount;
    }

    public int getTotalCountInvoice() {
        Cursor cursor;
        int TotalCount = 0;
        try {
            cursor = mDb.rawQuery("select  count(*)  from " + DbSchema.OrderSchema.TABLE_NAME + " where " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "='" + BaseActivity.getPrefMahakId() + "' and " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "='" + BaseActivity.getPrefDatabaseId() + "' and " + DbSchema.OrderSchema.COLUMN_USER_ID + "=" + getPrefUserId() + " and " + DbSchema.OrderSchema.COLUMN_TYPE + "=" + ProjectInfo.TYPE_INVOCIE, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    TotalCount = cursor.getInt(0);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }

        return TotalCount;
    }

    public int getTotalCountReturnOfSale() {
        Cursor cursor;
        int TotalCount = 0;
        try {
            cursor = mDb.rawQuery("select  count(*)  from " + DbSchema.OrderSchema.TABLE_NAME + " where " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "='" + BaseActivity.getPrefMahakId() + "' and " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "='" + BaseActivity.getPrefDatabaseId() + "' and " + DbSchema.OrderSchema.COLUMN_USER_ID + "=" + getPrefUserId() + " and " + DbSchema.OrderSchema.COLUMN_TYPE + "=" + ProjectInfo.TYPE_RETURN_OF_SALE, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    TotalCount = cursor.getInt(0);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }

        return TotalCount;
    }

    public int getTotalCountDeliveryOrder() {
        Cursor cursor;
        int TotalCount = 0;
        try {
            cursor = mDb.rawQuery("select  count(*)  from " + DbSchema.OrderSchema.TABLE_NAME + " where " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "='" + BaseActivity.getPrefMahakId() + "' and " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "='" + BaseActivity.getPrefDatabaseId() + "' and " + DbSchema.OrderSchema.COLUMN_USER_ID + "=" + getPrefUserId() + " and " + DbSchema.OrderSchema.COLUMN_TYPE + "=" + ProjectInfo.TYPE_Delivery + " and " + DbSchema.OrderSchema.COLUMN_ISFINAL + "=" + ProjectInfo.NOt_FINAL, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    TotalCount = cursor.getInt(0);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }

        return TotalCount;
    }

    public long getTotalReceiptFromOrder(String code) {

        Cursor cursor;
        long TotalReceipt = 0;
        try {
            String rawquery = "select sum(" + DbSchema.ReceiptSchema.COLUMN_CASHAMOUNT + "+ifnull((select sum(" + DbSchema.ChequeSchema.COLUMN_AMOUNT + ") from "
                    + DbSchema.ChequeSchema.TABLE_NAME + " where " + DbSchema.ChequeSchema.COLUMN_ReceiptClientId
                    + "=" + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_ReceiptClientId + "),0)) as totalsum"
                    + " from " + DbSchema.ReceiptSchema.TABLE_NAME + " where " + DbSchema.ReceiptSchema.COLUMN_CODE + "=" + "'" + code + "'";


            cursor = mDb.rawQuery(rawquery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    TotalReceipt = cursor.getLong(cursor.getColumnIndex("totalsum"));
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorgetTotalReceipt", e.getMessage());
        }

        return TotalReceipt;
    }

    public long getTotalReceiptWithId(long id) {

        Cursor cursor;
        long TotalReceipt = 0;
        try {
            String rawquery = "select sum(" + DbSchema.ReceiptSchema.COLUMN_CASHAMOUNT + "+ifnull((select sum(" + DbSchema.ChequeSchema.COLUMN_AMOUNT + ") from "
                    + DbSchema.ChequeSchema.TABLE_NAME + " where " + DbSchema.ChequeSchema.COLUMN_RECEIPTID
                    + "=" + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_ID + "),0)) as totalsum"
                    + " from " + DbSchema.ReceiptSchema.TABLE_NAME + " where " + DbSchema.ReceiptSchema.COLUMN_ID + "=" + id;


            cursor = mDb.rawQuery(rawquery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    TotalReceipt = cursor.getLong(cursor.getColumnIndex("totalsum"));
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorgetTotalReceipt", e.getMessage());
        }

        return TotalReceipt;
    }

    public long getTotalCustomerReceiptWithId(long id) {

        Cursor cursor;
        long TotalReceipt = 0;
        try {
            String rawquery = "select sum(" + DbSchema.ReceiptSchema.COLUMN_CASHAMOUNT + "+ifnull((select sum(" + DbSchema.ChequeSchema.COLUMN_AMOUNT + ") from "
                    + DbSchema.ChequeSchema.TABLE_NAME + " where " + DbSchema.ChequeSchema.COLUMN_RECEIPTID
                    + "=" + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_ID + "),0)) as totalsum"
                    + " from " + DbSchema.ReceiptSchema.TABLE_NAME + " where " + DbSchema.ReceiptSchema.PERSON_ID + "=" + id;


            cursor = mDb.rawQuery(rawquery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    TotalReceipt = cursor.getLong(cursor.getColumnIndex("totalsum"));
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorgetTotalReceipt", e.getMessage());
        }

        return TotalReceipt;
    }

    //Get Id From Table
    public long getId(String Type, String tablename, long id) {
        Cursor cursor;
        long Id = 0;
        try {
            if (Type.equals(BaseActivity.T_DontUserId)) {
                cursor = mDb.rawQuery("select Id from " + tablename + " where MahakId ='" + BaseActivity.getPrefMahakId() + "' and DatabaseId ='" + BaseActivity.getPrefDatabaseId() + "' and MasterId =" + id, null);
                //Log.d("da",String.va BaseActivity.getPrefDatabaseId());
            } else
                cursor = mDb.rawQuery("select Id from " + tablename + " where UserId =" + getPrefUserId() + " and MahakId ='" + BaseActivity.getPrefMahakId() + "' and DatabaseId ='" + BaseActivity.getPrefDatabaseId() + "' and MasterId =" + id, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    Id = cursor.getInt(0);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return Id;
    }

    public Visitor getVisitor(long aLong) {

        Visitor visitor = new Visitor();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.VisitorSchema.TABLE_NAME, null, DbSchema.VisitorSchema.COLUMN_ID + "=? and " + DbSchema.CustomerSchema.COLUMN_MAHAK_ID + "=? and " + DbSchema.CustomerSchema.COLUMN_DATABASE_ID + "=?", new String[]{String.valueOf(aLong), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {

                    visitor.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_ID)));
                    visitor.setName(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_NAME)));
                    visitor.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_MODIFYDATE)));
                    visitor.setVisitorCode(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_VisitorCode)));
                    visitor.setBankCode(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_BANKCODE)));
                    visitor.setCashCode(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_CASHCODE)));
                    visitor.setUsername(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_USERNAME)));
                    visitor.setMobile(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_TELL)));
                    visitor.setStoreCode(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_STORECODE)));
                    visitor.setHasPriceAccess(cursor.getInt((cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_PriceAccess))) > 0);
                    visitor.setHasPriceLevelAccess(cursor.getInt((cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_CostLevelAccess))) > 0);
                    visitor.setHasRadara(cursor.getInt((cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_HasRadara))) > 0);
                    visitor.setSellPriceLevel(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_Sell_DefaultCostLevel)));
                    visitor.setSelectedPriceLevels(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_SelectedCostLevels)));
                    visitor.setChequeCredit(cursor.getDouble(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_ChequeCredit)));
                    visitor.setTotalCredit(cursor.getDouble(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_TotalCredit)));

                    visitor.setVisitorId(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_VisitorId)));
                    visitor.setVisitorClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_VisitorClientId)));
                    visitor.setPassword(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_Password)));
                    visitor.setPersonCode(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_PersonCode)));
                    visitor.setVisitorType(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_VisitorType)));
                    visitor.setDeviceId(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_DeviceId)));
                    visitor.setIsActive(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_Active)));
                    visitor.setColor(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_Color)));
                    visitor.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_DataHash)));
                    visitor.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_CreateDate)));
                    visitor.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_UpdateDate)));
                    visitor.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_CreateSyncId)));
                    visitor.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_UpdateSyncId)));
                    visitor.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_RowVersion)));


                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetUser", e.getMessage());
        }
        return visitor;
    }

    public Promotion getPromotion(long aLong) {

        Promotion promotion = new Promotion();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.PromotionSchema.TABLE_NAME, null, DbSchema.PromotionSchema.COLUMN_ID + "=? and " + DbSchema.PromotionSchema.COLUMN_MahakId + "=? and " + DbSchema.PromotionSchema.COLUMN_DatabaseId + "=?", new String[]{String.valueOf(aLong), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {

                    promotion.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_ID)));
                    promotion.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_MODIFYDATE)));
                    promotion.setCreatedBy(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_CreatedBy)));
                    promotion.setCreatedDate(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_CreatedDate)));
                    promotion.setModifiedBy(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_ModifiedBy)));
                    promotion.setPromotionCode(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_PromotionCode)));

                    promotion.setPriorityPromotion(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_PriorityPromotion)));
                    promotion.setLevelPromotion(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_LevelPromotion)));
                    promotion.setAccordingTo(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_AccordingTo)));
                    promotion.setIsCalcLinear(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_IsCalcLinear)));
                    promotion.setTypeTasvieh(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_TypeTasvieh)));
                    promotion.setDeadlineTasvieh(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_DeadlineTasvieh)));
                    promotion.setIsActive(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_IsActive)));
                    promotion.setIsAllCustomer(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_IsAllCustomer)));
                    promotion.setIsAllVisitor(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_IsAllVisitor)));
                    promotion.setIsAllStore(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_IsAllStore)));
                    promotion.setIsAllGood(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_IsAllGood)));
                    promotion.setAggregateWithOther(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_AggregateWithOther)));
                    promotion.setNamePromotion(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_NamePromotion)));
                    promotion.setDateStart(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_DateStart)));
                    promotion.setDateEnd(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_DateEnd)));
                    promotion.setTimeStart(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_TimeStart)));
                    promotion.setTimeEnd(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_TimeEnd)));
                    promotion.setDesPromotion(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_DesPromotion)));

                    promotion.setPromotionId(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_PromotionId)));
                    promotion.setPromotionClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_PromotionClientId)));
                    promotion.setPromotionCode(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_PromotionCode)));
                    promotion.setVisitors(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_Visitors)));
                    promotion.setStores(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_Stores)));
                    promotion.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_DataHash)));
                    promotion.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_CreateDate)));
                    promotion.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_UpdateDate)));
                    promotion.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_CreateSyncId)));
                    promotion.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_UpdateSyncId)));
                    promotion.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_RowVersion)));
                    promotion.setDeleted(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_Deleted)));


                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetUser", e.getMessage());
        }
        return promotion;
    }

    public Promotion getPromotion2(Cursor cursor) {
        Promotion promotion = new Promotion();
        try {
            if (cursor != null) {
                promotion.setAccordingTo(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_AccordingTo)));
                promotion.setIsCalcLinear(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_IsCalcLinear)));
                promotion.setPromotionCode(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_PromotionCode)));
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetUser", e.getMessage());
        }
        return promotion;
    }

    public Promotion getPromotionByCode(String codePromotion) {

        Promotion promotion = new Promotion();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.PromotionSchema.TABLE_NAME, null, DbSchema.PromotionSchema.COLUMN_PromotionCode + "=? and " + DbSchema.PromotionSchema.COLUMN_MahakId + "=? and " + DbSchema.PromotionSchema.COLUMN_DatabaseId + "=?", new String[]{codePromotion, BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {

                    promotion.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_ID)));
                    promotion.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_MODIFYDATE)));
                    promotion.setCreatedBy(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_CreatedBy)));
                    promotion.setCreatedDate(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_CreatedDate)));
                    promotion.setModifiedBy(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_ModifiedBy)));
                    promotion.setPromotionCode(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_PromotionCode)));
                    promotion.setPriorityPromotion(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_PriorityPromotion)));
                    promotion.setLevelPromotion(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_LevelPromotion)));
                    promotion.setAccordingTo(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_AccordingTo)));
                    promotion.setIsCalcLinear(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_IsCalcLinear)));
                    promotion.setTypeTasvieh(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_TypeTasvieh)));
                    promotion.setDeadlineTasvieh(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_DeadlineTasvieh)));
                    promotion.setIsActive(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_IsActive)));
                    promotion.setIsAllCustomer(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_IsAllCustomer)));
                    promotion.setIsAllVisitor(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_IsAllVisitor)));
                    promotion.setIsAllStore(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_IsAllStore)));
                    promotion.setIsAllGood(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_IsAllGood)));
                    promotion.setAggregateWithOther(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_AggregateWithOther)));
                    promotion.setNamePromotion(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_NamePromotion)));
                    promotion.setDateStart(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_DateStart)));
                    promotion.setDateEnd(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_DateEnd)));
                    promotion.setTimeStart(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_TimeStart)));
                    promotion.setTimeEnd(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_TimeEnd)));
                    promotion.setDesPromotion(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_DesPromotion)));

                    promotion.setPromotionId(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_PromotionId)));
                    promotion.setPromotionClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_PromotionClientId)));
                    promotion.setPromotionCode(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_PromotionCode)));
                    promotion.setVisitors(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_Visitors)));
                    promotion.setStores(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_Stores)));
                    promotion.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_DataHash)));
                    promotion.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_CreateDate)));
                    promotion.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_UpdateDate)));
                    promotion.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_CreateSyncId)));
                    promotion.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_UpdateSyncId)));
                    promotion.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_RowVersion)));
                    promotion.setDeleted(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_Deleted)));

                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetUser", e.getMessage());
        }
        return promotion;
    }

    public PromotionDetail getPromotionDetail(Cursor cursor) {
        PromotionDetail promotionDetail = new PromotionDetail();

        promotionDetail.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_ID)));
        promotionDetail.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_MODIFYDATE)));
        promotionDetail.setCreatedBy(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_CreatedBy)));
        promotionDetail.setCreatedDate(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_CreatedDate)));
        promotionDetail.setModifiedBy(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_ModifiedBy)));
        promotionDetail.setPromotionDetailCode(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_PromotionDetailCode)));

        promotionDetail.setHowToPromotion(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_HowToPromotion)));
        promotionDetail.setIsCalcAdditive(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_IsCalcAdditive)));
        promotionDetail.setReducedEffectOnPrice(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_ReducedEffectOnPrice)));
        promotionDetail.setStoreCode(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_StoreCode)));
        promotionDetail.setCodeGood(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_CodeGood)));
        promotionDetail.setTedad(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_tedad)));
        promotionDetail.setToolidCode(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_ToolidCode)));
        promotionDetail.setToPayment(cursor.getDouble(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_ToPayment)));
        promotionDetail.setTool(cursor.getDouble(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_tool)));
        promotionDetail.setArz(cursor.getDouble(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_arz)));
        promotionDetail.setMeghdar(cursor.getDouble(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_meghdar)));
        promotionDetail.setMeghdar2(cursor.getDouble(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_meghdar2)));
        promotionDetail.setMeghdarPromotion(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_MeghdarPromotion)));
        promotionDetail.setGhotr(cursor.getDouble(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_ghotr)));

        promotionDetail.setPromotionDetailId(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_PromotionDetailId)));
        promotionDetail.setPromotionDetailClientId(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_PromotionDetailClientId)));
        promotionDetail.setPromotionCode(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_PromotionCode)));
        promotionDetail.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_DataHash)));
        promotionDetail.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_CreateDate)));
        promotionDetail.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_UpdateDate)));
        promotionDetail.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_CreateSyncId)));
        promotionDetail.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_UpdateSyncId)));
        promotionDetail.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.PromotionDetailSchema.COLUMN_RowVersion)));

        return promotionDetail;
    }

    public PromotionEntity getEntitiesOfPromotion(long aLong) {

        PromotionEntity promotionEntity = new PromotionEntity();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.PromotionEntitySchema.TABLE_NAME, null, DbSchema.PromotionEntitySchema.COLUMN_ID + "=? and " + DbSchema.PromotionEntitySchema.COLUMN_MahakId + "=? and " + DbSchema.PromotionEntitySchema.COLUMN_USER_ID + "=? and " + DbSchema.PromotionEntitySchema.COLUMN_DatabaseId + "=?", new String[]{String.valueOf(aLong), BaseActivity.getPrefMahakId(), String.valueOf(getPrefUserId()), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {

                    promotionEntity.setId(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_ID)));
                    promotionEntity.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_MODIFYDATE)));
                    promotionEntity.setCreatedBy(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_CreatedBy)));
                    promotionEntity.setCreatedDate(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_CreatedDate)));
                    promotionEntity.setModifiedBy(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_ModifiedBy)));

                    // promotionEntity.setPromotionCode(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_CodePromotion)));

                    promotionEntity.setCodeEntity(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_CodeEntity)));
                    promotionEntity.setCodePromotionEntity(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_CodePromotionEntity)));
                    promotionEntity.setEntityType(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_EntityType)));

                    promotionEntity.setPromotionEntityId(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_PromotionEntityId)));
                    promotionEntity.setPromotionId(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_PromotionId)));
                    promotionEntity.setPromotionEntityClientId(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_PromotionEntityClientId)));
                    //promotionEntity.setPromotionCode(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_PromotionCode)));
                    promotionEntity.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_DataHash)));
                    promotionEntity.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_CreateDate)));
                    promotionEntity.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_UpdateDate)));
                    promotionEntity.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_CreateSyncId)));
                    promotionEntity.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_UpdateSyncId)));
                    promotionEntity.setRowVersion(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_RowVersion)));

                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetUser", e.getMessage());
        }
        return promotionEntity;
    }

    public Visitor getVisitor() {
        Visitor visitor = new Visitor();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.VisitorSchema.TABLE_NAME, null, DbSchema.VisitorSchema.COLUMN_VisitorId + "=?", new String[]{String.valueOf(getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    visitor.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_ID)));
                    visitor.setName(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_NAME)));
                    visitor.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_MODIFYDATE)));
                    visitor.setVisitorCode(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_VisitorCode)));
                    visitor.setBankCode(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_BANKCODE)));
                    visitor.setCashCode(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_CASHCODE)));
                    visitor.setUsername(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_USERNAME)));
                    visitor.setMobile(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_TELL)));
                    visitor.setStoreCode(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_STORECODE)));
                    visitor.setSellPriceLevel(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_Sell_DefaultCostLevel)));
                    visitor.setSelectedPriceLevels(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_SelectedCostLevels)));
                    visitor.setHasPriceAccess(cursor.getInt((cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_PriceAccess))) > 0);
                    visitor.setHasPriceLevelAccess(cursor.getInt((cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_CostLevelAccess))) > 0);
                    visitor.setHasRadara(cursor.getInt((cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_HasRadara))) > 0);
                    visitor.setChequeCredit(cursor.getDouble(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_ChequeCredit)));
                    visitor.setTotalCredit(cursor.getDouble(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_TotalCredit)));
                    visitor.setUserId(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_USER_ID)));

                    visitor.setVisitorId(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_VisitorId)));
                    visitor.setVisitorClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_VisitorClientId)));
                    visitor.setPassword(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_Password)));
                    visitor.setPersonCode(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_PersonCode)));
                    visitor.setVisitorType(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_VisitorType)));
                    visitor.setDeviceId(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_DeviceId)));
                    visitor.setIsActive(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_Active)));
                    visitor.setColor(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_Color)));
                    visitor.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_DataHash)));
                    visitor.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_CreateDate)));
                    visitor.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_UpdateDate)));
                    visitor.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_CreateSyncId)));
                    visitor.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_UpdateSyncId)));
                    visitor.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_RowVersion)));

                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetUser", e.getMessage());
        }
        return visitor;
    }

    public Visitor getVisitorWithVisitorID(long visitorID) {

        Visitor visitor = new Visitor();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.VisitorSchema.TABLE_NAME, null, DbSchema.VisitorSchema.COLUMN_VisitorId + "=? and " + DbSchema.CustomerSchema.COLUMN_MAHAK_ID + "=? and " + DbSchema.CustomerSchema.COLUMN_DATABASE_ID + "=?", new String[]{String.valueOf(visitorID), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {

                    visitor.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_ID)));
                    visitor.setName(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_NAME)));
                    visitor.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_MODIFYDATE)));
                    visitor.setVisitorCode(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_VisitorCode)));
                    visitor.setBankCode(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_BANKCODE)));
                    visitor.setCashCode(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_CASHCODE)));
                    visitor.setUsername(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_USERNAME)));
                    visitor.setMobile(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_TELL)));
                    visitor.setStoreCode(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_STORECODE)));
                    visitor.setHasPriceAccess(cursor.getInt((cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_PriceAccess))) > 0);
                    visitor.setHasPriceLevelAccess(cursor.getInt((cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_CostLevelAccess))) > 0);
                    visitor.setHasRadara(cursor.getInt((cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_HasRadara))) > 0);
                    visitor.setSellPriceLevel(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_Sell_DefaultCostLevel)));
                    visitor.setSelectedPriceLevels(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_SelectedCostLevels)));
                    visitor.setChequeCredit(cursor.getDouble(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_ChequeCredit)));
                    visitor.setTotalCredit(cursor.getDouble(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_TotalCredit)));
                    visitor.setUserId(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_USER_ID)));

                    visitor.setVisitorId(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_VisitorId)));
                    visitor.setVisitorClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_VisitorClientId)));
                    visitor.setPassword(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_Password)));
                    visitor.setPersonCode(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_PersonCode)));
                    visitor.setVisitorType(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_VisitorType)));
                    visitor.setDeviceId(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_DeviceId)));
                    visitor.setIsActive(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_Active)));
                    visitor.setColor(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_Color)));
                    visitor.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_DataHash)));
                    visitor.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_CreateDate)));
                    visitor.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_UpdateDate)));
                    visitor.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_CreateSyncId)));
                    visitor.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_UpdateSyncId)));
                    visitor.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_RowVersion)));

                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetUser", e.getMessage());
        }
        return visitor;
    }

    public Visitor getVisitorWithStoreCode(int storeCode) {

        Visitor visitor = new Visitor();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.VisitorSchema.TABLE_NAME, null, DbSchema.VisitorSchema.COLUMN_STORECODE + "=? and " + DbSchema.CustomerSchema.COLUMN_MAHAK_ID + "=? and " + DbSchema.CustomerSchema.COLUMN_DATABASE_ID + "=?", new String[]{String.valueOf(storeCode), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    visitor.setId(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_ID)));
                    visitor.setName(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_NAME)));
                    visitor.setModifyDate(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_MODIFYDATE)));
                    visitor.setVisitorCode(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_VisitorCode)));
                    visitor.setBankCode(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_BANKCODE)));
                    visitor.setCashCode(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_CASHCODE)));
                    visitor.setUsername(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_USERNAME)));
                    visitor.setMobile(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_TELL)));
                    visitor.setStoreCode(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_STORECODE)));
                    visitor.setHasPriceAccess(cursor.getInt((cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_PriceAccess))) > 0);
                    visitor.setHasPriceLevelAccess(cursor.getInt((cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_CostLevelAccess))) > 0);
                    visitor.setHasRadara(cursor.getInt((cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_HasRadara))) > 0);
                    visitor.setSellPriceLevel(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_Sell_DefaultCostLevel)));
                    visitor.setSelectedPriceLevels(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_SelectedCostLevels)));
                    visitor.setChequeCredit(cursor.getDouble(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_ChequeCredit)));
                    visitor.setTotalCredit(cursor.getDouble(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_TotalCredit)));
                    visitor.setUserId(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_USER_ID)));


                    visitor.setVisitorId(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_VisitorId)));
                    visitor.setVisitorClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_VisitorClientId)));
                    visitor.setPassword(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_Password)));
                    visitor.setPersonCode(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_PersonCode)));
                    visitor.setVisitorType(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_VisitorType)));
                    visitor.setDeviceId(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_DeviceId)));
                    visitor.setIsActive(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_Active)));
                    visitor.setColor(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_Color)));
                    visitor.setDataHash(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_DataHash)));
                    visitor.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_CreateDate)));
                    visitor.setUpdateDate(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_UpdateDate)));
                    visitor.setCreateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_CreateSyncId)));
                    visitor.setUpdateSyncId(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_UpdateSyncId)));
                    visitor.setRowVersion(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_RowVersion)));

                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetUser", e.getMessage());
        }
        return visitor;
    }

    public boolean isInEntity(long codeEntity, int promotionId, int entityType) {

        Cursor cursor;
        try {
            cursor = mDb.rawQuery("select * from " + DbSchema.PromotionEntitySchema.TABLE_NAME + " where " + DbSchema.PromotionEntitySchema.COLUMN_CodeEntity + " = " + codeEntity + " AND " + DbSchema.PromotionEntitySchema.COLUMN_PromotionId + " = " + promotionId + " AND " + DbSchema.PromotionEntitySchema.COLUMN_EntityType + " = " + entityType, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionEntitySchema.COLUMN_ID)) != 0) {
                    cursor.close();
                    return true;
                } else {
                    cursor.close();
                    return false;
                }
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrisInEntity", e.getMessage());
            return false;
        }
        return false;
    }

    public Order getAllTransferPublish(long userId, String transferCode) {
        Order order = new Order();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null, DbSchema.OrderSchema.COLUMN_USER_ID + " =? AND " + DbSchema.OrderSchema.COLUMN_CODE + " =? AND " + DbSchema.OrderSchema.COLUMN_PUBLISH + " =? AND " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + " =? AND " + DbSchema.OrderSchema.COLUMN_TYPE + " =? ", new String[]{String.valueOf(userId), transferCode, String.valueOf(ProjectInfo.PUBLISH), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId(), String.valueOf(ProjectInfo.TYPE_SEND_TRANSFERENCE)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    order = getOrderFromCursor(cursor);
                    if (order != null)
                        cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetTransfer", e.getMessage());
        }
        return order;
    }

    private Cursor getAllProductQuery(String searchStr, long categoryCode, long categoryId, String LIMIT, int mode_asset) {
        Cursor cursor;
        int defPriceLevel = BaseActivity.getPrefDefSellPrice();
        String orderBy = BaseActivity.getPrefSortBase_product() + " " + BaseActivity.getPrefSortDirection();
        if (ServiceTools.checkArabic(searchStr)) {
            searchStr = ServiceTools.replaceWithEnglish(searchStr);
        }
        cursor = mDb.rawQuery(" SELECT Products.ProductId , Products.productcode , products.name , UnitRatio , DefaultSellPriceLevel, PromotionId , UnitName2 , UnitName  , ProductDetail.productDetailId , pc.CategoryCode , " +
                getPriceLevel(defPriceLevel) + getDiscountLevel() + " productdetail.Customerprice , sum(visitorproduct.Count1) as sumcount1 , sum(visitorproduct.Count2) as sumcount2 " +
                " from Products inner join ProductDetail on Products.productId = ProductDetail.productId and Products.UserId = ProductDetail.UserId " +
                " left join visitorproduct on visitorproduct.productdetailid = productdetail.productdetailid and visitorproduct.userid = products.userid" +
                " LEFT join PromotionEntity on products.ProductCode = PromotionEntity.CodeEntity and PromotionEntity.entitytype = 4" +
                " LEFT  join ( SELECT DISTINCT userid , productcode , CategoryCode from ProductCategory ) as pc on  products.ProductCode = pc.ProductCode " +
                " where " +  getSearchString(searchStr) + DbSchema.ProductSchema.TABLE_NAME + " . " + DbSchema.ProductSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                + getProductAssetStrnig(mode_asset) + getProductCategoryString(categoryId) + getCategoryString2(categoryCode)
                + " and visitorproduct.deleted = 0 " + " GROUP by Products.productId " + " order by " + orderBy  + " LIMIT " + LIMIT, null);
        return cursor;
    }

    public String getPriceLevel(int defPriceLevel){
        if (defPriceLevel != 0) {
            return " case  " + defPriceLevel +
                    " when 1 then Price1 " +
                    " when 2 then Price2 " +
                    " when 3 then price3 " +
                    " when 4 then price4 " +
                    " when 5 then price5 " +
                    " when 6 then price6 " +
                    " when 7 then price7 " +
                    " when 8 then price8 " +
                    " when 9 then price9 " +
                    " when 10 then price10 end as price , ";
        }else
            return " case DefaultSellPriceLevel "  +
                    " when 1 then Price1 " +
                    " when 2 then Price2 " +
                    " when 3 then price3 " +
                    " when 4 then price4 " +
                    " when 5 then price5 " +
                    " when 6 then price6 " +
                    " when 7 then price7 " +
                    " when 8 then price8 " +
                    " when 9 then price9 " +
                    " when 10 then price10 end as price , ";

    }

    public String getDiscountLevel(){
        return " case DefaultDiscountLevel "  +
                " when 1 then Discount1 " +
                " when 2 then Discount2 " +
                " when 3 then Discount3 " +
                " when 4 then Discount4 end as discount , ";
    }


    //QUERIES GET ALL___________________________________________________________________

    public ArrayList<Reasons> getAllReasonByType(int type) {

        Reasons reasons;
        Cursor cursor;
        ArrayList<Reasons> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.ReasonsSchema.TABLE_NAME, null, DbSchema.ReasonsSchema.COLUMN_TYPE + "=? ", new String[]{String.valueOf(type)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    reasons = getReasonFromCursor(cursor);
                    array.add(reasons);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorReasonByTypet", e.getMessage());
        }
        return array;

    }

    public ArrayList<ProductCategory> getAllProductCategory() {
        ProductCategory productCategory;
        Cursor cursor;
        ArrayList<ProductCategory> productCategories = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.ProductCategorySchema.TABLE_NAME, null, DbSchema.ExtraDataSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    productCategory = productCategoryFromCursor(cursor);
                    productCategories.add(productCategory);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorReasonByTypet", e.getMessage());
        }
        return productCategories;
    }

    public ArrayList<PersonCategory> getAllPersonCategory() {
        PersonCategory personCategory;
        Cursor cursor;
        ArrayList<PersonCategory> personCategories = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.PersonCategorySchema.TABLE_NAME, null, DbSchema.PersonCategorySchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    personCategory = personCategoryFromCursor(cursor);
                    personCategories.add(personCategory);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorReasonByTypet", e.getMessage());
        }
        return personCategories;
    }

    public ArrayList<Category> getAllRootCategories() {
        Category category;
        Cursor cursor;
        ArrayList<Category> categories = new ArrayList<>();
        try {
            cursor = mDb.rawQuery(" select * from Category where  userId = ? and  CategoryCode in (SELECT  pc.CategoryCode  from Products \n" +
                    " LEFT join ProductDetail on Products.productId = ProductDetail.productId and Products.UserId = ProductDetail.UserId \n" +
                    " LEFT join visitorproduct on visitorproduct.productdetailid = productdetail.productdetailid and visitorproduct.userid = products.userid \n" +
                    " LEFT  join ( SELECT DISTINCT userid , productcode , CategoryCode from ProductCategory ) as pc on  products.ProductCode = pc.ProductCode \n" +
                    " where Products . UserId = ?  and visitorproduct.deleted = 0 \n" +
                    " GROUP by Products.productId  order by Products.ProductCode Asc LIMIT 0,100)", new String[]{String.valueOf(getPrefUserId()), String.valueOf(getPrefUserId())});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    category = categoryFromCursor(cursor);
                    categories.add(category);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorReasonByTypet", e.getMessage());
        }
        return categories;
    }

    public ArrayList<Category> getAllCategorizedPeople() {
        Category category;
        Cursor cursor;
        ArrayList<Category> categories = new ArrayList<>();
        try {
            cursor = mDb.rawQuery(" select * from Category where  userId = ? and  CategoryCode in (SELECT  pc.CategoryCode  from Customers left join VisitorPeople on VisitorPeople.PersonId = Customers.PersonId\n" +
                    " LEFT  join ( SELECT DISTINCT userid , PersonCode , CategoryCode from PersonCategory ) as pc on  Customers.PersonCode = pc.PersonCode\n" +
                    " where Customers.UserId = ? and VisitorPeople.Deleted = 0 )", new String[]{String.valueOf(getPrefUserId()), String.valueOf(getPrefUserId())});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    category = categoryFromCursor(cursor);
                    categories.add(category);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorReasonByTypet", e.getMessage());
        }
        return categories;
    }

    public ArrayList<Category> getAllExistedCategory() {
        Category category;
        Cursor cursor;
        ArrayList<Category> categories = new ArrayList<>();
        try {
            cursor = mDb.rawQuery(" SELECT Category.* from products left join  ProductCategory on ProductCategory.ProductCode = Products.ProductCode left join Category on ProductCategory.CategoryCode = Category.CategoryCode where userid =? and Category.CategoryCode is not null  GROUP by ParentCode order by ParentCode ", new String[]{String.valueOf(getPrefUserId())});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    category = categoryFromCursor(cursor);
                    categories.add(category);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorReasonByTypet", e.getMessage());
        }
        return categories;
    }

    public ArrayList<Category> getAllParentCategory(int categoryCode) {
        Category category;
        Cursor cursor;
        ArrayList<Category> categories = new ArrayList<>();
        try {
            cursor = mDb.rawQuery(" SELECT * from Category where CategoryCode =? and userid = ? ", new String[]{String.valueOf(categoryCode),String.valueOf(getPrefUserId())});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    category = categoryFromCursor(cursor);
                    categories.add(category);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorReasonByTypet", e.getMessage());
        }
        return categories;
    }

    public ArrayList<Promotion> getAllPromotion() {
        Promotion promotion;
        Cursor cursor;
        ArrayList<Promotion> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.PromotionSchema.TABLE_NAME, null,
                    DbSchema.PromotionSchema.COLUMN_MahakId + "=? and " + DbSchema.PromotionSchema.COLUMN_USER_ID + "=? and " + DbSchema.PromotionSchema.COLUMN_Deleted + "=? and " + DbSchema.PromotionSchema.COLUMN_DatabaseId + "=?", new String[]{BaseActivity.getPrefMahakId(), String.valueOf(getPrefUserId()), String.valueOf(0), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    promotion = getPromotion(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_ID)));
                    if (promotion != null)
                        array.add(promotion);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllVisitor", e.getMessage());
        }
        return array;
    }

    public ArrayList<PromotionDetail> getPromotionDetails(String codePromotion, double meghdar) {

        PromotionDetail promotionDetail;
        String orderBy = DbSchema.PromotionDetailSchema.COLUMN_ToPayment + " DESC";
        Cursor cursor;
        ArrayList<PromotionDetail> array = new ArrayList<>();
        try {
            cursor = mDb.rawQuery("SELECT * FROM PromotionDetail WHERE MahakId=? and UserId=? and DatabaseId=? and ToPayment <=? and PromotionCode =?  ORDER BY ToPayment DESC",new String[]{BaseActivity.getPrefMahakId(), String.valueOf(getPrefUserId()), BaseActivity.getPrefDatabaseId(), String.valueOf(meghdar), codePromotion});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    promotionDetail = getPromotionDetail(cursor);
                    if (promotionDetail != null)
                        array.add(promotionDetail);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllVisitor", e.getMessage());
        }
        return array;
    }

    public ArrayList<PromotionDetail> getPromotionDetails(String codePromotion) {

        PromotionDetail promotionDetail;
        String orderBy = DbSchema.PromotionDetailSchema.COLUMN_ToPayment + " DESC";
        Cursor cursor;
        ArrayList<PromotionDetail> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.PromotionDetailSchema.TABLE_NAME, null,
                    DbSchema.PromotionDetailSchema.COLUMN_MahakId + "=? and " + DbSchema.PromotionDetailSchema.COLUMN_USER_ID + "=? and " + DbSchema.PromotionDetailSchema.COLUMN_DatabaseId + "=? and " + DbSchema.PromotionDetailSchema.COLUMN_PromotionCode + " =? ", new String[]{BaseActivity.getPrefMahakId(), String.valueOf(getPrefUserId()), BaseActivity.getPrefDatabaseId(), codePromotion}, null, null, orderBy);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    promotionDetail = getPromotionDetail(cursor);
                    if (promotionDetail != null)
                        array.add(promotionDetail);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllVisitor", e.getMessage());
        }
        return array;
    }

    public ArrayList<PromotionEntity> getEntitiesOfPromotions(int promotionId) {

        PromotionEntity promotionEntity;
        Cursor cursor;
        ArrayList<PromotionEntity> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.PromotionEntitySchema.TABLE_NAME, null,
                    DbSchema.PromotionEntitySchema.COLUMN_MahakId + "=? and " + DbSchema.PromotionEntitySchema.COLUMN_USER_ID + "=? and " + DbSchema.PromotionEntitySchema.COLUMN_DatabaseId + "=? and " + DbSchema.PromotionEntitySchema.COLUMN_PromotionId + " =? ", new String[]{BaseActivity.getPrefMahakId(), String.valueOf(getPrefUserId()), BaseActivity.getPrefDatabaseId(), String.valueOf(promotionId)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    promotionEntity = getEntitiesOfPromotion(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_ID)));
                    if (promotionEntity != null)
                        array.add(promotionEntity);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllVisitor", e.getMessage());
        }
        return array;
    }

    public ArrayList<Visitor> getAllVisitor() {
        Visitor visitor;
        Cursor cursor;
        ArrayList<Visitor> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.VisitorSchema.TABLE_NAME, null,
                    DbSchema.VisitorSchema.COLUMN_VisitorId + " !=? and " + DbSchema.CustomerSchema.COLUMN_DATABASE_ID + "=?", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    visitor = getVisitor(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_ID)));
                    if (visitor != null)
                        array.add(visitor);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllVisitor", e.getMessage());
        }
        return array;
    }

    public ArrayList<Customer> getAllCustomer(long categoryCode ,  long groupId, int totalItemCount, String searchString) {
        Customer customer;
        Cursor cursor;
        if (ServiceTools.checkArabic(searchString)) {
            searchString = ServiceTools.replaceWithEnglish(searchString);
        }
        String LikeStr = ServiceTools.anyPartOfPersonNameLikeString(searchString);
        String orderBy = BaseActivity.getPrefSortBase_customer() + " " + BaseActivity.getPrefSortDirection();
        String LIMIT = String.valueOf(totalItemCount) + ",15";
        ArrayList<Customer> array = new ArrayList<>();
        try {
            cursor = mDb.rawQuery("select Customers.Organization, Customers.name, Customers.Address, Customers.PersonCode, PromotionId, Customers.PersonClientId, Customers.PersonId, Customers.PersonGroupId, Customers.Mobile, Customers.Phone, Customers.Id , Customers.balance  , pc.CategoryCode" +
                    " from Customers inner join CustomersGroups on Customers.PersonGroupId = CustomersGroups.PersonGroupId and CustomersGroups.userId = Customers.userId  " +
                    " LEFT join visitorpeople  on visitorpeople.personid = Customers.personid and VisitorPeople.userId = Customers.userId " +
                    " LEFT join PromotionEntity  on PromotionEntity.CodeEntity = Customers.PersonCode and EntityType = 2 " +
                    " LEFT  join ( SELECT DISTINCT userid , Personcode , CategoryCode from PersonCategory ) as pc on  Customers.PersonCode = pc.PersonCode" +
                    " where ( " + LikeStr +
                    " or " + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonCode + " LIKE " + "'%" + searchString + "%'" +
                    " or " + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_ADDRESS + " LIKE " + "'%" + searchString + "%'" +
                    " ) and " + groupIdScript(groupId) +
                    " Customers.UserId = ? and Customers.MahakId = ? and Customers.DatabaseId = ? and (visitorpeople.Deleted = 0 or visitorpeople.Deleted is NULL)" +
                    getCategoryString2(categoryCode)+ " order by " + orderBy + " LIMIT " + LIMIT, new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId()});
            // cursor = mDb.query(DbSchema.Customerschema.TABLE_NAME, null, DbSchema.Customerschema.COLUMN_USER_ID + " =? and " + DbSchema.Customerschema.COLUMN_MAHAK_ID + "=? and " + DbSchema.Customerschema.COLUMN_DATABASE_ID + "=? and " + groupIdScript(groupId) + DbSchema.Customerschema.COLUMN_Deleted + "=?", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId(), String.valueOf(0)}, null, null, orderBy, LIMIT);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    //customer = getCustomer(cursor.getLong(cursor.getColumnIndex(DbSchema.Customerschema.COLUMN_ID)));
                    customer = getCustomerFromCursor2(cursor);
                    //extra_info_people.put(customer.getPersonId(),getMoreCustomerInfo(customer.getPersonCode()));
                    array.add(customer);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllCustomer", e.getMessage());
        }
        return array;
    }

    public String groupIdScript(long groupId) {
        if (groupId == ProjectInfo.DONT_CUSTOMER_GROUP)
            return "";
        else if (groupId == ProjectInfo.promo_CUSTOMER_GROUP)
            return " PromotionId > 0 " + " and ";
        else
            return " Customers.PersonGroupId = " + groupId + " and ";
    }

    public ArrayList<ReportUserDetail> getCustomerReturnOfSale(long startdate, long enddate, int orderType) {

        Cursor cursor;
        ReportUserDetail item;
        ArrayList<ReportUserDetail> items = new ArrayList<>();

        String str_guest_customer = "مشتری مهمان";
        String strSelect = "select " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_PersonId + " , ifnull(" + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_NAME + ",'" + str_guest_customer + "') as Name"
                + " , SUM(" + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_Price + " * " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz + ") as sump from " + DbSchema.OrderSchema.TABLE_NAME
                + " INNER JOIN " + DbSchema.OrderDetailSchema.TABLE_NAME
                + " ON " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ID + " = " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_OrderId
                + " LEFT JOIN " + DbSchema.CustomerSchema.TABLE_NAME
                + " ON " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_PersonId + " = " + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonId
                + " Where " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " > " + startdate + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " <= " + enddate
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_TYPE + " == " + orderType
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_DATABASE_ID + " ='" + BaseActivity.getPrefDatabaseId() + "'"
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_MAHAK_ID + " ='" + BaseActivity.getPrefMahakId() + "'"
                + " Group by " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_PersonId
                + " Having sump > 0";


        try {
            cursor = mDb.rawQuery(strSelect, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    item = new ReportUserDetail();
                    item.setId(cursor.getInt(0));
                    item.setName(cursor.getString(1));
                    item.setReturnOfSale(cursor.getLong(2));
                    items.add(item);
                    cursor.moveToNext();
                }
                cursor.close();
            }
            for (int i = 0; i < items.size(); i++) {
                items.get(i).setCountOfProduct(getIntervalCountOfProduct(startdate, enddate, ProjectInfo.TYPE_INVOCIE, items.get(i).getId()));//get count of product
                items.get(i).setCountOfReturn(getIntervalCountOfReturnProduct(startdate, enddate, orderType, items.get(i).getId()));
                items.get(i).setTaxCharge(getIntervalTaxCharge3(startdate, enddate, ProjectInfo.TYPE_INVOCIE, items.get(i).getId()));//get tax and charge
                items.get(i).setDiscount(getIntervalDiscount(startdate, enddate, ProjectInfo.TYPE_INVOCIE, items.get(i).getId()));//get discount//get count of product
                items.get(i).setSale(getCustomerSale(startdate, enddate, items.get(i).getId(), ProjectInfo.TYPE_INVOCIE));//get count of product
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return items;
    }

    public ArrayList<Customer> searchCustomer(int categoryCode , long groupId, String searchString) {
        Customer customer;
        Cursor cursor;
        if (ServiceTools.checkArabic(searchString)) {
            searchString = ServiceTools.replaceWithEnglish(searchString);
        }
        String orderBy = BaseActivity.getPrefSortBase_customer() + " " + BaseActivity.getPrefSortDirection();
        String LikeStr = ServiceTools.anyPartOfPersonNameLikeString(searchString);
        ArrayList<Customer> array = new ArrayList<>();
        try {
            cursor = mDb.rawQuery("select Customers.Organization, Customers.name, Customers.Address, Customers.PersonCode, PromotionId, Customers.PersonClientId, Customers.PersonId, Customers.PersonGroupId, Customers.Mobile, Customers.Phone, Customers.Id, Customers.balance , pc.CategoryCode " +
                    " from Customers inner join CustomersGroups on Customers.PersonGroupId = CustomersGroups.PersonGroupId  and CustomersGroups.userId = Customers.userId  " +
                    " LEFT join PromotionEntity  on PromotionEntity.CodeEntity = Customers.PersonCode and EntityType = 2 " +
                    " LEFT join visitorpeople  on visitorpeople.personid = Customers.personid and VisitorPeople.userId = Customers.userId " +
                    " LEFT join ( SELECT DISTINCT userid , Personcode , CategoryCode from PersonCategory ) as pc on  Customers.PersonCode = pc.PersonCode" +
                    " where ( " + LikeStr +
                    " or " + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonCode + " LIKE " + "'%" + searchString + "%'" +
                    " or " + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_ADDRESS + " LIKE " + "'%" + searchString + "%'" +
                    " ) and " + groupIdScript(groupId)  +
                    " Customers.UserId = ? and Customers.MahakId = ? and Customers.DatabaseId = ? and visitorpeople.Deleted = ?" + getCategoryString2(categoryCode) +
                    " order by " + orderBy, new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId(), String.valueOf(0)});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    customer = getCustomerFromCursor2(cursor);
                    array.add(customer);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllCustomer", e.getMessage());
        }
        return array;
    }

    private String getProductCategoryString(long categoryId) {
        if (categoryId != 0)
            return " and " + DbSchema.ProductSchema.TABLE_NAME + "." + DbSchema.ProductSchema.COLUMN_CATEGORYID + " = " + categoryId;
        else return "";
    }

    private String getSearchString(String searchString) {
        if (!TextUtils.isEmpty(searchString))
            return " ( " + getLikeString(searchString) + " or " + DbSchema.ProductSchema.TABLE_NAME + "." + DbSchema.ProductSchema.COLUMN_PRODUCT_CODE + " LIKE " + "'%" + searchString + "%'" + " ) " + " and ";
        else return "";
    }

    private String getCategoryString(long categoryCode) {
        if (categoryCode != 0)
            return " and " + DbSchema.ProductCategorySchema.TABLE_NAME + "." + DbSchema.ProductCategorySchema.COLUMN_CategoryCode + " = " + categoryCode;
        else return "";
    }

    private String getCategoryString2(long categoryCode) {
        if (categoryCode != 0)
            return " and " + "pc.CategoryCode" + " = " + categoryCode;
        else return "";
    }

    private String getProductAssetStrnig(int modeasset) {
        if (modeasset == ProjectInfo.ASSET_ALL_PRODUCT) {
            return "";
        } else if (modeasset == ProjectInfo.ASSET_EXIST_PRODUCT) {
            return " and " + "visitorproduct.Count1" + " > " + " 0.0 ";
        } else if (modeasset == ProjectInfo.ASSET_ZERO_PRODUCT) {
            return " and " + "visitorproduct.Count1" + " = " + " 0.0 ";
        } else if (modeasset == ProjectInfo.ASSET_NOT_EXIST_PRODUCT) {
            return " and " + "visitorproduct.Count1" + " < " + " 0.0 ";
        } else if (modeasset == ProjectInfo.ASSET_promotion) {
            return " and " + "PromotionId" + ">" + " 0 ";
        }
        return "";
    }

    public ArrayList<Customer> getAllOfCustomer() {
        Customer customer;
        Cursor cursor;
        String orderBy = DbSchema.CustomerSchema.COLUMN_PersonCode;
        ArrayList<Customer> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.CustomerSchema.TABLE_NAME, null, DbSchema.CustomerSchema.COLUMN_USER_ID + " =? and " + DbSchema.CustomerSchema.COLUMN_MAHAK_ID + "=? and " + DbSchema.CustomerSchema.COLUMN_DATABASE_ID + " =? " , new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), String.valueOf(BaseActivity.getPrefDatabaseId())}, null, null, orderBy);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    customer = getCustomerFromCursor(cursor);
                    if (customer != null)
                        array.add(customer);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllCustomer", e.getMessage());
        }
        return array;
    }

    public ArrayList<CustomerGroup> getAllCustomerGroup() {
        CustomerGroup customergroup;
        Cursor cursor;
        ArrayList<CustomerGroup> array = new ArrayList<>();
        String orderBy = DbSchema.CustomersGroupSchema.COLUMN_PersonGroupId;
        try {
            cursor = mDb.query(DbSchema.CustomersGroupSchema.TABLE_NAME, null, DbSchema.CustomersGroupSchema.COLUMN_MAHAK_ID + "=? and " + DbSchema.CustomersGroupSchema.COLUMN_DATABASE_ID + "=? and " + DbSchema.CustomersGroupSchema.COLUMN_USER_ID + "=? ", new String[]{BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId() , String.valueOf(BaseActivity.getPrefUserId())}, null, null, orderBy);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    customergroup = GetCustomerGroup(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_ID)));
                    if (customergroup != null)
                        array.add(customergroup);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Erorr Query", e.getMessage());
        }
        return array;
    }

    public ArrayList<ProductDetail> getAllProductDetail(long id) {
        long DontGroupId = 0;
        ProductDetail productDetail;
        Cursor cursor;
        ArrayList<ProductDetail> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.ProductDetailSchema.TABLE_NAME, null, DbSchema.ProductDetailSchema.COLUMN_ProductId + " =? and " + DbSchema.ProductDetailSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(id) , String.valueOf(getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    productDetail = getProductDetailFromCursor(cursor);
                    array.add(productDetail);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getAllProductDetail", e.getMessage());
        }

        return array;
    }

    public HashMap<String, Long> getMapProductIdBarcode() {
        HashMap<String, Long> hashMap = new HashMap<>();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.ProductDetailSchema.TABLE_NAME, null,  DbSchema.ProductDetailSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    long productid = cursor.getLong(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_ProductId));
                    String barcode = cursor.getString(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_Barcode));
                    hashMap.put(barcode, productid);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("getMapProductIdBarcode", e.getMessage());
        }

        return hashMap;
    }

    public ArrayList<ProductDetail> getAllProductDetailWithProductId(long id, int type, int mode) {
        long DontGroupId = 0;
        ProductDetail productDetail;
        Cursor cursor;
        ArrayList<ProductDetail> array = new ArrayList<>();
        try {
            cursor = mDb.rawQuery("select * from " + DbSchema.ProductDetailSchema.TABLE_NAME +
                    " where " + DbSchema.ProductDetailSchema.COLUMN_ProductId + " = " + id , null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    productDetail = getProductDetail(cursor.getLong(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_ProductDetailId)));
                    if (productDetail != null) {
                        array.add(productDetail);
                    }
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("AllProductDetail2", e.getMessage());
        }

        return array;
    }

    public ArrayList<ProductDetail> getAllProductDetailWithProductId(long id) {
        long DontGroupId = 0;
        ProductDetail productDetail;
        Cursor cursor;
        ArrayList<ProductDetail> array = new ArrayList<>();
        try {
            cursor = mDb.rawQuery("select * from " + DbSchema.ProductDetailSchema.TABLE_NAME +
                    " where " + DbSchema.ProductDetailSchema.COLUMN_ProductId + " = " + id , null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    productDetail = getProductDetail(cursor.getLong(cursor.getColumnIndex(DbSchema.ProductDetailSchema.COLUMN_ProductDetailId)));
                    if (productDetail != null) {
                        array.add(productDetail);
                    }
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("AllProductDetail3", e.getMessage());
        }
        return array;
    }

    public ArrayList<Product> getAllProduct(String searchStr, long categoryCode, long categoryId, int mode_asset, int totalItem) {
        Product product;
        String LIMIT = totalItem + ",100";
        Cursor cursor = null;
        ArrayList<Product> array = new ArrayList<>();
        try {
            cursor = getAllProductQuery(searchStr , categoryCode ,categoryId, LIMIT, mode_asset);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    product = getProductFromCursor2(cursor);
                    array.add(product);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllProduct", e.getMessage());
        }
        return array;
    }

    public String getPhotoUrl( long productId ) {
        String url = null;
        Cursor cursor;
        try {
            cursor = mDb.rawQuery(" select url , PicturesProduct.pictureid from PicturesProduct LEFT join PhotoGallery  on PicturesProduct.pictureId = PhotoGallery.pictureId where itemCode = ? and deleted = 0 order by PicturesProduct.pictureid desc ", new String[]{String.valueOf(productId)});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    url = cursor.getString(cursor.getColumnIndex(DbSchema.PicturesProductSchema.COLUMN_URL));
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorReasonByTypet", e.getMessage());
        }
        return url;
    }

    public ArrayList<ProductGroup> getAllProductGroup() {
        ProductGroup productGroup;
        Cursor cursor;
        ArrayList<ProductGroup> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.ProductGroupSchema.TABLE_NAME, null, DbSchema.ProductGroupSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(getPrefUserId())}, null, null, null);
            //cursor = mDb.rawQuery(DbSchema.ProductGroupSchema.TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    productGroup = getProductGroupFromCursor(cursor);
                    array.add(productGroup);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrgetAllCategory", e.getMessage());
        }

        return array;
    }

    public ArrayList<ProductPriceLevelName> getAllPriceLevelName() {
        ProductPriceLevelName productPriceLevelName;
        Cursor cursor;
        ArrayList<ProductPriceLevelName> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.PriceLevelNameSchema.TABLE_NAME, null, DbSchema.PriceLevelNameSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(getPrefUserId())}, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    productPriceLevelName = GetPriceLevelName(cursor.getLong(cursor.getColumnIndex(DbSchema.PriceLevelNameSchema._ID)));
                    if (productPriceLevelName != null)
                        array.add(productPriceLevelName);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrPriceLevelName", e.getMessage());
        }
        return array;
    }

    public ArrayList<Promotion> getValidPromotions(String InvoiceDate) {
        Promotion promotion;
        Cursor cursor;
        ArrayList<Promotion> array = new ArrayList<>();
        try {
            cursor = mDb.rawQuery("select * from " + DbSchema.PromotionSchema.TABLE_NAME + " where " + DbSchema.PromotionSchema.COLUMN_Deleted + "='" + 0 + "' and '" + InvoiceDate + "'" + " BETWEEN " + DbSchema.PromotionSchema.COLUMN_DateStart + " AND " + DbSchema.PromotionSchema.COLUMN_DateEnd + " Order by " + DbSchema.PromotionSchema.COLUMN_PriorityPromotion + "," + DbSchema.PromotionSchema.COLUMN_PromotionCode, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    promotion = getPromotion(cursor.getInt(cursor.getColumnIndex(DbSchema.PromotionSchema.COLUMN_ID)));
                    if (promotion != null)
                        array.add(promotion);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrValidPromotions", e.getMessage());
        }
        return array;
    }

    public ArrayList<Bank> getAllBank(long bankCode) {
        Bank bank;
        Cursor cursor;
        ArrayList<Bank> array = new ArrayList<>();
        try {
            String orderBy = DbSchema.BanksSchema.COLUMN_BANK_CODE + " = " + bankCode + " DESC";
            cursor = mDb.query(DbSchema.BanksSchema.TABLE_NAME, null, DbSchema.BanksSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.BanksSchema.COLUMN_DATABASE_ID + "=?", new String[]{BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId()}, null, null, orderBy);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    bank = getBank(cursor.getLong(cursor.getColumnIndex(DbSchema.BanksSchema.COLUMN_ID)));
                    if (bank != null)
                        array.add(bank);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllBanks", e.getMessage());
        }

        return array;
    }

    public ArrayList<Bank> getAllBankWithBankId(long bankId) {
        Bank bank;
        Cursor cursor;
        ArrayList<Bank> array = new ArrayList<>();
        try {
            String orderBy = DbSchema.BanksSchema.COLUMN_BankId + " = " + bankId + " DESC";
            cursor = mDb.query(DbSchema.BanksSchema.TABLE_NAME, null, DbSchema.BanksSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.BanksSchema.COLUMN_DATABASE_ID + "=?", new String[]{BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId()}, null, null, orderBy);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    bank = getBank(cursor.getLong(cursor.getColumnIndex(DbSchema.BanksSchema.COLUMN_ID)));
                    array.add(bank);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllBanks", e.getMessage());
        }

        return array;
    }

    public ArrayList<Bank> getAllBank() {
        Bank bank;
        Cursor cursor;
        ArrayList<Bank> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.BanksSchema.TABLE_NAME, null, DbSchema.BanksSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.BanksSchema.COLUMN_DATABASE_ID + "=?", new String[]{BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    bank = getBank(cursor.getLong(cursor.getColumnIndex(DbSchema.BanksSchema.COLUMN_ID)));
                    array.add(bank);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllBanks", e.getMessage());
        }

        return array;
    }

    public ArrayList<OrderDetail> getAllOrderDetailWithOrderId(long orderId) {
        OrderDetail orderDetail;
        Cursor cursor;
        ArrayList<OrderDetail> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.OrderDetailSchema.TABLE_NAME, null, DbSchema.OrderDetailSchema.COLUMN_OrderId + "=?", new String[]{String.valueOf(orderId)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    orderDetail = GetProductInOrder(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ID)));
                    if (orderDetail != null)
                        array.add(orderDetail);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllProductInOrder", e.getMessage());
        }

        return array;
    }

    public ArrayList<OrderDetail> getAllOrderDetailForSend(long orderId) {
        OrderDetail orderDetail;
        Cursor cursor;
        ArrayList<OrderDetail> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.OrderDetailSchema.TABLE_NAME, null, DbSchema.OrderDetailSchema.COLUMN_OrderId + "=?", new String[]{String.valueOf(orderId)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    orderDetail = GetOrderDetailWithId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ID)));
                    if (orderDetail != null)
                        array.add(orderDetail);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }

        return array;
    }

    public ArrayList<OrderDetail> getAllOrderDetail(long Id) {
        OrderDetail orderDetail;
        Cursor cursor;
        ArrayList<OrderDetail> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.OrderDetailSchema.TABLE_NAME, null, DbSchema.OrderDetailSchema.COLUMN_OrderId + "=?", new String[]{String.valueOf(Id)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    orderDetail = GetOrderDetailWithId(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ID)));
                    if (orderDetail != null)
                        array.add(orderDetail);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }

        return array;
    }

    public ArrayList<OrderDetailProperty> getAllOrderDetailProperty(long orderId, int productId) {
        OrderDetailProperty orderDetailProperty;
        Cursor cursor;
        ArrayList<OrderDetailProperty> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.OrderDetailPropertySchema.TABLE_NAME, null, DbSchema.OrderDetailPropertySchema.COLUMN_OrderId + "=? and " + DbSchema.OrderDetailPropertySchema.COLUMN_ProductId + "=? ", new String[]{String.valueOf(orderId), String.valueOf(productId)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    orderDetailProperty = GetOrderDetailProperty(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderDetailPropertySchema.COLUMN_ID)));
                    if (orderDetailProperty != null)
                        array.add(orderDetailProperty);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }

        return array;
    }

    public ArrayList<OrderDetail> getAllProductWithOrderDetail(long id) {
        OrderDetail orderDetail;
        Cursor cursor;
        ArrayList<OrderDetail> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.OrderDetailSchema.TABLE_NAME, null, DbSchema.OrderDetailSchema.COLUMN_OrderId + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    orderDetail = GetOrderWithProduct(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ID)));
                    if (orderDetail != null)
                        array.add(orderDetail);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllProductInOrder", e.getMessage());
        }
        return array;
    }

    public ArrayList<OrderDetail> getAllProductDeliveryOrderDetail(long id) {
        OrderDetail orderDetail;
        Cursor cursor;
        ArrayList<OrderDetail> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.OrderDetailSchema.TABLE_NAME, null, DbSchema.OrderDetailSchema.COLUMN_OrderId + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    orderDetail = GetOrderWithProduct(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ID)));
                    if (orderDetail != null)
                        array.add(orderDetail);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllProductInOrder", e.getMessage());
        }
        return array;
    }

    public ArrayList<Order> getAllOrder() {
        Order order;
        Cursor cursor;
        ArrayList<Order> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null, DbSchema.OrderSchema.COLUMN_USER_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_TYPE + " =? ", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId(), String.valueOf(ProjectInfo.TYPE_ORDER)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    order = GetOrder(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID)));
                    if (order != null)
                        array.add(order);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllOrder", e.getMessage());
        }

        return array;
    }

    public ArrayList<Order> getAllInvoice() {
        Order order;
        Cursor cursor;
        ArrayList<Order> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null, DbSchema.OrderSchema.COLUMN_USER_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_TYPE + " =? ", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId(), String.valueOf(ProjectInfo.TYPE_INVOCIE)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    order = GetOrder(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID)));
                    if (order != null)
                        array.add(order);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllOrder", e.getMessage());
        }

        return array;
    }

    public ArrayList<Order> getAllReturnOfSale() {
        Order order;
        Cursor cursor;
        ArrayList<Order> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null, DbSchema.OrderSchema.COLUMN_USER_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_TYPE + "=? ", new String[]{String.valueOf(getPrefUserId()), String.valueOf(ProjectInfo.TYPE_RETURN_OF_SALE)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    order = getOrderFromCursor(cursor);
                    if (order != null)
                        array.add(order);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllOrder", e.getMessage());
        }

        return array;
    }

    public ArrayList<NonRegister> getAllNonRegister() {
        NonRegister nonRegister;
        Cursor cursor;
        ArrayList<NonRegister> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.NonRegisterSchema.TABLE_NAME, null, DbSchema.NonRegisterSchema.COLUMN_USER_ID + "=? AND " + DbSchema.NonRegisterSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.NonRegisterSchema.COLUMN_DATABASE_ID + "=?", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    nonRegister = GetNonRegister(cursor.getLong(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_ID)));
                    if (nonRegister != null)
                        array.add(nonRegister);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllOrder", e.getMessage());
        }

        return array;
    }

    public ArrayList<Order> getAllInvoiceNotPublished() {
        Order order;
        Cursor cursor;
        ArrayList<Order> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null, DbSchema.OrderSchema.COLUMN_USER_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_PUBLISH + "=? AND " + DbSchema.OrderSchema.COLUMN_TYPE + " =? ", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId(), String.valueOf(ProjectInfo.DONT_PUBLISH), String.valueOf(ProjectInfo.TYPE_INVOCIE)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    order = getOrderFromCursor(cursor);
                    if (order != null)
                        array.add(order);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllOrder", e.getMessage());
        }

        return array;
    }

    public ArrayList<Order> getAllTransfer() {
        Order order;
        Cursor cursor;
        ArrayList<Order> array = new ArrayList<>();
        try {

            String orderBy = DbSchema.OrderSchema.COLUMN_MODIFYDATE + " DESC";
            cursor = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null,
                    DbSchema.OrderSchema.COLUMN_USER_ID + "=? AND " +
                            DbSchema.OrderSchema.COLUMN_MAHAK_ID + "=? AND " +
                            DbSchema.OrderSchema.COLUMN_DATABASE_ID + "=? AND " +
                            DbSchema.OrderSchema.COLUMN_TYPE + " =? ",
                    new String[]{String.valueOf(getPrefUserId()),
                            BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId(),
                            String.valueOf(ProjectInfo.TYPE_SEND_TRANSFERENCE)}, null, null, orderBy);

            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    order = getOrderFromCursor(cursor);
                    if (order != null)
                        array.add(order);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllOrder", e.getMessage());
        }

        return array;
    }

    public ArrayList<ReceivedTransfers> getAllReceivedTransfer() {
        ReceivedTransfers receivedTransfers;
        Cursor cursor;
        ArrayList<ReceivedTransfers> array = new ArrayList<>();
        try {
            String orderBy = DbSchema.ReceivedTransfersSchema.COLUMN_ModifyDate + " DESC";
            cursor = mDb.query(DbSchema.ReceivedTransfersSchema.TABLE_NAME, null,
                    DbSchema.ReceivedTransfersSchema.COLUMN_MahakId + "=? AND " + DbSchema.ReceivedTransfersSchema.COLUMN_DatabaseId + "=? AND " + DbSchema.ReceivedTransfersSchema.COLUMN_ReceiverVisitorId + "=? ", new String[]{BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId(), String.valueOf(BaseActivity.getPrefUserMasterId())},
                    null, null, orderBy);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    receivedTransfers = GetReceivedTransfer(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransfersSchema.COLUMN_TransferStoreId)));
                    if (receivedTransfers != null)
                        array.add(receivedTransfers);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllOrder", e.getMessage());
        }

        return array;
    }

    public ArrayList<ReceivedTransferProducts> getReceivedTransferProducts(String transferStoreId) {
        ReceivedTransferProducts receivedTransferProducts;
        Cursor cursor;
        ArrayList<ReceivedTransferProducts> array = new ArrayList<>();
        try {

            cursor = mDb.query(
                    DbSchema.ReceivedTransferProductsSchema.TABLE_NAME,
                    null,
                    DbSchema.ReceivedTransferProductsSchema.COLUMN_MahakId + "=? AND "
                            + DbSchema.ReceivedTransferProductsSchema.COLUMN_DatabaseId + "=? AND "
                            + DbSchema.ReceivedTransferProductsSchema.COLUMN_TransferId + "=? "
                    , new String[]{BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId(), transferStoreId},
                    null,
                    null,
                    null);

            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    receivedTransferProducts = GetReceivedTransferProduct(cursor.getString(cursor.getColumnIndex(DbSchema.ReceivedTransferProductsSchema.COLUMN_TransferStoreDetailId)));
                    if (receivedTransferProducts != null)
                        array.add(receivedTransferProducts);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllOrder", e.getMessage());
        }

        return array;
    }

    public ArrayList<Receipt> getAllReceipt() {
        Receipt receipt;
        Cursor cursor;
        ArrayList<Receipt> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.ReceiptSchema.TABLE_NAME, null, DbSchema.ReceiptSchema.COLUMN_USER_ID + "=? AND  " + DbSchema.ReceiptSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.ReceiptSchema.COLUMN_DATABASE_ID + "=?", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    receipt = GetReceipt(cursor.getLong(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_ID)));
                    if (receipt != null)
                        array.add(receipt);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllReceipt", e.getMessage());
        }

        return array;
    }

    public ArrayList<Receipt> getAllReceiptWithTrackingCode(String code) {
        Receipt receipt;
        Cursor cursor;
        ArrayList<Receipt> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.ReceiptSchema.TABLE_NAME, null, DbSchema.ReceiptSchema.COLUMN_USER_ID + "=? AND  " + DbSchema.ReceiptSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.ReceiptSchema.COLUMN_DATABASE_ID + "=? and " + DbSchema.ReceiptSchema.COLUMN_CODE + "=? ", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId(), code }, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    receipt = GetReceipt(cursor.getLong(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_ID)));
                    if (receipt != null)
                        array.add(receipt);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllReceipt", e.getMessage());
        }

        return array;
    }

    public ArrayList<Receipt> getAllReceiptNotPublished() {
        Receipt receipt;
        Cursor cursor;
        ArrayList<Receipt> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.ReceiptSchema.TABLE_NAME, null, DbSchema.ReceiptSchema.COLUMN_USER_ID + "=? AND  " + DbSchema.ReceiptSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.ReceiptSchema.COLUMN_PUBLISH + "=? AND " + DbSchema.ReceiptSchema.COLUMN_DATABASE_ID + "=?", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), String.valueOf(ProjectInfo.DONT_PUBLISH), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    receipt = GetReceipt(cursor.getLong(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_ID)));
                    if (receipt != null)
                        array.add(receipt);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllReceipt", e.getMessage());
        }

        return array;
    }

    public ArrayList<PayableTransfer> getAllPayable() {
        PayableTransfer payableTransfer;
        Cursor cursor;
        ArrayList<PayableTransfer> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.PayableSchema.TABLE_NAME, null, DbSchema.PayableSchema.COLUMN_USER_ID + "=? AND  " + DbSchema.PayableSchema.COLUMN_MahakId + "=? AND " + DbSchema.PayableSchema.COLUMN_DatabaseId + "=?", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    payableTransfer = GetPayable(cursor.getLong(cursor.getColumnIndex(DbSchema.PayableSchema.COLUMN_ID)));
                    if (payableTransfer != null)
                        array.add(payableTransfer);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllReceipt", e.getMessage());
        }

        return array;
    }

    public ArrayList<Cheque> getAllCheque(long receiptClientId) {
        Cheque cheque;
        Cursor cursor;
        ArrayList<Cheque> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.ChequeSchema.TABLE_NAME, null, DbSchema.ChequeSchema.COLUMN_ReceiptClientId + "=?", new String[]{String.valueOf(receiptClientId)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    cheque = GetCheque(cursor.getLong(cursor.getColumnIndex(DbSchema.ChequeSchema.COLUMN_ID)));
                    if (cheque != null)
                        array.add(cheque);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllCheque", e.getMessage());
        }

        return array;
    }

    public ArrayList<CheckList> getAllDoneChecklistNotPublish() {
        CheckList checklist;
        Cursor cursor;
        ArrayList<CheckList> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.CheckListSchema.TABLE_NAME, null, DbSchema.CheckListSchema.COLUMN_USER_ID + "=? AND " + DbSchema.CheckListSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.CheckListSchema.COLUMN_STATUS + "=? AND " + DbSchema.CheckListSchema.COLUMN_PUBLISH + "=? AND " + DbSchema.CheckListSchema.COLUMN_DATABASE_ID + "=?", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), "3", String.valueOf(ProjectInfo.DONT_PUBLISH), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    checklist = GetCheckList(cursor.getLong(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_ID)));
                    if (checklist != null)
                        array.add(checklist);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllDoneChecklist", e.getMessage());
        }

        return array;
    }

    public ArrayList<CheckList> getAllChecklistNotPublished() {
        CheckList checklist;
        Cursor cursor;
        ArrayList<CheckList> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.CheckListSchema.TABLE_NAME, null, DbSchema.CheckListSchema.COLUMN_USER_ID + "=? AND " + DbSchema.CheckListSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.CheckListSchema.COLUMN_PUBLISH + "=? AND " + DbSchema.CheckListSchema.COLUMN_DATABASE_ID + "=?", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), String.valueOf(ProjectInfo.DONT_PUBLISH), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    checklist = GetCheckList(cursor.getLong(cursor.getColumnIndex(DbSchema.CheckListSchema.COLUMN_ID)));
                    if (checklist != null)
                        array.add(checklist);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllChecklist", e.getMessage());
        }

        return array;
    }

    public int getDefVisitorPriceLevel() {
        int def = 0;
        try {
            Cursor cursor = mDb.rawQuery("select Sell_DefaultCostLevel from Visitors where VisitorId =? ", new String[]{String.valueOf(getPrefUserId())});
            if (cursor != null) {
                cursor.moveToFirst();
                def = cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorSchema.COLUMN_Sell_DefaultCostLevel));
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public int getDefCustomerPriceLevel(int personId) {
        int def = 0;
        try {
            Cursor cursor = mDb.rawQuery("select SellPriceLevel from Customers where personid = ? and UserId = ? ", new String[]{String.valueOf(personId), String.valueOf(getPrefUserId())});
            if (cursor != null) {
                cursor.moveToFirst();
                def = cursor.getInt(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_SellPriceLevel));
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public int getDefGroupCustomerPriceLevel(long PersonGroupId) {
        int def = 0;
        try {
            Cursor cursor = mDb.rawQuery("select SellPriceLevel from CustomersGroups where UserId = ?  and PersonGroupId = ?  ", new String[]{String.valueOf(getPrefUserId()), String.valueOf(PersonGroupId)});
            if (cursor != null) {
                cursor.moveToFirst();
                def = cursor.getInt(cursor.getColumnIndex(DbSchema.CustomersGroupSchema.COLUMN_SellPriceLevel));
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public ArrayList<TransactionsLog> getAllTransactionlog(int customerid) {
        TransactionsLog transactionlog;
        Cursor cursor;
        ArrayList<TransactionsLog> array = new ArrayList<>();
        try {
            //cursor = mDb.query(DbSchema.Transactionslogschema.TABLE_NAME, null, DbSchema.Transactionslogschema.COLUMN_PersonId + "=?", new String[]{String.valueOf(customerid)}, null, null,DbSchema.Transactionslogschema.COLUMN_DATE + "ASC");
            //cursor = mDb.rawQuery("select * from "+DbSchema.Transactionslogschema.TABLE_NAME+ " where "+DbSchema.Transactionslogschema.COLUMN_PersonId +"="+customerid+" ORDER BY" + DbSchema.Transactionslogschema.COLUMN_DATE,DbSchema.Transactionslogschema.COLUMN_BANK_CODE + "ASC",null);
            cursor = mDb.rawQuery("select * from " + DbSchema.TransactionsLogSchema.TABLE_NAME + " where " + DbSchema.TransactionsLogSchema.COLUMN_PersonId + "=" + customerid + " ORDER BY " + DbSchema.TransactionsLogSchema.COLUMN_DATE + "," + DbSchema.TransactionsLogSchema.COLUMN_TransactionCode + " ASC", null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    transactionlog = getTransactionslog(cursor.getLong(cursor.getColumnIndex(DbSchema.TransactionsLogSchema.COLUMN_ID)));
                    array.add(transactionlog);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllTransactionlog", e.getMessage());
        }

        return array;
    }

    public ArrayList<Order> getAllOrderNotPublish(long userId) {
        Order order;
        Cursor cursor;
        ArrayList<Order> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null, DbSchema.OrderSchema.COLUMN_USER_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_PUBLISH + "=? AND " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_TYPE + "=? ", new String[]{String.valueOf(userId), String.valueOf(ProjectInfo.DONT_PUBLISH), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId(), String.valueOf(ProjectInfo.TYPE_ORDER)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    order = getOrderFromCursor(cursor);
                    if (order != null)
                        array.add(order);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllOrder", e.getMessage());
        }

        return array;
    }

    public ArrayList<Order> getAllOrderFamily(long userId) {
        Order order;
        Cursor cursor;
        ArrayList<Order> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null, DbSchema.OrderSchema.COLUMN_USER_ID + " =? AND " + DbSchema.OrderSchema.COLUMN_PUBLISH + " =? AND " + " ( " + DbSchema.OrderSchema.COLUMN_TYPE + " =? or " + DbSchema.OrderSchema.COLUMN_TYPE + " =? or " + DbSchema.OrderSchema.COLUMN_TYPE + " =? " + " ) ", new String[]{String.valueOf(userId), String.valueOf(ProjectInfo.DONT_PUBLISH), String.valueOf(ProjectInfo.TYPE_INVOCIE), String.valueOf(ProjectInfo.TYPE_ORDER), String.valueOf(ProjectInfo.TYPE_RETURN_OF_SALE)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    order = getOrderFromCursor(cursor);
                    if (order != null)
                        array.add(order);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllInvoice", e.getMessage());
        }
        return array;
    }

    public ArrayList<Order> getAllOrderWithPersonClientId(long personClientId) {
        Order order;
        Cursor cursor;
        ArrayList<Order> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null, DbSchema.OrderSchema.COLUMN_PersonClientId + " =? AND " + DbSchema.OrderSchema.COLUMN_USER_ID + " =? " , new String[]{String.valueOf(personClientId), String.valueOf(getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    order = getOrderFromCursor(cursor);
                    if (order != null)
                        array.add(order);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllInvoice", e.getMessage());
        }
        return array;
    }

    public ArrayList<NonRegister> getAllNonRegisterNotPublish(long userId) {

        NonRegister nonRegister;
        Cursor cursor;
        ArrayList<NonRegister> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.NonRegisterSchema.TABLE_NAME, null, DbSchema.NonRegisterSchema.COLUMN_USER_ID + " =? AND " + DbSchema.NonRegisterSchema.COLUMN_PUBLISH + " =? AND " + DbSchema.NonRegisterSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.NonRegisterSchema.COLUMN_DATABASE_ID + " =? ", new String[]{String.valueOf(userId), String.valueOf(ProjectInfo.DONT_PUBLISH), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    nonRegister = GetNonRegister(cursor.getLong(cursor.getColumnIndex(DbSchema.NonRegisterSchema.COLUMN_ID)));
                    if (nonRegister != null)
                        array.add(nonRegister);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllInvoice", e.getMessage());
        }
        return array;
    }

    public ArrayList<Order> getAllTransferNotPublish(long userId, String transferCode) {
        Order order;
        Cursor cursor;
        ArrayList<Order> array = new ArrayList<>();
        if (transferCode.equals("")) {
            try {
                cursor = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null, DbSchema.OrderSchema.COLUMN_USER_ID + " =? AND " + DbSchema.OrderSchema.COLUMN_PUBLISH + " =? AND " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + " =? AND " + DbSchema.OrderSchema.COLUMN_TYPE + " =? ", new String[]{String.valueOf(userId), String.valueOf(ProjectInfo.DONT_PUBLISH), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId(), String.valueOf(ProjectInfo.TYPE_SEND_TRANSFERENCE)}, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        order = getOrderFromCursor(cursor);
                        if (order != null)
                            array.add(order);
                        cursor.moveToNext();
                    }
                    cursor.close();
                }

            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                FirebaseCrashlytics.getInstance().recordException(e);
                Log.e("ErrAllTransfer", e.getMessage());
            }
            return array;
        } else {
            try {
                cursor = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null, DbSchema.OrderSchema.COLUMN_USER_ID + " =? AND " + DbSchema.OrderSchema.COLUMN_CODE + " =? AND " + DbSchema.OrderSchema.COLUMN_PUBLISH + " =? AND " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + " =? AND " + DbSchema.OrderSchema.COLUMN_TYPE + " =? ", new String[]{String.valueOf(userId), transferCode, String.valueOf(ProjectInfo.DONT_PUBLISH), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId(), String.valueOf(ProjectInfo.TYPE_SEND_TRANSFERENCE)}, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        order = getOrderFromCursor(cursor);
                        if (order != null)
                            array.add(order);
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                FirebaseCrashlytics.getInstance().recordException(e);
                Log.e("ErrorGetTransfer", e.getMessage());
            }
            return array;
        }

    }

    public ArrayList<Receipt> getAllReceiptNotPublish(long userId) {
        Receipt receipt;
        Cursor cursor;
        ArrayList<Receipt> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.ReceiptSchema.TABLE_NAME, null, DbSchema.ReceiptSchema.COLUMN_USER_ID + " =? AND " + DbSchema.ReceiptSchema.COLUMN_PUBLISH + " =? AND " + DbSchema.ReceiptSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.ReceiptSchema.COLUMN_DATABASE_ID + "=?", new String[]{String.valueOf(userId), String.valueOf(ProjectInfo.DONT_PUBLISH), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    receipt = GetReceipt(cursor.getLong(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_ID)));
                    if (receipt != null)
                        array.add(receipt);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllReceipt", e.getMessage());
        }

        return array;
    }

    public ArrayList<Receipt> getAllReceiptWithPersonClientId(long personClientId) {
        Receipt receipt;
        Cursor cursor;
        ArrayList<Receipt> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.ReceiptSchema.TABLE_NAME, null, DbSchema.ReceiptSchema.COLUMN_PersonClientId + " =? AND " + DbSchema.ReceiptSchema.COLUMN_USER_ID , new String[]{String.valueOf(personClientId), String.valueOf(getPrefUserId())}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    receipt = GetReceipt(cursor.getLong(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_ID)));
                    if (receipt != null)
                        array.add(receipt);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllReceipt", e.getMessage());
        }

        return array;
    }

    public ArrayList<PayableTransfer> getAllPayableNotPublish(long userId) {
        PayableTransfer payableTransfer;
        Cursor cursor;
        ArrayList<PayableTransfer> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.PayableSchema.TABLE_NAME, null, DbSchema.PayableSchema.COLUMN_USER_ID + " =? AND " + DbSchema.PayableSchema.COLUMN_PUBLISH + " =? AND " + DbSchema.PayableSchema.COLUMN_MahakId + "=? AND " + DbSchema.PayableSchema.COLUMN_DatabaseId + "=?", new String[]{String.valueOf(userId), String.valueOf(ProjectInfo.DONT_PUBLISH), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId()}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    payableTransfer = GetPayable(cursor.getLong(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_ID)));
                    if (payableTransfer != null)
                        array.add(payableTransfer);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllReceipt", e.getMessage());
        }

        return array;
    }

    public ArrayList<Customer> getAllCustomerForUpdate() {
        Customer customer;
        Cursor cursor;
        String orderBy = DbSchema.CustomerSchema.COLUMN_PersonCode;
        ArrayList<Customer> array = new ArrayList<>();
        try {
            //cursor = mDb.query(DbSchema.Customerschema.TABLE_NAME, new String[]{DbSchema.Customerschema.COLUMN_ID,DbSchema.Customerschema.COLUMN_PersonId,DbSchema.Customerschema.COLUMN_PersonGroupId,DbSchema.Customerschema.COLUMN_ADDRESS,DbSchema.Customerschema.COLUMN_PHONE,DbSchema.Customerschema.COLUMN_MOBILE,DbSchema.Customerschema.COLUMN_LATITUDE,DbSchema.Customerschema.COLUMN_LONGITUDE}, DbSchema.Customerschema.COLUMN_USER_ID + " =? AND " + DbSchema.Customerschema.COLUMN_DATABASE_ID + "=? " , new String[]{String.valueOf(BaseActivity.getPrefUserId()), BaseActivity.getPrefDatabaseId()}, null, null, orderBy);
            cursor = mDb.query(DbSchema.CustomerSchema.TABLE_NAME, null, DbSchema.CustomerSchema.COLUMN_USER_ID + " =? AND " + DbSchema.CustomerSchema.COLUMN_PersonId + " !=? and " + DbSchema.CustomerSchema.COLUMN_DATABASE_ID + "=? and " + DbSchema.CustomerSchema.COLUMN_PUBLISH + " =? ", new String[]{String.valueOf(getPrefUserId()), String.valueOf(0), BaseActivity.getPrefDatabaseId() , String.valueOf(ProjectInfo.DONT_PUBLISH)}, null, null, orderBy);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    customer = getCustomerFromCursor(cursor);
                    array.add(customer);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrCustomerForUpdate", e.getMessage());
        }

        return array;
    }

    public ArrayList<Customer> getAllNewCustomer() {
        Customer customer;
        Cursor cursor;
        String orderBy = DbSchema.CustomerSchema.COLUMN_PersonCode;
        ArrayList<Customer> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.CustomerSchema.TABLE_NAME, null, DbSchema.CustomerSchema.COLUMN_USER_ID + " =? AND " + DbSchema.CustomerSchema.COLUMN_DATABASE_ID + "=? AND " + DbSchema.CustomerSchema.COLUMN_PersonId + " =? ", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefDatabaseId(), String.valueOf(0)}, null, null, orderBy);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    customer = getCustomer(cursor.getLong(cursor.getColumnIndex(DbSchema.CustomerSchema.COLUMN_ID)));
                    if (customer != null)
                        array.add(customer);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllNewCustomer", e.getMessage());
        }

        return array;
    }

    public ArrayList<Order> getAllDeliveryOrderNotFinal() {
        Order order;
        Cursor cursor;
        ArrayList<Order> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.OrderSchema.TABLE_NAME, null, DbSchema.OrderSchema.COLUMN_USER_ID + " =? AND " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "=? AND " + DbSchema.OrderSchema.COLUMN_TYPE + "=? AND " + DbSchema.OrderSchema.COLUMN_ISFINAL + " =? ", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefDatabaseId(), String.valueOf(ProjectInfo.TYPE_Delivery), String.valueOf(ProjectInfo.NOt_FINAL)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    order = getDeliveryOrder(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID)));
                    if (order != null)
                        array.add(order);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Err", e.getMessage());
        }

        return array;
    }

    public ArrayList<OrderDetail> getAllDeliveryOrder(long deliveryid) {
        OrderDetail orderDetail;
        Cursor cursor;
        ArrayList<OrderDetail> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.OrderDetailSchema.TABLE_NAME, null, DbSchema.OrderDetailSchema.COLUMN_OrderId + "=?", new String[]{String.valueOf(deliveryid)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    orderDetail = getDeliveryOrderDetail(cursor.getLong(cursor.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ID)));
                    if (orderDetail != null)
                        array.add(orderDetail);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Err", e.getMessage());
        }
        return array;
    }

    public ArrayList<Receipt> getAllReceipt(String code) {

        Receipt receipt;
        Cursor cursor;
        ArrayList<Receipt> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.ReceiptSchema.TABLE_NAME, null, DbSchema.ReceiptSchema.COLUMN_USER_ID + "=? AND  " + DbSchema.ReceiptSchema.COLUMN_MAHAK_ID + "=? AND " + DbSchema.ReceiptSchema.COLUMN_DATABASE_ID + "=? AND " + DbSchema.ReceiptSchema.COLUMN_CODE + "=? ", new String[]{String.valueOf(getPrefUserId()), BaseActivity.getPrefMahakId(), BaseActivity.getPrefDatabaseId(), code}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    receipt = GetReceipt(cursor.getLong(cursor.getColumnIndex(DbSchema.ReceiptSchema.COLUMN_ID)));
                    if (receipt != null)
                        array.add(receipt);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllReceipt", e.getMessage());
        }

        return array;

    }

    public ArrayList<ReportUserDetail> getCustomerSaleDetail(long startdate, long enddate, int orderType) {

        Cursor cursor;
        ReportUserDetail item;
        ArrayList<ReportUserDetail> items = new ArrayList<>();

        String str_guest_customer = "مشتری مهمان";
        String strSelect = "select " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_PersonId + " , ifnull(" + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_NAME + ",'" + str_guest_customer + "') as Name"
                + " , SUM(" + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_Price + " * " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz + ") as sump from " + DbSchema.OrderSchema.TABLE_NAME
                + " INNER JOIN " + DbSchema.OrderDetailSchema.TABLE_NAME
                + " ON " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ID + " = " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_OrderId
                + " LEFT JOIN " + DbSchema.CustomerSchema.TABLE_NAME
                + " ON " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_PersonId + " = " + DbSchema.CustomerSchema.TABLE_NAME + "." + DbSchema.CustomerSchema.COLUMN_PersonId
                + " Where " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " > " + startdate + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " <= " + enddate
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_TYPE + " == " + orderType
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_DATABASE_ID + " ='" + BaseActivity.getPrefDatabaseId() + "'"
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_MAHAK_ID + " ='" + BaseActivity.getPrefMahakId() + "'"
                + " Group by " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_PersonId
                + " Having sump > 0";

        try {
            cursor = mDb.rawQuery(strSelect, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    item = new ReportUserDetail();
                    item.setId(cursor.getInt(0));
                    item.setName(cursor.getString(1));
                    item.setSale(cursor.getDouble(2));
                    items.add(item);
                    cursor.moveToNext();
                }
                cursor.close();
            }
            for (int i = 0; i < items.size(); i++) {
                items.get(i).setCountOfProduct(getIntervalCountOfProduct(startdate, enddate, orderType, items.get(i).getId()));//get count of product
                items.get(i).setTaxCharge(getIntervalTaxCharge3(startdate, enddate, orderType, items.get(i).getId()));//get tax and charge
                items.get(i).setDiscount(getIntervalDiscount(startdate, enddate, orderType, items.get(i).getId()));//get discount
                items.get(i).setCashAmount(getIntervalReceipt(startdate, enddate, ProjectInfo.TYPE_CASH, items.get(i).getId()));
                items.get(i).setChequeAmount(getIntervalReceipt(startdate, enddate, ProjectInfo.TYPE_CHEQUE, items.get(i).getId()));
                items.get(i).setCashReceiptAmount(getIntervalReceipt(startdate, enddate, ProjectInfo.TYPE_CASH_RECEIPT, items.get(i).getId()));
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return items;
    }

    public ArrayList<ReportProductDetail> getProductSaleDetail(long startdate, long enddate, int orderType) {

        Cursor cursor;
        ReportProductDetail item;
        ArrayList<ReportProductDetail> items = new ArrayList<>();

        String strSelect = "select  OrderDetail.ProductId , SUM(OrderDetail.SumCountBaJoz), SUM(OrderDetail.Price * OrderDetail.SumCountBaJoz) as sump"
                + " , SUM((((OrderDetail.Price * OrderDetail.SumCountBaJoz) * OrderDetail.Discount) / 100)) as offamount,"
                + " SUM(((OrderDetail.Price * OrderDetail.SumCountBaJoz) - (((OrderDetail.Price * OrderDetail.SumCountBaJoz) * OrderDetail.Discount) / 100))) as netsale from OrderDetail "
                + " INNER JOIN Orders "
                + " ON Orders.Id = OrderDetail.OrderId "
                + " Where " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " > " + startdate + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " <= " + enddate
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_TYPE + " == " + orderType
                + " AND " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_DATABASE_ID + " ='" + BaseActivity.getPrefDatabaseId() + "'"
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_MAHAK_ID + " ='" + BaseActivity.getPrefMahakId() + "'"
                + " group by OrderDetail.ProductId";

        try {
            cursor = mDb.rawQuery(strSelect, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    item = new ReportProductDetail();
                    item.setName(ServiceTools.getProductName(cursor.getInt(0),mContext));
                    item.setAsset(ServiceTools.getSumCount1(cursor.getInt(0),mContext));
                    item.setCount(cursor.getDouble(1));
                    item.setSale(cursor.getDouble(2));
                    item.setDiscount(cursor.getDouble(3));
                    item.setNetSale(cursor.getDouble(4));

                    /*item.setName(cursor.getString(0));
                    item.setCount(cursor.getDouble(1));
                    item.setSale(cursor.getDouble(2));
                    item.setAsset(cursor.getDouble(3));
                    item.setDiscount(cursor.getDouble(4));
                    item.setNetSale(cursor.getDouble(5));*/

                    items.add(item);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return items;
    }

    public ArrayList<GroupedTax> getGroupedTaxCharge(long Id) {
        ArrayList<GroupedTax> groupedTaxArrayList = new ArrayList<>();
        GroupedTax groupedTax = new GroupedTax();
        Cursor cursor2;
        try {
            String strSelect =
                    "select " + DbSchema.OrderDetailSchema.COLUMN_TaxPercent
                            + " , SUM(" + DbSchema.OrderDetailSchema.COLUMN_Price + " * " + DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz + " * " + DbSchema.OrderDetailSchema.COLUMN_TaxPercent + "/" + " 100 " + ") as sump from "
                            + DbSchema.OrderDetailSchema.TABLE_NAME + " Where " + DbSchema.OrderDetailSchema.COLUMN_OrderId + " == " + Id
                            + " Group by " + DbSchema.OrderDetailSchema.COLUMN_TaxPercent;

            cursor2 = mDb.rawQuery(strSelect, null);
            if (cursor2 != null) {
                cursor2.moveToFirst();
                while (!cursor2.isAfterLast()) {
                    groupedTax = new GroupedTax();
                    groupedTax.setTaxPercent(cursor2.getDouble(0));
                    groupedTax.setSumPrice(cursor2.getDouble(1));
                    if (groupedTax.getTaxPercent() > 0)
                        groupedTaxArrayList.add(groupedTax);
                    cursor2.moveToNext();
                }//End of while
                cursor2.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }
        return groupedTaxArrayList;
    }

    public ArrayList<CityZone_Extra_Data> getStates() {
        CityZone_Extra_Data cityZone_extra_data = new CityZone_Extra_Data();
        ArrayList<CityZone_Extra_Data> cityZoneExtraDataArrayList = new ArrayList<>();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.CityZoneSchema.TABLE_NAME, null, DbSchema.CityZoneSchema.COLUMN_ParentCode + " =? ", new String[]{String.valueOf(0)}, null, null, DbSchema.CityZoneSchema.COLUMN_ZoneName);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    cityZone_extra_data = getCityZone_extra_data(cursor);
                    cityZoneExtraDataArrayList.add(cityZone_extra_data);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrgetMorCustInfo", e.getMessage());
        }
        return cityZoneExtraDataArrayList;
    }

    public ArrayList<Region> getStates2() {
        Region region = new Region();
        ArrayList<Region> regions = new ArrayList<>();
        Cursor cursor;
        try {
            cursor = mDb.rawQuery("select * from Region where ((CityID % 100000)  = 0 or ProvinceID = 0) and userid = ? order by ProvinceName " , new String[]{String.valueOf(BaseActivity.getPrefUserId())});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    region = getRegionFromCursor(cursor);
                    regions.add(region);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrgetMorCustInfo", e.getMessage());
        }
        return regions;
    }

    public Region getRegionWithCityId(int cityId) {
        Region region = new Region();
        Cursor cursor;
        try {
            cursor = mDb.rawQuery("select * from Region where CityID =? " , new String[]{String.valueOf(cityId)});
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    region = getRegionFromCursor(cursor);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrgetMorCustInfo", e.getMessage());
        }
        return region;
    }

    public ArrayList<Region> getCities(int ProvinceID) {
        Region region = new Region();
        ArrayList<Region> regions = new ArrayList<>();
        Cursor cursor;
        try {
            cursor = mDb.rawQuery("select * from Region where ProvinceID =? and userid =? order by CityName " , new String[]{String.valueOf(ProvinceID), String.valueOf(BaseActivity.getPrefUserId())});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    region = getRegionFromCursor(cursor);
                    regions.add(region);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrgetMorCustInfo", e.getMessage());
        }
        return regions;
    }

    public ArrayList<Setting> getAllSettings() {
        Setting setting;
        Cursor cursor;
        ArrayList<Setting> array = new ArrayList<>();
        try {
            cursor = mDb.rawQuery("select * from Setting where deleted = 0  and userid =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    setting = getSettingFromCursor(cursor);
                    array.add(setting);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("@Error", this.getClass().getName() + " - L:2036 - " + e.getMessage());
        }
        return array;
    }

    public ArrayList<Setting> getAllSettingsWithVisitorId(long visitorId) {
        Setting setting;
        Cursor cursor;
        ArrayList<Setting> array = new ArrayList<>();
        try {
            cursor = mDb.query(DbSchema.SettingSchema.TABLE_NAME, null, DbSchema.SettingSchema.COLUMN_USER_ID + " =? And " + DbSchema.SettingSchema.COLUMN_Deleted + " =? ", new String[]{String.valueOf(visitorId), String.valueOf(0)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    setting = getSettingFromCursor(cursor);
                    array.add(setting);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("@Error", this.getClass().getName() + " - L:2036 - " + e.getMessage());
        }
        return array;
    }

    public List<PicturesProduct> getAllPictureByProductId(long productId) {
        PicturesProduct picturesProduct;
        Cursor cursor;
        ArrayList<PicturesProduct> array = new ArrayList<>();
        try {
            cursor = mDb.rawQuery("select PicturesProduct.* , itemCode from PicturesProduct left join PhotoGallery on PicturesProduct.pictureId = PhotoGallery.pictureId where ( itemcode =? or itemid =? ) and PicturesProduct.userid =? order by PicturesProduct.pictureId ", new String[]{String.valueOf(productId), String.valueOf(getPrefUserId())});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    picturesProduct = getPictureProductFromCursor(cursor);
                    if (picturesProduct.getUrl() != null)
                        array.add(picturesProduct);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllReceipt", e.getMessage());
        }

        return array;
    }

    public List<PicturesProduct> getAllPictureByProductId2(long productId , long productCode) {
        PicturesProduct picturesProduct;
        Cursor cursor;
        ArrayList<PicturesProduct> array = new ArrayList<>();
        try {
            cursor = mDb.rawQuery("select PicturesProduct.* , itemCode from PicturesProduct left join PhotoGallery on PicturesProduct.pictureId = PhotoGallery.pictureId where ( itemcode =? or itemid =? ) and PhotoGallery.userid =? order by PicturesProduct.pictureId ", new String[]{String.valueOf(productId),String.valueOf(productCode), String.valueOf(getPrefUserId())});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    picturesProduct = getPictureProductFromCursor(cursor);
                    if (picturesProduct.getUrl() != null)
                        array.add(picturesProduct);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllReceipt", e.getMessage());
        }

        return array;
    }

    public List<PicturesProduct> getAllSignWithoutUrl() {
        PicturesProduct picturesProduct;
        Cursor cursor;
        ArrayList<PicturesProduct> array = new ArrayList<>();
        try {
            cursor = mDb.rawQuery("Select * from " + DbSchema.PicturesProductSchema.TABLE_NAME + " Where " + DbSchema.PicturesProductSchema.COLUMN_ITEM_TYPE + " = 2 and " + DbSchema.PicturesProductSchema.COLUMN_URL + " IS NULL ", null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    picturesProduct = getPictureProductFromCursor(cursor);
                    array.add(picturesProduct);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrAllReceipt", e.getMessage());
        }

        return array;
    }

    public ReportMonth getPriceReceipt(long startdate, long enddate) {

        Cursor cursor;
        ReportMonth object;
        ArrayList<ReportMonth> arrayReportMonth = new ArrayList<>();
        ReportMonth reportMonth = new ReportMonth();
        double TotalCashAmount = 0, TotalCheque = 0, TotalCashReceipt = 0;

        String strSelectt = "select " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_ID + "," + DbSchema.ReceiptSchema.COLUMN_CASHAMOUNT + "," +
                "( select sum(ch1.Amount)  From Receipts as rc1 JOIN Cheques as ch1 on ch1.ReceiptId=rc1.Id where ch1.type=1 and rc1.id= receipts.id   group by  rc1.Id ) as checksum ," +
                "( select sum(ch1.Amount)  From Receipts as rc1 JOIN Cheques as ch1 on ch1.ReceiptId=rc1.Id where ch1.type=2 and rc1.id= receipts.id   group by  rc1.Id ) as recsum" +
                " from " + DbSchema.ReceiptSchema.TABLE_NAME +
                " where " + DbSchema.ReceiptSchema.COLUMN_USER_ID + "=" + getPrefUserId() + " and " +
                DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_DATABASE_ID + "='" + BaseActivity.getPrefDatabaseId() + "' and " +
                DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_MAHAK_ID + "='" + BaseActivity.getPrefMahakId() + "' and " +
                DbSchema.ReceiptSchema.COLUMN_DATE + ">=" + startdate + " and " + DbSchema.ReceiptSchema.COLUMN_DATE + "<" + enddate;
        try {
            cursor = mDb.rawQuery(strSelectt, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    object = new ReportMonth();
                    object.setId(cursor.getLong(0));// get column id
                    object.setCashAmount(cursor.getLong(1));// get column CashReceipt
                    object.setCheque(cursor.getInt(2));// get column Cheque
                    object.setCashReceipt(cursor.getLong(3));//get column Receipt
                    arrayReportMonth.add(object);
                    cursor.moveToNext();
                }//End of while
                cursor.close();
            }
            for (ReportMonth item : arrayReportMonth) {
                TotalCashAmount = TotalCashAmount + item.getCashAmount();
                TotalCashReceipt = TotalCashReceipt + item.getCashReceipt();
                TotalCheque = TotalCheque + item.getCheque();
            }// end of for

            reportMonth.setStatusAnimation(false);
            reportMonth.setCashAmount(TotalCashAmount);
            reportMonth.setCashReceipt(TotalCashReceipt);
            reportMonth.setCheque(TotalCheque);
            reportMonth.setPrice(TotalCashAmount + TotalCashReceipt + TotalCheque);

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return reportMonth;
    }

    public double getIntervalSale(long startdate, long enddate, int orderType) {
        Cursor cursor;
        double sum = 0;
        String strSelect = "select ifnull(SUM(" + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_Price + " * " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz + "),'0') from " + DbSchema.OrderSchema.TABLE_NAME
                + " INNER JOIN " + DbSchema.OrderDetailSchema.TABLE_NAME
                + " ON " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ID + " = " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_OrderId
                + " Where " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " > " + startdate + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " <= " + enddate
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_TYPE + " == " + orderType
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_DATABASE_ID + " ='" + BaseActivity.getPrefDatabaseId() + "'"
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_MAHAK_ID + " ='" + BaseActivity.getPrefMahakId() + "'";
        try {
            cursor = mDb.rawQuery(strSelect, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    sum = cursor.getDouble(0);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return sum;
    }

    public double getIntervalDiscount(long startdate, long enddate, int orderType) {
        Cursor cursor;
        double sum = 0;
        String strSelect = "select("
                + " select ifnull(SUM(((" + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_Price + " * " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz + ") * " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_Discount + ") / 100),'0') from " + DbSchema.OrderDetailSchema.TABLE_NAME
                + " INNER JOIN " + DbSchema.OrderSchema.TABLE_NAME
                + " ON " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ID + " = " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_OrderId
                + " Where " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " > " + startdate + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " <= " + enddate
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_TYPE + " == " + orderType
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_DATABASE_ID + " ='" + BaseActivity.getPrefDatabaseId() + "'"
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_MAHAK_ID + " ='" + BaseActivity.getPrefMahakId() + "'"
                + " )+("
                + " select ifnull(SUM(" + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_DISCOUNT + "),'0') from " + DbSchema.OrderSchema.TABLE_NAME
                + " Where " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " > " + startdate + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " <= " + enddate
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_TYPE + " == " + orderType
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_DATABASE_ID + " ='" + BaseActivity.getPrefDatabaseId() + "'"
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_MAHAK_ID + " ='" + BaseActivity.getPrefMahakId() + "'"
                + " ) as totalsum";
        try {
            cursor = mDb.rawQuery(strSelect, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    sum = cursor.getDouble(0);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return sum;
    }

    private double getIntervalDiscount(long startdate, long enddate, int orderType, int customerId) {
        Cursor cursor;
        double sum = 0;
        String strSelect = "select("
                + " select ifnull(SUM(((" + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_Price + " * " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz + ") * " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_Discount + ") / 100),'0') from " + DbSchema.OrderDetailSchema.TABLE_NAME
                + " INNER JOIN " + DbSchema.OrderSchema.TABLE_NAME
                + " ON " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ID + " = " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_OrderId
                + " Where " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " > " + startdate + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " <= " + enddate
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_TYPE + " == " + orderType
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_PersonId + " == " + customerId
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_DATABASE_ID + " ='" + BaseActivity.getPrefDatabaseId() + "'"
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_MAHAK_ID + " ='" + BaseActivity.getPrefMahakId() + "'"
                + " )+("
                + " select ifnull(SUM(" + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_DISCOUNT + "),'0') from " + DbSchema.OrderSchema.TABLE_NAME
                + " Where " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " > " + startdate + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " <= " + enddate
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_TYPE + " == " + orderType
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_PersonId + " == " + customerId
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_DATABASE_ID + " ='" + BaseActivity.getPrefDatabaseId() + "'"
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_MAHAK_ID + " ='" + BaseActivity.getPrefMahakId() + "'"
                + " ) as totalsum";
        try {
            cursor = mDb.rawQuery(strSelect, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    sum = cursor.getDouble(0);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return sum;
    }

    public double getIntervalTaxCharge2(long startdate, long enddate, int orderType) {

        Cursor cursor1;
        Cursor cursor2;
        long Id;
        double Tax;
        double Charge;
        double count;
        double offValue, TaxPercent, ChargePercent, Off = 0;
        double Price, TotalTaxAndCharge = 0;
        try {
            cursor1 = mDb.rawQuery("select " + DbSchema.OrderSchema.COLUMN_ID + " from " + DbSchema.OrderSchema.TABLE_NAME
                    + " Where " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " > " + startdate + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " <= " + enddate
                    + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_TYPE + " == " + orderType
                    + " And " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "='" + BaseActivity.getPrefMahakId() + "' and " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "='" + BaseActivity.getPrefDatabaseId() + "' and " + DbSchema.OrderSchema.COLUMN_USER_ID + "=" + getPrefUserId(), null);
            if (cursor1 != null) {
                cursor1.moveToFirst();
                while (!cursor1.isAfterLast()) {
                    Id = cursor1.getLong(cursor1.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID));
                    cursor2 = mDb.query(DbSchema.OrderDetailSchema.TABLE_NAME, null, DbSchema.OrderDetailSchema.COLUMN_OrderId + " =? ", new String[]{String.valueOf(Id)}, null, null, null);
                    if (cursor2 != null) {
                        cursor2.moveToFirst();
                        while (!cursor2.isAfterLast()) {
                            count = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz));
                            Price = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Price));
                            offValue = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Discount));
                            TaxPercent = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_TaxPercent));
                            ChargePercent = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ChargePercent));
                            int discountType = ServiceTools.toInt(cursor2.getString(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_DiscountType)));
                            Price = Price * count;
                            Price = (Price - offValue);
                            Tax = ((Price * TaxPercent) / 100);
                            Charge = ((Price * ChargePercent) / 100);
                            TotalTaxAndCharge += Tax + Charge;
                            cursor2.moveToNext();
                        }//End of while
                        cursor2.close();
                    }
                    cursor1.moveToNext();
                }//End of while
                cursor1.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }
        /*if(AvarezMaliat().equals("0"))
            TotalTaxAndCharge = 0;*/
        return TotalTaxAndCharge;

    }

    public double getIntervalTaxCharge3(long startdate, long enddate, int orderType, int customerId) {

        Cursor cursor1;
        Cursor cursor2;
        long Id;
        double Tax;
        double Charge;
        double count;
        double offValue, TaxPercent, ChargePercent, Off = 0;
        double Price, TotalTaxAndCharge = 0;
        try {
            cursor1 = mDb.rawQuery("select " + DbSchema.OrderSchema.COLUMN_ID + " from " + DbSchema.OrderSchema.TABLE_NAME
                    + " Where " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " > " + startdate + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " <= " + enddate
                    + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_TYPE + " == " + orderType
                    + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_PersonId + " == " + customerId
                    + " And " + DbSchema.OrderSchema.COLUMN_MAHAK_ID + "='" + BaseActivity.getPrefMahakId() + "' and " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "='" + BaseActivity.getPrefDatabaseId() + "' and " + DbSchema.OrderSchema.COLUMN_USER_ID + "=" + getPrefUserId(), null);
            if (cursor1 != null) {
                cursor1.moveToFirst();
                while (!cursor1.isAfterLast()) {
                    Id = cursor1.getLong(cursor1.getColumnIndex(DbSchema.OrderSchema.COLUMN_ID));
                    cursor2 = mDb.query(DbSchema.OrderDetailSchema.TABLE_NAME, null, DbSchema.OrderDetailSchema.COLUMN_OrderId + " =? ", new String[]{String.valueOf(Id)}, null, null, null);
                    if (cursor2 != null) {
                        cursor2.moveToFirst();
                        while (!cursor2.isAfterLast()) {
                            count = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz));
                            Price = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Price));
                            offValue = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_Discount));
                            TaxPercent = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_TaxPercent));
                            ChargePercent = cursor2.getDouble(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_ChargePercent));
                            int discountType = ServiceTools.toInt(cursor2.getString(cursor2.getColumnIndex(DbSchema.OrderDetailSchema.COLUMN_DiscountType)));
                            Price = Price * count;
                            Price = (Price - offValue);
                            Tax = ((Price * TaxPercent) / 100);
                            Charge = ((Price * ChargePercent) / 100);
                            TotalTaxAndCharge += Tax + Charge;
                            cursor2.moveToNext();
                        }//End of while
                        cursor2.close();
                    }
                    cursor1.moveToNext();
                }//End of while
                cursor1.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Errorget", e.getMessage());
        }
        /*if(AvarezMaliat().equals("0"))
            TotalTaxAndCharge = 0;*/
        return TotalTaxAndCharge;

    }

    public double getIntervalCountOfProduct(long startdate, long enddate, int orderType) {
        Cursor cursor;
        double sum = 0;
        String strSelect = "select ifnull(SUM(" + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz + "),'0') as sums from " + DbSchema.OrderDetailSchema.TABLE_NAME
                + " INNER JOIN " + DbSchema.OrderSchema.TABLE_NAME
                + " ON " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ID + " = " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_OrderId
                + " Where " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " > " + startdate + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " <= " + enddate
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_TYPE + " == " + orderType
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_DATABASE_ID + " ='" + BaseActivity.getPrefDatabaseId() + "'"
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_MAHAK_ID + " ='" + BaseActivity.getPrefMahakId() + "'";
        try {
            cursor = mDb.rawQuery(strSelect, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    sum = cursor.getDouble(0);
                    cursor.moveToNext();
                }

                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return sum;
    }

    private double getIntervalCountOfProduct(long startdate, long enddate, int orderType, int customerId) {
        Cursor cursor;
        double sum = 0;
        String strSelect = "select ifnull(SUM(" + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz + "),'0') as sums from " + DbSchema.OrderDetailSchema.TABLE_NAME
                + " INNER JOIN " + DbSchema.OrderSchema.TABLE_NAME
                + " ON " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ID + " = " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_OrderId
                + " Where " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " > " + startdate + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " <= " + enddate
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_TYPE + " == " + orderType
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_PersonId + " == " + customerId
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_DATABASE_ID + " ='" + BaseActivity.getPrefDatabaseId() + "'"
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_MAHAK_ID + " ='" + BaseActivity.getPrefMahakId() + "'";
        try {
            cursor = mDb.rawQuery(strSelect, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    sum = cursor.getDouble(0);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return sum;
    }

    private double getIntervalCountOfReturnProduct(long startdate, long enddate, int orderType, long customerId) {
        Cursor cursor;
        double sum = 0;
        String strSelect = "select ifnull(SUM(" + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz + "),'0') as sums from " + DbSchema.OrderDetailSchema.TABLE_NAME
                + " INNER JOIN " + DbSchema.OrderSchema.TABLE_NAME
                + " ON " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ID + " = " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_OrderId
                + " Where " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " > " + startdate + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " <= " + enddate
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_PersonId + " == " + customerId
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_TYPE + " == " + orderType
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_DATABASE_ID + " ='" + BaseActivity.getPrefDatabaseId() + "'"
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_MAHAK_ID + " ='" + BaseActivity.getPrefMahakId() + "'";
        try {
            cursor = mDb.rawQuery(strSelect, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    sum = cursor.getDouble(0);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return sum;
    }

    public double getCustomerSale(long startdate, long enddate, long customerId, int orderType) {

        Cursor cursor;
        double sale = 0;
        String strSelect = "select " + " SUM(" + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_Price + " * " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz + ") as sump from " + DbSchema.OrderDetailSchema.TABLE_NAME
                + " INNER JOIN " + DbSchema.OrderSchema.TABLE_NAME
                + " ON " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ID + " = " + DbSchema.OrderDetailSchema.TABLE_NAME + "." + DbSchema.OrderDetailSchema.COLUMN_OrderId
                + " Where " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " > " + startdate
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_ORDERDATE + " <= " + enddate
                + " AND " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_TYPE + " = " + orderType
                + " and " + DbSchema.OrderSchema.TABLE_NAME + "." + DbSchema.OrderSchema.COLUMN_PersonId + " = " + String.valueOf(customerId);
        try {
            cursor = mDb.rawQuery(strSelect, null);
            if (cursor != null) {
                cursor.moveToFirst();
                sale = cursor.getDouble(0);
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return sale;
    }

    public double getIntervalReceipt(long startdate, long enddate, int type) {
        Cursor cursor;
        double sum = 0;
        String strSelect = "";

        if (type == ProjectInfo.TYPE_CASH) {
            strSelect = "select ifnull(SUM(" + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_CASHAMOUNT + "),'0') as sums from " + DbSchema.ReceiptSchema.TABLE_NAME
                    + " Where " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_MODIFYDATE + " > " + startdate + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_MODIFYDATE + " <= " + enddate
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_DATABASE_ID + " ='" + BaseActivity.getPrefDatabaseId() + "'"
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_MAHAK_ID + " ='" + BaseActivity.getPrefMahakId() + "'";
        } else if (type == ProjectInfo.TYPE_CHEQUE) {
            strSelect = "select ifnull(SUM(" + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_AMOUNT + "),'0') as sums from " + DbSchema.ChequeSchema.TABLE_NAME
                    + " INNER JOIN " + DbSchema.ReceiptSchema.TABLE_NAME
                    + " ON " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_ID + " = " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_RECEIPTID + ""
                    + " Where " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_MODIFYDATE + " > " + startdate + " AND " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_MODIFYDATE + " <= " + enddate
                    + " AND " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_TYPE + " = " + ProjectInfo.TYPE_CHEQUE
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_DATABASE_ID + " ='" + BaseActivity.getPrefDatabaseId() + "'"
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_MAHAK_ID + " ='" + BaseActivity.getPrefMahakId() + "'";
        } else if (type == ProjectInfo.TYPE_CASH_RECEIPT) {
            strSelect = "select ifnull(SUM(" + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_AMOUNT + "),'0') as sums from " + DbSchema.ChequeSchema.TABLE_NAME
                    + " INNER JOIN " + DbSchema.ReceiptSchema.TABLE_NAME
                    + " ON " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_ID + " = " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_RECEIPTID + ""
                    + " Where " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_MODIFYDATE + " > " + startdate + " AND " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_MODIFYDATE + " <= " + enddate
                    + " AND " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_TYPE + " = " + ProjectInfo.TYPE_CASH_RECEIPT
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_DATABASE_ID + " ='" + BaseActivity.getPrefDatabaseId() + "'"
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_MAHAK_ID + " ='" + BaseActivity.getPrefMahakId() + "'";
        }

        try {
            cursor = mDb.rawQuery(strSelect, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    sum = cursor.getDouble(0);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return sum;
    }

    public double getIntervalPayment(long startdate, long enddate, int type) {

        Cursor cursor;
        double sum = 0;
        String strSelect = "";

        if (type == ProjectInfo.Bank_TYPE) {
            strSelect = "select ifnull(SUM(" + DbSchema.PayableSchema.TABLE_NAME + "." + DbSchema.PayableSchema.COLUMN_Price + "),'0') as sums from " + DbSchema.PayableSchema.TABLE_NAME
                    + " Where " + DbSchema.PayableSchema.TABLE_NAME + "." + DbSchema.PayableSchema.COLUMN_TransferDate + " > " + startdate + " AND " + DbSchema.PayableSchema.TABLE_NAME + "." + DbSchema.PayableSchema.COLUMN_TransferDate + " <= " + enddate
                    + " AND " + DbSchema.PayableSchema.TABLE_NAME + "." + DbSchema.PayableSchema.COLUMN_TransferType + " = " + ProjectInfo.Bank_TYPE
                    + " AND " + DbSchema.PayableSchema.TABLE_NAME + "." + DbSchema.PayableSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                    + " AND " + DbSchema.PayableSchema.TABLE_NAME + "." + DbSchema.PayableSchema.COLUMN_DatabaseId + " ='" + BaseActivity.getPrefDatabaseId() + "'"
                    + " AND " + DbSchema.PayableSchema.TABLE_NAME + "." + DbSchema.PayableSchema.COLUMN_MahakId + " ='" + BaseActivity.getPrefMahakId() + "'";
        } else if (type == ProjectInfo.Expense_TYPE) {
            strSelect = "select ifnull(SUM(" + DbSchema.PayableSchema.TABLE_NAME + "." + DbSchema.PayableSchema.COLUMN_Price + "),'0') as sums from " + DbSchema.PayableSchema.TABLE_NAME
                    + " Where " + DbSchema.PayableSchema.TABLE_NAME + "." + DbSchema.PayableSchema.COLUMN_TransferDate + " > " + startdate + " AND " + DbSchema.PayableSchema.TABLE_NAME + "." + DbSchema.PayableSchema.COLUMN_TransferDate + " <= " + enddate
                    + " AND " + DbSchema.PayableSchema.TABLE_NAME + "." + DbSchema.PayableSchema.COLUMN_TransferType + " = " + ProjectInfo.Expense_TYPE
                    + " AND " + DbSchema.PayableSchema.TABLE_NAME + "." + DbSchema.PayableSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                    + " AND " + DbSchema.PayableSchema.TABLE_NAME + "." + DbSchema.PayableSchema.COLUMN_DatabaseId + " ='" + BaseActivity.getPrefDatabaseId() + "'"
                    + " AND " + DbSchema.PayableSchema.TABLE_NAME + "." + DbSchema.PayableSchema.COLUMN_MahakId + " ='" + BaseActivity.getPrefMahakId() + "'";
        }

        try {
            cursor = mDb.rawQuery(strSelect, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    sum = cursor.getDouble(0);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return sum;
    }

    private double getIntervalReceipt(long startdate, long enddate, int type, int customerId) {
        Cursor cursor;
        double sum = 0;
        String strSelect = "";

        if (type == ProjectInfo.TYPE_CASH) {
            strSelect = "select ifnull(SUM(" + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_CASHAMOUNT + "),'0') as sums from " + DbSchema.ReceiptSchema.TABLE_NAME
                    + " Where " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_DATE + " > " + startdate + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_DATE + " <= " + enddate
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.PERSON_ID + " == " + customerId
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_DATABASE_ID + " ='" + BaseActivity.getPrefDatabaseId() + "'"
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_MAHAK_ID + " ='" + BaseActivity.getPrefMahakId() + "'";
        } else if (type == ProjectInfo.TYPE_CHEQUE) {
            strSelect = "select ifnull(SUM(" + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_AMOUNT + "),'0') as sums from " + DbSchema.ChequeSchema.TABLE_NAME
                    + " INNER JOIN " + DbSchema.ReceiptSchema.TABLE_NAME
                    + " ON " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_ID + " = " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_RECEIPTID + ""
                    + " Where " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_DATE + " > " + startdate + " AND " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_DATE + " <= " + enddate
                    + " AND " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_TYPE + " = " + ProjectInfo.TYPE_CHEQUE
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.PERSON_ID + " == " + customerId
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_DATABASE_ID + " ='" + BaseActivity.getPrefDatabaseId() + "'"
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_MAHAK_ID + " ='" + BaseActivity.getPrefMahakId() + "'";
        } else if (type == ProjectInfo.TYPE_CASH_RECEIPT) {
            strSelect = "select ifnull(SUM(" + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_AMOUNT + "),'0') as sums from " + DbSchema.ChequeSchema.TABLE_NAME
                    + " INNER JOIN " + DbSchema.ReceiptSchema.TABLE_NAME
                    + " ON " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_ID + " = " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_RECEIPTID + ""
                    + " Where " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_DATE + " > " + startdate + " AND " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_DATE + " <= " + enddate
                    + " AND " + DbSchema.ChequeSchema.TABLE_NAME + "." + DbSchema.ChequeSchema.COLUMN_TYPE + " = " + ProjectInfo.TYPE_CASH_RECEIPT
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.PERSON_ID + " == " + customerId
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_USER_ID + " = " + getPrefUserId()
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_DATABASE_ID + " ='" + BaseActivity.getPrefDatabaseId() + "'"
                    + " AND " + DbSchema.ReceiptSchema.TABLE_NAME + "." + DbSchema.ReceiptSchema.COLUMN_MAHAK_ID + " ='" + BaseActivity.getPrefMahakId() + "'";
        }

        try {
            cursor = mDb.rawQuery(strSelect, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    sum = cursor.getDouble(0);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return sum;
    }

    public long UpdatePicturesProduct(PicturesProduct picturesProduct) {
        try {
            ContentValues initialvalue = getOnlinePictureProduct(picturesProduct);
            return mDb.update(DbSchema.PicturesProductSchema.TABLE_NAME, initialvalue, DbSchema.PicturesProductSchema.COLUMN_PICTURE_ID + "=?", new String[]{String.valueOf(picturesProduct.getPictureId())});
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            return 0;
        }
    }


    //QUERIES UPDATE____________________________________________________________________

    public long UpdatePicturesProductWithClientId(PicturesProduct picturesProduct) {
        try {
            ContentValues initialvalue = getOnlinePictureProduct(picturesProduct);
            return mDb.update(DbSchema.PicturesProductSchema.TABLE_NAME, initialvalue, DbSchema.PicturesProductSchema.COLUMN_PictureClientId + "=?", new String[]{String.valueOf(picturesProduct.getPictureClientId())});
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            return 0;
        }
    }

    public boolean UpdateOrAddVisitorProductFast(List<VisitorProduct> visitorProducts, long rowVersion) {
        boolean result = false;
        mDb.beginTransaction();
        try {
            ContentValues initialvalue = new ContentValues();
            for (VisitorProduct visitorProduct : visitorProducts) {
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_VisitorProductId, visitorProduct.getVisitorProductId());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_ProductDetailId, visitorProduct.getProductDetailId());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_USER_ID, visitorProduct.getVisitorId());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_Count1, visitorProduct.getCount1());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_Count2, visitorProduct.getCount2());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_Price, visitorProduct.getPrice());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_Serials, visitorProduct.getSerials());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_Deleted, visitorProduct.getDelete());

                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_DataHash, visitorProduct.getDataHash());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_CreateDate, visitorProduct.getCreateDate());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_UpdateDate, visitorProduct.getUpdateDate());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_CreateSyncId, visitorProduct.getCreateSyncId());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_UpdateSyncId, visitorProduct.getUpdateSyncId());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_RowVersion, visitorProduct.getRowVersion());

                if (rowVersion == 0)
                    mDb.insert(DbSchema.VisitorProductSchema.TABLE_NAME, null, initialvalue);
                else {
                    result = (mDb.update(DbSchema.VisitorProductSchema.TABLE_NAME, initialvalue, DbSchema.VisitorProductSchema.COLUMN_VisitorProductId + "=?", new String[]{String.valueOf(visitorProduct.getVisitorProductId())})) > 0;
                    if (!result)
                        mDb.insert(DbSchema.VisitorProductSchema.TABLE_NAME, null, initialvalue);
                }
            }

            mDb.setTransactionSuccessful();

        } finally {
            mDb.endTransaction();
        }
        return result;
    }

    public boolean UpdateOrAddVisitorProductFast(VisitorProduct visitorProduct) {
        boolean result = false;
        mDb.beginTransaction();
        try {
            ContentValues initialvalue = new ContentValues();
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_VisitorProductId, visitorProduct.getVisitorProductId());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_ProductDetailId, visitorProduct.getProductDetailId());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_USER_ID, visitorProduct.getVisitorId());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_Count1, visitorProduct.getCount1());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_Count2, visitorProduct.getCount2());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_Price, visitorProduct.getPrice());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_Serials, visitorProduct.getSerials());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_Deleted, visitorProduct.getDelete());

                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_DataHash, visitorProduct.getDataHash());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_CreateDate, visitorProduct.getCreateDate());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_UpdateDate, visitorProduct.getUpdateDate());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_CreateSyncId, visitorProduct.getCreateSyncId());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_UpdateSyncId, visitorProduct.getUpdateSyncId());
                initialvalue.put(DbSchema.VisitorProductSchema.COLUMN_RowVersion, visitorProduct.getRowVersion());

                result = (mDb.update(DbSchema.VisitorProductSchema.TABLE_NAME, initialvalue, DbSchema.VisitorProductSchema.COLUMN_VisitorProductId + "=?", new String[]{String.valueOf(visitorProduct.getVisitorProductId())})) > 0;


            mDb.setTransactionSuccessful();

        } finally {
            mDb.endTransaction();
        }
        return result;
    }

    public boolean UpdateOrAddVisitorPeopleFast(List<VisitorPeople> visitorPeople, long rowVersion) {
        boolean result = false;
        mDb.beginTransaction();
        try {
            ContentValues initialvalue = new ContentValues();
            for (VisitorPeople visitorPerson : visitorPeople) {

                initialvalue.put(DbSchema.VisitorPeopleSchema.COLUMN_VisitorPersonId, visitorPerson.getVisitorPersonId());
                initialvalue.put(DbSchema.VisitorPeopleSchema.COLUMN_PersonId, visitorPerson.getPersonId());
                initialvalue.put(DbSchema.VisitorPeopleSchema.COLUMN_USER_ID, visitorPerson.getVisitorId());
                initialvalue.put(DbSchema.VisitorPeopleSchema.COLUMN_Deleted, visitorPerson.isDeleted());

                initialvalue.put(DbSchema.VisitorPeopleSchema.COLUMN_DataHash, visitorPerson.getDataHash());
                initialvalue.put(DbSchema.VisitorPeopleSchema.COLUMN_CreateDate, visitorPerson.getCreateDate());
                initialvalue.put(DbSchema.VisitorPeopleSchema.COLUMN_UpdateDate, visitorPerson.getUpdateDate());
                initialvalue.put(DbSchema.VisitorPeopleSchema.COLUMN_CreateSyncId, visitorPerson.getCreateSyncId());
                initialvalue.put(DbSchema.VisitorPeopleSchema.COLUMN_UpdateSyncId, visitorPerson.getUpdateSyncId());
                initialvalue.put(DbSchema.VisitorPeopleSchema.COLUMN_RowVersion, visitorPerson.getRowVersion());

                if (rowVersion == 0)
                    mDb.insert(DbSchema.VisitorPeopleSchema.TABLE_NAME, null, initialvalue);
                else {
                    result = (mDb.update(DbSchema.VisitorPeopleSchema.TABLE_NAME, initialvalue, DbSchema.VisitorPeopleSchema.COLUMN_VisitorPersonId + "=?", new String[]{String.valueOf(visitorPerson.getVisitorPersonId())})) > 0;
                    if (!result)
                        mDb.insert(DbSchema.VisitorPeopleSchema.TABLE_NAME, null, initialvalue);
                }
            }

            mDb.setTransactionSuccessful();

        } finally {
            mDb.endTransaction();
        }
        return result;
    }

    public boolean UpdateCustomer(Customer customer) {

        boolean result;
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_PersonGroupId, customer.getPersonGroupId());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_PersonGroupCode, customer.getPersonGroupCode());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_NAME, customer.getName());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_FirstName, customer.getFirstName());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_LastName, customer.getLastName());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_Prefix, customer.getPrefix());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_ORGANIZATION, customer.getOrganization());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_CREDIT, customer.getCredit());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_BALANCE, customer.getBalance());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_STATE, customer.getState());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_CITY, customer.getCity());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_CityCode, customer.getCityCode());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_ADDRESS, customer.getAddress());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_ZONE, customer.getZone());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_PHONE, customer.getTell());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_HasOrder, customer.getOrderCount());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_MOBILE, customer.getMobile());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_LATITUDE, customer.getLatitude());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_LONGITUDE, customer.getLongitude());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_SHIFT, customer.getShift());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_MODIFYDATE, customer.getModifyDate());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_PUBLISH, customer.getPublish());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_RowVersion, customer.getRowVersion());

        //result = (mDb.update(DbSchema.Customerschema.TABLE_NAME, initialvalue, DbSchema.Customerschema.COLUMN_ID + "=?", new String[]{String.valueOf(customer.getId())})) > 0;
        if (customer.getPersonId() != 0)
            result = (mDb.update(DbSchema.CustomerSchema.TABLE_NAME, initialvalue, DbSchema.CustomerSchema.COLUMN_PersonId + "=? and " + DbSchema.CustomerSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(customer.getPersonId()), String.valueOf(getPrefUserId())})) > 0;
        else
            result = (mDb.update(DbSchema.CustomerSchema.TABLE_NAME, initialvalue, DbSchema.CustomerSchema.COLUMN_PersonClientId + "=? and " + DbSchema.CustomerSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(customer.getPersonClientId()), String.valueOf(getPrefUserId())})) > 0;

        return result;
    }

    public boolean UpdateCustomerWithClientId(Customer customer) {
        boolean result;
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_PersonGroupId, customer.getPersonGroupId());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_PersonGroupCode, customer.getPersonGroupCode());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_PersonId, customer.getPersonId());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_HasOrder, customer.getOrderCount());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_NAME, customer.getName());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_FirstName, customer.getFirstName());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_LastName, customer.getLastName());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_Prefix, customer.getPrefix());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_ORGANIZATION, customer.getOrganization());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_CREDIT, customer.getCredit());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_BALANCE, customer.getBalance());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_STATE, customer.getState());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_CITY, customer.getCity());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_CityCode, customer.getCityCode());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_ADDRESS, customer.getAddress());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_ZONE, customer.getZone());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_PHONE, customer.getTell());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_MOBILE, customer.getMobile());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_LATITUDE, customer.getLatitude());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_LONGITUDE, customer.getLongitude());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_SHIFT, customer.getShift());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_MODIFYDATE, customer.getModifyDate());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_PUBLISH, customer.getPublish());
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_RowVersion, customer.getRowVersion());

        //result = (mDb.update(DbSchema.Customerschema.TABLE_NAME, initialvalue, DbSchema.Customerschema.COLUMN_ID + "=?", new String[]{String.valueOf(customer.getId())})) > 0;
        result = (mDb.update(DbSchema.CustomerSchema.TABLE_NAME, initialvalue, DbSchema.CustomerSchema.COLUMN_PersonClientId + "=? and " + DbSchema.CustomerSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(customer.getPersonClientId()), String.valueOf(getPrefUserId())})) > 0;
        return result;
    }

    public boolean UpdateOrAddServerCustomerFast(List<Customer> customers, long rowVersion) {
        boolean result = false;
        mDb.beginTransaction();
        try {
            ContentValues initialvalue = new ContentValues();
            for (Customer customer : customers) {
                if (customer.getFirstName() == null)
                    customer.setFirstName("");
                if (customer.getLastName() == null)
                    customer.setLastName("");
                if (customer.getPrefix() == null)
                    customer.setPrefix("");
                customer.setName(customer.getPrefix() + " " + customer.getFirstName() + " " + customer.getLastName());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_PersonGroupId, customer.getPersonGroupId());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_PersonGroupClientId, customer.getPersonGroupClientId());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_PersonGroupCode, customer.getPersonGroupCode());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_NAME, customer.getName());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_FirstName, customer.getFirstName());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_LastName, customer.getLastName());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_Prefix, customer.getPrefix());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_ORGANIZATION, customer.getOrganization());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_CREDIT, customer.getCredit());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_BALANCE, customer.getBalance());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_STATE, customer.getState());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_CITY, customer.getCity());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_ADDRESS, customer.getAddress());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_ZONE, customer.getZone());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_PHONE, customer.getTell());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_MOBILE, customer.getMobile());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_LATITUDE, customer.getLatitude());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_LONGITUDE, customer.getLongitude());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_SHIFT, customer.getShift());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_MODIFYDATE, customer.getModifyDate());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_MAHAK_ID, customer.getMahakId());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_PersonCode, customer.getPersonCode());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_DATABASE_ID, customer.getDatabaseId());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_USER_ID, customer.getUserId());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_PUBLISH, customer.getPublish());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_DiscountPercent, customer.getDiscountPercent());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_SellPriceLevel, customer.getSellPriceLevel());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_PersonId, customer.getPersonId());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_HasOrder, customer.getOrderCount());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_PersonClientId, customer.getPersonClientId());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_PersonType, customer.getPersonType());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_Gender, customer.getGender());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_NationalCode, customer.getNationalCode());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_Email, customer.getEmail());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_UserName, customer.getUserName());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_Password, customer.getPassword());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_CityCode, customer.getCityCode());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_Fax, customer.getFax());
                initialvalue.put(DbSchema.CustomerSchema.COLUMN_RowVersion, customer.getRowVersion());
                if(customer.getPersonClientId() == 0)
                    result = (mDb.update(DbSchema.CustomerSchema.TABLE_NAME, initialvalue, DbSchema.CustomerSchema.COLUMN_PersonId + "=? and " + DbSchema.CustomerSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(customer.getPersonId()), String.valueOf(getPrefUserId())})) > 0;
                else
                    result = (mDb.update(DbSchema.CustomerSchema.TABLE_NAME, initialvalue, DbSchema.CustomerSchema.COLUMN_PersonClientId + "=? and " + DbSchema.CustomerSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(customer.getPersonClientId()), String.valueOf(getPrefUserId())})) > 0;
                if (!result)
                    mDb.insert(DbSchema.CustomerSchema.TABLE_NAME, null, initialvalue);
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
        return result;
    }

    public boolean UpdateOrAddPhotoGallery(List<PhotoGallery> photoGalleries, long rowVersion) {
        boolean result = false;
        mDb.beginTransaction();
        try {
            ContentValues initialvalue = new ContentValues();
            for (PhotoGallery photoGallery : photoGalleries) {

                initialvalue.put(DbSchema.PhotoGallerySchema.COLUMN_photoGalleryId, photoGallery.getPhotoGalleryId());
                initialvalue.put(DbSchema.PhotoGallerySchema.COLUMN_pictureId, photoGallery.getPictureId());
                initialvalue.put(DbSchema.PhotoGallerySchema.COLUMN_entityType, photoGallery.getEntityType());
                initialvalue.put(DbSchema.PhotoGallerySchema.COLUMN_itemCode, photoGallery.getItemCode());
                initialvalue.put(DbSchema.PhotoGallerySchema.COLUMN_itemType, photoGallery.getItemType());
                initialvalue.put(DbSchema.PhotoGallerySchema.COLUMN_deleted, photoGallery.isDeleted());
                initialvalue.put(DbSchema.PhotoGallerySchema.COLUMN_dataHash, photoGallery.getDataHash());
                initialvalue.put(DbSchema.PhotoGallerySchema.COLUMN_createDate, photoGallery.getCreateDate());
                initialvalue.put(DbSchema.PhotoGallerySchema.COLUMN_updateDate, photoGallery.getUpdateDate());
                initialvalue.put(DbSchema.PhotoGallerySchema.COLUMN_createSyncId, photoGallery.getCreateSyncId());
                initialvalue.put(DbSchema.PhotoGallerySchema.COLUMN_updateSyncId, photoGallery.getUpdateSyncId());
                initialvalue.put(DbSchema.PhotoGallerySchema.COLUMN_rowVersion, photoGallery.getRowVersion());
                initialvalue.put(DbSchema.PhotoGallerySchema.COLUMN_userId, getPrefUserId());

                if (rowVersion == 0)
                    mDb.insert(DbSchema.PhotoGallerySchema.TABLE_NAME, null, initialvalue);
                else {
                    result = (mDb.update(DbSchema.PhotoGallerySchema.TABLE_NAME, initialvalue, DbSchema.PhotoGallerySchema.COLUMN_photoGalleryId + " =? ", new String[]{String.valueOf(photoGallery.getPhotoGalleryId())})) > 0;
                    if (!result)
                        mDb.insert(DbSchema.PhotoGallerySchema.TABLE_NAME, null, initialvalue);
                }
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
        return result;
    }

    public boolean UpdateOrAddRegion(List<Region> regions, long rowVersion) {
        boolean result = false;
        mDb.beginTransaction();
        try {
            ContentValues initialvalue = new ContentValues();
            for (Region region : regions) {

                initialvalue.put(DbSchema.RegionSchema.COLUMN_CityID, region.getCityID());
                initialvalue.put(DbSchema.RegionSchema.COLUMN_CityName, region.getCityName());
                initialvalue.put(DbSchema.RegionSchema.COLUMN_ProvinceID, region.getProvinceID());
                initialvalue.put(DbSchema.RegionSchema.COLUMN_ProvinceName, region.getProvinceName());
                initialvalue.put(DbSchema.RegionSchema.COLUMN_MapCode, region.getMapCode());
                initialvalue.put(DbSchema.RegionSchema.COLUMN_RowVersion, region.getRowVersion());
                initialvalue.put(DbSchema.RegionSchema.COLUMN_userId, getPrefUserId());

                if (rowVersion == 0)
                    mDb.insert(DbSchema.RegionSchema.TABLE_NAME, null, initialvalue);
                else {
                    result = (mDb.update(DbSchema.RegionSchema.TABLE_NAME, initialvalue, DbSchema.RegionSchema.COLUMN_ProvinceID + " =? ", new String[]{String.valueOf(region.getProvinceID())})) > 0;
                    if (!result)
                        mDb.insert(DbSchema.RegionSchema.TABLE_NAME, null, initialvalue);
                }
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
        return result;
    }

    public boolean UpdateProduct(Product product) {
        boolean result;

        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.ProductSchema.COLUMN_CATEGORYID, product.getProductGroupId());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_NAME, product.getName());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_REALPRICE, product.getRealPrice());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_TAGS, product.getTags());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_UnitRatio, product.getUnitRatio());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_CODE, product.getCode());

        initialvalue.put(DbSchema.ProductSchema.COLUMN_WEIGHT, product.getWeight());

        initialvalue.put(DbSchema.ProductSchema.COLUMN_Width, product.getWidth());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_Height, product.getHeight());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_Length, product.getLength());


        initialvalue.put(DbSchema.ProductSchema.COLUMN_IMAGE, product.getImage());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_MIN, product.getMin());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_MODIFYDATE, product.getModifyDate());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_UNITNAME, product.getUnitName());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_UNITNAME2, product.getUnitName2());

        initialvalue.put(DbSchema.ProductSchema.COLUMN_TAX, product.getTaxPercent());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_CHARGE, product.getChargePercent());

        initialvalue.put(DbSchema.ProductSchema.COLUMN_DiscountPercent, product.getDiscountPercent());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_Barcode, product.getBarcode());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_ProductId, product.getProductId());
        //initialvalue.put(DbSchema.Productschema.COLUMN_ProductDetailId, product.getProductDetailId());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_ProductClientId, product.getProductClientId());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_DataHash, product.getDataHash());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_CreateDate, product.getCreateDate());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_UpdateDate, product.getUpdateDate());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_CreateSyncId, product.getCreateSyncId());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_UpdateSyncId, product.getUpdateSyncId());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_RowVersion, product.getRowVersion());
        initialvalue.put(DbSchema.ProductSchema.COLUMN_Deleted, product.getDeleted());


        //result = (mDb.update(DbSchema.Productschema.TABLE_NAME, initialvalue, DbSchema.Productschema.COLUMN_PRODUCT_CODE + "=? " , new String[]{String.valueOf(product.getProductCode())})) > 0;
        result = (mDb.update(DbSchema.ProductSchema.TABLE_NAME, initialvalue, DbSchema.ProductSchema.COLUMN_ProductId + "=? and " + DbSchema.ProductSchema.COLUMN_USER_ID + "=? ", new String[]{String.valueOf(product.getProductId()), String.valueOf(getPrefUserId())})) > 0;
        return result;
    }

    public boolean UpdateOrAddServerProductFast(List<Product> products, long rowVersion) {
        boolean result = false;
        mDb.beginTransaction();
        try {
            ContentValues initialvalue = new ContentValues();
            for (Product product : products) {

                initialvalue.put(DbSchema.ProductSchema.COLUMN_CATEGORYID, product.getProductGroupId());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_NAME, product.getName());

                initialvalue.put(DbSchema.ProductSchema.COLUMN_PUBLISH, product.getPublish());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_MAHAK_ID, product.getMahakId());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_DATABASE_ID, product.getDatabaseId());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_USER_ID, product.getUserId());


                initialvalue.put(DbSchema.ProductSchema.COLUMN_REALPRICE, product.getRealPrice());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_Barcode, product.getBarcode());

                initialvalue.put(DbSchema.ProductSchema.COLUMN_UnitRatio, product.getUnitRatio());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_CODE, product.getCode());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_TAGS, product.getTags());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_IMAGE, product.getImage());

                initialvalue.put(DbSchema.ProductSchema.COLUMN_WEIGHT, product.getWeight());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_Width, product.getWidth());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_Height, product.getHeight());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_Length, product.getLength());

                initialvalue.put(DbSchema.ProductSchema.COLUMN_MIN, product.getMin());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_MODIFYDATE, product.getModifyDate());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_PRODUCT_CODE, product.getProductCode());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_UNITNAME, product.getUnitName());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_UNITNAME2, product.getUnitName2());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_TAX, product.getTaxPercent());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_CHARGE, product.getChargePercent());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_DiscountPercent, product.getDiscountPercent());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_ProductId, product.getProductId());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_ProductClientId, product.getProductClientId());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_DataHash, product.getDataHash());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_CreateDate, product.getCreateDate());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_UpdateDate, product.getUpdateDate());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_CreateSyncId, product.getCreateSyncId());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_UpdateSyncId, product.getUpdateSyncId());
                initialvalue.put(DbSchema.ProductSchema.COLUMN_RowVersion, product.getRowVersion());
                //initialvalue.put(DbSchema.Productschema.COLUMN_Deleted, product.getDeleted());
                if (rowVersion == 0)
                    mDb.insert(DbSchema.ProductSchema.TABLE_NAME, null, initialvalue);
                else {
                    result = (mDb.update(DbSchema.ProductSchema.TABLE_NAME, initialvalue, DbSchema.ProductSchema.COLUMN_ProductId + "=? and " + DbSchema.ProductSchema.COLUMN_USER_ID + "=? ", new String[]{String.valueOf(product.getProductId()), String.valueOf(getPrefUserId())})) > 0;
                    if (!result)
                        mDb.insert(DbSchema.ProductSchema.TABLE_NAME, null, initialvalue);
                }
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
        return result;
    }

    public boolean UpdateLocationCustomer(long id, String latitude, String longitude) {
        boolean result;
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_LATITUDE, latitude);
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_LONGITUDE, longitude);
        initialvalue.put(DbSchema.CustomerSchema.COLUMN_PUBLISH, ProjectInfo.DONT_PUBLISH);
        result = (mDb.update(DbSchema.CustomerSchema.TABLE_NAME, initialvalue, DbSchema.CustomerSchema.COLUMN_ID + "=?", new String[]{String.valueOf(id)})) > 0;
        return result;
    }

    public boolean UpdateOrAddServerVisitor(List<Visitor> visitors) {

        boolean result = false;
        mDb.beginTransaction();
        try {
            ContentValues initialvalue = new ContentValues();
            for (Visitor visitor : visitors) {
                if (visitor.getDeleted() == 1) {
                    DeleteServerVisitor(visitor);
                } else {
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_VisitorCode, visitor.getVisitorCode());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_MODIFYDATE, visitor.getModifyDate());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_NAME, visitor.getName());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_USERNAME, visitor.getUsername());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_STORECODE, visitor.getStoreCode());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_TELL, visitor.getMobile());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_BANKCODE, visitor.getBankCode());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_CASHCODE, visitor.getCashCode());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_DatabaseId, BaseActivity.getPrefDatabaseId());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_MahakId, BaseActivity.getPrefMahakId());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_PriceAccess, visitor.isHasPriceAccess());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_CostLevelAccess, visitor.isHasPriceLevelAccess());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_HasRadara, visitor.HasRadara());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_Sell_DefaultCostLevel, visitor.getSellPriceLevel());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_SelectedCostLevels, visitor.getSelectedPriceLevels());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_ChequeCredit, visitor.getChequeCredit());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_TotalCredit, visitor.getTotalCredit());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_USER_ID, visitor.getUserId());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_USERNAME, visitor.getUsername());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_STORECODE, visitor.getStoreCode());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_DataHash, visitor.getDataHash());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_CreateDate, visitor.getCreateDate());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_UpdateDate, visitor.getUpdateDate());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_CreateSyncId, visitor.getCreateSyncId());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_UpdateSyncId, visitor.getUpdateSyncId());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_RowVersion, visitor.getRowVersion());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_VisitorId, visitor.getVisitorId());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_VisitorClientId, visitor.getVisitorClientId());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_Password, visitor.getPassword());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_PersonCode, visitor.getPersonCode());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_VisitorType, visitor.getVisitorType());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_DeviceId, visitor.getDeviceId());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_Active, visitor.isIsActive());
                    initialvalue.put(DbSchema.VisitorSchema.COLUMN_Color, visitor.getColor());
                    result = (mDb.update(DbSchema.VisitorSchema.TABLE_NAME, initialvalue, DbSchema.VisitorSchema.COLUMN_VisitorId + "=? ", new String[]{String.valueOf(visitor.getVisitorId())})) > 0;
                    if (!result)
                        mDb.insert(DbSchema.VisitorSchema.TABLE_NAME, null, initialvalue);
                }
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
        return result;

    }

    public boolean UpdateServerCustomerGroup(CustomerGroup customergroup) {
        boolean result;

        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_NAME, customergroup.getName());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_COLOR, customergroup.getColor());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_ICON, customergroup.getIcon());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_MODIFYDATE, customergroup.getModifyDate());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_SellPriceLevel, customergroup.getSellPriceLevel());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_DiscountPercent, customergroup.getDiscountPercent());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_DataHash, customergroup.getDataHash());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_CreateDate, customergroup.getCreateDate());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_UpdateDate, customergroup.getUpdateDate());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_CreateSyncId, customergroup.getCreateSyncId());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_UpdateSyncId, customergroup.getUpdateSyncId());
        initialvalue.put(DbSchema.CustomersGroupSchema.COLUMN_RowVersion, customergroup.getRowVersion());

        result = (mDb.update(DbSchema.CustomersGroupSchema.TABLE_NAME, initialvalue, DbSchema.CustomersGroupSchema.COLUMN_PersonGroupId + "=? and  " + DbSchema.CustomersGroupSchema.COLUMN_USER_ID + " =?  " , new String[]{String.valueOf(customergroup.getPersonGroupId()) , String.valueOf(getPrefUserId())})) > 0;
        return result;
    }

    public boolean UpdateServerBank(Bank bank) {
        boolean result;

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbSchema.BanksSchema.COLUMN_NAME, bank.getName());
        contentValues.put(DbSchema.BanksSchema.COLUMN_USER_ID, BaseActivity.getPrefUserId());
        contentValues.put(DbSchema.BanksSchema.COLUMN_DESCRIPTION, bank.getDescription());
        contentValues.put(DbSchema.BanksSchema.COLUMN_BANK_CODE, bank.getBankCode());
        contentValues.put(DbSchema.BanksSchema.COLUMN_MODIFYDATE, bank.getModifyDate());
        contentValues.put(DbSchema.BanksSchema.COLUMN_BankId, bank.getBankId());
        contentValues.put(DbSchema.BanksSchema.COLUMN_BankClientId, bank.getBankClientId());
        contentValues.put(DbSchema.BanksSchema.COLUMN_DataHash, bank.getDataHash());
        contentValues.put(DbSchema.BanksSchema.COLUMN_CreateDate, bank.getCreateDate());
        contentValues.put(DbSchema.BanksSchema.COLUMN_UpdateDate, bank.getUpdateDate());
        contentValues.put(DbSchema.BanksSchema.COLUMN_CreateSyncId, bank.getCreateSyncId());
        contentValues.put(DbSchema.BanksSchema.COLUMN_UpdateSyncId, bank.getUpdateSyncId());
        contentValues.put(DbSchema.BanksSchema.COLUMN_RowVersion, bank.getRowVersion());

        result = (mDb.update(DbSchema.BanksSchema.TABLE_NAME, contentValues, DbSchema.BanksSchema.COLUMN_BankId + "=? ", new String[]{String.valueOf(bank.getBankId())})) > 0;
        return result;
    }

    public boolean UpdateReasons(Reasons reasons) {
        boolean result;

        ContentValues initialvalue = new ContentValues();

        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_NAME, reasons.getName());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_DESCRIPTION, reasons.getDescription());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_TYPE, reasons.getType());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_MAHAK_ID, reasons.getMahakId());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_ReturnReasonCode, reasons.getReturnReasonCode());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_ReturnReasonId, reasons.getReturnReasonId());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_ReturnReasonClientId, reasons.getReturnReasonClientId());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_RowVersion, reasons.getRowVersion());

        /*initialvalue.put(DbSchema.ReasonsSchema.COLUMN_DATABASE_ID, reasons.getDatabaseId());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_MODIFYDATE, reasons.getModifyDate());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_USER_ID, reasons.getVisitorId());*/

        /*initialvalue.put(DbSchema.ReasonsSchema.COLUMN_DataHash, reasons.getDataHash());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_UpdateDate, reasons.getUpdateDate());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_CreateDate, reasons.getDate());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_CreateSyncId, reasons.getCreateSyncId());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_UpdateSyncId, reasons.getUpdateSyncId());
        initialvalue.put(DbSchema.ReasonsSchema.COLUMN_RowVersion, reasons.getRowVersion());*/

        result = (mDb.update(DbSchema.ReasonsSchema.TABLE_NAME, initialvalue, DbSchema.ReasonsSchema.COLUMN_ReturnReasonId + "=?  ", new String[]{String.valueOf(reasons.getReturnReasonId())})) > 0;
        return result;
    }

    public boolean UpdateOrAddExtraInfo(List<ExtraData> extraDatas, long rowVersion) {

        Person_Extra_Data person_extra_data = new Person_Extra_Data();

        Gson gson = new Gson();

        boolean result = false;
        mDb.beginTransaction();
        try {
            ContentValues initialvalue = new ContentValues();
            for (ExtraData extraData : extraDatas) {

                initialvalue.put(DbSchema.ExtraDataSchema.COLUMN_USER_ID, extraData.getUserId());
                initialvalue.put(DbSchema.ExtraDataSchema.COLUMN_MAHAK_ID, extraData.getMahakId());
                initialvalue.put(DbSchema.ExtraDataSchema.COLUMN_DATABASE_ID, extraData.getDatabaseId());
                initialvalue.put(DbSchema.ExtraDataSchema.COLUMN_MODIFYDATE, extraData.getModifyDate());

                initialvalue.put(DbSchema.ExtraDataSchema.COLUMN_ItemId, extraData.getItemId());
                initialvalue.put(DbSchema.ExtraDataSchema.COLUMN_ExtraDataId, extraData.getExtraDataId());
                initialvalue.put(DbSchema.ExtraDataSchema.COLUMN_ItemType, extraData.getItemType());
                initialvalue.put(DbSchema.ExtraDataSchema.COLUMN_Data, extraData.getData());

                initialvalue.put(DbSchema.ExtraDataSchema.COLUMN_DataHash, extraData.getDataHash());
                initialvalue.put(DbSchema.ExtraDataSchema.COLUMN_CreateDate, extraData.getCreateDate());
                initialvalue.put(DbSchema.ExtraDataSchema.COLUMN_UpdateDate, extraData.getUpdateDate());
                initialvalue.put(DbSchema.ExtraDataSchema.COLUMN_CreateSyncId, extraData.getCreateSyncId());
                initialvalue.put(DbSchema.ExtraDataSchema.COLUMN_UpdateSyncId, extraData.getUpdateSyncId());
                initialvalue.put(DbSchema.ExtraDataSchema.COLUMN_RowVersion, extraData.getRowVersion());

                if (rowVersion == 0) {
                    mDb.insert(DbSchema.ExtraDataSchema.TABLE_NAME, null, initialvalue);
                } else {
                    result = (mDb.update(DbSchema.ExtraDataSchema.TABLE_NAME, initialvalue, DbSchema.ExtraDataSchema.COLUMN_MAHAK_ID + "=? and " + DbSchema.ExtraDataSchema.COLUMN_ItemId + "=? and " + DbSchema.ExtraDataSchema.COLUMN_DATABASE_ID + "=?", new String[]{String.valueOf(extraData.getMahakId()), String.valueOf(extraData.getItemId()), extraData.getDatabaseId()})) > 0;
                    if (!result)
                        mDb.insert(DbSchema.ExtraDataSchema.TABLE_NAME, null, initialvalue);
                }
                if (extraData.getItemType() == BaseActivity.person_extra_info) {
                    ContentValues initvalue = new ContentValues();
                    try {
                        person_extra_data = gson.fromJson(extraData.getData(), Person_Extra_Data.class);
                    } catch (JsonSyntaxException e) {
                        FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                        FirebaseCrashlytics.getInstance().recordException(e);
                        e.printStackTrace();
                    }
                    double amount = person_extra_data.getRemainAmount();
                    if (person_extra_data.getRemainStatus() == 1) {
                        amount = amount * -1;
                    }
                    initvalue.put(DbSchema.CustomerSchema.COLUMN_BALANCE, amount);
                    mDb.update(DbSchema.CustomerSchema.TABLE_NAME, initvalue,
                            DbSchema.CustomerSchema.COLUMN_PersonCode + "=? and " + DbSchema.CustomerSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(extraData.getItemId()), String.valueOf(getPrefUserId())});
                } else if (extraData.getItemType() == BaseActivity.city_zone) {
                    add_city_zone(extraData);
                } else if (extraData.getItemType() == BaseActivity.category) {
                    add_category(extraData);
                } else if (extraData.getItemType() == BaseActivity.product_category) {
                    add_product_category(extraData);
                }else if (extraData.getItemType() == BaseActivity.person_category) {
                    add_person_category(extraData);
                }
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }

        return result;
    }

    private void add_product_category(ExtraData extraData) {
        Gson gson = new Gson();
        ProductCategory productCategory;
        try {
            productCategory = gson.fromJson(extraData.getData(), ProductCategory.class);
            AddProductCategory(productCategory);
        } catch (JsonSyntaxException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }

    private void add_person_category(ExtraData extraData) {
        Gson gson = new Gson();
        PersonCategory personCategory;
        try {
            personCategory = gson.fromJson(extraData.getData(), PersonCategory.class);
            AddPersonCategory(personCategory);
        } catch (JsonSyntaxException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }

    private void add_category(ExtraData extraData) {
        Gson gson = new Gson();
        Category category;
        try {
            category = gson.fromJson(extraData.getData(), Category.class);
            AddCategory(category);
        } catch (JsonSyntaxException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }

    private void add_city_zone(ExtraData extraData) {
        Gson gson = new Gson();
        CityZone_Extra_Data cityZone_extra_data;
        try {
            cityZone_extra_data = gson.fromJson(extraData.getData(), CityZone_Extra_Data.class);
            AddCityZone(cityZone_extra_data);
        } catch (JsonSyntaxException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }

    public boolean UpdateOrder(Order order) {
        boolean result;

        ContentValues initialvalue = new ContentValues();

        initialvalue.put(DbSchema.OrderSchema.COLUMN_PersonId, order.getPersonId());

        initialvalue.put(DbSchema.OrderSchema.COLUMN_LATITUDE, order.getLatitude());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_LONGITUDE, order.getLongitude());

        initialvalue.put(DbSchema.OrderSchema.COLUMN_ReturnReasonId, order.getReturnReasonId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_PersonClientId, order.getPersonClientId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_DELIVERYDATE, order.getDeliveryDate());

        initialvalue.put(DbSchema.OrderSchema.COLUMN_ORDERDATE, order.getOrderDate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_DISCOUNT, order.getDiscount());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_DESCRIPTION, order.getDescription());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_CODE, order.getCode());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_SETTLEMEMNTTYPE, order.getSettlementType());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_TYPE, order.getOrderType());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_MODIFYDATE, order.getModifyDate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_PUBLISH, order.getPublish());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_PROMOTION_CODE, order.getPromotionCode());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_GIFT_TYPE, order.getGiftType());

        initialvalue.put(DbSchema.OrderSchema.COLUMN_OrderId, order.getOrderId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_OrderClientId, order.getOrderClientId());

        initialvalue.put(DbSchema.OrderSchema.COLUMN_ReceiptId, order.getReceiptId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_ReceiptClientId, order.getReceiptClientId());

        initialvalue.put(DbSchema.OrderSchema.COLUMN_SendCost, order.getSendCost());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_OtherCost, order.getOtherCost());

        initialvalue.put(DbSchema.OrderSchema.COLUMN_DataHash, order.getDataHash());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_CreateDate, order.getCreateDate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_UpdateDate, order.getUpdateDate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_CreateSyncId, order.getCreateSyncId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_UpdateSyncId, order.getUpdateSyncId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_RowVersion, order.getRowVersion());

        result = (mDb.update(DbSchema.OrderSchema.TABLE_NAME, initialvalue, DbSchema.OrderSchema.COLUMN_ID + "=?", new String[]{String.valueOf(order.getId())})) > 0;
        return result;
    }

    public boolean UpdateReceipt(Receipt receipt) {
        boolean result;

        ContentValues initialvalue = new ContentValues();

        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_USER_ID, receipt.getVisitorId());
        initialvalue.put(DbSchema.ReceiptSchema.PERSON_ID, receipt.getPersonId());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_PersonClientId, receipt.getPersonClientId());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_CASHAMOUNT, receipt.getCashAmount());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_DESCRIPTION, receipt.getDescription());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_MODIFYDATE, receipt.getModifyDate());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_DATE, receipt.getDate());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_PUBLISH, receipt.getPublish());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_CODE, receipt.getTrackingCode());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_ReceiptClientId, receipt.getReceiptClientId());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_ReceiptId, receipt.getReceiptId());
        initialvalue.put(DbSchema.ReceiptSchema.COLUMN_CashCode, receipt.getCashCode());

        result = (mDb.update(DbSchema.ReceiptSchema.TABLE_NAME, initialvalue, DbSchema.ReceiptSchema.COLUMN_ID + "=?", new String[]{String.valueOf(receipt.getId())})) > 0;
        return result;
    }

    public boolean UpdateNonRegister(NonRegister nonRegister) {
        boolean result;

        ContentValues initialvalue = new ContentValues();

        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_USER_ID, nonRegister.getVisitorId());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_PersonId, nonRegister.getPersonId());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_PersonClientId, nonRegister.getPersonClientId());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_DESCRIPTION, nonRegister.getDescription());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_MODIFYDATE, nonRegister.getModifyDate());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_NonRegister_DATE, nonRegister.getNotRegisterDate());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_PUBLISH, nonRegister.getPublish());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_CODE, nonRegister.getCode());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_ReasonCode, nonRegister.getReasonCode());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_CustomerName, nonRegister.getCustomerName());

        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_NotRegisterId, nonRegister.getNotRegisterId());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_NotRegisterClientId, nonRegister.getNotRegisterClientId());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_DataHash, nonRegister.getDataHash());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_CreateDate, nonRegister.getCreateDate());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_UpdateDate, nonRegister.getUpdateDate());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_CreateSyncId, nonRegister.getCreateSyncId());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_UpdateSyncId, nonRegister.getUpdateSyncId());
        initialvalue.put(DbSchema.NonRegisterSchema.COLUMN_RowVersion, nonRegister.getRowVersion());

        result = (mDb.update(DbSchema.NonRegisterSchema.TABLE_NAME, initialvalue, DbSchema.NonRegisterSchema.COLUMN_ID + "=?", new String[]{String.valueOf(nonRegister.getId())})) > 0;
        return result;
    }

    public boolean UpdatePayable(PayableTransfer payableTransfer) {
        boolean result;

        ContentValues initialvalue = new ContentValues();

        initialvalue.put(DbSchema.PayableSchema.COLUMN_USER_ID, payableTransfer.getUserId());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_MahakId, payableTransfer.getMahakId());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_TransferCode, payableTransfer.getTransferCode());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_TransferDate, payableTransfer.getTransferDate());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_TransferType, payableTransfer.getTransferType());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_Receiverid, payableTransfer.getReceiverId());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_Comment, payableTransfer.getDescription());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_PayerId, payableTransfer.getPayerId());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_PUBLISH, payableTransfer.getPublish());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_VisitorId, payableTransfer.getVisitorId());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_Price, payableTransfer.getPrice());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_TransferAccountId, payableTransfer.getTransferAccountId());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_TransferAccountClientId, payableTransfer.getTransferAccountClientId());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_TransferAccountCode, payableTransfer.getTransferAccountCode());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_DataHash, payableTransfer.getDataHash());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_CreateDate, payableTransfer.getCreateDate());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_UpdateDate, payableTransfer.getUpdateDate());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_CreateSyncId, payableTransfer.getCreateSyncId());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_UpdateSyncId, payableTransfer.getUpdateSyncId());
        initialvalue.put(DbSchema.PayableSchema.COLUMN_RowVersion, payableTransfer.getRowVersion());


        result = (mDb.update(DbSchema.PayableSchema.TABLE_NAME, initialvalue, DbSchema.PayableSchema.COLUMN_TransferAccountClientId + "=?", new String[]{String.valueOf(payableTransfer.getTransferAccountClientId())})) > 0;
        return result;
    }

    public boolean UpdateOrAddProductDetail(List<ProductDetail> productDetails, long rowVersion) {
        boolean result = false;
        mDb.beginTransaction();
        try {
            ContentValues initialvalue = new ContentValues();
            for (ProductDetail productDetail : productDetails) {

                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_USER_ID, getPrefUserId());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_ProductDetailId, productDetail.getProductDetailId());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_ProductDetailClientId, productDetail.getProductDetailClientId());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_ProductDetailCode, productDetail.getProductDetailCode());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_ProductId, productDetail.getProductId());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Properties, productDetail.getProperties());

                //gets their value from visitor product
                //initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Count1, productDetail.getCount1());
                //initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Count2, productDetail.getCount2());

                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Barcode, productDetail.getBarcode());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Price1, productDetail.getPrice1());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Price2, productDetail.getPrice2());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Price3, productDetail.getPrice3());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Price4, productDetail.getPrice4());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Price5, productDetail.getPrice5());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Price6, productDetail.getPrice6());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Price7, productDetail.getPrice7());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Price8, productDetail.getPrice8());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Price9, productDetail.getPrice9());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Price10, productDetail.getPrice10());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_DefaultSellPriceLevel, productDetail.getDefaultSellPriceLevel());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Discount, productDetail.getDiscount());

                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Serials, productDetail.getSerials());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_DataHash, productDetail.getDataHash());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_CreateDate, productDetail.getCreateDate());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_UpdateDate, productDetail.getUpdateDate());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_CreateSyncId, productDetail.getCreateSyncId());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_UpdateSyncId, productDetail.getUpdateSyncId());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_RowVersion, productDetail.getRowVersion());

                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Discount1, productDetail.getDiscount1());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Discount2, productDetail.getDiscount2());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Discount3, productDetail.getDiscount3());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Discount4, productDetail.getDiscount4());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_DefaultDiscountLevel, productDetail.getDefaultDiscountLevel());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_DiscountType, productDetail.getDiscountType());
                initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_CustomerPrice, productDetail.getCustomerPrice());
                //initialvalue.put(DbSchema.ProductDetailSchema.COLUMN_Deleted, productDetail.isDeleted());

                if (rowVersion == 0)
                    mDb.insert(DbSchema.ProductDetailSchema.TABLE_NAME, null, initialvalue);
                else {
                    result = (mDb.update(DbSchema.ProductDetailSchema.TABLE_NAME, initialvalue, DbSchema.ProductDetailSchema.COLUMN_ProductDetailId + "=?", new String[]{String.valueOf(productDetail.getProductDetailId())})) > 0;
                    if (!result)
                        mDb.insert(DbSchema.ProductDetailSchema.TABLE_NAME, null, initialvalue);
                }

            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
        return result;
    }

    public boolean UpdateServerPriceLevelName(ProductPriceLevelName productPriceLevelName) {
        boolean result;

        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.PriceLevelNameSchema.PRICE_LEVEL_NAME, productPriceLevelName.getPriceLevelName());
        initialvalue.put(DbSchema.PriceLevelNameSchema.PRICE_LEVEL_CODE, productPriceLevelName.getPriceLevelCode());
        initialvalue.put(DbSchema.PriceLevelNameSchema._SyncId, productPriceLevelName.getSyncID());
        initialvalue.put(DbSchema.PriceLevelNameSchema.COLUMN_USER_ID, productPriceLevelName.getUserId());
        initialvalue.put(DbSchema.PriceLevelNameSchema._MODIFY_DATE, productPriceLevelName.getModifyDate());
        initialvalue.put(DbSchema.PriceLevelNameSchema.RowVersion, productPriceLevelName.getRowVersion());
        result = (mDb.update(DbSchema.PriceLevelNameSchema.TABLE_NAME, initialvalue, DbSchema.PriceLevelNameSchema.CostLevelNameId + "=? and " + DbSchema.ProductSchema.COLUMN_USER_ID + "=?", new String[]{String.valueOf(productPriceLevelName.getCostLevelNameId()), String.valueOf(productPriceLevelName.getUserId())})) > 0;
        return result;
    }

    public boolean UpdateServerCategory(ProductGroup productGroup) {
        boolean result;

        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_NAME, productGroup.getName());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_USER_ID, getPrefUserId());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_PARENTID, productGroup.getParentId());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_ICON, productGroup.getIcon());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_COLOR, productGroup.getColor());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_MODIFYDATE, productGroup.getModifyDate());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_MAHAK_ID, productGroup.getMahakId());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_ProductGroupCode, productGroup.getProductGroupCode());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_DATABASE_ID, productGroup.getDatabaseId());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_ProductGroupClientId, productGroup.getProductCategoryClientId());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_ProductGroupId, productGroup.getProductCategoryId());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_DataHash, productGroup.getDataHash());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_CreateDate, productGroup.getCreateDate());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_UpdateDate, productGroup.getUpdateDate());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_CreateSyncId, productGroup.getCreateSyncId());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_UpdateSyncId, productGroup.getUpdateSyncId());
        initialvalue.put(DbSchema.ProductGroupSchema.COLUMN_RowVersion, productGroup.getRowVersion());
        result = (mDb.update(DbSchema.ProductGroupSchema.TABLE_NAME, initialvalue, DbSchema.ProductGroupSchema.COLUMN_ProductGroupId + " =? ", new String[]{String.valueOf(productGroup.getProductCategoryId())})) > 0;
        return result;
    }

    public boolean UpdateProductFromTransfer(String productDetailId, double count1, double count2) {

        double Sum1 = getProductAsset1(productDetailId) + count1;
        double Sum2 = getProductAsset2(productDetailId) + count2;
        boolean result;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbSchema.VisitorProductSchema.COLUMN_Count1, Sum1);
        contentValues.put(DbSchema.VisitorProductSchema.COLUMN_Count2, Sum2);
        result = (mDb.update(DbSchema.VisitorProductSchema.TABLE_NAME, contentValues, DbSchema.VisitorProductSchema.COLUMN_ProductDetailId + " =? ", new String[]{productDetailId})) > 0;
        return result;

    }

    public boolean UpdateReceivedTransfer(String TransferStoreClientId, int Modify) {

        boolean result;
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_IsAccepted, Modify);
        result = (mDb.update(DbSchema.ReceivedTransfersSchema.TABLE_NAME, initialvalue, DbSchema.ReceivedTransfersSchema.COLUMN_TransferStoreClientId + "=?", new String[]{TransferStoreClientId})) > 0;
        return result;

    }

    public boolean UpdateReceivedTransfersFromServer(ReceivedTransfers receivedTransfers) {
        boolean result;

        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_TransferStoreCode, receivedTransfers.getTransferStoreCode());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_TransferDate, receivedTransfers.getTransferDate());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_DatabaseId, receivedTransfers.getDatabaseId());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_MahakId, receivedTransfers.getMahakId());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_Description, receivedTransfers.getDescription());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_CreatedBy, receivedTransfers.getCreatedBy());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_IsAccepted, receivedTransfers.getIsAccepted());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_ModifiedBy, receivedTransfers.getModifiedBy());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_SenderVisitorId, receivedTransfers.getSenderVisitorId());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_ReceiverVisitorId, receivedTransfers.getReceiverVisitorId());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_SyncId, receivedTransfers.getSyncId());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_ModifyDate, receivedTransfers.getModifyDate());
        initialvalue.put(DbSchema.ReceivedTransfersSchema.COLUMN_TransferStoreId, receivedTransfers.getTransferStoreId());

        result = (mDb.update
                (DbSchema.ReceivedTransfersSchema.TABLE_NAME, initialvalue, DbSchema.ReceivedTransfersSchema.COLUMN_TransferStoreId + "=? and " + DbSchema.ReceivedTransfersSchema.COLUMN_MahakId + "=? and " + DbSchema.ReceivedTransfersSchema.COLUMN_DatabaseId + "=?",
                        new String[]{String.valueOf(receivedTransfers.getTransferStoreId()),
                                receivedTransfers.getMahakId(),
                                receivedTransfers.getDatabaseId()})) > 0;
        return result;
    }

    public boolean UpdateReceivedTransferProducts(ReceivedTransferProducts receivedTransferProducts, Product product) {
        boolean result;

        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_Id, receivedTransferProducts.getId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_DatabaseId, receivedTransferProducts.getDatabaseId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_MahakId, receivedTransferProducts.getMahakId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_ModifyDate, receivedTransferProducts.getModifyDate());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_ProductId, receivedTransferProducts.getProductDetailId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_Count1, receivedTransferProducts.getCount1());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_Name, product.getName());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_TransferId, receivedTransferProducts.getTransferStoreId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_CreatedDate, receivedTransferProducts.getCreatedDate());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_CreatedBy, receivedTransferProducts.getCreatedBy());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_Description, receivedTransferProducts.getDescription());

        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_TransferStoreDetailId, receivedTransferProducts.getTransferStoreDetailId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_TransferStoreDetailClientId, receivedTransferProducts.getTransferStoreDetailClientId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_TransferStoreId, receivedTransferProducts.getTransferStoreId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_ProductDetailId, receivedTransferProducts.getProductDetailId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_Count2, receivedTransferProducts.getCount2());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_DataHash, receivedTransferProducts.getDataHash());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_CreateDate, receivedTransferProducts.getCreateDate());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_UpdateDate, receivedTransferProducts.getUpdateDate());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_CreateSyncId, receivedTransferProducts.getCreateSyncId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_UpdateSyncId, receivedTransferProducts.getUpdateSyncId());
        initialvalue.put(DbSchema.ReceivedTransferProductsSchema.COLUMN_RowVersion, receivedTransferProducts.getRowVersion());

        result = (mDb.update(DbSchema.ReceivedTransferProductsSchema.TABLE_NAME, initialvalue, DbSchema.ReceivedTransferProductsSchema.COLUMN_Id + "=? and " + DbSchema.ReceivedTransferProductsSchema.COLUMN_MahakId + "=? and " + DbSchema.ReceivedTransferProductsSchema.COLUMN_DatabaseId + "=?", new String[]{String.valueOf(receivedTransferProducts.getId()), receivedTransferProducts.getMahakId(), receivedTransferProducts.getDatabaseId()})) > 0;
        return result;
    }

    public boolean UpdateCheque(Cheque cheque) {
        boolean result;

        ContentValues initialvalue = new ContentValues();

        initialvalue.put(DbSchema.ChequeSchema.COLUMN_RECEIPTID, cheque.getReceiptId());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_BANK, cheque.getBankName());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_BANK_ID, cheque.getBankId());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_NUMBER, cheque.getNumber());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_BRANCH, cheque.getBranch());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_DESCRIPTION, cheque.getDescription());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_MODIFYDATE, cheque.getModifyDate());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_DATE, cheque.getDate());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_ChequeClientId, cheque.getChequeClientId());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_ReceiptClientId, cheque.getReceiptClientId());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_BankClientId, cheque.getBankClientId());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_TYPE, cheque.getType());
        initialvalue.put(DbSchema.ChequeSchema.COLUMN_PUBLISH, cheque.getPublish());

        result = (mDb.update(DbSchema.ChequeSchema.TABLE_NAME, initialvalue, DbSchema.ChequeSchema.COLUMN_ID + "=?", new String[]{String.valueOf(cheque.getId())})) > 0;
        return result;
    }

    public boolean UpdateServerTransactionsLog(TransactionsLog transactionlog) {
        boolean result;

        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_PersonId, transactionlog.getPersonId());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_USER_ID, BaseActivity.getPrefUserId());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_TYPE, transactionlog.getType());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_DEBITAMOUNT, transactionlog.getDebtAmount());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_CREDITAMOUNT, transactionlog.getCreditAmount());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_Balance, transactionlog.getBalance());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_STATUS, transactionlog.getStatus());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_DESCRIPTION, transactionlog.getDescription());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_MODIFYDATE, transactionlog.getModifyDate());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_TRANSACTIONID, transactionlog.getTransactionId());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_TransactionClientId, transactionlog.getTransactionClientId());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_DataHash, transactionlog.getDataHash());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_CreateDate, transactionlog.getCreateDate());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_UpdateDate, transactionlog.getUpdateDate());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_CreateSyncId, transactionlog.getCreateSyncId());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_UpdateSyncId, transactionlog.getUpdateSyncId());
        initialvalue.put(DbSchema.TransactionsLogSchema.COLUMN_RowVersion, transactionlog.getRowVersion());

        result = (mDb.update(DbSchema.TransactionsLogSchema.TABLE_NAME, initialvalue, DbSchema.TransactionsLogSchema.COLUMN_TRANSACTIONID + "=?", new String[]{String.valueOf(transactionlog.getTransactionId())})) > 0;
        return result;
    }

    public boolean UpdatePropertyDescription(PropertyDescription propertyDescription) {
        boolean result;

        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_PropertyDescriptionId, propertyDescription.getPropertyDescriptionId());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_PropertyDescriptionClientId, propertyDescription.getPropertyDescriptionClientId());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_PropertyDescriptionCode, propertyDescription.getPropertyDescriptionCode());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_NAME, propertyDescription.getName());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_Title, propertyDescription.getTitle());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_EmptyTitle, propertyDescription.getEmptyTitle());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_DataType, propertyDescription.getDescription());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_DisplayType, propertyDescription.getDataType());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_ExtraData, propertyDescription.getExtraData());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_Description, propertyDescription.getDescription());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_DataHash, propertyDescription.getDataHash());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_CreateDate, propertyDescription.getCreateDate());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_UpdateDate, propertyDescription.getUpdateDate());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_CreateSyncId, propertyDescription.getCreateSyncId());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_UpdateSyncId, propertyDescription.getUpdateSyncId());
        initialvalue.put(DbSchema.PropertyDescriptionSchema.COLUMN_RowVersion, propertyDescription.getRowVersion());

        result = (mDb.update(DbSchema.PropertyDescriptionSchema.TABLE_NAME, initialvalue, DbSchema.PropertyDescriptionSchema.COLUMN_PropertyDescriptionId + "=?", new String[]{String.valueOf(propertyDescription.getPropertyDescriptionId())})) > 0;
        return result;
    }

    public boolean UpdateCheckList(CheckList checklist) {
        boolean result;

        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_CUSTOMERID, checklist.getPersonId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_USER_ID, checklist.getUserId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_STATUS, checklist.getStatus());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_PUBLISH, checklist.getPublish());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_TYPE, checklist.getType());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_DESCRIPTION, checklist.getDescription());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_MODIFYDATE, checklist.getModifyDate());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_ChecklistId, checklist.getChecklistId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_ChecklistClientId, checklist.getChecklistClientId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_VisitorId, checklist.getVisitorId());

        result = (mDb.update(DbSchema.CheckListSchema.TABLE_NAME, initialvalue, DbSchema.CheckListSchema.COLUMN_ChecklistId + "=? ", new String[]{String.valueOf(checklist.getChecklistId())})) > 0;
        return result;
    }

    public boolean UpdateServerCheckList(CheckList checklist) {
        boolean result;

        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_CUSTOMERID, checklist.getPersonId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_USER_ID, checklist.getUserId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_STATUS, checklist.getStatus());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_TYPE, checklist.getType());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_DESCRIPTION, checklist.getDescription());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_MODIFYDATE, checklist.getModifyDate());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_CHECK_LIST_CODE, checklist.getChecklistCode());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_MAHAK_ID, checklist.getMahakId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_DATABASE_ID, checklist.getDatabaseId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_ChecklistId, checklist.getChecklistId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_ChecklistClientId, checklist.getChecklistClientId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_VisitorId, checklist.getVisitorId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_DataHash, checklist.getDataHash());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_CreateDate, checklist.getCreateDate());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_UpdateDate, checklist.getUpdateDate());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_CreateSyncId, checklist.getCreateSyncId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_UpdateSyncId, checklist.getUpdateSyncId());
        initialvalue.put(DbSchema.CheckListSchema.COLUMN_RowVersion, checklist.getRowVersion());

        result = (mDb.update(DbSchema.CheckListSchema.TABLE_NAME, initialvalue, DbSchema.CheckListSchema.COLUMN_ChecklistId + "=? ", new String[]{String.valueOf(checklist.getChecklistId())})) > 0;
        return result;
    }

    public boolean UpdateUser(User user) {
        boolean result;

        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.UserSchema.COLUMN_NAME, user.getName());
        initialvalue.put(DbSchema.UserSchema.COLUMN_PASSWORD, user.getPassword());
        initialvalue.put(DbSchema.UserSchema.COLUMN_USERNAME, user.getUsername());
        initialvalue.put(DbSchema.UserSchema.COLUMN_TYPE, user.getType());
        initialvalue.put(DbSchema.UserSchema.COLUMN_MODIFYDATE, user.getModifyDate());
        initialvalue.put(DbSchema.UserSchema.COLUMN_LOGINDATE, user.getLoginDate());
        initialvalue.put(DbSchema.UserSchema.COLUMN_MAHAK_ID, user.getMahakId());
        initialvalue.put(DbSchema.UserSchema.COLUMN_MASTER_ID, user.getMasterId());
        initialvalue.put(DbSchema.UserSchema.COLUMN_DATABASE_ID, user.getDatabaseId());
        initialvalue.put(DbSchema.UserSchema.COLUMN_PACKAGE_SERIAL, user.getPackageSerial());
        initialvalue.put(DbSchema.UserSchema.COLUMN_SYNC_ID, user.getSyncId());
        initialvalue.put(DbSchema.UserSchema.COLUMN_DATE_SYNC, user.getDateSync());
        initialvalue.put(DbSchema.UserSchema.COLUMN_USER_ID, user.getServerUserID());
        initialvalue.put(DbSchema.UserSchema.COLUMN_UserToken, user.getUserToken());

        result = (mDb.update(DbSchema.UserSchema.TABLE_NAME, initialvalue, DbSchema.UserSchema.COLUMN_ID + "=?", new String[]{String.valueOf(user.getId())})) > 0;
        return result;
    }

    public boolean UpdateFinalDeliveryOrder(long deliveryid) {
        boolean result;

        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.OrderSchema.COLUMN_ISFINAL, ProjectInfo.FINAL);
        result = (mDb.update(DbSchema.OrderSchema.TABLE_NAME, initialvalue, DbSchema.OrderSchema.COLUMN_OrderId + "=?", new String[]{String.valueOf(deliveryid)})) > 0;
        return result;
    }

    public boolean UpdateServerDeliveryOrder(Order deliveryorder) {
        boolean result;

        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.OrderSchema.COLUMN_PersonId, deliveryorder.getPersonId());

        initialvalue.put(DbSchema.OrderSchema.COLUMN_LATITUDE, deliveryorder.getLatitude());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_LONGITUDE, deliveryorder.getLongitude());

        initialvalue.put(DbSchema.OrderSchema.COLUMN_ReturnReasonId, deliveryorder.getReturnReasonId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_PersonClientId, deliveryorder.getPersonClientId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_USER_ID, deliveryorder.getUserId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_DELIVERYDATE, deliveryorder.getDeliveryDate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_DESCRIPTION, deliveryorder.getDescription());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_DISCOUNT, deliveryorder.getDiscount());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_IMMEDIATE, deliveryorder.getImmediate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_MODIFYDATE, deliveryorder.getModifyDate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_SETTLEMEMNTTYPE, deliveryorder.getSettlementType());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_CODE, deliveryorder.getCode());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_ISFINAL, deliveryorder.getIsFinal());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_TYPE, deliveryorder.getOrderType());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_OrderId, deliveryorder.getOrderId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_OrderClientId, deliveryorder.getOrderClientId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_ReceiptId, deliveryorder.getReceiptId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_SendCost, deliveryorder.getSendCost());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_OtherCost, deliveryorder.getOtherCost());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_DataHash, deliveryorder.getDataHash());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_CreateDate, deliveryorder.getCreateDate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_UpdateDate, deliveryorder.getUpdateDate());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_CreateSyncId, deliveryorder.getCreateSyncId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_UpdateSyncId, deliveryorder.getUpdateSyncId());
        initialvalue.put(DbSchema.OrderSchema.COLUMN_RowVersion, deliveryorder.getRowVersion());

        result = (mDb.update(DbSchema.OrderSchema.TABLE_NAME, initialvalue, DbSchema.OrderSchema.COLUMN_OrderId + "=? ", new String[]{String.valueOf(deliveryorder.getOrderId())})) > 0;
        return result;
    }

    public boolean UpdateDeliveryOrderDetail(OrderDetail orderDetail) {
        boolean result;
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_OrderDetailId, orderDetail.getOrderDetailId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_OrderDetailClientId, orderDetail.getOrderDetailClientId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_OrderClientId, orderDetail.getOrderClientId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_OrderId, orderDetail.getOrderId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_ProductDetailId, orderDetail.getProductDetailId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_USER_ID, BaseActivity.getPrefUserId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Price, orderDetail.getPrice());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Count1, orderDetail.getCount1());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Count2, orderDetail.getCount2());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_GiftType, orderDetail.getGiftType());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Description, orderDetail.getDescription());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_TaxPercent, orderDetail.getTaxPercent());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_ChargePercent, orderDetail.getChargePercent());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Discount, orderDetail.getDiscount());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_DiscountType, orderDetail.getDiscountType());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_DataHash, orderDetail.getDataHash());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_CreateDate, orderDetail.getCreateDate());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_UpdateDate, orderDetail.getUpdateDate());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_CreateSyncId, orderDetail.getCreateSyncId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_UpdateSyncId, orderDetail.getUpdateSyncId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_RowVersion, orderDetail.getRowVersion());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_CostLevel, orderDetail.getCostLevel());

        result = (mDb.update(DbSchema.OrderDetailSchema.TABLE_NAME, initialvalue, DbSchema.OrderDetailSchema.COLUMN_OrderDetailId + "=?", new String[]{String.valueOf(orderDetail.getOrderDetailId())})) > 0;
        return result;
    }

    public boolean UpdateOrderDetail(OrderDetail orderDetail) {
        boolean result;
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_OrderDetailId, orderDetail.getOrderDetailId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_OrderDetailClientId, orderDetail.getOrderDetailClientId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_OrderClientId, orderDetail.getOrderClientId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_OrderId, orderDetail.getOrderId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_ProductDetailId, orderDetail.getProductDetailId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_USER_ID, BaseActivity.getPrefUserId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_ProductId, orderDetail.getProductId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Price, orderDetail.getPrice());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Count1, orderDetail.getCount1());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz, orderDetail.getSumCountBaJoz());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Count2, orderDetail.getCount2());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_GiftType, orderDetail.getGiftType());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Description, orderDetail.getDescription());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_TaxPercent, orderDetail.getTaxPercent());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_ChargePercent, orderDetail.getChargePercent());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Discount, orderDetail.getDiscount());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_DiscountType, orderDetail.getDiscountType());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_DataHash, orderDetail.getDataHash());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_CreateDate, orderDetail.getCreateDate());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_UpdateDate, orderDetail.getUpdateDate());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_CreateSyncId, orderDetail.getCreateSyncId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_UpdateSyncId, orderDetail.getUpdateSyncId());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_RowVersion, orderDetail.getRowVersion());
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_CostLevel, orderDetail.getCostLevel());

        result = (mDb.update(DbSchema.OrderDetailSchema.TABLE_NAME, initialvalue, DbSchema.OrderDetailSchema.COLUMN_ID + "=?", new String[]{String.valueOf(orderDetail.getId())})) > 0;
        return result;
    }

    public boolean UpdateOrderDetailSync(OrderDetail orderDetail) {
        boolean result;
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_OrderDetailId, orderDetail.getOrderDetailId());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_OrderDetailClientId, orderDetail.getOrderDetailClientId());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_OrderClientId, orderDetail.getOrderClientId());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_OrderId, orderDetail.getOrderId());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_ProductDetailId, orderDetail.getProductDetailId());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_USER_ID, BaseActivity.getPrefUserId());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_ProductId, orderDetail.getProductId());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Price, orderDetail.getPrice());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Count1, orderDetail.getCount1());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_SumCountBaJoz, orderDetail.getSumCountBaJoz());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Count2, orderDetail.getCount2());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_GiftType, orderDetail.getGiftType());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_Description, orderDetail.getDescription());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_TaxPercent, orderDetail.getTaxPercent());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_ChargePercent, orderDetail.getChargePercent());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_DiscountType, orderDetail.getDiscountType());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_DataHash, orderDetail.getDataHash());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_CreateDate, orderDetail.getCreateDate());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_UpdateDate, orderDetail.getUpdateDate());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_CreateSyncId, orderDetail.getCreateSyncId());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_UpdateSyncId, orderDetail.getUpdateSyncId());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_RowVersion, orderDetail.getRowVersion());
//        initialvalue.put(DbSchema.OrderDetailSchema.COLUMN_CostLevel, orderDetail.getCostLevel());

        result = (mDb.update(DbSchema.OrderDetailSchema.TABLE_NAME, initialvalue, DbSchema.OrderDetailSchema.COLUMN_ID + "=?", new String[]{String.valueOf(orderDetail.getId())})) > 0;
        return result;
    }

    public boolean UpdateOrderDetailPropertyWithOrderid(long orderId, OrderDetailProperty orderDetailProperty) {
        boolean result;
        ContentValues initialvalue = new ContentValues();
        initialvalue.put(DbSchema.OrderDetailPropertySchema.COLUMN_OrderDetailClientId, orderDetailProperty.getOrderDetailClientId());
        initialvalue.put(DbSchema.OrderDetailPropertySchema.COLUMN_OrderId, orderDetailProperty.getOrderId());
        initialvalue.put(DbSchema.OrderDetailPropertySchema.COLUMN_ProductDetailId, orderDetailProperty.getProductDetailId());
        initialvalue.put(DbSchema.OrderDetailPropertySchema.COLUMN_ProductId, orderDetailProperty.getProductId());
        initialvalue.put(DbSchema.OrderDetailPropertySchema.COLUMN_Count1, orderDetailProperty.getCount1());
        initialvalue.put(DbSchema.OrderDetailPropertySchema.COLUMN_SumCountBaJoz, orderDetailProperty.getSumCountBaJoz());
        initialvalue.put(DbSchema.OrderDetailPropertySchema.COLUMN_Count2, orderDetailProperty.getCount2());
        initialvalue.put(DbSchema.OrderDetailPropertySchema.COLUMN_ProductSpec, orderDetailProperty.getProductSpec());

        result = (mDb.update(DbSchema.OrderDetailPropertySchema.TABLE_NAME, initialvalue, DbSchema.OrderDetailPropertySchema.COLUMN_ProductDetailId + " =? and " + DbSchema.OrderDetailPropertySchema.COLUMN_OrderId + " =? ", new String[]{String.valueOf(orderDetailProperty.getProductDetailId()), String.valueOf(orderId)})) > 0;
        return result;
    }

    public boolean DeleteOrderDetailProperty(long id) {
        return (mDb.delete(DbSchema.OrderDetailPropertySchema.TABLE_NAME, DbSchema.OrderDetailPropertySchema.COLUMN_OrderId + "=?", new String[]{String.valueOf(id)})) > 0;
    }


    //QUERIES DELETE__________________________________________________________________

    public boolean DeleteCustomer(Long id) {
        return (mDb.delete(DbSchema.CustomerSchema.TABLE_NAME, DbSchema.CustomerSchema.COLUMN_ID + "=?", new String[]{String.valueOf(id)})) > 0;
    }

    public boolean DeleteClientCustomer(Long id) {
        return (mDb.delete(DbSchema.CustomerSchema.TABLE_NAME, DbSchema.CustomerSchema.COLUMN_PersonClientId + "=?", new String[]{String.valueOf(id)})) > 0;
    }

    public boolean DeleteServerVisitor(Visitor visitor) {
        return (mDb.delete(DbSchema.VisitorSchema.TABLE_NAME, DbSchema.VisitorSchema.COLUMN_VisitorId + "=? ", new String[]{String.valueOf(visitor.getVisitorId())})) > 0;
    }

    public boolean DeleteServerBank(Bank bank) {
        return (mDb.delete(DbSchema.BanksSchema.TABLE_NAME, DbSchema.BanksSchema.COLUMN_MAHAK_ID + "=? and " + DbSchema.BanksSchema.COLUMN_BANK_CODE + "=? and " + DbSchema.BanksSchema.COLUMN_DATABASE_ID + "=? ", new String[]{bank.getMahakId(), String.valueOf(bank.getBankCode()), bank.getDatabaseId()})) > 0;
    }

    public boolean DeleteCustomerGroup(CustomerGroup customergroup) {
        return (mDb.delete(DbSchema.CustomersGroupSchema.TABLE_NAME, DbSchema.CustomersGroupSchema.COLUMN_MAHAK_ID + "=? and " + DbSchema.CustomersGroupSchema.COLUMN_PersonGroupCode + "=? and " + DbSchema.CustomersGroupSchema.COLUMN_DATABASE_ID + "=?", new String[]{customergroup.getMahakId(), String.valueOf(customergroup.getPersonGroupCode()), customergroup.getDatabaseId()})) > 0;
    }

    public void DeleteCategory(ProductGroup productGroup) {
        mDb.delete(DbSchema.ProductGroupSchema.TABLE_NAME, DbSchema.ProductGroupSchema.COLUMN_MAHAK_ID + "=? and " + DbSchema.ProductGroupSchema.COLUMN_ProductGroupCode + "=? and " + DbSchema.ProductGroupSchema.COLUMN_DATABASE_ID + "=?", new String[]{productGroup.getMahakId(), String.valueOf(productGroup.getProductGroupCode()), productGroup.getDatabaseId()});
    }

    public boolean DeleteOrderDetail(long id) {
        return (mDb.delete(DbSchema.OrderDetailSchema.TABLE_NAME, DbSchema.OrderDetailSchema.COLUMN_OrderId + "=?", new String[]{String.valueOf(id)})) > 0;
    }

    public boolean DeleteUser(int id) {
        return (mDb.delete(DbSchema.UserSchema.TABLE_NAME, DbSchema.UserSchema.COLUMN_USER_ID + "=?", new String[]{String.valueOf(id)})) > 0;
    }

    public boolean DeleteOrder(long id) {
        return (mDb.delete(DbSchema.OrderSchema.TABLE_NAME, DbSchema.OrderSchema.COLUMN_ID + "=?", new String[]{String.valueOf(id)})) > 0;
    }

    public boolean DeleteOrderWithOrderId(long orderId) {
        return (mDb.delete(DbSchema.OrderSchema.TABLE_NAME, DbSchema.OrderSchema.COLUMN_OrderId + "=?", new String[]{String.valueOf(orderId)})) > 0;
    }

    public void DeleteNonRegister(long id) {
        mDb.delete(DbSchema.NonRegisterSchema.TABLE_NAME, DbSchema.NonRegisterSchema.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public boolean DeleteReceipt(long id) {
        return (mDb.delete(DbSchema.ReceiptSchema.TABLE_NAME, DbSchema.ReceiptSchema.COLUMN_ID + "=?", new String[]{String.valueOf(id)})) > 0;
    }

    public boolean DeleteReceivedTransfer(String id) {
        return (mDb.delete(DbSchema.ReceivedTransfersSchema.TABLE_NAME, DbSchema.ReceivedTransfersSchema.COLUMN_TransferStoreId + "=?", new String[]{id})) > 0;
    }

    public boolean DeletePayable(long id) {
        return (mDb.delete(DbSchema.PayableSchema.TABLE_NAME, DbSchema.PayableSchema.COLUMN_ID + "=?", new String[]{String.valueOf(id)})) > 0;
    }

    public boolean DeleteReceipts(String code) {
        return (mDb.delete(DbSchema.ReceiptSchema.TABLE_NAME, DbSchema.ReceiptSchema.COLUMN_CODE + "=?", new String[]{code})) > 0;
    }

    public boolean DeleteChequesInReceipt(long id) {
        return (mDb.delete(DbSchema.ChequeSchema.TABLE_NAME, DbSchema.ChequeSchema.COLUMN_RECEIPTID + "=?", new String[]{String.valueOf(id)})) > 0;
    }

    public boolean DeleteChecklist(CheckList check) {
        return (mDb.delete(DbSchema.CheckListSchema.TABLE_NAME, DbSchema.CheckListSchema.COLUMN_MAHAK_ID + "=? and " + DbSchema.CheckListSchema.COLUMN_CHECK_LIST_CODE + "=? and " + DbSchema.CheckListSchema.COLUMN_DATABASE_ID + "=?", new String[]{check.getMahakId(), String.valueOf(check.getChecklistCode()), check.getDatabaseId()})) > 0;
    }

    public boolean DeleteTransactionLog(TransactionsLog tran) {
        return (mDb.delete(DbSchema.TransactionsLogSchema.TABLE_NAME, DbSchema.TransactionsLogSchema.COLUMN_TRANSACTIONID + " =?", new String[]{String.valueOf(tran.getTransactionId())})) > 0;
    }

    public boolean DeleteDeliveryOrder(Order deliveryOrder) {
        return (mDb.delete(DbSchema.OrderSchema.TABLE_NAME, DbSchema.OrderSchema.COLUMN_MAHAK_ID + "=? and " + DbSchema.OrderSchema.COLUMN_OrderId + "=? and " + DbSchema.OrderSchema.COLUMN_DATABASE_ID + "=? ", new String[]{deliveryOrder.getMahakId(), String.valueOf(deliveryOrder.getOrderId()), deliveryOrder.getDatabaseId()})) > 0;
    }

    public boolean DeleteOrderDetail(OrderDetail orderDetail) {
        return (mDb.delete(DbSchema.OrderDetailSchema.TABLE_NAME, DbSchema.OrderDetailSchema.COLUMN_OrderDetailId + "=?", new String[]{String.valueOf(orderDetail.getOrderDetailId())})) > 0;
    }

    public boolean DeletePicturesProduct(long pictureId) {
        return (mDb.delete(DbSchema.PicturesProductSchema.TABLE_NAME, DbSchema.PicturesProductSchema.COLUMN_PICTURE_ID + " =? ", new String[]{String.valueOf(pictureId)})) > 0;
    }

    public boolean DeletePropertyDescription(long propertyDescriptionId) {
        return (mDb.delete(DbSchema.PropertyDescriptionSchema.TABLE_NAME, DbSchema.PropertyDescriptionSchema.COLUMN_PropertyDescriptionId + " =? ", new String[]{String.valueOf(propertyDescriptionId)})) > 0;
    }

    public boolean DeleteAllPromotion() {
        return (mDb.delete(DbSchema.PromotionSchema.TABLE_NAME, null, null)) > 0;
    }

    public boolean DeleteAllPromotionEntity() {
        return (mDb.delete(DbSchema.PromotionEntitySchema.TABLE_NAME, null, null)) > 0;
    }

    public boolean DeleteAllPromotionDetail() {
        return (mDb.delete(DbSchema.PromotionDetailSchema.TABLE_NAME, null, null)) > 0;
    }

    public boolean isColumnExists (String table, String column) {
        boolean isExists = false;
        Cursor cursor = null;
        try {
            cursor = mDb.rawQuery("PRAGMA table_info("+ table +")", null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    if (column.equalsIgnoreCase(name)) {
                        isExists = true;
                        break;
                    }
                }
            }

        } finally {
            if (cursor != null)
                cursor.close();
        }
        return isExists;
    }

    public void DeleteAllData() {

        mDb.delete(DbSchema.CustomersGroupSchema.TABLE_NAME, DbSchema.CustomersGroupSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.CustomerSchema.TABLE_NAME, DbSchema.CustomerSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.TransactionsLogSchema.TABLE_NAME, DbSchema.TransactionsLogSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.BanksSchema.TABLE_NAME, DbSchema.BanksSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.ProductSchema.TABLE_NAME, DbSchema.ProductSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.ReceiptSchema.TABLE_NAME, DbSchema.ReceiptSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.ChequeSchema.TABLE_NAME, null, null);
        mDb.delete(DbSchema.ProductGroupSchema.TABLE_NAME, DbSchema.ProductGroupSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.CheckListSchema.TABLE_NAME, DbSchema.CheckListSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.OrderSchema.TABLE_NAME, DbSchema.OrderSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.OrderDetailSchema.TABLE_NAME, DbSchema.OrderDetailSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.ConfigsSchema.TABLE_NAME, DbSchema.ConfigsSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.PicturesProductSchema.TABLE_NAME, DbSchema.PicturesProductSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.ReceivedTransfersSchema.TABLE_NAME, null, null);
        mDb.delete(DbSchema.ReceivedTransferProductsSchema.TABLE_NAME, null, null);
        mDb.delete(DbSchema.PayableSchema.TABLE_NAME, DbSchema.PayableSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.VisitorSchema.TABLE_NAME, DbSchema.VisitorSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.ReasonsSchema.TABLE_NAME, null, null);
        mDb.delete(DbSchema.NonRegisterSchema.TABLE_NAME, DbSchema.NonRegisterSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.PriceLevelNameSchema.TABLE_NAME, DbSchema.PriceLevelNameSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.ExtraDataSchema.TABLE_NAME, DbSchema.ExtraDataSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.PromotionSchema.TABLE_NAME, DbSchema.PromotionSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.PromotionDetailSchema.TABLE_NAME, DbSchema.PromotionDetailSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.PromotionEntitySchema.TABLE_NAME, DbSchema.PromotionEntitySchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.ProductDetailSchema.TABLE_NAME, DbSchema.ProductDetailSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.OrderDetailPropertySchema.TABLE_NAME, null, null);
        mDb.delete(DbSchema.PropertyDescriptionSchema.TABLE_NAME, null, null);
        mDb.delete(DbSchema.VisitorProductSchema.TABLE_NAME, DbSchema.VisitorProductSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.VisitorPeopleSchema.TABLE_NAME, DbSchema.VisitorPeopleSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.SettingSchema.TABLE_NAME, DbSchema.SettingSchema.COLUMN_USER_ID + " =? ", new String[]{String.valueOf(BaseActivity.getPrefUserId())});
        mDb.delete(DbSchema.CityZoneSchema.TABLE_NAME, null, null);
        mDb.delete(DbSchema.ProductCategorySchema.TABLE_NAME, null, null);
        mDb.delete(DbSchema.PersonCategorySchema.TABLE_NAME, null, null);
        mDb.delete(DbSchema.CategorySchema.TABLE_NAME, null, null);


        BaseActivity.setPrefAdminControl(false);
        BaseActivity.setPrefTrackingControl(0);

        //reset setting
        BaseActivity.setPrefUnit2Setting(BaseActivity.MODE_YekVahedi);
        BaseActivity.setPrefTaxAndChargeIsActive(BaseActivity.InActive);
        BaseActivity.setPrefTaxPercent(BaseActivity.InActive);
        BaseActivity.setPrefChargePercent(BaseActivity.InActive);
        BaseActivity.setPrefRowDiscountIsActive(BaseActivity.invisible);
        BaseActivity.setPrefAutoSyncValue(BaseActivity.InActive);

        // BaseActivity.setPrefCustomerMaxRowVersion(0);

        ServiceTools.scheduleAlarm(mCtx);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String INDEX_Product = "products_index";
        private SQLiteDatabase Db;

        DatabaseHelper(Context context , String DatabaseName) {
            super(context, DatabaseName, null, DbSchema.DATABASE_VERSION);
            DB_PATH = Environment.getDataDirectory() + "/data/" + context.getPackageName() + "/databases/";
        }

        SQLiteDatabase openDataBase(String DbName) throws SQLException {
            final String mPath = mContext.getDatabasePath(DbName).toString();
            if(CheckDatabase(mPath)){
                try {
                    Db = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READWRITE);
                } catch (SQLiteDatabaseLockedException e) {
                    e.printStackTrace();
                }
            }else {
                Db = this.getWritableDatabase();
            }
            return Db;
        }

        Boolean CheckDatabase(String mPath) {

            Boolean res;
            File file = new File(mPath);
            res = file.exists();
            return res;
        }

        @Override
        public synchronized void close() {
            if (Db != null)
                Db.close();
            super.close();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DbSchema.UserSchema.CREATE_TABLE);
            db.execSQL(DbSchema.CustomerSchema.CREATE_TABLE);
            db.execSQL(DbSchema.VisitorSchema.CREATE_TABLE);

            db.execSQL(DbSchema.VisitorProductSchema.CREATE_TABLE);
            db.execSQL(DbSchema.VisitorPeopleSchema.CREATE_TABLE);
            db.execSQL(DbSchema.PromotionSchema.CREATE_TABLE);

            db.execSQL(DbSchema.PromotionDetailSchema.CREATE_TABLE);
            db.execSQL(DbSchema.PromotionEntitySchema.CREATE_TABLE);
            db.execSQL(DbSchema.CustomersGroupSchema.CREATE_TABLE);

            db.execSQL(DbSchema.ProductSchema.CREATE_TABLE);
            db.execSQL(DbSchema.ProductDetailSchema.CREATE_TABLE);
            db.execSQL(DbSchema.PriceLevelNameSchema.CREATE_TABLE);

            db.execSQL(DbSchema.ReceivedTransfersSchema.CREATE_TABLE);
            db.execSQL(DbSchema.ReceivedTransferProductsSchema.CREATE_TABLE);
            db.execSQL(DbSchema.ProductGroupSchema.CREATE_TABLE);

            db.execSQL(DbSchema.OrderSchema.CREATE_TABLE);
            db.execSQL(DbSchema.OrderDetailSchema.CREATE_TABLE);
            db.execSQL(DbSchema.OrderDetailPropertySchema.CREATE_TABLE);

            db.execSQL(DbSchema.NonRegisterSchema.CREATE_TABLE);
            db.execSQL(DbSchema.ReceiptSchema.CREATE_TABLE);
            db.execSQL(DbSchema.PayableSchema.CREATE_TABLE);

            db.execSQL(DbSchema.ChequeSchema.CREATE_TABLE);
            db.execSQL(DbSchema.CheckListSchema.CREATE_TABLE);
            db.execSQL(DbSchema.TransactionsLogSchema.CREATE_TABLE);

            db.execSQL(DbSchema.BanksSchema.CREATE_TABLE);
            db.execSQL(DbSchema.PropertyDescriptionSchema.CREATE_TABLE);
            db.execSQL(DbSchema.ReasonsSchema.CREATE_TABLE);

            db.execSQL(DbSchema.ExtraDataSchema.CREATE_TABLE);
            db.execSQL(DbSchema.ProductCategorySchema.CREATE_TABLE);
            db.execSQL(DbSchema.PersonCategorySchema.CREATE_TABLE);
            db.execSQL(DbSchema.CategorySchema.CREATE_TABLE);

            db.execSQL(DbSchema.CityZoneSchema.CREATE_TABLE);
            db.execSQL(DbSchema.ConfigsSchema.CREATE_TABLE);

            db.execSQL(DbSchema.PicturesProductSchema.CREATE_TABLE);
            db.execSQL(DbSchema.SettingSchema.CREATE_TABLE);

            
            db.execSQL(DbSchema.PhotoGallerySchema.CREATE_TABLE);
            db.execSQL(DbSchema.RegionSchema.CREATE_TABLE);

            db.execSQL("CREATE INDEX " + INDEX_Product + " ON " + DbSchema.ProductSchema.TABLE_NAME + "(" + DbSchema.ProductSchema.COLUMN_PRODUCT_CODE + ")");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.e("TAG", "Updating table from " + oldVersion + " to " + newVersion);

            ServiceTools.writeLog("oldVersion is " + oldVersion + "newVersion" + newVersion);

            if (newVersion > oldVersion) {

                if (oldVersion < 3) {
                    db.execSQL(DbSchema.PicturesProductSchema.CREATE_TABLE);
                }
                if (oldVersion < 6) {
                    db.execSQL(DbSchema.PayableSchema.CREATE_TABLE);
                    db.execSQL(DbSchema.ReceivedTransfersSchema.CREATE_TABLE);
                    db.execSQL(DbSchema.ReceivedTransferProductsSchema.CREATE_TABLE);
                    db.execSQL("ALTER TABLE " + DbSchema.CustomerSchema.TABLE_NAME + " ADD " + DbSchema.CustomerSchema.COLUMN_DiscountPercent + " NUMERIC;");
                    db.execSQL("ALTER TABLE " + DbSchema.ProductSchema.TABLE_NAME + " ADD " + DbSchema.ProductSchema.COLUMN_DiscountPercent + " TEXT;");

                }
                if (oldVersion < 7) {
                    db.execSQL("ALTER TABLE " + DbSchema.PromotionSchema.TABLE_NAME + " ADD " + DbSchema.PromotionSchema.COLUMN_Deleted + " INTEGER;");
                }
                if (oldVersion < 3450){
                    db.execSQL(DbSchema.PersonCategorySchema.CREATE_TABLE);
                }
            }
        }

        @Override
        public void onOpen(SQLiteDatabase db) {

            Cursor cursor = null;
            try {
                cursor = db.rawQuery("PRAGMA synchronous=OFF", null);
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            // From Honeycomb on, it's possible to run several db
            // commands in parallel using multiple connections.
            db.enableWriteAheadLogging();
            db.setLockingEnabled(false);
        }

    }

}
