package com.picaproject.pica.Fragment;

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

import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.R;

public class NewAlbumUploadFragment extends Fragment {
    private ImageView imageView;
    private EditText locationEdit;
    private EditText contentEdit;
    private EditText tagEdit;
    private UploadPicData uploadPicData;

    public NewAlbumUploadFragment() {

    }
    // Fragment 활성화 전에 꼭 호출할것
    public void setUploadPicData(UploadPicData uploadPicData) {
        this.uploadPicData = uploadPicData;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.item_album_upload_view_pager, container, false);

        imageView = rootView.findViewById(R.id.image);
        locationEdit = rootView.findViewById(R.id.location_edit);
        contentEdit = rootView.findViewById(R.id.content_edit);
        tagEdit = rootView.findViewById(R.id.tag_edit);


        if(uploadPicData!=null){
            try {
                imageView.setImageURI(Uri.parse(uploadPicData.getSrc()));
            }catch (Exception e){
                imageView.setImageResource(R.drawable.error_icon);
                Log.e("test_hs","NewAlbumUploadFragment error e : "+e.toString());
            }

        }
        else{
            Log.e("test_hs","NewAlbumUploadFragment : uploadPicData is NULL");
        }



        return rootView;
    }
}
