package com.company;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AdminModifyMenu {
    private JFrame frame = new JFrame();
    private JComboBox sportsBox;
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
    private JTextField sportNameField;
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
    private JTextField sportsNameField;
    private JTextField sportIDField;
    private JTextField sportFeesField;
    private JButton saveCloseButtonSports;
    private JLabel sportFeesLabel;
    private JLabel sportIDLabel;
    private JLabel sportsNameLabel;
    private JPanel coachPanel;
    private Student student;
    private Coach coach;
    private Session session;
    private DisplayAllRecord parentFrame;
    private Admin admin;
    private setCoachTab coachPanelManager;
    private SetSessionTab sessionPanelManger;


    public AdminModifyMenu(Object received, Admin admin, DisplayAllRecord returnFrame) {
        this.admin = admin;
        parentFrame = returnFrame;
        try {
            if (received instanceof Coach) {
                coach = (Coach) received;
                coachPanelManager = new setCoachTab();
                frame.setContentPane(coachPanel);
            } else if (received instanceof Student) {
                student = (Student) received;
            } else if (received instanceof Session){
                session = (Session) received;
                sessionPanelManger = new SetSessionTab();
                frame.setContentPane(modifySessionTab);
            }
            frame.setTitle("Modifying " + received.getClass().getName());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        } catch (ClassCastException e) {
            JOptionPane.showMessageDialog(frame, "Error, Did not received appropriate type object",
                    "Operation Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class setCoachTab {
        public setCoachTab() {
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
            ratingField.setText(Float.toString(coach.getRating()));
            saveCloseButtonCoach.addActionListener(new saveCloseButtonListener());
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
    }

    private class SetSessionTab {
        public SetSessionTab(){
            dayField.setText(session.getDay());
            sessionIDField.setText(session.getSessionID());
            startTimeField.setText(session.getStartTime().toString());
            endTimeField.setText(session.getEndTime().toString());
            durationField.setText(Long.toString(session.getDuration().toHours()));
            coachNameField.setText(session.getCoachName());
            sportNameField.setText(session.getSportName());
        }
    }

    private class saveCloseButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (admin.verifyCoachDetails(AdminModifyMenu.this, coachPanelManager.getEnteredCoachDetails()) == 1) {
                JOptionPane.showMessageDialog(frame, "Invalid value/format for red coloured border fields. Please try again");
            } else {
                admin.modCoach(coachPanelManager.getEnteredCoachDetails(), coach);
                parentFrame.coachPanelManager.clearUpdateTable();
                parentFrame.frame.setVisible(true);
                frame.dispose();
            }
        }
    }

    public void setBorderRed(int index, String message) {
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

}
