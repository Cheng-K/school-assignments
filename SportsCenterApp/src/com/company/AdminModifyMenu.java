package com.company;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminModifyMenu {
    private JFrame frame = new JFrame();
    private JTextField coachIDField;
    private JTextField nameField;
    private JTextField dateJoinedField;
    private JTextField dateTerminatedField;
    private JTextField hourlyRateField;
    private JTextField contactNumberField;
    private JTextField addressField;
    private JTextField sportsCenterIDField;
    private JTextField ratingField;
    private JButton saveCloseButtonCoach;
    private JLabel coachIDLabel;
    private JLabel nameLabel;
    private JLabel dateJoinedLabel;
    private JLabel dateTerminatedLabel;
    private JLabel hourlyRateLabel;
    private JLabel contactLabel;
    private JLabel addressLabel;
    private JLabel ratingLabel;
    private JLabel sportCenterCodeLabel;
    private JLabel sportsLabel;
    private JPanel rootPanel;
    private JPanel modifyCoachTab;
    private JPanel modifySessionTab;
    private JTextField sessionIDField;
    private JTextField startTimeField;
    private JTextField endTimeField;
    private JTextField durationField;
    private JTextField coachNameField;
    private JTextField sportNameFieldSession;
    private JTextField dayField;
    private JLabel sessionIDLabel;
    private JLabel startTimeLabel;
    private JLabel endTimeLabel;
    private JLabel durationLabel;
    private JLabel coachNameLabel;
    private JLabel sportNameLabel;
    private JLabel dayLabel;
    private JTabbedPane tabbedPane1;
    private JButton saveCloseButtonSession;
    private JPanel modifySportsTab;
    private JTextField sportNameField;
    private JTextField sportIDField;
    private JTextField sportFeesField;
    private JButton saveCloseButtonSports;
    private JLabel sportFeesLabel;
    private JLabel sportIDLabel;
    private JLabel sportsNameLabel;
    private JTextField sportsCoachingField;
    private JTextField totalFeedbackField;
    private JPanel coachPanel;
    private Coach coach;
    private Session session;
    private Sports sports;
    private DisplayAllRecord parentFrame;
    private Admin admin;
    private SetCoachTab coachPanelManager;
    private SetSessionTab sessionPanelManager;
    private SetSportsTab sportsPanelManager;


    public AdminModifyMenu(Object received, Admin admin, DisplayAllRecord returnFrame) {
        this.admin = admin;
        parentFrame = returnFrame;
        try {
            if (received instanceof Coach) {
                coach = (Coach) received;
                coachPanelManager = new SetCoachTab();
                frame.setContentPane(modifyCoachTab);
            } else if (received instanceof Sports) {
                sports = (Sports) received;
                sportsPanelManager = new SetSportsTab();
                frame.setContentPane(modifySportsTab);
            } else if (received instanceof Session){
                session = (Session) received;
                sessionPanelManager = new SetSessionTab();
                frame.setContentPane(modifySessionTab);
            }
            frame.setTitle("Modifying " + received.getClass().getSimpleName());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        } catch (ClassCastException e) {
            JOptionPane.showMessageDialog(frame, "Error, Did not received appropriate type object",
                    "Operation Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class SetCoachTab {
        public SetCoachTab() {
            coachIDField.setText(coach.getCoachID());
            nameField.setText(coach.getName());
            dateJoinedField.setText(coach.getDateJoined().toString());
            try {
                dateTerminatedField.setText(coach.getDateTerminated().toString());
            } catch (Exception e) {
                dateTerminatedField.setText("null");
            }
            hourlyRateField.setText(Integer.toString(coach.getHourlyRate()));
            contactNumberField.setText(coach.getPhone());
            addressField.setText(coach.getAddress());
            sportsCenterIDField.setText(coach.getSportsCenterID());
            sportsCoachingField.setText(coach.getSportsCode());
            ratingField.setText(Integer.toString(coach.getRating()/coach.getTotalRates()));
            totalFeedbackField.setText(Integer.toString(coach.getTotalRates()));
            saveCloseButtonCoach.addActionListener(new saveCloseButtonCoachListener());
        }

        public List<String> getEnteredCoachDetails() {
            List<String> returnList = new ArrayList<>();
            returnList.add(coachIDField.getText());
            returnList.add(nameField.getText());
            returnList.add(dateJoinedField.getText());
            returnList.add(dateTerminatedField.getText());
            returnList.add(hourlyRateField.getText());
            returnList.add(contactNumberField.getText());
            returnList.add(addressField.getText());
            returnList.add(sportsCenterIDField.getText());
            return returnList;
        }

        private int verifyCoachDetails (List<String> coachDetails, FormChecker validator){
            int returnNum = 0;
            // Check for each length
            for (int index = 0; index<coachDetails.size(); index++){
                if (coachDetails.get(index).length() >= 100){
                    setCoachBorderRed(index,"Value is too unrealistic large/long");
                    returnNum = 1;
                }
                else {
                    switch (index){
                        case 0 :
                            break;
                        case 1:
                            break;
                        case 2:
                            if (!validator.isDateObject(coachDetails.get(2))) {
                                setCoachBorderRed(index, "Date format should be YYYY-MM-DD");
                                returnNum = 1;
                            }
                            break;
                        case 3:
                            if (!validator.isDateObject(coachDetails.get(3)) && !coachDetails.get(3).equals("null")) {
                                setCoachBorderRed(index, "Date format should be YYYY-MM-DD");
                                returnNum = 1;
                            }
                            break;
                        case 4:
                            if (!validator.isIntegerObject(coachDetails.get(4))) {
                                setCoachBorderRed(index, "Invalid integer provided");
                                returnNum = 1;
                            }
                            break;
                        case 5 :
                            if (!validator.onlyDigits(coachDetails.get(5))) {
                                setCoachBorderRed(index, "Invalid contact number, only digits are allowed");
                                returnNum = 1;
                            }
                            break;
                        case 6 :
                            break;
                        case 7:
                            break;
                        case 8 :
                            break;
                        case 9 :
                            break;
                    }
                }
            }
            return returnNum;
        }
        private void setCoachBorderRed(int index, String message) {
            switch (index) {
                case 0:
                    coachIDField.setBorder(new LineBorder(Color.RED, 2));
                    coachIDField.setToolTipText(message);
                    break;
                case 1:
                    break;
                case 2:
                    dateJoinedField.setBorder(new LineBorder(Color.RED, 2));
                    dateJoinedField.setToolTipText(message);
                    break;
                case 3:
                    dateTerminatedField.setBorder(new LineBorder(Color.RED, 2));
                    dateTerminatedField.setToolTipText(message);
                    break;
                case 4:
                    hourlyRateField.setBorder(new LineBorder(Color.RED, 2));
                    hourlyRateField.setToolTipText(message);
                    break;
                case 5:
                    contactNumberField.setBorder(new LineBorder(Color.RED, 2));
                    contactNumberField.setToolTipText(message);
                    break;
                case 6:
                    addressField.setBorder(new LineBorder(Color.RED, 2));
                    addressField.setToolTipText(message);
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    ratingField.setBorder(new LineBorder(Color.RED, 2));
                    ratingField.setToolTipText(message);
                    break;
            }

        }
        private void setBorderBlack() {
            dateJoinedField.setBorder(new LineBorder(Color.BLACK,1));
            dateJoinedField.setToolTipText("");
            dateTerminatedField.setBorder(new LineBorder(Color.BLACK,1));
            dateTerminatedField.setToolTipText("");
            hourlyRateField.setBorder(new LineBorder(Color.BLACK,1));
            hourlyRateField.setToolTipText("");
            contactNumberField.setBorder(new LineBorder(Color.BLACK,1));
            contactNumberField.setToolTipText("");
            addressField.setBorder(new LineBorder(Color.BLACK,1));
            addressField.setToolTipText("");
        }
    }

    private class SetSessionTab {
        public SetSessionTab(){
            dayField.setText(session.getDay());
            sessionIDField.setText(session.getSessionID());
            startTimeField.setText(session.getStartTime().toString());
            endTimeField.setText(session.getEndTime().toString());
            durationField.setText(Double.toString((session.getDuration().toMinutes()/60.0)) + "hours");
            coachNameField.setText(session.getCoachName());
            sportNameFieldSession.setText(session.getSportName());
            saveCloseButtonSession.addActionListener(new saveCloseButtonSessionListener());
        }
        private List<String> getEnteredDetails () {
            List<String> returnList = new ArrayList<>();
            returnList.add(dayField.getText());
            returnList.add(sessionIDField.getText());
            returnList.add(startTimeField.getText());
            returnList.add(endTimeField.getText());
            returnList.add(durationField.getText());
            returnList.add(coachNameField.getText());
            returnList.add(sportNameFieldSession.getText());
            return returnList;
        }

        private int verifySessionDetails (List<String> sessionDetails, FormChecker validator){
            int returnNum = 0;
            if (!validator.isDay(sessionDetails.get(0))){
                setSessionBorderRed(0,"Invalid day provided.");
                returnNum = 1;
            }
            if (!validator.isTime(sessionDetails.get(2))){
                setSessionBorderRed(2,"Please provide a valid time in HH:MM 24 hour format");
                returnNum = 1;
            }
            if (!validator.isTime(sessionDetails.get(3))){
                setSessionBorderRed(3,"Please provide a valid time in HH:MM 24 hour format");
                returnNum = 1;
            }
            if (validator.isLogicalDuration(sessionDetails.get(2),sessionDetails.get(3)) == 1){
                setSessionBorderRed(4,"Please provide a valid time with start time earlier than end time.");
                returnNum = 1;
            }

            return returnNum;
        }

        private void setSessionBorderRed(int index,String message){
            switch(index){
                case 0 :
                    dayField.setBorder(new LineBorder(Color.RED,2));
                    dayField.setToolTipText(message);
                    break;
                case 1 :
                    sessionIDField.setBorder(new LineBorder(Color.RED,2));
                    sessionIDField.setToolTipText(message);
                    break;
                case 2:
                    startTimeField.setBorder(new LineBorder(Color.RED,2));
                    startTimeField.setToolTipText(message);
                    break;
                case 3:
                    endTimeField.setBorder(new LineBorder(Color.RED,2));
                    endTimeField.setToolTipText(message);
                    break;
                case 4:
                    durationField.setBorder(new LineBorder(Color.RED,2));
                    durationField.setToolTipText(message);
                    break;
                case 5:
                    coachNameField.setBorder(new LineBorder(Color.RED,2));
                    coachNameField.setToolTipText(message);
                    break;
                case 6:
                    sportNameFieldSession.setBorder(new LineBorder(Color.RED,2));
                    sportNameFieldSession.setToolTipText(message);
                    break;
                default :
                    break;
            }
        }
        private void setBorderBlack() {
            dayField.setBorder(new LineBorder(Color.BLACK,1));
            dayField.setToolTipText("");
            startTimeField.setBorder(new LineBorder(Color.BLACK,1));
            startTimeField.setToolTipText("");
            endTimeField.setBorder(new LineBorder(Color.BLACK,1));
            endTimeField.setToolTipText("");

        }

    }

    private class SetSportsTab {
        public SetSportsTab (){
            sportIDField.setText(sports.getSportsID());
            sportNameField.setText(sports.getName());
            sportFeesField.setText(Integer.toString(sports.getSportFees()));
            saveCloseButtonSports.addActionListener(new saveCloseButtonSportsListener());
        }
        private int verifySportsDetails (String newSportFees,FormChecker validator){
            if (validator.isIntegerObject(newSportFees)) {
                return 0;
            }
            else {
                setSportsBorderRed(2,"Please provide a proper integer value");
                return 1;
            }
        }
        private void setSportsBorderRed (int index,String message){
            switch (index) {
                case 0:
                    sportIDField.setBorder(new LineBorder(Color.RED, 2));
                    sportIDField.setToolTipText(message);
                case 1:
                    sportNameFieldSession.setBorder(new LineBorder(Color.RED,2));
                    sportNameFieldSession.setToolTipText(message);
                case 2 :
                    sportFeesField.setBorder(new LineBorder(Color.RED,2));
                    sportFeesField.setToolTipText(message);
            }
        }
        private void setSportsBorderBlack() {
            sportFeesField.setBorder(new LineBorder(Color.BLACK,1));
            sportFeesField.setToolTipText("");
        }

    }

    private class saveCloseButtonCoachListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            coachPanelManager.setBorderBlack();
            if (coachPanelManager.verifyCoachDetails(coachPanelManager.getEnteredCoachDetails(),new FormChecker()) == 1) {
                JOptionPane.showMessageDialog(frame, "Invalid value/format for red coloured border fields. Please try again");
            } else {
                if (admin.modCoach(coachPanelManager.getEnteredCoachDetails(), coach) == 1)
                    JOptionPane.showMessageDialog(frame,"Cannot modify record. Contact technical assistance. Please try again!","Error",JOptionPane.ERROR_MESSAGE);
                else {
                    parentFrame.coachPanelManager.clearUpdateTable();
                    parentFrame.frame.setVisible(true);
                    frame.dispose();
                }
            }
        }
    }
    
    private class saveCloseButtonSessionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            sessionPanelManager.setBorderBlack();
            if (sessionPanelManager.verifySessionDetails(sessionPanelManager.getEnteredDetails(),new FormChecker()) == 1) {
                JOptionPane.showMessageDialog(frame, "Invalid value/format for red coloured border fields. Please try again");
            } else {
                if (admin.modSession(sessionPanelManager.getEnteredDetails(), session) == 1){
                    JOptionPane.showMessageDialog(frame,"Cannot modify record. Contact technical assistance","Error",JOptionPane.ERROR_MESSAGE);
                }
                else {
                    parentFrame.schedulePanelManager.clearUpdateTable();
                    parentFrame.frame.setVisible(true);
                    frame.dispose();
                }
            }
        }
    }
    private class saveCloseButtonSportsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            sportsPanelManager.setSportsBorderBlack();
            if (sportsPanelManager.verifySportsDetails(sportFeesField.getText(),new FormChecker()) == 1) {
                JOptionPane.showMessageDialog(frame, "Invalid value/format for red coloured border fields. Please try again");
            } else {
                if (admin.modSports(Integer.parseInt(sportFeesField.getText()), sports) == 1)
                    JOptionPane.showMessageDialog(frame,"Cannot modify record. Contact technical assistance","Error",JOptionPane.ERROR_MESSAGE);
                else {
                    parentFrame.sportsPanelManager.clearUpdateTable();
                    parentFrame.frame.setVisible(true);
                    frame.dispose();
                }
            }
        }
    }

}

