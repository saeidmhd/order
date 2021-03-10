package com.mahak.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mahak.order.apiHelper.ApiClient;
import com.mahak.order.apiHelper.ApiInterface;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.User;
import com.mahak.order.common.login.LoginBody;
import com.mahak.order.common.login.LoginResult;
import com.mahak.order.common.loginSignalr.SignalLoginBody;
import com.mahak.order.common.loginSignalr.SignalLoginResult;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.storage.DbSchema;
import com.mahak.order.widget.FontAlertDialog;
import com.mahak.order.widget.FontProgressDialog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityRestApi extends BaseActivity {

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

    //private AsyncLogin asyncLogin;

    private Dialog dialog;
    private FontProgressDialog pd;

    private DbAdapter mDb;

    private int TypeDate = 1;
    private int TypeLogin = 2;

    private Typeface font_robot;
    private DbAdapter db;
    private String username;
    private String password;

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
                username = txtUsername.getText().toString();
                password = txtPassword.getText().toString();

                if (TextUtils.isEmpty(password)) {
                    txtPassword.setError(getString(R.string.error_field_required));
                    focusView = txtPassword;
                    cancel = true;
                }
                if (TextUtils.isEmpty(username)) {
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
                        /*asyncLogin = new AsyncLogin(TypeDate);
                        asyncLogin.execute();*/
                        login(username, ServiceTools.computeMD5Hash(password), mContext);
                    } else {
                        mDb.open();
                        User user = mDb.getUser(username);
                        mDb.close();
                        //if user is exist
                        if (user.getServerUserID() != null) {

                            //set LoginDate in db
                            mDb.open();
                            user.setLoginDate(new Date().getTime());
                            mDb.UpdateUser(user);
                            mDb.close();

                            setPrefUserMasterId(user.getMasterId());
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

    /**
     * Initialize Variable
     */
    private void init() {

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        mDb = new DbAdapter(mContext);

    }

    public void login(final String username, final String password, final Context context) {

        LoginBody loginBody = new LoginBody();
        loginBody.setAppId("MahakOrder");
        loginBody.setDatabaseId(0);
        loginBody.setLanguage("en-US");
        loginBody.setDeviceId("");
        loginBody.setDescription("login");
        loginBody.setUserName(username);
        loginBody.setPassword(password);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResult> call = apiService.Login(loginBody);
        pd = new FontProgressDialog(context);
        pd.setMessage(getString(R.string.reviewing_user_info));
        pd.setCancelable(false);
        pd.show();
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                pd.dismiss();
                if (response.body() != null) {
                    if (response.body().isResult()) {
                        if (!ServiceTools.checkDate2(response.body().getData().getServerTime())) {
                            pd.dismiss();
                            Dialog dialog = Dialog(getString(R.string.str_message_date));
                            dialog.show();
                            FontAlertDialog.FontDialog(dialog);
                        } else {
                            mDb.open();
                            User user = new User();
                            User userState = mDb.getUser(username);
                            int userDatabaseId = ServiceTools.toInt(userState.getDatabaseId());
                            int userId = ServiceTools.toInt(userState.getServerUserID());
                            if (userState.getDatabaseId() == null) {
                                addNewUser(response, user, username, password, context);
                            } else {
                                if (userId != response.body().getData().getVisitorId()) {
                                    mDb.DeleteUser(userDatabaseId);
                                    addNewUser(response, user, username, password, context);
                                } else {
                                    userState.setUsername(username);
                                    userState.setPassword(password);
                                    userState.setLoginDate(new Date().getTime());
                                    userState.setModifyDate(new Date().getTime());
                                    userState.setUserToken(response.body().getData().getUserToken());
                                    userState.setName(response.body().getData().getUserTitle());
                                    userState.setServerUserID(String.valueOf(response.body().getData().getVisitorId()));
                                    setPrefUserToken(response.body().getData().getUserToken());
                                    boolean res = mDb.UpdateUser(userState);
                                    if (res) {
                                        setPrefUserMasterId(userState.getMasterId());
                                        setPrefUsername(userState.getUsername());
                                        setPrefname(userState.getName());
                                        setPrefMahakId(userState.getMahakId());
                                        setPrefDatabaseId(userState.getDatabaseId());
                                        setPrefDateSyncInformation(userState.getDateSync());
                                        setPrefSyncId(userState.getSyncId());
                                        setPrefUserId(response.body().getData().getVisitorId());
                                        gotoDashboard(userState);
                                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                    } else
                                        Toast.makeText(context, getString(R.string.str_message_error_save), Toast.LENGTH_LONG).show();
                                }
                            }
                            mDb.close();
                        }
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

    private void addNewUser(Response<LoginResult> response, User user, String username, String password, Context context) {
        user.setDatabaseId(String.valueOf(response.body().getData().getDatabaseId()));
        user.setMahakId("1");
        setPrefMahakId(user.getMahakId());
        setPrefDatabaseId(user.getDatabaseId());
        setPrefUserId(response.body().getData().getVisitorId());
        setPrefSyncId(response.body().getData().getSyncId());
        user.setUsername(username);
        user.setPassword(password);
        user.setPackageSerial("1");
        user.setMasterId((long) response.body().getData().getVisitorId());
        user.setName(response.body().getData().getUserTitle());
        user.setLoginDate(new Date().getTime());
        user.setModifyDate(new Date().getTime());
        user.setDateSync(new Date().getTime());
        user.setSyncId(String.valueOf(response.body().getData().getSyncId()));
        user.setServerUserID(String.valueOf(response.body().getData().getVisitorId()));
        user.setUserToken(response.body().getData().getUserToken());
        setPrefUserToken(response.body().getData().getUserToken());

        long add = mDb.AddUser(user);
        if (add >= 1) {
            long id = mDb.getMax(DbSchema.Userschema.TABLE_NAME, DbSchema.Userschema.COLUMN_ID);
            user.setId(id);
            setPrefUserMasterId(user.getMasterId());
            setPrefUsername(user.getUsername());
            setPrefname(user.getName());
            setPrefMahakId(user.getMahakId());
            setPrefDatabaseId(user.getDatabaseId());
            setPrefDateSyncInformation(user.getDateSync());
            setPrefSyncId(user.getSyncId());
            setPrefServerUserId(user.getServerUserID());
            gotoDashboard(user);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } else
            Toast.makeText(context, getString(R.string.str_message_error_save), Toast.LENGTH_LONG).show();
    }

    private void gotoDashboard(User user) {
            setPrefUserId(ServiceTools.toLong(user.getServerUserID()));
            Intent intent = new Intent(LoginActivityRestApi.this, DashboardActivity.class);
            intent.putExtra(Type_Login, bnd_Login_Splash);
            startActivity(intent);
            finish();
    }

    /**
     * @param Operation_Name is String Type
     * @param data           is String Type
     * @return value server is String Type
     */

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
        request.addProperty(PA_USER_ID, user.getMasterId());
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
        builder.setTitle(FontAlertDialog.getFontTitle(getString(R.string.str_title_message)))
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

    /**
     * @return Json is String Type
     */
   /* public String SendSign() {
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
    }*/

   /* public class ValidationAsynTask extends AsyncTask<String, String, Boolean> {
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
            pd = new ProgressDialog(LoginActivityRestApi.this);
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
                builder.setTitle(R.string.str_title_delete);
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
    }*/


}
