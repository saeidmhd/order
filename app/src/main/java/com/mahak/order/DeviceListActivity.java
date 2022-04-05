package com.mahak.order;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DeviceListActivity extends Activity {

    public static final String TAG = "DeviceListActivity";
    public static final boolean D = true;
    // ����  Intent��extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    // ��Ա�ֶ�
    public BluetoothAdapter mBtAdapter;
    private BluetoothDevice mmDevice;
    private BluetoothSocket mmSocket;
    private InputStream mmInStream;
    private OutputStream mmOutStream;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public List<String> pairedDeviceList = null;
    public List<String> newDeviceList = null;
    public ArrayAdapter<String> mPairedDevicesArrayAdapter;
    public ArrayAdapter<String> mNewDevicesArrayAdapter;
    public static String toothAddress = null;
    public static String toothName = null;
    private Context thisCon = null;
    private String strAddressList = "";
    private TextView tvTitle;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_device_list);
        setResult(Activity.RESULT_CANCELED);
        Button scanButton = (Button) findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                strAddressList = "";
                doDiscovery();
                v.setVisibility(View.GONE);
            }
        });

        thisCon = this.getApplicationContext();

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        progress = (ProgressBar) findViewById(R.id.progress);

        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getPairedData());
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);// ��BroadcastReceiver��ȡ��������� 
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(ACTION_PAIRING_REQUEST);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, intent);
        try {
            pairedListView.setOnItemClickListener(mDeviceClickListener);
            newDevicesListView.setOnItemClickListener(mDeviceClickListener);
        } catch (Exception excpt) {
            FirebaseCrashlytics.getInstance().recordException(excpt);
            Toast.makeText(this, thisCon.getString(R.string.get_device_err) + excpt, Toast.LENGTH_LONG).show();
        }
    }

    public List<String> getPairedData() {
        List<String> data = new ArrayList<String>();

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().toLowerCase().startsWith("mp") || device.getName().toLowerCase().startsWith("ml")) //filter just hprt printer devices
                    data.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.msg_no_connected_printer).toString();
            data.add(noDevices);
        }
        return data;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
    }

    public void doDiscovery() {
        if (D) Log.d(TAG, "doDiscovery()");
        //setProgressBarIndeterminateVisibility(true);
        //setName(R.string.scanning);
        tvTitle.setText(getString(R.string.scanning));
        progress.setVisibility(View.VISIBLE);
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        int intStartCount = 0;
        while (!mBtAdapter.startDiscovery() && intStartCount < 5) {
            intStartCount++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
        }
    }

    public OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            boolean hasConnected = false;
            try {
                if (mBtAdapter.isDiscovering()) {
                    mBtAdapter.cancelDiscovery();
                }

                String info = ((TextView) v).getText().toString();
                toothAddress = info.substring(info.length() - 17);
                if (!toothAddress.contains(":")) {
                    return;
                }

                hasConnected = ConnectDevice();
                if (hasConnected) {
                    DisConnect();
                }

                Intent intent = new Intent();
                intent.putExtra("is_connected", (hasConnected) ? "OK" : "NO");
                intent.putExtra("BTAddress", toothAddress);
                setResult(10, intent);
                finish();
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            } finally {
                finish();
            }
        }
    };

    public final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            BluetoothDevice device = null;
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                    if (device.getBluetoothClass().getMajorDeviceClass() == 1536) {
                        if (!strAddressList.contains(device.getAddress())) {
                            strAddressList += device.getAddress() + ",";
                            mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                        }
                    }
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        break;
                    case BluetoothDevice.BOND_NONE:
                    default:
                        break;
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //setProgressBarIndeterminateVisibility(false);
                //setName(R.string.select_device);
                tvTitle.setText(getString(R.string.select_device));
                progress.setVisibility(View.INVISIBLE);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                }
            }
        }
    };

    private boolean ConnectDevice() {
        boolean bRet = false;
        boolean isOldVersion = false;

        if (Build.VERSION.SDK_INT < 15) {
            isOldVersion = true;
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }

        try {
            mmDevice = mBtAdapter.getRemoteDevice(toothAddress);
            if (isOldVersion) {
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
            } else {
                mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            }
			
			/*try {  
	            // ���ӽ���֮ǰ�������  
	            if (mmDevice.getBondState() == BluetoothDevice.BOND_NONE) {  
	                Method creMethod = BluetoothDevice.class  
	                        .getMethod("createBond");  
	                Log.e("TAG", "��ʼ���");  
	                creMethod.invoke(mmDevice);  
	            } else {  
	            }  
	        } catch (Exception e) {
                                   FirebaseCrashlytics.getInstance().setCustomKey("user_tell",BaseActivity.getPrefname() +"_"+ BaseActivity.getPrefTell());
FirebaseCrashlytics.getInstance().setCustomKey("user_tell",BaseActivity.getPrefname() +"_"+ BaseActivity.getPrefTell());
FirebaseCrashlytics.getInstance().recordException(e);
	            // TODO: handle exception  
	            //DisplayMessage("�޷���ԣ�");  
	            e.printStackTrace();  
	        }  */

            mBtAdapter.cancelDiscovery();
            Thread.sleep(200);
            if (mBtAdapter.isDiscovering()) {
                int iCnt = 0;
                while (iCnt < 5) {
                    Thread.sleep(500);
                    iCnt++;
                    if (mBtAdapter.cancelDiscovery()) {
                        break;
                    }
                }
            }
            mmSocket.connect();
            bRet = GetIOInterface();
            return bRet;
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            try {
                Method m;
                m = mmDevice.getClass().getMethod("createRfcommSocket", int.class);
                mmSocket = (BluetoothSocket) m.invoke(mmDevice, 1);
                if (mBtAdapter.isDiscovering()) {
                    int iCnt = 0;
                    while (iCnt < 5) {
                        Thread.sleep(500);
                        iCnt++;
                        if (mBtAdapter.cancelDiscovery()) {
                            break;
                        }
                    }
                }

                mmSocket.connect();
                bRet = GetIOInterface();
                return bRet;
            } catch (Exception e1) {
                FirebaseCrashlytics.getInstance().recordException(e1);
                if (mmDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Toast.makeText(thisCon, R.string.BondError, Toast.LENGTH_LONG).show();
                }
                Log.d("PRTLIB", (new StringBuilder("BTO_ConnectDevice --> create ")).append(e1.getMessage()).toString());
                return false;
            }
        }
    }

    public boolean ConnectToDevice() {
        boolean hasConnected = false;
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        hasConnected = ConnectDevice();
        if (hasConnected) {
            DisConnect();
        }
        return hasConnected;
    }

    private boolean DisConnect() {
        boolean bRet = true;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        try {
            if (mmInStream != null) {
                mmInStream.close();
                mmInStream = null;
            }
            if (mmOutStream != null) {
                mmOutStream.close();
                mmOutStream = null;
            }
            mmSocket.close();
            mmSocket = null;
            mmDevice = null;
        } catch (IOException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            System.out.println((new StringBuilder(
                    "BTO_ConnectDevice close ")).append(e.getMessage())
                    .toString());
            bRet = false;
        }
        return bRet;
    }

    private boolean GetIOInterface() {
        Log.d("PRTLIB", "BTO_GetIOInterface...");
        try {
            mmInStream = mmSocket.getInputStream();
            mmOutStream = mmSocket.getOutputStream();
        } catch (IOException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell_databaseid", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell() + "_" + BaseActivity.getPrefDatabaseId());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.d("PRTLIB", (new StringBuilder("BTO_GetIOInterface ")).append(e.getMessage()).toString());
            return false;
        }
        return true;
    }
}


