package com.mahak.order.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.ProductItemInitialize;
import com.mahak.order.ProductPickerListActivity;
import com.mahak.order.ProductsListActivity;
import com.mahak.order.R;
import com.mahak.order.common.Category;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductCategory;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.threeLevelAdapter.Item;
import com.multilevelview.MultiLevelAdapter;
import com.multilevelview.MultiLevelRecyclerView;
import com.multilevelview.models.RecyclerViewItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mahak.order.BaseActivity.CUSTOMER_GROUP_KEY;

/**
 * Created by mostafavi on 10/4/2016.
 */
public class PlaceholderListGalleryFragment extends Fragment {

    private static final int BUTTON_COUNT_REFERENCE = 1;
    private static final int BUTTON_GIFT_REFERENCE = 2;
    private RecyclerView lstProduct;
    // private AdapterListProduct adapterlistProduct;
    private Product SelectedProduct;
    private long SelectedProductId;
    private Activity mActivity;
    private ArrayList<Product> array = new ArrayList<Product>();
    private ProductPickerListActivity productPickerListActivity;
    private ProductsListActivity productsListActivity;
    private static Context mContext;
    private DbAdapter db;
    private ProductItemInitialize productItemInitialize;
    private CustomFilterList filter;
    private ArrayList<Product> arrayOrginal = new ArrayList<Product>();
    private RecyclerProductAdapter productAdapter;
    private int type;
    private int customerId;
    private long GroupId;
    LinearLayoutManager layoutManager;
    private int mode;
    private long OrderId;
    private int visibleItemCount;
    private int totalItemCount;
    private int firstVisibleItem;
    private int totalItem;
    private int CountProduct;
    private static int MODE_ASSET;
    private static long CategoryId;
    private int clickedItemCategoryCode;

    public static PlaceholderListGalleryFragment newInstance(int type, int customerId, long groupId, int mode, long orderId) {
        PlaceholderListGalleryFragment frag = new PlaceholderListGalleryFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("mode", mode);
        args.putInt("customerId", customerId);
        args.putLong(CUSTOMER_GROUP_KEY, groupId);
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
        productItemInitialize = new ProductItemInitialize(mContext, productsListActivity, productPickerListActivity);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            type = bundle.getInt("type");
            mode = bundle.getInt("Mode");
            customerId = bundle.getInt("customerId");
            GroupId = bundle.getInt(CUSTOMER_GROUP_KEY);
            OrderId = bundle.getLong("OrderId");
            CountProduct = bundle.getInt("CountProduct");
            MODE_ASSET = 0;
            CategoryId = 0;
            clickedItemCategoryCode = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.fragment_products_list, container, false);
        View rootView = inflater.inflate(R.layout.fragment_products_list3, container, false);
        lstProduct = (RecyclerView) rootView.findViewById(R.id.lstProduct);
        layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        lstProduct.setLayoutManager(layoutManager);

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        totalItem = 10;
        mActivity = getActivity();
        db.open();
        ////////////////////////////////////////
        TextView txtSearch = null;
        if (productPickerListActivity != null) {
            array.addAll(ProductPickerListActivity.arrayProductMain);
            arrayOrginal.addAll(array);
            txtSearch = ProductPickerListActivity.txtSearch;
            MODE_ASSET = ProductPickerListActivity.MODE_ASSET;
            CategoryId = ProductPickerListActivity.CategoryId;
            clickedItemCategoryCode = ProductPickerListActivity.clickedItemCategoryCode;
        } else if (productsListActivity != null) {
            array.addAll(ProductsListActivity.arrayProductMain);
            arrayOrginal.addAll(array);
            txtSearch = ProductsListActivity.txtSearch;
            MODE_ASSET = ProductsListActivity.MODE_ASSET;
            CategoryId = ProductsListActivity.CategoryId;
            clickedItemCategoryCode = ProductsListActivity.clickedItemCategoryCode;
        }
        if (mActivity != null) {
            calculateSelectedProduct();
            productAdapter = new RecyclerProductAdapter(mContext, array, productPickerListActivity, R.layout.fragment_item_product_gallery, type, customerId, GroupId, mode, OrderId);
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
                            totalItemCount = layoutManager.getItemCount();
                            firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                            if((firstVisibleItem + visibleItemCount) >= totalItemCount){
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

    private void calculateSelectedProduct() {
        if (array == null || productPickerListActivity == null)
            return;
        for (int i = 0; i < array.size(); i++) {
            Product product = array.get(i);
            if (ProductPickerListActivity.HashMap_Product != null && ProductPickerListActivity.HashMap_Product.size() > 0 && ProductPickerListActivity.HashMap_Product.containsKey(product.getProductId())) {
                product.setSelectedCount(ProductPickerListActivity.HashMap_Product.get(product.getProductId()).getCount1());
            } else
                product.setSelectedCount(0);
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

            array = (ArrayList<Product>) results.values;
            getAdapter().notifyDataSetChanged();

        }
    }

    public RecyclerProductAdapter getAdapter() {
        return productAdapter;
    }

}
