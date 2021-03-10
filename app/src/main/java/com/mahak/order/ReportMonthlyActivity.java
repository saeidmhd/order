package com.mahak.order;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mahak.order.calendar.CivilDate;
import com.mahak.order.calendar.DateConverter;
import com.mahak.order.calendar.PersianDate;
import com.mahak.order.common.ExpandAnimation;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ReportMonth;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportMonthlyActivity extends BaseActivity {

    private ListView lstReportMonthly;
    private List<ReportMonth> arrayReportMonthly = new ArrayList<ReportMonth>();
    private Activity mActivity;
    private ImageButton btnNext, btnPrev;
    private TextView tvMonthTitle, tvFinalPrice;
    private Calendar cal;
    private PersianDate persian;
    private CivilDate cv;
    private int ReportType;
    private Bundle Extras;
    private DbAdapter db;
    private Context mContext;
    private AdapterListReportMonthly adapterReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_monthly);

        mActivity = this;
        mContext = this;

        Extras = getIntent().getExtras();
        if (Extras != null) {
            ReportType = Extras.getInt(MODE_PAGE);
        }

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
        TextView tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        if (ReportType == ProjectInfo.REPORT_TYPE_ORDER)
            tvPageTitle.setText(getString(R.string.str_report_monthly_order));
        else if (ReportType == ProjectInfo.REPORT_TYPE_INVOICE)
            tvPageTitle.setText(getString(R.string.str_report_monthly_invoice));
        else if (ReportType == ProjectInfo.REPORT_TYPE_RECEIPT)
            tvPageTitle.setText(getString(R.string.str_report_monthly_receipt));
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________

        initialise();

        //Fill Month title///////////////////////
        cal = Calendar.getInstance();
        cv = new CivilDate(cal);


        persian = DateConverter.civilToPersian(cv);
        persian.setDayOfMonth(1);
        setMonthTitle();

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                persian.addMonth();
                setMonthTitle();
                FillArrayMonthlyOrder(ReportType, persian.getYear(), persian.getMonth(), persian.getDayOfMonth());
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                persian.minusMonth();
                setMonthTitle();
                FillArrayMonthlyOrder(ReportType, persian.getYear(), persian.getMonth(), persian.getDayOfMonth());
            }
        });

        FillArrayMonthlyOrder(ReportType, persian.getYear(), persian.getMonth(), persian.getDayOfMonth());

        lstReportMonthly.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (ReportType == ProjectInfo.REPORT_TYPE_RECEIPT) {
                    LinearLayout llReceipt = (LinearLayout) view.findViewById(R.id.llReceipt);
                    ExpandAnimation expandAni = new ExpandAnimation(llReceipt, 350);
                    llReceipt.startAnimation(expandAni);
                }
            }

        });
    }

    /**
     * Initializing Variables
     */
    private void initialise() {

        lstReportMonthly = (ListView) findViewById(R.id.lstReportMonthly);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrev = (ImageButton) findViewById(R.id.btnPrev);
        tvMonthTitle = (TextView) findViewById(R.id.tvMonth);
        tvFinalPrice = (TextView) findViewById(R.id.tvFinalPrice);


        db = new DbAdapter(mContext);
    }

    /**
     * set month title
     */
    private void setMonthTitle() {

        String strDate = "";
        strDate = persian.getMonthName() + " " + persian.getYear();
        tvMonthTitle.setText(strDate);
    }

    /**
     * get day of week
     *
     * @param day
     * @return
     */
    public String getDayName(int day) {
        String dayName = "";
        if (day == 0)
            day = 7;
        dayName = PersianDate.WeekDayName[day];
        return dayName;
    }

    /**
     * Fill Array report Month and set Adapter and set TextView FinalPrice
     *
     * @param reporttype
     * @param y
     * @param m
     * @param d
     */
    private void FillArrayMonthlyOrder(int reporttype, int y, int m, int d) {

        PersianDate per = new PersianDate(y, m, d);
        long startdate = 0, enddate = 0;
        double TotalPrice = 0;
        db.open();
        int month = per.getMonth();
        arrayReportMonthly = new ArrayList<ReportMonth>();
        ReportMonth reportmonth;
        String DayName = "";
        do {
            reportmonth = new ReportMonth();
            Calendar cal = Calendar.getInstance();

            CivilDate civil = new CivilDate();
            civil = DateConverter.persianToCivil(per);
            cal.set(Calendar.YEAR, civil.getYear());
            cal.set(Calendar.MONTH, civil.getMonth() - 1);
            cal.set(Calendar.DAY_OF_MONTH, civil.getDayOfMonth());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            /////////////////////////////////////
            DayName = getDayName(cal.get(Calendar.DAY_OF_WEEK)) + " " + per.getDayOfMonth();
            reportmonth.setName(DayName);

            startdate = cal.getTimeInMillis();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            enddate = cal.getTimeInMillis();
            //fill item ReportMonth ///////////////////////////
            if (reporttype == ProjectInfo.REPORT_TYPE_ORDER) {
                double price = db.getIntervalSale(startdate, enddate, ProjectInfo.TYPE_ORDER);
                TotalPrice = TotalPrice + price;
                reportmonth.setPrice(price);
            } else if (reporttype == ProjectInfo.REPORT_TYPE_INVOICE) {
                double price = db.getIntervalSale(startdate, enddate, ProjectInfo.TYPE_INVOCIE);
                TotalPrice = TotalPrice + price;
                reportmonth.setPrice(price);
            } else if (reporttype == ProjectInfo.REPORT_TYPE_RECEIPT) {
                reportmonth = db.getPriceReceipt(startdate, enddate);
                TotalPrice = TotalPrice + reportmonth.getPrice();
                reportmonth.setName(DayName);
            }
            arrayReportMonthly.add(reportmonth);
            ///////////////////////////////////////
            per.addDay();

        } while (month == per.getMonth());

        ///Calculate Percent///////////////////////////
        for (ReportMonth object : arrayReportMonthly) {
            double percent = 0;
            double price = object.getPrice();
            if (TotalPrice > 0)
                percent = (price * 100) / TotalPrice;

            object.setPercent((percent));
        }
        db.close();
        //Fill Adapter////////////////////////////////////////
        adapterReport = new AdapterListReportMonthly(mActivity, reporttype);
        lstReportMonthly.setAdapter(adapterReport);
        adapterReport.notifyDataSetChanged();
        //setFinalPrice//////////////////////////////////////
        tvFinalPrice.setText(ServiceTools.formatPrice(TotalPrice));
    }

    private class AdapterListReportMonthly extends ArrayAdapter<ReportMonth> {

        Activity mcontext;
        int Type = 0;
        final int aniTime = 1200;

        public AdapterListReportMonthly(Activity context, int reporttype) {
            super(context, android.R.layout.simple_list_item_1, arrayReportMonthly);
            mcontext = context;
            Type = reporttype;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rowview = convertView;
            Holder holder = null;
            LayoutInflater inflater = null;
            final ReportMonth item = getItem(position);

            if (rowview == null) {
                inflater = mcontext.getLayoutInflater();
                rowview = inflater.inflate(R.layout.lst_monthly_report_item, null, false);
                holder = new Holder(rowview);
                rowview.setTag(holder);
            } else
                holder = (Holder) rowview.getTag();

            holder.Populate(item, position);


            return rowview;
        }

        public class Holder {

            TextView tvName, tvPrice, tvPercent;
            ProgressBar pbPrice;
            LinearLayout llReceipt;
            TextView tvCashAmount, tvCashReceipt, tvChegue;
            LayerDrawable layerDrawable;
            ScaleDrawable scaleDrawable;

            public Holder(View rowview) {

                tvName = (TextView) rowview.findViewById(R.id.tvDayName);
                tvPrice = (TextView) rowview.findViewById(R.id.tvPrice);
                tvPercent = (TextView) rowview.findViewById(R.id.tvPercent);
                pbPrice = (ProgressBar) rowview.findViewById(R.id.pbPrice);

                llReceipt = (LinearLayout) rowview.findViewById(R.id.llReceipt);
                tvCashAmount = (TextView) rowview.findViewById(R.id.tvCash);
                tvCashReceipt = (TextView) rowview.findViewById(R.id.tvCashReceipt);
                tvChegue = (TextView) rowview.findViewById(R.id.tvCheque);

                layerDrawable = (LayerDrawable) getResources().getDrawable(R.drawable.progress_bg);
                scaleDrawable = (ScaleDrawable) layerDrawable.findDrawableByLayerId(android.R.id.progress);
            }

            public void Populate(ReportMonth item, int Position) {

                ///////////////////////////////////////
                scaleDrawable.setColorFilter(Color.parseColor("#3CB371"), android.graphics.PorterDuff.Mode.SRC_IN);
                pbPrice.setProgressDrawable(layerDrawable);
                pbPrice.setProgress((int) (item.getPercent()));
				/*ProgressBarAnimation anim = new ProgressBarAnimation(pbPrice, 0, Math.round(item.getPercent()));
		        anim.setDuration(aniTime);*/
		        /*if(!arrayReportMonthly.get(Position).isStatusAnimation()){
		        	pbPrice.startAnimation(anim);
		        	arrayReportMonthly.get(Position).setStatusAnimation(true);
		        }*/
                //else{
                // 	pbPrice.setProgress(Math.round(arrayReportMonthly.get(Position).getPercent()));
                // }
                //////////////////////////////////////
                tvName.setText(item.getName());
                tvPrice.setText(ServiceTools.formatPrice(item.getPrice()));
                tvPercent.setText(String.valueOf(item.getPercent()));

                if (Type == ProjectInfo.REPORT_TYPE_RECEIPT) {
                    tvCashAmount.setText(ServiceTools.formatPrice(item.getCashAmount()));
                    tvCashReceipt.setText(ServiceTools.formatPrice(item.getCashReceipt()));
                    tvChegue.setText(ServiceTools.formatPrice(item.getCheque()));
                }//end of if
                ////////////////////////////////////
                ((LinearLayout.LayoutParams) llReceipt.getLayoutParams()).bottomMargin = -90;
                llReceipt.setVisibility(View.GONE);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }
}
