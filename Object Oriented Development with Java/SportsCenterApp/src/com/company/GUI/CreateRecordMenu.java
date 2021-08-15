package com.company.GUI;

import com.company.Admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateRecordMenu {
    private JFrame frame;
    private JPanel mainPanel;
    private JLabel headerField;
    private JButton createNewStudentRecordButton;
    private JButton createNewCoachRecordButton;
    private JButton createNewSportsButton;
    private JButton approveStudentAccountsButton;
    private JButton goBackButton;
    private JButton createNewSessionButton;
    private Admin admin;

    public CreateRecordMenu(Admin admin) {
        this.admin = admin;
        frame = new JFrame("Create record menu");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        createNewStudentRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                String type = "student";
                new CreateRecord(admin,type);
            }
        });
        createNewCoachRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                String type = "coach";
                new CreateRecord(admin,type);
            }
        });
        createNewSportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                String type = "sport";
                new CreateRecord(admin,type);
            }
        });
        createNewSessionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                String type = "session";
                new CreateRecord(admin,type);
            }
        });
        approveStudentAccountsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new ApproveStudents(admin);
            }
        });
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new AdminMenu(admin);
            }
        });
    }
}
