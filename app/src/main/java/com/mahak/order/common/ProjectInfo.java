package com.mahak.order.common;

public class ProjectInfo {

    public static String DIRECTORY_BACKUPS = "Backups";
    public static int SortAsc = 1;
    public static int SortDesc = 2;
    public static String DIRECTORY_MAHAKORDER = "MahakOrder";
    public static String DIRECTORY_IMAGES = "Images";
    public static String DIRECTORY_SIGNS = "Signs";
    public static String DIRECTORY_ORDER_SIGNS = "OrderSign";
    public static String DIRECTORY_PDF = "PDF";
    public static String DIRECTORY_INVOICES = "Invoices";
    public static String DIRECTORY_Receipt = "Receipts";
    public static String DIRECTORY_REPORTS = "Reports";
    public static String DIRECTORY_PRODUCTS = "Products";
    public static String DIRECTORY_ASSETS = "Assets";
    public static String PRINT_LOGO_FILE_NAME = "img_print_logo.png";

    public static int IMMEDIATE = 1;
    public static int DONT_IMMEDIATE = 2;
    public static int CHEQUE_TYPE = 1;
    public static int CASHRECEIPT_TYPE = 2;
    public static int Bank_TYPE = 3;
    public static int Expense_TYPE = 4;
    public static int PUBLISH = 1;
    public static int DONT_PUBLISH = 0;
    public static int PUBLISH_ACCEPT = 2;
    public static int PUBLISH_REJECT = 3;
    public static int CUSTOMERID_GUEST = 0;
    public static long DONT_CUSTOMER_GROUP = 0;
    public static int promo_CUSTOMER_GROUP = -2;
    public static String DONT_CODE = "-1";

    public static int CHECKLIST_TYPE_ORDER = 1;
    public static int CHECKLIST_TYPE_DELIVERY = 2;
    public static int CHECKLIST_TYPE_RECEIPT = 3;

    public static int STATUS_DO = 3;
    public static int STATUS_NOT_DO = 2;

    public static final int TYPE_INVOCIE = 201;
    public static final int TYPE_RETURN_OF_SALE = 202;
    public static final int TYPE_ORDER = 203;
    public static final int TYPE_Delivery = 299;

    public static final int TYPE_SEND_TRANSFERENCE = 3;
    public static final int TYPE_RECEIVE_TRANSFERENCE = 4;
    public static final int TYPE_NON_REGISTER = 6;
    public static final int TYPE_PROMOTION = 7;
    public static final int TYPE_NON = -1;

    public static final int TYPE_Accept = 1;
    public static final int TYPE_Reject = -1;
    public static final int TYPE_NaN = 0;

    public static int Active_AdminControl = 1;
    public static int InActive_AdminControl = 0;

    public static int FINAL = 1;
    public static int NOt_FINAL = 2;

    public static double DEFAULT_LATITUDE = 31.638294;
    public static double DEFAULT_LONGITUDE = 54.165760;

    public static double DEFAULT_LATITUDE_DE = 48.134608;
    public static double DEFAULT_LONGITUDE_DE = 11.553203;

    public static int REPORT_TYPE_ORDER = 1;
    public static int REPORT_TYPE_INVOICE = 2;
    public static int REPORT_TYPE_RECEIPT = 3;

    public static int ASSET_ALL_PRODUCT = 0;
    public static int ASSET_EXIST_PRODUCT = 1;
    public static int ASSET_ZERO_PRODUCT = 2;
    public static int ASSET_NOT_EXIST_PRODUCT = 3;
    public static int ASSET_promotion = 4;

    //Printers Brand
    public static int PRINTER_BIXOLON_SPP_R200_II = 0;
    public static int PRINTER_HPRT = 1;
    public static int PRINTER_BABY_380_A = 2;
    public static int PRINTER_BABY_380_KOOHII = 3;
    public static int PRINTER_OSCAR_POS88MW = 4;
    public static int PRINTER_BIXOLON_SPP_R310 = 5;
    public static int PRINTER_DELTA_380_A = 6;
    public static int PRINTER_BABY_280_A = 7;
    public static int PRINTER_Rongta_RPP200 = 8;
    public static int PRINTER_SZZT_KS8223 = 9;
    public static int UROVO_K319 = 10;
    public static int Woosim_WSP_R341 = 11;
    public static int Centerm_K9 = 12;
    public static int SMART_POS_UROVO_i9000s = 13;

