package com.picaproject.pica.Util.NetworkItems;

import android.icu.text.StringPrepParseException;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;
import com.picaproject.pica.Util.AppUtility;

import java.util.ArrayList;

public class ImageResultItem implements ClusterItem, Parcelable {
    @SerializedName("picture_id")
    @Expose
    private Integer pictureId;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("upload_date")
    @Expose
    private String uploadDate;
    @SerializedName("p_member_id")
    @Expose
    private Integer pMemberId;
    @SerializedName("p_album_id")
    @Expose
    private Integer pAlbumId;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("contents")
    @Expose
    private String contents;
    @SerializedName("nickName")
    @Expose
    private String nickName;
    @SerializedName("isMyUpload")
    @Expose
    private String isMyUpload;
    @SerializedName("isLikePicture")
    @Expose
    private String isLikePicture;
    @SerializedName("tags")
    @Expose
    private String tags;

    private String parsed_tags;

    private int arrayIndex;
    private int sampleImg;
    public ImageResultItem(){}

    protected ImageResultItem(Parcel in) {
        if (in.readByte() == 0) {
            pictureId = null;
        } else {
            pictureId = in.readInt();
        }
        file = in.readString();
        arrayIndex = in.readInt();
        sampleImg = in.readInt();
        uploadDate = in.readString();
        if (in.readByte() == 0) {
            pMemberId = null;
        } else {
            pMemberId = in.readInt();
        }
        if (in.readByte() == 0) {
            pAlbumId = null;
        } else {
            pAlbumId = in.readInt();
        }
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        contents = in.readString();
        nickName = in.readString();
        isMyUpload = in.readString();
        isLikePicture = in.readString();
        tags = in.readString();
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;

    }

    public String getParsed_tags() {
        if (parsed_tags == null) {
            StringBuffer sb = new StringBuffer("");

            if (tags != null && tags.length() > 0) {
                String[] array = tags.split("/");
                for (String a : array) {
                    sb.append("#");
                    sb.append(a);
                    sb.append(" ");
                }

            }
            parsed_tags = sb.toString();

        }

        return parsed_tags;
    }


    public boolean getIsMyUpload() {
        return ("y".equals(isMyUpload));
    }

    public void setIsMyUpload(String isMyUpload) {
        this.isMyUpload = isMyUpload;
    }

    public boolean getIsLikePicture() {
        return ("y".equals(isLikePicture));
    }

    public void setIsLikePicture(String isLikePicture) {
        this.isLikePicture = isLikePicture;
    }

    public String getNickname() {
        return nickName;
    }

    public void setNickname(String nickname) {
        this.nickName = nickname;
    }

    public int getSampleImg() {
        return sampleImg;
    }

    public void setSampleImg(int sampleImg) {
        this.sampleImg = sampleImg;
    }

    public int getArrayIndex() {
        return arrayIndex;
    }

    public void setArrayIndex(int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public static final Creator<ImageResultItem> CREATOR = new Creator<ImageResultItem>() {
        @Override
        public ImageResultItem createFromParcel(Parcel in) {
            return new ImageResultItem(in);
        }

        @Override
        public ImageResultItem[] newArray(int size) {
            return new ImageResultItem[size];
        }
    };


    public Integer getPictureId() {
        return pictureId;
    }

    public void setPictureId(Integer pictureId) {
        this.pictureId = pictureId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Integer getPMemberId() {
        return pMemberId;
    }

    public void setPMemberId(Integer pMemberId) {
        this.pMemberId = pMemberId;
    }

    public Integer getPAlbumId() {
        return pAlbumId;
    }

    public void setPAlbumId(Integer pAlbumId) {
        this.pAlbumId = pAlbumId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public LatLng getPosition() {
        if (latitude == AppUtility.IMAGE_HAS_NO_LOCATION && longitude == AppUtility.IMAGE_HAS_NO_LOCATION)
            return null;
        return new LatLng(latitude, longitude);
    }

    public boolean hasLocations() {
        if (latitude == null && longitude == null)
            return false;
        if (latitude == AppUtility.IMAGE_HAS_NO_LOCATION || longitude == AppUtility.IMAGE_HAS_NO_LOCATION)
            return false;
        if (latitude > 90.0 && latitude < -99.0 && longitude < -180.0 && longitude > 180.0)
            return false;
        return true;
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
        if (pictureId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(pictureId);
        }
        dest.writeString(file);
        dest.writeInt(arrayIndex);
        dest.writeInt(sampleImg);
        dest.writeString(uploadDate);
        if (pMemberId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(pMemberId);
        }
        if (pAlbumId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(pAlbumId);
        }
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeString(contents);
        dest.writeString(nickName);
        dest.writeString(isMyUpload);
        dest.writeString(isLikePicture);
        dest.writeString(tags);
    }

    @Override
    public String toString() {
        return "ImageResultItem{" +
                "pictureId=" + pictureId +
                ", file='" + file + '\'' +
                ", uploadDate='" + uploadDate + '\'' +
                ", pMemberId=" + pMemberId +
                ", pAlbumId=" + pAlbumId +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", contents='" + contents + '\'' +
                ", nickName='" + nickName + '\'' +
                ", isMyUpload='" + isMyUpload + '\'' +
                ", isLikePicture='" + isLikePicture + '\''+
                ", tags=" + tags+
                '}';
    }
}
