package com.mahak.order;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mahak.order.calendar.CivilDate;
import com.mahak.order.calendar.DateConverter;
import com.mahak.order.calendar.PersianDate;
import com.mahak.order.common.MyCalendar;
import com.mahak.order.common.Printer;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.SharedPreferencesHelper;
import com.mahak.order.libs.DatePicker;
import com.mahak.order.storage.DbAdapter;

import java.util.Calendar;
import java.util.Date;

import static com.mahak.order.common.ProjectInfo.Woosim_WSP_R341;
import static com.mahak.order.common.ServiceTools.formatCount;

public class DailyReportActivity extends BaseActivity {

    TextView tvDailySale, tvDailyDiscount, tvDailyNetSale, tvDailyTaxCharge, tvDailyFinalSum, tvDailyCountOfProduct;
    TextView tvDailyPreSale, tvDailyPreDiscount, tvDailyPreNetSale, tvDailyPreTaxCharge, tvDailyPreFinalSum, tvDailyPreCountOfProduct;
    TextView tvDailyCashAmount, tvDailyChequeAmount, tvDailyCashReceiptAmount, tvDailySumOfReceipts;
    TextView tvDailyBank, tvDailyExpense, tvSumOfPayment, tvDailyRemained, tvDailyRemainedVisitor;

    long dtReport;
    private DbAdapter db;
    Context mContext;
    private long sDay, eDay;
    View ll;
    int printerBrand;

