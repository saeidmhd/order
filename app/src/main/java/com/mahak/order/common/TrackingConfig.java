package com.mahak.order.common;

/**
 * Created by Saeid.mhd-at-Gmail.com on 12/2/17.
 */

public class TrackingConfig {


    public static String TAG_AdminControl = "AdminControl";
    public static String TAG_TrackingControl = "TrackingControl";

    private int adminControl;
    private int trackingControl;

    public int getAdminControl() {
        return adminControl;
    }

    public void setAdminControl(int adminControl) {
        this.adminControl = adminControl;
    }

    public int getTrackingControl() {
        return trackingControl;
    }

    public void setTrackingControl(int trackingControl) {
        this.trackingControl = trackingControl;
    }
}
