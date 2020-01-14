package com.picaproject.pica.Listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.picaproject.pica.CustomView.UploadPicActivity;

/*
* 앨범 추가 화면에서 "사진추가" 버튼을 눌렀을때 동작
*
* */
public class AddPicImageButtonClickListener implements View.OnClickListener {
    private Context context;

    public AddPicImageButtonClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        context.startActivity(new Intent(context,UploadPicActivity.class));
    }
}
