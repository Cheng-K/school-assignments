package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private JButton modifyDetailsButton;
    private MyTableModel coachTableModel;
    private DefaultTableModel studentTableModel = (DefaultTableModel) studentRecordTable.getModel();
    private Admin admin;
    private setCoachPanel coachPanelManager;
    private setStudentPanel studentPanelManager;


    /*  Class : setCoachPanel
        Description : Responsible for setting up/ changing components in Coach Tab (Coach Tab Manager)
     */
    private class setCoachPanel {
        private ArrayList<Coach> coachList = new ArrayList<>();

        public setCoachPanel (){
            getAllCoach();
            prepareCoachTable();
            updateCoachTable();
            setSortDropMenu();
        }

        /*  Method Name : getAllCoach
            Description : Read coach file and instantiate all coach objects & store it in array list
         */

        private void getAllCoach () {
            String[] coachFileContent = FileServer.readFile(admin.getSportsCenterCode(),"Coach.txt");
            for (String coachInfo: coachFileContent){
                Coach coach = new Coach(coachInfo.split("\\|"));
                coachList.add(coach);
            }
        }

        /*  Method Name : prepareCoachTable
            Description : Set the number of columns in JTable
         */
        private void prepareCoachTable (){
            coachRecordTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            coachTableModel = new MyTableModel(Coach.getAllAttributes(),coachList.size());
            coachRecordTable.setModel(coachTableModel);
        }

        /*  Method Name : updateCoachTable
            Description : Add rows in JTable based on the order of arrayList containing coaches
                          (pair with clearCoachTable Method to clear & update table order)
         */
        private void updateCoachTable() {
            for (Coach coach : coachList){
                coachTableModel.addRow(coach.toString().split("\\|"));
            }
        }

        /*  Method Name : prepareCoachTable
            Description : Set the number of columns in JTable
         */
        private void setSortDropMenu (){
            sortByCoachMenu.addItem(new Coach.sortByID());
            sortByCoachMenu.addItem(new Coach.sortByRating());
            sortByCoachMenu.addItem(new Coach.sortByPay());
        }

        /*  Method Name : prepareCoachTable
            Description : Clear all the rows in JTable
         */
        private void clearCoachTable() {coachTableModel.setRowCount(0);}

    }

    /*  Class : setStudentPanel
        Description : Responsible for setting up/ changing components in Coach Tab (Student Tab Manager)
     */

    private class setStudentPanel {
        private ArrayList<Student> studentList = new ArrayList<>();

        public setStudentPanel() {
            getAllStudent();
            prepareStudentTable();
            updateStudentTable();
            setDropMenu();
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

        private void clearStudentTable() {studentTableModel.setRowCount(0);}

        private void setDropMenu () {sortByStudentMenu.addItem(new Student.sortByName());}

    }

    private class setSportsPanel {}

    /*  Class : sortTableButtonListener
        Description : Calls method to sort the list of objects (student,coach) in specified order
     */

    private class sortTableButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedTab = tabbedPane1.getSelectedIndex();
            switch (selectedTab){
                case 0 :
                    studentPanelManager.clearStudentTable();
                    admin.sort(studentPanelManager.studentList,(Comparator<Student>)sortByStudentMenu.getSelectedItem(),ascendingRadioButton.isSelected());
                    studentPanelManager.updateStudentTable(); // display the table in sorted order
                   break;
                case 1 :
                    coachPanelManager.clearCoachTable();
                    admin.sort(coachPanelManager.coachList,(Comparator<Coach>) sortByCoachMenu.getSelectedItem(),ascendingRadioButton.isSelected());
                    coachPanelManager.updateCoachTable();
                    break;
                default:
                    break;
            }
        }
    }

    private class modifyDetailsListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            // determine which tab is selected
            int tabNumber = tabbedPane1.getSelectedIndex();
            // check whether it is -1
            switch (tabNumber){
                case 0 :
                    break;
                case 1 :
                    coachTableModel.setRowEditable(coachRecordTable.getSelectedRow());
                    coachTableModel.isCellEditable(coachRecordTable.getSelectedRow(),0);
                    break;
                case 2:
                    break;
                default:
                    JOptionPane.showMessageDialog(frame,"Individual is not specified","Error",JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    private class mouseClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e){
            coachRecordTable.rowAtPoint(e.getPoint());
            modifyDetailsButton.setEnabled(true);
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
        modifyDetailsButton.addActionListener(new modifyDetailsListener());
        coachRecordTable.addMouseListener(new mouseClickListener());
        frame = new JFrame("Showing All Records");
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }


}
