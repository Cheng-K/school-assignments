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
    private JButton saveCloseButton;
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
    private Student student;
    private Coach coach;
    private DisplayAllRecord parentFrame;
    private Admin admin;
    private setCoachPanel coachPanelManager;


    public AdminModifyMenu(Object received, Admin admin, DisplayAllRecord returnFrame) {
        this.admin = admin;
        parentFrame = returnFrame;
        try {
            if (received instanceof Coach) {
                coach = (Coach) received;
                coachPanelManager = new setCoachPanel();
            } else if (received instanceof Student) {
                student = (Student) received;
            }
            frame.setTitle("Modifying " + received.getClass().getName());
            frame.setContentPane(rootPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        } catch (ClassCastException e) {
            JOptionPane.showMessageDialog(frame, "Error, Did not received appropriate type object",
                    "Operation Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class setCoachPanel {
        public setCoachPanel() {
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
            saveCloseButton.addActionListener(new saveCloseButtonListener());
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
