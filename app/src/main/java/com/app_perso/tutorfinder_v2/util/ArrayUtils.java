package com.app_perso.tutorfinder_v2.util;


import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {

    public static boolean isNullOrEmpty(List<String> listStr) {
        boolean res = true;

        if (listStr != null && listStr.size() > 0) {
            for (String itemStr: listStr) {
                if (!itemStr.isEmpty()) {
                    res = false;
                    break;
                }
            }
        }

        return res;
    }

    public static List<String> copyOf(List<String> list) {
        List<String> res = new ArrayList<>();

        list.remove("");

        for (String item: list) {
            res.add(item);
        }

        return res;
    }
}
