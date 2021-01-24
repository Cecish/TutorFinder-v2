package com.app_perso.tutorfinder_v2;

import com.app_perso.tutorfinder_v2.repository.model.Subject;
import com.app_perso.tutorfinder_v2.util.SubjectUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class SubjectUtilsUnitTest {
    @Test
    public void intersectSubjectsTest() {
        List<Subject> listL1 = new ArrayList<Subject>() {{ add(new Subject("1", "Maths")); add(new Subject("2", "French")); }};
        List<String> listR1 = new ArrayList<String>() {{ add("3"); add("4"); }};
        List<Subject> listL2 = new ArrayList<Subject>() {{ add(new Subject("1", "Maths")); add(new Subject("2", "French")); }};
        List<String> listR2 = new ArrayList<String>() {{ add("2"); add("5"); }};
    }

    @Test(expected = NullPointerException.class)
    public void intersectSubjectsInvalidDataTest1() {
        List<Subject> listL3 = null;
        List<String> listR3 = new ArrayList<String>() {{ add("2"); add("5"); }};

        // should throw NullPointerException
        assertEquals(new ArrayList<String>(), SubjectUtils.intersectSubjects(listL3, listR3));
    }

    @Test(expected = NullPointerException.class)
    public void intersectSubjectsInvalidDataTest2() {
        List<Subject> listL4 = new ArrayList<Subject>() {{ add(new Subject("1", "Maths")); add(new Subject("2", "French")); }};
        List<String> listR4 = null;

        // should throw NullPointerException
        assertEquals(new ArrayList<String>(), SubjectUtils.intersectSubjects(listL4, listR4));
    }

    @Test(expected = NullPointerException.class)
    public void intersectSubjectsInvalidDataTest3() {
        List<Subject> listL5 = null;
        List<String> listR5 = null;

        // should throw NullPointerException
        assertEquals(new ArrayList<String>(), SubjectUtils.intersectSubjects(listL5, listR5));
    }
}