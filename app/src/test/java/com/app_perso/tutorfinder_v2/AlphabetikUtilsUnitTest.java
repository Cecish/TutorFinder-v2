package com.app_perso.tutorfinder_v2;

import com.app_perso.tutorfinder_v2.repository.model.Subject;
import com.app_perso.tutorfinder_v2.util.AlphabetikUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class AlphabetikUtilsUnitTest {
    //testing the getPositionFromDataTest method and assuring that it returns the correct index of
    //the an element in the entryset according to the order
    @Test
    public void getPositionFromDataTest(){
        List<Subject> testCases = new ArrayList<>();
        testCases.add(new Subject("Ancient History"));
        testCases.add(new Subject("Computing"));
        testCases.add(new Subject("Math"));
        testCases.add(new Subject("Latin"));

        int[] expectedResults = {0,1,2,3};
        for (int i=0; i< expectedResults.length; i++){
            int actual = AlphabetikUtils.getPositionFromData(""+testCases.get(i).getName().charAt(0), testCases);
            assertEquals(actual,(expectedResults[i]));
        }
    }

    //Testing getCustomAlphabetSetTest, assuring that it returns the expected characters
    @Test
    public void getCustomAlphabetListTest() {
        List<Subject> testCases = new ArrayList<>();
        testCases.add(new Subject("Ancient History"));
        testCases.add(new Subject("Computing"));
        testCases.add(new Subject("Maths"));
        testCases.add(new Subject("Latin"));

        String[] expectedResults = {"A","C","L","M"};

        String[] actualResults = AlphabetikUtils.getCustomAlphabetList(testCases);
        for (int i=0; i< expectedResults.length; i++){
            assertEquals(actualResults[i],(expectedResults[i]));
        }
    }

}