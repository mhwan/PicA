package com.picaproject.pica.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.picaproject.pica.Fragment.ImageDetailFragment;
import com.picaproject.pica.IntentProtocol;
import com.picaproject.pica.Item.ImageItem;
import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.R;

import java.util.ArrayList;


/**
 * 이미지를 클릭했을때 자세히 보이는 뷰, 앨범내 존재하는 이미지리스트와, 몇번째 이미지인지 id도 함께 넘어감
 * 삭제하는것도 추가해야함
 */
public class ImageDetailActivity extends BaseToolbarActivity implements View.OnClickListener {

    private ArrayList<UploadPicData> imageList;
    private ViewPager viewPager;
    private int startId;
    private boolean isShow = true;
    private ImageView showInfo;
    private ScreenSlidePagerAdapter pagerAdapter;
    private RelativeLayout indicatorFrame;
    private TextView indicatorText;

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
                showIndicatorView();
                ((ImageDetailFragment)viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem())).setInfoView(isShow);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(startId);
        showInfo = (ImageView) findViewById(R.id.show_img_info);
        showInfo.setOnClickListener(this);
        findViewById(R.id.favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if (startId == 0)
            showIndicatorView();
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
        setToolbarTitle("Mhwan");
        setToolbarSubTitle("2020/02/06");
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

    }

}
