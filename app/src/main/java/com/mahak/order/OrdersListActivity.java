package com.mahak.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.Button;
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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.mahak.order.apiHelper.ApiClient;
import com.mahak.order.apiHelper.ApiInterface;
import com.mahak.order.common.Cheque;
import com.mahak.order.common.Customer;
import com.mahak.order.common.Order;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.OrderDetailProperty;
import com.mahak.order.common.Person_Extra_Data;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductDetail;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.Receipt;
import com.mahak.order.common.ReceivedTransferProducts;
import com.mahak.order.common.ReceivedTransfers;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.User;
import com.mahak.order.common.Visitor;
import com.mahak.order.common.VisitorPeople;
import com.mahak.order.common.login.LoginBody;
import com.mahak.order.common.login.LoginResult;
import com.mahak.order.common.request.GetAllDataBody;
import com.mahak.order.common.request.GetAllDataResult.GetDataResult;
import com.mahak.order.common.request.SetAllDataBody;
import com.mahak.order.common.request.SetAllDataResult.SaveAllDataResult;
import com.mahak.order.service.DataService;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.storage.DbSchema;
import com.mahak.order.widget.FontDialog;
import com.mahak.order.widget.FontPopUp;
import com.mahak.order.widget.FontProgressDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.mahak.order.common.ProductInOrder;

public class OrdersListActivity extends BaseActivity {

    private static final int REQUEST_CUSTOMER_LIST = 2;
    private static final int REQUEST_USER_LIST = 3;
    private ListView lstOrder;
    private DbAdapter db;
    private Context mContext;
    private Activity mActivity;
    private Customer customer;
    private ArrayList<Order> arrayOrder = new ArrayList<>();
    private ArrayList<ReceivedTransfers> arrayReceivedTransfer = new ArrayList<>();
    private ArrayList<ReceivedTransferProducts> arrayReceivedTransferProducts;
    private ArrayList<OrderDetail> orderDetails;

    private AdapterListOrder adOrder;
    private AdapterReceivedTransfer adReceivedTransfer;
    private long OrderId;
    private String Code;
    private int Position;
    private EditText txtSearch;
    private ConstraintLayout empty, emptyReceive;
    private Bundle Extras;
    private int Type, Page;
    private static int REQUEST_EDIT = 1;
    private String Description = "";
    private TextView tvPageTitle;
    private Button btnSend, btnReceived;
    private ConstraintLayout llReceived;
    private Visitor visitor;

    public static int OrderType = 0;
    private static int SYNC_TYPE_SUCCESS = 1;
    private static int SYNC_TYPE_FAILED = 2;
    private static int SYNC_TYPE_NO_NEED = 3;
    private static int SYNC_TYPE_MSG_SERVER = 4;

    private static String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    private static String PA_APP_SIGN = "AppSign";
    private static String PA_DATA = "Data";
    private static String PA_USER_ID = "UserId";
    private static String PA_MAHAK_ID = "MahakId";
    private static String PA_DATABASE_ID = "DatabaseId";
    private static String PA_LASTUPDATE = "LastUpdate";

    private Menu menu;
    private MenuItem Add_Item,
            SEND_TRANSFER_Item,
            RECEIVE_TRANSFER_Item;

    private static final int MENU_ADD = Menu.FIRST;
    private static final int MENU_SEND_TRANSFER = Menu.FIRST + 1;
    private static final int MENU_RECEIVE_TRANSFER = Menu.FIRST + 2;
    private static final int MENU_SORT = Menu.FIRST + 3;

    boolean enable_add, enable_send_transfer, enable_receive_transfer;
    private int CustomerId;
    private long VisitorId;
    private long CustomerClientId;
    private long GroupId;
    public static long StoreCode = 0;
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
        if (Type == ProjectInfo.TYPE_ORDER)
            tvPageTitle.setText(getString(R.string.str_nav_order_list));
        else if (Type == ProjectInfo.TYPE_INVOCIE)
            tvPageTitle.setText(getString(R.string.str_nav_invoice_list));
        else if (Type == ProjectInfo.TYPE_SEND_TRANSFERENCE)
            tvPageTitle.setText(getString(R.string.str_nav_transfer_list));
        else if (Type == ProjectInfo.TYPE_RECEIVE_TRANSFERENCE)
            tvPageTitle.setText(getString(R.string.Received_transfer_list));
        else if (Type == ProjectInfo.TYPE_RETURN_OF_SALE)
            tvPageTitle.setText(getString(R.string.Received_transfer_list));
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________

        initialise();
        db.open();


