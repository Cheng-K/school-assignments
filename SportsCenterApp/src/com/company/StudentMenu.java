package com.company;

import javax.swing.*;

public class StudentMenu {
    private JFrame frame;
    private JLabel welcomeHeading;
    private JButton viewSportsDetailsButton;
    private JButton personalProfileButton;
    private JButton coachDetailsButton;
    private JButton sportScheduleButton;
    private JLabel secondHeading;
    private JPanel studentMenuPanel;
    private JButton signOutButton;
    // private Student studentA ;

    /*  Description : Constructor that creates studentMenu frame
        Parameter   : Student object (current user)
        Output      : Menu Screen
     */
    public StudentMenu (/*Student studentA*/){
        frame = new JFrame("Main Menu");
        frame.setContentPane(studentMenuPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

}
