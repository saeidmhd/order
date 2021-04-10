package com.mahak.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.device.PrinterManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuItemCompat;
import androidx.print.PrintHelper;

import com.android.print.sdk.PrinterConstants;
import com.android.print.sdk.PrinterInstance;
import com.bixolon.printer.BixolonPrinter;
import com.centerm.smartpos.aidl.printer.AidlPrinter;
import com.centerm.smartpos.aidl.printer.AidlPrinterStateChangeListener;
import com.centerm.smartpos.aidl.sys.AidlDeviceManager;
import com.centerm.smartpos.constant.Constant;
import com.centerm.smartpos.util.LogUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;


import com.kishcore.sdk.hybrid.api.DataCallback;
import com.kishcore.sdk.hybrid.api.SDKManager;
import com.mahak.order.Baby380A.ConnMoreDevicesActivity;
import com.mahak.order.Baby380A.DeviceConnFactoryManager;
import com.mahak.order.SDKUrovo.PublicClass;
import com.mahak.order.SDKUrovo.UrovoDeviceList;
import com.mahak.order.Woosim.BluetoothPrintService;
import com.mahak.order.Woosim.DeviceList;
import com.mahak.order.common.Printer;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.SharedPreferencesHelper;
import com.mahak.order.oscar_printer.IPrinterOpertion;
import com.mahak.order.oscar_printer.WifiOperation;
import com.mahak.order.oscar_printer.utils.PrintUtils;
import com.mahak.order.rongta.RongtaDeviceListActivity;
import com.mahak.order.utils.PDF;
import com.mahak.order.utils.PDF_DE;
import com.mahak.order.widget.FontAlertDialog;
import com.mahak.order.widget.FontProgressDialog;
import com.mocoo.hang.rtprinter.driver.Contants;
import com.mocoo.hang.rtprinter.driver.HsBluetoothPrintDriver;
import com.printer.command.EscCommand;
import com.woosim.printer.WoosimCmd;
import com.woosim.printer.WoosimImage;
import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.Vector;

import PRTAndroidSDK.PRTAndroidPrint;

import static android.device.PrinterManager.PRNSTS_BUSY;
import static android.device.PrinterManager.PRNSTS_ERR;
import static android.device.PrinterManager.PRNSTS_ERR_DRIVER;
import static android.device.PrinterManager.PRNSTS_OK;
import static android.device.PrinterManager.PRNSTS_OUT_OF_PAPER;
import static android.device.PrinterManager.PRNSTS_OVER_HEAT;
import static android.device.PrinterManager.PRNSTS_UNDER_VOLTAGE;
import static com.mahak.order.BackupListActivity.DATABASE_DIRECTORY;
import static com.mahak.order.common.ProjectInfo.DIRECTORY_ORDER_SIGNS;
import static com.mahak.order.common.ProjectInfo.PRINTER_SZZT_KS8223;
import static com.mahak.order.common.ProjectInfo.Woosim_WSP_R341;

public class PrintActivity extends BaseActivity {

    private static final int PRINT_SIGN_REQUEST = 1001;

    private static final int CONN_MOST_DEVICES = 0x11;
    private int id = 0;

    String sdPath = "";
    private String OrderCode = "";
    private Context mContext;
    private Activity mActivity;
    int printerBrand = ProjectInfo.PRINTER_BIXOLON_SPP_R200_II;
    //Bixolon
    static BixolonPrinter mBixolonPrinter;
    private String mConnectedDeviceName = null;
    private boolean mIsConnected;
    //HPRT
    private static PRTAndroidPrint PRT = null;
    public static final String TAG = "Print";

    static final String ACTION_GET_DEFINEED_NV_IMAGE_KEY_CODES = "com.bixolon.anction.GET_DEFINED_NV_IMAGE_KEY_CODES";
    static final String ACTION_COMPLETE_PROCESS_BITMAP = "com.bixolon.anction.COMPLETE_PROCESS_BITMAP";
    static final String ACTION_GET_MSR_TRACK_DATA = "com.bixolon.anction.GET_MSR_TRACK_DATA";
    static final String EXTRA_NAME_NV_KEY_CODES = "NvKeyCodes";
    static final String EXTRA_NAME_MSR_MODE = "MsrMode";
    static final String EXTRA_NAME_MSR_TRACK_DATA = "MsrTrackData";
    static final String EXTRA_NAME_BITMAP_WIDTH = "BitmapWidth";
    static final String EXTRA_NAME_BITMAP_HEIGHT = "BitmapHeight";
    static final String EXTRA_NAME_BITMAP_PIXELS = "BitmapPixels";

    static final int REQUEST_CODE_SELECT_FIRMWARE = Integer.MAX_VALUE;
    static final int RESULT_CODE_SELECT_FIRMWARE = Integer.MAX_VALUE - 1;
    static final int MESSAGE_START_WORK = Integer.MAX_VALUE - 2;
    static final int MESSAGE_END_WORK = Integer.MAX_VALUE - 3;
    static final String FIRMWARE_FILE_NAME = "FirmwareFileName";
    private static final int REQUEST_WRITE_STORAGE = 112;

    //rongta
    private static final int REQUEST_CONNECT_RONGTA = 3;
    private static BluetoothDevice device;
    private BluetoothAdapter mBluetoothAdapter = null;
    public static HsBluetoothPrintDriver BLUETOOTH_PRINTER = null;


    ImageView imgPrint;
    LinearLayout ll_btn_connect;
    Button btnPrint, btnConnect;
    TextView tvStatus;
    CoordinatorLayout coordinatorLayout;
    Bitmap bPrint = null;
    String fPath = "";
    String fName = "";
    View placeholder;
    //int pMode = ProjectInfo._MODE_PATH;
    private static final int PRINT_SETTING_REQUEST = 999;
    private String pageName;
    private int orderType;


    //Baby380A
    public static final String CONNECT_STATUS = "connect.status";
    private static final String DEBUG_TAG = "MainActivity";
    private int mPrinterIndex = 0;
    private int mTotalCopies = 0;
    private static final int MAIN_QUERY_PRINTER_STATUS = 0xfe;
    private static final int REQUEST_PRINT_LABEL = 0xfd;
    private static final int REQUEST_PRINT_RECEIPT = 0xfc;

    //share
    private ShareActionProvider mShareActionProvider;

    //Baby380

    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    int width;
    int height;

    //Oscar

    private IPrinterOpertion myOpertion;
    private PrinterInstance mPrinter;
    private FontProgressDialog progressDialog;
    private Dialog dialog;
    public static final int CONNECT_DEVICE = 88;
    private String FileName;
    Bitmap bitmapSign = null;
    private View print_lay;

    //urovo
    private PublicClass publicclass;
    private Runnable runnablePrint;
    private String mess;
    static final int PRINTER_UROVO = 1900;

    //woosim
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1234;
    static BluetoothPrintService mPrintService = null;

    public static final String FileNamePDF = "Pdf";

    public static File DIRECTORY_PDF = new File(DATABASE_DIRECTORY + "/" + FileNamePDF);


