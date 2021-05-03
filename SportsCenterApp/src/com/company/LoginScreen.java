package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Console;


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
                    SportCenter admin = new SportCenter(username, password);
                    if (admin.getMasterAdmin() == null) {
                        errorMessage.setText("Password or username entered is incorrect!");
                    } else {
                        AdminMenu screenSwitch = new AdminMenu();
                        frame.setVisible(false);
                    }

                } else if (studentBox.isSelected()) {
                    //Enter Student login here

                } else if (guestBox.isSelected()) {
                    //Enter guest login here
                } else if (!(studentBox.isSelected() || adminBox.isSelected() || guestBox.isSelected())) {
                    errorMessage.setText("Please select student, admin, or guest!");
                } else if ((usernameField.getText().equals("") || passwordField.getText().equals("")) && guestBox.isSelected() == false) {
                    errorMessage.setText("Please enter both username and password!");
                }

            }
        });
    }
}


