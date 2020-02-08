package com.picaproject.pica.Item;

import java.io.Serializable;
import java.util.ArrayList;

/*
* 업로드 될 대상으로 선택된 사진의 데이터를 보관하는 클래스
* src : 이미지 경로(로컬)
* contents : 사진 설명
* tags : 태그 목록
* contents,tags는 null일수있음
* */

/**
 * 2차 수정 from. mhwan
 * 임시적으로 사용할 샘플 이미지의 id값을 넣기위함
 */
public class UploadPicData implements Serializable {
    // 직렬화 버전 표시 : https://lktprogrammer.tistory.com/150
    private static final long serialVersionUID = 1L;
    // UploadPicData 클래스 ID
    // 사진 추가 화면에서 아이템의 구분을 위해 고유한 ID가 필요함.
    private static long CLASS_ID=0;
    // EOF를 가르키는 메시지 EOF만 쓰면 유저가 EOF라는 말만 쓴다면 에러나니까 유저들이 쓰지않을 고유한 코드가 필요함
    public static final String STATE_EOF = "CODE_EOF_ALBUM_PIC_LIST";
    // 추가 버튼을 나타내는 변수
    public static final String ADD_BTN = "CODE_ADD_BTN";
    private String src;
    private String contents;
    private ArrayList<String> tags;


    private PicPlaceData location;
    private long classId;
    private int imgSampleId;
    public UploadPicData(String src){
        classId = CLASS_ID;
        CLASS_ID++;
        this.src=src;
    }

    public UploadPicData(int id){
        classId = CLASS_ID;
        CLASS_ID++;
        this.imgSampleId = id;
    }

    public int getImgSampleId() {
        return imgSampleId;
    }

    public void setImgSampleId(int imgSampleId) {
        this.imgSampleId = imgSampleId;
    }

    public String getContents() {
        return contents;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getSrc() {
        return src;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public long getClassId() {
        return classId;
    }

    public PicPlaceData getLocation() {
        return location;
    }

    public void setLocation(PicPlaceData location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "UploadPicData{" +
                "src=" + src +
                ", contents='" + contents + '\'' +
                ", tags=" + tags +
                '}';
    }
}
