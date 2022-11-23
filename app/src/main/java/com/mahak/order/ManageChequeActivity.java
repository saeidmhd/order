package com.mahak.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.common.Bank;
import com.mahak.order.common.Cheque;
import com.mahak.order.common.MyCalendar;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.Visitor;
import com.mahak.order.libs.DatePicker;
import com.mahak.order.storage.DbAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.mahak.order.common.ServiceTools.getDateAndTimeForLong;

@SuppressLint("ValidFragment")
public class ManageChequeActivity extends BaseActivity {


    private static String AMOUNT_KEY = "Amount";
    private static String NUMBER_KEY = "Number";
    private static String BRANCH_KEY = "Branch";
    private static String DESCRIPTION_KEY = "Description";
    private static String STR_DATE_KEY = "StrDate";
    private static String LNG_DATE_KEY = "LngDate";
    private static String TYPE_KEY = "Type";
    private static String BANK_KEY = "Bank";
    private static int Mode;
    private static int PositionArray;
    private static int ClickSpinnerBank = 0;
    private static int ClickSpinnerChequeType = 0;
    private static Boolean Issaved;
    private String Amount, Number, Branch, Description, StrDate;
    private long LngDate;
    private int Type;//PositionBank;

    private String[] array;
    private ArrayList<Bank> arrayBank;
    private Bundle Extras;
    private Date dt;
    private TextView tvDate, tvBranch;
    private Spinner spnChequeType;
    private Spinner spnBank;
    private AutoCompleteTextView acBank;
    private EditText txtNumber, txtCashAmount, txtBranch, txtDescription;
    private Button btnDatePicker, btnSave, btnDelete;
    private LinearLayout ll_branch;
    private DbAdapter db;
    private Context mContext;
    private Bank bank;
    private static final long NoLimit = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_cheque);

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
        TextView tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        tvPageTitle.setText(getString(R.string.str_save_cheque));
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________
        mContext = this;
        initialise();

        Extras = getIntent().getExtras();

        if (savedInstanceState == null) {
            if (Extras != null) {
                Mode = Extras.getInt(MODE_PAGE);
                if (Mode == MODE_EDIT) {
                    PositionArray = Extras.getInt(POSITION_KEY);
                    FillView();
                } else if (Mode == MODE_NEW) {
                    dt = new Date();
                    Type = Extras.getInt(TYPE_KEY);
                    btnDelete.setVisibility(View.GONE);
                    LngDate = dt.getTime();
                    StrDate = getDateAndTimeForLong(LngDate);
                    tvDate.setText(StrDate);
                    if (Type == ProjectInfo.CHEQUE_TYPE)
                        spnChequeType.setSelection(0);
                    else if (Type == ProjectInfo.CASHRECEIPT_TYPE)
                        spnChequeType.setSelection(1);

                    if (Type == ProjectInfo.CASHRECEIPT_TYPE) {
                        ll_branch.setVisibility(View.GONE);
                    } else if (Type == ProjectInfo.CHEQUE_TYPE) {
                        ll_branch.setVisibility(View.VISIBLE);
                    }
                }
                Issaved = false;
            }
        } else {
            Amount = savedInstanceState.getString(AMOUNT_KEY);
            Number = savedInstanceState.getString(NUMBER_KEY);
            Branch = savedInstanceState.getString(BRANCH_KEY);
            Description = savedInstanceState.getString(DESCRIPTION_KEY);
            StrDate = savedInstanceState.getString(STR_DATE_KEY);
            LngDate = savedInstanceState.getLong(LNG_DATE_KEY);
            Type = savedInstanceState.getInt(TYPE_KEY);
            Issaved = true;
            txtCashAmount.setText(Amount);
            txtBranch.setText(Branch);
            txtDescription.setText(Description);
            txtNumber.setText(Number);
            tvDate.setText(getDateAndTimeForLong(LngDate));

            if (Type == ProjectInfo.CHEQUE_TYPE)
                spnChequeType.setSelection(0);
            else if (Type == ProjectInfo.CASHRECEIPT_TYPE)
                spnChequeType.setSelection(1);

        }

        txtNumber.addTextChangedListener(new TextWatcher() {

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
                Number = s.toString();

            }
        });

        txtBranch.addTextChangedListener(new TextWatcher() {

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
                Branch = s.toString();

            }
        });

        txtCashAmount.addTextChangedListener(new TextWatcher() {

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
                Amount = ServiceTools.MoneyFormatToNumber(s.toString());

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

        btnDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MyCalendar myCalendar = new MyCalendar(LngDate, ManageChequeActivity.this, getFragmentManager(), new MyCalendar.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker date) {
                        LngDate = date.getDate().getTime();
                        tvDate.setText(ServiceTools.getDateAndTimeForLong(LngDate));
                    }

                    @Override
                    public void onGregorianSet(int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        LngDate = calendar.getTimeInMillis();
                        tvDate.setText(ServiceTools.getDateAndTimeForLong(LngDate));

                    }
                });
                myCalendar.showDialog();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                if (Type == ProjectInfo.CHEQUE_TYPE) {

                    if (!acBank.getText().toString().equals("")) {
                        bank = new Bank();
                        bank.setId(0l);
                        bank.setName(acBank.getText().toString());
                    } else {
                        bank = null;
                    }

                }
                boolean cancel = false;
                View focusView = null;
                String cashAmount = txtCashAmount.getText().toString();
                String number = txtNumber.getText().toString();

                if (TextUtils.isEmpty(cashAmount)) {
                    txtCashAmount.setError(getString(R.string.error_field_required));
                    focusView = txtCashAmount;
                    cancel = true;
                }
                if (TextUtils.isEmpty(number)) {
                    txtNumber.setError(getString(R.string.error_field_required));
                    focusView = txtNumber;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    if (bank != null) {
                        String amount = txtCashAmount.getText().toString();
                        if (Type == ProjectInfo.CHEQUE_TYPE) {
                            if (!visitorChequeCredit(ServiceTools.MoneyFormatToNumber(amount))) {
                                Toast.makeText(mContext, R.string.validity_of_check, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        if (Mode == MODE_NEW) {
                            Cheque cheque = new Cheque();
                            cheque.setAmount(ServiceTools.toDouble(amount));
                            if (Type == ProjectInfo.CHEQUE_TYPE) {
                                cheque.setBankId(null);
                                cheque.setBranch(txtBranch.getText().toString().trim());
                            } else if (Type == ProjectInfo.CASHRECEIPT_TYPE) {
                                cheque.setBranch("");
                                cheque.setBankId(String.valueOf(bank.getBankId()));
                            }
                            cheque.setDescription(txtDescription.getText().toString().trim());
                            cheque.setNumber(txtNumber.getText().toString().trim());
                            cheque.setModifyDate(date.getTime());
                            // cheque.setChequeClientId(ServiceTools.toLong(ServiceTools.getGenerationCode()));
                            cheque.setDate(LngDate);
                            if (bank != null) {
                                cheque.setBankName(bank.getName());
                            }
                            cheque.setType(Type);
                            ManageReceiptActivity.arrayCheque.add(cheque);
                            setResult(RESULT_OK);
                            ClickSpinnerBank = 0;
                            ClickSpinnerChequeType = 0;
                            finish();
                        } else if (Mode == MODE_EDIT) {
                            ManageReceiptActivity.arrayCheque.get(PositionArray).setAmount(ServiceTools.toDouble(amount));
                            if (Type == ProjectInfo.CHEQUE_TYPE)
                                ManageReceiptActivity.arrayCheque.get(PositionArray).setBranch(txtBranch.getText().toString().trim());
                            else if (Type == ProjectInfo.CASHRECEIPT_TYPE)
                                ManageReceiptActivity.arrayCheque.get(PositionArray).setBranch("");
                            ManageReceiptActivity.arrayCheque.get(PositionArray).setDescription(txtDescription.getText().toString().trim());
                            ManageReceiptActivity.arrayCheque.get(PositionArray).setNumber(txtNumber.getText().toString());
                            ManageReceiptActivity.arrayCheque.get(PositionArray).setType(Type);
                            ManageReceiptActivity.arrayCheque.get(PositionArray).setModifyDate(date.getTime());
                            ManageReceiptActivity.arrayCheque.get(PositionArray).setDate(LngDate);
                            if (bank != null) {
                                if (Type == ProjectInfo.CHEQUE_TYPE)
                                    ManageReceiptActivity.arrayCheque.get(PositionArray).setBankId(null);
                                else if (Type == ProjectInfo.CASHRECEIPT_TYPE)
                                    ManageReceiptActivity.arrayCheque.get(PositionArray).setBankId(String.valueOf(bank.getBankId()));

                                ManageReceiptActivity.arrayCheque.get(PositionArray).setBankName(bank.getName());
                            }
                            setResult(RESULT_OK);
                            ClickSpinnerBank = 0;
                            ClickSpinnerChequeType = 0;
                            finish();
                        }
                    } else
                        Toast.makeText(mContext, getString(R.string.str_message_dont_select_bank), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ManageReceiptActivity.arrayCheque.remove(PositionArray);
                setResult(RESULT_OK);
                ClickSpinnerBank = 0;
                ClickSpinnerChequeType = 0;
                finish();
            }
        });

        spnBank.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (ClickSpinnerBank > 0) {
                    bank = (Bank) parent.getItemAtPosition(position);
                }
                ClickSpinnerBank++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        acBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acBank.showDropDown();
            }
        });

