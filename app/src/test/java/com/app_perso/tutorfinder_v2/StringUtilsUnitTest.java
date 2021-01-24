package com.app_perso.tutorfinder_v2;

import com.app_perso.tutorfinder_v2.util.StringUtils;

import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class StringUtilsUnitTest {
    @Test
    public void toUpperCaseFirstLetterTest() {
        String text1 = "hello";
        String text2 = "a";
        String text3 = "hello world";

        assertEquals("Hello", StringUtils.toUpperCaseFirstLetter(text1));
        assertEquals("A", StringUtils.toUpperCaseFirstLetter(text2));
        assertEquals("Hello world", StringUtils.toUpperCaseFirstLetter(text3));
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void toUpperCaseFirstLetterInvalidDataTest2() {
        String text4 = "";

        // should throw StringIndexOutOfBoundsException
        StringUtils.toUpperCaseFirstLetter(text4);
    }


    @Test(expected = NullPointerException.class)
    public void toUpperCaseFirstLetterInvalidDataTest1() {
        String text5 = null;

        // should throw NullPointerException
        StringUtils.toUpperCaseFirstLetter(text5);
    }

    @Test
    public void listToStringTest() {
        List<String> listStr1 = new ArrayList<String>() {{ add("Hello"); add("World"); add("!"); }};
        List<String> listStr2 = new ArrayList<String>() {{ add("Hello"); }};
        List<String> listStr3 = new ArrayList<>();

        assertEquals("Hello,World,!", StringUtils.listToString(listStr1));
        assertEquals("Hello", StringUtils.listToString(listStr2));
        assertEquals("", StringUtils.listToString(listStr3));
    }

    @Test
    public void stringToListTest() {
        String text1 = "Hello, world,nice to meet you";
        String text2 = "";
        String text3 = "Hello";

        assertEquals(new ArrayList<String>() {{ add("Hello"); add(" world"); add("nice to meet you"); }}, StringUtils.stringToList(text1));
        assertEquals(new ArrayList<String>() {{ add(""); }}, StringUtils.stringToList(text2));
        assertEquals(new ArrayList<String>() {{ add("Hello"); }}, StringUtils.stringToList(text3));
    }

    @Test(expected = NullPointerException.class)
    public void stringToListInvalidDataTest1() {
        String text4 = null;

        // should throw NullPointerException
        StringUtils.stringToList(text4);
    }

    @Test
    public void appendStringsAlphabetically() {
        String strL1 = "World";
        String strR1 = "Hello";
        String strL2 = "Hello";
        String strR2 = "hello";
        String strL3 = "Hello";
        String strR3 = "Hello";

        assertEquals(strR1+"_"+strL1, StringUtils.appendStringsAlphabetically(strL1, strR1));
        assertEquals(strL2+"_"+strR2, StringUtils.appendStringsAlphabetically(strL2, strR2));
        assertEquals(strR3+"_"+strL3, StringUtils.appendStringsAlphabetically(strL3, strR3));
    }

    @Test
    public void getOtherIdTest() {
        String fromTo1 = "A_B";
        String id1 = "A";
        String id2 = "B";

        assertEquals(id2, StringUtils.getOtherId(fromTo1, id1));
        assertEquals(id1, StringUtils.getOtherId(fromTo1, id2));
    }
}