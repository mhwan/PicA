package com.picaproject.pica.Activity;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.picaproject.pica.IntentProtocol;
import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.Listener.PicImageViewClickListener;
import com.picaproject.pica.Listener.UploadPicSubmitButtonClickListener;
import com.picaproject.pica.R;

import java.util.ArrayList;

public class UploadPicActivity extends AppCompatActivity {

    private UploadPicData picData;
    private ImageView imageView;
    private Button submit;
    private EditText picTagEdit;
    private EditText picContentsEdit;
    private Uri selectedImageUri;
    private int mode;
    // 수정모드에서만 사용
    // 수정 대상인 PicData가 들어있던 위치
    private int updateIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pic);


        submit = (Button) findViewById(R.id.submit_btn);
        picTagEdit = (EditText) findViewById(R.id.pic_tag);
        picContentsEdit = (EditText) findViewById(R.id.pic_contents);
        imageView = (ImageView) findViewById(R.id.imageView);

        //임시 삽입 지도액티비티로의 진입
        (findViewById(R.id.map_contents)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Intent intent = getIntent();
        // 앨범추가 화면에서 넘어온 데이터가 "기존 사진 수정" 인지 "새 사진 추가인지" MODE 구분 / 만약 값이 없다면 기본값은 새 사진 추가
        mode = intent.getIntExtra(IntentProtocol.INTENT_FLAG_MODE,IntentProtocol.ADD_PIC_MODE);
        UploadPicData picData = (UploadPicData)intent.getSerializableExtra(IntentProtocol.PIC_DATA_CLASS_NAME);
        this.picData=picData;


        // 수정모드이면 수정대상인 사진 띄워줌
        if(mode==IntentProtocol.UPDATE_PIC_MODE){
            selectedImageUri = Uri.parse(picData.getSrc());

            imageView.setImageURI(selectedImageUri);

            picContentsEdit.setText(picData.getContents());

            ArrayList<String> tags = picData.getTags();
            if(tags!=null&&tags.size()>0){
                String tagText= "";
                for (int i=0;i<tags.size()-2;i++){
                    tagText+=tags.get(i)+" ";
                }
                tagText+=tags.get(tags.size()-1);
                picTagEdit.setText(tagText);
            }


            updateIndex = intent.getIntExtra(IntentProtocol.INTENT_FLAG_DATA_INDEX,-1);
        }
        else{
            updateIndex=-1;
        }

        imageView.setOnClickListener(new PicImageViewClickListener(this));
        submit.setOnClickListener(new UploadPicSubmitButtonClickListener(this));
        Log.i("test_hs","UploadPicActivity : "+picData.getContents());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == IntentProtocol.GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            Log.i("test_hs","UploadPicActivity onActivityResult : "+data.getData().toString());
            selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 이 화면에서 변경된 picData 전달하고 이 화면 종료하기
    // submit 버튼 클릭리스너에서만 호출됨
    public void submit(){
        // 이미지 선택도 안하고 등록버튼을 감히 누르려고 든다면
        // 토스트 메시지로 엄히 벌하고 등록하지 아니할것이다

        if(selectedImageUri==null){
            Toast myToast = Toast.makeText(this.getApplicationContext(),"등록할 사진을 선택해주세요.", Toast.LENGTH_SHORT);
            myToast.show();
            return;
        }


        picData.setSrc(selectedImageUri.toString());
        picData.setContents(picContentsEdit.getText().toString());
        // 임시로 태그는 띄어쓰기로 구분
        String tagListText = picTagEdit.getText().toString();
        String[] tagArray = tagListText.split("\\s+");
        //String[] tagArray = tagListText.split("#");
        if(tagArray!=null){
            ArrayList<String> tagList = new ArrayList<>();
            for(String tag : tagArray){
                tagList.add(tag);
            }
            picData.setTags(tagList);
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra(IntentProtocol.PIC_DATA_CLASS_NAME,picData);
        // 수정모드였을경우 갖고있던 인덱스 반환
        if(mode==IntentProtocol.UPDATE_PIC_MODE)
            resultIntent.putExtra(IntentProtocol.INTENT_FLAG_DATA_INDEX,updateIndex);

        setResult(mode, resultIntent);
        finish();
        // 화면에서 수정한 Pic 데이터 전달하고 종료
        // 이후에 UploadAlbumActivity에서 onActivityResult로 받아주면됨.
    }



}
