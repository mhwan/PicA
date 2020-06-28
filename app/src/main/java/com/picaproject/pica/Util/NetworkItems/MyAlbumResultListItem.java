package com.picaproject.pica.Util.NetworkItems;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.picaproject.pica.Item.AlbumItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAlbumResultListItem {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("result")
    @Expose
    private List<AlbumItem> result = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<AlbumItem> getResult() {
        return result;
    }

    public void setResult(List<AlbumItem> result) {
        this.result = result;
    }


    @Override
    public String toString() {
        return "MyAlbumResultListItem{" +
                "code=" + code +
                ", result=" + result +
                '}';
    }
}