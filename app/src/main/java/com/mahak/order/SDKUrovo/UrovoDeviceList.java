package com.mahak.order.SDKUrovo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.BaseActivity;
import com.mahak.order.R;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UrovoDeviceList extends BaseActivity {

    private Button BtnRefrs;
    //    private ToggleButton BtnInch;
    private String PrinterName;
    private String PrinterId;
    private ListView lstView;
    private TextView txtdevicename;
    private EditText txtwidth;
    private EditText txtheight;
    public static BluetoothAdapter myBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.urovo_print_setting);

        setTitle(R.string.title_setting);
        BtnRefrs = (Button) findViewById(R.id.printsetting_Btn_Refresh);
        lstView = (ListView) findViewById(R.id.printsetting_lst_device);
//        BtnInch = findViewById(R.id.printsetting_togglebutton);
        txtdevicename = (TextView) findViewById(R.id.printsetting_Txt_NameDevice);
        txtwidth = (EditText) findViewById(R.id.printsetting_Txt_Width);
        txtheight = (EditText) findViewById(R.id.printsetting_Txt_Height);
        lstView.getLayoutParams().height = 0;
        SetValue();

//        BtnInch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    Hawk.put("Inch3", true);
//                } else {
        Hawk.put("Inch3", false);
//                }
//            }
//        });
        BtnRefrs.setOnClickListener(new Button.OnClickListener() {
            //            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(View arg0) {

                try {

                    ListBluetoothDevice();

                } catch (Exception e) {
                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                    FirebaseCrashlytics.getInstance().recordException(e);
                    e.printStackTrace();
                }
            }
        });

        txtwidth.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                if (txtwidth.getText().length() == 0) {
                    txtwidth.setText("0");
                } else if (Integer.valueOf(txtwidth.getText().toString()) > 100) {
                    txtwidth.setText("100");
                }


//                if (txtwidth.getText().length() == 0) {
//                    Hawk.put("Width", "0");
//                } else {

                Hawk.put("Width", txtwidth.getText().toString());
//                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        txtheight.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (txtheight.getText().length() == 0) {
                    txtheight.setText("0");
                } else if (Integer.valueOf(txtheight.getText().toString()) > 100) {
                    txtheight.setText("100");
                }

                Hawk.put("Height", txtheight.getText().toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }

    private void SetValue() {

        String valueId = Hawk.get("PrinterId");
        if (valueId != null) {
            PrinterId = valueId;
        }

        String valueName = Hawk.get("PrinterName");
        if (valueName != null) {
            txtdevicename.setText(valueName);
            PrinterName = valueName;
        }

//        Boolean valueinch = Hawk.get("Inch3");
//        if (valueinch != null) {
//            BtnInch.setChecked(valueinch);
//        }


        String valueWidth = Hawk.get("Width");
        if (valueWidth != null) {
            txtwidth.setText(valueWidth);
        } else {
            txtwidth.setText("0");
        }
        String valueHeight = Hawk.get("Height");
        if (valueHeight != null) {
            txtheight.setText(valueHeight);
        } else {
            txtheight.setText("0");
        }

    }

    public boolean ListBluetoothDevice() {
        final List<Map<String, String>> list = new ArrayList<Map<String, String>>();


        SimpleAdapter m_adapter = new SimpleAdapter(this, list,
                android.R.layout.simple_list_item_2,
                new String[]{"DeviceName", "BDAddress"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );
        lstView.setAdapter(m_adapter);

        lstView.getLayoutParams().height = 300;

        if ((myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()) == null) {
            Toast.makeText(this, "can not find Bluetooth Adapter", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!myBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 2);
        }
        Set<BluetoothDevice> pairedDevices = myBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() <= 0) return false;
        for (BluetoothDevice device : pairedDevices) {
            if (pairedDevices.size() > 0) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("DeviceName", device.getName());
                map.put("BDAddress", device.getAddress());
                if (device.getName().contains("K319"))
                    list.add(map);
            }

        }
        lstView.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                PrinterName = list.get(arg2).get("DeviceName");
                PrinterId = list.get(arg2).get("BDAddress");

                if (((ListView) arg0).getTag() != null) {
                    ((View) ((ListView) arg0).getTag()).setBackgroundDrawable(null);
                }
                ((ListView) arg0).setTag(arg1);
                arg1.setBackgroundColor(Color.parseColor("#C9C5C5"));

                if (PrinterId.length() > 0) {
                    Hawk.put("PrinterId", PrinterId);
                    Hawk.put("PrinterName", PrinterName);
                    PublicClass.PrinterId = PrinterId;
                    PublicClass.PrinterName = PrinterName;
                    txtdevicename.setText(PrinterName);
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
        return true;
    }
}
