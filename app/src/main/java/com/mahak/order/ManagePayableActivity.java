package com.mahak.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.mahak.order.common.Bank;
import com.mahak.order.common.MyCalendar;
import com.mahak.order.common.PayableTransfer;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.Visitor;
import com.mahak.order.libs.DatePicker;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.mahak.order.common.ServiceTools.getDateAndTimeForLong;

@SuppressLint("ValidFragment")
public class ManagePayableActivity extends BaseActivity {

    private Button btnDatePicker, btnSave, btnCancel;
    private EditText txtCashAmount;
    private long LngDate;
    private String StrDate, DayName;
    private Date dt;
    private Calendar cal;
    private DbAdapter db;
    private Context mContext;
    private ArrayList<Bank> arrayBank;
    private Visitor visitor;
    private Spinner spnBank;
    private RadioButton radioButtonBank;
    private RadioButton radioButtonExpense;
    private LinearLayout llspnBank;
    private int Type;
    private static int Page;
    private static int Mode;
    private Bundle Extras;
    private TextView txtDescription;
    private long BankCode = 0;
    private long PayableId;
    private PayableTransfer payableTransfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_payable);

//config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        TextView tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        tvPageTitle.setText(getString(R.string.str_save_payable));
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________

        mContext = this;

        initialise();
        Extras = getIntent().getExtras();


        if (Extras != null) {
            Page = Extras.getInt(PAGE);
            Mode = Extras.getInt(MODE_PAGE);

            if (Page == PAGE_PAYABLE_LIST) {
                if (Mode == MODE_EDIT) {
                    PayableId = (long) Extras.getInt(ID);
                    FillView();
                    FillSpinnerEdit();

                } else if (Mode == MODE_NEW) {
                    dt = new Date();
                    LngDate = dt.getTime();
                    StrDate = getDateAndTimeForLong(LngDate);
                    btnDatePicker.setText(StrDate);
                    radioButtonBank.setChecked(true);
                    Type = ProjectInfo.Bank_TYPE;
                    FillSpinner();

                }
            }

        }


        btnDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MyCalendar myCalendar = new MyCalendar(LngDate, ManagePayableActivity.this, getFragmentManager(), new MyCalendar.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker date) {
                        LngDate = date.getDate().getTime();
                        btnDatePicker.setText(ServiceTools.getDateAndTimeForLong(LngDate));
                    }

                    @Override
                    public void onGregorianSet(int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        LngDate = calendar.getTimeInMillis();
                        btnDatePicker.setText(ServiceTools.getDateAndTimeForLong(LngDate));
                    }
                });
                myCalendar.showDialog();
            }
        });

        radioButtonBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                radioButtonExpense.setChecked(false);
                llspnBank.setVisibility(View.VISIBLE);
                Type = ProjectInfo.Bank_TYPE;
            }
        });

        radioButtonExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtonBank.setChecked(false);
                radioButtonExpense.setChecked(true);
                llspnBank.setVisibility(View.GONE);
                Type = ProjectInfo.Expense_TYPE;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save();

            }
        });

    }

    private void save() {

        boolean cancel = false;
        View focusView = null;
        String cashAmount = txtCashAmount.getText().toString();

        if (TextUtils.isEmpty(cashAmount)) {
            txtCashAmount.setError(getString(R.string.error_field_required));
            focusView = txtCashAmount;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            if (Mode == MODE_NEW) {
                db.open();

                visitor = new Visitor();
                visitor = db.getVisitorWithVisitorID(BaseActivity.getPrefUserMasterId());

                dt = new Date();

                String TransferCode = ServiceTools.getGenerationCode();
                String Price = ServiceTools.MoneyFormatToNumber(txtCashAmount.getText().toString());

                PayableTransfer payableTransfer = new PayableTransfer();
                payableTransfer.setPrice(ServiceTools.toInt(Price));
                payableTransfer.setUserId(BaseActivity.getPrefUserId());
                payableTransfer.setPayerId((int) visitor.getCashCode());
                payableTransfer.setVisitorId((int) BaseActivity.getPrefUserMasterId());
                if (Type == ProjectInfo.Bank_TYPE) {

                    payableTransfer.setReceiverid((int) BankCode);
                    payableTransfer.setTransferType(3);

                } else if (Type == ProjectInfo.Expense_TYPE) {
                    payableTransfer.setReceiverid(0);
                    payableTransfer.setTransferType(4);
                }

                payableTransfer.setTransferDate(LngDate);
                payableTransfer.setTransferCode(ServiceTools.toLong(TransferCode));
                payableTransfer.setTransferAccountClientId(ServiceTools.toLong(TransferCode));
                payableTransfer.setDescription(txtDescription.getText().toString());
                payableTransfer.setMahakId(BaseActivity.getPrefMahakId());
                payableTransfer.setDataBaseId(BaseActivity.getPrefDatabaseId());

                db.AddPayable(payableTransfer);
                setResult(RESULT_OK);
                Clear();
                if (Page == PAGE_ORDER_DETAIL) {
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else
                    finish();

            } else if (Mode == MODE_EDIT) {
                db.open();
                visitor = new Visitor();
                visitor = db.getVisitorWithVisitorID(BaseActivity.getPrefUserMasterId());

                payableTransfer.setUserId(BaseActivity.getPrefUserId());
                payableTransfer.setMahakId(BaseActivity.getPrefMahakId());
                payableTransfer.setDataBaseId(BaseActivity.getPrefDatabaseId());
                payableTransfer.setVisitorId(payableTransfer.getVisitorId());
                payableTransfer.setPayerId((int) visitor.getCashCode());
                payableTransfer.setTransferCode(payableTransfer.getTransferCode());
                payableTransfer.setTransferAccountClientId(payableTransfer.getTransferAccountClientId());
                payableTransfer.setTransferDate(LngDate);
                payableTransfer.setPrice(ServiceTools.toInt(ServiceTools.MoneyFormatToNumber(txtCashAmount.getText().toString())));
                payableTransfer.setDescription(txtDescription.getText().toString().trim());
                if (Type == ProjectInfo.Bank_TYPE) {

                    payableTransfer.setReceiverid((int) BankCode);
                    payableTransfer.setTransferType(3);

                } else if (Type == ProjectInfo.Expense_TYPE) {
                    payableTransfer.setReceiverid(0);
                    payableTransfer.setTransferType(4);
                }
                db.UpdatePayable(payableTransfer);
                setResult(RESULT_OK);
                Clear();
                if (Page == PAGE_ORDER_DETAIL) {
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else
                    finish();
            }
        }
    }

    private void Clear() {
        StrDate = "";
        LngDate = 0;
    }

    private void initialise() {
        btnDatePicker = (Button) findViewById(R.id.btnDatePicker);
        txtCashAmount = (EditText) findViewById(R.id.tvAmount);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        radioButtonExpense = (RadioButton) findViewById(R.id.radioButtonExpense);
        radioButtonBank = (RadioButton) findViewById(R.id.radioButtonBank);
        spnBank = (Spinner) findViewById(R.id.spnBank);
        llspnBank = (LinearLayout) findViewById(R.id.llspnBank);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSave = (Button) findViewById(R.id.btnSave);
        db = new DbAdapter(mContext);
    }

    public String getDayName(int day) {
        String dayName = "";
        if (day == 0)
            day = 7;
        dayName = com.mahak.order.calendar.PersianDate.WeekDayName[day];
        return dayName;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void FillSpinner() {
        db.open();
        arrayBank = new ArrayList<Bank>();
        Visitor visitor = BaseActivity.getVisitor();
        if (visitor != null)
            arrayBank = db.getAllBank(visitor.getBankCode());
        AdapterSpnBank adSpinner = new AdapterSpnBank(mContext, R.layout.item_spinner, arrayBank);
        spnBank.setAdapter(adSpinner);

        //Fill Adapter Spinner________________________________________________________________

        db.close();
    }

    public void FillSpinnerEdit() {

        db.open();
        arrayBank = new ArrayList<Bank>();
        PayableTransfer payableTransfer = db.GetPayable(PayableId);
        arrayBank = db.getAllBank(payableTransfer.getReceiverid());
        AdapterSpnBank adSpinner = new AdapterSpnBank(mContext, R.layout.item_spinner, arrayBank);
        spnBank.setAdapter(adSpinner);
        //Fill Adapter Spinner________________________________________________________________
        db.close();
    }

    private void FillView() {

        db.open();
        double Payment = 0;
        //Read From Database receipt_________________________
        payableTransfer = db.GetPayable(PayableId);
        //Fill Information____________________________________
        LngDate = payableTransfer.getTransferDate();
        StrDate = getDateAndTimeForLong(LngDate);
        txtDescription.setText(payableTransfer.getDescription());
        txtCashAmount.setText("" + payableTransfer.getPrice());
        btnDatePicker.setText(StrDate);

        if (payableTransfer.getTransferType() == 4) {

            radioButtonBank.setChecked(false);
            radioButtonExpense.setChecked(true);
            llspnBank.setVisibility(View.GONE);
        } else {

            radioButtonBank.setChecked(true);
            radioButtonExpense.setChecked(false);
            llspnBank.setVisibility(View.VISIBLE);
            Type = ProjectInfo.Bank_TYPE;

        }
    }

    public class AdapterSpnBank extends ArrayAdapter<Bank> {

        ArrayList<Bank> banks;

        public AdapterSpnBank(Context context, int textViewResourceId, ArrayList<Bank> objects) {
            super(context, textViewResourceId, objects);
            banks = objects;

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
            tvName.setText(banks.get(position).getName());
            BankCode = banks.get(position).getBankCode();

            return row;
        }
    }
}
