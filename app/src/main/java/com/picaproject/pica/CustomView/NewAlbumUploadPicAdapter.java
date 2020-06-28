package com.picaproject.pica.CustomView;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.picaproject.pica.Item.UploadImageItem;

import com.picaproject.pica.Listener.NewUploadRecyclerImageViewClickListener;
import com.picaproject.pica.Listener.NewUploadRecyclerRemoveBtnClickListener;
import com.picaproject.pica.R;


import java.util.ArrayList;

public class NewAlbumUploadPicAdapter extends RecyclerView.Adapter<NewUploadPicHolder> {

    private  static RequestManager glide;
    private ArrayList<UploadImageItem> dataList;
    private AppCompatActivity activity;
    private UploadPicController controller;
    /*
     * 주의 : dataList의 첫번째에는 무조건 추가버튼이 들어가야함.
     *
     * */
    public NewAlbumUploadPicAdapter(ArrayList<UploadImageItem> list, AppCompatActivity activity, UploadPicController controller) {
        this.dataList = list;
        this.activity = activity;
        this.controller = controller;
        glide = Glide.with(this.activity);
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
        UploadImageItem data = dataList.get(i);
        String contents = data.getContents();
        ImageView imgView = uploadPicHolder.getImageView();
        RelativeLayout removeBtn = uploadPicHolder.getRemoveBtn();

        glide.load(dataList.get(i).getSrc()).into(uploadPicHolder.getImageView()); //Glide을 이용해서 이미지뷰에 url에 있는 이미지를 세팅해줌
        imgView.setOnClickListener(new NewUploadRecyclerImageViewClickListener(controller,i));
        removeBtn.setOnClickListener(new NewUploadRecyclerRemoveBtnClickListener(controller,i));

    }

    /*
            ImageViewAsyncTask task = new ImageViewAsyncTask(uploadPicHolder.getImageView(),dataList.get(i).getSrc());
            task.execute();
            */


            /*
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
            */


    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }
}
