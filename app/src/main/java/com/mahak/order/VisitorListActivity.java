package com.mahak.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filter.FilterListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mahak.order.common.CustomerGroup;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.Visitor;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.widget.FontPopUp;

import java.util.ArrayList;

public class VisitorListActivity extends BaseActivity {

    private static int REQUESTCODE_MANAGE_CUSTOMER = 1;
    private static long GroupId;
    private static int PositionGroup = 0;
    private static String GROUPID_KEY = "GroupId";
    private static String POSITION_KEY = "Position";
    private boolean FIRST_LOADE = false;
    private Context mContext;
    private Activity mActivity;
    private ListView lstVisitor;
    private DbAdapter db;
    //private ArrayList<CustomerTest> arrayCustomer = new ArrayList<CustomerTest>();
    private ArrayList<Visitor> arrayVisitor = new ArrayList<Visitor>();
    private AdapterVisitor adVisitor;
    private Spinner spnGroup;
    private long Id;
    private int Position;
    private Bundle Extras;
    private int Page;
    private EditText txtSearch;
    private AsynVisitor asynVisitor;
    private TextView tvPageTitle;
    public static int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customers_list);

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
        arrayVisitor = new ArrayList<Visitor>();

        Extras = getIntent().getExtras();

        if (Extras != null) {
            Page = Extras.getInt(PAGE);
            type = Extras.getInt(TYPE_KEY);
        }

        initialise();

        db.open();

        FillSpinner();

        if (type == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
            spnGroup.setVisibility(View.GONE);
        }

        if (savedInstanceState == null) {
            GroupId = ProjectInfo.DONT_CUSTOMER_GROUP;
            PositionGroup = 0;
        } else {
            GroupId = savedInstanceState.getLong(GROUPID_KEY);
            PositionGroup = savedInstanceState.getInt(POSITION_KEY);
            spnGroup.setSelection(PositionGroup);
        }


        lstVisitor.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Visitor visitor = (Visitor) parent.getItemAtPosition(position);
                if (Page == PAGE_DASHBORD) {
                    Intent intent = new Intent(mContext, ManageCustomerActivity.class);
                    intent.putExtra(MODE_PAGE, MODE_EDIT);
                    intent.putExtra(ID, visitor.getId());
                    startActivityForResult(intent, REQUESTCODE_MANAGE_CUSTOMER);
                } else if (Page == PAGE_Invoice_Detail_Activity) {
                    Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                    intent.putExtra(_Key_VisitorID, visitor.getId());
                    intent.putExtra(_Key_StoreCode, visitor.getStoreCode());
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (Page == PAGE_MANAGE_RECEIPT) {
                    Intent intent = new Intent(mContext, ManageReceiptActivity.class);
                    intent.putExtra(CUSTOMERID_KEY, visitor.getId());
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (Page == PAGE_ADD_SEND_TRANSFER) {
                    Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                    intent.putExtra(_Key_VisitorID, visitor.getId());
                    intent.putExtra(_Key_StoreCode, visitor.getStoreCode());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adVisitor != null)
                    adVisitor.getFilter().filter(s, new FilterListener() {

                        @Override
                        public void onFilterComplete(int count) {
                            tvPageTitle.setText(getString(R.string.str_nav_visitor_list) + "(" + count + ")");
                        }
                    });
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Read All Visitor//////////////////////////
        asynVisitor = new AsynVisitor();
        asynVisitor.execute();

    }//End Of OnCreate

    /**
     * Initializing Variables
     */
    private void initialise() {

        db = new DbAdapter(mContext);
        lstVisitor = (ListView) findViewById(R.id.lstCustomer);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        spnGroup = (Spinner) findViewById(R.id.spnGroup);

        adVisitor = new AdapterVisitor(mActivity, arrayVisitor);
        lstVisitor.setAdapter(adVisitor);
        tvPageTitle.setText(getString(R.string.str_nav_visitor_list) + "(" + lstVisitor.getCount() + ")");
    }

    /**
     * Read All CustomerTest From Database
     */
    private void ReadALLVisitor() {

        ArrayList<Visitor> arrayTemp;
        arrayVisitor = new ArrayList<Visitor>();

        if (Page == PAGE_DASHBORD || Page == PAGE_Invoice_Detail_Activity || Page == PAGE_ADD_SEND_TRANSFER) {
            arrayVisitor = db.getAllVisitor();
        }
    }

    /**
     * Fill Spinner CustomerTest Group
     */
    private void FillSpinner() {
        ArrayList<CustomerGroup> arrayTemp = new ArrayList<CustomerGroup>();
        CustomerGroup group = new CustomerGroup();
        group.setId(ProjectInfo.DONT_CUSTOMER_GROUP);
        group.setName(getResources().getString(R.string.str_all));
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

    private class AdapterVisitor extends ArrayAdapter<Visitor> {
        Activity mcontaxt;
        ArrayList<Visitor> arrayorginal;
        ArrayList<Visitor> arrayVisitor;
        CustomFilterList Filter;

        public class CustomFilterList extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();
                Boolean CheckFilter = false;
                String UserName;

                if (constraint != null && constraint.toString().length() > 0) {
                    ArrayList<Visitor> filterItem = new ArrayList<Visitor>();
                    for (int i = 0; i < arrayorginal.size(); i++) {
                        Visitor visitor = arrayorginal.get(i);
                        UserName = visitor.getName();
                        boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), UserName.toLowerCase());
                        //if(CustomerName.toLowerCase().contains(constraint))
                        if (result_contain) {
                            filterItem.add(visitor);
                            CheckFilter = true;
                        }
                    }//End of for
                    if (CheckFilter == false) {
                        for (int i = 0; i < arrayorginal.size(); i++) {
                            Visitor visitor = arrayorginal.get(i);
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

                arrayVisitor = (ArrayList<Visitor>) results.values;
                notifyDataSetChanged();
                clear();
                for (int i = 0; i < arrayVisitor.size(); i++) {
                    add(arrayVisitor.get(i));
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

            public void Populate(Visitor visitor) {

                tvCustomerName.setText(visitor.getName());

                if (visitor.getId() == ProjectInfo.CUSTOMERID_GUEST)
                    btnMenu.setVisibility(View.INVISIBLE);

                if (type == ProjectInfo.TYPE_SEND_TRANSFERENCE) {

                    btnMenu.setVisibility(View.INVISIBLE);
                    tvRemained.setVisibility(View.INVISIBLE);
                    tvCustomerStatus.setVisibility(View.INVISIBLE);
                    tvAddress.setVisibility(View.INVISIBLE);
                    tvMarketName.setVisibility(View.INVISIBLE);
                }


            }
        }// End of Holder

        @NonNull
        @Override
        public Filter getFilter() {
            if (Filter == null)
                Filter = new CustomFilterList();
            return Filter;
        }

        public AdapterVisitor(Activity contaxt, ArrayList<Visitor> array) {
            super(contaxt, android.R.layout.simple_list_item_1, array);
            mcontaxt = contaxt;
            arrayVisitor = new ArrayList<Visitor>();
            arrayorginal = new ArrayList<Visitor>();
            arrayVisitor.addAll(array);
            arrayorginal.addAll(array);
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowview = convertView;
            Holder holder = null;
            LayoutInflater inflater = null;
            final Visitor visitor = getItem(position);

            if (rowview == null) {
                inflater = mcontaxt.getLayoutInflater();
                rowview = inflater.inflate(R.layout.lst_customer_item, null, false);
                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();
            holder.Populate(visitor);
            final LinearLayout btnMenu = holder.btnMenu;
            holder.btnMenu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(mcontaxt, btnMenu);
                    MenuInflater inflater = popup.getMenuInflater();
                    //if (customer.getMobile().equals("")	&& customer.getMobile().equals(""))
                    //	 inflater.inflate(R.menu.pmenu_delete, popup.getMenu());
                    //else
                    inflater.inflate(R.menu.pmenu_call, popup.getMenu());

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


    public class AsynVisitor extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPreExecute() {
            setSupportProgressBarIndeterminateVisibility(true);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {

            ReadALLVisitor();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            setSupportProgressBarIndeterminateVisibility(false);

            adVisitor = new AdapterVisitor(mActivity, arrayVisitor);
            lstVisitor.setAdapter(adVisitor);
            tvPageTitle.setText(getString(R.string.str_nav_visitor_list) + "(" + lstVisitor.getCount() + ")");

            super.onPostExecute(result);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_MANAGE_CUSTOMER) {
                // Read All CUstomer//////////////////////////
                asynVisitor = new AsynVisitor();
                asynVisitor.execute();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

		/*menu.add(0, 0, 0, R.string.str_add_customer).setIcon(R.drawable.ic_add_inverse)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
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
                if (asynVisitor != null) {
                    if (asynVisitor.getStatus() == Status.RUNNING) {
                        asynVisitor.cancel(true);
                    }
                }
                Clear();
                break;
        }
        return super.onOptionsItemSelected(item);
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

        if (asynVisitor != null) {
            if (asynVisitor.getStatus() == Status.RUNNING) {
                asynVisitor.cancel(true);
            }
        }
        Clear();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }
}
