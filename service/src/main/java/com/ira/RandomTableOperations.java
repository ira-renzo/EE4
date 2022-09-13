package com.ira;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class RandomTableOperations {
    public static void initializeRandomTable(RandomTable table, int numberOfRows, int numberOfColumns) {
        reset(table, numberOfRows, numberOfColumns);
    }

    public static void reset(RandomTable table, int numberOfRows, int numberOfColumns) {
        table.clear();
        for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
            table.add(new ArrayList<>());
            for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
                table.get(rowIndex).add(Collections.singletonMap(randomString(), randomString()));
            }
        }
    }

    public static String search(RandomTable table, String keyword) {
        StringBuilder searchLines = new StringBuilder();
        for (int rowIndex = 0; rowIndex < table.size(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < table.get(rowIndex).size(); columnIndex++) {
                Map<String, String> cell = table.get(rowIndex).get(columnIndex);
                String key = cell.keySet().iterator().next();
                String value = cell.get(key);
                int keyFrequency = countFrequency(keyword, key);
                int valueFrequency = countFrequency(keyword, value);
                if (keyFrequency > 0) {
                    String format = "Found \"%s\" On (y: %d, x: %d) With %d Instance/s On Key Field\n";
                    searchLines.append(String.format(format, keyword, rowIndex, columnIndex, keyFrequency));
                }
                if (valueFrequency > 0) {
                    String format = "Found \"%s\" On (y: %d, x: %d) With %d Instance/s On Value Field\n";
                    searchLines.append(String.format(format, keyword, rowIndex, columnIndex, valueFrequency));
                }
            }
        }
        if (searchLines.length() == 0) searchLines.append("No Occurrence");
        return searchLines.toString().trim();
    }

    private static int countFrequency(String keyword, String fullString) {
        int occurrence = 0;
        int cur_index = 0;
        while ((cur_index = fullString.indexOf(keyword, cur_index)) != -1) {
            occurrence++;
            cur_index++;
        }
        return occurrence;
    }

    public static void edit(RandomTable table, int yCoordinate, int xCoordinate, Map<String, String> replacement) {
        table.get(yCoordinate).set(xCoordinate, replacement);
    }

    public static String print(RandomTable table) {
        StringBuilder tableText = new StringBuilder("\n");
        for (ArrayList<Map<String, String>> row : table) {
            StringBuilder rowText = new StringBuilder();
            for (Map<String, String> cell : row) {
                String key = cell.keySet().iterator().next();
                String cellText = String.format("%-7s : %-7s", key, cell.get(key));
                rowText.append(String.format("%-30s", cellText));
            }
            tableText.append(rowText).append("\n");
        }
        return tableText.toString().trim();
    }

    public static void addColumn(RandomTable table, int rowSpan) {
        for (int missingRow = table.getRowLength(); missingRow < rowSpan; missingRow++) {
            table.add(new ArrayList<>());
        }
        for (int rowIndex = 0; rowIndex < rowSpan; rowIndex++) {
            table.get(rowIndex).add(Collections.singletonMap(randomString(), randomString()));
        }
    }

    public static void addRow(RandomTable table, int columnSpan) {
        ArrayList<Map<String, String>> newRow = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < columnSpan; columnIndex++) {
            newRow.add(Collections.singletonMap(randomString(), randomString()));
        }
        table.add(newRow);
    }

    public static void sortEachRow(RandomTable table) {
        for (ArrayList<Map<String, String>> row : table) {
            row.sort(Comparator.comparing(map -> {
                String key = map.keySet().iterator().next();
                return key + map.get(key);
            }));
        }
    }

    private static String randomString() {
        int minLength = 3;
        int maxLength = 7;
        int length = (int) (Math.random() * (maxLength - minLength + 1)) + minLength;

        StringBuilder generatedString = new StringBuilder();
        for (int charIndex = 0; charIndex < length; charIndex++) {
            generatedString.append((char) ((int) (Math.random() * 94) + 33));
        }

        return generatedString.toString();
    }
}