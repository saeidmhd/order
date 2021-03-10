package com.mahak.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filter.FilterListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mahak.order.apiHelper.ApiClient;
import com.mahak.order.apiHelper.ApiInterface;
import com.mahak.order.common.Customer;
import com.mahak.order.common.Order;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.OrderDetailProperty;
import com.mahak.order.common.Person_Extra_Data;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.User;
import com.mahak.order.common.VisitorPeople;
import com.mahak.order.common.login.LoginBody;
import com.mahak.order.common.login.LoginResult;
import com.mahak.order.common.request.SetAllDataBody;
import com.mahak.order.common.request.SetAllDataResult.SaveAllDataResult;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.widget.FontDialog;
import com.mahak.order.widget.FontPopUp;
import com.mahak.order.widget.FontProgressDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mahak.order.common.ServiceTools.getDateAndTimeForLong;

//import com.mahak.order.common.ProductInReturn;

public class ReturnsListActivity extends BaseActivity {

    private static final int REQUEST_CUSTOMER_LIST = 2;
    private ListView lstReturn;
    private DbAdapter db;
    private Context mContext;
    private Activity mActivity;
    private Customer customer;
    private ArrayList<Order> arrayOrder = new ArrayList<>();

    private AdapterListReturns returnAdapter;
    private long ReturnId;
    private int Position;
    private EditText txtSearch;
    private Bundle Extras;
    private int Type, Page;
    private static int REQUEST_EDIT = 1;
    private String Name, LastName, MarketName, Address;
    private String Description = "";
    private TextView tvPageTitle;

    private static final int MENU_ADD = 1;
    private static final int MENU_SORT = 2;
    boolean enable_add, enable_send_transfer, enable_receive_transfer;
    private int CustomerId;
    private long customerClientId;
    private FontProgressDialog pd;
    private int sortType = ProjectInfo.SortDesc;

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
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
        tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        tvPageTitle.setText(getString(R.string.str_nav_returnOfSale_list));
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________

        initialise();
        db.open();

        FillView();

