package com.mahak.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mahak.order.apiHelper.ApiClient;
import com.mahak.order.apiHelper.ApiInterface;
import com.mahak.order.common.Customer;
import com.mahak.order.common.GroupedTax;
import com.mahak.order.common.Order;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.OrderDetailProperty;
import com.mahak.order.common.Person_Extra_Data;
import com.mahak.order.common.Printer;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ReceivedTransferProducts;
import com.mahak.order.common.ReceivedTransfers;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.SharedPreferencesHelper;
import com.mahak.order.common.User;
import com.mahak.order.common.Visitor;
import com.mahak.order.common.request.SetAllDataBody;
import com.mahak.order.common.request.SetAllDataResult.SaveAllDataResult;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mahak.order.common.ProjectInfo.Woosim_WSP_R341;
import static com.mahak.order.common.ServiceTools.formatCount;
import static com.mahak.order.common.ServiceTools.getDateAndTimeForLong;

public class OrderDetailActivity extends BaseActivity {


    private static double visitorCreditValue;
    private static double mSpentCredit;

    private Bundle Extras;
    private static int page;
    private static int publish;
    private TextView
            tvCustomerName,
            tvMarketName,
            tvOrderDate,
            tvTotalPrice,
            txtTotalPrice,
            tvDiscount,
            tvFinalPrice,
            tvDescription,
            tvInvocieNumber,
            tvOrderType,
            tvPageTitle,
            tvTotalChargeAndTax,
            tvTotalOff,
            tvTotalReccipt,
            tvRemainingOrder,
            tvLableRemaining,
            tvPrice,
            tvfee,
            CustomerType;

    private TextView tvRemainingCustomer, tvFinalRemainingCustomer, tvCustomerStatus1, tvCustomerStatus2, tvPayable;
    private Button btnPrint, btnAddReceipt;
    private ListView lstProduct, lstGroupedTax;

    private LinearLayout
            llTotalReceipt,
            llRemainingOrder,
            llMargin,
            ll_transfer_width,
            llFinalRemainingCustomer,
            llRemainingCustomer,
            llPayable,
            llTotalChargeAndTax,
            llTotalOff,
            lltvDiscount,
            llFinalPrice,
            llMarketName;
    private LayoutParams layoutParams;

    private Context mContext;
    private Activity mActivity;
    private DbAdapter db;
    private Order order;
    //private ReturnOfSale returnOfSale;
    private Customer customer;
    private Visitor visitor;
    private long OrderId;
    private ArrayList<OrderDetail> orderDetails;
    private ArrayList<GroupedTax> groupedTaxes;
    private ArrayList<OrderDetail> orderDetailArrayList;
    private ArrayList<OrderDetail> arrayGiftProductForPrint;
    private AdapterListProduct adProduct;
    private AdapterListProductForPrint _adProduct;
    private AdapterGroupedTaxForPrint _adGroupedTax;
    private double TotalPrice, FinalPrice, Discount, Payable;
    private double TotalCount = 0;
    private int PackageCount = 0;
    private double TotalItems = 0;
    double TotalOff = 0, TotalReceipt = 0, RemainigOrder = 0;
    double RemainedCustomer = 0, FinalRemainedCustomer = 0;
    double TotalCharge = 0;
    double TotalTax = 0;
    double Tax, Charge, off;
    double ChargePercent, TaxPercent;
    double offValue;
    private View llInvoice;
    private String Name, LastName, MarketName, Description;
    int printerBrand;
    View ll;
    public static int OrderType = 0;
    private static int SYNC_TYPE_SUCCESS = 1;
    private static int SYNC_TYPE_FAILED = 2;
    private static int SYNC_TYPE_NO_NEED = 3;
    private static int SYNC_TYPE_MSG_SERVER = 4;

    private static String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    private static String PA_APP_SIGN = "AppSign";
    private static String PA_DATA = "Data";
    private static String PA_USER_ID = "UserId";
    private static String PA_MAHAK_ID = "MahakId";
    private static String PA_DATABASE_ID = "DatabaseId";
    private ProgressBar pbLoading;
    private double TotalTaxAndCharge;
    public static double TaxAndCharge = 0;

    public static int lst_transfer_detail = R.layout.lst_transfer_detail;
    private static final int REQUEST_WRITE_STORAGE = 112;

