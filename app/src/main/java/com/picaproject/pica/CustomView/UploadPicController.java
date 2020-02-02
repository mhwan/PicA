package com.picaproject.pica.CustomView;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.picaproject.pica.Activity.NewAlbumUploadActivity;

/*
 * UploadPic 화면 Ui에서 리사이클러 <-> 플래그먼트 <-> 액티비티가 서로 이벤트를 주고받기위한 컨트롤러
 * 리사이클러의 이미지를 터치할때마다 플래그먼트의 이미지가 리사이클러의 이미지로 변경된다.
 * */
public class UploadPicController {
    private RecyclerView recyclerView;
    private ViewPager viewPager;
    private NewAlbumUploadActivity activity;

    public UploadPicController(RecyclerView recyclerView, ViewPager viewPager, NewAlbumUploadActivity activity) {
        this.recyclerView = recyclerView;
        this.viewPager = viewPager;
        this.activity = activity;
    }

    // 뷰페이저 페이지 이동하기
    public int moveViewPagerPage(int page){
        viewPager.setCurrentItem(page);
        return page;
    }


    public View getRecyclerBorder(int pos){
        NewUploadPicHolder h = (NewUploadPicHolder)recyclerView.findViewHolderForAdapterPosition(pos);
        if(h==null)
            return null;
        return h.getBorderView();
    }

    /*
    * 리사이클러 뷰의 아이템에서 X 버튼이 눌렸을때 해당 아이템(사진) 목록에서 제거하기
    * */
    public int removePic(int picPossition){
        activity.removeItem(picPossition);
        return picPossition;
    }





}
