package com.company.GUI;

import com.company.BaseStudent;
import com.company.Coach;
import com.company.RegisteredStudent;
import com.company.UnregisteredStudent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JPanel regStudentButtonPanel;
    private JButton registerButton;
    private JPanel guestButtonPanel;
    private RegisteredStudent regStudent;
    private UnregisteredStudent guestStudent;

    /*  Description : Constructor that creates studentMenu frame
        Parameter   : BaseStudent object (current user)
        Output      : Menu Screen
     */
    public StudentMenu (BaseStudent studentA){
        frame = new JFrame("Main Menu");
        if (studentA instanceof RegisteredStudent) {
            regStudent = (RegisteredStudent) studentA;
            guestStudent = null;
            viewSportsDetailsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                    new ViewSports(regStudent);
                }
            });
        }
        else{
            guestStudent = (UnregisteredStudent) studentA;
            regStudent = null;
            viewSportsDetailsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                    new ViewSports(guestStudent);
                }
            });
        }

        setWelcomeHeading();
        setVisibleButtons();
        frame.setContentPane(studentMenuPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        personalProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new StudentProfile(regStudent);
            }
        });
        signOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new LoginScreen();
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new CreateRecord(guestStudent);
            }
        });
        coachDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new CoachProfile(regStudent);
            }
        });
        sportScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new ViewSelfSchedule(regStudent);
            }
        });
    }

    /*
        Method name : setVisibleButtons
        Description : Sets which buttons that can be viewed by registered and non-registered student
     */

    public void setVisibleButtons () {
        if (guestStudent != null)
            regStudentButtonPanel.setVisible(false);
        else
            guestButtonPanel.setVisible(false);
    }

    /*
        Method name : setWelcomeHeading
        Description : Sets welcome message according to type of student (registered/unregistered)
     */
    public void setWelcomeHeading(){
        if (guestStudent != null)
            welcomeHeading.setText("Welcome, Guest 001");
        else
            welcomeHeading.setText("Welcome, "+ regStudent.getName());
    }

}
