package com.picaproject.pica.Listener;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.picaproject.pica.Activity.UploadPicActivity;
import com.picaproject.pica.IntentProtocol;
import com.picaproject.pica.Item.UploadPicData;

/*
* 앨범 추가 화면에서 "사진추가" 버튼 혹은 사진 리스트중에 하나를
* 클릭했을때 동작
*
* */
public class PicImageButtonClickListener implements View.OnClickListener {
    private AppCompatActivity context;
    private UploadPicData data;
    // PicData가 Array에 들어있던 위치
    private int dataIndex;
    public PicImageButtonClickListener(AppCompatActivity context, UploadPicData data,int dataIndex) {
        this.context = context;
        this.data = data;
        this.dataIndex = dataIndex;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context,UploadPicActivity.class);
        intent.putExtra(IntentProtocol.PIC_DATA_CLASS_NAME,data);

        int mode = -1;

        if(data.getContents().equals(UploadPicData.STATE_EOF)){
            mode = IntentProtocol.ADD_PIC_MODE;
        }

        else{
            mode = IntentProtocol.UPDATE_PIC_MODE;
            intent.putExtra(IntentProtocol.INTENT_FLAG_DATA_INDEX,dataIndex);
        }

        intent.putExtra(IntentProtocol.INTENT_FLAG_MODE,mode);
        context.startActivityForResult(intent,mode);
    }
    /*
    * 사진 추가/수정 액티비티에서
    * 이미 동일한 Uri가 있는 액티비티일 경우 내용만 변경?
    *
    * */
}
