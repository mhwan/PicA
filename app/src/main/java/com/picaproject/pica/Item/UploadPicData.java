package com.picaproject.pica.Item;

import android.net.Uri;

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
    public static final String STATE_EOF = "EOF";
    private Uri src;
    private String contents;
    private ArrayList<String> tags;


    public UploadPicData(Uri src){
        this.src=src;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Uri getSrc() {
        return src;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
