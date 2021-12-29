package com.mahak.order.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.BaseActivity;
import com.mahak.order.PhotoViewerActivity;
import com.mahak.order.ProductItemInitialize;
import com.mahak.order.ProductPickerListActivity;
import com.mahak.order.ProductsListActivity;
import com.mahak.order.R;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductDetail;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.mahak.order.common.ServiceTools.formatCount;

/**
 * Created by mostafavi on 8/20/2016.
 */
public class ProductGridFragment extends Fragment {

    private static final int BUTTON_COUNT_REFERENCE = 1;
    private static final int BUTTON_GIFT_REFERENCE = 2;
    private long OrderId;
    private int eshantion_dasti;
    //private AdapterListProduct adapterlistProduct;
    private Product SelectedProduct;
    private ProductDetail SelectedProductDetail;
    private long SelectedProductId;
    private Activity mActivity;
    private ArrayList<Product> array = new ArrayList<Product>();
    private ArrayList<Product> arrayTemp = new ArrayList<>();
    private ProductPickerListActivity productPickerListActivity;
    private ProductsListActivity productsListActivity;
    private Context mContext;
    //private GridView gvProduct;
    private RecyclerView lstProduct;
    private RecyclerProductAdapter productAdapter;
    private int type;
    private int customerId;
    private long GroupId;
    GridLayoutManager gridLayoutManager;
    private int mode;

    private int visibleItemCount;
    private int totalItemCount;
    private int firstVisibleItem;
    private int totalItem;
    private DbAdapter db;
    private int CountProduct;
    private static int MODE_ASSET;
    private static long CategoryId;
    private int clickedItemCategoryCode;

    public static ProductGridFragment newInstance(int type, int customerId, long groupId, int mode, long orderId) {
        ProductGridFragment frag = new ProductGridFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("mode", mode);
        args.putInt("CustomerId", customerId);
        args.putLong("GroupId", groupId);
        args.putLong("OrderId", orderId);
        return frag;
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        db = new DbAdapter(mContext);
        db.open();
        super.onAttach(context);
        if (context instanceof ProductPickerListActivity)
            productPickerListActivity = (ProductPickerListActivity) context;
        else if (context instanceof ProductsListActivity)
            productsListActivity = (ProductsListActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            customerId = bundle.getInt("CustomerId");
            GroupId = bundle.getLong("GroupId");
            OrderId = bundle.getLong("OrderId");
            eshantion_dasti = bundle.getInt("eshantion_dasti");
            type = bundle.getInt("type");
            mode = bundle.getInt("Mode");
            CountProduct = bundle.getInt("CountProduct");
            MODE_ASSET = 0;
            CategoryId = 0;
            clickedItemCategoryCode = 0;
        }
        super.onCreate(savedInstanceState);

    }

    public class AdapterListProduct extends ArrayAdapter<Product> {
        Activity mContext;
        ArrayList<Product> arrayOrginal = new ArrayList<Product>();
        ArrayList<Product> arrayProduct = new ArrayList<Product>();
        CustomFilterList Filter;
        DbAdapter db;

        public AdapterListProduct(Activity context, ArrayList<Product> array) {
            super(context, android.R.layout.simple_list_item_1, array);
            mContext = context;
            arrayOrginal.addAll(array);
            arrayProduct.addAll(array);
            db = new DbAdapter(mContext);
        }

