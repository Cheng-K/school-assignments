package com.company;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class ApproveStudents {
    private JFrame frame;
    private JPanel mainPanel;
    private JTable studentTable;
    private JTextField nameField;
    private JButton approveButton;
    private JButton rejectButton;
    private JButton goBackButton;
    private JLabel errorLabel;
    private Admin admin;
    private DefaultTableModel studentTableModel = (DefaultTableModel)studentTable.getModel();
    private setTable tableManager;
    private class setTable {
        private ArrayList<Student> studentList = new ArrayList<>();
        public setTable() {
            String[] fileContent = FileServer.readFile("Unregstudent.txt");
            if (fileContent.length == 1 && fileContent[0] == "") {
                studentTableModel.addColumn("There are currently no accounts to be approved.");
            } else {
                for (String line : fileContent) {
                    String[] token = line.split("\\|");
                    Student newStudent = new Student(token);
                    if (newStudent.getSportsCenterCode().equals(admin.getSportsCenterCode())) {
                        studentList.add(newStudent);
                    }
                }
                studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                for (String column : Student.getAllAttributes()) {
                    studentTableModel.addColumn(column);
                }
                for (Student student : studentList) {
                    studentTableModel.addRow(student.toString().split("\\|"));
                }
            }

        }
    }
    public boolean checkName(String name) {
        for (Student student : tableManager.studentList) {
            if (name.equals(student.getName())) {
                return true;
            }
        }
        return false;
    }

    public ApproveStudents(Admin admin) {
        this.admin = admin;
        frame = new JFrame("Student profile approval");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        tableManager = new setTable();
        approveButton.setForeground(Color.blue);
        rejectButton.setForeground(Color.red);
        errorLabel.setForeground(Color.red);
        frame.setVisible(true);
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new CreateRecordMenu(admin);
            }
        });
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                if (name.equals("")) {
                    errorLabel.setText("Please enter a student name!");
                } else if(checkName(name)==false) {
                    errorLabel.setText("Student name does not exist!"); }
                else {
                    admin.approveAccount(name);
                    JOptionPane.showMessageDialog(frame, "Student account has been approved!",
                                "Successful", JOptionPane.INFORMATION_MESSAGE);
                    studentTableModel.setRowCount(0);
                    studentTableModel.setColumnCount(0);
                    tableManager = new setTable();
                    }
                }
        });
        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                if (name.equals(""))
                {
                    errorLabel.setText("Please enter a student name!"); }
                else if (checkName(name)==false){
                    errorLabel.setText("Student name does not exist!");
                }
                else{
                    admin.rejectAccount(name);
                    studentTableModel.setRowCount(0);
                    studentTableModel.setColumnCount(0);
                    tableManager = new setTable();
                }
            }
        });
    }
}
