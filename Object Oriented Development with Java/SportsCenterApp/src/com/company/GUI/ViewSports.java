package com.company.GUI;

import com.company.FileServer;
import com.company.Sports;
import com.company.RegisteredStudent;
import com.company.UnregisteredStudent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class ViewSports {
    private JFrame frame;
    private JPanel mainPanel;
    private JTable sportsTable;
    private JButton viewSportsScheduleButton;
    private JButton goBackButton;
    private JComboBox sportsCentreBox;
    private DefaultTableModel sportsTableModel;
    private RegisteredStudent student;
    private UnregisteredStudent guestStudent;
    private ArrayList<Sports> sportsList = new ArrayList<>();


    public ViewSports(RegisteredStudent student) {
        frame = new JFrame("Sports details");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        this.student = student;
        setTable(student.getSportsCenterCode());
        sportsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sportsCentreBox.setVisible(false);
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new StudentMenu(student);
            }
        });
        viewSportsScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowSelected = sportsTable.getSelectedRow();
                if (rowSelected == -1)
                    JOptionPane.showMessageDialog(frame,"Please select a row.");
                else {
                    new DisplayAllRecord(student, sportsList.get(rowSelected));
                    frame.dispose();
                }

            }
        });
    }

    public ViewSports(UnregisteredStudent guestStudent) {
        frame = new JFrame("Sports details");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        this.guestStudent = guestStudent;
        sportsCentreBox.addItem("---Select a sports centre---");
        sportsCentreBox.addItem("L001");
        sportsCentreBox.addItem("L002");
        sportsCentreBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!(sportsCentreBox.getSelectedIndex()==0)) {
                    setTable(String.valueOf(sportsCentreBox.getSelectedItem()));
                }
            }
        });
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new StudentMenu(guestStudent);
            }
        });
        viewSportsScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowSelected = sportsTable.getSelectedRow();
                if (rowSelected == -1)
                    JOptionPane.showMessageDialog(frame,"Please select a row.");
                else {
                    new DisplayAllRecord(guestStudent, sportsList.get(rowSelected));
                    frame.dispose();
                }

            }
        });
    }

    public void setTable(String sportsCentreCode){
        sportsList.clear();
        String[] sportFileContent = FileServer.readFile(sportsCentreCode,"Sports.txt");
        for (String sportsInfo:sportFileContent){
            Sports sport = new Sports(sportsCentreCode, sportsInfo.split("\\|"));
            sportsList.add(sport);
        }
        sportsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        sportsTableModel = new DefaultTableModel(Sports.getAllAttributes(),0){
            @Override
            public boolean isCellEditable (int row, int column){ return false; }
        };
        sportsTable.setModel(sportsTableModel);
        for(Sports sports:sportsList){
            sportsTableModel.addRow(sports.toString().split("\\|"));
        }
    }



}
