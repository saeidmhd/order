package com.mahak.order;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;
import com.mahak.order.common.CityZone_Extra_Data;
import com.mahak.order.common.Customer;
import com.mahak.order.common.CustomerGroup;
import com.mahak.order.common.GPSTracker;
import com.mahak.order.common.Person_Extra_Data;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.mahak.order.common.ServiceTools.replaceWithEnglish;

public class AddPersonActivity extends BaseActivity {

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
            txtLastName,
            txtFirstName,
            txtMarketName,
            txtTell,
            txtMobile,
            txtAddress,
            txtZone,
            txtLongitude,
            txtLatitude;

    private Spinner spnCustomerGroup, spnCity, spnState;
    private Button btnGetLoction, btnSave, btnContact;
    private Bundle Extras;
    private static int Mode = 0;
    private long Id, CustomerGroupId, CustomerGroupCode;
    private Customer customer;
    private String[] states, cities, CitiesOnState;
    private ArrayList<String> arrayCity;
    private ArrayList<CityZone_Extra_Data> cityZone_extra_data_list;
    private ArrayList<CustomerGroup> arrayCustomerGroup;
    private String StateName = "", CityName = "",cityCode = "";
    private boolean FirstLoadspnState;
    private GPSTracker gpsTracker;
    private ArrayList<LatLng> positions = new ArrayList<>();
    private boolean hasContactPermission;
    private static final int REQUEST_CONTACT = 113;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        hasContactPermission = (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED);
        if (!hasContactPermission) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_CONTACTS},
                        REQUEST_CONTACT);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_CONTACTS},
                        REQUEST_CONTACT);
            }
        }

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        TextView tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        tvPageTitle.setText(getString(R.string.str_add_new_customer));
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________
        mContext = this;
        mActivity = this;

        Extras = getIntent().getExtras();
        if (Extras != null) {
            Mode = Extras.getInt(MODE_PAGE);
        }

        initialise();
        txtFirstName.requestFocus();
        Fillspinner();

        if (Mode == MODE_EDIT) {
            Id = Extras.getLong(ID);
            FillValues(Id);
        }
        ///////////////////////////////////////////////////////
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean cancel = false;
                View focusView = null;

                String LastName = txtLastName.getText().toString();
                String FirsName = txtFirstName.getText().toString();

                if (TextUtils.isEmpty(LastName)) {
                    txtLastName.setError(getString(R.string.error_field_required));
                    focusView = txtLastName;
                    cancel = true;
                }
                if (TextUtils.isEmpty(FirsName)) {
                    txtFirstName.setError(getString(R.string.error_field_required));
                    focusView = txtFirstName;
                    cancel = true;
                }

                if (cancel) {
                    // There was an error; focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    if(CustomerGroupId != 0){
                        Customer customer = SaveInDb();
                        Intent intent = new Intent(AddPersonActivity.this, PeopleListActivity.class);
                        intent.putExtra("PersonClientId", customer.getPersonClientId());
                        setResult(RESULT_OK, intent);
                        finish();
                    }else {
                        Toast.makeText(mContext, "به علت عدم وجود گروه مشتری،امکان ثبت مشتری جدید وجود ندارد", Toast.LENGTH_SHORT).show();
                    }
                    
                }


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
                        db.close();

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

        spnCustomerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomerGroup customergoup = (CustomerGroup) parent.getItemAtPosition(position);
                CustomerGroupId = customergoup.getPersonGroupId();
                CustomerGroupCode = customergoup.getPersonGroupCode();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        spnState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (FirstLoadspnState) {
                    CityZone_Extra_Data cityZone_extra_data = cityZone_extra_data_list.get(position);
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
        spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CityZone_Extra_Data cityZoneExtraData = (CityZone_Extra_Data) parent.getItemAtPosition(position);
                CityName = cityZoneExtraData.getZoneName();
                cityCode = String.valueOf(cityZoneExtraData.getZoneCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtMarketName = (EditText) findViewById(R.id.txtMarketName);
        txtTell = (EditText) findViewById(R.id.txtTell);
        txtMobile = (EditText) findViewById(R.id.txtMobile);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtZone = (EditText) findViewById(R.id.txtZone);

        txtLatitude = (EditText) findViewById(R.id.txtLatitude);
        txtLongitude = (EditText) findViewById(R.id.txtLongitude);

        btnGetLoction = (Button) findViewById(R.id.btnGetGeographicallocation);
        spnCustomerGroup = (Spinner) findViewById(R.id.spnCustomerGroup);
        spnCity = (Spinner) findViewById(R.id.spnCity);
        spnState = (Spinner) findViewById(R.id.spnState);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnContact = (Button) findViewById(R.id.btnContact);
        //______________________________________________________________________
        db = new DbAdapter(mContext);
        customer = new Customer();
        FirstLoadspnState = false;
        //________________________________________________________________________
        //Fill Array
        arrayCity = new ArrayList<String>();

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
        //________________________________________________________________________________________________________________
        //Fill spnState
        cityZone_extra_data_list = db.getStates();
        AdapterSpnState adState = new AdapterSpnState(mActivity, cityZone_extra_data_list);
        spnState.setAdapter(adState);
        //_________________________________________________________________________________________________________________
        //Fill spnCity
        if (cityZone_extra_data_list.size() > 0) {
            CityZone_Extra_Data cityZone_extra_data = cityZone_extra_data_list.get(0);
            ArrayList<CityZone_Extra_Data> cityWithZoneCodeList = db.cityWithZoneCode(cityZone_extra_data.getZoneCode());
            AdapterSpnCity adCity = new AdapterSpnCity(mActivity, cityWithZoneCodeList);
            spnCity.setAdapter(adCity);
            spnCity.setSelection(0);
        }
    }

    /**
     * Read From Database CustomerTest And Fill Fill view
     *
     * @param id
     */
    public void FillValues(long id) {
        db.open();
        customer = db.getCustomerWithPersonId(id);
        db.close();

        if (customer != null) {
            if (customer.getPersonCode() != 0) {
                Person_Extra_Data extraData = db.getMoreCustomerInfo(customer.getPersonCode());
                txtAddress.setText(extraData.getStoreAddress());
            }else
                txtAddress.setText(customer.getAddress());

            txtFirstName.setText(customer.getFirstName());
            txtLastName.setText(customer.getLastName());
            txtMarketName.setText(customer.getOrganization());

            txtTell.setText(customer.getTell());
            txtMobile.setText(customer.getMobile());

            txtLatitude.setText(customer.getLatitude());
            txtLongitude.setText(customer.getLongitude());

            //set selection spnCustomerGroup
            for (int i = 0; i < arrayCustomerGroup.size(); i++) {
                if (arrayCustomerGroup.get(i).getId() == customer.getPersonGroupId()) {
                    spnCustomerGroup.setSelection(i);
                    break;
                }
            }//End of for i
            //set selection spnState And spnCity
            for (int i = 0; i < cityZone_extra_data_list.size(); i++) {
                if (cityZone_extra_data_list.get(i).getZoneName().equals(customer.getState())) {
                    spnState.setSelection(i);

                    ArrayList<CityZone_Extra_Data> cityWithZoneCodeList = db.cityWithZoneCode(cityZone_extra_data_list.get(i).getZoneCode());
                    AdapterSpnCity adspncity = new AdapterSpnCity(mActivity, cityWithZoneCodeList);

                    spnCity.setAdapter(adspncity);
                    for (int j = 0; j < cityWithZoneCodeList.size(); j++) {
                        if (cityWithZoneCodeList.get(j).getZoneName().equals(customer.getCity())) {
                            spnCity.setSelection(j);
                            break;
                        }//End of if
                    }//End of for j
                    break;
                }// End of if
            }// End of for i
        }
    }

    /**
     * Save Information In Database
     */
    public Customer SaveInDb() {
        db.open();

        customer.setName(replaceWithEnglish(txtFirstName.getText().toString() + " " + txtLastName.getText().toString()));
        customer.setFirstName(replaceWithEnglish(txtFirstName.getText().toString()));
        customer.setLastName(replaceWithEnglish(txtLastName.getText().toString()));
        customer.setPersonGroupId(CustomerGroupId);
        customer.setPersonGroupCode(CustomerGroupCode);
        customer.setOrganization(replaceWithEnglish(txtMarketName.getText().toString()));
        customer.setPersonClientId(ServiceTools.toLong(ServiceTools.getGenerationCode()));
        customer.setState(StateName);
        customer.setCity(CityName);
        customer.setCityCode(cityCode);
        customer.setZone(txtZone.getText().toString());
        customer.setMobile(txtMobile.getText().toString());
        customer.setTell(txtTell.getText().toString());
        customer.setAddress(replaceWithEnglish(txtAddress.getText().toString()));
        customer.setLatitude(txtLatitude.getText().toString());
        customer.setLongitude(txtLongitude.getText().toString());
        customer.setCredit(0);
        customer.setBalance(0);
        customer.setUserId(BaseActivity.getPrefUserId());
        customer.setMahakId(BaseActivity.getPrefMahakId());
        customer.setDatabaseId(BaseActivity.getPrefDatabaseId());
        customer.setDeleted(false);

        db.AddCustomer(customer);
        db.close();
        return customer;

    }

    /**
     * Reset Value
     */
    public void Clear() {
        spnCustomerGroup.setSelection(0);
        txtFirstName.setText("");
        txtLastName.setText("");
        txtMarketName.setText("");
        txtZone.setText("");
        txtTell.setText("");
        txtMobile.setText("");
        txtAddress.setText("");
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
                txtLastName.setText(Name);
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
