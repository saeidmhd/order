package com.mahak.order;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mahak.order.common.CheckList;
import com.mahak.order.common.Customer;
import com.mahak.order.common.GPSTracker;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.SharedPreferencesHelper;
import com.mahak.order.common.User;
import com.mahak.order.gpsTracking.GpsTracking;
import com.mahak.order.service.ReadOfflinePicturesProducts;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.widget.FontAlertDialog;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends BaseActivity implements View.OnClickListener, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener {

    private static final int REQUEST_CUSTOMER_LIST = 2;
    private static int REQUEST_DATASYNC = 1;
    private Context mContext;
    private Activity mActivity;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerLeft;
    private ActionBarDrawerToggle mDrawerToggle;
    private int Type;
    private static final int ACCESS_FINE_LOCATION = 113;
    private static final int ACCESS_COARSE_LOCATION = 114;
    private static final int REQUEST_WRITE_STORAGE = 115;
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
            tvSumOfTransference,
            tvTrackingService;

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

    //------------GCM------------
    String SENDER_ID = "779811760050";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private PolylineOptions polylineOptions;
    private Marker marker;
    private Polyline polyline;
    private GpsTracking gpsTracking;
    private SwitchCompat btnTrackingService;
    private Menu menu;
    private int CustomerId;
    private long CustomerClientId;
    private long GroupId;
    private boolean ACCESS_COARSE_LOCATION_Permission;
    private boolean ACCESS_FINE_LOCATION_Permission;
    private boolean hasWritePermission;
    private AsynCustomer asyncustomer;
    SupportMapFragment mapFragment;

    int CountCustomer;
    int CountProduct;
    int CountOrder;
    int CountInvoice;
    int CountDeliverOrder;
    int CountReceipt;
    int CountPayable;
    int CountReturnOfSales;
    int CountNonRegisters;
    int CountPromotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

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

        init();

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

        //getCountForShowInDrawer();

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

                if (isChecked && BaseActivity.getPrefAdminControl(mContext) == 0) {
                    tvTrackingService.setText(R.string.tracking_system_is_active);
                    suggestEnableGps();
                } else if (!isChecked && BaseActivity.getPrefAdminControl(mContext) == 0) {
                    tvTrackingService.setText(R.string.tracking_system_is_disabled);
                }

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
                Type = ProjectInfo.TYPE_INVOCIE;
                Intent intent = new Intent(mContext, PeopleListActivity.class);
                intent.putExtra(PAGE, PAGE_ADD_INVOICE);
                startActivityForResult(intent, REQUEST_CUSTOMER_LIST);
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

        registerInBackground();



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

    private void registerInBackground() {
        FirebaseApp.initializeApp(DashboardActivity.this);
        FirebaseMessaging.getInstance().subscribeToTopic("");
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                String token = task.getResult();
                Intent intent = new Intent(DashboardActivity.this, RegistrationIntentService.class);
                intent.putExtra("token",token);
                startService(intent);
            }
        });


       /* FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<InstallationTokenResult> task) {
                String token = task.getResult().getToken();
                Intent intent = new Intent(DashboardActivity.this, RegistrationIntentService.class);
                intent.putExtra("token",token);
                startService(intent);
            }
        });*/
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BaseActivity.TransferenceFlag = 0;
    }

    /**
     * Initializing Variables
     */
    private void init() {

        db = new DbAdapter(mContext);
        db.open();

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
        btnTrackingService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ACCESS_FINE_LOCATION_Permission) {
                    ActivityCompat.requestPermissions(DashboardActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            ACCESS_FINE_LOCATION);
                }
                if (gpsTracking == null) gpsTracking = new GpsTracking(mContext);
                if (gpsTracking.isRunService()) {
                    gpsTracking.stopTracking();
                    gpsTracking.stopNotificationServiceTracking();
                    btnTrackingService.setChecked(false);
                    BaseActivity.setPrefTrackingControl(0);
                } else {
                    ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_is_tracking_pause, "0");
                    //gpsTrackingNewApi.setupSignalR(mContext);
                    gpsTracking.startTracking();
                    gpsTracking.showNotificationServiceRun();
                    if (gpsTracking.isRunService()) {
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
            }
        });


        //Version/////////////////////////////////////////////////////
        String version = ServiceTools.getVersionName(mContext);
        tvVersion.setText(version);
        //////////////////////////////////////////////////////////////

    }//end of init

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

                    double Latitude = ServiceTools.RegulartoDouble(customer.getLatitude());
                    double Longitude = ServiceTools.RegulartoDouble(customer.getLongitude());

                    if (Latitude != 0 && Longitude != 0) {
                        positions.add(new LatLng(ServiceTools.RegulartoDouble(customer.getLatitude()), ServiceTools.RegulartoDouble(customer.getLongitude())));
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
                    initMap();
                }
            });
        }

        GpsTracking.addEventLocation(this.getLocalClassName(), new GpsTracking.EventLocation() {
            @Override
            public void onReceivePoint(final Location location) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (location == null) {
                            if (gpsTracking == null)
                                gpsTracking = new GpsTracking(getBaseContext());
                            if (btnTrackingService != null)
                                btnTrackingService.setChecked(gpsTracking.isRunService());
                            return;
                        }
                        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                        if (polyline != null) {
                            List<LatLng> points = polyline.getPoints();
                            points.add(position);
                            polyline.setPoints(points);
                        }
                        if (marker != null) {
                            marker.remove();
                        }
                        if (mGoogleMap != null) {
                            marker = mGoogleMap.addMarker(new MarkerOptions().position(position));
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_visitor_3));
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, mGoogleMap.getCameraPosition().zoom));
                        }

                    }
                });
            }
        });
        /////////////////////////////////////////
        try {
            if (arrayChecklist.size() == 0)
                positions.clear();

            if (positions.size() == 0) {
                GPSTracker gpsTracker = new GPSTracker(mContext);
                double _Latitude, _Longitude;
                if (gpsTracker.canGetLocation()) {
                    _Latitude = gpsTracker.getLatitude();
                    _Longitude = gpsTracker.getLongitude();
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(_Latitude, _Longitude), 10));
                } else {
                    if (SharedPreferencesHelper.getCurrentLanguage(mContext).equals("en"))
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ProjectInfo.DEFAULT_LATITUDE, ProjectInfo.DEFAULT_LONGITUDE), 8));
                    else if (SharedPreferencesHelper.getCurrentLanguage(mContext).equals("de_DE")) {
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ProjectInfo.DEFAULT_LATITUDE_DE, ProjectInfo.DEFAULT_LONGITUDE_DE), 8));
                    }
                }
            } else {
                //setMapPoints(positions);
                for (int i = 0; i < positions.size(); i++) {
                    if (arrayChecklist.get(i).getName() != null)
                        mGoogleMap.addMarker(new MarkerOptions().position(positions.get(i)).title(arrayChecklist.get(i).getName()));
                }
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positions.get(positions.size() - 1), 14));
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);

        }
        /////////////////////////////////////////

        //Fill btnNav___________________________________________________________________________

    }

    public class AsynCustomer extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            customers = db.getAllOfCustomer();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            addMarkerToMap();

            super.onPostExecute(result);
        }
    }

    private void addMarkerToMap() {
        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                if (googleMap != null)
                    mGoogleMap = googleMap;

                // Add Customer markers to the map.
                addCustomerMarkersToMap();

                // Set listener for marker click event.  See the bottom of this class for its behavior.
                mGoogleMap.setOnMarkerClickListener(DashboardActivity.this);

                // Set listener for map click event.  See the bottom of this class for its behavior.
                mGoogleMap.setOnMapClickListener(DashboardActivity.this);

                // Override the default content description on the view, for accessibility mode.
                // Ideally this string would be localized.
                googleMap.setContentDescription("");

               /* LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (LatLng latLng : customerPositions){
                    builder.include(latLng);
                }
                LatLngBounds bounds = builder.build();

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));*/

                initMap();
            });
        }
    }

    private void addCustomerMarkersToMap() {

        mGoogleMap.clear();

        for (Customer customer : customers) {

            if (customer.getOrderCount() > 0) {
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(ServiceTools.RegulartoDouble(customer.getLatitude()), ServiceTools.RegulartoDouble(customer.getLongitude())))
                        .title(customer.getName())
                        .snippet(getString(R.string.order_has_been_registered))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            } else {
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(ServiceTools.RegulartoDouble(customer.getLatitude()), ServiceTools.RegulartoDouble(customer.getLongitude())))
                        .title(customer.getName())
                        .snippet(getString(R.string.order_has_not_been_registered))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }

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

    private void initMap() {
        if (polyline != null)
            polyline.remove();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(2);
        polylineOptions.color(Color.RED);
        polylineOptions.visible(true);
        polyline = mGoogleMap.addPolyline(polylineOptions);
        gpsTracking = new GpsTracking(getBaseContext());
        marker = gpsTracking.drawGoogleMap(mGoogleMap, marker, polyline);
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
            // The showing info window has already been closed - that's the first thing to happen
            // when any marker is clicked.
            // Return true to indicate we have consumed the event and that we do not want the
            // the default behavior to occur (which is for the camera to move such that the
            // marker is centered and for the marker's info window to open, if it has one).
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
                        Intent intent = new Intent(getApplicationContext(), DataSyncActivityRestApi.class);
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

   /* @Override
    protected void onResume() {

        if (db == null)
            db = new DbAdapter(this);

        db.open();


        if (!ACCESS_FINE_LOCATION_Permission) {
            ActivityCompat.requestPermissions(DashboardActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION);
        }

        if (!ACCESS_COARSE_LOCATION_Permission) {
            ActivityCompat.requestPermissions(DashboardActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_COARSE_LOCATION);
        }

        invalidateOptionsMenu();

        refreshCountInformation();


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (mGoogleMap != null)
            initMap();

        if (gpsTracking == null) gpsTracking = new GpsTracking(this);
        if (gpsTracking.isRunService()) {
            btnTrackingService.setChecked(true);
            gpsTracking.showNotificationServiceRun();
        } else {
            btnTrackingService.setChecked(false);
            gpsTracking.stopNotificationServiceTracking();
        }

        getTrackingconfig();

        if (btnTrackingService.isChecked() && BaseActivity.getPrefAdminControl(mContext) == 1) {

            tvTrackingService.setText(R.string.tracking_system_is_active_admin);
            forceEnableGps();

        } else if (!btnTrackingService.isChecked() && BaseActivity.getPrefAdminControl(mContext) == 1) {
            tvTrackingService.setText(R.string.tracking_system_is_disabled_admin);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        *//*asyncustomer = new AsynCustomer();
        asyncustomer.execute();*//*


        super.onResume();

        //----------GCM------------
        registerReceiver();
    }*/


    private void forceEnableGps() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isGPSPROVIDEREnabled = service != null && service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPSPROVIDEREnabled) {
            buildAlertMessageNoGps();
        }
    }

    private void suggestEnableGps() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isGPSPROVIDEREnabled = service != null && service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = service != null && service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSPROVIDEREnabled && !isNetworkEnabled) {
            buildAlertSuggestGpsEnable();
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

    private void getTrackingconfig() {

        if (BaseActivity.getPrefAdminControl(mContext) == 1 && BaseActivity.getPrefTrackingControl(mContext) == 1) {

            if (gpsTracking == null) gpsTracking = new GpsTracking(mContext);
            ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_is_tracking_pause, "0");
            gpsTracking.startTracking();
            gpsTracking.showNotificationServiceRun();
            if (gpsTracking.isRunService()) {
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
            btnTrackingService.setEnabled(false);
        } else if (BaseActivity.getPrefAdminControl(mContext) == 1 && BaseActivity.getPrefTrackingControl(mContext) == 0) {

            if (gpsTracking.isRunService()) {
                gpsTracking.stopTracking();
                gpsTracking.stopNotificationServiceTracking();
                btnTrackingService.setChecked(false);
            }
            btnTrackingService.setEnabled(false);


        } else if (BaseActivity.getPrefAdminControl(mContext) == 0 && BaseActivity.getPrefTrackingControl(mContext) == 1) {
            if (gpsTracking == null) gpsTracking = new GpsTracking(mContext);
            ServiceTools.setKeyInSharedPreferences(mContext, ProjectInfo.pre_is_tracking_pause, "0");
            gpsTracking.startTracking();
            gpsTracking.showNotificationServiceRun();
            if (gpsTracking.isRunService()) {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CUSTOMER_LIST) {

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
        GpsTracking.removeEventLocation(this.getLocalClassName());
        super.onDestroy();
    }

}
