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

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mahak.order.common.Customer;
import com.mahak.order.common.Printer;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductDetail;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.Properties;
import com.mahak.order.common.ReceivedTransferProducts;
import com.mahak.order.common.ReceivedTransfers;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.SharedPreferencesHelper;
import com.mahak.order.common.Visitor;
import com.mahak.order.storage.DbAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.mahak.order.common.ProjectInfo.Woosim_WSP_R341;
import static com.mahak.order.common.ServiceTools.getDateAndTimeForLong;

public class TransferDetailActivity extends BaseActivity {

    private static String TAG_NAME = "Name";
    private static String TAG_LASTNAME = "LastName";
    private static String TAG_DESCRIPTION = "Description";
    private static String TAG_MARKET = "MarketName";

    private Bundle Extras;
    private static int page;
    private static int publish;
    private static String transferStoreId;

    private TextView
            tvCustomerName,
            tvMarketName,
            tvOrderDate,
            tvDescription,
            tvInvocieNumber,
            tvOrderType,
            tvPageTitle,
            CustomerType,
            tvTotalPrice;

    private Button btnPrint, btnAddReceipt;
    private ListView lstProduct;


    private Context mContext;
    private Activity mActivity;
    private DbAdapter db;
    private ReceivedTransfers receivedTransfers;
    private Customer customer;
    private Visitor visitor;
    private ArrayList<ReceivedTransferProducts> arrayReceivedTransferProducts;
    private AdapterListProduct adProduct;
    private AdapterListProductForPrint _adProduct;

