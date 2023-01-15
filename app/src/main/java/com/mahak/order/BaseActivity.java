package com.mahak.order;

import android.annotation.SuppressLint;
import android.app.ActivityManager.TaskDescription;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.SharedPreferencesHelper;
import com.mahak.order.common.User;
import com.mahak.order.common.Visitor;
import com.mahak.order.libs.FontsOverride;
import com.mahak.order.storage.DbAdapter;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    //public static final String BaseUrl ="http://192.168.89.31/mds/sync/" ;
    //public static final String BaseUrl ="http://order.mahaksoft.com:8085/sync/" ;
    //public static final String BaseUrl = "https://bazaraservices.mahaksoft.com/v3/sync/";
    public static final String baseUrlApi = "https://mahakacc.mahaksoft.com/api/v3/sync/";
    //public static final String baseUrlApi = "https://mahakacc.mahaksoft.com/api/testv3/sync/";
    public static final String setDeviceTokenUrl = "https://mahakacc.mahaksoft.com/public/api/";

    /*public static final String baseUrlTracking = "https://tracking.mahaksoft.com/api/";
    public static final String baseUrlSignalR = "https://tracking.mahaksoft.com/TrackingHub?token=";
    public static final String baseUrlShowRouting = "https://tracking.mahaksoft.com/showrouting";*/

    public static final String baseUrlTracking = "https://tracking.mahaksoft.com:404/api/";
    public static final String baseUrlSignalR = "https://tracking.mahaksoft.com:404/TrackingHub?token=";
    public static final String baseUrlShowRouting = "https://tracking.mahaksoft.com:404/showrouting";


    //public static final String BaseUrl ="http://bazaraservices.mahaksoft.com:444/sync/" ;
    public static final String baseUrlImage = "https://mahakacc.mahaksoft.com";
    public static String NegotiateToken = "";

    public static Typeface font_yekan;
    public static Typeface font_openSans;
    public static Typeface font_yekan_bold;

    public static String MODE_PAGE = "ModePage";
    public static String PAGE = "Page";
    public static String ID = "Id";
    public static String COORDINATE = "Coordinate";
    public static String CustomerPositions = "CustomerPositions";
    public static String Customers = "Customers";
    public static String CUSTOMERID_KEY = "CustomerId";
    public static String CUSTOMER_CLIENT_ID_KEY = "CustomerClientId";
    public static String CUSTOMER_GROUP_KEY = "CustomerGroupId";
    public static String Category_GROUP_KEY = "CategoryGroupId";
    public static String CUSTOMER_NAME_KEY = "CustomerName";
    public static String POSITION_KEY = "Position";
    public static String TYPE_KEY = "Type";
    public static String eshantion_dasti = "eshantion_dasti";
    public static String duplicate_product = "duplicate_product";
    public static String CODE_KEY = "Code";
    public static String PAYMENT_KEY = "Payment";
    public static String remainVisitorCredit = "remainVisitorCredit";
    public static String RETURN_ASSET_KEY = "ReturnAsset";
    public static String RETURN_PUBLISH_KEY = "Publish";


    public static int MODE_EDIT = 1;
    public static int MODE_NEW = 2;

    public static int darsadi = 1;
    public static int mablaghi = 0;

    public static int PAGE_DASHBORD = 4;
    public static int PAGE_CHECKLIST = 5;
    public static int PAGE_MANAGE_RECEIPT = 6;
    public static int PAGE_RECEIPTLIST = 7;
    public static int PAGE_ORDERLIST = 8;
    public static int PAGE_MANAGE_DLIVERYORDER = 9;
    public static int PAGE_ADD_ORDER = 10;
    public static int PAGE_ADD_INVOICE = 11;
    public static int PAGE_ORDER_DETAIL = 12;
    public static int PAGE_ADD_SEND_TRANSFER = 13;
    public static int PAGE_PAYABLE_LIST = 14;
    public static int PAGE_ADD_RETURN = 15;
    public static int PAGE_ADD_NON_REGISTER = 16;
    public static int PAGE_NON_REGISTER_LIST = 17;
    public static int PAGE_NON_REGISTER_DETAIL = 18;
    public static int PAGE_Invoice_Detail_Activity = 19;

    public static int discount_mablaghi = 0;
    public static int discount_darsadi = 1;

    static SharedPreferences sh;
    public static long DateSyncInformation = 0;
    public static String _Key_DateSyncInformation = "DateSyncInfo";
    public static String _Key_VisitorID = "UserId";
    public static String _Key_DefPrice = "VisitorDefPrice";
    public static String _Key_UserToken = "UserToken";
    public static String _Key_SignalUserToken = "SignalUserToken";
    public static String _Key_StoreCode = "StoreCode";
    public static String _Key_UserMasterId = "UserMasterId";
    public static String _Key_productCount = "productCount";
    public static String _Key_personCount = "personCount";
    public static String _Key_Username = "Username";
    public static String _Key_PrefArabicReceipt = "PrefArabicReceipt";
    public static String _Key_PrefRadara = "PrefRadara";
    public static String _Key_name = "name";
    public static String _Key_tell = "tell";
    public static String _Key_MahakId = "MahakId";
    public static String _Key_DatabaseId = "DatabaseId";
    public static String _Key_AdminControl = "AdminControl";
    public static String _Key_Unit2Setting = "Unit2Setting";
    public static String _Key_TextView = "TextView";
    public static String _Key_TrackingControl = "TrackingControl";
    public static String _Key_DbVersion = "DbVersion";

    //setting
    public static String _Key_CubicMeterActive = "CubicMeterActive";
    public static String _Key_ForceDetailActive = "ForceDetailActive";
    public static String _Key_PropertyActive = "PropertyActive";
    public static String _Key_RowDiscountIsActive = "RowDiscountIsActive";
    public static String _Key_ApplyRowDiscount = "ApplyRowDiscount";
    public static String _Key_ChargePercent = "ChargePercent";
    public static String _Key_TaxPercent = "TaxPercent";
    public static String _Key_CountDecimalPoint = "CountDecimalPoint";
    public static String _Key_PriceDecimalPoint = "PriceDecimalPoint";
    public static String _Key_TaxAndCharge = "TaxAndCharge";
    public static String _Key_AutoSync = "AutoSync";

    //Extra info item type
    public static int person_extra_info = 101;
    public static int tax_sell_in_cost = 102;
    public static int average_last_price = 10201;
    public static int city_zone = 140;
    public static int product_category = 10202;
    public static int person_category = 10102;
    public static int category = 130;


    public static String _Key_SortDirection = "SortDirection";
    public static String _Key_SortBase = "SortBase";
    public static String _Key_SortBase_customer = "SortBase_customer";

    //Global Count --- Price

    public static String strCount;
    public static String strPrice;

    //////////////////////////sml
    public static String __pref_current_printer = "CurrentPrinter";
    public static String __pref_under_print_text = "UnderPrintText";
    public static String __pref_under_print_text1 = "UnderPrintText1";
    public static String __pref_under_print_text2 = "UnderPrintText2";
    public static String __pref_under_print_text_status = "UnderPrintTextStatus";
    public static String __pref_under_print_sign = "UnderPrintSign";
    public static String __pref_print_logo_status = "PrintLogoStatus";
    public static String __pref_current_Width = "CurrentWidth";
    public static String __pref_title_text = "titleText";
    public static String __pref_title_text_status = "titleTextStatus";

    public static String _Key_SyncId = "SyncId";
    public static String _Key_Store = "Store";
    public static String _Key_ServerUserId = "ServerUserId";

    public static String pref_sort_base_product ="Products.ProductCode";
    public static String pref_sort_base_customer = "Customers.PersonCode";
    public static String pref_sort_direction = "Asc";


    public static long DefaultUserId = 0;
    public static int DefaultDefLevel = 0;
    public static int Defaultcount = 0;
    public static long Default = 0;
    public static String DefaultUserToken = "";
    public static long DefaultMasterId = 0;
    public static String DefaultDatabaseId = "";
    public static boolean DefaultAdminControl = false;
    public static int DefaultUnit2Setting = 1;
    public static int DefaultTextView = 0;
    public static int DefaultTrackingControl = 0;
    public static int DefaultDBVersion = 0;
    public static String DefaultSyncId = "0";
    public static String DefaultStore = "1";
    public static String DefaultServerUserId = "0";
    public static String SharedPreferencesMahak = "shMahak";

    public static String Active = "1.00000000";
    public static String InActive = "0.00000000";
    public static String TaxPercent = "4.00000000";
    public static String ChargePercent = "6.00000000";
    public static String CountDecimalPoint = "4";
    public static String PriceDecimalPoint = "0";

    public static String invisible = "invisible";

    //Setting
    public static String DefaultTaxAndChargeIsActive = Active;
    public static String DefaultAutoSyncValue = InActive;
    public static String DefaultTaxPercent = ChargePercent;
    public static String DefaultCountDecimalPoint = CountDecimalPoint;
    public static String DefaultPriceDecimalPoint = PriceDecimalPoint;
    public static String DefaultChargePercent = TaxPercent;
    public static String DefaultRowDiscountIsActive = invisible;
    public static String DefaultApplyRowDiscount = InActive;
    public static String DefaultTwoUnitActive = Active;
    public static String DefaultKolJozActive = Active;
    public static String DefaultPropertyActive = Active;
    public static String DefaultVariableTwoUnitActive = Active;
    public static String DefaultOneUnitActive = Active;
    public static String DefaultForceDetailActive = Active;
    public static String DefaultCubicMeterActive = Active;

    public static String T_WithUserId = "WUserId";
    public static String T_DontUserId = "UserId";

    public static String Type_Login = "T_login";
    public static int bnd_Login_Splash = 1;

    public static final int MODE_YekVahedi = 1;
    public static final int Mode_DoVahedi = 2;
    public static final int MODE_MeghdarJoz = 3;


    /////// TAG INTERNET /////////////////////////////
    public static String TAG_RESULT = "result";
    public static String TAG_MSG = "msg";
    public static String TAG_DATA = "data";
    public static String TAG_DETAILS = "details";
    public static String RES_TRUE = "True";
    public static String RES_FALSE = "False";
    public static String TAG_ARRAY = "array";
    public static String TAG_NEXT = "next";
    public static String TAG_Transfers = "Transfers";
    public static String TAG_DELETED_DATA = "DeletedData";

    //////////////////////////////////////////////////

    //Type Transference
    public static int TransferenceFlag = 0;

    //Type LastUpdate
    public static int CondDontUserId = 1;
    public static int CondWithUserId = 2;
    public static int CondTran = 3;
    public static int CondDelivery = 4;

    public static int ReturnReason = 202;
    public static int NonRegisterReason = 138;

    //Tax And Charge//////////////////////////////
    public static int TAX_EXEMPTE = -1;
    public static int CHARGE_EXEMPTE = -1;
    public static int TAX = 0;
    public static int CHARGE = 0;
    ////////////////////////////////////////////
    public static int REPORT_MONTHLY_ORDER = 1;
    public static int REPORT_MONTHLY_INVOICE = 2;
    public static int REPORT_MONTHLY_RECEIPT = 3;
    ////////////////////////////////////////////

    public static Context mContext;
    public static Typeface font_byekan;
    private static String _Key_CustomerMaxRowVersion = "CustomerMaxRowVersion";
    public static String __pref_template2 = "template2";
    public Tracker mTracker;

    private static final int REQUEST_PHONE_PERMISSION = 113;
    private static final int REQUEST_CONTACTS_PERMISSION = 114;
    private static final int REQUEST_LOCATION_PERMISSION = 115;
    private static final int REQUEST_STORAGE_PERMISSION = 116;
    private static final int REQUEST_CAMERA_PERMISSION = 116;

    public static boolean HasCameraPermission = false;
    public static boolean HasContactsPermission = false;
    public static boolean HasLocationPermission = false;
    public static boolean HasPhonePermission = false;
    public static boolean HasStoragePermission = false;


    public final static int OneUnitActiveCode = 1022;
    public final static int TwoUnitActiveCode = 1000;
    public final static int TwoUnitKolJozCode = 1015;

    public final static int CountDecimalPointCode = 1032;
    public final static int PriceDecimalPointCode = 1033;

    public final static int Row_Discount_Type = 1042;
    public final static int APPLY_DISCOUNT = 26008;

    public final static int ChargePercentCode = 14000;
    public final static int TaxPercentCode = 14001;
    public final static int TaxAndChargeIsActiveCode = 14008;


    public final static int VariableTwoUnitActiveCode = 10037;
    public final static int PropertyActiveCode = 1013;
    public final static int ForceDetailActiveCode = 1038;
    public final static int CubicMeterActiveCode = 1019;

    public final static int AutoSyncCode = 26006;



    //126501,126502,126503


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mContext = this;

        super.onCreate(savedInstanceState);
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {

            //change status bar color
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));

            //change recent app header color
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            TaskDescription taskDesc = new TaskDescription(getString(R.string.app_name), bm, getResources().getColor(R.color.primary_dark));
            setTaskDescription(taskDesc);
        }

        FontsOverride.setDefaultFont(this, "DEFAULT", "DroidNaskh-Regular-SystemUI.ttf");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getPrimaryColor()));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setLogo(R.drawable.img_null);

        font_byekan = Typeface.createFromAsset(getAssets(), "fonts/BYEKAN.TTF");
        sh = this.getSharedPreferences(SharedPreferencesMahak, MODE_PRIVATE);
        RefreshPreference();

        getPrefReduceAsset(mContext);
        getPrefShowFieldOrder(mContext);

        AnalyticsTrackers application = (AnalyticsTrackers) getApplication();
        mTracker = application.getDefaultTracker();

        //font_yekan = Typeface.createFromAsset(getAssets(), "fonts/IRANYekanRegularMobile(FaNum).ttf");
        font_yekan = Typeface.createFromAsset(getAssets(), "fonts/IRANYekanBold(FaNum).ttf");
        font_openSans = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Semibold.ttf");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Configuration configuration = getResources().getConfiguration();
            configuration.setLayoutDirection(new Locale("en"));
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        }


    }//end of onCreate

    public void checkCameraPermission() {
        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Page~" + getClass().getName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Share")
                .build());
        setDimensions();
        getPrefUsername();

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SOURCE, "Page~" + getClass().getName());
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, getPrefDatabaseId());
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        firebaseAnalytics.setUserProperty("visitorDetail", getPrefname() + "_" + getPrefTell());
        firebaseAnalytics.setUserId(getPrefname() + "_" + getPrefTell());
    }


    private void setDimensions() {

        //custom dimension
        //DeviceModelNameDetail
        try {
            mTracker.send(new HitBuilders.ScreenViewBuilder()
                    .setCustomDimension(1, "" + Build.MANUFACTURER + Build.MODEL)
                    .build()
            );
        } catch (Exception e) {
            ServiceTools.logToFireBase(e);
            e.printStackTrace();
        }
    }

    public static void RefreshPreference() {
        //
        DateSyncInformation = sh.getLong(_Key_DateSyncInformation, 0);
        sh.getLong(_Key_VisitorID, DefaultUserId);
        sh.getLong(_Key_UserMasterId, DefaultMasterId);
        sh.getString(_Key_Username, "");
        sh.getString(_Key_MahakId, "");
        sh.getString(_Key_DatabaseId, DefaultDatabaseId);
        ///////////////////////sml
        sh.getString(__pref_current_printer, "");
    }

    public static void RefreshPreferenceUser() {
        sh.edit().putLong(_Key_VisitorID, DefaultUserId).commit();
        sh.edit().putLong(_Key_UserMasterId, DefaultMasterId).commit();
        sh.edit().putString(_Key_Username, "").commit();
        sh.edit().putString(_Key_name, "").commit();
        sh.edit().putString(_Key_MahakId, "").commit();
        sh.edit().putString(_Key_DatabaseId, DefaultDatabaseId).commit();
        sh.edit().putString(_Key_SyncId, DefaultSyncId).commit();
    }


    public static String getPrefAutoSyncValue() {
        return sh.getString(_Key_AutoSync, DefaultAutoSyncValue);
    }

    public static void setPrefAutoSyncValue(String value) {
        sh.edit().putString(_Key_AutoSync, value).commit();
    }

    public static String getPrefTaxAndChargeIsActive() {
        return sh.getString(_Key_TaxAndCharge, DefaultTaxAndChargeIsActive);
    }

    public static void setPrefTaxAndChargeIsActive(String value) {
        sh.edit().putString(_Key_TaxAndCharge, value).commit();
    }

    public static String getPrefSortDirection() {
        return sh.getString(_Key_SortDirection, pref_sort_direction);
    }

    public static void setPrefSortDirection(String value) {
        sh.edit().putString(_Key_SortDirection, value).commit();
    }

    public static String getPrefSortBase_product() {
        return sh.getString(_Key_SortBase, pref_sort_base_product);
    }

    public static void setPrefSortBase_product(String value) {
        sh.edit().putString(_Key_SortBase, value).commit();
    }

    public static String getPrefSortBase_customer() {
        return sh.getString(_Key_SortBase_customer, pref_sort_base_customer);
    }

    public static void setPrefSortBase_customer(String value) {
        sh.edit().putString(_Key_SortBase_customer, value).commit();
    }

    public static String getPrefTaxPercent() {
        return sh.getString(_Key_TaxPercent, DefaultTaxPercent);
    }

    public static void setPrefTaxPercent(String value) {
        sh.edit().putString(_Key_TaxPercent, value).commit();
    }


    public static String getPrefCountDecimalPoint() {
        return sh.getString(_Key_CountDecimalPoint, DefaultCountDecimalPoint);
    }

    public static void setPrefCountDecimalPoint(String value) {
        sh.edit().putString(_Key_CountDecimalPoint, value).commit();
    }

    public static String getPrefPriceDecimalPoint() {
        return sh.getString(_Key_PriceDecimalPoint, DefaultPriceDecimalPoint);
    }

    public static void setPrefPriceDecimalPoint(String value) {
        sh.edit().putString(_Key_PriceDecimalPoint, value).commit();
    }

    public static String getPrefChargePercent() {
        return sh.getString(_Key_ChargePercent, DefaultChargePercent);
    }

    public static void setPrefChargePercent(String value) {
        sh.edit().putString(_Key_ChargePercent, value).commit();
    }

    public static String getRowDiscountType() {
        return sh.getString(_Key_RowDiscountIsActive, DefaultRowDiscountIsActive);
    }

    public static void setRowDiscountType(String value) {
        sh.edit().putString(_Key_RowDiscountIsActive, value).commit();
    }

    public static String getPrefApplyRowDiscount() {
        return sh.getString(_Key_ApplyRowDiscount, DefaultApplyRowDiscount);
    }

    public static void setPrefApplyRowDiscount(String value) {
        sh.edit().putString(_Key_ApplyRowDiscount, value).commit();
    }

    public static String getPrefPropertyActive() {
        return sh.getString(_Key_PropertyActive, DefaultPropertyActive);
    }

    public static void setPrefPropertyActive(String value) {
        sh.edit().putString(_Key_PropertyActive, value).commit();
    }

    public static String getPrefForceDetailActive() {
        return sh.getString(_Key_ForceDetailActive, DefaultForceDetailActive);
    }

    public static void setPrefForceDetailActive(String value) {
        sh.edit().putString(_Key_ForceDetailActive, value).commit();
    }

    public static String getPrefCubicMeterActive() {
        return sh.getString(_Key_CubicMeterActive, DefaultCubicMeterActive);
    }

    public static void setPrefCubicMeterActive(String value) {
        sh.edit().putString(_Key_CubicMeterActive, value).commit();
    }


    public static long getPrefUserId(Context context) {
        if (sh == null) sh = context.getSharedPreferences(SharedPreferencesMahak, MODE_PRIVATE);
        return sh.getLong(_Key_VisitorID, DefaultUserId);
    }

    public static long getPrefUserId() {
        return sh.getLong(_Key_VisitorID, DefaultUserId);
    }

    public static void setPrefUserId(long userId) {
        sh.edit().putLong(_Key_VisitorID, userId).commit();
    }

    public static int getPrefDefSellPrice() {
        return sh.getInt(_Key_DefPrice, DefaultDefLevel);
    }

    public static void setPrefDefSellPrice(int defLevel) {
        sh.edit().putInt(_Key_DefPrice, defLevel).commit();
    }

    public static String getPrefUserToken() {
        return sh.getString(_Key_UserToken, DefaultUserToken);
    }

    public static void setPrefUserToken(String userToken) {
        sh.edit().putString(_Key_UserToken, userToken).commit();
    }

    public static String getPrefSignalUserToken() {
        return sh.getString(_Key_SignalUserToken, DefaultUserToken);
    }

    public static void setPrefSignalUserToken(String userToken) {
        sh.edit().putString(_Key_SignalUserToken, userToken).commit();
    }

    public static long getPrefUserMasterId() {
        return sh.getLong(_Key_UserMasterId, DefaultUserId);
    }

    public static long getPrefUserMasterId(Context context) {
        if (sh == null) sh = context.getSharedPreferences(SharedPreferencesMahak, MODE_PRIVATE);
        return sh.getLong(_Key_UserMasterId, DefaultUserId);
    }

    public static void setPrefUserMasterId(long userMasterId) {
        sh.edit().putLong(_Key_UserMasterId, userMasterId).commit();
    }


    public static int getPrefProductCount(Context context) {
        if (sh == null) sh = context.getSharedPreferences(SharedPreferencesMahak, MODE_PRIVATE);
        return sh.getInt(_Key_productCount, Defaultcount);
    }

    public static void setPrefProductCount(int count) {
        sh.edit().putInt(_Key_productCount, count).commit();
    }

    public static int getPrefPersonCount(Context context) {
        if (sh == null) sh = context.getSharedPreferences(SharedPreferencesMahak, MODE_PRIVATE);
        return sh.getInt(_Key_personCount, Defaultcount);
    }

    public static void setPrefPersonCount(int count) {
        sh.edit().putInt(_Key_personCount, count).commit();
    }

    public static boolean getPrefArabicReceipt() {
        return sh.getBoolean(_Key_PrefArabicReceipt, false);
    }

    public static void setPrefArabicReceipt(boolean value) {
        sh.edit().putBoolean(_Key_PrefArabicReceipt, value).commit();
    }

    public static boolean isRadaraActive() {
        return sh.getBoolean(_Key_PrefRadara, false);
    }

    public static void setRadaraActive(boolean value) {
        sh.edit().putBoolean(_Key_PrefRadara, value).commit();
    }


    public static String getPrefUsername() {
        return sh.getString(_Key_Username, "");
    }

    public static void setPrefUsername(String name) {
        sh.edit().putString(_Key_Username, name).commit();
    }

    public static String getPrefname() {
        return sh.getString(_Key_name, "");
    }

    public static void setPrefname(String name) {
        sh.edit().putString(_Key_name, name).commit();
    }

    public static String getPrefTell() {
        return sh.getString(_Key_tell, "");
    }

    public static void setPrefTell(String name) {
        sh.edit().putString(_Key_tell, name).commit();
    }

    public static String getPrefMahakId() {
        return sh.getString(_Key_MahakId, "");
    }

    public static String getPrefMahakId(Context context) {
        if (sh == null) sh = context.getSharedPreferences(SharedPreferencesMahak, MODE_PRIVATE);
        return sh.getString(_Key_MahakId, "");
    }

    public static void setPrefMahakId(String Username) {
        sh.edit().putString(_Key_MahakId, Username).commit();
    }

    public static String getPrefDatabaseId() {
        return sh.getString(_Key_DatabaseId, DefaultDatabaseId);
    }

    public static String getPrefDatabaseId(Context context) {
        if (sh == null) sh = context.getSharedPreferences(SharedPreferencesMahak, MODE_PRIVATE);
        return sh.getString(_Key_DatabaseId, DefaultDatabaseId);
    }

    public static void setPrefAdminControl(boolean AdminControl) {
        sh.edit().putBoolean(_Key_AdminControl, AdminControl).commit();
    }

    public static boolean getPrefAdminControl(Context context) {
        if (sh == null) sh = context.getSharedPreferences(SharedPreferencesMahak, MODE_PRIVATE);
        return sh.getBoolean(_Key_AdminControl, DefaultAdminControl);
    }

    public static void setPrefUnit2Setting(int Unit2Setting) {
        sh.edit().putInt(_Key_Unit2Setting, Unit2Setting).commit();
    }

    public static int getPrefUnit2Setting(Context context) {
        if (sh == null) sh = context.getSharedPreferences(SharedPreferencesMahak, MODE_PRIVATE);
        return sh.getInt(_Key_Unit2Setting, DefaultUnit2Setting);
    }

    public static void setPrefTextView(int TextViewControl) {
        sh.edit().putInt(_Key_TextView, TextViewControl).commit();
    }

    public static int getPrefTextView(Context context) {
        if (sh == null) sh = context.getSharedPreferences(SharedPreferencesMahak, MODE_PRIVATE);
        return sh.getInt(_Key_TextView, DefaultTextView);
    }

    public static void setPrefDbVersion(int dbVersion) {
        sh.edit().putInt(_Key_DbVersion, dbVersion).commit();
    }

    public static int getPrefDbVersion(Context context) {
        if (sh == null) sh = context.getSharedPreferences(SharedPreferencesMahak, MODE_PRIVATE);
        return sh.getInt(_Key_DbVersion, DefaultDBVersion);
    }

    public static void setPrefTrackingControl(int TrackingControl) {
        sh.edit().putInt(_Key_TrackingControl, TrackingControl).commit();
    }

    public static int getPrefTrackingControl(Context context) {
        if (sh == null) sh = context.getSharedPreferences(SharedPreferencesMahak, MODE_PRIVATE);
        return sh.getInt(_Key_TrackingControl, DefaultTrackingControl);
    }

    public static void setPrefDatabaseId(String databaseId) {
        sh.edit().putString(_Key_DatabaseId, databaseId).commit();
    }

    public static String getPrefSyncId() {
        return sh.getString(_Key_SyncId, DefaultSyncId);
    }

    public static void setPrefSyncId(String syncId) {
        sh.edit().putString(_Key_SyncId, syncId).commit();
    }

    public static String getPrefStore() {
        return sh.getString(_Key_Store, DefaultStore);
    }

    public static void setPrefStore(String Store) {
        sh.edit().putString(_Key_Store, Store).commit();
    }

    public static void setPrefServerUserId(String Store) {
        sh.edit().putString(_Key_ServerUserId, Store).commit();
    }

    public static String getPrefServerUserId() {
        return sh.getString(_Key_ServerUserId, DefaultServerUserId);
    }

    public static void setPrefDateSyncInformation(long Date) {
        sh.edit().putLong(_Key_DateSyncInformation, Date).commit();
    }

    public static long getPrefDateSyncInformation() {
        return sh.getLong(_Key_DateSyncInformation, DateSyncInformation);
    }

    public static Boolean getAuthentication() {
        return true;
    }

    public static User getUserProfile() {

        DbAdapter mDb = new DbAdapter(mContext);
        mDb.open();
        User mUser = mDb.getUser();
        mDb.close();

        return mUser;
    }

    public static Visitor getVisitor() {

        DbAdapter mDb = new DbAdapter(mContext);
        mDb.open();
        Visitor mVisitor = mDb.getVisitor();
        mDb.close();

        return mVisitor;
    }

    public static boolean getPrefReduceAsset(Context context) {
        return SharedPreferencesHelper.getPrefReduceAsset(context, false);
    }

    public static boolean getPrefShowFieldOrder(Context context) {
        return SharedPreferencesHelper.getPrefShowFieldOrder(context, false);
    }

    public static String getCurrentPrinter() {
        return sh.getString(__pref_current_printer, "");
    }

    public static void setCurrentPrinter(String mac) {
        sh.edit().putString(__pref_current_printer, mac).commit();
    }

    public static int getPrimaryColor() {
        return mContext.getResources().getColor(R.color.primary);
    }

    public static void setUnderPrintText(Context context, String pName, String text) {
        SharedPreferences spf = context.getSharedPreferences(BaseActivity.SharedPreferencesMahak, MODE_PRIVATE);
        spf.edit().putString(BaseActivity.__pref_under_print_text + "_" + pName, text.trim()).commit();
    }
    public static void setUnderPrintText1(Context context, String pName, String text) {
        SharedPreferences spf = context.getSharedPreferences(BaseActivity.SharedPreferencesMahak, MODE_PRIVATE);
        spf.edit().putString(BaseActivity.__pref_under_print_text1 + "_" + pName, text.trim()).commit();
    }
    public static void setUnderPrintText2(Context context, String pName, String text) {
        SharedPreferences spf = context.getSharedPreferences(BaseActivity.SharedPreferencesMahak, MODE_PRIVATE);
        spf.edit().putString(BaseActivity.__pref_under_print_text2 + "_" + pName, text.trim()).commit();
    }

    public static String getUnderPrintText(Context context, String pName) {
        SharedPreferences spf = context.getSharedPreferences(BaseActivity.SharedPreferencesMahak, MODE_PRIVATE);
        return spf.getString(BaseActivity.__pref_under_print_text + "_" + pName, "");
    }
    public static String getUnderPrintText1(Context context, String pName) {
        SharedPreferences spf = context.getSharedPreferences(BaseActivity.SharedPreferencesMahak, MODE_PRIVATE);
        return spf.getString(BaseActivity.__pref_under_print_text1 + "_" + pName, "");
    }
    public static String getUnderPrintText2(Context context, String pName) {
        SharedPreferences spf = context.getSharedPreferences(BaseActivity.SharedPreferencesMahak, MODE_PRIVATE);
        return spf.getString(BaseActivity.__pref_under_print_text2 + "_" + pName, "");
    }

    public static void setUnderPrintTextStatus(Context context, String pName, Boolean status) {
        SharedPreferences spf = context.getSharedPreferences(BaseActivity.SharedPreferencesMahak, MODE_PRIVATE);
        spf.edit().putBoolean(BaseActivity.__pref_under_print_text_status + "_" + pName, status).commit();
    }

    public static Boolean getUnderPrintTextStatus(Context context, String pName) {
        SharedPreferences spf = context.getSharedPreferences(BaseActivity.SharedPreferencesMahak, MODE_PRIVATE);
        return spf.getBoolean(BaseActivity.__pref_under_print_text_status + "_" + pName, false);
    }

    public static void setPrintLogoStatus(Context context, String pName, Boolean status) {
        SharedPreferences spf = context.getSharedPreferences(BaseActivity.SharedPreferencesMahak, MODE_PRIVATE);
        spf.edit().putBoolean(BaseActivity.__pref_print_logo_status + "_" + pName, status).commit();
    }

    public static Boolean getPrintLogoStatus(Context context, String pName) {
        SharedPreferences spf = context.getSharedPreferences(BaseActivity.SharedPreferencesMahak, MODE_PRIVATE);
        return spf.getBoolean(BaseActivity.__pref_print_logo_status + "_" + pName, true);
    }

    //back events
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return (true);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    public static long getPrefCustomerMaxRowVersion() {
        return sh.getLong(_Key_CustomerMaxRowVersion, Default);
    }

    public static void setPrefCustomerMaxRowVersion(long rowVersion) {
        sh.edit().putLong(_Key_CustomerMaxRowVersion, rowVersion).commit();
    }

    public static void setTemplate2Status(Context context, String pName, Boolean status) {
        SharedPreferences spf = context.getSharedPreferences(BaseActivity.SharedPreferencesMahak, MODE_PRIVATE);
        spf.edit().putBoolean(BaseActivity.__pref_template2 + "_" + pName, status).commit();
    }

    public static Boolean getTemplate2Status(Context context, String pName) {
        SharedPreferences spf = context.getSharedPreferences(BaseActivity.SharedPreferencesMahak, MODE_PRIVATE);
        return spf.getBoolean(BaseActivity.__pref_template2 + "_" + pName, false);
    }


    public static void setTitleTextStatus(Context context, Boolean status) {
        SharedPreferences spf = context.getSharedPreferences(BaseActivity.SharedPreferencesMahak, MODE_PRIVATE);
        spf.edit().putBoolean(BaseActivity.__pref_title_text_status, status).commit();
    }

    public static Boolean getTitleStatus(Context context) {
        SharedPreferences spf = context.getSharedPreferences(BaseActivity.SharedPreferencesMahak, MODE_PRIVATE);
        return spf.getBoolean(BaseActivity.__pref_title_text_status, false);
    }

    public static void setTitleText(Context context, String text) {
        SharedPreferences spf = context.getSharedPreferences(BaseActivity.SharedPreferencesMahak, MODE_PRIVATE);
        spf.edit().putString(BaseActivity.__pref_title_text, text.trim()).commit();
    }

    public static String getTitleText(Context context) {
        SharedPreferences spf = context.getSharedPreferences(BaseActivity.SharedPreferencesMahak, MODE_PRIVATE);
        return spf.getString(BaseActivity.__pref_title_text, "");
    }


}
