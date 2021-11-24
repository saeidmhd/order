package com.mahak.order.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.ProductPickerListActivity;
import com.mahak.order.ProductsListActivity;
import com.mahak.order.R;
import com.mahak.order.common.Category;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductCategory;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.threeLevelAdapter.Item;
import com.multilevelview.models.RecyclerViewItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mostafavi on 8/20/2016.
 */


public class ProductListFragment extends Fragment {

    private RecyclerView lstProduct;
    private RecyclerProductAdapter adapterlistProduct;
    private Activity mActivity;
    private ArrayList<Product> array = new ArrayList<>();
    private ArrayList<Long> arrayID = new ArrayList<>();
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
    private long OrderId;
    private int visibleItemCount;
    private int totalItemCount;
    private int firstVisibleItem;
    private int totalItem;
    private DbAdapter db;
    private int CountProduct;
    private LinearLayout  ll_category;
    ArrayList<Product> products = new ArrayList<>();

    int clickedItemCategoryCode = 0;
    int hasItemCategoryCode = 0;

    private RecyclerView category_list;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<Category> existedCategories;
    private Set<Category> categories = new HashSet<>();
    private Set<Integer> ids = new HashSet<>();
    private ArrayList<Category> rootCategories = new ArrayList<>();
    private CategoryAdapter mAdapter;

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
            type = bundle.getInt("type");
            mode = bundle.getInt("Mode");
            CountProduct = bundle.getInt("CountProduct");
            MODE_ASSET = bundle.getInt("MODE_ASSET");
            CategoryId = bundle.getInt("CategoryId");
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
        super.onAttach(context);
        if (context instanceof ProductPickerListActivity)
            productPickerListActivity = (ProductPickerListActivity) context;
        else if (context instanceof ProductsListActivity)
            productsListActivity = (ProductsListActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        db.open();
        //View rootView = inflater.inflate(R.layout.fragment_products_list, container, false);
        View rootView = inflater.inflate(R.layout.fragment_products_list3, container, false);
        lstProduct = (RecyclerView) rootView.findViewById(R.id.lstProduct);
        layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        lstProduct.setLayoutManager(layoutManager);

        category_list = (RecyclerView) rootView.findViewById(R.id.category_list);
        //category_menu = (ImageView) rootView.findViewById(R.id.menu);
        ll_category = (LinearLayout) rootView.findViewById(R.id.ll_category);

        ArrayList<ProductCategory> productCategories = db.getAllProductCategory();

        if (productCategories.size() > 0)
            addAllRelatedCategory();
        else
            ll_category.setVisibility(View.GONE);


        return rootView;
    }


    private void addAllRelatedCategory() {

        category_list.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false);
        mLayoutManager.setReverseLayout(true);
        category_list.setLayoutManager(mLayoutManager);

        existedCategories = db.getAllExistedCategory();
        categories.addAll(existedCategories);
        for (Category existed_category : existedCategories)
            if(existed_category.getParentCode() != 0){
                addAllParentCategory(existed_category.getParentCode());
            }else
                rootCategories.add(existed_category);

