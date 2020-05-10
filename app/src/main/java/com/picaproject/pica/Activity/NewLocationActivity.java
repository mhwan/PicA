package com.picaproject.pica.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.picaproject.pica.BuildConfig;
import com.picaproject.pica.IntentProtocol;
import com.picaproject.pica.Item.PicPlaceData;
import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.AppUtility;
import com.picaproject.pica.Util.GpsTracker;
import com.picaproject.pica.Util.PermissionChecker;

import java.util.Arrays;

/**
 *
 * 권한체크 -> gps 켜있는지 확인
 */
public class NewLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private AutocompleteSupportFragment autocompleteFragment;
    private UploadPicData pictureData;
    private GoogleMap googleMap;
    private LatLng currentLocation;
    private Marker currentMarker;
    private boolean isPickMode = false;
    private ImageView pick_img;
    private ViewGroup general_frame, pick_button_frame;
    private PermissionChecker permissionChecker;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private TextView location_label, location_okay;
    private LatLng mDefaultLocation = new LatLng(37.5650168,126.8491219);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location);
        pictureData = (UploadPicData) getIntent().getParcelableExtra(IntentProtocol.PIC_DATA_CLASS_NAME);
        initView();
    }

    private void initView(){
        pick_img = (ImageView) findViewById(R.id.pick_map_custom_marker);
        pick_button_frame = findViewById(R.id.pick_frame);
        general_frame = findViewById(R.id.generals_frame);
        location_label = (TextView) findViewById(R.id.location_label);
        location_okay = (TextView) findViewById(R.id.general_location_okay);

        location_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //최종 위치 등록
                if (currentLocation != null) {
                    Intent intent = new Intent();
                    pictureData.setLocation(new PicPlaceData(currentLocation.latitude, currentLocation.longitude));
                    intent.putExtra(IntentProtocol.PIC_DATA_CLASS_NAME,pictureData);
                    setResult(IntentProtocol.SET_PIC_LOCATION, intent);
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "위치를 선택한 이후에 등록해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.fab_now_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //현위치 갖고오기
                getCurrentLocation(true);
            }
        });

        findViewById(R.id.fab_pick_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managePickMode();
            }
        });

        findViewById(R.id.pick_mode_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPickMode) {
                    managePickMode();
                }
            }
        });
        findViewById(R.id.pick_mode_okay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPickMode) {
                    managePickMode();
                    LatLng center = googleMap.getCameraPosition().target;
                    changeCameraPosition(center);
                    addMarkerAtLatlng();
                    autocompleteFragment.setText(AppUtility.getAppinstance().getAddress(center.latitude, center.longitude));
                    setLocationLabelText(null);
                }
            }
        });
        //init googleMap
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        setupAutoCompleteFragment();

        //check permission
        permissionChecker = new PermissionChecker(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, NewLocationActivity.this);
        permissionChecker.checkPermission("위치 권한이 거부되었습니다. 지도 기반으로 위치를 선택하시려면 위치 권한이 필요합니다.");
    }

    /**
     * pickmode : true 위치 직접 선택하는 모드
     */
    private void managePickMode(){
        isPickMode = !isPickMode;
        if (isPickMode) {
            pick_button_frame.setVisibility(View.VISIBLE);
            general_frame.setVisibility(View.GONE);
            pick_img.setVisibility(View.VISIBLE);
        } else {
            pick_button_frame.setVisibility(View.GONE);
            general_frame.setVisibility(View.VISIBLE);
            pick_img.setVisibility(View.GONE);
        }
    }
    protected void showAlertbox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewLocationActivity.this);
        builder.setTitle("위치서비스 비활성화");
        builder.setMessage("현위치를 탐색하기 위해서 위치서비스가 필요합니다. 위치 설정을 켜시고 다시 현위치를 탐색해주세요.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), IntentProtocol.GPS_SETTING);
                //dialog.cancel();
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

    private void setLocationLabelText(String name){
        String text = "현재 사진의 위치가 설정되지 않았습니다.\n위치를 검색 또는 지도에서 직접 마커를 찍으세요.";
        if (currentLocation != null && currentMarker != null) {
            StringBuilder sb = new StringBuilder("사진 위치 : ");
            if (name != null) {
                sb.append(name);
                sb.append("(주소 : ");
                sb.append(AppUtility.getAppinstance().getAddress(currentLocation.latitude, currentLocation.longitude));
                sb.append(")");
            }
            else {
                sb.append(AppUtility.getAppinstance().getAddress(currentLocation.latitude, currentLocation.longitude));
            }

            text = sb.toString();
        }

        location_label.setText(text);

        setLocationOkayButton();
    }

    private void setLocationOkayButton(){
        if (currentLocation != null && currentMarker != null) {
            location_okay.setEnabled(true);
            location_okay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        } else {
            location_okay.setEnabled(false);
            location_okay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDarkGray));
        }
    }

    private void getCurrentLocation(boolean isAlertbox){
        if (!AppUtility.getAppinstance().checkLocationServicesStatus()){
            if (isAlertbox)
                showAlertbox();
            else
                Toast.makeText(getApplicationContext(), "현위치를 찾으려면 위치센서를 켜주세요", Toast.LENGTH_SHORT).show();
        } else {
            getDeviceLocation();
        }
    }

    private boolean getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        return ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void getDeviceLocation() {

        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (getLocationPermission()) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            googleMap.setMyLocationEnabled(true);
                            mLastKnownLocation = (Location) task.getResult();
                            if (mLastKnownLocation != null) {
                                changeCameraPosition(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
                                autocompleteFragment.setText(AppUtility.getAppinstance().getAddress(currentLocation.latitude, currentLocation.longitude));
                            }
                            else
                                setMapDefaultLocation();
                        } else {
                            Log.d("Google map", "Current location is null. Using defaults.");
                            Log.e("Google map", "Exception: %s", task.getException());
                            setMapDefaultLocation();
                            //googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void setMapDefaultLocation(){
        googleMap.setMyLocationEnabled(false);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 15));
        autocompleteFragment.setText(AppUtility.getAppinstance().getAddress(mDefaultLocation.latitude, mDefaultLocation.longitude));
    }
    private boolean isPictureHasLocation(){
        if (pictureData != null) {
            if (pictureData.getLocation() != null) {
                return true;
            }
        }

        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("activity result", "activity result");
        if (requestCode == IntentProtocol.GPS_SETTING) {
            getCurrentLocation(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionChecker.requestPermissionsResult(requestCode, grantResults, "위치 권한을 허용후에 이용해주세요.");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        if (isPictureHasLocation()){
            double latitude = pictureData.getLocation().getLatitude();
            double longitude = pictureData.getLocation().getLongitude();

            Log.d("Photo Data", latitude+", "+longitude);
            changeCameraPosition(new LatLng(latitude, longitude));
            addMarkerAtLatlng();
            autocompleteFragment.setText(AppUtility.getAppinstance().getAddress(latitude, longitude));
        } else {
            //현위치로
            getCurrentLocation(true);
        }

        setLocationLabelText(null);
    }

    private void addMarkerAtLatlng(){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLocation);

        if (currentMarker != null)
            currentMarker.remove();
        currentMarker = googleMap.addMarker(markerOptions);

    }
    private void changeCameraPosition(LatLng latLng) {
        this.currentLocation = latLng;


        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 15);
        googleMap.moveCamera(cameraUpdate);
    }

    @Override
    public void onBackPressed() {
        if (isPickMode) {
            managePickMode();
        } else
            super.onBackPressed();
    }

    private void setupAutoCompleteFragment() {
        // Initialize the SDK
        if (!Places.isInitialized())
            Places.initialize(getApplicationContext(), BuildConfig.API_KEY);

// Create a new Places client instance
        //PlacesClient placesClient = Places.createClient(getApplicationContext());
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        //autocompleteFragment.setCountry("KR");
// Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("place_autocomplete", "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng());
                LatLng latLng = place.getLatLng();
                changeCameraPosition(latLng);
                addMarkerAtLatlng();
                autocompleteFragment.setText(place.getAddress());
                setLocationLabelText(place.getName());
                //setLocationWork(latLng.latitude, latLng.longitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("place_autocomplete", "An error occurred: " + status);
            }
        });
    }
}
