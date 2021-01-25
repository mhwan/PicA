package com.picaproject.pica.Activity;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.picaproject.pica.Fragment.ImageDetailFragment;
import com.picaproject.pica.Item.PictuerDetailItem;
import com.picaproject.pica.Util.AppUtility;
import com.picaproject.pica.Util.IntentProtocol;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.NetworkItems.AlbumResultItem;
import com.picaproject.pica.Util.NetworkItems.DefaultResultItem;
import com.picaproject.pica.Util.NetworkItems.ImageResultItem;
import com.picaproject.pica.Util.NetworkUtility;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 이미지를 클릭했을때 자세히 보이는 뷰, 앨범내 존재하는 이미지리스트와, 몇번째 이미지인지 id도 함께 넘어감
 * 삭제하는것도 추가해야함
 */
public class ImageDetailActivity extends BaseToolbarActivity implements View.OnClickListener {

    private ArrayList<ImageResultItem> imageList;
    private ViewPager viewPager;
    private int startId;
    private boolean isShow = true;
    private ImageView showInfo;
    private ScreenSlidePagerAdapter pagerAdapter;
    private RelativeLayout indicatorFrame;
    private TextView indicatorText;
    private Animation scaleAnim;
    private boolean isHearted;
    private ImageView img_favorite;

    private TextView toolbarUserName, toolbarDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        imageList = getIntent().getParcelableArrayListExtra(IntentProtocol.INTENT_ALBUM_IMAGE_LIST);
        startId = getIntent().getIntExtra(IntentProtocol.INTENT_START_POSITION, 0);
        initView();
    }

    private void initView(){
        indicatorFrame = (RelativeLayout) findViewById(R.id.indicator_frame);
        indicatorText = (TextView) findViewById(R.id.indicator_text);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                startId = position;
                refresh();
                ((ImageDetailFragment)viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem())).setInfoView(isShow);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        showInfo = (ImageView) findViewById(R.id.show_img_info);
        showInfo.setOnClickListener(this);
        img_favorite = findViewById(R.id.favorite);
        img_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean s = imageList.get(viewPager.getCurrentItem()).getIsLikePicture();
                doPictureFavoriteTask(AppUtility.memberId, imageList.get(viewPager.getCurrentItem()).getPictureId());
                imageList.get(viewPager.getCurrentItem()).setIsLikePicture((s)? "n":"y");
            }
        });
        findViewById(R.id.comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_info = new Intent(getApplicationContext(), CommentActivity.class);
                intent_info.putExtra(IntentProtocol.INTENT_PICTURE_ID, imageList.get(viewPager.getCurrentItem()).getPictureId());
                startActivity(intent_info);
                overridePendingTransition(R.anim.slide_up, 0);
            }
        });
        findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.toolbar_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePage(viewPager.getCurrentItem());
            }
        });
        loadFavoriteAnimation();

        viewPager.setCurrentItem(startId);
        refresh();

    }

    private void refresh(){
        showIndicatorView();

        setToolbarTitle(imageList.get(startId).getNickname());
        setToolbarSubTitle(imageList.get(startId).getUploadDate());

        if (imageList.get(startId).getIsLikePicture()) {
            isHearted = true;
            changeFavoriteColor(android.R.color.holo_red_light);
        } else {
            isHearted = false;
            changeFavoriteColor(android.R.color.white);
        }

        if (imageList.get(startId).getIsMyUpload()) {
            toolbar.findViewById(R.id.toolbar_remove).setVisibility(View.VISIBLE);
        } else {
            toolbar.findViewById(R.id.toolbar_remove).setVisibility(View.GONE);
        }

    }

    private void doPictureFavoriteTask(int memberId, int pictureId) {
        NetworkUtility.getInstance().doFavortePhoto(memberId, pictureId, new Callback<DefaultResultItem>() {
            @Override
            public void onResponse(Call<DefaultResultItem> call, Response<DefaultResultItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DefaultResultItem item = response.body();
                    switch (item.getCode()) {
                        case NetworkUtility.APIRESULT.RESULT_SUCCESS :
                            img_favorite.startAnimation(scaleAnim);

                            break;
                        case NetworkUtility.APIRESULT.RESULT_FILE_GET_ERROR :
                            Toast.makeText(getApplicationContext(), "서버 오류로 좋아요하지 못했습니다.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResultItem> call, Throwable t) {

            }
        });
    }

    /*
    private void getMyPictureTask(int pictureIndex){
        NetworkUtility.getInstance().getPictureDetail(pictureId, 1, new Callback<PictuerDetailItem>() {
            @Override
            public void onResponse(Call<PictuerDetailItem> call, Response<PictuerDetailItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("pictureDetail_Response", response.body().toString());
                    PictuerDetailItem pictuerDetailItem = response.body();

                    switch (pictuerDetailItem.getCode()) {
                        case NetworkUtility.APIRESULT.RESULT_SUCCESS :
                            ImageResultItem result = pictuerDetailItem.getResult();
                            if (result != null) {

                            }
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
            public void onFailure(Call<PictuerDetailItem> call, Throwable t) {

            }
        });
    }*/

    private void changeFavoriteColor(int color) {
        PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(ContextCompat.getColor(getApplicationContext(), color),
                PorterDuff.Mode.SRC_ATOP);


        img_favorite.setColorFilter(porterDuffColorFilter);
    }
    private void showIndicatorView(){
        indicatorText.setText(getIndicatorString());
        indicatorFrame.setVisibility(View.VISIBLE);
        indicatorFrame.postDelayed(new Runnable() {
            public void run() {
                indicatorFrame.setVisibility(View.GONE);
            }
        }, 800);
    }
    private String getIndicatorString(){
        return (viewPager.getCurrentItem()+1)+"/"+imageList.size();
    }
    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    private void removePage(int position){
        imageList.remove(position);
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        isShow = !isShow;

        //현재 상태를 보여주는 상태로 바꿔야함 (아이콘은 눈깔이 꺼지는걸로
        if (isShow) {
            showInfo.setImageResource(R.drawable.ic_visibility_off_black_24dp);
            ((ImageDetailFragment)viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem())).setInfoView(isShow);
            //((ImageDetailFragment) pagerAdapter.getItem(viewPager.getCurrentItem())).setInfoView(isShow);
        } else {
            showInfo.setImageResource(R.drawable.ic_visibility_black_24dp);
            ((ImageDetailFragment)viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem())).setInfoView(isShow);
            //((ImageDetailFragment) pagerAdapter.getItem(viewPager.getCurrentItem())).setInfoView(isShow);
        }
    }

    private void loadFavoriteAnimation(){
        scaleAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_heart);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int color;
                isHearted = !isHearted;
                if(!isHearted){
                    color = android.R.color.white;
                }else{
                    color = android.R.color.holo_red_light;
                }

                changeFavoriteColor(color);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ImageDetailFragment fragment = new ImageDetailFragment();
            fragment.setPictureData(imageList.get(position), isShow);

            return fragment;
        }

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            int index = imageList.indexOf(object);

            if (index == -1)
                return POSITION_NONE;
            else
                return index;
        }
    }

}
