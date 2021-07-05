package com.mahak.order;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.common.CheckList;
import com.mahak.order.common.Customer;
import com.mahak.order.common.Order;
import com.mahak.order.common.Person_Extra_Data;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import org.osmdroid.ResourceProxy;
import org.osmdroid.views.MapController;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.Date;

import static com.mahak.order.BaseActivity.CUSTOMERID_KEY;
import static com.mahak.order.BaseActivity.MODE_PAGE;
import static com.mahak.order.BaseActivity.PAGE;
import static com.mahak.order.BaseActivity.TYPE_KEY;

//import com.mahak.order.common.DeliveryOrder;

public class CheckListDetailFragment extends Fragment {

    //private MapView mapview;
    private ItemizedOverlay<OverlayItem> locationoverlay;
    public static ResourceProxy RESOURCEPROXY;
    public MapController mapcontroller;
    public static int MAP_DEFAULT_ZOOM = 14;
    double amount = 0;
    String Shift = "";
    String Mobile = "";
    String Tell = "";
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;
    private DbAdapter db;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static CheckListDetailFragment create(int pageNumber) {
        CheckListDetailFragment fragment = new CheckListDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public CheckListDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout containing a title and body text.
        db = new DbAdapter(getActivity());
        db.open();
        final CheckList checklist = DashboardActivity.arrayChecklist.get(mPageNumber);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_check_list_detail, container, false);
        //mapview = (MapView)rootView.findViewById(R.id.mpvCheckList);
        TextView tvName = (TextView) rootView.findViewById(R.id.tvName);
        TextView tvMarketName = (TextView) rootView.findViewById(R.id.tvMarketName);
        TextView tvAddress = (TextView) rootView.findViewById(R.id.tvAddress);
        TextView tvShift = (TextView) rootView.findViewById(R.id.tvShift);
        TextView tvRemained = (TextView) rootView.findViewById(R.id.tvRemained);
        TextView tvStatus = (TextView) rootView.findViewById(R.id.tvStatus);

        Button btnCall = (Button) rootView.findViewById(R.id.btnCall);
        btnCall.setCompoundDrawables(new IconicsDrawable(getActivity(), FontAwesome.Icon.faw_phone).color(Color.WHITE).sizeDp(30), null, null, null);
        Button btnAddReceipt = (Button) rootView.findViewById(R.id.btnAddReceipt);
        Button btnAddOrder = (Button) rootView.findViewById(R.id.btnAddOrder);
        Button btnAddInvoice = (Button) rootView.findViewById(R.id.btnAddInvoice);
        Button btnTransaction = (Button) rootView.findViewById(R.id.btnTransaction);
        Button btnDeliveryOrder = (Button) rootView.findViewById(R.id.btnDeliveryOrder);

        final CheckBox chkChecklist = (CheckBox) rootView.findViewById(R.id.chkCheckListChecked);

