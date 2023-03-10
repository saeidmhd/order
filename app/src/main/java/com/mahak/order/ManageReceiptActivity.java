package com.mahak.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ava.thirdparty.container.ThirdPartyDTO;
import com.ava.thirdparty.listener.ResultListener;
import com.ava.thirdparty.manager.ThirdPartyManager;
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
import java.util.UUID;

import static com.mahak.order.common.ServiceTools.getDateAndTimeForLong;

//import com.mahak.order.common.ProductInOrder;

@SuppressLint("ValidFragment")
public class ManageReceiptActivity extends BaseActivity implements ResultListener  {

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
    private double  CashAmount , debtOfInvoce;
    public static ArrayList<Cheque> arrayCheque = new ArrayList<>();

    private EditText txtCustomerName, txtAmount, txtDescription, txtMarketName, txtTrackingCode, txtPayment,txtSumReceipt , invoiceBalance;
    private TextView tvDate;
    private Button btnAddCheque, btnSave, btnDatePicker, btnSelectCustomer, btnSelectInvoice, btnPayPos , btnAddReceipt;
    private ListView lstCheque;
    private LinearLayout invoice_detail,ll_factor_balance;
    private Bundle Extras;
    private Date dt;
    private String CustomerName, StrDate, Description, MarketName;
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
    private double remainVisitorCreditValue;
    private double payment;
    private double savedCashedAndCheque;
    private static double mCurrentPrice;
    private static final long NoLimit = -1;
    private static double visitorCreditValue;
    private ArrayList<Bank> arrayBank;
    double sum_receipt = 0;

    private HostApp hostApp;
    private androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
    int printerBrand;
    Order order;
    private long OrderId;
    String posDescription;

