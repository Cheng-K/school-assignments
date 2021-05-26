package com.company.GUI;

import com.company.Coach;
import com.company.Student;

import javax.swing.*;
import java.awt.event.*;

public class CoachProfile {
    private JFrame frame;
    private JPanel mainPanel;
    private JLabel headerLabel;
    private JButton backToMenuButton;
    private JLabel nameLabel;
    private JTextField nameField;
    private JTextField IDField;
    private JLabel IDLabel;
    private JTextField contactField;
    private JLabel contactLabel;
    private JTextField sportsField;
    private JTextField ratingField;
    private JLabel sportsLabel;
    private JLabel ratingLabel;
    private JLabel feedbackLabel;
    private JButton yesButton;
    private JButton noButton;
    private JPanel notRated;
    private JRadioButton a1RadioButton;
    private JRadioButton a2RadioButton;
    private JRadioButton a3RadioButton;
    private JRadioButton a4RadioButton;
    private JRadioButton a5RadioButton;
    private JButton submitRatingButton;
    private JButton cancelButton;
    private JLabel spacing;
    private ButtonGroup radioButtonGroup = new ButtonGroup();

    public CoachProfile(Student student) {
        frame = new JFrame("Coach details");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        radioButtonGroup.add(a1RadioButton);
        radioButtonGroup.add(a2RadioButton);
        radioButtonGroup.add(a3RadioButton);
        radioButtonGroup.add(a4RadioButton);
        radioButtonGroup.add(a5RadioButton);
        Coach coach = student.getCoach();
        backToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new StudentMenu(student);
            }
        });
        if (student.getGivenRating()==false){
            hideRadioButtons();
            submitRatingButton.setVisible(false);
            cancelButton.setVisible(false);
            noButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    notRated.setVisible(false);
                    feedbackLabel.setVisible(false);
                    spacing.setVisible(false);
                }
            });
            yesButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showRadioButtons();
                    submitRatingButton.setVisible(true);
                    cancelButton.setVisible(true);
                    yesButton.setVisible(false);
                    noButton.setVisible(false);
                    feedbackLabel.setText("Select rating for the coach performance");
                }
            });
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    hideRadioButtons();
                    cancelButton.setVisible(false);
                    submitRatingButton.setVisible(false);
                    yesButton.setVisible(true);
                    noButton.setVisible(true);
                    feedbackLabel.setText("Rating for this coach has not been given, give rating now?");
                }
            });
            submitRatingButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (radioButtonGroup.getSelection()==null){
                        feedbackLabel.setText("Please select a rating from 1 to 5 below!");}
                    else{
                        if(a1RadioButton.isSelected()==true){
                            student.giveRating(1);
                        }else if(a2RadioButton.isSelected()==true){
                            student.giveRating(2);
                        }else if(a3RadioButton.isSelected()==true){
                            student.giveRating(3);
                        }else if(a4RadioButton.isSelected()==true){
                            student.giveRating(4);
                        }else if(a5RadioButton.isSelected()==true) {
                            student.giveRating(5); }
                        JOptionPane.showMessageDialog(frame, "Feedback has been recorded!",
                                "Successful", JOptionPane.INFORMATION_MESSAGE);
                        frame.setVisible(false);
                        new StudentMenu(student);
                    }
                }
            });
        }
        else{
            notRated.setVisible(false);
            feedbackLabel.setText("Feedback has been given for this coach.");
        }
        nameField.setText(coach.getName());
        nameField.setEditable(false);
        IDField.setText(coach.getCoachID());
        IDField.setEditable(false);
        contactField.setText(coach.getPhone());
        contactField.setEditable(false);
        sportsField.setText(coach.getSportsCode());
        sportsField.setEditable(false);
        ratingField.setEditable(false);
        try{
            ratingField.setText(Integer.toString(coach.getRating() / coach.getTotalRates()));
        }catch (Exception e){
            ratingField.setText("0");
        }

    }

    public void hideRadioButtons(){
        a1RadioButton.setVisible(false);
        a2RadioButton.setVisible(false);
        a3RadioButton.setVisible(false);
        a4RadioButton.setVisible(false);
        a5RadioButton.setVisible(false);
    }

    public void showRadioButtons(){
        a1RadioButton.setVisible(true);
        a2RadioButton.setVisible(true);
        a3RadioButton.setVisible(true);
        a4RadioButton.setVisible(true);
        a5RadioButton.setVisible(true);
    }
}