        lstOrder.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (Page != PAGE_MANAGE_RECEIPT && Type != ProjectInfo.TYPE_SEND_TRANSFERENCE && Type != ProjectInfo.TYPE_RECEIVE_TRANSFERENCE) {
                    Order order = (Order) parent.getItemAtPosition(position);
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra(PAGE, PAGE_ORDERLIST);
                    intent.putExtra(ID, order.getId());
                    startActivity(intent);
                } else if (Page != PAGE_MANAGE_RECEIPT && Type == ProjectInfo.TYPE_RETURN_OF_SALE) {
                    Order order = (Order) parent.getItemAtPosition(position);
                    Intent intent = new Intent(mContext, ReturnDetailActivity.class);
                    //Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra(PAGE, PAGE_ORDERLIST);
                    intent.putExtra(ID, order.getId());
                    startActivity(intent);
                } else if (Page != PAGE_MANAGE_RECEIPT && Type == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
                    Order order = (Order) parent.getItemAtPosition(position);
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra(PAGE, PAGE_ORDERLIST);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_SEND_TRANSFERENCE);
                    intent.putExtra(ID, order.getId());
                    intent.putExtra(RETURN_PUBLISH_KEY, order.getPublish());
                    startActivity(intent);
                } else if (Page == PAGE_MANAGE_RECEIPT) {
                    Order order = (Order) parent.getItemAtPosition(position);
                    Intent intent = new Intent(mContext, ManageReceiptActivity.class);
                    intent.putExtra(CODE_KEY, order.getCode());
                    intent.putExtra(PAYMENT_KEY, order.getFinalPrice());
                    intent.putExtra(CUSTOMERID_KEY, order.getPersonId());
                    intent.putExtra(CUSTOMER_CLIENT_ID_KEY, order.getPersonClientId());
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (Page != PAGE_MANAGE_RECEIPT && Type == ProjectInfo.TYPE_RECEIVE_TRANSFERENCE) {
                    ReceivedTransfers receivedTransfers = (ReceivedTransfers) parent.getItemAtPosition(position);
                    Intent intent = new Intent(mContext, TransferDetailActivity.class);
                    intent.putExtra(PAGE, PAGE_ORDERLIST);
                    intent.putExtra(ID, receivedTransfers.getTransferStoreId());
                    startActivity(intent);
                }
            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (Type == ProjectInfo.TYPE_RECEIVE_TRANSFERENCE) {

                    adReceivedTransfer.getFilter().filter(s, new FilterListener() {

                        @Override
                        public void onFilterComplete(int count) {

                            tvPageTitle.setText(getString(R.string.str_nav_transfer_list) + "(" + count + ")");
                        }
                    });
                } else {

                    adOrder.getFilter().filter(s, new FilterListener() {

                        @Override
                        public void onFilterComplete(int count) {

                            if (Type == ProjectInfo.TYPE_ORDER)
                                tvPageTitle.setText(getString(R.string.str_nav_order_list) + "(" + count + ")");
                            else if (Type == ProjectInfo.TYPE_INVOCIE)
                                tvPageTitle.setText(getString(R.string.str_nav_invoice_list) + "(" + count + ")");
                            else if (Type == ProjectInfo.TYPE_SEND_TRANSFERENCE)
                                tvPageTitle.setText(getString(R.string.str_nav_transfer_list) + "(" + count + ")");
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }

    /**
     * Initializing Variables
     */
    private void initialise() {

        lstOrder = (ListView) findViewById(R.id.lstOrder);

        empty = (ConstraintLayout) findViewById(R.id.empty);
        emptyReceive = (ConstraintLayout) findViewById(R.id.emptyReceive);

        if (Type == ProjectInfo.TYPE_SEND_TRANSFERENCE) {

            empty.setVisibility(View.VISIBLE);
            emptyReceive.setVisibility(View.GONE);
            lstOrder.setEmptyView(empty);
        }

        txtSearch = (EditText) findViewById(R.id.txtSearch);
        btnReceived = (Button) findViewById(R.id.btnReceived);
        btnSend = (Button) findViewById(R.id.btnSend);
        llReceived = (ConstraintLayout) findViewById(R.id.llReceived);


        db = new DbAdapter(mContext);

        if (Type == ProjectInfo.TYPE_ORDER)
            tvPageTitle.setText(getString(R.string.str_nav_order_list) + "(" + lstOrder.getCount() + ")");
        else if (Type == ProjectInfo.TYPE_INVOCIE)
            tvPageTitle.setText(getString(R.string.str_nav_invoice_list) + "(" + lstOrder.getCount() + ")");
        else if (Type == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
            tvPageTitle.setText(getString(R.string.str_nav_transfer_list) + "(" + lstOrder.getCount() + ")");
            llReceived.setVisibility(View.VISIBLE);
        }

    }

    /**
     * Read From Database And Fill Adapter
     */
    private void FillView() {
        if (Type == ProjectInfo.TYPE_ORDER) {
            arrayOrder = db.getAllOrder();
            Collections.reverse(arrayOrder);
        } else if (Type == ProjectInfo.TYPE_INVOCIE) {
            arrayOrder = db.getAllInvoice();
            Collections.reverse(arrayOrder);
        } else if (Type == ProjectInfo.TYPE_SEND_TRANSFERENCE)
            arrayOrder = db.getAllTransfer();

        for (Order order : arrayOrder) {
            if (Type == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
                visitor = db.getVisitor(order.getPersonId());
                order.setCustomerName(visitor.getName());
                order.setMarketName("");
                order.setAddress("");
            } else {
                if (order.getPersonId() == ProjectInfo.CUSTOMERID_GUEST)
                    customer = db.getCustomerWithPersonClientId(order.getPersonClientId());
                else
                    customer = db.getCustomerWithPersonId(order.getPersonId());
                order.setMarketName(customer.getOrganization());
                order.setCustomerName(customer.getName());

                if (customer.getPersonCode() != 0) {
                    Person_Extra_Data extraData = db.getMoreCustomerInfo(customer.getPersonCode());
                    order.setAddress(extraData.getStoreAddress());
                }else {
                    order.setAddress(customer.getAddress());
                }
            }
        }//end of For
        adOrder = new AdapterListOrder(mActivity, arrayOrder);
        lstOrder.setAdapter(adOrder);

        if (Type == ProjectInfo.TYPE_ORDER)
            tvPageTitle.setText(getString(R.string.str_nav_order_list) + "(" + lstOrder.getCount() + ")");
        else if (Type == ProjectInfo.TYPE_INVOCIE)
            tvPageTitle.setText(getString(R.string.str_nav_invoice_list) + "(" + lstOrder.getCount() + ")");
        else if (Type == ProjectInfo.TYPE_SEND_TRANSFERENCE)
            tvPageTitle.setText(getString(R.string.str_nav_transfer_list) + "(" + lstOrder.getCount() + ")");
    }

    public void btnReceived(View view) {


        db.open();
        Type = ProjectInfo.TYPE_RECEIVE_TRANSFERENCE;
        arrayReceivedTransfer = db.getAllReceivedTransfer();
        adReceivedTransfer = new AdapterReceivedTransfer(mActivity, arrayReceivedTransfer);
        lstOrder.setAdapter(adReceivedTransfer);
        tvPageTitle.setText(getString(R.string.Received_transfer_list) + "(" + lstOrder.getCount() + ")");
        db.close();

        lstOrder.setEmptyView(emptyReceive);
        empty.setVisibility(View.GONE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            btnReceived.setBackground(getResources().getDrawable(R.drawable.roundedbutton));
            btnSend.setBackground(getResources().getDrawable(R.drawable.round_transparent));
        }


        Add_Item = menu.findItem(MENU_ADD);
        SEND_TRANSFER_Item = menu.findItem(MENU_SEND_TRANSFER);
        RECEIVE_TRANSFER_Item = menu.findItem(MENU_RECEIVE_TRANSFER);

        Add_Item.setVisible(false);
        SEND_TRANSFER_Item.setVisible(false);
        RECEIVE_TRANSFER_Item.setVisible(true);


    }

    public void btnSendTransferList(View view) {

        db.open();
        Type = ProjectInfo.TYPE_SEND_TRANSFERENCE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            btnSend.setBackground(getResources().getDrawable(R.drawable.roundedbutton));
            btnReceived.setBackground(getResources().getDrawable(R.drawable.round_transparent));
        }
        FillView();

        lstOrder.setEmptyView(empty);
        emptyReceive.setVisibility(View.GONE);

        Add_Item = menu.findItem(MENU_ADD);
        SEND_TRANSFER_Item = menu.findItem(MENU_SEND_TRANSFER);
        RECEIVE_TRANSFER_Item = menu.findItem(MENU_RECEIVE_TRANSFER);

        Add_Item.setVisible(true);
        SEND_TRANSFER_Item.setVisible(true);
        RECEIVE_TRANSFER_Item.setVisible(false);

    }


    public class AdapterReceivedTransfer extends ArrayAdapter<ReceivedTransfers> {
        Activity mcontaxt;
        ArrayList<ReceivedTransfers> arrayorgin;
        ArrayList<ReceivedTransfers> arrayReceivedTransfers;
        CustomReceivedFilterList Filter;

        public AdapterReceivedTransfer(Activity contaxt, ArrayList<ReceivedTransfers> array) {
            super(contaxt, android.R.layout.simple_list_item_1, array);
            mcontaxt = contaxt;
            arrayorgin = new ArrayList<ReceivedTransfers>();
            arrayReceivedTransfers = new ArrayList<ReceivedTransfers>();
            arrayReceivedTransfers.addAll(array);
            arrayorgin.addAll(array);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowview = convertView;
            Holder holder = null;
            LayoutInflater inflater = null;
            final ReceivedTransfers receivedTransfers = getItem(position);

            if (rowview == null) {
                inflater = mcontaxt.getLayoutInflater();
                rowview = inflater.inflate(R.layout.lst_order_recieve_item, null, false);
                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();

            holder.Populate(receivedTransfers);
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
                                    break;
                                case R.id.mnuEdit:

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
            public TextView
                    txtAmount,
                    tvAmount,
                    tvOrderCode,
                    tvDate,
                    tvCustomerName,
                    textStatus,
                    comment;
            public LinearLayout btnMenu;
            public ImageView imgSync;
            public ImageView AcceptBtn, rejectBtn;

            public Holder(View view) {

                tvAmount = (TextView) view.findViewById(R.id.tvAmount);
                comment = (TextView) view.findViewById(R.id.comment);
                txtAmount = (TextView) view.findViewById(R.id.txtAmount);
                tvOrderCode = (TextView) view.findViewById(R.id.tvOrderCode);
                tvDate = (TextView) view.findViewById(R.id.tvDate);
                tvCustomerName = (TextView) view.findViewById(R.id.tvCustomerName);
                imgSync = (ImageView) view.findViewById(R.id.imgSync);
                btnMenu = (LinearLayout) view.findViewById(R.id.btnmenu);
                AcceptBtn = (ImageView) view.findViewById(R.id.Accept);
                rejectBtn = (ImageView) view.findViewById(R.id.reject);
                textStatus = (TextView) view.findViewById(R.id.textStatus);

            }

            public void Populate(final ReceivedTransfers receivedTransfers) {


                txtAmount.setText(R.string.str_total_count);
                tvOrderCode.setText(String.valueOf(receivedTransfers.getTransferStoreClientId()));
                tvDate.setText(ServiceTools.getDateAndTimeForLong(receivedTransfers.getTransferDate()));

                imgSync.setVisibility(View.GONE);
                if (receivedTransfers.getDescription() != null) {
                    comment.setText(R.string.str_title_description + receivedTransfers.getDescription());
                } else {
                    comment.setText(R.string.without_desc);
                }

                if (receivedTransfers.getIsAccepted() == ProjectInfo.TYPE_NaN) {
                    AcceptBtn.setVisibility(View.VISIBLE);
                    rejectBtn.setVisibility(View.VISIBLE);
                    textStatus.setVisibility(View.GONE);
                } else if (receivedTransfers.getIsAccepted() == ProjectInfo.TYPE_Reject) {

                    textStatus.setVisibility(View.VISIBLE);
                    textStatus.setText(R.string.cancelled);
                    textStatus.setTextColor(getResources().getColor(R.color.reject));
                    AcceptBtn.setVisibility(View.GONE);
                    rejectBtn.setVisibility(View.GONE);
                    imgSync.setVisibility(View.VISIBLE);
                    imgSync.setImageResource(R.drawable.reject_recieve_transfer);

                } else if (receivedTransfers.getIsAccepted() == ProjectInfo.TYPE_Accept) {

                    textStatus.setVisibility(View.VISIBLE);
                    textStatus.setText(R.string.approved);
                    textStatus.setTextColor(getResources().getColor(R.color.accept2));
                    AcceptBtn.setVisibility(View.GONE);
                    rejectBtn.setVisibility(View.GONE);
                    imgSync.setVisibility(View.VISIBLE);
                    imgSync.setImageResource(R.drawable.accepted_receive_transfer);

                }


                rejectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendResult(receivedTransfers, ProjectInfo.TYPE_Reject);

                    }
                });

                AcceptBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendResult(receivedTransfers, ProjectInfo.TYPE_Accept);

                    }
                });


                db.open();
                visitor = db.getVisitorWithVisitorID(ServiceTools.toLong(receivedTransfers.getSenderVisitorId()));
                tvCustomerName.setText(getString(R.string.from) + visitor.getName());

                tvAmount.setText(ServiceTools.formatCount(db.getTotalReceivedTransferById(receivedTransfers.getTransferStoreId())));
                db.close();

            }
        }

        public class CustomReceivedFilterList extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();
                Boolean CheckFilter = false;
                String CustomerName = "", MarketName = "", InvoiceNumber = "";

                if (constraint.toString().length() > 0) {
                    ArrayList<ReceivedTransfers> filterItem = new ArrayList<ReceivedTransfers>();
                    for (int i = 0; i < arrayorgin.size(); i++) {
                        ReceivedTransfers receivedTransfers = arrayorgin.get(i);
                        InvoiceNumber = String.valueOf(receivedTransfers.getTransferStoreClientId());
                        boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), InvoiceNumber);
                        if (result_contain) {
                            filterItem.add(receivedTransfers);
                            CheckFilter = true;
                        }
                    }//End of for
                    if (!CheckFilter) {
                        for (int i = 0; i < arrayorgin.size(); i++) {
                            ReceivedTransfers receivedTransfers = arrayorgin.get(i);
                            db.open();
                            visitor = db.getVisitorWithVisitorID(ServiceTools.toLong(receivedTransfers.getSenderVisitorId()));
                            CustomerName = visitor.getName();
                            boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), CustomerName);
                            if (result_contain) {
                                filterItem.add(receivedTransfers);
                                CheckFilter = true;
                            }
                        }//End of for
                    }
                    db.close();
                    if (!CheckFilter) {
                        for (int i = 0; i < arrayorgin.size(); i++) {
                            ReceivedTransfers receivedTransfers = arrayorgin.get(i);
                            //    MarketName = receivedTransfers.getOrganization();
                            boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), MarketName);
                            if (result_contain) {
                                filterItem.add(receivedTransfers);

                            }
                        }//End of for
                    }
                    result.values = filterItem;
                    result.count = filterItem.size();
                } else {
                    synchronized (this) {
                        result.values = arrayorgin;
                        result.count = arrayorgin.size();
                    }
                }
                return result;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                arrayReceivedTransfer = (ArrayList<ReceivedTransfers>) results.values;
                notifyDataSetChanged();
                clear();
                for (int i = 0; i < arrayReceivedTransfer.size(); i++) {
                    add(arrayReceivedTransfer.get(i));
                    notifyDataSetInvalidated();
                }
            }
        }

        @Override
        public Filter getFilter() {
            if (Filter == null)
                Filter = new CustomReceivedFilterList();
            return Filter;
        }

    }// End of AdapterReceivedTransfer

    private void sendResult(final ReceivedTransfers receivedTransfers, final int TYPE) {
        db.open();
        final ApiInterface apiService;
        SetAllDataBody setAllDataBody = new SetAllDataBody();
        User user = db.getUser();
        Call<SaveAllDataResult> saveAllDataResultCall;

        setAllDataBody.setUserToken(user.getUserToken());
        apiService = ApiClient.getClient().create(ApiInterface.class);

        final List<ReceivedTransfers> transferStores = new ArrayList<>();
        receivedTransfers.setIsAccepted(TYPE);
        transferStores.add(receivedTransfers);

        setAllDataBody.setReceivedTransfers(transferStores);
        saveAllDataResultCall = apiService.SaveAllData(setAllDataBody);

        saveAllDataResultCall.enqueue(new Callback<SaveAllDataResult>() {
            @Override
            public void onResponse(Call<SaveAllDataResult> call, Response<SaveAllDataResult> response) {
                if (response.body() != null && response.body().isResult()) {
                    // Toast.makeText(mContext, mMsg, Toast.LENGTH_SHORT).show();
                    db.open();
                    db.UpdateReceivedTransfer(String.valueOf(receivedTransfers.getTransferStoreClientId()), TYPE);
                    if (TYPE == ProjectInfo.TYPE_Accept) {
                        arrayReceivedTransferProducts = db.getReceivedTransferProducts(receivedTransfers.getTransferStoreId());
                        for (ReceivedTransferProducts item : arrayReceivedTransferProducts) {
                            db.UpdateProductFromTransfer(item.getProductDetailId(), item.getCount1(), item.getCount2());
                        }
                        Toast.makeText(mContext, R.string.transfer_has_been_added, Toast.LENGTH_SHORT).show();
                    }
                    adReceivedTransfer.notifyDataSetChanged();
                    tvPageTitle.setText(getString(R.string.Received_transfer_list) + "(" + lstOrder.getCount() + ")");
                    db.close();
                } else if (response.body() != null) {
                    Toast.makeText(mContext, R.string.error_in_send, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SaveAllDataResult> call, Throwable t) {
                Toast.makeText(mContext, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        db.close();
    }


    public class AdapterListOrder extends ArrayAdapter<Order> {
        Activity mcontaxt;
        ArrayList<Order> arrayorginal;
        ArrayList<Order> arrayorder;
        CustomFilterList Filter;

        public AdapterListOrder(Activity contaxt, ArrayList<Order> array) {
            super(contaxt, android.R.layout.simple_list_item_1, array);
            mcontaxt = contaxt;
            arrayorginal = new ArrayList<Order>();
            arrayorder = new ArrayList<Order>();
            arrayorder.addAll(array);
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
                if (Type == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
                    rowview = inflater.inflate(R.layout.lst_order_send_item, null, false);
                } else {
                    rowview = inflater.inflate(R.layout.lst_order_item, null, false);
                }

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
                                    OrderId = order.getId();
                                    Code = order.getCode();
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
                                        intent.putExtra(BaseActivity.TYPE_KEY, order.getOrderType());
                                        intent.putExtra(BaseActivity.MODE_PAGE, BaseActivity.MODE_EDIT);
                                        intent.putExtra(BaseActivity.ID, order.getId());
                                        if (order.getOrderType() == ProjectInfo.TYPE_ORDER)
                                            intent.putExtra(BaseActivity.PAGE, BaseActivity.PAGE_ADD_ORDER);
                                        else if (order.getOrderType() == ProjectInfo.TYPE_INVOCIE)
                                            intent.putExtra(BaseActivity.PAGE, BaseActivity.PAGE_ADD_INVOICE);
                                        else if (order.getOrderType() == ProjectInfo.TYPE_SEND_TRANSFERENCE)
                                            intent.putExtra(BaseActivity.PAGE, BaseActivity.PAGE_ADD_SEND_TRANSFER);
                                        startActivityForResult(intent, REQUEST_EDIT);
                                    } else
                                        Toast.makeText(mContext, getResources().getString(R.string.str_message_publish_edit), Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.mnuSend:
                                    OrderId = order.getId();
                                    Code = order.getCode();
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
            public TextView tvAmount, tvOrderCode, tvMarketName, tvDate, tvAddress, tvCustomerName, txtAddress, description,
                    txtMarketName, txtAmount, textStatus;
            public LinearLayout btnMenu;
            public ImageView imgSync;

            public Holder(View view) {

                txtAddress = (TextView) view.findViewById(R.id.txtAddress);
                description = (TextView) view.findViewById(R.id.comment);
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
            }

            public void Populate(Order order) {

                double Price = 0, Discount, FinalPrice;
                double count = 0;
                tvOrderCode.setText(order.getCode());
                tvDate.setText(ServiceTools.getDateAndTimeForLong(order.getOrderDate()));

                if (Type == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis((order.getDeliveryDate()));
                    tvDate.setText(ServiceTools.getDateAndTimeForLong((order.getDeliveryDate())) +
                            "  " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
                    tvCustomerName.setText(getString(R.string.to) + order.getCustomerName());
                    txtAmount.setText(R.string.str_total_count);
                    orderDetails = db.getAllOrderDetailWithOrderId(order.getId());
                    for (OrderDetail item : orderDetails) {

                        count += ServiceTools.getTotalCount(item);
                    }
                    tvAmount.setText("" + count);
                    Description = order.getDescription();
                    description.setText(R.string.str_description + Description);

                    //Check Publish________________________________________________________________
                    if (order.getPublish() == ProjectInfo.DONT_PUBLISH) {
                        imgSync.setImageResource(R.drawable.ic_synchronize_red);
                        textStatus.setText(R.string.not_send);
                    } else if (order.getPublish() == ProjectInfo.PUBLISH) {
                        imgSync.setImageResource(R.drawable.waited_send_transfer);
                        textStatus.setText(R.string.wait_for_approval);
                    } else if (order.getPublish() == ProjectInfo.PUBLISH_REJECT) {
                        imgSync.setImageResource(R.drawable.reject_recieve_transfer);
                        textStatus.setText(R.string.cancelled);
                    } else if (order.getPublish() == ProjectInfo.PUBLISH_ACCEPT) {
                        imgSync.setImageResource(R.drawable.accepted_send_transfer);
                        textStatus.setText(R.string.approved);
                    }

                } else {

                    tvAddress.setText(order.getAddress());
                    tvMarketName.setText(order.getMarketName());
                    tvCustomerName.setText(order.getCustomerName());
                    //calculate FinalPrice________________________________________________________
                    ArrayList<OrderDetail> allOrderDetail = db.getAllOrderDetailWithOrderId(order.getId());
                    for (OrderDetail item : allOrderDetail) {
                        Price += ServiceTools.getCalculateFinalPrice(item, mContext);
                    }
                    Discount = order.getDiscount();
                    FinalPrice = Price - Discount;
                    order.setFinalPrice(FinalPrice);
                    tvAmount.setText(ServiceTools.formatPrice(order.getFinalPrice()));
                    //Check Publish________________________________________________________________
                    if (order.getPublish() == ProjectInfo.DONT_PUBLISH)
                        imgSync.setImageResource(R.drawable.ic_synchronize_red);
                    else
                        imgSync.setImageResource(R.drawable.ic_synchronize_green);
                }
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
                        Order order = arrayorginal.get(i);
                        InvoiceNumber = order.getCode();
                        boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), InvoiceNumber);
                        if (result_contain) {
                            filterItem.add(order);
                            CheckFilter = true;
                        }
                    }//End of for
                    if (!CheckFilter) {
                        for (int i = 0; i < arrayorginal.size(); i++) {
                            Order order = arrayorginal.get(i);
                            CustomerName = order.getCustomerName();
                            boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), CustomerName);
                            if (result_contain) {
                                filterItem.add(order);
                                CheckFilter = true;
                            }
                        }//End of for
                    }
                    if (!CheckFilter) {
                        for (int i = 0; i < arrayorginal.size(); i++) {
                            Order order = arrayorginal.get(i);
                            MarketName = order.getMarketName();
                            boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), MarketName);
                            if (result_contain) {
                                filterItem.add(order);

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

                arrayorder = (ArrayList<Order>) results.values;
                notifyDataSetChanged();
                clear();
                for (int i = 0; i < arrayorder.size(); i++) {
                    add(arrayorder.get(i));
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
                Order order = db.GetOrder(OrderId);
                deleteOrderDetail(order);

                //Delete Order and ProductsInOrder and Receipt and Cheque___________________________
                ArrayList<Receipt> arrayreceipt = db.getAllReceipt(Code);
                if (arrayreceipt.size() > 0) {
                    for (Receipt receipt : arrayreceipt) {
                        db.DeleteChequesInReceipt(receipt.getId());
                    }
                }//end of if
                db.DeleteReceipts(Code);
                db.DeleteOrder(OrderId);

                if (order.getPersonId() == ProjectInfo.CUSTOMERID_GUEST)
                    customer = db.getCustomerWithPersonClientId(order.getPersonClientId());
                else
                    customer = db.getCustomerWithPersonId(order.getPersonId());

                int count = customer.getOrderCount();
                if (count > 0) {
                    count -= 1;
                    customer.setOrderCount(count);
                    db.UpdateCustomer(customer);
                }
                //_______________________________________________________________________________
                arrayOrder.remove(Position);
                adOrder.notifyDataSetChanged();

                if (Type == ProjectInfo.TYPE_ORDER)
                    tvPageTitle.setText(getString(R.string.str_nav_order_list) + "(" + lstOrder.getCount() + ")");
                else if (Type == ProjectInfo.TYPE_INVOCIE)
                    tvPageTitle.setText(getString(R.string.str_nav_invoice_list) + "(" + lstOrder.getCount() + ")");
                else if (Type == ProjectInfo.TYPE_SEND_TRANSFERENCE)
                    tvPageTitle.setText(getString(R.string.str_nav_transfer_list) + "(" + lstOrder.getCount() + ")");
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

    private void deleteOrderDetail(Order order) {
        db.open();
        ProductDetail productDetail;
        ArrayList<OrderDetail> existOrderdetailInDb = db.getAllOrderDetail(order.getId());
        for (OrderDetail orderDetail : existOrderdetailInDb) {
            productDetail = db.getProductDetail(orderDetail.getProductDetailId());
            Product product = db.GetProductWithProductId(productDetail.getProductId());
            ArrayList<OrderDetailProperty> orderDetailProperties = db.getAllOrderDetailProperty(order.getId(), product.getProductId());
            if (order.getOrderType() == ProjectInfo.TYPE_INVOCIE || (order.getOrderType() == ProjectInfo.TYPE_ORDER && getPrefReduceAsset(mContext))) {
                if (orderDetailProperties.size() > 0) {
                    for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
                        productDetail = db.getProductDetail(orderDetailProperty.getProductDetailId());
                        if (ServiceTools.getSumGiftCount12(orderDetail.getGiftCount1(), orderDetail.getGiftCount2(), mContext) > 0) {
                            productDetail.setCount1(ServiceTools.getExistCount1Prop(orderDetailProperty, productDetail) + (orderDetail.getGiftCount1()));
                            productDetail.setCount2(ServiceTools.getExistCount2Prop(orderDetailProperty, productDetail) + (orderDetail.getGiftCount2()));
                        } else {
                            productDetail.setCount1(ServiceTools.getExistCount1Prop(orderDetailProperty, productDetail));
                            productDetail.setCount2(ServiceTools.getExistCount2Prop(orderDetailProperty, productDetail));
                        }
                        db.UpdateProductDetail(productDetail);
                    }
                    db.DeleteOrderDetailProperty(order.getId());
                } else {
                    if (ServiceTools.getSumGiftCount12(orderDetail.getGiftCount1(), orderDetail.getGiftCount2(), mContext) > 0) {
                        productDetail.setCount1(productDetail.getCount1() + orderDetail.getGiftCount1());
                        productDetail.setCount2(productDetail.getCount2() + orderDetail.getGiftCount2());
                    } else {
                        productDetail.setCount1(productDetail.getCount1() + orderDetail.getCount1());
                        productDetail.setCount2(productDetail.getCount2() + orderDetail.getCount2());
                    }
                    db.UpdateProductDetail(productDetail);
                }
            }
        }
        db.DeleteOrderDetail(order.getId());
    }

    private void DialogSend() {
        FontDialog fontDialog = new FontDialog();
        final AlertDialog dialog = fontDialog.CustomeDialog(mContext, getString(R.string.str_message_send));
        fontDialog.getPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Type == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
                    sendTransfer(Code);
                }
                SendOneOrder(OrderId, Code, mContext);
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

    public void SendOneOrder(final long orderId, final String code, final Context mContext) {

        final FontProgressDialog pd;
        pd = new FontProgressDialog(mContext);
        final DbAdapter db;
        db = new DbAdapter(mContext);
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
                        new SendOrderAsyncTask(response.body().getData().getUserToken(), orderId, code, mContext).execute();
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
        List<Receipt> arrayReceipt = new ArrayList<>();
        List<Cheque> arrayCheque = new ArrayList<>();
        List<Order> arrayInvoice = new ArrayList<>();
        List<OrderDetail> arrayInvoiceDetail = new ArrayList<>();
        List<OrderDetail> orderDetails = new ArrayList<>();
        List<OrderDetailProperty> orderDetailProperties = new ArrayList<>();
        List<VisitorPeople> visitorPeopleArrayList = new ArrayList<>();
        List<Customer> Customers = new ArrayList<>();
        String mUserToken;
        long orderId;
        String code;
        Context mContext;
        FontProgressDialog sendpd;

        SendOrderAsyncTask(String UserToken, long orderId, String code, Context mContext) {
            mUserToken = UserToken;
            this.orderId = orderId;
            this.code = code;
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sendpd = new FontProgressDialog(mContext);
            sendpd.setMessage(getString(R.string.send_message));
            sendpd.setCancelable(false);
            sendpd.show();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            DbAdapter db = new DbAdapter(mContext);
            db.open();
            arrayInvoice.add(db.GetOrder(orderId));
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
            arrayReceipt = db.getAllReceipt(code);
            arrayCheque = new ArrayList<>();
            for (int i = 0; i < arrayReceipt.size(); i++) {
                arrayCheque.addAll(db.getAllCheque(arrayReceipt.get(i).getId()));
            }
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
            setAllDataBody.setReceipts(arrayReceipt);
            setAllDataBody.setCheques(arrayCheque);
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
                        if (arrayReceipt.size() > 0) {
                            for (int i = 0; i < arrayReceipt.size(); i++) {
                                arrayReceipt.get(i).setReceiptId(response.body().getData().getObjects().getReceipts().getResults().get(i).getEntityID());
                                arrayReceipt.get(i).setPublish(ProjectInfo.PUBLISH);
                                db.UpdateReceipt(arrayReceipt.get(i));
                            }
                            for (int i = 0; i < arrayCheque.size(); i++) {
                                arrayCheque.get(i).setChequeId(response.body().getData().getObjects().getCheques().getResults().get(i).getEntityID());
                                arrayCheque.get(i).setPublish(ProjectInfo.PUBLISH);
                                db.UpdateCheque(arrayCheque.get(i));
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
                        adOrder.notifyDataSetChanged();
                        Toast.makeText(mContext, R.string.sent, Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_EDIT) {
                FillView();
            } else if (requestCode == REQUEST_CUSTOMER_LIST) {

                CustomerId = data.getIntExtra(CUSTOMERID_KEY, 0);
                CustomerClientId = data.getLongExtra(CUSTOMER_CLIENT_ID_KEY, 0);
                GroupId = data.getLongExtra(CUSTOMER_GROUP_KEY, 0);
                if (Type == ProjectInfo.TYPE_INVOCIE) {
                    Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                    intent.putExtra(CUSTOMERID_KEY, CustomerId);
                    intent.putExtra(CUSTOMER_CLIENT_ID_KEY, CustomerClientId);
                    intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                    intent.putExtra(PAGE, PAGE_ADD_INVOICE);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_INVOCIE);
                    intent.putExtra(MODE_PAGE, MODE_NEW);
                    startActivity(intent);


                } else if (Type == ProjectInfo.TYPE_ORDER) {
                    Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                    intent.putExtra(CUSTOMERID_KEY, CustomerId);
                    intent.putExtra(CUSTOMER_CLIENT_ID_KEY, CustomerClientId);
                    intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                    intent.putExtra(PAGE, PAGE_ADD_ORDER);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_ORDER);
                    intent.putExtra(MODE_PAGE, MODE_NEW);
                    startActivity(intent);
                }

            } else if (requestCode == REQUEST_USER_LIST) {

                VisitorId = data.getLongExtra(_Key_VisitorID, 0);
                StoreCode = data.getLongExtra(_Key_StoreCode, 0);

                Visitor visitor = new Visitor();
                visitor = db.getVisitor();

                if (visitor.getStoreCode() == 0) {
                    Toast.makeText(mContext, R.string.store_is_not_dedicated, Toast.LENGTH_SHORT).show();
                } else if (StoreCode == visitor.getStoreCode()) {
                    Toast.makeText(mContext, R.string.store_is_equal, Toast.LENGTH_SHORT).show();
                } else if (StoreCode == 0) {
                    Toast.makeText(mContext, R.string.no_store, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), InvoiceDetailActivity.class);
                    intent.putExtra(PAGE, PAGE_ADD_SEND_TRANSFER);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_SEND_TRANSFERENCE);
                    intent.putExtra(MODE_PAGE, MODE_NEW);
                    intent.putExtra(_Key_VisitorID, VisitorId);
                    startActivity(intent);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        Add_Item = menu.findItem(MENU_ADD);
        SEND_TRANSFER_Item = menu.findItem(MENU_SEND_TRANSFER);
        RECEIVE_TRANSFER_Item = menu.findItem(MENU_RECEIVE_TRANSFER);

        if (Type == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
            RECEIVE_TRANSFER_Item.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (Type == ProjectInfo.TYPE_INVOCIE || Type == ProjectInfo.TYPE_ORDER) {
            menu.setGroupCheckable(1, true, true);
            getMenuInflater().inflate(R.menu.pmenu_add_sort, menu);
        } else if (Type == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
            menu.add(0, MENU_ADD, 2, R.string.str_add_transfer).setIcon(R.drawable.ic_add_inverse)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add(0, MENU_SEND_TRANSFER, 1, R.string.TransferUpdate).setIcon(R.drawable.send_transfer)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add(0, MENU_RECEIVE_TRANSFER, 0, R.string.GetAllTransfer).setIcon(R.drawable.receive_transfer)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case MENU_ADD:
                if (Type == ProjectInfo.TYPE_RECEIVE_TRANSFERENCE) {
                    item.setVisible(false);
                } else if (Type == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
                    Intent intent = new Intent(mContext, VisitorListActivity.class);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_SEND_TRANSFERENCE);
                    intent.putExtra(PAGE, PAGE_Invoice_Detail_Activity);
                    startActivityForResult(intent, REQUEST_USER_LIST);
                } else {
                    Intent intent = new Intent(mContext, PeopleListActivity.class);
                    intent.putExtra(PAGE, PAGE_ADD_NON_REGISTER);
                    startActivityForResult(intent, REQUEST_CUSTOMER_LIST);
                }

                break;
            case MENU_SEND_TRANSFER:
                Type = ProjectInfo.TYPE_SEND_TRANSFERENCE;
                sendTransfer("");
                getResultOfTransfers(item);
                db.open();
                FillView();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btnSend.setBackground(getResources().getDrawable(R.drawable.roundedbutton));
                    btnReceived.setBackground(getResources().getDrawable(R.drawable.round_transparent));
                }

                break;
            case MENU_RECEIVE_TRANSFER:
                Type = ProjectInfo.TYPE_RECEIVE_TRANSFERENCE;
                getALLTransfer(item);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btnReceived.setBackground(getResources().getDrawable(R.drawable.roundedbutton));
                    btnSend.setBackground(getResources().getDrawable(R.drawable.round_transparent));
                }
                break;
            case R.id.mnuSort:
                View menuItemView = findViewById(R.id.mnuSort);
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

            case R.id.mnuAdd:
                InvoiceDetailActivity.orderDetails.clear();
                Intent intent = new Intent(mContext, PeopleListActivity.class);
                intent.putExtra(PAGE, PAGE_ADD_NON_REGISTER);
                startActivityForResult(intent, REQUEST_CUSTOMER_LIST);
                break;
            case android.R.id.home:
                Intent intent2 = new Intent(getApplicationContext(), DashboardActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                break;
            default:
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
                    adOrder.notifyDataSetChanged();
                }
                break;
            case R.id.sortDesc:
                if (!(sortType == ProjectInfo.SortDesc)) {
                    sortType = ProjectInfo.SortDesc;
                    item.setChecked(true);
                    Collections.reverse(arrayOrder);
                    adOrder.notifyDataSetChanged();
                }
                break;
        }
    }

    private void getResultOfTransfers(final MenuItem item) {
        item.setEnabled(false);
        db.open();
        User user = db.getUser();
        final ApiInterface apiService;
        final Call<GetDataResult> getDataResultCall;
        final ArrayList<Order> arrayInvoice = new ArrayList<Order>();

        final GetAllDataBody getAllDataBody;
        getAllDataBody = new GetAllDataBody();
        getAllDataBody.setUserToken(user.getUserToken());
        apiService = ApiClient.getClient().create(ApiInterface.class);
        getAllDataBody.setFromTransferStoreVersion(db.getMaxRowVersion(DbSchema.ReceivedTransfersschema.TABLE_NAME));
        getDataResultCall = apiService.GetAllData(getAllDataBody);
        getDataResultCall.enqueue(new Callback<GetDataResult>() {
            @Override
            public void onResponse(Call<GetDataResult> call, Response<GetDataResult> response) {
                item.setEnabled(true);
                if (response.body() != null && response.body().isResult()) {
                    if (response.body().getData() != null) {
                        db.open();
                        List<ReceivedTransfers> receivedTransferList = response.body().getData().getObjects().getReceivedTransfers();
                        for (int i = 0; i < receivedTransferList.size(); i++) {
                            if (receivedTransferList.get(i).getIsAccepted() == ProjectInfo.TYPE_Accept) {
                                Order order;
                                Product product;
                                ProductDetail productDetail;
                                order = db.getAllTransferPublish(BaseActivity.getPrefUserId(), "" + receivedTransferList.get(i).getTransferStoreClientId());
                                if (order.getCode().equals(String.valueOf(receivedTransferList.get(i).getTransferStoreClientId())))
                                    arrayInvoice.add(order);
                                if (arrayInvoice.size() > 0) {
                                    for (Order item : arrayInvoice) {
                                        item.setPublish(ProjectInfo.PUBLISH_ACCEPT);
                                        db.UpdateOrder(item);
                                    }
                                    for (int j = 0; j < arrayInvoice.size(); j++) {
                                        orderDetails = db.getAllOrderDetailWithOrderId(arrayInvoice.get(j).getId());
                                        for (OrderDetail item : orderDetails) {
                                            productDetail = db.getProductDetail(item.getProductDetailId());
                                            product = db.GetProductWithProductId(productDetail.getProductId());
                                            productDetail.setCount1(productDetail.getCount1() - (item.getSumCountBaJoz() + ServiceTools.getSumGiftCount12(item.getGiftCount1(), item.getGiftCount2(), mContext)));
                                            db.UpdateProduct(product);
                                            db.UpdateProductDetail(productDetail);
                                        }
                                    }
                                }
                                arrayInvoice.clear();
                            } else if (receivedTransferList.get(i).getIsAccepted() == ProjectInfo.TYPE_Reject) {
                                Order order;
                                order = db.getAllTransferPublish(BaseActivity.getPrefUserId(), "" + receivedTransferList.get(i).getTransferStoreClientId());
                                if (order.getCode().equals(String.valueOf(receivedTransferList.get(i).getTransferStoreClientId())))
                                    arrayInvoice.add(order);
                                if (arrayInvoice.size() > 0) {

                                    for (Order item : arrayInvoice) {
                                        item.setPublish(ProjectInfo.PUBLISH_REJECT);
                                        db.UpdateOrder(item);
                                    }
                                }
                                arrayInvoice.clear();
                            }
                            FillView();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDataResult> call, Throwable t) {
                item.setEnabled(true);
                Toast.makeText(mContext, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendTransfer(String transferCode) {
        //item.setEnabled(false);
        db.open();
        final ApiInterface apiService;
        Visitor visitor;
        SetAllDataBody setAllDataBody = new SetAllDataBody();
        User user = db.getUser();
        Call<SaveAllDataResult> saveAllDataResultCall;
        long TransferStoreClientId = ServiceTools.toLong(ServiceTools.getGenerationCode());
        long TransferStoreDetailClientId = ServiceTools.toLong(ServiceTools.getGenerationCode());
        List<OrderDetailProperty> orderDetailProperties;

        setAllDataBody.setUserToken(user.getUserToken());
        apiService = ApiClient.getClient().create(ApiInterface.class);

        final List<Order> arrayInvoice;
        final List<OrderDetail> arrayInvoiceDetail;

        ReceivedTransfers receivedTransfers;
        ReceivedTransferProducts receivedTransferProducts;

        final List<ReceivedTransfers> transferStores = new ArrayList<>();
        final List<ReceivedTransferProducts> transferStoreDetails = new ArrayList<>();

        arrayInvoice = db.getAllTransferNotPublish(BaseActivity.getPrefUserId(), transferCode);
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

        for (int i = 0; i < arrayInvoice.size(); i++) {
            visitor = db.getVisitor(arrayInvoice.get(i).getPersonId());
            receivedTransfers = new ReceivedTransfers();
            // TransferStoreClientId++;
            receivedTransfers.setTransferDate(arrayInvoice.get(i).getOrderDate());
            receivedTransfers.setSenderStoreCode(ServiceTools.toInt(BaseActivity.getPrefStore()));
            receivedTransfers.setReceiverStoreCode((int) visitor.getStoreCode());
            receivedTransfers.setSenderVisitorId(String.valueOf(BaseActivity.getPrefUserMasterId()));
            receivedTransfers.setReceiverVisitorId(String.valueOf(visitor.getVisitorId()));
            //receivedTransfers.setTransferStoreClientId(TransferStoreClientId);
            receivedTransfers.setTransferStoreClientId(ServiceTools.toLong(arrayInvoice.get(i).getCode()));
            // receivedTransfers.setTransferStoreCode(String.valueOf(arrayInvoice.get(i).getCode()));
            receivedTransfers.setIsAccepted(ProjectInfo.TYPE_NaN);
            receivedTransfers.setDescription(arrayInvoice.get(i).getDescription());
            transferStores.add(receivedTransfers);
        }
        for (int j = 0; j < arrayInvoice.size(); j++) {
            for (int i = 0; i < arrayInvoiceDetail.size(); i++) {
                receivedTransferProducts = new ReceivedTransferProducts();
                TransferStoreDetailClientId++;
                receivedTransferProducts.setCount1(arrayInvoiceDetail.get(i).getCount1());
                receivedTransferProducts.setCount2(arrayInvoiceDetail.get(i).getCount2());
                receivedTransferProducts.setProductDetailId(String.valueOf(arrayInvoiceDetail.get(i).getProductDetailId()));
                receivedTransferProducts.setDescription(arrayInvoiceDetail.get(i).getDescription());
                receivedTransferProducts.setTransferStoreDetailClientId(TransferStoreDetailClientId);
                receivedTransferProducts.setTransferStoreClientId(transferStores.get(j).getTransferStoreClientId());
                transferStoreDetails.add(receivedTransferProducts);
            }
        }

        if (transferStores.size() > 0) {
            setAllDataBody.setReceivedTransfers(transferStores);
            setAllDataBody.setReceivedTransferProducts(transferStoreDetails);
            saveAllDataResultCall = apiService.SaveAllData(setAllDataBody);
            saveAllDataResultCall.enqueue(new Callback<SaveAllDataResult>() {
                @Override
                public void onResponse(Call<SaveAllDataResult> call, Response<SaveAllDataResult> response) {
                    // item.setEnabled(true);
                    if (response.body() != null && response.body().isResult()) {
                        db.open();
                        for (int i = 0; i < arrayInvoice.size(); i++) {
                            arrayInvoice.get(i).setOrderId(response.body().getData().getObjects().getTransferStores().getResults().get(i).getEntityID());
                            arrayInvoice.get(i).setPublish(ProjectInfo.PUBLISH);
                            db.UpdateOrder(arrayInvoice.get(i));
                        }
                        for (int j = 0; j < arrayInvoiceDetail.size(); j++) {
                            arrayInvoiceDetail.get(j).setOrderDetailId(response.body().getData().getObjects().getTransferStoreDetails().getResults().get(j).getEntityID());
                            db.UpdateOrderDetail(arrayInvoiceDetail.get(j));
                        }

                        Toast.makeText(mActivity, R.string.sent, Toast.LENGTH_SHORT).show();

                        db.close();
                        Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                        intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_SEND_TRANSFERENCE);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else if (response.body() != null) {
                        Toast.makeText(mContext, R.string.error_in_send, Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<SaveAllDataResult> call, Throwable t) {
                    // item.setEnabled(true);
                    Toast.makeText(mContext, t.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        db.close();
    }

    public void getALLTransfer(final MenuItem item) {
        item.setEnabled(false);
        db.open();
        User user = db.getUser();
        final ApiInterface apiService;
        final Call<GetDataResult> getDataResultCall;

        final GetAllDataBody getAllDataBody;
        getAllDataBody = new GetAllDataBody();
        getAllDataBody.setUserToken(user.getUserToken());
        apiService = ApiClient.getClient().create(ApiInterface.class);

        getAllDataBody.setFromTransferStoreVersion(db.getMaxRowVersion(DbSchema.ReceivedTransfersschema.TABLE_NAME));
        getAllDataBody.setFromTransferStoreDetailVersion(db.getMaxRowVersion(DbSchema.ReceivedTransferProductsschema.TABLE_NAME));

        getDataResultCall = apiService.GetAllData(getAllDataBody);
        getDataResultCall.enqueue(new Callback<GetDataResult>() {
            @Override
            public void onResponse(Call<GetDataResult> call, Response<GetDataResult> response) {
                item.setEnabled(true);
                if (response.body() != null && response.body().isResult()) {
                    if (response.body().getData() != null) {
                        db.open();
                        List<ReceivedTransfers> receivedTransferList = response.body().getData().getObjects().getReceivedTransfers();
                        List<ReceivedTransferProducts> receivedTransferProductList = response.body().getData().getObjects().getReceivedTransferProducts();
                        for (ReceivedTransfers receivedTransfers : receivedTransferList) {
                            if (!receivedTransfers.isDeleted())
                                if (receivedTransfers.getIsAccepted() == ProjectInfo.TYPE_NaN) {
                                    if (receivedTransfers.getReceiverVisitorId().equals(String.valueOf(BaseActivity.getPrefUserId()))) {
                                        arrayReceivedTransfer.add(receivedTransfers);
                                        adReceivedTransfer.notifyDataSetChanged();
                                        tvPageTitle.setText(getString(R.string.Received_transfer_list) + "(" + lstOrder.getCount() + ")");
                                    }
                                }
                        }
                        if (receivedTransferList.size() > 0) {
                            DataService.InsertReceivedTransfers(db, receivedTransferList);
                            DataService.InsertReceivedTransferProduct(db, receivedTransferProductList);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDataResult> call, Throwable t) {
                item.setEnabled(true);
                Toast.makeText(mContext, t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onResume() {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (Type == ProjectInfo.TYPE_RECEIVE_TRANSFERENCE) {
            arrayReceivedTransfer = db.getAllReceivedTransfer();
            adReceivedTransfer = new AdapterReceivedTransfer(mActivity, arrayReceivedTransfer);
            lstOrder.setAdapter(adReceivedTransfer);
            tvPageTitle.setText(getString(R.string.str_nav_transfer_list) + "(" + lstOrder.getCount() + ")");

        } else {
            FillView();
        }

        super.onResume();
    }
}
