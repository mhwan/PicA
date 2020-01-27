package com.picaproject.pica.CustomView;

import android.net.Uri;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.picaproject.pica.Item.UploadPicData;

import com.picaproject.pica.R;

import java.util.ArrayList;

public class NewAlbumUploadPicAdapter extends RecyclerView.Adapter<NewUploadPicHolder> {


    private ArrayList<UploadPicData> dataList;
    /*
     * 주의 : dataList의 첫번째에는 무조건 추가버튼이 들어가야함.
     *
     * */
    public NewAlbumUploadPicAdapter(ArrayList<UploadPicData> list) {

        this.dataList = list;

    }


    // nonNUll 어노테이션은 해당 변수에 null값이 넘어오면 예외를 발생시켜줌으로써 null을방지함
    @NonNull
    @Override
    public NewUploadPicHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_new_album_upload_pic, viewGroup, false);

        NewUploadPicHolder viewHolder = new NewUploadPicHolder(view);

        return viewHolder;

    }
    /*
    * 주의 : dataList의 첫번째에는 무조건 추가버튼이 들어가야함.
    *
    * */
    @Override
    public void onBindViewHolder(@NonNull NewUploadPicHolder uploadPicHolder, int i) {
        Log.i("test_hs","AlbumUploadPicAdapter : "+dataList.toString());
        UploadPicData data = dataList.get(i);
        String contents = data.getContents();
        ImageView imgView = uploadPicHolder.getImageView();

        if(contents!=null&&contents.equals(UploadPicData.ADD_BTN)){
            imgView.setImageResource(R.drawable.plus_icon);
            // TODO : 사진 추가하기
        }
        // EOF가 아닌 다른 데이터만 보여주기 가능
        else {

            String imageSrc = dataList.get(i).getSrc();

            try {
                // 태그까진 View홀더에서 필요없음
                Uri imageUri = Uri.parse(imageSrc);
                uploadPicHolder.getImageView().setImageURI(imageUri);

            }catch (Exception e){
                String err = "이미지 로딩 에러 발생 " + e.toString();
                Log.e("test_hs",err);
                Log.e(this.getClass().getName(),err);
                uploadPicHolder.getImageView().setImageResource(R.drawable.error_icon);
            }
        }


    }


    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }
}
