package com.mahak.order;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mahak.order.common.Customer;
import com.mahak.order.common.Order;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.mission.Mission;
import com.mahak.order.mission.MissionDetail;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.widget.FontPopUp;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
import java.util.Date;


public class MissionDetailMoreInfo extends BaseActivity {

    private static final int REQUEST_PHONE_PERMISSION = 300;
    private Context mContext;
    private MissionDetail missionDetail = new MissionDetail();
    private DbAdapter db;

    private TextView tvPageTitle;
    private Bundle Extras;
    private int missionDetailId;

    TextView tvName , tvMarketName , tvAddress , tvRemained , tvStatus;
    Button btnCall , btnAddReceipt , btnAddOrder , btnAddInvoice , btnTransaction , btnDeliveryOrder;
    private boolean hasPhonePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_detail_info);

        hasPhonePermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED);

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________

        mContext = this;

        Extras = getIntent().getExtras();

        if (Extras != null) {
            missionDetailId = Extras.getInt("missionDetailId");
            tvPageTitle.setText("شناسه جزییات ماموریت : " + missionDetailId);
        }

        initialise();
        FillView();

    }//end of onCreate


    /**
     * Initializing Variables
     */
    private void initialise() {

        tvName = (TextView) findViewById(R.id.tvName);
        tvMarketName = (TextView) findViewById(R.id.tvMarketName);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvRemained = (TextView) findViewById(R.id.tvRemained);
        tvStatus = (TextView) findViewById(R.id.tvStatus);

        btnCall = (Button) findViewById(R.id.btnCall);
        btnCall.setCompoundDrawables(new IconicsDrawable(mContext, FontAwesome.Icon.faw_phone).color(Color.WHITE).sizeDp(30), null, null, null);
        btnAddReceipt = (Button) findViewById(R.id.btnAddReceipt);
        btnAddOrder = (Button) findViewById(R.id.btnAddOrder);
        btnAddInvoice = (Button) findViewById(R.id.btnAddInvoice);
        btnTransaction = (Button) findViewById(R.id.btnTransaction);
        btnDeliveryOrder = (Button) findViewById(R.id.btnDeliveryOrder);

        db = new DbAdapter(mContext);
    }

    /**
     * Read Receipts From Database And Fill Adapter
     */
    private void FillView() {
        db.open();
        double balance = 0;
        missionDetail = db.getMissionDetail(missionDetailId);
        Customer customer = db.getCustomerWithPersonId(missionDetail.getPersonId());
        tvName.setText(customer.getName());
        tvMarketName.setText(customer.getOrganization());
        tvAddress.setText(customer.getAddress());
        balance = customer.getBalance();
        if(balance > 0)
            tvStatus.setText("بستانکار");
        else if (balance < 0){
            tvStatus.setText("بدهکار");
            balance *= -1;
        }
        else
            tvStatus.setText("بی حساب");
        tvRemained.setText(ServiceTools.formatPrice(balance));

        try {
            SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
            if (mapFragment != null) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        double Latitude = customer.getLatitude();
                        double Longitude = customer.getLongitude();
                        if (Latitude != 0 || Longitude != 0) {
                            LatLng pos = new LatLng(Latitude, Longitude);
                            if (missionDetail.getDescription() != null)
                                googleMap.addMarker(new MarkerOptions().position(pos).title(missionDetail.getDescription()));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15), 3000, new GoogleMap.CancelableCallback() {
                                @Override
                                public void onFinish() {
                                }

                                @Override
                                public void onCancel() {
                                }
                            });
                        } else {
                            LatLng pos = new LatLng(ProjectInfo.DEFAULT_LATITUDE, ProjectInfo.DEFAULT_LONGITUDE);
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 4));
                        }
                    }
                });
            }

        } catch (Exception e) {
            ServiceTools.logToFireBase(e);
            e.printStackTrace();

        }

        btnCall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!hasPhonePermission) {
                    ActivityCompat.requestPermissions(MissionDetailMoreInfo.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_PHONE_PERMISSION);
                }else{
                    String Mobile = customer.getMobile();
                    String Tell = customer.getTell();

                    Intent intent = new Intent(Intent.ACTION_CALL);
                    if (Mobile.equals("") && Tell.equals(""))
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.str_message_call), Toast.LENGTH_SHORT).show();
                    else {
                        if (!Mobile.equals(""))
                            intent.setData(Uri.parse("tel:" + Mobile.trim()));
                        else
                            intent.setData(Uri.parse("tel:" + Tell.trim()));
                        startActivity(intent);
                    }
                }


            }
        });

        btnAddOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                intent.putExtra(PAGE, BaseActivity.PAGE_CHECKLIST);
                intent.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_ORDER);
                intent.putExtra(MODE_PAGE, BaseActivity.MODE_NEW);
                startActivity(intent);

            }
        });

        btnAddInvoice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
                intent.putExtra(PAGE, BaseActivity.PAGE_CHECKLIST);
                intent.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_INVOCIE);
                intent.putExtra(MODE_PAGE, BaseActivity.MODE_NEW);
                startActivity(intent);

            }
        });

        btnAddReceipt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ManageReceiptActivity.class);
                intent.putExtra(PAGE, BaseActivity.PAGE_CHECKLIST);
                intent.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                intent.putExtra(MODE_PAGE, BaseActivity.MODE_NEW);
                startActivity(intent);

            }
        });
        
        btnTransaction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TransactionsActivity.class);
                intent.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                startActivity(intent);
            }
        });

        btnDeliveryOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Order deliveryorder = db.getDeliveryOrderCustomr(customer.getPersonId());
                if (deliveryorder.getOrderId() != 0) {
                    Intent intent = new Intent(mContext, DeliveryOrderDetailActivity.class);
                    intent.putExtra(BaseActivity.ID, deliveryorder.getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, getString(R.string.str_message_get_delivery_order), Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_PHONE_PERMISSION:
                if (grantResults.length > 0 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasPhonePermission = true;
                }
                break;
        }
    }
}
