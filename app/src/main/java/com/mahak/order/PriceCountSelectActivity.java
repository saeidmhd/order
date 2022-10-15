package com.mahak.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.OrderDetailProperty;
import com.mahak.order.common.PriceInputFilterMinMax;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductDetail;
import com.mahak.order.common.ProductPriceLevelName;
import com.mahak.order.common.Product_Extra_Data;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.Properties;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.Setting;
import com.mahak.order.common.TaxInSell_Extra_Data;
import com.mahak.order.common.Visitor;
import com.mahak.order.fragment.RecyclerProductAdapter;
import com.mahak.order.fragment.ReturnToRecycler;
import com.mahak.order.goodDetail.GoodDetailKolJoz;
import com.mahak.order.goodDetail.GoodDetailOneUnit;
import com.mahak.order.goodDetail.GoodDetailTwoUnit;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.widget.FontAlertDialog;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mahak.order.common.ServiceTools.MoneyFormatToNumber;
import static com.mahak.order.common.ServiceTools.formatCount;
import static com.mahak.order.common.ServiceTools.formatPrice;
import static com.mahak.order.common.ServiceTools.formatPriceWithoutSymbol;

public class PriceCountSelectActivity extends BaseActivity {

    private static final int RequestDetail = 100;
    private final ReturnToRecycler returning = new RecyclerProductAdapter();
    //views
    private TextView tvPageTitle;

    private TextView txtAveragePrice;
    private TextView txtLastBuyPrice;

    private EditText txtPrice;


    private TextView productName;
    private TextView unitName;
    private TextView unitName2;
    private EditText txtDescription;
    private EditText txtOff;
    private EditText txtOffPercent;
    private TextView txtRetailCountExist;
    private TextView txtRetailCountExistJoz;
    private TextView txtRetailCount2Exist;
    private Spinner spnPriceLevel;
    private TextView spnDetailLevel;
    private Button btnSave;
    private TextView txtCharge;
    private TextView txtTax;
    private LinearLayout llPriceLevel;
    private LinearLayout llPrice;
    private LinearLayout llTaxCharge;
    private LinearLayout llOff;
    private ImageView image_detail;

    private TextView unitRatio;

    private EditText txtCountKol;
    private EditText txtSumCount12;
    private EditText txtCountJoz;


    private EditText txtCount;

    private EditText txtCount1;
    private EditText txtCount2;

    private boolean showAvg = false;
    private boolean showLastPrice = false;
    private boolean hasRowDiscountPermission = false;


    //helpers
    private DbAdapter db;
    private Context context;

    //Objects
    private Visitor visitor;

    //variables
    private String grantedVisitorLevel;
    private int defaultVisitorPrice = 0;
    private int defaultCustomerPrice = 0;
    private int defaultCustomerGroupPrice = 0;
    private int dbPriceLevel;
    double off;
    double percentOff;
    boolean showDetail = true;
    boolean BaMeghdarJoz = false;

    //from intent
    private Product product;
    private Product_Extra_Data product_extra_data;
    private TaxInSell_Extra_Data taxInSell_extra_data;
    private OrderDetail orderDetail;
    private int position;
    private int type;
    private int customerId;
    private int productId;
    private double price;
    private String count1;
    private String count2;
    private long groupId;
    ProductDetail productDetail = new ProductDetail();
    double default_DiscountValue;
    private boolean hasDetail = false;
    private String discount = "";
    private int fromRecycler;
    private String description;
    boolean txtCount2Edit;
    boolean txtCountEdit;
    private double sumCountBaJoz;
    private int mode;
    private double maxValueRetail;
    private double maxValueRetail2;
    private long orderId;
    private ArrayList<Properties> propertiesArrayList;

    private boolean editKol = false;
    private boolean editJoz = false;
    private boolean editSum = false;

