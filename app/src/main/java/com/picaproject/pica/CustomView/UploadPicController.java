package com.picaproject.pica.CustomView;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
/*
 * UploadPic 화면 Ui에서 리사이클러 <-> 플래그먼트가 서로 이벤트를 주고받기위한 컨트롤러
 * 리사이클러의 이미지를 터치할때마다 플래그먼트의 이미지가 리사이클러의 이미지로 변경된다.
 * */
public class UploadPicController {
    private RecyclerView recyclerView;
    private ViewPager viewPager;

    public UploadPicController(RecyclerView recyclerView, ViewPager viewPager) {
        this.recyclerView = recyclerView;
        this.viewPager = viewPager;
    }
    // 뷰페이저 페이지 이동하기
    public int moveViewPagerPage(int page){
        viewPager.setCurrentItem(page);
        return page;
    }
}
