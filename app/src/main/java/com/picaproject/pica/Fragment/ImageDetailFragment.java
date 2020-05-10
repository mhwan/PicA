package com.picaproject.pica.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.picaproject.pica.Item.PicPlaceData;
import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.AppUtility;

public class ImageDetailFragment extends Fragment {
    private UploadPicData pictureData;
    private SubsamplingScaleImageView imageView;
    private TextView contents, location, tag;
    private ViewGroup picInfoFrame;
    private boolean isShow;

    public ImageDetailFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ui_image_detail, container, false);
        initView(rootView);

        return rootView;
    }



    private void initView(View view){
        imageView = (SubsamplingScaleImageView) view.findViewById(R.id.imageView);
        contents = (TextView) view.findViewById(R.id.img_contents);
        location = (TextView) view.findViewById(R.id.img_location);
        tag = (TextView) view.findViewById(R.id.img_tags);
        picInfoFrame = view.findViewById(R.id.pic_info_frame);

        if (pictureData != null){
            imageView.setImage(ImageSource.resource(pictureData.getImgSampleId()));
            if (pictureData.getContents() == null && pictureData.getTags() == null && pictureData.getLocation() == null)
                picInfoFrame.setVisibility(View.GONE);
            else {
                if (isShow)
                    picInfoFrame.setVisibility(View.VISIBLE);
                else
                    picInfoFrame.setVisibility(View.GONE);
                if (pictureData.getContents() != null) {
                    contents.setVisibility(View.VISIBLE);
                    contents.setText("내용 : "+pictureData.getContents());
                }
                if (pictureData.getLocation() != null) {
                    location.setVisibility(View.VISIBLE);
                    PicPlaceData placeData = pictureData.getLocation();
                    if (placeData.getName() != null && !placeData.getName().equals(""))
                        location.setText("위치 : "+pictureData.getLocation().getName());
                    else {
                        Log.d("address", AppUtility.getAppinstance().getAddress(placeData.getLatitude(), placeData.getLongitude()));
                        location.setText("위치 : " + AppUtility.getAppinstance().getAddress(placeData.getLatitude(), placeData.getLongitude()));
                    }
                }
                if (pictureData.getTags() != null) {
                    tag.setVisibility(View.VISIBLE);
                    tag.setText("태그 : "+tagDataToString());
                }
            }

        }
    }

    public void setPictureData(UploadPicData pictureData, boolean isShow){
        this.pictureData = pictureData;
        this.isShow = isShow;
    }

    public void setInfoView(boolean isShow){
        if (picInfoFrame == null)
            Log.e("fragment", "view is null");
        else {
            if (isShow)
                picInfoFrame.setVisibility(View.VISIBLE);
            else
                picInfoFrame.setVisibility(View.GONE);
        }
    }

    private String tagDataToString(){
        String str = "";
        for (String s : pictureData.getTags()){
            str+=("#"+s+" ");
        }
        return str;
    }
}
