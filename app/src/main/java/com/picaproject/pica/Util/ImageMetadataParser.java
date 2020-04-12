package com.picaproject.pica.Util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.exifinterface.media.ExifInterface;

import com.picaproject.pica.Item.PicPlaceData;

import java.io.IOException;
import java.io.InputStream;

public class ImageMetadataParser {

    public static PicPlaceData getLocationMetaData(Context context, Uri uri){
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            ExifInterface exifInterface = new ExifInterface(inputStream);
            if (exifInterface != null) {
                double[] location = exifInterface.getLatLong();
                if (location != null){
                    return new PicPlaceData(location[0], location[1]);
                }
            }
        } catch (IOException e) {
            // Handle any errors
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {}
            }
        }

        return null;
    }
}