    double sumOfReceipt = 0;
    double sumOfPayment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);

        mContext = this;
        printerBrand = SharedPreferencesHelper.getPrefPrinterBrand(mContext);

        init();
        initData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 1, R.string.ReportPrint).setIcon(R.drawable.ic_print_white_48dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, 1, 1, R.string.ReportDate).setIcon(R.drawable.ic_date_range_white_48dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case 0:
                preparePrinterFile preparePrinterFile = new preparePrinterFile();
                preparePrinterFile.execute();
                break;
            case 1:

                MyCalendar myCalendar = new MyCalendar(dtReport, DailyReportActivity.this, getFragmentManager(), new MyCalendar.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker date) {
                        dtReport = date.getDate().getTime();
                        calculateDate();
                        makeReport();
                    }

                    @Override
                    public void onGregorianSet(int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        dtReport = calendar.getTimeInMillis();
                        calculateDate();
                        makeReport();
                    }
                });
                myCalendar.showDialog();

                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void makeReport() {
        db.open();
        //make report
        //for TYPE_INVOCIE
        double dailySale = db.getIntervalSale(sDay, eDay, ProjectInfo.TYPE_INVOCIE);
        double dailyDiscount = db.getIntervalDiscount(sDay, eDay, ProjectInfo.TYPE_INVOCIE);
        double dailyTaxCharge = db.getIntervalTaxCharge2(sDay, eDay, ProjectInfo.TYPE_INVOCIE);
        double dailySumOfProducts = db.getIntervalCountOfProduct(sDay, eDay, ProjectInfo.TYPE_INVOCIE);
        tvDailySale.setText(ServiceTools.formatPrice(dailySale));
        tvDailyDiscount.setText(ServiceTools.formatPrice(dailyDiscount));
        tvDailyNetSale.setText(ServiceTools.formatPrice(dailySale - dailyDiscount));
        tvDailyTaxCharge.setText(ServiceTools.formatPrice(dailyTaxCharge));
        tvDailyFinalSum.setText(ServiceTools.formatPrice((dailySale - dailyDiscount) + dailyTaxCharge));
        tvDailyCountOfProduct.setText(formatCount(dailySumOfProducts));

        //for TYPE_INVOCIE
        double dailyPreSale = db.getIntervalSale(sDay, eDay, ProjectInfo.TYPE_ORDER);
        double dailyPreDiscount = db.getIntervalDiscount(sDay, eDay, ProjectInfo.TYPE_ORDER);
        double dailyPreTaxCharge = db.getIntervalTaxCharge2(sDay, eDay, ProjectInfo.TYPE_ORDER);
        double dailyPreSumOfProducts = db.getIntervalCountOfProduct(sDay, eDay, ProjectInfo.TYPE_ORDER);
        tvDailyPreSale.setText(ServiceTools.formatPrice(dailyPreSale));
        tvDailyPreDiscount.setText(ServiceTools.formatPrice(dailyPreDiscount));
        tvDailyPreNetSale.setText(ServiceTools.formatPrice(dailyPreSale - dailyPreDiscount));
        tvDailyPreTaxCharge.setText(ServiceTools.formatPrice(dailyPreTaxCharge));
        tvDailyPreFinalSum.setText(ServiceTools.formatPrice((dailyPreSale - dailyPreDiscount) + dailyPreTaxCharge));
        tvDailyPreCountOfProduct.setText(formatCount(dailyPreSumOfProducts));
        //receipts
        double dailyCashAmount = db.getIntervalReceipt(sDay, eDay, ProjectInfo.TYPE_CASH);
        double dailyChequeAmount = db.getIntervalReceipt(sDay, eDay, ProjectInfo.TYPE_CHEQUE);
        double dailyCashReceiptAmount = db.getIntervalReceipt(sDay, eDay, ProjectInfo.TYPE_CASH_RECEIPT);
        sumOfReceipt = dailyCashAmount + dailyChequeAmount + dailyCashReceiptAmount;
        tvDailyCashAmount.setText(ServiceTools.formatPrice(dailyCashAmount));
        tvDailyChequeAmount.setText(ServiceTools.formatPrice(dailyChequeAmount));
        tvDailyCashReceiptAmount.setText(ServiceTools.formatPrice(dailyCashReceiptAmount));
        tvDailySumOfReceipts.setText(ServiceTools.formatPrice(sumOfReceipt));

        //payments
        double dailyBankPayment = db.getIntervalPayment(sDay, eDay, ProjectInfo.Bank_TYPE);
        double dailyExpense = db.getIntervalPayment(sDay, eDay, ProjectInfo.Expense_TYPE);
        sumOfPayment = dailyBankPayment + dailyExpense;
        tvDailyBank.setText(ServiceTools.formatPrice(dailyBankPayment));
        tvDailyExpense.setText(ServiceTools.formatPrice(dailyExpense));
        tvSumOfPayment.setText(ServiceTools.formatPrice(sumOfPayment));

        //Remained
        double dailyRemained = sumOfReceipt - sumOfPayment;

        tvDailyRemained.setText(ServiceTools.formatPrice(dailyRemained));
        tvDailyRemainedVisitor.setText(getString(R.string.balance_value) + BaseActivity.getUserProfile().getName());

        db.close();
    }

    public void initData() {
        db = new DbAdapter(mContext);
        Date dt = new Date();
        dtReport = dt.getTime();
        calculateDate();
        makeReport();
    }

    public void calculateDate() {
        Calendar calReport = Calendar.getInstance();
        calReport.setTimeInMillis(dtReport);
        calReport.set(Calendar.HOUR_OF_DAY, 0);
        calReport.set(Calendar.MINUTE, 0);
        calReport.set(Calendar.SECOND, 0);
        calReport.set(Calendar.MILLISECOND, 0);
        sDay = calReport.getTimeInMillis();
        CivilDate cdt = new CivilDate(calReport);
        calReport.add(Calendar.DAY_OF_MONTH, 1);
        eDay = calReport.getTimeInMillis();

        PersianDate pdt = DateConverter.civilToPersian(cdt);
    }

    public void init() {
        tvDailySale = (TextView) findViewById(R.id.tvDailySale);
        tvDailyDiscount = (TextView) findViewById(R.id.tvDailyDiscount);
        tvDailyNetSale = (TextView) findViewById(R.id.tvDailyNetSale);
        tvDailyTaxCharge = (TextView) findViewById(R.id.tvDailyTaxCharge);
        tvDailyFinalSum = (TextView) findViewById(R.id.tvDailyFinalSum);
        tvDailyCountOfProduct = (TextView) findViewById(R.id.tvDailyCountOfProduct);
        tvDailyPreSale = (TextView) findViewById(R.id.tvDailyPreSale);
        tvDailyPreDiscount = (TextView) findViewById(R.id.tvDailyPreDiscount);
        tvDailyPreNetSale = (TextView) findViewById(R.id.tvDailyPreNetSale);
        tvDailyPreTaxCharge = (TextView) findViewById(R.id.tvDailyPreTaxCharge);
        tvDailyPreFinalSum = (TextView) findViewById(R.id.tvDailyPreFinalSum);
        tvDailyPreCountOfProduct = (TextView) findViewById(R.id.tvDailyPreCountOfProduct);
        tvDailyCashAmount = (TextView) findViewById(R.id.tvDailyCashAmount);
        tvDailyChequeAmount = (TextView) findViewById(R.id.tvDailyChequeAmount);
        tvDailyCashReceiptAmount = (TextView) findViewById(R.id.tvDailyCashReceiptAmount);
        tvDailySumOfReceipts = (TextView) findViewById(R.id.tvDailySumOfReceipts);

        tvDailyBank = (TextView) findViewById(R.id.tvDailyBank);
        tvDailyExpense = (TextView) findViewById(R.id.tvDailyExpense);
        tvSumOfPayment = (TextView) findViewById(R.id.tvSumOfPayment);
        tvDailyRemained = (TextView) findViewById(R.id.tvDailyRemained);
        tvDailyRemainedVisitor = (TextView) findViewById(R.id.tvDailyRemainedVisitor);


    }


    public void initPrintViewData(View view) {

        TextView _tvDailySale = (TextView) view.findViewById(R.id.tvDailySale);
        TextView _tvDailyDiscount = (TextView) view.findViewById(R.id.tvDailyDiscount);
        TextView _tvDailyNetSale = (TextView) view.findViewById(R.id.tvDailyNetSale);
        TextView _tvDailyTaxCharge = (TextView) view.findViewById(R.id.tvDailyTaxCharge);
        TextView _tvDailyFinalSum = (TextView) view.findViewById(R.id.tvDailyFinalSum);
        TextView _tvDailyCountOfProduct = (TextView) view.findViewById(R.id.tvDailyCountOfProduct);
        TextView _tvDailyPreSale = (TextView) view.findViewById(R.id.tvDailyPreSale);
        TextView _tvDailyPreDiscount = (TextView) view.findViewById(R.id.tvDailyPreDiscount);
        TextView _tvDailyPreNetSale = (TextView) view.findViewById(R.id.tvDailyPreNetSale);
        TextView _tvDailyPreTaxCharge = (TextView) view.findViewById(R.id.tvDailyPreTaxCharge);
        TextView _tvDailyPreFinalSum = (TextView) view.findViewById(R.id.tvDailyPreFinalSum);
        TextView _tvDailyPreCountOfProduct = (TextView) view.findViewById(R.id.tvDailyPreCountOfProduct);
        TextView _tvDailyCashAmount = (TextView) view.findViewById(R.id.tvDailyCashAmount);
        TextView _tvDailyChequeAmount = (TextView) view.findViewById(R.id.tvDailyChequeAmount);
        TextView _tvDailyCashReceiptAmount = (TextView) view.findViewById(R.id.tvDailyCashReceiptAmount);
        TextView _tvDailySumOfReceipts = (TextView) view.findViewById(R.id.tvDailySumOfReceipts);
        TextView _tvDailyBank = (TextView) view.findViewById(R.id.tvDailyBank);
        TextView _tvDailyExpense = (TextView) view.findViewById(R.id.tvDailyExpense);
        TextView _tvSumOfPayment = (TextView) view.findViewById(R.id.tvSumOfPayment);
        TextView _tvDailyRemained = (TextView) view.findViewById(R.id.tvDailyRemained);


        _tvDailySale.setText(tvDailySale.getText().toString());
        _tvDailyDiscount.setText(tvDailyDiscount.getText().toString());
        _tvDailyNetSale.setText(tvDailyNetSale.getText().toString());
        _tvDailyTaxCharge.setText(tvDailyTaxCharge.getText().toString());
        _tvDailyFinalSum.setText(tvDailyFinalSum.getText().toString());
        _tvDailyCountOfProduct.setText(tvDailyCountOfProduct.getText().toString());
        _tvDailyPreSale.setText(tvDailyPreSale.getText().toString());
        _tvDailyPreDiscount.setText(tvDailyPreDiscount.getText().toString());
        _tvDailyPreNetSale.setText(tvDailyPreNetSale.getText().toString());
        _tvDailyPreTaxCharge.setText(tvDailyPreTaxCharge.getText().toString());
        _tvDailyPreFinalSum.setText(tvDailyPreFinalSum.getText().toString());
        _tvDailyPreCountOfProduct.setText(tvDailyPreCountOfProduct.getText().toString());
        _tvDailyCashAmount.setText(tvDailyCashAmount.getText().toString());
        _tvDailyChequeAmount.setText(tvDailyChequeAmount.getText().toString());
        _tvDailyCashReceiptAmount.setText(tvDailyCashReceiptAmount.getText().toString());
        _tvDailySumOfReceipts.setText(tvDailySumOfReceipts.getText().toString());
        _tvDailyBank.setText(tvDailyBank.getText().toString());
        _tvDailyExpense.setText(tvDailyExpense.getText().toString());
        _tvSumOfPayment.setText(tvSumOfPayment.getText().toString());
        _tvDailyRemained.setText(tvDailyRemained.getText().toString());
    }

    private class preparePrinterFile extends AsyncTask<String, Integer, Boolean> {

        Bitmap b = null;
        String fName = "";
        String fPath = ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_IMAGES + "/" + ProjectInfo.DIRECTORY_REPORTS;
        //View ll;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean status = false;

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (SharedPreferencesHelper.getCompactPrint(mContext)) {
                if (printerBrand == ProjectInfo.PRINTER_BABY_380_A || printerBrand == ProjectInfo.PRINTER_DELTA_380_A) {
                    ll = inflater.inflate(R.layout.print_view_report_1_80_compact, null, false);
                } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII) {
                    ll = inflater.inflate(R.layout.print_view_report_1_88_compact, null, false);
                    LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                    ChangePrintWidth(_llPrint);
                } else if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310) {
                    ll = inflater.inflate(R.layout.print_view_report_1_80_compact, null, false);
                } else if (printerBrand == ProjectInfo.PRINTER_BABY_280_A) {
                    ll = inflater.inflate(R.layout.print_view_report_1_50_compact, null, false);
                } else if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW || printerBrand == ProjectInfo.UROVO_K319 || printerBrand == Woosim_WSP_R341) {
                    ll = inflater.inflate(R.layout.print_view_report_1_88_compact, null, false);
                    LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                    ChangePrintWidth(_llPrint);
                } else {
                    ll = inflater.inflate(R.layout.print_view_report_1_compact, null, false);
                }
            } else {
                if (printerBrand == ProjectInfo.PRINTER_BABY_380_A || printerBrand == ProjectInfo.PRINTER_DELTA_380_A) {
                    ll = inflater.inflate(R.layout.print_view_report_1_80, null, false);
                } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII) {
                    ll = inflater.inflate(R.layout.print_view_report_1_88, null, false);
                    LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                    ChangePrintWidth(_llPrint);
                } else if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310) {
                    ll = inflater.inflate(R.layout.print_view_report_1_80, null, false);
                } else if (printerBrand == ProjectInfo.PRINTER_BABY_280_A) {
                    ll = inflater.inflate(R.layout.print_view_report_1_50, null, false);
                } else if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW || printerBrand == ProjectInfo.UROVO_K319 || printerBrand == Woosim_WSP_R341) {
                    ll = inflater.inflate(R.layout.print_view_report_1_88, null, false);
                    LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                    ChangePrintWidth(_llPrint);
                } else {
                    ll = inflater.inflate(R.layout.print_view_report_1, null, false);
                }
            }

            initPrintViewData(ll);
            ll.setDrawingCacheEnabled(true);
            ll.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            ll.layout(0, 0, ll.getMeasuredWidth(), ll.getMeasuredHeight());
            ll.buildDrawingCache(true);
            b = Printer.CreateBitmap(ll);
            ll.setDrawingCacheEnabled(false);
            fName = getFileName();
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
                Intent intent = new Intent(DailyReportActivity.this, PrintActivity.class);
                intent.putExtra(ProjectInfo._TAG_PAGE_NAME, ProjectInfo._pName_DailyReport);
                intent.putExtra(ProjectInfo._TAG_PATH, fPath);
                intent.putExtra(ProjectInfo._TAG_Name, fName);
                startActivity(intent);
            }
        }
    }

    public void ChangePrintWidth(LinearLayout ll) {

        ViewGroup.LayoutParams param = ll.getLayoutParams();
        final float scale = getResources().getDisplayMetrics().density;
        //convert mm to dp
        double Size = SharedPreferencesHelper.getCurrentWidthSize(mContext) * 6.3;
        int converter = (int) (Size * scale + 0.5f);
        param.width = converter;
        ll.setLayoutParams(param);

    }

    public String getFileName() {
        Calendar calendar = Calendar.getInstance();
        return ServiceTools.getFileName(calendar.getTimeInMillis()) + "_daily_reports.png";
    }
}
