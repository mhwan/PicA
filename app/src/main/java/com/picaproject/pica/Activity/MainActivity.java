package com.picaproject.pica.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.picaproject.pica.Fragment.MyAlbumFragment;
import com.picaproject.pica.Fragment.UserInfoFragment;
import com.picaproject.pica.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SpaceNavigationView navigationView;
    private ViewPager viewPager;
    private TextView toolbarTitle;
    private String[] titles = {"내 앨범", "내 계정"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView(savedInstanceState);
    }

    private void initView(Bundle instanceState) {
        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("search");
        searchView.setBackgroundResource(R.drawable.bg_rounded_white);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCustom);
        setSupportActionBar(toolbar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTextView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager();


        navigationView = (SpaceNavigationView) findViewById(R.id.spacenavigationview);
        navigationView.initWithSaveInstanceState(instanceState);
        navigationView.addSpaceItem(new SpaceItem("내 앨범", R.drawable.ic_collections_black_24dp));
        navigationView.addSpaceItem(new SpaceItem("내 계정", R.drawable.ic_account_circle_black_24dp));
        navigationView.setCentreButtonRippleColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        navigationView.setCentreButtonIconColorFilterEnabled(false);
        navigationView.showIconOnly();
        addListener();
        changeToolbarName(0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        navigationView.initWithSaveInstanceState(outState);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        MyAlbumFragment albumFragment = new MyAlbumFragment();
        UserInfoFragment userInfoFragment = new UserInfoFragment();
        adapter.addFragment(albumFragment);
        adapter.addFragment(userInfoFragment);
        viewPager.setAdapter(adapter);
    }

    private void changeToolbarName(int i) {
        toolbarTitle.setText(titles[i]);
    }

    private void addListener() {
        navigationView.setSpaceOnClickListener(new SpaceOnClickListener(){
            @Override
            public void onCentreButtonClick() {
                startActivity(new Intent(getApplicationContext(), CreateAlbumActivity.class));
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                if (itemIndex >= 0)
                    viewPager.setCurrentItem(itemIndex);
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigationView.changeCurrentItem(position);
                changeToolbarName(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

    }
}
