package com.picaproject.pica.CustomView;

import android.content.Intent;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.picaproject.pica.Activity.LocationListActivity;
import com.picaproject.pica.Activity.NewAlbumUploadActivity;
import com.picaproject.pica.Activity.NewLocationActivity;
import com.picaproject.pica.IntentProtocol;
import com.picaproject.pica.Item.UploadPicData;

/*
 * UploadPic 화면 Ui에서 리사이클러 <-> 플래그먼트 <-> 액티비티가 서로 이벤트를 주고받기위한 컨트롤러
 * 리사이클러의 이미지를 터치할때마다 플래그먼트의 이미지가 리사이클러의 이미지로 변경된다.
 * */
public class UploadPicController {
    private RecyclerView recyclerView;
    private ViewPager viewPager;
    private NewAlbumUploadActivity activity;

    public UploadPicController(RecyclerView recyclerView, ViewPager viewPager, NewAlbumUploadActivity activity) {
        this.recyclerView = recyclerView;
        this.viewPager = viewPager;
        this.activity = activity;
    }

    // 뷰페이저 페이지 이동하기
    public int moveViewPagerPage(int page){
        viewPager.setCurrentItem(page);
        return page;
    }


    public View getRecyclerBorder(int pos){
        NewUploadPicHolder h = (NewUploadPicHolder)recyclerView.findViewHolderForAdapterPosition(pos);
        if(h==null)
            return null;
        return h.getBorderView();
    }
    // currentPage : 현재 뷰페이저가 보이고있는 페이지
    // itemCnt : 뷰페이저/리사이클뷰에서 보여주고있는 데이터 총 갯수
    public void setBoder(int currentPage,int itemCnt){
        for(int i=0;i<itemCnt;i++){
            NewUploadPicHolder h = (NewUploadPicHolder)recyclerView.findViewHolderForAdapterPosition(i);
            if(h==null){
                continue;
            }
            //현재 보이고있는 페이지일경우
            if(i==currentPage){
                h.getBorderView().setVisibility(View.VISIBLE);
            }
            // 현재 보이는 페이지가 아닐경우
            else{
                h.getBorderView().setVisibility(View.GONE);
            }
        }
    }

    // 위치 선택 액티비티 열기
    // 사진 추가 플래그먼트에서 위치 TextView 선택시 이동
    public void openLoactionView(UploadPicData data){

        /*
        Intent intent = new Intent(activity, LocationListActivity.class);
        intent.putExtra(IntentProtocol.INTENT_ALBUM_MODE, true);
        intent.putExtra(IntentProtocol.PIC_DATA_CLASS_NAME,data);
        activity.startActivityForResult(intent,IntentProtocol.SET_PIC_LOCATION);*/

        Intent intent = new Intent(activity, NewLocationActivity.class);
        intent.putExtra(IntentProtocol.PIC_DATA_CLASS_NAME, data);
        activity.startActivity(intent);
    }



    /*
    * 리사이클러 뷰의 아이템에서 X 버튼이 눌렸을때 해당 아이템(사진) 목록에서 제거하기
    * */
    public int removePic(int picPossition){
        activity.removeItem(picPossition);
        return picPossition;
    }





}
