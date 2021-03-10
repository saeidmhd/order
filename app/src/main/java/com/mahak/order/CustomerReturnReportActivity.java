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
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mahak.order.calendar.CivilDate;
import com.mahak.order.calendar.DateConverter;
import com.mahak.order.calendar.PersianDate;
import com.mahak.order.common.MyCalendar;
import com.mahak.order.common.Printer;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ReportUserDetail;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.SharedPreferencesHelper;
import com.mahak.order.libs.DatePicker;
import com.mahak.order.libs.SmlExpandableListView;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.mahak.order.common.ProjectInfo.Woosim_WSP_R341;

public class CustomerReturnReportActivity extends BaseActivity {

    SmlExpandableListView list;
    long dtReport;
    TextView tvItemCount;
    private DbAdapter db;
    private long sDay, eDay;
    ArrayList<ReportUserDetail> items = new ArrayList<>();
    private int Type = ProjectInfo.TYPE_RETURN_OF_SALE;
    TextView tvTitle;
    Context mContext;
    View ll;
    int printerBrand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_report);

        mContext = this;
        printerBrand = SharedPreferencesHelper.getPrefPrinterBrand(mContext);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Type = extras.getInt(ProjectInfo._TAG_TYPE);
        }

        init();
        initData();


    }//end of onCreate

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
                MyCalendar myCalendar = new MyCalendar(dtReport, CustomerReturnReportActivity.this, getFragmentManager(), new MyCalendar.OnDateSetListener() {
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
        items.clear();
        items.addAll(db.getCustomerReturnOfSale(sDay, eDay, Type));
        list.setExpanded(true);
        list.setAdapter(new listAdapter(getApplicationContext(), items));
        db.close();
        tvItemCount.setText("(" + String.valueOf(items.size()) + ")");
    }

    public void initData() {
        if (Type == ProjectInfo.TYPE_FACTOR)
            tvTitle.setText(tvTitle.getText() + " (" + getString(R.string.return_of_sale) + ")");

        db = new DbAdapter(mContext);
        Date dt = new Date();
        dtReport = dt.getTime();
        calculateDate();
        makeReport();
    }

    public class listAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<ReportUserDetail> items = new ArrayList<>();
        LayoutInflater inflater;
        TextView tvName, tvCount, tvFinalSum, tvFinalSumRetrn, tvCountReturn, tvdeductionfromsale;

        double salePrice;
        double returnSalePrice;

        public listAdapter(Context context, ArrayList<ReportUserDetail> items) {
            this.context = context;
            this.items = items;
            inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {


            salePrice = items.get(position).getSale() - items.get(position).getDiscount() + items.get(position).getTaxCharge();
            returnSalePrice = items.get(position).getReturnOfSale() - items.get(position).getDiscount() + items.get(position).getTaxCharge();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.lst_return_report_cutomer_item, null);
                tvName = (TextView) convertView.findViewById(R.id.tvName);
                tvCount = (TextView) convertView.findViewById(R.id.tvCount);
                tvFinalSum = (TextView) convertView.findViewById(R.id.tvFinalSum);
                tvFinalSumRetrn = (TextView) convertView.findViewById(R.id.tvFinalSumRetrn);
                tvCountReturn = (TextView) convertView.findViewById(R.id.tvCountReturn);
                tvdeductionfromsale = (TextView) convertView.findViewById(R.id.tvdeductionfromsale);
            }
            tvName.setText(items.get(position).getName());
            tvCount.setText(ServiceTools.formatCount(items.get(position).getCountOfProduct()));
            tvCountReturn.setText(ServiceTools.formatCount(items.get(position).getCountOfReturn()));
            tvFinalSumRetrn.setText(ServiceTools.formatPrice(returnSalePrice));
            tvFinalSum.setText(ServiceTools.formatPrice(salePrice));
            double deduction = (salePrice - returnSalePrice);
            tvdeductionfromsale.setText(ServiceTools.formatPrice(deduction));
            return convertView;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    public class printListAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<ReportUserDetail> items = new ArrayList<>();
        LayoutInflater inflater;
        TextView tvName, tvFinalSumRetrn, tvDeduction, tvFinalSum;

        double salePrice;
        double returnSalePrice;

        public printListAdapter(Context context, ArrayList<ReportUserDetail> items) {
            this.context = context;
            this.items = items;
            inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            salePrice = items.get(position).getSale() - items.get(position).getDiscount() + items.get(position).getTaxCharge();
            returnSalePrice = items.get(position).getReturnOfSale() - items.get(position).getDiscount() + items.get(position).getTaxCharge();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.lst_report_cutomer_return_print, null);
                tvFinalSumRetrn = (TextView) convertView.findViewById(R.id.tvSumReturnAmount);
                tvFinalSum = (TextView) convertView.findViewById(R.id.tvFinalSum);
                tvName = (TextView) convertView.findViewById(R.id.tvName);
                tvDeduction = (TextView) convertView.findViewById(R.id.tvDeduction);
            }

            tvName.setText(items.get(position).getName());
            tvFinalSumRetrn.setText(ServiceTools.formatPrice(returnSalePrice));
            tvFinalSum.setText(ServiceTools.formatPrice(salePrice));
            double deduction = (salePrice - returnSalePrice);
            tvDeduction.setText(ServiceTools.formatPrice(deduction));
            return convertView;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    public void init() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        list = (SmlExpandableListView) findViewById(R.id.list);
        tvItemCount = (TextView) findViewById(R.id.tvItemCount);
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


            if (printerBrand == ProjectInfo.PRINTER_BABY_380_A || printerBrand == ProjectInfo.PRINTER_DELTA_380_A) {
                ll = inflater.inflate(R.layout.print_view_report_2_80, null, false);
            } else if (printerBrand == ProjectInfo.PRINTER_BABY_380_KOOHII) {
                ll = inflater.inflate(R.layout.print_view_report_2_88, null, false);
                LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                ChangePrintWidth(_llPrint);
            } else if (printerBrand == ProjectInfo.PRINTER_OSCAR_POS88MW || printerBrand == ProjectInfo.UROVO_K319 || printerBrand == Woosim_WSP_R341) {
                ll = inflater.inflate(R.layout.print_view_report_2_88, null, false);
                LinearLayout _llPrint = (LinearLayout) ll.findViewById(R.id._llPrint);
                ChangePrintWidth(_llPrint);
            } else if (printerBrand == ProjectInfo.PRINTER_BIXOLON_SPP_R310) {
                ll = inflater.inflate(R.layout.print_view_report_2_80, null, false);
            } else if (printerBrand == ProjectInfo.PRINTER_BABY_280_A) {
                ll = inflater.inflate(R.layout.print_view_report_2_50, null, false);
            } else {
                ll = inflater.inflate(R.layout.print_view_report_2, null, false);
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
                Intent intent = new Intent(CustomerReturnReportActivity.this, PrintActivity.class);
                intent.putExtra(ProjectInfo._TAG_PAGE_NAME, ProjectInfo._pName_CustomerReport);
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

    public void initPrintViewData(View view) {
        ListView _list = (ListView) view.findViewById(R.id.list);
        _list.setDrawingCacheEnabled(true);
        _list.setAdapter(new printListAdapter(getApplicationContext(), items));
        ServiceTools.setListViewHeightBasedOnChildren(_list);
        TextView _tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        _tvTitle.setText(tvTitle.getText());
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        Date dt = new Date();
        tvDate.setText(ServiceTools.getDateAndTimeForLong(dt.getTime()));
        TextView tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        if (BaseActivity.getAuthentication())
            tvUsername.setText(getUserProfile().getName());
    }

    public String getFileName() {
        Calendar calendar = Calendar.getInstance();
        return ServiceTools.getFileName(calendar.getTimeInMillis()) + "_daily_reports.png";
    }


}
