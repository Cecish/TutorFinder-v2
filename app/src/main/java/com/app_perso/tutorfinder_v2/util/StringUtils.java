package com.app_perso.tutorfinder_v2.util;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class StringUtils {

    public static boolean isNotValidEmail(String email) {
        return email == null || TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String toUpperCaseFirstLetter(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    public static String listToString(List<String> stringList) {
        String res = "";
        int listSize = stringList.size()-1;
        StringBuilder resBuilder = new StringBuilder();

        for (int i = 0; i < listSize; i++) {
            resBuilder.append(stringList.get(i)).append(",");
        }

        res = resBuilder.toString();
        res += stringList.get(listSize);

        return res;
    }

    public static List<String> stringToList(String string) {
        return new ArrayList<>(Arrays.asList(string.split(",")));
    }

    public static String appendStringsAlphabetically(String str1, String str2) {
        String res;

        if (str1.compareTo(str2) <= 0) {
            res = str1 + "_" + str2;

        } else {
            res = str2 + "_" + str1;
        }

        return res;
    }

    public static String getOtherId(String fromTo, String id) {
        String res;

        String[] idParts = fromTo.split("_");

        if (idParts[0].equals(id)) {
            res = idParts[1];
        } else {
            res = idParts[0];
        }

        return res;
    }
}
