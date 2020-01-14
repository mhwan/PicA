package com.picaproject.pica.Listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.picaproject.pica.Activity.UploadPicActivity;
import com.picaproject.pica.Item.UploadPicData;

/*
* 앨범 추가 화면에서 "사진추가" 버튼 혹은 사진 리스트중에 하나를
* 클릭했을때 동작
*
* */
public class PicImageButtonClickListener implements View.OnClickListener {
    private Context context;
    private UploadPicData data;

    public PicImageButtonClickListener(Context context, UploadPicData data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context,UploadPicActivity.class);
        intent.putExtra("UploadPicData",data);
        context.startActivity(intent);
    }
    /*
    * 사진 추가/수정 액티비티에서
    * 이미 동일한 Uri가 있는 액티비티일 경우 내용만 변경?
    *
    * */
}
