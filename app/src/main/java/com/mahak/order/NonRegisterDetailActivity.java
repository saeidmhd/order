package com.mahak.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mahak.order.common.Customer;
import com.mahak.order.common.NonRegister;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.Reasons;
import com.mahak.order.common.SharedPreferencesHelper;
import com.mahak.order.common.Visitor;
import com.mahak.order.storage.DbAdapter;

import java.util.Calendar;

import static com.mahak.order.common.ServiceTools.getDateAndTimeForLong;

public class NonRegisterDetailActivity extends BaseActivity {

    private Bundle Extras;
    private static int page;
    private static int publish;

    private TextView
            tvCustomerName,
            tvMarketName,
            tvOrderDate,
            tvDescription,
            tvInvocieNumber,
            tvOrderType,
            tvPageTitle;

    private Context mContext;
    private DbAdapter db;
    private Customer customer;
    private Visitor visitor;
    private long nonRegisterID;

    int printerBrand;
    private NonRegister nonRegister;
    private Reasons reasons;

    public static int OrderType = 0;
    //oscar
    public static int lst_order_detail_for_print = R.layout.lst_order_detail_for_print_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_register_detail);

        //____config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________
        mContext = this;

        printerBrand = SharedPreferencesHelper.getPrefPrinterBrand(mContext);
        lst_order_detail_for_print = R.layout.lst_order_detail_for_print_return;
        Extras = getIntent().getExtras();
        if (Extras != null) {
            page = Extras.getInt(PAGE);
            nonRegisterID = Extras.getLong(ID);
            OrderType = Extras.getInt(TYPE_KEY);
            publish = Extras.getInt(RETURN_PUBLISH_KEY);
        }
        initialise();
        FillView();

    }//End of OnCreate

    private void initialise() {

        tvCustomerName = (TextView) findViewById(R.id.tvCustomerName);
        tvMarketName = (TextView) findViewById(R.id.tvMarketName);
        tvOrderDate = (TextView) findViewById(R.id.tvOrderDate);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvInvocieNumber = (TextView) findViewById(R.id.tvInvocieNumber);
        tvOrderType = (TextView) findViewById(R.id.tvOrderType);
        db = new DbAdapter(mContext);
    }

    /**
     * Read From Database And Fill TextView And Fill Adapter
     */
    private void FillView() {
        db.open();
        if (OrderType == ProjectInfo.TYPE_NON_REGISTER) {
            nonRegister = db.GetNonRegister(nonRegisterID);
            reasons = db.GetReason(nonRegister.getReasonCode());

            //Parse Json Description _______________________________________________________
            String Description = nonRegister.getDescription();
            if (nonRegister.getPersonId() == ProjectInfo.CUSTOMERID_GUEST) {
                Customer customer = db.getCustomerWithPersonClientId(nonRegister.getPersonClientId());
                tvCustomerName.setText(customer.getName());
                tvMarketName.setText(customer.getOrganization());
            } else {
                customer = db.getCustomerWithPersonId(nonRegister.getPersonId());
                tvCustomerName.setText(customer.getName());
                tvMarketName.setText(customer.getOrganization());
            }
            tvDescription.setText(Description);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(nonRegister.getNotRegisterDate());
            tvOrderDate.setText(getDateAndTimeForLong(nonRegister.getNotRegisterDate()) + "  " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
            if (reasons != null)
                tvOrderType.setText(reasons.getName());
            tvPageTitle.setText(getString(R.string.str_detail_nonRegister));
            tvInvocieNumber.setText(nonRegister.getCode());

        } else {

            //Parse Json Description _______________________________________________________
            String Description = nonRegister.getDescription();

            //Fill TextView_________________________________________________________________
            if (nonRegister.getPersonId() == ProjectInfo.CUSTOMERID_GUEST) {
                customer = db.getCustomerWithPersonClientId(nonRegister.getPersonClientId());
                tvCustomerName.setText(customer.getName());
                tvMarketName.setText(customer.getOrganization());
            } else if (nonRegister.getPersonId() != ProjectInfo.CUSTOMERID_GUEST) {
                if (OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
                    visitor = db.getVisitor(nonRegister.getPersonId());
                    tvCustomerName.setText(visitor.getName());
                } else {
                    customer = db.getCustomerWithPersonId(nonRegister.getPersonId());
                    tvCustomerName.setText(customer.getName());
                    tvMarketName.setText(customer.getOrganization());
                }
            }

            tvDescription.setText(Description);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(nonRegister.getNotRegisterDate());
            tvOrderDate.setText(getDateAndTimeForLong(nonRegister.getNotRegisterDate()) + "  " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
            tvInvocieNumber.setText(nonRegister.getCode());
            tvOrderType.setText(getString(R.string.str_type_invoice));
            tvPageTitle.setText(getString(R.string.str_detail_nonRegister));
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                if (page == PAGE_ADD_NON_REGISTER) {
                    Intent intent = new Intent(getApplicationContext(), NonRegisterListActivity.class);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_ORDER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (page == PAGE_NON_REGISTER_LIST)
                    finish();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (page == PAGE_ADD_NON_REGISTER) {
            Intent intent = new Intent(getApplicationContext(), NonRegisterListActivity.class);
            intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_ORDER);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (page == PAGE_NON_REGISTER_LIST)
            finish();

    }
}
