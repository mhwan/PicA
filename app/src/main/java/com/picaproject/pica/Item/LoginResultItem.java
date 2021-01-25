package com.picaproject.pica.Item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.picaproject.pica.Util.NetworkItems.DefaultResultItem;

public class LoginResultItem extends DefaultResultItem {
    @SerializedName("member_id")
    @Expose
    protected Integer member_id;

    public Integer getMember_id() {
        return member_id;
    }

    public void setMember_id(Integer member_id) {
        this.member_id = member_id;
    }

    @Override
    public String toString() {
        return "LoginResultItem{" +
                "member_id=" + member_id +
                ", code=" + code +
                '}';
    }
}
