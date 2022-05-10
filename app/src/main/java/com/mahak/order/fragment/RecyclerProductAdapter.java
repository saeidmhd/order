package com.mahak.order.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mahak.order.BaseActivity;
import com.mahak.order.InvoiceDetailActivity;
import com.mahak.order.PhotoViewerActivity;
import com.mahak.order.PriceCountSelectActivity;
import com.mahak.order.ProductItemInitialize;
import com.mahak.order.ProductPickerListActivity;
import com.mahak.order.R;
import com.mahak.order.adapter.PromotionDetailAdapter;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.OrderDetailProperty;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductDetail;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.Promotion;
import com.mahak.order.common.PromotionDetail;
import com.mahak.order.common.Properties;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mahak.order.BaseActivity.getPrefReduceAsset;
import static com.mahak.order.common.ServiceTools.MoneyFormatToNumber;
import static com.mahak.order.common.ServiceTools.formatCount;

public class RecyclerProductAdapter extends RecyclerView.Adapter<ProductHolder> implements ReturnToRecycler {
    private static final int REQUEST_FOR_ACTIVITY_CODE = 100;
    private static Context mContext;
    private long orderId;
    private int mode;
    static public ArrayList<Product> products = new ArrayList<>();
    static DbAdapter db;
    ProductFilterDB2 Filter;
    final ArrayList<Product> arrayOrginal = new ArrayList<>();
    private static ProductPickerListActivity productPickerListActivity;
    private int idLayout;
    private int type;
    private int customerId;
    private long groupId;
    public static ProductHolder productHolder;
    public static RecyclerView mRecyclerView;
    private static TextView Count;
    private static TextView tvProductCode;
    private static TextView Price;
    private static TextView txtTotalCount;
    public static View row;
    static long categoryId;
    static long categoryCode;
    static int modeAsset;
    public static double mCount = 0;
    private final String description = "";
    int CountProduct;
    private static ArrayList<Promotion> promotions;

    double total_count2;
    double asset_count2;

    double total_count1;
    double asset_count1;

    int eshantion_dasti;
    int duplicate_product;


    public RecyclerProductAdapter(
            Context mContext,
            List<Product> products,
            ProductPickerListActivity productPickerListActivity,
            int idLayout,
            int type,
            int customerId,
            long groupId,
            int mode,
            long orderId,
            int eshantion_dasti,
            int duplicate_product) {
        RecyclerProductAdapter.mContext = mContext;
        RecyclerProductAdapter.productPickerListActivity = productPickerListActivity;
        this.idLayout = idLayout;
        arrayOrginal.addAll(products);
        RecyclerProductAdapter.products.clear();
        RecyclerProductAdapter.products.addAll(products);
        db = new DbAdapter(mContext);
        this.type = type;
        this.mode = mode;
        this.orderId = orderId;
        this.customerId = customerId;
        this.groupId = groupId;
        this.eshantion_dasti = eshantion_dasti;
        this.duplicate_product = duplicate_product;
    }

    public RecyclerProductAdapter() {
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(idLayout, parent, false);
        productHolder = new ProductHolder(view);
        return productHolder;
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        int pos = holder.getAbsoluteAdapterPosition();
        Product product = products.get(pos);
        if (product == null)
            product = arrayOrginal.get(pos);
        if (product != null) {
            initHolder(product, holder, pos, mCount);
        }
    }

