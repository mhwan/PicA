package com.picaproject.pica.Util;

import android.content.Context;

public class AcountPreference extends BaseSharedPreference{
    private final static String key_id = "ids";
    private final static String key_nickname = "nickNames";
    private final static String key_email= "emails";
    public AcountPreference(Context context) {
        super(context, "AccountData");
    }
    public void setId(int id) {
        putValue(key_id, id);
    }

    public void setNickname(String nickname) {
        putValue(key_nickname, nickname);
    }

    public String getNickname() {
        return getValue(key_nickname, "");
    }

    public void setEmail(String email) {
        putValue(key_email, email);
    }

    public String getEmail() {
        return getValue(key_email, "");
    }
    public int getId() {
        return getValue(key_id, -1);
    }
}
