package com.picaproject.pica.Fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.picaproject.pica.Activity.ImageDetailActivity;
import com.picaproject.pica.CustomView.ClusterMultiDrawable;
import com.picaproject.pica.IntentProtocol;
import com.picaproject.pica.Item.PicPlaceData;
import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.AppContext;
import com.picaproject.pica.Util.AppUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapPhotoClusterFragment extends Fragment implements OnMapReadyCallback, ClusterManager.OnClusterClickListener<UploadPicData>, ClusterManager.OnClusterInfoWindowClickListener<UploadPicData>, ClusterManager.OnClusterItemClickListener<UploadPicData>, ClusterManager.OnClusterItemInfoWindowClickListener<UploadPicData>{
    private GoogleMap googleMap;
    private MapView mapView;
    private ClusterManager<UploadPicData> mClusterManager;
    private ArrayList<UploadPicData> picDataArrayList;
    public MapPhotoClusterFragment() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            picDataArrayList = getArguments().getParcelableArrayList(IntentProtocol.KEY_PARCELABLE_PHOTO_DATA);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_map_photo_cluster, container, false);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        mapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(this);
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        Log.i("googleMap", "isReady!!");
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom((AppUtility.getAppinstance().getDefaultLocation()), 10.0f));
        startPhotoCluster();
    }
    private void startPhotoCluster(){
        mClusterManager = new ClusterManager<UploadPicData>(AppContext.getContext(), googleMap);
        mClusterManager.setRenderer(new ClusterPhotoRenderer());
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        addItems();
        mClusterManager.cluster();
    }


    private void addItems(){

        /*
        ArrayList<UploadPicData> sampleDatas = new ArrayList<>();
        for (int i = 0; i<58; i++) {
            UploadPicData uploadPicData = new UploadPicData(R.drawable.img_sample);
            uploadPicData.setLocation(new PicPlaceData(createRandomLocation()));
            sampleDatas.add(uploadPicData);
        }*/

        if (picDataArrayList == null)
            Log.d("picdata", "is null");

        mClusterManager.addItems(picDataArrayList);
    }

    private class ClusterPhotoRenderer extends DefaultClusterRenderer<UploadPicData> {
        private final IconGenerator mIconGenerator = new IconGenerator(AppContext.getContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(AppContext.getContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public ClusterPhotoRenderer() {
            super(AppContext.getContext(), googleMap, mClusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.ui_cluster_multi_photo, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            /**
             * dimension : 50dp, padding: 2dp
             */
            final float scale = getContext().getResources().getDisplayMetrics().density;
            mImageView = new ImageView(AppContext.getContext());
            mDimension = (int) (50 * scale + 0.5f);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) (2 * scale + 0.5f);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(UploadPicData uploadPicData, MarkerOptions markerOptions) {
            // Draw a single UploadPicData - show their profile photo and set the info window to show their name
            /**
             * 만일 사진을 눌렀을때 사진위의 마커를 띄우고 싶으면 title설정
             */
            markerOptions.icon(getItemIcon(uploadPicData));
        }

        @Override
        protected void onClusterItemUpdated(UploadPicData uploadPicData, Marker marker) {
            /**
             * 만일 사진을 눌렀을때 사진위의 마커를 띄우고 싶으면 setTitle설정
             */
            // Same implementation as onBeforeClusterItemRendered() (to update cached markers)
            marker.setIcon(getItemIcon(uploadPicData));
        }

        /**
         * Get a descriptor for a single UploadPicData (i.e., a marker outside a cluster) from their
         * profile photo to be used for a marker icon
         *
         * @return the UploadPicData's profile photo as a BitmapDescriptor
         */
        private BitmapDescriptor getItemIcon(UploadPicData uploadPicData) {
            mImageView.setImageResource(uploadPicData.getImgSampleId());
            Bitmap icon = mIconGenerator.makeIcon();
            return BitmapDescriptorFactory.fromBitmap(icon);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<UploadPicData> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            markerOptions.icon(getClusterIcon(cluster));
        }

        @Override
        protected void onClusterUpdated(Cluster<UploadPicData> cluster, Marker marker) {
            // Same implementation as onBeforeClusterRendered() (to update cached markers)
            marker.setIcon(getClusterIcon(cluster));
        }

        /**
         * Get a descriptor for multiple people (a cluster) to be used for a marker icon. Note: this
         * method runs on the UI thread. Don't spend too much time in here (like in this example).
         *
         * @param cluster cluster to draw a BitmapDescriptor for
         * @return a BitmapDescriptor representing a cluster
         */
        private BitmapDescriptor getClusterIcon(Cluster<UploadPicData> cluster) {
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (UploadPicData p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
                Drawable drawable = getResources().getDrawable(p.getImgSampleId());
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            ClusterMultiDrawable multiDrawable = new ClusterMultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            return BitmapDescriptorFactory.fromBitmap(icon);
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }
    @Override
    public boolean onClusterClick(Cluster<UploadPicData> cluster) {

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<UploadPicData> cluster) {
        // Does nothing, but you could go to a list of the users.
        Log.d("infowindowClick!", cluster.getSize()+"");
    }

    @Override
    public boolean onClusterItemClick(UploadPicData item) {
        // Does nothing, but you could go into the user's profile page, for example.
        /**
         * 확대되었을때 아이템 눌렀을때는 여기서..
         */

        int id = (int)item.getClassId();
        Intent intent = new Intent(getActivity(), ImageDetailActivity.class);
        intent.putExtra(IntentProtocol.INTENT_START_POSITION, id);
        intent.putExtra(IntentProtocol.INTENT_ALBUM_IMAGE_LIST, picDataArrayList);
        startActivity(intent);
        Log.d("clusterItemClick!", item.getLocation().getLatitude()+", "+item.getLocation().getLongitude());
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(UploadPicData item) {
        // Does nothing, but you could go into the user's profile page, for example.
        Log.d("iteminfowindowClick!", item.getLocation().getLatitude()+", "+item.getLocation().getLongitude());
    }
}
