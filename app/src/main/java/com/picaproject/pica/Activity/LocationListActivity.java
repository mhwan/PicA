package com.picaproject.pica.Activity;

import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.picaproject.pica.CustomView.BottomSheetListView;
import com.picaproject.pica.CustomView.SelectContactsAdapter;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.AppUtility;

public class LocationListActivity extends AppCompatActivity {
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetListView bottomSheetListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);
        initView();
    }


    /**
     * bottomsheetlistview는 일반 리스트뷰를 사용할때처럼 사용하면됨 (일반적인 리스트뷰에 세로로 스크롤할때만 재정의한 커스텀뷰)
     * xml에 id로 선언된 mapview는 임시적으로 linearlayout을 사용, 여기에 구글맵을 연결시켜서 쓰면됨 (혹시 다른 뷰그룹이 필요하면 바꿔도 상관없음)
     */
    private void initView(){
        FrameLayout bottomsheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomsheet);

        bottomSheetListView = (BottomSheetListView) findViewById(R.id.map_listview);

    }

}
