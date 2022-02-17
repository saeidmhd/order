package com.mahak.order;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Browser;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.PolyUtil;
import com.mahak.order.common.CheckList;
import com.mahak.order.common.Customer;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.User;
import com.mahak.order.common.Visitor;
import com.mahak.order.tracking.LocationService;
import com.mahak.order.tracking.MapPolygon;
import com.mahak.order.tracking.ShowPersonCluster;
import com.mahak.order.tracking.Utils;
import com.mahak.order.service.ReadOfflinePicturesProducts;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.widget.FontAlertDialog;
import com.mahak.order.widget.FontProgressDialog;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DashboardActivity extends BaseActivity implements View.OnClickListener, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener {

    private static final int REQUEST_CUSTOMER_LIST = 2;
    private static int REQUEST_DATASYNC = 1;
    private static Context mContext;
    private Activity mActivity;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerLeft;
    private ActionBarDrawerToggle mDrawerToggle;
    private int Type;
    private boolean inZone = false;
    //nav buttons
    private Button
            btnNavProductList,
            btnNavCustomerList,
            btnNavDataSync,
            btnNavContact,
            btnNavAbout,
            btnAddNewOrder,
            btnAddNewReceipt,
            btnNavReceiptList,
            btnNavOrderList,
            btnWhatsNew,
            btnNavPayableList,
            btnNavTransferList,
            btnNavBackUp,
            btnNavReturnOfSale,
            btnNavPromotionList,
            btnNavNonRegister;

    private Button
            btnNavInvoiceList,
            btnDeliveryOrderList,
            btnAddNewInvoice,
            btnNavReportsList;

    private ImageButton btnZoomMapView;


    private TextView
            tvSumOfReceipts,
            tvSumOfOrders,
            tvSumOfInvoices,
            tvSumOfCash,
            tvSumOfCheque,
            tvSumOfReceiptsAmount,
            tvSumOfDiscountInvoice,
            tvSumOfDiscountOrder,
            tvSumOfPureOrder,
            tvSumOfPureInvoice,
            tvSumOfChargeAndTaxOrder,
            tvSumOffChargeAndTaxInvoice,
            tvVersion;

    public static TextView tvTrackingService;


    private ListView lstCheckList;

    private LinearLayout
            llInvoice,
            llOrder,
            llReceipt;
    private Marker mSelectedMarker;

    private HorizontalScrollView horizontalScrollView;


    private int DefaultMapZoom = 14;
    private DbAdapter db;
    public static ArrayList<CheckList> arrayChecklist = new ArrayList<>();
    private ShowCheckListArrayAdapter adChecklist;
    private Customer customer;
    private Visitor visitor;
    private Dialog dialog;
    private ArrayList<LatLng> positions = new ArrayList<>();
    private ArrayList<LatLng> customerPositions = new ArrayList<>();
    private ArrayList<Customer> customers = new ArrayList<>();
    private GoogleMap mGoogleMap;
    Boolean first_page_load = true;

    private String TrackingState = "";

    private static MapPolygon mapPolygon;

    //------------GCM------------
    String SENDER_ID = "779811760050";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private PolylineOptions polylineOptions;
    private Marker marker;
    private LocationService locationService;
    public SwitchCompat btnTrackingService;
    private Menu menu;
    private int CustomerId;
    private long CustomerClientId;
    private long GroupId;
    private boolean hasWritePermission;
    private AsyncReport asyncReport;
    SupportMapFragment mapFragment;
    private static final int REQUEST_Location_ON = 1200;

    private static final String TAG = DashboardActivity.class.getSimpleName();

    // Used in checking for runtime permissions.
    private static final int REQUEST_LOCATION_PERMISSION = 34;
    private static final int REQUEST_WRITE_STORAGE = 115;

    // A reference to the service used to get location updates.

    // Tracks the bound state of the service.
    private boolean mBound = false;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            locationService = binder.getService(mContext , DashboardActivity.this);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            locationService = null;
            mBound = false;
        }
    };
    private List<LatLng> latLngpoints = new ArrayList<>();
    private FontProgressDialog pd;
    private static LatLng lastPosition;
    private int fromActivityResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerReceiverToCheckGpsOnOff();

        setContentView(R.layout.activity_dashboard);

        mContext = this;
        mActivity = this;

        initUI();

        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }

        hasWritePermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasWritePermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }

        //config actionbar___________________________________________

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.str_nav_close);

        getSupportActionBar().setDisplayShowCustomEnabled(true);

        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
        TextView tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        getSupportActionBar().setCustomView(view);

        //_______________________________________________________________

        if(!isMyServiceRunning(LocationService.class)){
            if(btnTrackingService.isChecked()){
                btnTrackingService.setChecked(false);
                setTackingServiceText(false);
            }
        }else {
            if(!btnTrackingService.isChecked()){
                btnTrackingService.setChecked(true);
                setTackingServiceText(true);
            }
        }

        btnTrackingService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isRadaraActive()){
                    showDialogRadara();
                    if(btnTrackingService.isChecked())
                        btnTrackingService.setChecked(false);
                }else {
                    if (locationService == null) locationService = new LocationService(mContext, DashboardActivity.this);
                    if(locationService.isRunService(mContext) && !btnTrackingService.isChecked()){
                        stopLocationUpdate();
                    }else {
                        startLocationUpdate();
                    }
                }
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        FillView();
        User user = db.getUser();
        if (user.getMahakId() != null)
            tvPageTitle.setText(user.getName() + "( " + getPrefUsername() + " )");
        //////////////////
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getInt(Type_Login) == bnd_Login_Splash) {
                int CountProduct = db.getTotalCountProduct();
                int CountCustomer = db.getTotalCountPeople();
                if (CountCustomer == 0 && CountProduct == 0) {
                    Dialog(getString(R.string.str_message_no_data)).show();
                    FontAlertDialog.FontDialog(dialog);
                }
            }
        }

        new ReadOfflinePicturesProducts(this).readAllImages();
        ///////////////////
        btnAddNewOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(mDrawerLeft))
                    mDrawerLayout.closeDrawers();
                Type = ProjectInfo.TYPE_ORDER;
                Intent intent = new Intent(mContext, PeopleListActivity.class);
                intent.putExtra(PAGE, PAGE_ADD_ORDER);
                startActivityForResult(intent, REQUEST_CUSTOMER_LIST);

            }
        });
        btnAddNewReceipt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(mDrawerLeft))
                    mDrawerLayout.closeDrawers();
                /**/

                Intent intent = new Intent(mContext, PeopleListActivity.class);
                intent.putExtra(MODE_PAGE, MODE_NEW);
                intent.putExtra(PAGE, PAGE_MANAGE_RECEIPT);
                startActivityForResult(intent, REQUEST_CUSTOMER_LIST);
            }
        });
        btnAddNewInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(mDrawerLeft))
                    mDrawerLayout.closeDrawers();
                if(isRadaraActive()){
                    if(!btnTrackingService.isChecked()){
                        showDialogRequest();
                    }else if(CanRegisterInvoiceOutOfZone()){
                        Type = ProjectInfo.TYPE_INVOCIE;
                        Intent intent = new Intent(mContext, PeopleListActivity.class);
                        intent.putExtra(PAGE, PAGE_ADD_INVOICE);
                        startActivityForResult(intent, REQUEST_CUSTOMER_LIST);

                    }else {
                        Toast.makeText(mContext, "خارج از منطقه یا خاموش بودن سامانه رادارا ! امکان ثبت فاکتور وجود ندارد.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Type = ProjectInfo.TYPE_INVOCIE;
                    Intent intent = new Intent(mContext, PeopleListActivity.class);
                    intent.putExtra(PAGE, PAGE_ADD_INVOICE);
                    startActivityForResult(intent, REQUEST_CUSTOMER_LIST);
                }
            }
        });
        btnZoomMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRadaraActive()){
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
                    Bundle headers = new Bundle();
                    headers.putString("token", BaseActivity.getPrefSignalUserToken());
                    customTabsIntent.intent.putExtra(Browser.EXTRA_HEADERS, headers);
                    customTabsIntent.launchUrl(DashboardActivity.this, Uri.parse(baseUrlShowRouting));
                }else {
                    Intent intent = new Intent(mContext, MapViewActivity.class);
                    intent.putParcelableArrayListExtra(COORDINATE, positions);
                    intent.putParcelableArrayListExtra(CustomerPositions, customerPositions);
                    startActivity(intent);
                }
            }
        });

        lstCheckList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (customer != null) {
                    Intent intent = new Intent(mContext, CheckListDetailActivity.class);
                    intent.putExtra(POSITION_KEY, position);
                    startActivity(intent);
                }
            }
        });

        llOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                intent.putExtra(PAGE, PAGE_DASHBORD);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_ORDER);
                startActivity(intent);
            }
        });

        llInvoice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                intent.putExtra(PAGE, PAGE_DASHBORD);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_INVOCIE);
                startActivity(intent);
            }
        });


        llReceipt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ReceiptsListActivity.class);
                startActivity(intent);

            }
        });


        horizontalScrollView.postDelayed(new Runnable() {
            public void run() {
                horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 1L);


        //on receive message from google gcm
        MyFcmListenerService.receiveMessag = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showBadgeNotification(menu);
                        }
                    });
                }
            }
        };
    }//end of onCreate

    public static void setTackingServiceText(boolean isChecked) {
        if (isChecked) {
            tvTrackingService.setText(R.string.tracking_system_is_active);
        } else {
            tvTrackingService.setText(R.string.tracking_system_is_disabled);
        }
        if (isChecked && BaseActivity.getPrefAdminControl(mContext)) {
            tvTrackingService.setText(R.string.tracking_system_is_active_admin);
        } else if (!isChecked && BaseActivity.getPrefAdminControl(mContext)) {
            tvTrackingService.setText(R.string.tracking_system_is_disabled_admin);
        }
    }

    public static boolean CanRegisterInvoiceOutOfZone() {
        boolean canRegister = true;
        String config = ServiceTools.getKeyFromSharedPreferences(mContext, ProjectInfo.pre_gps_config);
        if (!ServiceTools.isNull(config)) {
            try {
                JSONObject obj = new JSONObject(config);
                canRegister = obj.getBoolean(ProjectInfo._json_key_isRestricted);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(!canRegister)
            if(mapPolygon != null){

                return mapPolygon.checkPositionInZone(lastPosition);
            }

        return canRegister;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        if(fromActivityResult == 0){
            asyncReport = new AsyncReport();
            asyncReport.execute();
        }
        if(isRadaraActive())
            setTrackingConfig();
        super.onResume();
    }

    private void registerReceiverToCheckGpsOnOff() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.location.PROVIDERS_CHANGED");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                    LocationManager service = (LocationManager) context.getSystemService(LOCATION_SERVICE);
                    if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                        boolean isGPSPROVIDEREnabled = service != null && service.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        if (!isGPSPROVIDEREnabled) {
                            stopLocationUpdate();
                        }else {
                            startLocationUpdate();
                        }
                    }
                }
            }
        };
        this.getApplicationContext().registerReceiver(receiver, filter);
    }


    private void startLocationUpdate() {
        if(isRadaraActive()){
            if (locationService == null) locationService = new LocationService(mContext, DashboardActivity.this);
            if (!checkPermissions()) {
                requestPermissions();
            }else {
                if(!btnTrackingService.isChecked()){
                    btnTrackingService.setChecked(true);
                }
                setTackingServiceText(true);
                locationService.startTracking();
                locationService.setTrackingPrefOff("1");
            }
        }
    }

    private void stopLocationUpdate() {
        if(locationService.isRunService(mContext)){
            locationService.stopTracking();
            locationService.stopNotificationServiceTracking();
        }
        if(btnTrackingService.isChecked()){
            btnTrackingService.setChecked(false);
        }
        setTackingServiceText(false);
        locationService.setTrackingPrefOff("0");
        ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_last_location, null);
    }

    private boolean checkPermissions() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            Snackbar.make(
                    findViewById(R.id.drawer_layout),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(DashboardActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_LOCATION_PERMISSION);
                        }
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(DashboardActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length <= 0) {
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdate();
            } else {
                stopLocationUpdate();
                Snackbar.make(
                        findViewById(R.id.drawer_layout),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }).show();
            }
        }else if(requestCode == REQUEST_WRITE_STORAGE){
            if (grantResults.length > 0) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showPermissionDialog();
                }
            }
        }
    }

    private LatLng getLastPoint(){
        LatLng latLng = null;
        if(locationService != null){
            JSONObject obj = locationService.getLastLocationJson(mContext);
            if (obj != null) {
                Location lastLocation = new Location("");
                lastLocation.setLatitude(obj.optDouble(ProjectInfo._json_key_latitude));
                lastLocation.setLongitude(obj.optDouble(ProjectInfo._json_key_longitude));
                latLng = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
            }
        }
        return latLng;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BaseActivity.TransferenceFlag = 0;
    }

    /**
     * Initializing Variables
     */
    private void initUI() {

        db = new DbAdapter(mContext);
        db.open();

        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));

        tvSumOfOrders = (TextView) findViewById(R.id.tvSumOfOrders);
        tvSumOfReceipts = (TextView) findViewById(R.id.tvSumOfReceipts);
        lstCheckList = (ListView) findViewById(R.id.lstCheckList);
        mDrawerLeft = (RelativeLayout) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        btnAddNewOrder = (Button) findViewById(R.id.btnAddNewOrder);
        btnAddNewReceipt = (Button) findViewById(R.id.btnAddNewReceipt);
        btnAddNewInvoice = (Button) findViewById(R.id.btnAddNewInvoice);
        btnZoomMapView = (ImageButton) findViewById(R.id.btnZoomMapView);

        tvSumOfOrders = (TextView) findViewById(R.id.tvSumOfOrders);
        tvTrackingService = (TextView) findViewById(R.id.tvTrackingService);
        tvSumOfReceipts = (TextView) findViewById(R.id.tvSumOfReceipts);
        tvSumOfInvoices = (TextView) findViewById(R.id.tvSumOfInvoices);
        tvSumOfCash = (TextView) findViewById(R.id.tvSumOfCash);
        tvSumOfCheque = (TextView) findViewById(R.id.tvSumOfCheque);
        tvSumOfReceiptsAmount = (TextView) findViewById(R.id.tvSumOfReceiptsAmount);
        tvSumOfDiscountOrder = (TextView) findViewById(R.id.tvSumOfDiscountOrder);
        tvSumOfDiscountInvoice = (TextView) findViewById(R.id.tvSumOfDiscountInvoice);
        tvSumOfPureOrder = (TextView) findViewById(R.id.tvSumOfPureOrder);
        tvSumOfPureInvoice = (TextView) findViewById(R.id.tvSumOfPureInvoice);
        tvSumOfChargeAndTaxOrder = (TextView) findViewById(R.id.tvSumOfChargeAndTaxOrder);
        tvSumOffChargeAndTaxInvoice = (TextView) findViewById(R.id.tvSumOfChargeAndTaxInvoice);
        tvVersion = (TextView) findViewById(R.id.tvVersion);
        llReceipt = (LinearLayout) findViewById(R.id.llReceipt);
        llOrder = (LinearLayout) findViewById(R.id.llOrder);
        llInvoice = (LinearLayout) findViewById(R.id.llInvoice);


        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);

        //nav buttons
        btnNavProductList = (Button) findViewById(R.id.btnNavProductList);
        btnNavProductList.setCompoundDrawables(null, null, new IconicsDrawable(mContext, Ionicons.Icon.ion_social_dropbox).color(Color.WHITE).sizeDp(30), null);
        btnNavCustomerList = (Button) findViewById(R.id.btnNavCustomerList);
        btnNavCustomerList.setCompoundDrawables(null, null, new IconicsDrawable(mContext, Ionicons.Icon.ion_person_stalker).color(Color.WHITE).sizeDp(30), null);
        btnNavReceiptList = (Button) findViewById(R.id.btnNavReceiptList);
        btnNavReceiptList.setCompoundDrawables(null, null, new IconicsDrawable(mContext, Ionicons.Icon.ion_cash).color(Color.WHITE).sizeDp(30), null);
        btnNavOrderList = (Button) findViewById(R.id.btnNavOrderList);
        btnNavOrderList.setCompoundDrawables(null, null, new IconicsDrawable(mContext, FontAwesome.Icon.faw_file).color(Color.WHITE).sizeDp(30), null);
        btnNavInvoiceList = (Button) findViewById(R.id.btnNavInvoiceList);
        btnNavInvoiceList.setCompoundDrawables(null, null, new IconicsDrawable(mContext, FontAwesome.Icon.faw_file).color(Color.WHITE).sizeDp(30), null);
        btnNavDataSync = (Button) findViewById(R.id.btnNavDataSync);
        btnNavDataSync.setCompoundDrawables(null, null, new IconicsDrawable(mContext, Ionicons.Icon.ion_android_sync).color(Color.WHITE).sizeDp(30), null);
        btnNavContact = (Button) findViewById(R.id.btnNavContact);
        btnNavContact.setCompoundDrawables(null, null, new IconicsDrawable(mContext, Ionicons.Icon.ion_android_mail).color(Color.WHITE).sizeDp(30), null);
        btnNavAbout = (Button) findViewById(R.id.btnNavAbout);
        btnNavAbout.setCompoundDrawables(null, null, new IconicsDrawable(mContext, Ionicons.Icon.ion_chatbox_working).color(Color.WHITE).sizeDp(30), null);
        btnDeliveryOrderList = (Button) findViewById(R.id.btnNavDeliveryOrderList);
        btnDeliveryOrderList.setCompoundDrawables(null, null, new IconicsDrawable(mContext, FontAwesome.Icon.faw_truck).color(Color.WHITE).sizeDp(30), null);
        btnNavReportsList = (Button) findViewById(R.id.btnNavReportsList);
        btnNavReportsList.setCompoundDrawables(null, null, new IconicsDrawable(mContext, FontAwesome.Icon.faw_chart_bar).color(Color.WHITE).sizeDp(30), null);
        btnNavPayableList = (Button) findViewById(R.id.btnNavPayableList);
        btnNavPayableList.setCompoundDrawables(null, null, new IconicsDrawable(mContext, Ionicons.Icon.ion_card).color(Color.WHITE).sizeDp(30), null);

        btnNavTransferList = (Button) findViewById(R.id.btnNavTransferList);
        btnNavTransferList.setCompoundDrawables(null, null, new IconicsDrawable(mContext, Ionicons.Icon.ion_arrow_swap).color(Color.WHITE).sizeDp(30), null);

        btnNavBackUp = (Button) findViewById(R.id.btnBackup);
        btnNavBackUp.setCompoundDrawables(null, null, new IconicsDrawable(mContext, FontAwesome.Icon.faw_database).color(Color.WHITE).sizeDp(30), null);

        btnWhatsNew = (Button) findViewById(R.id.btnWhatsNew);
        btnWhatsNew.setCompoundDrawables(null, null, new IconicsDrawable(mContext, FontAwesome.Icon.faw_clock).color(Color.WHITE).sizeDp(30), null);

        btnNavReturnOfSale = (Button) findViewById(R.id.btnNavReturnOfSale);
        btnNavReturnOfSale.setCompoundDrawables(null, null, new IconicsDrawable(mContext, Ionicons.Icon.ion_arrow_return_left).color(Color.WHITE).sizeDp(30), null);
        btnNavNonRegister = (Button) findViewById(R.id.btnNavNonRegister);
        btnNavNonRegister.setCompoundDrawables(null, null, new IconicsDrawable(mContext, Ionicons.Icon.ion_android_sad).color(Color.WHITE).sizeDp(30), null);
        btnNavPromotionList = (Button) findViewById(R.id.btnNavPromotionList);
        btnNavPromotionList.setCompoundDrawables(null, null, new IconicsDrawable(mContext, FontAwesome.Icon.faw_gift).color(Color.WHITE).sizeDp(30), null);

        btnNavProductList.setOnClickListener(this);
        btnNavCustomerList.setOnClickListener(this);
        btnNavReceiptList.setOnClickListener(this);
        btnNavOrderList.setOnClickListener(this);
        btnNavDataSync.setOnClickListener(this);
        btnNavContact.setOnClickListener(this);
        btnNavAbout.setOnClickListener(this);
        btnDeliveryOrderList.setOnClickListener(this);
        btnNavInvoiceList.setOnClickListener(this);
        btnNavReportsList.setOnClickListener(this);
        btnNavPayableList.setOnClickListener(this);
        btnNavTransferList.setOnClickListener(this);
        btnNavBackUp.setOnClickListener(this);
        btnWhatsNew.setOnClickListener(this);
        btnNavReturnOfSale.setOnClickListener(this);
        btnNavNonRegister.setOnClickListener(this);
        btnNavPromotionList.setOnClickListener(this);

        btnTrackingService = (SwitchCompat) findViewById(R.id.btnTrackingService);

        //Version/////////////////////////////////////////////////////
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tvVersion.setText(version);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

    }//end of init

    private boolean checkIfInPolygon(LatLng position, List<LatLng> polygonPoints) {
        return PolyUtil.containsLocation(position, polygonPoints, true);
    }


    /**
     * Fill Adapter ShowCheckListArrayAdapter And Map
     */
    private void FillView() {

        arrayChecklist = new ArrayList<>();
        arrayChecklist = db.getAllChecklistNotPublished();
        visitor = db.getVisitor();
        customer = new Customer();
        //Coordinate coordinate;

        //Fill LstCheckList and Coordinate_________________________________________________________________________
        positions.clear();
        for (CheckList item : arrayChecklist) {
            if (item.getPersonId() == ProjectInfo.CUSTOMERID_GUEST) {
                item.setName(getResources().getString(R.string.str_guest_customer));
                item.setMarketName(getResources().getString(R.string.str_guest_market_customer));
            } else {
                customer = db.getCustomerWithPersonId(item.getPersonId());
                if (customer != null) {
                    item.setName(customer.getName());
                    item.setMarketName(customer.getOrganization());
                    item.setAddress(customer.getAddress());
                    item.setLatitude(customer.getLatitude());
                    item.setLongitude(customer.getLongitude());

                    double Latitude = customer.getLatitude();
                    double Longitude = customer.getLongitude();

                    if (Latitude != 0 && Longitude != 0) {
                        positions.add(new LatLng(customer.getLatitude(), customer.getLongitude()));
                    }
                }
            }

        }//End of for

        //Fill Adapter
        adChecklist = new ShowCheckListArrayAdapter(mActivity, R.layout.lst_check_list_item, arrayChecklist);
        lstCheckList.setAdapter(adChecklist);


        //Fill Map_______________________________________________________________________________
        //////////////////////////////////////// Map init
        SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if (googleMap != null)
                        mGoogleMap = googleMap;
                    new ShowPersonCluster(mGoogleMap,mContext).showPeople();
                    showCheckListPositon();
                    mapPolygon = new MapPolygon(mGoogleMap,mContext);
                    mapPolygon.showPolygon();
                    initMap();
                    if(getLastPoint() != null)
                        showMarkerOnMap(getLastPoint());

                }
            });
        }

        LocationService.addEventLocation(this.getLocalClassName(), new LocationService.EventLocation() {
            @Override
            public void onReceivePoint(final Location location, boolean saveInDb) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (locationService == null)
                            locationService = new LocationService(getBaseContext(),DashboardActivity.this);
                        if(location != null){
                            lastPosition = new LatLng(location.getLatitude(), location.getLongitude());
                            showMarkerOnMap(lastPosition);
                        }
                    }
                });
            }
        });

    }

    private void showMarkerOnMap(LatLng position) {
        if (marker != null) {
            marker.remove();
        }
        if (mGoogleMap != null) {
            if(position != null){
                marker = mGoogleMap.addMarker(new MarkerOptions().position(position));
                if (marker != null) {
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_visitor_3));
                }
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(position.latitude, position.longitude), 15));
            }
        }
    }

    private void showCheckListPositon() {
        try {
            if (arrayChecklist.size() == 0)
                positions.clear();

            if (positions.size() != 0) {
                for (int i = 0; i < positions.size(); i++) {
                    mGoogleMap.addMarker(new MarkerOptions().position(positions.get(i)).title(arrayChecklist.get(i).getName()));
                }
            }

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positions.get(positions.size() - 1), 14));

        } catch (Exception e) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        bindService(new Intent(this, LocationService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
        if(Utils.requestingLocationUpdates(this) && checkServiceLocationIsRunning(mContext))
            setButtonsState(true);

    }

    private void setButtonsState(boolean requestingLocationUpdates) {
        btnTrackingService.setChecked(requestingLocationUpdates);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
    }


    private void initMap() {
        if (locationService == null)
            locationService = new LocationService(getBaseContext(),DashboardActivity.this);
    }


    private void refreshCountInformation() {
        if (btnNavCustomerList == null)
            return;

        int CountCustomer = db.getTotalCountPeople();
        int CountProduct = db.getTotalCountProduct();
        int CountOrder = db.getTotalCountOrder();
        int CountInvoice = db.getTotalCountInvoice();
        int CountDeliverOrder = db.getTotalCountDeliveryOrder();
        int CountReceipt = db.getTotalCountReceipt();
        int CountPayable = db.getTotalCountPayable();
        int CountReturnOfSales = db.getTotalCountReturnOfSale();
        int CountNonRegisters = db.getTotalCountNonRegister();
        int CountPromotion = db.getTotalCountPromotion();


        btnNavCustomerList.setText(getString(R.string.str_nav_customer_list) + "(" + CountCustomer + ")");
        btnNavOrderList.setText(getString(R.string.str_nav_order_list) + "(" + CountOrder + ")");
        btnNavInvoiceList.setText(getString(R.string.str_nav_invoice_list) + "(" + CountInvoice + ")");
        btnNavProductList.setText(getString(R.string.str_nav_product_list) + "(" + CountProduct + ")");
        btnNavReceiptList.setText(getString(R.string.str_nav_receipt_list) + "(" + CountReceipt + ")");
        btnDeliveryOrderList.setText(getString(R.string.str_nav_delivery_list) + "(" + CountDeliverOrder + ")");
        btnNavPayableList.setText(getString(R.string.str_nav_payable_list) + "(" + CountPayable + ")");
        btnNavReturnOfSale.setText(getString(R.string.str_nav_returnOfSale_list) + "(" + CountReturnOfSales + ")");
        btnNavNonRegister.setText(getString(R.string.str_nav_nonRegister_list) + "(" + CountNonRegisters + ")");
        btnNavPromotionList.setText(getString(R.string.str_nav_promotion_list) + "(" + CountPromotion + ")");
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnNavProductList:
                mDrawerLayout.closeDrawers();
                intent = new Intent(getApplicationContext(), ProductsListActivity.class);
                startActivity(intent);
                break;
            case R.id.btnNavCustomerList:
                mDrawerLayout.closeDrawers();
                intent = new Intent(getApplicationContext(), PeopleListActivity.class);
                intent.putExtra(PAGE, PAGE_DASHBORD);
                startActivity(intent);
                break;
            case R.id.btnNavDataSync:
                mDrawerLayout.closeDrawers();
                intent = new Intent(getApplicationContext(), DataSyncActivityRestApi.class);
                startActivityForResult(intent, REQUEST_DATASYNC);
                break;
            case R.id.btnNavContact:
                mDrawerLayout.closeDrawers();
                /*intent = new Intent(getApplicationContext(), ContactActivity.class);
                startActivity(intent);*/
                break;
            case R.id.btnNavAbout:
                mDrawerLayout.closeDrawers();
                intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.btnNavReceiptList:
                mDrawerLayout.closeDrawers();
                intent = new Intent(getApplicationContext(), ReceiptsListActivity.class);
                startActivity(intent);
                break;
            case R.id.btnNavOrderList:
                mDrawerLayout.closeDrawers();
                intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                intent.putExtra(PAGE, PAGE_DASHBORD);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_ORDER);
                startActivity(intent);
                break;
            case R.id.btnNavInvoiceList:
                mDrawerLayout.closeDrawers();
                intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                intent.putExtra(PAGE, PAGE_DASHBORD);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_INVOCIE);
                startActivity(intent);
                break;
            case R.id.btnNavDeliveryOrderList:
                mDrawerLayout.closeDrawers();
                intent = new Intent(getApplicationContext(), DeliveryOrdersListActivity.class);
                startActivity(intent);
                break;
            case R.id.btnNavReportsList:
                mDrawerLayout.closeDrawers();
                intent = new Intent(getApplicationContext(), ReportsListActivity.class);
                startActivity(intent);
                break;
            case R.id.btnNavPayableList:
                mDrawerLayout.closeDrawers();
                intent = new Intent(getApplicationContext(), PayableListActivity.class);
                startActivity(intent);
                break;
            case R.id.btnNavTransferList:
                mDrawerLayout.closeDrawers();
                intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                intent.putExtra(PAGE, PAGE_DASHBORD);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_SEND_TRANSFERENCE);
                startActivity(intent);
                break;
            case R.id.btnBackup:
                mDrawerLayout.closeDrawers();
                intent = new Intent(getApplicationContext(), BackupListActivity.class);
                startActivity(intent);
                break;
            case R.id.btnWhatsNew:
                mDrawerLayout.closeDrawers();
                intent = new Intent(getApplicationContext(), WhatsNewActivity.class);
                startActivity(intent);
                break;
            case R.id.btnNavReturnOfSale:
                mDrawerLayout.closeDrawers();
                InvoiceDetailActivity.orderDetails.clear();
                intent = new Intent(getApplicationContext(), ReturnsListActivity.class);
                intent.putExtra(PAGE, PAGE_DASHBORD);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_RETURN_OF_SALE);
                startActivity(intent);
                break;
            case R.id.btnNavNonRegister:
                mDrawerLayout.closeDrawers();
                intent = new Intent(getApplicationContext(), NonRegisterListActivity.class);
                intent.putExtra(PAGE, PAGE_DASHBORD);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_NON_REGISTER);
                startActivity(intent);
                break;
            case R.id.btnNavPromotionList:
                mDrawerLayout.closeDrawers();
                intent = new Intent(getApplicationContext(), PromotionListActivity.class);
                intent.putExtra(PAGE, PAGE_DASHBORD);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_PROMOTION);
                startActivity(intent);
                break;

        }
    }//end of onClick

    @Override
    public void onMapClick(LatLng latLng) {
        // Any showing info window closes when the map is clicked.
        // Clear the currently selected marker.
        mSelectedMarker = null;

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // The user has re-tapped on the marker which was already showing an info window.
        if (marker.equals(mSelectedMarker)) {
            mSelectedMarker = null;
            return true;
        }

        mSelectedMarker = marker;

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur.
        return false;
    }

    private class ShowCheckListArrayAdapter extends ArrayAdapter<CheckList> {
        private final Activity context;
        private final int ViewResourceId;
        private ArrayList<CheckList> list;

        ShowCheckListArrayAdapter(Activity context, int ViewResourceId, ArrayList<CheckList> list) {
            super(context, ViewResourceId, list);
            this.ViewResourceId = ViewResourceId;
            this.list = list;
            this.context = context;
        }

        private class ViewHolder {
            TextView tvName;
            TextView tvAddress;
            TextView tvNumber;
            TextView tvMarketName;
            TextView tvType;
            TextView tvDescription;
            ImageView imgType;
            ImageButton btnMenu;
            LinearLayout llNumber;
            IconicsImageView icCheck;

            ViewHolder(View rowView) {

                tvName = (TextView) rowView.findViewById(R.id.tvCustomerName);
                tvMarketName = (TextView) rowView.findViewById(R.id.tvMarketName);
                tvAddress = (TextView) rowView.findViewById(R.id.tvAddress);
                tvNumber = (TextView) rowView.findViewById(R.id.tvNumber);
                tvDescription = (TextView) rowView.findViewById(R.id.tvDescription);
                tvType = (TextView) rowView.findViewById(R.id.tvType);
                imgType = (ImageView) rowView.findViewById(R.id.imgType);
                btnMenu = (ImageButton) rowView.findViewById(R.id.btnMenu);
                llNumber = (LinearLayout) rowView.findViewById(R.id.llNumber);
                icCheck = (IconicsImageView) rowView.findViewById(R.id.icCheck);
            }

            void Populate(CheckList checklist, int position) {

                if(checklist.getName() != null)
                    tvName.setText(checklist.getName().trim());
                if(checklist.getAddress() != null)
                    tvAddress.setText(checklist.getAddress().trim());
                if (checklist.getDescription() != null)
                    tvDescription.setText(checklist.getDescription().trim());

                if (checklist.getType() == ProjectInfo.CHECKLIST_TYPE_ORDER)
                    tvType.setText(getString(R.string.str_checklist_order));
                else if (checklist.getType() == ProjectInfo.CHECKLIST_TYPE_DELIVERY)
                    tvType.setText(getString(R.string.str_checklist_delivery));
                else if (checklist.getType() == ProjectInfo.CHECKLIST_TYPE_RECEIPT)
                    tvType.setText(getString(R.string.str_checklist_receipt));

                if (checklist.getStatus() == ProjectInfo.STATUS_DO) {
                    tvNumber.setVisibility(View.GONE);
                    icCheck.setVisibility(View.VISIBLE);
                    llNumber.setBackgroundResource(R.drawable.item_number_placeholder_checked);
                } else if (checklist.getStatus() != ProjectInfo.STATUS_DO) {
                    tvNumber.setVisibility(View.VISIBLE);
                    icCheck.setVisibility(View.GONE);
                    llNumber.setBackgroundResource(R.drawable.item_number_placeholder);
                    tvNumber.setText(String.valueOf(position + 1));
                }

                imgType.setImageResource(ServiceTools.getCheckListTypeIcon(checklist.getType()));
                btnMenu.setFocusable(false);
                btnMenu.setFocusableInTouchMode(false);

                if (customer == null)
                    tvMarketName.setText(getString(R.string.str_message_failed_get_information));
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rowView = convertView;
            final ViewHolder holder;
            final CheckList checklist = getItem(position);
            if (convertView == null) {
                rowView = getLayoutInflater().inflate(ViewResourceId, null);
                holder = new ViewHolder(rowView);
                rowView.setTag(holder);
            } else {
                holder = (ViewHolder) rowView.getTag();
            }
            holder.Populate(checklist, position);

            return rowView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Drawable exit = ContextCompat.getDrawable(mContext, R.drawable.ic_exit_to_app_white_48dp);
        menu.add(0, 0, 0, R.string.str_exit_app)
                .setIcon(exit)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_menu_notification);
        menu.add(2, 2, 1, R.string.str_notification)
                .setIcon(drawable)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        menu.add(1, 1, 2, R.string.str_setting)
                .setIcon(new IconicsDrawable(mContext, GoogleMaterial.Icon.gmd_settings).color(Color.WHITE).sizeDp(20))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        showBadgeNotification(menu);

        this.menu = menu;


        return super.onCreateOptionsMenu(menu);
    }

    private void showBadgeNotification(Menu menu) {
        long userId = BaseActivity.getPrefUserMasterId(mContext);
        if (db == null)
            db = new DbAdapter(mContext);
        db.open();
        int count = db.getCountNotReadNotification(String.valueOf(userId));
        if (count > 0) {
            MenuItem item = menu.findItem(2);
            LayerDrawable icon = (LayerDrawable) item.getIcon();

            // Update LayerDrawable's BadgeDrawable
            ServiceTools.setBadgeCount(this, icon, count);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerLeft);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(mDrawerLeft)) {
                    mDrawerLayout.closeDrawer(mDrawerLeft);
                } else {
                    mDrawerLayout.openDrawer(mDrawerLeft);
                }
                break;
            case 0:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.str_exit_app);
                builder.setMessage(R.string.str_exit_app_desc);
                builder.setPositiveButton(R.string.str_yes, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        stopLocationUpdate();
                        RefreshPreferenceUser();
                        setPrefSignalUserToken("");
                        dialog.dismiss();
                        Intent intent = new Intent(DashboardActivity.this, LoginActivityRestApi.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.str_cancel, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case 1:
                if (mDrawerLayout.isDrawerOpen(mDrawerLeft))
                    mDrawerLayout.closeDrawers();
                Intent Intent = new Intent(DashboardActivity.this, SettingActivity.class);
                startActivity(Intent);
                break;
            case 2:
                if (mDrawerLayout.isDrawerOpen(mDrawerLeft))
                    mDrawerLayout.closeDrawers();
                Intent = new Intent(DashboardActivity.this, NotificationActivity.class);
                startActivity(Intent);
                break;
        }
        return true;
    }

    private Dialog Dialog(String msg) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(FontAlertDialog.getFontTitle(getString(R.string.str_title_message)))
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.str_update_dialog), new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        Intent intent = new Intent(getApplicationContext(), DataSyncActivityRestApi.class);
                        startActivityForResult(intent, REQUEST_DATASYNC);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.str_ok), new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog.dismiss();
                    }
                });

        dialog = builder.create();
        return dialog;
    }


    public class AsyncReport extends AsyncTask<String, String, Boolean> {

        double TotalOrder;
        double TotalInvoice;
        double TotalReceiveTransfer;
        double TotalReceipt;
        double TotalCash;
        double TotalCheque;
        double TotalCashReceipt;
        double TotalDiscountOrder;
        double TotalDiscountInvoice;
        double TotalChargeAndTaxOrder;
        double TotalChargeAndTaxInvoice;
        double TotalPureOrder;
        double TotalPureInvoice;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
             TotalOrder = db.getTotalPriceOrder();
             TotalInvoice = db.getTotalPriceInvoice();
             TotalReceiveTransfer = db.getTotalReceiveTransfer();
             TotalReceipt = db.getTotalPriceReceipt();
             TotalCash = db.getTotalCashAmountReceipt();
             TotalCheque = db.getTotalChequeReceipt();
             TotalCashReceipt = db.getTotalCashReceipt();
             TotalDiscountOrder = db.getTotalDiscountOrder();
             TotalDiscountInvoice = db.getTotalDiscountInvoice();
             TotalChargeAndTaxOrder = db.getTotalChargeAndTaxOrder();
             TotalChargeAndTaxInvoice = db.getTotalChargeAndTaxInvoice();
             TotalPureOrder = db.getPurePriceOrder();
             TotalPureInvoice = db.getPurePriceInvoice();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            tvSumOfOrders.setText(ServiceTools.formatPrice(TotalOrder));
            tvSumOfOrders.setSelected(true);
            tvSumOfInvoices.setText(ServiceTools.formatPrice(TotalInvoice));
            tvSumOfInvoices.setSelected(true);
            tvSumOfReceipts.setText(ServiceTools.formatPrice((TotalReceipt)));
            tvSumOfReceipts.setSelected(true);
            tvSumOfCash.setText(ServiceTools.formatPrice(TotalCash));
            tvSumOfCash.setSelected(true);
            tvSumOfCheque.setText(ServiceTools.formatPrice(TotalCheque));
            tvSumOfCheque.setSelected(true);
            tvSumOfReceiptsAmount.setText(ServiceTools.formatPrice(TotalCashReceipt));
            tvSumOfReceiptsAmount.setSelected(true);
            tvSumOfDiscountOrder.setText(ServiceTools.formatPrice(TotalDiscountOrder));
            tvSumOfDiscountOrder.setSelected(true);
            tvSumOfDiscountInvoice.setText(ServiceTools.formatPrice(TotalDiscountInvoice));
            tvSumOfDiscountInvoice.setSelected(true);
            tvSumOfPureOrder.setText(ServiceTools.formatPrice(TotalPureOrder));
            tvSumOfPureOrder.setSelected(true);
            tvSumOfPureInvoice.setText(ServiceTools.formatPrice(TotalPureInvoice));
            tvSumOfPureInvoice.setSelected(true);
            tvSumOfChargeAndTaxOrder.setText(ServiceTools.formatPrice(TotalChargeAndTaxOrder));
            tvSumOfChargeAndTaxOrder.setSelected(true);
            tvSumOffChargeAndTaxInvoice.setText(ServiceTools.formatPrice(TotalChargeAndTaxInvoice));
            tvSumOffChargeAndTaxInvoice.setSelected(true);

            super.onPostExecute(result);
        }
    }


    private boolean checkServiceLocationIsRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setTrackingConfig() {
        btnTrackingService.setEnabled(!BaseActivity.getPrefAdminControl(mContext));
        if(BaseActivity.getPrefTrackingControl(mContext) == 1){
            btnTrackingService.setChecked(true);
            startLocationUpdate();
        }
    }


    private void forceEnableGps() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isGPSPROVIDEREnabled = service != null && service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPSPROVIDEREnabled) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.gps_must_be_activated)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.no, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void buildAlertSuggestGpsEnable() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.suggest_gps_activated)
                .setPositiveButton(getString(R.string.yes), new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.no, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        fromActivityResult = 1;

        if (requestCode == REQUEST_Location_ON) {
            if(resultCode != RESULT_OK){
                stopLocationUpdate();
            }else {
                startLocationUpdate();
            }
        }else if (requestCode == REQUEST_DATASYNC) {
            if(resultCode == RESULT_OK){
                FillView();
            }
        } else if (requestCode == REQUEST_CUSTOMER_LIST) {
            if(resultCode == RESULT_OK){
                CustomerId = data.getIntExtra(CUSTOMERID_KEY, 0);
                CustomerClientId = data.getLongExtra(CUSTOMER_CLIENT_ID_KEY, 0);
                Type = data.getIntExtra(TYPE_KEY, 0);
                GroupId = data.getLongExtra("GroupId", 0);

                if (Type == ProjectInfo.TYPE_INVOCIE) {
                    Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                    intent.putExtra(CUSTOMERID_KEY, CustomerId);
                    intent.putExtra(CUSTOMER_CLIENT_ID_KEY, CustomerClientId);
                    intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_INVOCIE);
                    intent.putExtra(MODE_PAGE, MODE_NEW);
                    startActivity(intent);
                } else if (Type == ProjectInfo.TYPE_ORDER) {
                    Intent intent = new Intent(getApplicationContext(), InvoiceDetailActivity.class);
                    intent.putExtra(CUSTOMERID_KEY, CustomerId);
                    intent.putExtra(CUSTOMER_CLIENT_ID_KEY, CustomerClientId);
                    intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_ORDER);
                    intent.putExtra(MODE_PAGE, MODE_NEW);
                    startActivity(intent);
                } else if (Type == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
                    Intent intent = new Intent(getApplicationContext(), ProductPickerListActivity.class);
                    intent.putExtra(PAGE, PAGE_DASHBORD);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_SEND_TRANSFERENCE);
                    intent.putExtra(MODE_PAGE, MODE_NEW);
                    intent.putExtra(CUSTOMERID_KEY, CustomerId);
                    intent.putExtra("GroupId", GroupId);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), ManageReceiptActivity.class);
                    intent.putExtra(PAGE, PAGE_DASHBORD);
                    intent.putExtra(CUSTOMERID_KEY, CustomerId);
                    intent.putExtra(CUSTOMER_CLIENT_ID_KEY, CustomerClientId);
                    intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                    intent.putExtra(MODE_PAGE, MODE_NEW);
                    startActivity(intent);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //-----------GCM-------------
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();

    }

    private void registerReceiver() {
        if (!isReceiverRegistered && mRegistrationBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    @Override
    protected void onDestroy() {
        LocationService.removeEventLocation(this.getLocalClassName());
        super.onDestroy();
    }

    private void showDialogRadara() {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_radara);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        Button dialog_btn_data = dialog.findViewById(R.id.dialog_btn_ok);

        dialog_btn_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void showDialogRequest() {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_request_gps);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        Button dialog_btn_data = dialog.findViewById(R.id.dialog_btn_ok);

        dialog_btn_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showPermissionDialog() {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);

        Button dialog_btn_data = dialog.findViewById(R.id.dialog_btn_ok);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        dialog_btn_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",
                        BuildConfig.APPLICATION_ID, null);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }
}
