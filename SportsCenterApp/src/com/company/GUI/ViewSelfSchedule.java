package com.company.GUI;

import com.company.FileServer;
import com.company.Session;
import com.company.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewSelfSchedule {
    private JFrame frame;
    private JPanel mainPanel;
    private JTextField sportsField;
    private JTextField coachField;
    private JTable scheduleTable;
    private JLabel sportLabel;
    private JLabel coachLabel;
    private JButton goBackButton;
    private DefaultTableModel scheduleTableModel;
    Student student;

    public ViewSelfSchedule(Student student) {
        frame = new JFrame("Schedule details");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        sportsField.setText(student.getRegisteredSports());
        sportsField.setEditable(false);
        coachField.setText(student.getCoach().getName());
        coachField.setEditable(false);
        this.student = student;
        setTable();
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new StudentMenu(student);
            }
        });
    }

    public void setTable() {
        ArrayList<Session> sessionArrayList = new ArrayList<>();
        String[] sessionFileContent = FileServer.readFile(student.getSportsCenterCode(),"Session.txt");
        for (String sessions:sessionFileContent){
            String[] sessionDetail = sessions.split("\\|");
            if (sessionDetail[7].equals(student.getCoach().getName())){
                Session session = new Session(sessionDetail);
                sessionArrayList.add(session);
            }
        }
        scheduleTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scheduleTableModel  = new DefaultTableModel(Session.getAllAttributes(),0){
            @Override
            public boolean isCellEditable (int row, int column){ return false; }
        };
        scheduleTable.setModel(scheduleTableModel);
        for (Session session:sessionArrayList){
            scheduleTableModel.addRow(session.toString().split("\\|"));
        }
    }
}
