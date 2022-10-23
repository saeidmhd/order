package com.mahak.order;

import static com.mahak.order.common.ServiceTools.getDateAndTimeForLong;

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

import androidx.appcompat.app.ActionBar;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
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
import com.mahak.order.common.PhotoGallery;
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
import com.mahak.order.common.Region;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.Setting;
import com.mahak.order.common.TransactionsLog;
import com.mahak.order.common.User;
import com.mahak.order.common.Visitor;
import com.mahak.order.common.VisitorLocation;
import com.mahak.order.common.VisitorPeople;
import com.mahak.order.common.VisitorProduct;
import com.mahak.order.common.login.LoginBody;
import com.mahak.order.common.login.LoginResult;
import com.mahak.order.common.request.GetAllDataBody;
import com.mahak.order.common.request.GetAllDataResult.GetDataResult;
import com.mahak.order.common.request.SetAllDataBody;
import com.mahak.order.common.request.SetAllDataResult.Objects;
import com.mahak.order.common.request.SetAllDataResult.SaveAllDataResult;
import com.mahak.order.common.request.SetSign.setSignImage;
import com.mahak.order.mission.Mission;
import com.mahak.order.mission.MissionDetail;
import com.mahak.order.service.DataService;
import com.mahak.order.service.ReadOfflinePicturesProducts;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.storage.DbSchema;
import com.mahak.order.storage.RadaraDb;
import com.mahak.order.tracking.TrackingConfig;
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

