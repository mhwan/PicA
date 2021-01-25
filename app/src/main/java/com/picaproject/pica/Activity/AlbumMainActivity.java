package com.picaproject.pica.Activity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.picaproject.pica.CustomView.AlbumRecyclerAdapter;
import com.picaproject.pica.Fragment.ImageGridFragment;
import com.picaproject.pica.Fragment.MapPhotoClusterFragment;
import com.picaproject.pica.Util.AppUtility;
import com.picaproject.pica.Util.IntentProtocol;
import com.picaproject.pica.Item.PicPlaceData;
import com.picaproject.pica.Item.UploadImageItem;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.ImageMetadataParser;
import com.picaproject.pica.Util.NetworkItems.AlbumResultItem;
import com.picaproject.pica.Util.NetworkItems.ImageResultItem;
import com.picaproject.pica.Util.NetworkItems.ImageTempResult;
import com.picaproject.pica.Util.NetworkUtility;
import com.picaproject.pica.Util.PicDataParser;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumMainActivity extends AppCompatActivity {
    private Random mRandom = new Random(1984);
    private int albumId;
    private TextView tab_header, tab_desc;
    private ImageView tab_image;
    private MapPhotoClusterFragment mapPhotoClusterFragment;
    private ImageGridFragment imageGridFragment;
    private AlbumResultItem albumItem;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_main);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            albumId = bundle.getInt(AlbumRecyclerAdapter.KEY_EXTRA_ALBUM_ID, -1);
        initView();
    }

    private void initView(){
        final RelativeLayout guideView = findViewById(R.id.tab_album_guide);
        final Toolbar toolbar = findViewById(R.id.tab_toolbar);
        final FloatingActionButton fab = findViewById(R.id.tab_fab);
        Log.d("statusbar", getStatusBarHeight()+"");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tab_image = findViewById(R.id.tab_header_image);
        tab_header = findViewById(R.id.tab_title);
        tab_desc = findViewById(R.id.tab_desc);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("test_hs","initView  : ");
                // 외부 갤러리 앱 열기
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(intent, IntentProtocol.GET_GALLERY_IMAGE);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), IntentProtocol.GET_GALLERY_IMAGE);

            }
        });

        final ViewPager viewPager = findViewById(R.id.tab_viewpager);
        setupViewPager(viewPager);


        TabLayout tabLayout = findViewById(R.id.tab_tabs);
        tabLayout.setupWithViewPager(viewPager);
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.tab_collapse_toolbar);

        AppBarLayout appBarLayout = findViewById(R.id.tab_appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0) {
                    getSupportActionBar().setTitle(albumItem.getName());
                    guideView.animate().alpha(0.0f);
                    guideView.setVisibility(View.GONE);
                    fab.hide();
                    //getSupportActionBar().show();
                }
                else {
                    getSupportActionBar().setTitle("");
                    guideView.animate().alpha(1.0f);
                    guideView.setVisibility(View.VISIBLE);
                    fab.show();
                    //getSupportActionBar().hide();
                }
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Log.d("AlbumMain", "onTabSelected: pos: " + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        findViewById(R.id.manage_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlbumMainActivity.this, CreateAlbumActivity.class);
                intent.putExtra(IntentProtocol.INTENT_ALBUM_MODE, true);
                intent.putExtra(IntentProtocol.INTENT_INPUT_ALBUM_TITLE, albumItem.getName());
                intent.putExtra(IntentProtocol.INTENT_INPUT_ALBUM_DESC, albumItem.getDescription());
                intent.putExtra(IntentProtocol.INTENT_INPUT_ALBUM_PICTURE, albumItem.getDefaultPicture());
                startActivity(intent);
            }
        });
        /*
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_sample);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @SuppressWarnings("ResourceType")
                @Override
                public void onGenerated(Palette palette) {

                    int vibrantColor = palette.getVibrantColor(R.color.colorPrimary);
                    int vibrantDarkColor = palette.getDarkVibrantColor(R.color.colorPrimaryDark);
                    collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                    //collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor);
                }
            });

        } catch (Exception e) {
            // if Bitmap fetch fails, fallback to primary colors
            collapsingToolbarLayout.setContentScrimColor(
                    ContextCompat.getColor(this, R.color.colorPrimary)
            );
            collapsingToolbarLayout.setStatusBarScrimColor(
                    ContextCompat.getColor(this, R.color.colorPrimaryDark)
            );
        }*/

        //temporaryImageLoadTask();
        getLoadPictureTask();
    }

    private void setupViewPager(ViewPager viewPager) {
        //getLoadPictureTask();
        //temporaryImageLoadTask();

        //ArrayList<ImageResultItem> resultItems = makeImageSamples();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        mapPhotoClusterFragment = new MapPhotoClusterFragment();
        imageGridFragment = (ImageGridFragment) ImageGridFragment.newInstance(true);

        adapter.addFragment(imageGridFragment, "사진");
        adapter.addFragment(mapPhotoClusterFragment, "지도로보기");
        viewPager.setAdapter(adapter);

        //temporaryImageLoadTask();
    }

    /*
    //initview에서 마지막 단계에서 이걸 진행시켰으나 프래그먼트에 initview가 실행되지 않아 문제 발생
    private void temporaryImageLoadTask(){
        ArrayList<ImageResultItem> resultItems = makeImageSamples();
        imageGridFragment.resetImageList(resultItems);
        mapPhotoClusterFragment.resetPhotoList(resultItems);
    }*/

    private void setAlbumInfo(){
        tab_header.setText(albumItem.getName());
        tab_desc.setText(albumItem.getDescription());
        Glide.with(getApplicationContext()).load(albumItem.getDefaultPicture()).error(R.drawable.img_sample).into(tab_image);
    }
    private void getLoadPictureTask(){
        Log.d("album_id", albumId+"");
        NetworkUtility.getInstance().getAlbumPhotoList(albumId, AppUtility.memberId,  new Callback<AlbumResultItem>() {
            @Override
            public void onResponse(Call<AlbumResultItem> call, Response<AlbumResultItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("album_photo_Response", response.body().toString());
                    AlbumResultItem albumResultItem = response.body();

                    switch (albumResultItem.getCode()){
                        case NetworkUtility.APIRESULT.RESULT_SUCCESS :
                            ArrayList<ImageResultItem> result;
                            albumItem = new AlbumResultItem();
                            albumItem.setName(albumResultItem.getName());
                            albumItem.setDescription(albumResultItem.getDescription());
                            albumItem.setDefaultPicture(albumResultItem.getDefaultPicture());

                            result = new ArrayList<>();
                            if (albumResultItem.getResult() != null && albumResultItem.getResult().size() > 0) {
                                for (int i = 0; i < albumResultItem.getResult().size(); i++) {
                                    result.add(albumResultItem.getResult().get(i).getResult());
                                    result.get(i).setArrayIndex(i);
                                }
                            }

                            setAlbumInfo();



                            imageGridFragment.resetImageList(result);
                            mapPhotoClusterFragment.resetPhotoList(result);
                            break;
                        case NetworkUtility.APIRESULT.RESULT_NOT_EXIST :
                            Toast.makeText(getApplicationContext(), "존재하지 않는 앨범입니다.", Toast.LENGTH_SHORT).show();
                            break;
                        case NetworkUtility.APIRESULT.RESULT_FILE_GET_ERROR :
                            Toast.makeText(getApplicationContext(), "파일을 가져오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            break;

                        case NetworkUtility.APIRESULT.RESULT_NO_AUTHOR :
                            Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<AlbumResultItem> call, Throwable t) {
                Log.i("GET_ALBUM_PHOTO", "FAILED");
                t.printStackTrace();
            }
        });
    }

    /*
    private ArrayList<ImageResultItem> makeImageSamples(){
        ArrayList<ImageResultItem> items = new ArrayList<>();
        for (int i = 0; i < 20; i++){
            ImageResultItem imageResultItem = new ImageResultItem();
            imageResultItem.setPictureId(i);
            imageResultItem.setArrayIndex(i);
            imageResultItem.setUploadDate("2014-01-01");
            imageResultItem.setFile("https://picsum.photos/200");
            LatLng latLng = createRandomLocation();
            imageResultItem.setLongitude(latLng.longitude);
            imageResultItem.setLatitude(latLng.latitude);
            items.add(imageResultItem);
        }

        return items;
    }*/
    /**
     * item의 위치를 랜덤으로 넣기위해 존재
     *
     * 37.561763, 126.903369 ~ 37.404931, 127.117462
     * 서울 마포부터 성남사이의 위치
     * @return
     */

    private double random(double min, double max) {
        return mRandom.nextDouble() * (max - min) + min;
    }

    private LatLng createRandomLocation() {
        return new LatLng(random(37.561763, 37.404931), random(127.117462, 126.903369));
    }
    /*

    private ArrayList<UploadImageItem> makeImageSampleList(){


        ArrayList<UploadImageItem> imageItems = new ArrayList<>();
        for (int i=0; i< 35; i++) {
            UploadImageItem data = new UploadImageItem(R.drawable.img_sample);
            data.setLocation(new PicPlaceData(createRandomLocation()));
            data.setContents("여기 케이크 진짜 맛있었는데 크림이 사르르");
            data.setTags(new ArrayList<String>(Arrays.asList(new String[] {"카페", "케이크", "송도맛집"})));
            data.setClassId(i);
            imageItems.add(data);

        }

        return imageItems;
    }
*/
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            result = getResources().getDimensionPixelSize(resourceId);

        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("test_hs","AlbumMainActivity  : ");
        if (requestCode == IntentProtocol.ADD_PIC_MULTI_MODE || requestCode == IntentProtocol.REQUEST_PIC_DETAIL) {
            Log.i("pictureDetail","AlbumMainActivity  : finished");
            getLoadPictureTask();
        }
        if(requestCode == IntentProtocol.GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null){
            ClipData datas = data.getClipData();
            ArrayList<UploadImageItem> prasePicDatas;
            // 사진 여러장 선택시
            if(datas!=null){
                prasePicDatas = PicDataParser.parseDataFromClipData(getApplicationContext(), datas);
                // 갤러리에서 가져온 n장의 사진 처리
                Log.i("test_hs","AlbumMainActivity onActivityResult : "+prasePicDatas.toString());

            }
            // 사진 1장 선택시
            else if(data.getData()!=null){
                Uri uri = data.getData();
                PicPlaceData placeData = ImageMetadataParser.getLocationMetaData(getApplicationContext(), uri);
                UploadImageItem uploadImageItem = new UploadImageItem(data.getData().toString());
                if (placeData != null)
                    uploadImageItem.setLocation(placeData);

                prasePicDatas = new ArrayList<>();
                prasePicDatas.add(uploadImageItem);
                Log.i("test_hs","AlbumMainActivity onActivityResult 2 : "+prasePicDatas.toString());
            }
            // 사진 선택 안했을시
            else{
                Toast.makeText(this,"사진을 선택해주세요.",Toast.LENGTH_LONG).show();
                return;
            }

            Intent intent = new Intent(this, NewAlbumUploadActivity.class);
            intent.putExtra(IntentProtocol.INTENT_INPUT_ALBUM_ID, albumId);
            intent.putExtra(IntentProtocol.PIC_DATA_LIST_NAME,prasePicDatas);
            startActivityForResult(intent, IntentProtocol.ADD_PIC_MULTI_MODE);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
