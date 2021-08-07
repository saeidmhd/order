package com.mahak.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kishcore.sdk.hybrid.api.HostApp;


import com.mahak.order.common.Bank;
import com.mahak.order.common.Cheque;
import com.mahak.order.common.Customer;
import com.mahak.order.common.MyCalendar;
import com.mahak.order.common.Order;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.Receipt;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.SharedPreferencesHelper;
import com.mahak.order.common.Visitor;
import com.mahak.order.libs.DatePicker;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.storage.DbSchema;
import com.mahak.order.szzt_pos.ResultActivity;

import com.kishcore.sdk.hybrid.api.GeneralPaymentCallback;
import com.kishcore.sdk.hybrid.api.SDKManager;
import com.kishcore.sdk.sepehr.rahyab.api.PurchaseType;
import com.mahak.order.szzt_pos.SelectWayDialog;
import com.mahak.order.szzt_pos.Tools;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.mahak.order.common.ServiceTools.getDateAndTimeForLong;

//import com.mahak.order.common.ProductInOrder;

@SuppressLint("ValidFragment")
public class ManageReceiptActivity extends BaseActivity {

    private static int REQUEST_CUSTOMER_LIST = 1;
    private static int REQUEST_MANAGE_CHEQUE = 2;
    private static int REQUEST_ORDER_LIST = 3;
    private static int REQUEST_i9000s = 103;
    private static String STR_DATE_KEY = "StrDate";
    private static String CASHAMOUNT_KEY = "CashAmount";
    private static String DESCRIPTION_KEY = "Description";
    private static String MARKET_KEY = "Market";
    private static String LNG_DATE_KEY = "LngDate";
    private static String CODE_KEY = "Code";
    private static String PAYMENT_KEY = "Payment";
    private static int Page;
    private static int Mode;
    private static long lngDate;
    private static long CustomerClientId;
    private static int CustomerId;
    private static String Code = ProjectInfo.DONT_CODE;
    private static double Payment;
    public static ArrayList<Cheque> arrayCheque = new ArrayList<Cheque>();

    private EditText txtCustomerName, txtAmount, txtDescription, txtMarketName, txtTrackingCode, txtPayment;
    private TextView tvDate;
    private Button btnAddCheque, btnSave, btnDatePicker, btnSelectCustomer, btnSelectInvoice, btnPayPos;
    private ListView lstCheque;
    private LinearLayout posLL;
    private Bundle Extras;
    private Date dt;
    private String CustomerName, StrDate, CashAmount, Description, MarketName;
    private Context mContext;
    private Activity mActivity;
    private AdapterCheque adCheque;
    private DbAdapter db;
    private long ReceiptId;
    private Receipt receipt;
    private Customer customer;
    private double currentVisitorCredit;
    private double mVisitorCredit;
    private double totalReceiptForInvoiceNotPublished;
    private double TotalPriceInvoice;
    private double TotalReceipt;
    private double mSpentCredit;
    private double remainCredit;
    private double payment;
    private double savedCashedAndCheque;
    private static double mCurrentPrice;
    private static final long NoLimit = -1;
    private static double visitorCreditValue;
    private ArrayList<Bank> arrayBank;