    private View llInvoice;
    private String Name, LastName, MarketName, Description;
    int printerBrand;
    View ll;
    public static int OrderType = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_detail);

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

        printerBrand = SharedPreferencesHelper.getPrefPrinterBrand(mContext);

        Extras = getIntent().getExtras();
        if (Extras != null) {
            page = Extras.getInt(PAGE);
            transferStoreId = Extras.getString(ID);

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
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalCount);
        btnPrint = (Button) findViewById(R.id.btnPrint);
        btnAddReceipt = (Button) findViewById(R.id.btnAddReceipt);
        lstProduct = (ListView) findViewById(R.id.lstProduct);

        db = new DbAdapter(mContext);
    }

    ///////////////////////////////sml
    public void FillPrintView(View view) {
        //controls
        TextView _tvInvocieNumber = (TextView) view.findViewById(R.id._tvInvocieNumber);
        TextView _tvCustomerName = (TextView) view.findViewById(R.id._tvCustomerName);
        ListView _lstProduct = (ListView) view.findViewById(R.id._lstProduct);
        TextView _tvOrderDate = (TextView) view.findViewById(R.id._tvOrderDate);
        TextView _tvFooterMessage = (TextView) view.findViewById(R.id._tvFooterMessage);
        TextView _tvUsername = (TextView) view.findViewById(R.id._tvUsername);

        LinearLayout _llMarketName = (LinearLayout) view.findViewById(R.id._llMarketName);
        LinearLayout _llFooterMessage = (LinearLayout) view.findViewById(R.id._llFooterMessage);
        LinearLayout _llTotalReceipt = (LinearLayout) view.findViewById(R.id._llTotalReceipt);
        LinearLayout _llPayable = (LinearLayout) view.findViewById(R.id._llPayable);
        LinearLayout _llCustomerStatus2 = (LinearLayout) view.findViewById(R.id._llCustomerStatus2);
        LinearLayout _llCustomerStatus1 = (LinearLayout) view.findViewById(R.id._llCustomerStatus1);
        LinearLayout _llFinalPrice = (LinearLayout) view.findViewById(R.id._llFinalPrice);
        LinearLayout _llDiscount = (LinearLayout) view.findViewById(R.id._llDiscount);
        LinearLayout _llTotalItems = (LinearLayout) view.findViewById(R.id._llTotalItems);
        LinearLayout _llTotalCount = (LinearLayout) view.findViewById(R.id._llTotalCount);
        TextView CustomerName = (TextView) view.findViewById(R.id.tvCustomerName);
        TextView _tvType = (TextView) view.findViewById(R.id._tvType);
        TextView _txtPrice = (TextView) view.findViewById(R.id._txtPrice);
        TextView _tvTotalPrice = (TextView) view.findViewById(R.id._tvTotalPrice);
        TextView _txtTotalPrice = (TextView) view.findViewById(R.id._txtTotalPrice);



		/*if(order.getOrderType() == ProjectInfo.TYPE_INVOCIE)
			_tvType.setText(getString(R.string.str_type_sale_invoice));
		else if(order.getOrderType() == ProjectInfo.TYPE_SEND_TRANSFERENCE)
		{
            _tvType.setText(tvOrderType.getText().toString());
             CustomerName.setText("نام گیرنده حواله");
            _llMarketName.setVisibility(View.GONE);
            _llFooterMessage.setVisibility(View.GONE);
            _llTotalReceipt.setVisibility(View.GONE);
            _llPayable.setVisibility(View.GONE);
            _llCustomerStatus2.setVisibility(View.GONE);
            _llCustomerStatus1.setVisibility(View.GONE);
            _llFinalPrice.setVisibility(View.GONE);
            _llDiscount.setVisibility(View.GONE);

        }*/

		/*else
		    _tvType.setText(tvOrderType.getText().toString());*/

        _tvType.setText(tvOrderType.getText().toString());
        CustomerName.setText(R.string.transfer_sender_name);
        _llMarketName.setVisibility(View.GONE);
        _llFooterMessage.setVisibility(View.GONE);
        _llTotalReceipt.setVisibility(View.GONE);
        _llPayable.setVisibility(View.GONE);
        _llCustomerStatus2.setVisibility(View.GONE);
        _llCustomerStatus1.setVisibility(View.GONE);
        _llFinalPrice.setVisibility(View.GONE);
        _llDiscount.setVisibility(View.GONE);
        _txtPrice.setVisibility(View.GONE);
        _llTotalItems.setVisibility(View.GONE);
        _llTotalCount.setVisibility(View.GONE);
        _txtTotalPrice.setText(R.string.str_total_count);


        /*Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ServiceTools.toLong(receivedTransfers.getModifyDate())* 1000 );
        cal.add(Calendar.MINUTE,-210);*/
        _tvCustomerName.setText(tvCustomerName.getText().toString());
        _tvInvocieNumber.setText(tvInvocieNumber.getText().toString());
		/*_tvOrderDate.setText(GetDate(ServiceTools.toLong(receivedTransfers.getModifyDate()) * 1000 )+
                "  " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));*/
        _tvOrderDate.setText(getDateAndTimeForLong(receivedTransfers.getTransferDate()));
        if (BaseActivity.getAuthentication())
            _tvUsername.setText(BaseActivity.getUserProfile().getName());
        _adProduct = new AdapterListProductForPrint(mActivity, arrayReceivedTransferProducts);
        _lstProduct.setDrawingCacheEnabled(true);
        _lstProduct.setAdapter(_adProduct);
        ServiceTools.setListViewHeightBasedOnChildren(_lstProduct);
        _tvTotalPrice.setText(tvTotalPrice.getText().toString());
        _tvFooterMessage.setText(String.format(getResources().getString(R.string.print_footer_messages_part1),tvCustomerName.getText().toString()));

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
                ll = inflater.inflate(R.layout.factor_print_template_80mm, null, false);
            } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII) {
                ll = inflater.inflate(R.layout.factor_print_template_80mm, null, false);
                LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                ChangePrintWidth(_llPrint);
            } else if (printerBrand == ProjectInfo.PRINTER_BABY_280_A) {
                ll = inflater.inflate(R.layout.factor_print_template_50mm_fii_compact, null, false);
            } else if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW || printerBrand == ProjectInfo.UROVO_K319 || printerBrand == Woosim_WSP_R341) {
                ll = inflater.inflate(R.layout.factor_print_template_80mm, null, false);
                LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                ChangePrintWidth(_llPrint);
            } else if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310) {
                ll = inflater.inflate(R.layout.factor_print_template_80mm, null, false);
            } else {
                ll = inflater.inflate(R.layout.factor_print_template_compact, null, false);
            }

            FillPrintView(ll);
            ll.setDrawingCacheEnabled(true);
            ll.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            ll.layout(0, 0, ll.getMeasuredWidth(), ll.getMeasuredHeight());
            ll.buildDrawingCache(true);
            //b = Bitmap.createBitmap(ll.getDrawingCache());
            b = Printer.CreateBitmap(ll);
            ll.setDrawingCacheEnabled(false);

            fName = GetFileName(receivedTransfers.getTransferDate());
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
                Intent intent = new Intent(TransferDetailActivity.this, PrintActivity.class);
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

        receivedTransfers = db.GetReceivedTransfer(transferStoreId);

        arrayReceivedTransferProducts = new ArrayList<ReceivedTransferProducts>();
        //Read TotalReceipt___________________________________________________________

        //Read From Database ProductInOrder and Computing FinalPrice____________________
        arrayReceivedTransferProducts = db.getReceivedTransferProducts(transferStoreId);

        /*Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ServiceTools.toLong(receivedTransfers.getModifyDate())* 1000 );
       // cal.roll(Calendar.HOUR_OF_DAY,-3);
        cal.add(Calendar.MINUTE,-210);*/
        tvInvocieNumber.setText(String.valueOf(receivedTransfers.getTransferStoreClientId()));
        tvOrderType.setText(getString(R.string.str_type_received_transfer));
        tvPageTitle.setText(getString(R.string.str_detail_received_transfer));
        CustomerType.setText(R.string.transfer_sender_name);
        visitor = db.getVisitorWithVisitorID(ServiceTools.toLong(receivedTransfers.getSenderVisitorId()));
        tvCustomerName.setText(visitor.getName());

        tvOrderDate.setText(getDateAndTimeForLong(receivedTransfers.getTransferDate()));
        tvTotalPrice.setText(ServiceTools.formatCount(db.getTotalReceivedTransferById(receivedTransfers.getTransferStoreId())));
        for (ReceivedTransferProducts item : arrayReceivedTransferProducts) {
        }

        if (receivedTransfers.getDescription() != null) {
            tvDescription.setText(receivedTransfers.getDescription());
        } else
            tvDescription.setText("");

        tvOrderType.setText(getString(R.string.str_type_past_transfer));
        tvPageTitle.setText(getString(R.string.str_Receive_detail_transfer));

        //Fill Adapter_________________________________________________________
        adProduct = new AdapterListProduct(mActivity, arrayReceivedTransferProducts);
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
        return ServiceTools.getFileName(date) + receivedTransfers.getTransferStoreClientId() + ".png";
    }

    public class AdapterListProduct extends ArrayAdapter<ReceivedTransferProducts> {
        Activity mcontaxt;

        public AdapterListProduct(Activity contaxt, ArrayList<ReceivedTransferProducts> array) {
            super(contaxt, android.R.layout.simple_list_item_1, array);
            mcontaxt = contaxt;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowview = convertView;
            Holder holder = null;
            LayoutInflater inflater = null;
            final ReceivedTransferProducts product = getItem(position);

            if (rowview == null) {
                inflater = mcontaxt.getLayoutInflater();
                rowview = inflater.inflate(R.layout.lst_transfer_detail, null, false);
                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();
            holder.Populate(product, position);

            return rowview;
        }

        public class Holder {
            public TextView tvProductName, tvNumber, tvCount, tvProductPropSpec;
            public LinearLayout llitem, llprop_item;
            public View view_line;


            public Holder(View view) {
                tvProductName = (TextView) view.findViewById(R.id.tvProductSpec);
                tvNumber = (TextView) view.findViewById(R.id.tvNumber);
                tvCount = (TextView) view.findViewById(R.id.tvCount);
                tvProductPropSpec = (TextView) view.findViewById(R.id.tvProductPropSpec);

            }

            public void Populate(ReceivedTransferProducts product, int position) {


                ProductDetail productDetail = db.getProductDetail(ServiceTools.toLong(product.getProductDetailId()));
                Product product1 = db.GetProductWithProductId(productDetail.getProductId());

                String title = getTitle(productDetail);


                tvProductName.setText(product1.getName() + "\n" + title);
				/*ArrayList<OrderDetailProperty> orderDetailProperties = db.getAllOrderDetailProperty(product.getOrderId(),product.getProductId());
				if(orderDetailProperties.size() > 0){
					llprop_item.setVisibility(View.VISIBLE);
					view_line.setVisibility(View.VISIBLE);
					StringBuilder cTitle = new StringBuilder();
					for (OrderDetailProperty orderDetailProperty : orderDetailProperties){
						if(orderDetailProperty.getSumCountBaJoz()>0){
							cTitle.append("\n");
							cTitle.append(orderDetailProperty.getProductSpec() );
							cTitle.append(" : ");
							if ((int)(orderDetailProperty.getSumCountBaJoz()) == orderDetailProperty.getSumCountBaJoz())
								cTitle.append(String.valueOf((int)orderDetailProperty.getSumCountBaJoz()));
							else
								cTitle.append(String.valueOf(orderDetailProperty.getSumCountBaJoz()));
							cTitle.append("\n");
						}
					}
					tvProductPropSpec.setText(cTitle.toString());

				}*/

                tvCount.setText(ServiceTools.formatCount(product.getCount1()));
                tvNumber.setText(String.valueOf(position + 1));
            }
        }

    }// End of AdapterListProduct

    public class AdapterListProductForPrint extends ArrayAdapter<ReceivedTransferProducts> {
        Activity mcontaxt;

        public AdapterListProductForPrint(Activity contaxt, ArrayList<ReceivedTransferProducts> array) {
            super(contaxt, R.layout.lst_order_detail_for_print, array);
            mcontaxt = contaxt;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowview = convertView;
            Holder holder = null;
            LayoutInflater inflater = null;

            final ReceivedTransferProducts product = getItem(position);

            if (rowview == null) {
                inflater = mcontaxt.getLayoutInflater();
                rowview = inflater.inflate(R.layout.lst_order_detail_for_print, null, false);
                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();

            holder.Populate(product, position);

            return rowview;
        }

        public class Holder {
            public TextView tvPrice, tvProductName, tvNumber, tvCount;
            public LinearLayout llitem;

            public Holder(View view) {
                llitem = (LinearLayout) view.findViewById(R.id.llitem);
                tvProductName = (TextView) view.findViewById(R.id.tvProductSpec);
                tvPrice = (TextView) view.findViewById(R.id.tvPrice);
                tvCount = (TextView) view.findViewById(R.id.tvCount);
            }

            public void Populate(ReceivedTransferProducts product, int position) {


                //	TotalPrice = (long) (Price * product.getCount1());

                ProductDetail productDetail = db.getProductDetail(ServiceTools.toLong(product.getProductDetailId()));
                Product product1 = db.GetProductWithProductId(productDetail.getProductId());
                String title = getTitle(productDetail);
                tvProductName.setText(product1.getName() + "\n" + title);

                tvPrice.setVisibility(View.GONE);
                tvCount.setText(ServiceTools.formatCount(product.getCount1()));


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
                } else if (page == PAGE_ADD_INVOICE) {
                    Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_INVOCIE);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (page == PAGE_ORDERLIST)
                    finish();

                else if (page == PAGE_Invoice_Detail_Activity) {

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
        } else if (page == PAGE_Invoice_Detail_Activity) {

            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private String getTitle(ProductDetail productDetail) {
        String result = "";
        StringBuilder title = new StringBuilder();
        Gson gson = new Gson();
        Type property = new TypeToken<ArrayList<Properties>>() {
        }.getType();
        List<Properties> propertiesList = new ArrayList<>();
        try {
            propertiesList = gson.fromJson(productDetail.getProperties(), property);
        } catch (JsonSyntaxException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        if (propertiesList != null) {
            if (propertiesList.size() > 0) {
                for (Properties properties : propertiesList) {
                    if (!title.toString().toLowerCase().contains(properties.getValue().toLowerCase())) {
                        title.append(properties.getValue());
                        title.append(" - ");
                    }
                }
                result = removeLast(title.toString());
                //propertyTitleSet.add(result);
            }
        }
        return result;
    }

    private String removeLast(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ' ') {
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }

}
