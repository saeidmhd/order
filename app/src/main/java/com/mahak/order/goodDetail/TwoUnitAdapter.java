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
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductDetail;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.Properties;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.VisitorProduct;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.mahak.order.BaseActivity.Mode_DoVahedi;
import static com.mahak.order.BaseActivity.mContext;
import static com.mahak.order.common.ServiceTools.formatCount;
import static com.mahak.order.goodDetail.GoodDetailTwoUnit.txtsumCount1;
import static com.mahak.order.goodDetail.GoodDetailTwoUnit.txtsumCount2;


public class TwoUnitAdapter extends RecyclerView.Adapter<TwoUnitAdapter.ViewHolder> implements Filterable {

    private ArrayList<ProductDetail> productDetails;
    private ArrayList<VisitorProduct> visitorProducts;
    ArrayList<ProductDetail> filterItem;
    private LayoutInflater mInflater;
    private ArrayList<ProductDetail> productDetailOriginal = new ArrayList<>();
    private ArrayList<OrderDetailProperty> orderDetailProperties = new ArrayList<>();
    private CustomFilterList Filter;
    private Context context;
    private int mode;
    private double sumAmount1;
    private double sumAmount2;
    private Product product;

    boolean txtCount2Edit;
    boolean txtCountEdit;

    int type;
    private double maxValueRetail;
    private double maxValueRetail2;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //common
        TextView tvNumber;
        TextView tvProductSpec;

        LinearLayout btnDelete;

        //lst_detail_3 , 2
        TextView tv_asset1;
        TextView tv_asset2;

        EditText txtCountKol;
        EditText txtCountJoz;
        EditText txtCount;

        EditText txtCount1;
        EditText txtCount2;
        private double sum;

