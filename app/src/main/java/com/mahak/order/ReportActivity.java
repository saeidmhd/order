package com.mahak.order;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class ReportActivity extends BaseActivity {

    private Context mContext;
    private PieChart mPieChartOrder, mPieChartInvoice, mPieChartReceipt;
    private TextView tvTotalPriceOrder, tvTotalPriceInvoice, tvReceiptTotal;
    private TextView tvPureWithoutChargeAndTaxOrder, tvPureWithChargeAndTaxOrder, tvDiscountOrder, tvPureWithoutChargeAndTaxInvoice, tvPureWithChargeAndTaxInvoice, tvDiscountInvoice, tvSumOfCash, tvSumOfCheque, tvSumOfReceiptsAmount, tvChargeAndTaxOrder, tvChargeAndTaxInvoice;
    private Button btnReportOrder, btnReportInvoice, btnReportReceipt;
    private DbAdapter mDb;
    private ArrayList<Float> arrayPriceOrder, arrayPriceInvoice, arrayPriceReceipt;
    private double PureWithOutChargeTaxOrder, TotalChargeTaxOrder, PureWithChargeTaxOrder;
    private float TotalPriceInvoice, TotalPriceOrder, TotalDiscountOrder, TotalDiscountInvoice;
    private double PureWithOutChargeTaxInvoice, TotalChargeTaxInvoice, PureWithChargeTaxInvoice;
    private double TotalCashAmountReceipt, TotalChequeReceipt, TotalCashReceipt, TotalPriceReceipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_report);

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
        TextView tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        tvPageTitle.setText(getString(R.string.str_nav_report));
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________
        init();

        mDb.open();

        //Calculate order - invoice - receipt
        CalculateTotal();

        //Order
        setTextViewOrder();

        setChartOrder();

        //Invoice
        setTextViewInvoice();

        setChartInvoice();

        //Receipt
        setTextViewReceipt();

        setChartReceipt();

        btnReportOrder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent;
                if (Locale.getDefault().getLanguage().equals("de")) {
                    intent = new Intent(mContext, ReportMonthlyGregorian.class);
                } else
                    intent = new Intent(mContext, ReportMonthlyActivity.class);

                intent.putExtra(MODE_PAGE, ProjectInfo.REPORT_TYPE_ORDER);
                startActivity(intent);
            }
        });

        btnReportInvoice.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent;
                if (Locale.getDefault().getLanguage().equals("de")) {
                    intent = new Intent(mContext, ReportMonthlyGregorian.class);
                } else
                    intent = new Intent(mContext, ReportMonthlyActivity.class);
                intent.putExtra(MODE_PAGE, ProjectInfo.REPORT_TYPE_INVOICE);
                startActivity(intent);
            }
        });

        btnReportReceipt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent;
                if (Locale.getDefault().getLanguage().equals("de")) {
                    intent = new Intent(mContext, ReportMonthlyGregorian.class);
                } else
                    intent = new Intent(mContext, ReportMonthlyActivity.class);
                intent.putExtra(MODE_PAGE, ProjectInfo.REPORT_TYPE_RECEIPT);
                startActivity(intent);
            }
        });

        mDb.close();
    }

    /**
     * Calculate Total Price
     */
    private void CalculateTotal() {

        TotalPriceOrder = (float) mDb.getTotalPriceOrder();
        TotalDiscountOrder = (float) mDb.getTotalDiscountOrder();
        PureWithOutChargeTaxOrder = TotalPriceOrder - TotalDiscountOrder;
        TotalChargeTaxOrder = mDb.getTotalChargeAndTaxOrder();
        PureWithChargeTaxOrder = PureWithOutChargeTaxOrder + TotalChargeTaxOrder;

        TotalPriceInvoice = (float) mDb.getTotalPriceInvoice();
        TotalDiscountInvoice = (float) mDb.getTotalDiscountInvoice();
        PureWithOutChargeTaxInvoice = TotalPriceInvoice - TotalDiscountInvoice;
        TotalChargeTaxInvoice = mDb.getTotalChargeAndTaxInvoice();
        PureWithChargeTaxInvoice = PureWithOutChargeTaxInvoice + TotalChargeTaxInvoice;

        TotalCashAmountReceipt = mDb.getTotalCashAmountReceipt();
        TotalChequeReceipt = mDb.getTotalChequeReceipt();
        TotalCashReceipt = mDb.getTotalCashReceipt();
        TotalPriceReceipt = mDb.getTotalPriceReceipt();
    }

    /**
     * Fill view Order
     */
    private void setTextViewOrder() {

        tvTotalPriceOrder.setText(ServiceTools.formatPrice(TotalPriceOrder));
        tvTotalPriceOrder.setSelected(true);
        tvDiscountOrder.setText(ServiceTools.formatPrice(TotalDiscountOrder));
        tvDiscountOrder.setSelected(true);
        tvPureWithoutChargeAndTaxOrder.setText(ServiceTools.formatPrice(PureWithOutChargeTaxOrder));
        tvPureWithoutChargeAndTaxOrder.setSelected(true);
        tvChargeAndTaxOrder.setText(ServiceTools.formatPrice(TotalChargeTaxOrder));
        tvChargeAndTaxOrder.setSelected(true);
        tvPureWithChargeAndTaxOrder.setText(ServiceTools.formatPrice(PureWithChargeTaxOrder));
        tvPureWithoutChargeAndTaxOrder.setSelected(true);

        arrayPriceOrder.add(TotalPriceOrder);
        arrayPriceOrder.add(TotalDiscountOrder);
    }

    /**
     * Fill view Invoice
     */
    private void setTextViewInvoice() {

        tvTotalPriceInvoice.setText(ServiceTools.formatPrice(TotalPriceInvoice));
        tvTotalPriceInvoice.setSelected(true);
        tvDiscountInvoice.setText(ServiceTools.formatPrice(TotalDiscountInvoice));
        tvDiscountInvoice.setSelected(true);
        tvPureWithoutChargeAndTaxInvoice.setText(ServiceTools.formatPrice(PureWithOutChargeTaxInvoice));
        tvPureWithoutChargeAndTaxInvoice.setSelected(true);
        tvChargeAndTaxInvoice.setText(ServiceTools.formatPrice(TotalChargeTaxInvoice));
        tvChargeAndTaxInvoice.setSelected(true);
        tvPureWithChargeAndTaxInvoice.setText(ServiceTools.formatPrice(PureWithChargeTaxInvoice));
        tvPureWithChargeAndTaxInvoice.setSelected(true);

        arrayPriceInvoice.add(TotalPriceInvoice);
        arrayPriceInvoice.add(TotalDiscountInvoice);

    }

    /**
     * Fill view Receipt
     */
    private void setTextViewReceipt() {

        tvSumOfCash.setText(ServiceTools.formatPrice(TotalCashAmountReceipt));
        tvSumOfCash.setSelected(true);
        tvSumOfCheque.setText(ServiceTools.formatPrice(TotalChequeReceipt));
        tvSumOfCheque.setSelected(true);
        tvSumOfReceiptsAmount.setText(ServiceTools.formatPrice(TotalCashReceipt));
        tvSumOfReceiptsAmount.setSelected(true);
        tvReceiptTotal.setText(ServiceTools.formatPrice(TotalPriceReceipt));

        arrayPriceReceipt.add((float) mDb.getTotalCashAmountReceipt());
        arrayPriceReceipt.add((float) mDb.getTotalChequeReceipt());
        arrayPriceReceipt.add((float) mDb.getTotalCashReceipt());
    }

    /**
     * Set Chart order
     */
    private void setChartOrder() {

        if (TotalPriceOrder > 0)
            mPieChartOrder.setVisibility(View.VISIBLE);
        else
            mPieChartOrder.setVisibility(View.INVISIBLE);

        StringBuilder cTitle = new StringBuilder();
        cTitle.append(getString(R.string.str_pure_without));
        cTitle.append("\n");
        cTitle.append(getString(R.string.str_charge_tax) + " :");
        cTitle.append("\n");
        cTitle.append(ServiceTools.formatPrice(PureWithOutChargeTaxOrder));

        String sSumOrder[] = getResources().getStringArray(R.array.array_sum_invoice_order);
        //YVal
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < arrayPriceOrder.size(); i++) {
            yVals.add(new Entry(arrayPriceOrder.get(i), i));
        }

        //XVal
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < arrayPriceOrder.size(); i++)
            xVals.add(sSumOrder[i]);

        PieDataSet set1 = new PieDataSet(yVals, "");
        set1.setSliceSpace(3f);
        set1.setColors(ColorTemplate.createColors(ColorTemplate.PASTEL_COLORS));

        PieData data = new PieData(xVals, set1);

        initPieChart(mPieChartOrder, data, cTitle.toString());

    }

    /**
     * Set Chart Invoice
     */
    private void setChartInvoice() {

        if (TotalPriceInvoice > 0)
            mPieChartInvoice.setVisibility(View.VISIBLE);
        else
            mPieChartInvoice.setVisibility(View.INVISIBLE);

        StringBuilder cTitle = new StringBuilder();
        cTitle.append(getString(R.string.str_pure_without));
        cTitle.append("\n");
        cTitle.append(getString(R.string.str_charge_tax) + " :");
        cTitle.append("\n");
        cTitle.append(ServiceTools.formatPrice(PureWithOutChargeTaxInvoice));

        String sSumInvoice[] = getResources().getStringArray(R.array.array_sum_invoice_order);
        //YVal
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < arrayPriceInvoice.size(); i++) {
            yVals.add(new Entry(arrayPriceInvoice.get(i), i));
        }

        //XVal
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < arrayPriceInvoice.size(); i++)
            xVals.add(sSumInvoice[i]);

        PieDataSet set1 = new PieDataSet(yVals, "");
        set1.setSliceSpace(3f);
        set1.setColors(ColorTemplate.createColors(ColorTemplate.PASTEL_COLORS));

        //set1.se
        PieData data = new PieData(xVals, set1);

        initPieChart(mPieChartInvoice, data, cTitle.toString());
    }

    /**
     * set Chart Receipt
     */
    private void setChartReceipt() {

        if (TotalPriceReceipt > 0)
            mPieChartReceipt.setVisibility(View.VISIBLE);
        else
            mPieChartReceipt.setVisibility(View.INVISIBLE);

        StringBuilder cTitle = new StringBuilder();
        cTitle.append(getString(R.string.str_sum_of_receipts) + " :");
        cTitle.append("\n");
        cTitle.append(ServiceTools.formatPrice(mDb.getTotalPriceReceipt()));

        String sSumReceipt[] = getResources().getStringArray(R.array.array_sum_receipt);

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < arrayPriceReceipt.size(); i++) {
            yVals.add(new Entry(arrayPriceReceipt.get(i), i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < arrayPriceReceipt.size(); i++)
            xVals.add(sSumReceipt[i]);

        PieDataSet set1 = new PieDataSet(yVals, "");
        set1.setSliceSpace(3f);
        set1.setColors(ColorTemplate.createColors(ColorTemplate.COLORFUL_COLORS));
        PieData data = new PieData(xVals, set1);

        initPieChart(mPieChartReceipt, data, cTitle.toString());
    }

    private void initPieChart(PieChart pie, PieData data, String title) {
        pie.setUsePercentValues(true);
        pie.setDescription("");
        pie.setExtraOffsets(5, 10, 5, 5);
        pie.setDragDecelerationFrictionCoef(0.95f);
        pie.setDrawHoleEnabled(true);
        pie.setHoleColor(Color.WHITE);
        pie.setTransparentCircleColor(Color.WHITE);
        pie.setTransparentCircleAlpha(110);
        pie.setHoleRadius(58f);
        pie.setTransparentCircleRadius(61f);
        pie.setDrawCenterText(true);
        pie.setRotationAngle(0);
        pie.setRotationEnabled(true);
        pie.setHighlightPerTapEnabled(true);
        pie.animateXY(1400, 1400, Easing.EasingOption.EaseInOutQuad, Easing.EasingOption.EaseInOutQuad);
        pie.setCenterText(title);
        pie.setData(data);
    }

    /**
     * Initializing Variables
     */
    private void init() {

        mPieChartOrder = (PieChart) findViewById(R.id.PieChartOrder);
        mPieChartInvoice = (PieChart) findViewById(R.id.PieChartInvoice);
        mPieChartReceipt = (PieChart) findViewById(R.id.PieChartReceipt);
        //Order
        tvTotalPriceOrder = (TextView) findViewById(R.id.tvTotalPriceOrder);
        tvDiscountOrder = (TextView) findViewById(R.id.tvDiscountOrder);
        tvPureWithoutChargeAndTaxOrder = (TextView) findViewById(R.id.tvPureWithoutChargeAndTaxOrder);
        tvChargeAndTaxOrder = (TextView) findViewById(R.id.tvChargeAndTaxOrder);
        tvPureWithChargeAndTaxOrder = (TextView) findViewById(R.id.tvPureWithChargeAndTaxOrder);
        btnReportOrder = (Button) findViewById(R.id.btnReportOrder);
        //Invoice
        tvTotalPriceInvoice = (TextView) findViewById(R.id.tvTotalPriceInvoice);
        tvDiscountInvoice = (TextView) findViewById(R.id.tvDiscountInvoice);
        tvPureWithoutChargeAndTaxInvoice = (TextView) findViewById(R.id.tvPureWithoutChargeTaxInvoice);
        tvChargeAndTaxInvoice = (TextView) findViewById(R.id.tvChargeAndTaxInvoice);
        tvPureWithChargeAndTaxInvoice = (TextView) findViewById(R.id.tvPureWithChargeTaxInvoice);
        btnReportInvoice = (Button) findViewById(R.id.btnReportInvoice);
        //Receipt
        tvReceiptTotal = (TextView) findViewById(R.id.tvReceiptTotal);
        tvSumOfCash = (TextView) findViewById(R.id.tvSumOfCash);
        tvSumOfCheque = (TextView) findViewById(R.id.tvSumOfCheque);
        tvSumOfReceiptsAmount = (TextView) findViewById(R.id.tvSumOfReceiptsAmount);
        btnReportReceipt = (Button) findViewById(R.id.btnReportReceipt);

        mDb = new DbAdapter(mContext);

        arrayPriceOrder = new ArrayList<Float>();
        arrayPriceInvoice = new ArrayList<Float>();
        arrayPriceReceipt = new ArrayList<Float>();
    }

    //Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Back
    @Override
    public void onBackPressed() {
        finish();
    }

}
