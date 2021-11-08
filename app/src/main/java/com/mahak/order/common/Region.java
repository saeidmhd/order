package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Region {

    @SerializedName("CityID")
    @Expose
    private int cityID;
    @SerializedName("CityName")
    @Expose
    private String cityName;
    @SerializedName("ProvinceID")
    @Expose
    private int provinceID;
    @SerializedName("ProvinceName")
    @Expose
    private String provinceName;
    @SerializedName("MapCode")
    @Expose
    private String mapCode;
    @SerializedName("RowVersion")
    @Expose
    private int rowVersion;

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getMapCode() {
        return mapCode;
    }

    public void setMapCode(String mapCode) {
        this.mapCode = mapCode;
    }

    public int getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(int rowVersion) {
        this.rowVersion = rowVersion;
    }

}
