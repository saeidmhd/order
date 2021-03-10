package com.mahak.order.autoSync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.mahak.order.BaseActivity;
import com.mahak.order.R;
import com.mahak.order.apiHelper.ApiClient;
import com.mahak.order.apiHelper.ApiInterface;
import com.mahak.order.common.Bank;
import com.mahak.order.common.CheckList;
import com.mahak.order.common.Cheque;
import com.mahak.order.common.Customer;
import com.mahak.order.common.CustomerGroup;
import com.mahak.order.common.NonRegister;
import com.mahak.order.common.Order;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.OrderDetailProperty;
import com.mahak.order.common.PayableTransfer;
import com.mahak.order.common.PicturesProduct;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductDetail;
import com.mahak.order.common.ProductGroup;
import com.mahak.order.common.ProductPriceLevelName;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.Promotion;
import com.mahak.order.common.PromotionDetail;
import com.mahak.order.common.PromotionEntity;
import com.mahak.order.common.PropertyDescription;
import com.mahak.order.common.Reasons;
import com.mahak.order.common.Receipt;
import com.mahak.order.common.Setting;
import com.mahak.order.common.TransactionsLog;
import com.mahak.order.common.User;
import com.mahak.order.common.Visitor;
import com.mahak.order.common.VisitorPeople;
import com.mahak.order.common.VisitorProduct;
import com.mahak.order.common.login.LoginBody;
import com.mahak.order.common.login.LoginResult;
import com.mahak.order.common.request.GetAllDataBody;
import com.mahak.order.common.request.GetAllDataResult.GetDataResult;
import com.mahak.order.common.request.SetAllDataBody;
import com.mahak.order.common.request.SetAllDataResult.SaveAllDataResult;
import com.mahak.order.service.DataService;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.storage.DbSchema;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mahak.order.BaseActivity.MODE_MeghdarJoz;
import static com.mahak.order.BaseActivity.setPrefSyncId;

public class AutoSyncService extends IntentService {

    private DbAdapter db;

    List<Bank> bankLists;
    List<CustomerGroup> personGroupLists;
    List<Customer> customerLists;
    List<Visitor> visitorLists;

    List<CheckList> checkLists;
    List<TransactionsLog> transactionsLogs;
    List<Order> orders;
    List<OrderDetail> orderDetails;
    List<Reasons> reasons;

    List<Promotion> promotions;
    List<PromotionDetail> promotionDetails;
    List<PromotionEntity> promotionEntities;

    List<PicturesProduct> picturesProducts;
    List<com.mahak.order.common.ExtraData> extraData;
    List<PropertyDescription> propertyDescriptions;
    List<VisitorProduct> visitorProducts;
    List<Product> productList;
    List<ProductDetail> productDetails;
    List<ProductGroup> productGroupLists;
    List<ProductPriceLevelName> productPriceLevelNames;

    List<Setting> settings;
    Context mContext;
    private long CustomerMaxRowVersion;
    private long CustomersGroupMaxRowVersion;
    private long BankMaxRowVersion;
    private long VisitorMaxRowVersion;
    private long CheckListMaxRowVersion;
    private long TransactionslogMaxRowVersion;
    private long OrderRowMaxVersion;
    private long OrderDetailMaxRowVersion;
    private long ReasonMaxRowVersion;
    private long PromotionMaxRowVersion;
    private long PromotionDetailMaxRowVersion;
    private long PromotionEntityMaxRowVersion;
    private long PicturesMaxRowVersion;
    private long ExtraDataMaxRowVersion;
    private long PropertyDescriptionMaxRowVersion;
    private long VisitorProductMaxRowVersion;
    private long PriceLevelMaxRowVersion;
    private long CategoryMaxRowVersion;
    private long ProductMaxRowVersion;
    private long ProductDetailMaxRowVersion;
    private long SettingMaxRowVersion;


