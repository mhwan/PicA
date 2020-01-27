package com.picaproject.pica;
/*
* 인텐트 주고받을때 사용되는 ID등을 static으로 보관
*
* */
public class IntentProtocol {
    // 외부 갤러리에서 이미지 가져오기
    public static final int GET_GALLERY_IMAGE = 9000;
    // 앨범추가 -> 사진 추가로 이동할때 사용
    // "사진추가" 버튼을 눌러서 사진추가모드일때
    // 구버전
    public static final int ADD_PIC_MODE = 9001;
    // 앨범추가 -> 사진 추가로 이동할때 사용
    // 기존에 업로드된 사진을 클릭해서 수정해야할때
    // 구버전
    public static final int UPDATE_PIC_MODE = 9002;

    // 사진 선택 후 사진 편집 모드로 이동

    public static final int ADD_PIC_MULTI_MODE = 9003;



    public static final String INTENT_FLAG_MODE = "mode";
    public static final String INTENT_FLAG_DATA_INDEX = "DataIndex";
    public static final String PIC_DATA_CLASS_NAME = "UploadPicData";
    public static final String PIC_DATA_LIST_NAME = "ArrayList_UploadPicData";

}
