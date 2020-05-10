package com.picaproject.pica.Item;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

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
 *
 * id값을 다른곳에서 사용하기위해 하나 더 추가함 (mhwan)
 * 이걸 다른 id값과 같이 통일시켜야할 필요가 있지않나 싶음?
 *
 * 여러 이유로 serializable 에서 parcelable로 바꿈
 * 근데 classid는 그럼 자동으로 오르는 방식을 굳이 써야하는가?
 */
public class UploadPicData implements Parcelable, ClusterItem {
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
    private int id;
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

    protected UploadPicData(Parcel in) {
        src = in.readString();
        contents = in.readString();
        tags = in.createStringArrayList();
        location = in.readParcelable(PicPlaceData.class.getClassLoader());
        classId = in.readLong();
        imgSampleId = in.readInt();
        //id = in.readInt();
    }

    public static final Creator<UploadPicData> CREATOR = new Creator<UploadPicData>() {
        @Override
        public UploadPicData createFromParcel(Parcel in) {
            return new UploadPicData(in);
        }

        @Override
        public UploadPicData[] newArray(int size) {
            return new UploadPicData[size];
        }
    };

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

    public void setClassId(long id) {
        this.classId = id;
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

    @Override
    public LatLng getPosition() {
        if (location != null){
            return location.getLatLng();
        } else return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(src);
        dest.writeString(contents);
        dest.writeStringList(tags);
        dest.writeParcelable(location, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeLong(classId);
        dest.writeInt(imgSampleId);
        //dest.writeInt(id);
    }
}
