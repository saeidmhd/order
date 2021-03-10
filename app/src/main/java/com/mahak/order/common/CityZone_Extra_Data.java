package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityZone_Extra_Data {


    @SerializedName("ZoneCode")
    @Expose
    private long zoneCode;
    @SerializedName("ParentCode")
    @Expose
    private int parentCode;
    @SerializedName("ZoneName")
    @Expose
    private String zoneName;


    public long getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(long zoneCode) {
        this.zoneCode = zoneCode;
    }

    public int getParentCode() {
        return parentCode;
    }

    public void setParentCode(int parentCode) {
        this.parentCode = parentCode;
    }

    /*public String getZoneName() {
        String zoneNameStr = "";
        try {
            byte[] utf8Bytes = zoneName.getBytes("UTF-8");
            zoneNameStr = new String(utf8Bytes, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return zoneNameStr;
    }*/

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZoneName() {
        return zoneName;
    }
}
