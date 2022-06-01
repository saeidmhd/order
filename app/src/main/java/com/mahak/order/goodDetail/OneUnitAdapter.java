package com.mahak.order.goodDetail;


import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mahak.order.BaseActivity;
import com.mahak.order.R;
import com.mahak.order.common.CountInputFilterMinMax;
import com.mahak.order.common.OrderDetailProperty;
import com.mahak.order.common.ProductDetail;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.Properties;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.VisitorProduct;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.mahak.order.BaseActivity.MODE_EDIT;
import static com.mahak.order.BaseActivity.MODE_NEW;
import static com.mahak.order.BaseActivity.mContext;
import static com.mahak.order.goodDetail.GoodDetailOneUnit.txtsumCount1;


public class OneUnitAdapter extends RecyclerView.Adapter<OneUnitAdapter.ViewHolder> implements Filterable {

    private ArrayList<ProductDetail> productDetails;
    private ArrayList<VisitorProduct> visitorProducts;
    ArrayList<ProductDetail> filterItem;
    private LayoutInflater mInflater;
    private ArrayList<ProductDetail> productDetailOriginal = new ArrayList<>();
    private ArrayList<OrderDetailProperty> orderDetailProperties;
    private CustomFilterList Filter;
    private int mode;
    private int type;
    private double sumAmount1;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //common
        TextView tvNumber;
        TextView tvProductSpec;
        LinearLayout btnDelete;
        TextView tv_asset1;
        EditText txtCount;


        ViewHolder(View itemView) {
            super(itemView);

            tvNumber = (TextView) itemView.findViewById(R.id.tvNumber);
            tvProductSpec = (TextView) itemView.findViewById(R.id.tvProductNameSpec);
            btnDelete = (LinearLayout) itemView.findViewById(R.id.btnDelete);
            tv_asset1 = (TextView) itemView.findViewById(R.id.tv_asset1);
            txtCount = (EditText) itemView.findViewById(R.id.txtCount);

        }
    }

    OneUnitAdapter(Context context, ArrayList<ProductDetail> productDetails, ArrayList<VisitorProduct> visitorProducts, ArrayList<OrderDetailProperty> orderDetailProperties, int mode, int type) {

        this.productDetails = productDetails;
        this.visitorProducts = visitorProducts;
        this.orderDetailProperties = orderDetailProperties;
        this.mInflater = LayoutInflater.from(context);
        this.productDetailOriginal.addAll(productDetails);
        this.mode = mode;
        this.type = type;
    }

    public class CustomFilterList extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            String title = "";
            int productDetailId;
            Boolean CheckFilter = false;

            if (constraint.toString().length() > 0) {
                filterItem = new ArrayList<>();
                for (int i = 0; i < productDetailOriginal.size(); i++) {
                    title = productDetailOriginal.get(i).getProperties();
                    boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), title);
                    if (result_contain) {
                        filterItem.add(productDetailOriginal.get(i));
                        CheckFilter = true;
                    }
                }//End of for
                if (!CheckFilter) {
                    for (int i = 0; i < productDetailOriginal.size(); i++) {
                        productDetailId = productDetailOriginal.get(i).getProductDetailId();
                        boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), String.valueOf(productDetailId));
                        if (result_contain) {
                            filterItem.add(productDetailOriginal.get(i));
                        }
                    }//End of for
                }
                result.values = filterItem;
                result.count = filterItem.size();

            } else {
                synchronized (this) {
                    result.values = productDetailOriginal;
                    result.count = productDetailOriginal.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productDetails = ((ArrayList<ProductDetail>) results.values);
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        if (Filter == null)
            Filter = new CustomFilterList();
        return Filter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.lst_detail_item1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        sumAmount1 = 0;
        holder.tvNumber.setText(String.valueOf(position + 1));
        holder.tvProductSpec.setText(getTitle(position));
        if (visitorProducts.size() > 0) {
            for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
                if (orderDetailProperty.getProductDetailId() == visitorProducts.get(holder.getAdapterPosition()).getProductDetailId())
                    setUnit1(orderDetailProperty, holder, visitorProducts.get(holder.getAdapterPosition()));
            }
        }

        holder.txtCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    try {
                        if (productDetails.size() > 0) {
                            sumAmount1 = 0;
                            for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
                                if (orderDetailProperty.getProductDetailId() == productDetails.get(holder.getAdapterPosition()).getProductDetailId()) {
                                    orderDetailProperty.setCount1(ServiceTools.toDouble(s.toString()));
                                    orderDetailProperty.setSumCountBaJoz(ServiceTools.toDouble(s.toString()));
                                }

                                sumAmount1 += orderDetailProperty.getCount1();
                            }
                            txtsumCount1.setText(ServiceTools.formatCount(sumAmount1));
                        }

                    } catch (NumberFormatException e) {
                        FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                        FirebaseCrashlytics.getInstance().recordException(e);
                        e.printStackTrace();
                    }
                }
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.txtCount.setText(ServiceTools.formatCount(0));
                holder.txtCount.selectAll();
            }
        });
    }

    private String getTitle(int position) {
        String result = "";
        StringBuilder title = new StringBuilder();
        Gson gson = new Gson();
        Type property = new TypeToken<ArrayList<Properties>>() {
        }.getType();
        List<Properties> propertiesList = new ArrayList<>();
        try {
            if (productDetails.get(position).getProperties() != null)
                propertiesList = gson.fromJson(productDetails.get(position).getProperties(), property);
        } catch (JsonSyntaxException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
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

    private void setUnit1(OrderDetailProperty orderDetailProperty, ViewHolder holder, VisitorProduct visitorProduct) {


        if ((int) orderDetailProperty.getCount1() == orderDetailProperty.getCount1())
            holder.txtCount.setText(ServiceTools.formatCount((int) orderDetailProperty.getCount1()));
        else
            holder.txtCount.setText(ServiceTools.formatCount(orderDetailProperty.getCount1()));

        if (mode == MODE_NEW) {
            holder.tv_asset1.setText(ServiceTools.formatCount(visitorProduct.getCount1()));
        } else if (mode == MODE_EDIT) {
            holder.tv_asset1.setText(ServiceTools.formatCount(ServiceTools.getExistCount1Prop(orderDetailProperty, visitorProduct)));
        }

        if (type == ProjectInfo.TYPE_INVOCIE) {
            if (mode == MODE_NEW)
                holder.txtCount.setFilters(new InputFilter[]{new CountInputFilterMinMax(0, visitorProduct.getCount1())});
            else if (mode == MODE_EDIT)
                holder.txtCount.setFilters(new InputFilter[]{new CountInputFilterMinMax(0, ServiceTools.getExistCount1Prop(orderDetailProperty, visitorProduct))});

        }

    }

    @Override
    public int getItemCount() {
        return productDetails.size();
    }
}
