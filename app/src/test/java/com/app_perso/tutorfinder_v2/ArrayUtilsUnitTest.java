package com.app_perso.tutorfinder_v2;

import com.app_perso.tutorfinder_v2.util.ArrayUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ArrayUtilsUnitTest {
    @Test
    public void isNullOrEmptyTest() {
        List<String> listStr1 = new ArrayList<String>() {{ add("Hello"); add("World"); add(""); }};
        List<String> listStr2 = new ArrayList<>();
        List<String> listStr3 = new ArrayList<String>() {{ add(null); }};
        List<String> listStr4 = new ArrayList<String>() {{ add(""); }};

        assertFalse(ArrayUtils.isNullOrEmpty(listStr1));
        assertTrue(ArrayUtils.isNullOrEmpty(null));
        assertTrue(ArrayUtils.isNullOrEmpty(listStr2));
        assertTrue(ArrayUtils.isNullOrEmpty(listStr3));
        assertTrue(ArrayUtils.isNullOrEmpty(listStr4));
    }

    @Test
    public void copyOfTest() {
        List<String> listStr1 = new ArrayList<String>() {{ add("Hello"); add("World"); add(""); add("!"); }};
        List<String> listStr2 = new ArrayList<String>() {{ add("Hello"); add("World"); add("!"); }};

        assertEquals(listStr2, ArrayUtils.copyOf(listStr1));
        assertEquals(listStr2, ArrayUtils.copyOf(listStr2));
    }

    @Test
    public void intersectionListStrTest() {
        List<String> listStrL1 = new ArrayList<String>() {{ add("Hello"); add("World"); add("!"); }};
        List<String> listStrR1 = new ArrayList<String>() {{ add("I"); add("am"); add("Cec"); }};
        List<String> listStrL2 = new ArrayList<String>() {{ add("Hello"); add("World"); add("!"); }};
        List<String> listStrR2 = new ArrayList<String>() {{ add("I"); add("am"); add("Cec"); add("hello"); add("."); }};
        List<String> listStrL3 = new ArrayList<String>() {{ add("Hello"); add("World"); add("!"); }};
        List<String> listStrR3 = new ArrayList<String>() {{ add("I"); add("am"); add("Cec"); add("Hello"); add("."); }};

        assertEquals(new ArrayList<String>(), ArrayUtils.intersectionListStr(listStrL1, listStrR1));
        assertEquals(new ArrayList<String>(), ArrayUtils.intersectionListStr(listStrL2, listStrR2));
        assertEquals(new ArrayList<String>() {{ add("Hello"); }}, ArrayUtils.intersectionListStr(listStrL3, listStrR3));
    }

    @Test
    public void listToString() {
        List<String> listStr1 = new ArrayList<String>() {{ add("Hello"); add("World"); add("!"); }};
        List<String> listStr2 = new ArrayList<String>() {{ add("Hello"); }};
        List<String> listStr3 = new ArrayList<>();

        assertEquals("Hello, World, !", ArrayUtils.listToString(listStr1));
        assertEquals("Hello", ArrayUtils.listToString(listStr2));
        assertEquals("", ArrayUtils.listToString(listStr3));
    }
}