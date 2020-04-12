package com.picaproject.pica.Activity;

        import android.Manifest;
        import android.content.Intent;
        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import android.os.Bundle;

        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import android.util.Log;
        import com.picaproject.pica.CustomView.AlbumUploadPicAdapter;
        import com.picaproject.pica.CustomView.SpacesItemDecoration;
        import com.picaproject.pica.IntentProtocol;
        import com.picaproject.pica.Item.UploadPicData;
        import com.picaproject.pica.R;
        import com.picaproject.pica.Util.PermissionChecker;

        import java.util.ArrayList;

public class UploadAlbumActivity extends AppCompatActivity {

    private RecyclerView updaloadPicListView;
    private ArrayList<UploadPicData> dataList;
    private AlbumUploadPicAdapter picAdapter ;
    private PermissionChecker pc;

    String[] permission_list = {
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS
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
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(SpacesItemDecoration.RecyclerViewOrientation.LINEAR_HORIZONTAL, 10);
        updaloadPicListView.addItemDecoration(spacesItemDecoration);

        addEOFPicData();
        picAdapter.notifyDataSetChanged();
        //필수 2줄
        pc = new PermissionChecker(permission_list,this);
        pc.checkPermission("외부 저장소 접근 권한이 거부되었습니다. 사진을 불러오려면 권한을 허용해주세요.");
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //필수
        pc.requestPermissionsResult(requestCode,grantResults, "외부저장소 접근권한을 허용 후에 이용해주세요.");
    }

}

