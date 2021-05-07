package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentProfile {
    private JFrame frame;
    private JPanel studentProfilePanel;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JTextField studentIDField;
    private JTextField ageField;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField sportsEnrolledField;
    private JTextField sportsCenterField;
    private JButton backToMenuButton;
    private JButton modifyDetailsButton;
    private JLabel nameLabel;
    private JLabel passwordLabel;
    private JLabel studentIDLabel;
    private JLabel ageLabel;
    private JLabel addressLabel;
    private JLabel phoneLabel;
    private JLabel emailLabel;
    private JLabel sportsEnrolledLabel;
    private JLabel sportsCenterLabel;
    private JLabel Heading;
    private JButton saveDetailsButton;
    private JPanel saveDetailsPanel;
    private JPanel modifyDetailsPanel;
    private Student student;

    private class modifyButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            enableEdit();
            frame.setTitle("Modify Personal Details");
            modifyDetailsPanel.setVisible(false);
            saveDetailsPanel.setVisible(true);
        }
    }

    private class saveDetailsButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            //* code here*//
        }
    }

    public StudentProfile (Student currStudent){
        student = currStudent;
        setAllField();
        setAllFieldDisabled();
        modifyDetailsButton.addActionListener(new modifyButtonListener());
        frame = new JFrame("Personal Profile");
        frame.setContentPane(studentProfilePanel);
        saveDetailsPanel.setVisible(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    private void setAllField (){
        nameField.setText(student.getName());
        //passwordField.setText()
        studentIDField.setText(student.getStudentID());
        ageField.setText(Integer.toString(student.getAge()));
        addressField.setText(student.getAddress());
        phoneField.setText(student.getContactNumber());
        emailField.setText(student.getEmail());
        //sportsEnrolledField.setText(student.getRegisteredSports());
        sportsCenterField.setText(student.getSportsCenterCode());
    }

    private void setAllFieldDisabled() {
        nameField.setEditable(false);
        passwordField.setEditable(false);
        studentIDField.setEditable(false);
        ageField.setEditable(false);
        addressField.setEditable(false);
        phoneField.setEditable(false);
        emailField.setEditable(false);
        sportsEnrolledField.setEditable(false);
        sportsCenterField.setEditable(false);
    }

    private void enableEdit() {
        passwordField.setEditable(true);
        ageField.setEditable(true);
        addressField.setEditable(true);
        phoneField.setEditable(true);
        emailField.setEditable(true);
        setTooltipText();
    }

    private void setTooltipText() {
        nameField.setToolTipText("Only admin has rights to change this field");
        studentIDField.setToolTipText("Only admin has rights to change this field");
        sportsEnrolledField.setToolTipText("Only admin has rights to change this field");
        sportsCenterField.setToolTipText("Only admin has rights to change this field");
    }


}
