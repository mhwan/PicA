package com.picaproject.pica.Item;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.picaproject.pica.Util.AppUtility;

import java.io.Serializable;

public class PicPlaceData implements Serializable, Parcelable {
    // 직렬화 버전 표시 : https://lktprogrammer.tistory.com/150
    private static final long serialVersionUID = 1L;

    private String name;
    private double latitude = AppUtility.IMAGE_HAS_NO_LOCATION;
    private double longitude = AppUtility.IMAGE_HAS_NO_LOCATION;

    public PicPlaceData(String name) {
        this.name = name;
    }

    public PicPlaceData(){}

    public PicPlaceData(LatLng latLng) {
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
        this.name = "";
    }

    public PicPlaceData(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = "";
    }

    protected PicPlaceData(Parcel in) {
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<PicPlaceData> CREATOR = new Creator<PicPlaceData>() {
        @Override
        public PicPlaceData createFromParcel(Parcel in) {
            return new PicPlaceData(in);
        }

        @Override
        public PicPlaceData[] newArray(int size) {
            return new PicPlaceData[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatLng(LatLng latLng) {
        setLatitude(latLng.latitude);
        setLongitude(latLng.longitude);
    }

    public LatLng getLatLng(){
        return new LatLng(latitude, longitude);
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "PicPlaceData{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
