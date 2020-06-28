package com.picaproject.pica.CustomView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import androidx.fragment.app.FragmentStatePagerAdapter;


import com.picaproject.pica.Fragment.NewAlbumUploadFragment;
import com.picaproject.pica.Item.UploadImageItem;

import java.util.ArrayList;

public class NewAlbumUploadAdapter extends FragmentStatePagerAdapter {
    private ArrayList<NewAlbumUploadFragment> items = new ArrayList<NewAlbumUploadFragment>();

    //어댑터 안에서 각각의 아이템을 데이터로서 관리한다
    public NewAlbumUploadAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(NewAlbumUploadFragment fragment){
        items.add(fragment);
    }
    // 주어진 인덱스의 UploadPicData를 변경한다.
    // 성공시 True 실패시 False 리턴
    public boolean setDataAtIndex(int index, UploadImageItem data){
        NewAlbumUploadFragment f = items.get(index);
        if(f!=null){
            f.setUploadImageItem(data);
            return true;
        }
        return false;
    }
    // addFragment로 추가된 Fragment를 여기서 리턴해줌.
    @Override
    public NewAlbumUploadFragment getItem(int position) {
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
