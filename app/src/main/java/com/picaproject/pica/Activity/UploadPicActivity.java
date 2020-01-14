package com.picaproject.pica.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.R;

public class UploadPicActivity extends AppCompatActivity {

    private UploadPicData picData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pic);
        Intent intent = getIntent();
        UploadPicData picData = (UploadPicData)intent.getSerializableExtra("UploadPicData");
        this.picData=picData;
        Log.i("test_hs","UploadPicActivity : "+picData.getContents());
    }



}
