package com.mahak.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter.FilterListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kishcore.sdk.hybrid.api.SDKManager;
import com.mahak.order.common.Category;
import com.mahak.order.common.OrderDetail;
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
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.threeLevelAdapter.Item;
import com.mahak.order.widget.FontAlertDialog;
import com.mahak.order.widget.SortDialogActivity;
import com.multilevelview.MultiLevelAdapter;
import com.multilevelview.MultiLevelRecyclerView;
import com.multilevelview.models.RecyclerViewItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static com.mahak.order.common.ServiceTools.formatCount;


public class ProductPickerListActivity extends BaseActivity {

    private static final String MODE = "Mode";
    private static final String CATEGORYID = "CategoryId";
    private static final String SELECTION = "Selction";
    private static final String ASSET_KEY = "Asset";
    public static int MODE_ASSET = 0;
    private static int POSITION;
    private static int PAGE_STATE;
    private static final int LIST_STATE = 1;
    private static final int GRID_STATE = 2;
    private static final int GALLERY_STATE = 3;
    private static final int GALLERY_GRID_STATE = 4;
    private static final int GALLERY_PAGE_STATE = 5;
    private static final long DONT_CATEGORYID = 0;
    private boolean FIRST_LOADE_SPN_CATEGORY = false, FIRST_LOADE_SPN_ASSET = false, RetuenAsset = false;
    public static TextView txtSearch;
    private static DbAdapter db;
    public static long CategoryId;

    public static ArrayList<Product> arrayProductMain = new ArrayList<>();
    public static LinkedHashMap<Integer, OrderDetail> HashMap_Product = new LinkedHashMap<>();
    public static final List<OrderDetail> Product_Delete = new ArrayList<>();

    private ArrayList<ProductGroup> arrayProductGroup;
    private Spinner spnCategory, spnAssetProduct;
    private static Context mContext;
    private Activity mActivity;
    private Button btnAddCart;
    private LinearLayout ll_product_picker;
    private static TextView tvTotalCount, txtTotalCount;
    private static TextView tvTotalPrice;
    private static double TotalPrice;
    private static double TotalCount;
    private Bundle Extras;
    private static int page;
    public static int Type;
    private static int Mode;
    public static int CustomerId;
    public static long GroupId;
    private AsyncProduct asynproduct;
    public static TextView tvPageTitle;
    private int CountProduct = 0;
    private String[] ArrayAssetProduct;
    public ProductGridGalleryFragment productGridGalleryFragment;
    private ProductPagerFragment productPageGalleryFragment;
    public static DisplayImageOptions options;
    private PlaceholderListGalleryFragment placeholderListGalleryFragment;
    private ProductListFragment productListFragment;
    private ProductGridFragment productGridFragment;
    private long OrderId;

    AdapterSpnAssetProduct adspnAssetProduct;

    private int totalItem = 0;
    private int printerBrand;

