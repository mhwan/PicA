package com.picaproject.pica.Listener;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.picaproject.pica.Activity.UploadPicActivity;
import com.picaproject.pica.IntentProtocol;
import com.picaproject.pica.Item.UploadPicData;

/*
* 사진추가 화면에서 이미지 뷰
* 클릭했을때 동작
* 외부 갤러리를 호출하고 외부 갤러리에서 가져온 이미지 정보를 가져옴
* */
public class PicImageViewClickListener implements View.OnClickListener {

    private AppCompatActivity context;
    private Fragment fragment;
    public PicImageViewClickListener(AppCompatActivity context) {
        this.context = context;
    }
    public PicImageViewClickListener(Fragment fragment) { this.fragment = fragment; }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        if (context != null)
            context.startActivityForResult(intent, IntentProtocol.GET_GALLERY_IMAGE);
        else if (fragment != null)
            fragment.startActivityForResult(intent, IntentProtocol.GET_GALLERY_IMAGE);
    }

}