    public AutoSyncService() {
        super("AutoSyncService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        Log.i("AutoSyncService", "Service running");
        SendReceive();
    }

    private OrderDetail createOrderDetail(OrderDetailProperty orderDetailProperty, OrderDetail orderDetail) {

        OrderDetail orderDetailSend = new OrderDetail();

        orderDetailSend.setOrderClientId(orderDetail.getOrderClientId());
        orderDetailSend.setOrderDetailClientId(orderDetailProperty.getOrderDetailClientId());

        orderDetailSend.setProductId(orderDetailProperty.getProductId());
        orderDetailSend.setProductDetailId(orderDetailProperty.getProductDetailId());

        orderDetailSend.setCount2(orderDetailProperty.getCount2());
        if (BaseActivity.getPrefUnit2Setting(mContext) == MODE_MeghdarJoz)
            orderDetailSend.setCount1(orderDetailProperty.getSumCountBaJoz());
        else
            orderDetailSend.setCount1(orderDetailProperty.getCount1());
        orderDetailSend.setSumCountBaJoz(orderDetailProperty.getSumCountBaJoz());

        orderDetailSend.setDeleted(orderDetail.isDeleted());
        orderDetailSend.setDescription(orderDetail.getDescription());
        orderDetailSend.setDiscountType(orderDetail.getDiscountType());
        orderDetailSend.setDiscount(orderDetail.getDiscount());
        orderDetailSend.setPrice("" + orderDetail.getPrice());
        orderDetailSend.setGiftCount1(orderDetail.getGiftCount1());
        orderDetailSend.setGiftCount2(orderDetail.getGiftCount2());
        orderDetailSend.setGiftType(orderDetail.getGiftType());
        orderDetailSend.setTaxPercent(orderDetail.getTaxPercent());
        orderDetailSend.setChargePercent(orderDetail.getChargePercent());

        return orderDetailSend;

    }


    public void SendReceive() {

        db = new DbAdapter(this);
        db.open();
        final User user = db.getUser();
        LoginBody loginBody = new LoginBody();
        loginBody.setAppId("MahakOrder");
        loginBody.setDatabaseId(0);
        loginBody.setLanguage("en-US");
        loginBody.setDeviceId("");
        loginBody.setDescription("login");
        loginBody.setUserName(user.getUsername());
        loginBody.setPassword(user.getPassword());

        ApiInterface apiService = ApiClient.orderRetrofitClient().create(ApiInterface.class);
        Call<LoginResult> call = apiService.Login(loginBody);
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.body() != null) {
                    if (response.body().isResult()) {
                        BaseActivity.setPrefUserToken(response.body().getData().getUserToken());
                        setPrefSyncId(response.body().getData().getSyncId());
                        //Save db
                        db.open();
                        user.setSyncId(response.body().getData().getSyncId());
                        user.setUserToken(response.body().getData().getUserToken());
                        db.UpdateUser(user);
                        db.close();
                        new SendAsyncTask(response.body().getData().getUserToken()).execute();
                        //setAndGetRequest(response.body().getData().getUserToken());
                    } else {
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
            }
        });
    }

    class SendAsyncTask extends AsyncTask<String, String, Integer> {

        List<Receipt> arrayReceipt = new ArrayList<>();
        List<Cheque> arrayCheque = new ArrayList<>();
        List<Order> arrayInvoice = new ArrayList<>();
        List<OrderDetail> arrayInvoiceDetail = new ArrayList<>();
        List<OrderDetail> orderDetails = new ArrayList<>();
        List<OrderDetailProperty> orderDetailProperties = new ArrayList<>();
        List<VisitorPeople> visitorPeopleArrayList = new ArrayList<>();
        List<NonRegister> arrayNonRegister = new ArrayList<>();
        List<PayableTransfer> payableTransfers = new ArrayList<>();
        List<CheckList> checkLists = new ArrayList<>();
        List<Customer> Customers = new ArrayList<>();
        List<Customer> newCustomers = new ArrayList<>();
        String mUserToken;

        SendAsyncTask(String UserToken) {
            mUserToken = UserToken;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            db.open();
            arrayInvoice = db.getAllOrderFamily(BaseActivity.getPrefUserId());
            Set<OrderDetail> set = new LinkedHashSet<>();
            for (int i = 0; i < arrayInvoice.size(); i++) {
                arrayInvoice.get(i).setOrderId(0);
                orderDetails = db.getAllOrderDetailForSend(arrayInvoice.get(i).getId());
                for (OrderDetail orderDetail : orderDetails) {
                    orderDetailProperties = db.getAllOrderDetailProperty(orderDetail.getOrderId(), orderDetail.getProductId());
                    if (orderDetailProperties.size() > 0) {
                        for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
                            if (orderDetailProperty.getSumCountBaJoz() > 0) {
                                set.add(createOrderDetail(orderDetailProperty, orderDetail));
                            }
                        }

                    } else
                        set.add(orderDetail);
                }
            }
            arrayInvoiceDetail = new ArrayList<>(set);

            arrayReceipt = db.getAllReceiptNotPublish(BaseActivity.getPrefUserId());
            arrayCheque = new ArrayList<>();

            for (int i = 0; i < arrayReceipt.size(); i++) {
                arrayCheque.addAll(db.getAllCheque(arrayReceipt.get(i).getId()));
            }
            arrayNonRegister = db.getAllNonRegisterNotPublish(BaseActivity.getPrefUserId());
            newCustomers = new ArrayList<>(db.getAllNewCustomer());
            for (Customer customer : newCustomers) {
                VisitorPeople visitorPeople = new VisitorPeople();
                visitorPeople.setPersonClientId(customer.getPersonClientId());
                visitorPeople.setVisitorId(customer.getUserId());
                visitorPeopleArrayList.add(visitorPeople);
            }
            Customers = new ArrayList<>(db.getAllCustomerForUpdate());
            payableTransfers = db.getAllPayableNotPublish(BaseActivity.getPrefUserId());
            checkLists = db.getAllDoneChecklistNotPublish();
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {

            final String[] mMsg = {""};

            ApiInterface apiService = ApiClient.orderRetrofitClient().create(ApiInterface.class);

            SetAllDataBody setAllDataBody = new SetAllDataBody();
            setAllDataBody.setUserToken(mUserToken);
            setAllDataBody.setOrderDetails(arrayInvoiceDetail);
            setAllDataBody.setOrders(arrayInvoice);
            setAllDataBody.setReceipts(arrayReceipt);
            setAllDataBody.setCheques(arrayCheque);
            setAllDataBody.setNonRegisters(arrayNonRegister);
            setAllDataBody.setVisitorPeople(visitorPeopleArrayList);
            setAllDataBody.setPeople(Customers);
            setAllDataBody.setPayableTransfers(payableTransfers);
            setAllDataBody.setChecklists(checkLists);
            Call<SaveAllDataResult> saveAllDataResultCall = apiService.SaveAllData(setAllDataBody);

            saveAllDataResultCall.enqueue(new Callback<SaveAllDataResult>() {
                @Override
                public void onResponse(@NonNull Call<SaveAllDataResult> call, @NonNull Response<SaveAllDataResult> response) {
                    if (response.body() != null && response.body().isResult()) {
                        db.open();
                        if (arrayInvoice.size() > 0) {
                            for (int i = 0; i < arrayInvoice.size(); i++) {
                                arrayInvoice.get(i).setOrderId(response.body().getData().getObjects().getOrders().getResults().get(i).getEntityID());
                                arrayInvoice.get(i).setPublish(ProjectInfo.PUBLISH);
                                db.UpdateOrder(arrayInvoice.get(i));
                                for (int j = 0; j < arrayInvoiceDetail.size(); j++) {
                                    arrayInvoiceDetail.get(j).setOrderDetailId(response.body().getData().getObjects().getOrderDetails().getResults().get(j).getEntityID());
                                    db.UpdateOrderDetail(arrayInvoiceDetail.get(j));
                                }
                            }
                        }
                        if (arrayReceipt.size() > 0) {
                            for (int i = 0; i < arrayReceipt.size(); i++) {
                                arrayReceipt.get(i).setReceiptId(response.body().getData().getObjects().getReceipts().getResults().get(i).getEntityID());
                                arrayReceipt.get(i).setPublish(ProjectInfo.PUBLISH);
                                db.UpdateReceipt(arrayReceipt.get(i));
                            }
                            for (int i = 0; i < arrayCheque.size(); i++) {
                                arrayCheque.get(i).setChequeId(response.body().getData().getObjects().getCheques().getResults().get(i).getEntityID());
                                arrayCheque.get(i).setPublish(ProjectInfo.PUBLISH);
                                db.UpdateCheque(arrayCheque.get(i));
                            }
                        }
                        if (arrayNonRegister.size() > 0) {
                            for (int i = 0; i < arrayNonRegister.size(); i++) {
                                arrayNonRegister.get(i).setNotRegisterId(response.body().getData().getObjects().getNotRegisters().getResults().get(i).getEntityID());
                                arrayNonRegister.get(i).setPublish(ProjectInfo.PUBLISH);
                                db.UpdateNonRegister(arrayNonRegister.get(i));
                            }
                        }
                        if (payableTransfers.size() > 0) {
                            for (int i = 0; i < payableTransfers.size(); i++) {
                                payableTransfers.get(i).setTransferAccountId(response.body().getData().getObjects().getPayableTransfers().getResults().get(i).getEntityID());
                                payableTransfers.get(i).setPublish(ProjectInfo.PUBLISH);
                                db.UpdatePayable(payableTransfers.get(i));
                            }
                        }
                        if (checkLists.size() > 0) {
                            for (int i = 0; i < checkLists.size(); i++) {
                                checkLists.get(i).setChecklistId(response.body().getData().getObjects().getChecklists().getResults().get(i).getEntityID());
                                checkLists.get(i).setPublish(ProjectInfo.PUBLISH);
                                db.UpdateCheckList(checkLists.get(i));
                            }
                        }
                        if (newCustomers.size() > 0) {
                            for (int i = 0; i < newCustomers.size(); i++) {
                                newCustomers.get(i).setPersonId(response.body().getData().getObjects().getPeople().getResults().get(i).getEntityID());
                                db.UpdateCustomerWithClientId(newCustomers.get(i));
                            }
                        }
                        db.close();

                        new ReceiveAsyncTask(mUserToken).execute();


                    } else if (response.body() != null) {
                        // mMsg[0] = response.body().getData().getObjects().getOrders().getResults().get(0).getErrors().get(0).getError();
                        mMsg[0] = getString(R.string.send_error);
                    }
                }

                @Override
                public void onFailure(Call<SaveAllDataResult> call, Throwable t) {
                    mMsg[0] = t.toString();
                }
            });
        }
    }

    class ReceiveAsyncTask extends AsyncTask<String, String, Integer> {
        String mUserToken;
        GetAllDataBody getAllDataBody;

        ReceiveAsyncTask(String UserToken) {
            mUserToken = UserToken;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... arg0) {

            db.open();

            getAllDataBody = new GetAllDataBody();
            getAllDataBody.setUserToken(mUserToken);

            CustomerMaxRowVersion = db.getMaxRowVersion(DbSchema.Customerschema.TABLE_NAME);
            CustomersGroupMaxRowVersion = db.getMaxRowVersion(DbSchema.CustomersGroupschema.TABLE_NAME);
            BankMaxRowVersion = db.getMaxRowVersion(DbSchema.BanksSchema.TABLE_NAME);
            VisitorMaxRowVersion = db.getMaxRowVersion(DbSchema.Visitorschema.TABLE_NAME);

            CheckListMaxRowVersion = db.getMaxRowVersion(DbSchema.CheckListschema.TABLE_NAME);
            TransactionslogMaxRowVersion = db.getMaxRowVersion(DbSchema.Transactionslogschema.TABLE_NAME);

            OrderRowMaxVersion = db.getMaxRowVersion(DbSchema.Orderschema.TABLE_NAME);
            OrderDetailMaxRowVersion = db.getMaxRowVersion(DbSchema.OrderDetailSchema.TABLE_NAME);

            ReasonMaxRowVersion = db.getMaxRowVersion(DbSchema.ReasonsSchema.TABLE_NAME);
            PromotionMaxRowVersion = db.getMaxRowVersion(DbSchema.PromotionSchema.TABLE_NAME);
            PromotionDetailMaxRowVersion = db.getMaxRowVersion(DbSchema.PromotionDetailSchema.TABLE_NAME);
            PromotionEntityMaxRowVersion = db.getMaxRowVersion(DbSchema.PromotionEntitySchema.TABLE_NAME);

            PicturesMaxRowVersion = db.getMaxRowVersion(DbSchema.PicturesProductSchema.TABLE_NAME);
            ExtraDataMaxRowVersion = db.getMaxRowVersion(DbSchema.ExtraDataSchema.TABLE_NAME);
            PropertyDescriptionMaxRowVersion = db.getMaxRowVersion(DbSchema.PropertyDescriptionSchema.TABLE_NAME);
            VisitorProductMaxRowVersion = db.getMaxRowVersion(DbSchema.VisitorProductSchema.TABLE_NAME);
            PriceLevelMaxRowVersion = db.getMaxRowVersion(DbSchema.PriceLevelNameSchema.TABLE_NAME);
            CategoryMaxRowVersion = db.getMaxRowVersion(DbSchema.ProductGroupSchema.TABLE_NAME);
            ProductMaxRowVersion = db.getMaxRowVersion(DbSchema.Productschema.TABLE_NAME);
            ProductDetailMaxRowVersion = db.getMaxRowVersion(DbSchema.ProductDetailSchema.TABLE_NAME);

            SettingMaxRowVersion = db.getMaxRowVersion(DbSchema.SettingSchema.TABLE_NAME);


            getAllDataBody.setFromPersonVersion(CustomerMaxRowVersion);
            getAllDataBody.setFromPersonGroupVersion(CustomersGroupMaxRowVersion);
            getAllDataBody.setFromBankVersion(BankMaxRowVersion);
            getAllDataBody.setFromVisitorVersion(VisitorMaxRowVersion);

            getAllDataBody.setFromChecklistVersion(CheckListMaxRowVersion);
            getAllDataBody.setFromTransactionVersion(TransactionslogMaxRowVersion);

            getAllDataBody.setFromOrderVersion(OrderRowMaxVersion);
            getAllDataBody.setFromOrderDetailVersion(OrderDetailMaxRowVersion);

            ArrayList<Integer> orderTypes = new ArrayList<>();
            orderTypes.add(ProjectInfo.TYPE_Delivery);
            getAllDataBody.setOrderTypes(orderTypes);

            getAllDataBody.setFromReturnReasonVersion(ReasonMaxRowVersion);

            getAllDataBody.setFromPromotionVersion(PromotionMaxRowVersion);
            getAllDataBody.setFromPromotionDetailVersion(PromotionDetailMaxRowVersion);
            getAllDataBody.setFromPromotionEntityVersion(PromotionEntityMaxRowVersion);

            getAllDataBody.setFromPictureVersion(PicturesMaxRowVersion);
            getAllDataBody.setFromExtraDataVersion(ExtraDataMaxRowVersion);
            getAllDataBody.setFromPropertyDescriptionVersion(PropertyDescriptionMaxRowVersion);
            getAllDataBody.setFromVisitorProductVersion(VisitorProductMaxRowVersion);
            getAllDataBody.setFromCostLevelNameVersion(PriceLevelMaxRowVersion);
            getAllDataBody.setFromProductCategoryVersion(CategoryMaxRowVersion);
            getAllDataBody.setFromProductVersion(ProductMaxRowVersion);
            getAllDataBody.setFromProductDetailVersion(ProductDetailMaxRowVersion);

            getAllDataBody.setFromSettingVersion(SettingMaxRowVersion);

            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {

            final String[] mMsg = {""};

            ApiInterface apiService = ApiClient.orderRetrofitClient().create(ApiInterface.class);
            Call<GetDataResult> getDataResultCall;
            getDataResultCall = apiService.GetAllData(getAllDataBody);
            getDataResultCall.enqueue(new Callback<GetDataResult>() {
                @Override
                public void onResponse(Call<GetDataResult> call, Response<GetDataResult> response) {
                    if (response.body() != null && response.body().isResult()) {
                        if (response.body().getData() != null) {

                            bankLists = response.body().getData().getObjects().getBanks();
                            personGroupLists = response.body().getData().getObjects().getPersonGroups();
                            customerLists = response.body().getData().getObjects().getPeople();

                            visitorLists = response.body().getData().getObjects().getVisitors();
                            checkLists = response.body().getData().getObjects().getChecklists();
                            transactionsLogs = response.body().getData().getObjects().getTransactions();
                            orders = response.body().getData().getObjects().getOrders();
                            orderDetails = response.body().getData().getObjects().getOrderDetails();
                            reasons = response.body().getData().getObjects().getReturnReasons();

                            promotions = response.body().getData().getObjects().getPromotions();
                            promotionDetails = response.body().getData().getObjects().getPromotionDetails();
                            promotionEntities = response.body().getData().getObjects().getPromotionEntities();

                            extraData = response.body().getData().getObjects().getExtraData();
                            picturesProducts = response.body().getData().getObjects().getPictures();
                            propertyDescriptions = response.body().getData().getObjects().getPropertyDescriptions();
                            visitorProducts = response.body().getData().getObjects().getVisitorProducts();
                            productPriceLevelNames = response.body().getData().getObjects().getCostLevelNames();
                            productGroupLists = response.body().getData().getObjects().getProductCategories();
                            productList = response.body().getData().getObjects().getProducts();
                            productDetails = response.body().getData().getObjects().getProductDetails();

                            settings = response.body().getData().getObjects().getSettings();
                            new SaveAsyncTask().execute();

                        }
                    } else if (response.body() != null) {
                        mMsg[0] = response.body().getMessage();
                    }
                }

                @Override
                public void onFailure(Call<GetDataResult> call, Throwable t) {
                    mMsg[0] = t.toString();
                }
            });

        }
    }

    class SaveAsyncTask extends AsyncTask<String, String, Integer> {
        SaveAsyncTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... arg0) {

            db.open();

            if (customerLists != null)
                if (customerLists.size() > 0)
                    if (DataService.InsertCustomer(db, customerLists, CustomerMaxRowVersion) == -1) {
                    }
            if (visitorLists != null)
                if (visitorLists.size() > 0)
                    if (DataService.InsertVisitor(db, visitorLists) == -1) {
                    }
            if (productList != null)
                if (productList.size() > 0)
                    if (DataService.InsertProduct(db, productList, ProductMaxRowVersion) == -1) {
                    }
            if (productDetails != null)
                if (productDetails.size() > 0) {
                    if (DataService.InsertProductDetail(db, productDetails, ProductDetailMaxRowVersion) == -1) {
                    }
                }
            if (productGroupLists != null)
                if (productGroupLists.size() > 0) {
                    if (DataService.InsertCategory(db, productGroupLists) == -1) {
                    }
                }
            if (picturesProducts != null)
                if (picturesProducts.size() > 0)
                    if (DataService.InsertPicturesProduct(db, picturesProducts) == -1) {
                    }
            if (propertyDescriptions != null)
                if (propertyDescriptions.size() > 0) {
                    if (DataService.InsertPropertyDescription(db, propertyDescriptions) == -1) {
                    }
                }
            if (visitorProducts != null)
                if (visitorProducts.size() > 0) {
                    if (DataService.InsertVisitorProducts(db, visitorProducts, VisitorProductMaxRowVersion) == -1) {
                    }
                }
            if (productPriceLevelNames != null)
                if (productPriceLevelNames.size() > 0) {
                    if (DataService.InsertCostLevelName(db, productPriceLevelNames) == -1) {
                    }
                }
            if (extraData != null)
                if (extraData.size() > 0)
                    if (DataService.InsertExtraInfo(db , extraData, ExtraDataMaxRowVersion) == -1) {
                    }
            if (checkLists != null)
                if (checkLists.size() > 0)
                    if (DataService.InsertCheckList(db, checkLists) == -1) {
                    }

            if (transactionsLogs != null)
                if (transactionsLogs.size() > 0)
                    if (DataService.InsertTransactionsLog(db, transactionsLogs) == -1) {
                    }
            if (promotions != null)
                if (promotions.size() > 0) {
                    if (DataService.InsertPromotion(db, promotions) != -1) {
                    }

                }
            if (promotionDetails != null)
                if (promotionDetails.size() > 0) {
                    if (DataService.InsertPromotionDetails(db, promotionDetails) == -1) {
                    }
                }
            if (promotionEntities != null)
                if (promotionEntities.size() > 0) {
                    if (DataService.InsertEntitiesOfPromotions(db, promotionEntities) == -1) {
                    }
                }
            if (orders != null)
                if (orders.size() > 0) {
                    if (DataService.InsertDeliveryOrder(db, orders) == -1) {
                    }
                }
            if (orderDetails != null)
                if (orderDetails.size() > 0) {
                    if (DataService.InsertDeliveryOrderDetail(db, orderDetails, mContext) == -1) {
                    }
                }
            if (bankLists != null)
                if (bankLists.size() > 0) {
                    if (DataService.InsertBank(db, bankLists) == -1) {
                    }
                }
            if (personGroupLists != null)
                if (personGroupLists.size() > 0) {
                    if (DataService.InsertCustomerGroup(db, personGroupLists) == -1) {
                    }
                }
            if (reasons != null)
                if (reasons.size() > 0) {
                    if (DataService.InsertReason(db, reasons) == -1) {
                    }
                }
            if (settings != null)
                if (settings.size() > 0) {
                    if (DataService.InsertSettings(db, settings, mContext) == -1) {
                    }
                }
            db.close();
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {

        }

    }
}
