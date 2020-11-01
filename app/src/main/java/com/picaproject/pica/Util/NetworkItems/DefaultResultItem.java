package com.picaproject.pica.Util.NetworkItems;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DefaultResultItem
{
    @SerializedName("code")
    @Expose
    protected Integer code;

    public int getCode ()
    {
        return code;
    }

    public void setCode (int code)
    {
        this.code = code;
    }

    @Override
    public String toString()
    {
        return "[result = "+code+"]";
    }
}
