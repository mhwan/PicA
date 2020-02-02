package com.picaproject.pica.CustomView;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentStatePagerAdapter;


import java.util.ArrayList;

public class NewAlbumUploadAdapter extends FragmentStatePagerAdapter {
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

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
    // Delete a page at a `position`
    public void deletePage(int position)
    {
        // Remove the corresponding item in the data set
        items.remove(position);
        // Notify the adapter that the data set is changed
        notifyDataSetChanged();
    }
}
