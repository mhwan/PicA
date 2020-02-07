package com.picaproject.pica.Util;

import android.location.Address;

import com.picaproject.pica.Item.PicPlaceData;

import java.util.ArrayList;
import java.util.List;

import noman.googleplaces.Place;


public class LocationParser {
    // place를 PlaceData로 변환
    public static ArrayList<PicPlaceData> placeToPicPlaceData(List<Place> places){
        ArrayList<PicPlaceData> result = new ArrayList<>();
        for(Place p : places){
            PicPlaceData data = new PicPlaceData();
            data.setName(p.getName());
            data.setLatitude(p.getLatitude());
            data.setLongitude(p.getLongitude());
            result.add(data);
        }
        return result;
    }
    // place를 PlaceData로 변환
    public static PicPlaceData placeToPicPlaceData(Place p){
        PicPlaceData data = new PicPlaceData();
        data.setName(p.getName());
        data.setLatitude(p.getLatitude());
        data.setLongitude(p.getLongitude());
        return data;
    }
    // place를 PlaceData로 변환
    public static ArrayList<PicPlaceData> addressToPicPlaceData(List<Address> addresses){
        ArrayList<PicPlaceData> result = new ArrayList<>();
        for(Address a : addresses){
            PicPlaceData data = new PicPlaceData();
            data.setName(a.getAddressLine(0).toString());
            data.setLatitude(a.getLatitude());
            data.setLongitude(a.getLongitude());
            result.add(data);
        }
        return result;
    }
}
