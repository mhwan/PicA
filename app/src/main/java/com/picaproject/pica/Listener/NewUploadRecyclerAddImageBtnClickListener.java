package com.picaproject.pica.Listener;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.picaproject.pica.IntentProtocol;

// 사진추가 화면에서 + (사진추가) 버튼을 눌렀을때 동작
public class NewUploadRecyclerAddImageBtnClickListener implements View.OnClickListener {

    private AppCompatActivity activity;

    public NewUploadRecyclerAddImageBtnClickListener(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        // 외부 갤러리 앱 열기
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(intent, IntentProtocol.GET_GALLERY_IMAGE);
        activity.startActivityForResult(Intent.createChooser(intent,"Select Picture"), IntentProtocol.GET_GALLERY_IMAGE);
    }
}
