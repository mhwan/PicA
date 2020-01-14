package com.picaproject.pica.CustomView;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
        Uri imageSrc = dataList.get(i).getSrc();
        String contents = dataList.get(i).getContents();

        // 태그까진 View홀더에서 필요없음
        uploadPicHolder.getImageView().setImageURI(imageSrc);

        if(contents!=null)
            uploadPicHolder.getMetaTextView().setMetaText(contents);



    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }
}
