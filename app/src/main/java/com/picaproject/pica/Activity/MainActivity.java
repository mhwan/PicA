package com.picaproject.pica.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.picaproject.pica.Fragment.MyAlbumFragment;
import com.picaproject.pica.Fragment.UserInfoFragment;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.IntentProtocol;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SpaceNavigationView navigationView;
    private ViewPager viewPager;
    private MyAlbumFragment albumFragment;
    private UserInfoFragment userInfoFragment;
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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() >= 1){
                    Intent intent = new Intent(MainActivity.this, PictureSearchActivity.class);
                    intent.putExtra(IntentProtocol.INTENT_QUERY, query);
                    startActivity(intent);
                } else
                    Toast.makeText(getApplicationContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCustom);
        setSupportActionBar(toolbar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTextView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.findViewById(R.id.toolbar_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });

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
        albumFragment = new MyAlbumFragment();
        userInfoFragment = new UserInfoFragment();
        adapter.addFragment(albumFragment);
        adapter.addFragment(userInfoFragment);
        viewPager.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == IntentProtocol.CREATE_MANAGE_ALBUM) {

            Log.d("refresh main view", "refresh");
            albumFragment.updateMyAlbum();
            userInfoFragment.refreshAllData();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void changeToolbarName(int i) {
        toolbarTitle.setText(titles[i]);
    }

    private void addListener() {
        navigationView.setSpaceOnClickListener(new SpaceOnClickListener(){
            @Override
            public void onCentreButtonClick() {
                startActivityForResult(new Intent(getApplicationContext(), CreateAlbumActivity.class), IntentProtocol.CREATE_MANAGE_ALBUM);
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
