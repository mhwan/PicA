package com.picaproject.pica.Fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.picaproject.pica.CustomView.UploadPicController;
import com.picaproject.pica.Item.UploadImageItem;
import com.picaproject.pica.Listener.OnBackPressedListener;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.AppUtility;
import com.picaproject.pica.Util.InterverToast;

public class NewAlbumUploadFragment extends Fragment {
    private ImageView imageView;
    private UploadImageItem uploadImageItem;
    private  static RequestManager glide;
    private Activity activity;
    private UploadPicController controller;
    public NewAlbumUploadFragment() {

    }
    // Fragment 활성화 전에 꼭 호출할것
    public void setUploadImageItem(UploadImageItem uploadImageItem) {
        this.uploadImageItem = uploadImageItem;
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


        // 주의사항 : 이 화면에서 이미지를 여러개 List처럼 불러들이는곳은 뷰페이저, 리사이클러 뷰 두군데다.
        if(uploadImageItem !=null){

            glide.load(uploadImageItem.getSrc()).into(imageView); //Glide을 이용해서 이미지뷰에 url에 있는 이미지를 세팅해줌
            /*
            try {
                imageView.setImageURI(Uri.parse(uploadPicData.getSrc()));
            }catch (Exception e){
                imageView.setImageResource(R.drawable.error_icon);
                Log.e("test_hs","NewAlbumUploadFragment error e : "+e.toString());
            }
            */

            /*
            PicPlaceData p = uploadPicData.getLocation();

            if(p!=null){
                //locationEdit.setText(p.getName());
                locations_imageview.setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
            }*/
        }
        else{
            Log.e("test_hs","NewAlbumUploadFragment : uploadPicData is NULL");
        }




        return rootView;
    }

}
