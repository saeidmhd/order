package com.mahak.order;/*
package com.mahak.order;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.User;
import com.mahak.order.service.Parser;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.storage.DbSchema;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

    private static String APPSIGN_LOGIN = "05b14e27-f2cd-4329-8269-cbc62b182e78";
    private static String APPSIGN_ORDER = "777E45E1-C80A-4D24-854F-DF8A75446B9B";
    private static String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    private static String SOAP_ADDRESS = "http://login.mahaksoft.com/webservice.asmx";
    private static String SOAP_ADDRESS_ORDER = "http://order.mahaksoft.com/orderservice.asmx";
    private static String PA_APP_SIGN = "AppSign";
    private static String PA_JSON_STRING = "jsonString";
    private static String OPERATION_VALIDATE = "ValidateUser";
    private static String OPERATION_DATE_VALIDATE = "GetNowDate";

    private static String PA_INPUT = "Input";
    private static String PA_LASTUPDATE = "LastUpdate";
    private static String PA_DEVICE_ID = "DeviceId";
    private static String PA_USER_ID = "UserId";
    private static String PA_MAHAK_ID = "MahakId";
    private static String PA_DATABASE_ID = "DatabaseId";
    private static String PA_DATA = "Data";
    private static String PA_SYNC_ID = "SyncId";
    private static String PA_CHANGED_AFTER = "ChangedAfter";

    private String strFalse = "false";

    private EditText txtUsername, txtPassword;
    private Button btnLogin;

    private Context mContext;
    private Activity mActivity;

    private AsyncLogin asyncLogin;

    private Dialog dialog;
    private ProgressDialog pd;

    private DbAdapter mDb;

    private int TypeDate = 1;
    private int TypeLogin = 2;

    private Typeface font_robot;
    private DbAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mContext = this;
        mActivity = this;

        setContentView(R.layout.activity_login);
        init();

        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean cancel = false;
                View focusView = null;
                String userName = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                if (TextUtils.isEmpty(password)) {
                    txtPassword.setError(getString(R.string.error_field_required));
                    focusView = txtPassword;
                    cancel = true;
                }
                if (TextUtils.isEmpty(userName)) {
                    txtUsername.setError(getString(R.string.error_field_required));
                    focusView = txtUsername;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    //Check Connect to internet
                    if (ServiceTools.isOnline(mContext)) {
                        asyncLogin = new AsyncLogin(TypeDate);
                        asyncLogin.execute();
                    } else {
                        mDb.open();
                        User user = mDb.getUser(txtUsername.getText().toString(), txtPassword.getText().toString() , getPrefServerUserId());
                        mDb.close();
                        //if user is exist
                        if (user != null) {

                            //set LoginDate in db
                            mDb.open();
                            user.setLoginDate(new Date().getTime());
                            mDb.UpdateUser(user);
                            mDb.close();

                            setPrefUserMasterId(user.getReceiptCode());
                            setPrefUsername(user.getUsername());
                            setPrefname(user.getName());
                            setPrefMahakId(user.getMahakId());
                            setPrefDatabaseId(user.getDatabaseId());
                            setPrefDateSyncInformation(user.getDateSync());
                            setPrefSyncId(user.getSyncId());
                            ////////////////////
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                            gotoDashboard(user);

                        } else // if user is not exist
                            Toast.makeText(mContext, getString(R.string.str_message_disconnect_checkUser), Toast.LENGTH_LONG).show();
                    }
                }
            }//Onclick
        });

    }

    */
