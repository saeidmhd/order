package com.mahak.order.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.ProductPickerListActivity;
import com.mahak.order.ProductsListActivity;
import com.mahak.order.R;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;

public class ProductListFragment extends Fragment {

    private RecyclerView lstProduct;
    private RecyclerProductAdapter adapterlistProduct;
    private Activity mActivity;
    private ArrayList<Product> array = new ArrayList<>();
    private ArrayList<Product> arrayTemp = new ArrayList<>();
    private ProductPickerListActivity productPickerListActivity;
    private ProductsListActivity productsListActivity;
    private Context mContext;
    private int type;
    private int customerId;
    private long GroupId;
    LinearLayoutManager layoutManager;
    private int mode;
    private static int MODE_ASSET;
    private static long CategoryId;
    private static int clickedItemCategoryCode;
    private long OrderId;
    private int eshantion_dasti;
    private int visibleItemCount;
    private int totalItemCount;
    private int firstVisibleItem;
    private DbAdapter db;

    public static ProductListFragment newInstance(int type, int customerId, long groupId, int mode, long orderId, int countProduct) {
        ProductListFragment frag = new ProductListFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("mode", mode);
        args.putLong("CustomerId", customerId);
        args.putLong("GroupId", groupId);
        args.putLong("OrderId", orderId);
        args.putInt("CountProduct", countProduct);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            customerId = bundle.getInt("CustomerId");
            GroupId = bundle.getLong("GroupId");
            OrderId = bundle.getLong("OrderId");
            eshantion_dasti = bundle.getInt("eshantion_dasti");
            type = bundle.getInt("type");
            mode = bundle.getInt("Mode");
            MODE_ASSET = 0;
            CategoryId = 0;
            clickedItemCategoryCode = 0;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_products_list3, container, false);
        lstProduct = (RecyclerView) rootView.findViewById(R.id.lstProduct);
        layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        lstProduct.setLayoutManager(layoutManager);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mActivity = getActivity();
        array = new ArrayList<>();
        arrayTemp = new ArrayList<>();
        db.open();
        ////////////////////////////////////////
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
            adapterlistProduct = new RecyclerProductAdapter(
                    mActivity,
                    array,
                    productPickerListActivity,
                    R.layout.lst_product_picker_item,
                    type,
                    customerId,
                    GroupId, mode, OrderId,eshantion_dasti);
            lstProduct.setAdapter(adapterlistProduct);
            if (txtSearch != null) {
                adapterlistProduct.getFilter(clickedItemCategoryCode,CategoryId,MODE_ASSET).filter(txtSearch.getText().toString(), new Filter.FilterListener() {
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

    public RecyclerProductAdapter getAdapter() {
        return adapterlistProduct;
    }


}
