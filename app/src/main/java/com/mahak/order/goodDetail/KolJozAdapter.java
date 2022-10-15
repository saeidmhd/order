package com.mahak.order.goodDetail;


import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.mahak.order.common.OrderDetailProperty;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductDetail;
import com.mahak.order.common.Properties;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.VisitorProduct;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.mahak.order.BaseActivity.MODE_EDIT;
import static com.mahak.order.BaseActivity.MODE_NEW;
import static com.mahak.order.BaseActivity.mContext;
import static com.mahak.order.goodDetail.GoodDetailKolJoz.txtsumCount1;
import static com.mahak.order.goodDetail.GoodDetailKolJoz.txtsumCount2;
import static com.mahak.order.goodDetail.GoodDetailKolJoz.txtsumCount3;


public class KolJozAdapter extends RecyclerView.Adapter<KolJozAdapter.ViewHolder> implements Filterable {

    private ArrayList<ProductDetail> productDetails;
    private ArrayList<VisitorProduct> visitorProducts;
    ArrayList<ProductDetail> filterItem;
    private LayoutInflater mInflater;
    private ArrayList<ProductDetail> productDetailOriginal = new ArrayList<>();
    private ArrayList<OrderDetailProperty> orderDetailProperties = new ArrayList<>();
    private CustomFilterList Filter;
    private Context context;
    private int mode;
    private int type;
    private double sumAmount1;
    private double sumAmount2;
    private double sumAmount3;
    private Product product;

    private boolean editKol = false;
    private boolean editJoz = false;
    private boolean editSum = false;