/**
 * Initialize Variable
 *
 * @param Operation_Name is String Type
 * @param data           is String Type
 * @return value server is String Type
 * @return Json is String Type
 * @param Operation_Name is String Type
 * @param data           is String Type
 * @return value server is String Type
 * @return Json is String Type
 *//*

    private void init() {

        //font_robot = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.tff");
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);

        //txtUsername.setTypeface(font_robot);
        //txtPassword.setTypeface(font_robot);

        btnLogin = findViewById(R.id.btnLogin);

        asyncLogin = new AsyncLogin();

        mDb = new DbAdapter(mContext);
    }

    public class AsyncLogin extends AsyncTask<String, String, Boolean> {
        boolean status = false;
        String value = "null", mData = "", mMsg = "";
        int mType;

        AsyncLogin() {
        }

        AsyncLogin(int type) {
            mType = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (mType == TypeDate) {
                pd = new ProgressDialog(LoginActivity.this);
                pd.setMessage("در حال اتصال....");
                pd.setCancelable(false);
                pd.show();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                if (mType == TypeDate) {
                    value = Request(APPSIGN_ORDER, SOAP_ADDRESS_ORDER, OPERATION_DATE_VALIDATE, "");
                    status = !value.equals(strFalse);
                } else if (mType == TypeLogin) {
                    value = Request(APPSIGN_LOGIN, SOAP_ADDRESS, OPERATION_VALIDATE, SendSign());
                    try {
                        JSONObject objGetUser = new JSONObject(value);
                        String resVal = objGetUser.getString(TAG_RESULT);

                        if (resVal.equals(RES_TRUE)) {
                            status = true;
                        } else {
                            mMsg = objGetUser.getString(TAG_MSG);
                            status = false;
                        }
                    } catch (Exception e) {
                                   FirebaseCrashlytics.getInstance().setCustomKey("user_tell",BaseActivity.getPrefname() +"_"+ BaseActivity.getPrefTell());
FirebaseCrashlytics.getInstance().setCustomKey("user_tell",BaseActivity.getPrefname() +"_"+ BaseActivity.getPrefTell());
FirebaseCrashlytics.getInstance().recordException(e);
                        mMsg = getString(R.string.str_message_pars_data);
                        status = false;
                    }
                }
            } catch (Exception e) {
                                   FirebaseCrashlytics.getInstance().setCustomKey("user_tell",BaseActivity.getPrefname() +"_"+ BaseActivity.getPrefTell());
FirebaseCrashlytics.getInstance().setCustomKey("user_tell",BaseActivity.getPrefname() +"_"+ BaseActivity.getPrefTell());
FirebaseCrashlytics.getInstance().recordException(e);
                status = false;
            }

            return status;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result) {
                if (mType == TypeDate) {

                    if (ServiceTools.checkDate(value)) {
                        //	pd.dismiss();
                        asyncLogin = new AsyncLogin(TypeLogin);
                        asyncLogin.execute();
                    } else {
                        pd.dismiss();
                        Dialog(getString(R.string.str_message_date)).show();
                    }
                }//Type = TypeDate
                else if (mType == TypeLogin) {
                    pd.dismiss();
                    try {
                        JSONObject objGetUser = new JSONObject(value);
                        mDb.open();

                        User user;
                        JSONArray jArray = objGetUser.getJSONArray(TAG_DATA);
                        user = Parser.getUser(jArray.toString());

                        User userState = mDb.getUser(txtUsername.getText().toString(), txtPassword.getText().toString(),user.getServerUserID());
                        if (userState.getDatabaseId() == null) {

                            user.setDatabaseId(user.getDatabaseId());
                            user.setMahakId(user.getMahakId());
                            user.setUsername(txtUsername.getText().toString());
                            user.setPassword(ServiceTools.computeMD5Hash(txtPassword.getText().toString()));
                            user.setPackageSerial(user.getPackageSerial());
                            user.setReceiptCode(user.getReceiptCode());
                            user.setName(user.getName());
                            user.setLoginDate(new Date().getTime());
                            user.setModifyDate(new Date().getTime());
                            user.setDateSync(0);
                            user.setSyncId(user.getSyncId());
                            user.setStoreCode(user.getStoreCode());
                            user.setServerUserID(user.getServerUserID());

                            long add = mDb.AddUser(user);
                            if (add >= 1) {
                                long id = mDb.getMax(DbSchema.Userschema.TABLE_NAME, DbSchema.Userschema.COLUMN_ID);
                                user.setId(id);
                                setPrefUserMasterId(user.getReceiptCode());
                                setPrefUsername(user.getUsername());
                                setPrefname(user.getName());
                                setPrefMahakId(user.getMahakId());
                                setPrefDatabaseId(user.getDatabaseId());
                                setPrefDateSyncInformation(user.getDateSync());
                                setPrefSyncId(user.getSyncId());
                                setPrefStore(user.getStoreCode());
                                setPrefServerUserId(user.getServerUserID());
                                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                                ValidationAsynTask validationAsynTask = new ValidationAsynTask(user, false);
                                validationAsynTask.execute();
                            } else
                                Toast.makeText(mContext, getString(R.string.str_message_error_save), Toast.LENGTH_LONG).show();
                        }//
                        else {

                            userState.setUsername(txtUsername.getText().toString());
                            userState.setPassword(ServiceTools.computeMD5Hash(txtPassword.getText().toString()));
                            userState.setLoginDate(new Date().getTime());
                            userState.setModifyDate(new Date().getTime());

                            boolean res = mDb.UpdateUser(userState);
                            if (res) {
                                setPrefUserMasterId(userState.getReceiptCode());
                                setPrefUsername(userState.getUsername());
                                setPrefname(userState.getName());
                                setPrefMahakId(userState.getMahakId());
                                setPrefDatabaseId(userState.getDatabaseId());
                                setPrefDateSyncInformation(userState.getDateSync());
                                setPrefSyncId(String.valueOf(userState.getSyncId()));
                                setPrefStore(userState.getStoreCode());

                                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                ValidationAsynTask validationAsynTask = new ValidationAsynTask(userState, false);
                                validationAsynTask.execute();

                            } else
                                Toast.makeText(mContext, getString(R.string.str_message_error_save), Toast.LENGTH_LONG).show();
                        }
                        mDb.close();

                    } catch (Exception e) {
                                   FirebaseCrashlytics.getInstance().setCustomKey("user_tell",BaseActivity.getPrefname() +"_"+ BaseActivity.getPrefTell());
FirebaseCrashlytics.getInstance().setCustomKey("user_tell",BaseActivity.getPrefname() +"_"+ BaseActivity.getPrefTell());
FirebaseCrashlytics.getInstance().recordException(e);
                        e.printStackTrace();
                    }
                }
            } else//Result
            {
                pd.dismiss();
                if (mType == TypeDate) {
                    Toast.makeText(mContext, getString(R.string.str_message_error_connect), Toast.LENGTH_LONG).show();
                } else if (mType == TypeLogin) {
                    Dialog(mMsg).show();
                }
            }//End of else
        }
    }

    private void gotoDashboard(User user) {
        setPrefUserId(user.getId());
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.putExtra(Type_Login, bnd_Login_Splash);
        startActivity(intent);
        finish();
    }

    */
