package com.ira;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for RandomTable.
 */
public class RandomTableTest {
    RandomTable randomTable;

    @Before
    public void before() {
        Map<Axis, Integer> dimension = new HashMap<>();
        dimension.put(Axis.X, 5);
        dimension.put(Axis.Y, 5);
        randomTable = new RandomTable(dimension);
    }

    @Test
    public void testEditAndSearch() {
        Map<Axis, Integer> replacementCoordinates = new HashMap<>();
        replacementCoordinates.put(Axis.X, 1);
        replacementCoordinates.put(Axis.Y, 1);
        randomTable.edit(replacementCoordinates, Collections.singletonMap("aaa", "aaaa"));

        String expectedResult = "Found \"aa\" On (y: 1, x: 1) With 2 Instance/s On Key Field\n" +
                "Found \"aa\" On (y: 1, x: 1) With 3 Instance/s On Value Field";
        assertEquals(expectedResult, randomTable.search("aa"));
    }

    @Test
    public void checkThisManually() {
        System.out.println("<BEFORE SORT>");
        System.out.println(randomTable.print());
        randomTable.sortRow();
        System.out.println("\n<AFTER SORT>");
        System.out.println(randomTable.print());
    }
}