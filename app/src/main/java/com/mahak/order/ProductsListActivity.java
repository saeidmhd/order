package com.mahak.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter.FilterListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.common.Category;
import com.mahak.order.common.Printer;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductCategory;
import com.mahak.order.common.ProductDetail;
import com.mahak.order.common.ProductGroup;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.SharedPreferencesHelper;
import com.mahak.order.fragment.PlaceholderListGalleryFragment;
import com.mahak.order.fragment.ProductGridFragment;
import com.mahak.order.fragment.ProductGridGalleryFragment;
import com.mahak.order.fragment.ProductListFragment;
import com.mahak.order.fragment.ProductPagerFragment;
import com.mahak.order.fragment.RecyclerProductAdapter;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.widget.DrawableClickListener;
import com.mahak.order.widget.FontAlertDialog;
import com.mahak.order.widget.FontEditText;
import com.mahak.order.widget.FontProgressDialog;
import com.mahak.order.widget.SortDialogActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.mahak.order.common.ProjectInfo.Woosim_WSP_R341;
import static com.mahak.order.common.ServiceTools.formatCount;
import static com.mahak.order.common.ServiceTools.getDateAndTimeForLong;

public class ProductsListActivity extends BaseActivity {

    private static String MODE = "Mode";
    private static String CATEGORYID = "CategoryId";
    private static String SELECTION = "Selction";
    private static String PRODUCT = "Product";
    private static String ASSET_KEY = "Asset";
    public static int MODE_ASSET = 0;
    private static int POSITION = 0;
    private static int LIST_STATE = 1;
    private static int GRID_STATE = 2;
    private static int GALLERY_STATE = 3;
    private static int GALLERY_GRID_STATE = 4;
    private static int GALLERY_PAGE_STATE = 5;
    private static int PAGE_STATE = LIST_STATE;
    private static long DONT_CATEGORYID = 0;
    public static ArrayList<Product> arrayProductMain = new ArrayList<Product>();
    private static boolean FIRST_LOADE_SPN_CATEGORY = false;
    private static boolean FIRST_LOADE_SPN_ASSET = false;
    public static FontEditText txtSearch;
    private static DbAdapter db;
    public static long CategoryId = DONT_CATEGORYID;
    public static boolean openCategory;
    private ArrayList<ProductGroup> arrayProductGroup;
    private Spinner spnCategory, spnAssetProduct;
    private LinearLayout llprogressBar;
    private static Context mContext;
    private Activity mActivity;
    private AsynProduct asyproduct;
    private int CountProduct = 0;
    public static TextView tvPageTitle;
    private String[] ArrayAssetProduct;
    static ImageLoader imageLoader;
    private ProductGridGalleryFragment productGridGalleryFragment;
    private ProductPagerFragment productPageGalleryFragment;
    public static DisplayImageOptions options;
    private PlaceholderListGalleryFragment placeholderGalleryListFragment;
    private ProductListFragment productListFragment;
    private ProductGridFragment productGridFragment;
    private AdapterListProductForPrint _adProduct;
    int printerBrand;
    LinearLayout ll;
    private Date dt = new Date();
    private int totalItem = 0;

    //oscar

    public static int lst_print_kala = R.layout.print_kala_item;
    private FontProgressDialog pd;
    AdapterSpnAssetProduct adspnAssetProduct;

    private ImageView show_category;
    private ImageView close_category;
    private LinearLayout ll_category;
    private LinearLayout category_layout;
    private RecyclerView multiLevelRecyclerView;
    public static int clickedItemCategoryCode = 0;
    static int row_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        setSupportProgressBarIndeterminateVisibility(false);

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
        tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________

        mContext = this;
        mActivity = this;

        printerBrand = SharedPreferencesHelper.getPrefPrinterBrand(mContext);
        lst_print_kala = R.layout.print_kala_item;

        initialise();
        FillSpinner();

        String canShow = ServiceTools.getKeyFromSharedPreferences(mContext, "show" + getPrefUserMasterId(mContext));
        String pathPictures = ServiceTools.getKeyFromSharedPreferences(this, getPrefUserMasterId(mContext) + "");
        if (pathPictures.equals("") && (canShow.equals("") || canShow.equals("1"))) {

            Dialog dialog = Dialog(getString(R.string.str_msg_dialog_choose_folder));
            dialog.show();
            FontAlertDialog.FontDialog(dialog);
        }

