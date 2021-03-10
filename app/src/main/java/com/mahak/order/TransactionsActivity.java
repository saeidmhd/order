package com.mahak.order;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.common.Customer;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.TransactionsLog;
import com.mahak.order.storage.DbAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TransactionsActivity extends BaseActivity {

    private ListView lstTransaction;
    private DbAdapter db;
    private Context mContext;
    private Activity mActivity;
    private ArrayList<TransactionsLog> arrayTransacion = new ArrayList<TransactionsLog>();
    private AdapterTransaction adTransaction;
    private Bundle Extras;
    private int CustomerId = 0;
    private TextView tvPageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
        tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        tvPageTitle.setText(getString(R.string.str_transactionlog));
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________
        mContext = this;
        mActivity = this;

        initialise();

        Extras = getIntent().getExtras();
        if (Extras != null)
            CustomerId = Extras.getInt(CUSTOMERID_KEY);

        FillView();
    }//end of onCreate

    /**
     * Initializing Variables
     */
    private void initialise() {
        lstTransaction = (ListView) findViewById(R.id.lstTransactions);
        db = new DbAdapter(mContext);
    }

    /**
     * Read From Database And Fill Adapter
     */


    private String before(String value, String a) {
        // Return substring containing all characters before a string.
        int posA = value.indexOf(a);
        if (posA == -1) {
            return "";
        }
        return value.substring(0, posA);
    }

    private void FillView() {
        db.open();
        arrayTransacion = db.getAllTransactionlog(CustomerId);
        adTransaction = new AdapterTransaction(mActivity, arrayTransacion);
        lstTransaction.setAdapter(adTransaction);
        Customer customer = db.getCustomerWithPersonId(CustomerId);
        if (customer != null)
            tvPageTitle.setText(getString(R.string.str_transactionlog) + "(" + customer.getName() + ")");
    }

    private class AdapterTransaction extends ArrayAdapter<TransactionsLog> {
        Activity mcontaxt;

        private class Holder {
            public TextView tvStatus, tvRemainedAmount, tvCreditAmount, tvDebitAmount, tvNumber, tvDescription, tvDate;

            public Holder(View view) {
                tvStatus = (TextView) view.findViewById(R.id.tvStatus);
                tvRemainedAmount = (TextView) view.findViewById(R.id.tvRemainedAmount);
                tvCreditAmount = (TextView) view.findViewById(R.id.tvCreditAmount);
                tvDebitAmount = (TextView) view.findViewById(R.id.tvDebitAmount);
                tvNumber = (TextView) view.findViewById(R.id.tvNumber);
                tvDescription = (TextView) view.findViewById(R.id.tvDescription);
                tvDate = (TextView) view.findViewById(R.id.tvDate);
            }

            public void Populate(TransactionsLog transaction, int position) {


                tvRemainedAmount.setText(ServiceTools.formatPrice(transaction.getBalance()));
                tvCreditAmount.setText(ServiceTools.formatPrice(transaction.getCreditAmount()));
                tvDebitAmount.setText(ServiceTools.formatPrice(transaction.getDebtAmount()));

                switch (transaction.getStatus()) {
                    case 0:
                        tvStatus.setText(R.string.zero_balance);
                        break;
                    case 1:
                        tvStatus.setText(R.string.debtor);
                        break;
                    case 2:
                        tvStatus.setText(R.string.creditor);
                        break;
                }
                tvNumber.setText(String.valueOf(position + 1));
                tvDescription.setText(transaction.getDescription());

                String pattern = "yyyy-MM-dd'T'HH:mm:ss";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                try {
                    Date date = simpleDateFormat.parse(transaction.getTransactionDate());
                    tvDate.setText(before(transaction.getTransactionDate(), "T"));
                } catch (ParseException e) {
                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                    e.printStackTrace();
                }
            }
        }

        AdapterTransaction(Activity contaxt, ArrayList<TransactionsLog> array) {
            super(contaxt, android.R.layout.simple_list_item_1, array);
            mcontaxt = contaxt;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowview = convertView;
            Holder holder = null;
            LayoutInflater inflater = null;
            final TransactionsLog transaction = getItem(position);

            if (rowview == null) {
                inflater = mcontaxt.getLayoutInflater();
                rowview = inflater.inflate(R.layout.lst_transaction_item, null, false);
                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();
            holder.Populate(transaction, position);

            return rowview;
        }

    }

    @Override
    protected void onResume() {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }
}