        @Override
        public android.widget.Filter getFilter() {
            if (Filter == null)
                Filter = new CustomFilterList();
            return Filter;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            Holder holder;
            final Product product = getItem(position);
            if (product.getPictures() == null) {
                if (db == null) db = new DbAdapter(mContext);
                db.open();
                product.setPictures(db.getAllPictureByProductId(product.getProductCode()));
                db.close();
            }
            if (view == null) {
                LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = mInflater.inflate(R.layout.grv_product_picker_item, parent, false);
                holder = new Holder(view);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }

            holder.Populate(product);

            holder.txtCount.setTag(R.id.ProductId, product.getProductId());
            holder.txtCount.setTag(R.id.Product, product);
            holder.txtTotalGift.setTag(R.id.ProductId, product.getProductId());
            holder.txtTotalGift.setTag(R.id.Product, product);

            holder.txtCount.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("rawtypes")
                @Override
                public void onClick(View v) {

                    if (db == null) db = new DbAdapter(mContext);
                    db.open();
                    double SumCount2 = 0;
                    double SumCount1 = 0;

                    ArrayList<ProductDetail> productDetails = db.getAllProductDetailWithProductId(product.getProductId(), type, mode);

                    for (ProductDetail productDetail : productDetails) {
                        SumCount1 = ServiceTools.getSumCount1(productDetail.getProductId(), mContext);
                        SumCount2 = ServiceTools.getSumCount2(productDetail.getProductId(), mContext);
                    }
                    db.close();

                    double GiftCount1 = 0, Count = 0;
                    double GiftCount2 = 0;
                    SelectedProductId = ServiceTools.toLong(v.getTag(R.id.ProductId).toString());
                    SelectedProduct = (Product) v.getTag(R.id.Product);
                    SelectedProductDetail = (ProductDetail) v.getTag(R.id.Product);
                    Set mapSet = ProductPickerListActivity.HashMap_Product.entrySet();
                    Iterator mapIterator = mapSet.iterator();
                    OrderDetail item;
                    while (mapIterator.hasNext()) {
                        Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                        int keyValue = (int) mapEntry.getKey();
                        if (product.getProductId() == keyValue) {
                            item = (OrderDetail) mapEntry.getValue();
                            GiftCount1 = item.getGiftCount1();
                            GiftCount2 = item.getGiftCount2();
                            Count = item.getCount1();
                            break;
                        }// End of if
                    }// End of While
                }
            });

