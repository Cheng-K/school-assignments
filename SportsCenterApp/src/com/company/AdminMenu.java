package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminMenu {
    private JFrame frame;
    private JButton createNewRecordButton;
    private JButton showRecordsButton;
    private JButton searchRecordButton;
    private JButton signOutButton;
    private JButton displaySportCenterDetailsButton;
    private JLabel secondHeading;
    private JLabel welcomeHeading;
    private JPanel adminMenuPanel;
    private Admin admin;

    /*  Description : Constructor that creates studentMenu frame
        Parameter   : Admin object (current user)
        Output      : Menu Screen
    */
    public AdminMenu (Admin admin){
        this.admin = admin;
        frame = new JFrame("Main Menu");
        welcomeHeading.setText("Welcome, Admin "+ admin.getID());
        frame.setContentPane(adminMenuPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        createNewRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new CreateRecordMenu(admin);
            }
        });
        showRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new DisplayAllRecord(admin);
            }
        });
        searchRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new AdminSearchForm(admin);
            }
        });
        signOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new LoginScreen();
            }
        });
    }

}
