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
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.PolyUtil;
import com.mahak.order.common.CheckList;
import com.mahak.order.common.Customer;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.User;
import com.mahak.order.tracking.LocationService;
import com.mahak.order.tracking.MapPolygon;
import com.mahak.order.tracking.ShowPersonCluster;
import com.mahak.order.tracking.TrackingConfig;
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
import java.util.Calendar;
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
    private static final int ACCESS_FINE_LOCATION = 113;
    private static final int ACCESS_COARSE_LOCATION = 114;
    private static final int REQUEST_WRITE_STORAGE = 115;
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
            btnNavReportsList,
            btnAddNewTransference;

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
            tvVersion,
            tvSumOfTransference;
    public static TextView tvTrackingService;


    private ListView lstCheckList;

    private LinearLayout
            llInvoice,
            llOrder,
            llReceipt,
            llTransference;

    private Marker mSelectedMarker;


    private int DefaultMapZoom = 14;
    private DbAdapter db;
    public static ArrayList<CheckList> arrayChecklist = new ArrayList<>();
    private ShowCheckListArrayAdapter adChecklist;
    private Customer customer;
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
    private static final String TAG = "MainActivity";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private PolylineOptions polylineOptions;
    private Marker marker;
    private Polyline polyline;
    private LocationService locationService;
    public static SwitchCompat btnTrackingService;
    private Menu menu;
    private int CustomerId;
    private long CustomerClientId;
    private long GroupId;
    private boolean ACCESS_COARSE_LOCATION_Permission;
    private boolean ACCESS_FINE_LOCATION_Permission;
    private boolean hasWritePermission;
    private AsynCustomer asyncustomer;
    SupportMapFragment mapFragment;
    private static final int REQUEST_Location_ON = 1200;


    private static final String TAG = DashboardActivity.class.getSimpleName();

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

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
    private boolean isServiceRun = false;
    private List<LatLng> latLngpoints = new ArrayList<>();
    private FontProgressDialog pd;
    private static LatLng lastPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerReceiverToCheckGpsOnOff();

        setContentView(R.layout.activity_dashboard);

        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }

        ACCESS_COARSE_LOCATION_Permission = (ContextCompat.checkSelfPermission(DashboardActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        ACCESS_FINE_LOCATION_Permission = (ContextCompat.checkSelfPermission(DashboardActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        hasWritePermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if (!hasWritePermission) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
            }
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

        mContext = this;
        mActivity = this;

        initUI();

        isServiceRun = isMyServiceRunning(LocationService.class);

        if(!isServiceRun){
            btnTrackingService.setChecked(false);
            long masterUserId = BaseActivity.getPrefUserMasterId(mContext);
            ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_is_tracking + masterUserId, "0");
        }

        btnTrackingService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationService == null) locationService = new LocationService(mContext,DashboardActivity.this);
                if (locationService.isRunService()) {
                    locationService.stopTracking();
                    locationService.stopNotificationServiceTracking();
                    btnTrackingService.setChecked(false);

                } else {
                    startTracking();
                }
            }
        });

        update();

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
        if (user != null)
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

        btnTrackingService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                setTackingServiceText(isChecked);
            }
        });
        new ReadOfflinePicturesProducts(this).readAllImages();
        ///////////////////
        btnAddNewTransference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(mDrawerLeft))
                    mDrawerLayout.closeDrawers();
                BaseActivity.TransferenceFlag = 1;
                Intent intent = new Intent(getApplicationContext(), ProductPickerListActivity.class);
                intent.putExtra(PAGE, PAGE_DASHBORD);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_SEND_TRANSFERENCE);
                intent.putExtra(MODE_PAGE, MODE_NEW);
                intent.putExtra(CUSTOMERID_KEY, ProjectInfo.CUSTOMERID_GUEST);
                startActivity(intent);

            }
        });
        btnAddNewOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InvoiceDetailActivity.orderDetails.clear();
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
                InvoiceDetailActivity.orderDetails.clear();
                if (mDrawerLayout.isDrawerOpen(mDrawerLeft))
                    mDrawerLayout.closeDrawers();
                if(CanRegisterInvoiceOutOfZone()){
                    Type = ProjectInfo.TYPE_INVOCIE;
                    Intent intent = new Intent(mContext, PeopleListActivity.class);
                    intent.putExtra(PAGE, PAGE_ADD_INVOICE);
                    startActivityForResult(intent, REQUEST_CUSTOMER_LIST);
                }else {
                    Toast.makeText(mContext, "خارج از منطقه یا خاموش بودن سامانه ردیابی ! امکان ثبت فاکتور وجود ندارد.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnZoomMapView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, MapViewActivity.class);
                intent.putParcelableArrayListExtra(COORDINATE, positions);
                intent.putParcelableArrayListExtra(CustomerPositions, customerPositions);
                startActivity(intent);

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

        llTransference.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                intent.putExtra(PAGE, PAGE_DASHBORD);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_SEND_TRANSFERENCE);
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


        //on receive message from google gcm
        MyGcmListenerService.receiveMessag = new View.OnClickListener() {
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
        super.onResume();
        setTrackingConfig();
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
                            long masterUserId = BaseActivity.getPrefUserMasterId(context);
                            ServiceTools.setKeyInSharedPreferences(context, ProjectInfo.pre_is_tracking + masterUserId, "0");
                            if (locationService == null) locationService = new LocationService(mContext, DashboardActivity.this);
                            locationService.stopTracking();
                            locationService.stopNotificationServiceTracking();
                            btnTrackingService.setChecked(false);
                        }
                    }
                }
            }
        };
        this.getApplicationContext().registerReceiver(receiver, filter);
    }


    private boolean checkPermissions() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
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
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(DashboardActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                locationService.startTracking();
            } else {
                btnTrackingService.setChecked(false);
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
                        })
                        .show();
            }
        }
    }

    private void loadLastPoint() {
        new loadingGpsLocation().execute();
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

        loadLastPoint();

        ServiceTools.setSettingPreferences(db, mContext);

        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));

        tvSumOfOrders = (TextView) findViewById(R.id.tvSumOfOrders);
        tvSumOfReceipts = (TextView) findViewById(R.id.tvSumOfReceipts);
        lstCheckList = (ListView) findViewById(R.id.lstCheckList);
        mDrawerLeft = (RelativeLayout) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        btnAddNewOrder = (Button) findViewById(R.id.btnAddNewOrder);
        btnAddNewInvoice = (Button) findViewById(R.id.btnAddNewInvoice);

        btnAddNewReceipt = (Button) findViewById(R.id.btnAddNewReceipt);
        btnAddNewTransference = (Button) findViewById(R.id.btnAddNewTransference);
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
        tvSumOfTransference = (TextView) findViewById(R.id.tvSumOfTransference);
        tvVersion = (TextView) findViewById(R.id.tvVersion);
        llReceipt = (LinearLayout) findViewById(R.id.llReceipt);
        llOrder = (LinearLayout) findViewById(R.id.llOrder);
        llInvoice = (LinearLayout) findViewById(R.id.llInvoice);
        llTransference = (LinearLayout) findViewById(R.id.llTransference);

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
        btnNavInvoiceList.setCompoundDrawables(null, null, new IconicsDrawable(mContext, FontAwesome.Icon.faw_file_alt2).color(Color.WHITE).sizeDp(30), null);
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

                    item.setAddress(customer.getAddress());
                    item.setName(customer.getName());
                    item.setMarketName(customer.getOrganization());
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

                    if(polyline != null)
                        polyline.setPoints(latLngpoints);
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
                            if(saveInDb)
                                drawLineBetweenPoints(lastPosition);
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
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_visitor_3));
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
                    if (arrayChecklist.get(i).getName() != null)
                        mGoogleMap.addMarker(new MarkerOptions().position(positions.get(i)).title(arrayChecklist.get(i).getName()));
                }
            }

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positions.get(positions.size() - 1), 14));

        } catch (Exception e) {

        }
    }

    private void drawLineBetweenPoints(LatLng position) {
        if (polyline != null) {
            List<LatLng> points = polyline.getPoints();
            points.add(position);
            polyline.setPoints(points);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        bindService(new Intent(this, LocationService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);

        setButtonsState(Utils.requestingLocationUpdates(this));
    }

    private void setButtonsState(boolean requestingLocationUpdates) {
        btnTrackingService.setChecked(requestingLocationUpdates);
    }

    class loadingGpsLocation extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            if (db == null) db = new DbAdapter(mContext);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            long userId = BaseActivity.getPrefUserMasterId(mContext);
            db.open();
            latLngpoints = db.getAllLatLngPointsFromDate(calendar.getTimeInMillis(), userId);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(polyline != null)
                polyline.setPoints(latLngpoints);
            showMarkerOnMap(getLastPoint());
        }
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
        if (polyline != null)
            polyline.remove();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(2);
        polylineOptions.color(Color.RED);
        polylineOptions.visible(true);
        polyline = mGoogleMap.addPolyline(polylineOptions);
        if (locationService == null)
            locationService = new LocationService(getBaseContext(),DashboardActivity.this);
    }


    private void refreshCountInformation() {
        if (btnNavCustomerList == null)
            return;

        CountCustomer = db.getTotalCountPeople();
        CountProduct = db.getTotalCountProduct();
        CountOrder = db.getTotalCountOrder();
        CountInvoice = db.getTotalCountInvoice();
        CountDeliverOrder = db.getTotalCountDeliveryOrder();
        CountReceipt = db.getTotalCountReceipt();
        CountPayable = db.getTotalCountPayable();
        CountReturnOfSales = db.getTotalCountReturnOfSale();
        CountNonRegisters = db.getTotalCountNonRegister();
        CountPromotion = db.getTotalCountPromotion();

        BaseActivity.setPrefProductCount(CountProduct);
        BaseActivity.setPrefPersonCount(CountCustomer);


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
                startActivity(intent);
                break;
            case R.id.btnNavContact:
                mDrawerLayout.closeDrawers();
                intent = new Intent(getApplicationContext(), ContactActivity.class);
                startActivity(intent);
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
                if (checklist.getName() != null)
                    tvName.setText(checklist.getName().trim());

                if (checklist.getAddress() != null)
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
                        RefreshPreferenceUser();
                        Intent intent = new Intent(DashboardActivity.this, LoginActivityRestApi.class);
                        startActivity(intent);
                        setPrefSignalUserToken("");
                        dialog.dismiss();
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
                        Intent intent = new Intent(getApplicationContext(),DataSyncActivityRestApi.class);
                        startActivity(intent);
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

    public void update (){

        if (db == null)
            db = new DbAdapter(this);

        db.open();

        double TotalOrder = db.getTotalPriceOrder();
        double TotalInvoice = db.getTotalPriceInvoice();
        double TotalReceiveTransfer = db.getTotalReceiveTransfer();
        double TotalReceipt = db.getTotalPriceReceipt();
        double TotalCash = db.getTotalCashAmountReceipt();
        double TotalCheque = db.getTotalChequeReceipt();
        double TotalCashReceipt = db.getTotalCashReceipt();
        double TotalDiscountOrder = db.getTotalDiscountOrder();
        double TotalDiscountInvoice = db.getTotalDiscountInvoice();
        double TotalChargeAndTaxOrder = db.getTotalChargeAndTaxOrder();
        double TotalChargeAndTaxInvoice = db.getTotalChargeAndTaxInvoice();
        double TotalPureOrder = db.getPurePriceOrder();
        double TotalPureInvoice = db.getPurePriceInvoice();

        tvSumOfOrders.setText(ServiceTools.formatPrice(TotalOrder));
        tvSumOfOrders.setSelected(true);
        tvSumOfInvoices.setText(ServiceTools.formatPrice(TotalInvoice));
        tvSumOfInvoices.setSelected(true);
        tvSumOfTransference.setText(ServiceTools.formatPrice(TotalReceiveTransfer));
        tvSumOfTransference.setSelected(true);
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
    }


    private boolean checkServiceLocationIsRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void startTracking() {
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            if (locationService == null) locationService = new LocationService(mContext,DashboardActivity.this);
            ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_is_tracking_pause, "0");
            locationService.startTracking();
            if (locationService.isRunService()) {
                btnTrackingService.setChecked(true);
            } else {
                Toast.makeText(DashboardActivity.this, R.string.can_not_active_gps_tracking, Toast.LENGTH_SHORT).show();
                if (mTracker != null) {
                    mTracker.send(new HitBuilders.ExceptionBuilder()
                            .setDescription("Can't Start GPS")
                            .setFatal(true)
                            .build());
                }
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

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CUSTOMER_LIST) {

        if (requestCode == REQUEST_Location_ON) {
            if(resultCode != RESULT_OK){
                if (locationService.isRunService()) {
                    locationService.stopTracking();
                    locationService.stopNotificationServiceTracking();
                    btnTrackingService.setChecked(false);
                }
            }else {
                if (!checkPermissions()) {
                    requestPermissions();
                } else {
                    if (locationService.isRunService()) {
                        ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_is_tracking_pause, "0");
                        locationService.startTracking();
                    }
                }
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
                GroupId = data.getLongExtra(CUSTOMER_GROUP_KEY, 0);

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
                    intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
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

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        LocationService.removeEventLocation(this.getLocalClassName());
        super.onDestroy();
    }

}
