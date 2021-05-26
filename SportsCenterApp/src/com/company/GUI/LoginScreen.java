package com.company.GUI;

import com.company.Admin;
import com.company.BaseStudent;
import com.company.Student;
import com.company.UnregStudent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class LoginScreen {
    private JFrame frame;
    private JPanel mainPanel;
    private JLabel U_Label;
    private JLabel P_Label;
    private JPasswordField passwordField;
    private JCheckBox studentBox;
    private JCheckBox adminBox;
    private JButton loginButton;
    private JTextField usernameField;
    private JCheckBox guestBox;
    private JLabel errorMessage;

    public LoginScreen() {
        JFrame frame = new JFrame("Login");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        errorMessage.setForeground(Color.RED);
        studentBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    adminBox.setSelected(false);
                    guestBox.setSelected(false);

                }
            }
        });


        adminBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    studentBox.setSelected(false);
                    guestBox.setSelected(false);
                }
            }
        });
        guestBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    studentBox.setSelected(false);
                    adminBox.setSelected(false);
                }
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                if (adminBox.isSelected()) {
                    Admin admin = Admin.adminLogin(username, password);
                    if (admin == null) {
                        errorMessage.setText("Password or username entered is incorrect!");
                    } else {
                        AdminMenu screenSwitch = new AdminMenu(admin);
                        frame.setVisible(false);
                    }

                } else if (studentBox.isSelected()) {
                    //Enter Student login here
                    Student student = Student.studentLogin(username,password);
                    if (student == null)
                        errorMessage.setText("Password or username entered is incorrect!");
                    else{
                        StudentMenu screenSwitch = new StudentMenu(student);
                        frame.setVisible(false);
                    }

                } else if (guestBox.isSelected()) {
                    //Enter guest login here
                    BaseStudent guest = new UnregStudent();
                    StudentMenu screenSwitch = new StudentMenu(guest);
                    frame.setVisible(false);
                } else if (!(studentBox.isSelected() || adminBox.isSelected() || guestBox.isSelected())) {
                    errorMessage.setText("Please select student, admin, or guest!");
                } else if ((usernameField.getText().equals("") || passwordField.getText().equals("")) && guestBox.isSelected() == false) {
                    errorMessage.setText("Please enter both username and password!");
                }

            }
        });
    }
}


