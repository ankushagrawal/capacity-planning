package com.capacity.planner.engine.impl;

/**
 * Created by ankush.a on 17/03/17.
 */
public class Coordinate{
    private int rowIndex;

    public int getColumnIndex() {
        return ColumnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        ColumnIndex = columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    private int ColumnIndex;

    @Override
    public String toString() {
        return "Coordinate{" +
                "rowIndex=" + rowIndex +
                ", ColumnIndex=" + ColumnIndex +
                '}';
    }
}
