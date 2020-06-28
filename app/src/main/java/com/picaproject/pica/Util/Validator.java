package com.picaproject.pica.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if (m.matches()) {
            err = true;
        }
        return err;
    }

    public static boolean isValidPhoneNumber(String s) {
        Pattern telephone = Pattern.compile("(\\d{3})[.-]?(\\d{3,4})[.-]?(\\d{4})");
        Matcher m = telephone.matcher(s);
        if (m.matches()) {
            return true;
        }
        return false;
    }


}
