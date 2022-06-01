package com.mahak.order.goodDetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kishcore.sdk.hybrid.api.SDKManager;
import com.mahak.order.BaseActivity;
import com.mahak.order.PriceCountSelectActivity;
import com.mahak.order.R;
import com.mahak.order.common.Customer;
import com.mahak.order.common.CustomerGroup;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.OrderDetailProperty;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductDetail;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.Properties;
import com.mahak.order.common.PropertyDescription;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.SharedPreferencesHelper;
import com.mahak.order.common.VisitorProduct;
import com.mahak.order.scan.SmallCaptureActivity;
import com.mahak.order.storage.DbAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import static com.mahak.order.common.ServiceTools.formatCount;

public class GoodDetailKolJoz extends BaseActivity {

    //views
    private TextView tvPageTitle;
    private Button btnSave;
    View view = null;
    private ImageView image_arrow;

    private RecyclerView productDetailRecycler;
    private TextView propertyTitle;
    //private LinearLayout ll_detail;
    private LinearLayout ll_spec;
    //test

    String strCount = "";
    String strCount1 = "";
    String strCount2 = "";
    String strCountJoz = "";
    String strCountKol = "";
    ArrayList<ProductDetail> filterItem;

    private Set<String> propertyTitleSet = new HashSet<>();


    private KolJozAdapter adapterListProductDetail;

    public LinkedHashMap<Integer, ProductDetail> HashMap_ProductDetail = new LinkedHashMap<>();

    //helpers
    private DbAdapter db;
    private Context context;


    //variables
    private String grantedVisitorLevel;

    boolean showDetail = true;
    boolean BaMeghdarJoz = false;

    //from intent
    private Product product;
    private OrderDetail orderDetail;
    private int position;
    private int type;
    private int customerId;
    private int productId;
    private String price;
    private String countKol;
    private String countJoz;
    private String count;
    private long groupId;
    private Customer customer;
    private CustomerGroup customerGroup;
    private boolean hasDetail = false;
    private String discount;
    private int fromRecycler;
    private String description;
    boolean txtCount2Edit;
    boolean txtCountEdit;
    private double sumCountBaJoz;
    private int mode;
    private double maxValueRetail;
    private double maxValueRetail2;
    private long orderId;

    private boolean editKol = false;
    private boolean editJoz = false;
    private boolean editSum = false;

    private InputMethodManager imm;
    private double TotalCount;
    ArrayList<ProductDetail> productDetails = new ArrayList<>();
    ArrayList<VisitorProduct> visitorProducts = new ArrayList<>();

    private TextView sumAsset, tvUnit, tvUnit1_sell, tvUnitjoz_sell, tvUnitKol_sell, tvUnit1;

    static TextView txtsumCount1;
    static TextView txtsumCount2;
    static TextView txtsumCount3;

    private TextView row;
    private EditText txtSearch;
    private ArrayList<OrderDetailProperty> orderDetailProperties = new ArrayList<>();
    OrderDetailProperty orderDetailProperty = new OrderDetailProperty();

    double sumAmount1 = 0;
    double sumAmount2 = 0;
    double sumAmount3 = 0;

    double sumAsset1 = 0;
    double sumAsset2 = 0;
    double sumAsset3 = 0;
    private boolean fromFilter = false;

