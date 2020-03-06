package com.picaproject.pica.Item;


import com.google.android.gms.maps.model.Marker;

/*
* PicPlaceData를 포함하여 부가적인 데이터를 넣는 Wrapper
* PicPlaceData 자체는 다른 액티비티와 정보교환을 위한 클래스로 Maker와 같은 클래스를 넣을수없고
* 오직 직렬화가 가능한 데이터만 넣을수있다.
* 리스트뷰와 연결되는 클래스
* */
public class PicPlaceDataWrapper {
    PicPlaceData picPlaceData;
    Marker marker;

    public PicPlaceDataWrapper(PicPlaceData picPlaceData) {
        this.picPlaceData = picPlaceData;
    }
    // Maker은 NULL일수있음.
    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public PicPlaceData getPicPlaceData() {
        return picPlaceData;
    }

    public Marker getMarker() {
        return marker;
    }
}
