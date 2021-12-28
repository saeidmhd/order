package com.mahak.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.mahak.order.apiHelper.ApiClient;
import com.mahak.order.apiHelper.ApiInterface;
import com.mahak.order.common.Customer;
import com.mahak.order.common.NonRegister;
import com.mahak.order.common.Person_Extra_Data;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.User;
import com.mahak.order.common.login.LoginBody;
import com.mahak.order.common.login.LoginResult;
import com.mahak.order.common.request.SetAllDataBody;
import com.mahak.order.common.request.SetAllDataResult.SaveAllDataResult;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.widget.FontDialog;
import com.mahak.order.widget.FontPopUp;
import com.mahak.order.widget.FontProgressDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mahak.order.common.ServiceTools.getDateAndTimeForLong;

public class NonRegisterListActivity extends BaseActivity {

    private static String TAG_NAME = "Name";
    private static String TAG_LASTNAME = "LastName";
    private static String TAG_DESCRIPTION = "Description";
    private static String TAG_MARKET = "MarketName";
    private static String TAG_ADDRESS = "Address";

    private ListView lstNonRegister;
    private DbAdapter db;
    private Context mContext;
    private Activity mActivity;
    private Customer customer;
    private ArrayList<NonRegister> arrayNonRegister = new ArrayList<NonRegister>();

    private AdapterListNonRegister nonRegisterAdapter;
    private long nonRegisterId;
    private int Position;
    private EditText txtSearch;
    private Bundle Extras;
    private int Type, Page;
    private static int REQUEST_EDIT = 1;
    private String Name, LastName, MarketName, Address;
    private String Description = "توضیحات ندارد";
    private TextView tvPageTitle;

