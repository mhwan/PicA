package com.picaproject.pica.Fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.picaproject.pica.CustomView.UploadPicController;
import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.R;

public class NewAlbumUploadFragment extends Fragment {
    private ImageView imageView;
    private TextView locationEdit;
    private EditText contentEdit;
    private EditText tagEdit;
    private UploadPicData uploadPicData;
    private  static RequestManager glide;
    private Activity activity;
    private UploadPicController controller;

    // 현재 플래그먼트의 인덱스
    private int idx;
    public NewAlbumUploadFragment() {

    }
    // Fragment 활성화 전에 꼭 호출할것
    public void setUploadPicData(UploadPicData uploadPicData) {
        this.uploadPicData = uploadPicData;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
        glide = Glide.with(this.activity);
    }

    public void setController(UploadPicController controller) {
        this.controller = controller;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.item_album_upload_view_pager, container, false);

        imageView = rootView.findViewById(R.id.image);
        locationEdit = rootView.findViewById(R.id.location_edit);
        contentEdit = rootView.findViewById(R.id.content_edit);
        tagEdit = rootView.findViewById(R.id.tag_edit);

        // 주의사항 : 이 화면에서 이미지를 여러개 List처럼 불러들이는곳은 뷰페이저, 리사이클러 뷰 두군데다.
        if(uploadPicData!=null){

            glide.load(uploadPicData.getSrc()).into(imageView); //Glide을 이용해서 이미지뷰에 url에 있는 이미지를 세팅해줌
            /*
            try {
                imageView.setImageURI(Uri.parse(uploadPicData.getSrc()));
            }catch (Exception e){
                imageView.setImageResource(R.drawable.error_icon);
                Log.e("test_hs","NewAlbumUploadFragment error e : "+e.toString());
            }
            */
        }
        else{
            Log.e("test_hs","NewAlbumUploadFragment : uploadPicData is NULL");
        }



        return rootView;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        View boder = controller.getRecyclerBorder(idx);
        if(boder==null){
            return;
        }
        if (menuVisible) {
            boder.setVisibility(View.VISIBLE);
        }
        else{
            boder.setVisibility(View.GONE);
        }


    }
}
