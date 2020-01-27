package com.picaproject.pica.Util;

import android.content.ClipData;

import com.picaproject.pica.Item.UploadPicData;

import java.util.ArrayList;

public class PicDataParser {

    public static ArrayList<UploadPicData> parseDataFromClipData(ClipData clipData){
        int dataCnt = clipData.getItemCount();
        ArrayList<UploadPicData> result = new ArrayList<>();
        for(int i=0;i<dataCnt;i++){
            String src = clipData.getItemAt(i).getUri().toString();
            UploadPicData picData = new UploadPicData(src);
            result.add(picData);
        }
        return result;

    }
}
