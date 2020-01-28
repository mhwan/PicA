package com.picaproject.pica.Activity;

import android.Manifest;
import android.content.Intent;

import android.support.annotation.NonNull;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.widget.Button;

import com.picaproject.pica.CustomView.NewAlbumUploadAdapter;
import com.picaproject.pica.CustomView.NewAlbumUploadPicAdapter;
import com.picaproject.pica.CustomView.UploadPicController;
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
    private ArrayList<UploadPicData> dataList;
    // 리사이클러용 데이터 리스트랑 Fragment용 데이터리스트는 따로 둠
    // recyclerDataList에는 항상 첫번째에 ADD_BTN 데이터가 들어가고
    // Fragment에는 이런 ADD_BTN 데이터가 영향을 미치지 않기 위함.
    private ArrayList<UploadPicData> recyclerDataList;
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
        recyclerDataList = new ArrayList<>();
        Intent intent = getIntent();
        dataList = (ArrayList<UploadPicData>)intent.getSerializableExtra(IntentProtocol.PIC_DATA_LIST_NAME);
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

        recyclerDataList.addAll(dataList);
        recyclerDataList.add(0,addBtnData);

        UploadPicController controller = new UploadPicController(uploadPicListView,viewPager);

        recyclerAdapter = new NewAlbumUploadPicAdapter(recyclerDataList,this,controller);
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
            f.setActivity(this);
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
