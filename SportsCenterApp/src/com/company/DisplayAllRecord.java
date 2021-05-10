package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DisplayAllRecord {
    private JFrame frame;
    private JTabbedPane tabbedPane1;
    private JTable coachRecordTable;
    private JPanel studentRecordPanel;
    private JPanel coachRecordPanel;
    private JPanel sportsRecordPanel;
    private JPanel rootPanel;
    private JScrollPane coachTableContainer;
    private JComboBox sortByDropMenu;
    private ButtonGroup radioButtonGroup = new ButtonGroup();
    private JRadioButton ascendingRadioButton;
    private JRadioButton descendingRadioButton;
    private JButton sortTableButton;
    private JButton backToMenuButton;
    private JLabel sortByLabel;
    private JLabel orderLabel;
    private DefaultTableModel coachTableModel = (DefaultTableModel)coachRecordTable.getModel();
    private Admin admin;

    private class setCoachPanel {
        private ArrayList<Coach> coachList = new ArrayList<Coach>();

        public setCoachPanel (){
            getAllCoach();
            prepareCoachTable();
            setSortDropMenu(new String[]{"Coach ID", "Hourly Rate", "Rating"});
        }

        private void getAllCoach () {
            String[] coachFileContent = FileServer.readFile(admin.getSportsCenterCode(),"Coach.txt");
            // Instantiate all coaches & store in coach list
            for (String coachInfo: coachFileContent){
                Coach coach = new Coach(coachInfo.split("\\|"));
                coachList.add(coach);
            }
        }

        private void prepareCoachTable (){
            coachRecordTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            for (String column : Coach.getAllAttributes())
                coachTableModel.addColumn(column);
            for (Coach coach: coachList) {
                coachTableModel.addRow(coach.toString().split("\\|"));
            }
        }
        private void setSortDropMenu (String [] options){
            for (String value : options)
                sortByDropMenu.addItem(value);
        }

    }

    private class setStudentPanel {}

    private class setSportsPanel {}

    private class sortTableButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedTab = tabbedPane1.getSelectedIndex();
            switch (selectedTab){
//                case 0 :
//                   determine the tab, and the determine sort by what in what order then pass into the respective class
            }
        }
    }

    public DisplayAllRecord (Admin admin) {
        this.admin = admin;
        setCoachPanel coachPanelManager = new setCoachPanel();
        radioButtonGroup.add(ascendingRadioButton);
        radioButtonGroup.add(descendingRadioButton);
        frame = new JFrame("Showing All Records");
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
