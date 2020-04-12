package com.picaproject.pica.Fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.picaproject.pica.CustomView.UploadPicController;
import com.picaproject.pica.Item.PicPlaceData;
import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.Listener.NewAlbumSetLocationClickListener;
import com.picaproject.pica.R;

public class NewAlbumUploadFragment extends Fragment {
    private ImageView imageView;
    private RelativeLayout tags, locations, contents;
    private UploadPicData uploadPicData;
    private  static RequestManager glide;
    private Activity activity;
    private UploadPicController controller;
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

    /*
    public void setLocationText(String s){
        if(locationEdit!=null)
            locationEdit.setText(s);
    }*/


    public void setController(UploadPicController controller) {
        this.controller = controller;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.item_album_upload_view_pager, container, false);

        imageView = rootView.findViewById(R.id.image);
        locations = rootView.findViewById(R.id.locations);
        contents = rootView.findViewById(R.id.contents);
        tags = rootView.findViewById(R.id.tags);

        locations.setOnClickListener(new NewAlbumSetLocationClickListener(controller,uploadPicData));
        contents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentsInput();
            }
        });

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

        PicPlaceData p = uploadPicData.getLocation();

        if(p!=null){
            //locationEdit.setText(p.getName());
        }


        return rootView;
    }

    private void showContentsInput(){
        InputBottomSheetDialogFragment inputBottomSheetDialogFragment = (InputBottomSheetDialogFragment) InputBottomSheetDialogFragment.newInstance(false);
        inputBottomSheetDialogFragment.show(getFragmentManager(), "");
    }


}
