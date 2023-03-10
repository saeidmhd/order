package com.mahak.order;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;
import com.mahak.order.adapter.InvoicePagerAdapter;
import com.mahak.order.adapter.OrderPagerAdapter;
import com.mahak.order.adapter.ReturnPagerAdapter;
import com.mahak.order.adapter.TransferPagerAdapter;
import com.mahak.order.common.Customer;
import com.mahak.order.common.Order;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.OrderDetailProperty;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductDetail;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.Receipt;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.Visitor;
import com.mahak.order.common.VisitorProduct;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.storage.DbSchema;
import com.mahak.order.tracking.LocationService;
import com.mahak.order.widget.FontProgressDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import static com.mahak.order.common.ProjectInfo.TYPE_Delivery;
import static com.mahak.order.common.ProjectInfo.TYPE_SEND_TRANSFERENCE;
import static com.mahak.order.common.ServiceTools.getDateForLong;

import org.json.JSONObject;

@SuppressLint("ValidFragment")
public class InvoiceDetailActivity extends BaseActivity {

    /////////////////////

    private AlertDialog.Builder alertDialogBuilder;
    private static final int REQUEST_PRODUCT_LIST = 2;
    private static final int REQUEST_PAY_FACTOR = 110;

    public static ArrayList<OrderDetail> orderDetails = new ArrayList<>();
    public static ArrayList<OrderDetailProperty> orderDetailProperties = new ArrayList<>();
    public static ArrayList<OrderDetail> orderDetailArrayList = new ArrayList<>();
    public static LinkedHashSet<Integer> CommitPromoCode = new LinkedHashSet<>();

    public static int CustomerId = 0;
    public static long CustomerClientId = 0;
    public static long visitorId = 0;
    public static long GroupId = 0;
    public static long UserId = 0;
    public static String InvoiceCode = "";
    public static String Description = "";
    public static String strOrderDate = "";
    public static String strDeliveryDate = "";
    public static String CustomerName = "";
    public static String Name = "";
    public static String LastName = "";
    public static String MarketName = "";
    public static String Tell = "";
    public static String Address = "";
    public static String StrLongitude = "";
    public static String StrLatitude = "";
    public static int Mode;
    public static int Immediate = ProjectInfo.DONT_IMMEDIATE;
    public static int SettlementType = 0;
    public static int OrderType = 0;
    public static int ReasonCode = 0;
    public static double TotalCount;
    public static double TotalPrice;
    public static double percentDiscount = 0;
    public static double Discount = 0;
    public static long lngDeliveryDate;
    public static long lngOrderDate;
    private static DbAdapter db;
    private Context mContext;
    public static int orderPromotionCode = 0;
    public static int orderGiftType = 0;
    public static Button btnSave_close;
    public static TextView tvPageTitle;
    public static Customer customer;
    public static Visitor visitor;
    private Order order;
    // public static ReturnOfSale returnOfSale;
    public static long OrderId;
    private static long DeliveryId;

    public static double FinalPrice;
    private static double visitorCreditValue;
    private double customerCreditValue;
    private static final long NoLimit = -1;
    private static double mSpentCredit;
    private static double mSpentCustomerCredit;
    private static double mCurrentPrice;


    private FontProgressDialog pd;

    double Latitude = 0;
    double Longitude = 0;

    /////////////////

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<ProductDetail> productDetails;

    private final int[] tabIcons = {
            R.drawable.ic_goods,
            R.drawable.ic_base_info
    };
    private final int[] tabIconInvoices = {
            R.drawable.ic_gifts,
            R.drawable.ic_goods,
            R.drawable.ic_base_info
    };
    private int[] navLabels = {
            R.string.str_invoice_promo,
            R.string.str_invoice_goods,
            R.string.str_invoice_spec
    };
    private int[] navLabelsOrder = {
            R.string.str_order_goods,
            R.string.str_order_spec
    };
    private int[] navLabelsReturn = {
            R.string.str_return_goods,
            R.string.str_return_spec
    };

    private int[] navLabelsTransfer = {
            R.string.str_transfer_goods,
            R.string.str_transfer_spec
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail);

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        tvPageTitle.setText(getString(R.string.str_nav_promotion_list));
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________

