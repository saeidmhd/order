package com.mahak.order;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.mahak.order.common.CityZone_Extra_Data;
import com.mahak.order.common.Customer;
import com.mahak.order.common.CustomerGroup;
import com.mahak.order.common.GPSTracker;
import com.mahak.order.common.Person_Extra_Data;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;
import java.util.List;

public class ManageCustomerActivity extends BaseActivity {

    private static int PICK_CONTACT_REQUEST = 1;
    private static final Uri URI = ContactsContract.Contacts.CONTENT_URI;
    private static final Uri PURI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    private static final Uri SURI = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
    private static final Uri DURI = ContactsContract.Data.CONTENT_URI;

    private static final String CID = ContactsContract.Contacts._ID;
    private static final String PID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    private static final String SID = ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID;
    private static final String OID = ContactsContract.CommonDataKinds.Organization.CONTACT_ID;

    private static final String DNAME = ContactsContract.Contacts.DISPLAY_NAME;
    private static final String HPN = ContactsContract.Contacts.HAS_PHONE_NUMBER;

    private static final String SADDRESS = ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS;
    private static final String STYPE = ContactsContract.CommonDataKinds.StructuredPostal.TYPE;
    private static final int SHOME = ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME;

    private static final String OCOMPANY = ContactsContract.CommonDataKinds.Organization.COMPANY;
    private static final String DMIMETYPE = ContactsContract.Data.MIMETYPE;
    private static final String OITEM_TYPE = ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE;

    private static final String PNUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private static final String PTYPE = ContactsContract.CommonDataKinds.Phone.TYPE;
    private static final int PMOBILE = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
    private static final int PHOME = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;

    private Context mContext;
    private Activity mActivity;
    private DbAdapter db;

    private EditText
            txtCustomerName,
            txtMarketName,
            txtRemained,
            txtTell,
            txtMobile,
            txtAddress,
            txtZone,
            txtShift,
            txtLongitude,
            txtLatitude,
            txtCredit,
            txtInvoiceAVGAmount,
            txtInvoiceAVGDiscount,
            txtLastInvoiceAmount,
            txtLastInvoiceDate,
            txtAvailableChequeAmount,
            txtAvailableChequeCount,
            txtInReceiptChequeAmount,
            txtInReceiptChequeCount,
            txtSpentChequeAmount,
            txtSpentChequeCount,
            txtReturnChequeAmount,
            txtReturnChequeCount;

