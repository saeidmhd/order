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
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.mahak.order.apiHelper.ApiClient;
import com.mahak.order.apiHelper.ApiInterface;
import com.mahak.order.common.Cheque;
import com.mahak.order.common.Customer;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.Receipt;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.User;
import com.mahak.order.common.Visitor;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mahak.order.common.ServiceTools.getDateAndTimeForLong;

public class ReceiptsListActivity extends BaseActivity {

    private static final int MENU_SORT = 1;
    private static final int MENU_ADD = 2;
    private Context mContext;
    private Activity mActivty;
    private ExpandableListView ExpandList;
    private ArrayList<Receipt> arrayReceipt;
    private List<Cheque> arrayCheque;
    private int REQUESTCODE_MANAGE_RECEIPT;
    private static final int REQUEST_CUSTOMER_LIST = 2;
    private DbAdapter db;
    private ExpandListAdapter expandlistAdapter;
    private EditText Search;
    private long lngDate;
    private long ReceiptId = 0;
    private int PositionArray;
    private TextView tvPageTitle;
    private double TotalPriceInvoice;
    private double TotalReceipt;
    private double mSpentCredit;
    private double mVisitorCredit;
    private double savedCashedAndCheque;
    private double remainCredit;
    private Visitor mVisitor;

    private int CustomerId;
    private long CustomerClientId;
    private long GroupId;
    private FontProgressDialog pd;
    private int sortType = ProjectInfo.SortDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipts_list);

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
        tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________

        mContext = this;
        mActivty = this;

        initialise();
        db.open();

        mVisitor = db.getVisitor();

        FillView();

        Search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence query, int arg1, int arg2, int arg3) {
                expandlistAdapter.filterData(query.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

    }//end of onCreate

    /**
     * Initializing Variables
     */
    private void initialise() {
        ExpandList = (ExpandableListView) findViewById(R.id.explistReceipt);
        Search = (EditText) findViewById(R.id.txtSearch);
        db = new DbAdapter(mContext);
        tvPageTitle.setText(getString(R.string.str_nav_receipt_list) + "(" + ExpandList.getCount() + ")");
    }

    /**
     * Read Receipts From Database And Fill Adapter
     */
    private void FillView() {
        arrayReceipt = db.getAllReceipt();
        Customer customer;
        double CashAmount = 0.0;
        Collections.reverse(arrayReceipt);

        for (Receipt item : arrayReceipt) {

            CashAmount = item.getCashAmount();
            if (item.getPersonId() == ProjectInfo.CUSTOMERID_GUEST) {
                customer = db.getCustomerWithPersonClientId(item.getPersonClientId());
                item.setCustomerName(customer.getName());
            } else {
                customer = db.getCustomerWithPersonId(item.getPersonId());
                item.setCustomerName(customer.getName());
            }
            arrayCheque = db.getAllCheque(item.getId());

            double totalCheque = 0, totalCashReceipt = 0, totalAmount = 0;

            for (int i = 0; i < arrayCheque.size(); i++) {
                if (arrayCheque.get(i).getType() == ProjectInfo.CHEQUE_TYPE)
                    totalCheque += arrayCheque.get(i).getAmount();
                else if (arrayCheque.get(i).getType() == ProjectInfo.CASHRECEIPT_TYPE)
                    totalCashReceipt += arrayCheque.get(i).getAmount();
            }

            item.setTotalCheque(totalCheque);
            item.setTotalCashReceipt(totalCashReceipt);

            totalAmount = CashAmount + totalCashReceipt + totalCheque;

            item.setTotalAmount(totalAmount);
            item.setItems(arrayCheque);

        }// End of For

        expandlistAdapter = new ExpandListAdapter(mContext, arrayReceipt);
        ExpandList.setAdapter(expandlistAdapter);

        tvPageTitle.setText(getString(R.string.str_nav_receipt_list) + "(" + ExpandList.getCount() + ")");

    }

    public class ExpandListAdapter extends BaseExpandableListAdapter {

        private Context context;
        private List<Receipt> ReceiptList;
        private List<Receipt> originalList;

        private class HolderChild {
            public TextView tvDate, tvChequeType, tvNumber, tvAmount, tvBank, tvDescription;

            HolderChild(View view) {
                tvDate = (TextView) view.findViewById(R.id.tvDate);
                tvNumber = (TextView) view.findViewById(R.id.tvNumber);
                tvAmount = (TextView) view.findViewById(R.id.txtAmount);
                tvBank = (TextView) view.findViewById(R.id.tvBank);
                tvChequeType = (TextView) view.findViewById(R.id.tvChequeType);
                tvDescription = (TextView) view.findViewById(R.id.tvDescription);
            }

            public void Populate(Cheque item) {
                tvNumber.setText(item.getNumber());
                tvAmount.setText(ServiceTools.formatPrice(item.getAmount()));
                tvBank.setText(item.getBankName());
                if (item.getType() == ProjectInfo.CHEQUE_TYPE)
                    tvChequeType.setText(getResources().getString(R.string.str_cheque_type));
                else if (item.getType() == ProjectInfo.CASHRECEIPT_TYPE)
                    tvChequeType.setText(getResources().getString(R.string.str_cash_receipt_type));
                lngDate = item.getDate();
                tvDate.setText(getDateAndTimeForLong(lngDate));
                tvDescription.setText(item.getDescription());
            }
        }

        private class HolderGroup {
            public TextView tvCustomerName, tvDate, tvCashAmount, tvChequeAmount, tvReceiptAmount, tvTotalAmount, tvDescription, tvCode;
            public LinearLayout btnMenu;
            ImageView imgExpand, imgSync;

            HolderGroup(View view) {
                tvCashAmount = (TextView) view.findViewById(R.id.tvCashAmount);
                tvCustomerName = (TextView) view.findViewById(R.id.tvCustomerName);
                tvDate = (TextView) view.findViewById(R.id.tvDate);
                tvDescription = (TextView) view.findViewById(R.id.tvDescription);
                tvCode = (TextView) view.findViewById(R.id.tvCode);
                btnMenu = (LinearLayout) view.findViewById(R.id.btnmenu);
                imgExpand = (ImageView) view.findViewById(R.id.imgExpandedList);
                tvChequeAmount = (TextView) view.findViewById(R.id.tvChequeAmount);
                tvReceiptAmount = (TextView) view.findViewById(R.id.tvReceiptAmount);
                tvTotalAmount = (TextView) view.findViewById(R.id.tvTotalAmount);
                imgSync = (ImageView) view.findViewById(R.id.imgSync);

            }

            public void Populate(final Receipt receipt, boolean isExpand, final int position) {
                tvCustomerName.setText(receipt.getCustomerName());
                tvCashAmount.setText(ServiceTools.formatPrice(receipt.getCashAmount()));
                tvChequeAmount.setText(ServiceTools.formatPrice((receipt.getTotalCheque())));
                tvReceiptAmount.setText(ServiceTools.formatPrice(receipt.getTotalCashReceipt()));
                tvTotalAmount.setText(ServiceTools.formatPrice(receipt.getTotalAmount()));
                lngDate = receipt.getDate();
                tvDate.setText(getDateAndTimeForLong(lngDate));

                tvDescription.setText(receipt.getDescription());
                //Check Publish________________________________________________________________
                if (receipt.getPublish() == ProjectInfo.DONT_PUBLISH)
                    imgSync.setImageResource(R.drawable.ic_synchronize_red);
                else
                    imgSync.setImageResource(R.drawable.ic_synchronize_green);
                //Check Code______________________________________________________________________________
                if (!receipt.getTrackingCode().equals(ProjectInfo.DONT_CODE))
                    tvCode.setText(receipt.getTrackingCode());
                else
                    tvCode.setText("");
                //______________________________________________________________________________
                if (isExpand)
                    imgExpand.setImageResource(R.drawable.ic_expand_down);
                else
                    imgExpand.setImageResource(R.drawable.ic_expand_up);

                if (receipt.getItems().size() == 0)
                    imgExpand.setVisibility(View.INVISIBLE);
                else
                    imgExpand.setVisibility(View.VISIBLE);

                btnMenu.setFocusable(false);
                btnMenu.setFocusableInTouchMode(false);
                btnMenu.setOnClickListener(new View.OnClickListener() {

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
                                        PositionArray = position;
                                        ReceiptId = receipt.getId();

                                        //مجموع تمام فاکتور ها چه ارسال شده یا نشده
                                        TotalPriceInvoice = db.getPurePriceInvoice();
                                        //مجموع تمام دریافتی ها چه ارسال شده یا نشده
                                        TotalReceipt = db.getTotalPriceReceipt();
                                        //اعتبار مصرف شده
                                        mSpentCredit = TotalPriceInvoice - TotalReceipt;
                                        //اعتبار ویزیتور
                                        mVisitorCredit = mVisitor.getTotalCredit();
                                        //اعتبار باقیمانده = اعتبار ویزیتور - اعتبار  مصرف شده
                                        remainCredit = mVisitorCredit - mSpentCredit;
                                        //میزان دریافتی های مربوط به این ReceiptId
                                        savedCashedAndCheque = db.getTotalReceiptWithId(ReceiptId);

                                        if (mVisitorCredit != -1) {
                                            if (remainCredit - savedCashedAndCheque < 0) {
                                                Toast.makeText(mContext, getResources().getString(R.string.str_message_less_remain_credit), Toast.LENGTH_SHORT).show();
                                                break;
                                            }
                                        } else {
                                            if (receipt.getPublish() == ProjectInfo.DONT_PUBLISH) {
                                                Dialogdelete();
                                            } else
                                                Toast.makeText(mContext, getResources().getString(R.string.str_message_publish_delete), Toast.LENGTH_SHORT).show();
                                        }

                                        break;
                                    case R.id.mnuEdit:
                                        if (receipt.getPublish() == ProjectInfo.DONT_PUBLISH) {
                                            Intent intent = new Intent(mContext, ManageReceiptActivity.class);
                                            intent.putExtra(PAGE, PAGE_RECEIPTLIST);
                                            intent.putExtra(MODE_PAGE, MODE_EDIT);
                                            intent.putExtra(ID, receipt.getId());
                                            startActivityForResult(intent, REQUESTCODE_MANAGE_RECEIPT);
                                        } else
                                            Toast.makeText(mContext, getResources().getString(R.string.str_message_publish_edit), Toast.LENGTH_SHORT).show();
                                        break;
                                    case R.id.mnuSend:
                                        PositionArray = position;
                                        ReceiptId = receipt.getId();
                                        if (receipt.getPublish() == ProjectInfo.DONT_PUBLISH) {
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
            }
        }

        public ExpandListAdapter(Context context, List<Receipt> groups) {
            this.context = context;
            this.ReceiptList = new ArrayList<Receipt>();
            this.originalList = new ArrayList<Receipt>();

            this.ReceiptList.addAll(groups);
            this.originalList.addAll(groups);
        }

        public void addItem(Cheque item, Receipt group) {
            if (!ReceiptList.contains(group)) {
                ReceiptList.add(group);
            }
            int index = ReceiptList.indexOf(group);
            List<Cheque> ch = ReceiptList.get(index).getItems();
            ch.add(item);
            ReceiptList.get(index).setItems(ch);
        }

        public Object getChild(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            List<Cheque> chList = ReceiptList.get(groupPosition).getItems();
            return chList.get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return childPosition;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {

            Cheque child = (Cheque) getChild(groupPosition, childPosition);
            HolderChild holderchild = null;
            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.expandlist_child_receipt, null);
                holderchild = new HolderChild(view);
                view.setTag(holderchild);
            } else
                holderchild = (HolderChild) view.getTag();

            holderchild.Populate(child);
            // TODO Auto-generated method stub
            return view;
        }

        public int getChildrenCount(int groupPosition) {
            // TODO Auto-generated method stub
            List<Cheque> chList = ReceiptList.get(groupPosition).getItems();

            return chList.size();

        }

        public Object getGroup(int groupPosition) {
            // TODO Auto-generated method stub
            return ReceiptList.get(groupPosition);
        }

        public int getGroupCount() {
            // TODO Auto-generated method stub
            return ReceiptList.size();
        }

        public long getGroupId(int groupPosition) {
            // TODO Auto-generated method stub
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {

            Receipt group = (Receipt) getGroup(groupPosition);
            HolderGroup holdergroup = null;
            if (view == null) {
                LayoutInflater inf = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                view = inf.inflate(R.layout.expandlist_group_receipt, null);
                holdergroup = new HolderGroup(view);
                view.setTag(holdergroup);
            } else {
                holdergroup = (HolderGroup) view.getTag();
            }
            holdergroup.Populate(group, isExpanded, groupPosition);

            return view;
        }

        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return true;
        }

        public boolean isChildSelectable(int arg0, int arg1) {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public void onGroupExpanded(int groupPosition) {
            // TODO Auto-generated method stub
            super.onGroupExpanded(groupPosition);
        }

        public void filterData(String query) {

            query = query.toLowerCase();
            ReceiptList.clear();
            Boolean CheckFilter = false;
            ArrayList<Receipt> newList = new ArrayList<Receipt>();

            if (query.isEmpty()) {
                ReceiptList.addAll(originalList);
            } else {
                for (Receipt item : originalList) {
                    boolean result_contain = ServiceTools.CheckContainsWithSimillar(query, item.getCustomerName());
                    //if(item.getCustomerName().toLowerCase().contains(query))
                    if (result_contain) {
                        newList.add(item);
                        CheckFilter = true;
                    }
                }
                if (CheckFilter == false) {

                    for (Receipt item : originalList) {
                        boolean result_contain = ServiceTools.CheckContainsWithSimillar(query, String.valueOf(item.getTrackingCode()));
                        //if(item.getCustomerName().toLowerCase().contains(query))
                        if (result_contain) {
                            newList.add(item);
                            CheckFilter = true;
                        }
                    }
                }
                ReceiptList.addAll(newList);
            }
            notifyDataSetChanged();
            tvPageTitle.setText(getString(R.string.str_nav_receipt_list) + "(" + ExpandList.getCount() + ")");

        }

    }

    public ExpandListAdapter getExpandlistAdapter() {
        return expandlistAdapter;
    }

    private void Dialogdelete() {
        FontDialog fontDialog = new FontDialog();
        final AlertDialog dialog = fontDialog.CustomeDialog(mContext, getString(R.string.str_message_delete));
        fontDialog.getPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.DeleteReceipt(ReceiptId)) {
                    if (arrayReceipt.get(PositionArray).getItems().size() > 0) {
                        if (db.DeleteChequesInReceipt(ReceiptId)) {
                            arrayReceipt.remove(PositionArray);
                            expandlistAdapter = new ExpandListAdapter(mContext, arrayReceipt);
                            ExpandList.setAdapter(expandlistAdapter);
                        } else
                            Toast.makeText(mContext, getResources().getString(R.string.str_message_error), Toast.LENGTH_SHORT).show();
                    } else {
                        arrayReceipt.remove(PositionArray);
                        expandlistAdapter = new ExpandListAdapter(mContext, arrayReceipt);
                        ExpandList.setAdapter(expandlistAdapter);
                    }
                    tvPageTitle.setText(getString(R.string.str_nav_receipt_list) + "(" + ExpandList.getCount() + ")");
                } else
                    Toast.makeText(mContext, getResources().getString(R.string.str_message_error), Toast.LENGTH_SHORT).show();
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
            if (requestCode == REQUEST_CUSTOMER_LIST) {

                CustomerId = data.getIntExtra(CUSTOMERID_KEY, 0);
                CustomerClientId = data.getLongExtra(CUSTOMER_CLIENT_ID_KEY, 0);
                GroupId = data.getLongExtra(CUSTOMER_GROUP_KEY, 0);

                Intent intent = new Intent(getApplicationContext(), ManageReceiptActivity.class);
                intent.putExtra(PAGE, PAGE_RECEIPTLIST);
                intent.putExtra(CUSTOMERID_KEY, CustomerId);
                intent.putExtra(CUSTOMER_CLIENT_ID_KEY, CustomerClientId);
                intent.putExtra(CUSTOMER_GROUP_KEY, GroupId);
                intent.putExtra(MODE_PAGE, MODE_NEW);
                startActivityForResult(intent, REQUESTCODE_MANAGE_RECEIPT);

            }

            if (requestCode == REQUESTCODE_MANAGE_RECEIPT) {
                FillView();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, MENU_ADD, 0, R.string.str_add_receipt).setIcon(R.drawable.ic_add_inverse)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, MENU_SORT, 0, R.string.str_sort).setIcon(R.drawable.ic_sort)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case MENU_ADD:
                Intent intent = new Intent(mContext, PeopleListActivity.class);
                intent.putExtra(MODE_PAGE, MODE_NEW);
                intent.putExtra(PAGE, PAGE_MANAGE_RECEIPT);
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
            case android.R.id.home:
                finishing();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finishing();
        super.onBackPressed();
    }

    public void sortItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortAsc:
                if (!(sortType == ProjectInfo.SortAsc)) {
                    sortType = ProjectInfo.SortAsc;
                    item.setChecked(true);
                    Collections.reverse(arrayReceipt);
                    expandlistAdapter = new ExpandListAdapter(mContext, arrayReceipt);
                    ExpandList.setAdapter(expandlistAdapter);
                }
                break;
            case R.id.sortDesc:
                if (!(sortType == ProjectInfo.SortDesc)) {
                    sortType = ProjectInfo.SortDesc;
                    item.setChecked(true);
                    Collections.reverse(arrayReceipt);
                    expandlistAdapter = new ExpandListAdapter(mContext, arrayReceipt);
                    ExpandList.setAdapter(expandlistAdapter);
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
        User user = db.getUser();
        setAllDataBody = new SetAllDataBody();
        setAllDataBody.setUserToken(userToken);

        apiService = ApiClient.orderRetrofitClient().create(ApiInterface.class);

        final List<Receipt> receipts = new ArrayList<>();
        final List<Cheque> cheques = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();
        Receipt receipt;

        receipt = db.GetReceipt(ReceiptId);
        receipts.add(receipt);

        for (int i = 0; i < receipts.size(); i++) {
            if (receipts.get(i).getPersonId() == 0)
                customers.add(db.getCustomerWithPersonClientId(receipts.get(i).getPersonClientId()));
            cheques.addAll(db.getAllCheque(receipts.get(i).getId()));
        }

        setAllDataBody.setReceipts(receipts);
        setAllDataBody.setCheques(cheques);
        if (customers.size() > 0)
            setAllDataBody.setPeople(customers);

        saveAllDataResultCall = apiService.SaveAllData(setAllDataBody);

        // Set up progress before call
        final FontProgressDialog progressDoalog;
        progressDoalog = new FontProgressDialog(ReceiptsListActivity.this);
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
                    if (receipts.size() > 0) {
                        for (int i = 0; i < receipts.size(); i++) {
                            receipts.get(i).setReceiptId(response.body().getData().getObjects().getReceipts().getResults().get(i).getEntityID());
                            receipts.get(i).setPublish(ProjectInfo.PUBLISH);
                            db.UpdateReceipt(receipts.get(i));
                        }
                        for (int i = 0; i < cheques.size(); i++) {
                            cheques.get(i).setChequeId(response.body().getData().getObjects().getCheques().getResults().get(i).getEntityID());
                            cheques.get(i).setPublish(ProjectInfo.PUBLISH);
                            db.UpdateCheque(cheques.get(i));
                        }
                    }

                    arrayReceipt.get(PositionArray).setPublish(ProjectInfo.PUBLISH);
                    expandlistAdapter.notifyDataSetChanged();
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


    public void finishing(){
        Intent intent2 = new Intent(getApplicationContext(), DashboardActivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent2);
    }
}