    private HostApp hostApp;
    private androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
    int printerBrand;
    Order order;
    private long OrderId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_receipt);

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
        TextView tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        tvPageTitle.setText(getString(R.string.str_save_receipt));
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________

        mContext = this;
        mActivity = this;

        //hostApp = SDKManager.init(this);

        if(printerBrand == ProjectInfo.PRINTER_SZZT_KS8223){
            hostApp = SDKManager.init(this);
            switch (hostApp) {
                case IKC:
                    Toast.makeText(mContext, "IKC", Toast.LENGTH_SHORT).show();
                    break;
                case SEP:
                    Toast.makeText(mContext, "SEP", Toast.LENGTH_SHORT).show();
                    break;
                case SEP_IKCC:
                    Toast.makeText(mContext, "SEP_IKCC", Toast.LENGTH_SHORT).show();
                    break;
                case FANAVA:
                    Toast.makeText(mContext, "FANAVA", Toast.LENGTH_SHORT).show();
                    break;
                case SAYAN_CARD:
                    Toast.makeText(mContext, "SAYAN_CARD", Toast.LENGTH_SHORT).show();
                    break;
                case PEC:
                    Toast.makeText(mContext, "PEC", Toast.LENGTH_SHORT).show();
                    break;
                case NAVACO:
                    Toast.makeText(mContext, "NAVACO", Toast.LENGTH_SHORT).show();
                    break;
                case SEPEHR:
                    Toast.makeText(mContext, "SEPEHR", Toast.LENGTH_SHORT).show();
                    break;
                case PEC_MEHRANA:
                    Toast.makeText(mContext, "PEC_MEHRANA", Toast.LENGTH_SHORT).show();
                    break;
                case UNKNOWN:
                    Toast.makeText(mContext, "UNKNOWN", Toast.LENGTH_SHORT).show();
                    break;
            }
        }


        alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);

        initialise();
        Extras = getIntent().getExtras();

        arrayBank = new ArrayList<>();
        Visitor visitor = BaseActivity.getVisitor();
        if (visitor != null) {
            arrayBank = db.getAllBank(visitor.getBankCode());
        } else {
            arrayBank = db.getAllBank();
        }

        Visitor mVisitor = db.getVisitor();

        //مجموع تمام فاکتور ها چه ارسال شده یا نشده
        TotalPriceInvoice = db.getPurePriceInvoice();
        //مجموع تمام دریافتی ها چه ارسال شده یا نشده
        TotalReceipt = db.getTotalPriceReceipt();
        //اعتبار مصرف شده
        mSpentCredit = TotalPriceInvoice - TotalReceipt;
        //اعتبار ویزیتور
        mVisitorCredit = mVisitor.getTotalCredit();

        totalReceiptForInvoiceNotPublished = db.getTotalReceiptForInvoiceNotPublished();

        if (savedInstanceState == null) {
            if (Extras != null) {
                Page = Extras.getInt(PAGE);
                Mode = Extras.getInt(MODE_PAGE);
                CustomerId = Extras.getInt(CUSTOMERID_KEY);
                CustomerClientId = Extras.getLong(CUSTOMER_CLIENT_ID_KEY);
                OrderId = Extras.getLong(ID);
                Code = Extras.getString(CODE_KEY) != null ? Extras.getString(CODE_KEY) : ProjectInfo.DONT_CODE;
                Payment = Extras.getDouble(PAYMENT_KEY);

                ReceiptId = Extras.getLong(ID);

                visitorHasCredit(Payment);
                currentVisitorCredit = remainVisitorCredit(Payment);

                if (Page == PAGE_CHECKLIST) {
                    dt = new Date();
                    lngDate = dt.getTime();
                    StrDate = getDateAndTimeForLong(lngDate);
                    tvDate.setText(StrDate);
                } else if (Page == PAGE_DASHBORD) {
                    dt = new Date();
                    lngDate = dt.getTime();
                    StrDate = getDateAndTimeForLong(lngDate);
                    tvDate.setText(StrDate);
                } else if (Page == PAGE_ORDER_DETAIL) {
                    dt = new Date();
                    lngDate = dt.getTime();
                    StrDate = getDateAndTimeForLong(lngDate);
                    tvDate.setText(StrDate);
                    txtTrackingCode.setText(Code);
                    txtPayment.setText(ServiceTools.formatPrice(Payment));
                } else if (Page == PAGE_RECEIPTLIST) {
                    if (Mode == MODE_EDIT) {
                        savedCashedAndCheque = db.getTotalReceiptWithId(ReceiptId);
                        // اعتبار مصرف شده بدون احتساب دریافتی فعلی
                        mSpentCredit = mSpentCredit + savedCashedAndCheque;
                        FillView();
                    } else if (Mode == MODE_NEW) {
                        dt = new Date();
                        lngDate = dt.getTime();
                        StrDate = getDateAndTimeForLong(lngDate);
                        tvDate.setText(StrDate);
                    }
                } else if (Page == PAGE_Invoice_Detail_Activity) {
                    if (Mode == MODE_NEW) {
                        dt = new Date();
                        lngDate = dt.getTime();
                        StrDate = getDateAndTimeForLong(lngDate);
                        tvDate.setText(StrDate);
                        txtTrackingCode.setText(Code);
                        txtPayment.setText(ServiceTools.formatPrice(Payment));
                    }
                }

                if (CustomerId == ProjectInfo.CUSTOMERID_GUEST) {
                    Customer customer = db.getCustomerWithPersonClientId(CustomerClientId);
                    txtCustomerName.setText(customer.getName());
                    txtMarketName.setText(customer.getOrganization());
                } else {
                    Customer customer = db.getCustomerWithPersonId(CustomerId);
                    txtCustomerName.setText(customer.getName());
                    txtMarketName.setText(customer.getOrganization());
                }
            }
        } else {
            CustomerName = savedInstanceState.getString(CUSTOMER_NAME_KEY);
            MarketName = savedInstanceState.getString(MARKET_KEY);
            StrDate = savedInstanceState.getString(STR_DATE_KEY);
            CashAmount = savedInstanceState.getString(CASHAMOUNT_KEY);
            Description = savedInstanceState.getString(DESCRIPTION_KEY);
            Code = savedInstanceState.getString(CODE_KEY);
            Payment = savedInstanceState.getDouble(PAYMENT_KEY);

            txtCustomerName.setText(CustomerName);
            txtMarketName.setText(MarketName);
            tvDate.setText(StrDate);
            txtAmount.setText(CashAmount);
            txtDescription.setText(Description);
            txtTrackingCode.setText(Code);
            txtPayment.setText(ServiceTools.formatPrice(Payment));
        }

        lstCheque.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ManageChequeActivity.class);
                intent.putExtra(POSITION_KEY, position);
                intent.putExtra(MODE_PAGE, MODE_EDIT);
                startActivityForResult(intent, REQUEST_MANAGE_CHEQUE);

            }
        });

        txtAmount.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                CashAmount = ServiceTools.MoneyFormatToNumber(s.toString());
            }
        });

        txtDescription.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                Description = s.toString();

            }
        });

        txtCustomerName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                CustomerName = s.toString();

            }
        });

        txtMarketName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                MarketName = s.toString();

            }
        });

        txtTrackingCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable text) {
                if (!text.toString().equals(ProjectInfo.DONT_CODE))
                    Code = text.toString();

            }
        });

        txtPayment.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                Payment = ServiceTools.toDouble(s.toString());

            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                MyCalendar myCalendar = new MyCalendar(lngDate, ManageReceiptActivity.this, getFragmentManager(), new MyCalendar.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker date) {
                        lngDate = date.getDate().getTime();
                        tvDate.setText(ServiceTools.getDateAndTimeForLong(lngDate));
                    }

                    @Override
                    public void onGregorianSet(int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        lngDate = calendar.getTimeInMillis();
                        btnDatePicker.setText(ServiceTools.getDateAndTimeForLong(lngDate));

                    }
                });
                myCalendar.showDialog();
            }
        });

        btnSelectCustomer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PeopleListActivity.class);
                intent.putExtra(PAGE, PAGE_MANAGE_RECEIPT);
                startActivityForResult(intent, REQUEST_CUSTOMER_LIST);

            }
        });

        btnAddCheque.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ManageChequeActivity.class);
                intent.putExtra(MODE_PAGE, MODE_NEW);
                startActivityForResult(intent, REQUEST_MANAGE_CHEQUE);

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                /*//Validate____________________________________________
                boolean ValidateCashAmount = false;
                //Validate____________________________________________
                Validate validateAmount = new Validate(txtAmount);
                NotEmptyValidator ValidatorEmpty = new NotEmptyValidator(mContext ,R.string.str_valid_empty);
                validateAmount.addValidator(ValidatorEmpty);
                Form form = new Form();
                form.addValidates(validateAmount);
                if(arrayCheque.size() == 0)
                    ValidateCashAmount = form.validate();

                if(!TextUtils.isEmpty(txtPayment.getText().toString()))
                    payment = ServiceTools.toLong(ServiceTools.MoneyFormatToNumber(txtPayment.getText().toString()));
                else
                    payment = 0L;*/
                /*//باقیمانده اعتبار ویزیتور
                remainCredit = mVisitorCredit - (payment + mSpentCredit);*/
                saveReciept();
            }
        });

        btnSelectInvoice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                intent.putExtra(PAGE, PAGE_MANAGE_RECEIPT);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_INVOCIE);
                startActivityForResult(intent, REQUEST_ORDER_LIST);
            }
        });

        btnPayPos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223)
                    managePaySzzt();
                else if (printerBrand == ProjectInfo.SMART_POS_UROVO_i9000s)
                    managePayUrovoI9000();
            }
        });


    }// End of OnCreate


    private void saveReciept() {
        if (mVisitorCredit == -1) {
            Save();
        } else {
            if (Mode == MODE_EDIT) {
                //باقیمانده اعتبار ویزیتور
                remainCredit = mVisitorCredit - mSpentCredit;
                if (remainCredit + totalCashAndCheque() < 0) {
                    Toast.makeText(mContext, "باید مجموع دریافتی نقد و چک از باقیمانده اعتبار ویزیتور بیشتر باشد!", Toast.LENGTH_SHORT).show();
                } else
                    Save();
            } else {
                if (Page == PAGE_Invoice_Detail_Activity) {
                    //باقیمانده اعتبار ویزیتور با احتساب فاکتور فعلی
                    remainCredit = mVisitorCredit - mSpentCredit - Payment;
                    if (remainCredit + totalCashAndCheque() < 0)
                        Toast.makeText(mContext, "باید مجموع دریافتی نقد و چک از باقیمانده اعتبار ویزیتور بیشتر باشد!", Toast.LENGTH_SHORT).show();
                    else
                        Save();

                } else
                    Save();
            }
        }
    }

    private void managePaySzzt() {

        String invoiceNumber = Code;
        String amount = String.valueOf((int) Payment);
        if (hostApp == HostApp.SEPEHR) {
            Tools.displaySafeDialog(ManageReceiptActivity.this, new SelectWayDialog(ManageReceiptActivity.this, data -> {
                String paymentWay = "";
                if (data != null && data.length > 0)
                    paymentWay = (String) data[0];
                SDKManager.purchase(ManageReceiptActivity.this, hostApp, amount, invoiceNumber, paymentWay.equals("normal") ? PurchaseType.NORMAL_CARD.value : PurchaseType.BON_CARD.value, "-1", "تست پیغام تبلیغاتی زیر رسید", "تست پیغام نمایش", new GeneralPaymentCallback() {
                    @Override
                    public void onPaymentInitializationFailed(int status, String statusDescription, String reserveNumber, String maskedPan, String panHash) {
                        startActivity(ResultActivity.getIntent(ManageReceiptActivity.this, String.format(Locale.ENGLISH, "آغاز فرایند پرداخت با کد فاکتور %s با خطا مواجه شد. \nکد وضعیت: %d \n" + statusDescription, reserveNumber, status), String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan), String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                    }

                    @Override
                    public void onPaymentSucceed(String terminalNo, String merchantId, String posSerial, String reserveNumber, String traceNumber, String rrn, String ref, String amount, String txnDate, String txnTime, String maskedPan, String panHash) {
                        saveReceipt(traceNumber, amount);
                        startActivity(ResultActivity.getIntent(ManageReceiptActivity.this, "پرداخت با موفقیت انجام شد.",
                                String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                                String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                                String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                                String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                    }

                    @Override
                    public void onPaymentFailed(int errorCode, String errorDescription, String terminalNo, String merchantId, String posSerial, String reserveNumber, String traceNumber, String rrn, String ref, String amount, String txnDate, String txnTime, String maskedPan, String panHash) {
                        startActivity(ResultActivity.getIntent(ManageReceiptActivity.this, "پرداخت با خطا مواجه شد.",
                                String.format(Locale.ENGLISH, "کد خطا: %d", errorCode),
                                String.format(Locale.ENGLISH, "شرح خطا: %s", errorDescription),
                                String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                                String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                                String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                                String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                    }

                    @Override
                    public void onPaymentCancelled(String reserveNumber, String maskedPan, String panHash) {
                        startActivity(ResultActivity.getIntent(ManageReceiptActivity.this, String.format(Locale.ENGLISH, "فرایند پرداخت با کد فاکتور %s توسط کاربر لغو شد.", reserveNumber),
                                String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                String.format(Locale.ENGLISH, "هش کارت کاربر: %s", panHash)));
                    }
                });

            }));
        } else {
            SDKManager.purchase(ManageReceiptActivity.this, hostApp, amount, invoiceNumber, 0, "-1", "تست پیغام تبلیغاتی زیر رسید", "تست پیغام نمایش", new GeneralPaymentCallback() {
                @Override
                public void onPaymentInitializationFailed(int status, String statusDescription, String reserveNumber, String maskedPan, String panHash) {
                    startActivity(ResultActivity.getIntent(ManageReceiptActivity.this, String.format(Locale.ENGLISH, "آغاز فرایند پرداخت با کد فاکتور %s با خطا مواجه شد. \nکد وضعیت: %d \n" + statusDescription, reserveNumber, status), String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan), String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                }

                @Override
                public void onPaymentSucceed(String terminalNo, String merchantId, String posSerial, String reserveNumber, String traceNumber, String rrn, String ref, String amount, String txnDate, String txnTime, String maskedPan, String panHash) {
                    saveReceipt(traceNumber, amount);
                    startActivity(ResultActivity.getIntent(ManageReceiptActivity.this, "پرداخت با موفقیت انجام شد.",
                            String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                            String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                            String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                            String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                            String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                            String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                }

                @Override
                public void onPaymentFailed(int errorCode, String errorDescription, String terminalNo, String merchantId, String posSerial, String reserveNumber, String traceNumber, String rrn, String ref, String amount, String txnDate, String txnTime, String maskedPan, String panHash) {
                    startActivity(ResultActivity.getIntent(ManageReceiptActivity.this, "پرداخت با خطا مواجه شد.",
                            String.format(Locale.ENGLISH, "کد خطا: %d", errorCode),
                            String.format(Locale.ENGLISH, "شرح خطا: %s", errorDescription),
                            String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                            String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                            String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                            String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                            String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                            String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                }

                @Override
                public void onPaymentCancelled(String reserveNumber, String maskedPan, String panHash) {
                    startActivity(ResultActivity.getIntent(ManageReceiptActivity.this, String.format(Locale.ENGLISH, "فرایند پرداخت با کد فاکتور %s توسط کاربر لغو شد.", reserveNumber),
                            String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                            String.format(Locale.ENGLISH, "هش کارت کاربر: %s", panHash)));
                }
            });
        }
    }

    private void managePayUrovoI9000() {
        String amount = String.valueOf((int) Payment);
        Intent intent = new Intent("ir.totan.pos.view.cart.TXN");
        intent.putExtra("type", 3);
        intent.putExtra("invoiceNumber", Code);
        intent.putExtra("amount", amount);
        intent.putExtra("res_num", 2L);
        startActivityForResult(intent, REQUEST_i9000s);

    }

    private void saveReceipt(String traceNumber, String amount) {
        int posBankPosition = SharedPreferencesHelper.getPrefBankPos(mContext);
        Date date = new Date();
        Cheque cheque = new Cheque();
        cheque.setAmount(ServiceTools.toDouble(amount));
        cheque.setBranch("");
        cheque.setDescription(txtDescription.getText().toString().trim());
        cheque.setNumber(traceNumber);
        cheque.setModifyDate(date.getTime());
        cheque.setDate(date.getTime());
        if (arrayBank != null) {
            cheque.setBankId(String.valueOf(arrayBank.get(posBankPosition).getBankId()));
        }
        cheque.setType(ProjectInfo.CASHRECEIPT_TYPE);
        arrayCheque.add(cheque);

        if (adCheque == null) {
            adCheque = new AdapterCheque(mActivity, arrayCheque);
            lstCheque.setAdapter(adCheque);
            ServiceTools.setListViewChequeHeightBasedOnChildren(lstCheque);
        } else {
            adCheque.notifyDataSetChanged();
            ServiceTools.setListViewChequeHeightBasedOnChildren(lstCheque);
        }
    }


    public boolean visitorHasCredit(double finalPrice) {

        mSpentCredit = 0;
        long invoicesWithoutReciept = 0;

        Visitor visitor = db.getVisitor();

        double visitorCreditValue = visitor.getTotalCredit();
        //اگر اعتبار نسیه ویزیتور بدون محدودیت باشد
        if (visitorCreditValue == NoLimit)
            return true;

        //مجموع تمام فاکتور ها چه ارسال شده یا نشده
        double totalPriceInvoice = db.getPurePriceInvoice();

        //مجموع تمام دریافتی ها چه ارسال شده یا نشده
        double totalReceipt = db.getTotalPriceReceipt();
        //اعتبار مصرف شده
        mSpentCredit = totalPriceInvoice - totalReceipt;

        if (Mode == MODE_EDIT)
            //اعتبار مصرف شده بدون احتساب فاکتور فعلی جهت ویرایش
            mSpentCredit = mSpentCredit - mCurrentPrice;

        //اگر مجموع اعتبار مصرف شده و  فاکتور فعلی بیشتر از اعتبار باشد باید برای ثبت فاکتور دریافتی ثبت کند
        if ((finalPrice + mSpentCredit) > visitorCreditValue) {
            double mMinimumReceipt = (finalPrice + mSpentCredit) - visitorCreditValue;
            return false;
        }
        ///////////////////////////////
        return true;
    }

    //اعتبار باقیمانده ویزیتور
    private double remainVisitorCredit(double finalPrice) {
        return (visitorCreditValue - (finalPrice + mSpentCredit));
    }

    private double totalCashAndCheque() {
        double totalCheque = 0;
        double totalReceipt = 0;
        String stringCash;
        for (Cheque item : arrayCheque) {
            totalCheque += item.getAmount();
        }
        stringCash = txtAmount.getText().toString();
        if (!TextUtils.isEmpty(stringCash))
            totalReceipt = ServiceTools.toDouble(ServiceTools.MoneyFormatToNumber(stringCash));
        return totalCheque + totalReceipt;
    }

    /**
     * Read From Database And Fill AdapterCheque And Edittext
     */
    private void FillView() {

        double Payment = 0;
        //Read From Database receipt_________________________
        receipt = db.GetReceipt(ReceiptId);
        CustomerId = receipt.getPersonId();
        CustomerClientId = receipt.getPersonClientId();
        //Fill Information____________________________________
        lngDate = receipt.getDate();
        StrDate = getDateAndTimeForLong(lngDate);
        txtDescription.setText(receipt.getDescription());
        txtAmount.setText(ServiceTools.formatPrice(receipt.getCashAmount()));
        tvDate.setText(StrDate);
        if (!receipt.getTrackingCode().equals(ProjectInfo.DONT_CODE)) {
            Code = receipt.getTrackingCode();
            txtTrackingCode.setText(receipt.getTrackingCode());
            Payment = CalculatePayment(receipt.getTrackingCode());
            txtPayment.setText(ServiceTools.formatPrice(Payment));
        } else
            Code = ProjectInfo.DONT_CODE;
        //Read arrayCheque From Database And Set Adapter_______
        arrayCheque = db.getAllCheque(receipt.getId());
        adCheque = new AdapterCheque(mActivity, arrayCheque);
        lstCheque.setAdapter(adCheque);
        ServiceTools.setListViewChequeHeightBasedOnChildren(lstCheque);
    }

    private double CalculatePayment(String code) {
        order = db.GetOrder(code);
        ArrayList<OrderDetail> array;
        double Price = 0, Discount = 0, FinalPrice = 0;
        //calculate FinalPrice________________________________________________________
        array = db.getAllOrderDetailWithOrderId(order.getId());
        for (OrderDetail item : array) {
            Price += ServiceTools.getCalculateFinalPrice(item, mContext);
        }
        Discount = order.getDiscount();
        FinalPrice = Price - Discount;
        return FinalPrice;
    }

    /**
     * Initializing Variables
     */
    private void initialise() {

        txtCustomerName = (EditText) findViewById(R.id.txtCustomerName);
        txtAmount = (EditText) findViewById(R.id.txtCashAmount);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        txtMarketName = (EditText) findViewById(R.id.txtMarketName);
        txtTrackingCode = (EditText) findViewById(R.id.txtTrackingCode);
        txtPayment = (EditText) findViewById(R.id.txtPayment);
        tvDate = (TextView) findViewById(R.id.tvDate);
        btnAddCheque = (Button) findViewById(R.id.btnAddCheque);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDatePicker = (Button) findViewById(R.id.btnDatePicker);
        btnSelectCustomer = (Button) findViewById(R.id.btnSelectCustomer);
        btnSelectInvoice = (Button) findViewById(R.id.btnSelectInvoice);
        lstCheque = (ListView) findViewById(R.id.lstCheque);
        btnPayPos = (Button) findViewById(R.id.btnPayPos);
        posLL = (LinearLayout) findViewById(R.id.posLL);
        lstCheque = (ListView) findViewById(R.id.lstCheque);

        printerBrand = SharedPreferencesHelper.getPrefPrinterBrand(mContext);
        if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223 || printerBrand == ProjectInfo.SMART_POS_UROVO_i9000s)
            posLL.setVisibility(View.VISIBLE);

        db = new DbAdapter(mContext);
        db.open();
        adCheque = new AdapterCheque(mActivity, arrayCheque);
        lstCheque.setAdapter(adCheque);
    }

    /**
     * Save Information In database
     */
    private void Save() {
        Visitor visitor;
        visitor = db.getVisitor();
        dt = new Date();
        long result;
        long receiptClientId = ServiceTools.toLong(ServiceTools.getGenerationCode());
        View focusView;
        String amount = txtAmount.getText().toString();
        if (!TextUtils.isEmpty(amount) || arrayCheque.size() != 0) {
            if (Mode == MODE_NEW) {
                receipt = new Receipt();
                receipt.setPersonId(CustomerId);
                receipt.setPersonClientId(CustomerClientId);
                receipt.setVisitorId(BaseActivity.getPrefUserId());
                receipt.setModifyDate(dt.getTime());
                receipt.setDate(lngDate);
                receipt.setCashAmount(ServiceTools.toDouble(txtAmount.getText().toString()));
                receipt.setDescription(txtDescription.getText().toString().trim());
                receipt.setMahakId(BaseActivity.getPrefMahakId());
                receipt.setDatabaseId(BaseActivity.getPrefDatabaseId());
                receipt.setTrackingCode(Code);
                receipt.setReceiptClientId(receiptClientId);

                if (visitor != null) {
                    receipt.setCashCode(String.valueOf(visitor.getCashCode()));
                }
                result = db.AddReceipt(receipt);
                if (result > 0) {
                    long ReceiptId = db.getMax(DbSchema.Receiptschema.TABLE_NAME, DbSchema.Receiptschema.COLUMN_ID);
                    for (Cheque item : arrayCheque) {
                        item.setReceiptId(ReceiptId);
                        item.setChequeClientId(ServiceTools.toLong(ServiceTools.getGenerationCode()) + arrayCheque.indexOf(item) + 1);
                        item.setReceiptClientId(receiptClientId);
                        item.setMahakId(BaseActivity.getPrefMahakId());
                        item.setDatabaseId(BaseActivity.getPrefDatabaseId());
                        db.AddCheque(item);
                    }
                    setResult(RESULT_OK);
                    if (Page == PAGE_Invoice_Detail_Activity) {
                        Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                        intent.putExtra("totalCashAndCheque", totalCashAndCheque());
                        setResult(RESULT_OK, intent);
                        //finish();
                    } else if (Page == PAGE_ORDER_DETAIL ) {
                        Intent intent = new Intent(getApplicationContext(), OrderDetailActivity.class);
                        intent.putExtra(PAGE, PAGE_Invoice_Detail_Activity);
                        intent.putExtra(ID, OrderId);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                       // finish();
                    }else if(Page == PAGE_RECEIPTLIST){
                        Intent intent = new Intent(mContext, ReceiptsListActivity.class);
                        setResult(RESULT_OK, intent);
                    }else {
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                       // finish();
                    }
                }
                Clear();
            } else if (Mode == MODE_EDIT) {
                receipt.setPersonId(CustomerId);
                receipt.setPersonClientId(CustomerClientId);
                receipt.setVisitorId(BaseActivity.getPrefUserId());
                receipt.setMahakId(BaseActivity.getPrefMahakId());
                receipt.setDatabaseId(BaseActivity.getPrefDatabaseId());
                receipt.setModifyDate(dt.getTime());
                receipt.setDate(lngDate);
                receipt.setCashAmount(ServiceTools.toDouble(txtAmount.getText().toString()));
                receipt.setDescription(txtDescription.getText().toString().trim());
                receipt.setTrackingCode(Code);
                receipt.setReceiptClientId(receiptClientId);
                if (visitor != null) {
                    receipt.setCashCode(String.valueOf(visitor.getCashCode()));
                }
                db.UpdateReceipt(receipt);
                db.DeleteChequesInReceipt(receipt.getId());
                for (Cheque item : arrayCheque) {
                    item.setReceiptId(ReceiptId);

                    item.setReceiptClientId(receiptClientId);

                    item.setChequeClientId(ServiceTools.toLong(ServiceTools.getGenerationCode()) + arrayCheque.indexOf(item) + 1);
                    item.setMahakId(BaseActivity.getPrefMahakId());
                    item.setDatabaseId(BaseActivity.getPrefDatabaseId());
                    db.AddCheque(item);
                }
                setResult(RESULT_OK);
                Clear();
                //___________________________________________________
                if (Page == PAGE_ORDER_DETAIL) {
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else
                    finish();
            }

        } else {
            txtAmount.setError(getString(R.string.error_field_required));
            focusView = txtAmount;
            focusView.requestFocus();
        }

    }

    /**
     * Reset Values
     */
    private void Clear() {
        arrayCheque.clear();
        CustomerName = "";
        StrDate = "";
        CashAmount = "";
        Description = "";
        MarketName = "";
        Code = ProjectInfo.DONT_CODE;
        Payment = 0.0;
        lngDate = 0;
    }

    private class AdapterCheque extends ArrayAdapter<Cheque> {
        Activity mcontaxt;

        public AdapterCheque(Activity contaxt, ArrayList<Cheque> array) {
            super(contaxt, android.R.layout.simple_list_item_1, array);
            mcontaxt = contaxt;
        }

        public class Holder {
            private TextView tvType, tvBank, tvAmount, tvNumber, tvDate;

            public Holder(View view) {
                tvType = (TextView) view.findViewById(R.id.tvChequeType);
                tvBank = (TextView) view.findViewById(R.id.tvBank);
                tvAmount = (TextView) view.findViewById(R.id.txtAmount);
                tvNumber = (TextView) view.findViewById(R.id.tvNumber);
                tvDate = (TextView) view.findViewById(R.id.tvDate);
            }

            public void Populate(Cheque cheque) {
                if (cheque.getType() == ProjectInfo.CHEQUE_TYPE)
                    tvType.setText(getResources().getString(R.string.str_cheque_type));
                else if (cheque.getType() == ProjectInfo.CASHRECEIPT_TYPE)
                    tvType.setText(getResources().getString(R.string.str_cash_receipt_type));

                tvBank.setText(cheque.getBankName());
                tvAmount.setText(ServiceTools.formatPrice(cheque.getAmount()));
                tvNumber.setText(cheque.getNumber());
                tvDate.setText(getDateAndTimeForLong(cheque.getDate()));
            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowview = convertView;
            Holder holder = null;
            LayoutInflater inflater = null;
            final Cheque cheque = getItem(position);

            if (rowview == null) {
                inflater = mcontaxt.getLayoutInflater();
                rowview = inflater.inflate(R.layout.lst_cheque_item, null, false);
                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();
            holder.Populate(cheque);

            return rowview;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putString(CUSTOMER_NAME_KEY, CustomerName);
        outState.putString(MARKET_KEY, MarketName);
        outState.putString(STR_DATE_KEY, StrDate);
        outState.putLong(LNG_DATE_KEY, lngDate);
        outState.putString(CASHAMOUNT_KEY, CashAmount);
        outState.putString(DESCRIPTION_KEY, Description);
        outState.putDouble(PAYMENT_KEY, Payment);
        outState.putString(CODE_KEY, Code);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);

        CustomerName = savedInstanceState.getString(CUSTOMER_NAME_KEY);
        MarketName = savedInstanceState.getString(MARKET_KEY);
        StrDate = savedInstanceState.getString(STR_DATE_KEY);
        lngDate = savedInstanceState.getLong(LNG_DATE_KEY);
        CashAmount = savedInstanceState.getString(CashAmount);
        Description = savedInstanceState.getString(DESCRIPTION_KEY);
        Payment = savedInstanceState.getDouble(PAYMENT_KEY);
        Code = savedInstanceState.getString(CODE_KEY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CUSTOMER_LIST) {
            if (resultCode == RESULT_OK) {
                CustomerId = data.getIntExtra(CUSTOMERID_KEY, 0);
                CustomerClientId = data.getLongExtra(CUSTOMER_CLIENT_ID_KEY, 0);

                if (CustomerId != ProjectInfo.CUSTOMERID_GUEST) {
                    customer = db.getCustomerWithPersonId(CustomerId);
                    txtCustomerName.setText(customer.getName());
                    txtMarketName.setText(customer.getOrganization());
                } else {
                    customer = db.getCustomerWithPersonClientId(CustomerClientId);
                    txtCustomerName.setText(customer.getName());
                    txtMarketName.setText(customer.getOrganization());
                }
            }

        } else if (requestCode == REQUEST_MANAGE_CHEQUE) {

            if (resultCode == RESULT_OK) {
                if (adCheque == null) {
                    adCheque = new AdapterCheque(mActivity, arrayCheque);
                    lstCheque.setAdapter(adCheque);
                    ServiceTools.setListViewChequeHeightBasedOnChildren(lstCheque);
                } else {
                    adCheque.notifyDataSetChanged();
                    ServiceTools.setListViewChequeHeightBasedOnChildren(lstCheque);
                }
                //Check Validate________________________________________________________________
                boolean cancel = false;
                View focusView = null;
                String amount = txtAmount.getText().toString();

                if (TextUtils.isEmpty(amount) && arrayCheque.size() == 0) {
                    txtAmount.setError(getString(R.string.str_valid_empty));
                    focusView = txtAmount;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                }
            }
        } else if (requestCode == REQUEST_ORDER_LIST) {
            if (resultCode == RESULT_OK) {
                Code = data.getStringExtra(CODE_KEY);
                Payment = data.getDoubleExtra(PAYMENT_KEY, 0);
                CustomerId = data.getIntExtra(CUSTOMERID_KEY, 0);
                CustomerClientId = data.getLongExtra(CUSTOMER_CLIENT_ID_KEY, 0);
                txtTrackingCode.setText(Code);
                txtPayment.setText(ServiceTools.formatPrice(Payment));
                if (CustomerId != ProjectInfo.CUSTOMERID_GUEST) {
                    customer = db.getCustomerWithPersonId(CustomerId);
                    txtCustomerName.setText(customer.getName());
                    txtMarketName.setText(customer.getOrganization());
                } else {
                    customer = db.getCustomerWithPersonClientId(CustomerClientId);
                    txtCustomerName.setText(customer.getName());
                    txtMarketName.setText(customer.getOrganization());
                }
            }
        } else if (requestCode == REQUEST_i9000s) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getBundleExtra("response");
                String trace = b.getString("trace", null);
                String amount = b.getString("amount", null);
                saveReceipt(trace, amount);
            } else
                Toast.makeText(mContext, "خطا در پرداخت توسط پوز", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    //back events
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (arrayCheque.size() > 0)
                    onBackPressedStrategy();
                else {
                    Clear();
                    finish();
                }
                break;
        }
        return (true);
    }

    private void onBackPressedStrategy() {
        String alertMsg = getResources().getString(R.string.receipt_save_alert);
        alertDialogBuilder
                .setMessage(alertMsg)
                .setCancelable(false)
                .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveReciept();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Clear();
                        finish();
                    }
                })
                .setNeutralButton("انصراف", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        Clear();
        finish();
    }

}
