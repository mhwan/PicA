package com.picaproject.pica.Util.NetworkItems;

public class MemberRegisterItem {
    public String email;
    public String password;
    public String nickname;
    public String phonenumber;

    public MemberRegisterItem(String email, String password, String nickname, String phonenumber) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phonenumber = phonenumber;
    }
}
