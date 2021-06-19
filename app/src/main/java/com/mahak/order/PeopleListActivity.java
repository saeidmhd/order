package com.mahak.order;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filter.FilterListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kishcore.sdk.hybrid.api.SDKManager;
import com.mahak.order.apiHelper.ApiClient;
import com.mahak.order.apiHelper.ApiInterface;
import com.mahak.order.common.Customer;
import com.mahak.order.common.CustomerGroup;
import com.mahak.order.common.Person_Extra_Data;
import com.mahak.order.common.Printer;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.SharedPreferencesHelper;
import com.mahak.order.common.User;
import com.mahak.order.common.login.LoginBody;
import com.mahak.order.common.login.LoginResult;
import com.mahak.order.common.request.GetAllDataBody;
import com.mahak.order.common.request.GetAllDataResult.GetDataResult;
import com.mahak.order.scan.SmallCaptureActivity;
import com.mahak.order.service.DataService;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.storage.DbSchema;
import com.mahak.order.widget.FontDialog;
import com.mahak.order.widget.FontPopUp;
import com.mahak.order.widget.FontProgressDialog;
import com.mahak.order.widget.SortDialogActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mahak.order.common.ProjectInfo.Woosim_WSP_R341;
import static com.mahak.order.common.ServiceTools.getDateAndTimeForLong;
import static com.mahak.order.common.ServiceTools.getFileName;

public class PeopleListActivity extends BaseActivity {

    private static final int REQUESTCODE_MANAGE_CUSTOMER = 1;
    private static final int REQUESTCODE_ADD_CUSTOMER = 2;
    private static long GroupId;
    private static int PositionGroup = 0;
    private static String GROUPID_KEY = CUSTOMER_GROUP_KEY;
    private static String POSITION_KEY = "Position";
    private boolean FIRST_LOADE = false;
    private Context mContext;
    private Activity mActivity;
    private ListView lstCustomer;
    private DbAdapter db;
    private ArrayList<Customer> arrayCustomer;
    private AdapterCustomer adCustomer;
    private Spinner spnGroup;
    private long Id;
    private int Position;
    private Bundle Extras;
    private int Page;
    private EditText txtSearch;
    public static ArrayList<CustomerGroup> arrayGroup = new ArrayList<CustomerGroup>();
    private TextView tvPageTitle;
    public static int type = 0;
    private static final int ACCESS_CALL = 114;
    private static final int REQUEST_PHONE_PERMISSION = 116;
    // private SwipeRefreshLayout swipeRefresh;
    private FontProgressDialog pd;
    List<CustomerGroup> personGroupLists;
    List<Customer> customerLists;
    List<com.mahak.order.common.ExtraData> extraDataList;
    AdapterSpnGroup adspinner;
    int printerBrand;
    LinearLayout ll;

    private AdapterListCustomerForPrint _adProduct;

