package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

public class CreateRecord {
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
    private JLabel hourlyRateLabel;
    private JTextField hourlyRateField;
    private JLabel sportsCodeLabel;
    private JTextField sportsCodeField;
    private JLabel headerLabel;
    private JLabel sportsCentreCodeLabel;
    private JTextField sportsCentreCodeField;


    public CreateRecord(Admin admin, String type) {
        frame = new JFrame("Create " + type + " profile");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        errorLabel.setForeground(Color.red);
        frame.setVisible(true);
        headerLabel.setText("Enter " + type + " details below: ");
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
                            emailField.getText(), (sportsField.getText()), admin.getSportsCenterCode()};
                    for (String item : studentDetail) {
                        if (item.equals("")) {
                            errorLabel.setText("Enter details for all options above!");
                        }
                    }
                    String[] existingSports = FileServer.readFile(admin.getSportsCenterCode(), "Sports.txt");
                    boolean validSport = false;
                    for (String line : existingSports) {
                        String[] token = line.split("\\|");
                        if (token[0].toLowerCase().equals(sportsField.getText().toLowerCase())) {
                            validSport = true;
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
                    } else if (validSport == false) {
                        errorLabel.setText("Sports entered does not exists!");
                    } else {
                        try {
                            Integer.parseInt(studentDetail[2]);
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
                    String[] fileContent = FileServer.readFile(admin.getSportsCenterCode(), "Sports.txt");
                    boolean validSport = false;
                    for (String line : fileContent) {
                        String[] token = line.split("\\|");
                        if (token[1].equals(sportsCodeField.getText())) {
                            validSport = true;
                            break;
                        }
                    }
                    if (nameField.getText().equals("") || addressField.getText().equals("") ||
                            contactField.getText().equals("") || sportsCodeField.equals("")) {
                        errorLabel.setText("Enter details for all options above!");
                    } else if (nameField.getText().length() < 8) {
                        errorLabel.setText("Name entered is too short! (min. 8 char)");
                    } else if (contactField.getText().length() < 10) {
                        errorLabel.setText("Contact entered is invalid!");
                    } else if (validSport == false) {
                        errorLabel.setText("Sports code entered does not exist!");
                    } else {
                        try {
                            Integer.parseInt(hourlyRateField.getText());
                            System.out.println("1");
                            String[] coachDetails = {nameField.getText(), "null", java.time.LocalDate.now() + "", "null",
                                    hourlyRateField.getText(), contactField.getText(), addressField.getText(),
                                    admin.getSportsCenterCode(), sportsCodeField.getText(), "0"};
                            admin.createCoach(coachDetails);
                            System.out.println("2");
                            JOptionPane.showMessageDialog(frame, "Coach record successfully created!",
                                    "Successful", JOptionPane.INFORMATION_MESSAGE);
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
        sportsCentreCodeField.setVisible(true);
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
                        contactField.getText().equals("") || emailField.getText().equals("") || sportsCodeField.equals("") ||
                        sportsCentreCodeField.getText().equals("") || passwordField.getText().equals("")) {
                    errorLabel.setText("Enter details for all options above!");
                } else if (nameField.getText().length() < 8) {
                    errorLabel.setText("Name entered is too short! (min. 8 char)");
                } else if (contactField.getText().length() < 10) {
                    errorLabel.setText("Contact entered is invalid!");
                } else if (!(emailField.getText().contains("@"))) {
                    errorLabel.setText("Format of email entered is invalid!");
                } else if (!(sportsCentreCodeField.getText().equals("L001") || sportsCentreCodeField.getText().equals("L002"))) {
                    errorLabel.setText("Invalid sports centre code! (L001/L002)");
                }else{
                    String[] fileContent = FileServer.readFile(sportsCentreCodeField.getText(),"Sports.txt");
                    boolean validSports=false;
                    for (String line:fileContent){
                        if(line.toLowerCase().contains(sportsField.getText().toLowerCase())){
                            validSports=true;
                            break;
                        } }
                    if(validSports==true){
                        try{
                            Integer.parseInt(ageField.getText());
                            String[] studentDetail = {nameField.getText(),"null",ageField.getText(),addressField.getText(),
                                    contactField.getText(),emailField.getText(),sportsField.getText(),sportsCentreCodeField.getText()};
                            int check = student.registerAccount(studentDetail,passwordField.getText());
                            switch (check){
                                case 0: JOptionPane.showMessageDialog(frame, "Account creation request made!\nAwait for admins' approval to access your account.",
                                        "Successful", JOptionPane.INFORMATION_MESSAGE);
                                frame.setVisible(false);
                                new StudentMenu(student);
                                break;
                                case 1: errorLabel.setText("<html>Profile with the same name already created<br/>and awaiting for admins' approval!<html>");
                                break;
                                case 2: errorLabel.setText("Profile with the same name already exists!");
                            }
                        }catch (Exception a){errorLabel.setText("Please enter a number for the age field!");}                }
                    else{
                        errorLabel.setText("Sport entered does not exists!");
                    }
                }
            }
        });
    }

    public void setStudentVisibility() {
        hourlyRateField.setVisible(false);
        hourlyRateLabel.setVisible(false);
        sportsCodeField.setVisible(false);
        sportsCodeLabel.setVisible(false);
        sportsCentreCodeField.setVisible(false);
        sportsCentreCodeLabel.setVisible(false);
    }

    public void setCoachVisibility(){
        ageField.setVisible(false);
        ageLabel.setVisible(false);
        emailField.setVisible(false);
        eMailLabel.setVisible(false);
        sportsField.setVisible(false);
        sportsLabel.setVisible(false);
        passwordField.setVisible(false);
        passwordLabel.setVisible(false);
        sportsCentreCodeField.setVisible(false);
        sportsCentreCodeLabel.setVisible(false);
    }
}
