package com.picaproject.pica.Fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.picaproject.pica.CustomView.UploadPicController;
import com.picaproject.pica.Item.PicPlaceData;
import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.Listener.NewAlbumSetLocationClickListener;
import com.picaproject.pica.Listener.OnBackPressedListener;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.AppUtility;
import com.picaproject.pica.Util.InterverToast;

public class NewAlbumUploadFragment extends Fragment implements OnBackPressedListener {
    private ImageView imageView, locations_imageview, edit_imageview;
    private RelativeLayout general_frame, editmode_frame;
    private UploadPicData uploadPicData;
    private EditText edit_edittext;
    private TextView edit_byteTextview;
    private  static RequestManager glide;
    private Activity activity;
    private UploadPicController controller;
    private boolean isEditmode = false;
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

        general_frame = rootView.findViewById(R.id.general_frame);
        editmode_frame = rootView.findViewById(R.id.put_text_mode_frame);
        imageView = rootView.findViewById(R.id.image);
        edit_imageview = rootView.findViewById(R.id.edit_imageview);
        edit_edittext = rootView.findViewById(R.id.edit_edittext);

        locations_imageview = rootView.findViewById(R.id.locations_imageview);
        edit_byteTextview = rootView.findViewById(R.id.edit_textview);



        setByteTextview(0);
        edit_edittext.addTextChangedListener(new TextWatcher() {
            InterverToast interverToast = new InterverToast(getActivity(), "80byte이내로 적어주세요", Toast.LENGTH_SHORT);
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (AppUtility.getAppinstance().getTextByte(edit_edittext.getText().toString()) <= 80){
                    if (interverToast != null)
                        interverToast.killAllToast();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int text_byte = AppUtility.getAppinstance().getTextByte(edit_edittext.getText().toString());
                //80바이트를 넘을경우 진동과 함께 토스트를 띄움
                if (text_byte > 80)
                    interverToast.show();
                setByteTextview(text_byte);
            }
        });

        // 주의사항 : 이 화면에서 이미지를 여러개 List처럼 불러들이는곳은 뷰페이저, 리사이클러 뷰 두군데다.
        if(uploadPicData!=null){

            glide.load(uploadPicData.getSrc()).into(imageView); //Glide을 이용해서 이미지뷰에 url에 있는 이미지를 세팅해줌
            glide.load(uploadPicData.getSrc()).into(edit_imageview);
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

    public void showContentsInput(boolean isEditmode){
        this.isEditmode = isEditmode;

        if (isEditmode) {
            general_frame.setVisibility(View.GONE);
            editmode_frame.setVisibility(View.VISIBLE);

        } else {
            general_frame.setVisibility(View.VISIBLE);
            editmode_frame.setVisibility(View.GONE);

        }
    }

    private void setByteTextview(int now){
        edit_byteTextview.setText(String.format("%02d/%d byte", now, 80));
    }

    @Override
    public boolean onBackPressed() {
        if (isEditmode) {

        }
        return false;
    }
}
