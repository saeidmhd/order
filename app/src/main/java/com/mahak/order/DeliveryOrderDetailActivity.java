package com.mahak.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mahak.order.common.Customer;
import com.mahak.order.common.Order;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;

//import com.mahak.order.common.ProductInDeliveryOrder;
//import com.mahak.order.common.ProductInOrder;

public class DeliveryOrderDetailActivity extends BaseActivity {

    private static int REQUEST_CODE_MANAGE_DELIVERY_ORDER = 1;
    private Bundle Extras;
    private TextView tvCustomerName, tvMarketName, tvDeliveryDate, tvTotalPrice, tvDiscount, tvFinalPrice, tvDescription, tvInvocieNumber, tvTotalOff, tvTotalChargeAndTax;
    private Button btnEdit;
    private ListView lstProduct;
    private Context mContext;
    private Activity mActivity;
    private DbAdapter db;
    private Order deliveryorder;
    private Customer customer;
    private long DeliveryId;
    private ArrayList<OrderDetail> orderDetails;
    private AdapterListProduct adProduct;
    private double TotalPrice, FinalPrice, Discount;
    private int CustomerId;
    private double TotalOff = 0;
    private double TotalCharge = 0;
    private double TotalTax = 0;
    private double Tax, Charge, off;
    private double ChargePercent;
    private double TaxPercent;
    private double offValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order_detail);

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
        TextView tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        tvPageTitle.setText(getString(R.string.str_detail_delivery));
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________
        mContext = this;
        mActivity = this;

        Extras = getIntent().getExtras();
        if (Extras != null)
            DeliveryId = Extras.getLong(ID);

        initialise();

        FillView();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                intent.putExtra(BaseActivity.CUSTOMERID_KEY, deliveryorder.getPersonId());
                intent.putExtra(BaseActivity.TYPE_KEY, ProjectInfo.TYPE_Delivery);
                intent.putExtra(BaseActivity.MODE_PAGE, BaseActivity.MODE_EDIT);
                intent.putExtra(BaseActivity.ID, deliveryorder.getId());
                intent.putExtra(BaseActivity.PAGE, BaseActivity.PAGE_ADD_INVOICE);
                startActivityForResult(intent, REQUEST_CODE_MANAGE_DELIVERY_ORDER);
            }
        });

    }//End of OnCreate

    /**
     * Initializing Variables
     */
    private void initialise() {

        tvCustomerName = (TextView) findViewById(R.id.tvCustomerName);
        tvMarketName = (TextView) findViewById(R.id.tvMarketName);
        tvDeliveryDate = (TextView) findViewById(R.id.tvDeliveryDate);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalCount);
        tvFinalPrice = (TextView) findViewById(R.id.tvFinalPrice);
        tvDiscount = (TextView) findViewById(R.id.tvDiscount);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvInvocieNumber = (TextView) findViewById(R.id.tvInvocieNumber);
        tvTotalOff = (TextView) findViewById(R.id.tvTotalOff);
        tvTotalChargeAndTax = (TextView) findViewById(R.id.tvTotalChargeAndTax);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        //btnConvert = findViewById(R.id.btnConvert);
        lstProduct = (ListView) findViewById(R.id.lstProduct);

        db = new DbAdapter(mContext);
    }

    /**
     * Read From db Information And Fill Adapter
     */
    private void FillView() {

        db.open();
        TotalPrice = 0;
        TotalCharge = 0;
        TotalTax = 0;
        TotalOff = 0;
        FinalPrice = 0;
        Discount = 0;
        //Read From Database Delivery Order__________________________________________________
        deliveryorder = db.GetOrder(DeliveryId);
        CustomerId = deliveryorder.getPersonId();
        Discount = deliveryorder.getDiscount();
        orderDetails = new ArrayList<>();

        //Read From Database Product in Delivery Order And Calculation  FinalPrice____________
        orderDetails = db.getAllProductDeliveryOrderDetail(deliveryorder.getOrderId());

        for (OrderDetail item : orderDetails) {

            ProductPickerListActivity.HashMap_Product.put(item.getProductId(), item);

            double Price = 0;
            if (item.getCount1() != 0 || item.getCount2() != 0)
                Price = (item.getPrice() * item.getSumCountBaJoz());
            offValue = item.getDiscount();
            TotalPrice += Price;
            off = (offValue * 1);
            Price = Price - off;
            TotalOff += off;

            TaxPercent = item.getTaxPercent();
            ChargePercent = item.getChargePercent();
            Tax = ((Price * TaxPercent) / 100);
            Charge = ((Price * ChargePercent) / 100);
            TotalCharge += Charge;
            TotalTax += Tax;
        }
        Discount -= TotalOff;
        FinalPrice = (TotalPrice - TotalOff) + (TotalTax + TotalCharge);
        FinalPrice = FinalPrice - Discount;

        //Check Publish For Enable And Disable btnEdit________________________________________
        if (deliveryorder.getPublish() == ProjectInfo.PUBLISH)
            btnEdit.setEnabled(false);
        else if (deliveryorder.getPublish() == ProjectInfo.DONT_PUBLISH)
            btnEdit.setEnabled(true);
        //Check CustomerTest Read From database or Guest__________________________________________
        if (deliveryorder.getPersonId() == ProjectInfo.CUSTOMERID_GUEST) {
            tvCustomerName.setText(getResources().getString(R.string.str_guest_customer));
            tvMarketName.setText(getResources().getString(R.string.str_market_name));
        } else if (deliveryorder.getPersonId() != ProjectInfo.CUSTOMERID_GUEST) {
            customer = db.getCustomerWithPersonId(deliveryorder.getPersonId());
            tvCustomerName.setText(customer.getName());
            tvMarketName.setText(customer.getOrganization());
        }
        //Fill Textview_______________________________________________________
        tvDescription.setText(deliveryorder.getDescription());
        tvTotalPrice.setText(ServiceTools.formatPrice(TotalPrice));
        tvTotalOff.setText(ServiceTools.formatPrice(TotalOff));
        tvTotalChargeAndTax.setText(ServiceTools.formatPrice(TotalTax + TotalCharge));
        tvDiscount.setText(ServiceTools.formatPrice(Discount));
        tvFinalPrice.setText(ServiceTools.formatPrice(FinalPrice));
        tvDeliveryDate.setText(ServiceTools.getDateAndTimeForLong(deliveryorder.getDeliveryDate()));
        tvInvocieNumber.setText(deliveryorder.getCode());
        //Fill Adapter _________________________________________________________
        adProduct = new AdapterListProduct(mActivity, orderDetails);
        lstProduct.setAdapter(adProduct);
        ServiceTools.setListViewHeightBasedOnChildren(lstProduct);

        db.close();
    }

    /**
     * Copy Delivery Order AND ProductIndeliveryOrder To Order And productInOrder And Update Asset Product
     */

    public class AdapterListProduct extends ArrayAdapter<OrderDetail> {
        Activity mContext;

        public AdapterListProduct(Activity context, ArrayList<OrderDetail> array) {
            super(context, android.R.layout.simple_list_item_1, array);
            mContext = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowview = convertView;
            Holder holder = null;
            LayoutInflater inflater = null;
            final OrderDetail product = getItem(position);

            if (rowview == null) {
                inflater = mContext.getLayoutInflater();
                rowview = inflater.inflate(R.layout.lst_order_detail, null, false);
                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();

            holder.Populate(product, position);

            return rowview;
        }

        public class Holder {
            public TextView tvPrice, tvProductName, tvNumber, tvGift, tvCount, tvFee, tvOff, tvChargeAndTax, tvFinalPriceProduct, tvDescription;

            public Holder(View view) {
                tvProductName = (TextView) view.findViewById(R.id.tvProductNameSpec);
                tvPrice = (TextView) view.findViewById(R.id.tvPrice);
                tvNumber = (TextView) view.findViewById(R.id.tvNumber);
                tvGift = (TextView) view.findViewById(R.id.tvGift);
                tvCount = (TextView) view.findViewById(R.id.tvCount);
                tvFee = (TextView) view.findViewById(R.id.tvFee);
                tvOff = (TextView) view.findViewById(R.id.tvOff);
                tvFinalPriceProduct = (TextView) view.findViewById(R.id.tvFinalPriceProduct);
                tvChargeAndTax = (TextView) view.findViewById(R.id.tvChargeAndTax);
                tvDescription = (TextView) view.findViewById(R.id.tvDescription);
            }

            public void Populate(OrderDetail orderDetail, int position) {

                double TotalPrice = 0;
                double off = 0;
                double Tax = 0;
                double Charge = 0;
                double offValue = orderDetail.getDiscount();
                double TaxPercent = orderDetail.getTaxPercent();
                double ChargePercent = orderDetail.getChargePercent();
                double Price = orderDetail.getPrice();
                TotalPrice = (Price * (orderDetail.getSumCountBaJoz()));
                Price = (Price * (orderDetail.getSumCountBaJoz()));
                off = (offValue * 1);
                Price = Price - off;
                Tax = ((Price * TaxPercent) / 100);
                Charge = ((Price * ChargePercent) / 100);
                double TaxAndCharge = Tax + Charge;
                Price = Price + TaxAndCharge;

                //long Price = ServiceTools.toLong(orderDetail.getPrice())* orderDetail.getCount1();
                tvProductName.setText(orderDetail.getProductName());
                tvDescription.setText(orderDetail.getDescription());
                tvFee.setText(ServiceTools.formatPrice(orderDetail.getPrice()));
                tvPrice.setText(ServiceTools.formatPrice(TotalPrice));
                tvFinalPriceProduct.setText(ServiceTools.formatPrice(Price));
                tvCount.setText(ServiceTools.formatCount(orderDetail.getSumCountBaJoz()));
                tvNumber.setText(String.valueOf(position + 1));
                tvOff.setText(ServiceTools.formatPrice(off));
                tvChargeAndTax.setText(ServiceTools.formatPrice(TaxAndCharge));
            }
        }

    }// End of AdapterListProduct

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_MANAGE_DELIVERY_ORDER) {
                FillView();
                setResult(RESULT_OK);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }
}
