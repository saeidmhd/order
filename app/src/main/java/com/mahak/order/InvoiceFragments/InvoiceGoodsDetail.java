package com.mahak.order.InvoiceFragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kishcore.sdk.hybrid.api.SDKManager;
import com.mahak.order.BaseActivity;
import com.mahak.order.InvoiceDetailActivity;
import com.mahak.order.PriceCountSelectActivity;
import com.mahak.order.ProductPickerListActivity;
import com.mahak.order.R;
import com.mahak.order.common.Customer;
import com.mahak.order.common.CustomerGroup;
import com.mahak.order.common.GroupedTax;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.PriceInputFilterMinMax;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductDetail;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.Promotion;
import com.mahak.order.common.PromotionDetail;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.SharedPreferencesHelper;
import com.mahak.order.common.Visitor;
import com.mahak.order.interfaces.FragmentLifecycle;
import com.mahak.order.libs.SoftKeyboard;
import com.mahak.order.scan.SmallCaptureActivity;
import com.mahak.order.storage.DbAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.app.Activity.RESULT_OK;
import static com.mahak.order.BaseActivity.CUSTOMERID_KEY;
import static com.mahak.order.BaseActivity.MODE_EDIT;
import static com.mahak.order.BaseActivity.MODE_NEW;
import static com.mahak.order.BaseActivity.MODE_PAGE;
import static com.mahak.order.BaseActivity.PAGE;
import static com.mahak.order.BaseActivity.PAGE_ORDERLIST;
import static com.mahak.order.BaseActivity.RETURN_ASSET_KEY;
import static com.mahak.order.BaseActivity.TYPE_KEY;
import static com.mahak.order.BaseActivity.getPrefReduceAsset;
import static com.mahak.order.InvoiceDetailActivity.CommitPromoCode;
import static com.mahak.order.InvoiceDetailActivity.CustomerId;
import static com.mahak.order.InvoiceDetailActivity.Discount;
import static com.mahak.order.InvoiceDetailActivity.FinalPrice;
import static com.mahak.order.InvoiceDetailActivity.GroupId;
import static com.mahak.order.InvoiceDetailActivity.Mode;
import static com.mahak.order.InvoiceDetailActivity.OrderType;
import static com.mahak.order.InvoiceDetailActivity.SettlementType;
import static com.mahak.order.InvoiceDetailActivity.orderDetails;
import static com.mahak.order.InvoiceDetailActivity.orderGiftType;
import static com.mahak.order.InvoiceDetailActivity.orderPromotionCode;
import static com.mahak.order.InvoiceDetailActivity.percentDiscount;
import static com.mahak.order.common.ServiceTools.formatCount;
import static com.mahak.order.common.ServiceTools.formatPrice;
import static com.mahak.order.common.ServiceTools.formatPriceWithoutSymbol;
import static com.mahak.order.common.ServiceTools.getDateForLong;


public class InvoiceGoodsDetail extends Fragment implements FragmentLifecycle {

    private static final int REQUEST_FOR_ACTIVITY_CODE = 100;
    public static HashMap<String, Long> hashMap = new HashMap<>();
    private TextView tvTotalCount;
    private TextView tvTotalPrice;
    private TextView tvTotalOff;
    private TextView tvTotalChargeAndTax;
    private TextView tvFinalPrice;
    private ListView lstCart;
    private LinearLayout
            llTotalOff,
            llTotalChargeAndTax,
            llTotalPrice,
            llFinalPrice;
    private TextView tvCurrency;
    private TextView tvFii;
    private TextView gift;
    private EditText txtDiscount, txtDiscountPercent;
    //  private ImageView checkDiscount;
    private static AdapterListProduct adListProduct;
    private AdapterGroupedTaxForPrint _adGroupedTax;
    private DbAdapter db;
    private static Product product;
    private static ProductDetail productDetail;

    public static ScrollView goodsScroll;

    public static double promoDiscount = 0;
    public static double promoPercentDiscount = 0;

    static int howToPromotion;

    public double TotalCount;
    public double TotalCountWithoutGift;
    public double TotalWeightWithoutGift;
    public double TotalPrice;

    public OrderDetail mPromoObject;
    public TextView tvPageTitle;

    public Customer customer;
    public Visitor visitor;

    /////////////////

    private double totalTaxAndCharge;
    private double TotalOff;
    public static Activity activity;

    private static final int REQUEST_PRODUCT_LIST = 2;
    private ListView _lstGroupedTax;
    private int printerBrand;

    public InvoiceGoodsDetail() {
        // Required empty public constructor
    }

