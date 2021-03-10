package com.mahak.order.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.BaseActivity;
import com.mahak.order.PhotoViewerActivity;
import com.mahak.order.ProductItemInitialize;
import com.mahak.order.ProductPickerListActivity;
import com.mahak.order.ProductsListActivity;
import com.mahak.order.R;
import com.mahak.order.common.Category;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductCategory;
import com.mahak.order.common.ProductDetail;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.threeLevelAdapter.Item;
import com.multilevelview.MultiLevelAdapter;
import com.multilevelview.MultiLevelRecyclerView;
import com.multilevelview.models.RecyclerViewItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
    private static int CategoryId;
    private LinearLayout show_all_product, ll_category;

    private MultiLevelRecyclerView multiLevelRecyclerView;

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
            type = bundle.getInt("type");
            mode = bundle.getInt("Mode");
            CountProduct = bundle.getInt("CountProduct");
            MODE_ASSET = bundle.getInt("MODE_ASSET");
            CategoryId = bundle.getInt("CategoryId");
        }
        super.onCreate(savedInstanceState);

    }

    public class AdapterListProduct extends ArrayAdapter<Product> {
        Activity mcontaxt;
        ArrayList<Product> arrayOrginal = new ArrayList<Product>();
        ArrayList<Product> arrayProduct = new ArrayList<Product>();
        CustomFilterList Filter;
        DbAdapter db;

        public AdapterListProduct(Activity contaxt, ArrayList<Product> array) {
            super(contaxt, android.R.layout.simple_list_item_1, array);
            mcontaxt = contaxt;
            arrayOrginal.addAll(array);
            arrayProduct.addAll(array);
            db = new DbAdapter(mcontaxt);
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
                LayoutInflater mInflater = (LayoutInflater) mcontaxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                if (productPickerListActivity == null)
                    view.findViewById(R.id.panelCount).setVisibility(View.GONE);
                else
                    view.findViewById(R.id.panelCount).setVisibility(View.VISIBLE);
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
                        boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), name.toLowerCase());
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
        View rootView = inflater.inflate(R.layout.fragment_products_grid2, container, false);
        lstProduct = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(mContext, 3);
        lstProduct.setLayoutManager(gridLayoutManager);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            type = bundle.getInt("type");
            customerId = bundle.getInt("CustomerId");
            GroupId = bundle.getLong("GroupId");
            OrderId = bundle.getLong("OrderId");
            CountProduct = bundle.getInt("CountProduct");
            MODE_ASSET = bundle.getInt("MODE_ASSET");
            CategoryId = bundle.getInt("CategoryId");
        }

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
                refreshRecyclerview(arrayTemp);
            }
        });


        return rootView;
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
        db.open();
        ////////////////////////////////////////

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


        array = new ArrayList<>();
        arrayTemp = new ArrayList<>();
        TextView txtSearch = null;
        if (productPickerListActivity != null) {
            array.addAll(ProductPickerListActivity.arrayProductMain);
            arrayTemp.addAll(ProductPickerListActivity.arrayProductMain);
            txtSearch = ProductPickerListActivity.txtSearch;
        } else if (productsListActivity != null) {
            array.addAll(ProductsListActivity.arrayProductMain);
            arrayTemp.addAll(ProductsListActivity.arrayProductMain);
            txtSearch = ProductsListActivity.txtSearch;
        }
        if (mActivity != null) {
//            adapterlistProduct = new AdapterListProduct(mActivity, array);
//            gvProduct.setAdapter(adapterlistProduct);
            productAdapter = new RecyclerProductAdapter(mContext, array, productPickerListActivity, R.layout.grv_product_picker_item, type, customerId, GroupId, mode, OrderId);
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
                ProductPickerListActivity.tvPageTitle.setText(getString(R.string.str_nav_product_list) + "(" + array.size() + ")");
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
                            totalItemCount = gridLayoutManager.getItemCount();
                            firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                            if((firstVisibleItem + visibleItemCount) >= totalItemCount ){
                                if (type == ProjectInfo.TYPE_INVOCIE)
                                    getAdapter().addAll(db.getAllProduct(CategoryId,ProjectInfo.ASSET_EXIST_PRODUCT,totalItemCount));
                                else
                                    getAdapter().addAll(db.getAllProduct(CategoryId,MODE_ASSET,totalItemCount));
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
                            for (Product product : arrayTemp) {
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
                mContext,
                arrayTemp,
                productPickerListActivity,
                R.layout.grv_product_picker_item,
                type,
                customerId,
                GroupId,
                mode,
                OrderId);
        lstProduct.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
    }


}
