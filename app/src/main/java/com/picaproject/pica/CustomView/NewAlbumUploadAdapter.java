package com.picaproject.pica.CustomView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.ArrayList;

public class NewAlbumUploadAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> items = new ArrayList<Fragment>();

    //어댑터 안에서 각각의 아이템을 데이터로서 관리한다
    public NewAlbumUploadAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment){
        items.add(fragment);
    }
    // addFragment로 추가된 Fragment를 여기서 리턴해줌.
    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
