package com.app_perso.tutorfinder_v2.util;

import com.app_perso.tutorfinder_v2.repository.model.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectUtils {
    public static List<String> intersectSubjects(List<Subject> subjects, List<String> subjectIds) {
        List<String> list = new ArrayList<>();

        for (Subject t : subjects) {
            if(subjectIds.contains(t.getId())) {
                list.add(t.getName());
            }
        }

        return list;
    }
}
