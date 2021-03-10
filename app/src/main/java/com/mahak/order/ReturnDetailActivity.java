package com.mahak.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mahak.order.common.Customer;
import com.mahak.order.common.Order;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.OrderDetailProperty;
import com.mahak.order.common.Printer;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.Reasons;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.SharedPreferencesHelper;
import com.mahak.order.common.Visitor;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;
import java.util.Calendar;

import static com.mahak.order.common.ProjectInfo.Woosim_WSP_R341;
import static com.mahak.order.common.ServiceTools.formatCount;
import static com.mahak.order.common.ServiceTools.getDateAndTimeForLong;

//import com.mahak.order.common.ProductInOrder;
//import com.mahak.order.common.ProductInReturn;
//import com.mahak.order.common.ReturnOfSale;

public class ReturnDetailActivity extends BaseActivity {

    private static String TAG_NAME = "Name";
    private static String TAG_LASTNAME = "LastName";
    private static String TAG_DESCRIPTION = "Description";
    private static String TAG_MARKET = "MarketName";

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
            tvPageTitle,
            CustomerType,
            tvTotalCount;

    private Button btnPrint;
    private ListView lstProduct;

    private Context mContext;
    private Activity mActivity;
    private DbAdapter db;
    private Order order;
    private Customer customer;
    private Visitor visitor;
    private long OrderId;
    private ArrayList<OrderDetail> orderDetails;
    //private ArrayList<ProductInReturn> arrayProductInReturn;
    private AdapterListProduct adProduct;
    private AdapterListProductForPrint _adProduct;
    private double TotalCount = 0;
    private double TotalItems = 0;
    private View llInvoice;
    private String Name, LastName, MarketName, Description;
    int printerBrand;
    View ll;
    public static int OrderType = 0;