    private Spinner spnCustomerGroup, spnCity, spnState, spnCustomerType;
    private Button btnGetLoction, btnSave, btnSaveAndNew, btnContact, btnShowlocation;
    private Bundle Extras;
    private static int Mode = 0;
    private long CustomerGroupId;
    private int personId;
    private long personClientId;
    private Customer customer;
    private Person_Extra_Data extraData;
    private ArrayList<CustomerGroup> arrayCustomerGroup;
    private ArrayList<CityZone_Extra_Data> states;
    private String StateName, CityName,cityCode = "";
    private boolean FirstLoadspnState;
    private GPSTracker gpsTracker;
    private ArrayList<LatLng> positions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_customer);

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
        TextView tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        tvPageTitle.setText(getString(R.string.str_detail_customer));
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________
        mContext = this;
        mActivity = this;

        Extras = getIntent().getExtras();
        if (Extras != null) {
            Mode = Extras.getInt(MODE_PAGE);
        }

        initialise();
        Fillspinner();

        if (Mode == MODE_EDIT) {
            personId = Extras.getInt(CUSTOMERID_KEY);
            personClientId = Extras.getLong(CUSTOMER_CLIENT_ID_KEY);

        }

        FillValues();
        ///////////////////////////////////////////////////////
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveInDb();
                setResult(RESULT_OK);
                finish();
            }
        });
        btnSaveAndNew.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SaveInDb();
                Clear();
            }
        });
        btnGetLoction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double Latitude = 0;
                double Longitude = 0;
                gpsTracker = new GPSTracker(mContext);
                if (gpsTracker.canGetLocation()) {
                    Latitude = gpsTracker.getLatitude();
                    Longitude = gpsTracker.getLongitude();

                    if (Latitude == 0 && Longitude == 0)
                        Toast.makeText(mContext, getResources().getString(R.string.str_message_dont_connect_gps), Toast.LENGTH_SHORT).show();
                    else {
                        db.open();
                        db.UpdateLocationCustomer(customer.getId(), String.valueOf(Latitude), String.valueOf(Longitude));

                        LatLng pos = new LatLng(Latitude, Longitude);
                        positions = new ArrayList<>();
                        positions.add(pos);

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
        btnShowlocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                LatLng pos = new LatLng(ServiceTools.RegulartoDouble(txtLatitude.getText().toString()), ServiceTools.RegulartoDouble(txtLongitude.getText().toString()));
                positions = new ArrayList<>();
                positions.add(pos);

                Intent intent = new Intent(mContext, MapViewActivity.class);
                intent.putParcelableArrayListExtra(COORDINATE, positions);
                startActivity(intent);
            }
        });


        spnCustomerGroup.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomerGroup customergoup = (CustomerGroup) parent.getItemAtPosition(position);
                CustomerGroupId = customergoup.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        spnCustomerType.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)// CustomerTest type = Incalculable
                {
                    txtRemained.setEnabled(false);
                    txtRemained.setText(ServiceTools.formatPrice(0));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        spnState.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (FirstLoadspnState) {
                    CityZone_Extra_Data cityZone_extra_data = states.get(position);
                    ArrayList<CityZone_Extra_Data> cityWithZoneCodeList = db.cityWithZoneCode(cityZone_extra_data.getZoneCode());
                    AdapterSpnCity adCity = new AdapterSpnCity(mActivity, cityWithZoneCodeList);
                    spnCity.setAdapter(adCity);
                    spnCity.setSelection(0);
                    StateName = cityZone_extra_data.getZoneName();
                    cityCode = String.valueOf(cityZone_extra_data.getZoneCode());
                }
                FirstLoadspnState = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnCity.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CityZone_Extra_Data cityZoneExtraData = (CityZone_Extra_Data) parent.getItemAtPosition(position);
                CityName = cityZoneExtraData.getZoneName();
                cityCode = String.valueOf(cityZoneExtraData.getZoneCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        btnContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, URI);
                startActivityForResult(intent, PICK_CONTACT_REQUEST);

            }
        });
    }//End of OnCreate

    /**
     * Initializing Variables
     */
    private void initialise() {

        //Find Views
        txtCustomerName = (EditText) findViewById(R.id.txtCustomerName);
        txtMarketName = (EditText) findViewById(R.id.txtMarketName);
        txtRemained = (EditText) findViewById(R.id.txtRemained);
        txtTell = (EditText) findViewById(R.id.txtTell);
        txtMobile = (EditText) findViewById(R.id.txtMobile);
        txtCredit = (EditText) findViewById(R.id.txtCredit);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtZone = (EditText) findViewById(R.id.txtZone);
        txtShift = (EditText) findViewById(R.id.txtShift);

        txtInvoiceAVGAmount = (EditText) findViewById(R.id.txtInvoiceAVGAmount);
        txtInvoiceAVGDiscount = (EditText) findViewById(R.id.txtInvoiceAVGDiscount);
        txtLastInvoiceAmount = (EditText) findViewById(R.id.txtLastInvoiceAmount);
        txtLastInvoiceDate = (EditText) findViewById(R.id.txtLastInvoiceDate);
        txtAvailableChequeAmount = (EditText) findViewById(R.id.txtAvailableChequeAmount);
        txtAvailableChequeCount = (EditText) findViewById(R.id.txtAvailableChequeCount);
        txtInReceiptChequeAmount = (EditText) findViewById(R.id.txtInReceiptChequeAmount);
        txtInReceiptChequeCount = (EditText) findViewById(R.id.txtInReceiptChequeCount);
        txtSpentChequeAmount = (EditText) findViewById(R.id.txtSpentChequeAmount);
        txtSpentChequeCount = (EditText) findViewById(R.id.txtSpentChequeCount);
        txtReturnChequeAmount = (EditText) findViewById(R.id.txtReturnChequeAmount);
        txtReturnChequeCount = (EditText) findViewById(R.id.txtReturnChequeCount);

        txtLatitude = (EditText) findViewById(R.id.txtLatitude);
        txtLongitude = (EditText) findViewById(R.id.txtLongitude);

        btnGetLoction = (Button) findViewById(R.id.btnGetGeographicallocation);
        btnShowlocation = (Button) findViewById(R.id.btnShowGeographicallocation);
        spnCustomerGroup = (Spinner) findViewById(R.id.spnCustomerGroup);
        spnCity = (Spinner) findViewById(R.id.spnCity);
        spnState = (Spinner) findViewById(R.id.spnState);
        spnCustomerType = (Spinner) findViewById(R.id.spnCustomerType);
        spnCustomerType.setEnabled(false);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSaveAndNew = (Button) findViewById(R.id.btnSaveAndNew);
        btnContact = (Button) findViewById(R.id.btnContact);
        //___________________________________________________________________
        if (Mode == MODE_EDIT)
            btnSaveAndNew.setVisibility(View.GONE);
        else if (Mode == MODE_NEW)
            btnSaveAndNew.setVisibility(View.VISIBLE);
        //______________________________________________________________________
        //btnSave.setVisibility(View.GONE);
        btnSaveAndNew.setVisibility(View.GONE);
        //______________________________________________________________________
        db = new DbAdapter(mContext);
        customer = new Customer();
        extraData = new Person_Extra_Data();
        FirstLoadspnState = false;
        //________________________________________________________________________

    }

    /**
     * Fill spinner CustomerGroup ,State ,City
     */
    public void Fillspinner() {
        //Fill spnCustomerGroup
        db.open();
        arrayCustomerGroup = db.getAllCustomerGroup();
        AdapterSpnCustomerGroup adcustomergroup = new AdapterSpnCustomerGroup(mActivity, arrayCustomerGroup);
        spnCustomerGroup.setAdapter(adcustomergroup);
        //Fill spnState
        states = db.getStates();

        //_________________________________________________________________________________________________________________
        //Fill spnCity

        ArrayList<String> cities = new ArrayList<>();

        CityZone_Extra_Data cityZone_extra_data = new CityZone_Extra_Data();
        cityZone_extra_data.setZoneName("نامشخص");
        cityZone_extra_data.setZoneCode(-1);
        cityZone_extra_data.setParentCode(-1);
        states.add(0, cityZone_extra_data);

        if (states.size() > 0) {

            cityZone_extra_data = states.get(0);
            ArrayList<CityZone_Extra_Data> cityWithZoneCodeList = db.cityWithZoneCode(cityZone_extra_data.getZoneCode());
            cityWithZoneCodeList.add(0,cityZone_extra_data);
            AdapterSpnCity adCity = new AdapterSpnCity(mActivity, cityWithZoneCodeList);
            spnCity.setAdapter(adCity);
            spnCity.setSelection(0);

            /*CityZone_Extra_Data cityZone_extra_data = states.get(0);
            long stateZoneCode = cityZone_extra_data.getZoneCode();
            cities = db.cityNames(stateZoneCode);
            String unknown = "نامشخص";
            cities.add(0, unknown);
            adCity = new AdapterSpnCity(mActivity, cities);*/
        }

        AdapterSpnState adState = new AdapterSpnState(mActivity, states);
        spnState.setAdapter(adState);
        spnState.setSelection(0);

        //spnCity.setAdapter(adCity);
        spnCity.setSelection(0);
    }

    public void FillValues() {
        db.open();

        if (personId == ProjectInfo.CUSTOMERID_GUEST) {
            customer = db.getCustomerWithPersonClientId(personClientId);
            txtCustomerName.setText(customer.getName());
            txtMarketName.setText(customer.getOrganization());
        } else {
            customer = db.getCustomerWithPersonId(personId);
            txtCustomerName.setText(customer.getName());
            txtMarketName.setText(customer.getOrganization());
        }


        if (customer != null) {
            txtCustomerName.setText(customer.getName());
            txtMarketName.setText(customer.getOrganization());

            if (customer.getCredit() == -1) {
                txtCredit.setText(R.string.free);
            } else
                txtCredit.setText(ServiceTools.formatPrice(customer.getCredit()));


            txtZone.setText(customer.getZone());
            txtMobile.setText(customer.getMobile());

            txtShift.setText(customer.getShift());
            txtLatitude.setText(String.valueOf(customer.getLatitude()));
            txtLongitude.setText(String.valueOf(customer.getLongitude()));

            //set selection spnCustomerGroup
            for (int i = 0; i < arrayCustomerGroup.size(); i++) {
                if (arrayCustomerGroup.get(i).getPersonGroupId() == customer.getPersonGroupId()) {
                    spnCustomerGroup.setSelection(i);
                    break;
                }
            }//End of for i
            //set selection spnState And spnCity

            CityZone_Extra_Data cityExtra = db.getCityExtra(customer.getCityCode());

            for (int i = 0; i < states.size(); i++) {
                if (states.get(i).getZoneCode() == cityExtra.getParentCode()) {
                    spnState.setSelection(i);
                    ArrayList<CityZone_Extra_Data> cityWithZoneCodeList = db.cityWithZoneCode(states.get(i).getZoneCode());
                    AdapterSpnCity adCity = new AdapterSpnCity(mActivity, cityWithZoneCodeList);
                    spnCity.setAdapter(adCity);
                    for (int j = 0; j < cityWithZoneCodeList.size(); j++) {
                        if (cityWithZoneCodeList.get(j).getZoneName().equals(customer.getCity())) {
                            spnCity.setSelection(j);
                            break;
                        }//End of if
                    }//End of for j
                    break;
                }// End of if
            }// End of for i

            if (customer.getPersonCode() != 0) {
                extraData = db.getMoreCustomerInfo(customer.getPersonCode());
                double RemainedAmount = extraData.getRemainAmount();
                if (extraData.getRemainStatus() == 1)
                    RemainedAmount = RemainedAmount * -1;

                if (RemainedAmount == 0)    // if customerStatus = Incalculable
                    spnCustomerType.setSelection(0);
                else if (RemainedAmount < 0)    // if customerStatus =	Debtor
                {
                    spnCustomerType.setSelection(1);
                    RemainedAmount = RemainedAmount * -1;
                } else if (RemainedAmount > 0) // if customerStaus =	Creditor
                {
                    spnCustomerType.setSelection(2);
                }
                txtRemained.setText(ServiceTools.formatPrice(RemainedAmount));

                if (extraData != null) {

                    txtAddress.setText(extraData.getStoreAddress());
                    txtTell.setText(extraData.getStorePhone());

                    txtInvoiceAVGAmount.setText(ServiceTools.formatPrice(extraData.getInvoiceAVGAmount()));
                    txtInvoiceAVGDiscount.setText(ServiceTools.formatPrice(extraData.getInvoiceAVGDiscount()));
                    txtLastInvoiceAmount.setText(ServiceTools.formatPrice(extraData.getLastInvoiceAmount()));
                    if (!TextUtils.isEmpty(extraData.getLastInvoiceDate()))
                        txtLastInvoiceDate.setText(extraData.getLastInvoiceDate());
                    txtAvailableChequeAmount.setText(ServiceTools.formatPrice(extraData.getAvailableChequeAmount()));
                    txtAvailableChequeCount.setText(ServiceTools.formatPrice(extraData.getAvailableChequeCount()));
                    txtInReceiptChequeAmount.setText(ServiceTools.formatPrice(extraData.getInReceiptChequeAmount()));
                    txtInReceiptChequeCount.setText(ServiceTools.formatPrice(extraData.getInReceiptChequeCount()));
                    txtSpentChequeAmount.setText(ServiceTools.formatPrice(extraData.getSpentChequeAmount()));
                    txtSpentChequeCount.setText(ServiceTools.formatPrice(extraData.getSpentChequeCount()));
                    txtReturnChequeAmount.setText(ServiceTools.formatPrice(extraData.getReturnChequeAmount()));
                    txtReturnChequeCount.setText(ServiceTools.formatPrice(extraData.getReturnChequeCount()));
                }
            }else {
                txtAddress.setText(customer.getAddress());
                txtTell.setText(customer.getMobile());
            }
        }
    }

    /**
     * Save Information In Database
     */
    public void SaveInDb() {
        db.open();

        customer.setName(txtCustomerName.getText().toString());
        customer.setPersonGroupId(arrayCustomerGroup.get(spnCustomerGroup.getSelectedItemPosition()).getPersonGroupId());
        customer.setPersonGroupCode(arrayCustomerGroup.get(spnCustomerGroup.getSelectedItemPosition()).getPersonGroupCode());
        customer.setOrganization(txtMarketName.getText().toString());
        customer.setState(StateName);
        customer.setCity(CityName);
        customer.setCityCode(cityCode);
        customer.setZone(txtZone.getText().toString());
        customer.setMobile(txtMobile.getText().toString());
        customer.setTell(txtTell.getText().toString());
        customer.setAddress(txtAddress.getText().toString());
        customer.setShift(txtShift.getText().toString());
        customer.setLatitude(ServiceTools.toDouble(txtLatitude.getText().toString()));
        customer.setLongitude(ServiceTools.toDouble(txtLongitude.getText().toString()));
        customer.setRowVersion(0);

        if (Mode == MODE_NEW)
            db.AddCustomer(customer);
        else if (Mode == MODE_EDIT)
            db.UpdateCustomer(customer);


    }

    /**
     * Reset Value
     */
    public void Clear() {
        spnCustomerGroup.setSelection(0);
        txtCustomerName.setText("");
        txtMarketName.setText("");
        txtRemained.setText(ServiceTools.formatPrice(0));
        spnCustomerType.setSelection(0);
        txtCredit.setText("");
        txtZone.setText("");
        txtTell.setText("");
        txtMobile.setText("");
        txtAddress.setText("");
        txtShift.setText("");
        txtLatitude.setText("");
        txtLongitude.setText("");
    }

    public class AdapterSpnCustomerGroup extends ArrayAdapter<CustomerGroup> {
        Activity context;

        public AdapterSpnCustomerGroup(Activity context, ArrayList<CustomerGroup> array) {
            super(context, android.R.layout.simple_spinner_dropdown_item, array);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            View rowview = convertView;
            TextView tvName = null;
            CustomerGroup item = getItem(position);
            if (rowview == null) {
                LayoutInflater lf = context.getLayoutInflater();
                rowview = lf.inflate(R.layout.item_spinner, null, false);
                tvName = (TextView) rowview.findViewById(R.id.tvName);

                rowview.setTag(tvName);
            } else
                tvName = (TextView) rowview.getTag();
            tvName.setText(item.getName());
            return rowview;
        }

    }

    public class AdapterSpnState extends ArrayAdapter<CityZone_Extra_Data> {
        Activity context;

        public AdapterSpnState(Activity context, List<CityZone_Extra_Data> array) {
            super(context, android.R.layout.simple_spinner_dropdown_item, array);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            View rowview = convertView;
            TextView tvName = null;
            CityZone_Extra_Data item = getItem(position);
            if (rowview == null) {
                LayoutInflater lf = context.getLayoutInflater();
                rowview = lf.inflate(R.layout.item_custom_spinner, null, false);
                tvName = (TextView) rowview.findViewById(R.id.tvName);

                rowview.setTag(tvName);
            } else
                tvName = (TextView) rowview.getTag();
            if (item != null) {
                tvName.setText(item.getZoneName());
            }
            return rowview;
        }

    }

    public class AdapterSpnCity extends ArrayAdapter<CityZone_Extra_Data> {
        Activity context;

        public AdapterSpnCity(Activity context, ArrayList<CityZone_Extra_Data> array) {
            super(context, android.R.layout.simple_spinner_dropdown_item, array);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            View rowview = convertView;
            TextView tvName = null;
            CityZone_Extra_Data item = getItem(position);
            if (rowview == null) {
                LayoutInflater lf = context.getLayoutInflater();
                rowview = lf.inflate(R.layout.item_custom_spinner, null, false);
                tvName = (TextView) rowview.findViewById(R.id.tvName);

                rowview.setTag(tvName);
            } else
                tvName = (TextView) rowview.getTag();
            tvName.setText(item.getZoneName());
            return rowview;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_CONTACT_REQUEST) {
                String Name = "", Moblie = "", Telephone = "", Address = "", Id = "", Number = "", Type = "", TypeAddress = "", Company = "";

                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(data.getData(), null, null, null, null);

                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    Name = cursor.getString(cursor.getColumnIndex(DNAME));
                    Id = cursor.getString(cursor.getColumnIndex(CID));

                    if (ServiceTools.toInt(cursor.getString(cursor.getColumnIndex(HPN))) > 0) {
                        Cursor cPhone = cr.query(PURI, null, PID + "=?", new String[]{Id}, null);
                        cPhone.moveToFirst();
                        if (cPhone.getCount() > 0) {
                            do {
                                Number = cPhone.getString(cPhone.getColumnIndex(PNUMBER));
                                Type = cPhone.getString(cPhone.getColumnIndex(PTYPE));
                                if (ServiceTools.toInt(Type) == PMOBILE) {
                                    Moblie = Number;
                                    break;
                                }
                            } while (cPhone.moveToNext());

                            cPhone.moveToFirst();
                            do {
                                Number = cPhone.getString(cPhone.getColumnIndex(PNUMBER));
                                Type = cPhone.getString(cPhone.getColumnIndex(PTYPE));
                                if (ServiceTools.toInt(Type) == PHOME) {
                                    Telephone = Number;
                                    break;
                                }
                            } while (cPhone.moveToNext());
                        }
                        cPhone.close();
                    }
                    Cursor cAddress = cr.query(SURI, null, SID + " =? ", new String[]{Id}, null);
                    if (cAddress.getCount() > 0) {
                        while (cAddress.moveToNext()) {
                            TypeAddress = cAddress.getString(cAddress.getColumnIndex(STYPE));
                            if (ServiceTools.toInt(TypeAddress) == SHOME) {
                                Address = cAddress.getString(cAddress.getColumnIndex(SADDRESS));
                                break;
                            }
                        }
                        cAddress.close();
                    }

                    Cursor cCompany = cr.query(DURI, null, OID + "=? AND " + DMIMETYPE + " =?", new String[]{Id, OITEM_TYPE}, null);
                    cCompany.moveToFirst();
                    if (cCompany.getCount() > 0) {
                        Company = cCompany.getString(cCompany.getColumnIndex(OCOMPANY));
                        cCompany.close();
                    }
                    cursor.close();
                }//End Of If
                txtCustomerName.setText(Name);
                txtMobile.setText(Moblie);
                txtTell.setText(Telephone);
                txtAddress.setText(Address);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                //setResult(RESULT_OK);
                //finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //setResult(RESULT_OK);
        //finish();
        super.onBackPressed();
    }

}
