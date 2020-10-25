package com.app_perso.tutorfinder_v2.util;

import android.text.TextUtils;
import android.util.Patterns;


public class StringUtils {

    public static boolean isNotValidEmail(String email) {
        return email == null || TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String toUpperCaseFirstLetter(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}
