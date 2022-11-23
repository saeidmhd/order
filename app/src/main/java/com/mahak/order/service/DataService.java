package com.mahak.order.service;

import android.content.Context;

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
import com.mahak.order.common.PhotoGallery;
import com.mahak.order.common.Region;
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
import com.mahak.order.common.TransactionsLog;
import com.mahak.order.common.Visitor;
import com.mahak.order.common.VisitorPeople;
import com.mahak.order.common.VisitorProduct;
import com.mahak.order.mission.Mission;
import com.mahak.order.mission.MissionDetail;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.storage.RadaraDb;
import com.mahak.order.tracking.visitorZone.Datum;
import com.mahak.order.tracking.visitorZone.ZoneLocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DataService {

    static int TRUE = 1;
    static int FALSE = 0;

    public static double InsertCustomer(DbAdapter db, List<Customer> data, long rowVersion) {
        long startTime = System.nanoTime();
        db.UpdateOrAddServerCustomerFast(data,rowVersion);
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static void InsertZone(RadaraDb db, Datum data) {
        
        db.AddZone(data);
       
    }

    public static void InsertZoneLocation(RadaraDb db, List<ZoneLocation> data) {
        
        db.AddZoneLocation(data);
       
    }

    public static double InsertCustomerGroup(DbAdapter db, List<CustomerGroup> data) {
        long startTime = System.nanoTime();
        
        Date date = new Date();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getDeleted() == FALSE) {
                data.get(i).setModifyDate(date.getTime());
                if (!db.UpdateServerCustomerGroup(data.get(i)))
                    db.AddCustomerGroup(data.get(i));
            } else
                db.DeleteCustomerGroup(data.get(i));
        }
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertVisitorPeople(DbAdapter db, List<VisitorPeople> data, long rowVersion) {
        long startTime = System.nanoTime();
        
        db.UpdateOrAddVisitorPeopleFast(data , rowVersion);
        //db.UpdatePersonFromVisitorPeopleFast(data);
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertVisitor(DbAdapter db, List<Visitor> data) {
        long startTime = System.nanoTime();
        
        db.UpdateOrAddServerVisitor(data);
        Visitor visitor = db.getVisitor();
        BaseActivity.setRadaraActive(visitor.HasRadara());
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertBank(DbAdapter db, List<Bank> data) {
        long startTime = System.nanoTime();
        
        Date date = new Date();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getDeleted() == FALSE) {
                data.get(i).setModifyDate(date.getTime());
                if (!db.UpdateServerBank(data.get(i)))
                    db.AddBank(data.get(i));
            } else
                db.DeleteServerBank(data.get(i));
        }
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertCategory(DbAdapter db, List<ProductGroup> data) {
        long startTime = System.nanoTime();
        
        Date date = new Date();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getDeleted() == FALSE) {
                data.get(i).setModifyDate(date.getTime());
                if (!db.UpdateServerCategory(data.get(i)))
                    db.AddProductGroup(data.get(i));
            } else
                db.DeleteCategory(data.get(i));
        }
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertProduct(DbAdapter db, List<Product> data, long rowVersion) {
        long startTime = System.nanoTime();
        
        db.UpdateOrAddServerProductFast(data , rowVersion);
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertProductDetail(DbAdapter db, List<ProductDetail> data, long rowVersion) {
        long startTime = System.nanoTime();
        
        db.UpdateOrAddProductDetail(data,rowVersion);
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertVisitorProducts(DbAdapter db, List<VisitorProduct> data, long rowVersion) {
        long startTime = System.nanoTime();
        db.UpdateOrAddVisitorProductFast(data,rowVersion);
        //db.UpdateProductFromVisitorProductFast2();
       /* db.UpdateProductFromVisitorProductFast(data);
        db.UpdateProductDetailFromVisitorProductFast(data);*/

       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertSettings(DbAdapter db, List<Setting> data, Context mContext) {
        long startTime = System.nanoTime();
        
        for (int i = 0; i < data.size(); i++) {
            Setting setting = data.get(i);
            db.AddSetting(setting);
        }
       
        ServiceTools.setSettingPreferences(db, mContext);
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }
    public static double InsertMissions(DbAdapter db, List<Mission> data, Context mContext) {
        long startTime = System.nanoTime();

        for (int i = 0; i < data.size(); i++) {
            Mission mission = data.get(i);
            db.AddMission(mission);
        }

        ServiceTools.setSettingPreferences(db, mContext);
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }
    public static double InsertMissionDetails(DbAdapter db, List<MissionDetail> data, Context mContext) {
        long startTime = System.nanoTime();
        for (int i = 0; i < data.size(); i++) {
            MissionDetail missionDetail = data.get(i);
            db.AddMissionDetail(missionDetail);
        }
        ArrayList<Mission> missions = new ArrayList<>();
        ArrayList<MissionDetail> missionDetails = new ArrayList<>();
        missions = db.getAllMission2();
        for (Mission mission : missions){
            missionDetails = db.getAllMissionDetailWithMissionId(mission.getMissionId());
            setMissionStatus(missionDetails,mission,db);
        }
        ServiceTools.setSettingPreferences(db, mContext);
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static void setMissionStatus(ArrayList<MissionDetail> missionDetails, Mission mission, DbAdapter db) {
        int done_count = 0;
        int non_started = 0;
        for (MissionDetail missionDetail : missionDetails){
            switch (missionDetail.getStatus()){
                case 1:
                    non_started++;
                    break;
                case 3:
                case 4:
                    done_count++;
                    break;
            }
        }
        if(missionDetails.size() == done_count){
            mission.setStatusDate(ServiceTools.getFormattedDateAndTime(new Date().getTime()));
            mission.setStatus(3);
        }
        else if(missionDetails.size() == non_started){
            mission.setStatusDate(null);
            mission.setStatus(1);
        }
        else{
            mission.setStatusDate(null);
            mission.setStatus(2);
        }
        db.AddMission(mission);
    }

    public static double InsertCheckList(DbAdapter db, List<CheckList> data) {
        long startTime = System.nanoTime();
        
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
                        db.AddCheckList(data.get(i));
            } else
                db.DeleteChecklist(data.get(i));
        }
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertTransactionsLog(DbAdapter db, List<TransactionsLog> arrayTransactionsLog) {
        long startTime = System.nanoTime();
        
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
                    db.AddTransactionsLog(arrayTransactionsLog.get(i));
            } else
                db.DeleteTransactionLog(arrayTransactionsLog.get(i));
        }
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertPicturesProduct(DbAdapter db, List<PicturesProduct> data) {
        long startTime = System.nanoTime();
        
        for (int i = 0; i < data.size(); i++) {
            PicturesProduct picturesProduct = data.get(i);
            if (picturesProduct.getItemType() == 102) {
                if (!picturesProduct.isDeleted()) {
                    if (db.UpdatePicturesProduct(picturesProduct) <= 0)
                        db.addPicturesProductFromWeb(picturesProduct);
                } else {
                    db.DeletePicturesProduct(picturesProduct.getPictureId());
                }
            }
        }
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertPhotoGallery(DbAdapter db, List<PhotoGallery> data ,long rowVersion) {

        long startTime = System.nanoTime();
        
        db.UpdateOrAddPhotoGallery(data,rowVersion);
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;

    }

    public static double InsertRegion(DbAdapter db, List<Region> data ,long rowVersion) {

        long startTime = System.nanoTime();
        
        db.UpdateOrAddRegion(data,rowVersion);
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;

    }

    public static double InsertPropertyDescription(DbAdapter db, List<PropertyDescription> data) {
        long startTime = System.nanoTime();

        for (int i = 0; i < data.size(); i++) {
            PropertyDescription propertyDescription = data.get(i);
            if (propertyDescription.getDeleted() == FALSE) {
                if (!db.UpdatePropertyDescription(propertyDescription))
                    db.AddPropertyDescription(propertyDescription);
            } else {
                db.DeletePropertyDescription(propertyDescription.getPropertyDescriptionId());
            }
        }
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertReceivedTransfers(DbAdapter db, List<ReceivedTransfers> data) {
        long startTime = System.nanoTime();
        
        //get json from server
        Date date = new Date();
        for (int i = 0; i < data.size(); i++) {
            //if item is new add then to database
            //else item isnot new then update to database
            if (!data.get(i).isDeleted()) {
                if (data.get(i).getIsAccepted() == ProjectInfo.TYPE_NaN && data.get(i).getReceiverVisitorId().equals(String.valueOf(BaseActivity.getPrefUserId()))) {
                    if (!db.UpdateReceivedTransfersFromServer(data.get(i)))
                        db.AddReceivedTransfers(data.get(i));
                }
            } else
                db.DeleteReceivedTransfer(data.get(i).getTransferStoreId());
        }
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertReceivedTransferProduct(DbAdapter db, List<ReceivedTransferProducts> data) {
        long startTime = System.nanoTime();
        
        Product product;
        ProductDetail productDetail;
        for (int i = 0; i < data.size(); i++) {
            productDetail = db.getProductDetail(ServiceTools.toLong(data.get(i).getProductDetailId()));
            product = db.GetProductWithProductId(productDetail.getProductId());
            if (!db.UpdateReceivedTransferProducts(data.get(i), product)) {
                db.AddReceivedTransferProducts(data.get(i), product);
            }

                /*if(db.CheckExistProduct(Long.parseLong(data.get(i).getProductDetailId())) == 0){
                    db.AddProductFromReceivedTransfer(data.get(i));
                }*/
        }
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertReason(DbAdapter db, List<Reasons> data) {
        long startTime = System.nanoTime();
        
        Date date = new Date();
        for (int i = 0; i < data.size(); i++) {
            //set Now date in modifyDate
            data.get(i).setModifyDate(date.getTime());
            //if item is new add then to database
            //else item isnot new then update to database
            if (!db.UpdateReasons(data.get(i)))
                db.AddReasons(data.get(i));
        }
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertPromotion(DbAdapter db, List<Promotion> data) {
        long startTime = System.nanoTime();
        
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
                    ServiceTools.logToFireBase(e);
                    e.printStackTrace();
                }
                if(data.get(i).getDeleted() == 0)
                    db.AddPromotion(data.get(i), promotionOtherFields);
        }
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertPromotionDetails(DbAdapter db, List<PromotionDetail> data) {
        long startTime = System.nanoTime();
        
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
                ServiceTools.logToFireBase(e);
                e.printStackTrace();
            }
            //if item is new add then to database
            //else item is not new then update to database
            if(!data.get(i).isDeleted())
                db.AddPromotionDetail(data.get(i), promotionDetailOtherFields);
        }
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertEntitiesOfPromotions(DbAdapter db, List<PromotionEntity> data) {
        long startTime = System.nanoTime();
        
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
                ServiceTools.logToFireBase(e);
                e.printStackTrace();
            }
            //if item is new add then to database
            //else item is not new then update to database
            if(!data.get(i).isDeleted())
                db.AddEntitiesOfPromotions(data.get(i), promotionEntityOtherFields);

        }
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertExtraInfo(DbAdapter db, List<ExtraData> data, long rowVersion) {
        long startTime = System.nanoTime();
        
        db.UpdateOrAddExtraInfo(data , rowVersion);
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertDeliveryOrder(DbAdapter db, List<Order> orders) {
        long startTime = System.nanoTime();
        

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
                        db.AddDeliveryOrder(orders.get(i));
                } else
                    db.DeleteDeliveryOrder(orders.get(i));
            }
        }
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

    public static double InsertDeliveryOrderDetail(DbAdapter db, List<OrderDetail> orderDetails, Context mContext) {
        long startTime = System.nanoTime();
        
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
                    sumCountBaJoz = orderDetails.get(i).getCount1();
                    if (product.getProductId() != 0) {
                        if (BaseActivity.getPrefUnit2Setting(mContext) != BaseActivity.MODE_YekVahedi){
                            if(product.getUnitRatio() > 0){
                                double count1 = orderDetails.get(i).getCount1() - (orderDetails.get(i).getCount2() * product.getUnitRatio());
                                orderDetails.get(i).setCount1(count1);
                            }
                        }
                        orderDetails.get(i).setSumCountBaJoz(sumCountBaJoz);
                        orderDetails.get(i).setProductId(product.getProductId());
                        if (orderDetails.get(i).getDescription() == null)
                            orderDetails.get(i).setDescription("");
                        db.AddDeliveryOrderDetail(orderDetails.get(i));
                    } else
                        db.DeleteOrderWithOrderId(orderDetails.get(i).getOrderId());
                }
            } else
                db.DeleteOrderDetail(orderDetails.get(i));
        }
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;

    }

    public static double InsertCostLevelName(DbAdapter db, List<ProductPriceLevelName> data) {
        long startTime = System.nanoTime();
        
        Date date = new Date();
        for (int i = 0; i < data.size(); i++) {

            data.get(i).setModifyDate(date.getTime());
            if (!db.UpdateServerPriceLevelName(data.get(i)))
                db.AddPriceLevelName(data.get(i));
        }
       
        long endTime = System.nanoTime();
        return (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;
    }

}
