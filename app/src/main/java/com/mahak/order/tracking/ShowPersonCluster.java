package com.mahak.order.tracking;


import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.mahak.order.R;
import com.mahak.order.common.Customer;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;

public class ShowPersonCluster {
    private DbAdapter db;
    private ArrayList<Customer> customers = new ArrayList<>();
    private ClusterManager<ClusterPoint> clusterManager;
    private final Context mContext;
    private final GoogleMap mMap;

    public ShowPersonCluster(GoogleMap map,Context context){
        mContext = context;
        mMap = map;
        db = new DbAdapter(mContext);
    }

    class getPeopleTask extends AsyncTask<String, String, Integer> {
        @Override
        protected Integer doInBackground(String... arg0) {
            db.open();
            customers = db.getAllOfCustomer();
            db.close();
            return 0;
        }
        @Override
        protected void onPostExecute(Integer result) {
            if(result == 0){
                setUpClusterer();
            }
        }
    }

    public void showPeople(){
        if(customers != null)
            if(customers.size() == 0)
                new getPeopleTask().execute();
            else
                setUpClusterer();
    }

    private void setUpClusterer() {
        clusterManager = new ClusterManager<ClusterPoint>(mContext, mMap);
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);
        addItems();
    }

    private void addItems() {
        String snippet = "";

        for(Customer customer : customers){
            if (customer.getOrderCount() > 0) {
                snippet = mContext.getString(R.string.order_has_been_registered);
            }else {
                snippet = mContext.getString(R.string.order_has_not_been_registered);
            }
            ClusterPoint offsetItem = new ClusterPoint(customer.getLatitude(), customer.getLongitude(), customer.getName() + " ( " + customer.getPersonCode() + " ) " , snippet);
            clusterManager.addItem(offsetItem);
        }
    }

    private void addMarkersToMap() {
        for (Customer customer : customers) {
            if (customer.getOrderCount() > 0) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(customer.getLatitude(), customer.getLongitude()))
                        .title(customer.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            } else {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(customer.getLatitude(), customer.getLongitude()))
                        .title(customer.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }
    }

}