    //k9
    private AidlPrinter printDev = null;
    private AidlPrinterStateChangeListener callback = new PrinterCallback();
    public AidlDeviceManager manager = null;
    public ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            manager = null;
            //  LogUtil.print(getResources().getString(R.string.bind_service_fail));
            LogUtil.print("manager = " + manager);
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            manager = AidlDeviceManager.Stub.asInterface(service);
            // LogUtil.print(getResources().getString(R.string.bind_service_success));
            LogUtil.print("manager = " + manager);
            if (null != manager) {
                try {
                    printDev = AidlPrinter.Stub.asInterface(manager
                            .getDevice(Constant.DEVICE_TYPE.DEVICE_TYPE_PRINTERDEV));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                printDev.initPrinter();
                                mIsConnected = true;
                                refreshStatus();
                                //showMessage(getString(R.string.printer_init_success));
                            } catch (Exception e) {
                                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                                FirebaseCrashlytics.getInstance().recordException(e);
                                e.printStackTrace();
                                //showMessage(getString(R.string.printer_init_exception));
                            }
                        }
                    }).start();
                } catch (RemoteException e) {
                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                    FirebaseCrashlytics.getInstance().recordException(e);
                    e.printStackTrace();
                }
            }
        }
    };

    private PrinterManager mPrinterManager;

    //smartpos k9
    private class PrinterCallback extends AidlPrinterStateChangeListener.Stub {

        @Override
        public void onPrintError(int arg0) throws RemoteException {
            // showMessage("打印机异常" + arg0, Color.RED);
            // getMessStr(arg0);
        }

        @Override
        public void onPrintFinish() throws RemoteException {
            //showMessage(getString(R.string.printer_finish), "", Color.BLACK);
        }

        @Override
        public void onPrintOutOfPaper() throws RemoteException {
            // showMessage(getString(R.string.printer_need_paper), "", Color.RED);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        //Take instance of SDKManager in the Application class or the first activity

        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }

        mContext = this;
        mActivity = this;

        printerBrand = SharedPreferencesHelper.getPrefPrinterBrand(mContext);
        if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223){
            SDKManager.init(this);
        }


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fPath = extras.getString(ProjectInfo._TAG_PATH);
            fName = extras.getString(ProjectInfo._TAG_Name);
            pageName = extras.getString(ProjectInfo._TAG_PAGE_NAME);
            OrderCode = extras.getString("OrderCode", pageName);
            orderType = extras.getInt(ProjectInfo._TAG_Order_Type);
        }

        FileName = String.format("Signature_%s.png", OrderCode);
        bitmapSign = ServiceTools.getSign(FileName);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        printerBrand = SharedPreferencesHelper.getPrefPrinterBrand(mContext);

        //Rongta
        // Get device's Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //Baby380

        if (!(printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW || printerBrand == ProjectInfo.PRINTER_SZZT_KS8223 || printerBrand == ProjectInfo.Centerm_K9)) {
            mService = new BluetoothService(this, baby380mHandler);
            if (!mService.isAvailable()) {
                Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
                finish();
            }
        }

        init();
        initData();
        initPrinter();
        refreshStatus();

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsConnected) {

                    if (printerBrand == ProjectInfo.Woosim_WSP_R341) {
                        Intent intent = new Intent(PrintActivity.this, DeviceList.class);
                        startActivityForResult(intent, REQUEST_CONNECT_DEVICE_SECURE);
                    } else if (printerBrand == ProjectInfo.UROVO_K319) {
                        Intent intent = new Intent(PrintActivity.this, UrovoDeviceList.class);
                        startActivityForResult(intent, PRINTER_UROVO);
                    }
                    if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R200_II || printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310) {
                        mBixolonPrinter.findBluetoothPrinters();
                    } else if (printerBrand == ProjectInfo.PRINTER_HPRT) {
                        if (PRT != null) {
                            PRT.CloseProt();
                        }
                        Intent serverIntent = new Intent(getApplicationContext(), DeviceListActivity.class);
                        startActivityForResult(serverIntent, 10);
                        //ConnectType = "Bluetooth";
                    } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_A || printerBrand == ProjectInfo.PRINTER_DELTA_380_A || printerBrand == ProjectInfo.PRINTER_BABY_280_A) {

                        startActivityForResult(new Intent(PrintActivity.this, ConnMoreDevicesActivity.class), CONN_MOST_DEVICES);

                    } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII) {

                        Intent serverIntent = new Intent(PrintActivity.this, com.mahak.order.baby380_koohii_printer.DeviceListActivity.class);
                        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

                    } else if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW) {

                        myOpertion = new WifiOperation(PrintActivity.this, oscarHandler);
                        myOpertion.chooseDevice();

                    } else if (printerBrand == ProjectInfo.PRINTER_Rongta_RPP200) {

                        Intent intent = new Intent(PrintActivity.this, RongtaDeviceListActivity.class);
                        startActivityForResult(intent, REQUEST_CONNECT_RONGTA);

                    }
                } else {

                    if (printerBrand == ProjectInfo.Woosim_WSP_R341) {
                        if (mPrintService != null) mPrintService.stop();
                    } else if (printerBrand == ProjectInfo.UROVO_K319) {
                        publicclass.PrintDisconnect();
                        PublicClass.PrinterId = "";
                        mIsConnected = false;
                    } else if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R200_II || printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310) {
                        mBixolonPrinter.disconnect();
                    } else if (printerBrand == ProjectInfo.PRINTER_HPRT) {
                        PRT.ClosePort();
                        mIsConnected = false;
                        //invalidateOptionsMenu();
                    } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_A || printerBrand == ProjectInfo.PRINTER_DELTA_380_A || printerBrand == ProjectInfo.PRINTER_BABY_280_A) {

                        startActivityForResult(new Intent(PrintActivity.this, ConnMoreDevicesActivity.class), CONN_MOST_DEVICES);

                    } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII) {

                        if (mService != null)
                            mService.stop();
                        mIsConnected = false;
                    } else if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW) {

                        myOpertion.close();
                        myOpertion = null;
                        mPrinter = null;
                        mIsConnected = false;
                    } else if (printerBrand == ProjectInfo.PRINTER_Rongta_RPP200) {
                        if (BLUETOOTH_PRINTER != null) {
                            BLUETOOTH_PRINTER.stop();
                        }
                        mIsConnected = false;
                    }
                    setCurrentPrinter("");
                    RefreshPreference();
                    refreshStatus();
                }

            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (printerBrand == ProjectInfo.SMART_POS_UROVO_i9000s) {
                    Message msg = mPrintHandler.obtainMessage(PRINT_BITMAP);
                    msg.obj = bPrint;
                    msg.sendToTarget();
                } else if (printerBrand == ProjectInfo.Centerm_K9) {
                    printBmpFast(bPrint);
                } else if (printerBrand == ProjectInfo.Woosim_WSP_R341) {
                    printBitmap(10, 10, bPrint);
                } else if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223) {
                    if (SDKManager.getPrinterStatus() == SDKManager.STATUS_OK) {
                        btnPrint.setEnabled(false);
                        SDKManager.printBitmap(PrintActivity.this, bPrint, true, 100, data -> {
                            btnPrint.setEnabled(true);
                        });
                       /* SDKManager.print(PrintActivity.this, new PrinterPrintableData(bPrint),0, new DataCallback() {
                            @Override
                            public void onDataInserted(Object... objects) {
                            }
                        });*/
                    }else {
                        Toast.makeText(mContext, "پرینتر با مشکل مواجه است.", Toast.LENGTH_SHORT).show();
                    }
                } else if (printerBrand == ProjectInfo.UROVO_K319) {
                    if (bPrint != null) {
                        try {
                            SetImageBitmap();
                            if (PublicClass.PrinterId != null && PublicClass.PrinterId.length() > 0) {
                                new Thread(runnablePrint).start();
                            }
                        } catch (Exception e) {
                            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                            FirebaseCrashlytics.getInstance().recordException(e);
                            e.printStackTrace();
                            Toast.makeText(PrintActivity.this, getString(R.string.error) + "\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(PrintActivity.this, R.string.image_not_upload, Toast.LENGTH_LONG).show();
                    }
                } else {
                    makePrint(bPrint);
                }
            }
        });

    }//end of onCreate

    private void SetImageBitmap() {
        //int w = (bPrint.getHeight() * 570) / bPrint.getWidth();
        bPrint = publicclass.getResizedBitmap(bPrint, 570, bPrint.getHeight());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //reload my activity with permission granted or use the features what required the permission
                } else {
                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        if (!(printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW ||
                printerBrand == ProjectInfo.PRINTER_SZZT_KS8223 ||
                printerBrand == ProjectInfo.Centerm_K9)) {
            if(mService != null){
                  if (!mService.isBTopen()) {
                      Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                      startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                  }
            }
        }
        if (printerBrand == ProjectInfo.PRINTER_Rongta_RPP200) {
            if (BLUETOOTH_PRINTER == null) {
                initializeBluetoothDevice();
            } else {
                if (BLUETOOTH_PRINTER.IsNoConnection()) {
                    mIsConnected = false;
                    refreshStatus();
                } else {
                    mIsConnected = true;
                    refreshStatus();
                }
            }
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if(printerBrand == ProjectInfo.PRINTER_SZZT_KS8223){
            try {
                SDKManager.closePrinter();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onStop();
    }

    //woosim
    private final MyHandler Woosim_mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<PrintActivity> mActivity;

        MyHandler(PrintActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            PrintActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }

    private void handleMessage(Message msg) {
        switch (msg.what) {
            case BluetoothService.STATE_CONNECTED:
                mIsConnected = true;
                btnConnect.setEnabled(true);
                refreshStatus();
                break;
            case BluetoothService.STATE_CONNECTING:
                tvStatus.setText(getString(R.string.str_printer_mode_is_connecting));
                tvStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                btnConnect.setEnabled(false);
                break;
            case BluetoothService.STATE_LISTEN:
                if (!getCurrentPrinter().equals("")) {
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(getCurrentPrinter());
                    // Attempt to connect to the device
                    if (mPrintService != null) {
                        mPrintService.connect(device, true);
                    }
                }
                break;
            case BluetoothService.STATE_NONE:
                mIsConnected = false;
                RefreshPreference();
                refreshStatus();
                break;
            case BluetoothService.MESSAGE_CONNECTION_LOST:
                btnConnect.setEnabled(true);
                tvStatus.setText(getString(R.string.str_printer_mode_is_not_connect));
                break;
            case BluetoothService.MESSAGE_UNABLE_CONNECT:
                btnConnect.setEnabled(true);
                tvStatus.setText(getString(R.string.str_printer_mode_is_not_connect));
                BaseActivity.setCurrentPrinter("");
                break;
        }
    }


    public void init() {
        imgPrint = (ImageView) findViewById(R.id.imgPrint);
        ll_btn_connect = (LinearLayout) findViewById(R.id.ll_btn_connect);
        btnPrint = (Button) findViewById(R.id.btnPrint);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        progressDialog = new FontProgressDialog(mContext);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle(getString(R.string.trying_for_connection));
        progressDialog.setMessage(getString(R.string.please_wait_moment));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
    }

    public void initData() {
        View ll;
        printerBrand = SharedPreferencesHelper.getPrefPrinterBrand(mContext);

        if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223 || printerBrand == ProjectInfo.Centerm_K9 || printerBrand == ProjectInfo.SMART_POS_UROVO_i9000s) {
            ll_btn_connect.setVisibility(View.GONE);
        }

        bPrint = null;
        sdPath = Environment.getExternalStorageDirectory().toString() + "/" + fPath + "/" + fName;
        //bPrint = ShrinkBitmap(sdPath,800 , height);

        bPrint = BitmapFactory.decodeFile(sdPath);
        if (
                printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW ||
                        printerBrand == ProjectInfo.PRINTER_BABY_380_A ||
                        printerBrand == ProjectInfo.PRINTER_DELTA_380_A ||
                        printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII
        ) {

            // bPrint = ShrinkBitmap(sdPath,500 , bPrint.getHeight());
        }

        //  bPrint = Bitmap.createScaledBitmap(bPrint, 200, height, false);

        setTitleStr();

        if (getPrintLogoStatus(mContext, pageName)) {
            Bitmap blogo = ServiceTools.getPrintLogo();
            if (blogo != null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (printerBrand == ProjectInfo.PRINTER_BABY_380_A || printerBrand == ProjectInfo.PRINTER_DELTA_380_A) {
                    ll = inflater.inflate(R.layout.placeholder_print_logo80mm, null, false);
                    ImageView imgLogo = (ImageView) ll.findViewById(R.id.imgLogo);
                    imgLogo.setImageBitmap(blogo);

                } else if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310) {
                    ll = inflater.inflate(R.layout.placeholder_print_logo80mm, null, false);
                    ImageView imgLogo = (ImageView) ll.findViewById(R.id.imgLogo);
                    imgLogo.setImageBitmap(blogo);
                } else if (printerBrand == ProjectInfo.PRINTER_BABY_280_A) {
                    ll = inflater.inflate(R.layout.placeholder_print_logo50mm, null, false);
                    ImageView imgLogo = (ImageView) ll.findViewById(R.id.imgLogo);
                    imgLogo.setImageBitmap(blogo);
                } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII) {
                    ll = inflater.inflate(R.layout.placeholder_print_logo88mm, null, false);
                    ImageView imgLogo = (ImageView) ll.findViewById(R.id.imgLogo);
                    imgLogo.setImageBitmap(blogo);
                    LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                    ChangePrintWidth(_llPrint);

                } else if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW || printerBrand == ProjectInfo.UROVO_K319 || printerBrand == Woosim_WSP_R341) {
                    ll = inflater.inflate(R.layout.placeholder_print_logo88mm, null, false);
                    ImageView imgLogo = (ImageView) ll.findViewById(R.id.imgLogo);
                    imgLogo.setImageBitmap(blogo);
                    LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                    ChangePrintWidth(_llPrint);
                } else {
                    ll = inflater.inflate(R.layout.placeholder_print_logo, null, false);
                    ImageView imgLogo = (ImageView) ll.findViewById(R.id.imgLogo);
                    imgLogo.setImageBitmap(blogo);
                }
                ViewGroup.LayoutParams param = ll.getLayoutParams();

                ll.setDrawingCacheEnabled(true);
                ll.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                ll.layout(0, 0, bPrint.getWidth(), ll.getMeasuredHeight());
                ll.buildDrawingCache(true);
                Bitmap b = Printer.CreateBitmap(ll);
                ll.setDrawingCacheEnabled(false);
                bPrint = ServiceTools.mergeBitmap(b, bPrint, bPrint.getWidth());
                if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII)
                    Printer.CreateFile(bPrint, fName, fPath);
            }
        }

        if (getUnderPrintTextStatus(mContext, pageName)) {
            if (!getUnderPrintText(mContext, pageName).trim().equals("")) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                if (printerBrand == ProjectInfo.PRINTER_BABY_380_A || printerBrand == ProjectInfo.PRINTER_DELTA_380_A) {
                    placeholder = inflater.inflate(R.layout.placeholder_plain_text80mm, null, false);
                    TextView tvText = (TextView) placeholder.findViewById(R.id.tvText);
                    tvText.setText(getUnderPrintText(mContext, pageName).trim());

                } else if (printerBrand == ProjectInfo.PRINTER_BABY_280_A) {
                    placeholder = inflater.inflate(R.layout.placeholder_plain_text50mm, null, false);
                    TextView tvText = (TextView) placeholder.findViewById(R.id.tvText);
                    tvText.setText(getUnderPrintText(mContext, pageName).trim());

                } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII) {
                    placeholder = inflater.inflate(R.layout.placeholder_plain_text88mm, null, false);
                    TextView tvText = (TextView) placeholder.findViewById(R.id.tvText);
                    tvText.setText(getUnderPrintText(mContext, pageName).trim());
                    LinearLayout _llPrint = (LinearLayout) placeholder.findViewById(R.id._llPrint);
                    ChangePrintWidth(_llPrint);

                } else if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310) {
                    placeholder = inflater.inflate(R.layout.placeholder_plain_text80mm, null, false);
                    TextView tvText = (TextView) placeholder.findViewById(R.id.tvText);
                    tvText.setText(getUnderPrintText(mContext, pageName).trim());

                } else if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW || printerBrand == ProjectInfo.UROVO_K319 || printerBrand == Woosim_WSP_R341) {
                    placeholder = inflater.inflate(R.layout.placeholder_plain_text88mm, null, false);
                    TextView tvText = (TextView) placeholder.findViewById(R.id.tvText);
                    tvText.setText(getUnderPrintText(mContext, pageName).trim());
                    LinearLayout _llPrint = (LinearLayout) placeholder.findViewById(R.id._llPrint);
                    ChangePrintWidth(_llPrint);
                } else {
                    placeholder = inflater.inflate(R.layout.placeholder_plain_text, null, false);
                    TextView tvText = (TextView) placeholder.findViewById(R.id.tvText);
                    tvText.setText(getUnderPrintText(mContext, pageName).trim());
                }
                placeholder.setDrawingCacheEnabled(true);
                placeholder.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                placeholder.layout(0, 0, bPrint.getWidth(), placeholder.getMeasuredHeight());
                placeholder.buildDrawingCache(true);
                Bitmap b = Printer.CreateBitmap(placeholder);
                placeholder.setDrawingCacheEnabled(false);
                bPrint = ServiceTools.mergeBitmap(bPrint, b, bPrint.getWidth());
                if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII)
                    Printer.CreateFile(bPrint, fName, fPath);
            }
        }

        if (SharedPreferencesHelper.getSignUnderFactor(mContext) && bitmapSign != null) {
            ImageView imgSign;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (printerBrand == ProjectInfo.PRINTER_BABY_380_A || printerBrand == ProjectInfo.PRINTER_DELTA_380_A) {
                ll = inflater.inflate(R.layout.placeholder_print_sign80mm, null, false);
                imgSign = (ImageView) ll.findViewById(R.id.imgSign);
                imgSign.setImageBitmap(bitmapSign);

            } else if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310) {
                ll = inflater.inflate(R.layout.placeholder_print_sign80mm, null, false);
                imgSign = (ImageView) ll.findViewById(R.id.imgSign);
                imgSign.setImageBitmap(bitmapSign);
            } else if (printerBrand == ProjectInfo.PRINTER_BABY_280_A) {
                ll = inflater.inflate(R.layout.placeholder_print_sign50mm, null, false);
                imgSign = (ImageView) ll.findViewById(R.id.imgSign);
                imgSign.setImageBitmap(bitmapSign);
            } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII) {
                ll = inflater.inflate(R.layout.placeholder_print_sign88mm, null, false);
                imgSign = (ImageView) ll.findViewById(R.id.imgSign);
                imgSign.setImageBitmap(bitmapSign);
                LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                ChangePrintWidth(_llPrint);

            } else if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW || printerBrand == ProjectInfo.UROVO_K319 || printerBrand == Woosim_WSP_R341) {
                ll = inflater.inflate(R.layout.placeholder_print_sign88mm, null, false);
                imgSign = (ImageView) ll.findViewById(R.id.imgSign);
                imgSign.setImageBitmap(bitmapSign);
                LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                ChangePrintWidth(_llPrint);
            } else {
                ll = inflater.inflate(R.layout.placeholder_print_sign, null, false);
                imgSign = (ImageView) ll.findViewById(R.id.imgSign);
                imgSign.setImageBitmap(bitmapSign);
            }
            ViewGroup.LayoutParams param = ll.getLayoutParams();

            ll.setDrawingCacheEnabled(true);
            ll.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            // ll.layout(0, 0, ll.getMeasuredWidth(), ll.getMeasuredHeight());
            ll.layout(0, 0, bPrint.getWidth(), ll.getMeasuredHeight());
            ll.buildDrawingCache(true);
            Bitmap b = Printer.CreateBitmap(ll);
            ll.setDrawingCacheEnabled(false);
            bPrint = ServiceTools.mergeBitmap(bPrint, b, bPrint.getWidth());
            if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII)
                Printer.CreateFile(bPrint, fName, fPath);
        }

        if (SharedPreferencesHelper.getSignUnderFactor(mContext) && bitmapSign != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (printerBrand == ProjectInfo.PRINTER_BABY_380_A || printerBrand == ProjectInfo.PRINTER_DELTA_380_A) {
                ll = inflater.inflate(R.layout.placeholder_sign80mm_text, null, false);

            } else if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310) {
                ll = inflater.inflate(R.layout.placeholder_sign80mm_text, null, false);
            } else if (printerBrand == ProjectInfo.PRINTER_BABY_280_A) {
                ll = inflater.inflate(R.layout.placeholder_sign50mm_text, null, false);
            } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII) {
                ll = inflater.inflate(R.layout.placeholder_sign88mm_text, null, false);

            } else if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW || printerBrand == ProjectInfo.UROVO_K319 || printerBrand == Woosim_WSP_R341) {
                ll = inflater.inflate(R.layout.placeholder_sign88mm_text, null, false);
            } else {
                ll = inflater.inflate(R.layout.placeholder_sign_text, null, false);
            }
            ViewGroup.LayoutParams param = ll.getLayoutParams();

            ll.setDrawingCacheEnabled(true);
            ll.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            //ll.layout(0, 0, ll.getMeasuredWidth(), ll.getMeasuredHeight());
            ll.layout(0, 0, bPrint.getWidth(), ll.getMeasuredHeight());
            ll.buildDrawingCache(true);
            Bitmap b = Printer.CreateBitmap(ll);
            ll.setDrawingCacheEnabled(false);
            bPrint = ServiceTools.mergeBitmap(bPrint, b, bPrint.getWidth());
            if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII)
                Printer.CreateFile(bPrint, fName, fPath);
        }
        imgPrint.setImageBitmap(bPrint);
        imgPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSignActivity();
            }
        });
        getShareIntent();
    }


    private void setTitleStr() {

        String titleString = "";

        if (orderType == ProjectInfo.TYPE_INVOCIE) {
            titleString = getString(R.string.str_type_sale_invoice);
            insertTitleWithType(titleString);
        } else if (orderType == ProjectInfo.TYPE_ORDER) {
            titleString = getString(R.string.str_type_past_invoice);
            insertTitleWithType(titleString);
        } else if (orderType == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
            titleString = getString(R.string.str_type_past_transfer);
            insertTitleWithType(titleString);
        } else if (orderType == ProjectInfo.TYPE_RETURN_OF_SALE) {
            titleString = getString(R.string._returnOfSale);
            insertTitleWithType(titleString);
        }


    }

    private void insertTitleWithType(String titleString) {
        if (getTitleStatus(mContext)) {
            if (!getTitleText(mContext).trim().equals("")) {
                titleString = getTitleText(mContext);
                insertTitle(titleString);
            }
        } else
            insertTitle(titleString);
    }

    private void insertTitle(String titleString) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if (printerBrand == ProjectInfo.PRINTER_BABY_380_A || printerBrand == ProjectInfo.PRINTER_DELTA_380_A) {
            view = inflater.inflate(R.layout.placeholder_print_title80mm, null, false);
            TextView title = (TextView) view.findViewById(R.id._tvType);
            title.setText(titleString);
        } else if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310) {
            view = inflater.inflate(R.layout.placeholder_print_title80mm, null, false);
            TextView title = (TextView) view.findViewById(R.id._tvType);
            title.setText(titleString);
        } else if (printerBrand == ProjectInfo.PRINTER_BABY_280_A) {
            view = inflater.inflate(R.layout.placeholder_print_title50mm, null, false);
            TextView title = (TextView) view.findViewById(R.id._tvType);
            title.setText(titleString);
        } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII) {
            view = inflater.inflate(R.layout.placeholder_print_title88mm, null, false);
            TextView title = (TextView) view.findViewById(R.id._tvType);
            title.setText(titleString);
            LinearLayout _llPrint = (LinearLayout) view.findViewById(R.id._llPrint);
            //ChangePrintWidth(_llPrint);
        } else if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW || printerBrand == ProjectInfo.UROVO_K319 || printerBrand == Woosim_WSP_R341) {
            view = inflater.inflate(R.layout.placeholder_print_title88mm, null, false);
            TextView title = (TextView) view.findViewById(R.id._tvType);
            title.setText(titleString);
            LinearLayout _llPrint = (LinearLayout) view.findViewById(R.id._llPrint);
            // ChangePrintWidth(_llPrint);
        } else {
            view = inflater.inflate(R.layout.placeholder_print_title, null, false);
            TextView title = (TextView) view.findViewById(R.id._tvType);
            title.setText(titleString);
        }
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // view.setLayoutParams(layoutParams);

        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, bPrint.getWidth(), view.getMeasuredHeight());
        view.buildDrawingCache(true);
        Bitmap b = Printer.CreateBitmap(view);
        view.setDrawingCacheEnabled(false);
        bPrint = ServiceTools.mergeBitmap(b, bPrint, bPrint.getWidth());
        if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII)
            Printer.CreateFile(bPrint, fName, fPath);
    }

    Bitmap ShrinkBitmap(String sdPath, int width, int height) {


        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(sdPath, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(sdPath, bmpFactoryOptions);

        return bitmap;

    }

    public void ChangePrintWidth(LinearLayout ll) {

        ViewGroup.LayoutParams param = ll.getLayoutParams();
        final float scale = getResources().getDisplayMetrics().density;
        //convert mm to dp
        double Size = SharedPreferencesHelper.getCurrentWidthSize(mContext) * 6.3;
        param.width = (int) (Size * scale + 0.5f);
        ll.setLayoutParams(param);

    }

    public void refreshStatus() {
        if (mIsConnected) {
            tvStatus.setText(getString(R.string.str_printer_mode_is_connect));
            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            btnPrint.setEnabled(true);
            btnConnect.setText(getString(R.string.disconnet));
        } else {
            tvStatus.setText(getString(R.string.str_printer_mode_is_not_connect));
            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            btnPrint.setEnabled(false);
            btnConnect.setText(getString(R.string.str_connect_to_printer));
        }
    }

    public void initPrinter() {
        if (printerBrand == ProjectInfo.SMART_POS_UROVO_i9000s) {
            new CustomThread().start();
            mIsConnected = true;
            btnConnect.setEnabled(true);
            refreshStatus();
        } else if (printerBrand == ProjectInfo.Centerm_K9) {
            bindK9Service();
        } else if (printerBrand == ProjectInfo.Woosim_WSP_R341) {
            if (mPrintService == null) {
                // Initialize the BluetoothPrintService to perform bluetooth connections
                mPrintService = new BluetoothPrintService(Woosim_mHandler);
                if (!getCurrentPrinter().equals("")) {
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(getCurrentPrinter());
                    // Attempt to connect to the device
                    if (mPrintService != null) {
                        mPrintService.connect(device, true);
                    }
                } else {
                    Intent intent = new Intent(PrintActivity.this, DeviceList.class);
                    startActivityForResult(intent, REQUEST_CONNECT_DEVICE_SECURE);
                }
            } else {
                if (!getCurrentPrinter().equals("")) {
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(getCurrentPrinter());
                    // Attempt to connect to the device
                    if (mPrintService != null) {
                        mPrintService.connect(device, true);
                    }
                }
            }
        } else if (printerBrand == ProjectInfo.UROVO_K319) {
            publicclass = new PublicClass();
            publicclass.SetPublicClass(this);
            runnablePrint = new Runnable() {
                public void run() {
                    mess = "";
                    try {
                        mess = publicclass.PrintBitmapCPCL(bPrint, bPrint.getWidth(), bPrint.getHeight());
                    } catch (IOException e) {
                        FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                        FirebaseCrashlytics.getInstance().recordException(e);
                        e.printStackTrace();
                        mess = e.getMessage();
                    }
                }
            };
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (PublicClass.PrinterId != null && PublicClass.PrinterId.length() > 0) {
                            if (publicclass.getZpSDK().check_connect()) {
                                mIsConnected = true;
                                refreshStatus();
                            } else
                                Toast.makeText(mContext, R.string.printer_off_disconnected, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
        } else if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223) {
            //Take instance of SDKManager in the Application class or the first activity
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (SDKManager.getPrinterStatus() == SDKManager.STATUS_OK) {
                        mIsConnected = true;
                        refreshStatus();
                    }else
                        Toast.makeText(mContext, "" + SDKManager.getPrinterStatus(), Toast.LENGTH_SHORT).show();
                }
            }, 1000);

        } else if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R200_II || printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310) {
            mBixolonPrinter = new BixolonPrinter(this, mHandler, null);
            if (!getCurrentPrinter().equals("")) {
                mBixolonPrinter.connect(getCurrentPrinter());
                if (mIsConnected) {
                    //invalidateOptionsMenu();
                }
            }
        } else if (printerBrand == ProjectInfo.PRINTER_HPRT) {
            if (!getCurrentPrinter().equals("")) {
                DeviceListActivity dla = new DeviceListActivity();
                dla.toothAddress = getCurrentPrinter();
                if (dla.ConnectToDevice()) {
                    PRT = new PRTAndroidPrint(mContext, "Bluetooth");
                    PRT.InitPort();
                    PRT.OpenPort(getCurrentPrinter());
                    mIsConnected = true;
                    //invalidateOptionsMenu();
                }
            }
        } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_A || printerBrand == ProjectInfo.PRINTER_DELTA_380_A || printerBrand == ProjectInfo.PRINTER_BABY_280_A) {

            //Baby380A***************************

        } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII) {


            if (!getCurrentPrinter().equals("")) {

                btnConnect.setEnabled(false);
                if (mService != null) {
                    con_dev = mService.getDevByMac(getCurrentPrinter());
                    mService.connect(con_dev);
                }
            }

        } else if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW) {


            if (!getCurrentPrinter().equals("")) {

                myOpertion = new WifiOperation(PrintActivity.this, oscarHandler);
                myOpertion.chooseDevice();
            }

        }
    }

    public void bindK9Service() {
        Intent intent = new Intent();
        intent.setPackage("com.centerm.smartposservice");
        intent.setAction("com.centerm.smartpos.service.MANAGER_SERVICE");
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void makePrint(final Bitmap bm) {
        //Bitmap b1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight()/2);
        //Bitmap b2 = Bitmap.createBitmap(bm, 0, bm.getHeight()/2, bm.getWidth(), bm.getHeight()/2);

        int div = 500; //div image to 1000px part size
        ArrayList<Bitmap> bits = new ArrayList<>();
        int count = bm.getHeight() / div;
        int r = bm.getHeight() % div;
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                bits.add(Bitmap.createBitmap(bm, 0, i * div, bm.getWidth(), div));
            }
            if (r > 0) {
                bits.add(Bitmap.createBitmap(bm, 0, count * div, bm.getWidth(), r));
            }
        } else {
            bits.add(Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight()));
        }

        if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R200_II) {
            //mBixolonPrinter.printBitmap(b1, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.BITMAP_WIDTH_FULL, 88, true);
            //mBixolonPrinter.printBitmap(b2, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.BITMAP_WIDTH_FULL, 88, true);
            for (int i = 0; i < bits.size(); i++) {
                mBixolonPrinter.printBitmap(bits.get(i), BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.BITMAP_WIDTH_FULL, 70, false);
            }
            mBixolonPrinter.lineFeed(3, true);
        } else if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310) {
            for (int i = 0; i < bits.size(); i++) {
                mBixolonPrinter.printBitmap(bits.get(i), BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.BITMAP_WIDTH_FULL, 70, false);
            }
            mBixolonPrinter.lineFeed(3, true);
        } else if (printerBrand == ProjectInfo.PRINTER_HPRT) {
            PRT.PRTAlignType(1);
            //PRT.PRTPrintBitmap(b1);
            //PRT.PRTPrintBitmap(b2);
            for (int i = 0; i < bits.size(); i++) {
                PRT.PRTPrintBitmap(bits.get(i));
            }
            PRT.PRTFeedLines(50);
            PRT.PRTReset();
        } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_A || printerBrand == ProjectInfo.PRINTER_DELTA_380_A || printerBrand == ProjectInfo.PRINTER_BABY_280_A) {
            EscCommand esc = new EscCommand();
            esc.addInitializePrinter();
            esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
            esc.addRastBitImage(bm, 500, 0);
            esc.addPrintAndLineFeed();
            esc.addPrintAndLineFeed();
            esc.addPrintAndLineFeed();
            Vector<Byte> datas = esc.getCommand();
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(datas);
        } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII) {

            byte[] sendData = null;
            PrintPic pg = new PrintPic();
            pg.initCanvas(510);
            pg.initPaint();
            pg.drawImage(6, 0, "/storage/emulated/0/" + fPath + "/" + fName);
            sendData = pg.printDraw();
            mService.write(sendData);

        } else if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW) {

            /*preparePrint preparePrint = new preparePrint(bm);
            preparePrint.execute();*/

            new Thread(new Runnable() {
                public void run() {
                    PrintUtils.printImage(bm, mPrinter, false);
                }
            }).start();
        } else if (printerBrand == ProjectInfo.PRINTER_Rongta_RPP200) {
            BLUETOOTH_PRINTER.Begin();
            BLUETOOTH_PRINTER.LF();
            BLUETOOTH_PRINTER.LF();
            BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//CENTER
            BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);    //30 * 0.125mm
            BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal
            BLUETOOTH_PRINTER.printImage(bm);
            BLUETOOTH_PRINTER.LF();
        }
    }

    private class preparePrint extends AsyncTask<String, Integer, Boolean> {

        Bitmap bitmap = null;

        public preparePrint(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected void onPreExecute() {

            //btnPrint.setEnabled(false);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean status = false;

            try {
                status = PrintUtils.printImage(bitmap, mPrinter, false);

            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                status = false;
                Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
            }

            return status;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {

                btnPrint.setEnabled(true);
                Toast.makeText(mContext, "ok", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();


            }
        }
    }

    private void initializeBluetoothDevice() {
        Log.d(TAG, "setupChat()");
        // Initialize HsBluetoothPrintDriver class to perform bluetooth connections
        BLUETOOTH_PRINTER = HsBluetoothPrintDriver.getInstance();//
        BLUETOOTH_PRINTER.setHandler(new BluetoothHandler(PrintActivity.this));
    }

    /**
     * The Handler that gets information back from Bluetooth Devices
     */
    class BluetoothHandler extends Handler {
        private final WeakReference<PrintActivity> myWeakReference;

        //Creating weak reference of BluetoothPrinterActivity class to avoid any leak
        BluetoothHandler(PrintActivity weakReference) {
            myWeakReference = new WeakReference<PrintActivity>(weakReference);
        }

        @Override
        public void handleMessage(Message msg) {
            PrintActivity bluetoothPrinterActivity = myWeakReference.get();
            if (bluetoothPrinterActivity != null) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                switch (data.getInt("flag")) {
                    case Contants.FLAG_STATE_CHANGE:
                        int state = data.getInt("state");
                        Log.i(TAG, "MESSAGE_STATE_CHANGE: " + state);
                        switch (state) {
                            case HsBluetoothPrintDriver.CONNECTED_BY_BLUETOOTH:

                                Toast.makeText(bluetoothPrinterActivity, R.string.is_connected, Toast.LENGTH_SHORT).show();
                                mIsConnected = true;
                                refreshStatus();
                                RefreshPreference();
                                break;
                            case HsBluetoothPrintDriver.FLAG_SUCCESS_CONNECT:
                                Toast.makeText(bluetoothPrinterActivity, R.string.coneccting, Toast.LENGTH_SHORT).show();
                                //txtPrinterStatus.setText(R.string.title_connecting);
                                break;

                            case HsBluetoothPrintDriver.UNCONNECTED:
                                Toast.makeText(bluetoothPrinterActivity, R.string.no_printer, Toast.LENGTH_SHORT).show();
                                mIsConnected = false;
                                refreshStatus();
                                break;
                        }
                        break;
                    case Contants.FLAG_SUCCESS_CONNECT:
                        Toast.makeText(bluetoothPrinterActivity, R.string.coneccting, Toast.LENGTH_SHORT).show();
                        mIsConnected = true;
                        refreshStatus();
                        RefreshPreference();
                        // txtPrinterStatus.setText(R.string.title_connecting);
                        break;
                    case Contants.FLAG_FAIL_CONNECT:
                        Toast.makeText(bluetoothPrinterActivity, R.string.no_connect, Toast.LENGTH_SHORT).show();
                        mIsConnected = false;
                        refreshStatus();
                        break;
                    default:
                        break;

                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CONN_MOST_DEVICES:
                id = data.getIntExtra("id", -1);
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] != null &&
                        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState()) {
                    mIsConnected = true;
                    refreshStatus();
                    BaseActivity.setCurrentPrinter(String.valueOf(id));
                    RefreshPreference();
                } else {
                    //tvConnState.setText( getString( R.string.str_conn_state_disconnect ) );
                }
                break;

            //woosim
            case REQUEST_CONNECT_DEVICE_SECURE:
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;

            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth open successful", Toast.LENGTH_LONG).show();
                } else {
                    finish();
                }
                break;
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == 380) {
                    String address = data.getExtras().getString(com.mahak.order.baby380_koohii_printer.DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    if (mService != null) {
                        con_dev = mService.getDevByMac(address);
                        mService.connect(con_dev);
                        BaseActivity.setCurrentPrinter(address);
                        RefreshPreference();
                    }
                } else {
                    mIsConnected = false;
                    refreshStatus();
                    BaseActivity.setCurrentPrinter("");
                    RefreshPreference();
                    Toast.makeText(mContext, R.string.printer_off_disconnected, Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_CONNECT_RONGTA:
                // When RongtaDeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras()
                            .getString(RongtaDeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    BLUETOOTH_PRINTER.start();
                    BLUETOOTH_PRINTER.connect(device);
                }
                break;

            case CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    progressDialog.show();
                    new Thread(new Runnable() {
                        public void run() {
                            myOpertion.open(data);
                        }
                    }).start();

                    mIsConnected = true;
                    refreshStatus();
                    BaseActivity.setCurrentPrinter(data.getStringExtra("ip_address"));
                    RefreshPreference();
                }
                break;

            case PRINT_SETTING_REQUEST:
                initData();
                break;

            case PRINT_SIGN_REQUEST:
                if (data != null) {
                    bitmapSign = ServiceTools.getSign(data.getStringExtra("FileName"));
                    initData();
                    getShareIntent();
                }
                break;
            case PRINTER_UROVO:
                if (resultCode == RESULT_OK) {
                    if (publicclass.getZpSDK().connect(PublicClass.PrinterId)) {
                        mIsConnected = true;
                        refreshStatus();
                        RefreshPreference();
                    }
                }
                break;

        }
        try {
            switch (resultCode) {
                case 10:
                    String strIsConnected = data.getExtras().getString("is_connected");
                    if (strIsConnected.equals("NO")) {
                        //txtTips.setText(thisCon.getString(R.string.scan_error));
                        //Toast.makeText(thisCon, R.string.connecterr, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Problem connecting!", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        String strBTAddress = data.getExtras().getString("BTAddress");
                        if (strBTAddress == null) {
                            return;
                        } else if (!strBTAddress.contains(":")) {
                            return;
                        } else if (strBTAddress.length() != 17) {
                            return;
                        }

                        BaseActivity.setCurrentPrinter(strBTAddress);
                        RefreshPreference();
                        mIsConnected = true;
                        //invalidateOptionsMenu();
                        refreshStatus();

                        PRT = new PRTAndroidPrint(mContext, "Bluetooth");
                        //PRT=new PRTAndroidPrint(thisCon,"Bluetooth");
                        PRT.InitPort();

                        if (!PRT.OpenPort(strBTAddress)) {
                            //Toast.makeText(thisCon, R.string.connecterr, Toast.LENGTH_SHORT).show();
                            //txtTips.setText(thisCon.getString(R.string.scan_error));
                            return;
                        } else {
                            //Toast.makeText(thisCon, R.string.connected, Toast.LENGTH_SHORT).show();
                            //txtTips.setText(thisCon.getString(R.string.scan_success));
                            return;
                        }
                    }

            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }

        /*if (requestCode == PRINT_SETTING_REQUEST) {
            initData();
        }*/
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem signItem = menu.findItem(R.id.menu_sign);
        MenuItem printa4 = menu.findItem(R.id.printa4);
        MenuItem deleteSign = menu.findItem(R.id.delete_sign);
        if (!SharedPreferencesHelper.getSignUnderFactor(mContext)) {
            signItem.setVisible(false);
            deleteSign.setVisible(false);
            imgPrint.setOnClickListener(null);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            printa4.setEnabled(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /*menu.add(1, 1, 1, R.string.str_setting)
                .setIcon(new IconicsDrawable(mContext, GoogleMaterial.Icon.gmd_settings).color(Color.WHITE).sizeDp(23))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/

        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.share_menu, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        mShareActionProvider.setShareIntent(getShareIntent());

        // Return true to display menu
        return super.onCreateOptionsMenu(menu);

    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        if (getImagePath(mContext, bPrint) != null)
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(getImagePath(mContext, bPrint)));
        return shareIntent;
    }

    private Intent getShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        File sharedFile = new File(sdPath);
        //    Uri uri = Uri.fromFile(sharedFile);

        Uri photoURI = FileProvider.getUriForFile(mContext,
                BuildConfig.APPLICATION_ID + ".provider",
                sharedFile);

        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R200_II || printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310)
                    mBixolonPrinter.disconnect();
                else if (printerBrand == ProjectInfo.PRINTER_HPRT)
                    if (PRT != null)
                        PRT.ClosePort();
                    else if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW) {
                        if (myOpertion != null) {
                            myOpertion.close();
                            myOpertion = null;
                            mPrinter = null;
                            mIsConnected = false;
                        }
                    } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII) {
                        if (mService != null)
                            mService.stop();
                        mService = null;
                    }

                finish();
                break;

            case R.id.menu_setting:
                Intent intent = new Intent(PrintActivity.this, PrintSettingActivity.class);
                intent.putExtra(ProjectInfo._TAG_PAGE_NAME, pageName);
                startActivityForResult(intent, PRINT_SETTING_REQUEST);
                break;
            case R.id.menu_sign:

                gotoSignActivity();

                break;

            case R.id.printa4:

                if (SharedPreferencesHelper.getCurrentLanguage(mContext).equals("de_DE")) {
                    PDF_DE pdf_de = new PDF_DE(mContext,OrderCode);
                    pdf_de.exportOrderDetailToPdf();
                }else {
                    PDF pdf = new PDF(mContext, OrderCode);
                    pdf.exportOrderDetailToPdf();
                }

                Dialog(getString(R.string.pdf_created_string)).show();
                FontAlertDialog.FontDialog(dialog);

                break;

            case R.id.delete_sign:

                bitmapSign = null;
                deleteSign(FileName);
                initData();

                break;
           /* case R.id.printa4:

                doPhotoPrint(bPrint,this);

                break;*/
        }

        return super.onOptionsItemSelected(item);
    }

    private void doPhotoPrint(Bitmap bitmap, Context context) {
        PrintHelper photoPrinter = new PrintHelper(context);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap(OrderCode, bitmap);
    }

    protected void deleteSign(String signFileName) {

        String signPath = ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_SIGNS + "/" + DIRECTORY_ORDER_SIGNS;

        File DELETE_FILE = new File(Environment.getExternalStoragePublicDirectory(
                signPath), signFileName);
        if (!DELETE_FILE.exists()) {
            return;
        }
        try {
            if (DELETE_FILE.delete())
                Toast.makeText(PrintActivity.this, R.string.signature_deleted_successfully, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            // TODO: handle exception
        }
    }

    private void gotoSignActivity() {
        Intent intent1 = new Intent(PrintActivity.this, SignActivity.class);
        intent1.putExtra("OrderCode", OrderCode);
        startActivityForResult(intent1, PRINT_SIGN_REQUEST);
    }

    private void showSnack(final String filepath) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, R.string.pdf_created, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.observe, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openPdf(filepath);
                    }
                });

        snackbar.show();
    }

    private void openPdf(String filepath) {
        File file = new File(filepath);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        Uri photoURI = FileProvider.getUriForFile(mContext,
                BuildConfig.APPLICATION_ID + ".provider",
                file);

        intent.setDataAndType(photoURI, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        try {
            startActivity(intent);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }

    private void extractPdf() {

        Document document = new Document();
        String pdfFileName;

        createPdfDirectory();

        String directoryPath = Environment.getExternalStorageDirectory().toString();
        String pdfPath = "/" + ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_PDF + "/";
        if (OrderCode != null) {
            pdfFileName = String.format("%s.pdf", OrderCode);
        } else
            pdfFileName = String.format("%s.pdf", pageName);

        String filepath = directoryPath + pdfPath + pdfFileName;

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filepath));
        } catch (DocumentException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }

        document.open();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bPrint.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Image image = null;  // Change image's name and extension.
        try {
            image = Image.getInstance(byteArray);
            // image.scalePercent(1);
        } catch (BadElementException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        } catch (IOException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }

        float scaler = ((document.getPageSize().getHeight() - document.leftMargin()
                - document.rightMargin() - 0) / image.getHeight()) * 100; // 0 means you have no indentation. If you have any, change it.
        image.scalePercent(scaler);
        image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);

        try {
            document.add(image);
        } catch (DocumentException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        document.close();
        showSnack(filepath);
    }

    private void createPdfDirectory() {
        try {
            String fPath = ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_PDF;
            String FilePath = Environment.getExternalStorageDirectory().toString();
            File Directory = new File(FilePath, fPath);
            if (!Directory.exists())
                Directory.mkdirs();
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }

    public String getImagePath(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, String.valueOf(OrderCode), null);
    }

    @Override
    public void onBackPressed() {
        if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R200_II || printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310)
            mBixolonPrinter.disconnect();
        else if (printerBrand == ProjectInfo.PRINTER_HPRT)
            if (PRT != null)
                PRT.ClosePort();

        if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII) {
            if (mService != null)
                mService.stop();
            mService = null;
        }
        if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW) {
            if (myOpertion != null) {

                myOpertion.close();
                myOpertion = null;
                mPrinter = null;
                mIsConnected = false;
            }


        }
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R200_II || printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310)
            mBixolonPrinter.disconnect();

        else if (printerBrand == ProjectInfo.PRINTER_HPRT)
            if (PRT != null)
                PRT.ClosePort();

                //Baby380
            else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII) {
                if (mService != null)
                    mService.stop();
                mService = null;
            }

        if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW) {
            if (myOpertion != null) {
                myOpertion.close();
                myOpertion = null;
                mPrinter = null;
                mIsConnected = false;
            }
        }

        if (printerBrand == ProjectInfo.Woosim_WSP_R341) {
            if (mPrintService != null) {
                mPrintService.stop();
                mPrintService = null;
            }
        }


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private final Handler mHandler = new Handler(new Handler.Callback() {

        @SuppressWarnings("unchecked")
        @Override
        public boolean handleMessage(Message msg) {
            Log.d(TAG, "mHandler.handleMessage(" + msg + ")");

            switch (msg.what) {
                case BixolonPrinter.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BixolonPrinter.STATE_CONNECTED:
                            //setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            //mListView.setEnabled(true);
                            mIsConnected = true;
                            //invalidateOptionsMenu();
                            refreshStatus();
                            break;

                        case BixolonPrinter.STATE_CONNECTING:
                            //setStatus(R.string.title_connecting);
                            break;

                        case BixolonPrinter.STATE_NONE:
                            //setStatus(R.string.title_not_connected);
                            //mListView.setEnabled(false);
                            mIsConnected = false;
                            //invalidateOptionsMenu();
                            refreshStatus();
                            //mProgressBar.setVisibility(View.INVISIBLE);
                            break;
                    }
                    return true;

                case BixolonPrinter.MESSAGE_WRITE:
                    switch (msg.arg1) {
                        case BixolonPrinter.PROCESS_SET_DOUBLE_BYTE_FONT:
                            mHandler.obtainMessage(MESSAGE_END_WORK).sendToTarget();

                            Toast.makeText(getApplicationContext(), "Complete to set double byte font.", Toast.LENGTH_SHORT).show();
                            break;

                        case BixolonPrinter.PROCESS_DEFINE_NV_IMAGE:
                            mBixolonPrinter.getDefinedNvImageKeyCodes();
                            Toast.makeText(getApplicationContext(), "Complete to define NV image", Toast.LENGTH_LONG).show();
                            break;

                        case BixolonPrinter.PROCESS_REMOVE_NV_IMAGE:
                            mBixolonPrinter.getDefinedNvImageKeyCodes();
                            Toast.makeText(getApplicationContext(), "Complete to remove NV image", Toast.LENGTH_LONG).show();
                            break;

                        case BixolonPrinter.PROCESS_UPDATE_FIRMWARE:
                            mBixolonPrinter.disconnect();
                            Toast.makeText(getApplicationContext(), "Complete to download firmware.\nPlease reboot the printer.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;

                case BixolonPrinter.MESSAGE_READ:
                    //this.dispatchMessage(msg);
                    return true;

                case BixolonPrinter.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(BixolonPrinter.KEY_STRING_DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), mConnectedDeviceName, Toast.LENGTH_LONG).show();
                    return true;

                case BixolonPrinter.MESSAGE_TOAST:
                    //mListView.setEnabled(false);
                    Toast.makeText(getApplicationContext(), msg.getData().getString(BixolonPrinter.KEY_STRING_TOAST), Toast.LENGTH_SHORT).show();
                    return true;

                case BixolonPrinter.MESSAGE_BLUETOOTH_DEVICE_SET:
                    if (msg.obj == null) {
                        Toast.makeText(getApplicationContext(), getString(R.string.msg_no_connected_printer), Toast.LENGTH_SHORT).show();
                    } else {
                        DialogManager.showBluetoothDialog(PrintActivity.this, (Set<BluetoothDevice>) msg.obj);
                    }
                    return true;

                case BixolonPrinter.MESSAGE_PRINT_COMPLETE:
                    Toast.makeText(getApplicationContext(), getString(R.string.msg_compeleted_print), Toast.LENGTH_SHORT).show();
                    return true;

                case BixolonPrinter.MESSAGE_ERROR_INVALID_ARGUMENT:
                    Toast.makeText(getApplicationContext(), "Invalid argument", Toast.LENGTH_SHORT).show();
                    return true;

                case BixolonPrinter.MESSAGE_ERROR_NV_MEMORY_CAPACITY:
                    Toast.makeText(getApplicationContext(), "NV memory capacity error", Toast.LENGTH_SHORT).show();
                    return true;

                case BixolonPrinter.MESSAGE_ERROR_OUT_OF_MEMORY:
                    Toast.makeText(getApplicationContext(), "Out of memory", Toast.LENGTH_SHORT).show();
                    return true;

                case BixolonPrinter.MESSAGE_COMPLETE_PROCESS_BITMAP:
                    String text = "Complete to process bitmap.";
                    Bundle data = msg.getData();
                    byte[] value = data.getByteArray(BixolonPrinter.KEY_STRING_MONO_PIXELS);
                    if (value != null) {
                        Intent intent = new Intent();
                        intent.setAction(ACTION_COMPLETE_PROCESS_BITMAP);
                        intent.putExtra(EXTRA_NAME_BITMAP_WIDTH, msg.arg1);
                        intent.putExtra(EXTRA_NAME_BITMAP_HEIGHT, msg.arg2);
                        intent.putExtra(EXTRA_NAME_BITMAP_PIXELS, value);
                        sendBroadcast(intent);
                    }

                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    return true;

                case MESSAGE_START_WORK:
                    //mListView.setEnabled(false);
                    //mProgressBar.setVisibility(View.VISIBLE);
                    return true;

                case MESSAGE_END_WORK:
                    //mListView.setEnabled(true);
                    //mProgressBar.setVisibility(View.INVISIBLE);
                    return true;

                case BixolonPrinter.MESSAGE_USB_DEVICE_SET:
                    if (msg.obj == null) {
                        Toast.makeText(getApplicationContext(), "No connected device", Toast.LENGTH_SHORT).show();
                    } else {
                        //DialogManager.showUsbDialog(getApplicationContext(), (Set<UsbDevice>) msg.obj, mUsbReceiver);
                    }
                    return true;

                case BixolonPrinter.MESSAGE_USB_SERIAL_SET:
                    return true;

                case BixolonPrinter.MESSAGE_NETWORK_DEVICE_SET:
            }
            return false;
        }
    });

    @SuppressLint("HandlerLeak")
    private Handler oscarHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PrinterConstants.Connect.SUCCESS:
                    mIsConnected = true;
                    mPrinter = myOpertion.getPrinter();
                    refreshStatus();
                    break;
                case PrinterConstants.Connect.FAILED:
                    mIsConnected = false;
                    Toast.makeText(mContext, R.string.error_in_connection, Toast.LENGTH_SHORT).show();
                    BaseActivity.setCurrentPrinter("");
                    //invalidateOptionsMenu();
                    refreshStatus();
                    break;
                case PrinterConstants.Connect.CLOSED:
                    mIsConnected = false;
                    //BaseActivity.setCurrentPrinter("");
                    refreshStatus();
                    //Toast.makeText(mContext, "اتصال قطع شد!", Toast.LENGTH_SHORT).show();

                    break;
                default:
                    break;
            }


            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

    };

    private final Handler baby380mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:

                            mIsConnected = true;
                            btnConnect.setEnabled(true);
                            refreshStatus();

                            break;
                        case BluetoothService.STATE_CONNECTING:

                            tvStatus.setText(getString(R.string.str_printer_mode_is_connecting));
                            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                            btnConnect.setEnabled(false);

                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:


                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:

                    btnConnect.setEnabled(true);
                    tvStatus.setText(getString(R.string.str_printer_mode_is_not_connect));
                    BaseActivity.setCurrentPrinter("");

                    break;
            }
        }

    };

    private void connectDevice(Intent data, boolean secure) {
        String address = null;
        // Get the device MAC address
        if (data.getExtras() != null)
            address = data.getExtras().getString(DeviceList.EXTRA_DEVICE_ADDRESS);
        BaseActivity.setCurrentPrinter(address);
        // Get the BLuetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mPrintService.connect(device, secure);
    }

    private void printBitmap(int x, int y, Bitmap bitmap) {
        mPrintService.write(WoosimCmd.initPrinter());
        mPrintService.write(WoosimCmd.setPageMode());
        sendImg(1, 1, bitmap);
    }

    private void sendImg(int x, int y, Bitmap bitmap) {
        if (bitmap == null) return;
        byte[] data = WoosimImage.printBitmap(x, y, bitmap.getWidth(), bitmap.getHeight(), bitmap);
        mPrintService.write(WoosimCmd.setAlignment(WoosimCmd.ALIGN_CENTER));
        mPrintService.write(data);
    }

    //k9_centerm
    public void printBmpFast(Bitmap bmp) {
        try {
            printDev.setPrintQueue(true);
        } catch (RemoteException e1) {
            FirebaseCrashlytics.getInstance().recordException(e1);
            e1.printStackTrace();
        }
        try {
            printDev.printBmpFast(bmp, Constant.ALIGN.CENTER, callback);
            printDev.spitPaper(60);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        } finally {
            try {
                printDev.setPrintQueue(false);
            } catch (RemoteException e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onResume() {
        if ((printerBrand == ProjectInfo.PRINTER_BABY_380_A || printerBrand == ProjectInfo.PRINTER_DELTA_380_A || printerBrand == ProjectInfo.PRINTER_BABY_280_A)) {
            int id = ServiceTools.toInt(BaseActivity.getCurrentPrinter());
            if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] != null &&
                    DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState()) {
                mIsConnected = true;
                refreshStatus();
                BaseActivity.setCurrentPrinter(String.valueOf(id));
                RefreshPreference();
            } else {
                mIsConnected = false;
                refreshStatus();
            }
        }
        super.onResume();
    }

    private void doPrint(PrinterManager printerManager, int type, Object content) {
        int ret = printerManager.getStatus();   //Get printer status
        if (ret == PRNSTS_OK) {
            printerManager.setupPage(-1, -1);   //Set paper size
            switch (type) {
                case PRINT_BITMAP:
                    Bitmap bitmap = (Bitmap) content;
                    if (bitmap != null) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, 384, bitmap.getHeight(), false);
                        printerManager.drawBitmap(bitmap, 0, 0);  //print pictures
                    } else {
                        Toast.makeText(this, "Picture is null", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            ret = printerManager.printPage(0);  //Execution printing
            printerManager.paperFeed(8);  //paper feed
        }
        updatePrintStatus(ret);
    }

    private final int PRINT_TEXT = 0;   //Printed text
    private final int PRINT_BITMAP = 1;   //print pictures
    private final int PRINT_BARCOD = 2;   //Print bar code
    private final int PRINT_FORWARD = 3;   //Forward (paper feed)

    private Handler mPrintHandler;

    class CustomThread extends Thread {
        @Override
        public void run() {
            //To create a message loop
            Looper.prepare();   //1.Initialize looper
            mPrintHandler = new Handler() {   //2.Bind handler to looper object of customthread instance
                public void handleMessage(Message msg) {   //3.Define how messages are processed
                    switch (msg.what) {
                        case PRINT_TEXT:
                        case PRINT_BITMAP:
                        case PRINT_BARCOD:
                            doPrint(getPrinterManager(), msg.what, msg.obj);   //Print
                            break;
                        case PRINT_FORWARD:
                            getPrinterManager().paperFeed(20);
                            updatePrintStatus(100);
                            break;
                    }
                }
            };
            Looper.loop();   //4.Start message loop
        }
    }

    //Update printer status, toast reminder in case of exception
    private void updatePrintStatus(final int status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (status == PRNSTS_OUT_OF_PAPER) {
                    Toast.makeText(
                            PrintActivity.this,
                            R.string.tst_info_paper,
                            Toast.LENGTH_SHORT).show();
                } else if (status == PRNSTS_OVER_HEAT) {
                    Toast.makeText(
                            PrintActivity.this,
                            R.string.tst_info_temperature,
                            Toast.LENGTH_SHORT).show();
                } else if (status == PRNSTS_UNDER_VOLTAGE) {
                    Toast.makeText(
                            PrintActivity.this,
                            R.string.tst_info_voltage,
                            Toast.LENGTH_SHORT).show();
                } else if (status == PRNSTS_BUSY) {
                    Toast.makeText(
                            PrintActivity.this,
                            R.string.tst_info_busy,
                            Toast.LENGTH_SHORT).show();
                } else if (status == PRNSTS_ERR) {
                    Toast.makeText(
                            PrintActivity.this,
                            R.string.tst_info_error,
                            Toast.LENGTH_SHORT).show();
                } else if (status == PRNSTS_ERR_DRIVER) {
                    Toast.makeText(
                            PrintActivity.this,
                            R.string.tst_info_driver_error,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private PrinterManager getPrinterManager() {
        if (mPrinterManager == null) {
            mPrinterManager = new PrinterManager();
            try {
                mPrinterManager.open();
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return mPrinterManager;
    }


    private Dialog Dialog(String msg) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(FontAlertDialog.getFontTitle(getString(R.string.str_title_message)))
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(R.string.open_file, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String fileName = OrderCode + ".pdf";
                                    ServiceTools.openFile(mContext, new File(DIRECTORY_PDF, fileName));
                                } catch (Exception e) {
                                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                                    FirebaseCrashlytics.getInstance().recordException(e);
                                    Log.d("TAG", "run: ERror");
                                }
                            }
                        }, 1000);

                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.close_dialog, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog.dismiss();
                    }
                });

        dialog = builder.create();
        return dialog;
    }

}
