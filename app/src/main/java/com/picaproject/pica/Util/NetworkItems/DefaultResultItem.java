package com.picaproject.pica.Util.NetworkItems;

public class DefaultResultItem
{
    private int code;

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
