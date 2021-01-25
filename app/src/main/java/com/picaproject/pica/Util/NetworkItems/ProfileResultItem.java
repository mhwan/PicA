package com.picaproject.pica.Util.NetworkItems;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileResultItem extends DefaultResultItem{
    @SerializedName("result")
    @Expose
    protected String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ProfileResultItem{" +
                "result='" + result + '\'' +
                ", code=" + code +
                '}';
    }
}