        options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.img_default_product)
                .showImageForEmptyUri(R.drawable.img_default_product)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(100))
                .build();

        if (savedInstanceState == null) {
            CategoryId = DONT_CATEGORYID;
            String modeState = ServiceTools.getKeyFromSharedPreferences(mContext, ProjectInfo.pre_mode_state_product);
            if (modeState != null && !modeState.equals(""))
                PAGE_STATE = ServiceTools.toInt(modeState);
            else
                PAGE_STATE = LIST_STATE;
            POSITION = 0;
            MODE_ASSET = 0;
        } else {
            CategoryId = savedInstanceState.getLong(CATEGORYID);
            PAGE_STATE = savedInstanceState.getInt(MODE);
            POSITION = savedInstanceState.getInt(SELECTION);
            MODE_ASSET = savedInstanceState.getInt(ASSET_KEY);
        }

        spnCategory.setSelection(POSITION);
        spnAssetProduct.setSelection(MODE_ASSET);

        db.open();
        getProducts();

        spnCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (POSITION != position) {
                    POSITION = position;
                    //txtSearch.setText("");
                    ProductGroup productGroup = (ProductGroup) parent.getItemAtPosition(position);
                    CategoryId = productGroup.getProductCategoryId();
                    setPageTitle();
                    //Read Product And Fill Adapter///////////////////////////////////////////
                    if (asyproduct != null) {
                        if (asyproduct.getStatus() == Status.RUNNING)
                            asyproduct.cancel(true);
                    }
                    getProducts();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        spnAssetProduct.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setPageTitle();
                if (MODE_ASSET != position) {
                    MODE_ASSET = position;
                    if (asyproduct != null) {
                        if (asyproduct.getStatus() == Status.RUNNING)
                            asyproduct.cancel(true);
                    }
                    getProducts();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        txtSearch.addTextChangedListener(new TextWatcher() {

            private Timer timer=new Timer();
            private final long DELAY = 1000;

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                txtSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cancel_search, 0, R.drawable.ic_search_set_nav, 0);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    txtSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search_set_nav,0 );
                }
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                if (PAGE_STATE == LIST_STATE) {
                                    if (productListFragment != null && productListFragment.getAdapter() != null) {
                                        productListFragment.getAdapter().getFilter(clickedItemCategoryCode,CategoryId,MODE_ASSET).filter(s, new FilterListener() {
                                            @Override
                                            public void onFilterComplete(int count) {
                                                setPageTitle();
                                                productListFragment.getAdapter().notifyDataSetChanged();

                                            }
                                        });
                                    }
                                } else if (PAGE_STATE == GRID_STATE) {
                                    if (productGridFragment != null && productGridFragment.getAdapter() != null) {
                                        productGridFragment.getAdapter().getFilter(clickedItemCategoryCode,CategoryId,MODE_ASSET).filter(s, new FilterListener() {

                                            @Override
                                            public void onFilterComplete(int count) {
                                                setPageTitle();
                                                productGridFragment.getAdapter().notifyDataSetChanged();
                                            }
                                        });
                                    }
                                } else if (PAGE_STATE == GALLERY_STATE) {
                                    if (placeholderGalleryListFragment != null && placeholderGalleryListFragment.getAdapter() != null) {
                                        placeholderGalleryListFragment.getAdapter().getFilter(clickedItemCategoryCode,CategoryId,MODE_ASSET).filter(s, new FilterListener() {

                                            @Override
                                            public void onFilterComplete(int count) {
                                                setPageTitle();
                                                placeholderGalleryListFragment.getAdapter().notifyDataSetChanged();
                                            }
                                        });
                                    }
                                } else if (PAGE_STATE == GALLERY_GRID_STATE) {
                                    if (productGridGalleryFragment != null) {
                                        productGridGalleryFragment.filter(s, new FilterListener() {

                                            @Override
                                            public void onFilterComplete(int count) {
                                                setPageTitle();
                                            }
                                        });
                                    }
                                } else if (PAGE_STATE == GALLERY_PAGE_STATE) {
                                    if (s != null && productPageGalleryFragment != null)
                                        productPageGalleryFragment.searchInProduct(s.toString().toLowerCase());
                                }
                            }
                        },DELAY);
            }
        });

        txtSearch.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case LEFT:
                        txtSearch.setText("");
                        txtSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search_set_nav,0 );
                        break;
                    default:
                        break;
                }
            }
        });

    }// End of onCreate

    public void getProducts() {
        asyproduct = new AsynProduct(clickedItemCategoryCode,CategoryId, PAGE_STATE, MODE_ASSET);
        asyproduct.execute();
    }

    /**
     * Initializing Variables
     */
    private void initialise() {

        db = new DbAdapter(mContext);
        multiLevelRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        ll_category = (LinearLayout) findViewById(R.id.ll_category);
        category_layout = (LinearLayout) findViewById(R.id.category_layout);
        show_category = (ImageView) findViewById(R.id.show_category);
        close_category = (ImageView) findViewById(R.id.close_category);

        spnCategory = (Spinner) findViewById(R.id.spnCategory);
        spnAssetProduct = (Spinner) findViewById(R.id.spnAssetProduct);
        llprogressBar = (LinearLayout) findViewById(R.id.llprogressBar);
        txtSearch = (FontEditText) findViewById(R.id.txtSearch);

        ArrayAssetProduct = getResources().getStringArray(R.array.array_asset_product);
        multiLevelRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        db.open();
        ArrayList<ProductCategory> productCategories = db.getAllProductCategory();

        if(productCategories.size() == 0){
            category_layout.setVisibility(View.GONE);
        }

        if(clickedItemCategoryCode != 0){

            close_category.setVisibility(View.VISIBLE);
            show_category.setVisibility(View.GONE);
            ll_category.setVisibility(View.VISIBLE);

            addAllRelatedCategory();
        }
        show_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productCategories.size() > 0){

                    close_category.setVisibility(View.VISIBLE);
                    show_category.setVisibility(View.GONE);
                    ll_category.setVisibility(View.VISIBLE);

                    addAllRelatedCategory();
                }
            }
        });
        close_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close_category.setVisibility(View.GONE);
                show_category.setVisibility(View.VISIBLE);
                ll_category.setVisibility(View.GONE);
            }
        });
    }

    private void addAllRelatedCategory() {
        ArrayList<Category> rootCategories = new ArrayList<>();
        Category category = new Category();
        category.setCategoryName("???????? ????????????");
        category.setCategoryCode(0);
        rootCategories.add(category);
        rootCategories.addAll(db.getAllRootCategories());
        multiLevelRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        CategoryAdapter2 myAdapter = new CategoryAdapter2(rootCategories, mContext);
        multiLevelRecyclerView.setAdapter(myAdapter);
    }
    /**
     * Fill Spinner ProductGroupSchema
     */
    private void FillSpinner() {

        arrayProductGroup = new ArrayList<ProductGroup>();
        ProductGroup productGroup = new ProductGroup();
        productGroup.setId(DONT_CATEGORYID);
        productGroup.setName(getResources().getString(R.string.str_all));
        arrayProductGroup.add(productGroup);
        ArrayList<ProductGroup> arrayTemp;
        db.open();
        arrayTemp = db.getAllProductGroup();
        db.close();
        arrayProductGroup.addAll(arrayTemp);
        AdapterSpnCategory adspinner = new AdapterSpnCategory(mContext, R.layout.item_spinner, arrayProductGroup);
        spnCategory.setAdapter(adspinner);

        adspnAssetProduct = new AdapterSpnAssetProduct(mContext, R.layout.item_spinner, ArrayAssetProduct);
        spnAssetProduct.setAdapter(adspnAssetProduct);

        //txtSearch.setText("");

    }

    /**
     * Replace Fragment to PlaceholderListFragment
     */
    public void gotoListView() {
        PAGE_STATE = LIST_STATE;
        ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_mode_state_product, PAGE_STATE + "");
        FragmentManager fragment = getSupportFragmentManager();
        if (fragment != null) {
            FragmentTransaction ft = fragment.beginTransaction();
            productListFragment = new ProductListFragment();
            Bundle args = new Bundle();
            args.putInt("CountProduct", CountProduct);
            args.putLong("CategoryId", CategoryId);
            args.putInt("MODE_ASSET", MODE_ASSET);
            productListFragment.setArguments(args);
            ft.replace(R.id.flContent, productListFragment);
            ft.commit();
        }
    }

    /**
     * Replace Fragment to PlaceholderGridFragment
     */
    public void gotoGridView() {

        PAGE_STATE = GRID_STATE;
        ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_mode_state_product, PAGE_STATE + "");
        FragmentManager fm = getSupportFragmentManager();

        if (fm != null) {
            FragmentTransaction ft = fm.beginTransaction();
            productGridFragment = new ProductGridFragment();
            Bundle args = new Bundle();
            args.putInt("CountProduct", CountProduct);
            args.putLong("CategoryId", CategoryId);
            args.putInt("MODE_ASSET", MODE_ASSET);
            productGridFragment.setArguments(args);
            ft.replace(R.id.flContent, productGridFragment);
            ft.commit();
        }

    }

    public void gotoGalleryView() {

        PAGE_STATE = GALLERY_STATE;
        ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_mode_state_product, PAGE_STATE + "");
        FragmentManager fm = getSupportFragmentManager();
        if (fm != null) {
            FragmentTransaction ft = fm.beginTransaction();
            placeholderGalleryListFragment = new PlaceholderListGalleryFragment();
            Bundle args = new Bundle();
            args.putInt("CountProduct", CountProduct);
            args.putLong("CategoryId", CategoryId);
            args.putInt("MODE_ASSET", MODE_ASSET);
            placeholderGalleryListFragment.setArguments(args);
            ft.replace(R.id.flContent, placeholderGalleryListFragment);
            ft.commit();
        }

    }

    public void gotoGalleryGridView() {

        PAGE_STATE = GALLERY_GRID_STATE;
        ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_mode_state_product, PAGE_STATE + "");

        FragmentManager fm = getSupportFragmentManager();

        if (fm != null) {
            FragmentTransaction ft = fm.beginTransaction();
            productGridGalleryFragment = new ProductGridGalleryFragment();
            ft.replace(R.id.flContent, productGridGalleryFragment);
            ft.commit();

        }

    }

    public void gotoGalleryPageView() {

        PAGE_STATE = GALLERY_PAGE_STATE;
        ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_mode_state_product, PAGE_STATE + "");

        FragmentManager fm = getSupportFragmentManager();
        if (fm != null) {
            FragmentTransaction ft = fm.beginTransaction();
            productPageGalleryFragment = new ProductPagerFragment();
            ft.replace(R.id.flContent, productPageGalleryFragment);
            ft.commit();
        }
    }

    /**
     * Reset Value To Default
     */
    public void Clear() {
        FIRST_LOADE_SPN_CATEGORY = false;
        FIRST_LOADE_SPN_ASSET = false;
        CategoryId = DONT_CATEGORYID;
        POSITION = 0;
        PAGE_STATE = LIST_STATE;
    }


    public class PlaceholderListFragment extends Fragment {

        private ListView lstProduct;
        public AdapterListProduct adapterlistProduct;
        private Activity mActivity;
        private ArrayList<Product> array = new ArrayList<Product>();

        public PlaceholderListFragment() {
        }

        public class AdapterListProduct extends ArrayAdapter<Product> {
            Activity mContext;
            ArrayList<Product> arrayOrginal = new ArrayList<Product>();
            ArrayList<Product> arrayProduct = new ArrayList<Product>();

            public AdapterListProduct(Activity context, ArrayList<Product> array) {
                super(context, android.R.layout.simple_list_item_1, array);
                mContext = context;
                arrayOrginal.addAll(array);
                arrayProduct.addAll(array);
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View rowview = convertView;
                Holder holder = null;
                LayoutInflater inflater = null;
                final Product product = getItem(position);
                if (product.getPictures() == null) {
                    if (db == null) db = new DbAdapter(mContext);
                    db.open();
                    product.setPictures(db.getAllPictureByProductId(product.getProductCode()));
                    db.close();
                }
                if (rowview == null) {
                    inflater = mContext.getLayoutInflater();
                    rowview = inflater.inflate(R.layout.lst_product_item, null, false);
                    holder = new Holder(rowview);
                    rowview.setTag(holder);
                } else
                    holder = (Holder) rowview.getTag();
                holder.Populate(product);

                return rowview;
            }

            public class Holder {
                public TextView tvAsset, tvAsset2, tvInbox, tvPrice, tvProductName, tvUnit, tvUnit2;
                public ImageView imgProduct;

                public Holder(View view) {
                    tvProductName = (TextView) view.findViewById(R.id.tvName);
                    tvProductName.setSelected(true);
                    tvAsset = (TextView) view.findViewById(R.id.tvAsset);
                    tvAsset2 = (TextView) view.findViewById(R.id.tvAsset2);
                    tvInbox = (TextView) view.findViewById(R.id.tvInbox);
                    tvPrice = (TextView) view.findViewById(R.id.tvPrice);
                    imgProduct = (ImageView) view.findViewById(R.id.imgProduct);
                    tvUnit = (TextView) view.findViewById(R.id.tvUnit);
                    tvUnit2 = (TextView) view.findViewById(R.id.tvUnit2);
                }

                public void Populate(final Product product) {

                    if (db == null) db = new DbAdapter(mContext);
                    db.open();
                    double SumCount2 = 0;
                    double SumCount1 = 0;

                    ArrayList<ProductDetail> productDetails = db.getAllProductDetailWithProductId(product.getProductId());

                    for (ProductDetail productDetail : productDetails) {
                        SumCount1 = ServiceTools.getSumCount1(productDetail.getProductId(), mContext);
                        SumCount2 = ServiceTools.getSumCount2(productDetail.getProductId(), mContext);
                    }
                    db.close();

                    tvProductName.setText(product.getName());
                    tvAsset.setText(formatCount(SumCount1));
                    tvAsset2.setText(formatCount(SumCount2));
                    tvInbox.setText(formatCount(product.getUnitRatio()));
                    if (productDetails.size() > 0)
                        tvPrice.setText(ServiceTools.getPriceFormated(productDetails.get(0).getProductDetailId(), mContext));
                    tvUnit.setText(product.getUnitName());
                    tvUnit2.setText(product.getUnitName2());
                    if (product.getPictures() != null && product.getPictures().size() > 0) {
                        ProductItemInitialize.loadImage(mContext, product.getPictures().get(0).getUrl(), imgProduct);
                    } else {
                        imgProduct.setImageDrawable(null);
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
                }
            }

        }// End of AdapterListProduct

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_products_list3, container, false);
            lstProduct = (ListView) findViewById(R.id.lstProduct);

            lstProduct.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    DialogFragment dialog = DialogDetailProduct.newInstance(array.get(position));
                    dialog.show(getChildFragmentManager(), "dialog");
                }
            });

            return rootView;
        }


        @Override
        public void onActivityCreated(Bundle savedInstanceState) {

            mActivity = getActivity();
            array.addAll(arrayProductMain);
            if (mActivity != null) {
                adapterlistProduct = new AdapterListProduct(mActivity, array);
                lstProduct.setAdapter(adapterlistProduct);
                adapterlistProduct.getFilter().filter(ProductsListActivity.txtSearch.getText().toString(), new FilterListener() {

                    @Override
                    public void onFilterComplete(int count) {
                        tvPageTitle.setText(getString(R.string.str_nav_product_list) + "(" + count + ")");
                    }
                });

            }
            super.onActivityCreated(savedInstanceState);
        }

    }// End of PlaceholderListFragment

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            sort();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static class PlaceholderGridFragment extends Fragment {

        private GridView gvProduct;
        private Activity mActivity;
        public static AdapterGridProduct adaptergrid;
        private ArrayList<Product> array = new ArrayList<Product>();


        public PlaceholderGridFragment() {
        }

        public class AdapterGridProduct extends BaseAdapter {

            private LayoutInflater mInflater;
            private ArrayList<Product> arrayProduct = new ArrayList<Product>();
            private ArrayList<Product> arrayOrginal = new ArrayList<Product>();
            private Activity mContext;

            public AdapterGridProduct(Activity context, ArrayList<Product> array) {
                mContext = context;
                mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                arrayProduct.addAll(array);
                arrayOrginal.addAll(array);
            }

            @Override
            public int getCount() {
                if (arrayProduct != null) {
                    return arrayProduct.size();
                }
                return 0;
            }

            @Override
            public Object getItem(int position) {
                if (arrayProduct != null && position >= 0 && position < getCount()) {
                    return arrayProduct.get(position);
                }
                return null;
            }

            @Override
            public long getItemId(int position) {
                if (arrayProduct != null && position >= 0 && position < getCount()) {
                    return arrayProduct.get(position).getId();
                }
                return 0;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                View view = convertView;
                Holder holder;
                Product item = (Product) getItem(position);
                if (item.getPictures() == null) {
                    if (db == null) db = new DbAdapter(mContext);
                    db.open();
                    item.setPictures(db.getAllPictureByProductId(item.getProductCode()));
                    db.close();
                }
                if (view == null) {
                    view = mInflater.inflate(R.layout.grv_product_item, parent, false);
                    holder = new Holder(view);
                    view.setTag(holder);
                } else {
                    holder = (Holder) view.getTag();
                }
                holder.Populate(item);

                return view;
            }

            private class Holder {

                public TextView tvName, tvAsset, tvAsset2, tvInbox, tvPrice, tvUnit, tvUnit2;
                public ImageView imgProduct;

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
                }

                public void Populate(final Product product) {

                    if (db == null) db = new DbAdapter(mContext);
                    db.open();
                    double SumCount2 = 0;
                    double SumCount1 = 0;

                    ArrayList<ProductDetail> productDetails = db.getAllProductDetailWithProductId(product.getProductId());

                    for (ProductDetail productDetail : productDetails) {
                        SumCount1 = ServiceTools.getSumCount1(productDetail.getProductId(), mContext);
                        SumCount2 = ServiceTools.getSumCount2(productDetail.getProductId(), mContext);
                    }
                    db.close();

                    tvName.setText(product.getName());
                    tvAsset.setText(formatCount(SumCount1));
                    tvAsset2.setText(formatCount(SumCount2));
                    tvInbox.setText(formatCount(product.getUnitRatio()));
                    if (productDetails.size() > 0)
                        tvPrice.setText(ServiceTools.getPriceFormated(productDetails.get(0).getProductDetailId(), mContext));
                    tvUnit.setText(product.getUnitName());
                    tvUnit2.setText(product.getUnitName2());
                    if (product.getPictures() != null && product.getPictures().size() > 0) {

                        ProductItemInitialize.loadImage(mContext, product.getPictures().get(0).getUrl(), imgProduct);
                    } else {
                        imgProduct.setImageDrawable(null);
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
                }

            }

        }// End Of AdapterGrid

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_products_grid, container, false);

            gvProduct.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    DialogFragment dialog = DialogDetailProduct.newInstance(array.get(position));
                    dialog.show(getChildFragmentManager(), "dialog");

                }
            });

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            mActivity = getActivity();
            array.addAll(arrayProductMain);
            if (mActivity != null) {
                adaptergrid = new AdapterGridProduct(mActivity, array);
                gvProduct.setAdapter(adaptergrid);
            }
            super.onActivityCreated(savedInstanceState);
        }
    }

    public static class DialogDetailProduct extends DialogFragment {

        TextView tvName, tvAsset, tvAsset2, tvUnit, tvUnit2, tvPrice, tvInbox, tvCustomerPrice;
        ImageView imgProduct;
        int position;

        public static DialogDetailProduct newInstance(Product product) {
            DialogDetailProduct fragment = new DialogDetailProduct();
            Bundle args = new Bundle();
            args.putParcelable(PRODUCT, product);
            fragment.setArguments(args);
            return fragment;

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rowview = inflater.inflate(R.layout.dialog_product, container, false);

            getDialog().setTitle(getResources().getString(R.string.str_title_product));
            Product product = null;
            if (getArguments() != null) {
                product = getArguments().getParcelable(PRODUCT);
            }
            if (db == null) db = new DbAdapter(mContext);
            db.open();

            double SumCount2 = 0;
            double SumCount1 = 0;
            ArrayList<ProductDetail> productDetails = null;
            if (product != null) {
                productDetails = db.getAllProductDetailWithProductId(product.getProductId());
            }
            if (productDetails != null) {
                for (ProductDetail productDetail : productDetails) {
                    SumCount1 = ServiceTools.getSumCount1(productDetail.getProductId(), mContext);
                    SumCount2 = ServiceTools.getSumCount2(productDetail.getProductId(), mContext);
                }
            }
            db.close();

            tvName = (TextView) rowview.findViewById(R.id.tvName);
            tvAsset = (TextView) rowview.findViewById(R.id.tvAsset);
            tvPrice = (TextView) rowview.findViewById(R.id.tvPrice);
            tvInbox = (TextView) rowview.findViewById(R.id.tvInbox);
            tvAsset2 = (TextView) rowview.findViewById(R.id.tvAsset2);
            tvUnit2 = (TextView) rowview.findViewById(R.id.tvUnit2);
            tvUnit = (TextView) rowview.findViewById(R.id.tvUnit);
            tvCustomerPrice = (TextView) rowview.findViewById(R.id.tvCustomerPrice);
            imgProduct = (ImageView) rowview.findViewById(R.id.imgProduct);

            tvName.setText(product.getName());
            tvAsset.setText(formatCount(SumCount1));
            if (productDetails.size() > 0)
                tvPrice.setText(ServiceTools.getPriceFormated(productDetails.get(0).getProductDetailId(), mContext));
            tvInbox.setText(formatCount(product.getUnitRatio()));
            tvUnit.setText(product.getUnitName());
            tvUnit2.setText(product.getUnitName2());
            tvAsset2.setText(String.valueOf(SumCount2));

            if (product.getPictures() != null && product.getPictures().size() > 0) {
                if (imageLoader == null) {
                    imageLoader = ImageLoader.getInstance();
                    imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
                }
                imageLoader.displayImage(product.getPictures().get(0).getUrl(), imgProduct);
            }


            return rowview;
        }
    }

    public class AdapterSpnCategory extends ArrayAdapter<ProductGroup> {

        ArrayList<ProductGroup> Objects;

        public AdapterSpnCategory(Context context, int textViewResourceId, ArrayList<ProductGroup> objects) {
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

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.item_spinner, parent, false);
            TextView tvName = (TextView) row.findViewById(R.id.tvName);
            tvName.setText(Objects.get(position).getName());

            return row;
        }
    }// End of AdapterSpnCategory

    public class AdapterSpnAssetProduct extends ArrayAdapter<String> {

        String[] Objects;

        public AdapterSpnAssetProduct(Context context, int textViewResourceId, String[] objects) {
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

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.item_spinner, parent, false);
            TextView tvName = (TextView) row.findViewById(R.id.tvName);
            tvName.setText(Objects[position]);

            return row;
        }
    }// End of AdapterSpnAssetProduct

    public class AsynProduct extends AsyncTask<String, String, Boolean> {
        long Id;
        int StatePage;
        int ModeAssetProduct;
        int CategoryCode;

        public AsynProduct(int CategoryCode , long id, int statepage, int modeasset) {

            this.Id = id;
            this.StatePage = statepage;
            this.ModeAssetProduct = modeasset;
            this.CategoryCode = CategoryCode;
        }

        @Override
        protected void onPreExecute() {
            setSupportProgressBarIndeterminateVisibility(true);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            db.open();
            BaseActivity.setPrefDefSellPrice(ServiceTools.getPrefDefPrice(db,0,0));
            arrayProductMain = db.getAllProduct("", CategoryCode,Id, ModeAssetProduct, 0);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            setSupportProgressBarIndeterminateVisibility(false);

                if (StatePage == LIST_STATE)
                    gotoListView();
                else if (PAGE_STATE == GRID_STATE)
                    gotoGridView();
                else if (PAGE_STATE == GALLERY_STATE)
                    gotoGalleryView();
                else if (PAGE_STATE == GALLERY_GRID_STATE)
                    gotoGalleryGridView();
                else if (PAGE_STATE == GALLERY_PAGE_STATE)
                    gotoGalleryPageView();

                setPageTitle();

            super.onPostExecute(result);
        }

    }

    public void setPageTitle() {
        db.open();
        CountProduct = db.getTotalCountProduct(txtSearch.getText().toString(), clickedItemCategoryCode , CategoryId , MODE_ASSET);
        tvPageTitle.setText(getString(R.string.str_nav_product_list) + "(" + CountProduct + ")");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_products_list, menu);
        menu.add(0, 1, 1, R.string.Print).setIcon(R.drawable.ic_print_white_48dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.removeItem(R.id.mnuBarcode);

        return true;
    }

    public void FillPrintView(View view) {
        //controls
        ListView _lstProduct = (ListView) view.findViewById(R.id._lstProduct);
        TextView _tvOrderDate = (TextView) view.findViewById(R.id._tvOrderDate);
        TextView _tvUsername = (TextView) view.findViewById(R.id._tvUsername);
        _tvOrderDate.setText(getDateAndTimeForLong(dt.getTime()));
        if (BaseActivity.getAuthentication())
            _tvUsername.setText(BaseActivity.getUserProfile().getName());
        _adProduct = new AdapterListProductForPrint(mActivity, RecyclerProductAdapter.products);
        _lstProduct.setDrawingCacheEnabled(true);
        _lstProduct.setAdapter(_adProduct);
        ServiceTools.setListViewHeightBasedOnChildren(_lstProduct);

    }

    private class PreparePrinterData extends AsyncTask<String, Integer, Boolean> {

        Bitmap b = null;
        String fName = "";
        String fPath = ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_IMAGES + "/" + ProjectInfo.DIRECTORY_INVOICES;

        @Override
        protected void onPreExecute() {
            llprogressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean status = false;

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ll = new LinearLayout(mContext);
            if (printerBrand == ProjectInfo.PRINTER_BABY_380_A || printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310 || printerBrand == ProjectInfo.PRINTER_DELTA_380_A) {
                inflater.inflate(R.layout.kala_print_template_80mm, ll, true);
            } else if (printerBrand == ProjectInfo.PRINTER_BABY_280_A) {
                inflater.inflate(R.layout.kala_print_template_50mm, ll, true);
            } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII || printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW || printerBrand == ProjectInfo.UROVO_K319 || printerBrand == Woosim_WSP_R341) {
                ll.setDividerPadding(2);
                ll.setBackgroundColor(getResources().getColor(R.color.black));
                inflater.inflate(R.layout.kala_print_template_80mm_3parts, ll, true);
                LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                ChangePrintWidth(_llPrint);
            } else {
                inflater.inflate(R.layout.kala_print_template_60mm, ll, true);
            }

            FillPrintView(ll);
            ll.setDrawingCacheEnabled(true);
            ll.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            ll.layout(0, 0, ll.getMeasuredWidth(), ll.getMeasuredHeight());
            ll.buildDrawingCache(true);
            //b = Bitmap.createBitmap(ll.getDrawingCache());
            b = Printer.CreateBitmap(ll);
            ll.setDrawingCacheEnabled(false);

            fName = GetFileName(dt.getTime());
            if (b != null) {
                if (Printer.CreateFile(b, fName, fPath)) {
                }
            }

            if (b != null)
                status = true;

            return status;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                Intent intent = new Intent(ProductsListActivity.this, PrintActivity.class);
                intent.putExtra(ProjectInfo._TAG_PAGE_NAME, ProjectInfo._pName_OrderDetail);
                intent.putExtra(ProjectInfo._TAG_PATH, fPath);
                intent.putExtra(ProjectInfo._TAG_Name, fName);
                startActivity(intent);
                llprogressBar.setVisibility(View.GONE);
            } else {
            }
        }
    }

    public void ChangePrintWidth(LinearLayout ll) {

        LayoutParams param = ll.getLayoutParams();
        final float scale = getResources().getDisplayMetrics().density;
        //convert mm to dp
        double Size = SharedPreferencesHelper.getCurrentWidthSize(mContext) * 6.3;
        int converter = (int) (Size * scale + 0.5f);
        param.width = converter;
        ll.setLayoutParams(param);

    }

    public String GetFileName(long date) {
        return ServiceTools.getFileName(date) + ".png";
    }

    public class AdapterListProductForPrint extends ArrayAdapter<Product> {
        Activity mContext;

        public AdapterListProductForPrint(Activity context, ArrayList<Product> array) {

            super(context, lst_print_kala, array);
            mContext = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowview = convertView;
            Holder holder = null;
            LayoutInflater inflater = null;

            final Product product = getItem(position);

            if (rowview == null) {
                inflater = mContext.getLayoutInflater();
                rowview = inflater.inflate(lst_print_kala, null, false);
                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();

            holder.Populate(product, position);

            return rowview;
        }

        public class Holder {
            public TextView tvProductName, tvNumber, tvCount, tvKalaCode;
            public LinearLayout llitem;

            public Holder(View view) {

                llitem = (LinearLayout) view.findViewById(R.id.llitem);
                tvProductName = (TextView) view.findViewById(R.id.tvProductNameSpec);
                tvCount = (TextView) view.findViewById(R.id.tvCount);
                tvKalaCode = (TextView) view.findViewById(R.id.tvKalaCode);

            }

            public void Populate(Product product, int position) {
                tvProductName.setText(product.getName());
                tvCount.setText(ServiceTools.formatCount(product.getSumCount1()));
                tvKalaCode.setText(String.valueOf(product.getProductCode()));
            }
        }

    }// End of AdapterListProduct

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuListview:
                if (PAGE_STATE != LIST_STATE)
                    gotoListView();
                break;
            case R.id.mnuGridview:
                if (PAGE_STATE != GRID_STATE)
                    gotoGridView();
                break;
            case R.id.mnuGalleryView:
                if (PAGE_STATE != GALLERY_STATE)
                    gotoGalleryView();
                break;
            /*case R.id.mnuGalleryGridView:
                if (PAGE_STATE != GALLERY_GRID_STATE)
                    gotoGalleryGridView();
                break;
            case R.id.mnuGalleryPageView:
                if (PAGE_STATE != GALLERY_PAGE_STATE)
                    gotoGalleryPageView();
                break;*/
            case R.id.home:
                if (asyproduct != null) {
                    if (asyproduct.getStatus() == Status.RUNNING) {
                        asyproduct.cancel(true);
                    }
                }
                Clear();
                finish();
                break;
            case 1:
                PreparePrinterData ppd = new PreparePrinterData();
                ppd.execute();
                break;
            case R.id.mnuSort:
                Intent intent = new Intent(ProductsListActivity.this, SortDialogActivity.class);
                intent.putExtra("type", "product");
                startActivityForResult(intent, 1001);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putInt(MODE, PAGE_STATE);
        outState.putLong(CATEGORYID, CategoryId);
        outState.putInt(SELECTION, POSITION);
        outState.putInt(ASSET_KEY, MODE_ASSET);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        PAGE_STATE = savedInstanceState.getInt(MODE);
        CategoryId = savedInstanceState.getLong(CATEGORYID);
        POSITION = savedInstanceState.getInt(SELECTION);
    }

    @Override
    public void onBackPressed() {

        if (asyproduct != null) {
            if (asyproduct.getStatus() == Status.RUNNING) {
                asyproduct.cancel(true);

            }
        }
        Clear();
        finish();
        super.onBackPressed();
    }

    public Dialog Dialog(String msg) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(FontAlertDialog.getFontTitle(getString(R.string.str_msg_choose_folder_pictures)))
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.str_ok), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNeutralButton(getString(R.string.str_setting), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(mContext, SettingActivity.class));

                    }
                })
                .setNegativeButton(getString(R.string.str_btn_dont_show_again), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ServiceTools.setKeyInSharedPreferences(mContext, "show" + getPrefUserMasterId(mContext), "0");
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();


        return dialog;
    }

    public void sort() {
        getProducts();
    }

    public class CategoryAdapter2 extends RecyclerView.Adapter<CategoryAdapter2.ViewHolder> {

        private ArrayList<Category> categories;
        private LayoutInflater mInflater;
        private ArrayList<Category> arrayOriginal = new ArrayList<>();


        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView mTitle;
            LinearLayout mTextBox, item_category;

            ViewHolder(View itemView) {
                super(itemView);
                mTitle = (TextView) itemView.findViewById(R.id.title);
                mTextBox = (LinearLayout) itemView.findViewById(R.id.text_box);
                item_category = (LinearLayout) itemView.findViewById(R.id.item_category);
            }
        }

        public CategoryAdapter2( ArrayList<Category> categories, Context context) {

            this.categories = categories;
            this.mInflater = LayoutInflater.from(context);
            arrayOriginal.addAll(categories);
        }

        @Override
        public CategoryAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_layout2, parent, false);
            return new CategoryAdapter2.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CategoryAdapter2.ViewHolder holder, final int position) {
            holder.mTitle.setText(categories.get(position).getCategoryName());

            holder.item_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickedItemCategoryCode = categories.get(position).getCategoryCode();
                    getProducts();
                    row_index=position;
                    notifyDataSetChanged();
                }
            });

            if(row_index==position){
                holder.item_category.setBackgroundColor(Color.parseColor("#dedede"));
            }
            else
            {
                holder.item_category.setBackgroundColor(Color.parseColor("#f4f4f4"));
            }
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }
    }


}