/**
 * @param Operation_Name is String Type
 * @param data           is String Type
 * @return value server is String Type
 *//*


    public String Request(String AppSign, String SoapAddress, String Operation_Name, String data) {

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, Operation_Name);
        request.addProperty(PA_APP_SIGN, AppSign);
        request.addProperty(PA_JSON_STRING, data);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SoapAddress, 60000);
        String strResponse = strFalse;

        try {
            httpTransport.call(WSDL_TARGET_NAMESPACE + Operation_Name, envelope);
            Object response = envelope.getResponse();
            strResponse = response.toString();

        } catch (Exception exception) {
            FirebaseCrashlytics.getInstance().recordException(exception);
            FirebaseCrashlytics.getInstance().recordException(exception);
            Log.e("", exception.getMessage());
        }
        return strResponse;
    }

    public String Request(String Input, long LastUpdate, String OPERATION_NAME, String Data, String SyncId, User user) {

        String DeviceID = ServiceTools.getDeviceID(mContext);
        String DatabaseID = BaseActivity.getPrefDatabaseId();


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);
        request.addProperty(PA_APP_SIGN, ProjectInfo.APPSIGN);
        request.addProperty(PA_INPUT, Input);
        request.addProperty(PA_LASTUPDATE, LastUpdate);
        request.addProperty(PA_DEVICE_ID, DeviceID);
        request.addProperty(PA_USER_ID, user.getReceiptCode());
        request.addProperty(PA_MAHAK_ID, user.getMahakId());
        request.addProperty(PA_DATABASE_ID, DatabaseID);
        request.addProperty(PA_DATA, Data);
        request.addProperty(PA_SYNC_ID, SyncId);

        return soapRequest(OPERATION_NAME, request);
    }

    private String soapRequest(String OPERATION_NAME, SoapObject request) {
        int TimeOut = 600000;
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(ProjectInfo.SOAP_ADDRESS, TimeOut);
        String strResponse = RES_FALSE;

        try {

            httpTransport.call(WSDL_TARGET_NAMESPACE + OPERATION_NAME, envelope);
            Object response = envelope.getResponse();
            strResponse = response.toString();

        } catch (Exception exception) {
            FirebaseCrashlytics.getInstance().recordException(exception);
            FirebaseCrashlytics.getInstance().recordException(exception);
            exception.printStackTrace();
        }
        return strResponse;
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

    public Dialog Dialog(String msg) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setName(getString(R.string.str_title_message))
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.str_ok), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog = builder.create();
        return dialog;
    }

    //back
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    */
/**
 * @return Json is String Type
 *//*

    public String SendSign() {
        String json;
        String token = ServiceTools.getKeyFromSharedPreferences(mContext, ProjectInfo.pre_device_token);

        json = "[{";
        json += "\"Username\"" + ":\"" + txtUsername.getText().toString() + "\",";
        json += "\"Password\"" + ":\"" + txtPassword.getText().toString() + "\",";
        json += "\"deviceToken\"" + ":\"" + token + "\",";
        json += "\"DeviceId\"" + ":\"" + ServiceTools.getDeviceID(mContext) + "\",";
        json += "\"BackUrl\"" + ":\"\"";
        json += "}]";

        return json;
    }

//    private void sendSyncValidations(final boolean deletedData, final User user) {
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                final String url = ProjectInfo.SOAP_ADDRESS;
//                final String methodName = "SyncValidation";
//                final HashMap<String, Object> params = new HashMap<>();
//
//                String DeviceID = ServiceTools.getDeviceID(mContext);
//                String SyncID = BaseActivity.getPrefSyncId();
//                String DatabaseID = BaseActivity.getPrefDatabaseId();
//
//                JSONObject data = new JSONObject();
//                try {
//                    data.put("DeletedData", deletedData);
//                } catch (JSONException e) {
//                }
//                params.put("LastUpdate", 0);
//                params.put("DeviceId", DeviceID);
//                params.put("DatabaseId", DatabaseID);
//                params.put("Data", data.toString());
//                params.put("SyncId", SyncID);
//
//                final RequestSender.ResponseReceiver receiver = new RequestSender.ResponseReceiver() {
//                    @Override
//                    public void gotResponse(Object sender, final StringBuffer result, StringBuffer cookies) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                String mMsg;
//                                try {
//                                    if (result == null || result.equals("") || result.equals("invalid"))
//                                        return;
//
//
//                                    JSONObject jObjSync = new JSONObject(result.toString());
//                                    String resVal = jObjSync.getString(TAG_RESULT);
//
//                                    if (resVal.equals(RES_TRUE)) {
//                                        if (!jObjSync.optBoolean(TAG_DELETED_DATA)) {
//                                            gotoDashboard(user);
//                                        } else {
//                                            checkDeletedData(jObjSync, user);
//                                        }
//                                    } else {
//                                        mMsg = jObjSync.getString(TAG_MSG);
//                                        Dialog(mMsg).show();
//                                    }
//                                } catch (Exception e) {
                                   FirebaseCrashlytics.getInstance().setCustomKey("user_tell",BaseActivity.getPrefname() +"_"+ BaseActivity.getPrefTell());
FirebaseCrashlytics.getInstance().setCustomKey("user_tell",BaseActivity.getPrefname() +"_"+ BaseActivity.getPrefTell());
FirebaseCrashlytics.getInstance().recordException(e);
//
//                                    mMsg = getString(R.string.str_message_error_connect);
//                                    Dialog(mMsg).show();
//
//                                    Log.e("Mahak_SyncValid", e.getMessage());
//                                }
//                            }
//                        });
//
//                    }
//                };
//                new RunInternetService().Do(mContext, "", url, methodName, params, receiver, null, 14000);
//            }
//        });
//
//    }

    public class ValidationAsynTask extends AsyncTask<String, String, Boolean> {
        String mValue = "null";
        String mMsg = "";
        User user = null;
        private boolean deletedData;
        Boolean status = false;

        public ValidationAsynTask() {
        }

        ValidationAsynTask(User user, boolean deletedData) {
            this.user = user;
            this.deletedData = deletedData;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("در حال اتصال....");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected Boolean doInBackground(String... params) {

            JSONObject object = new JSONObject();
            try {
                object.put(TAG_DELETED_DATA, deletedData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mValue = Request("", 0, "SyncValidation", object.toString(), BaseActivity.getPrefSyncId(), user);
            try {
                JSONObject jObjSync = new JSONObject(mValue);
                String resVal = jObjSync.getString(TAG_RESULT);

                if (resVal.equals(RES_TRUE)) {
                    if (!jObjSync.optBoolean(TAG_DELETED_DATA))
                        status = true;
                    else {
                        checkDeletedData(jObjSync);
                    }
                } else {
                    mMsg = jObjSync.getString(TAG_MSG);
                    status = false;
                }
            } catch (Exception e) {
                                   FirebaseCrashlytics.getInstance().setCustomKey("user_tell",BaseActivity.getPrefname() +"_"+ BaseActivity.getPrefTell());
FirebaseCrashlytics.getInstance().setCustomKey("user_tell",BaseActivity.getPrefname() +"_"+ BaseActivity.getPrefTell());
FirebaseCrashlytics.getInstance().recordException(e);
                mMsg = getString(R.string.str_message_error_connect);
                status = false;

                Log.e("Mahak_SyncValid", e.getMessage());
            }

            return status;
        }

        private void checkDeletedData(JSONObject jObjSync) {
            if (db == null) db = new DbAdapter(mContext);
            db.open();
            int size = db.getAllOrderNotPublish(user.getId()).size();
            size += db.getAllOrderFamily(user.getId()).size();
            size += db.getAllReceiptNotPublish(user.getId()).size();
            db.close();
            if (size > 0) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setName(R.string.str_title_delete);
                builder.setMessage(jObjSync.optString(TAG_MSG));
                builder.setPositiveButton(R.string.str_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        db.open();
                        db.DeleteAllData();

                        Calendar cal = Calendar.getInstance();
                        setPrefSyncId(String.valueOf(cal.getTimeInMillis() / 1000));

                        //Save db
                        user.setSyncId(getPrefSyncId());
                        db.UpdateUser(user);
                        db.close();
                        ValidationAsynTask validationAsynTask = new ValidationAsynTask(user, true);
                        validationAsynTask.execute();
                    }
                });
                builder.setNegativeButton(R.string.str_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
                mMsg = "";
                status = false;
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ValidationAsynTask validationAsynTask = new ValidationAsynTask(user, true);
                        validationAsynTask.execute();
                        status = false;
                    }
                });

            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pd.dismiss();
            if (result) {

                gotoDashboard(user);
            } else if (!mMsg.equals("")) {

                Dialog(mMsg).show();
            }
        }
    }


}
*/
