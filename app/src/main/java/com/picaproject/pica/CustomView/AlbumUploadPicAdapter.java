package com.picaproject.pica.CustomView;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.Listener.PicImageButtonClickListener;
import com.picaproject.pica.R;

import java.util.ArrayList;

public class AlbumUploadPicAdapter extends RecyclerView.Adapter<UploadPicHolder> {


    private ArrayList<UploadPicData> dataList;
    // context : 액티비티 화면 전환용 context
    private AppCompatActivity context;
    public AlbumUploadPicAdapter(AppCompatActivity context, ArrayList<UploadPicData> list) {
        this.dataList = list;
        this.context = context;
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
        Log.i("test_hs","AlbumUploadPicAdapter : "+dataList.toString());
        UploadPicData data = dataList.get(i);
        String contents = data.getContents();
        ImageView imgView = uploadPicHolder.getImageView();

        // list의 끝을 가르키는 EOF 데이터일 경우
        if(contents.equals(UploadPicData.STATE_EOF)){
            imgView.setImageResource(R.drawable.plus_icon);

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

            if(contents!=null)
                uploadPicHolder.getMetaTextView().setMetaText(contents);

        }

        imgView.setOnClickListener(new PicImageButtonClickListener(context,data,i));


    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }
}
