package com.picaproject.pica.Util;

import android.content.ClipData;
import android.content.Context;

import com.picaproject.pica.Item.PicPlaceData;
import com.picaproject.pica.Item.UploadImageItem;

import java.util.ArrayList;

public class PicDataParser {

    public static ArrayList<UploadImageItem> parseDataFromClipData(Context context, ClipData clipData){
        int dataCnt = clipData.getItemCount();
        ArrayList<UploadImageItem> result = new ArrayList<>();
        for(int i=0;i<dataCnt;i++){
            String src = clipData.getItemAt(i).getUri().toString();
            PicPlaceData placeData = ImageMetadataParser.getLocationMetaData(context, clipData.getItemAt(i).getUri());
            UploadImageItem picData = new UploadImageItem(src);
            if (placeData != null)
                picData.setLocation(placeData);
            result.add(picData);
        }
        return result;

    }
}