    private ImageView show_category;
    private ImageView close_category;
    private LinearLayout ll_category;
    private LinearLayout show_all_product;
    private MultiLevelRecyclerView multiLevelRecyclerView;
    public static int clickedItemCategoryCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_picker_list);

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
        tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________

        options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.img_default_product)
                .showImageForEmptyUri(R.drawable.img_default_product)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(100))
                .build();

        mContext = this;
        mActivity = this;

        printerBrand = SharedPreferencesHelper.getPrefPrinterBrand(mContext);
        if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223)
            SDKManager.init(this);

        //   bus.register(this);

        Extras = getIntent().getExtras();
        if (Extras != null) {

            Mode = Extras.getInt(MODE_PAGE);
            page = Extras.getInt(PAGE);
            OrderId = Extras.getLong("OrderId");

            if (Mode == MODE_NEW) {
                CustomerId = Extras.getInt(CUSTOMERID_KEY);
                Type = Extras.getInt(TYPE_KEY);
                GroupId = Extras.getLong(CUSTOMER_GROUP_KEY);
            } else if (Mode == MODE_EDIT) {
                RetuenAsset = Extras.getBoolean(RETURN_ASSET_KEY);
                Type = Extras.getInt(TYPE_KEY);
            }
        }

        initialise();

        db.open();

        FillSpinner();

        String canShow = ServiceTools.getKeyFromSharedPreferences(mContext, "show" + getPrefUserMasterId(mContext));
        String pathPictures = ServiceTools.getKeyFromSharedPreferences(this, getPrefUserMasterId(mContext) + "");
        if (pathPictures.equals("") && (canShow.equals("") || canShow.equals("1"))) {
            Dialog dialog = Dialog(getString(R.string.str_msg_dialog_choose_folder));
            dialog.show();
            FontAlertDialog.FontDialog(dialog);
        }

        ll_product_picker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });


        if (savedInstanceState == null) {
            CategoryId = DONT_CATEGORYID;
            String modeState = ServiceTools.getKeyFromSharedPreferences(mContext, ProjectInfo.pre_mode_state_product);
            if (modeState != null && !modeState.equals(""))
                PAGE_STATE = ServiceTools.toInt(modeState);
            else
                PAGE_STATE = LIST_STATE;
            POSITION = 0;
            MODE_ASSET = 0;
            if (Type == ProjectInfo.TYPE_SEND_TRANSFERENCE
                    || Type == ProjectInfo.TYPE_INVOCIE ||
                    (Type == ProjectInfo.TYPE_ORDER && getPrefReduceAsset(mContext))) {
                MODE_ASSET = ProjectInfo.ASSET_EXIST_PRODUCT;
                //spnAssetProduct.setEnabled(false);
            } else
                MODE_ASSET = ProjectInfo.ASSET_ALL_PRODUCT;

            spnAssetProduct.setSelection(MODE_ASSET);
            asynproduct = new AsyncProduct(clickedItemCategoryCode ,CategoryId, PAGE_STATE, Mode, MODE_ASSET);
            asynproduct.execute();
        } else {

            CategoryId = savedInstanceState.getLong(CATEGORYID);
            PAGE_STATE = savedInstanceState.getInt(MODE);
            POSITION = savedInstanceState.getInt(SELECTION);
            MODE_ASSET = savedInstanceState.getInt(ASSET_KEY);
            spnCategory.setSelection(POSITION);
            spnAssetProduct.setSelection(MODE_ASSET);
            getProducts();
        }

        spnCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (POSITION != position) {
                    POSITION = position;
                    ProductGroup productGroup = (ProductGroup) parent.getItemAtPosition(position);
                    CategoryId = productGroup.getProductCategoryId();
                    setPageTitle();
                    //Read Product And Fill Adapter///////////////////////////////////////////
                    if (asynproduct != null) {
                        if (asynproduct.getStatus() == Status.RUNNING)
                            asynproduct.cancel(true);
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
                    //Read Product And Fill Adapter///////////////////////////////////////////
                    if (asynproduct != null) {
                        if (asynproduct.getStatus() == Status.RUNNING)
                            asynproduct.cancel(true);
                    }
                    getProducts();
                    /////////////////////////////////////////////////////////////////////////
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
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                                    if (placeholderListGalleryFragment != null && placeholderListGalleryFragment.getAdapter() != null) {
                                        placeholderListGalleryFragment.getAdapter().getFilter(clickedItemCategoryCode,CategoryId,MODE_ASSET).filter(s, new FilterListener() {

                                            @Override
                                            public void onFilterComplete(int count) {
                                                setPageTitle();
                                                placeholderListGalleryFragment.getAdapter().notifyDataSetChanged();

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

        btnAddCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //ClearZero();
                if (Mode == MODE_NEW) {
                    if (HashMap_Product.size() != 0) {
                        Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                        intent.putExtra(CUSTOMERID_KEY, CustomerId);
                        intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                        intent.putExtra(Category_GROUP_KEY, CategoryId);
                        intent.putExtra(PAGE, page);
                        intent.putExtra(TYPE_KEY, Type);
                        intent.putExtra(MODE_PAGE, Mode);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(mContext, getResources().getString(R.string.str_message_no_product), Toast.LENGTH_SHORT).show();
                    }
                } else if (Mode == MODE_EDIT) {
                    if (HashMap_Product.size() != 0) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(mContext, getResources().getString(R.string.str_message_no_product), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }// End Of OnCreate

    private void getProducts() {
        asynproduct = new AsyncProduct(clickedItemCategoryCode,CategoryId, PAGE_STATE, Mode, MODE_ASSET);
        asynproduct.execute();
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(ll_product_picker.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void setPageTitle() {
        db.open();
        CountProduct = db.getTotalCountProduct(txtSearch.getText().toString(), clickedItemCategoryCode ,CategoryId , MODE_ASSET);
        tvPageTitle.setText(getString(R.string.str_nav_product_list) + "(" + CountProduct + ")");
    }

    /**
     * Initializing Variables
     */

    private void initialise() {

        btnAddCart = (Button) findViewById(R.id.btnAddCart);
        ll_product_picker = (LinearLayout) findViewById(R.id.ll_product_picker);
        spnCategory = (Spinner) findViewById(R.id.spnCategory);
        spnAssetProduct = (Spinner) findViewById(R.id.spnAssetProduct);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        tvTotalCount = (TextView) findViewById(R.id.tvTotalCount);
        txtTotalCount = (TextView) findViewById(R.id.txtTotalCount);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);

        if (Type == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
            btnAddCart.setText(R.string.add_to_transfer_list);
            tvTotalPrice.setVisibility(View.GONE);
            txtTotalCount.setVisibility(View.GONE);
        }
        if (Type == ProjectInfo.TYPE_RETURN_OF_SALE) {
            btnAddCart.setText(R.string.add_to_return_list);
            tvTotalPrice.setVisibility(View.GONE);
            txtTotalCount.setVisibility(View.GONE);
        }

        ArrayAssetProduct = getResources().getStringArray(R.array.array_asset_product);
        db = new DbAdapter(mContext);

        multiLevelRecyclerView = (MultiLevelRecyclerView) findViewById(R.id.rv_list);
        show_all_product = (LinearLayout) findViewById(R.id.show_all_product);
        ll_category = (LinearLayout) findViewById(R.id.ll_category);
        show_category = (ImageView) findViewById(R.id.show_category);
        close_category = (ImageView) findViewById(R.id.close_category);

        multiLevelRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        db.open();
        ArrayList<ProductCategory> productCategories = db.getAllProductCategory();

       /* if(clickedItemCategoryCode != 0){

            close_category.setVisibility(View.VISIBLE);
            show_category.setVisibility(View.GONE);
            ll_category.setVisibility(View.VISIBLE);

            addAllRelatedCategory();
        }*/

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

        show_all_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedItemCategoryCode = 0;
                getProducts();
            }
        });
    }

    private void addAllRelatedCategory() {
        ArrayList<Category> rootCategories;
        rootCategories = db.getAllRootCategories();

        multiLevelRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        List<Item> itemList = (List<Item>) recursivePopulateData(rootCategories.size(), rootCategories);
        MyLevelCategoryAdapter myAdapter = new MyLevelCategoryAdapter(mContext, itemList, multiLevelRecyclerView);

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

    /**
     * Fill Spinner ProductGroupSchema
     */
    private void FillSpinner() {
        arrayProductGroup = new ArrayList<>();
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
            productListFragment = ProductListFragment.newInstance(Type, CustomerId, GroupId, Mode, OrderId, CountProduct);
            Bundle args = new Bundle();
            args.putInt("Mode", Mode);
            args.putInt("type", Type);
            args.putLong("CustomerId", CustomerId);
            args.putLong("OrderId", OrderId);
            args.putLong(CUSTOMER_GROUP_KEY, GroupId);
            args.putInt("CountProduct", CountProduct);
            productListFragment.setArguments(args);
            ft.replace(R.id.flContent, productListFragment);
            try {
                ft.commit();
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
        }
    }

    /**
     * Replace Fragment to PlaceholderGridFragment
     */
    public void gotoGridView() {

        PAGE_STATE = GRID_STATE;
        ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_mode_state_product, PAGE_STATE + "");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        productGridFragment = ProductGridFragment.newInstance(Type, CustomerId, GroupId, Mode, OrderId);
        Bundle args = new Bundle();
        args.putInt("Mode", Mode);
        args.putInt("type", Type);
        args.putLong("OrderId", OrderId);
        args.putInt("CustomerId", CustomerId);
        args.putLong(CUSTOMER_GROUP_KEY, GroupId);
        args.putInt("CountProduct", CountProduct);
        productGridFragment.setArguments(args);
        ft.replace(R.id.flContent, productGridFragment);
        try {
            ft.commit();
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }

    public void gotoGalleryView() {

        PAGE_STATE = GALLERY_STATE;
        ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_mode_state_product, PAGE_STATE + "");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        placeholderListGalleryFragment = PlaceholderListGalleryFragment.newInstance(Type, CustomerId, GroupId, Mode, OrderId);
        Bundle args = new Bundle();
        args.putInt("Mode", Mode);
        args.putInt("type", Type);
        args.putLong(CUSTOMER_GROUP_KEY, GroupId);
        args.putLong("OrderId", OrderId);
        args.putLong("CustomerId", CustomerId);
        args.putInt("CountProduct", CountProduct);
        placeholderListGalleryFragment.setArguments(args);
        ft.replace(R.id.flContent, placeholderListGalleryFragment);
        try {
            ft.commit();
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
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
            try {
                ft.commit();
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
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
            try {
                ft.commit();
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
        }
    }

    /**
     * Calculation totalCount,totalPrice
     */
    @SuppressWarnings("rawtypes")
    public static void CalculationTotal() {
        double Price, FinalPrice;
        Set mapSet = HashMap_Product.entrySet();
        Iterator mapIterator = mapSet.iterator();
        TotalCount = 0;
        TotalPrice = 0;
        while (mapIterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
            int keyValue = (int) mapEntry.getKey();
            OrderDetail orderDetail = (OrderDetail) mapEntry.getValue();
            Price = orderDetail.getPrice();
            TotalPrice += Price * orderDetail.getSumCountBaJoz();
            TotalCount += orderDetail.getSumCountBaJoz() + ServiceTools.getSumGiftCount12(orderDetail.getGiftCount1(), orderDetail.getGiftCount2(), mContext);

        }// End of While
        tvTotalCount.setText(formatCount(TotalCount));
        tvTotalPrice.setText(ServiceTools.formatPrice(TotalPrice));

        //////////////////////////////////////////////
    }

    public static void CalculationTotalReturn() {
        double Price, FinalPrice;
        Set mapSet = HashMap_Product.entrySet();
        Iterator mapIterator = mapSet.iterator();
        TotalCount = 0;
        while (mapIterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
            int keyValue = (int) mapEntry.getKey();
            OrderDetail orderDetail = (OrderDetail) mapEntry.getValue();
            TotalCount += orderDetail.getSumCountBaJoz() + ServiceTools.getSumGiftCount12(orderDetail.getGiftCount1(), orderDetail.getGiftCount2(), mContext);

        }// End of While
        tvTotalCount.setText(formatCount(TotalCount));
    }

    /**
     * Remove item count = 0 && gift=0 From HashMap_Product
     */
    @SuppressWarnings("rawtypes")
    public void ClearZero() {
        Set mapSet = HashMap_Product.entrySet();
        Iterator mapIterator = mapSet.iterator();
        TotalCount = 0;
        TotalPrice = 0;

        while (mapIterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
            int keyValue = (int) mapEntry.getKey();
            OrderDetail orderDetail = (OrderDetail) mapEntry.getValue();
            if (ServiceTools.getSumGiftCount12(orderDetail.getGiftCount1(), orderDetail.getGiftCount2(), mContext) == 0 && orderDetail.getCount1() == 0 && orderDetail.getCount2() == 0)
                mapIterator.remove();

        }// End of While

        //Remove from Product_Delete/////////////////////////////
        mapSet = HashMap_Product.entrySet();
        mapIterator = mapSet.iterator();
        while (mapIterator.hasNext()) {
            Map.Entry mapEntryMain = (Map.Entry) mapIterator.next();
            int keyValueMain = (int) mapEntryMain.getKey();
            if (Product_Delete != null) {
                for (Iterator<OrderDetail> iterator = Product_Delete.iterator(); iterator.hasNext(); ) {
                    OrderDetail orderDetail = iterator.next();
                    if (keyValueMain == orderDetail.getProductDetailId())
                        iterator.remove();

                }
            }

        }//End of While
        ///////////////////////////////////////////////////////
    }

    /**
     * Reset Values Default
     */
    public void Clear() {

        InvoiceDetailActivity.InvoiceCode = "";
        InvoiceDetailActivity.Description = "";
        InvoiceDetailActivity.strOrderDate = "";
        InvoiceDetailActivity.strDeliveryDate = "";
        InvoiceDetailActivity.CustomerName = "";
        InvoiceDetailActivity.Immediate = ProjectInfo.DONT_IMMEDIATE;
        InvoiceDetailActivity.SettlementType = 0;
        InvoiceDetailActivity.TotalCount = 0;
        InvoiceDetailActivity.Discount = 0;
        InvoiceDetailActivity.TotalPrice = 0;
        InvoiceDetailActivity.Discount = 0;
        InvoiceDetailActivity.Name = "";
        InvoiceDetailActivity.LastName = "";
        InvoiceDetailActivity.Tell = "";
        InvoiceDetailActivity.Address = "";
        InvoiceDetailActivity.MarketName = "";
        InvoiceDetailActivity.StrLatitude = "";
        InvoiceDetailActivity.StrLongitude = "";
        InvoiceDetailActivity.CustomerId = 0;
        InvoiceDetailActivity.OrderType = 0;

        HashMap_Product.clear();
        //arrayProductMain.clear();
        Product_Delete.clear();
    }

    /**
     * Fill arrayProductMain from database
     *
     * @param mode
     * @param categoryid
     */
    public void FillArrayProduct(int CategoryCode , int mode, long categoryid, int modeasset) {
        totalItem = 0;
        //Read Tax And Charge From Config //////////////////////////////////
        if (db == null) db = new DbAdapter(mContext);
        db.open();

        BaseActivity.setPrefDefSellPrice(ServiceTools.getPrefDefPrice(db , CustomerId , GroupId));

        /*Default_TaxPercent = db.GetTaxPercent();
        Default_ChargePercent = db.GetChargePercent();*/
        //////////////////////////////////////////////////////////////////
        if (mode == MODE_EDIT) {
            ArrayList<Product> arrayproduct;
            arrayproduct = db.getAllProduct(CategoryCode,categoryid, modeasset, 0);
            //Set Correct Asset______________________________________________
            for (OrderDetail object : InvoiceDetailActivity.orderDetailArrayList) {
                for (Product item : arrayproduct) {
                    if (item.getProductId() == object.getProductId()) {
                        ArrayList<ProductDetail> productDetails = db.getAllProductDetailWithProductId(item.getProductId());
                        for (ProductDetail productDetail : productDetails) {
                            double asset = productDetail.getCount1() + object.getSumCountBaJoz() + ServiceTools.getSumGiftCount12(object.getGiftCount1(), object.getGiftCount2(), mContext);
                            productDetail.setCount1(asset);
                        }
                        break;
                    }//End Of If
                }//End of For
            }//End Of For
            arrayProductMain.clear();
            arrayProductMain.addAll(arrayproduct);
            //____________________________________________________________________
        }//End of if
        else if (mode == MODE_NEW) {
            arrayProductMain = db.getAllProduct(CategoryCode,categoryid, modeasset, 0);
        }
        db.close();
    }

    public static class PlaceholderListFragment extends Fragment {

        private static final int BUTTON_COUNT_REFERENCE = 1;
        private static final int BUTTON_GIFT_REFERENCE = 2;
        private static ListView lstProduct;
        private static AdapterListProduct adapterlistProduct;
        private static Product SelectedProduct;
        private static long SelectedProductId;
        private Activity mActivity;
        private final ArrayList<Product> array = new ArrayList<>();


        public PlaceholderListFragment() {
        }

        public class AdapterListProduct extends ArrayAdapter<Product> {
            final Activity mContext;
            final ArrayList<Product> arrayOrginal = new ArrayList<>();
            ArrayList<Product> arrayProduct = new ArrayList<>();

            public AdapterListProduct(Activity contaxt, ArrayList<Product> array) {
                super(contaxt, android.R.layout.simple_list_item_1, array);
                mContext = contaxt;
                arrayOrginal.addAll(array);
                arrayProduct.addAll(array);
            }


            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View rowview = convertView;
                Holder holder;
                LayoutInflater inflater;
                final Product product = getItem(position);
                if ((product != null ? product.getPictures() : null) == null) {
                    if (db == null) db = new DbAdapter(ProductPickerListActivity.mContext);
                    db.open();
                    product.setPictures(db.getAllPictureByProductId(product.getProductCode()));
                    db.close();
                }
                if (rowview == null) {
                    inflater = mContext.getLayoutInflater();
                    rowview = inflater.inflate(R.layout.lst_product_picker_item, null, false);
                    holder = new Holder(rowview);
                    rowview.setTag(holder);
                } else
                    holder = (Holder) rowview.getTag();

                holder.Populate(product);
                holder.txtCount.setTag(R.id.ProductId, product.getProductId());
                holder.txtCount.setTag(R.id.Product, product);
                holder.btnGift.setTag(R.id.ProductId, product.getProductId());
                holder.btnGift.setTag(R.id.Product, product);
                holder.txtCount.setOnClickListener(new View.OnClickListener() {
                    @SuppressWarnings("rawtypes")
                    @Override
                    public void onClick(View v) {

                        double Gift = 0, Count = 0;
                        SelectedProductId = ServiceTools.toLong(v.getTag(R.id.ProductId).toString());
                        SelectedProduct = (Product) v.getTag(R.id.Product);
                        Set mapSet = HashMap_Product.entrySet();
                        Iterator mapIterator = mapSet.iterator();
                        OrderDetail item;
                        while (mapIterator.hasNext()) {
                            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                            Long keyValue = (Long) mapEntry.getKey();
                            if (SelectedProductId == keyValue) {
                                item = (OrderDetail) mapEntry.getValue();
                                Gift = ServiceTools.getSumGiftCount12(item.getGiftCount1(), item.getGiftCount2(), mContext);
                                Count = item.getCount1();
                                break;
                            }// End of if
                        }// End of While
                    }
                });

                holder.btnGift.setOnClickListener(new View.OnClickListener() {

                    @SuppressWarnings("rawtypes")
                    @Override
                    public void onClick(View v) {

                        double Count = 0, Gift = 0;
                        SelectedProductId = ServiceTools.toLong(v.getTag(R.id.ProductId).toString());
                        SelectedProduct = (Product) v.getTag(R.id.Product);

                        Set mapSet = HashMap_Product.entrySet();
                        Iterator mapIterator = mapSet.iterator();
                        OrderDetail item;
                        while (mapIterator.hasNext()) {
                            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                            Long keyValue = (Long) mapEntry.getKey();
                            if (SelectedProductId == keyValue) {
                                item = (OrderDetail) mapEntry.getValue();
                                Count = item.getCount1();
                                Gift = ServiceTools.getSumGiftCount12(item.getGiftCount1(), item.getGiftCount2(), mContext);
                                break;
                            }// End of if
                        }// End of While
                    }
                });

                return rowview;
            }

            public class Holder {
                public final TextView tvAsset;
                public final TextView tvAsset2;
                public final TextView tvInbox;
                public final TextView tvPrice;
                public final TextView tvProductName;
                public final TextView tvUnit;
                public final TextView tvUnit2;
                public final TextView txtCount;
                public final ImageView imgProduct;
                public final Button btnGift;
                public final LinearLayout llPrice;

                public Holder(View view) {

                    tvProductName = (TextView) view.findViewById(R.id.tvName);
                    tvProductName.setSelected(true);
                    tvAsset = (TextView) view.findViewById(R.id.tvAsset);
                    tvAsset2 = (TextView) view.findViewById(R.id.tvAsset2);
                    tvInbox = (TextView) view.findViewById(R.id.tvInbox);
                    tvPrice = (TextView) view.findViewById(R.id.tvPrice);
                    llPrice = (LinearLayout) view.findViewById(R.id.llPrice);
                    imgProduct = (ImageView) view.findViewById(R.id.imgProduct);
                    tvUnit = (TextView) view.findViewById(R.id.tvUnit);
                    tvUnit2 = (TextView) view.findViewById(R.id.tvUnit2);
                    txtCount = (TextView) view.findViewById(R.id.txtCountKol);
                    btnGift = (Button) view.findViewById(R.id.btnGift);
                }

                @SuppressWarnings("rawtypes")
                public void Populate(final Product product) {

                    if (db == null) db = new DbAdapter(ProductPickerListActivity.mContext);
                    db.open();
                    double SumCount2 = 0;
                    double SumCount1 = 0;

                    ArrayList<ProductDetail> productDetails = db.getAllProductDetailWithProductId(product.getProductId());

                    for (ProductDetail productDetail : productDetails) {
                        SumCount1 = ServiceTools.getSumCount1(productDetail.getProductId(), ProductPickerListActivity.mContext);
                        SumCount2 = ServiceTools.getSumCount2(productDetail.getProductId(), ProductPickerListActivity.mContext);
                    }
                    db.close();

                    Boolean res = false;

                    tvProductName.setText(product.getName());
                    tvAsset.setText(formatCount(SumCount1));
                    tvAsset2.setText(formatCount(SumCount2));
                    tvInbox.setText(formatCount(product.getUnitRatio()));
                    if (productDetails.size() > 0)
                        tvPrice.setText(ServiceTools.getPriceFormated(productDetails.get(0).getProductDetailId(), mContext));
                    tvUnit.setText(product.getUnitName());
                    tvUnit2.setText(product.getUnitName2());
                    llPrice.setVisibility(View.GONE);

                    /////////////////////////////////////////////////////////
                    // Fill NumberPickerCount && NumberPickerGift
                    Set mapSet = HashMap_Product.entrySet();
                    Iterator mapIterator = mapSet.iterator();
                    OrderDetail item;
                    while (mapIterator.hasNext()) {
                        Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                        Long keyValue = (Long) mapEntry.getKey();
                        long ProcId = product.getProductId();
                        if (ProcId == keyValue) {
                            item = (OrderDetail) mapEntry.getValue();
                            txtCount.setText(formatCount(item.getCount1()));
                            btnGift.setText(String.valueOf(ServiceTools.getSumGiftCount12(item.getGiftCount1(), item.getGiftCount2(), mContext)));
                            res = true;
                            break;
                        }// End of if

                    }// End of While
                    if (res == false) {
                        txtCount.setText(ServiceTools.formatCount(0));
                        btnGift.setText(ServiceTools.formatCount(0));
                    }
                    if (product.getPictures() != null && product.getPictures().size() > 0) {
                        ProductItemInitialize.loadImage(ProductPickerListActivity.mContext, product.getPictures().get(0).getUrl(), imgProduct);
                    } else {
                        imgProduct.setImageResource(R.drawable.img_default_product);
                        imgProduct.setBackgroundResource(R.drawable.image_empty_box);
                    }
                    imgProduct.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (product.getPictures() == null || product.getPictures().size() == 0)
                                return;
                            Intent intent = new Intent(ProductPickerListActivity.mContext, PhotoViewerActivity.class);
                            intent.putExtra(ProjectInfo._json_key_user_id, BaseActivity.getPrefUserId());
                            intent.putExtra(ProjectInfo._json_key_product_id, product.getProductCode());
                            intent.putExtra(ProjectInfo._json_key_index, 0);
                            ProductPickerListActivity.mContext.startActivity(intent);
                        }
                    });
                }
            }

        }// End of AdapterListProduct

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_products_list, container, false);
            lstProduct = (ListView) rootView.findViewById(R.id.lstProduct);

            lstProduct.setItemsCanFocus(true);
            lstProduct.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    EditText txtGift = (EditText) view.findViewById(R.id.txtGift);
                    lstProduct.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                    txtGift.requestFocus();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    lstProduct.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                }
            });

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {

            mActivity = getActivity();
            ////////////////////////////////////////
            array.addAll(arrayProductMain);
            if (mActivity != null) {
                adapterlistProduct = new AdapterListProduct(mActivity, array);
                lstProduct.setAdapter(adapterlistProduct);
                adapterlistProduct.getFilter().filter(ProductPickerListActivity.txtSearch.getText().toString(), new FilterListener() {

                    @Override
                    public void onFilterComplete(int count) {
                    }
                });
            }
            super.onActivityCreated(savedInstanceState);
        }
    } // End of PlaceholderListFragment

    public static class PlaceholderGridFragment extends Fragment {

        private GridView gvProduct;
        private Activity mActivity;
        public static AdapterGridProduct adaptergrid;
        public static Product SelectedProduct;
        public static long SelectedProductId;
        private static final int BUTTON_COUNT_REFERENCE = 1;
        private static final int BUTTON_GIFT_REFERENCE = 2;

        private final ArrayList<Product> array = new ArrayList<>();

        public PlaceholderGridFragment() {
        }

        public class AdapterGridProduct extends BaseAdapter  {

            private final LayoutInflater mInflater;
            private ArrayList<Product> arrayProduct = new ArrayList<>();
            private final ArrayList<Product> arrayOrginal = new ArrayList<>();

            public AdapterGridProduct(Activity context, ArrayList<Product> array) {
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
                final Product product = (Product) getItem(position);
                if (product.getPictures() == null) {
                    if (db == null) db = new DbAdapter(mContext);
                    db.open();
                    product.setPictures(db.getAllPictureByProductId(product.getProductCode()));
                    db.close();
                }
                if (view == null) {
                    view = mInflater.inflate(R.layout.grv_product_picker_item, parent, false);
                    holder = new Holder(view);
                    view.setTag(holder);
                } else {
                    holder = (Holder) view.getTag();
                }

                holder.Populate(product);

                holder.btnCount.setTag(R.id.ProductId, product.getProductId());
                holder.btnCount.setTag(R.id.Product, product);
                holder.btnGift.setTag(R.id.ProductId, product.getProductId());
                holder.btnGift.setTag(R.id.Product, product);

                holder.btnCount.setOnClickListener(new View.OnClickListener() {
                    @SuppressWarnings("rawtypes")
                    @Override
                    public void onClick(View v) {

                        double Gift = 0, Count = 0;
                        SelectedProductId = ServiceTools.toLong(v.getTag(R.id.ProductId).toString());
                        SelectedProduct = (Product) v.getTag(R.id.Product);
                        Set mapSet = HashMap_Product.entrySet();
                        Iterator mapIterator = mapSet.iterator();
                        OrderDetail item;
                        while (mapIterator.hasNext()) {
                            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                            Long keyValue = (Long) mapEntry.getKey();
                            if (SelectedProductId == keyValue) {
                                item = (OrderDetail) mapEntry.getValue();
                                Gift = ServiceTools.getSumGiftCount12(item.getGiftCount1(), item.getGiftCount2(), mContext);
                                Count = item.getCount1();
                                break;
                            }// End of if
                        }// End of While

                    }
                });

                holder.btnGift.setOnClickListener(new View.OnClickListener() {

                    @SuppressWarnings("rawtypes")
                    @Override
                    public void onClick(View v) {

                        double Count = 0, Gift = 0;
                        SelectedProductId = ServiceTools.toLong(v.getTag(R.id.ProductId).toString());
                        SelectedProduct = (Product) v.getTag(R.id.Product);

                        Set mapSet = HashMap_Product.entrySet();
                        Iterator mapIterator = mapSet.iterator();
                        OrderDetail item;
                        while (mapIterator.hasNext()) {
                            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                            Long keyValue = (Long) mapEntry.getKey();
                            if (SelectedProductId == keyValue) {
                                item = (OrderDetail) mapEntry.getValue();
                                Count = item.getCount1();
                                Gift = ServiceTools.getSumGiftCount12(item.getGiftCount1(), item.getGiftCount2(), mContext);
                                break;
                            }// End of if
                        }// End of While
                    }
                });

                return view;
            }

            private class Holder {

                public final TextView tvName;
                public final TextView tvAsset;
                public final TextView tvAsset2;
                public final TextView tvInbox;
                public final TextView tvPrice;
                public final TextView tvUnit;
                public final TextView tvUnit2;
                public final ImageView imgProduct;
                public final Button btnCount;
                public final Button btnGift;

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
                    btnCount = (Button) view.findViewById(R.id.txtCountKol);
                    btnGift = (Button) view.findViewById(R.id.btnGift);
                }

                @SuppressWarnings("rawtypes")
                public void Populate(final Product product) {

                    Boolean res = false;

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
                    // Fill NumberPickerCount && NumberPickerGift/////////////////////////////
                    Set mapSet = HashMap_Product.entrySet();
                    Iterator mapIterator = mapSet.iterator();
                    OrderDetail item;
                    while (mapIterator.hasNext()) {
                        Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                        Long keyValue = (Long) mapEntry.getKey();
                        long ProcId = product.getProductId();
                        if (ProcId == keyValue) {
                            item = (OrderDetail) mapEntry.getValue();
                            btnCount.setText(formatCount(item.getCount1()));
                            btnGift.setText(formatCount(ServiceTools.getSumGiftCount12(item.getGiftCount1(), item.getGiftCount2(), mContext)));
                            res = true;
                            break;
                        }// End of if

                    }// End of While
                    if (res == false) {
                        btnCount.setText(ServiceTools.formatCount(0));
                        btnGift.setText(ServiceTools.formatCount(0));
                    }
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
                    /////////////////////////////////////////////////////////////////////////
                }
            }

        }// End Of AdapterGrid

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_products_grid, container, false);
            gvProduct.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

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
    }// End of PlaceholderGridFragment

    public class AdapterSpnCategory extends ArrayAdapter<ProductGroup> {

        final ArrayList<ProductGroup> Objects;

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

        final String[] Objects;

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

    public class AsyncProduct extends AsyncTask<String, String, Boolean> {
        final Long Id;
        final int StatePage;
        final int ModePage;
        final int ModeAsset;
        int CategoryCode;

        AsyncProduct(int CategoryCode ,Long id, int statepage, int modepage, int modeasset) {

            this.Id = id;
            this.StatePage = statepage;
            this.ModePage = modepage;
            this.ModeAsset = modeasset;
            this.CategoryCode = CategoryCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {

            FillArrayProduct(CategoryCode , ModePage, Id, ModeAsset);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_products_list, menu);
        return true;
    }

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
            case android.R.id.home:
                if (Mode == MODE_NEW) {
                    finish();
                } else if (Mode == MODE_EDIT) {
                    ClearZero();
                    setResult(RESULT_OK);
                    finish();
                }
                if (asynproduct != null) {
                    if (asynproduct.getStatus() == Status.RUNNING) {
                        asynproduct.cancel(true);
                    }
                }
                break;
            case R.id.mnuSort:
                Intent intent = new Intent(ProductPickerListActivity.this, SortDialogActivity.class);
                intent.putExtra("type", "product");
                startActivityForResult(intent, 1001);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return super.onCreateDialog(id);
    }

    @Override
    public void onBackPressed() {

        if (Mode == MODE_NEW) {
            finish();
        } else if (Mode == MODE_EDIT) {
            ClearZero();
            // setResult(RESULT_OK);
            finish();
        }

    }

    @Override
    protected void onResume() {
        CalculationTotal();
        super.onResume();
    }

    private void sortProductList() {
        asynproduct = new AsyncProduct(clickedItemCategoryCode , CategoryId, PAGE_STATE, Mode, MODE_ASSET);
        asynproduct.execute();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putInt(MODE, PAGE_STATE);
        outState.putLong(CATEGORYID, CategoryId);
        outState.putInt(SELECTION, POSITION);
        outState.putInt(ASSET_KEY, MODE_ASSET);

        Gson gson = new Gson();
        JSONArray array = new JSONArray();
        int index = 0;
        for (Map.Entry<Integer, OrderDetail> entry : HashMap_Product.entrySet()) {
            Integer key = entry.getKey();
            OrderDetail value = entry.getValue();
            JSONObject obj = new JSONObject();
            try {
                obj.put("key", key);
                String json = gson.toJson(value);
                Log.d("@JSON", json);
                obj.put("value", json);

                array.put(index++, obj);
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
        }
        outState.putString("product", array.toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        Gson gson = new Gson();

        PAGE_STATE = savedInstanceState.getInt(MODE);
        CategoryId = savedInstanceState.getLong(CATEGORYID);
        POSITION = savedInstanceState.getInt(SELECTION);
        MODE_ASSET = savedInstanceState.getInt(ASSET_KEY);

        if (savedInstanceState.containsKey("product")) {
            String json = savedInstanceState.getString("product");
            HashMap_Product = new LinkedHashMap<>();
            try {
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.optJSONObject(i);
                    Integer key = obj.optInt("key");
                    String value = obj.optString("value");
                    OrderDetail orderDetail = null;
                    try {
                        orderDetail = gson.fromJson(value, OrderDetail.class);
                    } catch (JsonSyntaxException e) {
                        FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                        FirebaseCrashlytics.getInstance().recordException(e);
                        e.printStackTrace();
                    }
                    HashMap_Product.put(key, orderDetail);
                }
            } catch (JSONException e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        db.open();
        if (requestCode == 1001) {
            sortProductList();
        }
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

    public class MyLevelCategoryAdapter extends MultiLevelAdapter {

        private MyLevelCategoryAdapter.Holder mViewHolder;
        private Context mContext;
        private List<Item> mListItems = new ArrayList<>();
        private Item mItem;
        private MultiLevelRecyclerView mMultiLevelRecyclerView;

        public MyLevelCategoryAdapter(Context mContext, List<Item> mListItems, MultiLevelRecyclerView mMultiLevelRecyclerView) {
            super(mListItems);
            this.mListItems = mListItems;
            this.mContext = mContext;
            this.mMultiLevelRecyclerView = mMultiLevelRecyclerView;
        }

        private void setExpandButton(ImageView expandButton, boolean isExpanded) {
            expandButton.setImageResource(isExpanded ? R.drawable.ic_keyboard_arrow_up_black_24dp : R.drawable.ic_keyboard_arrow_down_black_24dp);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyLevelCategoryAdapter.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            mViewHolder = (MyLevelCategoryAdapter.Holder) holder;
            mItem = mListItems.get(position);

            if (mItem.hasChildren())
                holder.itemView.setBackgroundColor(Color.parseColor("#efefef"));
            else {
                holder.itemView.setBackgroundColor(Color.parseColor("#dedede"));
                mViewHolder.mTitle.setTextSize(14);
            }

            mViewHolder.mTitle.setText(mItem.getText());

            if (mItem.hasChildren() && mItem.getChildren().size() > 0) {
                mViewHolder.mExpandButton.setVisibility(View.VISIBLE);
            } else {
                mViewHolder.mExpandButton.setVisibility(View.GONE);
            }
        }


        private class Holder extends RecyclerView.ViewHolder {

            TextView mTitle;
            ImageView mExpandIcon;
            LinearLayout mTextBox, mExpandButton, item_category;

            Holder(View itemView) {
                super(itemView);
                mTitle = (TextView) itemView.findViewById(R.id.title);
                mExpandIcon = (ImageView) itemView.findViewById(R.id.image_view);
                mTextBox = (LinearLayout) itemView.findViewById(R.id.text_box);
                item_category = (LinearLayout) itemView.findViewById(R.id.item_category);
                mExpandButton = (LinearLayout) itemView.findViewById(R.id.expand_field);

                item_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<Product> productArrayList;
                        clickedItemCategoryCode = mListItems.get(getAdapterPosition()).getCategoryCode();
                        getProducts();
                    }
                });
                mExpandButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMultiLevelRecyclerView.toggleItemsGroup(getAdapterPosition());
                        mExpandIcon.animate().rotation(mListItems.get(getAdapterPosition()).isExpanded() ? -180 : 0).start();
                    }
                });
            }
        }

    }
}
