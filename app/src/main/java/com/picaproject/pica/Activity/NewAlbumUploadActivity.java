package com.picaproject.pica.Activity;

import android.Manifest;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.picaproject.pica.CustomView.NewAlbumUploadAdapter;
import com.picaproject.pica.CustomView.NewAlbumUploadPicAdapter;
import com.picaproject.pica.Fragment.NewAlbumUploadFragment;
import com.picaproject.pica.IntentProtocol;
import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.PermissionChecker;

import java.util.ArrayList;

public class NewAlbumUploadActivity extends AppCompatActivity {
    private Button backBtn;
    private Button submitBtn;
    private ViewPager viewPager;
    private RecyclerView uploadPicListView;
    private NewAlbumUploadAdapter adapter;
    private PermissionChecker pc;
    private NewAlbumUploadPicAdapter recyclerAdapter;
    String[] permission_list = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_album_upload);
        backBtn = (Button) findViewById(R.id.back_btn);
        submitBtn = (Button) findViewById(R.id.submit_btn);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        uploadPicListView = (RecyclerView) findViewById(R.id.upload_pic_list_view);
        adapter = new NewAlbumUploadAdapter(getSupportFragmentManager());

        Intent intent = getIntent();
        ArrayList<UploadPicData> dataList = (ArrayList<UploadPicData>)intent.getSerializableExtra(IntentProtocol.PIC_DATA_LIST_NAME);
        viewPager.setOffscreenPageLimit(dataList.size());
        // 데이터를 가져와서 플래그먼트로 변환해 추가하기
        addFragment(dataList);
        viewPager.setAdapter(adapter);


        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        uploadPicListView.setLayoutManager(mLinearLayoutManager);
        // 추가 버튼 역할을 해줄 데이터
        UploadPicData addBtnData = new UploadPicData("NULL");

        addBtnData.setContents(UploadPicData.ADD_BTN);
        dataList.add(0,addBtnData);
        recyclerAdapter = new NewAlbumUploadPicAdapter(dataList);
        uploadPicListView.setAdapter(recyclerAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(uploadPicListView.getContext(),
                mLinearLayoutManager.getOrientation());
        uploadPicListView.addItemDecoration(dividerItemDecoration);
        //picAdapter.notifyDataSetChanged();

        //필수 2줄
        pc = new PermissionChecker(permission_list,this);
        pc.checkPermission();
    }

    // 데이터를 가져와서 플래그먼트로 변환해 추가하기
    private void addFragment(ArrayList<UploadPicData> dataList){
        for(UploadPicData d : dataList){
            NewAlbumUploadFragment f = new NewAlbumUploadFragment();
            f.setUploadPicData(d);
            adapter.addFragment(f);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //필수
        pc.requestPermissionsResult(requestCode,grantResults);
    }

}
