package com.mahak.order.tracking;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.RadaraDb;
import com.mahak.order.tracking.visitorZone.Datum;
import com.mahak.order.tracking.visitorZone.ZoneLocation;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapPolygon implements GoogleMap.OnPolygonClickListener {

    GoogleMap mGoogleMap;
    RadaraDb radaraDb;
    Context mContext;

    public MapPolygon(GoogleMap googleMap , Context context){
        mGoogleMap = googleMap;
        mContext = context;
        radaraDb = new RadaraDb(mContext);
    }

    private static final int COLOR_Fill_Color = 0x77B9BAAc;
    private static final int COLOR_Stroke = 0xff5E5F51;

    private static final int POLYGON_STROKE_WIDTH_PX = 4;
    private static final int PATTERN_DASH_LENGTH_PX = 10;

    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);

    private static final int PATTERN_GAP_LENGTH_PX = 10;
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    private static final List<Polygon> polygons = new ArrayList<>();

    private void stylePolygon(Polygon polygon) {

        polygon.setStrokePattern(PATTERN_POLYGON_ALPHA);
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon.setStrokeColor(COLOR_Stroke);
        polygon.setFillColor(COLOR_Fill_Color);

        polygon.setClickable(true);
    }

    public List<LatLng> getPolygonPoints(Datum datum){
        List<LatLng> polygonPoints = new ArrayList<>();
        ArrayList<ZoneLocation> zoneLocations = radaraDb.getAllZoneLocation(datum.getZoneId());
        for (ZoneLocation zoneLocation : zoneLocations){
            polygonPoints.add(new LatLng(zoneLocation.getLatitude(), zoneLocation.getLongitude()));
        }
        return polygonPoints;
    }

    public void showPolygon() {
        radaraDb.open();
        ArrayList<Datum> data = radaraDb.getAllZone();
        for(Datum datum : data){
            List<LatLng> polygonPoints = getPolygonPoints(datum);
            if(polygonPoints.size() > 0){
                Polygon polygon = mGoogleMap.addPolygon(new PolygonOptions()
                        .clickable(true)
                        .addAll(polygonPoints));
                polygon.setClickable(true);
                polygons.add(polygon);
               // polygon.setTag(datum.getTitle());
                stylePolygon(polygon);
            }
        }
    }

    public boolean checkPositionInZone(LatLng position) {
        int radius = 0;
        String config = ServiceTools.getKeyFromSharedPreferences(mContext, ProjectInfo.pre_gps_config);
        if (!ServiceTools.isNull(config)) {
            try {
                JSONObject obj = new JSONObject(config);
                radius = obj.getInt(ProjectInfo._json_key_radius);
            } catch (Exception e) {
                ServiceTools.logToFireBase(e);
                e.printStackTrace();
            }
        }
        if(position == null)
            return false;
        radaraDb.open();
        ArrayList<Datum> data = radaraDb.getAllZone();
        for(Datum datum : data){
            List<LatLng> polygonPoints = getPolygonPoints(datum);
            if(polygonPoints.size() > 0){
                if(PolyUtil.isLocationOnEdge(position, polygonPoints, true, radius)){
                    return true;
                }
                if(PolyUtil.isLocationOnPath(position, polygonPoints, true, radius)){
                    return true;
                }
                if(PolyUtil.containsLocation(position, polygonPoints,true)){
                    return true;
                }

            }
        }
        return false;
    }

    @Override
    public void onPolygonClick(@NonNull Polygon polygon) {
        Toast.makeText(mContext, String.valueOf(polygon.getTag()), Toast.LENGTH_SHORT).show();
    }
}