    //oscar
    public static int lst_order_detail_for_print = R.layout.lst_order_detail_for_print_return;
    private Reasons reasons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_detail);


        //____config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
        tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________
        mContext = this;
        mActivity = this;

        tvPageTitle.setText(getString(R.string.str_detail_return));

        printerBrand = SharedPreferencesHelper.getPrefPrinterBrand(mContext);
        lst_order_detail_for_print = R.layout.lst_order_detail_for_print_return;
        Extras = getIntent().getExtras();
        if (Extras != null) {
            page = Extras.getInt(PAGE);
            OrderId = Extras.getLong(ID);
            OrderType = Extras.getInt(TYPE_KEY);
            publish = Extras.getInt(RETURN_PUBLISH_KEY);

        }
        initialise();
        FillView();
        ///////////////////////sml
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreparePrinterData ppd = new PreparePrinterData();
                ppd.execute();
            }
        });

    }//End of OnCreate

    /**
     * Initializing Variables
     */

    private void initialise() {

        tvCustomerName = (TextView) findViewById(R.id.tvCustomerName);
        tvMarketName = (TextView) findViewById(R.id.tvMarketName);
        tvOrderDate = (TextView) findViewById(R.id.tvOrderDate);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvInvocieNumber = (TextView) findViewById(R.id.tvInvocieNumber);
        tvOrderType = (TextView) findViewById(R.id.tvOrderType);
        CustomerType = (TextView) findViewById(R.id.CustomerType);
        tvTotalCount = (TextView) findViewById(R.id.tvTotalCount);
        llInvoice = findViewById(R.id.llInvoice);

        btnPrint = (Button) findViewById(R.id.btnPrint);
        lstProduct = (ListView) findViewById(R.id.lstProduct);

        db = new DbAdapter(mContext);
    }

    ///////////////////////////////sml
    public void FillPrintView(View view) {
        //controls
        TextView _tvInvocieNumber = (TextView) view.findViewById(R.id._tvInvocieNumber);
        TextView _tvTotalItems = (TextView) view.findViewById(R.id._tvTotalItems);
        TextView _tvCustomerName = (TextView) view.findViewById(R.id._tvCustomerName);
        ListView _lstProduct = (ListView) view.findViewById(R.id._lstProduct);
        TextView _tvTotalCount = (TextView) view.findViewById(R.id._tvTotalCount);
        TextView _tvOrderDate = (TextView) view.findViewById(R.id._tvOrderDate);
        TextView _tvType = (TextView) view.findViewById(R.id._tvType);
        TextView _tvReason = (TextView) view.findViewById(R.id._tvReason);
        TextView _tvUsername = (TextView) view.findViewById(R.id._tvUsername);
        TextView CustomerName = (TextView) view.findViewById(R.id.tvCustomerName);
        if (OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {
            _tvType.setText(R.string.return_of_sale);
            CustomerName.setText(R.string.customer_name);
        } else
            _tvType.setText(tvOrderType.getText().toString());
        _tvCustomerName.setText(tvCustomerName.getText().toString());
        _tvTotalCount.setText(tvTotalCount.getText().toString());
        _tvInvocieNumber.setText(tvInvocieNumber.getText().toString());
        _tvOrderDate.setText(tvOrderDate.getText().toString());
        _tvReason.setText(tvOrderType.getText().toString());

        if ((int) (TotalItems) == TotalItems)
            _tvTotalItems.setText("" + (int) (TotalItems));
        else
            _tvTotalItems.setText(formatCount(TotalItems));

        if (BaseActivity.getAuthentication())
            _tvUsername.setText(BaseActivity.getUserProfile().getName());

        _adProduct = new AdapterListProductForPrint(mActivity, orderDetails);
        _lstProduct.setDrawingCacheEnabled(true);
        _lstProduct.setAdapter(_adProduct);
        ServiceTools.setListViewHeightBasedOnChildren(_lstProduct);
    }

    private class PreparePrinterData extends AsyncTask<String, Integer, Boolean> {

        Bitmap b = null;
        String fName = "";
        String fPath = ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_IMAGES + "/" + ProjectInfo.DIRECTORY_INVOICES;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean status = false;

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (printerBrand == ProjectInfo.PRINTER_BABY_380_A || printerBrand == ProjectInfo.PRINTER_DELTA_380_A) {
                ll = inflater.inflate(R.layout.return_print_80, null, false);
            } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII) {

                ll = inflater.inflate(R.layout.return_print_80, null, false);
                // Gets the layout params that will allow you to resize the layout
                LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                ChangePrintWidth(_llPrint);

            } else if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW || printerBrand == ProjectInfo.UROVO_K319 || printerBrand == Woosim_WSP_R341) {
                ll = inflater.inflate(R.layout.return_print_80, null, false);
                // Gets the layout params that will allow you to resize the layout
                LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                ChangePrintWidth(_llPrint);

            } else if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310) {
                ll = inflater.inflate(R.layout.return_print_80, null, false);
            } else if (printerBrand == ProjectInfo.PRINTER_BABY_280_A) {
                ll = inflater.inflate(R.layout.return_print_50, null, false);
            } else {
                ll = inflater.inflate(R.layout.return_print, null, false);
            }

            LinearLayout _ll_Remain_Detail = (LinearLayout) ll.findViewById(R.id._ll_Remain_Detail);
            LinearLayout _llFooterMessage = (LinearLayout) ll.findViewById(R.id._llFooterMessage);
            if (!SharedPreferencesHelper.getDetailUnderFactor(mContext)) {
                _ll_Remain_Detail.setVisibility(View.GONE);
            }
            if (!SharedPreferencesHelper.getSignUnderFactor(mContext)) {
                _llFooterMessage.setVisibility(View.GONE);
            }

            FillPrintView(ll);
            ll.setDrawingCacheEnabled(true);
            ll.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            ll.layout(0, 0, ll.getMeasuredWidth(), ll.getMeasuredHeight());
            ll.buildDrawingCache(true);
            //b = Bitmap.createBitmap(ll.getDrawingCache());
            b = Printer.CreateBitmap(ll);
            ll.setDrawingCacheEnabled(false);

            if (OrderType == ProjectInfo.TYPE_RETURN_OF_SALE)
                fName = GetFileName(order.getOrderDate());
            else
                fName = GetFileName(order.getOrderDate());
            if (b != null) {
                if (Printer.CreateFile(b, fName, fPath)) {
                }
            }

            if (b != null)
                status = true;

            return status;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                Intent intent = new Intent(ReturnDetailActivity.this, PrintActivity.class);
                intent.putExtra(ProjectInfo._TAG_PAGE_NAME, ProjectInfo._pName_OrderDetail);
                intent.putExtra(ProjectInfo._TAG_PATH, fPath);
                intent.putExtra(ProjectInfo._TAG_Name, fName);
                startActivity(intent);
            }
        }
    }

    public void ChangePrintWidth(LinearLayout ll) {

        LayoutParams param = ll.getLayoutParams();
        final float scale = getResources().getDisplayMetrics().density;
        //convert mm to dp
        double Size = SharedPreferencesHelper.getCurrentWidthSize(mContext) * 6.3;
        int converter = (int) (Size * scale + 0.5f);
        param.width = converter;
        ll.setLayoutParams(param);

    }

    /**
     * Read From Database And Fill TextView And Fill Adapter
     */
    private void FillView() {
        db.open();
        //Read Order From Database_____________________________________________________
        order = db.GetOrder(OrderId);
        reasons = db.GetReason(order.getReturnReasonId());
        orderDetails = new ArrayList<>();
        //Read TotalReceipt___________________________________________________________

        //Read From Database ProductInOrder and Computing FinalPrice____________________
        orderDetails = db.getAllProductWithOrderDetail(order.getId());
        for (OrderDetail item : orderDetails) {
            double Price = (item.getPrice() * item.getSumCountBaJoz());
            TotalCount += item.getSumCountBaJoz();
        }
        TotalItems = orderDetails.size();
        tvTotalCount.setText(formatCount(TotalCount));
        //Parse Json Description _______________________________________________________
        Description = order.getDescription();
        /*ParseJsonDescription(JsonDescription);*/
        //Fill TextView_________________________________________________________________

        if (order.getPersonId() == ProjectInfo.CUSTOMERID_GUEST)
            customer = db.getCustomerWithPersonClientId(order.getPersonClientId());
        else
            customer = db.getCustomerWithPersonId(order.getPersonId());

        tvCustomerName.setText(customer.getName());
        tvMarketName.setText(customer.getOrganization());
        tvDescription.setText(Description);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(order.getOrderDate());
        tvOrderDate.setText(getDateAndTimeForLong(order.getOrderDate()) + "  " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
        tvInvocieNumber.setText(order.getCode());

        if (reasons != null)
            tvOrderType.setText(reasons.getName());

        //Fill Adapter_________________________________________________________
        adProduct = new AdapterListProduct(mActivity, orderDetails);
        lstProduct.setAdapter(adProduct);
        ServiceTools.setListViewHeightBasedOnChildren(lstProduct);
    }

    /**
     * ReturnOfSale FileName
     *
     * @param date
     * @return
     */

    public String GetFileName(long date) {
        return ServiceTools.getFileName(date) + order.getCode() + ".png";
    }
    /*
     */

    /**
     * Parse Json Description
     *
     * @param
     *//*
	public void ParseJsonDescription(String json)
	{
		try {
			JSONObject jobject = new JSONObject(json);
			Description = jobject.getString(TAG_DESCRIPTION);
			Name = jobject.getString(TAG_NAME);
			LastName = jobject.getString(TAG_LASTNAME);
			MarketName = jobject.getString(TAG_MARKET);
		} catch (Exception e) {
                                   FirebaseCrashlytics.getInstance().setCustomKey("user_tell",BaseActivity.getPrefname() +"_"+ BaseActivity.getPrefTell());
FirebaseCrashlytics.getInstance().recordException(e);
			// TODO: handle exception
		}
	}*/

    public class AdapterListProduct extends ArrayAdapter<OrderDetail> {
        Activity mcontaxt;

        public AdapterListProduct(Activity contaxt, ArrayList<OrderDetail> array) {
            super(contaxt, android.R.layout.simple_list_item_1, array);
            mcontaxt = contaxt;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowview = convertView;
            Holder holder = null;
            LayoutInflater inflater = null;
            final OrderDetail product = getItem(position);

            if (rowview == null) {
                inflater = mcontaxt.getLayoutInflater();
                rowview = inflater.inflate(R.layout.lst_return_detail, null, false);
                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();

            holder.Populate(product, position);

            return rowview;
        }

        public class Holder {
            public TextView tvProductName, tvNumber, tvCount, tvProductPropSpec;
            LinearLayout llprop_item;

            public Holder(View view) {
                tvProductName = (TextView) view.findViewById(R.id.tvProductSpec);
                tvNumber = (TextView) view.findViewById(R.id.tvNumber);
                tvCount = (TextView) view.findViewById(R.id.tvCount);
                tvProductPropSpec = (TextView) view.findViewById(R.id.tvProductPropSpec);
                llprop_item = (LinearLayout) view.findViewById(R.id.llprop_item);
            }

            public void Populate(OrderDetail orderDetail, int position) {
                if (OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {
                    tvProductName.setText(orderDetail.getProductName());
                    tvCount.setText(formatCount(orderDetail.getSumCountBaJoz()));
                    tvNumber.setText(String.valueOf(position + 1));
                    ArrayList<OrderDetailProperty> orderDetailProperties = db.getAllOrderDetailProperty(orderDetail.getOrderId(), orderDetail.getProductId());
                    if (orderDetailProperties.size() > 0) {
                        llprop_item.setVisibility(View.VISIBLE);
                        StringBuilder cTitle = new StringBuilder();
                        for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
                            if (orderDetailProperty.getSumCountBaJoz() > 0) {
                                cTitle.append("\n");
                                cTitle.append(orderDetailProperty.getProductSpec());
                                cTitle.append(" : ");

                                if ((int) (orderDetailProperty.getSumCountBaJoz()) == orderDetailProperty.getSumCountBaJoz())
                                    cTitle.append(String.valueOf((int) orderDetailProperty.getSumCountBaJoz()));
                                else
                                    cTitle.append(String.valueOf(orderDetailProperty.getSumCountBaJoz()));
                                cTitle.append("\n");
                            }
                        }
                        tvProductPropSpec.setText(cTitle.toString());
                    }
                }
            }
        }

    }// End of AdapterListProduct

    public class AdapterListProductForPrint extends ArrayAdapter<OrderDetail> {
        Activity mcontaxt;

        public AdapterListProductForPrint(Activity contaxt, ArrayList<OrderDetail> array) {

            super(contaxt, lst_order_detail_for_print, array);
            mcontaxt = contaxt;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowview = convertView;
            Holder holder = null;
            LayoutInflater inflater = null;

            final OrderDetail product = getItem(position);

            if (rowview == null) {
                inflater = mcontaxt.getLayoutInflater();
                rowview = inflater.inflate(lst_order_detail_for_print, null, false);
                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();
            holder.Populate(product, position);
            return rowview;
        }

        public class Holder {
            public TextView tvPrice, tvProductName, tvNumber, tvGift, tvCount, tvOff, tvChargeAndTax, tvFinalPriceProduct;
            public LinearLayout llitem;

            public Holder(View view) {
                llitem = (LinearLayout) view.findViewById(R.id.llitem);
                tvProductName = (TextView) view.findViewById(R.id.tvProductSpec);
                tvPrice = (TextView) view.findViewById(R.id.tvPrice);
                tvCount = (TextView) view.findViewById(R.id.tvCount);
            }

            public void Populate(OrderDetail orderDetail, int position) {

			    /*float size = SharedPreferencesHelper.getCurrentFontSize(mcontaxt);
                tvProductName.setTextSize(size);
                tvPrice.setTextSize(size);
                tvCount.setTextSize(size);*/
                tvProductName.setText(orderDetail.getProductName());
                tvCount.setText(formatCount(orderDetail.getSumCountBaJoz()));

            }
        }

    }// End of AdapterListProduct

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (page == PAGE_DASHBORD) {
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (page == PAGE_CHECKLIST) {
                    Intent intent = new Intent(getApplicationContext(), CheckListDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (page == PAGE_ADD_ORDER) {
                    Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_ORDER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (page == PAGE_ADD_SEND_TRANSFER) {
                    Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_ORDER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (page == PAGE_ADD_INVOICE) {
                    Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_INVOCIE);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (page == PAGE_ORDERLIST)
                    finish();


                else if (page == PAGE_Invoice_Detail_Activity && OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE) {

                    Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_SEND_TRANSFERENCE);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (page == PAGE_Invoice_Detail_Activity) {

                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (page == PAGE_DASHBORD) {
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (page == PAGE_CHECKLIST) {
            Intent intent = new Intent(getApplicationContext(), CheckListDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (page == PAGE_ADD_ORDER) {
            Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
            intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_ORDER);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (page == PAGE_ADD_SEND_TRANSFER) {
            Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
            intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_ORDER);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (page == PAGE_ADD_INVOICE) {
            Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
            intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_INVOCIE);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (page == PAGE_ADD_SEND_TRANSFER) {
            Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
            intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_SEND_TRANSFERENCE);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (page == PAGE_ORDERLIST) {
            finish();
        } else if (page == PAGE_Invoice_Detail_Activity && OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE) {

            Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
            intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_SEND_TRANSFERENCE);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (page == PAGE_Invoice_Detail_Activity) {

            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

}