    public InvoiceGoodsDetail newInstance() {
        InvoiceGoodsDetail fragment = new InvoiceGoodsDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        db = new DbAdapter(getActivity());
        db.open();
        hashMap = db.getMapProductIdBarcode();
        clearStatics();
        FillArrayProduct();
        if ((OrderType == ProjectInfo.TYPE_INVOCIE || OrderType == ProjectInfo.TYPE_Delivery))
            CalculatePromotion();

        printerBrand = SharedPreferencesHelper.getPrefPrinterBrand(getActivity());
        if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223)
            SDKManager.init(getActivity());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_invoice_goods_detail, container, false);
        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.frmLayout);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView(v);
        FillView();
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

       /* SoftKeyboard softKeyboard;
        softKeyboard = new SoftKeyboard(linearLayout, imm);*/

       /* softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
            @Override
            public void onSoftKeyboardHide() {
            }

            @Override
            public void onSoftKeyboardShow() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                goodsScroll.pageScroll(View.FOCUS_DOWN);
                                //txtDiscount.requestFocus();
                            }
                        }, 500);
                    }
                });
            }
        });*/

        if (Mode == MODE_EDIT) {
            FillArrayProduct();
            CalculationTotal();
            setCalculateResult();
        }
        setPromoDiscountView();

        if (OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE || OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {

            llTotalPrice.setVisibility(View.GONE);
            llTotalOff.setVisibility(View.GONE);
            llFinalPrice.setVisibility(View.GONE);
            llTotalChargeAndTax.setVisibility(View.GONE);
        }


        lstCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                OrderDetail orderDetail = InvoiceDetailActivity.orderDetails.get(position);
                showCountPriceDialog(position, orderDetail.getPrice(), orderDetail.getCount1(), orderDetail.getCount2(), orderDetail.getProductId(), orderDetail.getDescription(), Mode, InvoiceDetailActivity.OrderId);
            }
        });

        txtDiscount.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                double discount;
                if (txtDiscount.hasFocus()) {
                    percentDiscount = 0;
                    String result = s.toString();
                    if (TextUtils.isEmpty(result)) {
                        txtDiscountPercent.setText(String.valueOf(0));
                        Discount = 0;
                        return;
                    }
                    if (result.contains(".")) {
                        if (result.substring(result.length() - 1).equals(".")) {
                            result = result.replace(".", "");
                            discount = ServiceTools.toDouble(result);
                        } else
                            discount = ServiceTools.toDouble(result);
                    } else
                        discount = ServiceTools.toDouble(result);

                    double final_price = (TotalPrice - TotalOff) + (totalTaxAndCharge);
                    //double final_price = (TotalPrice);
                    double calculatedPercentDiscount = (final_price * promoPercentDiscount) / 100;
                    if (promoDiscount > 0 || promoPercentDiscount > 0) {
                        Discount = promoDiscount + calculatedPercentDiscount;
                        txtDiscountPercent.setText(String.valueOf(promoPercentDiscount));
                    } else {
                        percentDiscount = roundDouble((discount * 100) / final_price);
                        txtDiscountPercent.setText(String.valueOf(percentDiscount));
                        Discount = ServiceTools.toDouble(txtDiscount.getText().toString());
                    }
                }
            }
        });

        txtDiscountPercent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (txtDiscountPercent.hasFocus()) {
                    Discount = 0;
                    String result = s.toString();
                    if (TextUtils.isEmpty(result)) {
                        txtDiscount.setText(ServiceTools.formatPrice(0));
                        percentDiscount = 0;
                        return;
                    }
                    if (result.contains(".")) {
                        if (result.substring(result.length() - 1).equals(".")) {
                            result = result.replace(".", "");
                            percentDiscount = ServiceTools.toFloat(result);
                        } else
                            percentDiscount = ServiceTools.toFloat(result);
                    } else
                        percentDiscount = ServiceTools.toFloat(result);

                    double final_price = (TotalPrice - TotalOff) + (totalTaxAndCharge);

                    double dis =  ((final_price * percentDiscount) / 100);
                    Discount = promoDiscount  + dis;

                    double calculatedPercentDiscount = (final_price * promoPercentDiscount) / 100;
                    if (promoDiscount > 0 || promoPercentDiscount > 0) {
                        Discount = promoDiscount + calculatedPercentDiscount;
                    }
                    txtDiscount.setText(String.valueOf(Discount));
                }
            }
        });
        return v;
    }

    private void clearStatics() {

        Set mapSet = ProductPickerListActivity.HashMap_Product.entrySet();
        if (Mode == MODE_NEW) {
            for (Object aMapSet : mapSet) {
                Map.Entry mapEntry = (Map.Entry) aMapSet;
                OrderDetail object = (OrderDetail) mapEntry.getValue();
                object.setGiftCount1(0);
                object.setGiftCount2(0);
            }// End of While
        }
        CommitPromoCode.clear();
        if (promoDiscount != 0 || promoPercentDiscount != 0) {
            percentDiscount = 0;
            Discount = 0;
            promoDiscount = 0;
            promoPercentDiscount = 0;
        }
    }



    private void showCountPriceDialog(int position, double price, double count, double PackageCount, int productId, String description, int mode, long orderId) {
        Intent intent = new Intent(getActivity(), PriceCountSelectActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("price", formatPrice(price));
        intent.putExtra("count", formatCount(count));
        intent.putExtra("count2", formatCount(PackageCount));
        intent.putExtra("type", OrderType);
        intent.putExtra("productId", productId);
        intent.putExtra("fromRecycler", 0);
        intent.putExtra("description", description);
        intent.putExtra("mode", mode);
        intent.putExtra("OrderId", orderId);
        startActivityForResult(intent, REQUEST_FOR_ACTIVITY_CODE);
    }

    @Override
    public void onPauseFragment() {
    }

    @Override
    public void onResumeFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_product, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuAddProduct:
                Intent intent = new Intent(getActivity(), ProductPickerListActivity.class);
                intent.putExtra(TYPE_KEY, OrderType);
                intent.putExtra(PAGE, PAGE_ORDERLIST);
                intent.putExtra(CUSTOMERID_KEY, CustomerId);
                intent.putExtra("GroupId", GroupId);
                intent.putExtra(MODE_PAGE, Mode);
                intent.putExtra("OrderId", InvoiceDetailActivity.OrderId);
                intent.putExtra(RETURN_ASSET_KEY, true);
                startActivityForResult(intent, REQUEST_PRODUCT_LIST);
                return true;
            case R.id.mnuBarcode:
                if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223) {
                    try {
                        SDKManager.openBarcodeScanner(true, true, objs -> {
                            String data = (String) objs[0];
                            handleBarcode(data);
                        }, data -> {
                            int ret = (int) data[0];
                            if (ret == SDKManager.TIMEOUT) {
                                Toast.makeText(getActivity(), "زمان اسکن پایان یافت.", Toast.LENGTH_SHORT).show();
                            } else if (ret == SDKManager.DEVICE_USED) {
                                Toast.makeText(getActivity(), "اسکنر مشغول است. لطفا مجددا تلاش نمایید.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "خطا در اسکن. لطفا مجددا تلاش نمایید.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                        FirebaseCrashlytics.getInstance().recordException(e);
                        e.printStackTrace();
                    }
                }else {
                    IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
                    intentIntegrator.setOrientationLocked(false);
                    intentIntegrator.setCaptureActivity(SmallCaptureActivity.class);
                    Intent i = intentIntegrator.createScanIntent();
                    startActivityForResult(i, IntentIntegrator.REQUEST_CODE);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PRODUCT_LIST:
                    clearStatics();
                    FillArrayProduct();
                    FillView();
                    if ((OrderType == ProjectInfo.TYPE_INVOCIE || OrderType == ProjectInfo.TYPE_Delivery))
                        CalculatePromotion();
                    setPromoDiscountView();
                    break;
                case REQUEST_FOR_ACTIVITY_CODE:
                    double count = data.getDoubleExtra("count", 0);
                    double count2 = data.getDoubleExtra("count2", 0);
                    double price = data.getDoubleExtra("price", 0);
                    int position = data.getIntExtra("position", 0);
                    String description = data.getStringExtra("description");
                    int spnPriceLevel = data.getIntExtra("spnPriceLevel", 0);
                    String discount = data.getStringExtra("discount");
                    double sumCountBaJoz = data.getDoubleExtra("sumCountBaJoz", 0);
                    int discountType = data.getIntExtra("discountType", 0);
                    setResult(count, count2, price, position, description, spnPriceLevel, discount, sumCountBaJoz, discountType);
                    break;
                case 49374:
                    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    if (result != null) {
                        if (result.getContents() == null) {
                            Log.d("MainActivity", "Cancelled scan");
                            Toast.makeText(getActivity(), "اسکن کنسل شد!", Toast.LENGTH_LONG).show();
                        } else {
                            handleBarcode(result.getContents());
                        }
                    }
                    break;


            }
        }
    }

    private void handleBarcode(String Barcode) {

        double count1 = 0;
        double count2 = 0;
        double price = 0;
        int position = -1;
        Log.v("barcode",Barcode);

        if(!Barcode.isEmpty()){
            long productId = 0;
            if(hashMap.get(Barcode) != null)
                productId = hashMap.get(Barcode);
            if(productId != 0){
                db.open();
                product = db.GetProductWithProductId(productId);
                productDetail = db.getProductDetailWithProductId(productId);

                ArrayList<OrderDetail> orderDetails = new ArrayList<>(InvoiceDetailActivity.orderDetails);
                if(orderDetails.size()>0){
                    for (int i = 0; i < orderDetails.size(); i++) {
                        if (orderDetails.get(i).getProductId() == productId)
                            position = i;
                    }
                }
                Set mapSet = ProductPickerListActivity.HashMap_Product.entrySet();
                Iterator mapIterator = mapSet.iterator();
                OrderDetail item = new OrderDetail();
                while (mapIterator.hasNext()) {
                    Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                    int keyValue = (int) mapEntry.getKey();
                    if (productId == keyValue) {
                        item = (OrderDetail) mapEntry.getValue();
                        count1 =  item.getCount1();
                        count2  = item.getCount2();
                        price = item.getPrice();
                        break;
                    }// End of if
                }
                showCountPriceDialog(position, price, count1, count2, (int) productId, item.getDescription(), MODE_NEW, item.getOrderId());
            }else
                Toast.makeText(activity, "کالایی با این بارکد تشخیص داده نشد، لطفا دوباره تلاش کنید!", Toast.LENGTH_SHORT).show();
        }
    }

    void setResult(double count, double count2, double price, int position, String description, int spnPriceLevel, String discount, double sumCountBaJoz, int discountType) {

        if(position == -1){
            if (count > 0 || count2 > 0) {
                OrderDetail object = new OrderDetail();
                object.setCount1(count);
                object.setCount2(count2);
                object.setSumCountBaJoz(sumCountBaJoz);
                if (!BaseActivity.getPrefRowDiscountIsActive().equals(BaseActivity.invisible)) {
                    double d = ServiceTools.RegulartoDouble(BaseActivity.getPrefRowDiscountIsActive());
                    object.setDiscountType((long) d);
                }
                object.setProductDetailId(productDetail.getProductDetailId());
                object.setProductId(product.getProductId());
                //object.setProductMasterId(product.getProductCode());
                object.setProductName(product.getName());
                object.setPrice(String.valueOf(price));
                object.setMin((int) product.getMin());

                if (!BaseActivity.getPrefRowDiscountIsActive().equals(BaseActivity.invisible))
                    object.setDiscount(ServiceTools.toDouble(discount));

                object.setDescription(description);
                object.setCostLevel(spnPriceLevel);

                if (BaseActivity.getPrefTaxAndChargeIsActive().equals(BaseActivity.Active))
                    object.setTaxPercent(ServiceTools.getTax(product));
                else
                    object.setTaxPercent(0);

                if (BaseActivity.getPrefTaxAndChargeIsActive().equals(BaseActivity.Active))
                    object.setChargePercent(ServiceTools.getCharge(product));
                else
                    object.setChargePercent(0);
                //////////////////////////////////////////////////////////////
                double FinalPrice = ServiceTools.getCalculateFinalPrice(object, getActivity());
                object.setFinalPrice(String.valueOf(FinalPrice));
                ProductPickerListActivity.HashMap_Product.put((int) product.getProductId(), object);
                InvoiceDetailActivity.orderDetails.add(object);
                position = InvoiceDetailActivity.orderDetails.size() - 1;
            }//End of if

        }else {
            //Set Item////////////////////////////////////////////////////////////////
            InvoiceDetailActivity.orderDetails.get(position).setDiscount(ServiceTools.toFloat(discount));
            InvoiceDetailActivity.orderDetails.get(position).setCount1(count);
            InvoiceDetailActivity.orderDetails.get(position).setCount2(count2);
            InvoiceDetailActivity.orderDetails.get(position).setSumCountBaJoz(sumCountBaJoz);
            InvoiceDetailActivity.orderDetails.get(position).setDescription(description);
            InvoiceDetailActivity.orderDetails.get(position).setDiscountType(discountType);
            InvoiceDetailActivity.orderDetails.get(position).setPrice(String.valueOf(price));
            InvoiceDetailActivity.orderDetails.get(position).setTaxPercent(InvoiceDetailActivity.orderDetails.get(position).getTaxPercent());
            InvoiceDetailActivity.orderDetails.get(position).setChargePercent(InvoiceDetailActivity.orderDetails.get(position).getChargePercent());
            double finalPrice = ServiceTools.getCalculateFinalPrice(InvoiceDetailActivity.orderDetails.get(position), getActivity());
            InvoiceDetailActivity.orderDetails.get(position).setFinalPrice(String.valueOf(finalPrice));
            InvoiceDetailActivity.orderDetails.get(position).setCostLevel(spnPriceLevel);
            //Fill HashMap_Product////////////////////////////////////////////////////////
            Set mapSet = ProductPickerListActivity.HashMap_Product.entrySet();
            for (Object aMapSet : mapSet) {
                Map.Entry mapEntry = (Map.Entry) aMapSet;
                int keyValue = (int) mapEntry.getKey();
                if (InvoiceDetailActivity.orderDetails.get(position).getProductId() == keyValue) {
                    OrderDetail object = (OrderDetail) mapEntry.getValue();
                    object.setDescription(InvoiceDetailActivity.orderDetails.get(position).getDescription());
                    object.setTaxPercent(InvoiceDetailActivity.orderDetails.get(position).getTaxPercent());
                    object.setChargePercent(InvoiceDetailActivity.orderDetails.get(position).getChargePercent());
                    object.setDiscount(InvoiceDetailActivity.orderDetails.get(position).getDiscount());
                    object.setFixedOff(InvoiceDetailActivity.orderDetails.get(position).getFixedOff());
                    ProductPickerListActivity.HashMap_Product.put(object.getProductId(), object);
                    break;
                }// End of if
            }// End of While
        }

        //Refresh Adapter And Calculate Total///////////////////////////////////////

        if ((OrderType == ProjectInfo.TYPE_INVOCIE || OrderType == ProjectInfo.TYPE_Delivery)) {
            if(InvoiceDetailActivity.orderDetails.size()>0){
                ClearZero(InvoiceDetailActivity.orderDetails.get(position));
                clearStatics();
                CalculatePromotion();
            }
        }
        setPromoDiscountView();
        adListProduct.notifyDataSetChanged();
        lstCart.setAdapter(adListProduct);
        ServiceTools.setListViewHeightBasedOnChildren(lstCart);
        ///////////////////////////////////////////////////////////////////////////
        db.close();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {
                if (OrderType == ProjectInfo.TYPE_ORDER && InvoiceDetailActivity.orderDetails.size() > 0) {
                    clearStatics();
                    ClearAllGift();
                    setPromoDiscountView();
                    adListProduct.notifyDataSetChanged();
                } else if ((OrderType == ProjectInfo.TYPE_INVOCIE || OrderType == ProjectInfo.TYPE_Delivery) && InvoiceDetailActivity.orderDetails.size() > 0) {
                    clearStatics();
                    ClearAllGift();
                    CalculatePromotion();
                    setPromoDiscountView();
                    adListProduct.notifyDataSetChanged();
                }
            }
        }
    }

    private void FillArrayProduct() {

        InvoiceDetailActivity.orderDetails = new ArrayList<>();
        Set mapSet = ProductPickerListActivity.HashMap_Product.entrySet();
        for (Object aMapSet : mapSet) {
            Map.Entry mapEntry = (Map.Entry) aMapSet;
            OrderDetail object = (OrderDetail) mapEntry.getValue();
            object.setDiscount(object.getDiscount());
            object.setFixedOff(String.valueOf(object.getFixedOff()));
            object.setCostLevel(object.getCostLevel());
            InvoiceDetailActivity.orderDetails.add(object);
        }// End of While
    }

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


    class AdapterListProduct extends ArrayAdapter<OrderDetail> {
        private final Activity mcontext;

        private int SelectedPosition;

        AdapterListProduct(Activity context, ArrayList<OrderDetail> array) {
            super(context, android.R.layout.simple_list_item_1, array);
            mcontext = context;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            View rowview = convertView;
            Holder holder;
            LayoutInflater inflater;
            final OrderDetail orderDetail = getItem(position);

            if (rowview == null) {
                inflater = mcontext.getLayoutInflater();
                rowview = inflater.inflate(R.layout.lst_cart_item, null, false);
                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();

            holder.Populate(orderDetail, position);
            holder.btnCount.setTag(R.id.ProductId, orderDetail != null ? orderDetail.getProductId() : 0);
            holder.btnCount.setTag(R.id.Product, orderDetail);
            holder.btnCount.setTag(R.id.Position, position);
            holder.txtTotalGift.setTag(R.id.ProductId, orderDetail != null ? orderDetail.getProductId() : 0);
            holder.txtTotalGift.setTag(R.id.Product, orderDetail);
            holder.txtTotalGift.setTag(R.id.Position, position);
            holder.btnDelete.setTag(orderDetail);
            if (orderDetail != null) {
                if (orderDetail.getCount1() == 0 && orderDetail.getCount2() == 0 && orderDetail.getSumCountBaJoz() == 0) {
                    holder.btnDelete.setVisibility(View.INVISIBLE);
                }
            }
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {

                @SuppressWarnings("rawtypes")
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.str_delete_item_description);
                    builder.setPositiveButton(R.string.str_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            OrderDetail obj = (OrderDetail) v.getTag();
                            if (InvoiceDetailActivity.orderDetails.size() == 1)
                                InvoiceDetailActivity.orderDetails.clear();
                            else
                                InvoiceDetailActivity.orderDetails.remove(position);
                            Set mapSet = ProductPickerListActivity.HashMap_Product.entrySet();
                            Iterator mapIterator = mapSet.iterator();
                            while (mapIterator.hasNext()) {
                                Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                                int keyValue = (int) mapEntry.getKey();
                                if (obj.getProductId() == keyValue)
                                    mapIterator.remove();
                            }// End of While
                            //Calculate Count and Final Price And Refresh Adapter////////////////////////
                            ClearAllGift();
                            if ((OrderType == ProjectInfo.TYPE_INVOCIE || OrderType == ProjectInfo.TYPE_Delivery)) {
                                clearStatics();
                                CalculatePromotion();
                            }
                            setPromoDiscountView();
                            /*adListProduct.notifyDataSetChanged();
                            lstCart.setAdapter(adListProduct);
                            ServiceTools.setListViewHeightBasedOnChildren(lstCart);*/

                            adListProduct = new AdapterListProduct(getActivity(), InvoiceDetailActivity.orderDetails);
                            lstCart.setAdapter(adListProduct);
                            ServiceTools.setListViewHeightBasedOnChildren(lstCart);

                            /*db.open();
                            ProductDetail productDetail = db.getProductDetail(obj.getProductDetailId());
                            productDetail.setCount1(obj.getMax());
                            db.UpdateProductDetail(productDetail);*/

                            /////////////////////////////////////////////////////////
                            ProductPickerListActivity.Product_Delete.add(obj);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            return rowview;
        }

        class Holder {
            final TextView tvPrice;
            final TextView tvProductName;
            final TextView tvNumber;
            final TextView tvFee;
            final TextView tvOff;
            final TextView tvDescription;
            final TextView tvChargeAndTax;
            final TextView tvFinalPriceProduct;
            final TextView btnCount;
            final TextView txtTotalGift;
            final LinearLayout btnDelete;
            final LinearLayout llDescription;
            final LinearLayout llcartDiscount;

            Holder(View view) {
                tvProductName = (TextView) view.findViewById(R.id.tvProductSpec);
                tvPrice = (TextView) view.findViewById(R.id.tvPrice);
                tvFee = (TextView) view.findViewById(R.id.tvFee);
                tvOff = (TextView) view.findViewById(R.id.tvOff);
                tvFinalPriceProduct = (TextView) view.findViewById(R.id.tvFinalPriceProduct);
                tvChargeAndTax = (TextView) view.findViewById(R.id.tvChargeAndTax);
                tvDescription = (TextView) view.findViewById(R.id.tvDescription);
                btnDelete = (LinearLayout) view.findViewById(R.id.btnDelete);
                tvNumber = (TextView) view.findViewById(R.id.tvNumber);
                btnCount = (TextView) view.findViewById(R.id.txtCountKol);
                txtTotalGift = (TextView) view.findViewById(R.id.txtTotalGift);
                llDescription = (LinearLayout) view.findViewById(R.id.llDescription);
                llcartDiscount = (LinearLayout) view.findViewById(R.id.llcartDiscount);
                tvProductName.setSelected(true);
            }

            void Populate(OrderDetail orderDetail, int position) {

                if (OrderType == ProjectInfo.TYPE_ORDER)
                    txtTotalGift.setVisibility(View.GONE);

                if (OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE || OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {

                    tvProductName.setText(orderDetail.getProductName());
                    tvFee.setVisibility(View.INVISIBLE);
                    tvPrice.setVisibility(View.INVISIBLE);
                    tvFinalPriceProduct.setVisibility(View.GONE);
                    tvNumber.setText(String.valueOf(position + 1));
                    btnCount.setText(formatCount(orderDetail.getSumCountBaJoz()));
                    txtTotalGift.setVisibility(View.GONE);
                    tvDescription.setText(orderDetail.getDescription());
                    tvOff.setVisibility(View.GONE);
                    tvChargeAndTax.setVisibility(View.GONE);
                    llcartDiscount.setVisibility(View.GONE);

                } else {

                    double TotalPrice;
                    double off = 0;
                    double Tax;
                    double Charge;
                    double offValue = orderDetail.getDiscount();
                    double TaxPercent = orderDetail.getTaxPercent();
                    double ChargePercent = orderDetail.getChargePercent();
                    double Price = orderDetail.getPrice();
                    TotalPrice = (Price * orderDetail.getSumCountBaJoz());

                    Price = (Price * orderDetail.getSumCountBaJoz());
                    off = (offValue * 1);
                    Price = Price - off;
                    Tax = ((Price * TaxPercent) / 100);
                    Charge = ((Price * ChargePercent) / 100);
                    double TaxAndCharge = Tax + Charge;
                    Price = Price + TaxAndCharge;

                    tvProductName.setText(orderDetail.getProductName());
                    tvFee.setText(ServiceTools.formatPrice(orderDetail.getPrice()));
                    tvPrice.setText(ServiceTools.formatPrice(TotalPrice));
                    tvFinalPriceProduct.setText(ServiceTools.formatPrice(Price));
                    tvNumber.setText(String.valueOf(position + 1));
                    btnCount.setText(formatCount(orderDetail.getSumCountBaJoz()));
                    txtTotalGift.setText(formatCount(ServiceTools.getSumGiftCount12(orderDetail.getGiftCount1(), orderDetail.getGiftCount2(), mcontext)));
                    tvDescription.setText(orderDetail.getDescription());
                    tvOff.setText(ServiceTools.formatPrice(off));
                    tvChargeAndTax.setText(ServiceTools.formatPrice(TaxAndCharge));
                }

                String Description = orderDetail.getDescription();
                if (Description != null) {
                    if (Description.length() > 0)
                        llDescription.setVisibility(View.VISIBLE);
                    else
                        llDescription.setVisibility(View.GONE);
                }
            }
        }

    }// End of AdapterListProduct

    private void FillView() {
        if (OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE || OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {
            tvFii.setVisibility(View.INVISIBLE);
            gift.setVisibility(View.GONE);
            tvCurrency.setVisibility(View.INVISIBLE);
        } else if (OrderType == ProjectInfo.TYPE_ORDER) {
            gift.setVisibility(View.GONE);
        }
        adListProduct = new AdapterListProduct(getActivity(), InvoiceDetailActivity.orderDetails);
        lstCart.setAdapter(adListProduct);
        ServiceTools.setListViewHeightBasedOnChildren(lstCart);
        calculateGroupedtax();
    }

    private void calculateGroupedtax() {
        if (SharedPreferencesHelper.getCurrentLanguage(getActivity()).equals("en"))
            _lstGroupedTax.setVisibility(View.GONE);
        else if (SharedPreferencesHelper.getCurrentLanguage(getActivity()).equals("de_DE")) {
            // db.open();

            ArrayList<GroupedTax> groupedTaxes = new ArrayList<>();

            HashMap<Double, List<OrderDetail>> hashMap = new HashMap<>();
            for (OrderDetail orderDetail : orderDetails) {
                if (!hashMap.containsKey(orderDetail.getTaxPercent())) {
                    List<OrderDetail> list = new ArrayList<>();
                    list.add(orderDetail);
                    hashMap.put(orderDetail.getTaxPercent(), list);
                } else {
                    hashMap.get(orderDetail.getTaxPercent()).add(orderDetail);
                }
            }

            for (Map.Entry mapElement : hashMap.entrySet()) {
                GroupedTax groupedTax = new GroupedTax();
                Double key = (Double) mapElement.getKey();
                ArrayList<OrderDetail> value = (ArrayList<OrderDetail>) mapElement.getValue();
                double taxValue = 0.0;
                for (OrderDetail orderDetail : value) {
                    double Price = orderDetail.getPrice() * orderDetail.getSumCountBaJoz();
                    Price = Price - (Price * orderDetail.getDiscount() * 1);
                    taxValue += ((Price * orderDetail.getTaxPercent()) / 100);
                }
                groupedTax.setSumPrice(taxValue);
                groupedTax.setTaxPercent(key);

                if (groupedTax.getTaxPercent() > 0)
                    groupedTaxes.add(groupedTax);

            }

            _adGroupedTax = new AdapterGroupedTaxForPrint(getActivity(), groupedTaxes);
            _lstGroupedTax.setDrawingCacheEnabled(true);
            _lstGroupedTax.setAdapter(_adGroupedTax);
            ServiceTools.setListViewHeightBasedOnChildren(_lstGroupedTax);
        }
    }

    public AdapterListProduct getAdListProduct() {
        return adListProduct;
    }

    private void initView(View v) {

        lstCart = (ListView) v.findViewById(R.id.lstCart);
        _lstGroupedTax = (ListView) v.findViewById(R.id.lstGroupedTax);

        tvTotalCount = (TextView) v.findViewById(R.id.tvTotalCount);
        tvTotalPrice = (TextView) v.findViewById(R.id.tvTotalPrice);
        tvFinalPrice = (TextView) v.findViewById(R.id.tvFinalPrice);
        tvTotalOff = (TextView) v.findViewById(R.id.tvTotalOff);
        goodsScroll = (ScrollView) v.findViewById(R.id.goodsScroll);
        tvTotalChargeAndTax = (TextView) v.findViewById(R.id.tvTotalChargeAndTax);

        tvCurrency = (TextView) v.findViewById(R.id.tvCurrency);
        tvFii = (TextView) v.findViewById(R.id.tvFii);
        gift = (TextView) (TextView) v.findViewById(R.id.gift);
        llTotalOff = (LinearLayout) v.findViewById(R.id.llTotalOff);
        llTotalChargeAndTax = (LinearLayout) v.findViewById(R.id.llTotalChargeAndTax);
        llTotalPrice = (LinearLayout) v.findViewById(R.id.llTotalPrice);
        llFinalPrice = (LinearLayout) v.findViewById(R.id.llFinalPrice);

        txtDiscount = (EditText) v.findViewById(R.id.txtDiscount);

        // checkDiscount = (ImageView) v.findViewById(R.id.checkDiscount);

        txtDiscountPercent = (EditText) v.findViewById(R.id.txtDiscountPercent);
        txtDiscountPercent.setFilters(new InputFilter[]{new PriceInputFilterMinMax(0, 100, true)});

    }

    @SuppressWarnings("rawtypes")
    private void CalculationTotal() {
        db.open();
        Product product;
        double finalDiscount = 0;
        totalTaxAndCharge = 0;
        double Price, Final, Tax, Charge;
        FinalPrice = 0;
        TotalOff = 0;
        double off = 0;
        double ChargePercent;
        double TaxPercent;
        double offValue;
        //////////////////////////////////////////////////////////////////////////////////
        Set mapSet = ProductPickerListActivity.HashMap_Product.entrySet();
        Iterator mapIterator = mapSet.iterator();
        TotalCount = 0;
        TotalCountWithoutGift = 0;
        TotalWeightWithoutGift = 0;
        TotalPrice = 0;
        while (mapIterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
            int keyValue = (int) mapEntry.getKey();
            OrderDetail orderDetail = (OrderDetail) mapEntry.getValue();
            if (keyValue == orderDetail.getProductId()) {
                product = db.GetProductWithProductId(orderDetail.getProductId());
                Price = orderDetail.getPrice();
                Final = (Price * orderDetail.getSumCountBaJoz());
                offValue = orderDetail.getDiscount();
                TaxPercent = orderDetail.getTaxPercent();
                ChargePercent = orderDetail.getChargePercent();
                off = (offValue * 1);
                Final = Final - off;

                Tax = ((Final * TaxPercent) / 100);
                Charge = ((Final * ChargePercent) / 100);
                double TaxAndCharge = Tax + Charge;

                TotalPrice += Price * orderDetail.getSumCountBaJoz();
                TotalWeightWithoutGift += product.getWeight() * orderDetail.getSumCountBaJoz();
                TotalOff += off;
                totalTaxAndCharge += TaxAndCharge;
                TotalCount += orderDetail.getSumCountBaJoz() + ServiceTools.getSumGiftCount12(orderDetail.getGiftCount1(), orderDetail.getGiftCount2(), getActivity());
                TotalCountWithoutGift += orderDetail.getSumCountBaJoz();
            }
            if (OrderType != ProjectInfo.TYPE_RETURN_OF_SALE) {
                FinalPrice = (TotalPrice - TotalOff) + (totalTaxAndCharge);
            }
        }// End of While
        if (OrderType != ProjectInfo.TYPE_RETURN_OF_SALE) {
            if (Discount > 0) {
                if (Discount > FinalPrice && FinalPrice != 0) {
                    Discount = FinalPrice;
                }
                FinalPrice = FinalPrice - Discount;
                finalDiscount = Discount;
            } else if (Discount == 0 && percentDiscount > 0) {
                double dis = ((FinalPrice * percentDiscount) / 100);
                FinalPrice = FinalPrice - dis;
                finalDiscount = dis;
            }
        }
    }

    private void setCalculateResult() {
        tvTotalCount.setText(formatCount(TotalCount));
        if (OrderType != ProjectInfo.TYPE_RETURN_OF_SALE) {
            tvTotalPrice.setText(ServiceTools.formatPrice(TotalPrice));
            tvTotalChargeAndTax.setText(ServiceTools.formatPrice(totalTaxAndCharge));
            tvTotalOff.setText(ServiceTools.formatPrice(TotalOff));

            txtDiscount.setText(ServiceTools.formatPrice(Discount));
            percentDiscount = roundDouble((Discount * 100) / (FinalPrice + Discount));
            txtDiscountPercent.setText(String.valueOf(percentDiscount));
            tvFinalPrice.setText(ServiceTools.formatPrice(FinalPrice));

            InvoiceDetailActivity.btnSave.setEnabled(OrderType != ProjectInfo.TYPE_INVOCIE || InvoiceDetailActivity.visitorHasCredit(FinalPrice));
        }
    }

    private double roundDouble(double d) {
        BigDecimal bd = null;
        try {
            bd = new BigDecimal(Double.toString(d));
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        if (bd != null) {
            return bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        return 0;
    }

    private int roundDoubleToInt(double d) {
        BigDecimal bd = null;
        try {
            bd = new BigDecimal(Double.toString(d));
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        if (bd != null) {
            return bd.setScale(2, BigDecimal.ROUND_HALF_UP).intValue();
        }
        return 0;
    }

    public void CalculatePromotion() {

        CommitPromoCode.clear();
        CalculationTotal();
        mPromoObject = new OrderDetail();
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        if (ProductPickerListActivity.HashMap_Product.size() != 0) {
            Set mapSet = ProductPickerListActivity.HashMap_Product.entrySet();
            for (Object aMapSet : mapSet) {
                Map.Entry mapEntry = (Map.Entry) aMapSet;
                OrderDetail object = (OrderDetail) mapEntry.getValue();
                orderDetails.add(object);
            }// End of While
            validPromotionList(orderDetails);
        }
    }

    public void validPromotionList(ArrayList<OrderDetail> orderDetails) {

        int mPromoCode;
        int zarib = 1;
        ArrayList<PromotionDetail> arrayPromotionDetail;
        int mGiftCount1 = 0;
        int mGiftCount2 = 0;
        int KalaCode;
        int fixedOff;
        double offPercent;
        double fixedDiscount;
        double percentDiscount;
        orderPromotionCode = 0;
        orderGiftType = 0;
        howToPromotion = 0;
        CommitPromoCode.clear();
        int jameTedadAghlam = 0;
        ArrayList<Promotion> promotions;
        ProductDetail productDetail;
        Product product;
        String InvoiceDate = getDateForLong(new Date().getTime());
        if (db == null) db = new DbAdapter(getActivity());
        db.open();
        Customer customer = db.getCustomerWithPersonId(CustomerId);
        promotions = promotionCanBeApplied(InvoiceDate, customer);
        if (promotions != null) {
            if (promotions.size() > 0) {
                for (Promotion promotion : promotions) {
                    if (promotion.getTypeTasvieh() == 0 || (promotion.getTypeTasvieh() != 0 && promotion.getTypeTasvieh() == SettlementType + 1)) {
                        switch (promotion.getAccordingTo()) {
                            case Promotion.Mablaghe_kole_Faktor:
                                double final_price = (TotalPrice - TotalOff) + (totalTaxAndCharge);
                                arrayPromotionDetail = db.getPromotionDetails(promotion.getPromotionCode(), final_price);
                                if (arrayPromotionDetail.size() > 0) {
                                    switch (arrayPromotionDetail.get(0).getHowToPromotion()) {
                                        case Promotion.takhfif_mablaghe_sabet:
                                            if (arrayPromotionDetail.get(0).getIsCalcAdditive() == 1)
                                                zarib = roundDoubleToInt(final_price / arrayPromotionDetail.get(0).getToPayment());
                                            fixedDiscount = zarib * arrayPromotionDetail.get(0).getMeghdarPromotion();
                                            howToPromotion = 3;
                                            mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                            CommitPromoCode.add(mPromoCode);
                                            wholeFactorFixedDiscount(fixedDiscount, mPromoCode);
                                            break;
                                        case Promotion.takhfif_darsadi:
                                            if (promotion.getIsCalcLinear() == 1)
                                                percentDiscount = arrayPromotionDetail.get(0).getMeghdarPromotion();
                                            else {
                                                double mStairOff = calculateStairOff(arrayPromotionDetail, (final_price));
                                                percentDiscount = ((mStairOff * 100) / (final_price));
                                            }
                                            howToPromotion = 3;
                                            mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                            CommitPromoCode.add(mPromoCode);
                                            wholeFactorPercentDiscount(percentDiscount, mPromoCode);
                                            break;
                                        case Promotion.takhfif_Az_Sotooh:
                                            for (OrderDetail orderDetail : orderDetails) {
                                                productDetail = db.getProductDetail(orderDetail.getProductDetailId());
                                                product = db.GetProductWithProductId(productDetail.getProductId());
                                                offPercent = getDiscountFromDiscountLevel(arrayPromotionDetail.get(0).getMeghdarPromotion(), productDetail);
                                                howToPromotion = 3;
                                                mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                                CommitPromoCode.add(mPromoCode);
                                                rowPercentOff(offPercent, mPromoCode, product);
                                            }
                                            break;
                                        case Promotion.eshantion_Az_hamanKala:
                                            //gift az haman kala agar be andaze gift kala mojood bashad.
                                            for (OrderDetail orderDetail : orderDetails) {
                                                productDetail = db.getProductDetail(orderDetail.getProductDetailId());
                                                product = db.GetProductWithProductId(productDetail.getProductId());
                                                KalaCode = (int) (product.getProductCode());
                                                if (arrayPromotionDetail.get(0).getIsCalcAdditive() == 1)
                                                    zarib = (int) (final_price / arrayPromotionDetail.get(0).getToPayment());
                                                mGiftCount1 = ((int) arrayPromotionDetail.get(0).getMeghdar() * zarib);
                                                mGiftCount2 = ((int) arrayPromotionDetail.get(0).getMeghdar2() * zarib);
                                                mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                                howToPromotion = 2;
                                                CommitPromoCode.add(mPromoCode);
                                                giftFromSameProduct(mGiftCount1, mGiftCount2, KalaCode, mPromoCode, productDetail);
                                            }
                                            break;
                                        case Promotion.eshantion_Az_kalahaye_digar:
                                            //gift az kalaye digar.
                                            KalaCode = (arrayPromotionDetail.get(0).getCodeGood());
                                            Product product1 = db.getProductWithProductCode(KalaCode);
                                            productDetail = db.getProductDetailWithProductId(product1.getProductId());
                                            if (arrayPromotionDetail.get(0).getIsCalcAdditive() == 1)
                                                zarib = (int) (final_price / arrayPromotionDetail.get(0).getToPayment());
                                            mGiftCount1 = ((int) arrayPromotionDetail.get(0).getMeghdar() * zarib);
                                            mGiftCount2 = ((int) arrayPromotionDetail.get(0).getMeghdar2() * zarib);
                                            mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                            howToPromotion = 2;
                                            CommitPromoCode.add(mPromoCode);
                                            giftFromAnotherProduct(mGiftCount1, mGiftCount2, KalaCode, mPromoCode, productDetail);
                                            break;
                                    }
                                }
                                break;

                            case Promotion.Jame_Aghlame_Faktor:
                                arrayPromotionDetail = db.getPromotionDetails(promotion.getPromotionCode(), TotalCountWithoutGift);
                                if (arrayPromotionDetail.size() > 0) {
                                    switch (arrayPromotionDetail.get(0).getHowToPromotion()) {
                                        case Promotion.takhfif_mablaghe_sabet:
                                            if (arrayPromotionDetail.get(0).getIsCalcAdditive() == 1)
                                                zarib = roundDoubleToInt(TotalCountWithoutGift / arrayPromotionDetail.get(0).getToPayment());
                                            fixedDiscount = zarib * arrayPromotionDetail.get(0).getMeghdarPromotion();
                                            howToPromotion = 3;
                                            mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                            CommitPromoCode.add(mPromoCode);
                                            wholeFactorFixedDiscount(fixedDiscount, mPromoCode);
                                            break;
                                        case Promotion.takhfif_darsadi:
                                            if (promotion.getIsCalcLinear() == 1)
                                                percentDiscount = arrayPromotionDetail.get(0).getMeghdarPromotion();
                                            else {
                                                double mStairOff = calculateStairOff(arrayPromotionDetail, TotalCountWithoutGift);
                                                percentDiscount = ((mStairOff * 100) / TotalCountWithoutGift);
                                            }
                                            howToPromotion = 3;
                                            mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                            CommitPromoCode.add(mPromoCode);
                                            wholeFactorPercentDiscount(percentDiscount, mPromoCode);
                                            break;
                                        case Promotion.takhfif_Az_Sotooh:
                                            for (OrderDetail orderDetail : orderDetails) {
                                                productDetail = db.getProductDetail(orderDetail.getProductDetailId());
                                                product = db.GetProductWithProductId(productDetail.getProductId());
                                                offPercent = getDiscountFromDiscountLevel(arrayPromotionDetail.get(0).getMeghdarPromotion(), productDetail);
                                                howToPromotion = 3;
                                                mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                                CommitPromoCode.add(mPromoCode);
                                                rowPercentOff(offPercent, mPromoCode, product);
                                            }
                                            break;
                                        case Promotion.eshantion_Az_hamanKala:
                                            //gift az haman kala agar be andaze gift kala mojood bashad.
                                            for (OrderDetail orderDetail : orderDetails) {
                                                productDetail = db.getProductDetail(orderDetail.getProductDetailId());
                                                product = db.GetProductWithProductId(productDetail.getProductId());
                                                KalaCode = (int) (product.getProductCode());
                                                if (arrayPromotionDetail.get(0).getIsCalcAdditive() == 1)
                                                    zarib = (int) (TotalCountWithoutGift / arrayPromotionDetail.get(0).getToPayment());
                                                mGiftCount1 = ((int) arrayPromotionDetail.get(0).getMeghdar() * zarib);
                                                mGiftCount2 = ((int) arrayPromotionDetail.get(0).getMeghdar2() * zarib);
                                                mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                                howToPromotion = 2;
                                                CommitPromoCode.add(mPromoCode);
                                                giftFromSameProduct(mGiftCount1, mGiftCount2, KalaCode, mPromoCode, productDetail);
                                            }
                                            break;
                                        case Promotion.eshantion_Az_kalahaye_digar:
                                            //gift az kalaye digar.
                                            KalaCode = (arrayPromotionDetail.get(0).getCodeGood());
                                            Product product1 = db.getProductWithProductCode(KalaCode);
                                            productDetail = db.getProductDetailWithProductId(product1.getProductId());
                                            if (arrayPromotionDetail.get(0).getIsCalcAdditive() == 1)
                                                zarib = (int) (TotalCountWithoutGift / arrayPromotionDetail.get(0).getToPayment());
                                            mGiftCount1 = ((int) arrayPromotionDetail.get(0).getMeghdar() * zarib);
                                            mGiftCount2 = ((int) arrayPromotionDetail.get(0).getMeghdar2() * zarib);
                                            mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                            howToPromotion = 2;
                                            CommitPromoCode.add(mPromoCode);
                                            giftFromAnotherProduct(mGiftCount1, mGiftCount2, KalaCode, mPromoCode, productDetail);
                                            break;
                                    }
                                }
                                break;

                            case Promotion.Jame_Vazne_Faktor:
                                arrayPromotionDetail = db.getPromotionDetails(promotion.getPromotionCode(), TotalWeightWithoutGift);
                                if (arrayPromotionDetail.size() > 0) {
                                    switch (arrayPromotionDetail.get(0).getHowToPromotion()) {
                                        case Promotion.takhfif_mablaghe_sabet:
                                            if (arrayPromotionDetail.get(0).getIsCalcAdditive() == 1)
                                                zarib = roundDoubleToInt(TotalWeightWithoutGift / arrayPromotionDetail.get(0).getToPayment());
                                            fixedDiscount = zarib * arrayPromotionDetail.get(0).getMeghdarPromotion();
                                            mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                            howToPromotion = 3;
                                            CommitPromoCode.add(mPromoCode);
                                            wholeFactorFixedDiscount(fixedDiscount, mPromoCode);
                                            break;
                                        case Promotion.takhfif_darsadi:
                                            if (promotion.getIsCalcLinear() == 1)
                                                percentDiscount = arrayPromotionDetail.get(0).getMeghdarPromotion();
                                            else {
                                                double mStairOff = calculateStairOff(arrayPromotionDetail, TotalWeightWithoutGift);
                                                percentDiscount = ((mStairOff * 100) / TotalWeightWithoutGift);
                                            }
                                            mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                            howToPromotion = 3;
                                            CommitPromoCode.add(mPromoCode);
                                            wholeFactorPercentDiscount(percentDiscount, mPromoCode);
                                            break;
                                        case Promotion.takhfif_Az_Sotooh:
                                            for (OrderDetail orderDetail : orderDetails) {
                                                productDetail = db.getProductDetail(orderDetail.getProductDetailId());
                                                product = db.GetProductWithProductId(productDetail.getProductId());
                                                offPercent = getDiscountFromDiscountLevel(arrayPromotionDetail.get(0).getMeghdarPromotion(), productDetail);
                                                mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                                howToPromotion = 3;
                                                CommitPromoCode.add(mPromoCode);
                                                rowPercentOff(offPercent, mPromoCode, product);
                                            }

                                            break;
                                        case Promotion.eshantion_Az_hamanKala:
                                            //gift az haman kala agar be andaze gift kala mojood bashad.

                                            for (OrderDetail orderDetail : orderDetails) {
                                                productDetail = db.getProductDetail(orderDetail.getProductDetailId());
                                                product = db.GetProductWithProductId(productDetail.getProductId());
                                                KalaCode = (int) (product.getProductCode());
                                                if (arrayPromotionDetail.get(0).getIsCalcAdditive() == 1)
                                                    zarib = roundDoubleToInt(TotalWeightWithoutGift / arrayPromotionDetail.get(0).getToPayment());
                                                mGiftCount1 = ((int) arrayPromotionDetail.get(0).getMeghdar() * zarib);
                                                mGiftCount2 = ((int) arrayPromotionDetail.get(0).getMeghdar2() * zarib);
                                                mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                                howToPromotion = 2;
                                                CommitPromoCode.add(mPromoCode);
                                                giftFromSameProduct(mGiftCount1, mGiftCount2, KalaCode, mPromoCode, productDetail);
                                            }
                                            break;
                                        case Promotion.eshantion_Az_kalahaye_digar:
                                            //gift az kalaye digar.
                                            KalaCode = (arrayPromotionDetail.get(0).getCodeGood());
                                            Product product1 = db.getProductWithProductCode(KalaCode);
                                            productDetail = db.getProductDetailWithProductId(product1.getProductId());
                                            if (arrayPromotionDetail.get(0).getIsCalcAdditive() == 1)
                                                zarib = roundDoubleToInt(TotalWeightWithoutGift / arrayPromotionDetail.get(0).getToPayment());
                                            mGiftCount1 = ((int) arrayPromotionDetail.get(0).getMeghdar() * zarib);
                                            mGiftCount2 = ((int) arrayPromotionDetail.get(0).getMeghdar2() * zarib);
                                            mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                            howToPromotion = 2;
                                            CommitPromoCode.add(mPromoCode);
                                            giftFromAnotherProduct(mGiftCount1, mGiftCount2, KalaCode, mPromoCode, productDetail);
                                            break;
                                    }
                                }
                                break;

                            case Promotion.Jame_anvae_Aghlame_faktor:
                                for (OrderDetail orderDetail : orderDetails) {
                                    jameTedadAghlam++;
                                }
                                arrayPromotionDetail = db.getPromotionDetails(promotion.getPromotionCode(), jameTedadAghlam);
                                if (arrayPromotionDetail.size() > 0) {
                                    switch (arrayPromotionDetail.get(0).getHowToPromotion()) {
                                        case Promotion.takhfif_mablaghe_sabet:
                                            if (arrayPromotionDetail.get(0).getIsCalcAdditive() == 1)
                                                zarib = roundDoubleToInt(jameTedadAghlam / arrayPromotionDetail.get(0).getToPayment());
                                            fixedDiscount = zarib * arrayPromotionDetail.get(0).getMeghdarPromotion();
                                            howToPromotion = 3;
                                            mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                            CommitPromoCode.add(mPromoCode);
                                            wholeFactorFixedDiscount(fixedDiscount, mPromoCode);
                                            break;
                                        case Promotion.takhfif_darsadi:
                                            if (promotion.getIsCalcLinear() == 1)
                                                percentDiscount = arrayPromotionDetail.get(0).getMeghdarPromotion();
                                            else {
                                                double mStairOff = calculateStairOff(arrayPromotionDetail, jameTedadAghlam);
                                                percentDiscount = ((mStairOff * 100) / jameTedadAghlam);
                                            }
                                            howToPromotion = 3;
                                            mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                            CommitPromoCode.add(mPromoCode);
                                            wholeFactorPercentDiscount(percentDiscount, mPromoCode);
                                            break;
                                        case Promotion.takhfif_Az_Sotooh:
                                            for (OrderDetail orderDetail : orderDetails) {
                                                productDetail = db.getProductDetail(orderDetail.getProductDetailId());
                                                product = db.GetProductWithProductId(productDetail.getProductId());
                                                offPercent = getDiscountFromDiscountLevel(arrayPromotionDetail.get(0).getMeghdarPromotion(), productDetail);
                                                howToPromotion = 3;
                                                mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                                CommitPromoCode.add(mPromoCode);
                                                rowPercentOff(offPercent, mPromoCode, product);
                                            }
                                            break;
                                        case Promotion.eshantion_Az_hamanKala:
                                            //gift az haman kala agar be andaze gift kala mojood bashad.
                                            for (OrderDetail orderDetail : orderDetails) {
                                                productDetail = db.getProductDetail(orderDetail.getProductDetailId());
                                                product = db.GetProductWithProductId(productDetail.getProductId());
                                                KalaCode = (int) (product.getProductCode());
                                                if (arrayPromotionDetail.get(0).getIsCalcAdditive() == 1)
                                                    zarib = roundDoubleToInt(jameTedadAghlam / arrayPromotionDetail.get(0).getToPayment());
                                                mGiftCount1 = ((int) arrayPromotionDetail.get(0).getMeghdar() * zarib);
                                                mGiftCount2 = ((int) arrayPromotionDetail.get(0).getMeghdar2() * zarib);
                                                mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                                howToPromotion = 2;
                                                CommitPromoCode.add(mPromoCode);
                                                giftFromSameProduct(mGiftCount1, mGiftCount2, KalaCode, mPromoCode, productDetail);
                                            }
                                            break;
                                        case Promotion.eshantion_Az_kalahaye_digar:
                                            //gift az kalaye digar.
                                            KalaCode = (arrayPromotionDetail.get(0).getCodeGood());
                                            Product product1 = db.getProductWithProductCode(KalaCode);
                                            productDetail = db.getProductDetailWithProductId(product1.getProductId());
                                            if (arrayPromotionDetail.get(0).getIsCalcAdditive() == 1)
                                                zarib = roundDoubleToInt(jameTedadAghlam / arrayPromotionDetail.get(0).getToPayment());
                                            mGiftCount1 = ((int) arrayPromotionDetail.get(0).getMeghdar() * zarib);
                                            mGiftCount2 = ((int) arrayPromotionDetail.get(0).getMeghdar2() * zarib);
                                            mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                            howToPromotion = 2;
                                            CommitPromoCode.add(mPromoCode);
                                            giftFromAnotherProduct(mGiftCount1, mGiftCount2, KalaCode, mPromoCode, productDetail);
                                            break;
                                    }
                                }
                                break;

                            case Promotion.Mablaghe_Satr:
                                //mablaghe satr
                                for (OrderDetail orderDetail : orderDetails) {
                                    productDetail = db.getProductDetail(orderDetail.getProductDetailId());
                                    product = db.GetProductWithProductId(productDetail.getProductId());
                                    if (promotion.getIsAllGood() == 1 || db.isInEntity(product.getProductCode(), promotion.getPromotionId(), Promotion.EntityGoods) || db.isInEntity(product.getProductCategoryId(), promotion.getPromotionId(), Promotion.EntityGroupGoods)) {
                                        double totalPrice = (orderDetail.getPrice()) * orderDetail.getSumCountBaJoz();
                                        arrayPromotionDetail = db.getPromotionDetails(promotion.getPromotionCode(), totalPrice);
                                        if (arrayPromotionDetail.size() > 0) {
                                            switch (arrayPromotionDetail.get(0).getHowToPromotion()) {
                                                case Promotion.takhfif_mablaghe_sabet:
                                                    if (arrayPromotionDetail.get(0).getIsCalcAdditive() == 1)
                                                        zarib = roundDoubleToInt(totalPrice / arrayPromotionDetail.get(0).getToPayment());
                                                    fixedOff = zarib * arrayPromotionDetail.get(0).getMeghdarPromotion();
                                                    howToPromotion = 3;
                                                    mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                                    CommitPromoCode.add(mPromoCode);
                                                    rowFixedOff(fixedOff, mPromoCode, product);
                                                    break;
                                                case Promotion.takhfif_darsadi:
                                                    if (promotion.getIsCalcLinear() == 1)
                                                        offPercent = arrayPromotionDetail.get(0).getMeghdarPromotion();
                                                    else {
                                                        float mStairOff = calculateStairOff(arrayPromotionDetail, totalPrice);
                                                        offPercent = (float) ((mStairOff * 100) / (orderDetail.getPrice() * orderDetail.getSumCountBaJoz()));
                                                    }
                                                    howToPromotion = 3;
                                                    mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                                    CommitPromoCode.add(mPromoCode);
                                                    rowPercentOff(offPercent, mPromoCode, product);
                                                    break;
                                                case Promotion.takhfif_Az_Sotooh:
                                                    offPercent = getDiscountFromDiscountLevel(arrayPromotionDetail.get(0).getMeghdarPromotion(), productDetail);
                                                    howToPromotion = 3;
                                                    mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                                    CommitPromoCode.add(mPromoCode);
                                                    rowPercentOff(offPercent, mPromoCode, product);
                                                    break;
                                                case Promotion.eshantion_Az_hamanKala:
                                                    //gift az haman kala agar be andaze gift kala mojood bashad.
                                                    productDetail = db.getProductDetail(orderDetail.getProductDetailId());
                                                    KalaCode = (int) (product.getProductCode());
                                                    if (arrayPromotionDetail.get(0).getIsCalcAdditive() == 1)
                                                        zarib = roundDoubleToInt(totalPrice / arrayPromotionDetail.get(0).getToPayment());
                                                    mGiftCount1 = ((int) arrayPromotionDetail.get(0).getMeghdar() * zarib);
                                                    mGiftCount2 = ((int) arrayPromotionDetail.get(0).getMeghdar2() * zarib);
                                                    mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                                    howToPromotion = 2;
                                                    CommitPromoCode.add(mPromoCode);
                                                    giftFromSameProduct(mGiftCount1, mGiftCount2, KalaCode, mPromoCode, productDetail);
                                                    break;
                                                case Promotion.eshantion_Az_kalahaye_digar:
                                                    //gift az kalaye digar.
                                                    KalaCode = (arrayPromotionDetail.get(0).getCodeGood());
                                                    if (arrayPromotionDetail.get(0).getIsCalcAdditive() == 1)
                                                        zarib = roundDoubleToInt(totalPrice / arrayPromotionDetail.get(0).getToPayment());
                                                    mGiftCount1 = ((int) arrayPromotionDetail.get(0).getMeghdar() * zarib);
                                                    mGiftCount2 = ((int) arrayPromotionDetail.get(0).getMeghdar2() * zarib);
                                                    mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                                    howToPromotion = 2;
                                                    CommitPromoCode.add(mPromoCode);
                                                    giftFromAnotherProduct(mGiftCount1, mGiftCount2, KalaCode, mPromoCode, productDetail);
                                                    break;
                                            }
                                        }
                                    }
                                }
                                break;
                            case Promotion.Meghdare_Satr:
                                //meghdare satr
                                for (OrderDetail orderDetail : orderDetails) {
                                    productDetail = db.getProductDetail(orderDetail.getProductDetailId());
                                    product = db.GetProductWithProductId(productDetail.getProductId());
                                    if (promotion.getIsAllGood() == 1 || db.isInEntity(product.getProductCode(), promotion.getPromotionId(), Promotion.EntityGoods) || db.isInEntity(product.getProductCategoryId(), promotion.getPromotionId(), Promotion.EntityGroupGoods)) {
                                        arrayPromotionDetail = db.getPromotionDetails(promotion.getPromotionCode(), orderDetail.getSumCountBaJoz());
                                        if (arrayPromotionDetail.size() > 0) {
                                            switch (arrayPromotionDetail.get(0).getHowToPromotion()) {
                                                case Promotion.takhfif_mablaghe_sabet:
                                                    if (arrayPromotionDetail.get(0).getIsCalcAdditive() == 1)
                                                        zarib = roundDoubleToInt(orderDetail.getSumCountBaJoz() / arrayPromotionDetail.get(0).getToPayment());
                                                    fixedOff = zarib * arrayPromotionDetail.get(0).getMeghdarPromotion();
                                                    mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                                    howToPromotion = 3;
                                                    CommitPromoCode.add(mPromoCode);
                                                    rowFixedOff(fixedOff, mPromoCode, product);
                                                    break;
                                                case Promotion.takhfif_darsadi:
                                                    offPercent = arrayPromotionDetail.get(0).getMeghdarPromotion();
                                                    mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                                    howToPromotion = 3;
                                                    CommitPromoCode.add(mPromoCode);
                                                    rowPercentOff(offPercent, mPromoCode, product);
                                                    break;
                                                case Promotion.takhfif_Az_Sotooh:
                                                    offPercent = getDiscountFromDiscountLevel(arrayPromotionDetail.get(0).getMeghdarPromotion(), productDetail);
                                                    mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                                    howToPromotion = 3;
                                                    CommitPromoCode.add(mPromoCode);
                                                    rowPercentOff(offPercent, mPromoCode, product);
                                                    break;
                                                case Promotion.eshantion_Az_hamanKala:
                                                    //gift az haman kala agar be andaze gift kala mojood bashad.
                                                    productDetail = db.getProductDetail(orderDetail.getProductDetailId());
                                                    KalaCode = (int) (product.getProductCode());
                                                    if (arrayPromotionDetail.get(0).getIsCalcAdditive() == 1)
                                                        zarib = roundDoubleToInt(orderDetail.getSumCountBaJoz() / arrayPromotionDetail.get(0).getToPayment());
                                                    mGiftCount1 = ((int) arrayPromotionDetail.get(0).getMeghdar() * zarib);
                                                    mGiftCount2 = ((int) arrayPromotionDetail.get(0).getMeghdar2() * zarib);
                                                    mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                                    howToPromotion = 2;
                                                    CommitPromoCode.add(mPromoCode);
                                                    giftFromSameProduct(mGiftCount1, mGiftCount2, KalaCode, mPromoCode, productDetail);
                                                    break;
                                                case Promotion.eshantion_Az_kalahaye_digar:
                                                    //gift az kalaye digar.
                                                    KalaCode = (arrayPromotionDetail.get(0).getCodeGood());
                                                    if (arrayPromotionDetail.get(0).getIsCalcAdditive() == 1)
                                                        zarib = roundDoubleToInt(orderDetail.getSumCountBaJoz() / arrayPromotionDetail.get(0).getToPayment());
                                                    mGiftCount1 = ((int) arrayPromotionDetail.get(0).getMeghdar() * zarib);
                                                    mGiftCount2 = ((int) arrayPromotionDetail.get(0).getMeghdar2() * zarib);
                                                    mPromoCode = ServiceTools.toInt(arrayPromotionDetail.get(0).getPromotionCode());
                                                    howToPromotion = 2;
                                                    CommitPromoCode.add(mPromoCode);
                                                    giftFromAnotherProduct(mGiftCount1, mGiftCount2, KalaCode, mPromoCode, productDetail);
                                                    break;
                                            }
                                        }
                                    }
                                }
                                break;
                        }
                    }
                    if (mGiftCount1 > 0 && promotion.getAggregateWithOther() == 0)
                        break;
                }
            }
        }

    }

    private double getDiscountFromDiscountLevel(int meghdarPromotion, ProductDetail productDetail) {
        switch (meghdarPromotion) {
            case 1:
                return productDetail.getDiscount1();
            case 2:
                return productDetail.getDiscount2();
            case 3:
                return productDetail.getDiscount3();
            case 4:
                return productDetail.getDiscount4();
        }
        return 0;
    }

    private ArrayList<Promotion> promotionCanBeApplied(String invoiceDate, Customer customer) {

        ArrayList<Promotion> customerPromotions = new ArrayList<>();
        ArrayList<Promotion> promotions = db.getValidPromotions(invoiceDate);
        CustomerGroup personGroup = db.GetCustomerGroupWithGroupId(customer.getPersonGroupId());
        if (promotions != null) {

            for (Promotion promotion : promotions) {
                if (promotion.getIsAllCustomer() == 1 || db.isInEntity(customer.getPersonCode(), promotion.getPromotionId(), Promotion.EntityCustomer) || db.isInEntity(personGroup.getPersonGroupCode(), promotion.getPromotionId(), Promotion.EntityGroupCustomer)) {
                    customerPromotions.add(promotion);
                }
                if (promotion.getAggregateWithOther() == 0)
                    break;
            }

        }
        return customerPromotions;
    }

    private void giftFromSameProduct(int mGiftCount1, int mGiftCount2, int kalaCode, int mPromoCode, ProductDetail productDetail) {
        Product promoProduct;
        promoProduct = db.getProductWithProductCode(kalaCode);
        ProductDetail promoProductDetail = db.getProductDetailWithProductId(promoProduct.getProductId());
        if (promoProduct != null && mGiftCount1 != 0) {
            if (promoProduct.getProductId() == productDetail.getProductId()) {

                mGiftCount1 += ProductPickerListActivity.HashMap_Product.get(productDetail.getProductId()).getGiftCount1();
                mGiftCount2 += ProductPickerListActivity.HashMap_Product.get(productDetail.getProductId()).getGiftCount2();

                ProductPickerListActivity.HashMap_Product.get(productDetail.getProductId()).setGiftCount1(mGiftCount1);
                ProductPickerListActivity.HashMap_Product.get(productDetail.getProductId()).setGiftCount2(mGiftCount2);

                ProductPickerListActivity.HashMap_Product.get(productDetail.getProductId()).setPromotionCode(mPromoCode);
                ProductPickerListActivity.HashMap_Product.get(productDetail.getProductId()).setGiftType(Promotion.Eshantion_Tarhi);
            } else {
                if (ProductPickerListActivity.HashMap_Product.get(promoProduct.getProductId()) != null) {

                    mGiftCount1 += ProductPickerListActivity.HashMap_Product.get(productDetail.getProductId()).getGiftCount1();
                    mGiftCount2 += ProductPickerListActivity.HashMap_Product.get(productDetail.getProductId()).getGiftCount2();

                    ProductPickerListActivity.HashMap_Product.get(productDetail.getProductId()).setGiftCount1(mGiftCount1);
                    ProductPickerListActivity.HashMap_Product.get(productDetail.getProductId()).setGiftCount2(mGiftCount2);

                    ProductPickerListActivity.HashMap_Product.get(productDetail.getProductId()).setPromotionCode(mPromoCode);
                    ProductPickerListActivity.HashMap_Product.get(promoProduct.getProductId()).setGiftType(Promotion.Eshantion_Tarhi);
                } else {
                    mGiftCount1 += mPromoObject.getGiftCount1();
                    mGiftCount2 += mPromoObject.getGiftCount2();
                    mPromoObject.setProductDetailId((int) promoProductDetail.getProductDetailId());
                    mPromoObject.setProductId((int) promoProduct.getProductId());
                    mPromoObject.setProductName(promoProduct.getName());
                    mPromoObject.setMin((int) promoProduct.getMin());
                    //mPromoObject.setMax((int)promoProduct.getAsset());
                    mPromoObject.setPrice(String.valueOf(ServiceTools.getPriceFromPriceLevel(productDetail.getProductDetailId(), activity)));

                    mPromoObject.setGiftCount1(mGiftCount1);
                    mPromoObject.setGiftCount2(mGiftCount2);

                    mPromoObject.setPromotionCode(mPromoCode);
                    ProductPickerListActivity.HashMap_Product.put((int) promoProduct.getProductId(), mPromoObject);
                    ProductPickerListActivity.HashMap_Product.get(promoProduct.getProductId()).setGiftType(Promotion.Eshantion_Tarhi);
                    ProductPickerListActivity.HashMap_Product.put((int) promoProduct.getProductId(), mPromoObject);
                    InvoiceDetailActivity.orderDetails.add(mPromoObject);
                }
            }
        } else {
            mGiftCount1 += ProductPickerListActivity.HashMap_Product.get(productDetail.getProductId()).getGiftCount1();
            mGiftCount2 += ProductPickerListActivity.HashMap_Product.get(productDetail.getProductId()).getGiftCount2();

            ProductPickerListActivity.HashMap_Product.get(productDetail.getProductId()).setGiftCount1(mGiftCount1);
            ProductPickerListActivity.HashMap_Product.get(productDetail.getProductId()).setGiftCount2(mGiftCount2);

            ProductPickerListActivity.HashMap_Product.get(productDetail.getProductId()).setGiftType(0);
            ProductPickerListActivity.HashMap_Product.get(productDetail.getProductId()).setPromotionCode(0);
        }
    }

    private void giftFromAnotherProduct(int mGiftCount1, int mGiftCount2, int kalaCode, int mPromoCode, ProductDetail productDetail) {
        Product promoProduct;
        promoProduct = db.getProductWithProductCode(kalaCode);
        ProductDetail promoProductDetail = db.getProductDetailWithProductId(promoProduct.getProductId());
        if (mGiftCount1 != 0) {
            if (ProductPickerListActivity.HashMap_Product.get(promoProduct.getProductId()) != null) {
                mGiftCount1 += ProductPickerListActivity.HashMap_Product.get(promoProduct.getProductId()).getGiftCount1();
                mGiftCount2 += ProductPickerListActivity.HashMap_Product.get(promoProduct.getProductId()).getGiftCount2();

                ProductPickerListActivity.HashMap_Product.get(promoProduct.getProductId()).setGiftCount1(mGiftCount1);
                ProductPickerListActivity.HashMap_Product.get(promoProduct.getProductId()).setGiftCount2(mGiftCount2);

                ProductPickerListActivity.HashMap_Product.get(promoProduct.getProductId()).setPromotionCode(mPromoCode);
                ProductPickerListActivity.HashMap_Product.get(promoProduct.getProductId()).setGiftType(Promotion.Eshantion_Tarhi);
            } else {

                mGiftCount1 += mPromoObject.getGiftCount1();
                mGiftCount2 += mPromoObject.getGiftCount2();

                mPromoObject.setProductDetailId((int) promoProductDetail.getProductDetailId());
                mPromoObject.setProductId((int) promoProduct.getProductId());
                mPromoObject.setProductName(promoProduct.getName());
                mPromoObject.setMin((int) promoProduct.getMin());
                mPromoObject.setPrice(String.valueOf(ServiceTools.getPriceFromPriceLevel(productDetail.getProductDetailId(), activity)));

                mPromoObject.setGiftCount1(mGiftCount1);
                mPromoObject.setGiftCount2(mGiftCount2);

                mPromoObject.setPromotionCode(mPromoCode);
                ProductPickerListActivity.HashMap_Product.put((int) promoProduct.getProductId(), mPromoObject);
                ProductPickerListActivity.HashMap_Product.get(promoProduct.getProductId()).setGiftType(Promotion.Eshantion_Tarhi);
                ProductPickerListActivity.HashMap_Product.put((int) promoProduct.getProductId(), mPromoObject);
                InvoiceDetailActivity.orderDetails.add(mPromoObject);
            }
        }
    }

    private void rowPercentOff(double offPercent, int mPromoCode, Product product) {
        OrderDetail orderDetail = ProductPickerListActivity.HashMap_Product.get(product.getProductId());
        double d = ServiceTools.RegulartoDouble(BaseActivity.getPrefRowDiscountIsActive());
        if(orderDetail != null){
            if (offPercent != 0) {
                orderDetail.setPromotionCode(mPromoCode);
                orderDetail.setGiftType(Promotion.Takhfif_Satri);
                orderDetail.setDiscount(ServiceTools.getRowOff(offPercent, orderDetail.getPrice(), orderDetail.getSumCountBaJoz()));
                double FinalPrice = ServiceTools.getCalculateFinalPrice(orderDetail, getActivity());
                orderDetail.setFinalPrice(String.valueOf(FinalPrice));
            } else {
                orderDetail.setDiscount((offPercent));
            }
        }

    }

    private void rowFixedOff(float fixedOff, int mPromoCode, Product product) {
        if (fixedOff != 0) {
            ProductPickerListActivity.HashMap_Product.get(product.getProductId()).setPromotionCode(mPromoCode);
            ProductPickerListActivity.HashMap_Product.get(product.getProductId()).setGiftType(Promotion.Takhfif_Satri);
            double price = (ProductPickerListActivity.HashMap_Product.get(product.getProductId()).getPrice());
            double count = ProductPickerListActivity.HashMap_Product.get(product.getProductId()).getCount1();
            ProductPickerListActivity.HashMap_Product.get(product.getProductId()).setDiscount((float) (int) ((fixedOff * 100) / (price * count)));
            double FinalPrice = ServiceTools.getCalculateFinalPrice(ProductPickerListActivity.HashMap_Product.get(product.getProductId()), getActivity());
            ProductPickerListActivity.HashMap_Product.get(product.getProductId()).setFinalPrice(String.valueOf(FinalPrice));
        }
    }

    private void wholeFactorFixedDiscount(double fixedDiscount, int mPromoCode) {
        if (fixedDiscount != 0) {
            promoDiscount = fixedDiscount;
            orderPromotionCode = mPromoCode;
            orderGiftType = Promotion.Takhfif_Kole_Sefaresh;
        }
    }

    private void wholeFactorPercentDiscount(double mpercentDiscount, int mPromoCode) {
        if (mpercentDiscount != 0) {
            promoPercentDiscount = (roundDouble(mpercentDiscount));
            orderPromotionCode = mPromoCode;
            orderGiftType = Promotion.Takhfif_Kole_Sefaresh;
            txtDiscountPercent.requestFocus();
        }
    }

    private void setPromoDiscountView() {

        double final_price = (TotalPrice - TotalOff) + (totalTaxAndCharge);
        //double final_price = (TotalPrice);
        double calculatedPercentDiscount = (final_price * promoPercentDiscount) / 100;
        if (promoDiscount > 0 || promoPercentDiscount > 0) {
            Discount = promoDiscount + calculatedPercentDiscount;
            txtDiscount.setText(String.valueOf(Discount));
            txtDiscountPercent.setText(String.valueOf(roundDoubleToInt(calculatedPercentDiscount * 100) / final_price));
        }
        CalculationTotal();
        calculateGroupedtax();
        setCalculateResult();
        ClearZeroGift();
        adListProduct.notifyDataSetChanged();
        lstCart.setAdapter(adListProduct);
        ServiceTools.setListViewHeightBasedOnChildren(lstCart);
    }

    private float calculateStairOff(ArrayList<PromotionDetail> arrayPromotionDetail, double price) {
        double differnce;
        float fixedOff = 0;
        for (PromotionDetail promotionDetail : arrayPromotionDetail) {
            differnce = price - promotionDetail.getToPayment();
            if (differnce >= 0) {
                fixedOff += (differnce * promotionDetail.getMeghdarPromotion()) / 100;
                price = price - differnce;
            }
        }
        return fixedOff;
    }

    public void ClearZero(OrderDetail prodInOrder) {
        Set mapSet = ProductPickerListActivity.HashMap_Product.entrySet();
        Iterator mapIterator = mapSet.iterator();
        while (mapIterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
            OrderDetail orderDetail = (OrderDetail) mapEntry.getValue();
            orderDetail.setGiftCount1(0);
            orderDetail.setGiftCount2(0);
            if (orderDetail.getCount1() == 0 && orderDetail.getCount2() == 0 && orderDetail.getSumCountBaJoz() == 0) {
                mapIterator.remove();
                ProductPickerListActivity.Product_Delete.add(prodInOrder);
                InvoiceDetailActivity.orderDetails.remove(orderDetail);
            }
        }// End of While
        /////////////////////////////////////////////////////////
    }

    public void ClearZeroGift() {
        Set mapSet = ProductPickerListActivity.HashMap_Product.entrySet();
        Iterator mapIterator = mapSet.iterator();
        while (mapIterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
            OrderDetail orderDetail = (OrderDetail) mapEntry.getValue();
            if (orderDetail.getCount1() == 0 && ServiceTools.getSumGiftCount12(orderDetail.getGiftCount1(), orderDetail.getGiftCount2(), getActivity()) == 0 && orderDetail.getCount2() == 0 && orderDetail.getSumCountBaJoz() == 0) {
                mapIterator.remove();
                InvoiceDetailActivity.orderDetails.remove(orderDetail);
            }
        }// End of While
        /////////////////////////////////////////////////////////
    }

    public void ClearAllGift() {
        Discount = 0;
        Set mapSet = ProductPickerListActivity.HashMap_Product.entrySet();
        Iterator mapIterator = mapSet.iterator();
        while (mapIterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
            OrderDetail orderDetail = (OrderDetail) mapEntry.getValue();
            if (ServiceTools.getSumGiftCount12(orderDetail.getGiftCount1(), orderDetail.getGiftCount2(), getActivity()) != 0)
                if (orderDetail.getCount1() == 0 && orderDetail.getCount2() == 0 && orderDetail.getSumCountBaJoz() == 0) {
                    mapIterator.remove();
                    ProductPickerListActivity.Product_Delete.add(orderDetail);
                    InvoiceDetailActivity.orderDetails.remove(orderDetail);
                } else {
                    orderDetail.setGiftCount1(0);
                    orderDetail.setGiftCount2(0);

                }


            if (ServiceTools.getSumGiftCount12(orderDetail.getGiftCount1(), orderDetail.getGiftCount2(), getActivity()) == 0 && orderDetail.getCount1() == 0 && orderDetail.getCount2() == 0 && orderDetail.getSumCountBaJoz() == 0) {
                mapIterator.remove();
                ProductPickerListActivity.Product_Delete.add(orderDetail);
                InvoiceDetailActivity.orderDetails.remove(orderDetail);
            }

        }// End of While
        /////////////////////////////////////////////////////////
    }

}
