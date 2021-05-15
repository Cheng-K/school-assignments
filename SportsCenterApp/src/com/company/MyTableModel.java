package com.company;

import javax.swing.table.DefaultTableModel;
import java.util.Arrays;

/*
    Class :       MyTableModel
    Description : Table that disable editing at default
 */

public class MyTableModel extends DefaultTableModel {

    boolean rowEditable[];

    public MyTableModel(Object[] columnNames, int rowCount) {
        super(columnNames,0);
        rowEditable = new boolean[rowCount];
    }

    public void setRowEditable (int rowIndex){
        rowEditable[rowIndex] = true;
    }

    public void disableRowEditable () {
        Arrays.fill(rowEditable, false);
    }


    @Override
    public boolean isCellEditable (int row, int column){
        return rowEditable[row];
    }




}
