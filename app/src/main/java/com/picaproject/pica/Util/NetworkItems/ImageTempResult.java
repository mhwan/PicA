package com.picaproject.pica.Util.NetworkItems;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageTempResult {
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

    @Override
    public String toString() {
        return "ImageTempResult{" +
                "code=" + code +
                ", result=" + result +
                '}';
    }
}
