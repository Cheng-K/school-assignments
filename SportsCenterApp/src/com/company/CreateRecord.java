package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Locale;

public class CreateRecord {
    private JFrame frame;
    private JPanel mainPanel;
    private JTextField ageField;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField contactField;
    private JTextField emailField;
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
    private JLabel hourlyRateLabel;
    private JTextField hourlyRateField;
    private JLabel sportsCodeLabel;
    private JLabel headerLabel;
    private JLabel sportsCentreCodeLabel;
    private JComboBox sportsBox;
    private JComboBox sportsCentreBox;
    private JComboBox sportsCodeBox;


    public CreateRecord(Admin admin, String type) {
        frame = new JFrame("Create " + type + " profile");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        errorLabel.setForeground(Color.red);
        frame.setVisible(true);
        headerLabel.setText("Enter " + type + " details below: ");
        setComboBox(admin.getSportsCenterCode());
        if (type.equals("student")) {
            setStudentVisibility();
        } else if (type.equals("coach")) {
            setCoachVisibility();
        } else if (type.equals("sport")) {
            setStudentVisibility();
            setCoachVisibility();
            addressField.setVisible(false);
            addressLabel.setVisible(false);
            contactField.setVisible(false);
            contactLabel.setVisible(false);
        }
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (type.equals("student")) {
                    String[] studentDetail = {nameField.getText(), "null", ageField.getText(), addressField.getText(), contactField.getText(),
                            emailField.getText(), String.valueOf(sportsBox.getSelectedItem()),admin.getSportsCenterCode()};
                    for (String item : studentDetail) {
                        if (item.equals("")) {
                            errorLabel.setText("Enter details for all options above!");
                        }
                    }
                    if (passwordField.getText().equals("")) {
                        errorLabel.setText("Enter details for all options above!");
                    } else if (studentDetail[0].length() < 8) {
                        errorLabel.setText("Name entered is too short! (min. 8 char)");
                    } else if (studentDetail[4].length() < 10) {
                        errorLabel.setText("Contact entered is invalid!");
                    } else if (!(studentDetail[5].contains("@"))) {
                        errorLabel.setText("Format of email entered is invalid!");
                    } else if (sportsBox.getSelectedItem().equals("---")) {
                        errorLabel.setText("Please select a sport!");
                    } else {
                        try {
                            Integer.parseInt(studentDetail[2]);
                            System.out.println("hi");
                            int check = admin.createAccount(studentDetail, passwordField.getText());
                            switch (check) {
                                case 0:
                                    JOptionPane.showMessageDialog(frame, "Account successfully created!",
                                            "Successful", JOptionPane.INFORMATION_MESSAGE);
                                    frame.setVisible(false);
                                    new AdminMenu(admin);
                                    break;
                                case 1:
                                    JOptionPane.showMessageDialog(frame, "Profile with the same name is already created and awaiting for admins' approval!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    break;
                                case 2:
                                    JOptionPane.showMessageDialog(frame, "Profile with the same name already exists!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception a) {
                            errorLabel.setText("Please enter a number for the age field!");
                        }
                    }
                } else if (type.equals("coach")) {
                    if (nameField.getText().equals("") || addressField.getText().equals("") ||
                            contactField.getText().equals("")) {
                        errorLabel.setText("Enter details for all options above!");
                    } else if (nameField.getText().length() < 8) {
                        errorLabel.setText("Name entered is too short! (min. 8 char)");
                    } else if (contactField.getText().length() < 10) {
                        errorLabel.setText("Contact entered is invalid!");
                    } else if (sportsCodeBox.getSelectedItem().equals("---")) {
                        errorLabel.setText("Select a sports code!");

                    } else {
                        try {
                            Integer.parseInt(hourlyRateField.getText());
                            String[] coachDetails = {nameField.getText(), "null", String.valueOf(java.time.LocalDate.now()), "null",
                                    hourlyRateField.getText(),contactField.getText(), addressField.getText(),
                                    admin.getSportsCenterCode(), String.valueOf(sportsCodeBox.getSelectedItem()), "0","0"};
                            int check = admin.createCoach(coachDetails);
                            switch (check){
                                case 0: JOptionPane.showMessageDialog(frame, "Coach record successfully created!",
                                        "Successful", JOptionPane.INFORMATION_MESSAGE);
                                        frame.setVisible(false);
                                        new CreateRecordMenu(admin);
                                        break;
                                case 1: JOptionPane.showMessageDialog(frame, "Coach profile with the same name already exists!",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }

                        } catch (Exception a) {
                            errorLabel.setText("Enter a number for 'hourly rate'!");
                        }
                    }
                } else if (type.equals("sport")) {
                    String[] existingSports = FileServer.readFile(admin.getSportsCenterCode(), "Sports.txt");
                    boolean validSport = true;
                    for (String line : existingSports) {
                        String[] token = line.split("\\|");
                        if (token[0].toLowerCase().equals(nameField.getText().toLowerCase())) {
                            validSport = false;
                            break;
                        }
                    }
                    if(nameField.getText().equals("")){
                        errorLabel.setText("Please enter the sports' name!");
                    }
                    else if (validSport == true) {
                        admin.createSports(nameField.getText());
                        JOptionPane.showMessageDialog(frame, "Sport successfully created!",
                                "Successful", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        errorLabel.setText("Sport entered already exist!");
                    }
                }
            }
        });

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new CreateRecordMenu(admin);
            }
        });
    }
    public CreateRecord(UnregStudent student){
        frame = new JFrame("Create profile");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        errorLabel.setForeground(Color.red);
        frame.setVisible(true);
        headerLabel.setText("Enter your details below: ");
        setStudentVisibility();
        sportsCentreCodeLabel.setVisible(true);
        sportsCentreBox.setVisible(true);
        sportsBox.setEnabled(false);
        sportsCentreBox.addItem("---");
        sportsCentreBox.addItem("L001");
        sportsCentreBox.addItem("L002");
        sportsCentreBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(!(sportsCentreBox.getSelectedItem().equals("---"))){
                    sportsBox.setEnabled(true);
                    sportsBox.removeAllItems();
                    setComboBox(String.valueOf(sportsCentreBox.getSelectedItem()));
                }
                else{
                    sportsBox.removeAllItems();
                    sportsBox.setEnabled(false);
                }
            }
        });
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new StudentMenu(student);
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nameField.getText().equals("") || ageField.getText().equals("") || addressField.getText().equals("") ||
                        contactField.getText().equals("") || emailField.getText().equals("") ||
                        passwordField.getText().equals("")) {
                    errorLabel.setText("Enter details for all options above!");
                } else if (nameField.getText().length() < 8) {
                    errorLabel.setText("Name entered is too short! (min. 8 char)");
                } else if (contactField.getText().length() < 10) {
                    errorLabel.setText("Contact entered is invalid!");
                } else if (!(emailField.getText().contains("@"))) {
                    errorLabel.setText("Format of email entered is invalid!");
                } else if (sportsCentreBox.getSelectedItem().equals("---")) {
                    errorLabel.setText("Select a sports centre!");
                } else if (sportsBox.getSelectedItem().equals("---")) {
                    errorLabel.setText("Select a sports!");
                } else {
                    try {
                        Integer.parseInt(ageField.getText());
                        String[] studentDetail = {nameField.getText(), "null", ageField.getText(), addressField.getText(),
                                contactField.getText(), emailField.getText(), sportsBox.getSelectedItem() + "", String.valueOf(sportsCentreBox.getSelectedItem())};
                        int check = student.registerAccount(studentDetail, passwordField.getText());
                        switch (check) {
                            case 0:
                                JOptionPane.showMessageDialog(frame, "Account creation request made!\nAwait for admins' approval to access your account.",
                                        "Successful", JOptionPane.INFORMATION_MESSAGE);
                                frame.setVisible(false);
                                new StudentMenu(student);
                                break;
                            case 1:
                                errorLabel.setText("<html>Profile with the same name already created<br/>and awaiting for admins' approval!<html>");
                                break;
                            case 2:
                                errorLabel.setText("Profile with the same name already exists!");
                        }
                    } catch (Exception a) {
                        errorLabel.setText("Please enter a number for the age field!");
                    }
                }
            }
        });
    }

    public void setStudentVisibility() {
        hourlyRateField.setVisible(false);
        hourlyRateLabel.setVisible(false);
        sportsCodeBox.setVisible(false);
        sportsCodeLabel.setVisible(false);
        sportsCentreBox.setVisible(false);
        sportsCentreCodeLabel.setVisible(false);
    }

    public void setCoachVisibility(){
        ageField.setVisible(false);
        ageLabel.setVisible(false);
        emailField.setVisible(false);
        eMailLabel.setVisible(false);
        sportsLabel.setVisible(false);
        sportsBox.setVisible(false);
        passwordField.setVisible(false);
        passwordLabel.setVisible(false);
        sportsCentreBox.setVisible(false);
        sportsCentreCodeLabel.setVisible(false);
    }

    public void setComboBox(String sportsCentreCode){
        sportsBox.addItem("---");
        sportsCodeBox.addItem("---");
        String[] allSports = FileServer.readFile(sportsCentreCode,"Sports.txt");
        for (String line:allSports){
            String sport[] = line.split("\\|");
            sportsBox.addItem(sport[0]);
            sportsCodeBox.addItem(sport[1]);
        }

    }
}
