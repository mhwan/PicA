package com.picaproject.pica.Item;

import java.io.Serializable;

public class PicPlaceData implements Serializable {
    // 직렬화 버전 표시 : https://lktprogrammer.tistory.com/150
    private static final long serialVersionUID = 1L;

    private String name;
    private double latitude;
    private double longitude;

    public PicPlaceData(String name) {
        this.name = name;
    }

    public PicPlaceData(){}

    public PicPlaceData(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
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
}
