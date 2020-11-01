package com.picaproject.pica.Util.NetworkItems;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 앨범 보기를 할때의 결과 item
 */
public class AlbumResultItem extends DefaultResultItem {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("defaultPicture")
    @Expose
    private String defaultPicture;
    @SerializedName("result")
    @Expose
    private List<ImageResultItem> result = null;


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

    public List<ImageResultItem> getResult() {
        return result;
    }

    public void setResult(List<ImageResultItem> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "AlbumResultItem{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", defaultPicture='" + defaultPicture + '\'' +
                ", result=" + result +
                '}';
    }
}