    private void gotoPriceCount(ProductHolder holder, int pos) {
        Count = holder.txtCount;
        tvProductCode = holder.tvProductCode;
        Price = holder.tvPrice;
        txtTotalCount = holder.txtTotalCount;
        if (type != 0) {
            showCountPriceDialog(
                    holder.getAbsoluteAdapterPosition(),
                    holder.tvPrice.getText().toString(),
                    holder.txtCount.getText().toString(),
                    holder.txtTotalCount.getText().toString(),
                    type,
                    customerId,
                    groupId,
                    products.get(pos).getProductId(),
                    description,
                    mode,
                    orderId);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        CountProduct = BaseActivity.getPrefProductCount(mContext);
        if (db == null) db = new DbAdapter(mContext);
        db.open();
    }

    public void initHolder(final Product product, final ProductHolder holder, final int position, double mCount) {

        double SumCount2 = 0;
        double SumCount1 = 0;
        double price = 0;
        double customerPrice = 0;
       // int promotionId = 0;

        SumCount1 = product.getSumCount1();
        SumCount2 = product.getSumCount2();
        price = product.getPrice();
       // promotionId = product.getPromotionId();
        customerPrice = product.getCustomerPrice();

        /*if(promotionId > 0){
            int count = db.getCountProductPromotionEntity();
            if(count > 0){
                holder.imgGift.setVisibility(View.VISIBLE);
            }else
                holder.imgGift.setVisibility(View.GONE);

            holder.imgGift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    promotions = db.getAllPromotionCodeForSpecificGood(product.getProductCode());
                    showPromotionDetail();
                }
            });
        }*/

        String url = db.getPhotoUrl(product.getProductId());
        product.setPictureUrl(url);

        if (type == 0) {
            holder.panelCount.setVisibility(View.GONE);
            holder.panelTotalCount.setVisibility(View.GONE);
            holder.inboxLayout.setVisibility(View.GONE);
        }

        Boolean res = false;
        if (BaseActivity.getPrefUnit2Setting(mContext) == BaseActivity.MODE_MeghdarJoz || BaseActivity.getPrefUnit2Setting(mContext) == BaseActivity.Mode_DoVahedi ) {
            holder.panelTotalAsset.setVisibility(View.VISIBLE);
            if (productPickerListActivity != null)
                holder.panelTotalCount.setVisibility(View.VISIBLE);
        } else {
            holder.panelTotalCount.setVisibility(View.GONE);
            holder.panelTotalAsset.setVisibility(View.GONE);
            holder.inboxLayout.setVisibility(View.GONE);
        }
        holder.tvName.setText(product.getName());
        holder.tvCustomerPrice.setText(ServiceTools.formatPrice(customerPrice));
        holder.tvProductCode.setText(String.valueOf(product.getProductCode()));

        holder.tvAsset.setText(formatCount(SumCount1));
        holder.tvAsset2.setText(formatCount(SumCount2));
        holder.tvPrice.setText(ServiceTools.formatPrice(price));

        holder.tvInbox.setText(formatCount(product.getUnitRatio()));

        holder.tvUnit.setText(product.getUnitName());
        holder.tvUnit2.setText(product.getUnitName2());

        if(holder.txtTotalCount1 != null)
            holder.txtTotalCount1.setText(product.getUnitName());

        if(holder.txtTotalCount2 != null)
            holder.txtTotalCount2.setText(product.getUnitName2());

        if (product.getPictureUrl() != null) {
            ProductItemInitialize.loadImage(mContext, product.getPictureUrl(), holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.img_default_product);
            holder.imgProduct.setBackgroundResource(R.drawable.image_empty_box);
        }
        holder.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.getPictureUrl() != null){
                    Intent intent = new Intent(mContext, PhotoViewerActivity.class);
                    intent.putExtra(ProjectInfo._json_key_user_id, BaseActivity.getPrefUserId());
                    intent.putExtra(ProjectInfo._json_key_product_id, product.getProductId());
                    intent.putExtra(ProjectInfo._json_key_product_code, product.getProductCode());
                    intent.putExtra(ProjectInfo._json_key_index, 0);
                    ((Activity) mContext).startActivityForResult(intent, REQUEST_FOR_ACTIVITY_CODE);
                }
            }
        });

        holder.txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPriceCount(holder, position);
            }
        });
        holder.txtTotalCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPriceCount(holder, position);
            }
        });



        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(hasProductDetail(product)){
                    gotoPriceCount(holder,position);
                }else {
                    double sum = 0;
                    double check_sum = 0;

                    double count1_sabad_kharid = 0;
                    ArrayList<OrderDetail> orderDetails =  InvoiceDetailActivity.orderDetails;
                    if(orderDetails != null){
                        for (OrderDetail orderDetail : orderDetails)
                            if(orderDetail.getProductId() == product.getProductId())
                                count1_sabad_kharid += orderDetail.getCount1();
                    }
                    String total1 = holder.txtCount.getText().toString();
                    String asset1 = holder.tvAsset.getText().toString();

                    String total2 = holder.txtTotalCount.getText().toString();
                    String asset2 = holder.tvAsset2.getText().toString();


                    total_count1 = ServiceTools.toDouble(total1);
                    asset_count1 = ServiceTools.toDouble(asset1);

                    total_count2 = ServiceTools.toDouble(total2);
                    asset_count2 = ServiceTools.toDouble(asset2);

                    total_count1 ++;

                    if(BaseActivity.getPrefUnit2Setting(mContext) == BaseActivity.MODE_MeghdarJoz || BaseActivity.getPrefUnit2Setting(mContext) == BaseActivity.Mode_DoVahedi)
                        sum = total_count1 + (total_count2 * product.getUnitRatio());
                    else
                        sum = total_count1;

                    if(duplicate_product == 1 || eshantion_dasti == 1){
                        check_sum = sum + count1_sabad_kharid;
                    }else
                        check_sum = sum;

                    if(check_sum > asset_count1 && type == ProjectInfo.TYPE_INVOCIE){
                        Toast.makeText(mContext, "موجودی کالا منفی می شود، ادامه عملیات امکان پذیر نیست!", Toast.LENGTH_SHORT).show();
                    }else {
                        holder.txtCount.setText(ServiceTools.formatCount(total_count1));
                        return_value_recycler2(String.valueOf(total_count2) , String.valueOf(total_count1) , product.getPrice(), position, sum);
                    }
                }


            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(hasProductDetail(product)){
                    gotoPriceCount(holder,position);
                }else {
                    double sum = 0;

                    String total1 = holder.txtCount.getText().toString();
                    String asset1 = holder.tvAsset.getText().toString();

                    String total2 = holder.txtTotalCount.getText().toString();
                    String asset2 = holder.tvAsset2.getText().toString();


                    total_count1 = ServiceTools.toDouble(total1);
                    asset_count1 = ServiceTools.toDouble(asset1);

                    total_count2 = ServiceTools.toDouble(total2);
                    asset_count2 = ServiceTools.toDouble(asset2);

                    if(total_count1 > 0){
                        total_count1--;
                        if(BaseActivity.getPrefUnit2Setting(mContext) == BaseActivity.MODE_MeghdarJoz || BaseActivity.getPrefUnit2Setting(mContext) == BaseActivity.Mode_DoVahedi)
                            sum = total_count1 + (total_count2 * product.getUnitRatio());
                        else
                            sum = total_count1;

                        holder.txtCount.setText(ServiceTools.formatCount(total_count1));
                        return_value_recycler2(String.valueOf(total_count2),String.valueOf(total_count1) , product.getPrice(), position, sum);
                    }
                }
            }
        });


        holder.plus_count2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(hasProductDetail(product)){
                    gotoPriceCount(holder,position);
                }else {
                    double sum = 0;
                    double check_sum = 0;
                    double count2_sabad_kharid = 0;

                    ArrayList<OrderDetail> orderDetails =  InvoiceDetailActivity.orderDetails;
                    if(orderDetails != null){
                        for (OrderDetail orderDetail : orderDetails)
                            if(orderDetail.getProductId() == product.getProductId())
                                count2_sabad_kharid += orderDetail.getCount2();
                    }

                    String total1 = holder.txtCount.getText().toString();
                    String asset1 = holder.tvAsset.getText().toString();

                    String total2 = holder.txtTotalCount.getText().toString();
                    String asset2 = holder.tvAsset2.getText().toString();


                    total_count1 = ServiceTools.toDouble(total1);
                    asset_count1 = ServiceTools.toDouble(asset1);

                    total_count2 = ServiceTools.toDouble(total2);
                    asset_count2 = ServiceTools.toDouble(asset2);

                    if(asset_count2 > 0){
                        total_count2++;

                        if(BaseActivity.getPrefUnit2Setting(mContext) == BaseActivity.MODE_MeghdarJoz || BaseActivity.getPrefUnit2Setting(mContext) == BaseActivity.Mode_DoVahedi)
                            sum = total_count1 + (total_count2 * product.getUnitRatio());
                        else
                            sum = total_count1;

                        if(duplicate_product == 1 || eshantion_dasti == 1){
                            check_sum = sum + count2_sabad_kharid;
                        }else
                            check_sum = sum;

                        if(check_sum > asset_count1 && type == ProjectInfo.TYPE_INVOCIE){
                            Toast.makeText(mContext, "موجودی کالا منفی می شود، ادامه عملیات امکان پذیر نیست!", Toast.LENGTH_SHORT).show();
                        }else {
                            holder.txtTotalCount.setText(ServiceTools.formatCount(total_count2));
                            return_value_recycler2(String.valueOf(total_count2),String.valueOf(total_count1) , product.getPrice(), position, sum);
                        }
                    }else
                        Toast.makeText(mContext, "موجودی کالا منفی می شود، ادامه عملیات امکان پذیر نیست!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.minus_count2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasProductDetail(product)){
                    gotoPriceCount(holder,position);
                }else {
                    double sum =0;
                    String total1 = holder.txtCount.getText().toString();
                    String asset1 = holder.tvAsset.getText().toString();

                    String total2 = holder.txtTotalCount.getText().toString();
                    String asset2 = holder.tvAsset2.getText().toString();

                    total_count1 = ServiceTools.toDouble(total1);
                    asset_count1 = ServiceTools.toDouble(asset1);

                    total_count2 = ServiceTools.toDouble(total2);
                    asset_count2 = ServiceTools.toDouble(asset2);

                    if(total_count2 > 0){
                        total_count2--;

                        if(BaseActivity.getPrefUnit2Setting(mContext) == BaseActivity.MODE_MeghdarJoz || BaseActivity.getPrefUnit2Setting(mContext) == BaseActivity.Mode_DoVahedi)
                            sum = total_count1 + (total_count2 * product.getUnitRatio());
                        else
                            sum = total_count1;

                        holder.txtTotalCount.setText(ServiceTools.formatCount(total_count2));
                        return_value_recycler2(String.valueOf(total_count2),String.valueOf(total_count1) , product.getPrice(), position, sum);
                    }
                }
            }
        });

        if (productPickerListActivity != null) {
            Set mapSet = ProductPickerListActivity.HashMap_Product.entrySet();
            Iterator mapIterator = mapSet.iterator();
            OrderDetail item;
            while (mapIterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                int keyValue = (int) mapEntry.getKey();
                long ProcId = product.getProductId();
                if (ProcId == keyValue) {
                    item = (OrderDetail) mapEntry.getValue();
                    OrderDetail orderDetail = db.GetOrderDetailWithId(item.getId());
                    if (type == ProjectInfo.TYPE_INVOCIE || (type == ProjectInfo.TYPE_ORDER && getPrefReduceAsset(mContext))) {
                        SumCount1 += orderDetail.getCount1() ;
                        SumCount2 += orderDetail.getCount2() ;
                        holder.tvAsset.setText(ServiceTools.formatCount(SumCount1));
                        holder.tvAsset2.setText(ServiceTools.formatCount(SumCount2));
                    } else {
                        holder.tvAsset.setText(ServiceTools.formatCount(SumCount1));
                        holder.tvAsset2.setText(ServiceTools.formatCount(SumCount2));
                    }
                    if(eshantion_dasti == 0 && duplicate_product == 0){
                        holder.txtCount.setText(ServiceTools.formatCount(item.getCount1()));
                        holder.txtTotalCount.setText(ServiceTools.formatCount(item.getCount2()));
                    }
                    holder.tvPrice.setText(ServiceTools.formatPrice(item.getPrice()));
                    res = true;
                    break;
                }// End of if

            }// End of While
            if (!res) {
                holder.txtCount.setText(ServiceTools.formatCount(0));
                holder.txtTotalGift.setText(ServiceTools.formatCount(0));
                holder.txtTotalCount.setText(ServiceTools.formatCount(0));
                holder.tvAsset.setText(ServiceTools.formatCount(SumCount1));
                holder.tvAsset2.setText(ServiceTools.formatCount(SumCount2));
            }
        }
        holder.txtCount.setTag(R.id.ProductId, product.getProductId());
        holder.txtCount.setTag(R.id.Product, product);
        holder.txtTotalCount.setTag(R.id.ProductId, product.getProductId());
        holder.txtTotalCount.setTag(R.id.Product, product);
        holder.txtTotalGift.setTag(R.id.ProductId, product.getProductId());
        holder.txtTotalGift.setTag(R.id.Product, product);
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    public void return_value_recycler2(
            String countPackage,
            String count,
            double price,
            final int position,
            double sumCountBaJoz) {


        double count2 = 0;
        double count1 = 0;
        Product product = products.get(position);

        //Price.setText(ServiceTools.formatPrice(price));
        if (!TextUtils.isEmpty(count)) {
            count1 = ServiceTools.toDouble(count);
           // Count.setText(count);
        }
        if (!TextUtils.isEmpty(countPackage) && BaseActivity.getPrefUnit2Setting(mContext) != BaseActivity.MODE_YekVahedi) {
            //txtTotalCount.setText(countPackage);
            count2 = ServiceTools.toDouble(countPackage);
        }

        Set mapSet = ProductPickerListActivity.HashMap_Product.entrySet();
        Iterator mapIterator = mapSet.iterator();
        OrderDetail item;
        boolean res = false;
        //double RealPrice = ServiceTools.toDouble(String.valueOf(ServiceTools.getPriceFromPriceLevel(productDetail.getProductDetailId(), mContext)));
        //validPromotionList(count1 , ServiceTools.MoneyFormatToNumber(price) , product , products);
        while (mapIterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
            int keyValue = (int) mapEntry.getKey();
            if (product.getProductId() == keyValue) {
                item = (OrderDetail) mapEntry.getValue();
                if (!TextUtils.isEmpty(countPackage) && BaseActivity.getPrefUnit2Setting(mContext) != BaseActivity.MODE_YekVahedi) {
                    item.setCount2(count2);
                }

                item.setCount1(count1);
                item.setSumCountBaJoz(sumCountBaJoz);

                if(eshantion_dasti == 1){
                    item.setGiftType(Promotion.Eshantion_Dasti);
                    item.setPrice("0");
                }else {
                    item.setPrice(String.valueOf(price));
                }
                item.setProductName(product.getName());

                if (!BaseActivity.getPrefRowDiscountIsActive().equals(BaseActivity.invisible)) {
                    double d = ServiceTools.RegulartoDouble(BaseActivity.getPrefRowDiscountIsActive());
                    item.setDiscountType((long) d);
                }

                if (BaseActivity.getPrefTaxAndChargeIsActive().equals(BaseActivity.Active))
                    item.setTaxPercent(ServiceTools.getTax(product));
                else
                    item.setTaxPercent(0);

                if (BaseActivity.getPrefTaxAndChargeIsActive().equals(BaseActivity.Active))
                    item.setChargePercent(ServiceTools.getCharge(product));
                else
                    item.setChargePercent(0);

                if (ProductPickerListActivity.Type != ProjectInfo.TYPE_RETURN_OF_SALE) {
                    double FinalPrice = ServiceTools.getCalculateFinalPrice(item, mContext);
                    item.setFinalPrice(String.valueOf(FinalPrice));
                }
                if(item.getSumCountBaJoz() > 0){
                    ProductPickerListActivity.HashMap_Product.put(product.getProductId(), item);
                }else{
                    ProductPickerListActivity.HashMap_Product.remove(product.getProductId());
                    InvoiceDetailActivity.orderDetails.remove(item);
                    ProductPickerListActivity.Product_Delete.add(item);
                }

                res = true;
                break;
            }// End of if
        }// End of While
        product.setSelectedCount(count1);
        if (!res) {
            if (count1 > 0 || count2 > 0) {
                OrderDetail object = new OrderDetail();
                object.setCount1(count1);
                if (!TextUtils.isEmpty(countPackage) && BaseActivity.getPrefUnit2Setting(mContext) != BaseActivity.MODE_YekVahedi) {
                    object.setCount2(count2);
                }
                object.setSumCountBaJoz(sumCountBaJoz);

                if(eshantion_dasti == 1){
                    object.setPrice("0");
                    object.setGiftType(Promotion.Eshantion_Dasti);
                }else {
                    object.setPrice(String.valueOf(price));
                }
                object.setProductName(product.getName());

                if (!BaseActivity.getPrefRowDiscountIsActive().equals(BaseActivity.invisible)) {
                    double d = ServiceTools.RegulartoDouble(BaseActivity.getPrefRowDiscountIsActive());
                    object.setDiscountType((long) d);
                }
                object.setProductDetailId(product.getProductDetailId());
                object.setProductId(product.getProductId());
                object.setMin((int) product.getMin());

                object.setDescription(description);

                if (BaseActivity.getPrefTaxAndChargeIsActive().equals(BaseActivity.Active))
                    object.setTaxPercent(ServiceTools.getTax(product));
                else
                    object.setTaxPercent(0);

                if (BaseActivity.getPrefTaxAndChargeIsActive().equals(BaseActivity.Active))
                    object.setChargePercent(ServiceTools.getCharge(product));
                else
                    object.setChargePercent(0);

                //////////////////////////////////////////////////////////////
                double FinalPrice = ServiceTools.getCalculateFinalPrice(object, mContext);
                object.setFinalPrice(String.valueOf(FinalPrice));
                ProductPickerListActivity.HashMap_Product.put(product.getProductId(), object);
                InvoiceDetailActivity.orderDetails.add(object);
            }//End of if
        }//End of if
        //Calculate Count and Final Price////////////////////////
        if (ProductPickerListActivity.Type != ProjectInfo.TYPE_RETURN_OF_SALE) {
            if (productPickerListActivity != null) {
                ProductPickerListActivity.CalculationTotal();
                if (productPickerListActivity.productGridGalleryFragment != null)
                    productPickerListActivity.productGridGalleryFragment.dismissDialog();
            }
        } else {
            if (productPickerListActivity != null) {
                ProductPickerListActivity.CalculationTotalReturn();
                if (productPickerListActivity.productGridGalleryFragment != null)
                    productPickerListActivity.productGridGalleryFragment.dismissDialog();
            }
        }
    }

    @Override
    public void return_value_recycler(
            String countPackage,
            String count,
            double price,
            final int position,
            String discount,
            String description,
            int selectedItemPosition,
            double sumCountBaJoz,
            ProductDetail productDetail,
            ArrayList<OrderDetailProperty> orderDetailProperties) {


        double count2 = 0;
        double count1 = 0;
        Product product = products.get(position);

        Price.setText(ServiceTools.formatPrice(price));
        if (!TextUtils.isEmpty(count)) {
            count1 = ServiceTools.toDouble(count);
           Count.setText(count);
        }
        if (!TextUtils.isEmpty(countPackage) && BaseActivity.getPrefUnit2Setting(mContext) != BaseActivity.MODE_YekVahedi) {
            txtTotalCount.setText(countPackage);
            count2 = ServiceTools.toDouble(countPackage);
        }

        Set mapSet = ProductPickerListActivity.HashMap_Product.entrySet();
        Iterator mapIterator = mapSet.iterator();
        OrderDetail item;
        boolean res = false;
        double RealPrice = ServiceTools.toDouble(String.valueOf(ServiceTools.getPriceFromPriceLevel(productDetail.getProductDetailId(), mContext)));
        //validPromotionList(count1 , ServiceTools.MoneyFormatToNumber(price) , product , products);
        while (mapIterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
            int keyValue = (int) mapEntry.getKey();
            if (product.getProductId() == keyValue) {
                item = (OrderDetail) mapEntry.getValue();
                if (!TextUtils.isEmpty(countPackage) && BaseActivity.getPrefUnit2Setting(mContext) != BaseActivity.MODE_YekVahedi) {
                    item.setCount2(count2);
                }
                item.setCount1(count1);
                item.setSumCountBaJoz(sumCountBaJoz);

                if(eshantion_dasti == 1){
                    item.setPrice("0");
                    item.setGiftType(Promotion.Eshantion_Dasti);
                }else {
                    item.setPrice(String.valueOf(price));
                }
                item.setProductName(product.getName());

                if (!BaseActivity.getPrefRowDiscountIsActive().equals(BaseActivity.invisible)) {
                    double d = ServiceTools.RegulartoDouble(BaseActivity.getPrefRowDiscountIsActive());
                    item.setDiscountType((long) d);
                }

                if (!BaseActivity.getPrefRowDiscountIsActive().equals(BaseActivity.invisible))
                    item.setDiscount(ServiceTools.toDouble(discount));

                item.setCostLevel(selectedItemPosition);
                item.setDescription(description);

                if (BaseActivity.getPrefTaxAndChargeIsActive().equals(BaseActivity.Active))
                    item.setTaxPercent(ServiceTools.getTax(product));
                else
                    item.setTaxPercent(0);

                if (BaseActivity.getPrefTaxAndChargeIsActive().equals(BaseActivity.Active))
                    item.setChargePercent(ServiceTools.getCharge(product));
                else
                    item.setChargePercent(0);

                if (ProductPickerListActivity.Type != ProjectInfo.TYPE_RETURN_OF_SALE) {
                    double FinalPrice = ServiceTools.getCalculateFinalPrice(item, mContext);
                    item.setFinalPrice(String.valueOf(FinalPrice));
                }
                ProductPickerListActivity.HashMap_Product.put(product.getProductId(), item);
                res = true;
                break;
            }// End of if
        }// End of While
        product.setSelectedCount(count1);
        if (!res) {
            if (count1 > 0 || count2 > 0) {
                OrderDetail object = new OrderDetail();
                object.setCount1(count1);
                if (!TextUtils.isEmpty(countPackage) && BaseActivity.getPrefUnit2Setting(mContext) != BaseActivity.MODE_YekVahedi) {
                    object.setCount2(count2);
                }
                object.setSumCountBaJoz(sumCountBaJoz);
                //object.setDiscountType(productDetail.getDiscountType());

                if (!BaseActivity.getPrefRowDiscountIsActive().equals(BaseActivity.invisible)) {
                    double d = ServiceTools.RegulartoDouble(BaseActivity.getPrefRowDiscountIsActive());
                    object.setDiscountType((long) d);
                }
                object.setProductDetailId(productDetail.getProductDetailId());
                object.setProductId(product.getProductId());

                if(eshantion_dasti == 1){
                    object.setPrice("0");
                    object.setGiftType(Promotion.Eshantion_Dasti);
                }else {
                    object.setPrice(String.valueOf(price));
                }
                object.setProductName(product.getName());

                object.setMin((int) product.getMin());

                if (!BaseActivity.getPrefRowDiscountIsActive().equals(BaseActivity.invisible))
                    object.setDiscount(ServiceTools.toDouble(discount));

                object.setDescription(description);
                object.setCostLevel(selectedItemPosition);

                if (BaseActivity.getPrefTaxAndChargeIsActive().equals(BaseActivity.Active))
                    object.setTaxPercent(ServiceTools.getTax(product));
                else
                    object.setTaxPercent(0);

                if (BaseActivity.getPrefTaxAndChargeIsActive().equals(BaseActivity.Active))
                    object.setChargePercent(ServiceTools.getCharge(product));
                else
                    object.setChargePercent(0);

                //////////////////////////////////////////////////////////////
                double FinalPrice = ServiceTools.getCalculateFinalPrice(object, mContext);
                object.setFinalPrice(String.valueOf(FinalPrice));
                ProductPickerListActivity.HashMap_Product.put(product.getProductId(), object);
                InvoiceDetailActivity.orderDetails.add(object);
            }//End of if
        }//End of if
        //Calculate Count and Final Price////////////////////////
        if (ProductPickerListActivity.Type != ProjectInfo.TYPE_RETURN_OF_SALE) {
            if (productPickerListActivity != null) {
                ProductPickerListActivity.CalculationTotal();
                if (productPickerListActivity.productGridGalleryFragment != null)
                    productPickerListActivity.productGridGalleryFragment.dismissDialog();
            }
        } else {
            if (productPickerListActivity != null) {
                ProductPickerListActivity.CalculationTotalReturn();
                if (productPickerListActivity.productGridGalleryFragment != null)
                    productPickerListActivity.productGridGalleryFragment.dismissDialog();
            }
        }
    }

    private void showCountPriceDialog(int position, String price, String count, String packageCount, int type, int customerId, long groupId, int productId, String description, int mode, long orderId) {
        Intent intent = new Intent(mContext, PriceCountSelectActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("price", MoneyFormatToNumber(price));
        intent.putExtra("count", count);
        intent.putExtra("count2", packageCount);
        intent.putExtra("type", type);
        intent.putExtra("customerId", customerId);
        intent.putExtra("groupId", groupId);
        intent.putExtra("productId", productId);
        intent.putExtra("fromRecycler", 1);
        intent.putExtra("mode", mode);
        intent.putExtra("OrderId", orderId);
        mContext.startActivity(intent);
    }

    private boolean hasProductDetail(Product product) {
        Gson gson = new Gson();
        Type property = new TypeToken<ArrayList<Properties>>() {}.getType();
        ArrayList<ProductDetail> productDetails = db.getAllProductDetailWithProductId(product.getProductId(), type, mode);
        for (ProductDetail productDetail : productDetails) {
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
                    return true;

                }
            }
        }
        return false;
    }

    public Filter getFilter(long CategoryCode , long CategoryId,int MODE_ASSET) {
        categoryId = CategoryId;
        categoryCode = CategoryCode;
        modeAsset = MODE_ASSET;
        if (Filter == null)
            Filter = new ProductFilterDB2(db,type);
        return Filter;
    }

    public void addAll(ArrayList<Product> allProduct) {
        RecyclerProductAdapter.products.addAll(allProduct);
        notifyDataSetChanged();
    }

    public void clear() {
        RecyclerProductAdapter.products.clear();
        notifyDataSetChanged();
    }

    class ProductFilterDB2 extends Filter {
        DbAdapter dbAdapter;
        int Type;


        public ProductFilterDB2(DbAdapter db, int type) {
            dbAdapter = db;
            Type = type;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String searchStr = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint.toString().length() > 0) {
                Set<Product> filterItem = new LinkedHashSet<>();
                filterItem.addAll(dbAdapter.searchProduct(searchStr,categoryCode, Type , categoryId , modeAsset));
                result.values = new ArrayList<>(filterItem);
                result.count = filterItem.size();
            } else {
                synchronized (this) {
                    result.values = arrayOrginal;
                    result.count = CountProduct;
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            RecyclerProductAdapter.products = (ArrayList<Product>) results.values;
            notifyDataSetChanged();
        }

    }

    public void showPromotionDetail() {

        TextView stair_linear;
        TextView promotion_type;
        RecyclerView promotionDetailRecycler;
        PromotionDetailAdapter promotionDetailAdapter;
        ArrayList<PromotionDetail> promotionDetail = new ArrayList<>();

        // Inflate the layout for this fragment
        View rootView = View.inflate(mContext, R.layout.fragment_promo_details, null);
        stair_linear = (TextView) rootView.findViewById(R.id.stair_linear);
        promotion_type = (TextView) rootView.findViewById(R.id.promotion_type);

        for (Promotion promotion : promotions){
            promotionDetail.addAll(db.getPromotionDetails(promotion.getPromotionCode()));
            if (promotion.getIsCalcLinear() == 1)
                stair_linear.setText(R.string.linear);
            else
                stair_linear.setText(R.string.stair);

            switch (promotion.getAccordingTo()) {
                case Promotion.Mablaghe_kole_Faktor:
                    promotion_type.setText(R.string.total_invoice_than);
                    break;
                case Promotion.Jame_Aghlame_Faktor:
                    promotion_type.setText(R.string.total_invoice_items_than);
                    break;
                case Promotion.Jame_Vazne_Faktor:
                    promotion_type.setText(R.string.total_weight_factor_than);
                    break;
                case Promotion.Jame_anvae_Aghlame_faktor:
                    promotion_type.setText(R.string.total_invoice_types_items_than);
                    break;
                case Promotion.Mablaghe_Satr:
                    promotion_type.setText(R.string.row_amount_than);
                    break;
                case Promotion.Meghdare_Satr:
                    promotion_type.setText(R.string.row_count_than);
                    break;
            }

        }

        promotionDetailRecycler = (RecyclerView) rootView.findViewById(R.id.promotionDetailRecycler);

        promotionDetailRecycler.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        promotionDetailRecycler.setLayoutManager(mLayoutManager);

        promotionDetailAdapter = new PromotionDetailAdapter(promotionDetail, mContext);
        promotionDetailRecycler.setAdapter(promotionDetailAdapter);

        AlertDialog alertDialog = new AlertDialog.Builder(mContext).setView(rootView)
                .setPositiveButton(mContext.getString(R.string.str_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
        alertDialog.setCanceledOnTouchOutside(true);
    }
}
