package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;

public class DisplayAllRecord {
    private JFrame frame;
    private JTabbedPane tabbedPane1;
    private JPanel rootPanel;
    private JPanel studentRecordPanel;
    private JPanel coachRecordPanel;
    private JPanel sportsRecordPanel;
    private JScrollPane studentTableContainer;
    private JScrollPane coachTableContainer;
    private JScrollPane sportsTableContainer;
    private JTable studentRecordTable;
    private JTable coachRecordTable;
    private JTable sportsRecordTable;
    private JComboBox<Comparator <Coach>> sortByCoachMenu;
    private JComboBox<Comparator <Student>> sortByStudentMenu;
    private JComboBox<Comparator <Sports>> sortBySportsMenu;
    private JLabel orderLabel;
    private ButtonGroup radioButtonGroup = new ButtonGroup();
    private JRadioButton ascendingRadioButton;
    private JRadioButton descendingRadioButton;
    private JButton sortTableButton;
    private JButton backToMenuButton;
    private DefaultTableModel coachTableModel = (DefaultTableModel)coachRecordTable.getModel();
    private DefaultTableModel studentTableModel = (DefaultTableModel)studentRecordTable.getModel();
    private Admin admin;
    private setCoachPanel coachPanelManager;
    private setStudentPanel studentPanelManager;

    private class setCoachPanel {
        private ArrayList<Coach> coachList = new ArrayList<>();

        public setCoachPanel (){
            getAllCoach();
            prepareCoachTable();
            updateCoachTable();
            setSortDropMenu();
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
        }

        private void updateCoachTable() {
            for (Coach coach : coachList){
                coachTableModel.addRow(coach.toString().split("\\|"));
            }
        }
        private void setSortDropMenu (){
            sortByCoachMenu.addItem(new Coach.sortByID());
            sortByCoachMenu.addItem(new Coach.sortByRating());
            sortByCoachMenu.addItem(new Coach.sortByPay());
        }

        private void clearCoachTable() {coachTableModel.setRowCount(0);}

    }

    private class setStudentPanel {
        private ArrayList<Student> studentList = new ArrayList<>();

        public setStudentPanel() {
            getAllStudent();
            prepareStudentTable();
            updateStudentTable();
        }

        private void getAllStudent () {
            String[] studentFileContent = FileServer.readFile(admin.getSportsCenterCode(),"Student.txt");
            for (String studentInfo : studentFileContent){
                Student student = new Student(studentInfo.split("\\|"));
                studentList.add(student);
            }
        }

        private void prepareStudentTable() {
            studentRecordTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            for (String column : Student.getAllAttributes())
                studentTableModel.addColumn(column);
        }

        private void updateStudentTable() {
            for (Student student:studentList)
                studentTableModel.addRow(student.toString().split("\\|"));
        }

    }

    private class setSportsPanel {}

    private class sortTableButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedTab = tabbedPane1.getSelectedIndex();
            switch (selectedTab){
                case 0 :
                   break;
                case 1 :
                    coachPanelManager.clearCoachTable();
                    admin.sortCoaches(coachPanelManager.coachList,(Comparator<Coach>) sortByCoachMenu.getSelectedItem(),ascendingRadioButton.isSelected());
                    coachPanelManager.updateCoachTable();
                    break;
                default:
                    break;
            }
            JOptionPane.showMessageDialog(frame,"Sorted Successfully");
        }
    }

    public DisplayAllRecord (Admin admin) {
        this.admin = admin;
        coachPanelManager = new setCoachPanel();
        studentPanelManager = new setStudentPanel();
        radioButtonGroup.add(ascendingRadioButton);
        radioButtonGroup.add(descendingRadioButton);
        ascendingRadioButton.setSelected(true);
        sortTableButton.addActionListener(new sortTableButtonListener());
        frame = new JFrame("Showing All Records");
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
