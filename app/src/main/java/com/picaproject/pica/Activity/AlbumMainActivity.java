package com.picaproject.pica.Activity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.picaproject.pica.Fragment.ImageGridFragment;
import com.picaproject.pica.Fragment.MapPhotoClusterFragment;
import com.picaproject.pica.Fragment.UserInfoFragment;
import com.picaproject.pica.IntentProtocol;
import com.picaproject.pica.Item.PicPlaceData;
import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.ImageMetadataParser;
import com.picaproject.pica.Util.PicDataParser;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AlbumMainActivity extends AppCompatActivity {
    private Random mRandom = new Random(1984);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_main);

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
                    getSupportActionBar().setTitle("앨범 샘플 1");
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
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(IntentProtocol.KEY_PARCELABLE_PHOTO_DATA, makeImageSampleList());
        MapPhotoClusterFragment fragment1 = new MapPhotoClusterFragment();
        fragment1.setArguments(bundle);


        ImageGridFragment fragment = (ImageGridFragment) ImageGridFragment.newInstance(makeImageSampleList());

        adapter.addFragment(fragment, "사진");
        adapter.addFragment(fragment1, "지도로보기");
        viewPager.setAdapter(adapter);
    }
    /**
     * item의 위치를 랜덤으로 넣기위해 존재
     *
     * 37.561763, 126.903369 ~ 37.404931, 127.117462
     * 서울 마포부터 성남사이의 위치
     * @param min
     * @param max
     * @return
     */
    private double random(double min, double max) {
        return mRandom.nextDouble() * (max - min) + min;
    }

    private LatLng createRandomLocation() {
        return new LatLng(random(37.561763, 37.404931), random(127.117462, 126.903369));
    }
    private ArrayList<UploadPicData> makeImageSampleList(){


        ArrayList<UploadPicData> imageItems = new ArrayList<>();
        for (int i=0; i< 35; i++) {
            UploadPicData data = new UploadPicData(R.drawable.img_sample);
            data.setLocation(new PicPlaceData(createRandomLocation()));
            data.setContents("여기 케이크 진짜 맛있었는데 크림이 사르르");
            data.setTags(new ArrayList<String>(Arrays.asList(new String[] {"카페", "케이크", "송도맛집"})));
            data.setClassId(i);
            imageItems.add(data);

        }

        return imageItems;
    }

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
        if(requestCode == IntentProtocol.GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null){
            ClipData datas = data.getClipData();
            ArrayList<UploadPicData> prasePicDatas;
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
                UploadPicData uploadPicData = new UploadPicData(data.getData().toString());
                if (placeData != null)
                    uploadPicData.setLocation(placeData);

                prasePicDatas = new ArrayList<>();
                prasePicDatas.add(uploadPicData);
                Log.i("test_hs","AlbumMainActivity onActivityResult 2 : "+prasePicDatas.toString());
            }
            // 사진 선택 안했을시
            else{
                Toast.makeText(this,"사진을 선택해주세요.",Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(this, NewAlbumUploadActivity.class);
            intent.putExtra(IntentProtocol.PIC_DATA_LIST_NAME,prasePicDatas);
            startActivityForResult(intent, IntentProtocol.ADD_PIC_MULTI_MODE);



        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
