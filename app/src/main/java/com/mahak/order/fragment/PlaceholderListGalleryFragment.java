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

    private MultiLevelRecyclerView multiLevelRecyclerView;
    private LinearLayout show_all_product, ll_category;


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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db.open();
        //View rootView = inflater.inflate(R.layout.fragment_products_list, container, false);
        View rootView = inflater.inflate(R.layout.fragment_products_list2, container, false);
        lstProduct = (RecyclerView) rootView.findViewById(R.id.lstProduct);
        layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        lstProduct.setLayoutManager(layoutManager);

        multiLevelRecyclerView = (MultiLevelRecyclerView) rootView.findViewById(R.id.rv_list);
        show_all_product = (LinearLayout) rootView.findViewById(R.id.show_all_product);
        ll_category = (LinearLayout) rootView.findViewById(R.id.ll_category);

        multiLevelRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        ArrayList<ProductCategory> productCategories = db.getAllProductCategory();
        /*if (productCategories.size() > 0)
            addAllRelatedCategory();
        else
            ll_category.setVisibility(View.GONE);*/

        ll_category.setVisibility(View.GONE);

        show_all_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshRecyclerview(array);
            }
        });

        return rootView;
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

    private ArrayList<Category> getAllCompanionCategories(int CategoryCode) {
        ArrayList<Category> categories = new ArrayList<>();
        Category category = db.getCategoryWithParentCode(CategoryCode);
        if (category.getParentCode() != 0) {
            getAllCompanionCategories(category.getParentCode());
        }
        categories.add(category);
        return categories;
    }

    private void addAllRelatedCategory() {
        ArrayList<Category> rootCategories;

        rootCategories = db.getAllRootCategories();

        multiLevelRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        List<Item> itemList = (List<Item>) recursivePopulateData(rootCategories.size(), rootCategories);

        MyLevelCategoryAdapter myAdapter = new MyLevelCategoryAdapter(mContext, itemList, multiLevelRecyclerView, getAdapter());

        multiLevelRecyclerView.setAdapter(myAdapter);
        multiLevelRecyclerView.setToggleItemOnClick(false);

        multiLevelRecyclerView.setAccordion(false);

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
        } else if (productsListActivity != null) {
            array.addAll(ProductsListActivity.arrayProductMain);
            arrayOrginal.addAll(array);
            txtSearch = ProductsListActivity.txtSearch;
            MODE_ASSET = ProductsListActivity.MODE_ASSET;
            CategoryId = ProductsListActivity.CategoryId;
        }
        if (mActivity != null) {
            calculateSelectedProduct();
            productAdapter = new RecyclerProductAdapter(mContext, array, productPickerListActivity, R.layout.fragment_item_product_gallery, type, customerId, GroupId, mode, OrderId);
            lstProduct.setAdapter(productAdapter);
            if (txtSearch != null) {
                productAdapter.getFilter(CategoryId,MODE_ASSET).filter(txtSearch.getText().toString(), new Filter.FilterListener() {

                    @Override
                    public void onFilterComplete(int count) {
                        //tvPageTitle.setText(getString(R.string.str_nav_product_list)+"("+count+")");
                    }
                });
            }

            //Set CountProducts ___________________________________________________
            /*if (productPickerListActivity != null)
               // ProductPickerListActivity.tvPageTitle.setText(getString(R.string.str_nav_product_list) + "(" + array.size() + ")");
            if (productsListActivity != null)
                ProductsListActivity.tvPageTitle.setText(getString(R.string.str_nav_product_list) + "(" + array.size() + ")");*/
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
                                    getAdapter().addAll(db.getAllProduct(0,CategoryId,ProjectInfo.ASSET_EXIST_PRODUCT,totalItemCount));
                                else
                                    getAdapter().addAll(db.getAllProduct(0,CategoryId,MODE_ASSET,totalItemCount));
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

    public class MyLevelCategoryAdapter extends MultiLevelAdapter {

        private Holder mViewHolder;
        private Context mContext;
        private List<Item> mListItems = new ArrayList<>();
        private Item mItem;
        private MultiLevelRecyclerView mMultiLevelRecyclerView;
        private RecyclerProductAdapter adapter;
        private DbAdapter db;

        public MyLevelCategoryAdapter(Context mContext, List<Item> mListItems, MultiLevelRecyclerView mMultiLevelRecyclerView, RecyclerProductAdapter adapter) {
            super(mListItems);
            this.mListItems = mListItems;
            this.mContext = mContext;
            this.mMultiLevelRecyclerView = mMultiLevelRecyclerView;
            this.adapter = adapter;
            db = new DbAdapter(mContext);
        }

        private void setExpandButton(ImageView expandButton, boolean isExpanded) {
            expandButton.setImageResource(isExpanded ? R.drawable.ic_keyboard_arrow_up_black_24dp : R.drawable.ic_keyboard_arrow_down_black_24dp);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            mViewHolder = (Holder) holder;
            mItem = mListItems.get(position);

            if (mItem.hasChildren())
                holder.itemView.setBackgroundColor(Color.parseColor("#efefef"));
            else {
                holder.itemView.setBackgroundColor(Color.parseColor("#dedede"));
                mViewHolder.mTitle.setTextSize(16);
            }


        /*switch (getItemViewType(position)) {
            case 1:
                holder.itemView.setBackgroundColor(Color.parseColor("#efefef"));
                break;
            case 2:
                holder.itemView.setBackgroundColor(Color.parseColor("#dedede"));
                break;
            default:
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
        }*/
            mViewHolder.mTitle.setText(mItem.getText());

            if (mItem.hasChildren() && mItem.getChildren().size() > 0) {
                // setExpandButton(mViewHolder.mExpandIcon, mItem.isExpanded());
                mViewHolder.mExpandButton.setVisibility(View.VISIBLE);
            } else {
                mViewHolder.mExpandButton.setVisibility(View.GONE);
            }

            Log.e("MuditLog", mItem.getLevel() + " " + mItem.getPosition() + " " + mItem.isExpanded() + "");

            // indent child items
            // Note: the parent item should start at zero to have no indentation
            // e.g. in populateFakeData(); the very first Item shold be instantiate like this: Item item = new Item(0);
        /*float density = mContext.getResources().getDisplayMetrics().density;
        ((ViewGroup.MarginLayoutParams) mViewHolder.mTextBox.getLayoutParams()).rightMargin = (int) ((getItemViewType(position) * 20) * density + 0.5f);*/
            /*float textSize = mViewHolder.mTitle.getTextSize();
             */
        }


        private class Holder extends RecyclerView.ViewHolder {

            TextView mTitle;
            ImageView mExpandIcon;
            LinearLayout mTextBox, mExpandButton, ll_category;

            Holder(View itemView) {
                super(itemView);
                mTitle = (TextView) itemView.findViewById(R.id.title);
                mExpandIcon = (ImageView) itemView.findViewById(R.id.image_view);
                mTextBox = (LinearLayout) itemView.findViewById(R.id.text_box);
                ll_category = (LinearLayout) itemView.findViewById(R.id.ll_category);
                mExpandButton = (LinearLayout) itemView.findViewById(R.id.expand_field);


                // The following code snippets are only necessary if you set multiLevelRecyclerView.removeItemClickListeners(); in MainActivity.java
                // this enables more than one click event on an item (e.g. Click Event on the item itself and click event on the expand button)
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //set click event on item here

                        // Toast.makeText(mContext, String.format(Locale.ENGLISH, "Item at position %d was clicked!", getAdapterPosition()), Toast.LENGTH_SHORT).show();
                        Toast.makeText(mContext, mListItems.get(getAdapterPosition()).getText(), Toast.LENGTH_SHORT).show();


                    }
                });


                ll_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        db.open();
                        int clickedItemCategoryCode = mListItems.get(getAdapterPosition()).getCategoryCode();
                        Set<Product> products = new HashSet<>();
                        Set<Category> categories = new HashSet<>(mListItems.get(getAdapterPosition()).getCategoryChildren());

                        Set<Category> childCategories = new HashSet<>(addAllChild(new ArrayList<>(categories)));

                        categories.addAll(childCategories);

                        //ArrayList<ProductCategory> productCategories = new ArrayList<>(db.getAllProductCategoryWithCategoryCode(clickedItemCategoryCode));
                        Set<ProductCategory> productCategories = new HashSet<>(db.getAllProductCategoryWithCategoryCode(clickedItemCategoryCode));
                        for (Category category : categories)
                            productCategories.addAll(db.getAllProductCategoryWithCategoryCode(category.getCategoryCode()));

                        for (ProductCategory productCategory : productCategories) {
                            for (Product product : array) {
                                if (productCategory.getProductCode() == product.getProductCode())
                                    products.add(product);
                            }
                        }
                        refreshRecyclerview(new ArrayList<>(products));
                    }
                });

                //set click listener on LinearLayout because the click area is bigger than the ImageView
                mExpandButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //adapter.getFilter().filter();

                        // set click event on expand button here
                        mMultiLevelRecyclerView.toggleItemsGroup(getAdapterPosition());
                        mExpandIcon.animate().rotation(mListItems.get(getAdapterPosition()).isExpanded() ? -180 : 0).start();
                        // refreshRecyclerview(arrayTemp);

                        // setExpandButton(mExpandIcon, mListItems.get(getAdapterPosition()).isExpanded());

                        //Toast.makeText(mContext, String.format(Locale.ENGLISH, "Item at position %d is expanded: %s", getAdapterPosition(), mItem.isExpanded()), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }

    private ArrayList<Category> addAllChild(ArrayList<Category> categories) {
        ArrayList<Category> childCategories = new ArrayList<>();
        for (Category category : categories) {
            if (db.getAllCategoryWithParentCode(category.getCategoryCode()).size() > 0) {
                childCategories.addAll(db.getAllCategoryWithParentCode(category.getCategoryCode()));
                addAllChild(childCategories);
            }
        }
        return childCategories;
    }

    private void refreshRecyclerview(ArrayList<Product> arrayTemp) {
        productAdapter = new RecyclerProductAdapter(
                mActivity,
                arrayTemp,
                productPickerListActivity,
                R.layout.fragment_item_product_gallery,
                type,
                customerId,
                GroupId,
                mode,
                OrderId);

        lstProduct.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
    }

}
