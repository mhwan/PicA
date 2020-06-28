package com.picaproject.pica.Fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.picaproject.pica.Item.PicPlaceData;
import com.picaproject.pica.Item.UploadImageItem;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.AppUtility;
import com.picaproject.pica.Util.NetworkItems.ImageResultItem;

public class ImageDetailFragment extends Fragment {
    private ImageResultItem pictureData;
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

            Glide.with(getActivity())
                    .asBitmap()
                    .load(pictureData.getFile())
                    .error(R.drawable.img_sample)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imageView.setImage(ImageSource.bitmap(resource));
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

            //이미지에 내용, 태그, 위치가 모두 없다면 안보이게 처리한다 (현재는 태그가 없으므로 위치와 내ㅐ용만있음)
            if (pictureData.getContents() == null && pictureData.getLatitude() == AppUtility.IMAGE_HAS_NO_LOCATION && pictureData.getLongitude() == AppUtility.IMAGE_HAS_NO_LOCATION)
                picInfoFrame.setVisibility(View.GONE);
            else {
                if (isShow)
                    picInfoFrame.setVisibility(View.VISIBLE);
                else
                    picInfoFrame.setVisibility(View.GONE);
                if (pictureData.getContents() != null) {
                    contents.setVisibility(View.VISIBLE);
                    contents.setText("내용 : " + pictureData.getContents());
                }
                if (pictureData.hasLocations()) {
                    if (pictureData.getLatitude() != AppUtility.IMAGE_HAS_NO_LOCATION && pictureData.getLongitude() != AppUtility.IMAGE_HAS_NO_LOCATION) {
                        location.setVisibility(View.VISIBLE);
                        location.setText("위치 : " + AppUtility.getAppinstance().getAddress(pictureData.getLatitude(), pictureData.getLongitude()));
                    /*
                    PicPlaceData placeData = pictureData.getLocation();
                    if (placeData.getName() != null && !placeData.getName().equals(""))
                        location.setText("위치 : "+pictureData.getLocation().getName());
                    else {
                        Log.d("address", AppUtility.getAppinstance().getAddress(placeData.getLatitude(), placeData.getLongitude()));
                        location.setText("위치 : " + AppUtility.getAppinstance().getAddress(placeData.getLatitude(), placeData.getLongitude()));
                    }*/
                    }
                /*
                if (pictureData.getTags() != null) {
                    tag.setVisibility(View.VISIBLE);
                    tag.setText("태그 : "+tagDataToString());
                }*/
                }
            }

        }
    }

    public void setPictureData(ImageResultItem pictureData, boolean isShow){
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

    /*
    private String tagDataToString(){
        String str = "";
        for (String s : pictureData.getTags()){
            str+=("#"+s+" ");
        }
        return str;
    }*/
}
