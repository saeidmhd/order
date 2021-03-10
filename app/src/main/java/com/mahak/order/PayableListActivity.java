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
import com.mahak.order.common.PayableTransfer;
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
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mahak.order.common.ServiceTools.getDateAndTimeForLong;

public class PayableListActivity extends BaseActivity {

    private static final int MENU_ADD = 1;
    private static final int MENU_SORT = 2;
    private Context mContext;
    private Activity mActivty;
    private ExpandableListView ExpandList;
    private ArrayList<PayableTransfer> arrayPayableTransfer;
    private int REQUESTCODE_MANAGE_PAYABLE;
    private DbAdapter db;
    private ExpandListAdapter expandlistAdapter;
    private EditText Search;
    private long lngDate;
    private long PayableTransferId = 0;
    private int PositionArray;
    private TextView tvPageTitle;
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
        tvPageTitle.setText(getString(R.string.str_nav_payable_list) + "(" + ExpandList.getCount() + ")");
    }

    /**
     * Read Receipts From Database And Fill Adapter
     */
    private void FillView() {
        arrayPayableTransfer = db.getAllPayable();
        Collections.reverse(arrayPayableTransfer);
        Customer customer;
        String CashAmount = "";

        for (PayableTransfer item : arrayPayableTransfer) {

            CashAmount = String.valueOf(item.getPrice());
            if (CashAmount == null)
                CashAmount = "0";
            else if (CashAmount.equals(""))
                CashAmount = "0";


            double totalAmount = 0;


            totalAmount = ServiceTools.toDouble(CashAmount);


        }// End of For

        expandlistAdapter = new ExpandListAdapter(mContext, arrayPayableTransfer);
        ExpandList.setAdapter(expandlistAdapter);

        tvPageTitle.setText(getString(R.string.str_nav_payable_list) + "(" + ExpandList.getCount() + ")");

    }

    public class ExpandListAdapter extends BaseExpandableListAdapter {

        private Context context;
        private List<PayableTransfer> PayableList;
        private List<PayableTransfer> originalList;

        private class HolderChild {
            public TextView tvDate, tvChequeType, tvNumber, tvAmount, tvBank, tvDescription;

            public HolderChild(View view) {
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
            public TextView tvCustomerName, tvDate, tvCashAmount, txtCustomerName, tvChequeAmount, tvReceiptAmount, tvTotalAmount, tvDescription, tvCode;
            public LinearLayout btnMenu, llTotalAmount, llChequeAmount;
            public ImageView imgExpand, imgSync;

            public HolderGroup(View view) {
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
                txtCustomerName = (TextView) view.findViewById(R.id.txtCustomerName);
                llTotalAmount = (LinearLayout) view.findViewById(R.id.llTotalAmount);
                llChequeAmount = (LinearLayout) view.findViewById(R.id.llChequeAmount);
                imgSync = (ImageView) view.findViewById(R.id.imgSync);

            }

            public void Populate(final PayableTransfer transfer, boolean isExpand, final int position) {

                User user = db.getUser();
                if (user != null)
                    tvCustomerName.setText(user.getName());

                tvCashAmount.setText(ServiceTools.formatPrice(transfer.getPrice()));
				/*tvChequeAmount.setText(ServiceTools.formatPrice(transfer.getTotalCheque()));
				tvReceiptAmount.setText(ServiceTools.formatPrice(transfer.getTotalCashReceipt()));*/
                tvTotalAmount.setText(ServiceTools.formatPrice(transfer.getPrice()));
                lngDate = transfer.getTransferDate();
                tvDate.setText(getDateAndTimeForLong(lngDate));
                imgExpand.setVisibility(View.GONE);
                llTotalAmount.setVisibility(View.GONE);
                llChequeAmount.setVisibility(View.GONE);
                tvCode.setText("" + transfer.getTransferCode());

                if (transfer.getTransferType() == 3) {
                    String bankName = db.getBankNameFromBankID(String.valueOf(transfer.getReceiverid()));
                    tvReceiptAmount.setText(getString(R.string.to_bank) + bankName);
                } else {
                    tvReceiptAmount.setText(R.string.to_expence);
                }

                txtCustomerName.setText(R.string.to_visitore);


                tvDescription.setText(transfer.getDescription());
                //Check Publish________________________________________________________________
                if (transfer.getPublish() == ProjectInfo.DONT_PUBLISH)
                    imgSync.setImageResource(R.drawable.ic_synchronize_red);
                else
                    imgSync.setImageResource(R.drawable.ic_synchronize_green);
                //Check Code______________________________________________________________________________
				/*if(!transfer.getTransferStoreCode().equals(ProjectInfo.DONT_CODE))
					tvCode.setText(transfer.getTrackingCode());
				else
					tvCode.setText("");*/
                //______________________________________________________________________________

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
                                        PayableTransferId = transfer.getId();
                                        if (transfer.getPublish() == ProjectInfo.DONT_PUBLISH) {
                                            Dialogdelete();
                                        } else
                                            Toast.makeText(mContext, getResources().getString(R.string.str_message_publish_delete), Toast.LENGTH_SHORT).show();
                                        break;
                                    case R.id.mnuEdit:
                                        if (transfer.getPublish() == ProjectInfo.DONT_PUBLISH) {
                                            Intent intent = new Intent(mContext, ManagePayableActivity.class);
                                            intent.putExtra(PAGE, PAGE_PAYABLE_LIST);
                                            intent.putExtra(MODE_PAGE, MODE_EDIT);
                                            intent.putExtra(ID, transfer.getId());
                                            startActivityForResult(intent, REQUESTCODE_MANAGE_PAYABLE);
                                        } else
                                            Toast.makeText(mContext, getResources().getString(R.string.str_message_publish_edit), Toast.LENGTH_SHORT).show();
                                        break;
                                    case R.id.mnuSend:
                                        PositionArray = position;
                                        PayableTransferId = transfer.getId();
                                        if (transfer.getPublish() == ProjectInfo.DONT_PUBLISH) {
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

        public ExpandListAdapter(Context context, List<PayableTransfer> groups) {
            this.context = context;
            this.PayableList = new ArrayList<PayableTransfer>();
            this.originalList = new ArrayList<PayableTransfer>();

            this.PayableList.addAll(groups);
            this.originalList.addAll(groups);
        }

        public void addItem(Cheque item, PayableTransfer group) {
            if (!PayableList.contains(group)) {
                PayableList.add(group);
            }
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


        public Object getGroup(int groupPosition) {
            // TODO Auto-generated method stub
            return PayableList.get(groupPosition);
        }

        /**
         * Gets the data associated with the given child within the given group.
         *
         * @param groupPosition the position of the group that the child resides in
         * @param childPosition the position of the child with respect to other
         *                      children in the group
         * @return the data of the child
         */
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        public int getGroupCount() {
            // TODO Auto-generated method stub
            return PayableList.size();
        }

        /**
         * Gets the number of children in a specified group.
         *
         * @param groupPosition the position of the group for which the children
         *                      count should be returned
         * @return the children count in the specified group
         */
        @Override
        public int getChildrenCount(int groupPosition) {
            return 0;
        }

        public long getGroupId(int groupPosition) {
            // TODO Auto-generated method stub
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {

            PayableTransfer group = (PayableTransfer) getGroup(groupPosition);
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
            PayableList.clear();
            Boolean CheckFilter = false;
            ArrayList<PayableTransfer> newList = new ArrayList<PayableTransfer>();

            if (query.isEmpty()) {
                PayableList.addAll(originalList);
            } else {
                for (PayableTransfer item : originalList) {
                    boolean result_contain = ServiceTools.CheckContainsWithSimillar(query, String.valueOf(item.getTransferCode()).toLowerCase());
                    if (String.valueOf(item.getTransferCode()).toLowerCase().contains(query))
                        if (result_contain) {
                            newList.add(item);
                            CheckFilter = true;
                        }

                }
                if (CheckFilter == false) {

                    for (PayableTransfer item : originalList) {
                        User user = db.getUser();
                        String txtUser = "";
                        if (user != null)
                            txtUser = user.getName();
                        boolean result_contain = ServiceTools.CheckContainsWithSimillar(query, String.valueOf(txtUser.toLowerCase()));
                        if (result_contain) {
                            newList.add(item);
                            CheckFilter = true;
                        }
                    }
                }
                PayableList.addAll(newList);
            }
            notifyDataSetChanged();
            tvPageTitle.setText(getString(R.string.str_nav_payable_list) + "(" + ExpandList.getCount() + ")");

        }
    }

    private void Dialogdelete() {
        FontDialog fontDialog = new FontDialog();
        final AlertDialog dialog = fontDialog.CustomeDialog(mContext, getString(R.string.str_message_restore));
        fontDialog.getPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.DeletePayable(PayableTransferId)) {
                    arrayPayableTransfer.remove(PositionArray);
                    expandlistAdapter = new ExpandListAdapter(mContext, arrayPayableTransfer);
                    ExpandList.setAdapter(expandlistAdapter);

                    tvPageTitle.setText(getString(R.string.str_nav_payable_list) + "(" + ExpandList.getCount() + ")");
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
            if (requestCode == REQUESTCODE_MANAGE_PAYABLE) {
                FillView();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, MENU_ADD, 0, R.string.str_add_payable).setIcon(R.drawable.ic_add_inverse)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, MENU_SORT, 0, R.string.str_sort).setIcon(R.drawable.ic_sort)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case MENU_ADD:
                Intent intent = new Intent(mContext, ManagePayableActivity.class);
                intent.putExtra(MODE_PAGE, MODE_NEW);
                intent.putExtra(PAGE, PAGE_PAYABLE_LIST);
                startActivityForResult(intent, REQUESTCODE_MANAGE_PAYABLE);
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
                    Collections.reverse(arrayPayableTransfer);
                    expandlistAdapter = new ExpandListAdapter(mContext, arrayPayableTransfer);
                    ExpandList.setAdapter(expandlistAdapter);
                }
                break;
            case R.id.sortDesc:
                if (!(sortType == ProjectInfo.SortDesc)) {
                    sortType = ProjectInfo.SortDesc;
                    item.setChecked(true);
                    Collections.reverse(arrayPayableTransfer);
                    expandlistAdapter = new ExpandListAdapter(mContext, arrayPayableTransfer);
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

        final List<PayableTransfer> payableTransfers = new ArrayList<>();
        PayableTransfer payableTransfer;

        payableTransfer = db.GetPayable(PayableTransferId);
        payableTransfers.add(payableTransfer);

        setAllDataBody.setPayableTransfers(payableTransfers);
        saveAllDataResultCall = apiService.SaveAllData(setAllDataBody);

        // Set up progress before call
        final FontProgressDialog progressDoalog;
        progressDoalog = new FontProgressDialog(PayableListActivity.this);
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
                    if (payableTransfers.size() > 0) {
                        for (int i = 0; i < payableTransfers.size(); i++) {
                            payableTransfers.get(i).setTransferAccountId(response.body().getData().getObjects().getPayableTransfers().getResults().get(i).getEntityID());
                            payableTransfers.get(i).setPublish(ProjectInfo.PUBLISH);
                            db.UpdatePayable(payableTransfers.get(i));
                        }
                    }
                    arrayPayableTransfer.get(PositionArray).setPublish(ProjectInfo.PUBLISH);
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
}
