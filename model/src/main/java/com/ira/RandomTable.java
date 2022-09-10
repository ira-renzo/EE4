package com.ira;

import java.util.ArrayList;
import java.util.Map;

public class RandomTable extends ArrayList<ArrayList<Map<String, String>>> {

    public RandomTable() {
    }

    public int getRowLength() {
        return this.size();
    }

    public int[] getColumnLengths() {
        int[] columnLengths = new int[getRowLength()];
        for (int rowIndex = 0; rowIndex < getRowLength(); rowIndex++) {
            columnLengths[rowIndex] = this.get(rowIndex).size();
        }
        return columnLengths;
    }
}