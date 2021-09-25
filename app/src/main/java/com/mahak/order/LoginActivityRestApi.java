package com.mahak.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mahak.order.apiHelper.ApiClient;
import com.mahak.order.apiHelper.ApiInterface;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.User;
import com.mahak.order.common.login.LoginBody;
import com.mahak.order.common.login.LoginResult;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.storage.DbSchema;
import com.mahak.order.widget.FontAlertDialog;
import com.mahak.order.widget.FontProgressDialog;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityRestApi extends BaseActivity {

    private EditText txtUsername, txtPassword;
    private Button btnLogin;

    private Context mContext;
    private Activity mActivity;

    private Dialog dialog;
    private FontProgressDialog pd;

    private DbAdapter mDb;

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

    private void dismissProgressDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
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
        loginBody.setDeviceId(ServiceTools.getDeviceID(mContext));
        loginBody.setDescription("login");
        loginBody.setUserName(username);
        loginBody.setPassword(password);

        ApiInterface apiService = ApiClient.orderRetrofitClient().create(ApiInterface.class);
        Call<LoginResult> call = apiService.Login(loginBody);
        pd = new FontProgressDialog(context);
        pd.setMessage(getString(R.string.reviewing_user_info));
        pd.setCancelable(false);
        pd.show();
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                dismissProgressDialog();
                if (response.body() != null) {
                    if (response.body().isResult()) {
                        if (!ServiceTools.checkDate2(response.body().getData().getServerTime())) {
                            dismissProgressDialog();
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
                        dismissProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                dismissProgressDialog();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewUser(Response<LoginResult> response, User user, String username, String password, Context context) {
        if (response.body() != null) {
            user.setDatabaseId(String.valueOf(response.body().getData().getDatabaseId()));
            user.setMahakId(response.body().getData().getMahakId());
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
        }
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
        if (ServiceTools.checkPlayServices(LoginActivityRestApi.this)) {
            getFcmTokenRegisterInBackground();
        }
        setPrefUserId(ServiceTools.toLong(user.getServerUserID()));
        Intent intent = new Intent(LoginActivityRestApi.this, DashboardActivity.class);
        intent.putExtra(Type_Login, bnd_Login_Splash);
        startActivity(intent);
        finish();
    }

    private void getFcmTokenRegisterInBackground() {
        FirebaseApp.initializeApp(LoginActivityRestApi.this);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                String token = task.getResult();
                Intent intent = new Intent(LoginActivityRestApi.this, RegistrationIntentService.class);
                intent.putExtra("token",token);
                startService(intent);
            }
        });
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
}
