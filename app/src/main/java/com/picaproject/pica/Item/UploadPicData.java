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
public class UploadPicData implements Serializable {
    // 직렬화 버전 표시 : https://lktprogrammer.tistory.com/150
    private static final long serialVersionUID = 1L;
    // EOF를 가르키는 메시지 EOF만 쓰면 유저가 EOF라는 말만 쓴다면 에러나니까 유저들이 쓰지않을 고유한 코드가 필요함
    public static final String STATE_EOF = "CODE_EOF_ALBUM_PIC_LIST";
    private String src;
    private String contents;
    private ArrayList<String> tags;


    public UploadPicData(String src){
        this.src=src;
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

    @Override
    public String toString() {
        return "UploadPicData{" +
                "src=" + src +
                ", contents='" + contents + '\'' +
                ", tags=" + tags +
                '}';
    }
}
