package com.company;

import javax.swing.*;

public class AdminMenu {
    private JFrame frame;
    private JButton createNewRecordButton;
    private JButton showRecordsButton;
    private JButton searchRecordButton;
    private JButton modifyExistingRecordButton;
    private JButton signOutButton;
    private JButton displaySportCenterDetailsButton;
    private JLabel secondHeading;
    private JLabel welcomeHeading;
    private JPanel adminMenuPanel;
    // private Admin admin (admin object)

    /*  Description : Constructor that creates studentMenu frame
        Parameter   : Admin object (current user)
        Output      : Menu Screen
    */
    public AdminMenu (/*Admin admin*/){
        frame = new JFrame("Main Menu");
        frame.setContentPane(adminMenuPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