        lstReturn.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Clear();
                Order returnOfSale = (Order) parent.getItemAtPosition(position);
                Intent intent = new Intent(mContext, ReturnDetailActivity.class);
                // Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra(ID, returnOfSale.getId());
                intent.putExtra(PAGE, PAGE_ORDERLIST);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_RETURN_OF_SALE);
                startActivity(intent);

            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                {
                    returnAdapter.getFilter().filter(s, new FilterListener() {

                        @Override
                        public void onFilterComplete(int count) {

                            tvPageTitle.setText(getString(R.string.str_nav_returnOfSale_list) + "(" + count + ")");
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

        lstReturn = (ListView) findViewById(R.id.lstOrder);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        db = new DbAdapter(mContext);
        tvPageTitle.setText(getString(R.string.str_nav_returnOfSale_list) + "(" + lstReturn.getCount() + ")");
    }

    /**
     * Read From Database And Fill Adapter
     */
    private void FillView() {
        arrayOrder = db.getAllReturnOfSale();
        Collections.reverse(arrayOrder);

        for (Order order : arrayOrder) {

            if (order.getPersonId() != ProjectInfo.CUSTOMERID_GUEST) {
                customer = db.getCustomerWithPersonId(order.getPersonId());

            } else {
                customer = db.getCustomerWithPersonClientId(order.getPersonClientId());

            }
            order.setMarketName(customer.getOrganization());
            order.setCustomerName(customer.getName());
            if (customer.getPersonCode() != 0) {
                Person_Extra_Data extraData = db.getMoreCustomerInfo(customer.getPersonCode());
                order.setAddress(extraData.getStoreAddress());
            }else {
                order.setAddress(customer.getAddress());
            }
        }//end of For
        returnAdapter = new AdapterListReturns(mActivity, arrayOrder);
        lstReturn.setAdapter(returnAdapter);
        tvPageTitle.setText(getString(R.string.str_nav_returnOfSale_list) + "(" + lstReturn.getCount() + ")");
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

    public class AdapterListReturns extends ArrayAdapter<Order> {
        Activity mcontaxt;
        ArrayList<Order> arrayorginal;
        ArrayList<Order> arrayReturnSale;
        CustomFilterList Filter;

        public AdapterListReturns(Activity contaxt, ArrayList<Order> array) {
            super(contaxt, android.R.layout.simple_list_item_1, array);
            mcontaxt = contaxt;
            arrayorginal = new ArrayList<Order>();
            arrayReturnSale = new ArrayList<Order>();
            arrayReturnSale.addAll(array);
            arrayorginal.addAll(array);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowview = convertView;
            Holder holder = null;
            LayoutInflater inflater = null;
            final Order order = getItem(position);

            if (rowview == null) {
                inflater = mcontaxt.getLayoutInflater();
                rowview = inflater.inflate(R.layout.lst_order_item, null, false);

                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();

            holder.Populate(order);
            final LinearLayout btnMenu = holder.btnMenu;
            holder.btnMenu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    PopupMenu popup = new PopupMenu(mcontaxt, btnMenu);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.pmenu_edit_delete, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.mnuDelete:
                                    ReturnId = order.getId();
                                    Position = position;
                                    if (order.getPublish() == ProjectInfo.DONT_PUBLISH) {
                                        Dialogdelete();
                                    } else
                                        Toast.makeText(mContext, getResources().getString(R.string.str_message_publish_delete), Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.mnuEdit:
                                    if (order.getPublish() == ProjectInfo.DONT_PUBLISH) {
                                        Intent intent = new Intent(mcontaxt, InvoiceDetailActivity.class);
                                        intent.putExtra(BaseActivity.CUSTOMERID_KEY, order.getPersonId());
                                        intent.putExtra(BaseActivity.CUSTOMER_CLIENT_ID_KEY, order.getPersonClientId());
                                        intent.putExtra(BaseActivity.MODE_PAGE, BaseActivity.MODE_EDIT);
                                        intent.putExtra(BaseActivity.ID, order.getId());
                                        intent.putExtra(BaseActivity.TYPE_KEY, ProjectInfo.TYPE_RETURN_OF_SALE);
                                        startActivityForResult(intent, REQUEST_EDIT);
                                    } else
                                        Toast.makeText(mContext, getResources().getString(R.string.str_message_publish_edit), Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.mnuSend:
                                    ReturnId = order.getId();
                                    Position = position;
                                    if (order.getPublish() == ProjectInfo.DONT_PUBLISH) {
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
                        FontPopUp.applyFontToMenuItem(mi, mcontaxt);
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
                //  description = view.findViewById(R.id.description);
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

            public void Populate(Order order) {

                tvDate.setText(getDateAndTimeForLong(order.getOrderDate()));
                tvAddress.setText(order.getAddress());
                tvMarketName.setText(order.getMarketName());
                tvCustomerName.setText(order.getCustomerName());
                tvOrderCode.setText(order.getCode());
                llPrice.setVisibility(View.GONE);
                //calculate FinalPrice________________________________________________________

                //Check Publish________________________________________________________________
                if (order.getPublish() == ProjectInfo.DONT_PUBLISH)
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

                if (constraint.toString().length() > 0) {
                    ArrayList<Order> filterItem = new ArrayList<Order>();
                    for (int i = 0; i < arrayorginal.size(); i++) {
                        Order returnOfSale = arrayorginal.get(i);
                        InvoiceNumber = returnOfSale.getCode();
                        boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), InvoiceNumber.toLowerCase());
                        if (result_contain) {
                            filterItem.add(returnOfSale);
                            CheckFilter = true;
                        }
                    }//End of for
                    if (!CheckFilter) {
                        for (int i = 0; i < arrayorginal.size(); i++) {
                            Order returnOfSale = arrayorginal.get(i);
                            CustomerName = returnOfSale.getCustomerName();
                            boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), CustomerName.toLowerCase());
                            if (result_contain) {
                                filterItem.add(returnOfSale);
                                CheckFilter = true;
                            }
                        }//End of for
                    }
                    if (!CheckFilter) {
                        for (int i = 0; i < arrayorginal.size(); i++) {
                            Order returnOfSale = arrayorginal.get(i);
                            MarketName = returnOfSale.getMarketName();
                            boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), MarketName.toLowerCase());
                            if (result_contain) {
                                filterItem.add(returnOfSale);
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
                arrayReturnSale = (ArrayList<Order>) results.values;
                notifyDataSetChanged();
                clear();
                for (int i = 0; i < arrayReturnSale.size(); i++) {
                    add(arrayReturnSale.get(i));
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
                db.DeleteOrder(ReturnId);
                db.DeleteOrderDetail(ReturnId);
                db.DeleteOrderDetailProperty(ReturnId);
                //_______________________________________________________________________________
                arrayOrder.remove(Position);
                returnAdapter.notifyDataSetChanged();
                tvPageTitle.setText(getString(R.string.str_nav_returnOfSale_list) + "(" + lstReturn.getCount() + ")");
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
            } else if (requestCode == REQUEST_CUSTOMER_LIST) {

                CustomerId = data.getIntExtra(CUSTOMERID_KEY, 0);
                customerClientId = data.getLongExtra(CUSTOMER_CLIENT_ID_KEY, 0);
                Intent intent = new Intent(getApplicationContext(), InvoiceDetailActivity.class);
                intent.putExtra(PAGE, PAGE_ADD_RETURN);
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_RETURN_OF_SALE);
                intent.putExtra(MODE_PAGE, MODE_NEW);
                intent.putExtra(CUSTOMERID_KEY, CustomerId);
                intent.putExtra(CUSTOMER_CLIENT_ID_KEY, customerClientId);
                startActivity(intent);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, MENU_ADD, 0, R.string.str_add_return).setIcon(R.drawable.ic_add_inverse)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, MENU_SORT, 0, R.string.str_sort).setIcon(R.drawable.ic_sort)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case MENU_ADD:
                Intent intent = new Intent(mContext, PeopleListActivity.class);
                intent.putExtra(PAGE, PAGE_ADD_RETURN);
                intent.putExtra(MODE_PAGE, MODE_NEW);
                startActivityForResult(intent, REQUEST_CUSTOMER_LIST);
                break;
            case MENU_SORT:
                View menuItemView = findViewById(MENU_SORT);
                PopupMenu popupMenu = new PopupMenu(mContext, menuItemView);
                popupMenu.inflate(R.menu.pmenu_sort);
                if (sortType == ProjectInfo.SortAsc) {
                    popupMenu.getMenu().getItem(2).setChecked(true);
                } else if (sortType == ProjectInfo.SortDesc) {
                    popupMenu.getMenu().getItem(1).setChecked(true);
                }
                popupMenu.show();
                Menu menu = popupMenu.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem mi = menu.getItem(i);
                    FontPopUp.applyFontToMenuItem(mi, mContext);
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void sortItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortAsc:
                if (!(sortType == ProjectInfo.SortAsc)) {
                    sortType = ProjectInfo.SortAsc;
                    item.setChecked(true);
                    Collections.reverse(arrayOrder);
                    returnAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.sortDesc:
                if (!(sortType == ProjectInfo.SortDesc)) {
                    sortType = ProjectInfo.SortDesc;
                    item.setChecked(true);
                    Collections.reverse(arrayOrder);
                    returnAdapter.notifyDataSetChanged();
                }
                break;
        }
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
                        new SendOrderAsyncTask(response.body().getData().getUserToken(), ReturnId, mContext).execute();
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

    class SendOrderAsyncTask extends AsyncTask<String, String, Integer> {
        List<Order> arrayInvoice = new ArrayList<>();
        List<OrderDetail> arrayInvoiceDetail = new ArrayList<>();
        List<OrderDetail> orderDetails = new ArrayList<>();
        List<OrderDetailProperty> orderDetailProperties = new ArrayList<>();
        List<VisitorPeople> visitorPeopleArrayList = new ArrayList<>();
        List<Customer> Customers = new ArrayList<>();
        String mUserToken;
        long returnId;
        String code;
        Context mContext;
        FontProgressDialog sendpd;

        SendOrderAsyncTask(String UserToken, long returnId, Context mContext) {
            mUserToken = UserToken;
            this.returnId = returnId;
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sendpd = new FontProgressDialog(mContext);
            sendpd.setMessage(getString(R.string.sending_info));
            sendpd.setCancelable(false);
            sendpd.show();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            DbAdapter db = new DbAdapter(mContext);
            db.open();
            arrayInvoice.add(db.GetOrder(returnId));
            Set<OrderDetail> set = new LinkedHashSet<>();
            for (int i = 0; i < arrayInvoice.size(); i++) {
                arrayInvoice.get(i).setOrderId(0);
                orderDetails = db.getAllOrderDetailForSend(arrayInvoice.get(i).getId());
                for (OrderDetail orderDetail : orderDetails) {
                    orderDetailProperties = db.getAllOrderDetailProperty(orderDetail.getOrderId(), orderDetail.getProductId());
                    if (orderDetailProperties.size() > 0) {
                        for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
                            if (orderDetailProperty.getSumCountBaJoz() > 0) {
                                set.add(createOrderDetail(orderDetailProperty, orderDetail));
                            }
                        }
                    } else
                        set.add(orderDetail);
                }
            }
            arrayInvoiceDetail = new ArrayList<>(set);
            if (arrayInvoice.get(0).getPersonClientId() != 0) {
                Customers.add(db.getCustomerWithPersonClientId(arrayInvoice.get(0).getPersonClientId()));
                for (Customer customer : Customers) {
                    VisitorPeople visitorPeople = new VisitorPeople();
                    visitorPeople.setPersonClientId(customer.getPersonClientId());
                    visitorPeople.setVisitorId(customer.getUserId());
                    visitorPeopleArrayList.add(visitorPeople);
                }
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {

            sendpd.setMessage(getString(R.string.update_tables));
            sendpd.setCancelable(false);
            sendpd.show();

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            SetAllDataBody setAllDataBody = new SetAllDataBody();
            setAllDataBody.setUserToken(mUserToken);
            setAllDataBody.setOrderDetails(arrayInvoiceDetail);
            setAllDataBody.setOrders(arrayInvoice);
            setAllDataBody.setVisitorPeople(visitorPeopleArrayList);
            setAllDataBody.setPeople(Customers);
            Call<SaveAllDataResult> saveAllDataResultCall = apiService.SaveAllData(setAllDataBody);

            saveAllDataResultCall.enqueue(new Callback<SaveAllDataResult>() {
                @Override
                public void onResponse(@NonNull Call<SaveAllDataResult> call, @NonNull Response<SaveAllDataResult> response) {
                    sendpd.dismiss();
                    if (response.body() != null && response.body().isResult()) {
                        DbAdapter db = new DbAdapter(mContext);
                        db.open();
                        if (arrayInvoice.size() > 0) {
                            for (int i = 0; i < arrayInvoice.size(); i++) {
                                arrayInvoice.get(i).setOrderId(response.body().getData().getObjects().getOrders().getResults().get(i).getEntityID());
                                arrayInvoice.get(i).setPublish(ProjectInfo.PUBLISH);
                                db.UpdateOrder(arrayInvoice.get(i));
                                for (int j = 0; j < arrayInvoiceDetail.size(); j++) {
                                    arrayInvoiceDetail.get(j).setOrderDetailId(response.body().getData().getObjects().getOrderDetails().getResults().get(j).getEntityID());
                                    db.UpdateOrderDetail(arrayInvoiceDetail.get(j));
                                }
                            }
                        }
                        if (Customers.size() > 0) {
                            for (int i = 0; i < Customers.size(); i++) {
                                Customers.get(i).setPersonId(response.body().getData().getObjects().getPeople().getResults().get(i).getEntityID());
                                db.UpdateCustomerWithClientId(Customers.get(i));
                            }
                        }
                        db.close();

                        arrayOrder.get(Position).setPublish(ProjectInfo.PUBLISH);
                        returnAdapter.notifyDataSetChanged();
                        Toast.makeText(mContext, getString(R.string.sent), Toast.LENGTH_SHORT).show();

                    } else if (response.body() != null) {
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        sendpd.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<SaveAllDataResult> call, Throwable t) {
                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                    sendpd.dismiss();
                }
            });
        }
    }

    private OrderDetail createOrderDetail(OrderDetailProperty orderDetailProperty, OrderDetail orderDetail) {

        OrderDetail orderDetailSend = new OrderDetail();

        orderDetailSend.setOrderClientId(orderDetail.getOrderClientId());
        orderDetailSend.setOrderDetailClientId(orderDetailProperty.getOrderDetailClientId());

        orderDetailSend.setProductId(orderDetailProperty.getProductId());
        orderDetailSend.setProductDetailId(orderDetailProperty.getProductDetailId());

        orderDetailSend.setCount2(orderDetailProperty.getCount2());
        if (BaseActivity.getPrefUnit2Setting(mContext) == MODE_MeghdarJoz)
            orderDetailSend.setCount1(orderDetailProperty.getSumCountBaJoz());
        else
            orderDetailSend.setCount1(orderDetailProperty.getCount1());
        orderDetailSend.setSumCountBaJoz(orderDetailProperty.getSumCountBaJoz());

        orderDetailSend.setDeleted(orderDetail.isDeleted());
        orderDetailSend.setDescription(orderDetail.getDescription());
        orderDetailSend.setDiscountType(orderDetail.getDiscountType());
        orderDetailSend.setDiscount(orderDetail.getDiscount());
        orderDetailSend.setPrice("" + orderDetail.getPrice());
        orderDetailSend.setGiftCount1(orderDetail.getGiftCount1());
        orderDetailSend.setGiftCount2(orderDetail.getGiftCount2());
        orderDetailSend.setGiftType(orderDetail.getGiftType());
        orderDetailSend.setTaxPercent(orderDetail.getTaxPercent());
        orderDetailSend.setChargePercent(orderDetail.getChargePercent());

        return orderDetailSend;

    }

}
