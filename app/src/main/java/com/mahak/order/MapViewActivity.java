package com.mahak.order;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.mahak.order.common.Customer;
import com.mahak.order.common.GPSTracker;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.gpsTracking.ClusterPoint;
import com.mahak.order.gpsTracking.GpsTracking;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;
import java.util.List;

public class MapViewActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private ArrayList<LatLng> positions = new ArrayList<>();
    private ArrayList<LatLng> customerPositions = new ArrayList<>();
    private PolylineOptions polylineOptions;
    private Marker marker;
    private Polyline polyline;
    private Marker mSelectedMarker;
    private DbAdapter db;
    private ArrayList<Customer> customers = new ArrayList<>();
    private ClusterManager<ClusterPoint> clusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        db = new DbAdapter(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            positions = extras.getParcelableArrayList(COORDINATE);
            customerPositions = extras.getParcelableArrayList(CustomerPositions);
        }

        db.open();
        //customers = db.getAllOfCustomer();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {

                mMap = googleMap;

                // Add Customer markers to the map.
                // addMarkersToMap();

                // Set listener for marker click event.  See the bottom of this class for its behavior.
                mMap.setOnMarkerClickListener(MapViewActivity.this);

                // Set listener for map click event.  See the bottom of this class for its behavior.
                mMap.setOnMapClickListener(MapViewActivity.this);

                // Override the default content description on the view, for accessibility mode.
                // Ideally this string would be localized.
                googleMap.setContentDescription("");

                GPSTracker gpsTracker = new GPSTracker(mContext);
                double _Latitude, _Longitude;
                for (int i = 0; i < positions.size(); i++) {
                    mMap.addMarker(new MarkerOptions().position(positions.get(i)));
                }
                if (positions.size() > 0) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positions.get(0), 10));
                } else if (gpsTracker.canGetLocation()) {
                    _Latitude = gpsTracker.getLatitude();
                    _Longitude = gpsTracker.getLongitude();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(_Latitude, _Longitude), 10));
                } else {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ProjectInfo.DEFAULT_LATITUDE, ProjectInfo.DEFAULT_LONGITUDE), 8));
                }
                initMap();
                mMap.getUiSettings().setZoomControlsEnabled(true);

                GpsTracking.addEventLocation(MapViewActivity.this.getLocalClassName(), new GpsTracking.EventLocation() {
                    @Override
                    public void onReceivePoint(final Location location) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (location == null) {
                                    return;
                                }
                                LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                                if (polyline != null) {
                                    List<LatLng> points = polyline.getPoints();
                                    points.add(position);
                                    polyline.setPoints(points);
                                }
                                if (marker != null) {
                                    marker.remove();
                                }
                                marker = googleMap.addMarker(new MarkerOptions().position(position));
                                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_visitor_3));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, googleMap.getCameraPosition().zoom));
                            }
                        });
                    }
                });
                // setUpClusterer();
            });
        }


    }

    private void setUpClusterer() {
        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        clusterManager = new ClusterManager<ClusterPoint>(mContext, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = 51.5145160;
        double lng = -0.1270060;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            ClusterPoint offsetItem = new ClusterPoint(lat, lng, "Title " + i, "Snippet " + i);
            clusterManager.addItem(offsetItem);
        }
    }

    private void addMarkersToMap() {

        for (Customer customer : customers) {
            if (customer.getOrderCount() > 0) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(ServiceTools.RegulartoDouble(customer.getLatitude()), ServiceTools.RegulartoDouble(customer.getLongitude())))
                        .title(customer.getName())
                        .snippet(getString(R.string.order_has_been_registered))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            } else {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(ServiceTools.RegulartoDouble(customer.getLatitude()), ServiceTools.RegulartoDouble(customer.getLongitude())))
                        .title(customer.getName())
                        .snippet(getString(R.string.order_has_not_been_registered))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }


    }

    @Override
    protected void onDestroy() {
        GpsTracking.removeEventLocation(this.getLocalClassName());
        super.onDestroy();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
    }

    private void initMap() {
        if (polyline != null)
            polyline.remove();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(2);
        polylineOptions.color(Color.RED);
        polylineOptions.visible(true);
        polyline = mMap.addPolyline(polylineOptions);
        marker = new GpsTracking(getBaseContext()).drawGoogleMap(mMap, marker, polyline);
    }

    @Override
    protected void onResume() {
        if (mMap != null)
            initMap();
        super.onResume();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Any showing info window closes when the map is clicked.
        // Clear the currently selected marker.
        mSelectedMarker = null;

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // The user has re-tapped on the marker which was already showing an info window.
        if (marker.equals(mSelectedMarker)) {
            // The showing info window has already been closed - that's the first thing to happen
            // when any marker is clicked.
            // Return true to indicate we have consumed the event and that we do not want the
            // the default behavior to occur (which is for the camera to move such that the
            // marker is centered and for the marker's info window to open, if it has one).
            mSelectedMarker = null;
            return true;
        }

        mSelectedMarker = marker;


        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur.
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}