//        spnBank.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            spnBank.showDropDown();
//                        }
//                    }, 1500);
//                }
//            }
//        });

        spnChequeType.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (ClickSpinnerChequeType > 0) {
                    if (position == 0)
                        Type = ProjectInfo.CHEQUE_TYPE;
                    else if (position == 1)
                        Type = ProjectInfo.CASHRECEIPT_TYPE;

                    FillSpinner();
                    //Default select first bank  in arraybank
                    bank = null;
                    if (arrayBank.size() > 0)
                        bank = arrayBank.get(0);

                    if (Type == ProjectInfo.CASHRECEIPT_TYPE) {
                        ll_branch.setVisibility(View.GONE);
                    } else if (Type == ProjectInfo.CHEQUE_TYPE) {
                        ll_branch.setVisibility(View.VISIBLE);
                    }
                }
                ClickSpinnerChequeType++;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        FillSpinner();

        SelectedSpinnerBank();
    }//End of OnCreate

    private boolean visitorChequeCredit(String amount) {
        db.open();
        double TotalCheque = 0;
        Visitor visitor = db.getVisitor();
        if (visitor.getChequeCredit() == NoLimit)
            return true;

        TotalCheque = db.getTotalChequeReceipt();

        if (Mode == MODE_EDIT && TotalCheque != 0) {
            Cheque cheque = db.GetCheque(ManageReceiptActivity.arrayCheque.get(PositionArray).getId());
            TotalCheque = TotalCheque - cheque.getAmount();
        }

        return TotalCheque + ServiceTools.toDouble(amount) <= visitor.getChequeCredit();
    }

    /**
     * Initializing Variables
     */
    private void initialise() {
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvBranch = (TextView) findViewById(R.id.tvBranch);
        txtCashAmount = (EditText) findViewById(R.id.tvAmount);
        txtNumber = (EditText) findViewById(R.id.txtNumber);
        txtBranch = (EditText) findViewById(R.id.txtBranch);
        ll_branch = (LinearLayout) findViewById(R.id.ll_branch);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        spnBank = (Spinner) findViewById(R.id.spnBank);
        acBank = (AutoCompleteTextView) findViewById(R.id.acBank);
        spnChequeType = (Spinner) findViewById(R.id.spnChequeType);
        btnDatePicker = (Button) findViewById(R.id.btnDatePicker);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        acBank.setThreshold(1);

        array = getResources().getStringArray(R.array.array_banks_name);


        db = new DbAdapter(mContext);
        ClickSpinnerBank = 0;
        ClickSpinnerChequeType = 0;

        if (Type == ProjectInfo.CASHRECEIPT_TYPE) {
            ll_branch.setVisibility(View.GONE);
        } else if (Type == ProjectInfo.CHEQUE_TYPE) {
            ll_branch.setVisibility(View.VISIBLE);
        }

        //setBankName();
    }

    private void setBankName() {
        String sBanks = ServiceTools.getKeyFromSharedPreferences(mContext, ProjectInfo._json_key_banks);
        if (sBanks.equals(""))
            return;
        try {
            JSONArray jArray = new JSONArray(sBanks);
            array = new String[jArray.length()];
            for (int i = 0; i < jArray.length(); i++) {
                array[i] = jArray.optString(i);
            }
        } catch (JSONException e) {
            ServiceTools.logToFireBase(e);;
        }
    }

    /**
     * Fill TextView
     */
    private void FillView() {
        Type = ManageReceiptActivity.arrayCheque.get(PositionArray).getType();
        LngDate = ManageReceiptActivity.arrayCheque.get(PositionArray).getDate();
        bank = new Bank();
        bank.setName(ManageReceiptActivity.arrayCheque.get(PositionArray).getBankName());
        if (Type == ProjectInfo.CASHRECEIPT_TYPE)
            bank.setId(ServiceTools.toLong(ManageReceiptActivity.arrayCheque.get(PositionArray).getBankId()));
        //Fill Textview_______________________________________________________________
        txtCashAmount.setText(ServiceTools.formatPrice(ManageReceiptActivity.arrayCheque.get(PositionArray).getAmount()));
        txtBranch.setText(ManageReceiptActivity.arrayCheque.get(PositionArray).getBranch());
        txtDescription.setText(ManageReceiptActivity.arrayCheque.get(PositionArray).getDescription());
        txtNumber.setText(ManageReceiptActivity.arrayCheque.get(PositionArray).getNumber());
        tvDate.setText(getDateAndTimeForLong(LngDate));
        //Check Type for spnChequeType________________________________________________
        if (Type == ProjectInfo.CHEQUE_TYPE) {
            spnChequeType.setSelection(0);
            ll_branch.setVisibility(View.VISIBLE);
        } else if (Type == ProjectInfo.CASHRECEIPT_TYPE) {
            spnChequeType.setSelection(1);
            ll_branch.setVisibility(View.GONE);
        }
    }

    /**
     * Fill Spinner Bank
     */
    public void FillSpinner() {
        db.open();
        arrayBank = new ArrayList<Bank>();
        //Check Type For Fill Bank __________________________________________________________
        if (Type == ProjectInfo.CHEQUE_TYPE) {
            acBank.setVisibility(View.VISIBLE);
            spnBank.setVisibility(View.GONE);
            //Read From array
            ArrayAdapter<String> adSpinner = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, array);
//        AdapterSpnBank adSpinner = new AdapterSpnBank(mContext, R.layout.item_spinner, arrayBank);
            acBank.setAdapter(adSpinner);
//            for (int i = 0; i < array.length; i++) {
//                Bank bank = new Bank();
//                bank.setName(array[i]);
//                bank.setId(ServiceTools.toLong(i));
//                arrayBank.add(bank);
//            }
        } else if (Type == ProjectInfo.CASHRECEIPT_TYPE) {
            //Read From Database
            acBank.setVisibility(View.GONE);
            spnBank.setVisibility(View.VISIBLE);

            Visitor visitor = BaseActivity.getVisitor();
            if (visitor != null) {
                arrayBank = db.getAllBank(visitor.getBankCode());
            } else {
                arrayBank = db.getAllBank();
            }
            AdapterSpnBank adSpinner = new AdapterSpnBank(mContext, R.layout.item_spinner, arrayBank);
//        AdapterSpnBank adSpinner = new AdapterSpnBank(mContext, R.layout.item_spinner, arrayBank);
            spnBank.setAdapter(adSpinner);
        }
        //Fill Adapter Spinner________________________________________________________________

        db.close();
    }

    /**
     * Select item Spinner Bank
     */
    public void SelectedSpinnerBank() {
        //Select item spinner Bank _____________________________________________________________
        if (Issaved == false) {
            if (Mode == MODE_NEW) {
                //spnBank.setSelection(0);
                if (arrayBank.size() > 0)
                    bank = arrayBank.get(0);
            } else if (Mode == MODE_EDIT) {
                if (ManageReceiptActivity.arrayCheque.size() > 0) {
                    if (Type == ProjectInfo.CASHRECEIPT_TYPE) {
                        for (int i = 0; i < arrayBank.size(); i++) {
                            if (ManageReceiptActivity.arrayCheque.get(PositionArray).getBankId().equals(String.valueOf(arrayBank.get(i).getId()))) {
                                spnBank.setSelection(i);
                                break;
                            }
                        }
                    } else {
                        acBank.setText(ManageReceiptActivity.arrayCheque.get(PositionArray).getBankName());
                    }
                }
            }
        } else {
            if (bank != null) {
                if (Type == ProjectInfo.CASHRECEIPT_TYPE) {
                    for (int i = 0; i < arrayBank.size(); i++) {
                        if (bank.getId() == arrayBank.get(i).getId()) {
                            spnBank.setSelection(i);
                            break;
                        }
                    }
                } else {
                    acBank.setText(bank.getName());
                }
            }
        }
    }

    public class AdapterSpnBank extends ArrayAdapter<Bank> {

        ArrayList<Bank> Objects;

        public AdapterSpnBank(Context context, int textViewResourceId, ArrayList<Bank> objects) {
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(AMOUNT_KEY, Amount);
        outState.putString(NUMBER_KEY, Number);
        outState.putString(BRANCH_KEY, Branch);
        outState.putString(DESCRIPTION_KEY, Description);
        outState.putString(STR_DATE_KEY, StrDate);
        outState.putInt(TYPE_KEY, Type);
        outState.putLong(LNG_DATE_KEY, LngDate);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        Amount = savedInstanceState.getString(AMOUNT_KEY);
        Number = savedInstanceState.getString(NUMBER_KEY);
        Branch = savedInstanceState.getString(BRANCH_KEY);
        Description = savedInstanceState.getString(DESCRIPTION_KEY);
        StrDate = savedInstanceState.getString(STR_DATE_KEY);
        Type = savedInstanceState.getInt(TYPE_KEY);
        LngDate = savedInstanceState.getLong(LNG_DATE_KEY);
    }

    @Override
    public void onBackPressed() {

        ClickSpinnerBank = 0;
        ClickSpinnerChequeType = 0;
        Issaved = false;
        finish();

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                ClickSpinnerBank = 0;
                ClickSpinnerChequeType = 0;
                Issaved = false;
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
