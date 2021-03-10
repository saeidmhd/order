package com.mahak.order;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;

import com.mahak.order.common.ProjectInfo;

import java.util.ArrayList;
import java.util.Arrays;

public class ReportsListActivity extends BaseActivity {

    GridView grvReports;
    ArrayList<String> Reports = new ArrayList<>();
    TypedArray Thumbs;
    ArrayList<Intent> intents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_list);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        init();

        initData();
    }//end of onCreate

    public void initData() {

        intents.add(new Intent(getApplicationContext(), CustomerReportActivity.class).putExtra(ProjectInfo._TAG_TYPE, ProjectInfo.TYPE_INVOCIE));
        intents.add(new Intent(getApplicationContext(), CustomerReportActivity.class).putExtra(ProjectInfo._TAG_TYPE, ProjectInfo.TYPE_ORDER));
        intents.add(new Intent(getApplicationContext(), ProductReportActivity.class).putExtra(ProjectInfo._TAG_TYPE, ProjectInfo.TYPE_INVOCIE));
        intents.add(new Intent(getApplicationContext(), ProductReportActivity.class).putExtra(ProjectInfo._TAG_TYPE, ProjectInfo.TYPE_ORDER));
        intents.add(new Intent(getApplicationContext(), DailyReportActivity.class));
        intents.add(new Intent(getApplicationContext(), ReportActivity.class));
        intents.add(new Intent(getApplicationContext(), CustomerReturnReportActivity.class).putExtra(ProjectInfo._TAG_TYPE, ProjectInfo.TYPE_RETURN_OF_SALE));

        String[] arrReports = getResources().getStringArray(R.array.reports_list_title);
        Thumbs = getResources().obtainTypedArray(R.array.reports_list_thumbs);
        Reports = new ArrayList<>(Arrays.asList(arrReports));
        grvReports.setAdapter(new ReportsAdapter(getApplicationContext(), Reports));
        grvReports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(intents.get(position));
            }
        });

    }

    public class ReportsAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<String> Reports;
        LayoutInflater inflater;
        ImageView imgThumb;
        TextView tvTitle;

        public ReportsAdapter(Context context, ArrayList<String> Reports) {
            this.context = context;
            this.Reports = Reports;
            inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_grid_reports_tile, null);
                imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);
                tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            }

            tvTitle.setText(Reports.get(position));
            imgThumb.setImageResource(Thumbs.getResourceId(position, -1));
            return convertView;
        }

        @Override
        public int getCount() {
            return Reports.size();
        }

        @Override
        public Object getItem(int position) {
            return Reports.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    public void init() {
        grvReports = (GridView) findViewById(R.id.grvReports);
    }
}