    private Date dt = new Date();
    private LinearLayout llprogressBar;
    private long CustomerMaxRowVersion;
    private long ExtraDataMaxRowVersion;
    private long CustomersGroupRowVersion;
    private int CountCustomer;
    private int totalItem = 0;
    Boolean CheckFilter = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_list);


        boolean hasPhonePermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED);

        if (!hasPhonePermission) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        REQUEST_PHONE_PERMISSION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        REQUEST_PHONE_PERMISSION);
            }
        }

        setSupportProgressBarIndeterminateVisibility(false);
        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
        tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________
        mContext = this;
        mActivity = this;

        printerBrand = SharedPreferencesHelper.getPrefPrinterBrand(mContext);

        arrayCustomer = new ArrayList<>();
        arrayGroup = new ArrayList<>();

        Extras = getIntent().getExtras();

        if (Extras != null) {
            Page = Extras.getInt(PAGE);
            type = Extras.getInt(TYPE_KEY);
        }

        initialise();

        db.open();
        CountCustomer = db.getTotalCountPeople();
        tvPageTitle.setText(getString(R.string.str_nav_customer_list) + "(" + CountCustomer + ")");

        FillSpinner();

        if (type == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
            spnGroup.setEnabled(false);
        }

        if (savedInstanceState == null) {
            GroupId = ProjectInfo.DONT_CUSTOMER_GROUP;
            PositionGroup = 0;
        } else {
            GroupId = savedInstanceState.getLong(GROUPID_KEY);
            PositionGroup = savedInstanceState.getInt(POSITION_KEY);
            spnGroup.setSelection(PositionGroup);
        }

        spnGroup.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PositionGroup = position;
                //Cancel Asyn//////////////////////////////////////////////////////////////
                if (FIRST_LOADE) {
                    CustomerGroup group = (CustomerGroup) parent.getItemAtPosition(position);
                    GroupId = arrayGroup.get(position).getPersonGroupId();
                    if (!TextUtils.isEmpty(txtSearch.getText().toString())) {
                        adCustomer.getFilter().filter(txtSearch.getText().toString(), new FilterListener() {
                            @Override
                            public void onFilterComplete(int count) {
                                tvPageTitle.setText(getString(R.string.str_nav_customer_list) + "(" + count + ")");
                            }
                        });
                    } else {
                        CheckFilter = false;
                        ReadALLCustomer();
                    }
                }
                FIRST_LOADE = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });

        txtSearch.addTextChangedListener(new TextWatcher() {

            private Timer timer=new Timer();
            private final long DELAY = 1500;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (adCustomer != null) {
                                            if (!TextUtils.isEmpty(s.toString())) {
                                                adCustomer.getFilter().filter(s.toString(), new FilterListener() {
                                                    @Override
                                                    public void onFilterComplete(int count) {
                                                        tvPageTitle.setText(getString(R.string.str_nav_customer_list) + "(" + count + ")");
                                                    }
                                                });
                                            } else {
                                                ReadALLCustomer();
                                                tvPageTitle.setText(getString(R.string.str_nav_customer_list) + "(" + CountCustomer + ")");
                                            }
                                        }
                                    }
                                });
                            }
                        },DELAY);
            }
        });

        // Read All CUstomer//////////////////////////
        ReadALLCustomer();

        lstCustomer.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int mLastFirstVisibleItem;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(mLastFirstVisibleItem < firstVisibleItem){
                    if (!CheckFilter) {
                        if (firstVisibleItem + visibleItemCount > totalItemCount - 2 && totalItemCount < CountCustomer) {
                            totalItem = totalItemCount;
                            adCustomer.addAll(db.getAllCustomer(GroupId, totalItem));
                            adCustomer.notifyDataSetChanged();
                        }
                    }
                }
                mLastFirstVisibleItem = firstVisibleItem;
            }
        });


    }//End Of OnCreate


    /**
     * Initializing Variables
     */
    private void initialise() {
        db = new DbAdapter(mContext);
        lstCustomer = (ListView) findViewById(R.id.lstCustomer);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        spnGroup = (Spinner) findViewById(R.id.spnGroup);
        // swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        llprogressBar = (LinearLayout) findViewById(R.id.llprogressBar);
    }

    /**
     * Read All CustomerTest From Database
     */
    private void ReadALLCustomer() {
        db.open();
        arrayCustomer = db.getAllCustomer(GroupId, 0);
        adCustomer = new AdapterCustomer(mActivity);
        lstCustomer.setAdapter(adCustomer);
       // tvPageTitle.setText(getString(R.string.str_nav_customer_list) + "(" + lstCustomer.getCount() + ")");
    }//End of if

    /**
     * Fill Spinner CustomerTest Group
     */
    private void FillSpinner() {
        ArrayList<CustomerGroup> arrayTemp = new ArrayList<CustomerGroup>();
        arrayGroup = new ArrayList<CustomerGroup>();
        CustomerGroup group = new CustomerGroup();
        group.setId(ProjectInfo.DONT_CUSTOMER_GROUP);
        group.setName(getResources().getString(R.string.str_all));
        arrayGroup.add(group);
        arrayTemp = db.getAllCustomerGroup();
        arrayGroup.addAll(arrayTemp);
        CustomerGroup group2 = new CustomerGroup();
        group2.setPersonGroupId(ProjectInfo.promo_CUSTOMER_GROUP);
        group2.setName("مشتریان دارای طرح تشویقی");
        arrayGroup.add(group2);

        adspinner = new AdapterSpnGroup(mContext, R.layout.item_spinner, arrayGroup);
        spnGroup.setAdapter(adspinner);
        txtSearch.setText("");
    }

    /**
     * Reset Value
     */
    private void Clear() {
        FIRST_LOADE = false;
        GroupId = ProjectInfo.DONT_CUSTOMER_GROUP;
        PositionGroup = 0;
    }

    private class AdapterCustomer extends ArrayAdapter<Customer> {
        Activity mcontaxt;
        CustomFilterList Filter;
        ArrayList<Customer> originalCustomers;

        public AdapterCustomer(Activity contaxt) {
            super(contaxt, android.R.layout.simple_list_item_1,arrayCustomer);
            mcontaxt = contaxt;
            originalCustomers = arrayCustomer;
        }

        public int getCount() {
            return arrayCustomer.size();
        }

        public class CustomFilterList extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();

                if (constraint.toString().length() > 0) {

                     ArrayList<Customer> newValue;

                     newValue = db.searchCustomer(GroupId, constraint.toString());

                    result.values = newValue;
                    result.count = newValue.size();
                } else {
                    synchronized (this) {
                        result.values = originalCustomers;
                        result.count = CountCustomer;
                    }
                }
                return result;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                adCustomer.clear();
                arrayCustomer = (ArrayList<Customer>) results.values;
                adCustomer.addAll(arrayCustomer);
                if (results.count > 0) {
                    notifyDataSetChanged();
                }else {
                    notifyDataSetInvalidated();
                }
            }
        }

        private class Holder {

            public TextView tvMarketName, tvRemained, tvCustomerStatus, tvAddress, tvCustomerName;
            public LinearLayout btnMenu;

            public Holder(View view) {

                tvMarketName = (TextView) view.findViewById(R.id.tvMarketName);
                tvRemained = (TextView) view.findViewById(R.id.tvRemained);
                tvCustomerStatus = (TextView) view.findViewById(R.id.tvCustomerStatus);
                tvAddress = (TextView) view.findViewById(R.id.tvAddress);
                tvCustomerName = (TextView) view.findViewById(R.id.tvCustomerName);
                btnMenu = (LinearLayout) view.findViewById(R.id.btnmenu);
                btnMenu.setFocusable(false);
                btnMenu.setFocusableInTouchMode(false);
            }

            public void Populate(Customer customer) {

                double amount = customer.getBalance();

                if (amount == 0) {
                    tvRemained.setText(ServiceTools.formatPrice(amount));
                    tvCustomerStatus.setText(mContext.getResources().getString(R.string.str_incalculable));
                }
                if (amount < 0) {
                    amount = amount * -1;
                    tvRemained.setText(ServiceTools.formatPrice(amount));
                    tvCustomerStatus.setText(mContext.getResources().getString(R.string.str_debitor));
                } else if (amount > 0) {
                    tvRemained.setText(ServiceTools.formatPrice(amount));
                    tvCustomerStatus.setText(mContext.getResources().getString(R.string.str_creditor));
                }

                tvMarketName.setText(customer.getOrganization());
                tvCustomerName.setText(customer.getName());
                tvAddress.setText(customer.getAddress());

            }
        }// End of Holder

        @Override
        public Filter getFilter() {
            if (Filter == null)
                Filter = new CustomFilterList();
            return Filter;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowview = convertView;
            Holder holder = null;
            LayoutInflater inflater = null;
            Customer customer = getItem(position);

            if (rowview == null) {
                inflater = mcontaxt.getLayoutInflater();
                rowview = inflater.inflate(R.layout.lst_customer_item, null, false);
                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();
            holder.Populate(customer);
            final LinearLayout btnMenu = holder.btnMenu;

            rowview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Customer customer = (Customer) parent.getItemAtPosition(position);
                    Customer customer = getItem(position);

                    if (customer.getPersonId() == 0 && customer.getPersonClientId() == 0) {
                        Intent intent = new Intent(mContext, AddPersonActivity.class);
                        startActivityForResult(intent, REQUESTCODE_ADD_CUSTOMER);
                    } else {
                        if (Page == PAGE_DASHBORD) {
                            Intent intent = new Intent(mContext, ManageCustomerActivity.class);
                            intent.putExtra(MODE_PAGE, MODE_EDIT);
                            intent.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                            intent.putExtra(CUSTOMER_CLIENT_ID_KEY, customer.getPersonClientId());
                            intent.putExtra(CUSTOMER_GROUP_KEY, customer.getPersonGroupId());
                            startActivityForResult(intent, REQUESTCODE_MANAGE_CUSTOMER);
                        } else if (Page == PAGE_ADD_INVOICE) {
                            Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                            intent.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                            intent.putExtra(CUSTOMER_CLIENT_ID_KEY, customer.getPersonClientId());
                            intent.putExtra(CUSTOMER_GROUP_KEY, customer.getPersonGroupId());
                            intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_INVOCIE);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else if (Page == PAGE_ADD_ORDER) {
                            Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                            intent.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                            intent.putExtra(CUSTOMER_CLIENT_ID_KEY, customer.getPersonClientId());
                            intent.putExtra(CUSTOMER_GROUP_KEY, customer.getPersonGroupId());
                            intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_ORDER);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else if (Page == PAGE_MANAGE_RECEIPT) {
                            Intent intent = new Intent(mContext, ManageReceiptActivity.class);
                            intent.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                            intent.putExtra(CUSTOMER_CLIENT_ID_KEY, customer.getPersonClientId());
                            intent.putExtra(CUSTOMER_GROUP_KEY, customer.getPersonGroupId());
                            intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_NON);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else if (Page == PAGE_ADD_NON_REGISTER) {
                            Intent intent = new Intent(mContext, NonRegisterActivity.class);
                            intent.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                            intent.putExtra(CUSTOMER_CLIENT_ID_KEY, customer.getPersonClientId());
                            intent.putExtra(CUSTOMER_GROUP_KEY, customer.getPersonGroupId());
                            setResult(RESULT_OK, intent);
                            finish();
                        } else if (Page == PAGE_ADD_RETURN) {
                            Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                            intent.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                            intent.putExtra(CUSTOMER_CLIENT_ID_KEY, customer.getPersonClientId());
                            intent.putExtra(CUSTOMER_GROUP_KEY, customer.getPersonGroupId());
                            intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_RETURN_OF_SALE);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }

                }
            });

            holder.btnMenu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(mcontaxt, btnMenu);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.pmenu_call, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            String Mobile = customer.getMobile();
                            String Tell = customer.getTell();

                            if (Mobile == null) Mobile = "";
                            if (Tell == null) Tell = "";

                            switch (item.getItemId()) {
                                case R.id.mnuDelete:
                                    Id = customer.getId();
                                    Position = position;
                                    Dialogdelete();
                                    break;
                                case R.id.mnuCall:
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    if (Mobile.equals("") && Tell.equals(""))
                                        Toast.makeText(mContext, getResources().getString(R.string.str_message_call), Toast.LENGTH_SHORT).show();
                                    else {
                                        if (!Mobile.equals(""))
                                            intent.setData(Uri.parse("tel:" + customer.getMobile().trim()));
                                        else
                                            intent.setData(Uri.parse("tel:" + customer.getTell().trim()));
                                        if (ActivityCompat.checkSelfPermission(PeopleListActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(PeopleListActivity.this,
                                                    new String[]{Manifest.permission.CALL_PHONE},
                                                    ACCESS_CALL);
                                        }
                                        startActivity(intent);
                                    }
                                    break;
                                case R.id.mnuSms:
                                    if (!Mobile.equals("")) {
                                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                        sendIntent.setData(Uri.parse("sms:" + customer.getMobile().trim()));
                                        startActivity(sendIntent);
                                    } else
                                        Toast.makeText(mContext, getResources().getString(R.string.str_message_mobil), Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.mnuTransaction:
                                    Intent in = new Intent(mContext, TransactionsActivity.class);
                                    in.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                                    startActivity(in);
                                    break;
                                case R.id.mnuMoreInfo:
                                    Intent intent1 = new Intent(mContext, ManageCustomerActivity.class);
                                    intent1.putExtra(MODE_PAGE, MODE_EDIT);
                                    intent1.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                                    intent1.putExtra(CUSTOMER_CLIENT_ID_KEY, customer.getPersonClientId());
                                    intent1.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                                    startActivity(intent1);
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
                    Menu menu = popup.getMenu();
                    for (int i = 0; i < menu.size(); i++) {
                        MenuItem mi = menu.getItem(i);
                        FontPopUp.applyFontToMenuItem(mi, mcontaxt);
                    }

                }
            });

            return rowview;
        }
    }

    public class AdapterSpnGroup extends ArrayAdapter<CustomerGroup> {

        ArrayList<CustomerGroup> Objects;

        public AdapterSpnGroup(Context context, int textViewResourceId, ArrayList<CustomerGroup> objects) {
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

    private void Dialogdelete() {
        FontDialog fontDialog = new FontDialog();
        final AlertDialog dialog = fontDialog.CustomeDialog(mContext, getString(R.string.str_message_delete));
        fontDialog.getPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.DeleteCustomer(Id)) {
                    arrayCustomer.remove(Position);
                    adCustomer.notifyDataSetChanged();
                }
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
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, R.string.scan_cancelled, Toast.LENGTH_LONG).show();
            } else {
                Customer customer = db.getCustomerByBarcode(result.getContents());
                if (customer != null) {

                    if (Page == PAGE_DASHBORD) {
                        Intent intent = new Intent(mContext, ManageCustomerActivity.class);
                        intent.putExtra(MODE_PAGE, MODE_EDIT);
                        intent.putExtra(ID, customer.getPersonId());
                        intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                        startActivityForResult(intent, REQUESTCODE_MANAGE_CUSTOMER);
                    } else if (Page == PAGE_Invoice_Detail_Activity) {
                        Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                        intent.putExtra(CUSTOMERID_KEY, customer.getId());
                        intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                        setResult(RESULT_OK, intent);
                        finish();

                    } else if (Page == PAGE_MANAGE_RECEIPT) {
                        Intent intent = new Intent(mContext, ManageReceiptActivity.class);
                        intent.putExtra(CUSTOMERID_KEY, customer.getId());
                        intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else if (Page == PAGE_ADD_NON_REGISTER) {
                        Intent intent = new Intent(mContext, NonRegisterActivity.class);
                        intent.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                        intent.putExtra(CUSTOMER_CLIENT_ID_KEY, customer.getPersonClientId());
                        intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    Log.d("MainActivity", "Scanned");
                } else
                    Toast.makeText(mContext, R.string.there_is_no_customer, Toast.LENGTH_SHORT).show();
            }

        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUESTCODE_ADD_CUSTOMER:
                    long personClientId;
                    Bundle bundle = data.getExtras();
                    personClientId = bundle != null ? bundle.getLong("PersonClientId", 0) : 0;
                    if (Page == PAGE_ADD_INVOICE) {
                        Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                        intent.putExtra(CUSTOMER_CLIENT_ID_KEY, personClientId);
                        intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                        intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_INVOCIE);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else if (Page == PAGE_ADD_ORDER) {
                        Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                        intent.putExtra(CUSTOMER_CLIENT_ID_KEY, personClientId);
                        intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                        intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_ORDER);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else if (Page == PAGE_MANAGE_RECEIPT) {
                        Intent intent = new Intent(mContext, ManageReceiptActivity.class);
                        intent.putExtra(CUSTOMER_CLIENT_ID_KEY, personClientId);
                        intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                        intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_NON);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else if (Page == PAGE_ADD_NON_REGISTER) {
                        Intent intent = new Intent(mContext, NonRegisterActivity.class);
                        intent.putExtra(CUSTOMER_CLIENT_ID_KEY, personClientId);
                        intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else if (Page == PAGE_ADD_RETURN) {
                        Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                        intent.putExtra(CUSTOMER_CLIENT_ID_KEY, personClientId);
                        intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                        intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_RETURN_OF_SALE);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        ReadALLCustomer();
                    }
                    break;
                case REQUESTCODE_MANAGE_CUSTOMER:
                    // Read All CUstomer//////////////////////////
                    ReadALLCustomer();
                    break;
                case 1001:
                    sortCustomer();
                    break;
            }
        }
    }

    private void sortCustomer() {
        ReadALLCustomer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_customer_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(mContext, ManageCustomerActivity.class);
                intent.putExtra(MODE_PAGE, MODE_NEW);
                startActivityForResult(intent, REQUESTCODE_MANAGE_CUSTOMER);
                break;
            case R.id.home:
                Clear();
                break;

            case R.id.mnuBarcode:

                if (printerBrand == ProjectInfo.PRINTER_SZZT_KS8223) {
                    SDKManager.openBarcodeScanner(true, true, objs -> {
                        String data = (String) objs[0];
                        handleCustomerWithBarcode(data);
                    }, data -> {
                        int ret = (int) data[0];
                        if (ret == SDKManager.TIMEOUT) {
                            Toast.makeText(mContext, "زمان اسکن پایان یافت.", Toast.LENGTH_SHORT).show();
                        } else if (ret == SDKManager.DEVICE_USED) {
                            Toast.makeText(mContext, "اسکنر مشغول است. لطفا مجددا تلاش نمایید.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "خطا در اسکن. لطفا مجددا تلاش نمایید.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    IntentIntegrator integrator = new IntentIntegrator(this);
                    integrator.setOrientationLocked(false);
                    integrator.setCaptureActivity(SmallCaptureActivity.class);
                    integrator.initiateScan();
                }
                break;

            case R.id.mnuAdd:
                Intent intent2 = new Intent(mContext, AddPersonActivity.class);
                startActivityForResult(intent2, REQUESTCODE_ADD_CUSTOMER);
                break;
            case R.id.mnuPrint:
                PreparePrinterData ppd = new PreparePrinterData();
                ppd.execute();
                break;
            case R.id.mnuSort:
                Intent intent3 = new Intent(PeopleListActivity.this, SortDialogActivity.class);
                intent3.putExtra("type", "customer");
                startActivityForResult(intent3, 1001);
                break;
            case R.id.mnuRefresh:
                SendReceive();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void handleCustomerWithBarcode(String contents) {
        Customer customer = db.getCustomerByBarcode(contents);
        if (customer != null) {
            if (Page == PAGE_DASHBORD) {
                Intent intent = new Intent(mContext, ManageCustomerActivity.class);
                intent.putExtra(MODE_PAGE, MODE_EDIT);
                intent.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                intent.putExtra(CUSTOMER_CLIENT_ID_KEY, customer.getPersonClientId());
                intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                startActivityForResult(intent, REQUESTCODE_MANAGE_CUSTOMER);
            } else if (Page == PAGE_ADD_INVOICE) {
                Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                intent.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                intent.putExtra(CUSTOMER_CLIENT_ID_KEY, customer.getPersonClientId());
                intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_INVOCIE);
                setResult(RESULT_OK, intent);
                finish();
            } else if (Page == PAGE_ADD_ORDER) {
                Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                intent.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                intent.putExtra(CUSTOMER_CLIENT_ID_KEY, customer.getPersonClientId());
                intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_ORDER);
                setResult(RESULT_OK, intent);
                finish();
            } else if (Page == PAGE_MANAGE_RECEIPT) {
                Intent intent = new Intent(mContext, ManageReceiptActivity.class);
                intent.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                intent.putExtra(CUSTOMER_CLIENT_ID_KEY, customer.getPersonClientId());
                intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_NON);
                setResult(RESULT_OK, intent);
                finish();
            } else if (Page == PAGE_ADD_NON_REGISTER) {
                Intent intent = new Intent(mContext, NonRegisterActivity.class);
                intent.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                intent.putExtra(CUSTOMER_CLIENT_ID_KEY, customer.getPersonClientId());
                intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                setResult(RESULT_OK, intent);
                finish();
            } else if (Page == PAGE_ADD_RETURN) {
                Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                intent.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                intent.putExtra(CUSTOMER_CLIENT_ID_KEY, customer.getPersonClientId());
                intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_RETURN_OF_SALE);
                setResult(RESULT_OK, intent);
                finish();
            }
            Log.d("MainActivity", "Scanned");
        } else
            Toast.makeText(mContext, R.string.there_is_no_customer, Toast.LENGTH_SHORT).show();
    }


    private class PreparePrinterData extends AsyncTask<String, Integer, Boolean> {

        Bitmap b = null;
        String fName = "";
        String fPath = ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_IMAGES + "/" + ProjectInfo.DIRECTORY_INVOICES;

        @Override
        protected void onPreExecute() {
            llprogressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean status = false;

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ll = new LinearLayout(mContext);


            if (printerBrand == ProjectInfo.PRINTER_BABY_380_A || printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310 || printerBrand == ProjectInfo.PRINTER_DELTA_380_A) {
                if (inflater != null) {
                    inflater.inflate(R.layout.customer_print_80mm, ll, true);
                }

            } else if (printerBrand == ProjectInfo.PRINTER_BABY_280_A) {
                if (inflater != null) {
                    inflater.inflate(R.layout.customer_print_50mm, ll, true);
                }
            } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII || printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW || printerBrand == ProjectInfo.UROVO_K319 || printerBrand == Woosim_WSP_R341) {

                ll.setDividerPadding(2);
                ll.setBackgroundColor(getResources().getColor(R.color.black));
                if (inflater != null) {
                    inflater.inflate(R.layout.customer_print_80mm, ll, true);
                }
                LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                ChangePrintWidth(_llPrint);
            } else {
                if (inflater != null) {
                    inflater.inflate(R.layout.customer_print_60mm, ll, true);
                }
            }

            FillPrintView(ll);
            ll.setDrawingCacheEnabled(true);
            ll.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            ll.layout(0, 0, ll.getMeasuredWidth(), ll.getMeasuredHeight());
            ll.buildDrawingCache(true);
            //b = Bitmap.createBitmap(ll.getDrawingCache());
            b = Printer.CreateBitmap(ll);
            ll.setDrawingCacheEnabled(false);

            fName = GetFileName(dt.getTime());
            if (b != null) {
                Printer.CreateFile(b, fName, fPath);
            }

            if (b != null)
                status = true;

            return status;
        }

        public void FillPrintView(View view) {
            //controls
            ListView _lstProduct = (ListView) view.findViewById(R.id._lstCustomer);
            TextView _tvOrderDate = (TextView) view.findViewById(R.id._tvOrderDate);
            TextView _tvUsername = (TextView) view.findViewById(R.id._tvUsername);
            _tvOrderDate.setText(getDateAndTimeForLong(dt.getTime()));
            if (BaseActivity.getAuthentication())
                _tvUsername.setText(BaseActivity.getUserProfile().getName());
            _adProduct = new AdapterListCustomerForPrint(mActivity, arrayCustomer);
            _lstProduct.setDrawingCacheEnabled(true);
            _lstProduct.setAdapter(_adProduct);
            ServiceTools.setListViewHeightBasedOnChildren(_lstProduct);

        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                Intent intent = new Intent(PeopleListActivity.this, PrintActivity.class);
                intent.putExtra(ProjectInfo._TAG_PAGE_NAME, ProjectInfo._pName_OrderDetail);
                intent.putExtra(ProjectInfo._TAG_PATH, fPath);
                intent.putExtra(ProjectInfo._TAG_Name, fName);
                startActivity(intent);
                llprogressBar.setVisibility(View.GONE);
            } else {
            }
        }
    }

    public String GetFileName(long date) {
        return getFileName(date) + "-" + ".png";
    }

    public class AdapterListCustomerForPrint extends ArrayAdapter<Customer> {
        Activity mcontaxt;

        public AdapterListCustomerForPrint(Activity contaxt, ArrayList<Customer> array) {

            super(contaxt, R.layout.lst_print_customer, array);
            mcontaxt = contaxt;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowview = convertView;
            Holder holder = null;
            LayoutInflater inflater = null;

            final Customer customer = getItem(position);

            if (rowview == null) {
                inflater = mcontaxt.getLayoutInflater();
                rowview = inflater.inflate(R.layout.lst_print_customer, null, false);
                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();

            holder.Populate(customer, position);

            return rowview;
        }

        public class Holder {
            public TextView tvCustomerCode, tvStatus, tvCustomerName, tvBalance;
            public LinearLayout llitem;

            public Holder(View view) {

                llitem = (LinearLayout) view.findViewById(R.id.llitem);
                tvCustomerCode = (TextView) view.findViewById(R.id.tvCustomerCode);
                tvStatus = (TextView) view.findViewById(R.id.tvStatus);
                tvCustomerName = (TextView) view.findViewById(R.id.tvCustomerName);
                tvBalance = (TextView) view.findViewById(R.id.tvBalance);

            }

            public void Populate(Customer customer, int position) {

                double amount = customer.getBalance();

                if (amount == 0) {
                    tvBalance.setText(ServiceTools.formatPrice(amount));
                    tvStatus.setText(mContext.getResources().getString(R.string.str_incalculable));
                }
                if (amount < 0) {
                    amount = amount * -1;
                    tvBalance.setText(ServiceTools.formatPrice(amount));
                    tvStatus.setText(mContext.getResources().getString(R.string.str_debitor));
                } else if (amount > 0) {
                    tvBalance.setText(ServiceTools.formatPrice(amount));
                    tvStatus.setText(mContext.getResources().getString(R.string.str_creditor));
                }

                tvCustomerCode.setText(String.valueOf(customer.getPersonCode()));
                tvCustomerName.setText(String.valueOf(customer.getName()));

            }
        }

    }// End of AdapterListProduct

    public void ChangePrintWidth(LinearLayout ll) {

        LayoutParams param = ll.getLayoutParams();
        final float scale = getResources().getDisplayMetrics().density;
        //convert mm to dp
        double Size = SharedPreferencesHelper.getCurrentWidthSize(mContext) * 6.3;
        int converter = (int) (Size * scale + 0.5f);
        param.width = converter;
        ll.setLayoutParams(param);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        outState.putInt(POSITION_KEY, PositionGroup);
        outState.putLong(GROUPID_KEY, GroupId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);

        PositionGroup = savedInstanceState.getInt(POSITION_KEY);
        GroupId = savedInstanceState.getLong(GROUPID_KEY);
    }

    @Override
    public void onBackPressed() {

        Clear();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    public void SendReceive() {

        db.open();
        final User user = db.getUser();
        LoginBody loginBody = new LoginBody();
        //String DeviceID = ServiceTools.getDeviceID(mContext);
        loginBody.setAppId("MahakOrder");
        loginBody.setDatabaseId(0);
        loginBody.setLanguage("en-US");
        loginBody.setDeviceId("");
        loginBody.setDescription("login");
        loginBody.setUserName(user.getUsername());
        loginBody.setPassword(user.getPassword());

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
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
                        new ReceiveAsyncTask(response.body().getData().getUserToken()).execute();
                        //setAndGetRequest(response.body().getData().getUserToken());
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

    class ReceiveAsyncTask extends AsyncTask<String, String, Integer> {
        String mUserToken;
        GetAllDataBody getAllDataBody;

        ReceiveAsyncTask(String UserToken) {
            mUserToken = UserToken;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... arg0) {

            db.open();
            getAllDataBody = new GetAllDataBody();
            getAllDataBody.setUserToken(mUserToken);

            CustomerMaxRowVersion = db.getMaxRowVersion(DbSchema.Customerschema.TABLE_NAME);
            ExtraDataMaxRowVersion = db.getMaxRowVersion(DbSchema.ExtraDataSchema.TABLE_NAME);
            CustomersGroupRowVersion = db.getMaxRowVersion(DbSchema.CustomersGroupschema.TABLE_NAME);

            getAllDataBody.setFromPersonVersion(CustomerMaxRowVersion);
            getAllDataBody.setFromPersonGroupVersion(CustomersGroupRowVersion);
            getAllDataBody.setFromExtraDataVersion(ExtraDataMaxRowVersion);

            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {

            pd = new FontProgressDialog(mContext);

            final String[] mMsg = {""};

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<GetDataResult> getDataResultCall;
            getDataResultCall = apiService.GetAllData(getAllDataBody);
            pd.setMessage(getString(R.string.recieiving_info));
            pd.setCancelable(false);
            pd.show();
            getDataResultCall.enqueue(new Callback<GetDataResult>() {
                @Override
                public void onResponse(Call<GetDataResult> call, Response<GetDataResult> response) {
                    pd.dismiss();
                    if (response.body() != null && response.body().isResult()) {
                        if (response.body().getData() != null) {
                            //swipeRefresh.setRefreshing(false);
                            personGroupLists = response.body().getData().getObjects().getPersonGroups();
                            customerLists = response.body().getData().getObjects().getPeople();
                            extraDataList = response.body().getData().getObjects().getExtraData();
                            new SaveAsyncTask().execute();
                        }
                    } else if (response.body() != null) {
                        pd.dismiss();
                        mMsg[0] = response.body().getMessage();
                    }
                }

                @Override
                public void onFailure(Call<GetDataResult> call, Throwable t) {
                    pd.dismiss();
                    mMsg[0] = t.toString();
                }
            });
        }
    }

    class SaveAsyncTask extends AsyncTask<String, String, Integer> {
        SaveAsyncTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new FontProgressDialog(mContext);
            pd.setMessage(getString(R.string.storing_info));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            if (customerLists != null)
                if (customerLists.size() > 0)
                    DataService.InsertCustomer(db, customerLists, CustomerMaxRowVersion);
            if (personGroupLists != null)
                if (personGroupLists.size() > 0) {
                    DataService.InsertCustomerGroup(db, personGroupLists);
                }
            if (extraDataList.size() > 0)
                    DataService.InsertExtraInfo(db, customerLists , extraDataList, ExtraDataMaxRowVersion);
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            pd.dismiss();
            ReadALLCustomer();
            arrayGroup.addAll(personGroupLists);
            adspinner.notifyDataSetChanged();
        }

    }


}