        //////////////////////////////////////// Map init
        try {
            SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
            if (mapFragment != null) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        Double Latitude = checklist.getLatitude();
                        Double Longitude = checklist.getLongitude();
                        if (Latitude != 0 || Longitude != 0) {
                            LatLng pos = new LatLng(Latitude, Longitude);
                            if (checklist.getName() != null)
                                googleMap.addMarker(new MarkerOptions().position(pos).title(checklist.getName()));
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
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();

        }
        /////////////////////////////////////////

        int CustomerId = checklist.getPersonId();
        Customer customer = new Customer();
        if (CustomerId == ProjectInfo.CUSTOMERID_GUEST) {
            customer.setName(getActivity().getString(R.string.str_guest_customer));
            customer.setOrganization(getActivity().getString(R.string.str_guest_market_customer));
        } else {
            customer = db.getCustomerWithPersonId(checklist.getPersonId());
            if (customer != null) {

                amount = customer.getBalance();

                Shift = customer.getShift();
                Mobile = customer.getMobile();
                Tell = customer.getTell();
            }
        }

        if (checklist.getName() != null)
            if (!checklist.getName().equals(""))
                tvName.setText(checklist.getName());
        else
            tvName.setVisibility(View.GONE);

        if (checklist.getMarketName() != null) {
            if (!checklist.getMarketName().equals("")) {
                tvMarketName.setText(checklist.getMarketName());
            }
        } else
            tvMarketName.setVisibility(View.GONE);

        tvAddress.setText(checklist.getAddress());
        tvShift.setText(Shift);


        if (amount == 0) {
            tvStatus.setText(getActivity().getResources().getString(R.string.str_incalculable));
            tvRemained.setText(ServiceTools.formatPrice(amount));
        }
        if (amount < 0) {
            amount = amount * -1;
            tvRemained.setText(ServiceTools.formatPrice(amount));
            tvStatus.setText(getActivity().getResources().getString(R.string.str_debitor));
        } else if (amount > 0) {
            tvRemained.setText(ServiceTools.formatPrice(amount));
            tvStatus.setText(getActivity().getResources().getString(R.string.str_creditor));
        }

        if (checklist.getStatus() == ProjectInfo.STATUS_DO)
            chkChecklist.setChecked(true);
        else if (checklist.getStatus() != ProjectInfo.STATUS_DO)
            chkChecklist.setChecked(false);

        //Fill MapView______________________________________________________________________________
        /*double Latitude,Longitude;
        Latitude = ServiceTools .toDouble(checklist.getLatitude());
        Longitude = ServiceTools .toDouble(checklist.getLongitude());
        
        RESOURCEPROXY = new DefaultResourceProxyImpl(getActivity());
		mapview.setUseDataConnection(true);
		mapcontroller = (MapController) mapview.getController();
		mapview.setBuiltInZoomControls(true);
		mapview.setMultiTouchControls(true);
		mapcontroller.setZoom(15);
		if(Latitude != 0 && Longitude != 0)
		{
			mapview.getController().setCenter(new GeoPoint(ServiceTools .toDouble(checklist.getLatitude()),ServiceTools.toDouble(checklist.getLongitude())));
			ShowPosition(ServiceTools .toDouble(checklist.getLatitude()),ServiceTools .toDouble(checklist.getLongitude()));
		}
		else{
			GPSTracker gpsTracker = new GPSTracker(getActivity());
			double CurrentLatitude,CurrentLongitude;
			if(gpsTracker.canGetLocation())
			{
				CurrentLatitude = gpsTracker.getLatitude();
				CurrentLongitude = gpsTracker.getLongitude();
			    mapview.getController().setCenter(new GeoPoint(CurrentLatitude,CurrentLongitude));
			}
			else{
				mapcontroller.setZoom(5);	
				mapview.getController().setCenter(new GeoPoint(ProjectInfo.DEFAULT_LATITUDE,ProjectInfo.DEFAULT_LONGITUDE));}
		}*/

        //_______________________________________________________________________________

        btnCall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_CALL);
                if (Mobile.equals("") && Tell.equals(""))
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.str_message_call), Toast.LENGTH_SHORT).show();
                else {
                    if (!Mobile.equals(""))
                        intent.setData(Uri.parse("tel:" + Mobile.trim()));
                    else
                        intent.setData(Uri.parse("tel:" + Tell.trim()));
                    startActivity(intent);
                }
            }
        });

        btnAddOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InvoiceDetailActivity.class);
                intent.putExtra(PAGE, BaseActivity.PAGE_CHECKLIST);
                intent.putExtra(CUSTOMERID_KEY, checklist.getPersonId());
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_ORDER);
                intent.putExtra(MODE_PAGE, BaseActivity.MODE_NEW);
                startActivity(intent);

            }
        });

        btnAddInvoice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InvoiceDetailActivity.class);
                intent.putExtra(PAGE, BaseActivity.PAGE_CHECKLIST);
                intent.putExtra(CUSTOMERID_KEY, checklist.getPersonId());
                intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_INVOCIE);
                intent.putExtra(MODE_PAGE, BaseActivity.MODE_NEW);
                startActivity(intent);

            }
        });

        btnAddReceipt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ManageReceiptActivity.class);
                intent.putExtra(PAGE, BaseActivity.PAGE_CHECKLIST);
                intent.putExtra(CUSTOMERID_KEY, checklist.getPersonId());
                intent.putExtra(MODE_PAGE, BaseActivity.MODE_NEW);
                startActivity(intent);

            }
        });

        chkChecklist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Boolean isChecked = chkChecklist.isChecked();
                if (isChecked) {
                    checklist.setStatus(ProjectInfo.STATUS_DO);
                    checklist.setModifyDate(new Date().getTime());
                    if (db.UpdateCheckList(checklist))
                        DashboardActivity.arrayChecklist.get(mPageNumber).setStatus(ProjectInfo.STATUS_DO);

                } else {
                    checklist.setStatus(ProjectInfo.STATUS_NOT_DO);
                    checklist.setModifyDate(new Date().getTime());
                    if (db.UpdateCheckList(checklist))
                        DashboardActivity.arrayChecklist.get(mPageNumber).setStatus(ProjectInfo.STATUS_NOT_DO);
                }

            }
        });

        btnTransaction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TransactionsActivity.class);
                intent.putExtra(CUSTOMERID_KEY, checklist.getPersonId());
                startActivity(intent);
            }
        });

        btnDeliveryOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Order deliveryorder = db.getDeliveryOrderCustomr(checklist.getPersonId());
                if (deliveryorder.getOrderId() != 0) {
                    Intent intent = new Intent(getActivity(), DeliveryOrderDetailActivity.class);
                    intent.putExtra(BaseActivity.ID, deliveryorder.getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.str_message_get_delivery_order), Toast.LENGTH_LONG).show();
                }

            }
        });

        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
    
    /*public void ShowPosition(Double Latitude , Double Longitude ) {

    	
		mapview.getOverlays().clear();
		final ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
		Drawable drawPin;
		drawPin = this.getResources().getDrawable(R.drawable.ic_pin);
		
		GeoPoint geopoint = new GeoPoint(Latitude, Longitude);
		OverlayItem olItem = new OverlayItem("", "", geopoint);
		olItem.setMarker(drawPin);
		items.add(olItem);
		mapcontroller.animateTo(geopoint);
		
		this.locationoverlay = new ItemizedIconOverlay<OverlayItem>(items,new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
			public boolean onItemSingleTapUp(final int index,final OverlayItem item) {

				return true; // We 'handled' this event.
			}

			public boolean onItemLongPress(final int index,	final OverlayItem item) {
				return false;
			}
		}, CheckListDetailFragment.RESOURCEPROXY);
		this.mapview.getOverlays().add(this.locationoverlay);
		mapview.postInvalidate();
	}*/

}
