package com.picaproject.pica.Fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import com.picaproject.pica.Util.IntentProtocol;
import com.picaproject.pica.Util.NetworkItems.ImageResultItem;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.AppContext;
import com.picaproject.pica.Util.AppUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 * How to solve?
 * 로직을 바꿔야함
 * 먼저 지도를 정의해 준 이후에
 * item을 업데이트 해주고
 * clustermanager.cluster()를 해줌
 * onClusterItemRendered, onClusteredRendered구현해야함
 */
public class MapPhotoClusterFragment extends Fragment implements OnMapReadyCallback, ClusterManager.OnClusterClickListener<ImageResultItem>, ClusterManager.OnClusterInfoWindowClickListener<ImageResultItem>, ClusterManager.OnClusterItemClickListener<ImageResultItem>, ClusterManager.OnClusterItemInfoWindowClickListener<ImageResultItem>{
    private GoogleMap googleMap;
    private MapView mapView;
    private ClusterManager<ImageResultItem> mClusterManager;
    private ArrayList<ImageResultItem> picDataArrayList;
    public MapPhotoClusterFragment() {
        // Required empty public constructor
    }

    public MapPhotoClusterFragment(ArrayList<ImageResultItem> list) {
        this.picDataArrayList = new ArrayList<>();
        picDataArrayList.addAll(list);
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        mapView.getMapAsync(this::onMapReady);
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        Log.i("googleMap", "isReady!!");

        //camera위치도 여러 위치들의 평균값을 구하던지해야한다
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom((AppUtility.getAppinstance().getDefaultLocation()), 10.0f));

        //item update in here 먼저 map을 ready시켜야한다!
        startPhotoCluster();
    }


    private void startPhotoCluster(){
        mClusterManager = new ClusterManager<ImageResultItem>(AppContext.getContext(), googleMap);
        mClusterManager.setRenderer(new ClusterPhotoRenderer());
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        mClusterManager.clearItems();
        //add All items here
        addItemToClusterManager();
        //Log.d("googlemap_cluster", picDataArrayList.toString());
        mClusterManager.cluster();
    }


    private void addItemToClusterManager(){
        if (picDataArrayList != null) {
            for (ImageResultItem item : picDataArrayList) {
                if (item.hasLocations()) {
                    mClusterManager.addItem(item);
                }
            }
        }
    }
    public void resetPhotoList(ArrayList<ImageResultItem> list){
        if (picDataArrayList != null)
            picDataArrayList.clear();
        else
            picDataArrayList = new ArrayList<>();

        picDataArrayList.addAll(list);

        mapView.getMapAsync(this);
    }

    private class ClusterPhotoRenderer extends DefaultClusterRenderer<ImageResultItem> {
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
        protected void onBeforeClusterItemRendered(ImageResultItem imageResultItem, MarkerOptions markerOptions) {
            // Draw a single UploadPicData - show their profile photo and set the info window to show their name

            /**
             * 만일 사진을 눌렀을때 사진위의 마커를 띄우고 싶으면 title설정
             * 임시로 뜨는 아이콘 set
             */
            mImageView.setImageResource(R.drawable.img_sample);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
            //markerOptions.icon(getItemIcon(imageResultItem));
        }


        @Override
        protected void onClusterItemRendered(ImageResultItem clusterItem, Marker marker) {
            /*
            cluster item updated, onClusterItemUpdated대신 사용
             */
            Glide.with(getActivity())
                    .load(clusterItem.getFile())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            mImageView.setImageDrawable(resource);
                            Bitmap icon = mIconGenerator.makeIcon();
                            marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }


        @Override
        protected void onBeforeClusterRendered(Cluster<ImageResultItem> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            //markerOptions.icon(getClusterIcon(cluster));

            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        /**
         * 여기서 가끔 일부 cluster list의 사이즈가 0인경우가 발생한다. 이경우 multidrawable을 그리면 그냥 흰색이됨
         * @param cluster
         * @param marker
         */
        @Override
        protected void onClusterRendered(Cluster<ImageResultItem> cluster, Marker marker) {
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (ImageResultItem p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;

                Glide.with(getActivity())
                        .load(p.getFile())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                resource.setBounds(0, 0, width, height);
                                profilePhotos.add(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });

            }

            ClusterMultiDrawable multiDrawable = new ClusterMultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
        }


        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }
    @Override
    public boolean onClusterClick(Cluster<ImageResultItem> cluster) {

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
    public void onClusterInfoWindowClick(Cluster<ImageResultItem> cluster) {
        // Does nothing, but you could go to a list of the users.
        Log.d("infowindowClick!", cluster.getSize()+"");
    }

    @Override
    public boolean onClusterItemClick(ImageResultItem item) {
        // Does nothing, but you could go into the user's profile page, for example.
        /**
         * 확대되었을때 아이템 눌렀을때는 여기서..
         */

        int id = (int)item.getArrayIndex();
        Intent intent = new Intent(getActivity(), ImageDetailActivity.class);
        intent.putExtra(IntentProtocol.INTENT_START_POSITION, id);
        intent.putExtra(IntentProtocol.INTENT_ALBUM_IMAGE_LIST, picDataArrayList);
        startActivity(intent);
        Log.d("clusterItemClick!", item.getLatitude()+", "+item.getLongitude());
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(ImageResultItem item) {
        // Does nothing, but you could go into the user's profile page, for example.
        Log.d("iteminfowindowClick!", item.getLatitude()+", "+item.getLongitude());
    }
}
