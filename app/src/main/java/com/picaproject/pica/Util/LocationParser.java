package com.picaproject.pica.Util;

import android.location.Address;

import com.google.android.gms.maps.model.Marker;
import com.picaproject.pica.Item.PicPlaceData;
import com.picaproject.pica.Item.PicPlaceDataWrapper;

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
    public static PicPlaceDataWrapper placeToPicPlaceDataWrapper(Place p, Marker item){
        PicPlaceData data = new PicPlaceData();
        data.setName(p.getName());
        data.setLatitude(p.getLatitude());
        data.setLongitude(p.getLongitude());
        PicPlaceDataWrapper wrapper = new PicPlaceDataWrapper(data);
        wrapper.setMarker(item);
        return wrapper;
    }
    // place를 PlaceData로 변환
    // 여기선 마커가 없음.
    public static ArrayList<PicPlaceDataWrapper> addressToPicPlaceDataWrapper(List<Address> addresses){
        ArrayList<PicPlaceDataWrapper> result = new ArrayList<>();
        for(Address a : addresses){
            PicPlaceData data = new PicPlaceData();
            data.setName(a.getAddressLine(0).toString());
            data.setLatitude(a.getLatitude());
            data.setLongitude(a.getLongitude());
            PicPlaceDataWrapper wrapper = new PicPlaceDataWrapper(data);
            result.add(wrapper);
        }
        return result;
    }
}
