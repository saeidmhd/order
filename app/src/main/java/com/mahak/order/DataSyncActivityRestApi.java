package com.mahak.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
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
import com.mahak.order.common.ServiceTools;
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
import com.mahak.order.common.request.SetAllDataResult.Banks.Banks;
import com.mahak.order.common.request.SetAllDataResult.Checklists.Checklists;
import com.mahak.order.common.request.SetAllDataResult.Cheques.Cheques;
import com.mahak.order.common.request.SetAllDataResult.ExtraDatas.ExtraDatas;
import com.mahak.order.common.request.SetAllDataResult.NonRegisters.NonRegisters;
import com.mahak.order.common.request.SetAllDataResult.Objects;
import com.mahak.order.common.request.SetAllDataResult.OrderDetails.OrderDetails;
import com.mahak.order.common.request.SetAllDataResult.Orders.Orders;
import com.mahak.order.common.request.SetAllDataResult.PayableTransfers.PayableTransfers;
import com.mahak.order.common.request.SetAllDataResult.PersonGroups.PersonGroups;
import com.mahak.order.common.request.SetAllDataResult.Persons.People;
import com.mahak.order.common.request.SetAllDataResult.Pictures.Pictures;
import com.mahak.order.common.request.SetAllDataResult.ProductCategories.ProductCategories;
import com.mahak.order.common.request.SetAllDataResult.ProductDetails.ProductDetails;
import com.mahak.order.common.request.SetAllDataResult.Products.Products;
import com.mahak.order.common.request.SetAllDataResult.Receipts.Receipts;
import com.mahak.order.common.request.SetAllDataResult.SaveAllDataResult;
import com.mahak.order.common.request.SetAllDataResult.Settings.Settings;
import com.mahak.order.common.request.SetAllDataResult.Transactions.Transactions;
import com.mahak.order.common.request.SetAllDataResult.TransferStoreDetails.TransferStoreDetails;
import com.mahak.order.common.request.SetAllDataResult.TransferStores.TransferStores;
import com.mahak.order.common.request.SetAllDataResult.Visitors.Visitors;
import com.mahak.order.common.request.SetSign.setSignImage;
import com.mahak.order.service.DataService;
import com.mahak.order.service.ReadOfflinePicturesProducts;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.storage.DbSchema;
import com.mahak.order.widget.FontAlertDialog;
import com.mahak.order.widget.FontProgressDialog;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mahak.order.common.ServiceTools.getDateAndTimeForLong;

//import com.mahak.order.common.DeliveryOrder;
//import com.mahak.order.common.ProductInOrder;

public class DataSyncActivityRestApi extends BaseActivity {

    private ProgressBar pbLoading;
    private FontProgressDialog pd;

    final boolean[] send_result = {false};

    private TextView
            tvActivityList,
            tvOrderList,
            tvReceiptList,
            tvPayableList,
            tvSendDoneCheckList,
            tvProductList,
            tvDeliveryOrderList,
            tvConfig,
            tvPicturesProduct;

    private TextView
            tvCustomerList,
            tvMoreInfo,
            tvPriceLevel,
            tvTransaction,
            tvInvoiceList,
            tvReturnOfSaleList,
            tvSendNonRegister,
            tvDate,
            tvDayOfDate,
            tvSendCustomerList,
            tvVisitorٰList;
    private ScrollView mScrollView;

    private Button btnSync, btnCancel;
    private DbAdapter db;
    private Context mContext;
    private Activity mActivity;

    private User user;
    private TextView tvPromotion;

    List<Bank> bankLists = new ArrayList<>();
    List<CustomerGroup> personGroupLists = new ArrayList<>();
    List<Customer> customerLists = new ArrayList<>();
    List<VisitorPeople> visitorPeople = new ArrayList<>();
    List<Visitor> visitorLists = new ArrayList<>();

    List<CheckList> checkLists = new ArrayList<>();
    List<TransactionsLog> transactionsLogs = new ArrayList<>();
    List<Order> orders = new ArrayList<>();
    List<OrderDetail> orderDetails = new ArrayList<>();
    List<Reasons> reasons = new ArrayList<>();

    List<Promotion> promotions = new ArrayList<>();
    List<PromotionDetail> promotionDetails = new ArrayList<>();
    List<PromotionEntity> promotionEntities = new ArrayList<>();

    List<PicturesProduct> picturesProducts = new ArrayList<>();
    static List<PicturesProduct> signForSend = new ArrayList<>();
    List<com.mahak.order.common.ExtraData> extraData = new ArrayList<>();
    List<PropertyDescription> propertyDescriptions = new ArrayList<>();
    List<VisitorProduct> visitorProducts = new ArrayList<>();
    List<Product> productList = new ArrayList<>();
    List<ProductDetail> productDetails = new ArrayList<>();
    List<ProductGroup> productGroupLists = new ArrayList<>();
    List<ProductPriceLevelName> productPriceLevelNames = new ArrayList<>();

    List<Setting> settings = new ArrayList<>();

    private final boolean[] arrayCheckUpdate = new boolean[24];
    private final double[] arrayTime = new double[24];

    private long CustomerMaxRowVersion;
    private long CustomersGroupMaxRowVersion;
    private long VisitorPersonMaxRowVersion;

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
    private File[] files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mActivity = this;

