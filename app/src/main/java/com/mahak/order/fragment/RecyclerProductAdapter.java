package com.mahak.order.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.BaseActivity;
import com.mahak.order.InvoiceDetailActivity;
import com.mahak.order.PhotoViewerActivity;
import com.mahak.order.PriceCountSelectActivity;
import com.mahak.order.ProductItemInitialize;
import com.mahak.order.ProductPickerListActivity;
import com.mahak.order.R;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.OrderDetailProperty;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductDetail;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mahak.order.BaseActivity.category;
import static com.mahak.order.BaseActivity.getPrefReduceAsset;
import static com.mahak.order.common.ServiceTools.MoneyFormatToNumber;
import static com.mahak.order.common.ServiceTools.formatCount;

public class RecyclerProductAdapter extends RecyclerView.Adapter<ProductHolder> implements ReturnToRecycler {
    private static final int REQUEST_FOR_ACTIVITY_CODE = 100;
    private static Context mContext;
    private long orderId;
    private int mode;
    static public ArrayList<Product> products = new ArrayList<>();
    DbAdapter db;
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
    static int modeAsset;
    public static double mCount = 0;
    private final String description = "";
    int CountProduct;
    public static HashMap<Integer, ArrayList<ProductDetail>> HashMap_productDetail = new LinkedHashMap<>();

    public RecyclerProductAdapter(
            Context mContext,
            List<Product> products,
            ProductPickerListActivity productPickerListActivity,
            int idLayout,
            int type,
            int customerId,
            long groupId,
            int mode,
            long orderId) {
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
        Product product = products.get(position);
        if (product == null)
            product = arrayOrginal.get(position);
        if (product != null) {
            initHolder(product, holder, position, mCount);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Count = v.findViewById(R.id.txtCount);
                    tvProductCode = v.findViewById(R.id.tvProductCode);
                    Price = v.findViewById(R.id.tvPrice);
                    txtTotalCount = v.findViewById(R.id.txtTotalCount);
                    if (type != 0) {
                        showCountPriceDialog(
                                holder.getAbsoluteAdapterPosition(),
                                holder.tvPrice.getText().toString(),
                                holder.txtCount.getText().toString(),
                                holder.txtTotalCount.getText().toString(),
                                type,
                                customerId,
                                groupId,
                                products.get(position).getProductId(),
                                description,
                                mode,
                                orderId);
                    }
                }
            });
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        CountProduct = BaseActivity.getPrefProductCount(mContext);
    }

    public void initHolder(final Product product, final ProductHolder holder, final int position, double mCount) {

        double SumCount2 = 0;
        double SumCount1 = 0;
        double price = 0;
        double customerPrice = 0;

        SumCount1 = product.getSumCount1();
        SumCount2 = product.getSumCount2();
        price = product.getPrice();
        customerPrice = product.getCustomerPrice();

        if (type == 0) {
            holder.panelCount.setVisibility(View.GONE);
        }

        if (product.getPictures() == null) {
            if (db == null) db = new DbAdapter(mContext);
            db.open();
            product.setPictures(db.getAllPictureByProductId(product.getProductCode()));
            db.close();
        }

        Boolean res = false;
        if (BaseActivity.getPrefUnit2Setting(mContext) == BaseActivity.MODE_MeghdarJoz) {
            holder.panelTotalAsset.setVisibility(View.VISIBLE);
            if (productPickerListActivity != null)
                holder.panelTotalCount.setVisibility(View.VISIBLE);
        } else {
            holder.panelTotalCount.setVisibility(View.GONE);
            holder.panelTotalAsset.setVisibility(View.GONE);
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

        if (product.getPictures() != null && product.getPictures().size() > 0) {
            if (product.getPictures().get(0).getUrl() != null)
                ProductItemInitialize.loadImage(mContext, product.getPictures().get(0).getUrl(), holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.img_default_product);
            holder.imgProduct.setBackgroundResource(R.drawable.image_empty_box);
        }
        holder.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.getPictures() == null || product.getPictures().size() == 0)
                    return;
                Intent intent = new Intent(mContext, PhotoViewerActivity.class);
                intent.putExtra(ProjectInfo._json_key_user_id, BaseActivity.getPrefUserId());
                intent.putExtra(ProjectInfo._json_key_product_id, product.getProductCode());
                intent.putExtra(ProjectInfo._json_key_index, 0);
                ((Activity) mContext).startActivityForResult(intent, REQUEST_FOR_ACTIVITY_CODE);
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
                    db.open();
                    OrderDetail orderDetail = db.GetOrderDetailWithId(item.getId());
                    if (type == ProjectInfo.TYPE_INVOCIE || (type == ProjectInfo.TYPE_ORDER && getPrefReduceAsset(mContext))) {
                        holder.tvAsset.setText(ServiceTools.formatCount(orderDetail.getCount1() + orderDetail.getGiftCount1() + SumCount1));
                        holder.tvAsset2.setText(ServiceTools.formatCount(orderDetail.getCount2() + orderDetail.getGiftCount1() + SumCount2));
                    } else {
                        holder.tvAsset.setText(ServiceTools.formatCount(SumCount1));
                        holder.tvAsset2.setText(ServiceTools.formatCount(SumCount2));
                    }
                    holder.txtCount.setText(ServiceTools.formatCount(item.getCount1()));
                    holder.txtTotalCount.setText(ServiceTools.formatCount(item.getCount2()));
                    holder.tvPrice.setText(ServiceTools.formatPrice(item.getPrice()));
                    holder.txtTotalGift.setText(ServiceTools.formatCount(ServiceTools.getSumGiftCount12(item.getGiftCount1(), item.getGiftCount2(), mContext)));
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
                item.setCount2(count2);
                item.setSumCountBaJoz(sumCountBaJoz);

                if (!BaseActivity.getPrefRowDiscountIsActive().equals(BaseActivity.invisible)) {
                    double d = ServiceTools.RegulartoDouble(BaseActivity.getPrefRowDiscountIsActive());
                    item.setDiscountType((long) d);
                }
                item.setPrice(String.valueOf(price));

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
                //object.setProductMasterId(product.getProductCode());
                object.setProductName(product.getName());
                object.setPrice(String.valueOf(price));
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

    public Filter getFilter(long CategoryId,int MODE_ASSET) {
        if (db == null) db = new DbAdapter(mContext);
        db.open();
        categoryId = CategoryId;
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
                if(type == ProjectInfo.TYPE_INVOCIE){
                    filterItem.addAll(dbAdapter.searchProduct(searchStr, Type , categoryId , 1));
                }else
                    filterItem.addAll(dbAdapter.searchProduct(searchStr, Type , categoryId , modeAsset));
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
}
