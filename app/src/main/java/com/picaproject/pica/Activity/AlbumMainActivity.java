package com.picaproject.pica.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.picaproject.pica.CustomView.InvitedFriendsRecyclerAdapter;
import com.picaproject.pica.Fragment.AlbumMainFragment;
import com.picaproject.pica.Fragment.UserInfoFragment;
import com.picaproject.pica.IntentProtocol;
import com.picaproject.pica.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumMainActivity extends AppCompatActivity {

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : 외부 갤러리 앱 바로 연동
                //startActivity(new Intent(AlbumMainActivity.this, UploadAlbumActivity.class));

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
        adapter.addFragment(new AlbumMainFragment(), "사진보기");
        adapter.addFragment(new UserInfoFragment(), "기타");
        viewPager.setAdapter(adapter);
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
}