        setContentView(R.layout.activity_data_sync);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.item_actionbar_datasync);
            actionBar.getCustomView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    cleanDatabase();
                }
            });
        }

        init();

        //get User
        db.open();
        user = db.getUser();
        db.close();

        // DeviceID = ServiceTools.getDeviceID(mContext);
        sh = this.getSharedPreferences(SharedPreferencesMahak, MODE_PRIVATE);
        ShowDate();

        btnSync.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ServiceTools.isOnline(mContext)) {
                    SendReceive();

                }//End of if
                else
                    Toast.makeText(mContext, getString(R.string.str_message_dont_connect), Toast.LENGTH_LONG).show();
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Cancel();
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void dismissProgressDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    public void SendReceive() {

        db.open();
        final User user = db.getUser();
        int userDatabaseId = ServiceTools.toInt(user.getDatabaseId());
        int userId = ServiceTools.toInt(user.getServerUserID());
        LoginBody loginBody = new LoginBody();
        //String DeviceID = ServiceTools.getDeviceID(mContext);
        loginBody.setAppId("MahakOrder");
        loginBody.setDatabaseId(0);
        loginBody.setLanguage("en-US");
        loginBody.setDeviceId("");
        loginBody.setDescription("login");
        loginBody.setUserName(user.getUsername());
        loginBody.setPassword(user.getPassword());

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResult> call = apiService.Login(loginBody);
        pd = new FontProgressDialog(mContext);
        pd.setMessage(getString(R.string.reviewing_user_info));
        pd.setCancelable(false);
        pd.show();
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                dismissProgressDialog();
                if (response.body() != null) {
                    if (response.body().isResult()) {
                        if (userDatabaseId == response.body().getData().getDatabaseId() && userId == response.body().getData().getVisitorId()) {
                            BaseActivity.setPrefUserToken(response.body().getData().getUserToken());
                            setPrefSyncId(response.body().getData().getSyncId());
                            //Save db
                            db.open();
                            user.setSyncId(response.body().getData().getSyncId());
                            user.setUserToken(response.body().getData().getUserToken());
                            db.UpdateUser(user);
                            db.close();
                            new SendAsyncTask(response.body().getData().getUserToken()).execute();
                        } else {
                            ServiceTools.Backup(mContext);
                            db.open();
                            db.DeleteAllData();
                            db.DeleteUser(userId);
                            db.close();
                            Toast.makeText(DataSyncActivityRestApi.this, R.string.visitor_changed_login_again, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DataSyncActivityRestApi.this, LoginActivityRestApi.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        //setAndGetRequest(response.body().getData().getUserToken());
                    } else {
                        ServiceTools.Backup(mContext);
                        db.open();
                        db.DeleteUser(userId);
                        db.close();
                        Toast.makeText(DataSyncActivityRestApi.this, R.string.visitor_changed_login_again, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DataSyncActivityRestApi.this, LoginActivityRestApi.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        dismissProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().log(t.getMessage());
                dismissProgressDialog();
            }
        });
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

    private void setTextSendErrorResult() {
        tvInvoiceList.setText(getString(R.string.str_message_error));
        tvOrderList.setText(getString(R.string.str_message_error));
        tvReceiptList.setText(getString(R.string.str_message_error));
        tvReturnOfSaleList.setText(getString(R.string.str_message_error));
        tvPayableList.setText(getString(R.string.str_message_error));
        tvSendDoneCheckList.setText(getString(R.string.str_message_error));
        tvSendNonRegister.setText(getString(R.string.str_message_error));
        tvSendCustomerList.setText(getString(R.string.str_message_error));
    }

    private void setTextGetErrorResult() {
        tvDeliveryOrderList.setText(getString(R.string.str_message_error));
        tvTransaction.setText(getString(R.string.str_message_error));
        tvActivityList.setText(getString(R.string.str_message_error));
        tvProductList.setText(getString(R.string.str_message_error));
        tvVisitorٰList.setText(getString(R.string.str_message_error));
        tvCustomerList.setText(getString(R.string.str_message_error));
        tvPromotion.setText(getString(R.string.str_message_error));
        tvMoreInfo.setText(getString(R.string.str_message_error));
        tvPriceLevel.setText(getString(R.string.str_message_error));
        tvConfig.setText(getString(R.string.str_message_error));
        tvPicturesProduct.setText(getString(R.string.str_message_error));
    }

    /**
     * Initialize Variable
     */

    private void init() {

        tvActivityList = (TextView) findViewById(R.id.tvActivityList);
        tvOrderList = (TextView) findViewById(R.id.tvOrderList);
        tvReceiptList = (TextView) findViewById(R.id.tvReceiptList);
        tvPayableList = (TextView) findViewById(R.id.tvPayableList);
        tvProductList = (TextView) findViewById(R.id.tvProductList);
        tvSendDoneCheckList = (TextView) findViewById(R.id.tvSendDoneCheckList);
        tvConfig = (TextView) findViewById(R.id.tvConfig);
        tvPicturesProduct = (TextView) findViewById(R.id.tvPicturesProduct);
        tvCustomerList = (TextView) findViewById(R.id.tvCustomerList);
        tvMoreInfo = (TextView) findViewById(R.id.tvMoreInfo);
        tvPromotion = (TextView) findViewById(R.id.tvPromotion);
        tvPriceLevel = (TextView) findViewById(R.id.tvPriceLevel);
        tvTransaction = (TextView) findViewById(R.id.tvTransaction);
        tvDeliveryOrderList = (TextView) findViewById(R.id.tvDeliveryOrder);
        tvInvoiceList = (TextView) findViewById(R.id.tvInvoiceList);
        tvReturnOfSaleList = (TextView) findViewById(R.id.tvReturnOfSaleList);
        tvSendNonRegister = (TextView) findViewById(R.id.tvSendNonRegister);
        tvSendCustomerList = (TextView) findViewById(R.id.tvSendCustomerList);
        tvVisitorٰList = (TextView) findViewById(R.id.tvVisitorٰList);
        mScrollView = (ScrollView) findViewById(R.id.mScrollView);

        tvDate = (TextView) findViewById(R.id.tvDate);
        tvDayOfDate = (TextView) findViewById(R.id.tvDay);

        btnSync = (Button) findViewById(R.id.btnSync);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);

        db = new DbAdapter(mContext);

        for (int i = 0; i < arrayCheckUpdate.length; i++) {
            arrayCheckUpdate[i] = true;
        }
    }

    private void resetTextViews() {

        tvActivityList.setText("");
        tvOrderList.setText("");
        tvReceiptList.setText("");
        tvPayableList.setText("");
        tvSendDoneCheckList.setText("");
        tvProductList.setText("");
        tvDeliveryOrderList.setText("");
        tvConfig.setText("");
        tvPicturesProduct.setText("");
        tvCustomerList.setText("");
        tvMoreInfo.setText("");
        tvPriceLevel.setText("");
        tvTransaction.setText("");
        tvInvoiceList.setText("");
        tvReturnOfSaleList.setText("");
        tvSendNonRegister.setText("");
        tvSendCustomerList.setText("");
        tvVisitorٰList.setText("");
        tvPromotion.setText("");

    }

    private Dialog Dialog(String msg) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(FontAlertDialog.getFontTitle(getString(R.string.str_title_message)))
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.str_ok), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        Dialog dialog = builder.create();
        return dialog;
    }

    private void Cancel() {
        // syncAsyn.cancel(true);
        btnSync.setEnabled(true);
        pbLoading.setVisibility(View.GONE);
    }

    /**
     * show last date update
     */
    private void ShowDate() {
        tvDayOfDate.setText(getString(R.string.str_update));
        long SyncDate = user.getDateSync();
        if (SyncDate == 0)
            tvDate.setText("0");
        else {
            tvDate.setText(getDateAndTimeForLong(SyncDate));
        }
    }

    //Action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == android.R.id.home) {
            // syncAsyn.cancel(true);
            pbLoading.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void cleanDatabase() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(FontAlertDialog.getFontTitle(getString(R.string.str_title_delete)));
        builder.setMessage(getString(R.string.str_message_delete_all_of_data));
        builder.setPositiveButton(getString(R.string.str_ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(ServiceTools.Backup(mContext)){
                    db.open();
                    db.DeleteAllData();
                    db.close();
                    Toast toast = Toast.makeText(mContext, R.string.clean_database_alarm, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 16);
                    toast.show();
                    resetTextViews();
                    mScrollView.fullScroll(View.FOCUS_UP);
                    Calendar cal = Calendar.getInstance();
                    setPrefSyncId(String.valueOf(cal.getTimeInMillis() / 1000));
                    //Save db
                    db.open();
                    user.setSyncId(getPrefSyncId());
                    db.UpdateUser(user);
                    db.close();
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.show();
        FontAlertDialog.FontDialog(dialog);
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
            pbLoading.setVisibility(View.VISIBLE);
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
                    if ((orderDetail.getGiftCount1() > 0 || orderDetail.getGiftCount2() > 0)) {
                        orderDetail.setCount1(orderDetail.getGiftCount1());
                        orderDetail.setCount2(orderDetail.getGiftCount2());
                    }
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
            Customers.addAll(newCustomers);
            payableTransfers = db.getAllPayableNotPublish(BaseActivity.getPrefUserId());
            checkLists = db.getAllDoneChecklistNotPublish();
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {

            pbLoading.setVisibility(View.GONE);
            pbLoading.setVisibility(View.VISIBLE);


            final String[] mMsg = {""};

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

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

            pd.setMessage(getString(R.string.sending_info));
            pd.setCancelable(false);
            pd.show();
            saveAllDataResultCall.enqueue(new Callback<SaveAllDataResult>() {
                @Override
                public void onResponse(@NonNull Call<SaveAllDataResult> call, @NonNull Response<SaveAllDataResult> response) {
                    dismissProgressDialog();
                    if (response.body() != null && response.body().isResult()) {
                        db.open();
                        if (arrayInvoice.size() > 0) {
                            int invoiceCount = 0;
                            int orderCount = 0;
                            int returnCount = 0;
                            for (int i = 0; i < arrayInvoice.size(); i++) {
                                arrayInvoice.get(i).setOrderId(response.body().getData().getObjects().getOrders().getResults().get(i).getEntityID());
                                arrayInvoice.get(i).setPublish(ProjectInfo.PUBLISH);
                                db.UpdateOrder(arrayInvoice.get(i));
                                if (arrayInvoice.get(i).getOrderType() == ProjectInfo.TYPE_INVOCIE)
                                    invoiceCount++;
                                else if (arrayInvoice.get(i).getOrderType() == ProjectInfo.TYPE_ORDER)
                                    orderCount++;
                                else if (arrayInvoice.get(i).getOrderType() == ProjectInfo.TYPE_RETURN_OF_SALE)
                                    returnCount++;

                                for (int j = 0; j < arrayInvoiceDetail.size(); j++) {
                                    arrayInvoiceDetail.get(j).setOrderDetailId(response.body().getData().getObjects().getOrderDetails().getResults().get(j).getEntityID());
                                    db.UpdateOrderDetailSync(arrayInvoiceDetail.get(j));
                                }
                            }
                            if (invoiceCount > 0)
                                tvInvoiceList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + invoiceCount);
                            else
                                tvInvoiceList.setText(getString(R.string.str_message_no_need));

                            if (orderCount > 0)
                                tvOrderList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + orderCount);
                            else
                                tvOrderList.setText(getString(R.string.str_message_no_need));

                            if (returnCount > 0)
                                tvReturnOfSaleList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + returnCount);
                            else
                                tvReturnOfSaleList.setText(getString(R.string.str_message_no_need));

                        } else {
                            tvOrderList.setText(getString(R.string.str_message_no_need));
                            tvInvoiceList.setText(getString(R.string.str_message_no_need));
                            tvReturnOfSaleList.setText(getString(R.string.str_message_no_need));
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
                            tvReceiptList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + arrayReceipt.size());
                        } else
                            tvReceiptList.setText(getString(R.string.str_message_no_need));

                        if (arrayNonRegister.size() > 0) {
                            for (int i = 0; i < arrayNonRegister.size(); i++) {
                                arrayNonRegister.get(i).setNotRegisterId(response.body().getData().getObjects().getNotRegisters().getResults().get(i).getEntityID());
                                arrayNonRegister.get(i).setPublish(ProjectInfo.PUBLISH);
                                db.UpdateNonRegister(arrayNonRegister.get(i));
                            }
                            tvSendNonRegister.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + arrayNonRegister.size());
                        } else
                            tvSendNonRegister.setText(getString(R.string.str_message_no_need));

                        if (payableTransfers.size() > 0) {
                            for (int i = 0; i < payableTransfers.size(); i++) {
                                payableTransfers.get(i).setTransferAccountId(response.body().getData().getObjects().getPayableTransfers().getResults().get(i).getEntityID());
                                payableTransfers.get(i).setPublish(ProjectInfo.PUBLISH);
                                db.UpdatePayable(payableTransfers.get(i));
                            }

                            tvPayableList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + payableTransfers.size());
                        } else
                            tvPayableList.setText(getString(R.string.str_message_no_need));

                        if (checkLists.size() > 0) {
                            for (int i = 0; i < checkLists.size(); i++) {
                                checkLists.get(i).setChecklistId(response.body().getData().getObjects().getChecklists().getResults().get(i).getEntityID());
                                checkLists.get(i).setPublish(ProjectInfo.PUBLISH);
                                db.UpdateCheckList(checkLists.get(i));
                            }
                            tvSendDoneCheckList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + checkLists.size());
                        } else
                            tvSendDoneCheckList.setText(getString(R.string.str_message_no_need));

                        if (newCustomers.size() > 0) {
                            for (int i = 0; i < newCustomers.size(); i++) {
                                newCustomers.get(i).setPersonId(response.body().getData().getObjects().getPeople().getResults().get(i).getEntityID());
                                db.UpdateCustomerWithClientId(newCustomers.get(i));
                            }
                            tvSendCustomerList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + newCustomers.size());
                        } else
                            tvSendCustomerList.setText(getString(R.string.str_message_no_need));

                        picturesProducts = db.getAllSignWithoutUrl();

                        for (PicturesProduct picturesProduct : picturesProducts) {
                            Order order = db.GetOrderWithOrderClientId(picturesProduct.getPictureClientId());
                            picturesProduct.setItemId(order.getOrderId());
                            db.UpdatePicturesProductWithClientId(picturesProduct);
                        }

                        pbLoading.setVisibility(View.GONE);
                        db.close();

                        new ReceiveAsyncTask(mUserToken).execute();


                    } else if (response.body() != null) {
                        // mMsg[0] = response.body().getData().getObjects().getOrders().getResults().get(0).getErrors().get(0).getError();
                        dismissProgressDialog();
                        mMsg[0] = getString(R.string.send_error);
                        showDialog(getResponseError(response.body().getData().getObjects()));
                        setTextSendErrorResult();
                        pbLoading.setVisibility(View.GONE);

                        FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                        FirebaseCrashlytics.getInstance().log(response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<SaveAllDataResult> call, Throwable t) {
                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                    FirebaseCrashlytics.getInstance().log(t.getMessage());
                    dismissProgressDialog();
                    mMsg[0] = t.toString();
                    showDialog(mMsg[0]);
                    pbLoading.setVisibility(View.GONE);
                    setTextSendErrorResult();
                }
            });


        }
    }

    private String getResponseError(Objects objects) {
        if(objects.getPeople().getResults().size()>0)
            return "خطا در ارسال اشخاص" + "\n" +objects.getPeople().getResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getPeople().getResults().get(0).getErrors().get(0).getProperty();
        if(objects.getBanks().getResults().size()>0)
            return "خطا در ارسال بانک" + "\n" +objects.getBanks().getResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getBanks().getResults().get(0).getErrors().get(0).getProperty();
        if(objects.getChecklists().getResults().size()>0)
            return "خطا در ارسال چک لیست ها" + "\n" +objects.getChecklists().getResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getChecklists().getResults().get(0).getErrors().get(0).getProperty();
        if(objects.getCheques().getResults().size()>0)
            return "خطا در ارسال چک ها" + "\n" +objects.getCheques().getResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getCheques().getResults().get(0).getErrors().get(0).getProperty();
        if(objects.getPayableTransfers().getResults().size()>0)
            return "خطا در ارسال پرداختی ها" + "\n" +objects.getPayableTransfers().getResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getPayableTransfers().getResults().get(0).getErrors().get(0).getProperty();
        if(objects.getSettings().getSettingsResults().size()>0)
            return "خطا در ارسال تنظیمات" + "\n" +objects.getSettings().getSettingsResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getSettings().getSettingsResults().get(0).getErrors().get(0).getProperty();
        if(objects.getExtraDatas().getResults().size()>0)
            return "خطا در ارسال اطلاعات بیشتر" + "\n" +objects.getExtraDatas().getResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getExtraDatas().getResults().get(0).getErrors().get(0).getProperty();
        if(objects.getOrderDetails().getResults().size()>0)
            return "خطا در ارسال جزییات فاکتور" + "\n" +objects.getOrderDetails().getResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getOrderDetails().getResults().get(0).getErrors().get(0).getProperty();
        if(objects.getOrders().getResults().size()>0)
            return "خطا در ارسال فاکتور" + "\n" +objects.getOrders().getResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getOrders().getResults().get(0).getErrors().get(0).getProperty();
        if(objects.getPersonGroups().getResults().size()>0)
            return "خطا در ارسال گروه اشخاص" + "\n" +objects.getPersonGroups().getResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getPersonGroups().getResults().get(0).getErrors().get(0).getProperty();
        if(objects.getPictures().getResults().size()>0)
            return "خطا در ارسال تصاویر" + "\n" +objects.getPictures().getResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getPictures().getResults().get(0).getErrors().get(0).getProperty();
        if(objects.getProductCategories().getResults().size()>0)
            return "خطا در ارسال گروه کالاها" + "\n" +objects.getProductCategories().getResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getProductCategories().getResults().get(0).getErrors().get(0).getProperty();
        if(objects.getProductDetails().getResults().size()>0)
            return "خطا در ارسال جزییات کالاها" + "\n" +objects.getProductDetails().getResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getProductDetails().getResults().get(0).getErrors().get(0).getProperty();
        if(objects.getProducts().getResults().size()>0)
            return "خطا در ارسال کالاها" + "\n" +objects.getProducts().getResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getProducts().getResults().get(0).getErrors().get(0).getProperty();
        if(objects.getReceipts().getResults().size()>0)
            return "خطا در ارسال دریافتی ها" + "\n" +objects.getReceipts().getResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getReceipts().getResults().get(0).getErrors().get(0).getProperty();
        if(objects.getTransactions().getResults().size()>0)
            return "خطا در ارسال گردش حساب ها" + "\n" +objects.getTransactions().getResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getTransactions().getResults().get(0).getErrors().get(0).getProperty();
        if(objects.getVisitors().getResults().size()>0)
            return "خطا در ارسال ویزیتور ها" + "\n" +objects.getVisitors().getResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getVisitors().getResults().get(0).getErrors().get(0).getProperty();
        if(objects.getNotRegisters().getResults().size()>0)
            return "خطا در ارسال عدم ثبت سفارش" + "\n" +objects.getNotRegisters().getResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getNotRegisters().getResults().get(0).getErrors().get(0).getProperty();
        if(objects.getTransferStores().getResults().size()>0)
            return "خطا در ارسال حواله کالا" + "\n" +objects.getTransferStores().getResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getTransferStores().getResults().get(0).getErrors().get(0).getProperty();
        if(objects.getTransferStoreDetails().getResults().size()>0)
            return "خطا در ارسال جزییات حواله کالاها" + "\n" +objects.getTransferStoreDetails().getResults().get(0).getErrors().get(0).getError()+ "\n" + objects.getTransferStoreDetails().getResults().get(0).getErrors().get(0).getProperty();
        return "";
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
            pbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... arg0) {

            db.open();
            clearArraysForRecieve();

            getAllDataBody = new GetAllDataBody();
            getAllDataBody.setUserToken(mUserToken);

            //CustomerMaxRowVersion = db.getMaxRowVersion(DbSchema.Customerschema.TABLE_NAME);
            CustomerMaxRowVersion = db.getMaxRowVersion(DbSchema.Customerschema.TABLE_NAME);
            CustomersGroupMaxRowVersion = db.getMaxRowVersion(DbSchema.CustomersGroupschema.TABLE_NAME);
            VisitorPersonMaxRowVersion = db.getMaxRowVersion(DbSchema.VisitorPeopleSchema.TABLE_NAME);

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
            getAllDataBody.setFromVisitorPersonVersion(VisitorPersonMaxRowVersion);


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

            pbLoading.setVisibility(View.GONE);
            pbLoading.setVisibility(View.VISIBLE);
            pd = new FontProgressDialog(mContext);

            final String[] mMsg = {""};

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<GetDataResult> getDataResultCall;
            getDataResultCall = apiService.GetAllData(getAllDataBody);
            pbLoading.setVisibility(View.VISIBLE);
            pd.setMessage(getString(R.string.recieiving_info));
            pd.setCancelable(false);
            pd.show();
            getDataResultCall.enqueue(new Callback<GetDataResult>() {
                @Override
                public void onResponse(Call<GetDataResult> call, Response<GetDataResult> response) {
                    dismissProgressDialog();
                    if (response.body() != null && response.body().isResult()) {
                        if (response.body().getData() != null) {

                            long startTime = System.nanoTime();

                            bankLists = response.body().getData().getObjects().getBanks();
                            personGroupLists = response.body().getData().getObjects().getPersonGroups();
                            customerLists = response.body().getData().getObjects().getPeople();
                            visitorPeople = response.body().getData().getObjects().getVisitorPeople();

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

                            long endTime = System.nanoTime();

                            double a = (double) (TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) / 1000;

                            new SaveAsyncTask(mUserToken).execute();
                        }
                        pbLoading.setVisibility(View.GONE);
                    } else if (response.body() != null) {
                        dismissProgressDialog();
                        mMsg[0] = response.body().getMessage();
                        showDialog(mMsg[0]);
                        setTextGetErrorResult();
                        pbLoading.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<GetDataResult> call, Throwable t) {
                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                    FirebaseCrashlytics.getInstance().log(t.getMessage());
                    dismissProgressDialog();
                    mMsg[0] = t.toString();
                    showDialog(mMsg[0]);
                    setTextGetErrorResult();
                    pbLoading.setVisibility(View.GONE);
                }
            });
        }
    }

    class SaveAsyncTask extends AsyncTask<String, String, Integer> {
        String mUserToken;

        SaveAsyncTask(String userToken) {
            mUserToken = userToken;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbLoading.setVisibility(View.VISIBLE);
            pd = new FontProgressDialog(mContext);
            pd.setMessage(getString(R.string.storing_info));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Integer doInBackground(String... arg0) {

            db.open();

            if (customerLists != null)
                if (customerLists.size() > 0)
                    arrayTime[0] = DataService.InsertCustomer(db, customerLists, CustomerMaxRowVersion);

            if (personGroupLists != null)
                if (personGroupLists.size() > 0)
                    arrayTime[0] += DataService.InsertCustomerGroup(db, personGroupLists);

            if (visitorPeople != null)
                if (visitorPeople.size() > 0)
                    arrayTime[0] += DataService.InsertVisitorPeople(db, visitorPeople, VisitorPersonMaxRowVersion);

            if (visitorLists != null)
                if (visitorLists.size() > 0)
                    arrayTime[2] = DataService.InsertVisitor(db, visitorLists);

            if (productList != null)
                if (productList.size() > 0)
                    arrayTime[3] = DataService.InsertProduct(db, productList, ProductMaxRowVersion);

            if (productDetails != null)
                if (productDetails.size() > 0) {
                    arrayTime[4] = DataService.InsertProductDetail(db, productDetails, ProductDetailMaxRowVersion);
                }
            if (picturesProducts != null)
                if (picturesProducts.size() > 0)
                    arrayTime[5] = DataService.InsertPicturesProduct(db, picturesProducts);

            if (productGroupLists != null)
                if (productGroupLists.size() > 0) {
                    arrayTime[6] = DataService.InsertCategory(db, productGroupLists);
                }
            if (propertyDescriptions != null)
                if (propertyDescriptions.size() > 0) {
                    arrayTime[7] = DataService.InsertPropertyDescription(db, propertyDescriptions);
                }
            if (visitorProducts != null)
                if (visitorProducts.size() > 0) {
                    arrayTime[8] = DataService.InsertVisitorProducts(db, visitorProducts, VisitorProductMaxRowVersion);
                }
            if (productPriceLevelNames != null)
                if (productPriceLevelNames.size() > 0) {
                    arrayTime[9] = DataService.InsertCostLevelName(db, productPriceLevelNames);
                }
            if (checkLists != null)
                if (checkLists.size() > 0)
                    arrayTime[10] = DataService.InsertCheckList(db, checkLists);

            if (transactionsLogs != null)
                if (transactionsLogs.size() > 0)
                    arrayTime[11] = DataService.InsertTransactionsLog(db, transactionsLogs);

            if (promotions != null)
                if (promotions.size() > 0) {
                    arrayTime[12] = DataService.InsertPromotion(db, promotions);
                }
            if (promotionDetails != null)
                if (promotionDetails.size() > 0) {
                    arrayTime[13] = DataService.InsertPromotionDetails(db, promotionDetails);
                }
            if (promotionEntities != null)
                if (promotionEntities.size() > 0) {
                    arrayTime[14] = DataService.InsertEntitiesOfPromotions(db, promotionEntities);
                }
            if (extraData != null)
                if (extraData.size() > 0)
                    arrayTime[15] = DataService.InsertExtraInfo(db, extraData, ExtraDataMaxRowVersion);

            if (orders != null)
                if (orders.size() > 0) {
                    arrayTime[16] = DataService.InsertDeliveryOrder(db, orders);
                }
            if (orderDetails != null)
                if (orderDetails.size() > 0) {
                    arrayTime[17] = DataService.InsertDeliveryOrderDetail(db, orderDetails, mContext);
                }
            if (settings != null)
                if (settings.size() > 0) {
                    arrayTime[18] = DataService.InsertSettings(db, settings, mContext);
                }
            if (reasons != null)
                if (reasons.size() > 0) {
                    arrayTime[19] = DataService.InsertReason(db, reasons);
                }
            if (bankLists != null)
                if (bankLists.size() > 0) {
                    arrayTime[20] = DataService.InsertBank(db, bankLists);
                }
            db.close();
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {

            for (int i = 0; i < arrayCheckUpdate.length; i++) {
                switch (i) {
                    case 0:
                        if (arrayCheckUpdate[0])
                            if (customerLists.size() > 0) {
                                tvCustomerList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + customerLists.size() + " (" + String.format("%.2f", arrayTime[0]) + getString(R.string.secound) + ") ");
                            } else
                                tvCustomerList.setText(R.string.no_new_item_for_receive);
                        else
                            tvCustomerList.setText(getString(R.string.str_message_failed_save));
                        break;
                    case 1:
                        if (arrayCheckUpdate[1])
                            if (customerLists.size() > 0) {
                                tvCustomerList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + customerLists.size() + " (" + String.format("%.2f", arrayTime[0]) + getString(R.string.secound) + ") ");
                            } else
                                tvCustomerList.setText(R.string.no_new_item_for_receive);
                        else
                            tvCustomerList.setText(getString(R.string.str_message_failed_save) + getString(R.string.customer_group));
                        break;
                    case 2:
                        if (arrayCheckUpdate[2])
                            if (visitorLists.size() > 0) {
                                tvVisitorٰList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + visitorLists.size() + " (" + String.format("%.2f", arrayTime[2]) + getString(R.string.secound) + ") ");
                            } else
                                tvVisitorٰList.setText(R.string.no_new_item_for_receive);
                        else
                            tvVisitorٰList.setText(getString(R.string.str_message_failed_save));
                        break;
                    case 3:
                        if (arrayCheckUpdate[3])
                            if (productList.size() > 0) {
                                tvProductList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + productList.size() + " (" + String.format("%.2f", arrayTime[3]) + getString(R.string.secound) + ") ");
                            } else
                                tvProductList.setText(R.string.no_new_item_for_receive);
                        else
                            tvProductList.setText(getString(R.string.str_message_failed_save));
                        break;
                    case 4:
                        if (arrayCheckUpdate[4])
                            if (productDetails.size() > 0) {
                                tvPriceLevel.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + productDetails.size() + " (" + String.format("%.2f", arrayTime[4]) + getString(R.string.secound) + ") ");
                            } else
                                tvPriceLevel.setText(R.string.no_new_item_for_receive);
                        else
                            tvPriceLevel.setText(getString(R.string.str_message_failed_save) + getString(R.string.product_properties));
                        break;
                    case 5:
                        if (arrayCheckUpdate[5])
                            if (picturesProducts.size() > 0) {
                                tvPicturesProduct.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + picturesProducts.size() + " (" + String.format("%.2f", arrayTime[5]) + getString(R.string.secound) + ") ");
                            } else
                                tvPicturesProduct.setText(R.string.no_new_item_for_receive);
                        else
                            tvPicturesProduct.setText(getString(R.string.str_message_failed_save) + getString(R.string.product_images));
                        break;
                    case 6:
                        if (arrayCheckUpdate[6])
                            if (productList.size() > 0) {
                                tvProductList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + productDetails.size() + " (" + String.format("%.2f", arrayTime[6]) + getString(R.string.secound) + ") ");
                            } else
                                tvProductList.setText(R.string.no_new_item_for_receive);
                        else
                            tvProductList.setText(getString(R.string.str_message_failed_save) + getString(R.string.product_category));
                        break;
                    case 7:
                        if (arrayCheckUpdate[7])
                            if (productList.size() > 0) {
                                tvProductList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + productDetails.size() + " (" + String.format("%.2f", arrayTime[7]) + getString(R.string.secound) + ") ");
                            } else
                                tvProductList.setText(R.string.no_new_item_for_receive);
                        else
                            tvProductList.setText(getString(R.string.str_message_failed_save) + getString(R.string.product_details_desc));
                        break;
                    case 8:
                        if (arrayCheckUpdate[8])
                            if (productList.size() > 0) {
                                tvProductList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + productDetails.size() + " (" + String.format("%.2f", arrayTime[8]) + getString(R.string.secound) + ") ");
                            } else
                                tvProductList.setText(R.string.no_new_item_for_receive);
                        else
                            tvProductList.setText(getString(R.string.str_message_failed_save) + getString(R.string.visitor_product));
                        break;
                    case 9:
                        if (arrayCheckUpdate[9])
                            if (productList.size() > 0) {
                                double sumTime = arrayTime[3] + arrayTime[6] + arrayTime[7] + arrayTime[8] + arrayTime[9];
                                tvProductList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + productDetails.size() + " (" + String.format("%.2f", sumTime) + getString(R.string.secound) + ") ");
                            } else
                                tvProductList.setText(R.string.no_new_item_for_receive);
                        else
                            tvProductList.setText(getString(R.string.str_message_failed_save) + getString(R.string.price_level_names));
                        break;
                    case 10:
                        if (arrayCheckUpdate[10])
                            if (checkLists.size() > 0) {
                                tvActivityList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + checkLists.size() + " (" + String.format("%.2f", arrayTime[10]) + getString(R.string.secound) + ") ");
                            } else
                                tvActivityList.setText(R.string.no_new_item_for_receive);
                        else
                            tvActivityList.setText(getString(R.string.str_message_failed_save));
                        break;
                    case 11:
                        if (arrayCheckUpdate[11])
                            if (transactionsLogs.size() > 0) {
                                tvTransaction.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + transactionsLogs.size() + " (" + String.format("%.2f", arrayTime[11]) + getString(R.string.secound) + ") ");
                            } else
                                tvTransaction.setText(R.string.no_new_item_for_receive);
                        else
                            tvTransaction.setText(getString(R.string.str_message_failed_save));
                        break;
                    case 12:
                        if (arrayCheckUpdate[12])
                            if (promotions.size() > 0) {
                                tvPromotion.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + promotions.size() + " (" + String.format("%.2f", arrayTime[12]) + getString(R.string.secound) + ") ");
                            } else
                                tvPromotion.setText(R.string.no_new_item_for_receive);
                        else
                            tvPromotion.setText(getString(R.string.str_message_failed_save) + getString(R.string.promotion));
                        break;
                    case 13:
                        if (arrayCheckUpdate[13])
                            if (promotions.size() > 0) {
                                tvPromotion.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + promotions.size() + " (" + String.format("%.2f", arrayTime[13]) + getString(R.string.secound) + ") ");
                            } else
                                tvPromotion.setText(R.string.no_new_item_for_receive);
                        else
                            tvPromotion.setText(getString(R.string.str_message_failed_save) + getString(R.string.promotion_details));
                        break;
                    case 14:
                        if (arrayCheckUpdate[14])
                            if (promotions.size() > 0) {
                                double sumTime = arrayTime[12] + arrayTime[13] + arrayTime[14];
                                tvPromotion.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + promotions.size() + " (" + String.format("%.2f", sumTime) + getString(R.string.secound) + ") ");
                            } else
                                tvPromotion.setText(R.string.no_new_item_for_receive);
                        else
                            tvPromotion.setText(getString(R.string.str_message_failed_save) + getString(R.string.promotion_entity_details));
                        break;
                    case 15:
                        if (arrayCheckUpdate[15])
                            if (extraData.size() > 0) {
                                tvMoreInfo.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + extraData.size() + " (" + String.format("%.2f", arrayTime[15]) + getString(R.string.secound) + ") ");
                            } else
                                tvMoreInfo.setText(R.string.no_new_item_for_receive);
                        else
                            tvMoreInfo.setText(getString(R.string.str_message_failed_save));
                        break;
                    case 16:
                        if (arrayCheckUpdate[16])
                            if (orders.size() > 0) {
                                tvDeliveryOrderList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + orders.size() + " (" + String.format("%.2f", arrayTime[16]) + getString(R.string.secound) + ") ");
                            } else
                                tvDeliveryOrderList.setText(R.string.no_new_item_for_receive);
                        else
                            tvDeliveryOrderList.setText(getString(R.string.str_message_failed_save) + getString(R.string.delivery));
                        break;
                    case 17:
                        if (arrayCheckUpdate[17])
                            if (orders.size() > 0) {
                                tvDeliveryOrderList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + orders.size() + " (" + String.format("%.2f", arrayTime[17]) + getString(R.string.secound) + ") ");
                            } else
                                tvDeliveryOrderList.setText(R.string.no_new_item_for_receive);
                        else
                            tvDeliveryOrderList.setText(getString(R.string.str_message_failed_save) + getString(R.string.delivery_details));
                        break;
                    case 18:
                        if (arrayCheckUpdate[18])
                            if (settings.size() > 0) {
                                tvConfig.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + settings.size() + " (" + String.format("%.2f", arrayTime[18]) + getString(R.string.secound) + ") ");
                            } else
                                tvConfig.setText(R.string.no_new_item_for_receive);
                        else
                            tvConfig.setText(getString(R.string.str_message_failed_save));
                        break;
                }
            }

            new SendSignInfoAsyncTask(mUserToken).execute();

            pbLoading.setVisibility(View.GONE);
            dismissProgressDialog();
        }

    }

    class SendSignInfoAsyncTask extends AsyncTask<String, String, Integer> {

        List<PicturesProduct> picturesProducts = new ArrayList<>();
        String mUserToken;

        SendSignInfoAsyncTask(String UserToken) {
            mUserToken = UserToken;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            db.open();
            picturesProducts = db.getAllSignWithoutUrl();
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {

            pbLoading.setVisibility(View.GONE);
            pbLoading.setVisibility(View.VISIBLE);

            final String[] mMsg = {""};

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            SetAllDataBody setAllDataBody = new SetAllDataBody();
            setAllDataBody.setUserToken(mUserToken);
            setAllDataBody.setPictures(picturesProducts);
            Call<SaveAllDataResult> saveAllDataResultCall = apiService.SaveAllData(setAllDataBody);

            pd.setMessage(getString(R.string.sending_image));
            pd.setCancelable(false);
            pd.show();
            saveAllDataResultCall.enqueue(new Callback<SaveAllDataResult>() {
                @Override
                public void onResponse(@NonNull Call<SaveAllDataResult> call, @NonNull Response<SaveAllDataResult> response) {
                    dismissProgressDialog();
                    if (response.body() != null && response.body().isResult()) {
                        db.open();
                        if (picturesProducts.size() > 0) {
                            for (int i = 0; i < picturesProducts.size(); i++) {
                                picturesProducts.get(i).setPictureId(response.body().getData().getObjects().getPictures().getResults().get(i).getEntityID());
                                db.UpdatePicturesProductWithClientId(picturesProducts.get(i));
                            }
                        }
                        pbLoading.setVisibility(View.GONE);
                        db.close();

                        new SendSignImageAsyncTask(mUserToken).execute();

                    } else if (response.body() != null) {
                        mMsg[0] = getString(R.string.send_error);
                        //showDialog(response.body().getMessage());
                        pbLoading.setVisibility(View.GONE);

                        FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                        FirebaseCrashlytics.getInstance().log(response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<SaveAllDataResult> call, Throwable t) {
                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                    FirebaseCrashlytics.getInstance().log(t.getMessage());
                    dismissProgressDialog();
                    mMsg[0] = t.toString();
                    //showDialog(mMsg[0]);
                    pbLoading.setVisibility(View.GONE);
                }
            });
        }
    }

    class SendSignImageAsyncTask extends AsyncTask<String, String, Integer> {
        String mUserToken;

        SendSignImageAsyncTask(String UserToken) {
            mUserToken = UserToken;
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            getFileList();
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (files != null) {
                if (files.length > 0) {
                    pbLoading.setVisibility(View.VISIBLE);
                    for (File mFile : files) {
                        final String[] mMsg = {""};
                        long pictureId = db.getPictureIdWithFileName(mFile.getName());
                        RequestBody filePart = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                        MultipartBody.Part mpfile = MultipartBody.Part.createFormData("file", mFile.getName(), filePart);
                        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

                        Call<setSignImage> mSetSignImageResult = apiService.uploadSignImage(mpfile, pictureId, mFile.getName(), mUserToken);
                        pd.setMessage(getString(R.string.send_image) + mFile.getName());
                        pd.setCancelable(false);
                        pd.show();
                        mSetSignImageResult.enqueue(new Callback<setSignImage>() {
                            @Override
                            public void onResponse(@NonNull Call<setSignImage> call, @NonNull Response<setSignImage> response) {
                                dismissProgressDialog();
                                if (response.body() != null && response.body().getResult()) {
                                    db.open();
                                    PicturesProduct picturesProduct = db.getPictureWithPictureId(response.body().getData().getEntityId());
                                    picturesProduct.setUrl(response.body().getData().getAdditionalData().getFileUrl());
                                    picturesProduct.setPictureHash(response.body().getData().getAdditionalData().getFileHash());
                                    db.UpdatePicturesProductWithClientId(picturesProduct);
                                    pbLoading.setVisibility(View.GONE);

                                } else if (response.body() != null) {
                                    dismissProgressDialog();
                                    mMsg[0] = getString(R.string.send_error);
                                    pbLoading.setVisibility(View.GONE);

                                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                                    FirebaseCrashlytics.getInstance().log(response.body().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<setSignImage> call, Throwable t) {
                                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                                FirebaseCrashlytics.getInstance().log(t.getMessage());
                                dismissProgressDialog();
                                mMsg[0] = t.toString();
                                //showDialog(mMsg[0]);
                                pbLoading.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }

            SetDate();
            ShowDate();
            new ReadOfflinePicturesProducts(mContext).readAllImages();

        }
    }

    private void SetDate() {
        sh.edit().putLong(_Key_DateSyncInformation, new Date().getTime()).commit();
        db.open();
        user.setDateSync(getPrefDateSyncInformation());
        db.UpdateUser(user);
        db.close();
    }

    private void getFileList() {
        signForSend.clear();
        db.open();
        signForSend = db.getAllSignWithoutUrl();
        String state = Environment.getExternalStorageState();
        final File SIGN_DIRECTORY = new File(Environment.getExternalStorageDirectory(), ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_SIGNS + "/" + ProjectInfo.DIRECTORY_ORDER_SIGNS);
        if (!SIGN_DIRECTORY.exists()) {
            SIGN_DIRECTORY.mkdirs();
        }
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                for (PicturesProduct picturesProduct : signForSend) {
                    if (filename.contains(picturesProduct.getFileName()))
                        return true;
                }
                return false;
            }
        };
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            files = SIGN_DIRECTORY.listFiles(filenameFilter);
        }
    }

    private void clearArraysForRecieve() {
        bankLists.clear();
        personGroupLists.clear();
        customerLists.clear();
        visitorLists.clear();
        checkLists.clear();
        transactionsLogs.clear();
        orders.clear();
        orderDetails.clear();
        reasons.clear();
        promotions.clear();
        promotionDetails.clear();
        promotionEntities.clear();
        picturesProducts.clear();
        extraData.clear();
        propertyDescriptions.clear();
        visitorProducts.clear();
        productList.clear();
        productDetails.clear();
        productGroupLists.clear();
        productPriceLevelNames.clear();
        settings.clear();
    }

    private void showDialog(String msg) {
        if (!isFinishing()) {
            Dialog(msg).show();
        }
    }


}
