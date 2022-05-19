package com.mahak.order;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.mahak.order.common.Customer;
import com.mahak.order.common.CustomerGroup;
import com.mahak.order.common.GPSTracker;
import com.mahak.order.common.Person_Extra_Data;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.Region;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;
import java.util.List;

public class ManageCustomerActivity extends BaseActivity {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
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

    private TextView
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
    private Button btnGetLoction, btnContact, btnShowlocation;
    private Bundle Extras;
    private static int Mode = 0;
    private long CustomerGroupId;
    private int personId;
    private long personClientId;
    private Customer customer;
    private Person_Extra_Data extraData;
    private ArrayList<CustomerGroup> arrayCustomerGroup;
    private ArrayList<Region> stateRegions;
    private Region customerRegion;
    private ArrayList<Region> cityRegions;
    private String StateName;
    private String CityName;
    private int cityCode;
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

        initialise();

        Extras = getIntent().getExtras();
        if (Extras != null) {
            Mode = Extras.getInt(MODE_PAGE);
        }

        if (Mode == MODE_EDIT) {
            personId = Extras.getInt(CUSTOMERID_KEY);
            personClientId = Extras.getLong(CUSTOMER_CLIENT_ID_KEY);

        }

        Fillspinner();
        FillValues();

        btnGetLoction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermissions()) {
                    requestPermissions();
                }else {
                    getLatLang();
                }
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
                    Region region = stateRegions.get(position);
                    cityRegions = db.getCities(region.getProvinceID());
                    AdapterSpnCity adCity = new AdapterSpnCity(mActivity, cityRegions);
                    spnCity.setAdapter(adCity);
                    spnCity.setSelection(0);
                    StateName = region.getProvinceName();
                    // TODO: 11/6/21 bug
                    //cityCode = String.valueOf(cityZone_extra_data.getZoneCode());
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
                Region region = cityRegions.get(position);
                CityName = region.getCityName();
                cityCode = region.getCityID();
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


    private void getLatLang() {
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

    private boolean checkPermissions() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            Snackbar.make(
                    findViewById(R.id.drawer_layout),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(ManageCustomerActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(ManageCustomerActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLatLang();
            } else {
                Snackbar.make(
                        findViewById(R.id.drawer_layout),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }).show();
            }
        }
    }

    /**
     * Initializing Variables
     */
    private void initialise() {

        //Find Views
        txtCustomerName =  findViewById(R.id.txtCustomerName);
        txtMarketName =  findViewById(R.id.txtMarketName);
        txtRemained =  findViewById(R.id.txtRemained);
        txtTell =  findViewById(R.id.txtTell);
        txtMobile =  findViewById(R.id.txtMobile);
        txtCredit =  findViewById(R.id.txtCredit);
        txtAddress =  findViewById(R.id.txtAddress);
        txtZone =  findViewById(R.id.txtZone);
        txtShift =  findViewById(R.id.txtShift);

        txtInvoiceAVGAmount =  findViewById(R.id.txtInvoiceAVGAmount);
        txtInvoiceAVGDiscount =  findViewById(R.id.txtInvoiceAVGDiscount);
        txtLastInvoiceAmount =  findViewById(R.id.txtLastInvoiceAmount);
        txtLastInvoiceDate =  findViewById(R.id.txtLastInvoiceDate);
        txtAvailableChequeAmount =  findViewById(R.id.txtAvailableChequeAmount);
        txtAvailableChequeCount =  findViewById(R.id.txtAvailableChequeCount);
        txtInReceiptChequeAmount =  findViewById(R.id.txtInReceiptChequeAmount);
        txtInReceiptChequeCount =  findViewById(R.id.txtInReceiptChequeCount);
        txtSpentChequeAmount =  findViewById(R.id.txtSpentChequeAmount);
        txtSpentChequeCount =  findViewById(R.id.txtSpentChequeCount);
        txtReturnChequeAmount =  findViewById(R.id.txtReturnChequeAmount);
        txtReturnChequeCount =  findViewById(R.id.txtReturnChequeCount);

        txtLatitude =  findViewById(R.id.txtLatitude);
        txtLongitude =  findViewById(R.id.txtLongitude);

        btnGetLoction = (Button) findViewById(R.id.btnGetGeographicallocation);
        btnShowlocation = (Button) findViewById(R.id.btnShowGeographicallocation);

        spnCustomerGroup = (Spinner) findViewById(R.id.spnCustomerGroup);
        spnCustomerGroup.setClickable(false);
        spnCustomerGroup.setEnabled(false);

        spnCity = (Spinner) findViewById(R.id.spnCity);
        spnCity.setClickable(false);
        spnCity.setEnabled(false);


        spnState = (Spinner) findViewById(R.id.spnState);
        spnState.setClickable(false);
        spnState.setEnabled(false);

        spnCustomerType = (Spinner) findViewById(R.id.spnCustomerType);
        spnCustomerType.setClickable(false);
        spnCustomerType.setEnabled(false);
        btnContact = (Button) findViewById(R.id.btnContact);
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
        stateRegions = db.getStates2();
        AdapterSpnState adState = new AdapterSpnState(mActivity, stateRegions);
        spnState.setAdapter(adState);
        //_________________________________________________________________________________________________________________
        //Fill spnCity
        if (stateRegions.size() > 0) {
            Region region = stateRegions.get(0);
            cityRegions = db.getCities(region.getProvinceID());
            AdapterSpnCity adCity = new AdapterSpnCity(mActivity, cityRegions);
            spnCity.setAdapter(adCity);
            spnCity.setSelection(0);
        }
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

            customerRegion = db.getRegionWithCityId(customer.getCityCode());

            txtCustomerName.setText(customer.getName());
            txtMarketName.setText(customer.getOrganization());

            if (customer.getCredit() == -1) {
                txtCredit.setText(R.string.free);
            } else
                txtCredit.setText(ServiceTools.formatPrice(customer.getCredit()));

            txtZone.setText(customer.getZone());
            txtMobile.setText(customer.getMobile());
            txtAddress.setText(customer.getAddress());
            txtTell.setText(customer.getTell());

            txtShift.setText(customer.getShift());
            txtLatitude.setText(String.valueOf(customer.getLatitude()));
            txtLongitude.setText(String.valueOf(customer.getLongitude()));

            double amount = customer.getBalance();
            if (amount == 0) {
                spnCustomerType.setSelection(0);
            }
            if (amount < 0) {
                amount = amount * -1;
                spnCustomerType.setSelection(1);
            } else if (amount > 0) {
                spnCustomerType.setSelection(2);
            }

            txtRemained.setText(ServiceTools.formatPrice(amount));

            //set selection spnCustomerGroup
            for (int i = 0; i < arrayCustomerGroup.size(); i++) {
                if (arrayCustomerGroup.get(i).getPersonGroupId() == customer.getPersonGroupId()) {
                    spnCustomerGroup.setSelection(i);
                    break;
                }
            }//End of for i
            //set selection spnState And spnCity

            for (int i = 0; i < stateRegions.size(); i++) {
                Region region = stateRegions.get(i);
                if (region.getProvinceID() == customerRegion.getProvinceID()) {
                    spnState.setSelection(i);
                    StateName = region.getProvinceName();

                    cityRegions = db.getCities(region.getProvinceID());
                    AdapterSpnCity adspncity = new AdapterSpnCity(mActivity, cityRegions);
                    spnCity.setAdapter(adspncity);
                    for (int j = 0; j < cityRegions.size(); j++) {
                        if (cityRegions.get(j).getCityID() == customerRegion.getCityID()) {
                            spnCity.setSelection(j);
                            CityName = cityRegions.get(j).getCityName();
                            cityCode = cityRegions.get(j).getCityID();
                            break;
                        }//End of if
                    }//End of for j
                    break;
                }// End of if
            }// End of for i

            if (customer.getPersonCode() != 0) {

                extraData = db.getMoreCustomerInfo(customer.getPersonCode());
                if (extraData != null) {
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
            }
        }
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

    public class AdapterSpnState extends ArrayAdapter<Region> {
        Activity context;

        public AdapterSpnState(Activity context, List<Region> array) {
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
            Region item = getItem(position);
            if (rowview == null) {
                LayoutInflater lf = context.getLayoutInflater();
                rowview = lf.inflate(R.layout.item_custom_spinner, null, false);
                tvName = (TextView) rowview.findViewById(R.id.tvName);

                rowview.setTag(tvName);
            } else
                tvName = (TextView) rowview.getTag();
            if (item != null) {
                tvName.setText(item.getProvinceName());
            }
            return rowview;
        }

    }

    public class AdapterSpnCity extends ArrayAdapter<Region> {
        Activity context;

        public AdapterSpnCity(Activity context, ArrayList<Region> array) {
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
            Region item = getItem(position);
            if (rowview == null) {
                LayoutInflater lf = context.getLayoutInflater();
                rowview = lf.inflate(R.layout.item_custom_spinner, null, false);
                tvName = (TextView) rowview.findViewById(R.id.tvName);
                rowview.setTag(tvName);
            } else
                tvName = (TextView) rowview.getTag();
            tvName.setText(item.getCityName());
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