    private ArrayList<ProductDetail> mproductDetails = new ArrayList<>();
    private ArrayList<ProductDetail> productDetailOriginal = new ArrayList<>();
    private int printerBrand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_select3new);

        initView();
        context = this;

        printerBrand = SharedPreferencesHelper.getPrefPrinterBrand(mContext);
        if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223)
            SDKManager.init(this);


        db = new DbAdapter(this);
        db.open();
        if (getIntent() != null) {
            position = getIntent().getIntExtra("position", 0);
            price = getIntent().getStringExtra("price");
            countKol = getIntent().getStringExtra("countKol");
            countJoz = getIntent().getStringExtra("countJoz");
            count = getIntent().getStringExtra("count");
            type = getIntent().getIntExtra("type", 0);
            customerId = getIntent().getIntExtra("customerId", 0);
            productId = getIntent().getIntExtra("productId", 0);
            fromRecycler = getIntent().getIntExtra("fromRecycler", 0);
            description = getIntent().getStringExtra("description");
            mode = getIntent().getIntExtra("mode", 0);
            orderId = getIntent().getLongExtra("OrderId", 0);
        }

        product = db.GetProductWithProductId(productId);

        orderDetailProperties = new ArrayList<>();

        orderDetailProperties = db.getAllOrderDetailProperty(orderId, product.getProductId());

        changeLayoutWithConfig(product);

        if (customerId != ProjectInfo.CUSTOMERID_GUEST) {
            customer = db.getCustomerWithPersonId(customerId);
            groupId = db.GetgroupIdFromCustomer(customer);
            customerGroup = db.GetCustomerGroup(groupId);
        }

        //____config actionbar___________________________________________

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
            tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
            tvPageTitle.setText("جزییات کالا");
            getSupportActionBar().setCustomView(view);
        }
        //_______________________________________________________________

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getKolJozAdapter().getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double sumAmount1 = 0;
                double sumAmount2 = 0;
                double sumAmount3 = 0;
                db.open();
                for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
                    if (!db.UpdateOrderDetailPropertyWithOrderid(orderId, orderDetailProperty))
                        db.AddOrderDetailProperty(orderDetailProperty);
                    sumAmount1 += orderDetailProperty.getCount1();
                    sumAmount2 += orderDetailProperty.getCount2();
                    sumAmount3 += orderDetailProperty.getSumCountBaJoz();
                }

                txtsumCount1.setText(ServiceTools.formatCount(sumAmount1));
                txtsumCount2.setText(ServiceTools.formatCount(sumAmount2));
                txtsumCount3.setText(ServiceTools.formatCount(sumAmount3));

                //intent.putParcelableArrayListExtra("orderDetailProperties",orderDetailProperties);
                Intent intent = new Intent(GoodDetailKolJoz.this, PriceCountSelectActivity.class);
                intent.putExtra("count", formatCount(sumAmount1));
                intent.putExtra("count2", formatCount(sumAmount2));
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });

        tvUnit.setText("( " + product.getUnitName() + " )");
        tvUnit1.setText("( " + product.getUnitName() + " )");
        tvUnit1_sell.setText("( " + product.getUnitName() + " )");
        tvUnitjoz_sell.setText("( " + product.getUnitName() + " )");
        tvUnitKol_sell.setText("( " + product.getUnitName2() + " )");
    }

    public KolJozAdapter getKolJozAdapter() {
        return adapterListProductDetail;
    }

    private void changeLayoutWithConfig(Product product) {

        StringBuilder title = new StringBuilder();
        Gson gson = new Gson();
        Type property = new TypeToken<ArrayList<Properties>>() {
        }.getType();

        db.open();
        productDetails = db.getAllProductDetailWithProductId(product.getProductId(), type, mode);

        long orderDetailClientId = ServiceTools.toLong(ServiceTools.GenerationCodeOrderProperty());
        if (orderDetailProperties.size() == 0) {
            for (ProductDetail productDetail : productDetails) {
                orderDetailProperty = new OrderDetailProperty();
                orderDetailClientId++;
                orderDetailProperty = new OrderDetailProperty();
                orderDetailProperty.setCount1(0);
                orderDetailProperty.setCount2(0);
                orderDetailProperty.setProductDetailId(productDetail.getProductDetailId());
                orderDetailProperty.setProductId(product.getProductId());
                orderDetailProperty.setOrderId(orderId);
                orderDetailProperty.setOrderDetailClientId(orderDetailClientId);
                orderDetailProperty.setSumCountBaJoz(0);
                orderDetailProperty.setOrderDetailPropertyId(0);
                orderDetailProperty.setProductSpec(getPropertyTitle(productDetail));
                orderDetailProperties.add(orderDetailProperty);
            }
        }
        for (ProductDetail productDetail : productDetails) {
            visitorProducts.add(db.getVisitorProduct(productDetail.getProductDetailId()));
            List<Properties> propertiesList = new ArrayList<>();
            try {
                propertiesList = gson.fromJson(productDetail.getProperties(), property);
            } catch (JsonSyntaxException e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
            if (propertiesList != null) {
                if (propertiesList.size() > 0) {
                    hasDetail = true;
                    for (Properties properties : propertiesList) {
                        PropertyDescription propertyDescription = db.getPropertyDescription(properties.getCode());
                        if (!title.toString().toLowerCase().contains(propertyDescription.getTitle().toLowerCase())) {
                            title.append(propertyDescription.getTitle());
                            title.append(" - ");
                        }
                    }
                    String result = removeLast(title.toString());
                    propertyTitle.setText(result);
                }
            }
        }
        productDetailRecycler.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        productDetailRecycler.setLayoutManager(mLayoutManager);
        adapterListProductDetail = new KolJozAdapter(GoodDetailKolJoz.this, productDetails,visitorProducts, orderDetailProperties, mode, product, type);
        productDetailRecycler.setAdapter(adapterListProductDetail);
    }

    private String getPropertyTitle(ProductDetail productDetail) {
        String result = "";
        StringBuilder title = new StringBuilder();
        Gson gson = new Gson();
        Type property = new TypeToken<ArrayList<Properties>>() {
        }.getType();
        List<Properties> propertiesList = new ArrayList<>();
        try {
            propertiesList = gson.fromJson(productDetail.getProperties(), property);
        } catch (JsonSyntaxException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        if (propertiesList != null) {
            if (propertiesList.size() > 0) {
                for (Properties properties : propertiesList) {
                    if (!title.toString().toLowerCase().contains(properties.getValue().toLowerCase())) {
                        title.append(properties.getValue());
                        title.append(" - ");
                    }
                }
                result = removeLast(title.toString());
            }
        }

        return result;
    }

    public String removeLast(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ' ') {
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }

    private void initView() {

        btnSave = (Button) findViewById(R.id.btnSave);
        txtsumCount1 = (TextView) findViewById(R.id.sumCount1);
        txtsumCount2 = (TextView) findViewById(R.id.sumCount2);
        txtsumCount3 = (TextView) findViewById(R.id.sumCount3);
        sumAsset = (TextView) findViewById(R.id.sumAsset);

        tvUnit = (TextView) findViewById(R.id.tvUnit);
        tvUnit1 = (TextView) findViewById(R.id.tvUnit1);
        tvUnit1_sell = (TextView) findViewById(R.id.tvUnit1_sell);

        tvUnitjoz_sell = (TextView) findViewById(R.id.tvUnitjoz_sell);
        tvUnitKol_sell = (TextView) findViewById(R.id.tvUnitKol_sell);
        txtSearch = (EditText) findViewById(R.id.txtSearch);


        //test
        // image_arrow = (ImageView) findViewById(R.id.image_arrow);
        productDetailRecycler = (RecyclerView) findViewById(R.id.productDetailRecycler);
        propertyTitle = (TextView) findViewById(R.id.property_title);
        // ll_detail = findViewById(R.id.ll_detail);
        ll_spec = (LinearLayout) findViewById(R.id.ll_spec);
        row = (TextView) findViewById(R.id.row);
    }

    /*public void clearAllSelect3(View view) {

    }*/

    public void barcode(View view) {
        if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223) {
            try {
                SDKManager.openBarcodeScanner(true, true, objs -> {
                    String data = (String) objs[0];
                    getKolJozAdapter().getFilter().filter(data);
                }, data -> {
                    int ret = (int) data[0];
                    if (ret == SDKManager.TIMEOUT) {
                        Toast.makeText(mContext, "زمان اسکن پایان یافت.", Toast.LENGTH_SHORT).show();
                    } else if (ret == SDKManager.DEVICE_USED) {
                        Toast.makeText(mContext, "اسکنر مشغول است. لطفا مجددا تلاش نمایید.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "خطا در اسکن. لطفا مجددا تلاش نمایید.", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
        } else {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setOrientationLocked(false);
            integrator.setCaptureActivity(SmallCaptureActivity.class);
            integrator.initiateScan();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, R.string.scan_canceled, Toast.LENGTH_LONG).show();
            } else {

                getKolJozAdapter().getFilter().filter(result.getContents());
            }
        }
    }


    public void clearAllSelect3(View view) {
        for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
            orderDetailProperty.setCount1(0);
            orderDetailProperty.setCount2(0);
            orderDetailProperty.setSumCountBaJoz(0);
        }

        adapterListProductDetail = new KolJozAdapter(GoodDetailKolJoz.this, productDetails, visitorProducts, orderDetailProperties, mode, product, type);
        productDetailRecycler.setAdapter(adapterListProductDetail);
    }
}
