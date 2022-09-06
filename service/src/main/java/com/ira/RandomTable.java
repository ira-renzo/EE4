package com.ira;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class RandomTable implements Serializable {
    private final ArrayList<ArrayList<Map<String, String>>> table = new ArrayList<>();

    public RandomTable(Map<Axis, Integer> dimensions) {
        reset(dimensions);
    }

    public String search(String keyword) {
        StringBuilder searchLines = new StringBuilder();
        for (int row_index = 0; row_index < table.size(); row_index++) {
            for (int column_index = 0; column_index < table.get(row_index).size(); column_index++) {
                Map<String, String> cell = table.get(row_index).get(column_index);
                String key = cell.keySet().iterator().next();
                String value = cell.get(key);
                int keyFrequency = countFrequency(keyword, key);
                int valueFrequency = countFrequency(keyword, value);
                if (keyFrequency > 0) {
                    String format = "Found \"%s\" On (y: %d, x: %d) With %d Instance/s On Key Field\n";
                    searchLines.append(String.format(format, keyword, row_index, column_index, keyFrequency));
                }
                if (valueFrequency > 0) {
                    String format = "Found \"%s\" On (y: %d, x: %d) With %d Instance/s On Value Field\n";
                    searchLines.append(String.format(format, keyword, row_index, column_index, valueFrequency));
                }
            }
        }
        if (searchLines.length() == 0) searchLines.append("No Occurrence");
        return searchLines.toString().trim();
    }

    private int countFrequency(String keyword, String fullString) {
        int occurrence = 0;
        int cur_index = 0;
        while ((cur_index = fullString.indexOf(keyword, cur_index)) != -1) {
            occurrence++;
            cur_index++;
        }
        return occurrence;
    }

    public void edit(Map<Axis, Integer> coordinates, Map<String, String> replacement) {
        table.get(coordinates.get(Axis.Y)).set(coordinates.get(Axis.X), replacement);
    }

    public String print() {
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

    public void reset(Map<Axis, Integer> dimensions) {
        table.clear();
        for (int rowIndex = 0; rowIndex < dimensions.get(Axis.Y); rowIndex++) {
            table.add(new ArrayList<>());
            for (int columnIndex = 0; columnIndex < dimensions.get(Axis.X); columnIndex++) {
                table.get(rowIndex).add(Collections.singletonMap(randomString(), randomString()));
            }
        }
    }

    public void addColumn(int rowSpan) {
        for (int missingRow = getRowLength(); missingRow < rowSpan; missingRow++) {
            table.add(new ArrayList<>());
        }
        for (int rowIndex = 0; rowIndex < rowSpan; rowIndex++) {
            table.get(rowIndex).add(Collections.singletonMap(randomString(), randomString()));
        }
    }

    public void addRow(int columnSpan) {
        ArrayList<Map<String, String>> newRow = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < columnSpan; columnIndex++) {
            newRow.add(Collections.singletonMap(randomString(), randomString()));
        }
        table.add(newRow);
    }

    public void sortRow() {
        for (ArrayList<Map<String, String>> row : table) {
            row.sort(Comparator.comparing(map -> {
                String key = map.keySet().iterator().next();
                return key + map.get(key);
            }));
        }
    }

    private String randomString() {
        int minLength = 3;
        int maxLength = 7;
        int length = (int) (Math.random() * (maxLength - minLength + 1)) + minLength;

        StringBuilder generatedString = new StringBuilder();
        for (int charIndex = 0; charIndex < length; charIndex++) {
            generatedString.append((char) ((int) (Math.random() * 94) + 33));
        }

        return generatedString.toString();
    }

    public int getRowLength() {
        return table.size();
    }

    public int[] getColumnLengths() {
        int[] columnLengths = new int[getRowLength()];
        for (int rowIndex = 0; rowIndex < getRowLength(); rowIndex++) {
            columnLengths[rowIndex] = table.get(rowIndex).size();
        }
        return columnLengths;
    }
}