package com.picaproject.pica.Activity;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.picaproject.pica.CustomView.NewAlbumUploadAdapter;
import com.picaproject.pica.CustomView.NewAlbumUploadPicAdapter;
import com.picaproject.pica.CustomView.SpacesItemDecoration;
import com.picaproject.pica.CustomView.UploadPicController;
import com.picaproject.pica.Fragment.InputBottomSheetDialogFragment;
import com.picaproject.pica.Fragment.NewAlbumUploadFragment;
import com.picaproject.pica.Util.IntentProtocol;
import com.picaproject.pica.Item.PicPlaceData;
import com.picaproject.pica.Item.UploadImageItem;
import com.picaproject.pica.Listener.NewUploadRecyclerAddImageBtnClickListener;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.ImageMetadataParser;
import com.picaproject.pica.Util.NetworkItems.DefaultResultItem;
import com.picaproject.pica.Util.NetworkUtility;
import com.picaproject.pica.Util.PermissionChecker;
import com.picaproject.pica.Util.PicDataParser;
import com.picaproject.pica.Util.RetrofitRetryableCallback;
import com.yalantis.ucrop.UCrop;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAlbumUploadActivity extends BaseToolbarActivity {
    private ViewPager viewPager;
    private RecyclerView uploadPicListView;
    private NewAlbumUploadAdapter adapter;
    private PermissionChecker pc;
    private NewAlbumUploadPicAdapter recyclerAdapter;
    private UploadPicController controller;
    private int cropIndex = -1;
    private RelativeLayout resize, photoFilter, locations, contents;

    private ArrayList<UploadImageItem> dataList;
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
        Intent intent = getIntent();
        dataList = (ArrayList<UploadImageItem>)intent.getSerializableExtra(IntentProtocol.PIC_DATA_LIST_NAME);
        viewPager.setOffscreenPageLimit(dataList.size());
        // 데이터를 가져와서 플래그먼트로 변환해 추가하기
        controller = new UploadPicController(uploadPicListView,viewPager,this);
        addFragment(dataList);
        viewPager.setAdapter(adapter);


        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        uploadPicListView.setLayoutManager(mLinearLayoutManager);




        recyclerAdapter = new NewAlbumUploadPicAdapter(dataList,this,controller);
        uploadPicListView.setAdapter(recyclerAdapter);

        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(10,10,0,0);

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

        initBottomManageView();

        handler.sendEmptyMessage(0);
        //필수 2줄
        pc = new PermissionChecker(permission_list,this);
        pc.checkPermission("외부 저장소 접근 권한이 거부되었습니다. 사진을 불러오려면 권한을 허용해주세요.");
    }

    @Override
    protected void onStart() {
        super.onStart();

        setToolbarTitle("사진");
        setToolbarButton("업로드", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataList != null && dataList.size() > 0)
                    uploadImageWork();
                else
                    Toast.makeText(getApplicationContext(), "업로드할 사진이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * datalist에 있는 index를 차례대로 upload를 한다. 성공하면 다행이라지만 실패할 경우에 어떻게 할까
     */
    private void uploadImageWork(){
        for (int i = 0; i < dataList.size(); i++) {
            uploadSingleImageWork(i);
        }
    }
    private void uploadSingleImageWork(int index){
        NetworkUtility.getInstance().uploadSinglePictureToAlbum(1, 1, dataList.get(index), new Callback<DefaultResultItem>() {
            @Override
            public void onResponse(Call<DefaultResultItem> call, Response<DefaultResultItem> response) {
                DefaultResultItem defaultResultItem = response.body();

                if (response.isSuccessful() && defaultResultItem != null) {
                    Log.d("image upload", ""+defaultResultItem.getCode());
                    switch (defaultResultItem.getCode()) {
                        case NetworkUtility.APIRESULT.RESULT_SUCCESS:
                            Toast.makeText(getApplicationContext(), "사진이 업로드 되었습니다.", Toast.LENGTH_SHORT).show();

                            break;
                        case NetworkUtility.APIRESULT.RESULT_CREATEALBUM_SERVER_ERROR:
                            Toast.makeText(getApplicationContext(), "서버오류로 사진 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show();

                            break;
                        case NetworkUtility.APIRESULT.RESULT_CREATEALBUM_DB_ERROR:
                            Toast.makeText(getApplicationContext(), "DB오류로 사진 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show();

                            break;
                    }
                } else
                    Log.d("image upload", "failed");
            }

            @Override
            public void onFailure(Call<DefaultResultItem> call, Throwable t) {

            }
        });
    }
    private void initBottomManageView(){
        locations = findViewById(R.id.locations);
        contents = findViewById(R.id.contents);
        resize = findViewById(R.id.resize);
        photoFilter = findViewById(R.id.filter);

        locations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.openLoactionView(dataList.get(viewPager.getCurrentItem()));
            }
        });
        contents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showContentsInput();
                //changeEditMdoe();
                cropIndex = viewPager.getCurrentItem();
                controller.openInputContentsView(dataList.get(viewPager.getCurrentItem()));
            }
        });

        resize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropIndex = viewPager.getCurrentItem();
                controller.openCropImage(dataList.get(viewPager.getCurrentItem()), getCacheDir());
            }
        });

        photoFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropIndex = viewPager.getCurrentItem();
                controller.openFilterEffectImage(dataList.get(viewPager.getCurrentItem()));
            }
        });
    }
    // 데이터를 가져와서 플래그먼트로 변환해 추가하기
    private void addFragment(ArrayList<UploadImageItem> dataList){
        for(int i=0; i<dataList.size();i++){
            UploadImageItem d = dataList.get(i);
            NewAlbumUploadFragment f = new NewAlbumUploadFragment();
            f.setUploadImageItem(d);
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
        pc.requestPermissionsResult(requestCode,grantResults, "외부저장소 접근권한을 허용 후에 이용해주세요.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("test_hs","AlbumMainActivity  : ");
        if(requestCode == IntentProtocol.GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null){
            ClipData datas = data.getClipData();
            ArrayList<UploadImageItem> prasePicDatas;
            // 사진 여러장 선택시
            if(datas!=null){
                prasePicDatas = PicDataParser.parseDataFromClipData(getApplicationContext(), datas);
                // 갤러리에서 가져온 n장의 사진 처리
                Log.i("test_hs","NewAlbumUploadActivity onActivityResult : "+prasePicDatas.toString());

            }
            // 사진 1장 선택시
            else if(data.getData()!=null){
                Uri uri = data.getData();
                PicPlaceData placeData = ImageMetadataParser.getLocationMetaData(getApplicationContext(), uri);
                UploadImageItem uploadImageItem = new UploadImageItem(data.getData().toString());
                if (placeData != null)
                    uploadImageItem.setLocation(placeData);

                prasePicDatas = new ArrayList<>();
                prasePicDatas.add(uploadImageItem);
                Log.i("test_hs","NewAlbumUploadActivity onActivityResult 2 : "+prasePicDatas.toString());
            }
            // 사진 선택 안했을시 아무 동작 안함.
            else{
                return;
            }
            // 가져온 이미지를 UploadPicData 데이터로 만드는건 했고
            // 이제 만든 UploadPicData의 처리를 밑에서 하면 됨.
            // --밑 --
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
        //if(requestCode == IntentProtocol.UPDATE_PIC_MODE){
        if(resultCode == RESULT_OK && requestCode == IntentProtocol.SET_PIC_LOCATION && data != null){
            //TODO
            UploadImageItem picData = (UploadImageItem)data.getParcelableExtra(IntentProtocol.PIC_DATA_CLASS_NAME);
            int idx = serchItemIndex(picData.getClassId());
            if(idx!=-1){
                updateSpecificData(idx, picData);
            }
            Log.i("test_hs","NewAlbumUploadActivity SET_PIC_LOCATION : "+picData.getLocation().toString());
        }
         else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri mResultUri = UCrop.getOutput(data);
            if (mResultUri != null) {
                // ResultActivity.startWithUri(MainActivity.this, resultUri);
                if (cropIndex >= 0) {
                    Log.d("crop Index", cropIndex+"");
                    UploadImageItem picData = dataList.get(cropIndex);
                    picData.setSrc(mResultUri.toString());

                    updateSpecificData(serchItemIndex(picData.getClassId()), picData);
                }

            } else {
                Toast.makeText(getApplicationContext(), "이미지 자르기에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == UCrop.RESULT_ERROR)
            handleCropError(data);
         else if (resultCode == RESULT_OK && requestCode == IntentProtocol.REQUEST_IMAGE_FILTER) {
            String src = data.getStringExtra(IntentProtocol.INTENT_FILTER_OUTPUT);
            UploadImageItem picData = dataList.get(cropIndex);
            picData.setSrc(src);

            updateSpecificData(serchItemIndex(picData.getClassId()), picData);
        } else if (resultCode == RESULT_OK && requestCode == IntentProtocol.REQUEST_EDIT_CONTENTS) {
             String newContents = data.getStringExtra(IntentProtocol.INTENT_OUTPUT_CONTENT);
             ArrayList<String> newTags = data.getStringArrayListExtra(IntentProtocol.INTENT_OUTPUT_TAGS);

             UploadImageItem picData = dataList.get(cropIndex);
             picData.setContents(newContents);
             picData.setTags(newTags);

             updateSpecificData(serchItemIndex(picData.getClassId()), picData);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateSpecificData(int idx, UploadImageItem picData) {
        dataList.set(idx,picData);
        adapter.setDataAtIndex(idx,picData);
        adapter.notifyDataSetChanged();
        recyclerAdapter.notifyDataSetChanged();
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e("Crop", "handleCropError: ", cropError);
            Toast.makeText(getApplicationContext(), cropError.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "예상하지 못한 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeItem(int itemIdx){
        dataList.remove(itemIdx);
        removeFragment(itemIdx);
        adapter.notifyDataSetChanged();
        recyclerAdapter.notifyDataSetChanged();
    }

    // UploadPIcData의 ID를 기반으로 몇번째에 데이터가 있는지 인덱스를 찾아서
    // 반환함. 못찾았을경우 -1 반환
    private int serchItemIndex(long dataId){
        int cnt;
        for(cnt=0;cnt<dataList.size();cnt++){
            long itemId = dataList.get(cnt).getClassId();
            if(dataId==itemId)
                return cnt;
        }
        return -1;
    }

}
