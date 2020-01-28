package com.picaproject.pica.Listener;

import android.view.View;

import com.picaproject.pica.CustomView.UploadPicController;

// 사진 추가 화면 하단의 리사이클러 뷰속 이미지 뷰를 클릭했을때 동작을 정의하는 클래스
public class NewUploadRecyclerImageViewClickListener implements View.OnClickListener {
    private UploadPicController controller;
    private int idx;

    public NewUploadRecyclerImageViewClickListener(UploadPicController controller, int idx) {
        this.controller = controller;
        this.idx = idx;
    }

    @Override
    public void onClick(View view) {
        // 주의 : 리사이클러는 "사진추가" 아이템이 맨 앞에 추가되어 플래그먼트의 아이템과 인덱스가 다름
        // 리사이클러에서 플래그먼트랑 동작을 맞추려면 -1을 해주자.
        controller.moveViewPagerPage(idx-1);
    }
}
