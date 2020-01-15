package com.picaproject.pica.Listener;

import android.view.View;

import com.picaproject.pica.Activity.UploadPicActivity;

/*
* 사진추가/수정화면에서 "등록" 버튼을 눌렀을때 리스너
* */
public class UploadPicSubmitButtonClickListener implements View.OnClickListener {

    private UploadPicActivity activity;

    public UploadPicSubmitButtonClickListener(UploadPicActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        activity.submit();
    }
}