    //oscar
    public static int lst_order_detail_for_print = R.layout.lst_order_detail_for_print;
    boolean hasWritePermission = false;
    private Person_Extra_Data extraData = new Person_Extra_Data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        hasWritePermission = (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasWritePermission) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
            }
        }

        //____config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
        tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________
        mContext = this;
        mActivity = this;

        printerBrand = SharedPreferencesHelper.getPrefPrinterBrand(mContext);

        if (
                printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW ||
                        printerBrand == ProjectInfo.PRINTER_BABY_380_A ||
                        printerBrand == ProjectInfo.PRINTER_DELTA_380_A ||
                        printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII ||
                        printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310 || printerBrand == ProjectInfo.UROVO_K319 || printerBrand == ProjectInfo.Woosim_WSP_R341

        ) {

            lst_order_detail_for_print = R.layout.lst_order_detail_for_print_fii;

        } else if (printerBrand == ProjectInfo.PRINTER_BABY_280_A) {
            lst_order_detail_for_print = R.layout.lst_order_detail_for_print_50mm;
        } else if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223 || printerBrand == ProjectInfo.SMART_POS_UROVO_i9000s) {
            lst_order_detail_for_print = R.layout.lst_print_szzt;
        } else {
            if (getTemplate2Status(mContext, ProjectInfo._pName_OrderDetail))
                lst_order_detail_for_print = R.layout.lst_factor_bixolon2;
            else
                lst_order_detail_for_print = R.layout.lst_order_detail_for_print_prop;
        }


        Extras = getIntent().getExtras();
        if (Extras != null) {
            page = Extras.getInt(PAGE);
            OrderId = Extras.getLong(ID);
            OrderType = Extras.getInt(TYPE_KEY);
            publish = Extras.getInt(RETURN_PUBLISH_KEY);

        }

        if (OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE) {

            lst_transfer_detail = R.layout.lst_transfer_detail;
        } else if (OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {
            lst_order_detail_for_print = R.layout.lst_order_detail_for_print_return;
        } else {
            lst_transfer_detail = R.layout.lst_order_detail_for_print;
        }

        initialise();
        FillView();

        ///////////////////////sml
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasWritePermission) {
                    PreparePrinterData ppd = new PreparePrinterData();
                    ppd.execute();
                }
            }
        });

        btnAddReceipt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE) {

                    sendTransfer(order.getCode());

                } else {
                    Intent intent = new Intent(mContext, ManageReceiptActivity.class);
                    intent.putExtra(MODE_PAGE, MODE_NEW);
                    intent.putExtra(CODE_KEY, order.getCode());
                    intent.putExtra(CUSTOMERID_KEY, order.getPersonId());
                    intent.putExtra(CUSTOMER_CLIENT_ID_KEY, order.getPersonClientId());
                    intent.putExtra(PAGE, PAGE_ORDER_DETAIL);
                    intent.putExtra(PAYMENT_KEY, FinalPrice);
                    startActivity(intent);
                }
            }
        });

    }//End of OnCreate

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasWritePermission = true;
                } else {
                    Toast.makeText(this, R.string.no_permission_for_writing_sd, Toast.LENGTH_LONG).show();
                    hasWritePermission = false;
                }
            }
        }
    }

    private void sendTransfer(String transferCode) {

        db.open();
        final ApiInterface apiService;
        SetAllDataBody setAllDataBody = new SetAllDataBody();
        User user = db.getUser();
        Visitor senderVisitor = db.getVisitorWithVisitorID(getPrefUserId());
        Call<SaveAllDataResult> saveAllDataResultCall;
        long TransferStoreClientId = ServiceTools.toLong(ServiceTools.getGenerationCode());
        long TransferStoreDetailClientId = ServiceTools.toLong(ServiceTools.getGenerationCode());

        setAllDataBody.setUserToken(user.getUserToken());
        apiService = ApiClient.getClient().create(ApiInterface.class);

        final List<Order> arrayInvoice;
        final List<OrderDetail> arrayInvoiceDetail;
        List<OrderDetailProperty> orderDetailProperties;

        ReceivedTransfers receivedTransfers;
        ReceivedTransferProducts receivedTransferProducts;

        final List<ReceivedTransfers> transferStores = new ArrayList<>();
        final List<ReceivedTransferProducts> transferStoreDetails = new ArrayList<>();

        Visitor visitor;

        arrayInvoice = db.getAllTransferNotPublish(BaseActivity.getPrefUserId(), transferCode);
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


        for (int i = 0; i < arrayInvoice.size(); i++) {
            visitor = db.getVisitor(arrayInvoice.get(i).getPersonId());
            receivedTransfers = new ReceivedTransfers();
            // TransferStoreClientId++;
            receivedTransfers.setTransferDate(arrayInvoice.get(i).getDeliveryDate());
            receivedTransfers.setSenderStoreCode((int) senderVisitor.getStoreCode());
            receivedTransfers.setReceiverStoreCode((int) visitor.getStoreCode());
            receivedTransfers.setSenderVisitorId(String.valueOf(BaseActivity.getPrefUserMasterId()));
            receivedTransfers.setReceiverVisitorId(String.valueOf(visitor.getVisitorId()));
            receivedTransfers.setTransferStoreClientId(ServiceTools.toLong(arrayInvoice.get(i).getCode()));
            //receivedTransfers.setTransferStoreCode(String.valueOf(arrayInvoice.get(i).getCode()));
            receivedTransfers.setIsAccepted(ProjectInfo.TYPE_NaN);
            receivedTransfers.setDescription(arrayInvoice.get(i).getDescription());
            transferStores.add(receivedTransfers);
        }
        for (int j = 0; j < arrayInvoice.size(); j++) {
            for (int i = 0; i < arrayInvoiceDetail.size(); i++) {
                receivedTransferProducts = new ReceivedTransferProducts();
                TransferStoreDetailClientId++;
                receivedTransferProducts.setCount1(arrayInvoiceDetail.get(i).getCount1());
                receivedTransferProducts.setCount2(arrayInvoiceDetail.get(i).getCount2());
                receivedTransferProducts.setProductDetailId(String.valueOf(arrayInvoiceDetail.get(i).getProductDetailId()));
                receivedTransferProducts.setDescription(arrayInvoiceDetail.get(i).getDescription());
                receivedTransferProducts.setTransferStoreDetailClientId(TransferStoreDetailClientId);
                receivedTransferProducts.setTransferStoreClientId(transferStores.get(j).getTransferStoreClientId());
                transferStoreDetails.add(receivedTransferProducts);
            }
        }

        setAllDataBody.setReceivedTransfers(transferStores);
        setAllDataBody.setReceivedTransferProducts(transferStoreDetails);
        saveAllDataResultCall = apiService.SaveAllData(setAllDataBody);

        pbLoading.setVisibility(View.VISIBLE);
        saveAllDataResultCall.enqueue(new Callback<SaveAllDataResult>() {
            @Override
            public void onResponse(Call<SaveAllDataResult> call, Response<SaveAllDataResult> response) {
                if (response.body() != null && response.body().isResult()) {
                    db.open();
                    for (int i = 0; i < arrayInvoice.size(); i++) {
                        arrayInvoice.get(i).setOrderId(response.body().getData().getObjects().getTransferStores().getResults().get(i).getEntityID());
                        arrayInvoice.get(i).setPublish(ProjectInfo.PUBLISH);
                        db.UpdateOrder(arrayInvoice.get(i));
                    }
                    for (int j = 0; j < arrayInvoiceDetail.size(); j++) {
                        arrayInvoiceDetail.get(j).setOrderDetailId(response.body().getData().getObjects().getTransferStoreDetails().getResults().get(j).getEntityID());
                        db.UpdateOrderDetail(arrayInvoiceDetail.get(j));
                    }

                    Toast.makeText(mActivity, R.string.sent, Toast.LENGTH_SHORT).show();
                    pbLoading.setVisibility(View.GONE);
                    btnAddReceipt.setVisibility(View.GONE);

                    db.close();
                    Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_SEND_TRANSFERENCE);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (response.body() != null) {
                    Toast.makeText(mContext, R.string.error_in_sending, Toast.LENGTH_SHORT).show();
                    pbLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<SaveAllDataResult> call, Throwable t) {
                Toast.makeText(mContext, t.toString(), Toast.LENGTH_SHORT).show();
                pbLoading.setVisibility(View.GONE);
            }
        });
        db.close();
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

    private void initialise() {

        tvCustomerName = (TextView) findViewById(R.id.tvCustomerName);
        tvMarketName = (TextView) findViewById(R.id.tvMarketName);
        tvOrderDate = (TextView) findViewById(R.id.tvOrderDate);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalCount);
        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);
        tvFinalPrice = (TextView) findViewById(R.id.tvFinalPrice);
        tvDiscount = (TextView) findViewById(R.id.tvDiscount);
        tvTotalOff = (TextView) findViewById(R.id.tvTotalOff);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvInvocieNumber = (TextView) findViewById(R.id.tvInvocieNumber);
        tvOrderType = (TextView) findViewById(R.id.tvOrderType);
        tvTotalChargeAndTax = (TextView) findViewById(R.id.tvTotalChargeAndTax);
        tvTotalReccipt = (TextView) findViewById(R.id.tvTotalReceipt);
        tvRemainingOrder = (TextView) findViewById(R.id.tvRemainingOrder);
        tvFinalRemainingCustomer = (TextView) findViewById(R.id.tvFinalRemainingCustomer);
        tvRemainingCustomer = (TextView) findViewById(R.id.tvRemainingCustomer);
        tvCustomerStatus1 = (TextView) findViewById(R.id.tvCustomerStatus1);
        tvCustomerStatus2 = (TextView) findViewById(R.id.tvCustomerStatus2);
        tvLableRemaining = (TextView) findViewById(R.id.tvLableRemaining);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvfee = (TextView) findViewById(R.id.tvfee);
        tvPayable = (TextView) findViewById(R.id.tvPayable);
        CustomerType = (TextView) findViewById(R.id.CustomerType);
        llInvoice = findViewById(R.id.llInvoice);
        llRemainingOrder = (LinearLayout) findViewById(R.id.llRemainingOrder);
        llMargin = (LinearLayout) findViewById(R.id.llMargin);
        ll_transfer_width = (LinearLayout) findViewById(R.id.ll_transfer_width);
        layoutParams = ll_transfer_width.getLayoutParams();
        llTotalReceipt = (LinearLayout) findViewById(R.id.llTotalReceipt);
        llRemainingCustomer = (LinearLayout) findViewById(R.id.llRemainingCustomer);
        llFinalRemainingCustomer = (LinearLayout) findViewById(R.id.llFinalRemainingCustomer);
        llPayable = (LinearLayout) findViewById(R.id.llPayable);
        llTotalChargeAndTax = (LinearLayout) findViewById(R.id.llTotalChargeAndTax);
        llTotalOff = (LinearLayout) findViewById(R.id.llTotalOff);
        lltvDiscount = (LinearLayout) findViewById(R.id.lltvDiscount);
        llMarketName = (LinearLayout) findViewById(R.id.llMarketName);
        llFinalPrice = (LinearLayout) findViewById(R.id.llFinalPrice);
        pbLoading = (ProgressBar) findViewById(R.id.progressBar2);

        btnPrint = (Button) findViewById(R.id.btnPrint);
        btnAddReceipt = (Button) findViewById(R.id.btnAddReceipt);

        lstProduct = (ListView) findViewById(R.id.lstProduct);
        lstGroupedTax = (ListView) findViewById(R.id.lstGroupedTax);

        db = new DbAdapter(mContext);
    }

    ///////////////////////////////sml
    public void FillPrintView(View view) {
        //controls
        TextView _tvInvocieNumber = (TextView) view.findViewById(R.id._tvInvocieNumber);
        TextView _tvTotalCount = (TextView) view.findViewById(R.id._tvTotalCount);
        TextView _tvTotalItems = (TextView) view.findViewById(R.id._tvTotalItems);
        TextView _tvCustomerName = (TextView) view.findViewById(R.id._tvCustomerName);
        TextView _tvMarketName = (TextView) view.findViewById(R.id._tvMarketName);
        LinearLayout _llMarketName = (LinearLayout) view.findViewById(R.id._llMarketName);
        LinearLayout _llFooterMessage = (LinearLayout) view.findViewById(R.id._llFooterMessage);
        LinearLayout _llTotalReceipt = (LinearLayout) view.findViewById(R.id._llTotalReceipt);
        LinearLayout _llPayable = (LinearLayout) view.findViewById(R.id._llPayable);
        LinearLayout _llCustomerStatus2 = (LinearLayout) view.findViewById(R.id._llCustomerStatus2);
        LinearLayout _llCustomerStatus1 = (LinearLayout) view.findViewById(R.id._llCustomerStatus1);
        LinearLayout _llFinalPrice = (LinearLayout) view.findViewById(R.id._llFinalPrice);
        LinearLayout _llDiscount = (LinearLayout) view.findViewById(R.id._llDiscount);
        LinearLayout _llTotalCount = (LinearLayout) view.findViewById(R.id._llTotalCount);
        ListView _lstProduct = (ListView) view.findViewById(R.id._lstProduct);
        ListView _lstGroupedTax = (ListView) view.findViewById(R.id._lstGroupedTax);
        TextView _tvTotalPrice = (TextView) view.findViewById(R.id._tvTotalPrice);
        TextView _tvTotalOff = (TextView) view.findViewById(R.id._tvTotalOff);
        TextView _tvTotalChargeAndTax = (TextView) view.findViewById(R.id._tvTotalChargeAndTax);
        TextView _tvDiscount = (TextView) view.findViewById(R.id._tvDiscount);
        TextView _tvFinalPrice = (TextView) view.findViewById(R.id._tvFinalPrice);
        TextView _tvTotalReceipt = (TextView) view.findViewById(R.id._tvTotalReceipt);
        TextView _tvRemainingOrder = (TextView) view.findViewById(R.id._tvRemainingOrder);
        TextView _tvCustomerStatus1 = (TextView) view.findViewById(R.id._tvCustomerStatus1);
        TextView _tvCustomerStatus2 = (TextView) view.findViewById(R.id._tvCustomerStatus2);
        TextView _tvOrderDate = (TextView) view.findViewById(R.id._tvOrderDate);
        TextView _tvFooterMessage = (TextView) view.findViewById(R.id._tvFooterMessage);
        TextView _tvPayable = (TextView) view.findViewById(R.id._tvPayable);
        TextView _tvType = (TextView) view.findViewById(R.id._tvType);
        LinearLayout _llTitle = (LinearLayout) view.findViewById(R.id.llTitle);
        _llTitle.setVisibility(View.GONE);
        TextView _tvUsername = (TextView) view.findViewById(R.id._tvUsername);
        TextView CustomerName = (TextView) view.findViewById(R.id.tvCustomerName);
        TextView _txtPrice = (TextView) view.findViewById(R.id._txtPrice);
        TextView _txtTotalPrice = (TextView) view.findViewById(R.id._txtTotalPrice);
        if (OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {

            _tvType.setText(tvOrderType.getText().toString());
            CustomerName.setText(R.string.customer_name);
            _llMarketName.setVisibility(View.GONE);
            _llFooterMessage.setVisibility(View.GONE);
            _llTotalReceipt.setVisibility(View.GONE);
            _llPayable.setVisibility(View.GONE);
            _llCustomerStatus2.setVisibility(View.GONE);
            _llCustomerStatus1.setVisibility(View.GONE);
            _llFinalPrice.setVisibility(View.GONE);
            _llDiscount.setVisibility(View.GONE);
            _llTotalCount.setVisibility(View.GONE);
            _txtPrice.setVisibility(View.GONE);
            _txtTotalPrice.setText(R.string.str_total_count);
        } else if (order.getOrderType() == ProjectInfo.TYPE_INVOCIE)
            _tvType.setText(getString(R.string.str_type_sale_invoice));
        else if (order.getOrderType() == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
            _tvType.setText(tvOrderType.getText().toString());
            CustomerName.setText(R.string.transfer_reciept_name);
            _llMarketName.setVisibility(View.GONE);
            _llFooterMessage.setVisibility(View.GONE);
            _llTotalReceipt.setVisibility(View.GONE);
            _llPayable.setVisibility(View.GONE);
            _llCustomerStatus2.setVisibility(View.GONE);
            _llCustomerStatus1.setVisibility(View.GONE);
            _llFinalPrice.setVisibility(View.GONE);
            _llDiscount.setVisibility(View.GONE);
            _llTotalCount.setVisibility(View.GONE);
            _txtPrice.setVisibility(View.GONE);
            _txtTotalPrice.setText(R.string.str_total_count);
        } else
            _tvType.setText(tvOrderType.getText().toString());
        _tvCustomerName.setText(tvCustomerName.getText().toString());
        _tvMarketName.setText(tvMarketName.getText().toString());
        _tvInvocieNumber.setText(tvInvocieNumber.getText().toString());
        _tvOrderDate.setText(tvOrderDate.getText().toString());
        _tvTotalCount.setText(formatCount(TotalCount));
        _tvTotalItems.setText(formatCount(TotalItems));

        if (BaseActivity.getAuthentication())
            _tvUsername.setText(BaseActivity.getUserProfile().getName());

        orderDetailArrayList = (ArrayList<OrderDetail>) orderDetails.clone();

        if (SharedPreferencesHelper.getCurrentLanguage(mContext).equals("de_DE")) {
            _lstGroupedTax.setVisibility(View.VISIBLE);
            db.open();
            groupedTaxes = db.getGroupedTaxCharge(OrderId);
            _adGroupedTax = new AdapterGroupedTaxForPrint(mActivity, groupedTaxes);
            _lstGroupedTax.setDrawingCacheEnabled(true);
            _lstGroupedTax.setAdapter(_adGroupedTax);
            ServiceTools.setListViewHeightBasedOnChildren(_lstGroupedTax);
        }

        _adProduct = new AdapterListProductForPrint(mActivity, orderDetailArrayList);
        _lstProduct.setDrawingCacheEnabled(true);
        _lstProduct.setAdapter(_adProduct);
        ServiceTools.setListViewHeightBasedOnChildren(_lstProduct);

        _tvTotalPrice.setText(tvTotalPrice.getText().toString());
        _tvTotalOff.setText(tvTotalOff.getText().toString());
        _tvTotalChargeAndTax.setText(tvTotalChargeAndTax.getText().toString());
        _tvDiscount.setText(tvDiscount.getText().toString());
        _tvFinalPrice.setText(tvFinalPrice.getText().toString());
        _tvTotalReceipt.setText(tvTotalReccipt.getText().toString());
        _tvRemainingOrder.setText(tvRemainingOrder.getText().toString());
        _tvCustomerStatus1.setText(tvCustomerStatus1.getText().toString() + " " + tvRemainingCustomer.getText().toString());
        _tvCustomerStatus2.setText(tvCustomerStatus2.getText().toString() + " " + tvFinalRemainingCustomer.getText().toString());
        _tvPayable.setText(tvPayable.getText().toString());

        if (getTemplate2Status(mContext, ProjectInfo._pName_OrderDetail) && printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R200_II)
            _tvFooterMessage.setText(tvDescription.getText().toString());
        else
            _tvFooterMessage.setText(getString(R.string.print_footer_messages_part1) + " " + tvCustomerName.getText().toString() + "\n" + getString(R.string.print_footer_messages_part2));

    }

    private class PreparePrinterData extends AsyncTask<String, Integer, Boolean> {

        Bitmap b = null;
        String fName = "";
        String fPath = ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_IMAGES + "/" + ProjectInfo.DIRECTORY_INVOICES;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            Boolean status = false;

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (SharedPreferencesHelper.getCompactPrint(mContext)) {
                if (printerBrand == ProjectInfo.PRINTER_BABY_380_A || printerBrand == ProjectInfo.PRINTER_DELTA_380_A) {
                    ll = inflater.inflate(R.layout.factor_print_template_80mm_fii_compact, null, false);
                } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII) {

                    ll = inflater.inflate(R.layout.factor_print_template_80mm_fii_compact, null, false);
                    // Gets the layout params that will allow you to resize the layout
                    LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                    ChangePrintWidth(_llPrint);

                } else if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW || printerBrand == ProjectInfo.UROVO_K319 || printerBrand == Woosim_WSP_R341) {
                    ll = inflater.inflate(R.layout.factor_print_template_80mm_fii_compact, null, false);
                    // Gets the layout params that will allow you to resize the layout
                    LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                    ChangePrintWidth(_llPrint);

                } else if (printerBrand == ProjectInfo.PRINTER_BABY_280_A) {
                    ll = inflater.inflate(R.layout.factor_print_template_50mm_fii_compact, null, false);
                } else if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310) {
                    ll = inflater.inflate(R.layout.factor_print_template_80mm_fii_compact, null, false);

                } else if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223 || printerBrand == ProjectInfo.SMART_POS_UROVO_i9000s) {
                    ll = inflater.inflate(R.layout.factor_print_szzt, null, false);

                } else {
                    if (getTemplate2Status(mContext, ProjectInfo._pName_OrderDetail))
                        ll = inflater.inflate(R.layout.factor_bixolon_2, null, false);
                    else
                        ll = inflater.inflate(R.layout.factor_print_template_compact, null, false);
                }
            } else {
                if (printerBrand == ProjectInfo.PRINTER_BABY_380_A || printerBrand == ProjectInfo.PRINTER_DELTA_380_A) {
                    ll = inflater.inflate(R.layout.factor_print_template_80mm_fii, null, false);
                } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII) {

                    ll = inflater.inflate(R.layout.factor_print_template_80mm_fii, null, false);
                    // Gets the layout params that will allow you to resize the layout
                    LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                    ChangePrintWidth(_llPrint);

                } else if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW || printerBrand == ProjectInfo.UROVO_K319 || printerBrand == Woosim_WSP_R341) {
                    ll = inflater.inflate(R.layout.factor_print_template_80mm_fii, null, false);
                    // Gets the layout params that will allow you to resize the layout
                    LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                    ChangePrintWidth(_llPrint);

                } else if (printerBrand == ProjectInfo.PRINTER_BABY_280_A) {
                    ll = inflater.inflate(R.layout.factor_print_template_50mm_fii, null, false);
                } else if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310) {
                    ll = inflater.inflate(R.layout.factor_print_template_80mm_fii, null, false);

                } else if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223 || printerBrand == ProjectInfo.SMART_POS_UROVO_i9000s) {
                    ll = inflater.inflate(R.layout.factor_print_szzt, null, false);

                } else {
                    ll = inflater.inflate(R.layout.factor_print_template, null, false);
                }
            }

            LinearLayout _ll_Remain_Detail = (LinearLayout) ll.findViewById(R.id._ll_Remain_Detail);
            LinearLayout _llFooterMessage = (LinearLayout) ll.findViewById(R.id._llFooterMessage);
            if (!SharedPreferencesHelper.getDetailUnderFactor(mContext)) {
                _ll_Remain_Detail.setVisibility(View.GONE);
            }
            if (!SharedPreferencesHelper.getSignUnderFactor(mContext)) {
                _llFooterMessage.setVisibility(View.GONE);
            }
            FillPrintView(ll);
            ll.setDrawingCacheEnabled(true);
            ll.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            ll.layout(0, 0, ll.getMeasuredWidth(), ll.getMeasuredHeight());
            ll.buildDrawingCache(true);
            //b = Bitmap.createBitmap(ll.getDrawingCache());
            b = Printer.CreateBitmap(ll);
            ll.setDrawingCacheEnabled(false);

            if (OrderType == ProjectInfo.TYPE_RETURN_OF_SALE)
                fName = GetFileName(order.getOrderDate());
            else
                fName = GetFileName(order.getOrderDate());
            if (b != null) {
                if (Printer.CreateFile(b, fName, fPath)) {
                }
            }

            if (b != null)
                status = true;

            return status;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                Intent intent = new Intent(OrderDetailActivity.this, PrintActivity.class);
                intent.putExtra(ProjectInfo._TAG_PAGE_NAME, ProjectInfo._pName_OrderDetail);
                intent.putExtra(ProjectInfo._TAG_PATH, fPath);
                intent.putExtra(ProjectInfo._TAG_Name, fName);
                intent.putExtra("OrderCode", tvInvocieNumber.getText().toString());
                intent.putExtra(ProjectInfo._TAG_Order_Type, order.getOrderType());
                startActivity(intent);
            }
        }
    }

    public void ChangePrintWidth(LinearLayout ll) {

        LayoutParams param = ll.getLayoutParams();
        final float scale = getResources().getDisplayMetrics().density;
        //convert mm to dp
        double Size = SharedPreferencesHelper.getCurrentWidthSize(mContext) * 6.3;
        int converter = (int) (Size * scale + 0.5f);
        param.width = converter;
        ll.setLayoutParams(param);
        ll.setLayoutParams(param);

    }

    /**
     * Read From Database And Fill TextView And Fill Adapter
     */
    private void FillView() {
        db.open();

        if (SharedPreferencesHelper.getCurrentLanguage(mContext).equals("en"))
            lstGroupedTax.setVisibility(View.GONE);
        else if (SharedPreferencesHelper.getCurrentLanguage(mContext).equals("de_DE")) {
            groupedTaxes = db.getGroupedTaxCharge(OrderId);
            _adGroupedTax = new AdapterGroupedTaxForPrint(mActivity, groupedTaxes);
            lstGroupedTax.setDrawingCacheEnabled(true);
            lstGroupedTax.setAdapter(_adGroupedTax);
            ServiceTools.setListViewHeightBasedOnChildren(lstGroupedTax);
        }

        orderDetails = new ArrayList<>();
        orderDetailArrayList = new ArrayList<>();

        // order = db.GetOrderWithOrderId(OrderId);

        order = db.GetOrder(OrderId);

        if (order.getPersonId() == ProjectInfo.CUSTOMERID_GUEST) {
            customer = db.getCustomerWithPersonClientId(order.getPersonClientId());
        } else if (order.getPersonId() != ProjectInfo.CUSTOMERID_GUEST) {
            customer = db.getCustomerWithPersonId(order.getPersonId());
        }

        if (OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {

//            Discount = (order.getDiscount() == null) ? 0 :  ServiceTools.toLong(order.getDiscount());
            //Read TotalReceipt___________________________________________________________
            TotalReceipt = db.getTotalReceiptFromOrder(order.getCode());

            //Read From Database ProductInOrder and Computing FinalPrice____________________
            orderDetails = db.getAllProductWithOrderDetail(order.getId());
            //Parse Json Description _______________________________________________________
            Description = order.getDescription();

            tvCustomerName.setText(customer.getName());
            tvMarketName.setText(customer.getOrganization());
            tvDescription.setText(Description);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(order.getOrderDate());
            TotalItems = orderDetails.size();
            tvOrderDate.setText(getDateAndTimeForLong(order.getOrderDate()));


            adProduct = new AdapterListProduct(mActivity, orderDetails);
            lstProduct.setAdapter(adProduct);
            ServiceTools.setListViewHeightBasedOnChildren(lstProduct);

            tvOrderType.setText(getString(R.string._returnOfSale));
            tvPageTitle.setText(getString(R.string.str_detail_return));
            llMarketName.setVisibility(View.GONE);
            llFinalPrice.setVisibility(View.GONE);
            tvPrice.setVisibility(View.INVISIBLE);
            tvfee.setVisibility(View.INVISIBLE);
            txtTotalPrice.setText(R.string.str_total_count);
            tvLableRemaining.setVisibility(View.GONE);
            llRemainingOrder.setVisibility(View.GONE);
            llMargin.setVisibility(View.VISIBLE);
            llTotalReceipt.setVisibility(View.GONE);
            llRemainingCustomer.setVisibility(View.GONE);
            llFinalRemainingCustomer.setVisibility(View.GONE);
            btnAddReceipt.setVisibility(View.GONE);
            llPayable.setVisibility(View.GONE);
            llTotalChargeAndTax.setVisibility(View.GONE);
            llTotalOff.setVisibility(View.GONE);
            lltvDiscount.setVisibility(View.GONE);

            tvTotalPrice.setText("" + db.getTotalProductById(order.getId()));
            tvInvocieNumber.setText(order.getCode());

        } else {
            CalculateTotal();
            TotalItems = orderDetails.size();
            //Parse Json Description _______________________________________________________
            Description = order.getDescription();
            //Fill TextView_________________________________________________________________
            if (OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
                visitor = db.getVisitor(order.getPersonId());
                tvCustomerName.setText(visitor.getName());
            } else {
                tvCustomerName.setText(customer.getName());
                tvMarketName.setText(customer.getOrganization());

                if (customer != null)
                    extraData = db.getMoreCustomerInfo(customer.getPersonCode());
                RemainedCustomer = extraData.getRemainAmount();
                if (extraData.getRemainStatus() == 1)
                    RemainedCustomer = RemainedCustomer * -1;
            }

            tvDescription.setText(Description);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(order.getOrderDate());

            tvOrderDate.setText(getDateAndTimeForLong(order.getOrderDate()));
            tvTotalPrice.setText(ServiceTools.formatPrice(TotalPrice));
            tvTotalOff.setText(ServiceTools.formatPrice(TotalOff));
            tvDiscount.setText(ServiceTools.formatPrice(Discount));
            tvTotalChargeAndTax.setText(ServiceTools.formatPrice(TotalTaxAndCharge));
            tvFinalPrice.setText(ServiceTools.formatPrice(FinalPrice));
            tvInvocieNumber.setText(order.getCode());
            tvTotalReccipt.setText(ServiceTools.formatPrice(TotalReceipt));
            tvRemainingOrder.setText(ServiceTools.formatPrice(RemainigOrder));

            if (order.getOrderType() == ProjectInfo.TYPE_INVOCIE) {
                tvOrderType.setText(getString(R.string.str_type_invoice));
                tvPageTitle.setText(getString(R.string.str_detail_invoice));
                tvLableRemaining.setText(getString(R.string.str_remaining_invoice));
            } else if (order.getOrderType() == ProjectInfo.TYPE_ORDER) {
                tvOrderType.setText(getString(R.string.str_type_past_invoice));
                tvPageTitle.setText(getString(R.string.str_detail_order));
                tvLableRemaining.setText(getString(R.string.str_remaining_order));
            } else if (order.getOrderType() == ProjectInfo.TYPE_SEND_TRANSFERENCE) {

                tvOrderType.setText(getString(R.string.str_type_past_transfer));
                tvPageTitle.setText(getString(R.string.str_detail_transfer));
                CustomerType.setText(R.string.transfer_reciever_name);
                tvOrderDate.setText(getDateAndTimeForLong(order.getDeliveryDate()));
                llMarketName.setVisibility(View.GONE);
                llFinalPrice.setVisibility(View.GONE);
                tvPrice.setVisibility(View.INVISIBLE);
                tvfee.setVisibility(View.INVISIBLE);
                txtTotalPrice.setText(R.string.str_total_count);
                layoutParams.width = 250;
                tvLableRemaining.setVisibility(View.GONE);
                llRemainingOrder.setVisibility(View.GONE);
                llMargin.setVisibility(View.VISIBLE);
                llTotalReceipt.setVisibility(View.GONE);
                llRemainingCustomer.setVisibility(View.GONE);
                llFinalRemainingCustomer.setVisibility(View.GONE);
                if (order.getPublish() == ProjectInfo.DONT_PUBLISH) {
                    btnAddReceipt.setText(R.string.send_transfer);
                } else {
                    btnAddReceipt.setVisibility(View.GONE);
                }

                llPayable.setVisibility(View.GONE);
                llTotalChargeAndTax.setVisibility(View.GONE);
                llTotalOff.setVisibility(View.GONE);
                lltvDiscount.setVisibility(View.GONE);
                // tvTotalPrice.setText(""+ db.getTotalSendTransferById(order.getId()));
                tvTotalPrice.setText(String.valueOf(TotalCount));

            }

            //Check show TotalReceipt and Remaining order _________________________
            if (BaseActivity.getPrefShowFieldOrder(mContext)) {
                llTotalReceipt.setVisibility(View.VISIBLE);
                llRemainingOrder.setVisibility(View.VISIBLE);
                llRemainingCustomer.setVisibility(View.VISIBLE);
                llFinalRemainingCustomer.setVisibility(View.VISIBLE);
            } else {
                llTotalReceipt.setVisibility(View.GONE);
                llRemainingOrder.setVisibility(View.GONE);
                llRemainingCustomer.setVisibility(View.GONE);
                llFinalRemainingCustomer.setVisibility(View.GONE);
            }
            //Remained Customer_____________________________________________________
            if (RemainedCustomer == 0) {// if customerStatus =incalculable
                tvRemainingCustomer.setText("(" + getString(R.string.str_incalculable) + ")  " + ServiceTools.formatPrice(RemainedCustomer));
                FinalRemainedCustomer = RemainedCustomer + FinalPrice - TotalReceipt;
            }
            if (RemainedCustomer < 0) {// if customerStatus =Debtor
                //RemainedCustomer = RemainedCustomer * -1;
                tvRemainingCustomer.setText("(" + getString(R.string.str_debitor_short) + ")  " + ServiceTools.formatPrice(RemainedCustomer * -1));
                FinalRemainedCustomer = RemainedCustomer * -1 + FinalPrice - TotalReceipt;
            } else if (RemainedCustomer > 0)// if customerStaus =Creditor
            {
                tvRemainingCustomer.setText("(" + getString(R.string.str_creditor_short) + ")  " + ServiceTools.formatPrice(RemainedCustomer));
                FinalRemainedCustomer = (-1 * RemainedCustomer) + FinalPrice - TotalReceipt;
            }

            if (FinalRemainedCustomer > 0)
                tvFinalRemainingCustomer.setText("(" + getString(R.string.str_debitor_short) + ")  " + ServiceTools.formatPrice(FinalRemainedCustomer));
            else if (FinalRemainedCustomer < 0) {
                FinalRemainedCustomer = FinalRemainedCustomer * -1;
                tvFinalRemainingCustomer.setText("(" + getString(R.string.str_creditor_short) + ")  " + ServiceTools.formatPrice(FinalRemainedCustomer));
            } else if (FinalRemainedCustomer == 0)
                tvFinalRemainingCustomer.setText("(" + getString(R.string.str_incalculable) + ")  " + ServiceTools.formatPrice(FinalRemainedCustomer));
            //Fill Adapter_________________________________________________________
            adProduct = new AdapterListProduct(mActivity, orderDetails);
            lstProduct.setAdapter(adProduct);
            ServiceTools.setListViewHeightBasedOnChildren(lstProduct);

            //////Payable
            if (RemainedCustomer < 0) { //Debtor
                Payable = FinalPrice + (RemainedCustomer * -1);
            } else if (RemainedCustomer > 0) { //Creditor
                Payable = FinalPrice - RemainedCustomer;
            } else { //Incalculable
                Payable = FinalPrice;
            }
            if (Payable < 0) {
                tvPayable.setText("-" + ServiceTools.formatPrice(Payable * -1));
            } else
                tvPayable.setText(ServiceTools.formatPrice(Payable));
        }
    }

    private void CalculateTotal() {
        TotalPrice = 0;
        TotalCharge = 0;
        TotalTax = 0;
        TotalOff = 0;
        FinalPrice = 0;
        Discount = 0;
        Payable = 0;
        //Read Order From Database_____________________________________________________
        Discount = order.getDiscount();
        //Read TotalReceipt___________________________________________________________
        TotalReceipt = db.getTotalReceiptFromOrder(order.getCode());
        //Read From Database ProductInOrder and Computing FinalPrice____________________
        orderDetails = db.getAllProductWithOrderDetail(order.getId());
        for (OrderDetail item : orderDetails) {
            double Price = (item.getPrice() * item.getSumCountBaJoz());
            offValue = item.getDiscount();
            TaxPercent = item.getTaxPercent();
            ChargePercent = item.getChargePercent();
            TotalCount += item.getSumCountBaJoz();
            if (item.getPrice() == 0)
                TotalCount += ServiceTools.getSumGiftCount12(item.getGiftCount1(), item.getGiftCount2(), mContext);
            PackageCount += item.getCount2();
            TotalPrice += Price;
            off = (offValue * 1);
            Price = Price - off;
            Tax = ((Price * TaxPercent) / 100);
            Charge = ((Price * ChargePercent) / 100);
            TotalCharge += Charge;
            TotalTax += Tax;
            TotalOff += off;
        }
        TotalTaxAndCharge = TotalTax + TotalCharge;
        FinalPrice = (TotalPrice - TotalOff) + (TotalTaxAndCharge);
        FinalPrice = FinalPrice - Discount;
        RemainigOrder = FinalPrice - TotalReceipt;
    }

    /**
     * ReturnOfSale FileName
     *
     * @param date
     * @return
     */
    public String GetFileName(long date) {
        return ServiceTools.getFileName(date) + order.getCode() + ".png";
    }

    public class AdapterListProduct extends ArrayAdapter<OrderDetail> {
        Activity mcontaxt;

        public AdapterListProduct(Activity contaxt, ArrayList<OrderDetail> array) {
            super(contaxt, android.R.layout.simple_list_item_1, array);
            mcontaxt = contaxt;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowview = convertView;
            Holder holder = null;
            LayoutInflater inflater = null;
            final OrderDetail product = getItem(position);

            if (rowview == null) {
                inflater = mcontaxt.getLayoutInflater();

                rowview = inflater.inflate(R.layout.lst_order_detail, null, false);

                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();

            holder.Populate(product, position);

            return rowview;
        }

        public class Holder {
            public TextView tvPrice, tvProductName, tvNumber, tvGift, tvCount, tvFee, tvOff, tvChargeAndTax, tvFinalPriceProduct, tvRowDescription;
            public LinearLayout llFinalPriceProduct;
            public LinearLayout llprop_item;
            public TextView tvProductPropSpec;

            public Holder(View view) {
                tvProductName = (TextView) view.findViewById(R.id.tvProductSpec);
                tvPrice = (TextView) view.findViewById(R.id.tvPrice);
                tvNumber = (TextView) view.findViewById(R.id.tvNumber);
                tvGift = (TextView) view.findViewById(R.id.tvGift);
                tvCount = (TextView) view.findViewById(R.id.tvCount);
                tvFee = (TextView) view.findViewById(R.id.tvFee);
                tvOff = (TextView) view.findViewById(R.id.tvOff);
                tvFinalPriceProduct = (TextView) view.findViewById(R.id.tvFinalPriceProduct);
                llFinalPriceProduct = (LinearLayout) view.findViewById(R.id.llFinalPriceProduct);
                llprop_item = (LinearLayout) view.findViewById(R.id.llprop_item);
                tvProductPropSpec = (TextView) view.findViewById(R.id.tvProductPropSpec);
                tvRowDescription = (TextView) view.findViewById(R.id.tvDescription);

                tvChargeAndTax = (TextView) view.findViewById(R.id.tvChargeAndTax);
            }

            public void Populate(OrderDetail orderDetail, int position) {

                tvRowDescription.setText(orderDetail.getDescription());

                if (OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {
                    tvProductName.setText(orderDetail.getProductName());
                    tvCount.setText(formatCount(orderDetail.getSumCountBaJoz()));
                    tvNumber.setText(String.valueOf(position + 1));
                    tvFee.setVisibility(View.INVISIBLE);
                    tvPrice.setVisibility(View.INVISIBLE);
                    tvChargeAndTax.setVisibility(View.GONE);
                    tvOff.setVisibility(View.GONE);
                    tvGift.setVisibility(View.GONE);
                    tvFinalPriceProduct.setVisibility(View.GONE);
                    llFinalPriceProduct.setVisibility(View.GONE);

                } else {

                    double Price = orderDetail.getPrice();
                    double TotalPrice = 0;
                    double finalPrice = 0;

                    double off = 0;
                    double Tax, Charge;
                    double offValue = orderDetail.getDiscount();
                    double TaxPercent = orderDetail.getTaxPercent();
                    double ChargePercent = orderDetail.getChargePercent();

                    TotalPrice = (Price * orderDetail.getSumCountBaJoz());

                    off = (offValue * 1);
                    TotalPrice = TotalPrice - off;
                    Tax = ((TotalPrice * TaxPercent) / 100);
                    Charge = ((TotalPrice * ChargePercent) / 100);
                    double TaxAndCharge = Tax + Charge;

                    finalPrice = TotalPrice + TaxAndCharge;

                    tvProductName.setText(orderDetail.getProductName());
                    tvFee.setText(ServiceTools.formatPrice(orderDetail.getPrice()));

                    tvFinalPriceProduct.setText(ServiceTools.formatPrice(finalPrice));

                    //TotalPrice == 0
                    if (orderDetail.getCount1() == 0 && orderDetail.getCount2() == 0) {
                        tvPrice.setText(R.string.str_gift);
                        tvCount.setText(formatCount(ServiceTools.getSumGiftCount12(orderDetail.getGiftCount1(), orderDetail.getGiftCount2(), mContext)));
                    } else {
                        tvPrice.setText(ServiceTools.formatPrice(TotalPrice));
                        tvCount.setText(formatCount(orderDetail.getSumCountBaJoz()));
                    }

                    ArrayList<OrderDetailProperty> orderDetailProperties = db.getAllOrderDetailProperty(orderDetail.getOrderId(), orderDetail.getProductId());
                    if (orderDetailProperties.size() > 0) {
                        llprop_item.setVisibility(View.VISIBLE);
                        StringBuilder cTitle = new StringBuilder();
                        for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
                            if (orderDetailProperty.getSumCountBaJoz() > 0) {
                                cTitle.append("\n");
                                cTitle.append(orderDetailProperty.getProductSpec());
                                cTitle.append(" : ");

                                if ((int) (orderDetailProperty.getSumCountBaJoz()) == orderDetailProperty.getSumCountBaJoz())
                                    cTitle.append(String.valueOf((int) orderDetailProperty.getSumCountBaJoz()));
                                else
                                    cTitle.append(String.valueOf(orderDetailProperty.getSumCountBaJoz()));
                                cTitle.append("\n");
                            }
                        }
                        tvProductPropSpec.setText(cTitle.toString());
                    }

                    tvNumber.setText(String.valueOf(position + 1));
                    tvOff.setText(ServiceTools.formatPrice(off));
                    tvChargeAndTax.setText(ServiceTools.formatPrice(TaxAndCharge));

                    if (order.getOrderType() == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
                        tvFee.setVisibility(View.INVISIBLE);
                        tvPrice.setVisibility(View.INVISIBLE);
                        tvChargeAndTax.setVisibility(View.GONE);
                        tvOff.setVisibility(View.GONE);
                        tvGift.setVisibility(View.GONE);
                        tvFinalPriceProduct.setVisibility(View.GONE);
                        llFinalPriceProduct.setVisibility(View.GONE);
                    }

                }

            }
        }

    }// End of AdapterListProduct

    public class AdapterListProductForPrint extends ArrayAdapter<OrderDetail> {
        Activity mcontaxt;

        public AdapterListProductForPrint(Activity contaxt, ArrayList<OrderDetail> array) {
            super(contaxt, lst_order_detail_for_print, array);
            mcontaxt = contaxt;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowview = convertView;
            Holder holder = null;
            LayoutInflater inflater = null;

            final OrderDetail product = getItem(position);

            if (rowview == null) {
                inflater = mcontaxt.getLayoutInflater();
                rowview = inflater.inflate(lst_order_detail_for_print, null, false);
                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();
            holder.Populate(product, position);
            return rowview;
        }

        public class Holder {
            public TextView tvPrice, tvProductName, tvNumber, tvGift, tvCount, tvFee, tvOff, tvChargeAndTax, tvFinalPriceProduct, tvProductPropSpec, tvProductCode;
            public LinearLayout llitem, llprop_item;
            public View view_line;

            public Holder(View view) {
                llitem = (LinearLayout) view.findViewById(R.id.llitem);
                llprop_item = (LinearLayout) view.findViewById(R.id.llprop_item);
                tvProductName = (TextView) view.findViewById(R.id.tvProductSpec);
                tvPrice = (TextView) view.findViewById(R.id.tvPrice);
                tvCount = (TextView) view.findViewById(R.id.tvCount);
                tvProductPropSpec = (TextView) view.findViewById(R.id.tvProductPropSpec);

                view_line = (View) view.findViewById(R.id.view_line);
                if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW ||
                        printerBrand == ProjectInfo.PRINTER_BABY_380_A ||
                        printerBrand == ProjectInfo.PRINTER_DELTA_380_A ||
                        printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII ||
                        printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310 || printerBrand == ProjectInfo.UROVO_K319
                ) {
                    tvFee = (TextView) view.findViewById(R.id.tvfii);
                }
                if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223 || printerBrand == ProjectInfo.SMART_POS_UROVO_i9000s) {
                    tvFee = (TextView) view.findViewById(R.id.tvfii);
                    //tvCount2 = (TextView) view.findViewById(R.id.tvCount2);
                    //tvProductCode = (TextView) view.findViewById(R.id.tvProductCode);
                }

                if (getTemplate2Status(mContext, ProjectInfo._pName_OrderDetail))
                    tvFee = (TextView) view.findViewById(R.id.tvfii);
            }

            public void Populate(OrderDetail orderDetail, int position) {

                /*float size = SharedPreferencesHelper.getCurrentFontSize(mcontaxt);
                tvProductName.setTextSize(size);
                tvPrice.setTextSize(size);
                tvCount.setTextSize(size);
                tvProductPropSpec.setTextSize(size);*/


                if (OrderType != ProjectInfo.TYPE_RETURN_OF_SALE) {

                    double off = 0;
                    double Tax = 0;
                    double Charge = 0;
                    double Price = 0;
                    double offValue = orderDetail.getDiscount();
                    double TaxPercent = orderDetail.getTaxPercent();
                    double ChargePercent = orderDetail.getChargePercent();
                    if (String.valueOf(orderDetail.getPrice()).equals(getString(R.string.str_gift)))
                        Price = 0;
                    else
                        Price = orderDetail.getPrice();

                    Price = (Price * orderDetail.getSumCountBaJoz());
                    off = (offValue * 1);
                    Price = Price - off;

                    Tax = ((Price * TaxPercent) / 100);
                    Charge = ((Price * ChargePercent) / 100);
                    double TaxAndCharge = Tax + Charge;
                    Price = Price + TaxAndCharge;

                    tvProductName.setText(orderDetail.getProductName());

                    ArrayList<OrderDetailProperty> orderDetailProperties = db.getAllOrderDetailProperty(orderDetail.getOrderId(), orderDetail.getProductId());
                    if (orderDetailProperties.size() > 0) {
                        llprop_item.setVisibility(View.VISIBLE);
                        view_line.setVisibility(View.VISIBLE);
                        StringBuilder cTitle = new StringBuilder();
                        for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
                            if (orderDetailProperty.getSumCountBaJoz() > 0) {
                                cTitle.append("\n");
                                cTitle.append(orderDetailProperty.getProductSpec());
                                cTitle.append(" : ");
                                if ((int) (orderDetailProperty.getSumCountBaJoz()) == orderDetailProperty.getSumCountBaJoz())
                                    cTitle.append(String.valueOf((int) orderDetailProperty.getSumCountBaJoz()));
                                else
                                    cTitle.append(String.valueOf(orderDetailProperty.getSumCountBaJoz()));
                                cTitle.append("\n");
                            }
                        }
                        tvProductPropSpec.setText(cTitle.toString());

                    }
                    //tvFee.setText(ServiceTools.GetMoneyFormat(orderDetail.getPrice()));
                    //price == 0
                    if ((orderDetail.getCount1() == 0 && orderDetail.getCount2() == 0)) {
                        tvPrice.setGravity(Gravity.CENTER);
                        tvPrice.setText(R.string.str_gift);
                    } else
                        tvPrice.setText(ServiceTools.formatPrice(Price));

                    /*tvFinalPriceProduct.setText(ServiceTools.formatPrice(Price)));
                    tvGift.setText(String.valueOf(orderDetail.getGiftType()));*/
                }

                if (OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
                    tvPrice.setVisibility(View.GONE);
                }

                if (orderDetail.getCount1() == 0 && orderDetail.getCount2() == 0 && orderDetail.getSumCountBaJoz() == 0)
                    tvCount.setText(formatCount(ServiceTools.getSumGiftCount12(orderDetail.getGiftCount1(), orderDetail.getGiftCount2(), mContext)));
                else if (orderDetail.getSumCountBaJoz() > 0) {
                    tvCount.setText(formatCount(orderDetail.getSumCountBaJoz()));
                }
                if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW ||
                        printerBrand == ProjectInfo.PRINTER_BABY_380_A ||
                        printerBrand == ProjectInfo.PRINTER_DELTA_380_A ||
                        printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII ||
                        printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310 || printerBrand == ProjectInfo.UROVO_K319
                ) {
                    tvFee.setText(ServiceTools.formatPrice(orderDetail.getPrice()));
                }
                if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223 || printerBrand == ProjectInfo.SMART_POS_UROVO_i9000s) {
                    //db.open();
                    //Product product = db.GetProductWithProductId(orderDetail.getProductId());
                   // tvProductCode.setText(String.valueOf(product.getProductCode()));
                    tvFee.setText(ServiceTools.formatPrice(orderDetail.getPrice()));
                }

                if (getTemplate2Status(mContext, ProjectInfo._pName_OrderDetail))
                    tvFee.setText(ServiceTools.formatPrice(orderDetail.getPrice()));
            }
        }

    }// End of AdapterListProduct

    public class AdapterGroupedTaxForPrint extends ArrayAdapter<GroupedTax> {
        Activity mcontaxt;

        public AdapterGroupedTaxForPrint(Activity contaxt, ArrayList<GroupedTax> array) {
            super(contaxt, R.layout.lst_grouped_tax_for_print, array);
            mcontaxt = contaxt;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowview = convertView;

            Holder holder = null;
            LayoutInflater inflater = null;

            final GroupedTax groupedTax = getItem(position);

            if (rowview == null) {
                inflater = mcontaxt.getLayoutInflater();
                rowview = inflater.inflate(R.layout.lst_grouped_tax_for_print, null, false);
                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();
            holder.Populate(groupedTax, position);
            return rowview;
        }

        public class Holder {
            public TextView tvTaxValue, tvPriceValue;

            public Holder(View view) {
                tvTaxValue = (TextView) view.findViewById(R.id.tvTaxValue);
                tvPriceValue = (TextView) view.findViewById(R.id.tvPriceValue);
            }

            public void Populate(GroupedTax groupedTax, int position) {
                /*float size = SharedPreferencesHelper.getCurrentFontSize(mcontaxt);
                tvTaxValue.setTextSize(size);
                tvPriceValue.setTextSize(size);*/

                tvTaxValue.setText(String.valueOf(groupedTax.getTaxPercent()) + "% MwSt:");
                tvPriceValue.setText(ServiceTools.formatPrice(groupedTax.getSumPrice()));
            }
        }

    }// End of AdapterListProduct

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (page == PAGE_DASHBORD) {
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (page == PAGE_CHECKLIST) {
                    Intent intent = new Intent(getApplicationContext(), CheckListDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (page == PAGE_ADD_ORDER) {
                    Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_ORDER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (page == PAGE_ADD_SEND_TRANSFER) {
                    Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_ORDER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (page == PAGE_ADD_INVOICE) {
                    Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_INVOCIE);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (page == PAGE_ORDERLIST)
                    finish();

                else if (page == PAGE_Invoice_Detail_Activity && OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE) {

                    Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_SEND_TRANSFERENCE);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (page == PAGE_Invoice_Detail_Activity) {

                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (page == PAGE_DASHBORD) {
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (page == PAGE_CHECKLIST) {
            Intent intent = new Intent(getApplicationContext(), CheckListDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (page == PAGE_ADD_ORDER) {
            Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
            intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_ORDER);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (page == PAGE_ADD_SEND_TRANSFER) {
            Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
            intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_ORDER);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (page == PAGE_ADD_INVOICE) {
            Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
            intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_INVOCIE);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (page == PAGE_ADD_SEND_TRANSFER) {
            Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
            intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_SEND_TRANSFERENCE);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (page == PAGE_ORDERLIST) {
            finish();
        } else if (page == PAGE_Invoice_Detail_Activity && OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE) {

            Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
            intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_SEND_TRANSFERENCE);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (page == PAGE_Invoice_Detail_Activity) {

            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (page == PAGE_Invoice_Detail_Activity) {

            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

}