        ArrayList<Category> categoryArrayList = new ArrayList<>(categories);
        mAdapter = new CategoryAdapter(rootCategories,categoryArrayList, mContext , db);
        category_list.setAdapter(mAdapter);

    }

    private void addAllParentCategory(int id) {
        if(ids.add(id)){
            ArrayList<Category> parentCategories =  db.getAllParentCategory(id);
            categories.addAll(parentCategories);
            for (Category parentCategory : parentCategories)
                if(parentCategory.getParentCode() != 0){
                    addAllParentCategory(parentCategory.getCategoryCode());
                }else
                    rootCategories.add(parentCategory);
        }
    }


    private List<?> recursivePopulateData(int depth, ArrayList<Category> rootCategories) {
        List<RecyclerViewItem> itemList = new ArrayList<>();
        for (int i = 0; i < depth; i++) {
            Item item = new Item(i);
            item.setText(rootCategories.get(i).getCategoryName());
            item.setCategoryCode(rootCategories.get(i).getCategoryCode());
            ArrayList<Category> LevelCategories = db.getAllCategoryWithParentCode(rootCategories.get(i).getCategoryCode());
            item.setCategoryChildren(LevelCategories);
            item.addChildren((List<RecyclerViewItem>) recursivePopulateData(LevelCategories.size(), LevelCategories));
            itemList.add(item);
        }
        return itemList;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        totalItem = 10;
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

        } else if (productsListActivity != null) {
            array.addAll(ProductsListActivity.arrayProductMain);
            arrayTemp.addAll(ProductsListActivity.arrayProductMain);
            txtSearch = ProductsListActivity.txtSearch;
            MODE_ASSET = ProductsListActivity.MODE_ASSET;
            CategoryId = ProductsListActivity.CategoryId;
        }
        if (mActivity != null) {
            adapterlistProduct = new RecyclerProductAdapter(
                    mActivity,
                    array,
                    productPickerListActivity,
                    R.layout.lst_product_picker_item,
                    type,
                    customerId,
                    GroupId, mode, OrderId);
            lstProduct.setAdapter(adapterlistProduct);
            if (txtSearch != null) {
                adapterlistProduct.getFilter(CategoryId,MODE_ASSET).filter(txtSearch.getText().toString(), new Filter.FilterListener() {
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
                                    getAdapter().addAll(db.getAllProduct(hasItemCategoryCode,CategoryId,ProjectInfo.ASSET_EXIST_PRODUCT,totalItemCount));
                                else
                                    getAdapter().addAll(db.getAllProduct(hasItemCategoryCode,CategoryId,MODE_ASSET,totalItemCount));
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

    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

        private ArrayList<Category> categories;
        private ArrayList<Category> rootCategories;
        private LayoutInflater mInflater;
        private ArrayList<Category> arrayOriginal = new ArrayList<>();
        private Context context;
        private DbAdapter db;
        public  int lastCategoryCode;


        public  class ViewHolder extends RecyclerView.ViewHolder {

            public Button category_name;

            ViewHolder(View itemView) {
                super(itemView);
                category_name = (Button) itemView.findViewById(R.id.categoryName);
            }
        }

        public CategoryAdapter(ArrayList<Category> rootCategories, ArrayList<Category> categories, Context context, DbAdapter db) {

            this.categories = categories;
            this.rootCategories = rootCategories;
            this.mInflater = LayoutInflater.from(context);
            arrayOriginal.addAll(categories);
            this.context = context;
            this.db = db;
        }


        @Override
        public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                     int viewType) {
            View view = mInflater.inflate(R.layout.lst_category_item, parent, false);
            return new CategoryAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CategoryAdapter.ViewHolder holder, final int position) {

            holder.category_name.setText(rootCategories.get(position).getCategoryName());
            holder.category_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Product> products;
                    clickedItemCategoryCode = rootCategories.get(position).getCategoryCode();
                    db.open();
                    if (type == ProjectInfo.TYPE_INVOCIE){
                        products = db.getAllProduct(clickedItemCategoryCode,CategoryId,ProjectInfo.ASSET_EXIST_PRODUCT,totalItemCount);
                    }else
                        products = db.getAllProduct(clickedItemCategoryCode,CategoryId,MODE_ASSET,totalItemCount);

                    if(products.size() > 0){
                        getAdapter().clear();
                        getAdapter().addAll(products);
                        hasItemCategoryCode = clickedItemCategoryCode;
                    }

                    ArrayList<Category> temp = new ArrayList<>();
                    for (Category category : categories){
                        if(category.getParentCode() == rootCategories.get(position).getCategoryCode())
                            temp.add(category);
                    }
                    if(temp.size() > 0 ){
                        rootCategories = temp;
                        notifyDataSetChanged();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return rootCategories.size();
        }
    }



}