    protected ThirdPartyManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_receipt2);

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

        if(printerBrand == ProjectInfo.SMART_POS_MoreFun){
            manager = ThirdPartyManager.getInstance();
        }

        Extras = getIntent().getExtras();

        arrayBank = new ArrayList<>();
        Visitor visitor = BaseActivity.getVisitor();
        if (visitor != null) {
            arrayBank = db.getAllBank(visitor.getBankCode());
        } else {
            arrayBank = db.getAllBank();
        }

        Visitor mVisitor = db.getVisitor();

        //?????????? ???????? ???????????? ???? ???? ?????????? ?????? ???? ????????
        TotalPriceInvoice = db.getPurePriceInvoice();
        //?????????? ???????? ?????????????? ???? ???? ?????????? ?????? ???? ????????
        TotalReceipt = db.getTotalPriceReceipt();
        //???????????? ???????? ??????
        mSpentCredit = TotalPriceInvoice - TotalReceipt;
        //???????????? ??????????????
        mVisitorCredit = mVisitor.getTotalCredit();

        totalReceiptForInvoiceNotPublished = db.getTotalReceiptForInvoiceNotPublished();

        if (Extras != null) {
            Page = Extras.getInt(PAGE);
            Mode = Extras.getInt(MODE_PAGE);
            CustomerId = Extras.getInt(CUSTOMERID_KEY);
            CustomerClientId = Extras.getLong(CUSTOMER_CLIENT_ID_KEY);
            OrderId = Extras.getLong(ID);
            Code = Extras.getString(CODE_KEY) != null ? Extras.getString(CODE_KEY) : ProjectInfo.DONT_CODE;
            Payment = Extras.getDouble(PAYMENT_KEY);


            //lastBalance = calculatePaymentAmount(Payment);

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
                invoice_detail.setVisibility(View.VISIBLE);
                ll_factor_balance.setVisibility(View.VISIBLE);
                txtTrackingCode.setText(Code);
                txtPayment.setText(ServiceTools.formatPrice(Payment));
            } else if (Page == PAGE_RECEIPTLIST) {
                if (Mode == MODE_EDIT) {
                    savedCashedAndCheque = db.getTotalReceiptWithId(ReceiptId);
                    // ???????????? ???????? ?????? ???????? ???????????? ?????????????? ????????
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
                    invoice_detail.setVisibility(View.VISIBLE);
                    ll_factor_balance.setVisibility(View.VISIBLE);
                    txtTrackingCode.setText(Code);
                    txtPayment.setText(ServiceTools.formatPrice(Payment));
                }
            }

            Customer customer;
            if (CustomerId == ProjectInfo.CUSTOMERID_GUEST) {
                customer = db.getCustomerWithPersonClientId(CustomerClientId);
            } else {
                customer = db.getCustomerWithPersonId(CustomerId);
            }
            txtCustomerName.setText(customer.getName());
            txtMarketName.setText(customer.getOrganization());
        }else if (savedInstanceState != null) {

            CustomerName = savedInstanceState.getString(CUSTOMER_NAME_KEY);
            MarketName = savedInstanceState.getString(MARKET_KEY);
            StrDate = savedInstanceState.getString(STR_DATE_KEY);
            CashAmount = savedInstanceState.getDouble(CASHAMOUNT_KEY);
            Description = savedInstanceState.getString(DESCRIPTION_KEY);
            Code = savedInstanceState.getString(CODE_KEY);
            Payment = savedInstanceState.getDouble(PAYMENT_KEY);

            txtCustomerName.setText(CustomerName);
            txtMarketName.setText(MarketName);
            tvDate.setText(StrDate);
            txtAmount.setText(ServiceTools.formatPrice(CashAmount));
            txtDescription.setText(Description);
            invoice_detail.setVisibility(View.VISIBLE);
            ll_factor_balance.setVisibility(View.VISIBLE);
            txtTrackingCode.setText(Code);
            txtPayment.setText(ServiceTools.formatPrice(Payment));
        }

        debtOfInvoce = calculateDebtOfInvoice();
        sum_receipt = debtOfInvoce;
        invoiceBalance.setText(ServiceTools.formatPrice(debtOfInvoce));

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

                CashAmount = ServiceTools.toDouble(ServiceTools.MoneyFormatToNumber(s.toString()));
                calculate_sum_receipt();
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
                intent.putExtra(TYPE_KEY, ProjectInfo.CHEQUE_TYPE);
                startActivityForResult(intent, REQUEST_MANAGE_CHEQUE);

            }
        });
        btnAddReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ManageChequeActivity.class);
                intent.putExtra(MODE_PAGE, MODE_NEW);
                intent.putExtra(TYPE_KEY, ProjectInfo.CASHRECEIPT_TYPE);
                startActivityForResult(intent, REQUEST_MANAGE_CHEQUE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReceipt();
            }
        });

        btnSelectInvoice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                intent.putExtra(PAGE, PAGE_MANAGE_RECEIPT);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_INVOCIE);
                intent.putExtra(CUSTOMER_NAME_KEY, txtCustomerName.getText().toString());
                startActivityForResult(intent, REQUEST_ORDER_LIST);
            }
        });

        btnPayPos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String stringReceipt = txtAmount.getText().toString();
                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_smart_pos);

                TextView text = (TextView) dialog.findViewById(R.id.txtPosPrice);
                TextView description = (TextView) dialog.findViewById(R.id.txtDescription);
                text.setText(stringReceipt);

                Button ok = (Button) dialog.findViewById(R.id.ok_btn);
                Button cancel = (Button) dialog.findViewById(R.id.cancel_btn);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        posDescription = description.getText().toString();
                        String receiptValue = text.getText().toString();
                        double amount = ServiceTools.toDouble(ServiceTools.MoneyFormatToNumber(receiptValue)) ;
                        if(amount > 0){
                            if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223)
                                managePaySzzt(amount);
                            else if (printerBrand == ProjectInfo.SMART_POS_UROVO_i9000s)
                                managePayUrovoI9000(amount);
                            else if (printerBrand == ProjectInfo.SMART_POS_MoreFun)
                                manageMoreFun(amount);
                            dialog.dismiss();
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


    }// End of OnCreate

    private double calculate_sum_receipt() {
        sum_receipt = 0;
        if(arrayCheque.size() > 0){
            for(Cheque cheque : arrayCheque)
                sum_receipt += cheque.getAmount();
        }
        sum_receipt += CashAmount;
        txtSumReceipt.setText(ServiceTools.formatPrice(sum_receipt));
        return sum_receipt;
    }

    public boolean canSaveBaseOnCustomerCredit() {
        int customerId;
        if (Mode == MODE_NEW){
            customerId = CustomerId;
        }else
            customerId = receipt.getPersonId();
        if(customerId == 0)
            return true;
        Customer customer = db.getCustomerWithPersonId(customerId);
        double customerCredit = customer.getCredit();
        double customerBalance = customer.getBalance();
        if (customerCredit == -1)
            return true;
        else {
            return sum_receipt - debtOfInvoce + customerCredit + customerBalance  >= 0 ;
        }
    }

    private void saveReceipt() {
        double totalCashAndCheque = totalCashAndCheque();
        if (mVisitorCredit != -1)
            checkVisitorCredit(totalCashAndCheque);
        else if(!canSaveBaseOnCustomerCredit())
            Toast.makeText(mContext, "???????? ?????????? ?????????????? ?????? ?? ???? ???? ?????????????????? ???????????? ?????????? ?????????? ????????!", Toast.LENGTH_LONG).show();
        else
            Save();
    }

    private void checkVisitorCredit(double totalCashAndCheque) {
        remainVisitorCreditValue = mVisitorCredit - mSpentCredit;
        //?????????????????? ???????????? ?????????????? ???? ???????????? ???????????? ????????
        if (Mode == MODE_NEW) {
            remainVisitorCreditValue =- Payment;
        }
        if (remainVisitorCreditValue + totalCashAndCheque < 0)
            Toast.makeText(mContext, "???????? ?????????? ?????????????? ?????? ?? ???? ???? ?????????????????? ???????????? ?????????????? ?????????? ????????!", Toast.LENGTH_SHORT).show();
    }

    private void managePaySzzt(double receipt_amount) {

        String invoiceNumber = Code;
        String amount = String.valueOf((int) receipt_amount);
        if (hostApp == HostApp.SEPEHR) {
            Tools.displaySafeDialog(ManageReceiptActivity.this, new SelectWayDialog(ManageReceiptActivity.this, data -> {
                String paymentWay = "";
                if (data != null && data.length > 0)
                    paymentWay = (String) data[0];
                SDKManager.purchase(ManageReceiptActivity.this, hostApp, amount, invoiceNumber, paymentWay.equals("normal") ? PurchaseType.NORMAL_CARD.value : PurchaseType.BON_CARD.value, "-1", "?????? ?????????? ???????????????? ?????? ????????", "?????? ?????????? ??????????", new GeneralPaymentCallback() {
                    @Override
                    public void onPaymentInitializationFailed(int status, String statusDescription, String reserveNumber, String maskedPan, String panHash) {
                        startActivity(ResultActivity.getIntent(ManageReceiptActivity.this, String.format(Locale.ENGLISH, "???????? ???????????? ???????????? ???? ???? ???????????? %s ???? ?????? ?????????? ????. \n???? ??????????: %d \n" + statusDescription, reserveNumber, status), String.format(Locale.ENGLISH, "?????????? ???????? ??????????:\n %s", maskedPan), String.format(Locale.ENGLISH, "???? ???????? ??????????:\n %s", panHash)));
                    }

                    @Override
                    public void onPaymentSucceed(String terminalNo, String merchantId, String posSerial, String reserveNumber, String traceNumber, String rrn, String ref, String amount, String txnDate, String txnTime, String maskedPan, String panHash) {
                        saveSmartPosReceipt(traceNumber, amount);
                        startActivity(ResultActivity.getIntent(ManageReceiptActivity.this, "???????????? ???? ???????????? ?????????? ????.",
                                String.format(Locale.ENGLISH, "???? ????????????: %s", reserveNumber),
                                String.format(Locale.ENGLISH, "???? ????????????: %s", traceNumber),
                                String.format(Locale.ENGLISH, "?????????? ???????? ??????????????: %s", rrn),
                                String.format(Locale.ENGLISH, "???????? ????????????: %s", amount),
                                String.format(Locale.ENGLISH, "?????????? ???????? ??????????:\n %s", maskedPan),
                                String.format(Locale.ENGLISH, "???? ???????? ??????????:\n %s", panHash)));
                    }

                    @Override
                    public void onPaymentFailed(int errorCode, String errorDescription, String terminalNo, String merchantId, String posSerial, String reserveNumber, String traceNumber, String rrn, String ref, String amount, String txnDate, String txnTime, String maskedPan, String panHash) {
                        startActivity(ResultActivity.getIntent(ManageReceiptActivity.this, "???????????? ???? ?????? ?????????? ????.",
                                String.format(Locale.ENGLISH, "???? ??????: %d", errorCode),
                                String.format(Locale.ENGLISH, "?????? ??????: %s", errorDescription),
                                String.format(Locale.ENGLISH, "???? ????????????: %s", reserveNumber),
                                String.format(Locale.ENGLISH, "???? ????????????: %s", traceNumber),
                                String.format(Locale.ENGLISH, "?????????? ???????? ??????????????: %s", rrn),
                                String.format(Locale.ENGLISH, "???????? ????????????: %s", amount),
                                String.format(Locale.ENGLISH, "?????????? ???????? ??????????:\n %s", maskedPan),
                                String.format(Locale.ENGLISH, "???? ???????? ??????????:\n %s", panHash)));
                    }

                    @Override
                    public void onPaymentCancelled(String reserveNumber, String maskedPan, String panHash) {
                        startActivity(ResultActivity.getIntent(ManageReceiptActivity.this, String.format(Locale.ENGLISH, "???????????? ???????????? ???? ???? ???????????? %s ???????? ?????????? ?????? ????.", reserveNumber),
                                String.format(Locale.ENGLISH, "?????????? ???????? ??????????:\n %s", maskedPan),
                                String.format(Locale.ENGLISH, "???? ???????? ??????????: %s", panHash)));
                    }
                });

            }));
        } else {
            SDKManager.purchase(ManageReceiptActivity.this, hostApp, amount, invoiceNumber, 0, "-1", "?????? ?????????? ???????????????? ?????? ????????", "?????? ?????????? ??????????", new GeneralPaymentCallback() {
                @Override
                public void onPaymentInitializationFailed(int status, String statusDescription, String reserveNumber, String maskedPan, String panHash) {
                    startActivity(ResultActivity.getIntent(ManageReceiptActivity.this, String.format(Locale.ENGLISH, "???????? ???????????? ???????????? ???? ???? ???????????? %s ???? ?????? ?????????? ????. \n???? ??????????: %d \n" + statusDescription, reserveNumber, status), String.format(Locale.ENGLISH, "?????????? ???????? ??????????:\n %s", maskedPan), String.format(Locale.ENGLISH, "???? ???????? ??????????:\n %s", panHash)));
                }

                @Override
                public void onPaymentSucceed(String terminalNo, String merchantId, String posSerial, String reserveNumber, String traceNumber, String rrn, String ref, String amount, String txnDate, String txnTime, String maskedPan, String panHash) {
                    saveSmartPosReceipt(traceNumber, amount);
                    startActivity(ResultActivity.getIntent(ManageReceiptActivity.this, "???????????? ???? ???????????? ?????????? ????.",
                            String.format(Locale.ENGLISH, "???? ????????????: %s", reserveNumber),
                            String.format(Locale.ENGLISH, "???? ????????????: %s", traceNumber),
                            String.format(Locale.ENGLISH, "?????????? ???????? ??????????????: %s", rrn),
                            String.format(Locale.ENGLISH, "???????? ????????????: %s", amount),
                            String.format(Locale.ENGLISH, "?????????? ???????? ??????????:\n %s", maskedPan),
                            String.format(Locale.ENGLISH, "???? ???????? ??????????:\n %s", panHash)));
                }

                @Override
                public void onPaymentFailed(int errorCode, String errorDescription, String terminalNo, String merchantId, String posSerial, String reserveNumber, String traceNumber, String rrn, String ref, String amount, String txnDate, String txnTime, String maskedPan, String panHash) {
                    startActivity(ResultActivity.getIntent(ManageReceiptActivity.this, "???????????? ???? ?????? ?????????? ????.",
                            String.format(Locale.ENGLISH, "???? ??????: %d", errorCode),
                            String.format(Locale.ENGLISH, "?????? ??????: %s", errorDescription),
                            String.format(Locale.ENGLISH, "???? ????????????: %s", reserveNumber),
                            String.format(Locale.ENGLISH, "???? ????????????: %s", traceNumber),
                            String.format(Locale.ENGLISH, "?????????? ???????? ??????????????: %s", rrn),
                            String.format(Locale.ENGLISH, "???????? ????????????: %s", amount),
                            String.format(Locale.ENGLISH, "?????????? ???????? ??????????:\n %s", maskedPan),
                            String.format(Locale.ENGLISH, "???? ???????? ??????????:\n %s", panHash)));
                }

                @Override
                public void onPaymentCancelled(String reserveNumber, String maskedPan, String panHash) {
                    startActivity(ResultActivity.getIntent(ManageReceiptActivity.this, String.format(Locale.ENGLISH, "???????????? ???????????? ???? ???? ???????????? %s ???????? ?????????? ?????? ????.", reserveNumber),
                            String.format(Locale.ENGLISH, "?????????? ???????? ??????????:\n %s", maskedPan),
                            String.format(Locale.ENGLISH, "???? ???????? ??????????: %s", panHash)));
                }
            });
        }
    }

    private void managePayUrovoI9000(double receipt_amount) {
        String amount = String.valueOf((int) receipt_amount);
        Intent intent = new Intent("ir.totan.pos.view.cart.TXN");
        intent.putExtra("type", 3);
        intent.putExtra("invoiceNumber", Code);
        intent.putExtra("amount", amount);
        intent.putExtra("res_num", 2L);
        startActivityForResult(intent, REQUEST_i9000s);

    }

    private void manageMoreFun(double receipt_amount) {
        String amount = String.valueOf((int) receipt_amount);
        if (amount.equals("0"))
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"INVALID AMOUNT",Toast.LENGTH_LONG).show();
                }
            });
            return;
        }

        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ThirdPartyDTO request = new ThirdPartyDTO();
                request.setAmount(amount);
                request.setSessionID(UUID.randomUUID().toString());//random number for check request is unique
                request.setPackageName(getPackageName());//application package name for double check
                try {
                    request.setReturnTimeout(6);//automatically return to third party app after timeout is must be over 5 sec
                } catch (Exception e) {
                    ServiceTools.logToFireBase(e);
                    e.printStackTrace();
                }
                manager.sendRequestSale(request, ManageReceiptActivity.this);
            }
        });
    }

    @Override
    public void onReceiveResult(Bundle bundle) {
        int code = (int) bundle.get("RequestCode");
        if (code == ThirdPartyManager.CODE_REQUEST_TXN) {
            ThirdPartyDTO response=  manager.receiveResponse(bundle);
            if (response.getResponseCode().equals("0")&&
                    response.getResponseMessage().equalsIgnoreCase("SUCCESS"))
            {
                saveSmartPosReceipt(response.getTrace(), response.getAmount());
            }
        }
    }

    private void saveSmartPosReceipt(String traceNumber, String amount) {
        int posBankPosition = SharedPreferencesHelper.getPrefBankPos(mContext);
        Date date = new Date();
        Cheque cheque = new Cheque();
        cheque.setAmount(ServiceTools.toDouble(amount));
        cheque.setBranch("");
        cheque.setDescription(posDescription);
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
        } else {
            adCheque.notifyDataSetChanged();
        }
        ServiceTools.setListViewChequeHeightBasedOnChildren(lstCheque);
        calculate_sum_receipt();
    }

    public boolean visitorHasCredit(double finalPrice) {

        mSpentCredit = 0;
        long invoicesWithoutReciept = 0;

        Visitor visitor = db.getVisitor();

        double visitorCreditValue = visitor.getTotalCredit();
        //?????? ???????????? ???????? ?????????????? ???????? ?????????????? ????????
        if (visitorCreditValue == NoLimit)
            return true;

        //?????????? ???????? ???????????? ???? ???? ?????????? ?????? ???? ????????
        double totalPriceInvoice = db.getPurePriceInvoice();

        //?????????? ???????? ?????????????? ???? ???? ?????????? ?????? ???? ????????
        double totalReceipt = db.getTotalPriceReceipt();
        //???????????? ???????? ??????
        mSpentCredit = totalPriceInvoice - totalReceipt;

        if (Mode == MODE_EDIT)
            //???????????? ???????? ?????? ???????? ???????????? ???????????? ???????? ?????? ????????????
            mSpentCredit = mSpentCredit - mCurrentPrice;

        //?????? ?????????? ???????????? ???????? ?????? ??  ???????????? ???????? ?????????? ???? ???????????? ???????? ???????? ???????? ?????? ???????????? ?????????????? ?????? ??????
        if ((finalPrice + mSpentCredit) > visitorCreditValue) {
            double mMinimumReceipt = (finalPrice + mSpentCredit) - visitorCreditValue;
            return false;
        }
        ///////////////////////////////
        return true;
    }

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

    private void FillView() {

        //Read From Database receipt_________________________
        receipt = db.GetReceipt(ReceiptId);
        CustomerId = receipt.getPersonId();
        CustomerClientId = receipt.getPersonClientId();
        //Fill Information____________________________________
        lngDate = receipt.getDate();
        StrDate = getDateAndTimeForLong(lngDate);
        txtDescription.setText(receipt.getDescription());
        txtAmount.setText(ServiceTools.formatPrice(receipt.getCashAmount()));
        CashAmount = receipt.getCashAmount();
        tvDate.setText(StrDate);
        if (!receipt.getTrackingCode().equals(ProjectInfo.DONT_CODE)) {
            Code = receipt.getTrackingCode();
            invoice_detail.setVisibility(View.VISIBLE);
            ll_factor_balance.setVisibility(View.VISIBLE);
            txtTrackingCode.setText(receipt.getTrackingCode());
            Payment = CalculatePayment(receipt.getTrackingCode());
            txtPayment.setText(ServiceTools.formatPrice(Payment));
        } else
            Code = ProjectInfo.DONT_CODE;
        //Read arrayCheque From Database And Set Adapter_______
        arrayCheque = db.getAllCheque(receipt.getReceiptClientId());
        adCheque = new AdapterCheque(mActivity, arrayCheque);
        lstCheque.setAdapter(adCheque);
        ServiceTools.setListViewChequeHeightBasedOnChildren(lstCheque);

        calculate_sum_receipt();
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

    private void initialise() {

        txtCustomerName = (EditText) findViewById(R.id.txtCustomerName);
        txtAmount = (EditText) findViewById(R.id.txtCashAmount);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        txtMarketName = (EditText) findViewById(R.id.txtMarketName);
        txtTrackingCode = (EditText) findViewById(R.id.txtTrackingCode);
        txtPayment = (EditText) findViewById(R.id.txtPayment);
        txtSumReceipt = (EditText) findViewById(R.id.txtSumReceipt);
        invoiceBalance = (EditText) findViewById(R.id.invoiceBalance);
        tvDate = (TextView) findViewById(R.id.tvDate);
        btnAddCheque = (Button) findViewById(R.id.btnAddCheque);
        btnAddReceipt = (Button) findViewById(R.id.btnAddReceipt);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDatePicker = (Button) findViewById(R.id.btnDatePicker);
        btnSelectCustomer = (Button) findViewById(R.id.btnSelectCustomer);
        btnSelectInvoice = (Button) findViewById(R.id.btnSelectInvoice);
        invoice_detail = (LinearLayout) findViewById(R.id.invoice_detail);
        ll_factor_balance = (LinearLayout) findViewById(R.id.ll_factor_balance);
        lstCheque = (ListView) findViewById(R.id.lstCheque);
        btnPayPos = (Button) findViewById(R.id.btnPayPos);
        lstCheque = (ListView) findViewById(R.id.lstCheque);

        printerBrand = SharedPreferencesHelper.getPrefPrinterBrand(mContext);
        if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223 || printerBrand == ProjectInfo.SMART_POS_UROVO_i9000s || printerBrand == ProjectInfo.SMART_POS_MoreFun)
            btnPayPos.setVisibility(View.VISIBLE);

        db = new DbAdapter(mContext);
        db.open();
        adCheque = new AdapterCheque(mActivity, arrayCheque);
        lstCheque.setAdapter(adCheque);
    }

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
                    long ReceiptId = db.getMax(DbSchema.ReceiptSchema.TABLE_NAME, DbSchema.ReceiptSchema.COLUMN_ID);
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
                        finish();
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
                        finish();
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

    private void Clear() {
        arrayCheque.clear();
        CustomerName = "";
        StrDate = "";
        CashAmount = 0.0;
        Description = "";
        MarketName = "";
        Code = ProjectInfo.DONT_CODE;
        Payment = 0.0;
        lngDate = 0;
    }

    private class AdapterCheque extends ArrayAdapter<Cheque> {
        Activity mContext;

        public AdapterCheque(Activity context, ArrayList<Cheque> array) {
            super(context, android.R.layout.simple_list_item_1, array);
            mContext = context;
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
                    tvType.setText("???? ");
                else if (cheque.getType() == ProjectInfo.CASHRECEIPT_TYPE)
                    tvType.setText("?????????? ");

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
                inflater = mContext.getLayoutInflater();
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
        outState.putDouble(CASHAMOUNT_KEY, CashAmount);
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
        CashAmount = savedInstanceState.getDouble(CASHAMOUNT_KEY);
        Description = savedInstanceState.getString(DESCRIPTION_KEY);
        Payment = savedInstanceState.getDouble(PAYMENT_KEY);
        Code = savedInstanceState.getString(CODE_KEY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CUSTOMER_LIST) {
            if (resultCode == RESULT_OK) {
                invoice_detail.setVisibility(View.GONE);
                ll_factor_balance.setVisibility(View.GONE);
                CustomerId = data.getIntExtra(CUSTOMERID_KEY, 0);
                CustomerClientId = data.getLongExtra(CUSTOMER_CLIENT_ID_KEY, 0);
                if (CustomerId != ProjectInfo.CUSTOMERID_GUEST) {
                    customer = db.getCustomerWithPersonId(CustomerId);
                } else {
                    customer = db.getCustomerWithPersonClientId(CustomerClientId);
                }
                txtCustomerName.setText(customer.getName());
                txtMarketName.setText(customer.getOrganization());
            }

        } else if (requestCode == REQUEST_MANAGE_CHEQUE) {

            if (resultCode == RESULT_OK) {
                if (adCheque == null) {
                    adCheque = new AdapterCheque(mActivity, arrayCheque);
                    lstCheque.setAdapter(adCheque);
                } else {
                    adCheque.notifyDataSetChanged();
                }
                calculate_sum_receipt();
                ServiceTools.setListViewChequeHeightBasedOnChildren(lstCheque);
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

                invoice_detail.setVisibility(View.VISIBLE);
                ll_factor_balance.setVisibility(View.VISIBLE);
                txtTrackingCode.setText(Code);

                txtPayment.setText(ServiceTools.formatPrice(Payment));
                if (CustomerId != ProjectInfo.CUSTOMERID_GUEST) {
                    customer = db.getCustomerWithPersonId(CustomerId);
                } else {
                    customer = db.getCustomerWithPersonClientId(CustomerClientId);
                }
                txtCustomerName.setText(customer.getName());
                txtMarketName.setText(customer.getOrganization());
            }
        } else if (requestCode == REQUEST_i9000s) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getBundleExtra("response");
                String trace = null;
                if (b != null) {
                    trace = b.getString("trace", null);
                    String amount = b.getString("amount", null);
                    String result = b.getString("result", null);
                    if (result.equals("succeed")) {
                        if (trace != null && amount != null) {
                            saveSmartPosReceipt(trace, amount);
                        }
                    }
                }
            } else
                Toast.makeText(mContext, "?????? ???? ???????????? ???????? ??????", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (totalCashAndCheque() != 0) {
                    onBackPressedStrategy();
                }
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
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveReceipt();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Clear();
                        finish();
                    }
                })
                .setNeutralButton("????????????", new DialogInterface.OnClickListener() {
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

    public double calculateDebtOfInvoice() {
        double invoiceReceipt = db.getTotalPriceReceiptPerInvoice(Code);
        if(Mode == MODE_NEW)
            return Payment - invoiceReceipt;
        else
            return Payment;
    }

    @Override
    public void onBackPressed() {
        if (totalCashAndCheque() != 0 ) {
            onBackPressedStrategy();
        }
        else {
            Clear();
            finish();
        }
    }

}