    //Statics
    public static int TYPE_CASH = 0;
    public static int TYPE_CHEQUE = 1;
    public static int TYPE_CASH_RECEIPT = 2;

    public static int TYPE_FACTOR = 2;
    public static int TYPE_PRE_FACTOR = 1;

    //Intent tag
    public static String _TAG_PATH = "tag_path";
    public static String _TAG_Name = "tag_name";
    public static String _TAG_MODE = "mode";
    public static String _TAG_TYPE = "type";
    public static String _TAG_PAGE_NAME = "pname";
    public static String _TAG_Order_Type = "tag_order_type";

    //Modes
    public static int _MODE_PATH = 0;

    //Pages Name
    public static String _pName_OrderDetail = "orderdetail";
    public static String _pName_DailyReport = "dailyreport";
    public static String _pName_CustomerReport = "customerreport";
    public static String _pName_ProductReport = "productreport";

    //web service
    public static String APPSIGN = "777E45E1-C80A-4D24-854F-DF8A75446B9B";
    public static String SOAP_ADDRESS = "http://order.mahaksoft.com/orderservice.asmx";
    //public static String SOAP_ADDRESS = "http://192.168.96.46/orderservice.asmx";
    public static String App_Id = "211003";

    //share Preference
    public static String pre_mode_state_product = "modeState";
    public static String pre_is_tracking = "hasTrackingRun";
    public static String pre_last_location = "lastLocation";
    public static String pre_is_tracking_pause = "hasTrackingPause";
    public static String pre_start_time_tracking = "startTracking";
    public static String pre_end_time_tracking = "endTracking";
    public static String pre_last_date_notification_tracking = "lastStartNotification";
    public static String pre_last_end_date_notification_tracking = "lastEndNotification";
    public static String pre_waiter = "waiter";
    public static String pre_gps_config = "gpsConfig";
    public static String pre_device_token = "deviceToken";

    //Json
    public static String _json_key_user_id = "userId";
    public static String _json_key_product_id = "productId";
    public static String _json_key_index = "index";
    public static String _json_key_date = "date";
    public static String _json_key_latitude = "latitude";
    public static String _json_key_longitude = "longitude";
    public static String _json_key_points = "points";
    public static String _json_key_result = "result";
    public static String _json_key_config = "result";
    public static String _json_key_mingps_time_change = "minGpsTimeChange";
    public static String _json_key_mingps_distance_change = "minGpsDistanceChange";
    public static String _json_key_banks = "BankNames";

    //Action
    public static String _notification_action_yes = "actionYes";
    public static String _notification_action_no = "actionNo";
    public static String _notification_action_stop = "actionStop";
    public static String _notification_action_pause = "actionPause";


    //According To
    public static String jame_mablaghe_kole_faktor = "0";
    public static String jame_aghlame_faktor = "1";
    public static String jame_hajme_aghlame_faktor = "2";
    public static String jame_vazne_aghlame_faktor = "3";
    public static String jame_anvae_aghlame_faktor = "4";
    public static String mablaghe_satre_faktor = "5";
    public static String meghdare_satre_faktor = "6";


    private Long Id;
    private Long ModifyDate;
    private Long UserId;
    private String MahakId;
    private String DatabaseId;


    private String CodePromotion;
    private String NamePromotion;
    private String DateStart;
    private String DateEnd;
    private String TimeStart;
    private String TimeEnd;
    private String DesPromotion;
    private String syncID;
    private String visitors;
    private String stores;
    private String createdBy;
    private String createdDate;
    private String modifiedBy;

    private int PriorityPromotion;
    private int LevelPromotion;
    private int AccordingTo;
    private int IsCalcLinear;
    private int TypeTasvieh;
    private int DeadlineTasvieh;
    private int IsActive;
    private int IsAllCustomer;
    private int IsAllVisitor;
    private int IsAllGood;
    private int isAllService;
    private int isAllStore;
    private int AggregateWithOther;


}
