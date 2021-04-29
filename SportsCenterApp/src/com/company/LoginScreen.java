package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Console;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class LoginScreen extends JFrame {
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

    public LoginScreen(String title)
    {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        errorMessage.setForeground(Color.RED);
        studentBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED)
                {
                    adminBox.setSelected(false);
                    guestBox.setSelected(false);

                }            }});


        adminBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange()==ItemEvent.SELECTED)
                {
                    studentBox.setSelected(false);
                    guestBox.setSelected(false);
                }
            }
        });
        guestBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange()==ItemEvent.SELECTED)
                {
                    studentBox.setSelected(false);
                    adminBox.setSelected(false);
                }
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(studentBox.isSelected() || adminBox.isSelected() || guestBox.isSelected())) {
                    errorMessage.setText("Please select student, admin, or guest!");
                }
                else if ((usernameField.getText().equals("") || passwordField.getText().equals(""))&& guestBox.isSelected()==false)
                {
                    errorMessage.setText("Please enter both username and password!");
                }


                String username = usernameField.getText();
                String password = passwordField.getText();
                /*Enter file check code here*/

            }
        });
    }


    public static void main(String[] args) {
        JFrame frame = new LoginScreen("Login");
        frame.setVisible(true);
    }
}