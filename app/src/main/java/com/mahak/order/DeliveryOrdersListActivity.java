package com.mahak.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filter.FilterListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mahak.order.common.Customer;
import com.mahak.order.common.Order;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.Person_Extra_Data;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.widget.DrawableClickListener;
import com.mahak.order.widget.FontEditText;

import java.util.ArrayList;

public class DeliveryOrdersListActivity extends BaseActivity {

    private static int REQUEST_CODE_DELIVERY_ORDER_DETAIL = 1;
    private Context mContext;
    private Activity mActivity;
    private FontEditText txtSearch;
    private ListView lstDeliveryOrder;
    private DbAdapter db;
    private ArrayList<Order> arrayDeliveryOrder;
    private AdapterListDeliveryOrder adDeliveryOrder;
    private TextView tvPageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_orders_list);

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);


        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
        tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        tvPageTitle.setText(getString(R.string.str_nav_delivery_list));
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________
        mContext = this;
        mActivity = this;

        initialise();

        FillView();

        txtSearch.addTextChangedListener(new TextWatcher() {


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cancel_search, 0, R.drawable.ic_search_set_nav, 0);
                adDeliveryOrder.getFilter().filter(s, new FilterListener() {

                    @Override
                    public void onFilterComplete(int count) {

                        tvPageTitle.setText(getString(R.string.str_nav_delivery_list) + "(" + count + ")");
                    }
                });
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

        txtSearch.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case LEFT:
                        txtSearch.setText("");
                        txtSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search_set_nav,0 );
                        break;
                    default:
                        break;
                }
            }
        });

        lstDeliveryOrder.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {

                Order item = (Order) parent.getItemAtPosition(position);
                Intent intent = new Intent(mContext, DeliveryOrderDetailActivity.class);
                intent.putExtra(ID, item.getId());
                startActivityForResult(intent, REQUEST_CODE_DELIVERY_ORDER_DETAIL);
            }
        });
    }//End of OnCreate

    /**
     * Initializing Variables
     */
    private void initialise() {

        txtSearch = (FontEditText) findViewById(R.id.txtSearch);
        lstDeliveryOrder = (ListView) findViewById(R.id.lstDeliveryOrders);
        db = new DbAdapter(mContext);

        tvPageTitle.setText(getString(R.string.str_nav_delivery_list) + "(" + lstDeliveryOrder.getCount() + ")");
    }

    /**
     * Read from database And Fill Adapter
     */
    private void FillView() {

        db.open();
        //Read From database DelivaryOrder And Fill Adapter______________________________________
        arrayDeliveryOrder = db.getAllDeliveryOrderNotFinal();
        for (Order deliveryorder : arrayDeliveryOrder) {
            if (deliveryorder.getPersonId() == ProjectInfo.CUSTOMERID_GUEST) {
                deliveryorder.setCustomerName(getResources().getString(R.string.str_guest_customer));
                deliveryorder.setMarketName(getResources().getString(R.string.str_guest_market_customer));
            } else if ((deliveryorder.getPersonId() != ProjectInfo.CUSTOMERID_GUEST)) {
                Customer customer = db.getCustomerWithPersonId(deliveryorder.getPersonId());

                deliveryorder.setAddress(customer.getAddress());
                deliveryorder.setMarketName(customer.getOrganization());
                deliveryorder.setCustomerName(customer.getName());

            }
        }//end of For
        db.close();
        adDeliveryOrder = new AdapterListDeliveryOrder(mActivity, arrayDeliveryOrder);
        lstDeliveryOrder.setAdapter(adDeliveryOrder);

        tvPageTitle.setText(getString(R.string.str_nav_delivery_list) + "(" + lstDeliveryOrder.getCount() + ")");
    }

    public class AdapterListDeliveryOrder extends ArrayAdapter<Order> {
        Activity mContext;
        ArrayList<Order> arrayorginal;
        ArrayList<Order> arraydeliveryorder;
        CustomFilterList Filter;

        public AdapterListDeliveryOrder(Activity context, ArrayList<Order> array) {
            super(context, android.R.layout.simple_list_item_1, array);
            mContext = context;
            arrayorginal = new ArrayList<Order>();
            arraydeliveryorder = new ArrayList<Order>();
            arraydeliveryorder.addAll(array);
            arrayorginal.addAll(array);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowview = convertView;
            Holder holder = null;
            LayoutInflater inflater = null;
            final Order deliveryorder = getItem(position);

            if (rowview == null) {
                inflater = mContext.getLayoutInflater();
                rowview = inflater.inflate(R.layout.lst_deliveryorder_item, null, false);
                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();

            holder.Populate(deliveryorder);

            return rowview;
        }

        public class Holder {
            public TextView tvAmount, tvOrderCode, tvMarketName, tvDate, tvAddress, tvCustomerName;
            public ImageButton btnMenu;
            public ImageView imgSync;

            public Holder(View view) {
                tvAmount = (TextView) view.findViewById(R.id.txtAmount);
                tvOrderCode = (TextView) view.findViewById(R.id.tvOrderCode);
                tvMarketName = (TextView) view.findViewById(R.id.tvMarketName);
                tvDate = (TextView) view.findViewById(R.id.tvDate);
                tvAddress = (TextView) view.findViewById(R.id.tvAddress);
                tvCustomerName = (TextView) view.findViewById(R.id.tvCustomerName);
                imgSync = (ImageView) view.findViewById(R.id.imgSync);
                btnMenu = (ImageButton) view.findViewById(R.id.btnMenu);
            }

            public void Populate(Order deliveryorder) {

                double Price = 0, Discount = 0, FinalPrice;
                tvOrderCode.setText(deliveryorder.getCode());
                tvDate.setText(ServiceTools.getDateAndTimeForLong(deliveryorder.getDeliveryDate()));

                tvMarketName.setText(deliveryorder.getMarketName());
                tvAddress.setText(deliveryorder.getAddress());
                tvCustomerName.setText(deliveryorder.getCustomerName());
                //calculate FinalPrice________________________________________________________
                db.open();
                ArrayList<OrderDetail> arrayProduct = db.getAllDeliveryOrder(deliveryorder.getOrderId());
                for (OrderDetail item : arrayProduct) {
                    Price += ServiceTools.getCalculateDeliveryFinalPrice(item);
                }
                db.close();
                Discount = deliveryorder.getDiscount();
                FinalPrice = Price - Discount;
                tvAmount.setText(ServiceTools.formatPrice(FinalPrice));
                //Check Publish________________________________________________________________
                if (deliveryorder.getPublish() == ProjectInfo.DONT_PUBLISH)
                    imgSync.setImageResource(R.drawable.ic_synchronize_red);
                else
                    imgSync.setImageResource(R.drawable.ic_synchronize_green);

            }
        }

        public class CustomFilterList extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();
                Boolean CheckFilter = false;
                String CustomerName = "", MarketName = "", InvoiceNumber = "";

                if (constraint != null && constraint.toString().length() > 0) {
                    ArrayList<Order> filterItem = new ArrayList<Order>();
                    for (int i = 0; i < arrayorginal.size(); i++) {
                        Order deliveryorder = arrayorginal.get(i);
                        InvoiceNumber = deliveryorder.getCode();

                        boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), InvoiceNumber);

                        //if(InvoiceNumber.toLowerCase().contains(constraint))
                        if (result_contain) {
                            filterItem.add(deliveryorder);
                            CheckFilter = true;
                        }
                    }//End of for
                    if (CheckFilter == false) {
                        for (int i = 0; i < arrayorginal.size(); i++) {
                            Order order = arrayorginal.get(i);
                            CustomerName = order.getCustomerName();
                            boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), CustomerName);
                            if (result_contain)
                                //if(CustomerName.toLowerCase().contains(constraint))
                                if (result_contain) {
                                    filterItem.add(order);
                                    CheckFilter = true;
                                }
                        }//End of for
                    }
                    if (CheckFilter == false) {
                        for (int i = 0; i < arrayorginal.size(); i++) {
                            Order order = arrayorginal.get(i);
                            MarketName = order.getMarketName();
                            boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), MarketName);

                            //if(MarketName.toLowerCase().contains(constraint))
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

                arraydeliveryorder = (ArrayList<Order>) results.values;
                notifyDataSetChanged();
                clear();
                for (int i = 0; i < arraydeliveryorder.size(); i++) {
                    add(arraydeliveryorder.get(i));
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

    }// End of AdapterListOreder

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_DELIVERY_ORDER_DETAIL) {
                FillView();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