        ViewHolder(View itemView) {
            super(itemView);

            tvNumber = (TextView) itemView.findViewById(R.id.tvNumber);
            tvProductSpec = (TextView) itemView.findViewById(R.id.tvProductNameSpec);
            btnDelete = (LinearLayout) itemView.findViewById(R.id.btnDelete);

            tv_asset1 = (TextView) itemView.findViewById(R.id.tv_asset1);
            tv_asset2 = (TextView) itemView.findViewById(R.id.tv_asset2);

            txtCountKol = (EditText) itemView.findViewById(R.id.txtCountKol);
            txtCountJoz = (EditText) itemView.findViewById(R.id.txtCountJoz);
            txtCount = (EditText) itemView.findViewById(R.id.txtCount);

            txtCount1 = (EditText) itemView.findViewById(R.id.txtCount1);
            txtCount2 = (EditText) itemView.findViewById(R.id.txtCount2);
        }
    }

    TwoUnitAdapter(Context context, ArrayList<ProductDetail> productDetails, ArrayList<VisitorProduct> visitorProducts, ArrayList<OrderDetailProperty> orderDetailProperties, int mode, Product product, int type) {

        this.productDetails = productDetails;
        this.visitorProducts = visitorProducts;
        this.orderDetailProperties = orderDetailProperties;
        this.mInflater = LayoutInflater.from(context);
        this.productDetailOriginal.addAll(productDetails);
        this.context = context;
        this.mode = mode;
        this.product = product;
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
        View view = mInflater.inflate(R.layout.lst_detail_item2, parent, false);
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
                    setUnit2(orderDetailProperty, holder, visitorProducts.get(holder.getAdapterPosition()));
            }
        }
        holder.txtCount1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                txtCountEdit = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!txtCount2Edit) {
                    txtCountEdit = true;
                    double toDouble;
                    if (!TextUtils.isEmpty(s)) {
                        try {
                            //btnSave.setEnabled(true);
                            toDouble = ServiceTools.toDouble(s.toString());
                            if (BaseActivity.getPrefUnit2Setting(context) == Mode_DoVahedi) {

                                if (product.getUnitRatio() > 0) {
                                    holder.txtCount2.setText(formatCount(toDouble / product.getUnitRatio()));
                                    if (productDetails.size() > 0) {
                                        sumAmount1 = 0;
                                        sumAmount2 = 0;
                                        for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
                                            if (orderDetailProperty.getProductDetailId() == productDetails.get(holder.getAdapterPosition()).getProductDetailId()) {
                                                orderDetailProperty.setCount1(toDouble);
                                                orderDetailProperty.setCount2(toDouble / product.getUnitRatio());
                                                orderDetailProperty.setSumCountBaJoz(toDouble);
                                            }
                                            sumAmount1 += orderDetailProperty.getCount1();
                                            sumAmount2 += orderDetailProperty.getCount2();
                                        }
                                        txtsumCount1.setText(ServiceTools.formatCount(sumAmount1));
                                        txtsumCount2.setText(ServiceTools.formatCount(sumAmount2));
                                    }
                                } else {
                                    if (productDetails.size() > 0) {
                                        holder.txtCount2.setText(ServiceTools.formatCount(0));
                                        sumAmount1 = 0;
                                        sumAmount2 = 0;
                                        for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
                                            if (orderDetailProperty.getProductDetailId() == productDetails.get(holder.getAdapterPosition()).getProductDetailId()) {
                                                orderDetailProperty.setCount1(toDouble);
                                                orderDetailProperty.setSumCountBaJoz(toDouble);
                                            }
                                            sumAmount1 += orderDetailProperty.getCount1();
                                            sumAmount2 += orderDetailProperty.getCount2();
                                        }
                                        txtsumCount1.setText(ServiceTools.formatCount(sumAmount1));
                                        txtsumCount2.setText(ServiceTools.formatCount(sumAmount2));
                                    }
                                }
                            }
                        } catch (NumberFormatException e) {
                            ServiceTools.logToFireBase(e);
                            holder.txtCount1.setError(context.getString(R.string.error_not_valid));
                            //btnSave.setEnabled(false);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                txtCountEdit = false;
            }
        });
        holder.txtCount2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                txtCount2Edit = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtCountEdit) {
                    txtCount2Edit = true;
                    double toDouble;
                    if (!TextUtils.isEmpty(s)) {
                        try {
                            toDouble = ServiceTools.toDouble(s.toString());
                            if (BaseActivity.getPrefUnit2Setting(context) == Mode_DoVahedi) {
                                if (product.getUnitRatio() > 0) {
                                    holder.txtCount1.setText(ServiceTools.formatCount(toDouble * product.getUnitRatio()));
                                    if (productDetails.size() > 0) {
                                        sumAmount1 = 0;
                                        sumAmount2 = 0;
                                        for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
                                            if (orderDetailProperty.getProductDetailId() == productDetails.get(holder.getAdapterPosition()).getProductDetailId()) {
                                                orderDetailProperty.setCount2(toDouble);
                                                orderDetailProperty.setCount1(toDouble * product.getUnitRatio());
                                                orderDetailProperty.setSumCountBaJoz(toDouble * product.getUnitRatio());
                                            }
                                            sumAmount1 += orderDetailProperty.getCount1();
                                            sumAmount2 += orderDetailProperty.getCount2();
                                        }
                                        txtsumCount1.setText(ServiceTools.formatCount(sumAmount1));
                                        txtsumCount2.setText(ServiceTools.formatCount(sumAmount2));
                                    }
                                } else {
                                    if (productDetails.size() > 0) {
                                        sumAmount1 = 0;
                                        sumAmount2 = 0;
                                        for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
                                            if (orderDetailProperty.getProductDetailId() == productDetails.get(holder.getAdapterPosition()).getProductDetailId()) {
                                                orderDetailProperty.setCount2(toDouble);
                                            }
                                            sumAmount1 += orderDetailProperty.getCount1();
                                            sumAmount2 += orderDetailProperty.getCount2();
                                        }
                                        txtsumCount1.setText(ServiceTools.formatCount(sumAmount1));
                                        txtsumCount2.setText(ServiceTools.formatCount(sumAmount2));
                                    }
                                }
                            }
                        } catch (NumberFormatException e) {
                            ServiceTools.logToFireBase(e);
                            holder.txtCount1.setError(context.getString(R.string.error_not_valid));
                            /* btnSave.setEnabled(false);*/
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                txtCount2Edit = false;
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.txtCount1.setText(ServiceTools.formatCount(0));
                holder.txtCount2.setText(ServiceTools.formatCount(0));
                holder.txtCount1.selectAll();
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
            ServiceTools.logToFireBase(e);
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

    private void setUnit2(OrderDetailProperty orderDetailProperty, ViewHolder holder, VisitorProduct visitorProduct) {

        holder.txtCount1.setText(ServiceTools.formatCount(orderDetailProperty.getCount1()));
        holder.txtCount2.setText(ServiceTools.formatCount(orderDetailProperty.getCount2()));

        maxValueRetail = ServiceTools.getExistCount1Prop(orderDetailProperty, visitorProduct);
        maxValueRetail2 = ServiceTools.getExistCount2Prop(orderDetailProperty, visitorProduct);

        holder.tv_asset1.setText(ServiceTools.formatCount(maxValueRetail));
        holder.tv_asset2.setText(ServiceTools.formatCount(maxValueRetail2));

        if (type == ProjectInfo.TYPE_INVOCIE) {
            holder.txtCount1.setFilters(new InputFilter[]{new CountInputFilterMinMax(0, maxValueRetail)});
            holder.txtCount2.setFilters(new InputFilter[]{new CountInputFilterMinMax(0, maxValueRetail2)});
        }
    }

    @Override
    public int getItemCount() {
        return productDetails.size();
    }
}