    boolean txtCount2Edit;
    boolean txtCountEdit;

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

        }
    }

    KolJozAdapter(Context context, ArrayList<ProductDetail> productDetails, ArrayList<VisitorProduct> visitorProducts, ArrayList<OrderDetailProperty> orderDetailProperties, int mode, Product product, int type) {

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
        View view = mInflater.inflate(R.layout.lst_detail_item3, parent, false);
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
                    setUnitJozKol(orderDetailProperty, holder, visitorProducts.get(holder.getAdapterPosition()));
            }
        }
        setSumAmount(holder, orderDetailProperties.get(holder.getAdapterPosition()));

        if (product.getUnitRatio() > 0) {
            holder.txtCountKol.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (TextUtils.isEmpty(holder.txtCountKol.getText().toString())) {
                        holder.txtCountKol.setText(ServiceTools.formatCount(0));
                    }
                    if (product.getUnitRatio() > 0) {
                        /*if(type == ProjectInfo.TYPE_INVOCIE){
                            double remainPackage =  ((maxValueRetail - ServiceTools.toDouble(holder.txtCountKol.getText().toString())) / product.getUnitRatio());
                            holder.txtCountJoz.setFilters(new InputFilter[]{new CountInputFilterMinMax(0,  remainPackage)});
                        }*/
                    }

                }
            });
            holder.txtCountKol.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                        // DoneProductCount(finalProductPriceLevel);
                    }
                    return false;
                }
            });
            holder.txtCountKol.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    editJoz = false;
                    editSum = false;
                    editKol = true;
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    setSumAmount(holder, orderDetailProperties.get(holder.getAdapterPosition()));
                }
            });
        }


        holder.txtCountJoz.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CalculateJozKolSum(holder);
                holder.txtCountJoz.selectAll();
            }

        });
        holder.txtCountJoz.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editJoz = true;
                editSum = false;
                editKol = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editJoz) {
                    if (holder.txtCountJoz.hasFocus()) {
                        if (!TextUtils.isEmpty(holder.txtCountJoz.getText().toString())) {
                            try {
                                double kol = ServiceTools.toDouble(holder.txtCountKol.getText().toString()) * product.getUnitRatio();
                                double joz = ServiceTools.toDouble(holder.txtCountJoz.getText().toString());
                                holder.txtCount.setText(ServiceTools.formatCount((int) (kol + joz)));

                            } catch (NumberFormatException e) {
                                ServiceTools.logToFireBase(e);
                                holder.txtCountJoz.setError(context.getString(R.string.illegal));
                            }
                        }
                    }
                }

                setSumAmount(holder, orderDetailProperties.get(holder.getAdapterPosition()));


            }
        });
       /* holder.txtCount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    holder.txtCount.selectAll();
            }
        });
        holder.txtCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editJoz = false;
                editSum = false;
                editKol = true;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (editKol) {
                    if (holder.txtCount.hasFocus()) {
                        if (!TextUtils.isEmpty(holder.txtCount.getText().toString())) {
                            double sumKolJoz = 0.0;
                            try {
                                sumKolJoz = ServiceTools.toDouble(s.toString());
                            } catch (NumberFormatException e) {
                                FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                                FirebaseCrashlytics.getInstance().recordException(e);
                                e.printStackTrace();
                            }
                            int intValue = (int) (sumKolJoz / product.getUnitRatio());
                            double doubleValue = (sumKolJoz % product.getUnitRatio());
                            holder.txtCountKol.setText(ServiceTools.formatCount(intValue));
                            holder.txtCountJoz.setText(formatCount(doubleValue));
                        } else {
                            holder.txtCountKol.setText(ServiceTools.formatCount(0));
                            holder.txtCountJoz.setText(ServiceTools.formatCount(0.0));
                        }
                    }
                }
                setSumAmount(holder, orderDetailProperties.get(holder.getAdapterPosition()));
            }
        });*/

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setZero(holder);
            }
        });
    }

    private void setZero(ViewHolder holder) {
        holder.txtCount.setText(ServiceTools.formatCount(0));
        holder.txtCountJoz.setText(ServiceTools.formatCount(0));
        holder.txtCountKol.setText(ServiceTools.formatCount(0));
        holder.txtCountKol.selectAll();
    }

    private void setSumAmount(ViewHolder holder, OrderDetailProperty orderDetailProperty) {
        try {

            orderDetailProperty.setSumCountBaJoz(ServiceTools.toDouble(holder.txtCount.getText().toString()));
            orderDetailProperty.setCount2(ServiceTools.toDouble(holder.txtCountKol.getText().toString()));
            orderDetailProperty.setCount1(ServiceTools.toDouble(holder.txtCountJoz.getText().toString()));

            setSum();

        } catch (NumberFormatException e) {
            ServiceTools.logToFireBase(e);
            e.printStackTrace();
        }
    }

    private void setSum() {

        sumAmount1 = 0;
        sumAmount2 = 0;
        sumAmount3 = 0;

        for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
            sumAmount1 += orderDetailProperty.getCount1();
            sumAmount2 += orderDetailProperty.getCount2();
            sumAmount3 += orderDetailProperty.getSumCountBaJoz();
        }

        txtsumCount1.setText(ServiceTools.formatCount(sumAmount1));
        txtsumCount2.setText(ServiceTools.formatCount(sumAmount2));
        txtsumCount3.setText(ServiceTools.formatCount(sumAmount3));
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

    private void setUnitJozKol(OrderDetailProperty orderDetailProperty, ViewHolder holder, VisitorProduct visitorProduct) {

        if (orderDetailProperties.size() > 0) {
            holder.txtCount.setText(ServiceTools.formatCount(orderDetailProperty.getSumCountBaJoz()));
            holder.txtCountKol.setText(ServiceTools.formatCount(orderDetailProperty.getCount2()));
            holder.txtCountJoz.setText(ServiceTools.formatCount(orderDetailProperty.getCount1()));
        }

        if (mode == MODE_NEW) {
            holder.tv_asset1.setText(ServiceTools.formatCount(visitorProduct.getCount1()));
            holder.tv_asset2.setText(ServiceTools.formatCount(visitorProduct.getCount2()));
            maxValueRetail = visitorProduct.getCount1();
            maxValueRetail2 = visitorProduct.getCount2();
        } else if (mode == MODE_EDIT) {
            maxValueRetail = ServiceTools.getExistCount1Prop(orderDetailProperty, visitorProduct);
            maxValueRetail2 = ServiceTools.getExistCount2Prop(orderDetailProperty, visitorProduct);
            holder.tv_asset1.setText(ServiceTools.formatCount(maxValueRetail));
            holder.tv_asset2.setText(ServiceTools.formatCount(maxValueRetail2));
        }
        // CalculateJozKolSum(holder.txtCountKol, holder.txtCountJoz, holder.txtCount);
        /*if (type == ProjectInfo.TYPE_INVOCIE) {
            holder.txtCountJoz.setFilters(new InputFilter[]{new CountInputFilterMinMax(0, maxValueRetail)});
            holder.txtCountKol.setFilters(new InputFilter[]{new CountInputFilterMinMax(0, maxValueRetail2)});
            holder.txtCount.setFilters(new InputFilter[]{new CountInputFilterMinMax(0, maxValueRetail)});
        }*/

    }

    private void CalculateJozKolSum(ViewHolder holder) {

        String txtKol = holder.txtCountKol.getText().toString();
        String txtJoz = holder.txtCountJoz.getText().toString();
        String txtSum = holder.txtCount.getText().toString();

        BigDecimal kol = null;
        double joz = 0;
        double sum = 0;

        if (!TextUtils.isEmpty(txtKol)) {
            try {
                kol = new BigDecimal(txtKol);
            } catch (NumberFormatException e) {
                ServiceTools.logToFireBase(e);
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(txtJoz)) {
            try {
                joz = ServiceTools.toDouble(txtJoz);
            } catch (NumberFormatException e) {
                ServiceTools.logToFireBase(e);
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(txtSum)) {
            try {
                sum = ServiceTools.toDouble(txtSum);
            } catch (NumberFormatException e) {
                ServiceTools.logToFireBase(e);
                e.printStackTrace();
            }
        }

        int intPart = 0;
        double decimalPart = 0;

        if (kol != null) {
            intPart = kol.intValue();
            decimalPart = kol.subtract(new BigDecimal(intPart)).doubleValue();
        }
        if (decimalPart > 0) {
            joz += (decimalPart * (int) product.getUnitRatio());
        }
        if (joz >= product.getUnitRatio()) {
            intPart += (joz / product.getUnitRatio());
            joz -= product.getUnitRatio() * (int) (joz / product.getUnitRatio());

        }
        sum = (intPart * product.getUnitRatio() + joz);

        holder.txtCountJoz.setText(ServiceTools.formatCount(joz));
      //  holder.txtCountKol.setText(ServiceTools.formatCount(intPart));
        holder.txtCount.setText(ServiceTools.formatCount(sum));

        setSumAmount(holder, orderDetailProperties.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return productDetails.size();
    }
}
