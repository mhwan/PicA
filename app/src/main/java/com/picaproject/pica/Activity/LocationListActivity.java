package com.picaproject.pica.Activity;

import android.Manifest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.picaproject.pica.CustomView.BottomSheetListView;
import com.picaproject.pica.CustomView.PicLocationListAdapter;
import com.picaproject.pica.IntentProtocol;
import com.picaproject.pica.Item.PicPlaceData;
import com.picaproject.pica.Item.PicPlaceDataWrapper;
import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.LocationParser;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;

import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

/**
 *
 * mFusedLocationClient 현재 위치정보를 받아오는데 사용하는것
 */
public class LocationListActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, PlacesListener {


    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetListView bottomSheetListView;
    // 사진 추가화면에서 전달된 사진 정보 1장 (여러장은 동시에 여기로 올수없음)
    private UploadPicData picData;

    // 가져온 주변장소 마커를 보관하는 리스트
    List<Marker> previous_marker = null;
    // 어댑터와 연결될 주변 장소 객체 보관 리스트
    List<PicPlaceDataWrapper> placeList = null;
    //
    PicLocationListAdapter adapter;
    private GoogleMap mMap;
    private Marker currentMarker = null;

    private static final String TAG = "test_hs";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초
    // 안드로이드 스튜디오 대문자 변환 = 드래그해서 컨트+쉬프트+U
    // 주변 위치 검색 거리 500 = 500미터
    private static final int PLACE_SERCH_DISTANCE = 1500;
    // onRequestPermissionsResult에서 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용됩니다.
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;


    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.
    private String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};  // 외부 저장소


    private Location mCurrentLocatiion;
    private LatLng currentPosition;


    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;
    private Handler handler;
    private Geocoder geocoder;
    private View mLayout;  // Snackbar 사용하기 위해서는 View가 필요합니다.
    private EditText serchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_location_list);
        handler = new Handler();
        previous_marker = new ArrayList<Marker>();
        placeList = new ArrayList<>();

        Intent intent = getIntent();

        picData = (UploadPicData)intent.getSerializableExtra(IntentProtocol.PIC_DATA_CLASS_NAME);

        mLayout = findViewById(R.id.layout_location_main);

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);


        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // android:id="@+id/search_text"
        //serchView = (EditText) findViewById(R.id.search_text);

        FrameLayout bottomsheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomsheet);

        bottomSheetListView = (BottomSheetListView) findViewById(R.id.map_listview);

        ActivityCallBack callBack = new ActivityCallBack();

        adapter = new PicLocationListAdapter(placeList,callBack);
        bottomSheetListView.setAdapter(adapter);


    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d(TAG, "onMapReady :");

        mMap = googleMap;
        geocoder = new Geocoder(this);

        setDefaultLocation();

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);



        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            startLocationUpdates(); // 3. 위치 업데이트 시작, 권한 관련


        }else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                        ActivityCompat.requestPermissions( LocationListActivity.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions( this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }



        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        // 만약 가져온 PicData에 기존 위치정보가 있을경우
        // onMapReady에서 지도뷰가 준비되면 바로 기존 위치정보로 뷰를 갱신한다.
        if(picData.getLocation()!=null){
            PicPlaceData p = picData.getLocation();



            Location location = new Location(p.getName());
            location.setLatitude(p.getLatitude());
            location.setLongitude(p.getLongitude());


            currentPosition
                    = new LatLng(p.getLatitude(), p.getLongitude());


            String markerTitle = getAddressFromLatLng(currentPosition);
            String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                    + " 경도:" + String.valueOf(location.getLongitude());

            Log.d(TAG, "onLocationResult : " + markerSnippet);


            //현재 위치에 마커 생성하고 이동
            setLocationOnMap(p.getLatitude(),p.getLongitude(), markerTitle, markerSnippet);
            mCurrentLocatiion = location;
            Log.d(TAG, "LocationListActivity  showPlaceInformation 1 has Data : ");
            showPlaceInformation(currentPosition);
        }

        serchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        // 검색 동작
                        if(serchView.getText().length()<1){
                            break;
                        }
                        List<Address>  addresses = getAddressFromString(serchView.getText().toString(),20);
                        if(addresses==null){
                            Toast.makeText(getApplicationContext(), "장소를 찾지 못하였습니다.", Toast.LENGTH_LONG).show();
                            break;
                        }
                        setLocationOnMap(addresses);
                        placeList.clear();
                        // placeList의 아이템 -> PicPlaceDataWrapper
                        // 여기서는 마커가 없음.
                        placeList.addAll(LocationParser.addressToPicPlaceDataWrapper(addresses));
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        // 기본 엔터키 동작
                        return false;
                }
                return true;
            }
        });
    }

    OnCompleteListener<Location> locationCallback = new OnCompleteListener<Location>() {
        @Override
        public void onComplete(@NonNull Task<Location> task) {


            if (task.isSuccessful()&&task.getResult()!=null) {
                location = task.getResult();
                //location = locationList.get(0);

                currentPosition
                        = new LatLng(location.getLatitude(), location.getLongitude());


                String markerTitle = getAddressFromLatLng(currentPosition);
                String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                        + " 경도:" + String.valueOf(location.getLongitude());

                Log.d(TAG, "onLocationResult : " + markerSnippet);


                //현재 위치에 마커 생성하고 이동
                setLocationOnMap(location.getLatitude(),location.getLongitude(), markerTitle, markerSnippet);

                mCurrentLocatiion = location;
                Log.d(TAG, "LocationListActivity  showPlaceInformation 2 Not Data : ");
                showPlaceInformation(currentPosition);
            }
        }
    };



    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else {

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);



            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED   ) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }


            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");
            // 이 주석 지우면 현재위치 계속 갱신함 ( locationCallback 변수의 함수를 계속 호출 )
            // 최초 1번의 갱신은 OnStart에 있음
            //mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());


            //구글맵에 현재위치 업데이트 띄우는거 우측 위에 있는 조그마한 버튼
            if (checkPermission())
                mMap.setMyLocationEnabled(true);

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        if (checkPermission()) {

            Log.d(TAG, "onStart : call mFusedLocationClient.requestLocationUpdates");
            //mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            // 가져온 PicData의 위치정보가 NULL일때만 Onstart에서 현재위치가져옴.
            if(picData.getLocation()==null)
                mFusedLocationClient.getLastLocation().addOnCompleteListener(locationCallback);

            if (mMap!=null)
                mMap.setMyLocationEnabled(true);

        }


    }


    @Override
    protected void onStop() {

        super.onStop();

        if (mFusedLocationClient != null) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            //mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }



    // 좌표 정보가담긴 LatLng 객체를 입력받아 그 정보로 위치 정보 검색해서 반환하기
    public String getAddressFromLatLng(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            // 잘못된 GPS 좌표
            return "null";
        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            // 주소 미발견
            return "null";
        } else {
            // 검색된 주소중 첫번째를 리턴
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }
    // 지역 이름 검색해서 나온 결과를 최대max개만큼 검색해 반환
    public List<Address> getAddressFromString(String serch,int max) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocationName(serch,max);
        } catch (IOException ioException) {
            //네트워크 문제
            //Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            Log.e(TAG,"getAddressFromString 지오코더 서비스 사용불가 : "+ioException);
            return null;
        } catch (IllegalArgumentException illegalArgumentException) {
            //Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            Log.e(TAG,"getAddressFromString 잘못된 GPS 좌표 : "+illegalArgumentException);
            // 잘못된 GPS 좌표
            return null;
        }


        if (addresses == null || addresses.size() == 0) {
            //Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            Log.e(TAG,"getAddressFromString 주소 미발견");
            // 주소 미발견
            return null;
        } else {
            return addresses;
        }
    }



    /*
     * 사용자가 GPS 기능을 켰는지 확인하기 값이 TRue면 킨것
     * */
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    //  public void setLocationOnMap(Location location, String markerTitle, String markerSnippet) {
    //  지정한 위치를 맺에 표시하기
    public void setLocationOnMap(double latitude, double longitude, String markerTitle, String markerSnippet) {


        if (currentMarker != null) currentMarker.remove();


        LatLng currentLatLng = new LatLng(latitude, longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);


        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        mMap.moveCamera(cameraUpdate);

    }

    public void setLocationOnMap(List<Address> address){

        if (address == null||address.size()==0) return;

        if (currentMarker != null) currentMarker.remove();

        LatLng firstLng = null;

        for(Address a : address){
            double lat = a.getLatitude();
            double lng = a.getLongitude();
            LatLng latLng = new LatLng(lat,lng);

            if(firstLng==null){
                firstLng = latLng;
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                currentMarker = mMap.addMarker(markerOptions);
            }
            else{
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("asdfasdf");
                mMap.addMarker(markerOptions);
            }

        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(firstLng);
        mMap.moveCamera(cameraUpdate);
    }


    // 이전에도 사진에 위치정보가 있었을경우
    // 그 위치 정보를 기반으로 기본 위치 설정
    // 위치 기본은 서울로 설정
    public void setDefaultLocation() {

        double latitude;
        double longitude;

        if(picData.getLocation()==null){
            //37.56, 126.97
            Log.d("test_hs"," LocationListActivity : setDefaultLocation picData.getLocation()==null");
            latitude = 37.56;
            longitude = 126.97;
        }
        else{
            PicPlaceData p = picData.getLocation();
            latitude = p.getLatitude();
            longitude = p.getLongitude();
        }

        LatLng DEFAULT_LOCATION = new LatLng(latitude,longitude);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";


        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);

    }


    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    private boolean checkPermission() {

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);



        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {
            return true;
        }

        return false;

    }



    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {
                // 퍼미션을 허용했다면 위치 업데이트를 시작합니다. 할려했으나 이떄쯤이면 위치 업데이트가 적용안됨
                // 액티비티 재실행
                //startLocationUpdates();
                activityRestartRequest();
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {


                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();

                }else {


                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();
                }
            }

        }
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LocationListActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d(TAG, "onActivityResult : GPS 활성화 되있음");


                        needRequest = true;

                        return;
                    }
                }

                break;
        }
    }



    // PlacesListener 오버라이딩
    @Override
    public void onPlacesFailure(PlacesException e) {
        Log.e(TAG, "LocationListActivity  onPlacesFailure : "+e.toString());
    }

    @Override
    public void onPlacesStart() {

    }




    // 주변 정보를 전부 가져오는데 성공했을때 할 행동
    @Override
    public void onPlacesSuccess(final List<Place> places) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                boolean isRun=false;
                Log.d(TAG, "LocationListActivity  onPlacesSuccess : ");
                //https://github.com/nomanr/Android-Google-Places-API/blob/master/placesAPI/src/main/java/noman/googleplaces/Place.java
                for (noman.googleplaces.Place place : places) {
                    LatLng latLng
                            = new LatLng(place.getLatitude()
                            , place.getLongitude());

                    String markerSnippet = getAddressFromLatLng(latLng);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(place.getName());
                    markerOptions.snippet(markerSnippet);
                    // TODO : 1. 지도에 표시되는 주변위치의 마커
                    // 이 마커는 mMap (구글 지도 관리객체 , moveCamera등도 가능) 담기면 지도에 출력됨.
                    // 마커의 클릭
                    Marker item = mMap.addMarker(markerOptions);

                    previous_marker.add(item);
                    /**
                     * TODO : 2. NRPlaces.Builder 완료 후 얻어온 주변 위치를 나타내는 객체 places를 임의로 만든 PlaceData 변환하고
                     * PlaceData를 placeList에 추가하고 adapter를 통해 View 생성
                     * 원하는것 : 리스트 뷰의 아이템 클릭시 마커 위의 부연설명이 표시되면서
                     */
                    // 여기서 PlaceData에 마커도 같이넣고 리스트뷰에서 클릭 이벤트 발생시 마커에서 표시?

                    placeList.add(LocationParser.placeToPicPlaceDataWrapper(place,item));
                    isRun=true;
                }
                if(isRun==true){
                    //중복 마커 제거
                    HashSet<Marker> hashSet = new HashSet<Marker>();
                    hashSet.addAll(previous_marker);
                    previous_marker.clear();
                    previous_marker.addAll(hashSet);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void onPlacesFinished() {

    }
    // E/test_hs: LocationListActivity  onPlacesFailure : noman.googleplaces.PlacesException: OVER_QUERY_LIMIT
    // OVER_QUERY_LIMIT 발생을 막기 위해 천천히 실행
    // 2.5초 딜레이를 줘도 너무 자주 위치에 접근하면 OVER_QUERY_LIMIT 발생함
    // TODO 주의 : OVER_QUERY_LIMIT가 상당히 오래걸림 한번 발생하고 오래걸린다면 거의 1분 이상 지연해야 가능한듯.
    // 진짜 심각하게 느림 언제 회복되냐 애국가를 4절까지 불러야하는가
    public void showPlaceInformation(final LatLng location)
    {
        mMap.clear();//지도 클리어

        if (previous_marker != null)
            previous_marker.clear();//이전에 가져온 주변 지역정보 초기화
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //.type(PlaceType.RESTAURANT) //음식점
                new NRPlaces.Builder()
                        .listener(LocationListActivity.this)
                        .key("AIzaSyCGtSKl81RRcPu9xNFYXU3N4zBkwAFXSN8")
                        .latlng(location.latitude, location.longitude)//현재 위치
                        .radius(PLACE_SERCH_DISTANCE) // PLACE_SERCH_DISTANCE 미터 내에서 검색
                        .build()
                        .execute();
            }
        },3000);
    }

    // 조작된 UploadPicData를 NewAlbumUploadActivity에 전달
    private void submit(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(IntentProtocol.PIC_DATA_CLASS_NAME,picData);
        setResult(IntentProtocol.SET_PIC_LOCATION, resultIntent);
        finish();
    }

    // UploadPicData를 그대로 다시 NewAlbumUploadActivity에 전달하고 앱을 재실행해달라고 요청
    private void activityRestartRequest(){
        /*
        Intent resultIntent = new Intent();
        resultIntent.putExtra(IntentProtocol.PIC_DATA_CLASS_NAME,picData);
        setResult(IntentProtocol.RESTART_PIC_LOCATION_ACTIVITY, resultIntent);
        finish();
        */
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
    // TODO 등록버튼이 안되서 여기서 임시로 볼륨업키 구현
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            submit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class ActivityCallBack{

        /*
         * 사용자가 리스트에서 Place 목록중 하나를 터치했을때 동작
         * 1. 지도 위치를 선택한곳으로 맞춰줌
         * 2. UploadPicData의 Place 정보를 변경 (이렇게 변경한 정보는 submit()에서 전송)
         * 3. PicPlaceDataWrapper에서 마커를 꺼낸후 마커가 있으면 showInfoWindow
         * */
        //item.showInfoWindow();
        public void setLocationFromList(PicPlaceDataWrapper pWrapper){
            PicPlaceData p = pWrapper.getPicPlaceData();
            //1. 지도 위치를 선택한곳으로 맞춰줌
            LatLng latLng = new LatLng(p.getLatitude(),p.getLongitude());

            // String markerTitle = getAddressFromLatLng(currentPosition);
            String markerTitle = getAddressFromLatLng(latLng);
            String markerSnippet = "위도:" + String.valueOf(p.getLatitude())
                    + " 경도:" + String.valueOf(p.getLongitude());

            setLocationOnMap(p.getLatitude(),p.getLongitude(),markerTitle,markerSnippet);
            Log.d(TAG, "ActivityCallBack  setLocationOnMap : " + p.getName());
            // 2. UploadPicData의 Place 정보를 변경 (이렇게 변경한 정보는 submit()에서 전송)
            PicPlaceData placeData = new PicPlaceData();
            placeData.setLatitude(p.getLatitude());
            placeData.setLongitude(p.getLatitude());
            placeData.setName(p.getName());
            picData.setLocation(placeData);
            // 마커는 NULL일수있음.
            if(pWrapper.getMarker()!=null){
                Marker marker = pWrapper.getMarker();
                marker.showInfoWindow();
            }
        }



    }



}
