package com.company;

import javax.swing.table.DefaultTableModel;

/*
    Class :       MyTableModel
    Description : Table that disable editing at default
 */

public class MyTableModel extends DefaultTableModel {

    boolean rowEditable[];

    public MyTableModel(Object[] columnNames, int rowCount) {
        super(columnNames,rowCount);
        rowEditable = new boolean[rowCount];
    }

    public void setRowEditable (int rowIndex){
        rowEditable[rowIndex] = true;
    }

    public void disableRowEditable (int rowIndex){
        rowEditable[rowIndex] = false;
    }

    @Override
    public boolean isCellEditable (int row, int column){
        return rowEditable[row];
    }




}
