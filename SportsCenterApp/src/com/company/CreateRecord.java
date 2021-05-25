package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.Duration;
import java.time.LocalTime;

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
    private JComboBox coachBox;
    private JLabel coachLabel;
    private JLabel dayLabel;
    private JComboBox dayBox;
    private JComboBox startTimeBox;
    private JComboBox durationBox;
    private JLabel startTimeLabel;
    private JLabel durationLabel;

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
            coachBox.setEnabled(false);
            sportsBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (!(sportsBox.getSelectedItem().equals("---"))) {
                        coachBox.setEnabled(true);
                        coachBox.removeAllItems();
                        setCoachBox(admin.getSportsCenterCode(),String.valueOf(sportsBox.getSelectedItem()));
                    } else {
                        coachBox.removeAllItems();
                        coachBox.setEnabled(false);
                    }
                }
            });
        } else if (type.equals("coach")) {
            setCoachVisibility();
            sportsBox.setEnabled(false);
            sportsCodeBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    sportsBox.setSelectedIndex(sportsCodeBox.getSelectedIndex());
                }
            });
        } else if (type.equals("sport")) {
            setStudentVisibility();
            setCoachVisibility();
            addressField.setVisible(false);
            addressLabel.setVisible(false);
            contactField.setVisible(false);
            contactLabel.setVisible(false);
            sportsLabel.setVisible(false);
            sportsBox.setVisible(false);
        }else if (type.equals("session")) {
            setSessionVisibility();
            sportsBox.setEnabled(false);
            coachBox.setEnabled(false);
            setTimeBox();
            sportsCodeBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    sportsBox.setSelectedIndex(sportsCodeBox.getSelectedIndex());
                }
            });
            sportsBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (!(sportsBox.getSelectedItem().equals("---"))) {
                        coachBox.setEnabled(true);
                        coachBox.removeAllItems();
                        setCoachBox(admin.getSportsCenterCode(),String.valueOf(sportsBox.getSelectedItem()));
                    } else {
                        coachBox.removeAllItems();
                        coachBox.setEnabled(false);
                    }
                }
            });
        }
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (type.equals("student")) {
                    String[] studentDetail = {nameField.getText(), "null", ageField.getText(), addressField.getText(), contactField.getText(),
                            emailField.getText(), String.valueOf(sportsBox.getSelectedItem()), admin.getSportsCenterCode(),
                            String.valueOf(coachBox.getSelectedItem()),"false"};
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
                    } else if (coachBox.getSelectedItem().equals("---")) {
                        errorLabel.setText("Please select a coach!");
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
                                    hourlyRateField.getText(), contactField.getText(), addressField.getText(),
                                    admin.getSportsCenterCode(), String.valueOf(sportsCodeBox.getSelectedItem()), "0", "0"};
                            int check = admin.createCoach(coachDetails);
                            switch (check) {
                                case 0:
                                    JOptionPane.showMessageDialog(frame, "Coach record successfully created!",
                                            "Successful", JOptionPane.INFORMATION_MESSAGE);
                                    frame.setVisible(false);
                                    new CreateRecordMenu(admin);
                                    break;
                                case 1:
                                    JOptionPane.showMessageDialog(frame, "Coach profile with the same name already exists!",
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
                    if (nameField.getText().equals("")) {
                        errorLabel.setText("Please enter the sports' name!");
                    } else if (validSport == true) {
                        admin.createSports(nameField.getText());
                        JOptionPane.showMessageDialog(frame, "Sport successfully created!",
                                "Successful", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        errorLabel.setText("Sport entered already exist!");
                    }
                } else if(type.equals("session")){
                    if(dayBox.getSelectedIndex()==0||sportsCodeBox.getSelectedIndex()==1||coachBox.getSelectedIndex()==0||
                    startTimeBox.getSelectedIndex()==0||durationBox.getSelectedIndex()==0){
                        errorLabel.setText("Select an option for all details above!");
                    }
                    else{
                        String[] time = String.valueOf(startTimeBox.getSelectedItem()).split(":");
                        int startHour = Integer.parseInt(time[0]);
                        int startMinute = 0;
                        if(time.length==2){
                            startMinute = Integer.parseInt(time[1]);
                        }
                        LocalTime startTime = LocalTime.of(startHour,startMinute);
                        LocalTime endTime=LocalTime.of(0,0);
                        if (durationBox.getSelectedIndex()==1){
                            endTime=startTime.plusHours(1);
                        }else if(durationBox.getSelectedIndex()==2){
                            endTime=startTime.plusHours(1);
                            endTime=startTime.plusMinutes(30);
                        }else if(durationBox.getSelectedIndex()==3){
                            endTime=startTime.plusHours(2);
                        }else if(durationBox.getSelectedIndex()==4){
                            endTime=startTime.plusHours(2);
                            endTime=startTime.plusMinutes(30);
                        }else if(durationBox.getSelectedIndex()==5){
                            endTime=startTime.plusHours(3);
                        }
                        String[] sessionDetail = {String.valueOf(dayBox.getSelectedItem()),"null",String.valueOf(startHour),
                                String.valueOf(startMinute),String.valueOf(endTime.getHour()),String.valueOf(endTime.getMinute()),
                                String.valueOf(sportsBox.getSelectedItem()),String.valueOf(coachBox.getSelectedItem())};
                        admin.createSession(sessionDetail);
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

    public CreateRecord(UnregStudent student) {
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
        sportsBox.addItem("---");
        sportsCentreBox.addItem("L001");
        sportsCentreBox.addItem("L002");
        coachBox.setEnabled(false);
        sportsCentreBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!(sportsCentreBox.getSelectedIndex()==0)) {
                    sportsBox.setEnabled(true);
                    sportsBox.removeAllItems();
                    setComboBox(String.valueOf(sportsCentreBox.getSelectedItem()));
                } else {
                    coachBox.setEnabled(false);
                    coachBox.removeAllItems();
                    sportsBox.setEnabled(false);
                    sportsBox.setSelectedIndex(0);
                }
            }
        });
        sportsBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(String.valueOf(sportsBox.getSelectedItem()).equals("---"))) {
                    coachBox.removeAllItems();
                    setCoachBox(String.valueOf(sportsCentreBox.getSelectedItem()), String.valueOf(sportsBox.getSelectedItem()));
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
                } else if (coachBox.getSelectedItem().equals("---")) {
                    errorLabel.setText("Select a coach!");
                } else {
                    try {
                        Integer.parseInt(ageField.getText());
                        String[] studentDetail = {nameField.getText(), "null", ageField.getText(), addressField.getText(),
                                contactField.getText(), emailField.getText(), String.valueOf(sportsBox.getSelectedItem()) ,
                                String.valueOf(sportsCentreBox.getSelectedItem()), String.valueOf(coachBox.getSelectedItem()), "false"};
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
        dayBox.setVisible(false);
        dayLabel.setVisible(false);
        startTimeLabel.setVisible(false);
        startTimeBox.setVisible(false);
        durationBox.setVisible(false);
        durationLabel.setVisible(false);
    }

    public void setCoachVisibility() {
        ageField.setVisible(false);
        ageLabel.setVisible(false);
        emailField.setVisible(false);
        eMailLabel.setVisible(false);
        passwordField.setVisible(false);
        passwordLabel.setVisible(false);
        sportsCentreBox.setVisible(false);
        sportsCentreCodeLabel.setVisible(false);
        coachBox.setVisible(false);
        coachLabel.setVisible(false);
        dayBox.setVisible(false);
        dayLabel.setVisible(false);
        startTimeLabel.setVisible(false);
        startTimeBox.setVisible(false);
        durationBox.setVisible(false);
        durationLabel.setVisible(false);
    }

    public void setSessionVisibility(){
        nameField.setVisible(false);
        nameLabel.setVisible(false);
        addressLabel.setVisible(false);
        addressField.setVisible(false);
        ageLabel.setVisible(false);
        ageField.setVisible(false);
        contactLabel.setVisible(false);
        contactField.setVisible(false);
        eMailLabel.setVisible(false);
        emailField.setVisible(false);
        sportsCentreBox.setVisible(false);
        sportsCentreCodeLabel.setVisible(false);
        passwordLabel.setVisible(false);
        passwordField.setVisible(false);
        hourlyRateLabel.setVisible(false);
        hourlyRateField.setVisible(false);
        dayBox.addItem("---");
        dayBox.addItem("Sunday");
        dayBox.addItem("Monday");
        dayBox.addItem("Tuesday");
        dayBox.addItem("Wednesday");
        dayBox.addItem("Thursday");
        dayBox.addItem("Friday");
        dayBox.addItem("Saturday");
    }
    public void setComboBox(String sportsCentreCode) {
        coachBox.removeAllItems();
        coachBox.setEnabled(true);
        sportsBox.addItem("---");
        sportsCodeBox.addItem("---");
        coachBox.addItem("---");
        String[] allSports = FileServer.readFile(sportsCentreCode, "Sports.txt");
        for (String line : allSports) {
            String sport[] = line.split("\\|");
            sportsBox.addItem(sport[0]);
            sportsCodeBox.addItem(sport[1]);
        }
    }

    public void setCoachBox(String sportsCentreCode, String sportsName) {
        coachBox.addItem("---");
        String selectedSport = "";
        String[] sports = FileServer.readFile(sportsCentreCode, "Sports.txt");
        for (String line : sports) {
            String sportDetails[] = line.split("\\|");
            if (sportDetails[0].equals(sportsName)){
                selectedSport = sportDetails[1];
                break;
            }
        }
        String[] allCoach = FileServer.readFile(sportsCentreCode, "Coach.txt");
        for (String line : allCoach) {
            String coach[] = line.split("\\|");
            if (coach[8].equals(selectedSport)){
                coachBox.addItem(coach[0]);
            }
        }
    }
    public void setTimeBox() {
        String[] timeSlot = {"---","8:00", "8:30", "9:00", "9:30", "10:00", "10:30","11:00","11:30","12:00","12:30","13:00","13:30",
        "14:00","14:30","15:00","15:30","16:00","16:30","17:00"};
        for (String time : timeSlot) {
            startTimeBox.addItem(time);
        }
        String[] durationSlot = {"---","1 hour","1 hour30minutes","2 hours","2 hours30minutes","3 hours"};
        for(String duration:durationSlot){
            durationBox.addItem(duration);
        }
    }
}
