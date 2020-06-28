package com.picaproject.pica.Util;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.picaproject.pica.R;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;


public class ImageCropUtility {
    private static ImageCropUtility instance;
    private static final int RATIO_ORIGIN = 0;
    private static final int RATIO_SQUARE = 1;
    private static final int RATIO_DYNAMIC = 2;
    private static final int RATIO_CUSTOM = 3;

    private static final int FORMAT_PNG = 0;
    private static final int FORMAT_WEBP = 1;
    private static final int FORMAT_JPEG = 2;

    public static ImageCropUtility getInstance() {
        if (instance == null)
            instance = new ImageCropUtility();
        return instance;
    }

    public UCrop setRatio(@NonNull UCrop uCrop, int choice, float xratio, float yratio){
        switch (choice) {
            case RATIO_ORIGIN:
                uCrop = uCrop.useSourceImageAspectRatio();
                break;
            case RATIO_SQUARE:
                uCrop = uCrop.withAspectRatio(1, 1);
                break;
            case RATIO_DYNAMIC:
                // do nothing
                break;
            case RATIO_CUSTOM:
            default:
                try {
                    float ratioX = xratio;
                    float ratioY = yratio;
                    if (ratioX > 0 && ratioY > 0) {
                        uCrop = uCrop.withAspectRatio(ratioX, ratioY);
                    }
                } catch (NumberFormatException e) {
                    Log.e("Ucrop", "Number please", e);
                }
                break;
        }

        return uCrop;

    }

    public UCrop setSize(@NonNull UCrop uCrop, int maxWidth, int maxHeight){
        if(maxWidth > 0 && maxHeight > 0){
            return uCrop.withMaxResultSize(maxWidth, maxHeight);
        }
        return uCrop;
    }


    public UCrop advancedConfig(@NonNull UCrop uCrop, int format, int quality) {
        UCrop.Options options = new UCrop.Options();


        switch (format) {
            case FORMAT_PNG:
                options.setCompressionFormat(Bitmap.CompressFormat.PNG);
                break;
            case FORMAT_WEBP:
                options.setCompressionFormat(Bitmap.CompressFormat.WEBP);
                break;
            case FORMAT_JPEG:
            default:
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                break;
        }
        options.setCompressionQuality(quality); // range [0-100]


        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        options.setStatusBarColor(ContextCompat.getColor(AppContext.getContext(), R.color.colorPrimaryDark));
        options.setActiveControlsWidgetColor(ContextCompat.getColor(AppContext.getContext(), R.color.colorAccent));
        options.setToolbarWidgetColor(ContextCompat.getColor(AppContext.getContext(), R.color.colorPrimary));
        //options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
        //options.setToolbarTitleTextColor(ContextCompat.getColor(this, R.color.your_color_res));

        /*
        If you want to configure how gestures work for all UCropActivity tabs
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        * */

        /*
        This sets max size for bitmap that will be decoded from source Uri.
        More size - more memory allocation, default implementation uses screen diagonal.
        options.setMaxBitmapSize(640);
        * */


       /*
        Tune everything (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧
        options.setMaxScaleMultiplier(5);
        options.setImageToCropBoundsAnimDuration(666);
        options.setDimmedLayerColor(Color.CYAN);
        options.setOvalDimmedLayer(true);
        options.setShowCropFrame(false);
        options.setCropGridStrokeWidth(20);
        options.setCropGridColor(Color.GREEN);
        options.setCropGridColumnCount(2);
        options.setCropGridRowCount(1);
        // Color palette
        options.setToolbarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
		options.setToolbarTitleTextColor(ContextCompat.getColor(this, R.color.your_color_res));
       */

        return uCrop.withOptions(options);
    }
}
