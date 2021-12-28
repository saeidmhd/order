package com.mahak.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.mahak.order.common.Cheque;
import com.mahak.order.common.Customer;
import com.mahak.order.common.GPSTracker;
import com.mahak.order.common.MyCalendar;
import com.mahak.order.common.NonRegister;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.Reasons;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.libs.DatePicker;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.storage.DbSchema;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.mahak.order.common.ServiceTools.getDateAndTimeForLong;

@SuppressLint("ValidFragment")
public class NonRegisterActivity extends BaseActivity {

    private static int REQUEST_CUSTOMER_LIST = 1;
    private static String STR_DATE_KEY = "StrDate";
    private static String DESCRIPTION_KEY = "Description";
    private static String MARKET_KEY = "Market";
    private static String LNG_DATE_KEY = "LngDate";
    private static String CODE_KEY = "Code";
    private static int Page;
    private static int Mode;
    private static int ReasonCode = 0;
    private ArrayList<Reasons> arrayReasons = new ArrayList<>();
    private ArrayList<String> arrayTitles = new ArrayList<>();
    private static long lngDate;
    private static long CustomerClientId;
    private static int CustomerId;
    private static String Code = ProjectInfo.DONT_CODE;

    private EditText txtCustomerName, txtDescription, txtTrackingCode;
    private TextView tvDate;
    private Button btnSave, btnDatePicker, btnSelectCustomer, btnGetLocation;
    private Date dt;
    private String CustomerName, StrDate;
    private EditText txtLongitude, txtLatitude;
    private Context mContext;
    private DbAdapter db;
    private long nonRegisterID;
    private NonRegister nonRegister;
    private Spinner spnNonRegisterCause;

    public static long UserId = 0;
    private static String Name = "";
    private static String LastName = "";
    private static String Tell = "";
    private static String Address = "";
    private static String StrLongitude = "";
    private static String StrLatitude = "";
    private static String Description = "";
    private static String MarketName = "";

