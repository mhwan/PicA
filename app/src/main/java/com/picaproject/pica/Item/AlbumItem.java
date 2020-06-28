package com.picaproject.pica.Item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlbumItem {
    @SerializedName("album_id")
    @Expose
    private Integer albumId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("defaultPicture")
    @Expose
    private String defaultPicture;
    @SerializedName("create_p_member_id")
    @Expose
    private Integer createPMemberId;
    @SerializedName("nickName")
    @Expose
    private String nickName;

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultPicture() {
        return defaultPicture;
    }

    public void setDefaultPicture(String defaultPicture) {
        this.defaultPicture = defaultPicture;
    }

    public Integer getCreatePMemberId() {
        return createPMemberId;
    }

    public void setCreatePMemberId(Integer createPMemberId) {
        this.createPMemberId = createPMemberId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "AlbumItem{" +
                "albumId=" + albumId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", defaultPicture='" + defaultPicture + '\'' +
                ", createPMemberId=" + createPMemberId +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
