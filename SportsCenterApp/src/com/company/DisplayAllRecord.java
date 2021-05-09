package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class DisplayAllRecord {
    private JFrame frame;
    private JTabbedPane tabbedPane1;
    private JTable coachRecordTable;
    private JPanel studentRecordPanel;
    private JPanel coachRecordPanel;
    private JPanel sportsRecordPanel;
    private JPanel rootPanel;
    private DefaultTableModel coachTableModel = (DefaultTableModel)coachRecordTable.getModel();
    private Admin admin;

    public DisplayAllRecord (Admin admin) {
        this.admin = admin;
        prepareCoachTable();
        frame = new JFrame("Showing All Records");
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void prepareCoachTable (){
        for (String column : Coach.getAllAttributes())
            coachTableModel.addColumn(column);
        for (String row : FileServer.readFile(admin.getSportsCenterCode(),"Coach.txt")) {
            String[] coachDetails = row.split("\\|");
            coachTableModel.addRow(coachDetails);
        }
    }

    public void prepareStudentTable() {}

    public void prepareSportsTable(){}
}
