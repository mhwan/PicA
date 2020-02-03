package com.picaproject.pica.Activity;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.picaproject.pica.CustomView.NewAlbumUploadAdapter;
import com.picaproject.pica.CustomView.NewAlbumUploadPicAdapter;
import com.picaproject.pica.CustomView.SpacesItemDecoration;
import com.picaproject.pica.CustomView.UploadPicController;
import com.picaproject.pica.Fragment.NewAlbumUploadFragment;
import com.picaproject.pica.IntentProtocol;
import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.Listener.NewUploadRecyclerAddImageBtnClickListener;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.PermissionChecker;
import com.picaproject.pica.Util.PicDataParser;

import java.util.ArrayList;

public class NewAlbumUploadActivity extends BaseToolbarActivity {
    private Button backBtn;
    private Button submitBtn;
    private ViewPager viewPager;
    private RecyclerView uploadPicListView;
    private NewAlbumUploadAdapter adapter;
    private PermissionChecker pc;
    private NewAlbumUploadPicAdapter recyclerAdapter;
    private UploadPicController controller;
    // 리사이클러용 데이터 리스트랑 Fragment용 데이터리스트는 따로 둠
    // recyclerDataList에는 항상 첫번째에 ADD_BTN 데이터가 들어가고
    // Fragment에는 이런 ADD_BTN 데이터가 영향을 미치지 않기 위함.
    private ArrayList<UploadPicData> recyclerDataList;
    private ArrayList<UploadPicData> dataList;
    String[] permission_list = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private Handler handler;

    // 마지막 위치 저장하면서 사용하기
    // 위치가 바뀌었을때만 visible 명령을 사용하여 오버헤딩을 방지하기위함.
    private int lastPage=-99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_album_upload);
        findViewById(R.id.image_upload).setOnClickListener(new NewUploadRecyclerAddImageBtnClickListener(this));
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        uploadPicListView = (RecyclerView) findViewById(R.id.upload_pic_list_view);
        adapter = new NewAlbumUploadAdapter(getSupportFragmentManager());
        recyclerDataList = new ArrayList<>();
        Intent intent = getIntent();
        dataList = (ArrayList<UploadPicData>)intent.getSerializableExtra(IntentProtocol.PIC_DATA_LIST_NAME);
        viewPager.setOffscreenPageLimit(dataList.size());
        // 데이터를 가져와서 플래그먼트로 변환해 추가하기
        controller = new UploadPicController(uploadPicListView,viewPager,this);
        addFragment(dataList);
        viewPager.setAdapter(adapter);


        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        uploadPicListView.setLayoutManager(mLinearLayoutManager);

        recyclerDataList.addAll(dataList);



        recyclerAdapter = new NewAlbumUploadPicAdapter(recyclerDataList,this,controller);
        uploadPicListView.setAdapter(recyclerAdapter);

        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(SpacesItemDecoration.RecyclerViewOrientation.LINEAR_HORIZONTAL, 10);

        uploadPicListView.addItemDecoration(spacesItemDecoration);
        //picAdapter.notifyDataSetChanged();

        // 일일히 리스너에 이벤트를 붙여가며 왔다갔다하지않아도 아이템을 실시간으로 조정해보는 신세계
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                int currentPage = viewPager.getCurrentItem();
                // 위치가 바뀌었을때만 visible 명령을 사용하여 오버헤딩을 방지하기위함.
                if(currentPage!=lastPage){

                }
                controller.setBoder(currentPage,dataList.size());
                lastPage = currentPage;
                // 100 밀리초마다 반복수행
                handler.sendEmptyMessageDelayed(0,100);
            }
        };

        handler.sendEmptyMessage(0);
        //필수 2줄
        pc = new PermissionChecker(permission_list,this);
        pc.checkPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();

        setToolbarTitle("사진");
        setToolbarButton("업로드", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //사진이 등록될때의 행동
            }
        });
    }

    // 데이터를 가져와서 플래그먼트로 변환해 추가하기
    private void addFragment(ArrayList<UploadPicData> dataList){
        for(int i=0; i<dataList.size();i++){
            UploadPicData d = dataList.get(i);
            NewAlbumUploadFragment f = new NewAlbumUploadFragment();
            f.setUploadPicData(d);
            f.setActivity(this);
            f.setController(controller);
            adapter.addFragment(f);
        }
    }
    //  플래그먼트 제거하기
    private void removeFragment(int i){
        adapter.deletePage(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //필수
        pc.requestPermissionsResult(requestCode,grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("test_hs","AlbumMainActivity  : ");
        if(requestCode == IntentProtocol.GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null){
            ClipData datas = data.getClipData();
            ArrayList<UploadPicData> prasePicDatas;
            // 사진 여러장 선택시
            if(datas!=null){
                prasePicDatas = PicDataParser.parseDataFromClipData(datas);
                // 갤러리에서 가져온 n장의 사진 처리
                Log.i("test_hs","AlbumMainActivity onActivityResult : "+prasePicDatas.toString());

            }
            // 사진 1장 선택시
            else if(data.getData()!=null){
                prasePicDatas = new ArrayList<>();
                prasePicDatas.add(new UploadPicData(data.getData().toString()));
                Log.i("test_hs","AlbumMainActivity onActivityResult 2 : "+prasePicDatas.toString());
            }
            // 사진 선택 안했을시 아무 동작 안함.
            else{
                return;
            }
            // 가져온 이미지를 UploadPicData 데이터로 만드는건 했고
            // 이제 만든 UploadPicData의 처리를 밑에서 하면 됨.
            // --밑 --
            recyclerDataList.addAll(prasePicDatas);
            dataList.addAll(prasePicDatas);

            addFragment(prasePicDatas);
            adapter.notifyDataSetChanged();
            recyclerAdapter.notifyDataSetChanged();

            /*
            Intent intent = new Intent(this, NewAlbumUploadActivity.class);
            intent.putExtra(IntentProtocol.PIC_DATA_LIST_NAME,prasePicDatas);
            startActivityForResult(intent, IntentProtocol.ADD_PIC_MULTI_MODE);
            */


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void removeItem(int itemIdx){
        recyclerDataList.remove(itemIdx);
        dataList.remove(itemIdx);
        removeFragment(itemIdx);
        adapter.notifyDataSetChanged();
        recyclerAdapter.notifyDataSetChanged();
    }

}
