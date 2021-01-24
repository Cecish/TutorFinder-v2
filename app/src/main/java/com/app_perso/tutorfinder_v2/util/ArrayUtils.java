package com.app_perso.tutorfinder_v2.util;


import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {

    public static boolean isNullOrEmpty(List<String> listStr) {
        boolean res = true;

        if (listStr != null && listStr.size() > 0) {
            for (String itemStr: listStr) {
                if (itemStr != null && !itemStr.isEmpty()) {
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

    public static List<String> intersectionListStr(List<String> list1, List<String> list2) {
        List<String> list = new ArrayList<>();

        for (String t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }

    public static String listToString(List<String> list) {
        StringBuilder res = new StringBuilder();

        for (String item: list) {
            res.append(item).append(", ");
        }

        if (list.size() > 0) {
            res = new StringBuilder(res.substring(0, res.length() - 2));
        }

        return res.toString();
    }
}
