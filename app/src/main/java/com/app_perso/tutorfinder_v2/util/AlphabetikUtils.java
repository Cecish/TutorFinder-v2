package com.app_perso.tutorfinder_v2.util;

import com.app_perso.tutorfinder_v2.repository.model.Subject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlphabetikUtils {

    public static String[] getCustomAlphabetList(List<Subject> items) {
        Set<String> first_letters = new HashSet<>();
        String[] res;

        for (Subject item:items) {
            first_letters.add(item.getName().substring(0, 1).toUpperCase());
        }

        res = first_letters.toArray(new String[0]);
        Arrays.sort(res);

        return res;
    }

    public static int getPositionFromData(String character, List<Subject> orderedData) {
        int position = 0;
        for (Subject s : orderedData) {
            String letter = "" + s.getName().charAt(0);
            if (letter.equals("" + character)) {
                return position;
            }
            position++;
        }
        return 0;
    }
}
