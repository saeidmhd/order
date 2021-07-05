package com.mahak.order.service;

import android.content.Context;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mahak.order.BaseActivity;
import com.mahak.order.common.Bank;
import com.mahak.order.common.CheckList;
import com.mahak.order.common.Customer;
import com.mahak.order.common.CustomerGroup;
import com.mahak.order.common.ExtraData;
import com.mahak.order.common.Order;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.PicturesProduct;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductDetail;
import com.mahak.order.common.ProductGroup;
import com.mahak.order.common.ProductPriceLevelName;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.Promotion;
import com.mahak.order.common.PromotionDetail;
import com.mahak.order.common.PromotionDetailOtherFields;
import com.mahak.order.common.PromotionEntity;
import com.mahak.order.common.PromotionEntityOtherFields;
import com.mahak.order.common.PromotionOtherFields;
import com.mahak.order.common.PropertyDescription;
import com.mahak.order.common.Reasons;
import com.mahak.order.common.ReceivedTransferProducts;
import com.mahak.order.common.ReceivedTransfers;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.Setting;
import com.mahak.order.common.TrackingConfig;
import com.mahak.order.common.TransactionsLog;
import com.mahak.order.common.Visitor;
import com.mahak.order.common.VisitorPeople;
import com.mahak.order.common.VisitorProduct;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import com.mahak.order.common.DeliveryOrder;

public class DataService {

    static int TRUE = 1;
    static int FALSE = 0;
    private static long result = 0;