import io.reactivex.annotations.NonNull;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataSyncActivityRestApi extends BaseActivity {

    private FontProgressDialog pd;

    final boolean[] send_result = {false};

    private TextView
            tvOrderList,
            tvReceiptList,
            tvPayableList,
            tvSendDoneCheckList, tvDate,tvDayOfDate,tvInvoiceList,tvReturnOfSaleList,tvSendNonRegister,tvSendCustomerList;

    private TextView
            tvExtraData
            ,tvProductDetailList
            ,tvVisitorProduct
            ,tvProductList
            ,tvPerson
            ,personGroup
            ,tvVisitorPerson
            ,tvBanks
            ,tvVisitorٰList
            ,tvCheckList
            ,tvTransaction
            ,tvDelivery
            ,tvDeliveryDetail
            ,returnReason
            ,tvPromotionList
            ,tvPromotionDetailList
            ,tvPromotionDeliveryEntity
            ,tvProductPicture
            ,tvPictureDetails
            ,tvRegion
            ,tvPropertyDesc
            ,tvPriceLevelNames
            ,tvProductGroup
            ,tvSetting;
    private ScrollView mScrollView;

    private Button btnSync, btnCancel;
    private DbAdapter db;
    private RadaraDb radaraDb;
    private Context mContext;
    private Activity mActivity;

    private User user;

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
    List<Mission> missions = new ArrayList<>();
    List<MissionDetail> missionDetails = new ArrayList<>();
    List<PhotoGallery> photoGalleries = new ArrayList<>();
    List<Region> regions = new ArrayList<>();

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
    private long PhotoGalleryMaxRowVersion;
    private long RegionMaxRowVersion;
    private long MissionMaxRowVersion;
    private long MissionDetailMaxRowVersion;
    private File[] files;


    SendAsyncTask sendAsyncTask;
    ReceiveAsyncTask receiveAsyncTask;
    SaveAsyncTask saveAsyncTask;
    SendSignInfoAsyncTask sendSignInfoAsyncTask;
    SendSignImageAsyncTask sendSignImageAsyncTask;

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
        radaraDb.open();
        user = db.getUser();
        // DeviceID = ServiceTools.getDeviceID(mContext);
        sh = this.getSharedPreferences(SharedPreferencesMahak, MODE_PRIVATE);
        ShowDate();

        btnSync.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ServiceTools.isOnline(mContext)) {
                    checkUserAvailable();
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

    private void checkUserAvailable() {

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

        ApiInterface apiService = ApiClient.orderRetrofitClient().create(ApiInterface.class);
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

                            user.setSyncId(response.body().getData().getSyncId());
                            user.setUserToken(response.body().getData().getUserToken());
                            db.UpdateUser(user);

                            SendReceive();
                        } else {
                            ServiceTools.Backup(mContext);

                            db.DeleteAllData();
                            radaraDb.DeleteAllData();
                            db.DeleteUser(userId);

                            Toast.makeText(DataSyncActivityRestApi.this, R.string.visitor_changed_login_again, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DataSyncActivityRestApi.this, LoginActivityRestApi.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        ServiceTools.Backup(mContext);
                        db.DeleteUser(userId);
                        Toast.makeText(DataSyncActivityRestApi.this, R.string.visitor_changed_login_again, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DataSyncActivityRestApi.this, LoginActivityRestApi.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                FirebaseCrashlytics.getInstance().log(t.getMessage());
                dismissProgressDialog();
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
        pd = new FontProgressDialog(mContext);
        pd.setMessage(getString(R.string.sending_info));
        pd.setCancelable(false);
        pd.show();
        sendAsyncTask = new SendAsyncTask();
        sendAsyncTask.execute();
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
        tvTransaction.setText(getString(R.string.str_message_error));
        tvProductList.setText(getString(R.string.str_message_error));
        tvVisitorٰList.setText(getString(R.string.str_message_error));
    }

    /**
     * Initialize Variable
     */

    private void init() {

        tvOrderList = (TextView) findViewById(R.id.tvOrderList);
        tvInvoiceList = (TextView) findViewById(R.id.tvInvoiceList);
        tvReturnOfSaleList = (TextView) findViewById(R.id.tvReturnOfSaleList);
        tvSendNonRegister = (TextView) findViewById(R.id.tvSendNonRegister);
        tvSendCustomerList = (TextView) findViewById(R.id.tvSendCustomerList);
        tvReceiptList = (TextView) findViewById(R.id.tvReceiptList);
        tvPayableList = (TextView) findViewById(R.id.tvPayableList);
        tvProductList = (TextView) findViewById(R.id.tvProductList);
        tvSendDoneCheckList = (TextView) findViewById(R.id.tvSendDoneCheckList);
        tvTransaction = (TextView) findViewById(R.id.tvTransaction);
        tvVisitorٰList = (TextView) findViewById(R.id.tvVisitorٰList);
        mScrollView = (ScrollView) findViewById(R.id.mScrollView);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvProductDetailList = (TextView) findViewById(R.id.tvProductDetailList);
        tvVisitorProduct = (TextView) findViewById(R.id.tvVisitorProduct);
        tvExtraData = (TextView) findViewById(R.id.tvExtraData);
        tvPerson = (TextView) findViewById(R.id.tvPerson);
        personGroup = (TextView) findViewById(R.id.personGroup);
        tvVisitorPerson = (TextView) findViewById(R.id.tvVisitorPerson);
        tvBanks = (TextView) findViewById(R.id.tvBanks);
        tvVisitorٰList = (TextView) findViewById(R.id.tvVisitorٰList);
        tvCheckList = (TextView) findViewById(R.id.tvCheckList);
        tvDelivery = (TextView) findViewById(R.id.tvDelivery);
        tvDeliveryDetail = (TextView) findViewById(R.id.tvDeliveryDetail);
        returnReason = (TextView) findViewById(R.id.returnReason);
        tvPromotionList = (TextView) findViewById(R.id.tvPromotionList);
        tvPromotionDetailList = (TextView) findViewById(R.id.tvPromotionDetailList);
        tvPromotionDeliveryEntity = (TextView) findViewById(R.id.tvPromotionDeliveryEntity);
        tvProductPicture = (TextView) findViewById(R.id.tvProductPicture);
        tvPictureDetails = (TextView) findViewById(R.id.tvPictureDetails);
        tvRegion = (TextView) findViewById(R.id.tvRegion);
        tvPropertyDesc = (TextView) findViewById(R.id.tvPropertyDesc);
        tvPriceLevelNames = (TextView) findViewById(R.id.tvPriceLevelNames);
        tvProductGroup = (TextView) findViewById(R.id.tvProductGroup);
        tvSetting = (TextView) findViewById(R.id.tvSetting);


        tvDayOfDate = (TextView) findViewById(R.id.tvDay);

        btnSync = (Button) findViewById(R.id.btnSync);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        db = new DbAdapter(mContext);
        radaraDb = new RadaraDb(mContext);

        for (int i = 0; i < arrayCheckUpdate.length; i++) {
            arrayCheckUpdate[i] = true;
        }
    }

    private void resetTextViews() {

        tvOrderList.setText("");
        tvReceiptList.setText("");
        tvPayableList.setText("");
        tvSendDoneCheckList.setText("");
        tvInvoiceList.setText("");
        tvReturnOfSaleList.setText("");
        tvSendNonRegister.setText("");
        tvSendCustomerList.setText("");
        tvExtraData.setText("");
        tvProductDetailList.setText("");
        tvVisitorProduct.setText("");
        tvProductList.setText("");
        tvPerson.setText("");
        personGroup.setText("");
        tvVisitorPerson.setText("");
        tvBanks.setText("");
        tvVisitorٰList.setText("");
        tvCheckList.setText("");
        tvTransaction.setText("");
        tvDelivery.setText("");
        tvDeliveryDetail.setText("");
        returnReason.setText("");
        tvPromotionList.setText("");
        tvPromotionDetailList.setText("");
        tvPromotionDeliveryEntity.setText("");
        tvProductPicture.setText("");
        tvPictureDetails.setText("");
        tvRegion.setText("");
        tvPropertyDesc.setText("");
        tvPriceLevelNames.setText("");
        tvProductGroup.setText("");
        tvSetting.setText("");
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

        if(sendAsyncTask != null)
            sendAsyncTask.cancel(true);
        if(receiveAsyncTask != null)
            receiveAsyncTask.cancel(true);
        if(saveAsyncTask != null)
            saveAsyncTask.cancel(true);
        if(sendSignInfoAsyncTask != null)
            sendSignInfoAsyncTask.cancel(true);
        if(sendSignImageAsyncTask != null)
            sendSignImageAsyncTask.cancel(true);

        db.close();
        radaraDb.close();

        btnSync.setEnabled(true);
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
            Cancel();
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Cancel();
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
                if (ServiceTools.Backup(mContext)) {

                    db.DeleteAllData();
                    radaraDb.DeleteAllData();

                    Toast toast = Toast.makeText(mContext, R.string.clean_database_alarm, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 16);
                    toast.show();
                    resetTextViews();
                    //mScrollView.fullScroll(View.FOCUS_UP);
                    Calendar cal = Calendar.getInstance();
                    setPrefSyncId(String.valueOf(cal.getTimeInMillis() / 1000));
                    //Save db

                    user.setSyncId(getPrefSyncId());
                    db.UpdateUser(user);

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

    private String getResponseError(Objects objects) {
        try {
            if (objects.getPeople().getResults().size() > 0)
                return "خطا در ارسال اشخاص" + "\n" + objects.getPeople().getResults().get(0).getErrors().get(0).getError() + "\n" + objects.getPeople().getResults().get(0).getErrors().get(0).getProperty();
            if (objects.getBanks().getResults().size() > 0)
                return "خطا در ارسال بانک" + "\n" + objects.getBanks().getResults().get(0).getErrors().get(0).getError() + "\n" + objects.getBanks().getResults().get(0).getErrors().get(0).getProperty();
            if (objects.getChecklists().getResults().size() > 0)
                return "خطا در ارسال چک لیست ها" + "\n" + objects.getChecklists().getResults().get(0).getErrors().get(0).getError() + "\n" + objects.getChecklists().getResults().get(0).getErrors().get(0).getProperty();
            if (objects.getCheques().getResults().size() > 0)
                return "خطا در ارسال چک ها" + "\n" + objects.getCheques().getResults().get(0).getErrors().get(0).getError() + "\n" + objects.getCheques().getResults().get(0).getErrors().get(0).getProperty();
            if (objects.getPayableTransfers().getResults().size() > 0)
                return "خطا در ارسال پرداختی ها" + "\n" + objects.getPayableTransfers().getResults().get(0).getErrors().get(0).getError() + "\n" + objects.getPayableTransfers().getResults().get(0).getErrors().get(0).getProperty();
            if (objects.getSettings().getSettingsResults().size() > 0)
                return "خطا در ارسال تنظیمات" + "\n" + objects.getSettings().getSettingsResults().get(0).getErrors().get(0).getError() + "\n" + objects.getSettings().getSettingsResults().get(0).getErrors().get(0).getProperty();
            if (objects.getExtraDatas().getResults().size() > 0)
                return "خطا در ارسال اطلاعات بیشتر" + "\n" + objects.getExtraDatas().getResults().get(0).getErrors().get(0).getError() + "\n" + objects.getExtraDatas().getResults().get(0).getErrors().get(0).getProperty();
            if (objects.getOrders().getResults().size() > 0)
                return "خطا در ارسال فاکتور" + "\n" + objects.getOrders().getResults().get(0).getErrors().get(0).getError() + "\n" + objects.getOrders().getResults().get(0).getErrors().get(0).getProperty();
            if (objects.getOrderDetails().getResults().size() > 0)
                return "خطا در ارسال جزییات فاکتور" + "\n" + objects.getOrderDetails().getResults().get(0).getErrors().get(0).getError() + "\n" + objects.getOrderDetails().getResults().get(0).getErrors().get(0).getProperty();
            if (objects.getPersonGroups().getResults().size() > 0)
                return "خطا در ارسال گروه اشخاص" + "\n" + objects.getPersonGroups().getResults().get(0).getErrors().get(0).getError() + "\n" + objects.getPersonGroups().getResults().get(0).getErrors().get(0).getProperty();
            if (objects.getPictures().getResults().size() > 0)
                return "خطا در ارسال تصاویر" + "\n" + objects.getPictures().getResults().get(0).getErrors().get(0).getError() + "\n" + objects.getPictures().getResults().get(0).getErrors().get(0).getProperty();
            if (objects.getProductCategories().getResults().size() > 0)
                return "خطا در ارسال گروه کالاها" + "\n" + objects.getProductCategories().getResults().get(0).getErrors().get(0).getError() + "\n" + objects.getProductCategories().getResults().get(0).getErrors().get(0).getProperty();
            if (objects.getProductDetails().getResults().size() > 0)
                return "خطا در ارسال جزییات کالاها" + "\n" + objects.getProductDetails().getResults().get(0).getErrors().get(0).getError() + "\n" + objects.getProductDetails().getResults().get(0).getErrors().get(0).getProperty();
            if (objects.getProducts().getResults().size() > 0)
                return "خطا در ارسال کالاها" + "\n" + objects.getProducts().getResults().get(0).getErrors().get(0).getError() + "\n" + objects.getProducts().getResults().get(0).getErrors().get(0).getProperty();
            if (objects.getReceipts().getResults().size() > 0)
                return "خطا در ارسال دریافتی ها" + "\n" + objects.getReceipts().getResults().get(0).getErrors().get(0).getError() + "\n" + objects.getReceipts().getResults().get(0).getErrors().get(0).getProperty();
            if (objects.getTransactions().getResults().size() > 0)
                return "خطا در ارسال گردش حساب ها" + "\n" + objects.getTransactions().getResults().get(0).getErrors().get(0).getError() + "\n" + objects.getTransactions().getResults().get(0).getErrors().get(0).getProperty();
            if (objects.getVisitors().getResults().size() > 0)
                return "خطا در ارسال ویزیتور ها" + "\n" + objects.getVisitors().getResults().get(0).getErrors().get(0).getError() + "\n" + objects.getVisitors().getResults().get(0).getErrors().get(0).getProperty();
            if (objects.getNotRegisters().getResults().size() > 0)
                return "خطا در ارسال عدم ثبت سفارش" + "\n" + objects.getNotRegisters().getResults().get(0).getErrors().get(0).getError() + "\n" + objects.getNotRegisters().getResults().get(0).getErrors().get(0).getProperty();
            if (objects.getTransferStores().getResults().size() > 0)
                return "خطا در ارسال حواله کالا" + "\n" + objects.getTransferStores().getResults().get(0).getErrors().get(0).getError() + "\n" + objects.getTransferStores().getResults().get(0).getErrors().get(0).getProperty();
            if (objects.getTransferStoreDetails().getResults().size() > 0)
                return "خطا در ارسال جزییات حواله کالاها" + "\n" + objects.getTransferStoreDetails().getResults().get(0).getErrors().get(0).getError() + "\n" + objects.getTransferStoreDetails().getResults().get(0).getErrors().get(0).getProperty();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "خطای ناشناخته";
        }
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
        List<VisitorLocation> visitorLocation = new ArrayList<>();
        List<MissionDetail> missionDetails = new ArrayList<>();
        List<Mission> missions = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... arg0) {

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
                arrayCheque.addAll(db.getAllCheque(arrayReceipt.get(i).getReceiptClientId()));
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

            missions = db.getAllMission();

            for(Mission mission : missions)
                missionDetails.addAll(db.getAllMissionDetail(mission.getMissionId()));

            visitorLocation = radaraDb.getAllGpsPointsForSending();

            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {


            final String[] mMsg = {""};

            ApiInterface apiService = ApiClient.orderRetrofitClient().create(ApiInterface.class);

            SetAllDataBody setAllDataBody = new SetAllDataBody();
            setAllDataBody.setOrderDetails(arrayInvoiceDetail);
            setAllDataBody.setOrders(arrayInvoice);
            setAllDataBody.setReceipts(arrayReceipt);
            setAllDataBody.setCheques(arrayCheque);
            setAllDataBody.setNonRegisters(arrayNonRegister);
            setAllDataBody.setVisitorPeople(visitorPeopleArrayList);
            setAllDataBody.setPeople(Customers);
            setAllDataBody.setPayableTransfers(payableTransfers);
            setAllDataBody.setChecklists(checkLists);
            setAllDataBody.setVisitorLocations(visitorLocation);
            setAllDataBody.setMissionDetails(missionDetails);
            setAllDataBody.setMissions(missions);
            Call<SaveAllDataResult> saveAllDataResultCall = apiService.SaveAllData(setAllDataBody);
            saveAllDataResultCall.enqueue(new Callback<SaveAllDataResult>() {
                @Override
                public void onResponse(@NonNull Call<SaveAllDataResult> call, @NonNull Response<SaveAllDataResult> response) {
                    dismissProgressDialog();
                    if (response.body() != null && response.body().isResult()) {

                        if (arrayInvoice.size() > 0) {
                            int invoiceCount = 0;
                            int orderCount = 0;
                            int returnCount = 0;
                            for (int i = 0; i < arrayInvoice.size(); i++) {
                                arrayInvoice.get(i).setOrderId(response.body().getData().getObjects().getOrders().getResults().get(i).getEntityID());
                                arrayInvoice.get(i).setRowVersion(response.body().getData().getObjects().getOrders().getResults().get(i).getRowVersion());
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
                                    arrayInvoiceDetail.get(j).setRowVersion(response.body().getData().getObjects().getOrderDetails().getResults().get(j).getRowVersion());
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


                        if (visitorLocation.size() > 0) {
                            for (int i = 0; i < visitorLocation.size(); i++) {
                                visitorLocation.get(i).setVisitorLocationId(response.body().getData().getObjects().getVisitorLocations().getResults().get(i).getEntityID());
                                visitorLocation.get(i).setRowVersion(response.body().getData().getObjects().getVisitorLocations().getResults().get(i).getRowVersion());
                                radaraDb.updateGpsTrackingForSending(visitorLocation.get(i));
                            }
                        }

                        picturesProducts = db.getAllSignWithoutUrl();

                        for (PicturesProduct picturesProduct : picturesProducts) {
                            Order order = db.GetOrderWithOrderClientId(picturesProduct.getPictureClientId());
                            picturesProduct.setItemId(order.getOrderId());
                            db.UpdatePicturesProductWithClientId(picturesProduct);
                        }


                        new ReceiveAsyncTask(1).execute();

                    } else if (response.body() != null) {
                        // mMsg[0] = response.body().getData().getObjects().getOrders().getResults().get(0).getErrors().get(0).getError();
                        mMsg[0] = getString(R.string.send_error);

                        if (response.body().getData().getObjects() != null)
                            showDialog(getResponseError(response.body().getData().getObjects()));
                        else
                            showDialog(response.body().getMessage());

                        setTextSendErrorResult();
                    }
                }

                @Override
                public void onFailure(Call<SaveAllDataResult> call, Throwable t) {
                    dismissProgressDialog();
                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                    FirebaseCrashlytics.getInstance().log(t.getMessage());
                    mMsg[0] = t.toString();
                    showDialog(mMsg[0]);
                    setTextSendErrorResult();
                }
            });
        }
    }

    class ReceiveAsyncTask extends AsyncTask<String, String, Integer> {
        GetAllDataBody getAllDataBody;
        int whichUpdate = 0;

        public ReceiveAsyncTask(int i) {
            whichUpdate = i;
        }

        @Override
        protected void onPreExecute() {
            pd.setMessage("در حال دریافت " + getPdMessage(whichUpdate));
            pd.setCancelable(false);
            pd.show();
            super.onPreExecute();

        }

        @Override
        protected Integer doInBackground(String... arg0) {
            getAllDataBody = new GetAllDataBody();
            switch (whichUpdate){
                case 1:
                    ProductMaxRowVersion = db.getMaxRowVersion(DbSchema.ProductSchema.TABLE_NAME);
                    getAllDataBody.setFromProductVersion(ProductMaxRowVersion);
                    VisitorProductMaxRowVersion = db.getMaxRowVersion(DbSchema.VisitorProductSchema.TABLE_NAME);
                    getAllDataBody.setFromVisitorProductVersion(VisitorProductMaxRowVersion);
                    break;
                case 2:
                    ProductDetailMaxRowVersion = db.getMaxRowVersion(DbSchema.ProductDetailSchema.TABLE_NAME);
                    getAllDataBody.setFromProductDetailVersion(ProductDetailMaxRowVersion);
                    break;
                case 3:
                    ExtraDataMaxRowVersion = db.getMaxRowVersion(DbSchema.ExtraDataSchema.TABLE_NAME);
                    getAllDataBody.setFromExtraDataVersion(ExtraDataMaxRowVersion);
                    break;
                case 4:
                    CustomerMaxRowVersion = db.getMaxRowVersion(DbSchema.CustomerSchema.TABLE_NAME);
                    getAllDataBody.setFromPersonVersion(CustomerMaxRowVersion);
                    VisitorPersonMaxRowVersion = db.getMaxRowVersion(DbSchema.VisitorPeopleSchema.TABLE_NAME);
                    getAllDataBody.setFromVisitorPersonVersion(VisitorPersonMaxRowVersion);
                    break;
                case 5:
                  //  mScrollView.fullScroll(View.FOCUS_DOWN);
                    CustomersGroupMaxRowVersion = db.getMaxRowVersion(DbSchema.CustomersGroupSchema.TABLE_NAME);
                    getAllDataBody.setFromPersonGroupVersion(CustomersGroupMaxRowVersion);
                    break;
                case 6:
                    BankMaxRowVersion = db.getMaxRowVersion(DbSchema.BanksSchema.TABLE_NAME);
                    getAllDataBody.setFromBankVersion(BankMaxRowVersion);
                    break;
                case 7:
                    VisitorMaxRowVersion = db.getMaxRowVersion(DbSchema.VisitorSchema.TABLE_NAME);
                    getAllDataBody.setFromVisitorVersion(VisitorMaxRowVersion);
                    break;
                case 8:
                    CheckListMaxRowVersion = db.getMaxRowVersion(DbSchema.CheckListSchema.TABLE_NAME);
                    getAllDataBody.setFromChecklistVersion(CheckListMaxRowVersion);
                    break;
                case 9:
                    TransactionslogMaxRowVersion = db.getMaxRowVersion(DbSchema.TransactionsLogSchema.TABLE_NAME);
                    getAllDataBody.setFromTransactionVersion(TransactionslogMaxRowVersion);
                    break;
                case 10:
                    ArrayList<Integer> orderTypes = new ArrayList<>();
                    orderTypes.add(ProjectInfo.TYPE_Delivery);
                    getAllDataBody.setOrderTypes(orderTypes);
                    OrderRowMaxVersion = db.getMaxRowVersion(DbSchema.OrderSchema.TABLE_NAME);
                    getAllDataBody.setFromOrderVersion(OrderRowMaxVersion);
                    OrderDetailMaxRowVersion = db.getMaxRowVersion(DbSchema.OrderDetailSchema.TABLE_NAME);
                    getAllDataBody.setFromOrderDetailVersion(OrderDetailMaxRowVersion);
                    break;
                case 11:
                    getAllDataBody.setFromPromotionVersion(0L);
                    break;
                case 12:
                    getAllDataBody.setFromPromotionDetailVersion(0L);
                    break;
                case 13:
                    getAllDataBody.setFromPromotionEntityVersion(0L);
                    break;
                case 14:
                    ReasonMaxRowVersion = db.getMaxRowVersion(DbSchema.ReasonsSchema.TABLE_NAME);
                    getAllDataBody.setFromReturnReasonVersion(ReasonMaxRowVersion);
                    break;
                case 15:
                    PicturesMaxRowVersion = db.getMaxRowVersion(DbSchema.PicturesProductSchema.TABLE_NAME);
                    getAllDataBody.setFromPictureVersion(PicturesMaxRowVersion);
                    break;
                case 16:
                    PhotoGalleryMaxRowVersion = db.getMaxRowVersion(DbSchema.PhotoGallerySchema.TABLE_NAME);
                    getAllDataBody.setFromPhotoGalleryVersion(PhotoGalleryMaxRowVersion);
                    break;
                case 17:
                    RegionMaxRowVersion = db.getMaxRowVersion(DbSchema.RegionSchema.TABLE_NAME);
                    getAllDataBody.setFromRegionVersion(RegionMaxRowVersion);
                    break;
                case 18:
                    PropertyDescriptionMaxRowVersion = db.getMaxRowVersion(DbSchema.PropertyDescriptionSchema.TABLE_NAME);
                    getAllDataBody.setFromPropertyDescriptionVersion(PropertyDescriptionMaxRowVersion);
                    break;
                case 19:
                    PriceLevelMaxRowVersion = db.getMaxRowVersion(DbSchema.PriceLevelNameSchema.TABLE_NAME);
                    getAllDataBody.setFromCostLevelNameVersion(PriceLevelMaxRowVersion);
                    break;
                case 20:
                    CategoryMaxRowVersion = db.getMaxRowVersion(DbSchema.ProductGroupSchema.TABLE_NAME);
                    getAllDataBody.setFromProductCategoryVersion(CategoryMaxRowVersion);
                    break;
                case 21:
                    SettingMaxRowVersion = db.getMaxRowVersion(DbSchema.SettingSchema.TABLE_NAME);
                    getAllDataBody.setFromSettingVersion(SettingMaxRowVersion);
                    break;
                case 22:
                    MissionMaxRowVersion = db.getMaxRowVersion(DbSchema.MissionSchema.TABLE_NAME);
                    getAllDataBody.setFromMissionVersion(MissionMaxRowVersion);
                    break;
                case 23:
                    MissionDetailMaxRowVersion = db.getMaxRowVersion(DbSchema.MissionDetailSchema.TABLE_NAME);
                    getAllDataBody.setFromMissionDetailVersion(MissionDetailMaxRowVersion);
                    break;
            }

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
                            switch (whichUpdate){
                                case 1:
                                    productList = response.body().getData().getObjects().getProducts();
                                    visitorProducts = response.body().getData().getObjects().getVisitorProducts();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 2:
                                    productDetails = response.body().getData().getObjects().getProductDetails();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 3:
                                    extraData = response.body().getData().getObjects().getExtraData();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 4:
                                    customerLists = response.body().getData().getObjects().getPeople();
                                    visitorPeople = response.body().getData().getObjects().getVisitorPeople();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 5:
                                    personGroupLists = response.body().getData().getObjects().getPersonGroups();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 6:
                                    bankLists = response.body().getData().getObjects().getBanks();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 7:
                                    visitorLists = response.body().getData().getObjects().getVisitors();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 8:
                                    checkLists = response.body().getData().getObjects().getChecklists();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 9:
                                    transactionsLogs = response.body().getData().getObjects().getTransactions();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 10:
                                    orders = response.body().getData().getObjects().getOrders();
                                    orderDetails = response.body().getData().getObjects().getOrderDetails();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 11:
                                    promotions = response.body().getData().getObjects().getPromotions();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 12:
                                    promotionDetails = response.body().getData().getObjects().getPromotionDetails();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 13:
                                    promotionEntities = response.body().getData().getObjects().getPromotionEntities();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 14:
                                    reasons = response.body().getData().getObjects().getReturnReasons();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 15:
                                    picturesProducts = response.body().getData().getObjects().getPictures();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 16:
                                    photoGalleries = response.body().getData().getObjects().getPhotoGalleries();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 17:
                                    regions = response.body().getData().getObjects().getRegions();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 18:
                                    propertyDescriptions = response.body().getData().getObjects().getPropertyDescriptions();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 19:
                                    productPriceLevelNames = response.body().getData().getObjects().getCostLevelNames();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 20:
                                    productGroupLists = response.body().getData().getObjects().getProductCategories();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 21:
                                    settings = response.body().getData().getObjects().getSettings();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 22:
                                    missions= response.body().getData().getObjects().getMissions();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;
                                case 23:
                                    missionDetails= response.body().getData().getObjects().getMissionDetails();
                                    saveAsyncTask =  new SaveAsyncTask(whichUpdate);
                                    saveAsyncTask.execute();
                                    break;

                            }
                        }
                    } else if (response.body() != null) {
                        mMsg[0] = response.body().getMessage();
                        dismissProgressDialog();
                        showDialog(mMsg[0]);
                    }
                }

                @Override
                public void onFailure(Call<GetDataResult> call, Throwable t) {
                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                    FirebaseCrashlytics.getInstance().log(t.getMessage());
                    dismissProgressDialog();
                    mMsg[0] = t.toString();
                    showDialog(mMsg[0]);
                }
            });


        }
    }

    class SaveAsyncTask extends AsyncTask<String, String, Integer> {
        int whichUpdate = 0;


        public SaveAsyncTask(int which) {
            whichUpdate = which;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("در حال ذخیره " + getPdMessage(whichUpdate));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Integer doInBackground(String... arg0) {

            switch (whichUpdate){
                case 1:
                    if (productList != null)
                        if (productList.size() > 0)
                            arrayTime[3] = DataService.InsertProduct(db, productList, ProductMaxRowVersion);
                    if (visitorProducts != null)
                        if (visitorProducts.size() > 0) {
                            arrayTime[8] = DataService.InsertVisitorProducts(db, visitorProducts, VisitorProductMaxRowVersion);
                        }
                    break;
                case 2:
                    if (productDetails != null)
                        if (productDetails.size() > 0) {
                            arrayTime[4] = DataService.InsertProductDetail(db, productDetails, ProductDetailMaxRowVersion);
                        }
                    break;
                case 3:
                    if (extraData != null)
                        if (extraData.size() > 0)
                            arrayTime[15] = DataService.InsertExtraInfo(db, extraData, ExtraDataMaxRowVersion);
                    break;
                case 4:
                    if (customerLists != null)
                        if (customerLists.size() > 0)
                            arrayTime[0] = DataService.InsertCustomer(db, customerLists, CustomerMaxRowVersion);
                    if (visitorPeople != null)
                        if (visitorPeople.size() > 0)
                            arrayTime[0] += DataService.InsertVisitorPeople(db, visitorPeople, VisitorPersonMaxRowVersion);
                    break;
                case 5:
                    if (personGroupLists != null)
                        if (personGroupLists.size() > 0)
                            arrayTime[0] += DataService.InsertCustomerGroup(db, personGroupLists);
                    break;
                case 6:
                    if (bankLists != null)
                        if (bankLists.size() > 0) {
                            arrayTime[20] = DataService.InsertBank(db, bankLists);
                        }
                    break;
                case 7:
                    if (visitorLists != null)
                        if (visitorLists.size() > 0)
                            arrayTime[2] = DataService.InsertVisitor(db, visitorLists);
                    break;
                case 8:
                    if (checkLists != null)
                        if (checkLists.size() > 0)
                            arrayTime[10] = DataService.InsertCheckList(db, checkLists);
                    break;
                case 9:
                    if (transactionsLogs != null)
                        if (transactionsLogs.size() > 0)
                            arrayTime[11] = DataService.InsertTransactionsLog(db, transactionsLogs);
                    break;
                case 10:
                    if (orders != null)
                        if (orders.size() > 0) {
                            arrayTime[16] = DataService.InsertDeliveryOrder(db, orders);
                            if (orderDetails != null)
                                if (orderDetails.size() > 0) {
                                    arrayTime[17] = DataService.InsertDeliveryOrderDetail(db, orderDetails, mContext);
                                }
                        }
                    break;
                case 11:
                    if (promotions != null)
                        if (promotions.size() > 0) {
                            arrayTime[12] = DataService.InsertPromotion(db, promotions);
                        }
                    break;
                case 12:
                    if (promotionDetails != null)
                        if (promotionDetails.size() > 0) {
                            arrayTime[13] = DataService.InsertPromotionDetails(db, promotionDetails);
                        }
                    break;
                case 13:
                    if (promotionEntities != null)
                        if (promotionEntities.size() > 0) {
                            arrayTime[13] = DataService.InsertEntitiesOfPromotions(db, promotionEntities);
                        }
                    break;
                case 14:
                    if (reasons != null)
                        if (reasons.size() > 0) {
                            arrayTime[19] = DataService.InsertReason(db, reasons);
                        }
                    break;
                case 15:
                    if (picturesProducts != null)
                        if (picturesProducts.size() > 0)
                            arrayTime[5] = DataService.InsertPicturesProduct(db, picturesProducts);
                    break;
                case 16:
                    if (photoGalleries != null)
                        if (photoGalleries.size() > 0) {
                            arrayTime[21] = DataService.InsertPhotoGallery(db, photoGalleries,PhotoGalleryMaxRowVersion);
                        }
                    break;
                case 17:
                    if (regions != null)
                        if (regions.size() > 0) {
                            arrayTime[22] = DataService.InsertRegion(db, regions,RegionMaxRowVersion);
                        }
                    break;

                case 18:
                    if (propertyDescriptions != null)
                        if (propertyDescriptions.size() > 0) {
                            arrayTime[7] = DataService.InsertPropertyDescription(db, propertyDescriptions);
                        }
                    break;

                case 19:
                    if (productPriceLevelNames != null)
                        if (productPriceLevelNames.size() > 0) {
                            arrayTime[9] = DataService.InsertCostLevelName(db, productPriceLevelNames);
                        }
                    break;
                case 20:
                    if (productGroupLists != null)
                        if (productGroupLists.size() > 0) {
                            arrayTime[6] = DataService.InsertCategory(db, productGroupLists);
                        }
                    break;
                case 21:
                    if (settings != null)
                        if (settings.size() > 0) {
                            arrayTime[18] = DataService.InsertSettings(db, settings, mContext);
                        }
                    break;
                case 22:
                    if (missions != null)
                        if (missions.size() > 0) {
                            arrayTime[19] = DataService.InsertMissions(db, missions, mContext);
                        }
                    break;
                case 23:
                    if (missionDetails != null)
                        if (missionDetails.size() > 0) {
                            arrayTime[20] = DataService.InsertMissionDetails(db, missionDetails, mContext);
                        }
                    break;
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {

            switch (whichUpdate){
                case 1:
                    if (productList != null)
                        if (productList.size() > 0) {
                            tvProductList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + productList.size() );
                        }else
                            tvProductList.setText(R.string.no_new_item_for_receive);
                    if (visitorProducts != null)
                        if (visitorProducts.size() > 0) {
                            tvVisitorProduct.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + visitorProducts.size() );
                        }else
                            tvVisitorProduct.setText(R.string.no_new_item_for_receive);
                    break;
                case 2:
                    if (productDetails != null)
                        if (productDetails.size() > 0) {
                            tvProductDetailList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + productDetails.size() );
                        }else
                            tvProductDetailList.setText(R.string.no_new_item_for_receive);
                    break;
                case 3:
                    if (extraData != null)
                        if (extraData.size() > 0) {
                            tvExtraData.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + extraData.size() );
                        }else
                            tvExtraData.setText(R.string.no_new_item_for_receive);
                    break;
                case 4:
                    if (customerLists != null)
                        if (customerLists.size() > 0) {
                            tvPerson.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + customerLists.size() );
                        }else
                            tvPerson.setText(R.string.no_new_item_for_receive);
                    if (visitorPeople != null)
                        if (visitorPeople.size() > 0) {
                            tvVisitorPerson.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + visitorPeople.size() );
                        }else
                            tvVisitorPerson.setText(R.string.no_new_item_for_receive);
                    break;
                case 5:
                    if (personGroupLists != null)
                        if (personGroupLists.size() > 0) {
                            personGroup.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + personGroupLists.size() );
                        }else
                            personGroup.setText(R.string.no_new_item_for_receive);
                    break;
                case 6:
                    if (bankLists != null)
                        if (bankLists.size() > 0) {
                            tvBanks.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + bankLists.size() );
                        }else
                            tvBanks.setText(R.string.no_new_item_for_receive);
                    break;
                case 7:
                    if (visitorLists != null)
                        if (visitorLists.size() > 0) {
                            tvVisitorٰList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + visitorLists.size() );
                        }else
                            tvVisitorٰList.setText(R.string.no_new_item_for_receive);
                    break;
                case 8:
                    if (checkLists != null)
                        if (checkLists.size() > 0) {
                            tvCheckList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + checkLists.size() );
                        }else
                            tvCheckList.setText(R.string.no_new_item_for_receive);
                    break;
                case 9:
                    if (transactionsLogs != null)
                        if (transactionsLogs.size() > 0) {
                            tvTransaction.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + transactionsLogs.size() );
                        }else
                            tvTransaction.setText(R.string.no_new_item_for_receive);
                    break;
                case 10:
                    if (orders != null)
                        if (orders.size() > 0) {
                            tvDelivery.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + orders.size() );
                        }else
                            tvDelivery.setText(R.string.no_new_item_for_receive);
                    if (orderDetails != null)
                        if (orderDetails.size() > 0) {
                            tvDeliveryDetail.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + orderDetails.size() );
                        }else
                            tvDeliveryDetail.setText(R.string.no_new_item_for_receive);
                    break;
                case 11:
                    if (promotions != null)
                        if (promotions.size() > 0) {
                            tvPromotionList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + promotions.size() );
                        }else
                            tvPromotionList.setText(R.string.no_new_item_for_receive);
                    break;
                case 12:
                    if (promotionDetails != null)
                        if (promotionDetails.size() > 0) {
                            tvPromotionDetailList.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + promotionDetails.size() );
                        }else
                            tvPromotionDetailList.setText(R.string.no_new_item_for_receive);
                    break;
                case 13:
                    if (promotionEntities != null)
                        if (promotionEntities.size() > 0) {
                            tvPromotionDeliveryEntity.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + promotionEntities.size() );
                        }else
                            tvPromotionDeliveryEntity.setText(R.string.no_new_item_for_receive);
                    break;
                case 14:
                    if (reasons != null)
                        if (reasons.size() > 0) {
                            returnReason.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + reasons.size() );
                        }else
                            returnReason.setText(R.string.no_new_item_for_receive);
                    break;
                case 15:
                    if (picturesProducts != null)
                        if (picturesProducts.size() > 0) {
                            tvProductPicture.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + picturesProducts.size() );
                        }else
                            tvProductPicture.setText(R.string.no_new_item_for_receive);
                    break;
                case 16:
                    if (photoGalleries != null)
                        if (photoGalleries.size() > 0) {
                            tvPictureDetails.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + photoGalleries.size() );
                        }else
                            tvPictureDetails.setText(R.string.no_new_item_for_receive);
                    break;
                case 17:
                    if (regions != null)
                        if (regions.size() > 0) {
                            tvRegion.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + regions.size() );
                        }else
                            tvRegion.setText(R.string.no_new_item_for_receive);
                    break;
                case 18:
                    if (propertyDescriptions != null)
                        if (propertyDescriptions.size() > 0) {
                            tvPropertyDesc.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + propertyDescriptions.size() );
                        }else
                            tvPropertyDesc.setText(R.string.no_new_item_for_receive);
                    break;
                case 19:
                    if (productPriceLevelNames != null)
                        if (productPriceLevelNames.size() > 0) {
                            tvPriceLevelNames.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + productPriceLevelNames.size() );
                        }else
                            tvPriceLevelNames.setText(R.string.no_new_item_for_receive);
                    break;
                case 20:
                    if (productGroupLists != null)
                        if (productGroupLists.size() > 0) {
                            tvProductGroup.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + productGroupLists.size() );
                        }else
                            tvProductGroup.setText(R.string.no_new_item_for_receive);
                    break;
                case 21:
                    tvCheckList.requestFocus();
                    if (settings != null)
                        if (settings.size() > 0) {
                            tvSetting.setText(getString(R.string.str_message_ok) + getString(R.string.in_numbers) + " : " + settings.size() );
                        }else
                            tvSetting.setText(R.string.no_new_item_for_receive);
                    break;
            }

            if(whichUpdate < 23 ){
                whichUpdate++;
                new ReceiveAsyncTask(whichUpdate).execute();
            }else{
                dismissProgressDialog();
                sendSignInfoAsyncTask =  new SendSignInfoAsyncTask();
                sendSignInfoAsyncTask.execute();
            }


        }
    }

    class SendSignInfoAsyncTask extends AsyncTask<String, String, Integer> {

        List<PicturesProduct> picturesProducts = new ArrayList<>();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... arg0) {

            picturesProducts = db.getAllSignWithoutUrl();
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {

            if(picturesProducts.size() > 0){
                final String[] mMsg = {""};
                ApiInterface apiService = ApiClient.orderRetrofitClient().create(ApiInterface.class);
                SetAllDataBody setAllDataBody = new SetAllDataBody();
                setAllDataBody.setPictures(picturesProducts);
                Call<SaveAllDataResult> saveAllDataResultCall = apiService.SaveAllData(setAllDataBody);

                pd.setMessage(getString(R.string.sending_image));
                pd.setCancelable(false);
                pd.show();
                saveAllDataResultCall.enqueue(new Callback<SaveAllDataResult>() {
                    @Override
                    public void onResponse(@NonNull Call<SaveAllDataResult> call, @NonNull Response<SaveAllDataResult> response) {
                        if (response.body() != null && response.body().isResult()) {

                            if (picturesProducts.size() > 0) {
                                for (int i = 0; i < picturesProducts.size(); i++) {
                                    picturesProducts.get(i).setPictureId(response.body().getData().getObjects().getPictures().getResults().get(i).getEntityID());
                                    db.UpdatePicturesProductWithClientId(picturesProducts.get(i));
                                }
                            }


                            sendSignImageAsyncTask = new SendSignImageAsyncTask();
                            sendSignImageAsyncTask.execute();

                        } else if (response.body() != null) {
                            mMsg[0] = getString(R.string.send_error);
                            showDialog(response.body().getMessage());

                            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                            FirebaseCrashlytics.getInstance().log(response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<SaveAllDataResult> call, Throwable t) {
                        FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                        FirebaseCrashlytics.getInstance().log(t.getMessage());
                        dismissProgressDialog();
                        mMsg[0] = t.toString();
                        showDialog(mMsg[0]);
                    }
                });
            }else {
                if(isRadaraActive())
                    new TrackingConfig(mContext,pd).getSignalTokenAndSetting();

                new ReadOfflinePicturesProducts(mContext).readAllImages();
            }


        }
    }

    class SendSignImageAsyncTask extends AsyncTask<String, String, Integer> {


        @Override
        protected Integer doInBackground(String... arg0) {
            getFileList();
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (files != null) {
                if (files.length > 0) {
                    for (File mFile : files) {
                        final String[] mMsg = {""};
                        long pictureId = db.getPictureIdWithFileName(mFile.getName());
                        RequestBody filePart = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                        MultipartBody.Part mpfile = MultipartBody.Part.createFormData("file", mFile.getName(), filePart);
                        ApiInterface apiService = ApiClient.orderRetrofitClient().create(ApiInterface.class);

                        Call<setSignImage> mSetSignImageResult = apiService.uploadSignImage(mpfile, pictureId, mFile.getName(), getPrefUserToken());
                        pd.setMessage(getString(R.string.send_image) + mFile.getName());
                        pd.setCancelable(false);
                        pd.show();
                        mSetSignImageResult.enqueue(new Callback<setSignImage>() {
                            @Override
                            public void onResponse(@NonNull Call<setSignImage> call, @NonNull Response<setSignImage> response) {
                                if (response.body() != null && response.body().getResult()) {

                                    PicturesProduct picturesProduct = db.getPictureWithPictureId(response.body().getData().getEntityId());
                                    picturesProduct.setUrl(response.body().getData().getAdditionalData().getFileUrl());
                                    picturesProduct.setPictureHash(response.body().getData().getAdditionalData().getFileHash());
                                    db.UpdatePicturesProductWithClientId(picturesProduct);

                                } else if (response.body() != null) {
                                    mMsg[0] = getString(R.string.send_error);

                                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                                    FirebaseCrashlytics.getInstance().log(response.body().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<setSignImage> call, Throwable t) {
                                FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                                FirebaseCrashlytics.getInstance().log(t.getMessage());
                                dismissProgressDialog();
                                mMsg[0] = t.toString();
                                showDialog(mMsg[0]);
                            }
                        });
                    }
                }
            }

            SetDate();
            ShowDate();
            // TODO: 10/30/21 uncomment on tracking version
            if(isRadaraActive())
                new TrackingConfig(mContext,pd).getSignalTokenAndSetting();

            new ReadOfflinePicturesProducts(mContext).readAllImages();

        }
    }

    private void SetDate() {
        sh.edit().putLong(_Key_DateSyncInformation, new Date().getTime()).commit();
        user.setDateSync(getPrefDateSyncInformation());
        db.open();
        db.UpdateUser(user);
    }

    private void getFileList() {
        signForSend.clear();
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

    private void showDialog(String msg) {
        if (!isFinishing()) {
            Dialog(msg).show();
        }
    }

    public String getPdMessage(int whichUpdate){
        switch (whichUpdate){
            case 1:
                return "کالاها و کالاهای ویزیتور";
            case 2:
                return "جزییات کالاها";
            case 3:
                return "داده های افزوده";
            case 4:
                return "اشخاص و اشخاص ویزیتور";
            case 5:
                return "گروه اشخاص";
            case 6:
                return "بانکها";
            case 7:
                return "لیست ویزیتورها";
            case 8:
                return "چک لیستها";
            case 9:
                return "گردش اشخاص";
            case 10:
                return "لیست تحویلی ها";
            case 11:
                return "جزییات تحویلی ها";
            case 12:
                return "دلایل برگشتی";
            case 13:
                return "طرح های تشویقی";
            case 14:
                return "جزییات طرح های تشویقی";
            case 15:
                return "موجودیت های طرح های تشویقی";
            case 16:
                return "تصاویر کالاها";
            case 17:
                return "جزییات تصاویر کالاها";
            case 18:
                return "شهر ها و استان ها";
            case 19:
                return "شرح جزییات کالا";
            case 20:
                return "نام سطوح قیمتی";
            case 21:
                return "گروه بندی کالاها";
            case 22:
                return "ماموریت ها";
            case 23:
                return "دستور کارها";
        }
        return "";
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