            return view;
        }

        public class Holder {
            public TextView txtCount, txtTotalCount, tvAsset, tvAsset2, tvInbox, tvPrice, tvProductName, tvUnit, tvUnit2, txtTotalGift;
            public ImageView imgProduct;
            public TextView tvName;
            public View panelTotalCount, panelTotalAsset;

            public Holder(View view) {
                tvName = (TextView) view.findViewById(R.id.tvName);
                tvName.setSelected(true);
                tvAsset = (TextView) view.findViewById(R.id.tvAsset);
                tvAsset2 = (TextView) view.findViewById(R.id.tvAsset2);
                tvInbox = (TextView) view.findViewById(R.id.tvInbox);
                tvPrice = (TextView) view.findViewById(R.id.tvPrice);
                imgProduct = (ImageView) view.findViewById(R.id.imgProduct);
                tvUnit = (TextView) view.findViewById(R.id.tvUnit);
                tvUnit2 = (TextView) view.findViewById(R.id.tvUnit2);
                txtCount = (TextView) view.findViewById(R.id.txtCountKol);
                txtTotalGift = (TextView) view.findViewById(R.id.txtTotalGift);
                txtTotalCount = (TextView) view.findViewById(R.id.txtTotalCount);
                panelTotalAsset = view.findViewById(R.id.panelTotalAsset);
                panelTotalCount = view.findViewById(R.id.panelTotalCount);
                if (productPickerListActivity == null){
                    view.findViewById(R.id.panelCount).setVisibility(View.GONE);
                    view.findViewById(R.id.panelTotalCount).setVisibility(View.GONE);
                }else{
                    view.findViewById(R.id.panelCount).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.panelTotalCount).setVisibility(View.VISIBLE);
                }
            }

            @SuppressWarnings("rawtypes")
            public void Populate(final Product product) {

                Boolean res = false;
                if (db == null) db = new DbAdapter(mContext);
                db.open();
                double SumCount2 = 0;
                double SumCount1 = 0;

                //ProductDetail productDetail = db.getProductDetailWithProductId(product.getProductId());

                ArrayList<ProductDetail> productDetails = db.getAllProductDetailWithProductId(product.getProductId(), type, mode);

                for (ProductDetail productDetail : productDetails) {
                    SumCount1 = ServiceTools.getSumCount1(productDetail.getProductId(), mContext);
                    SumCount2 = ServiceTools.getSumCount2(productDetail.getProductId(), mContext);
                }
                db.close();

                if (BaseActivity.getPrefUnit2Setting(mContext) == BaseActivity.MODE_MeghdarJoz) {
                    panelTotalCount.setVisibility(View.VISIBLE);
                    panelTotalAsset.setVisibility(View.VISIBLE);
                } else {
                    panelTotalCount.setVisibility(View.GONE);
                    panelTotalAsset.setVisibility(View.GONE);
                }

                tvName.setText(product.getName());

                tvAsset.setText(formatCount(SumCount1));
                tvAsset2.setText(formatCount(SumCount2));

                tvInbox.setText(formatCount(product.getUnitRatio()));
                if (productDetails.size() > 0)
                    tvPrice.setText(ServiceTools.getPriceFormated(productDetails.get(0).getProductDetailId(), mContext));
                tvUnit.setText(product.getUnitName());
                tvUnit2.setText(product.getUnitName2());
                // Fill NumberPickerCount && NumberPickerGift/////////////////////////////
                if (product.getPictures() != null && product.getPictures().size() > 0) {
//                        Glide.with(mContext).load(product.getPictures().get(0).getUrl()).placeholder(R.drawable.image_empty_box).crossFade().listener(ServiceTools.getGlideListener(imgProduct))
//                                .into(imgProduct);
                    ProductItemInitialize.loadImage(mContext, product.getPictures().get(0).getUrl(), imgProduct);
                } else {
                    imgProduct.setImageResource(R.drawable.img_default_product);
                    imgProduct.setBackgroundResource(R.drawable.image_empty_box);
                }
                imgProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (product.getPictures() == null || product.getPictures().size() == 0)
                            return;
                        Intent intent = new Intent(mContext, PhotoViewerActivity.class);
                        intent.putExtra(ProjectInfo._json_key_user_id, BaseActivity.getPrefUserId());
                        intent.putExtra(ProjectInfo._json_key_product_id, product.getProductCode());
                        intent.putExtra(ProjectInfo._json_key_index, 0);
                        mContext.startActivity(intent);
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
                            txtCount.setText(formatCount(item.getCount1()));
                            txtTotalGift.setText(formatCount(ServiceTools.getSumGiftCount12(item.getGiftCount1(), item.getGiftCount2(), getActivity())));
                            res = true;
                            break;
                        }// End of if

                    }// End of While
                    if (res == false) {
                        txtCount.setText(ServiceTools.formatCount(0));
                        txtTotalGift.setText(ServiceTools.formatCount(0));
                    }

                    if (BaseActivity.getPrefUnit2Setting(mContext) != BaseActivity.MODE_YekVahedi) {

                        if (product.getUnitRatio() > 0) {
                            txtTotalCount.setText(formatCount(ServiceTools.toDouble(txtCount.getText().toString()) / product.getUnitRatio()));
                        } else {
                            txtTotalCount.setText(ServiceTools.formatCount(0));

                        }
                    }
                }
                /////////////////////////////////////////////////////////////////////////
            }
        }

        public class CustomFilterList extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();
                String name = "";

                if (constraint != null && constraint.toString().length() > 0) {
                    ArrayList<Product> filterItem = new ArrayList<Product>();
                    for (int i = 0; i < arrayOrginal.size(); i++) {
                        Product product = arrayOrginal.get(i);
                        name = product.getName();
                        boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), name);
                        if (result_contain)
                            filterItem.add(product);
                    }
                    result.values = filterItem;
                    result.count = filterItem.size();
                } else {
                    synchronized (this) {
                        result.values = arrayOrginal;
                        result.count = arrayOrginal.size();
                    }
                }
                return result;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                arrayProduct = (ArrayList<Product>) results.values;
                notifyDataSetChanged();
                clear();
                for (int i = 0; i < arrayProduct.size(); i++) {
                    add(arrayProduct.get(i));
                    notifyDataSetInvalidated();
                }
            }
        }

    }// End of AdapterListProduct

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        db.open();
        //View rootView = inflater.inflate(R.layout.fragment_products_grid, container, false);
        View rootView = inflater.inflate(R.layout.fragment_products_grid, container, false);
        lstProduct = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(mContext, 2);
        lstProduct.setLayoutManager(gridLayoutManager);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            type = bundle.getInt("type");
            customerId = bundle.getInt("CustomerId");
            GroupId = bundle.getLong("GroupId");
            OrderId = bundle.getLong("OrderId");
            eshantion_dasti = bundle.getInt("eshantion_dasti");
            CountProduct = bundle.getInt("CountProduct");
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        totalItem = 10;
        mActivity = getActivity();
        db.open();
        ////////////////////////////////////////

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            customerId = bundle.getInt("CustomerId");
            GroupId = bundle.getLong("GroupId");
            OrderId = bundle.getLong("OrderId");
            eshantion_dasti = bundle.getInt("eshantion_dasti");
            type = bundle.getInt("type");
            mode = bundle.getInt("Mode");
            CountProduct = bundle.getInt("CountProduct");
        }


        array = new ArrayList<>();
        arrayTemp = new ArrayList<>();
        TextView txtSearch = null;
        if (productPickerListActivity != null) {
            array.addAll(ProductPickerListActivity.arrayProductMain);
            arrayTemp.addAll(ProductPickerListActivity.arrayProductMain);
            txtSearch = ProductPickerListActivity.txtSearch;
            
            MODE_ASSET = ProductPickerListActivity.MODE_ASSET;
            CategoryId = ProductPickerListActivity.CategoryId;
            clickedItemCategoryCode = ProductPickerListActivity.clickedItemCategoryCode;
            
        } else if (productsListActivity != null) {
            array.addAll(ProductsListActivity.arrayProductMain);
            arrayTemp.addAll(ProductsListActivity.arrayProductMain);
            txtSearch = ProductsListActivity.txtSearch;
            
            MODE_ASSET = ProductsListActivity.MODE_ASSET;
            CategoryId = ProductsListActivity.CategoryId;
            clickedItemCategoryCode = ProductsListActivity.clickedItemCategoryCode;
        }
        if (mActivity != null) {
//            adapterlistProduct = new AdapterListProduct(mActivity, array);
//            gvProduct.setAdapter(adapterlistProduct);
            productAdapter = new RecyclerProductAdapter(mContext, array, productPickerListActivity, R.layout.grv_product_picker_item, type, customerId, GroupId, mode, OrderId,eshantion_dasti);
            lstProduct.setAdapter(productAdapter);

            if (txtSearch != null) {
                productAdapter.getFilter(clickedItemCategoryCode,CategoryId,MODE_ASSET).filter(txtSearch.getText().toString(), new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int count) {
                        //tvPageTitle.setText(getString(R.string.str_nav_product_list)+"("+count+")");
                    }
                });
            }
        }
        final TextView finalTxtSearch = txtSearch;
        lstProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if(finalTxtSearch != null){
                        if(TextUtils.isEmpty(finalTxtSearch.getText())){

                            visibleItemCount = lstProduct.getChildCount();
                            totalItemCount = gridLayoutManager.getItemCount();
                            firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                            if((firstVisibleItem + visibleItemCount) >= totalItemCount ){
                                if (type == ProjectInfo.TYPE_INVOCIE)
                                    getAdapter().addAll(db.getAllProduct(clickedItemCategoryCode,CategoryId,ProjectInfo.ASSET_EXIST_PRODUCT,totalItemCount));
                                else
                                    getAdapter().addAll(db.getAllProduct(clickedItemCategoryCode,CategoryId,MODE_ASSET,totalItemCount));
                            }
                        }
                    }
                }
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    public RecyclerProductAdapter getAdapter() {
        return productAdapter;
    }


}