    public static double InsertCustomer(DbAdapter db, List<Customer> data, long rowVersion) {
        long startTime = System.nanoTime();
        db.open();
        db.UpdateOrAddServerCustomerFast(data,rowVersion);
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertCustomerGroup(DbAdapter db, List<CustomerGroup> data) {
        long startTime = System.nanoTime();
        db.open();
        Date date = new Date();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getDeleted() == FALSE) {
                data.get(i).setModifyDate(date.getTime());
                if (!db.UpdateServerCustomerGroup(data.get(i)))
                    result = db.AddCustomerGroup(data.get(i));
            } else
                db.DeleteCustomerGroup(data.get(i));
        }
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertVisitorPeople(DbAdapter db, List<VisitorPeople> data, long rowVersion) {
        long startTime = System.nanoTime();
        db.open();
        db.UpdateOrAddVisitorPeopleFast(data , rowVersion);
        db.UpdatePersonFromVisitorPeopleFast(data);
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertVisitor(DbAdapter db, List<Visitor> data) {
        long startTime = System.nanoTime();
        db.open();
        db.UpdateOrAddServerVisitor(data);
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertBank(DbAdapter db, List<Bank> data) {
        long startTime = System.nanoTime();
        db.open();
        Date date = new Date();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getDeleted() == FALSE) {
                data.get(i).setModifyDate(date.getTime());
                if (!db.UpdateServerBank(data.get(i)))
                    result = db.AddBank(data.get(i));
            } else
                db.DeleteServerBank(data.get(i));
        }
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertCategory(DbAdapter db, List<ProductGroup> data) {
        long startTime = System.nanoTime();
        db.open();
        Date date = new Date();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getDeleted() == FALSE) {
                data.get(i).setModifyDate(date.getTime());
                if (!db.UpdateServerCategory(data.get(i)))
                    result = db.AddProductGroup(data.get(i));
            } else
                db.DeleteCategory(data.get(i));
        }
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertProduct(DbAdapter db, List<Product> data, long rowVersion) {
        long startTime = System.nanoTime();
        db.open();
        db.UpdateOrAddServerProductFast(data , rowVersion);
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertProductDetail(DbAdapter db, List<ProductDetail> data, long rowVersion) {
        long startTime = System.nanoTime();
        db.open();
        db.UpdateOrAddProductDetail(data,rowVersion);
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertVisitorProducts(DbAdapter db, List<VisitorProduct> data, long rowVersion) {
        long startTime = System.nanoTime();
        db.open();

        db.UpdateOrAddVisitorProductFast(data,rowVersion);
        db.UpdateProductFromVisitorProductFast(data);
        db.UpdateProductDetailFromVisitorProductFast(data);

        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertSettings(DbAdapter db, List<Setting> data, Context mContext) {
        long startTime = System.nanoTime();
        db.open();
        for (int i = 0; i < data.size(); i++) {
            Setting setting = data.get(i);
            if (!db.UpdateSetting(setting)) {
                result = db.AddSetting(setting);
            }
        }
        db.close();
        ServiceTools.setSettingPreferences(db, mContext);
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertCheckList(DbAdapter db, List<CheckList> data) {
        long startTime = System.nanoTime();
        db.open();
        Date date = new Date();
        for (int i = 0; i < data.size(); i++) {
            //if isDelete == false then new or update to database
            //else if iDelete == true then delete from database
            if (data.get(i).getDeleted() == FALSE) {
                //find Customerid from local CustomerTest category
               /* long Customerid = db.getId(BaseActivity.T_WithUserId, DbSchema.Customerschema.TABLE_NAME, data.get(i).getPersonId());
                data.get(i).setPersonId(Customerid);*/
                data.get(i).setModifyDate(date.getTime());

                //if item is new add then to database
                //else item isnot new then update to database
                if (data.get(i).getStatus() != ProjectInfo.STATUS_DO)
                    if (!db.UpdateServerCheckList(data.get(i)))
                        result = db.AddCheckList(data.get(i));
            } else
                db.DeleteChecklist(data.get(i));
        }
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertTransactionsLog(DbAdapter db, List<TransactionsLog> arrayTransactionsLog) {
        long startTime = System.nanoTime();
        db.open();
        Date date = new Date();
        for (int i = 0; i < arrayTransactionsLog.size(); i++) {
            //if isDelete == false then new or update to database
            //else if iDelete == true then delete from database
            if (arrayTransactionsLog.get(i).getDeleted() == FALSE) {
                //find Customerid from local CustomerTest category
              /*  long Customerid = db.getId(BaseActivity.T_WithUserId, DbSchema.Customerschema.TABLE_NAME, arrayTransactionsLog.get(i).getPersonId());
                arrayTransactionsLog.get(i).setPersonId(Customerid);*/
                arrayTransactionsLog.get(i).setModifyDate(date.getTime());

                //if item is new add then to database
                //else item isnot new then update to database
                if (!db.UpdateServerTransactionsLog(arrayTransactionsLog.get(i)))
                    result = db.AddTransactionsLog(arrayTransactionsLog.get(i));
            } else
                db.DeleteTransactionLog(arrayTransactionsLog.get(i));
        }
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertPicturesProduct(DbAdapter db, List<PicturesProduct> data) {
        long startTime = System.nanoTime();
        db.open();
        for (int i = 0; i < data.size(); i++) {
            PicturesProduct picturesProduct = data.get(i);
            if (picturesProduct.getItemType() == 102) {
                if (!picturesProduct.isDeleted()) {
                    if (db.UpdatePicturesProduct(picturesProduct) <= 0)
                        result = db.addPicturesProductFromWeb(picturesProduct);
                } else {
                    db.DeletePicturesProduct(picturesProduct.getPictureId());
                }
            }
        }
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertPropertyDescription(DbAdapter db, List<PropertyDescription> data) {
        long startTime = System.nanoTime();

        db.open();

        for (int i = 0; i < data.size(); i++) {
            PropertyDescription propertyDescription = data.get(i);
            if (propertyDescription.getDeleted() == FALSE) {
                if (!db.UpdatePropertyDescription(propertyDescription))
                    result = db.AddPropertyDescription(propertyDescription);
            } else {
                db.DeletePropertyDescription(propertyDescription.getPropertyDescriptionId());
            }
        }
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertReceivedTransfers(DbAdapter db, List<ReceivedTransfers> data) {
        long startTime = System.nanoTime();
        db.open();
        //get json from server
        Date date = new Date();
        for (int i = 0; i < data.size(); i++) {
            //if item is new add then to database
            //else item isnot new then update to database
            if (!data.get(i).isDeleted()) {
                if (data.get(i).getIsAccepted() == ProjectInfo.TYPE_NaN && data.get(i).getReceiverVisitorId().equals(String.valueOf(BaseActivity.getPrefUserId()))) {
                    if (!db.UpdateReceivedTransfersFromServer(data.get(i)))
                        result = db.AddReceivedTransfers(data.get(i));
                }
            } else
                db.DeleteReceivedTransfer(data.get(i).getTransferStoreId());
        }
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertReceivedTransferProduct(DbAdapter db, List<ReceivedTransferProducts> data) {
        long startTime = System.nanoTime();
        db.open();
        Product product;
        ProductDetail productDetail;
        for (int i = 0; i < data.size(); i++) {
            productDetail = db.getProductDetail(ServiceTools.toLong(data.get(i).getProductDetailId()));
            product = db.GetProductWithProductId(productDetail.getProductId());
            if (!db.UpdateReceivedTransferProducts(data.get(i), product)) {
                result = db.AddReceivedTransferProducts(data.get(i), product);
            }

                /*if(db.CheckExistProduct(Long.parseLong(data.get(i).getProductDetailId())) == 0){
                    db.AddProductFromReceivedTransfer(data.get(i));
                }*/
        }
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertReason(DbAdapter db, List<Reasons> data) {
        long startTime = System.nanoTime();
        db.open();
        Date date = new Date();
        for (int i = 0; i < data.size(); i++) {
            //set Now date in modifyDate
            data.get(i).setModifyDate(date.getTime());
            //if item is new add then to database
            //else item isnot new then update to database
            if (!db.UpdateReasons(data.get(i)))
                result = db.AddReasons(data.get(i));
        }
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertPromotion(DbAdapter db, List<Promotion> data) {
        long startTime = System.nanoTime();
        db.open();
        db.DeleteAllPromotion();
        Date date = new Date();
        Gson gson = new Gson();
        PromotionOtherFields promotionOtherFields = new PromotionOtherFields();
        for (int i = 0; i < data.size(); i++) {
                //set Now date in modifyDate
                data.get(i).setModifyDate(date.getTime());
                try {
                    promotionOtherFields = gson.fromJson(data.get(i).getPromotionOtherFields(), PromotionOtherFields.class);
                } catch (JsonSyntaxException e) {
                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                    FirebaseCrashlytics.getInstance().recordException(e);
                    e.printStackTrace();
                }
                db.upgradeDatabase();
                if(data.get(i).getDeleted() == 0)
                    result = db.AddPromotion(data.get(i), promotionOtherFields);
        }
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertPromotionDetails(DbAdapter db, List<PromotionDetail> data) {
        long startTime = System.nanoTime();
        db.open();
        db.DeleteAllPromotionDetail();
        Date date = new Date();
        Gson gson = new Gson();
        PromotionDetailOtherFields promotionDetailOtherFields = new PromotionDetailOtherFields();
        for (int i = 0; i < data.size(); i++) {
            //set Now date in modifyDate
            data.get(i).setModifyDate(date.getTime());
            try {
                promotionDetailOtherFields = gson.fromJson(data.get(i).getPromotionDetailOtherFields(), PromotionDetailOtherFields.class);
            } catch (JsonSyntaxException e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
            //if item is new add then to database
            //else item is not new then update to database
            if(!data.get(i).isDeleted())
                result = db.AddPromotionDetail(data.get(i), promotionDetailOtherFields);
        }
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertEntitiesOfPromotions(DbAdapter db, List<PromotionEntity> data) {
        long startTime = System.nanoTime();
        db.open();
        // ArrayList<PromotionEntity> arrayEntitiesOfPromotions = Parser.getEntitiesOfPromotions(data);
        db.DeleteAllPromotionEntity();
        Date date = new Date();
        Gson gson = new Gson();
        PromotionEntityOtherFields promotionEntityOtherFields = new PromotionEntityOtherFields();
        for (int i = 0; i < data.size(); i++) {
            //set Now date in modifyDate
            data.get(i).setModifyDate(date.getTime());
            try {
                promotionEntityOtherFields = gson.fromJson(data.get(i).getPromotionEntityOtherFields(), PromotionEntityOtherFields.class);
            } catch (JsonSyntaxException e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
            //if item is new add then to database
            //else item is not new then update to database
            if(!data.get(i).isDeleted())
                result = db.AddEntitiesOfPromotions(data.get(i), promotionEntityOtherFields);

        }
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertExtraInfo(DbAdapter db, List<ExtraData> data, long rowVersion) {
        long startTime = System.nanoTime();
        db.open();
        db.UpdateOrAddExtraInfo(data , rowVersion);
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertDeliveryOrder(DbAdapter db, List<Order> orders) {
        long startTime = System.nanoTime();
        db.open();

        Date date = new Date();
        for (int i = 0; i < orders.size(); i++) {
            //if isDelete == false then new or update to database
            //else if iDelete == true then delete from database
            if (orders.get(i).getOrderType() == ProjectInfo.TYPE_Delivery) {
                if (orders.get(i).getDelete() == FALSE) {
                    //find Customerid from local CustomerTest category
                    orders.get(i).setModifyDate(date.getTime());
                    //if item is new add then to database
                    //else item isnot new then update to database
                    if (!db.UpdateServerDeliveryOrder(orders.get(i)))
                        result = db.AddDeliveryOrder(orders.get(i));
                } else
                    db.DeleteDeliveryOrder(orders.get(i));
            }
        }
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertDeliveryOrderDetail(DbAdapter db, List<OrderDetail> orderDetails, Context mContext) {
        long startTime = System.nanoTime();
        db.open();
        //if isDelete == false then new or update to database
        //else if iDelete == true then delete from database
        Product product;
        ProductDetail productDetail;
        double sumCountBaJoz;

        for (int i = 0; i < orderDetails.size(); i++) {
            if (orderDetails.get(i).isDeleted() == FALSE) {
                if (!db.UpdateDeliveryOrderDetail(orderDetails.get(i))) {
                    productDetail = db.getProductDetail(orderDetails.get(i).getProductDetailId());
                    product = db.GetProductWithProductId(productDetail.getProductId());
                    if (product.getProductId() != 0) {
                        if (BaseActivity.getPrefUnit2Setting(mContext) == BaseActivity.Mode_DoVahedi)
                            sumCountBaJoz = orderDetails.get(i).getCount1() + (product.getUnitRatio() * orderDetails.get(i).getCount2());
                        else
                            sumCountBaJoz = orderDetails.get(i).getCount1();
                        orderDetails.get(i).setSumCountBaJoz(sumCountBaJoz);
                        orderDetails.get(i).setProductId(product.getProductId());
                        if (orderDetails.get(i).getDescription() == null)
                            orderDetails.get(i).setDescription("");
                        result = db.AddDeliveryOrderDetail(orderDetails.get(i));
                    } else
                        db.DeleteOrderWithOrderId(orderDetails.get(i).getOrderId());
                }
            } else
                db.DeleteOrderDetail(orderDetails.get(i));
        }
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;

    }

    public static double InsertCostLevelName(DbAdapter db, List<ProductPriceLevelName> data) {
        long startTime = System.nanoTime();
        db.open();
        Date date = new Date();
        for (int i = 0; i < data.size(); i++) {

            data.get(i).setModifyDate(date.getTime());
            if (!db.UpdateServerPriceLevelName(data.get(i)))
                result = db.AddPriceLevelName(data.get(i));
        }
        db.close();
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static void ChangeTrackingConfig(String data) {

        ArrayList<TrackingConfig> arrayTrackingConfig = Parser.getTrackingConfig(data);

        for (int i = 0; i < arrayTrackingConfig.size(); i++) {

            BaseActivity.setPrefAdminControl(arrayTrackingConfig.get(i).getAdminControl());
            BaseActivity.setPrefTrackingControl(arrayTrackingConfig.get(i).getTrackingControl());

        }
    }

}