    InputMethodManager imm;
    private ArrayList<ProductDetail> productDetails;
    private ArrayList<OrderDetailProperty> orderDetailProperties = new ArrayList<>();
    private ArrayList<OrderDetailProperty> orderDetailPropertylist = new ArrayList<>();
    private double taxPercent;
    private double chargePercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //____config actionbar___________________________________________
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
            tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
            tvPageTitle.setText(R.string.register_invoice);
            getSupportActionBar().setCustomView(view);
        }
        //_______________________________________________________________

        context = this;
        db = new DbAdapter(this);
        db.open();
        if (getIntent() != null) {
            position = getIntent().getIntExtra("position", 0);
            price = ServiceTools.toDouble(getIntent().getStringExtra("price"));
            count1 = getIntent().getStringExtra("count");
            count2 = getIntent().getStringExtra("count2");
            type = getIntent().getIntExtra("type", 0);
            productId = getIntent().getIntExtra("productId", 0);
            fromRecycler = getIntent().getIntExtra("fromRecycler", 0);
            description = getIntent().getStringExtra("description");
            mode = getIntent().getIntExtra("mode", 0);
            orderId = getIntent().getLongExtra("OrderId", 0);
            orderDetailProperties = getIntent().getParcelableArrayListExtra("orderDetailProperties");
            taxPercent = getIntent().getDoubleExtra("taxPercent", 0);
            chargePercent = getIntent().getDoubleExtra("chargePercent", 0);
        }

        visitor = db.getVisitor();

        product = db.GetProductWithProductId(productId);

        product_extra_data = db.getProductExtraInfo(product.getProductCode());
        taxInSell_extra_data = db.getTaxInSellExtra(product.getProductCode());

        customerId = ProductPickerListActivity.CustomerId;
        groupId = ProductPickerListActivity.GroupId;

        ArrayList<Setting> settings = db.getAllSettingsWithVisitorId(BaseActivity.getPrefUserMasterId());

        for (Setting setting : settings) {
            if (String.valueOf(setting.getSettingCode()).contains("26501")) {
                if (setting.getValue().equals("1"))
                    showAvg = true;
            } else if (String.valueOf(setting.getSettingCode()).contains("26502")) {
                if (setting.getValue().equals("1"))
                    showLastPrice = true;
            } else if (String.valueOf(setting.getSettingCode()).contains("26503")) {
                if (setting.getValue().equals("1"))
                    hasRowDiscountPermission = true;
            }
        }

        productDetails = db.getAllProductDetailWithProductId(product.getProductId(), type, mode);

        if (orderId != 0)
            orderDetail = db.OrderdetailWithproductDetailId(orderId, product.getProductId());

        if (mode == MODE_NEW) {
            maxValueRetail = ServiceTools.getSumCount1(product.getProductId(), this);
            if (BaseActivity.getPrefUnit2Setting(mContext) != BaseActivity.MODE_YekVahedi) {
                if (ServiceTools.getSumCount2(product.getProductId(), this) > 0) {
                    maxValueRetail2 = ServiceTools.getSumCount2(product.getProductId(), this);
                } else {
                    maxValueRetail2 = maxValueRetail / product.getUnitRatio();
                }
            }
        }
        if (mode == MODE_EDIT) {
            if (productDetails.size() > 1) {
                orderDetailPropertylist = db.getAllOrderDetailProperty(orderId, product.getProductId());
                maxValueRetail = ServiceTools.getSumCount1prop(product.getProductId(), orderDetailPropertylist, this);
            } else {
                maxValueRetail = ServiceTools.getExistCount1(orderDetail, productDetails.get(0) , context);
                if (BaseActivity.getPrefUnit2Setting(mContext) != BaseActivity.MODE_YekVahedi) {
                    if (ServiceTools.getSumCount2(product.getProductId(), this) > 0) {
                        maxValueRetail2 = ServiceTools.getExistCount2(orderDetail, productDetails.get(0),mContext);
                    } else {
                        maxValueRetail2 = maxValueRetail / product.getUnitRatio();
                    }
                }
            }
        }

        setValue();
        handle_price_discount_tax_charge();

        switch (BaseActivity.getPrefUnit2Setting(context)) {
            case MODE_MeghdarJoz:
                handleJozKol();
                break;
            case Mode_DoVahedi:
                if (product.getUnitRatio() > 0)
                    handleUnitTwoFormula();
                else
                    handleUnitTwoWithoutFormula();
                break;
            case MODE_YekVahedi:
                handleUnitOne();
                break;
        }
        if (hasProductDetail(product)) {
            gotoGoodDetail();
        } else
            image_detail.setVisibility(View.GONE);

        //setInitialOff(price);

        handleMablaghi(price);
        handleDarsadi(price);

    }

    private void handleJozKol() {
        if (product.getUnitRatio() > 0)
            txtCountKol.requestFocus();
        else {
            txtCountJoz.requestFocus();
            txtCountKol.setEnabled(false);
        }

        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(txtCountKol, InputMethodManager.SHOW_FORCED);
        }


        txtCountKol.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editKol = true;
                editJoz = false;
                editSum = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editKol) {
                    if (txtCountKol.hasFocus()) {
                        try {
                            txtCountJoz.setText("0");
                            double kol = ServiceTools.toDouble(txtCountKol.getText().toString()) * product.getUnitRatio();
                            double joz = ServiceTools.toDouble(txtCountJoz.getText().toString());
                            txtSumCount12.setText(formatCount((kol + joz)));

                        } catch (NumberFormatException e) {
                            ServiceTools.logToFireBase(e);
                            txtCountKol.setError(getString(R.string.illegal));
                        }

                    }
                }
                //setOff(price);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        txtCountJoz.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editJoz = true;
                editSum = false;
                editKol = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editJoz) {
                    if (txtCountJoz.hasFocus()) {
                        try {
                            double kol = ServiceTools.toDouble(txtCountKol.getText().toString()) * product.getUnitRatio();
                            double joz = ServiceTools.toDouble(txtCountJoz.getText().toString());
                            txtSumCount12.setText(formatCount((kol + joz)));

                        } catch (NumberFormatException e) {
                            ServiceTools.logToFireBase(e);
                            txtCountJoz.setError(getString(R.string.illegal));
                        }

                    }
                }
            }
        });

        txtCountJoz.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    int kol = 0;
                    double sum = ServiceTools.RegulartoDouble(txtSumCount12.getText().toString());
                    if(product.getUnitRatio() > 0)
                        kol = (int) (sum / product.getUnitRatio());
                    double joz =  (sum - (kol * product.getUnitRatio()));

                    txtCountKol.setText(formatCount(kol));
                    txtCountJoz.setText(formatCount(joz));
                }
            }
        });

        txtSumCount12.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editKol = true;
                editJoz = false;
                editSum = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editKol) {
                    if (txtSumCount12.hasFocus()) {
                        try {
                            int kol = 0;
                            double sum = ServiceTools.RegulartoDouble(txtSumCount12.getText().toString());
                            if(product.getUnitRatio() > 0)
                                kol = (int) (sum / product.getUnitRatio());
                            double joz =  (sum - (kol * product.getUnitRatio()));

                            txtCountKol.setText(formatCount(kol));
                            txtCountJoz.setText(formatCount(joz));

                        } catch (NumberFormatException e) {
                            ServiceTools.logToFireBase(e);
                            txtCountJoz.setError(getString(R.string.illegal));
                        }

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                setOff(price);
            }
        });

        txtCountKol.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (btnSave.isEnabled())
                        returnCountPrice();
                    // DoneProductCount(productDetail);
                }
                return false;
            }
        });
    }

    private void handleUnitOne() {
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(txtCount, InputMethodManager.SHOW_FORCED);
        }
        txtCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setOff(ServiceTools.toDouble(txtPrice.getText().toString()));
            }
        });
    }

    private void handleUnitTwoWithoutFormula() {
        txtCount1.setText(count1);
        txtCount2.setText(count2);
        txtCount1.requestFocus();
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(txtCount2, InputMethodManager.SHOW_FORCED);
        }
    }

    private void handleUnitTwoFormula() {
        txtCount1.requestFocus();
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(txtCount2, InputMethodManager.SHOW_FORCED);
        }
        txtCount1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                txtCountEdit = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!txtCount2Edit) {
                    txtCountEdit = true;
                    double toDouble;
                    try {
                        btnSave.setEnabled(true);
                        toDouble = ServiceTools.toDouble(s.toString());
                        if (product.getUnitRatio() > 0) {
                            txtCount2.setText(formatCount(toDouble / product.getUnitRatio()));
                        } else
                            txtCount2.setText(ServiceTools.formatCount(0));
                    } catch (NumberFormatException e) {
                        ServiceTools.logToFireBase(e);
                        txtCount1.setError(getString(R.string.no_valid));
                        btnSave.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                setOff(price);
                txtCountEdit = false;
            }
        });
        txtCount2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                txtCount2Edit = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtCountEdit) {
                    txtCount2Edit = true;
                    double toDouble;
                    try {
                        btnSave.setEnabled(true);
                        toDouble = ServiceTools.toDouble(s.toString());
                        if (BaseActivity.getPrefUnit2Setting(mContext) == Mode_DoVahedi)
                            txtCount1.setText(ServiceTools.formatCount(toDouble * product.getUnitRatio()));
                    } catch (NumberFormatException e) {
                        ServiceTools.logToFireBase(e);
                        txtCount1.setError(getString(R.string.error_not_valid));
                        btnSave.setEnabled(false);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                txtCount2Edit = false;
            }
        });
    }

    private boolean hasProductDetail(Product product) {
        Gson gson = new Gson();
        Type property = new TypeToken<ArrayList<Properties>>() {
        }.getType();
        db.open();
        productDetails = db.getAllProductDetailWithProductId(product.getProductId(), type, mode);
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
                    return true;

                }
            }
        }
        return false;
    }

    private boolean validateNegative(int price) {
        if (((double) price * sumCountBaJoz) - off < 0) {
            txtPrice.setError(getString(R.string.negative_final_price));
            return false;
        } else
            txtPrice.setError(null);

        return true;
    }

    private void setOff(double price) {
        double d = ServiceTools.RegulartoDouble(BaseActivity.getRowDiscountType());
        switch (BaseActivity.getPrefUnit2Setting(context)) {
            case MODE_YekVahedi:
                count1 = txtCount.getText().toString();
                try {
                    sumCountBaJoz = ServiceTools.toDouble(count1);
                } catch (NumberFormatException e) {
                    ServiceTools.logToFireBase(e);
                    e.printStackTrace();
                }
                break;
            case MODE_MeghdarJoz:
                count1 = txtCountJoz.getText().toString();
                count2 = txtCountKol.getText().toString();
                try {
                    sumCountBaJoz = ServiceTools.toDouble(txtSumCount12.getText().toString());
                } catch (NumberFormatException e) {
                    ServiceTools.logToFireBase(e);
                    e.printStackTrace();
                }
                break;
            case Mode_DoVahedi:
                count1 = txtCount1.getText().toString();
                count2 = txtCount2.getText().toString();
                try {
                    sumCountBaJoz = ServiceTools.toDouble(count1);
                } catch (NumberFormatException e) {
                    ServiceTools.logToFireBase(e);
                    e.printStackTrace();
                }
                break;
        }

        if (!BaseActivity.getPrefApplyRowDiscount().equals(InActive)) {
           // default_DiscountValue = getDiscountFromDiscountLevel(productDetail.getDefaultDiscountLevel(), productDetail, price);
            if(percentOff == 0){
                percentOff = getPercentOff(productDetail.getDefaultDiscountLevel(), productDetail, price);
            }

            if (sumCountBaJoz != 0)
                off = (percentOff * price * sumCountBaJoz) / 100;
            else
                off = (percentOff * price) / 100;

            txtOffPercent.setText(String.valueOf(roundDouble(percentOff)));

          //  off = (percentOff * price * sumCountBaJoz) / 100;
            txtOff.setText(String.valueOf(roundDouble(off)));
            discount = txtOff.getText().toString();
            if (ProductPickerListActivity.HashMap_Product.size() > 0)
                if (ProductPickerListActivity.HashMap_Product.get(productId) != null)
                    ProductPickerListActivity.HashMap_Product.get(productId).setDiscount(ServiceTools.toDouble(discount));
        }
    }

    private void handleDarsadi(double price) {

        txtOffPercent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (txtOffPercent.hasFocus()) {
                    String result = s.toString();
                    if (TextUtils.isEmpty(result)) {
                        txtOff.setText(String.valueOf(roundDouble(0)));
                        percentOff = 0;
                        return;
                    }
                    if (result.contains(".")) {
                        if (result.substring(result.length() - 1).equals(".")) {
                            result = result.replace(".", "");
                            percentOff = ServiceTools.toFloat(result);
                        } else
                            percentOff = ServiceTools.toFloat(result);
                    } else
                        percentOff = ServiceTools.toFloat(result);

                    if (sumCountBaJoz != 0)
                        off = ((price * sumCountBaJoz * percentOff) / 100);
                    else
                        off = ((price * percentOff) / 100);

                    txtOff.setText(String.valueOf(roundDouble(off)));
                    discount = String.valueOf(off);
                }
            }
        });

    }

    private void handleMablaghi(double price) {

        txtOff.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (txtOff.hasFocus()) {
                    percentOff = 0;
                    String result = s.toString();
                    if (TextUtils.isEmpty(result)) {
                        txtOffPercent.setText(String.valueOf(0));
                        return;
                    }
                    if (result.contains(".")) {
                        if (result.substring(result.length() - 1).equals(".")) {
                            result = result.replace(".", "");
                            discount = result;
                        } else
                            discount = result;
                    } else
                        discount = result;

                    if (sumCountBaJoz != 0)
                        percentOff = roundDouble((ServiceTools.toDouble(discount) * 100) / (price * sumCountBaJoz));
                    else
                        percentOff = roundDouble((ServiceTools.toDouble(discount) * 100) / price);

                        txtOffPercent.setText(String.valueOf(percentOff));

                }
            }
        });

    }

    private double roundDouble(double d) {
        BigDecimal bd = null;
        try {
            bd = new BigDecimal(Double.toString(d));
        } catch (Exception e) {
            ServiceTools.logToFireBase(e);
            e.printStackTrace();
        }
        if (bd != null) {
            return bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        return 0;
    }

    private void openKeyboard(EditText editText) {
        editText.requestFocus();
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_NAVIGATE_NEXT:
                Toast.makeText(context, "next", Toast.LENGTH_SHORT).show();
        }
        return super.onKeyUp(keyCode, event);
    }

    private boolean CalculateJozKolSum() {

        double sum;
        int kol = 0;
        double joz;

        if (type == ProjectInfo.TYPE_ORDER || type == ProjectInfo.TYPE_RETURN_OF_SALE) {
            return true;
        }

        sum = ServiceTools.RegulartoDouble(txtSumCount12.getText().toString());
        if(product.getUnitRatio() > 0)
            kol = (int) (sum / product.getUnitRatio());
        joz =  (sum - (kol * product.getUnitRatio()));

        txtCountKol.setText(formatCount(kol));
        txtCountJoz.setText(formatCount(joz));
        txtSumCount12.setText(formatCount(sum));

        return (sum <= maxValueRetail);

    }

    public String removeLast(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ' ') {
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }

    private void initView() {

        productName = (TextView) findViewById(R.id.productName);
        spnPriceLevel = (Spinner) findViewById(R.id.spnPriceLevel);
        txtPrice = (EditText) findViewById(R.id.txtPrice);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        spnPriceLevel = (Spinner) findViewById(R.id.spnPriceLevel);
        btnSave = (Button) findViewById(R.id.btnSave);
        llPriceLevel = (LinearLayout) findViewById(R.id.llPriceLevel);
        llPrice = (LinearLayout) findViewById(R.id.llPrice);
        txtCharge = (TextView) findViewById(R.id.txtCharge);
        txtTax = (TextView) findViewById(R.id.txtTax);
        txtOff = (EditText) findViewById(R.id.txtOff);
        txtOffPercent = (EditText) findViewById(R.id.txtOffPercent);

        image_detail = (ImageView) findViewById(R.id.image_detail);

        txtCount = (EditText) findViewById(R.id.txtCount);

        txtAveragePrice = (TextView) findViewById(R.id.txtAveragePrice);
        txtLastBuyPrice = (TextView) findViewById(R.id.txtLastBuyPrice);

        //count12
        txtCount1 = (EditText) findViewById(R.id.txtCount1);
        txtCount2 = (EditText) findViewById(R.id.txtCount2);

        //count,count12
        unitName = (TextView) findViewById(R.id.unitName);
        txtRetailCountExist = (TextView) findViewById(R.id.txtRetailCountExist);


        //kol_joz
        txtRetailCountExistJoz = (TextView) findViewById(R.id.txtRetailCountExistJoz);
        txtSumCount12 = (EditText) findViewById(R.id.txtSumCount12);
        unitRatio = (TextView) findViewById(R.id.unitRatio);
        txtCountKol = (EditText) findViewById(R.id.txtCountKol);
        txtCountJoz = (EditText) findViewById(R.id.txtCountJoz);

        //kol_joz _count12
        unitName2 = (TextView) findViewById(R.id.unitName2);
        txtRetailCount2Exist = (TextView) findViewById(R.id.txtRetailCount2Exist);

        //
        llOff = (LinearLayout) findViewById(R.id.llOff);
        llTaxCharge = (LinearLayout) findViewById(R.id.llTaxCharge);

    }

    private void FillSpinner(Spinner spnPriceLevel) {

        if (getExistNameList().size() > 0) {
            AdapterSpnPriceLevel adspinner = new AdapterSpnPriceLevel(this, R.layout.item_spinner, getExistNameList());
            spnPriceLevel.setAdapter(adspinner);
        }
    }

    private ArrayList<ProductPriceLevelName> getExistNameList() {

        ArrayList<ProductPriceLevelName> levelNameArrayList;
        ArrayList<ProductPriceLevelName> existLevelNameList = new ArrayList<>();
        levelNameArrayList = db.getAllPriceLevelName();
        if (visitor != null)
            grantedVisitorLevel = visitor.getSelectedPriceLevels();
        if (grantedVisitorLevel != null && grantedVisitorLevel.length() > 0)
            if (grantedVisitorLevel.charAt(0) == ',') {
                grantedVisitorLevel = grantedVisitorLevel.substring(1);
            }
        //convert a string to array list
        if (grantedVisitorLevel != null) {
            List<String> myList = new ArrayList<>(Arrays.asList(grantedVisitorLevel.split(",")));

            //add exist level to existLevelNameList
            if (myList.size() > 0 && levelNameArrayList.size() > 0) {
                for (int i = 0; i < myList.size(); i++) {
                    int index = 0;
                    try {
                        index = ServiceTools.toInt(myList.get(i));
                    } catch (NumberFormatException e) {
                        ServiceTools.logToFireBase(e);
                        e.printStackTrace();
                    }
                    for (int j = 0; j < levelNameArrayList.size(); j++) {

                        if (levelNameArrayList.get(j).getPriceLevelCode() == index)
                            existLevelNameList.add(levelNameArrayList.get(j));
                    }
                }
            }
        }
        return existLevelNameList;
    }

    class AdapterSpnPriceLevel extends ArrayAdapter<ProductPriceLevelName> {

        final ArrayList<ProductPriceLevelName> Objects;

        AdapterSpnPriceLevel(Context context, int textViewResourceId, ArrayList<ProductPriceLevelName> objects) {
            super(context, textViewResourceId, objects);
            Objects = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);
            // LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View row = inflater.inflate(R.layout.item_spinner, parent, false);
            TextView tvName = (TextView) row.findViewById(R.id.tvName);
            tvName.setText(Objects.get(position).getPriceLevelName());
            return row;

        }
    }

    private double getDiscountFromDiscountLevel(int defaultLevel, ProductDetail productDetail, double price) {
        if (hasRowDiscountPermission) {
            if(ServiceTools.toDouble(discount) != 0)
                return ServiceTools.toDouble(discount);
            if (ProductPickerListActivity.HashMap_Product.get(productId) != null) {
                if (ProductPickerListActivity.HashMap_Product.size() != 0) {
                    return ProductPickerListActivity.HashMap_Product.get(productId).getDiscount();
                }
            }else {
                switch (defaultLevel) {
                    case 1:
                        return (productDetail.getDiscount1() / 100) * price;
                    case 2:
                        return (productDetail.getDiscount2() / 100) * price;
                    case 3:
                        return (productDetail.getDiscount3() / 100) * price;
                    case 4:
                        return (productDetail.getDiscount4() / 100) * price;
                }
            }
        }else  {
            switch (defaultLevel) {
                case 1:
                    return (productDetail.getDiscount1() / 100) * price;
                case 2:
                    return (productDetail.getDiscount2() / 100) * price;
                case 3:
                    return (productDetail.getDiscount3() / 100) * price;
                case 4:
                    return (productDetail.getDiscount4() / 100) * price;
            }
        }
        return 0;
    }
    private double getPercentOff(int defaultLevel, ProductDetail productDetail, double price) {
        if (hasRowDiscountPermission) {
            if(ServiceTools.toDouble(discount) != 0)
                return (ServiceTools.toDouble(discount) * 100 / (price));
            if (ProductPickerListActivity.HashMap_Product.get(productId) != null) {
                if (ProductPickerListActivity.HashMap_Product.size() != 0) {
                    return (ProductPickerListActivity.HashMap_Product.get(productId).getDiscount()) * 100 / (price * sumCountBaJoz);
                }
            }else {
                switch (defaultLevel) {
                    case 1:
                        return productDetail.getDiscount1();
                    case 2:
                        return productDetail.getDiscount2();
                    case 3:
                        return productDetail.getDiscount3();
                    case 4:
                        return productDetail.getDiscount4();
                }
            }
        }else  {
            switch (defaultLevel) {
                case 1:
                    return productDetail.getDiscount1();
                case 2:
                    return productDetail.getDiscount2();
                case 3:
                    return productDetail.getDiscount3();
                case 4:
                    return productDetail.getDiscount4();
            }
        }
        return 0;
    }

    private List<String> getlistOfgrantedcostLevel() {

        grantedVisitorLevel = visitor.getSelectedPriceLevels();
        //if first character is ',' remove this
        if (grantedVisitorLevel != null)
            try {
                if (grantedVisitorLevel.charAt(0) == ',') {
                    grantedVisitorLevel = grantedVisitorLevel.substring(1);
                }
            } catch (Exception e) {
                ServiceTools.logToFireBase(e);
                e.printStackTrace();
            }
        //convert a string to array list
        return new ArrayList<>(Arrays.asList(grantedVisitorLevel != null ? grantedVisitorLevel.split(",") : new String[0]));
    }

    private void returnCountPrice() {

        boolean cancel = false;
        View focusView = null;
        // Reset errors.
        txtPrice.setError(null);

        discount = MoneyFormatToNumber(discount);

        switch (BaseActivity.getPrefUnit2Setting(context)) {
            case MODE_YekVahedi:
                count1 = txtCount.getText().toString();
                sumCountBaJoz = ServiceTools.toDouble(count1);
                break;
            case MODE_MeghdarJoz:
                txtCountKol.setError(null);
                count1 = txtCountJoz.getText().toString();
                count2 = txtCountKol.getText().toString();
                try {
                    sumCountBaJoz = ServiceTools.toDouble(txtSumCount12.getText().toString());
                } catch (NumberFormatException e) {
                    ServiceTools.logToFireBase(e);
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(count1)) {
                    txtCountKol.setError(getString(R.string.error_field_required));
                    focusView = txtCountKol;
                    cancel = true;
                }
                break;
            case Mode_DoVahedi:
                count1 = txtCount1.getText().toString();
                count2 = txtCount2.getText().toString();
                try {
                    sumCountBaJoz = ServiceTools.toDouble(count1);
                } catch (NumberFormatException e) {
                    ServiceTools.logToFireBase(e);
                    e.printStackTrace();
                }
                break;
        }

        if (type != ProjectInfo.TYPE_RETURN_OF_SALE) {
            price = ServiceTools.toDouble(txtPrice.getText().toString());
        }

        if (count1.isEmpty())
            count1 = "0";
        if (count2 != null)
            if (count2.isEmpty())
                count2 = "0";

        if (!cancel) {
            if (fromRecycler == 1) {

                returning.return_value_recycler(
                        count2,
                        count1,
                        price,
                        position,
                        discount,
                        txtDescription.getText().toString(),
                        spnPriceLevel.getSelectedItemPosition(),
                        sumCountBaJoz,
                        productDetails.get(0),
                        orderDetailProperties);

            } else {

                Intent intent = new Intent(PriceCountSelectActivity.this, InvoiceDetailActivity.class);
                if (count2 != null)
                    intent.putExtra("count2", ServiceTools.toDouble(count2));
                if (count1 != null)
                    intent.putExtra("count", ServiceTools.toDouble(count1));
                intent.putExtra("price", price);
                intent.putExtra("position", position);
                intent.putExtra("description", txtDescription.getText().toString());
                intent.putExtra("spnPriceLevel", spnPriceLevel.getSelectedItemPosition());
                intent.putExtra("discount", discount);
                intent.putExtra("sumCountBaJoz", sumCountBaJoz);
                double d = ServiceTools.RegulartoDouble(BaseActivity.getRowDiscountType());
                intent.putExtra("discountType", (int) d);
                intent.putParcelableArrayListExtra("orderDetailProperties", orderDetailProperties);
                setResult(Activity.RESULT_OK, intent);
            }

            db.close();
        }
    }

    public void joziat(View view) {

        gotoGoodDetail();

    }

    private void gotoGoodDetail() {
        Intent intent = new Intent();
        switch (BaseActivity.getPrefUnit2Setting(mContext)) {

            case Mode_DoVahedi:
                intent = new Intent(PriceCountSelectActivity.this, GoodDetailTwoUnit.class);
                break;

            case MODE_MeghdarJoz:
                intent = new Intent(PriceCountSelectActivity.this, GoodDetailKolJoz.class);
                break;

            case MODE_YekVahedi:
                intent = new Intent(PriceCountSelectActivity.this, GoodDetailOneUnit.class);
                break;
        }
        intent.putExtra("position", position);
        intent.putExtra("price", price);
        intent.putParcelableArrayListExtra("orderDetailProperties", orderDetailProperties);

        if (BaseActivity.getPrefUnit2Setting(mContext) == MODE_MeghdarJoz) {
            intent.putExtra("countJoz", txtCountJoz.getText().toString());
            intent.putExtra("countKol", txtCountKol.getText().toString());
            intent.putExtra("count", txtSumCount12.getText().toString());

        } else if (BaseActivity.getPrefUnit2Setting(mContext) == Mode_DoVahedi) {
            intent.putExtra("countJoz", txtCount1.getText().toString());
            intent.putExtra("countKol", txtCount2.getText().toString());
        } else {
            intent.putExtra("count", txtCount.getText().toString());
        }
        intent.putExtra("type", type);
        intent.putExtra("customerId", customerId);
        intent.putExtra(CUSTOMER_GROUP_KEY, groupId);
        intent.putExtra("productId", productId);
        intent.putExtra("fromRecycler", 1);
        intent.putExtra("mode", mode);
        intent.putExtra("OrderId", orderId);
        startActivityForResult(intent, RequestDetail);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case RequestDetail:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        orderDetailProperties = new ArrayList<>();
                        count1 = data.getStringExtra("count");
                        count2 = data.getStringExtra("count2");
                        orderDetailProperties = data.getParcelableArrayListExtra("orderDetailProperties");
                        setValue();
                        handle_price_discount_tax_charge();
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {

        showHideOffTaxCharge();


        switch (BaseActivity.getPrefUnit2Setting(context)) {
            case MODE_MeghdarJoz:
                if (hasProductDetail(product)) {
                    txtSumCount12.setEnabled(false);
                    txtCountKol.setEnabled(false);
                    txtCountJoz.setEnabled(false);
                }
                break;
            case Mode_DoVahedi:
                if (hasProductDetail(product)) {
                    txtCount1.setEnabled(false);
                    txtCount2.setEnabled(false);
                }
                break;
            case MODE_YekVahedi:
                if (hasProductDetail(product)) {
                    txtCount.setEnabled(false);
                    txtDescription.requestFocus();
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
                break;
        }
        super.onResume();
    }

    private void showHideOffTaxCharge() {

        if (BaseActivity.getRowDiscountType().equals(BaseActivity.invisible))
            llOff.setVisibility(View.GONE);
        else
            llOff.setVisibility(View.VISIBLE);

        if (BaseActivity.getPrefTaxAndChargeIsActive().equals(BaseActivity.Active))
            llTaxCharge.setVisibility(View.VISIBLE);
        else
            llTaxCharge.setVisibility(View.GONE);

    }

    @SuppressLint("SetTextI18n")
    public void setValue() {

        switch (BaseActivity.getPrefUnit2Setting(context)) {
            case MODE_MeghdarJoz:
                setContentView(R.layout.activity_good_detail_kol_joz);
                initView();

                show_avg_lastBuyPrice();

                if (hasProductDetail(product)) {
                    txtSumCount12.setEnabled(false);
                    txtCountKol.setEnabled(false);
                    txtCountJoz.setEnabled(false);
                }
                txtCountJoz.setText(count1);
                txtCountKol.setText(count2);
                double kol = ServiceTools.toDouble(count2) * product.getUnitRatio();
                double joz = ServiceTools.toDouble(count1);
                txtSumCount12.setText(formatCount( (kol + joz)));

                CalculateJozKolSum();
                unitName2.setText("( " + product.getUnitName2() + " )");
                unitRatio.setText(formatCount(product.getUnitRatio()));
                txtRetailCountExistJoz.setText(getString(R.string.asset_amount) + formatCount(maxValueRetail) + " " + product.getUnitName());

                break;
            case Mode_DoVahedi:
                setContentView(R.layout.activity_good_detail_count_1_2);
                initView();

                show_avg_lastBuyPrice();

                if (hasProductDetail(product)) {
                    txtCount1.setEnabled(false);
                    txtCount2.setEnabled(false);
                }
                txtCount1.setText(formatCount(ServiceTools.toDouble(count1)));
                txtCount2.setText(formatCount(ServiceTools.toDouble(count2)));

                unitName.setText("( " + product.getUnitName() + " )");
                unitName2.setText("( " + product.getUnitName2() + " )");
                txtRetailCountExist.setText(getString(R.string.asset_amount) + formatCount(maxValueRetail) + " " + product.getUnitName());
                txtRetailCount2Exist.setText(getString(R.string.asset_amount) + formatCount(maxValueRetail2) + " " + product.getUnitName2());

                break;
            case MODE_YekVahedi:
                setContentView(R.layout.activity_good_detail_count);
                initView();

                show_avg_lastBuyPrice();

                if (hasProductDetail(product)) {
                    txtCount.setEnabled(false);
                }

                unitName.setText("( " + product.getUnitName() + " )");
                txtRetailCountExist.setText(getString(R.string.asset_amount) + formatCount(maxValueRetail) + " " + product.getUnitName());
                txtCount.setText(count1);
                txtCount.requestFocus();
                txtCount.selectAll();
                break;
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double count1_sabad_kharid = 0;
                ArrayList<OrderDetail> orderDetails =  InvoiceDetailActivity.orderDetails;
                if(orderDetails != null){
                    for (OrderDetail orderDetail : orderDetails)
                        if(orderDetail.getProductId() == product.getProductId())
                            count1_sabad_kharid += orderDetail.getCount1();
                }

                switch (BaseActivity.getPrefUnit2Setting(context)) {
                    case MODE_YekVahedi:
                        count1 = txtCount.getText().toString();
                        sumCountBaJoz = ServiceTools.toDouble(count1);
                        break;
                    case MODE_MeghdarJoz:
                        txtCountKol.setError(null);
                        count1 = txtCountJoz.getText().toString();
                        count2 = txtCountKol.getText().toString();
                        CalculateJozKolSum();
                        try {
                            sumCountBaJoz = ServiceTools.toDouble(txtSumCount12.getText().toString());
                        } catch (NumberFormatException e) {
                            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                            FirebaseCrashlytics.getInstance().recordException(e);
                            e.printStackTrace();
                        }
                        if (TextUtils.isEmpty(count1)) {
                            txtCountKol.setError(getString(R.string.error_field_required));
                        }
                        break;
                    case Mode_DoVahedi:
                        count1 = txtCount1.getText().toString();
                        count2 = txtCount2.getText().toString();
                        try {
                            sumCountBaJoz = ServiceTools.toDouble(count1);
                        } catch (NumberFormatException e) {
                            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                            FirebaseCrashlytics.getInstance().recordException(e);
                            e.printStackTrace();
                        }
                        break;
                }

                if(type == ProjectInfo.TYPE_INVOCIE && sumCountBaJoz > maxValueRetail ){
                    Dialog(getString(R.string.str_negative_asset)).show();
                    FontAlertDialog.FontDialog(Dialog(getString(R.string.str_negative_asset)));
                }else {
                    returnCountPrice();
                    finish();
                }
            }
        });

    }

    private void show_avg_lastBuyPrice() {
        if (showAvg)
            txtAveragePrice.setText(getString(R.string.average_price) + " " + ServiceTools.formatPrice(product_extra_data.getAveragePrice()));
        else
            txtAveragePrice.setVisibility(View.GONE);

        if (showLastPrice)
            txtLastBuyPrice.setText(getString(R.string.last_price_buy) + " " + ServiceTools.formatPrice(product_extra_data.getLastBuyPrice()));
        else
            txtLastBuyPrice.setVisibility(View.GONE);
    }

    public void handle_price_discount_tax_charge() {
        if (description != null)
            txtDescription.setText(description);
        if (!BaseActivity.getRowDiscountType().equals(invisible)) {
            if (!hasRowDiscountPermission) {
                txtOffPercent.setEnabled(false);
                txtOff.setEnabled(false);
            }
        }
        productName.setText(product.getName());
        //productDetail = db.getcostLevelSellById(products.get(position).getProductCode());
        if (productDetails.size() > 0)
            productDetail = db.getProductDetail(productDetails.get(0).getProductDetailId());

        if (type == ProjectInfo.TYPE_RETURN_OF_SALE || type == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
            llPriceLevel.setVisibility(View.GONE);
            llPrice.setVisibility(View.GONE);
            llTaxCharge.setVisibility(View.GONE);
        }
        if (type == ProjectInfo.TYPE_ORDER || type == ProjectInfo.TYPE_INVOCIE)
            FillSpinner(spnPriceLevel);

        //if visitor has access to change price txtPrice enable if not disable
        if (!visitor.isHasPriceAccess())
            txtPrice.setEnabled(false);
        //if visitor has access to change price level spnPriceLevel enable if not disable
        if (!visitor.isHasPriceLevelAccess())
            spnPriceLevel.setEnabled(false);

        //get default price from visitor,customer,customerGroup And product
        // with priority of 1.visitor 2.customer 3.customerGroup 4.product

        defaultVisitorPrice = db.getDefVisitorPriceLevel();
        if(customerId > 0)
            defaultCustomerPrice = db.getDefCustomerPriceLevel(customerId);
        if(groupId > 0)
            defaultCustomerGroupPrice = db.getDefGroupCustomerPriceLevel(groupId);

        // String defaultProductPrice = db.getProductDefaultPrice(products.get(position).getProductCode());

        if (productDetail != null) {
            dbPriceLevel = productDetail.getDefaultSellPriceLevel();
        }
        //set spinner selection
        if (defaultVisitorPrice != 0) {
            if (getlistOfgrantedcostLevel().indexOf(String.valueOf(defaultVisitorPrice)) < getExistNameList().size())
                spnPriceLevel.setSelection(getlistOfgrantedcostLevel().indexOf(String.valueOf(defaultVisitorPrice)));
        } else if (defaultCustomerPrice != 0) {
            if (getlistOfgrantedcostLevel().indexOf(String.valueOf(defaultCustomerPrice)) < getExistNameList().size())
                spnPriceLevel.setSelection(getlistOfgrantedcostLevel().indexOf(String.valueOf(defaultCustomerPrice)));
        } else if (defaultCustomerGroupPrice != 0) {
            if (getlistOfgrantedcostLevel().indexOf(String.valueOf(defaultCustomerGroupPrice)) < getExistNameList().size())
                spnPriceLevel.setSelection(getlistOfgrantedcostLevel().indexOf(String.valueOf(defaultCustomerGroupPrice)));
        } else if (dbPriceLevel != 0) {
            if (getlistOfgrantedcostLevel().indexOf(String.valueOf(dbPriceLevel)) < getExistNameList().size())
                spnPriceLevel.setSelection(getlistOfgrantedcostLevel().indexOf(String.valueOf(dbPriceLevel)));
        }
        spnPriceLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (productDetail != null) {

                    switch (position) {
                        case 0:
                            txtPrice.setText(formatPrice(productDetail.getPrice1()));
                            setOff(productDetail.getPrice1());
                            break;
                        case 1:
                            txtPrice.setText(formatPrice(productDetail.getPrice2()));
                            setOff(productDetail.getPrice2());

                            break;
                        case 2:
                            txtPrice.setText(formatPrice(productDetail.getPrice3()));
                            setOff(productDetail.getPrice3());

                            break;
                        case 3:
                            txtPrice.setText(formatPrice(productDetail.getPrice4()));
                            setOff(productDetail.getPrice4());

                            break;
                        case 4:
                            txtPrice.setText(formatPrice(productDetail.getPrice5()));
                            setOff(productDetail.getPrice5());

                            break;
                        case 5:
                            txtPrice.setText(formatPrice(productDetail.getPrice6()));
                            setOff(productDetail.getPrice6());

                            break;
                        case 6:
                            txtPrice.setText(formatPrice(productDetail.getPrice7()));
                            setOff(productDetail.getPrice7());

                            break;
                        case 7:
                            txtPrice.setText(formatPrice(productDetail.getPrice8()));
                            setOff(productDetail.getPrice8());

                            break;
                        case 8:
                            txtPrice.setText(formatPrice(productDetail.getPrice9()));
                            setOff(productDetail.getPrice9());

                            break;
                        case 9:
                            txtPrice.setText(formatPrice(productDetail.getPrice10()));
                            setOff(productDetail.getPrice10());

                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /////////////////////////////////////////////////////////////////////

        txtPrice.setText(formatPrice(price));
        txtOffPercent.setFilters(new InputFilter[]{new PriceInputFilterMinMax(0, 100, true)});
        setOff(price);


        if (product.getTaxPercent() == BaseActivity.TAX_EXEMPTE) {
            txtTax.setText(getString(R.string.tax_exempte));
        } else {
            txtTax.setText(formatPriceWithoutSymbol(ServiceTools.getTax(product)) + " %");
        }
        if (product.getChargePercent() == BaseActivity.CHARGE_EXEMPTE) {
            txtCharge.setText(getString(R.string.charge_exempte));
        } else {
            txtCharge.setText(formatPriceWithoutSymbol(ServiceTools.getCharge(product)) + " %");
        }
    }

    private Dialog Dialog(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(FontAlertDialog.getFontTitle(getString(R.string.str_title_message)))
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.str_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}
