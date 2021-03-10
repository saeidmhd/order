package com.mahak.order;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.common.Contact;
import com.mahak.order.common.Department;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.service.Parser;
import com.mahak.order.utils.EmailValidate;
import com.mahak.order.widget.FontProgressDialog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ContactActivity extends BaseActivity {

    private EditText txtName, txtEmail, txtSubject, txtBody, txtTell;
    private Button btnSend;
    private Spinner spnDepartment;
    private static String CONTACT_APPSIGN = "C0C04EA5-7BD4-4018-A4D3-E29DEC29346B";
    private static String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    private static String SOAP_ADDRESS = "http://service.mahaksoft.com/mahakservice.asmx";
    private static String GET_DEPARTMENT = "GetDepartment";
    private static String Send_Email = "SendEmail";
    private static String PA_APPSIGN = "AppSign";
    private static String PA_DATA = "Data";
    private static String Department;
    private EmailValidate emailValidator = new EmailValidate();
    private static String EmailSupport = "info@mahaksoft.com";

    private static final int REQUEST_CONTACT = 117;


    private Contact contact;
    private Context mContext;
    private boolean resDepartment = false, resName = false, resEmail = false, resTell = false, resSubject = false, resBody = false;
    private AsyncGetDepartment asyncDepartment;
    private ArrayList<Department> arrayDepartment = new ArrayList<Department>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        setSupportProgressBarIndeterminateVisibility(false);
        mContext = this;


        boolean hasContactPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED);

        if (!hasContactPermission) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        REQUEST_CONTACT);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        REQUEST_CONTACT);
            }
        }

        initialise();
        txtEmail.addTextChangedListener(emailValidator);
        //CheckValidate();
        //GetDepartment/////////////////////////////
        if (ServiceTools.isOnline(mContext)) {
            asyncDepartment = new AsyncGetDepartment();
            asyncDepartment.execute();
        } else {
            Toast.makeText(mContext, getString(R.string.str_message_dont_connect), Toast.LENGTH_LONG).show();

            //if internet disConnect then add Support department
            arrayDepartment.clear();
            Department dep = new Department();
            dep.setEmail(EmailSupport);
            dep.setName(getString(R.string.str_support));
            arrayDepartment.add(dep);
            FillSpinner();
        }
        ///////////////////////////////
        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean cancel = false;
                View focusView = null;

                String name = txtName.getText().toString();
                String email = txtEmail.getText().toString();
                String tell = txtTell.getText().toString();
                String body = txtBody.getText().toString();

                if (TextUtils.isEmpty(body)) {
                    txtBody.setError(getString(R.string.error_field_required));
                    focusView = txtBody;
                    cancel = true;
                }
                if (TextUtils.isEmpty(tell)) {
                    txtTell.setError(getString(R.string.error_field_required));
                    focusView = txtTell;
                    cancel = true;
                }
                if (TextUtils.isEmpty(email)) {
                    txtEmail.setError(getString(R.string.error_field_required));
                    focusView = txtEmail;
                    cancel = true;
                } else if (!isEmailValid(email)) {
                    txtEmail.setError(getString(R.string.str_valid_email));
                    focusView = txtEmail;
                    cancel = true;
                }
                if (TextUtils.isEmpty(name)) {
                    txtName.setError(getString(R.string.error_field_required));
                    focusView = txtName;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    contact = new Contact();
                    contact.setName(txtName.getText().toString());
                    contact.setEmail(txtEmail.getText().toString());
                    contact.setSubject(txtSubject.getText().toString());
                    contact.setTell(txtTell.getText().toString());
                    contact.setBody(txtBody.getText().toString());
                    contact.setDepartment(Department);
                    if (ServiceTools.isOnline(mContext)) {
                        AsyncSendData asyn = new AsyncSendData();
                        asyn.execute();
                    } else
                        Toast.makeText(mContext, getString(R.string.str_message_dont_connect), Toast.LENGTH_LONG).show();
                }

            }
        });

        spnDepartment.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Department item = (Department) parent.getItemAtPosition(position);
                Department = item.getEmail();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });

    }//End of OnCreate

    /**
     * Initialize Variable
     */
    private void initialise() {

        txtName = (EditText) findViewById(R.id.txtFirstName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtSubject = (EditText) findViewById(R.id.txtTitle);
        txtBody = (EditText) findViewById(R.id.txtBody);
        txtTell = (EditText) findViewById(R.id.txtTell);

        spnDepartment = (Spinner) findViewById(R.id.spnSections);
        btnSend = (Button) findViewById(R.id.btnSend);
    }

    /**
     * Fill Spinner
     */
    private void FillSpinner() {
        AdapterSpnDepartment ad = new AdapterSpnDepartment(mContext, R.layout.item_spinner, arrayDepartment);
        spnDepartment.setAdapter(ad);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * Empty Filed
     */
    private void Clear() {
        txtBody.setText("");
        txtEmail.setText("");
        txtName.setText("");
        txtSubject.setText("");
        txtTell.setText("");

    }

    /**
     * @param OPERATION_NAME type String
     * @param data           type String
     * @return Json
     */
    public String Request(String OPERATION_NAME, String data) {

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);
        request.addProperty(PA_APPSIGN, CONTACT_APPSIGN);
        request.addProperty(PA_DATA, data);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        String strResponse = "False";

        try {
            httpTransport.call(WSDL_TARGET_NAMESPACE + OPERATION_NAME, envelope);
            Object response = envelope.getResponse();
            strResponse = response.toString();

        } catch (Exception exception) {
            FirebaseCrashlytics.getInstance().recordException(exception);
            strResponse = "False";
        }
        return strResponse;
    }

    public class AsyncSendData extends AsyncTask<String, String, Boolean> {
        String ResultData;
        FontProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new FontProgressDialog(ContactActivity.this);
            pd.setMessage(getString(R.string.send_message));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            Boolean result = false;
            String Data = Parser.SendEmail(contact);
            ResultData = Request(Send_Email, Data);

            if (!ResultData.equals("False")) {
                result = true;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            pd.dismiss();
            if (result) {
                String[] array = ResultData.split(";");
                //if server return true
                if (array[0].equals("True")) {
                    Toast.makeText(mContext, array[1], Toast.LENGTH_LONG).show();
                    Clear();
                }
                //if server return false
                else if (array[0].equals("False"))
                    Toast.makeText(mContext, array[1], Toast.LENGTH_LONG).show();
            }
            // if connect ba server failed
            else
                Toast.makeText(mContext, getString(R.string.str_message_error_connect), Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
        }
    }

    public class AsyncGetDepartment extends AsyncTask<String, String, Boolean> {
        FontProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new FontProgressDialog(ContactActivity.this);
            pd.setMessage(getString(R.string.str_loading));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            Boolean result = false;
            String Data = Request(GET_DEPARTMENT, "");
            if (!Data.equals("False")) {
                arrayDepartment = new ArrayList<Department>();
                arrayDepartment = Parser.getDepartment(Data, mContext);
                ///////////////////////////////////////
                if (arrayDepartment.size() > 0)
                    result = true;
                /////////////////////////////////////////
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            pd.dismiss();
            if (result) {
                FillSpinner();
                resDepartment = true;
            } else {
                resDepartment = false;
                Toast.makeText(mContext, getString(R.string.str_message_error_connect), Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }
    }

    public class AdapterSpnDepartment extends ArrayAdapter<Department> {

        ArrayList<Department> Objects;

        public AdapterSpnDepartment(Context context, int textViewResourceId, ArrayList<Department> objects) {
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

            // tvName.setText(Objects.get(position).getName());

            List<String> departments = (Arrays.asList(getResources().getStringArray(R.array.array_departments)));

            tvName.setText(departments.get(position));

            return row;
        }
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

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