class FormChecker{
    // Not sure where to put it
    public boolean onlyDigits (String str){
        for (int index = 0; index < str.length(); index ++){
            if (!Character.isDigit(str.charAt(index)))
                return false;
        }
        return true;
    }

    public boolean isDateObject (String str){
        try {
            LocalDate.parse(str);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean isIntegerObject (String str){
        try{
            Integer.parseInt(str);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean isDay (String str){
        ArrayList<String> listOfDays = new ArrayList<String> (Arrays.asList(
                "monday","tuesday","wednesday","thursday","friday","saturday","sunday"));
        return listOfDays.contains(str.toLowerCase());
    }

    public boolean isTime (String str){
        if (str.length() >5)
            return false;
        String[] tokens = str.split(":");
        if (tokens.length == 2){
            try {
                LocalTime.of(Integer.parseInt(tokens[0]),Integer.parseInt(tokens[1]));
                return true;
            } catch (Exception e){}
        }
        return false;
    }

    public int isLogicalDuration (String startStr, String endStr){
        String[] startTokens = startStr.split(":");
        String[] endTokens = endStr.split(":");
        try {
            LocalTime startTime = LocalTime.of(Integer.parseInt(startTokens[0]),Integer.parseInt(startTokens[1]));
            LocalTime endTime = LocalTime.of(Integer.parseInt(endTokens[0]),Integer.parseInt(endTokens[1]));
            if (startTime.isBefore(endTime))
                return 0;
            else
                return 1;
        } catch(Exception e) {
            return 2;
        }
    }
}