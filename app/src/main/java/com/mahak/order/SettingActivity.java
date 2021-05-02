package com.mahak.order;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mahak.order.common.Bank;
import com.mahak.order.common.Order;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.SharedPreferencesHelper;
import com.mahak.order.common.Visitor;
import com.mahak.order.service.ReadOfflinePicturesProducts;
import com.mahak.order.storage.DbAdapter;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.yariksoffice.lingver.Lingver;

import java.util.ArrayList;
import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    private static final int FOLDER_CODE = 131;
    private DbAdapter db;
    private CheckBox chkReduceAsset, chkShowField, chkShowSign, chkShowBelow, chkShowBelowPrice, chkPrintCompact, chkTemplate2;
    private RelativeLayout rvReduceAsset;
    private TextView tvReduceAsset;
    private EditText txtChargePercent, txtTaxPercent;
    private Spinner spnPrinterBrand;
    private Context mContext;
    private Spinner spnFromHour;
    private Spinner spnFromMinute;
    private Spinner spntoHour;
    private Spinner spntoMinute;
    private Spinner spnPosBank;
    private TextView tvFolderPictures;
    private int result = RESULT_CANCELED;
    private EditText PrinterSize;
    private LinearLayout llPrinterSize;
    private RadioButton small, medium, large, persian, deutsch;
    private ArrayList<Bank> arrayBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mContext = this;

        initialise();

        FillSpinner();

        int posBankPosition = SharedPreferencesHelper.getPrefBankPos(mContext);
        spnPosBank.setSelection(posBankPosition);

        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medium.setChecked(false);
                large.setChecked(false);
                SharedPreferencesHelper.setCurrentFontSize(mContext, 8);

            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                small.setChecked(false);
                large.setChecked(false);
                SharedPreferencesHelper.setCurrentFontSize(mContext, 10);

            }
        });

        large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                small.setChecked(false);
                medium.setChecked(false);
                SharedPreferencesHelper.setCurrentFontSize(mContext, 12);
            }
        });

        persian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deutsch.setChecked(false);
                Lingver.getInstance().setLocale(mContext, "en");
                Intent i = new Intent(SettingActivity.this, DashboardActivity.class);
                startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                SharedPreferencesHelper.setCurrentLanguage(mContext, "en");
            }
        });


        deutsch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                persian.setChecked(false);
                Lingver.getInstance().setLocale(mContext, Locale.GERMANY);
                Intent i = new Intent(SettingActivity.this, DashboardActivity.class);
                startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                SharedPreferencesHelper.setCurrentLanguage(mContext, "de_DE");
            }
        });


        int size = SharedPreferencesHelper.getCurrentWidthSize(mContext);
        PrinterSize.setText("" + size);

        chkReduceAsset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPreferencesHelper.setPrefReduceAsset(mContext, chkReduceAsset.isChecked());
            }
        });

        chkShowField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPreferencesHelper.setPrefShowFieldOrder(mContext, chkShowField.isChecked());

            }
        });
        chkPrintCompact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPreferencesHelper.setCompactPrint(mContext, chkPrintCompact.isChecked());

            }
        });

        chkShowSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferencesHelper.setSignUnderFactor(mContext, chkShowSign.isChecked());

            }
        });

        chkShowBelow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferencesHelper.setDetailUnderFactor(mContext, chkShowBelow.isChecked());

            }
        });

        chkShowBelowPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferencesHelper.setBelowPrice(mContext, chkShowBelowPrice.isChecked());

            }
        });

        chkTemplate2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                BaseActivity.setTemplate2Status(mContext, ProjectInfo._pName_OrderDetail, b);
            }
        });

        spnPrinterBrand.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferencesHelper.setPrefPrinterBrand(mContext, position);
                if (position == 3 || position == 4) {
                    llPrinterSize.setVisibility(View.VISIBLE);
                } else
                    llPrinterSize.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spnPosBank.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferencesHelper.setPrefBankPos(mContext, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        changeSpinerValue(spnFromHour, spnFromMinute, ProjectInfo.pre_start_time_tracking);
        changeSpinerValue(spntoHour, spntoMinute, ProjectInfo.pre_end_time_tracking);

        spnFromHour.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeTime(spnFromHour, spnFromMinute, ProjectInfo.pre_start_time_tracking);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnFromMinute.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeTime(spnFromHour, spnFromMinute, ProjectInfo.pre_start_time_tracking);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spntoHour.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeTime(spntoHour, spntoMinute, ProjectInfo.pre_end_time_tracking);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spntoMinute.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeTime(spntoHour, spntoMinute, ProjectInfo.pre_end_time_tracking);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }//End of Oncreate

    public void FillSpinner() {
        db.open();
        arrayBank = new ArrayList<Bank>();
        spnPosBank.setVisibility(View.VISIBLE);
        Visitor visitor = BaseActivity.getVisitor();
        if (visitor != null) {
            arrayBank = db.getAllBank(visitor.getBankCode());
        } else {
            arrayBank = db.getAllBank();
        }
        AdapterSpnBank adSpinner = new AdapterSpnBank(mContext, R.layout.item_spinner, arrayBank);
        spnPosBank.setAdapter(adSpinner);
        db.close();
    }

    public class AdapterSpnBank extends ArrayAdapter<Bank> {

        ArrayList<Bank> Objects;

        public AdapterSpnBank(Context context, int textViewResourceId, ArrayList<Bank> objects) {
            super(context, textViewResourceId, objects);
            Objects = objects;

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.item_spinner, parent, false);
            TextView tvName = (TextView) row.findViewById(R.id.tvName);
            tvName.setText(Objects.get(position).getName());

            return row;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (PrinterSize.getText().toString() != null && !PrinterSize.getText().toString().isEmpty()) {
            int size = ServiceTools.toInt(PrinterSize.getText().toString());
            SharedPreferencesHelper.setCurrentWidthSize(mContext, size);

        }
    }

    private void changeTime(Spinner spnHour, Spinner spnMinute, String key) {
        String time = "";
        int hour = spnHour.getSelectedItemPosition();
        if (hour == 0)
            time = "00:";
        else
            time = (hour - 1) + ":";
        int minute = spnMinute.getSelectedItemPosition();
        if (minute == 0)
            time += "00";
        else
            time += ((minute - 1) * 5);
        if (!time.equals("00:00"))
            ServiceTools.setKeyInSharedPreferences(mContext, key, time);
    }

    private void changeSpinerValue(Spinner spnHour, Spinner spnMinute, String key) {
        String time = ServiceTools.getKeyFromSharedPreferences(mContext, key);
        if (!ServiceTools.isNull(time)) {
            String[] sTime = time.split(":");
            int hour = ServiceTools.toInt(sTime[0]);
            int minute = ServiceTools.toInt(sTime[1]);
            spnHour.setSelection(hour + 1);
            spnMinute.setSelection((minute / 5) + 1);
        }
    }

    /**
     * Initializing Variables
     */
    private void initialise() {
        ArrayList<Order> arrayOrder = new ArrayList<Order>();
        db = new DbAdapter(mContext);
        llPrinterSize = (LinearLayout) findViewById(R.id.llPrinterSize);
        PrinterSize = (EditText) findViewById(R.id.PrinterSize);
        chkReduceAsset = (CheckBox) findViewById(R.id.chkReduceAsset);
        chkShowField = (CheckBox) findViewById(R.id.chkShowField);
        chkPrintCompact = (CheckBox) findViewById(R.id.chkPrintCompact);
        chkShowSign = (CheckBox) findViewById(R.id.chkShowSign);
        chkShowBelow = (CheckBox) findViewById(R.id.chkShowBelow);
        chkShowBelowPrice = (CheckBox) findViewById(R.id.chkBelowPrice);
        chkTemplate2 = (CheckBox) findViewById(R.id.chkTemplate2);
        rvReduceAsset = (RelativeLayout) findViewById(R.id.rvReduceAsset);
        tvReduceAsset = (TextView) findViewById(R.id.tvReduceAsset);
        txtChargePercent = (EditText) findViewById(R.id.txtChargePercent);
        txtTaxPercent = (EditText) findViewById(R.id.txtTaxPercent);
        spnPrinterBrand = (Spinner) findViewById(R.id.spnPrinterBrand);
        spnFromHour = (Spinner) findViewById(R.id.spinnerFromHour);
        spnFromMinute = (Spinner) findViewById(R.id.spinnerFromMinute);
        spntoHour = (Spinner) findViewById(R.id.spinnerToHour);
        spntoMinute = (Spinner) findViewById(R.id.spinnerToMinute);
        spnPosBank = (Spinner) findViewById(R.id.spnPosBank);
        tvFolderPictures = (TextView) findViewById(R.id.tvFolderPictures);

        small = (RadioButton) findViewById(R.id.small);
        medium = (RadioButton) findViewById(R.id.medium);
        large = (RadioButton) findViewById(R.id.large);

        persian = (RadioButton) findViewById(R.id.persian);
        deutsch = (RadioButton) findViewById(R.id.deutsch);

        tvFolderPictures.setText(ServiceTools.getKeyFromSharedPreferences(this, BaseActivity.getPrefUserMasterId(this) + ""));

        tvReduceAsset.setSelected(true);
        //Check Enable And Disable ReduceAsset__________________
        db.open();
        arrayOrder = db.getAllOrderNotPublish(BaseActivity.getPrefUserId());
        db.close();
        if (arrayOrder.size() > 0) {
            rvReduceAsset.setEnabled(false);
            chkReduceAsset.setEnabled(false);
            tvReduceAsset.setEnabled(false);
            tvReduceAsset.setTextColor(getResources().getColor(R.color.forms_sub_label_default));
        } else {
            rvReduceAsset.setEnabled(true);
            chkReduceAsset.setEnabled(true);
            tvReduceAsset.setEnabled(true);
            tvReduceAsset.setTextColor(getResources().getColor(android.R.color.black));
        }

        if (BaseActivity.getPrefReduceAsset(mContext))
            chkReduceAsset.setChecked(true);
        else
            chkReduceAsset.setChecked(false);

        if (BaseActivity.getPrefShowFieldOrder(mContext))
            chkShowField.setChecked(true);
        else
            chkShowField.setChecked(false);

        if (SharedPreferencesHelper.getSignUnderFactor(mContext))
            chkShowSign.setChecked(true);
        else
            chkShowSign.setChecked(false);

        if (SharedPreferencesHelper.getCompactPrint(mContext))
            chkPrintCompact.setChecked(true);
        else
            chkPrintCompact.setChecked(false);

        if (SharedPreferencesHelper.getDetailUnderFactor(mContext))
            chkShowBelow.setChecked(true);
        else
            chkShowBelow.setChecked(false);

        if (SharedPreferencesHelper.getBelowPrice(mContext))
            chkShowBelowPrice.setChecked(true);
        else
            chkShowBelowPrice.setChecked(false);

        if (SharedPreferencesHelper.getCurrentFontSize(mContext) == 8)
            small.setChecked(true);
        else if (SharedPreferencesHelper.getCurrentFontSize(mContext) == 10)
            medium.setChecked(true);
        else if (SharedPreferencesHelper.getCurrentFontSize(mContext) == 12)
            large.setChecked(true);

        if (SharedPreferencesHelper.getCurrentLanguage(mContext).equals("en"))
            persian.setChecked(true);
        else if (SharedPreferencesHelper.getCurrentLanguage(mContext).equals("de_DE"))
            deutsch.setChecked(true);

        chkTemplate2.setChecked(BaseActivity.getTemplate2Status(mContext, ProjectInfo._pName_OrderDetail));
        ////////////////////////////////////////////////////
        //Fill Tax And Charge

        if (!BaseActivity.getPrefTaxAndChargeIsActive().equals(BaseActivity.InActive)) {
            txtChargePercent.setText(BaseActivity.getPrefChargePercent());
            txtTaxPercent.setText(BaseActivity.getPrefTaxPercent());
        } else {
            txtChargePercent.setText(R.string.disabled);
            txtTaxPercent.setText(R.string.disabled);
        }

        int prtPos = SharedPreferencesHelper.getPrefPrinterBrand(mContext);
        spnPrinterBrand.setSelection(prtPos);

        tvFolderPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, FilePickerActivity.class);
                // This works if you defined the intent filter
                // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                // Set these depending on your use case. These are the defaults.
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);

                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);

                // Configure initial directory by specifying a String.
                // You could specify a String like "/storage/emulated/0/", but that can
                // dangerous. Always use Android's API calls to get paths to the SD-card or
                // internal memory.
                i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
                startActivityForResult(i, FOLDER_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FOLDER_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            tvFolderPictures.setText(uri.getPath());
            ServiceTools.setKeyInSharedPreferences(this, BaseActivity.getPrefUserMasterId(this) + "", uri.getPath());
            result = RESULT_OK;
        }
    }

    @Override
    public void finish() {
        if (result == RESULT_OK)
            new ReadOfflinePicturesProducts(getApplicationContext()).readAllImages();
        super.finish();
    }
}
