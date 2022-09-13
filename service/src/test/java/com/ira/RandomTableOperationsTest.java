package com.ira;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class RandomTableOperationsTest {
    RandomTable randomTable;
    List<Map<String, String>> randomTableContents = new ArrayList<>();

    {
        randomTableContents.add(Collections.singletonMap("bR", "EAd"));
        randomTableContents.add(Collections.singletonMap("AA", "aaa"));
        randomTableContents.add(Collections.singletonMap("GrApe", "S"));
        randomTableContents.add(Collections.singletonMap("BBB", "cC"));
    }

    @Before
    public void before() {
        randomTable = new RandomTable();
        RandomTableOperations.initializeRandomTable(randomTable, 2, 2);

        /*
         * bR:EAd   AA:aaa
         * GrApe:S  BBB:cC
         */
        RandomTableOperations.edit(randomTable, 0, 0, randomTableContents.get(0));
        RandomTableOperations.edit(randomTable, 0, 1, randomTableContents.get(1));
        RandomTableOperations.edit(randomTable, 1, 0, randomTableContents.get(2));
        RandomTableOperations.edit(randomTable, 1, 1, randomTableContents.get(3));
    }

    @Test
    public void successfulSearchTest() {
        String searchResult = RandomTableOperations.search(randomTable, "A");
        String expectedResult = "" +
                "Found \"A\" On (y: 0, x: 0) With 1 Instance/s On Value Field\n" +
                "Found \"A\" On (y: 0, x: 1) With 2 Instance/s On Key Field\n" +
                "Found \"A\" On (y: 1, x: 0) With 1 Instance/s On Key Field";
        assertEquals(expectedResult, searchResult);
    }

    @Test
    public void failedSearchTest() {
        String searchResult = RandomTableOperations.search(randomTable, "PHONE");
        String expectedResult = "No Occurrence";
        assertEquals(expectedResult, searchResult);
    }

    @Test
    public void printTest() {
        String[] printLines = RandomTableOperations.print(randomTable).split("\n");
        Assert.assertArrayEquals(
                randomTableContents.toArray(),
                Arrays.stream(printLines)
                        .map(row -> row.replaceAll("\\s+:\\s+", ":"))
                        .flatMap(row -> Arrays.stream(row.split("\\s+")))
                        .map(cell -> {
                            String[] keyValue = cell.split(":");
                            return Collections.singletonMap(keyValue[0], keyValue[1]);
                        }).toArray());
    }

    @Test
    public void addColumnTest() {
        RandomTableOperations.addColumn(randomTable, 3);
        int[] expectedColumnLengths = {3, 3, 1};
        assertArrayEquals(expectedColumnLengths, randomTable.getColumnLengths());
    }

    @Test
    public void addRowTest() {
        RandomTableOperations.addRow(randomTable, 3);
        RandomTableOperations.addRow(randomTable, 5);
        int[] expectedColumnLengths = {2, 2, 3, 5};
        assertArrayEquals(expectedColumnLengths, randomTable.getColumnLengths());
    }

    @Test
    public void sortEachRowTest() {
        RandomTableOperations.sortEachRow(randomTable);
        assertEquals(randomTableContents.get(0), randomTable.get(0).get(1));
        assertEquals(randomTableContents.get(1), randomTable.get(0).get(0));
        assertEquals(randomTableContents.get(2), randomTable.get(1).get(1));
        assertEquals(randomTableContents.get(3), randomTable.get(1).get(0));
    }
}