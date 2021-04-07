package com.mahak.order.tracking;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.tracking.visitorZone.Zone;
import com.mahak.order.tracking.visitorZone.ZoneLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapPolygon {

    GoogleMap mGoogleMap;
    DbAdapter db;
    Context mContext;

    public MapPolygon(GoogleMap googleMap , Context context){
        mGoogleMap = googleMap;
        mContext = context;
        db = new DbAdapter(mContext);
    }

    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;
    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;

    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);

    private static final int PATTERN_GAP_LENGTH_PX = 10;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dot.
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);

    // Create a stroke pattern of a gap followed by a dash.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private static final List<PatternItem> PATTERN_POLYGON_BETA =
            Arrays.asList(DOT, GAP, DASH, GAP);



    private void stylePolygon(Polygon polygon) {
        String type = "";
        // Get the data object stored with the polygon.
        if (polygon.getTag() != null) {
            type = polygon.getTag().toString();
        }

        List<PatternItem> pattern = PATTERN_POLYGON_ALPHA;
        int strokeColor = COLOR_BLACK_ARGB;
        int fillColor = COLOR_PURPLE_ARGB;

        pattern = PATTERN_POLYGON_ALPHA;
        strokeColor = COLOR_GREEN_ARGB;
        fillColor = COLOR_PURPLE_ARGB;

        /*switch (type) {
            // If no type is given, allow the API to use the default.
            case "alpha":
                // Apply a stroke pattern to render a dashed line, and define colors.
                pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = COLOR_GREEN_ARGB;
                fillColor = COLOR_PURPLE_ARGB;
                break;
            case "beta":
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_BETA;
                strokeColor = COLOR_ORANGE_ARGB;
                fillColor = COLOR_BLUE_ARGB;
                break;
        }*/

        polygon.setStrokePattern(pattern);
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon.setStrokeColor(strokeColor);
        polygon.setFillColor(fillColor);
    }

    public List<LatLng> getPolygonPoints(Zone zone){
        List<LatLng> polygonPoints = new ArrayList<>();
        ArrayList<ZoneLocation> zoneLocations = db.getAllZoneLocation(zone.getId());
        for (ZoneLocation zoneLocation : zoneLocations){
            polygonPoints.add(new LatLng(zoneLocation.getLatitude(), zoneLocation.getLongitude()));
        }
        return polygonPoints;
    }

    public void showPolygon() {
        db.open();
        ArrayList<Zone> zones = db.getAllZone();
        for(Zone zone : zones){
            List<LatLng> polygonPoints = getPolygonPoints(zone);
            if(polygonPoints.size()>0){
                Polygon polygon = mGoogleMap.addPolygon(new PolygonOptions()
                        .clickable(true)
                        .addAll(polygonPoints));
                polygon.setTag("alpha");
                stylePolygon(polygon);
            }

        }
    }
    public boolean checkPositionInZone(LatLng position) {
        if(position == null)
            return false;
        db.open();
        ArrayList<Zone> zones = db.getAllZone();
        for(Zone zone : zones){
            List<LatLng> polygonPoints = getPolygonPoints(zone);
            if(polygonPoints.size()>0){
                if(PolyUtil.containsLocation(position, polygonPoints, true))
                    return true;
            }
        }
        return false;
    }

}
