package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateStudentProfile{
    private JFrame frame;
    private JPanel mainPanel;
    private JTextField ageField;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField contactField;
    private JTextField emailField;
    private JTextField sportsField;
    private JTextField passwordField;
    private JLabel nameLabel;
    private JLabel ageLabel;
    private JLabel addressLabel;
    private JLabel contactLabel;
    private JLabel eMailLabel;
    private JLabel sportsLabel;
    private JLabel passwordLabel;
    private JButton goBackButton;
    private JButton registerButton;
    private JLabel errorLabel;


    public CreateStudentProfile(Admin admin)
    {
        frame = new JFrame("Create student profile");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        errorLabel.setForeground(Color.red);
        frame.setVisible(true);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] studentDetail= {nameField.getText(),"null",ageField.getText(),addressField.getText(),contactField.getText(),
                emailField.getText(),sportsField.getText(),admin.getSportsCenterCode()};
                for (String item:studentDetail)
                {
                    if (item.equals("")) {
                        errorLabel.setText("Enter details for all options above!");
                    }
                }
                if (passwordField.getText().equals("")) {
                    errorLabel.setText("Enter details for all options above!");
                }
                else if (studentDetail[0].length()>5) {
                    errorLabel.setText("Name entered is too short!");}
                else if (studentDetail[4].length()>10){
                    errorLabel.setText("Contact entered is too short!"); }
                else
                {
                    try{
                        Integer.parseInt(studentDetail[2]);
                        int check = admin.createAccount(studentDetail,passwordField.getText());
                        switch (check) {
                            case 0: JOptionPane.showMessageDialog(frame, "Account successfully created!",
                                    "Successful", JOptionPane.INFORMATION_MESSAGE);
                                    break;
                            case 1: JOptionPane.showMessageDialog(frame,"Profile with the same name is already created and awaiting for admins' approval!",
                                    "Error",JOptionPane.ERROR_MESSAGE);
                            break;
                            case 2: JOptionPane.showMessageDialog(frame,"Profile with the same name already exists!",
                                    "Error",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    catch (Exception a)
                    {
                        errorLabel.setText("Please enter a number of the age field!");
                    }

                }
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