        mContext = this;
        Activity mActivity = this;
        initialise();
        db.open();

        alertDialogBuilder = new AlertDialog.Builder(mActivity);
        OrderId = 0;

        //_________________________________________________
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            int page = extra.getInt(PAGE);
            if (OrderType == 0)
                OrderType = extra.getInt(TYPE_KEY);
            Mode = extra.getInt(MODE_PAGE);
            CustomerId = extra.getInt(CUSTOMERID_KEY, 0);
            CustomerClientId = extra.getLong(CUSTOMER_CLIENT_ID_KEY, 0);
            visitorId = extra.getLong(_Key_VisitorID, 0);
            if (Mode == MODE_NEW) {
                GroupId = extra.getLong(CUSTOMER_GROUP_KEY);
                long categoryId = extra.getLong(Category_GROUP_KEY);
            } else {
                OrderType = extra.getInt(TYPE_KEY);
                OrderId = extra.getLong(ID);
                ReadOrderFormDb(OrderId);
            }
        }
        if (OrderType == ProjectInfo.TYPE_Delivery) {
            tvPageTitle.setText(getString(R.string.delivery));
            // btnSave.setText(getString(R.string.str_conversion_invoice));
        }
        final InvoicePagerAdapter invoicePagerAdapter = new InvoicePagerAdapter(this, getSupportFragmentManager());
        final OrderPagerAdapter orderPagerAdapter = new OrderPagerAdapter(this, getSupportFragmentManager());
        final ReturnPagerAdapter returnPagerAdapter = new ReturnPagerAdapter(this, getSupportFragmentManager());
        final TransferPagerAdapter transferPagerAdapter = new TransferPagerAdapter(this, getSupportFragmentManager());
        if (OrderType == ProjectInfo.TYPE_INVOCIE || OrderType == ProjectInfo.TYPE_Delivery) {
            viewPager.setAdapter(invoicePagerAdapter);
            if (ProductPickerListActivity.HashMap_Product.size() != 0)
                viewPager.setCurrentItem(1);
            else
                viewPager.setCurrentItem(2);
        } else if (OrderType == ProjectInfo.TYPE_ORDER) {
            viewPager.setAdapter(orderPagerAdapter);
            viewPager.setCurrentItem(0);
        } else if (OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {
            viewPager.setAdapter(returnPagerAdapter);
            viewPager.setCurrentItem(0);
        } else if (OrderType == TYPE_SEND_TRANSFERENCE) {
            viewPager.setAdapter(transferPagerAdapter);
            viewPager.setCurrentItem(0);
        }
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
        setupTabIcons();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (OrderType == ProjectInfo.TYPE_INVOCIE && Mode == MODE_EDIT && position == 0) {
                    CommitPromoCode.clear();
                    for (OrderDetail orderDetail : orderDetails) {
                        if (orderDetail.getPromotionCode() != 0)
                            CommitPromoCode.add(orderDetail.getPromotionCode());
                    }
                    if (order.getPromotionCode() != 0)
                        CommitPromoCode.add(order.getPromotionCode());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (InvoiceCode.equals(""))
            InvoiceCode = ServiceTools.getGenerationCode();

        if (strDeliveryDate.equals("")) {
            lngDeliveryDate = new Date().getTime();
            strDeliveryDate = getDateForLong(mContext, lngDeliveryDate);
        }
        if (strOrderDate.equals("")) {
            lngOrderDate = new Date().getTime();
            strOrderDate = getDateForLong(mContext, lngOrderDate);
        }

        if (CustomerName.equals("")) {

            if (OrderType == TYPE_SEND_TRANSFERENCE) {
                visitor = db.getVisitor(visitorId);
                CustomerName = visitor.getName();
            } else {

                if (CustomerId != ProjectInfo.CUSTOMERID_GUEST) {
                    customer = db.getCustomerWithPersonId(CustomerId);
                    CustomerName = customer.getName();
                } else {
                    /*CustomerName = getResources().getString(R.string.str_guest_customer);*/
                    customer = db.getCustomerWithPersonClientId(CustomerClientId);
                    CustomerName = customer.getName();
                }
            }
        }
        FillView();

        strDeliveryDate = getDateForLong(mContext, new Date().getTime());

        if (Mode == MODE_EDIT && OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {
            // lngDeliveryDate = order.getOrderDate();
            strDeliveryDate = getDateForLong(mContext, lngDeliveryDate);
        }


        btnSave_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFactor();
            }
        });
    }

    private void saveFactor() {
        if(OrderType == ProjectInfo.TYPE_INVOCIE || OrderType == ProjectInfo.TYPE_ORDER){
            if (!visitorHasCredit() || !canSaveBasedOnCustomerCredit())
                SaveAndReceiptBasedOnOrder();
            else
                new AsyncSave(0, OrderType).execute();
        }else
            new AsyncSave(0, OrderType).execute();
    }

    private void SaveAndReceiptBasedOnOrder() {
            if (orderDetails.size() > 0) {
                Toast.makeText(mContext, R.string.credit_alarm, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, ManageReceiptActivity.class);
                intent.putExtra(MODE_PAGE, MODE_NEW);
                intent.putExtra(CODE_KEY, InvoiceCode);
                intent.putExtra(CUSTOMERID_KEY, CustomerId);
                intent.putExtra(CUSTOMER_CLIENT_ID_KEY, CustomerClientId);
                intent.putExtra(PAYMENT_KEY, FinalPrice);
                intent.putExtra(PAGE, PAGE_Invoice_Detail_Activity);
                startActivityForResult(intent, REQUEST_PAY_FACTOR);
            } else
                Toast.makeText(mContext, String.valueOf(getResources().getString(R.string.str_message_no_product)), Toast.LENGTH_SHORT).show();
    }

    public static boolean visitorHasCredit() {

        mSpentCredit = 0;
        long invoicesWithoutReciept = 0;

        visitor = db.getVisitor();
        ArrayList<Receipt> receiptArrayList = db.getAllReceiptNotPublished();
        ArrayList<Order> orderArrayList = db.getAllInvoiceNotPublished();

        visitorCreditValue = visitor.getTotalCredit();
        //?????? ???????????? ???????? ?????????????? ???????? ?????????????? ????????
        if (visitorCreditValue == NoLimit)
            return true;

        //?????????? ???????? ???????????? ???? ???? ?????????? ?????? ???? ????????
        double totalPriceInvoice = db.getPurePriceInvoice();

        //?????????? ???????? ?????????????? ???? ???? ?????????? ?????? ???? ????????
        double totalReceipt = db.getTotalPriceReceipt();
        //???????????? ???????? ??????
        mSpentCredit = totalPriceInvoice - totalReceipt;

        if (Mode == MODE_EDIT)
            //???????????? ???????? ?????? ???????? ???????????? ???????????? ???????? ?????? ????????????
            mSpentCredit = mSpentCredit - mCurrentPrice;

        //?????? ?????????? ???????????? ???????? ?????? ??  ???????????? ???????? ?????????? ???? ???????????? ???????? ???????? ???????? ?????? ???????????? ?????????????? ?????? ??????
        if ((FinalPrice + mSpentCredit) > visitorCreditValue) {
            double mMinimumReceipt = (FinalPrice + mSpentCredit) - visitorCreditValue;
            return false;
        }
        ///////////////////////////////
        return true;
    }
    public boolean canSaveBasedOnCustomerCredit() {
        if(CustomerId == 0 )
            return true;
        Customer customer = db.getCustomerWithPersonId(CustomerId);
        double customerCredit = customer.getCredit();
        if (customerCredit == NoLimit)
            return true;
        else {
            return customerCredit > FinalPrice - db.getTotalPriceReceiptPerInvoice(InvoiceCode);
        }
    }

    private void initialise() {

        db = new DbAdapter(mContext);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        btnSave_close = (Button) findViewById(R.id.btnSave_close);
    }

    private void FillView() {
        if (OrderType == ProjectInfo.TYPE_ORDER) {
            tvPageTitle.setText(getString(R.string.str_save_order));
            btnSave_close.setText(getString(R.string.str_save_order_close));
        } else if (OrderType == ProjectInfo.TYPE_INVOCIE || OrderType == TYPE_Delivery) {
            tvPageTitle.setText(getString(R.string.str_save_invoice));
            btnSave_close.setText(getString(R.string.str_save_invoice_close));
        } else if (OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
            tvPageTitle.setText(getString(R.string.str_save_Transference));
            btnSave_close.setText(getString(R.string.str_save_Transference_close));
        } else if (OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {
            tvPageTitle.setText(getString(R.string.str_add_return));
            btnSave_close.setText(getString(R.string.str_add_return_close));
        }
    }

    /**
     * Read Order And ProductInOrder And Fill HashMap_Product
     *
     * @param orderid
     */
    private void ReadOrderFormDb(long orderid) {
        double mGiftCount1 = 0;
        double mGiftCount2 = 0;
        int giftProductId = 0;

        order = db.GetOrder(orderid);

        /*arrayProduct = db.getAllProductWithOrderDetail(order.getId());
        orderDetailArrayList = db.getAllProductWithOrderDetail(order.getId());*/

        if (OrderType == ProjectInfo.TYPE_Delivery) {
            orderDetailArrayList = db.getAllProductWithOrderDetail(order.getOrderId());
        } else {
            orderDetailArrayList = db.getAllProductWithOrderDetail(order.getId());
        }

        InvoiceDetailActivity.orderDetails = orderDetailArrayList;

        //Fill HashMap __________________________________________________________
        ProductPickerListActivity.HashMap_Product = new LinkedHashMap<>();
        for (OrderDetail item : orderDetailArrayList) {
            mCurrentPrice = ServiceTools.getCalculateFinalPrice(item, mContext);
            item.setFinalPrice(String.valueOf(mCurrentPrice));
            ProductPickerListActivity.HashMap_Product.put(item.getProductId(), item);
        }
        //________________________________________________________
        CustomerId = order.getPersonId();
        CustomerClientId = order.getPersonClientId();

        if (CustomerId == ProjectInfo.CUSTOMERID_GUEST) {
            customer = db.getCustomerWithPersonClientId(CustomerClientId);
            CustomerName = customer.getName();
        } else {
            customer = db.getCustomerWithPersonId(CustomerId);
            CustomerName = customer.getName();
        }
        if (OrderType == TYPE_SEND_TRANSFERENCE) {
            visitor = db.getVisitor(order.getPersonId());
            CustomerName = visitor.getName();

        }
        if (order.getOrderType() == TYPE_Delivery) {
            strOrderDate = getDateForLong(mContext, order.getDeliveryDate());
            lngOrderDate = order.getDeliveryDate();
        } else {
            strOrderDate = getDateForLong(mContext, order.getOrderDate());
            lngOrderDate = order.getOrderDate();
        }

        ReasonCode = order.getReturnReasonId();

        strDeliveryDate = getDateForLong(mContext, order.getDeliveryDate());
        lngDeliveryDate = order.getDeliveryDate();

        InvoiceCode = order.getCode();
        Discount = order.getDiscount();
        percentDiscount = 0;
        SettlementType = order.getSettlementType();
        // OrderType = order.getOrderType();
        Description = order.getDescription();

        //____________________________________________

    }


    public class AsyncSave extends AsyncTask<String, String, Boolean> {
        int requestPayFactor;
        int orderType;

        public AsyncSave(int requestPayFactor , int orderType ){
            this.requestPayFactor = requestPayFactor;
            this.orderType = orderType;
        }

        @Override
        protected void onPreExecute() {
            pd = new FontProgressDialog(mContext);
            pd.setMessage(getString(R.string.storing_info));
            pd.setCancelable(false);
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {

            double mGiftCount1;
            double mGiftCount2;
            ArrayList<OrderDetailProperty> orderDetailProperties = new ArrayList<>();
            long OrderDetailClientId = ServiceTools.toLong(InvoiceCode);
            long OrderClientId = ServiceTools.toLong(InvoiceCode);
            if (db == null) db = new DbAdapter(mContext);
            db.open();
            boolean FinalResult = true;
            long res;
            order = new Order();
            Product product;
            ProductDetail productDetail;
            if (requestPayFactor == REQUEST_PAY_FACTOR)
                order.setReceiptClientId(InvoiceCode);
            if (orderType == TYPE_SEND_TRANSFERENCE)
                order.setPersonId((int) visitor.getId());
            else {
                order.setPersonClientId(customer.getPersonClientId());
                order.setPersonId(customer.getPersonId());
            }
            order.setCode(InvoiceCode);
            order.setOrderClientId(OrderClientId);
            order.setGiftType(orderGiftType);
            order.setPromotionCode(orderPromotionCode);
            order.setOrderDate(lngOrderDate);
            order.setDeliveryDate(lngDeliveryDate);
            order.setModifyDate(new Date().getTime());
            order.setDescription(Description);
            order.setMahakId(BaseActivity.getPrefMahakId());
            order.setDatabaseId(BaseActivity.getPrefDatabaseId());
            order.setVisitorId(BaseActivity.getPrefUserId());
            getLastPoint();
            order.setLatitude(Latitude);
            order.setLongitude(Longitude);

            if (Discount != 0) {
                order.setDiscount(Discount);
            } else if (percentDiscount != 0) {
                //Calculate Final Price//////////////////////////////////////////
                double finalPrice = 0;
                for (OrderDetail item : orderDetails) {
                    double result = ServiceTools.getCalculateFinalPrice(item, mContext);
                    finalPrice += result;
                }
                //////////////////////////////////////////////////////////////
                double discount = ((finalPrice * percentDiscount / 100));
                order.setDiscount(discount);
            } else
                order.setDiscount(0);

            order.setSettlementType(SettlementType);
            order.setOrderType(OrderType);
            if (OrderType == ProjectInfo.TYPE_RETURN_OF_SALE)
                order.setReturnReasonId(ReasonCode);

            //check asset for delivery
            if (order.getOrderType() == ProjectInfo.TYPE_Delivery) {
                if (orderDetails.size() > 0) {
                    for (OrderDetail item : orderDetails) {
                        productDetail = db.getProductDetail(item.getProductDetailId());
                        product = db.GetProductWithProductId(productDetail.getProductId());
                        orderDetailProperties = db.getAllOrderDetailProperty(0, product.getProductId());
                        if (!(FinalResult = checkAsset(item, orderDetailProperties))) {
                            break;
                        }

                    }
                }
            }

            if (orderDetails.size() > 0) {
                if (FinalResult) {
                    if (Mode == MODE_NEW) {
                        res = db.AddOrder(order);
                        if (res > 0) {
                            OrderId = db.getMax(DbSchema.OrderSchema.TABLE_NAME, DbSchema.OrderSchema.COLUMN_ID);
                            deleteOrderDetail(order);
                            for (OrderDetail item : orderDetails) {
                                OrderDetailClientId++;
                                productDetail = db.getProductDetail(item.getProductDetailId());
                                product = db.GetProductWithProductId(productDetail.getProductId());
                                orderDetailProperties = db.getAllOrderDetailProperty(0, product.getProductId());
                                for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
                                    if (orderDetailProperty.getOrderId() == 0) {
                                        orderDetailProperty.setOrderId(OrderId);
                                        db.UpdateOrderDetailPropertyWithOrderid(0, orderDetailProperty);
                                    }
                                }
                                if ((item.getCount1() > 0 || item.getCount2() > 0)) {
                                    item.setOrderId((int) OrderId);
                                    OrderDetailClientId++;
                                    item.setOrderDetailClientId(OrderDetailClientId);
                                    item.setOrderClientId(OrderClientId);
                                    db.AddOrderDetail(item);
                                    if (order.getOrderType() == ProjectInfo.TYPE_ORDER && getPrefReduceAsset(mContext)) {
                                        reduceAsset(item, orderDetailProperties);
                                    } else if (order.getOrderType() == ProjectInfo.TYPE_INVOCIE) {
                                        reduceAsset(item, orderDetailProperties);
                                    }
                                }
                            }
                        }
                    } else if (Mode == MODE_EDIT) {
                        if (OrderType == ProjectInfo.TYPE_Delivery) {
                            order.setOrderType(ProjectInfo.TYPE_INVOCIE);
                            db.UpdateFinalDeliveryOrder(DeliveryId);
                        }
                        order.setId(OrderId);
                        boolean result = db.UpdateOrder(order);
                        if (result) {
                            deleteOrderDetail(order);
                            for (OrderDetail item : orderDetails) {
                                productDetail = db.getProductDetail(item.getProductDetailId());
                                product = db.GetProductWithProductId(productDetail.getProductId());
                                orderDetailProperties = db.getAllOrderDetailProperty(order.getId(), product.getProductId());
                                //////////////////////////
                                if ((item.getCount1() > 0 || item.getCount2() > 0)) {
                                    OrderDetailClientId++;
                                    item.setOrderDetailClientId(OrderDetailClientId);
                                    item.setOrderId((int) order.getId());
                                    item.setOrderClientId(OrderClientId);
                                    db.AddOrderDetail(item);
                                    if (order.getOrderType() == ProjectInfo.TYPE_ORDER && getPrefReduceAsset(mContext)) {
                                        reduceAsset(item, orderDetailProperties);
                                    } else if (order.getOrderType() == ProjectInfo.TYPE_INVOCIE) {
                                        reduceAsset(item, orderDetailProperties);
                                    }
                                }
                            }//End of For
                        }//End of If
                    }
                } else {
                   // Toast.makeText(mContext, R.string.more_than_asset, Toast.LENGTH_SHORT).show();
                }
            } else {
              //  Toast.makeText(mContext, String.valueOf(getResources().getString(R.string.str_message_no_product)), Toast.LENGTH_SHORT).show();
                FinalResult = false;
            }
            //save in db___________________________________

            if (FinalResult && orderType != TYPE_SEND_TRANSFERENCE) {
             //   Toast.makeText(mContext, R.string.save_success, Toast.LENGTH_SHORT).show();
                int count = customer.getOrderCount();
                count += 1;
                customer.setOrderCount(count);
                db.UpdateCustomer(customer);
            }

            return FinalResult;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                if (OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {
                    Intent intent = new Intent(mContext, ReturnDetailActivity.class);
                    intent.putExtra(ID, OrderId);
                    intent.putExtra(PAGE, PAGE_Invoice_Detail_Activity);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_RETURN_OF_SALE);
                    startActivity(intent);
                } else if (OrderType == TYPE_SEND_TRANSFERENCE) {
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra(ID, OrderId);
                    intent.putExtra(PAGE, PAGE_Invoice_Detail_Activity);
                    intent.putExtra(TYPE_KEY, TYPE_SEND_TRANSFERENCE);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra(PAGE, PAGE_Invoice_Detail_Activity);
                    intent.putExtra(ID, OrderId);
                    startActivity(intent);
                }
                Clear();

            } else {
                Toast.makeText(mContext, R.string.no_good, Toast.LENGTH_SHORT).show();
            }

            pd.dismiss();

            super.onPostExecute(result);
        }

    }

    private void deleteOrderDetail(Order order) {
        db.open();
        ProductDetail productDetail;
        VisitorProduct visitorProduct;
        ArrayList<OrderDetail> existOrderDetailInDb = db.getAllOrderDetail(order.getId());
        for (OrderDetail orderDetail : existOrderDetailInDb) {
            productDetail = db.getProductDetail(orderDetail.getProductDetailId());
            visitorProduct = db.getVisitorProduct(orderDetail.getProductDetailId());
            Product product = db.GetProductWithProductId(productDetail.getProductId());
            ArrayList<OrderDetailProperty> orderDetailProperties = db.getAllOrderDetailProperty(order.getId(), product.getProductId());
            if (order.getOrderType() == ProjectInfo.TYPE_INVOCIE || (order.getOrderType() == ProjectInfo.TYPE_ORDER && getPrefReduceAsset(mContext))) {
                if (orderDetailProperties.size() > 0) {
                    for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {

                        visitorProduct.setCount1(ServiceTools.getExistCount1Prop(orderDetailProperty, visitorProduct));
                        visitorProduct.setCount2(ServiceTools.getExistCount2Prop(orderDetailProperty, visitorProduct));

                        db.UpdateOrAddVisitorProductFast(visitorProduct);
                    }
                    db.DeleteOrderDetailProperty(order.getId());
                }

                visitorProduct.setCount1(visitorProduct.getCount1() + orderDetail.getSumCountBaJoz());
                visitorProduct.setCount2(visitorProduct.getCount2() + orderDetail.getCount2());

                db.UpdateOrAddVisitorProductFast(visitorProduct);
            }
        }
        db.DeleteOrderDetail(order.getId());
    }

    private void reduceAsset(OrderDetail item, ArrayList<OrderDetailProperty> orderDetailProperties) {

        VisitorProduct visitorProduct;
        double remainCount1 = 0;
        double remainCount2 = 0;

        if (orderDetailProperties.size() > 0) {
            for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
                visitorProduct = db.getVisitorProduct(orderDetailProperty.getProductDetailId());
                if (orderDetailProperty.getOrderId() == 0) {
                    orderDetailProperty.setOrderId(OrderId);
                    db.UpdateOrderDetailPropertyWithOrderid(0, orderDetailProperty);
                } else
                    db.UpdateOrderDetailPropertyWithOrderid(OrderId, orderDetailProperty);

                remainCount1 = visitorProduct.getCount1() - ( orderDetailProperty.getSumCountBaJoz());
                remainCount2 = visitorProduct.getCount2() - ( orderDetailProperty.getCount2());

                visitorProduct.setCount1(remainCount1);
                visitorProduct.setCount2(remainCount2);

                db.UpdateOrAddVisitorProductFast(visitorProduct);

            }
        } else {
            visitorProduct = db.getVisitorProduct(item.getProductDetailId());
            visitorProduct.setCount1(visitorProduct.getCount1() - ( item.getSumCountBaJoz()));
            visitorProduct.setCount2(visitorProduct.getCount2() - ( item.getCount2()));
            db.UpdateOrAddVisitorProductFast(visitorProduct);
        }
    }

    private boolean checkAsset(OrderDetail item, ArrayList<OrderDetailProperty> orderDetailProperties) {

        VisitorProduct visitorProduct;
        double remainCount1 = 0;
        double remainCount2 = 0;

        if (orderDetailProperties.size() > 0) {
            for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
                visitorProduct = db.getVisitorProduct(orderDetailProperty.getProductDetailId());
                if (orderDetailProperty.getOrderId() == 0) {
                    orderDetailProperty.setOrderId(OrderId);
                    db.UpdateOrderDetailPropertyWithOrderid(0, orderDetailProperty);
                } else
                    db.UpdateOrderDetailPropertyWithOrderid(OrderId, orderDetailProperty);

                remainCount1 = visitorProduct.getCount1() - orderDetailProperty.getSumCountBaJoz();
                remainCount2 = visitorProduct.getCount2() - orderDetailProperty.getCount2();

                visitorProduct.setCount1(remainCount1);
                visitorProduct.setCount2(remainCount2);

            }
        } else {
            visitorProduct = db.getVisitorProduct(item.getProductDetailId());
            remainCount1 = visitorProduct.getCount1() - item.getSumCountBaJoz();
            remainCount2 = visitorProduct.getCount2() - item.getCount2();

            visitorProduct.setCount1(remainCount1);
            visitorProduct.setCount2(remainCount2);
        }
        if (BaseActivity.getPrefUnit2Setting(mContext) == MODE_YekVahedi)
            return remainCount1 >= 0;
        else
            return remainCount1 >= 0 && remainCount2 >= 0;

    }

    /**
     * Reset Values
     */
    private void Clear() {
        InvoiceCode = "";
        Description = "";
        strOrderDate = "";
        strDeliveryDate = "";
        CustomerName = "";
        StrLatitude = "";
        StrLongitude = "";
        Immediate = ProjectInfo.DONT_IMMEDIATE;
        SettlementType = 0;
        OrderType = 0;
        TotalCount = 0;
        Discount = 0;
        percentDiscount = 0;
        TotalPrice = 0;
        lngOrderDate = 0;
        lngDeliveryDate = 0;
        Name = "";
        LastName = "";
        MarketName = "";
        Tell = "";
        Address = "";
        CustomerId = 0;
        InvoiceDetailActivity.orderDetails.clear();
        ProductPickerListActivity.HashMap_Product.clear();
        ProductPickerListActivity.Product_Delete.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(mContext, ProductPickerListActivity.class);
                intent.putExtra(PAGE, PAGE_ORDERLIST);
                intent.putExtra(TYPE_KEY, OrderType);
                intent.putExtra(MODE_PAGE, MODE_EDIT);
                if (OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {
                    intent.putExtra(RETURN_ASSET_KEY, true);
                } else {

                    if (OrderType == ProjectInfo.TYPE_INVOCIE ||
                            (OrderType == ProjectInfo.TYPE_ORDER && getPrefReduceAsset(mContext) ||
                                    (OrderType == TYPE_SEND_TRANSFERENCE)
                            ))
                        intent.putExtra(RETURN_ASSET_KEY, true);
                    else
                        intent.putExtra(RETURN_ASSET_KEY, false);
                }

                startActivityForResult(intent, REQUEST_PRODUCT_LIST);
                break;
            case android.R.id.home:
                onBackPressedStrategy();
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PAY_FACTOR:
                    saveFactor();
                    break;
                case REQUEST_PRODUCT_LIST:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        onBackPressedStrategy();
    }

    private void onBackPressedStrategy() {

        if (orderDetails.size() > 0) {
            String alertMsg = "";
            switch (OrderType) {
                case ProjectInfo.TYPE_INVOCIE:
                    alertMsg = getResources().getString(R.string.invoice_save_alert);
                    break;
                case ProjectInfo.TYPE_ORDER:
                    alertMsg = getResources().getString(R.string.order_save_alert);
                    break;
                case TYPE_SEND_TRANSFERENCE:
                    alertMsg = getResources().getString(R.string.transfer_save_alert);
                    break;
                case ProjectInfo.TYPE_RETURN_OF_SALE:
                    alertMsg = getResources().getString(R.string.return_save_alert);
                    break;
                case ProjectInfo.TYPE_Delivery:
                    alertMsg = getResources().getString(R.string.invoice_save_alert);
                    break;
            }

            alertDialogBuilder.setMessage(alertMsg)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            saveFactor();
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            db.open();
                            db.DeleteOrderDetailProperty(0);
                            dialog.cancel();
                            Clear();
                            finish();
                        }
                    })
                    .setNeutralButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        } else {
            db.open();
            db.DeleteOrderDetailProperty(0);
            Clear();
            finish();
        }
    }

    private void setupTabIcons() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            ConstraintLayout tab = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.nav_tab, null);
            TextView tab_label = (TextView) tab.findViewById(R.id.lable_tab);
            ImageView tab_icon = (ImageView) tab.findViewById(R.id.icon_tab);
            if (OrderType == ProjectInfo.TYPE_INVOCIE || OrderType == TYPE_Delivery) {
                tab_label.setText(getResources().getString(navLabels[i]));
            } else if (OrderType == ProjectInfo.TYPE_ORDER) {
                tab_label.setText(getResources().getString(navLabelsOrder[i]));
            } else if (OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {
                tab_label.setText(getResources().getString(navLabelsReturn[i]));
            } else if (OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
                tab_label.setText(getResources().getString(navLabelsTransfer[i]));
            }
            if (OrderType == ProjectInfo.TYPE_INVOCIE || OrderType == TYPE_Delivery) {
                tab_icon.setImageResource(tabIconInvoices[i]);
            } else {
                tab_icon.setImageResource(tabIcons[i]);
            }
            tabLayout.getTabAt(i).setCustomView(tab);
        }
    }

    @Override
    protected void onResume() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    private LatLng getLastPoint(){
        LatLng latLng = null;
        LocationService locationService = new LocationService();
        JSONObject obj = locationService.getLastLocationJson(mContext);
        if (obj != null) {
            Latitude =  obj.optDouble(ProjectInfo._json_key_latitude);
            Longitude =  obj.optDouble(ProjectInfo._json_key_longitude);
        }
        return latLng;
    }

}
