package com.picaproject.pica.CustomView;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.R;

import java.util.ArrayList;

public class AlbumUploadPicAdapter extends RecyclerView.Adapter<UploadPicHolder> {


    private ArrayList<UploadPicData> dataList;

    public AlbumUploadPicAdapter(ArrayList<UploadPicData> list) {
        this.dataList = list;

    }
    /*
    *  Uri가 NULL이고 contents값이 "EOF" 문자열인 UploadPicData를
    *  dataList 끝에 집어넣는다.
    *  이 EOF 데이터는 RecyclerView에서 onBindViewHolder를 통해 View를 실제로 그려줄때
    *  "사진 추가" 화면으로 이동하는 버튼 역할을 한다.
    *  dataList에 새 데이터를 추가하기 전에 dataList 끝에 존재할 EOF 데이터를 반드시 삭제하고 추가해야한다.
    *  또한 새 데이터가 추가된 후에도 끝을 표시하는 EOF가 필요하다
    * */
    public void addEOFPicData(){
        UploadPicData eofData = new UploadPicData(null);
        eofData.setContents(UploadPicData.STATE_EOF);
        dataList.add(eofData);
    }
    /*
    *  반드시 dataList에 데이터가 추가되기 전에 호출되어 EOF를 제거한 후
    *  addEOFPicData를 호출하여 다시 EOF를 표시하는 데이터를 추가해야 한다.
    *
    * */
    public void removeEOFPicData(){
        dataList.remove(dataList.size());
    }

    // nonNUll 어노테이션은 해당 변수에 null값이 넘어오면 예외를 발생시켜줌으로써 null을방지함
    @NonNull
    @Override
    public UploadPicHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_album_upload_pic, viewGroup, false);

        UploadPicHolder viewHolder = new UploadPicHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull UploadPicHolder uploadPicHolder, int i) {
        Log.i("test_hs","2"+dataList.toString());
        String contents = dataList.get(i).getContents();
        Log.i("test_hs","3"+contents);
        // list의 끝을 가르키는 EOF 데이터일 경우
        if(contents.equals("EOF")){
            uploadPicHolder.getImageView().setImageResource(R.drawable.plus_icon);
        }
        // EOF가 아닌 다른 데이터만 보여주기 가능
        else {

            Uri imageSrc = dataList.get(i).getSrc();

            try {
                // 태그까진 View홀더에서 필요없음
                uploadPicHolder.getImageView().setImageURI(imageSrc);

            }catch (Exception e){
                String err = "이미지 로딩 에러 발생 " + e.toString();
                Log.e(this.getClass().getName(),err);
                uploadPicHolder.getImageView().setImageResource(R.drawable.error_icon);
            }

            if(contents!=null)
                uploadPicHolder.getMetaTextView().setMetaText(contents);

        }


    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }
}
