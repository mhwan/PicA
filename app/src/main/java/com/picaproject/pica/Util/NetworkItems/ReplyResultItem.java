package com.picaproject.pica.Util.NetworkItems;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.picaproject.pica.Item.ReplyItem;

import java.util.List;

public class ReplyResultItem extends DefaultResultItem{
    @SerializedName("nickName")
    @Expose
    private String nickName;
    @SerializedName("result")
    @Expose
    private List<ReplyItem> result = null;


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<ReplyItem> getResult() {
        return result;
    }

    public void setResult(List<ReplyItem> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ReplyResultItem{" +
                "code = " + code +
                ", nickName='" + nickName + '\'' +
                ", result=" + result +
                '}';
    }
}
