package com.picaproject.pica.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.picaproject.pica.CustomView.AlbumUploadPicAdapter;
import com.picaproject.pica.IntentProtocol;
import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.R;

import java.util.ArrayList;

public class UploadAlbumActivity extends AppCompatActivity {

    private RecyclerView updaloadPicListView;
    private ArrayList<UploadPicData> dataList;
    private AlbumUploadPicAdapter picAdapter ;


    String[] permission_list = {
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /*
      dataList.add(data);
    * picAdapter.notifyDataSetChanged();
    *
    *
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_album);


        updaloadPicListView = (RecyclerView) findViewById(R.id.updaloadPicListView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        updaloadPicListView.setLayoutManager(mLinearLayoutManager);
        dataList = new ArrayList<UploadPicData>();
        picAdapter = new AlbumUploadPicAdapter(this,dataList);
        updaloadPicListView.setAdapter(picAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(updaloadPicListView.getContext(),
                mLinearLayoutManager.getOrientation());
        updaloadPicListView.addItemDecoration(dividerItemDecoration);

        addEOFPicData();
        picAdapter.notifyDataSetChanged();
        checkPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(data == null){
            Log.w("test_hs","UploadAlbumActivity onActivityResult : Intent data is Null");
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        UploadPicData picData = (UploadPicData)data.getSerializableExtra(IntentProtocol.PIC_DATA_CLASS_NAME);
        // 사진 추가 모드일경우
        if(requestCode == IntentProtocol.ADD_PIC_MODE){
            Log.i("test_hs","UploadAlbumActivity onActivityResult2 : "+dataList.toString());
            addPicData(picData);

        }
        // 사진 수정 모드일경우
        if(requestCode == IntentProtocol.UPDATE_PIC_MODE){
            Log.i("test_hs","UploadAlbumActivity onActivityResult3 : "+dataList.toString());
            int index = data.getIntExtra(IntentProtocol.INTENT_FLAG_DATA_INDEX,-1);
            if(index!=-1){
                updatePicData(picData,index);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
     *  Uri가 NULL이고 contents값이 "EOF" 문자열인 UploadPicData를
     *  dataList 끝에 집어넣는다.
     *  이 EOF 데이터는 RecyclerView에서 onBindViewHolder를 통해 View를 실제로 그려줄때
     *  "사진 추가" 화면으로 이동하는 버튼 역할을 한다.
     *  dataList에 새 데이터를 추가하기 전에 dataList 끝에 존재할 EOF 데이터를 반드시 삭제하고 추가해야한다.
     *  또한 새 데이터가 추가된 후에도 끝을 표시하는 EOF가 필요하다
     * */
    private void addEOFPicData(){
        UploadPicData eofData = new UploadPicData(null);
        eofData.setContents(UploadPicData.STATE_EOF);
        dataList.add(eofData);
    }
    /*
     *  반드시 dataList에 데이터가 추가되기 전에 호출되어 EOF를 제거한 후
     *  addEOFPicData를 호출하여 다시 EOF를 표시하는 데이터를 추가해야 한다.
     *
     * */
    private void removeEOFPicData(){
        dataList.remove(dataList.size()-1);
    }

    private void addPicData(UploadPicData data){
        // Array 가장 끝에 있을 EOF 제거
        removeEOFPicData();
        dataList.add(data);
        addEOFPicData();
        // 리사이클 뷰 새로고침
        picAdapter.notifyDataSetChanged();

        Log.i("test_hs","UploadAlbumActivity addPicData : "+dataList.toString());
    }

    private void updatePicData(UploadPicData data,int index){
        dataList.set(index,data);
        // 리사이클 뷰 새로고침
        picAdapter.notifyDataSetChanged();
    }

    public void checkPermission(){
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int chk = checkCallingOrSelfPermission(permission);

            if(chk == PackageManager.PERMISSION_DENIED){
                //권한 허용을여부를 확인하는 창을 띄운다
                requestPermissions(permission_list,0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0)
        {
            for(int i=0; i<grantResults.length; i++)
            {
                //허용됬다면
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                }
                else {
                    Toast.makeText(getApplicationContext(),"저장소 접근 권한을 허용해주셔야 이용이 가능합니다.",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

}

