package com.mahak.order.goodDetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.mahak.order.widget.DrawableClickListener;
import com.mahak.order.widget.FontEditText;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.mahak.order.common.ServiceTools.formatCount;

public class GoodDetailOneUnit extends BaseActivity {

    //views
    private TextView tvPageTitle;
    private Button btnSave;

    private RecyclerView productDetailRecycler;
    private TextView propertyTitle;
    private OneUnitAdapter adapterListProductDetail;

    //helpers
    private DbAdapter db;

    //from intent
    private Product product;

    private int customerId;
    private int productId;

    private String count;
    private long groupId;
    private Customer customer;

    private int fromRecycler;
    private int type;

    private int max;
    private int mode;
    private double maxValueRetail;
    private long orderId;

    ArrayList<ProductDetail> productDetails = new ArrayList<>();
    ArrayList<VisitorProduct> visitorProducts = new ArrayList<>();
    static TextView txtsumCount1;
    static TextView sumAsset;
    private TextView row;
    private FontEditText txtSearch;
    private ArrayList<OrderDetailProperty> orderDetailProperties = new ArrayList<>();
    OrderDetailProperty orderDetailProperty = new OrderDetailProperty();

    double sumAmount1 = 0;
    double sumAmount2 = 0;
    double sumAsset1 = 0;
    private int printerBrand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_select1new);
        initView();

        printerBrand = SharedPreferencesHelper.getPrefPrinterBrand(mContext);
        if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223)
            SDKManager.init(this);

        db = new DbAdapter(this);
        db.open();
        if (getIntent() != null) {

            count = getIntent().getStringExtra("count");

            customerId = getIntent().getIntExtra("customerId", 0);
            productId = getIntent().getIntExtra("productId", 0);
            fromRecycler = getIntent().getIntExtra("fromRecycler", 0);
            type = getIntent().getIntExtra("type", 0);
            max = getIntent().getIntExtra("max", 0);
            mode = getIntent().getIntExtra("mode", 0);
            orderId = getIntent().getLongExtra("OrderId", 0);
        }

        product = db.GetProductWithProductId(productId);

        orderDetailProperties = new ArrayList<>();

        orderDetailProperties = db.getAllOrderDetailProperty(orderId, product.getProductId());

        changeLayoutWithConfig(product);

        for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
            sumAmount1 += orderDetailProperty.getCount1();
        }
        txtsumCount1.setText(ServiceTools.formatCount(sumAmount1));

        row.setText(String.valueOf(orderDetailProperties.size()));

        if (customerId != ProjectInfo.CUSTOMERID_GUEST) {
            customer = db.getCustomerWithPersonId(customerId);
            groupId = db.GetgroupIdFromCustomer(customer);
        }

        //____config actionbar___________________________________________

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
            tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
            tvPageTitle.setText(R.string.product_property);
            getSupportActionBar().setCustomView(view);
        }
        //_______________________________________________________________

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cancel_search, 0, R.drawable.ic_search_set_nav, 0);
                getAdapterListProductDetail().getFilter().filter(s);
                row.setText(String.valueOf(orderDetailProperties.size()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    txtSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search_set_nav,0 );
                }

            }
        });

        txtSearch.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case LEFT:
                        txtSearch.setText("");
                        txtSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search_set_nav,0 );
                        break;
                    default:
                        break;
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumAmount1 = 0;
                db.open();
                for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
                    if (!db.UpdateOrderDetailPropertyWithOrderid(orderId, orderDetailProperty))
                        db.AddOrderDetailProperty(orderDetailProperty);
                    sumAmount1 += orderDetailProperty.getCount1();
                }
                Intent intent = new Intent(GoodDetailOneUnit.this, PriceCountSelectActivity.class);
                intent.putExtra("count", formatCount(sumAmount1));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    public OneUnitAdapter getAdapterListProductDetail() {
        return adapterListProductDetail;
    }

    private void changeLayoutWithConfig(Product product) {

        double sumCount1 = 0;

        StringBuilder title = new StringBuilder();
        Gson gson = new Gson();
        Type property = new TypeToken<ArrayList<Properties>>() {
        }.getType();

        db.open();
        productDetails = db.getAllProductDetailWithProductId(product.getProductId(), type, mode);

        long orderDetailClientId = ServiceTools.toLong(ServiceTools.GenerationCodeOrderProperty());

        if (orderDetailProperties.size() == 0) {
            for (ProductDetail productDetail : productDetails) {
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
            List<Properties> propertiesList = new ArrayList<>();
            try {
                propertiesList = gson.fromJson(productDetail.getProperties(), property);
            } catch (JsonSyntaxException e) {
                ServiceTools.logToFireBase(e);
                e.printStackTrace();
            }
            if (propertiesList != null) {
                if (propertiesList.size() > 0) {
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
        for (ProductDetail productDetail : productDetails) {
            VisitorProduct visitorProduct = db.getVisitorProduct(productDetail.getProductDetailId());
            visitorProducts.add(visitorProduct);
            sumCount1 += visitorProduct.getCount1();
        }
        sumAsset.setText(ServiceTools.formatCount(sumCount1));
        productDetailRecycler.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        productDetailRecycler.setLayoutManager(mLayoutManager);

        adapterListProductDetail = new OneUnitAdapter(GoodDetailOneUnit.this, productDetails, visitorProducts,orderDetailProperties, mode, type);
        /*Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider);
        productDetailRecycler.addItemDecoration(new DividerItemDecoration(dividerDrawable));*/
        productDetailRecycler.setAdapter(adapterListProductDetail);
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
        sumAsset = (TextView) findViewById(R.id.sumAsset);
        txtSearch = (FontEditText) findViewById(R.id.txtSearch);

        productDetailRecycler = (RecyclerView) findViewById(R.id.productDetailRecycler);
        propertyTitle = (TextView) findViewById(R.id.property_title);

        row = (TextView) findViewById(R.id.row);
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
            ServiceTools.logToFireBase(e);
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

    public void clearAllSelect1(View view) {
        for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
            orderDetailProperty.setCount1(0);
            orderDetailProperty.setCount2(0);
            orderDetailProperty.setSumCountBaJoz(0);
        }
        getAdapterListProductDetail().notifyDataSetChanged();

    }

    public void barcode(View view) {
        if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223) {
            SDKManager.openBarcodeScanner(true, true, objs -> {
                String data = (String) objs[0];
                getAdapterListProductDetail().getFilter().filter(data);
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

                getAdapterListProductDetail().getFilter().filter(result.getContents());
            }
        }
    }


}
