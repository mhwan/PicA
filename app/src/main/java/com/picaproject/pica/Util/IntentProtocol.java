package com.picaproject.pica.Util;
/*
* 인텐트 주고받을때 사용되는 ID등을 static으로 보관
*
*
*/
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
    // 사진 추가화면 -> 사진에 위치정보 추가/수정
    public static final int SET_PIC_LOCATION = 9004;

    public static final int CREATE_MANAGE_ALBUM = 9006;

    public static final int REQUEST_IMAGE_FILTER = 9031;
    public static int RESULT_LOAD_IMAGE = 8001;
    public static int REQUEST_INVITE_MEMBER = 7001;
    public static int REQUEST_EDIT_CONTENTS = 7003;
    public static int REQUEST_PIC_DETAIL = 7005;
    public static final int GPS_SETTING = 1341;

    public static final String INTENT_PICTURE_ID = "picture_id";
    public static final String INTENT_QUERY = "querys";
    public static final String INTENT_INPUT_CONTENT = "input_contents";
    public static final String INTENT_INPUT_ALBUM_ID = "input_albumid";
    public static final String INTENT_OUTPUT_CONTENT = "output_contents";
    public static final String INTENT_INPUT_TAGS = "input_Tagsarr";
    public static final String INTENT_OUTPUT_TAGS = "output_Tagsarr";
    public static final String INTENT_INPUT_ALBUM_TITLE = "input_album_title";
    public static final String INTENT_INPUT_ALBUM_PICTURE = "input_album_pic";
    public static final String INTENT_INPUT_ALBUM_DESC = "input_album_desc";
    public static final String INTENT_FLAG_MODE = "mode";
    public static final String INTENT_START_POSITION = "startPosition";
    public static final String INTENT_ALBUM_IMAGE_LIST = "image_list";
    public static final String INTENT_ALBUM_MODE = "album_mode";
    public static final String INTENT_FLAG_DATA_INDEX = "DataIndex";
    public static final String INTENT_FILTER_INPUT = "filtered_input_extra";
    public static final String PIC_DATA_CLASS_NAME = "UploadPicData";
    public static final String PIC_DATA_LIST_NAME = "ArrayList_UploadPicData";
    public static final String INTENT_CONTACT_LIST = "select_contactlist";
    public static final String INTENT_RESULT_SELECTED_ID = "selected_id_list";
    public static final String INTENT_FILTER_OUTPUT = "filterd_new_image_output";
    public static final String KEY_PARCELABLE_PHOTO_DATA = "key_parcelabed_data_list";
}