    private static final int MENU_ADD = Menu.FIRST;
    boolean enable_add, enable_send_transfer, enable_receive_transfer;
    private FontProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);

        enable_add = true;
        enable_receive_transfer = false;
        enable_send_transfer = true;

        mContext = this;
        mActivity = this;

        Extras = getIntent().getExtras();

        if (Extras != null) {
            Page = Extras.getInt(PAGE);
            Type = Extras.getInt(TYPE_KEY);
        }

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        tvPageTitle.setText(getString(R.string.str_nav_nonRegister_list));
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________

        initialise();
        db.open();

        FillView();

        lstNonRegister.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Clear();
                NonRegister nonRegister = (NonRegister) parent.getItemAtPosition(position);
                Intent intent = new Intent(mContext, NonRegisterDetailActivity.class);
                intent.putExtra(ID, nonRegister.getId());
                intent.putExtra(PAGE, PAGE_NON_REGISTER_LIST);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_NON_REGISTER);
                startActivity(intent);

            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                {
                    nonRegisterAdapter.getFilter().filter(s, new Filter.FilterListener() {

                        @Override
                        public void onFilterComplete(int count) {

                            tvPageTitle.setText(getString(R.string.str_nav_nonRegister_list) + "(" + count + ")");
                        }
                    });
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }//End Of OnCreate

    /**
     * Initializing Variables
     */
    private void initialise() {

        lstNonRegister = (ListView) findViewById(R.id.lstOrder);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        db = new DbAdapter(mContext);
        tvPageTitle.setText(getString(R.string.str_nav_nonRegister_list) + "(" + lstNonRegister.getCount() + ")");
    }

    /**
     * Read From Database And Fill Adapter
     */
    private void FillView() {
        arrayNonRegister = db.getAllNonRegister();

        for (NonRegister nonRegister : arrayNonRegister) {

            if ((nonRegister.getPersonId() != ProjectInfo.CUSTOMERID_GUEST)) {
                customer = db.getCustomerWithPersonId(nonRegister.getPersonId());
                nonRegister.setMarketName(customer.getOrganization());
                nonRegister.setCustomerName(customer.getName());

            } else if ((nonRegister.getPersonId() == ProjectInfo.CUSTOMERID_GUEST)) {
                customer = db.getCustomerWithPersonClientId(nonRegister.getPersonClientId());
                nonRegister.setMarketName(customer.getOrganization());
                nonRegister.setCustomerName(customer.getName());
            }

            nonRegister.setAddress(customer.getAddress());

        }//end of For
        nonRegisterAdapter = new AdapterListNonRegister(mActivity, arrayNonRegister);
        lstNonRegister.setAdapter(nonRegisterAdapter);
        tvPageTitle.setText(getString(R.string.str_nav_nonRegister_list) + "(" + lstNonRegister.getCount() + ")");
    }

    /**
     * Reset Value To Default
     */
    private void Clear() {
        Name = "";
        LastName = "";
        MarketName = "";
        Address = "";
    }

    public class AdapterListNonRegister extends ArrayAdapter<NonRegister> {
        Activity mContext;
        ArrayList<NonRegister> arrayorginal;
        ArrayList<NonRegister> arrayNonRegister;
        CustomFilterList Filter;

        public AdapterListNonRegister(Activity context, ArrayList<NonRegister> array) {
            super(context, android.R.layout.simple_list_item_1, array);
            mContext = context;
            arrayorginal = new ArrayList<NonRegister>();
            arrayNonRegister = new ArrayList<NonRegister>();
            arrayNonRegister.addAll(array);
            arrayorginal.addAll(array);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowview = convertView;
            Holder holder = null;
            LayoutInflater inflater = null;
            final NonRegister nonRegister = getItem(position);

            if (rowview == null) {
                inflater = mContext.getLayoutInflater();
                rowview = inflater.inflate(R.layout.lst_order_item, null, false);

                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();

            holder.Populate(nonRegister);
            final LinearLayout btnMenu = holder.btnMenu;
            holder.btnMenu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    PopupMenu popup = new PopupMenu(mContext, btnMenu);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.pmenu_edit_delete, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.mnuDelete:
                                    nonRegisterId = nonRegister.getId();
                                    Position = position;
                                    if (nonRegister.getPublish() == ProjectInfo.DONT_PUBLISH) {
                                        Dialogdelete();
                                    } else
                                        Toast.makeText(mContext, getResources().getString(R.string.str_message_publish_delete), Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.mnuEdit:
                                    if (nonRegister.getPublish() == ProjectInfo.DONT_PUBLISH) {
                                        Intent intent = new Intent(mContext, NonRegisterActivity.class);
                                        intent.putExtra(BaseActivity.CUSTOMERID_KEY, nonRegister.getPersonId());
                                        intent.putExtra(BaseActivity.CUSTOMER_CLIENT_ID_KEY, nonRegister.getPersonClientId());
                                        intent.putExtra(BaseActivity.MODE_PAGE, BaseActivity.MODE_EDIT);
                                        intent.putExtra(BaseActivity.PAGE, BaseActivity.PAGE_NON_REGISTER_LIST);
                                        intent.putExtra(BaseActivity.ID, nonRegister.getId());
                                        intent.putExtra(BaseActivity.TYPE_KEY, ProjectInfo.TYPE_NON_REGISTER);
                                        startActivityForResult(intent, REQUEST_EDIT);
                                    } else
                                        Toast.makeText(mContext, getResources().getString(R.string.str_message_publish_edit), Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.mnuSend:
                                    nonRegisterId = nonRegister.getId();
                                    Position = position;
                                    if (nonRegister.getPublish() == ProjectInfo.DONT_PUBLISH) {
                                        DialogSend();
                                    } else
                                        Toast.makeText(mContext, getResources().getString(R.string.str_message_publish_send), Toast.LENGTH_SHORT).show();
                                    break;

                            }
                            return false;
                        }
                    });
                    popup.show();

                    Menu menu = popup.getMenu();
                    for (int i = 0; i < menu.size(); i++) {
                        MenuItem mi = menu.getItem(i);
                        FontPopUp.applyFontToMenuItem(mi, mContext);
                    }


                }
            });

            return rowview;
        }

        public class Holder {
            public TextView tvAmount, tvOrderCode, tvMarketName, tvDate, tvAddress, tvCustomerName, txtAddress, comment,
                    txtMarketName, txtAmount, textStatus;
            public LinearLayout btnMenu, llPrice;
            public ImageView imgSync;

            public Holder(View view) {

                txtAddress = (TextView) view.findViewById(R.id.txtAddress);
                // description = view.findViewById(R.id.description);
                textStatus = (TextView) view.findViewById(R.id.textStatus);
                txtMarketName = (TextView) view.findViewById(R.id.txtMarketName);
                txtAmount = (TextView) view.findViewById(R.id.txtAmount);
                tvAmount = (TextView) view.findViewById(R.id.tvAmount);
                tvOrderCode = (TextView) view.findViewById(R.id.tvOrderCode);
                tvMarketName = (TextView) view.findViewById(R.id.tvMarketName);
                tvDate = (TextView) view.findViewById(R.id.tvDate);
                tvAddress = (TextView) view.findViewById(R.id.tvAddress);
                tvCustomerName = (TextView) view.findViewById(R.id.tvCustomerName);
                imgSync = (ImageView) view.findViewById(R.id.imgSync);
                btnMenu = (LinearLayout) view.findViewById(R.id.btnmenu);
                llPrice = (LinearLayout) view.findViewById(R.id.llPrice);
            }

            public void Populate(NonRegister nonRegister) {

                tvDate.setText(getDateAndTimeForLong(nonRegister.getNotRegisterDate()));
                tvAddress.setText(nonRegister.getAddress());
                tvMarketName.setText(nonRegister.getMarketName());
                tvCustomerName.setText(nonRegister.getCustomerName());
                tvOrderCode.setText(nonRegister.getCode());
                llPrice.setVisibility(View.GONE);
                //calculate FinalPrice________________________________________________________

                //Check Publish________________________________________________________________
                if (nonRegister.getPublish() == ProjectInfo.DONT_PUBLISH)
                    imgSync.setImageResource(R.drawable.ic_synchronize_red);
                else
                    imgSync.setImageResource(R.drawable.ic_synchronize_green);

                //______________________________________________________________________________
                //btnMenu.setFocusable(false);
                //btnMenu.setFocusableInTouchMode(false);
            }
        }

        public class CustomFilterList extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();
                Boolean CheckFilter = false;
                String CustomerName = "", MarketName = "", InvoiceNumber = "";

                if (constraint != null && constraint.toString().length() > 0) {
                    ArrayList<NonRegister> filterItem = new ArrayList<NonRegister>();
                    for (int i = 0; i < arrayorginal.size(); i++) {
                        NonRegister nonRegister = arrayorginal.get(i);
                        InvoiceNumber = nonRegister.getCode();
                        boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), InvoiceNumber);
                        if (result_contain) {
                            filterItem.add(nonRegister);
                            CheckFilter = true;
                        }
                    }//End of for
                    if (CheckFilter == false) {
                        for (int i = 0; i < arrayorginal.size(); i++) {
                            NonRegister nonRegister = arrayorginal.get(i);
                            CustomerName = nonRegister.getCustomerName();
                            boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), CustomerName);
                            if (result_contain) {
                                filterItem.add(nonRegister);
                                CheckFilter = true;
                            }
                        }//End of for
                    }
                    if (CheckFilter == false) {
                        for (int i = 0; i < arrayorginal.size(); i++) {
                            NonRegister nonRegister = arrayorginal.get(i);
                            MarketName = nonRegister.getMarketName();
                            boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), MarketName);
                            if (result_contain) {
                                filterItem.add(nonRegister);

                            }
                        }//End of for
                    }
                    result.values = filterItem;
                    result.count = filterItem.size();
                } else {
                    synchronized (this) {
                        result.values = arrayorginal;
                        result.count = arrayorginal.size();
                    }
                }
                return result;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                arrayNonRegister = (ArrayList<NonRegister>) results.values;
                notifyDataSetChanged();
                clear();
                for (int i = 0; i < arrayNonRegister.size(); i++) {
                    add(arrayNonRegister.get(i));
                    notifyDataSetInvalidated();
                }
            }
        }

        @Override
        public Filter getFilter() {
            if (Filter == null)
                Filter = new CustomFilterList();
            return Filter;
        }

    }// End of AdapterListNonRegister

    private void Dialogdelete() {
        FontDialog fontDialog = new FontDialog();
        final AlertDialog dialog = fontDialog.CustomeDialog(mContext, getString(R.string.str_message_delete));
        fontDialog.getPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete Order and ProductsInOrder and Receipt and Cheque___________________________
                db.DeleteNonRegister(nonRegisterId);
                //db.DeleteProductInReturn(nonRegisterId);
                //_______________________________________________________________________________
                arrayNonRegister.remove(Position);
                nonRegisterAdapter.notifyDataSetChanged();
                tvPageTitle.setText(getString(R.string.str_nav_nonRegister_list) + "(" + lstNonRegister.getCount() + ")");
                dialog.dismiss();
            }
        });
        fontDialog.getNegative().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_EDIT) {
                FillView();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, MENU_ADD, 0, R.string.str_add_non_register).setIcon(R.drawable.ic_add_inverse)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 1:
                Intent intent = new Intent(getApplicationContext(), NonRegisterActivity.class);
                intent.putExtra(PAGE, PAGE_NON_REGISTER_LIST);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_NON_REGISTER);
                intent.putExtra(MODE_PAGE, MODE_NEW);
                intent.putExtra(CUSTOMERID_KEY, ProjectInfo.CUSTOMERID_GUEST);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    private void DialogSend() {
        FontDialog fontDialog = new FontDialog();
        final AlertDialog dialog = fontDialog.CustomeDialog(mContext, getString(R.string.str_message_send));
        fontDialog.getPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendReceive();
                dialog.dismiss();
            }
        });
        fontDialog.getNegative().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void SendReceive() {

        db.open();
        final User user = db.getUser();

        LoginBody loginBody = new LoginBody();
        //String DeviceID = ServiceTools.getDeviceID(mContext);
        loginBody.setAppId("MahakOrder");
        loginBody.setDatabaseId(0);
        loginBody.setLanguage("en-US");
        loginBody.setDeviceId(ServiceTools.getDeviceID(mContext));
        loginBody.setDescription("login");
        loginBody.setUserName(user.getUsername());
        loginBody.setPassword(user.getPassword());

        ApiInterface apiService = ApiClient.orderRetrofitClient().create(ApiInterface.class);
        Call<LoginResult> call = apiService.Login(loginBody);
        pd = new FontProgressDialog(mContext);
        pd.setMessage(getString(R.string.reviewing_user_info));
        pd.setCancelable(false);
        pd.show();
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                pd.dismiss();
                if (response.body() != null) {
                    if (response.body().isResult()) {
                        BaseActivity.setPrefUserToken(response.body().getData().getUserToken());
                        setPrefSyncId(response.body().getData().getSyncId());
                        //Save db
                        db.open();
                        user.setSyncId(response.body().getData().getSyncId());
                        user.setUserToken(response.body().getData().getUserToken());
                        db.UpdateUser(user);
                        db.close();
                        sendToServer(response.body().getData().getUserToken());

                    } else {
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

    private void sendToServer(String userToken) {

        SetAllDataBody setAllDataBody;
        final ApiInterface apiService;
        Call<SaveAllDataResult> saveAllDataResultCall;
        db.open();
        setAllDataBody = new SetAllDataBody();

        setAllDataBody.setUserToken(userToken);
        apiService = ApiClient.orderRetrofitClient().create(ApiInterface.class);

        //nonRegister
        final List<NonRegister> nonRegisters = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();
        NonRegister nonRegister;

        nonRegister = db.GetNonRegister(nonRegisterId);
        nonRegisters.add(nonRegister);

        for (int i = 0; i < nonRegisters.size(); i++) {
            if (nonRegisters.get(i).getPersonId() == 0)
                customers.add(db.getCustomerWithPersonClientId(nonRegisters.get(i).getPersonClientId()));
        }
        setAllDataBody.setNonRegisters(nonRegisters);
        if (customers.size() > 0)
            setAllDataBody.setPeople(customers);
        saveAllDataResultCall = apiService.SaveAllData(setAllDataBody);
        // Set up progress before call
        final FontProgressDialog progressDoalog;
        progressDoalog = new FontProgressDialog(NonRegisterListActivity.this);
        progressDoalog.setMessage(getString(R.string.wait_for_send));
        progressDoalog.setTitle(getString(R.string.send_to_server));
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();

        saveAllDataResultCall.enqueue(new Callback<SaveAllDataResult>() {
            @Override
            public void onResponse(Call<SaveAllDataResult> call, Response<SaveAllDataResult> response) {
                progressDoalog.dismiss();
                if (response.body() != null && response.body().isResult()) {
                    db.open();
                    if (nonRegisters.size() > 0) {
                        for (int i = 0; i < nonRegisters.size(); i++) {
                            nonRegisters.get(i).setNotRegisterId(response.body().getData().getObjects().getNotRegisters().getResults().get(i).getEntityID());
                            nonRegisters.get(i).setPublish(ProjectInfo.PUBLISH);
                            db.UpdateNonRegister(nonRegisters.get(i));
                        }
                    }
                    arrayNonRegister.get(Position).setPublish(ProjectInfo.PUBLISH);
                    nonRegisterAdapter.notifyDataSetChanged();
                    Toast.makeText(mContext, R.string.sent, Toast.LENGTH_SHORT).show();

                } else if (response.body() != null) {
                    Toast.makeText(mContext, R.string.send_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SaveAllDataResult> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(mContext, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