    private EditText txtName, txtMarketName, txtTell, txtAddress, txtLastName;
    private LinearLayout llGuestDetails;
    private GPSTracker gpsTracker;
    private ArrayList<LatLng> positions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_register);

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        TextView tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        tvPageTitle.setText(getString(R.string.str_save_non_register));
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________

        mContext = this;
        initialise();

        Code = ServiceTools.getGenerationCode();
        Bundle extras = getIntent().getExtras();
        db.open();
        FillArrayReasonCode();

        if (savedInstanceState == null) {
            if (extras != null) {
                Page = extras.getInt(PAGE);
                Mode = extras.getInt(MODE_PAGE);
                CustomerId = extras.getInt(CUSTOMERID_KEY);
                CustomerClientId = extras.getLong(CUSTOMER_CLIENT_ID_KEY);

                if (Page == PAGE_DASHBORD) {
                    dt = new Date();
                    lngDate = dt.getTime();
                    StrDate = getDateAndTimeForLong(lngDate);
                    tvDate.setText(StrDate);
                } else if (Page == PAGE_NON_REGISTER_DETAIL) {
                    dt = new Date();
                    lngDate = dt.getTime();
                    StrDate = getDateAndTimeForLong(lngDate);
                    tvDate.setText(StrDate);
                    Code = extras.getString(CODE_KEY);
                    txtTrackingCode.setText(Code);
                } else if (Page == PAGE_NON_REGISTER_LIST) {
                    if (Mode == MODE_EDIT) {
                        nonRegisterID = extras.getLong(ID);
                        FillView();
                    } else if (Mode == MODE_NEW) {
                        selectCustomer();
                        dt = new Date();
                        lngDate = dt.getTime();
                        StrDate = getDateAndTimeForLong(lngDate);
                        tvDate.setText(StrDate);
                        txtTrackingCode.setText(Code);

                    }
                }
            }
        } else {
            CustomerName = savedInstanceState.getString(CUSTOMER_NAME_KEY);
            MarketName = savedInstanceState.getString(MARKET_KEY);
            StrDate = savedInstanceState.getString(STR_DATE_KEY);
            Description = savedInstanceState.getString(DESCRIPTION_KEY);
            Code = savedInstanceState.getString(CODE_KEY);
            txtCustomerName.setText(CustomerName);
            txtMarketName.setText(MarketName);
            tvDate.setText(StrDate);
            txtDescription.setText(Description);
            txtTrackingCode.setText(Code);

        }

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

        txtDescription.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Description = s.toString();
            }
        });

        txtName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Name = s.toString();
            }
        });

        txtLastName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                LastName = s.toString();
            }
        });

        txtTell.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Tell = s.toString();
            }
        });

        txtAddress.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Address = s.toString();
            }
        });

        txtLatitude.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                StrLatitude = s.toString();
            }
        });

        txtLongitude.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                StrLongitude = s.toString();
            }
        });


        btnDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                MyCalendar myCalendar = new MyCalendar(lngDate, NonRegisterActivity.this, getFragmentManager(), new MyCalendar.OnDateSetListener() {
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
                        tvDate.setText(ServiceTools.getDateAndTimeForLong(lngDate));
                    }
                });
                myCalendar.showDialog();

            }
        });

        btnSelectCustomer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectCustomer();

            }
        });

        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double Latitude;
                double Longitude;
                gpsTracker = new GPSTracker(mContext);
                if (gpsTracker.canGetLocation()) {
                    Latitude = gpsTracker.getLatitude();
                    Longitude = gpsTracker.getLongitude();

                    if (Latitude == 0 && Longitude == 0)
                        Toast.makeText(mContext, getResources().getString(R.string.str_message_dont_connect_gps), Toast.LENGTH_SHORT).show();
                    else {
                        positions = new ArrayList<>();

                        LatLng pos = new LatLng(Latitude, Longitude);
                        positions = new ArrayList<>();
                        positions.add(pos);

                        StrLatitude = String.valueOf(Latitude);
                        StrLongitude = String.valueOf(Longitude);
                        txtLatitude.setText(String.valueOf(Latitude));
                        txtLongitude.setText(String.valueOf(Longitude));

                        Intent intent = new Intent(mContext, MapViewActivity.class);
                        intent.putParcelableArrayListExtra(COORDINATE, positions);
                        startActivity(intent);
                    }
                } else
                    Toast.makeText(mContext, getResources().getString(R.string.str_message_dont_connect_gps), Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });

        spnNonRegisterCause.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ReasonCode = arrayReasons.get(position).getReturnReasonCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }// End of OnCreate

    private void selectCustomer() {
        Intent intent = new Intent(mContext, PeopleListActivity.class);
        intent.putExtra(PAGE, PAGE_ADD_NON_REGISTER);
        startActivityForResult(intent, REQUEST_CUSTOMER_LIST);
    }

    private void FillArrayReasonCode() {

        arrayReasons = new ArrayList<>();
        arrayReasons = db.getAllReasonByType(BaseActivity.NonRegisterReason);

        for (Reasons reasons : arrayReasons) {
            arrayTitles.add(reasons.getName());
        }

        ArrayAdapter<String> adp = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayTitles);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNonRegisterCause.setAdapter(adp);
        if (arrayReasons.size() > 0)
            spnNonRegisterCause.setSelection(0);

    }


    /**
     * Read From Database And Fill AdapterCheque And Edittext
     */
    private void FillView() {

        //Read From Database receipt_________________________
        nonRegister = db.GetNonRegister(nonRegisterID);
        CustomerId = (int) nonRegister.getPersonId();
        CustomerClientId = nonRegister.getPersonClientId();
        if (CustomerId == ProjectInfo.CUSTOMERID_GUEST) {
            Customer customer = db.getCustomerWithPersonClientId(CustomerClientId);
            txtCustomerName.setText(customer.getName());
            txtMarketName.setText(customer.getOrganization());
        } else {
            Customer customer = db.getCustomerWithPersonId(CustomerId);
            txtCustomerName.setText(customer.getName());
            txtMarketName.setText(customer.getOrganization());
        }
        //Fill Information____________________________________
        StrDate = getDateAndTimeForLong(nonRegister.getNotRegisterDate());
        Description = nonRegister.getDescription();
        txtDescription.setText(Description);
        txtName.setText(Name);
        txtLastName.setText(LastName);
        txtMarketName.setText(MarketName);
        txtTell.setText(Tell);
        txtAddress.setText(Address);
        txtLatitude.setText(StrLatitude);
        txtLongitude.setText(StrLongitude);

        tvDate.setText(StrDate);
        spnNonRegisterCause.setSelection(getCategoryPos(nonRegister.getReasonCode()));

        if (!nonRegister.getCode().equals(ProjectInfo.DONT_CODE)) {
            Code = nonRegister.getCode();
            txtTrackingCode.setText(nonRegister.getCode());
        } else
            Code = ProjectInfo.DONT_CODE;
        //Read arrayCheque From Database And Set Adapter_______
    }

    private int getCategoryPos(int masterID) {
        int i = 0;
        for (Reasons reasons : arrayReasons) {
            if (reasons.getReturnReasonCode() == masterID)
                return i;
            else i++;
        }
        return i;
    }


    /**
     * Initializing Variables
     */
    private void initialise() {

        txtCustomerName = (EditText) findViewById(R.id.txtCustomerName);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        txtMarketName = (EditText) findViewById(R.id.txtMarketName);
        txtTrackingCode = (EditText) findViewById(R.id.txtTrackingCode);
        tvDate = (TextView) findViewById(R.id.tvDate);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDatePicker = (Button) findViewById(R.id.btnDatePicker);
        btnSelectCustomer = (Button) findViewById(R.id.btnSelectCustomer);
        spnNonRegisterCause = (Spinner) findViewById(R.id.spnNonRegisterCause);
        txtName = (EditText) findViewById(R.id.txtFirstName);
        txtTell = (EditText) findViewById(R.id.txtTell);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtLatitude = (EditText) findViewById(R.id.txtLatitude);
        txtLongitude = (EditText) findViewById(R.id.txtLongitude);
        llGuestDetails = (LinearLayout) findViewById(R.id.llGuestDetails);
        btnGetLocation = (Button) findViewById(R.id.btnGetGeographicallocation);

        db = new DbAdapter(mContext);
    }

    /**
     * Save Information In database
     */
    private void Save() {
        dt = new Date();
        long result;

        //Validate____________________________________________
        //_____________________________________________________________

        if (Mode == MODE_NEW) {
            nonRegister = new NonRegister();
            nonRegister.setPersonId(CustomerId);
            nonRegister.setPersonClientId(CustomerClientId);
            nonRegister.setDescription(txtDescription.getText().toString());
            nonRegister.setVisitorId((int) BaseActivity.getPrefUserId());
            nonRegister.setModifyDate(dt.getTime());
            nonRegister.setNotRegisterDate(new Date().getTime());
            nonRegister.setMahakId(BaseActivity.getPrefMahakId());
            nonRegister.setDatabaseId(BaseActivity.getPrefDatabaseId());
            nonRegister.setCode(Code);
            nonRegister.setNotRegisterClientId(ServiceTools.toLong(Code));
            nonRegister.setReasonCode(ReasonCode);
            nonRegister.setCustomerName(txtCustomerName.getText().toString());
            nonRegister.setMarketName(txtMarketName.getText().toString());
            result = db.AddNonRegister(nonRegister);
            if (result > 0) {
                long nonRegisterId = db.getMax(DbSchema.NonRegisterSchema.TABLE_NAME, DbSchema.NonRegisterSchema.COLUMN_ID);
                setResult(RESULT_OK);
                Intent intent = new Intent(mContext, NonRegisterDetailActivity.class);
                intent.putExtra(PAGE, PAGE_ADD_NON_REGISTER);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_NON_REGISTER);
                intent.putExtra(ID, nonRegisterId);
                startActivity(intent);
                Clear();


            }
        } else if (Mode == MODE_EDIT) {
            nonRegister.setPersonId(CustomerId);
            nonRegister.setPersonClientId(CustomerClientId);
            nonRegister.setVisitorId((int) BaseActivity.getPrefUserId());
            nonRegister.setMahakId(BaseActivity.getPrefMahakId());
            nonRegister.setDatabaseId(BaseActivity.getPrefDatabaseId());
            nonRegister.setModifyDate(dt.getTime());
            nonRegister.setNotRegisterDate(new Date().getTime());
            nonRegister.setDescription(txtDescription.getText().toString());
            nonRegister.setCode(Code);
            nonRegister.setNotRegisterClientId(ServiceTools.toLong(Code));
            nonRegister.setReasonCode(ReasonCode);
            nonRegister.setCustomerName(txtCustomerName.getText().toString());

            db.UpdateNonRegister(nonRegister);
            setResult(RESULT_OK);
            Clear();
            //___________________________________________________
            if (Page == PAGE_NON_REGISTER_DETAIL) {
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else
                finish();
        }
    }

    /**
     * Reset Values
     */
    private void Clear() {
        CustomerName = "";
        StrDate = "";
        Description = "";
        MarketName = "";
        Code = ProjectInfo.DONT_CODE;
        lngDate = 0;
    }

    private class AdapterCheque extends ArrayAdapter<Cheque> {
        Activity mContext;

        public AdapterCheque(Activity context, ArrayList<Cheque> array) {
            super(context, android.R.layout.simple_list_item_1, array);
            mContext = context;
        }

        class Holder {
            private TextView tvType, tvBank, tvAmount, tvNumber, tvDate;

            Holder(View view) {
                tvType = (TextView) view.findViewById(R.id.tvChequeType);
                tvBank = (TextView) view.findViewById(R.id.tvBank);
                tvAmount = (TextView) view.findViewById(R.id.txtAmount);
                tvNumber = (TextView) view.findViewById(R.id.tvNumber);
                tvDate = (TextView) view.findViewById(R.id.tvDate);
            }

            void Populate(Cheque cheque) {
                if (cheque.getType() == ProjectInfo.CHEQUE_TYPE)
                    tvType.setText(getResources().getString(R.string.str_cheque_type));
                else if (cheque.getType() == ProjectInfo.CASHRECEIPT_TYPE)
                    tvType.setText(getResources().getString(R.string.str_cash_receipt_type));
                tvBank.setText(cheque.getBankName());
                tvAmount.setText(ServiceTools.formatPrice(cheque.getAmount()));
                tvNumber.setText(cheque.getNumber());
            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowview = convertView;
            Holder holder;
            LayoutInflater inflater;
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
        outState.putString(DESCRIPTION_KEY, Description);
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
        Description = savedInstanceState.getString(DESCRIPTION_KEY);
        Code = savedInstanceState.getString(CODE_KEY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CUSTOMER_LIST) {
                CustomerId = data.getIntExtra(CUSTOMERID_KEY, 0);
                CustomerClientId = data.getLongExtra(CUSTOMER_CLIENT_ID_KEY, 0);

                if (CustomerId == ProjectInfo.CUSTOMERID_GUEST) {
                    Customer customer = db.getCustomerWithPersonClientId(CustomerClientId);
                    CustomerName = customer.getName();
                    txtCustomerName.setText(customer.getName());
                } else {
                    Customer customer = db.getCustomerWithPersonId(CustomerId);
                    CustomerName = customer.getName();
                    txtCustomerName.setText(customer.getName());
                }
            }
        } else
            finish();
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
                Clear();
                finish();
                break;
        }
        return (true);
    }

    @Override
    public void onBackPressed() {
        Clear();
        finish();
    }

}
