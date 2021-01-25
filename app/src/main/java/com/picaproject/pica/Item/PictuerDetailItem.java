package com.picaproject.pica.Item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.picaproject.pica.Util.NetworkItems.ImageResultItem;

public class PictuerDetailItem {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("result")
    @Expose
    private ImageResultItem result;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ImageResultItem getResult() {
        return result;
    }

    public void setResult(ImageResultItem result) {
        this.result = result;
    }

}
