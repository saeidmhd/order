package com.mahak.order.common;

import android.os.Parcel;
import android.os.Parcelable;

public class Coordinate implements Parcelable {

    double Latitude;
    double Longitude;

    public Coordinate() {
        Latitude = 0;
        Longitude = 0;
    }

    public Coordinate(Parcel in) {
        Latitude = in.readDouble();
        Longitude = in.readDouble();
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(Latitude);
        dest.writeDouble(Longitude);
    }

    public static final Creator<Coordinate> CREATOR = new Creator<Coordinate>() {
        public Coordinate createFromParcel(Parcel in) {
            return new Coordinate(in);
        }

        public Coordinate[] newArray(int size) {
            return new Coordinate[size];
        }
    };

}
