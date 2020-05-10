package com.picaproject.pica.Util;

import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.picaproject.pica.Item.ContactItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class AppUtility {
    private static AppUtility instance;

    private AppUtility() {
    }

    public synchronized static AppUtility getAppinstance() {
        if (instance == null)
            instance = new AppUtility();
        return instance;
    }

    public ArrayList<ContactItem> getContactList(Context context) {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts._ID
        };

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";

        Cursor cursor = context.getContentResolver().query(uri, projection, null,
                null, sortOrder);

        LinkedHashSet<ContactItem> hashlist = new LinkedHashSet<>();
        Log.d("contacts Count!", cursor.getCount()+"");
        if (cursor.moveToFirst()) {
            do {
                long person_id = cursor.getLong(2);

                ContactItem contactItem = new ContactItem();
                contactItem.setPhoneNumber(cursor.getString(0));
                contactItem.setName(cursor.getString(1));
                contactItem.setPersonId(person_id);

                hashlist.add(contactItem);
            } while (cursor.moveToNext());
        }

        ArrayList<ContactItem> contactItems = new ArrayList<>(hashlist);
        //Collections.sort(contactItems);

        for (int i = 0; i < contactItems.size(); i++) {
            contactItems.get(i).setId(i);
        }
        return contactItems;
    }

    public String getAddress(double lat, double lng) {
        String nowAddress ="주소를 알 수가 없습니다.";
        Log.d("Apputility-getAddress", lat+", "+lng);
        Geocoder geocoder = new Geocoder(AppContext.getContext(), Locale.getDefault());
        List<Address> address;
        try {
            if (geocoder != null) {
                //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress  = currentLocationAddress;
                }
            }
        } catch (IOException e) {
            nowAddress = "주소를 가져오는데 실패했습니다.";
            e.printStackTrace();
        }
        return nowAddress;
    }

    public int getTextByte(String s){
        int en = 0, kr = 0, etc = 0;
        char[] string = s.toCharArray();

        for (int i = 0; i < s.length(); i++) {
            if (string[i] >= 'A' && string[i] <= 'z')
                en++;
            else if (string[i] >= '\uAC00' && string[i] <= '\uD7A3')
                kr += 2;
            else
                etc++;
        }
        return en + kr + etc;
    }

    public LatLng getDefaultLocation(){
        return new LatLng(37.5647689,126.9720057);
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) AppContext.getContext().getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
