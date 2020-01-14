package com.picaproject.pica.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.picaproject.pica.CustomView.AlbumUploadPicAdapter;
import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.R;

import java.util.ArrayList;

public class UploadAlbumActivity extends AppCompatActivity {

    private RecyclerView updaloadPicListView;
    private ArrayList<UploadPicData> dataList;
    private AlbumUploadPicAdapter picAdapter ;


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
        // 최근 이미지 목록은 이 액티비티 시작시 바로 얻어옴

        updaloadPicListView = (RecyclerView) findViewById(R.id.updaloadPicListView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        updaloadPicListView.setLayoutManager(mLinearLayoutManager);
        dataList = new ArrayList<UploadPicData>();
        picAdapter = new AlbumUploadPicAdapter(dataList);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(updaloadPicListView.getContext(),
                mLinearLayoutManager.getOrientation());
        updaloadPicListView.addItemDecoration(dividerItemDecoration);
    }





}
