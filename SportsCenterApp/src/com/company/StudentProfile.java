package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* Questions : Modifying password*/

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

    /*
        Class Description : Perform action when modify button is clicked such as hiding modify button, enabling edit, etc..

     */
    private class modifyButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            enableEdit();
            frame.setTitle("Modify Personal Details");
            modifyDetailsPanel.setVisible(false);
            saveDetailsPanel.setVisible(true);
        }
    }

    /* Class Description : Perform action when save details button is clicked such as :
                            1. Updating the student information in the file
                            2. Displaying success or failure message

     */

    private class saveDetailsButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            String[] fileContent = FileServer.readFile(student.getSportsCenterCode(),"Student.txt");
            String oldDetails = student.toString();
            updateStudentDetails();
            String newDetails = student.toString();
            String[] newFileContent = FileServer.findAndReplace(fileContent,oldDetails,newDetails);
            int writeFailed = FileServer.writeFile(student.getSportsCenterCode(),"Student.txt",String.join("",newFileContent));

            if (writeFailed == 1){
                // display error message
                JOptionPane.showMessageDialog(frame,"Cannot update your information. Contact your admin","Error",JOptionPane.ERROR_MESSAGE);
            }else {
                // display success message
                JOptionPane.showMessageDialog(frame,"Update Successful","Successful",JOptionPane.INFORMATION_MESSAGE);
                // direct back to display profile menu
                saveDetailsPanel.setVisible(false);
                modifyDetailsPanel.setVisible(true);
                setAllField();
                setAllFieldDisabled();
            }
        }
    }

    public StudentProfile (Student currStudent){
        student = currStudent;
        setAllField();
        setAllFieldDisabled();
        modifyDetailsButton.addActionListener(new modifyButtonListener());
        saveDetailsButton.addActionListener (new saveDetailsButtonListener());
        frame = new JFrame("Personal Profile");
        frame.setContentPane(studentProfilePanel);
        saveDetailsPanel.setVisible(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /*
        Method Name : setAllField
        Parameter & Return  : Null
        Description : Set all text field with student information
     */

    private void setAllField (){
        nameField.setText(student.getName());
        //passwordField.setText()
        studentIDField.setText(student.getStudentID());
        ageField.setText(Integer.toString(student.getAge()));
        addressField.setText(student.getAddress());
        phoneField.setText(student.getContactNumber());
        emailField.setText(student.getEmail());
        sportsEnrolledField.setText(student.getRegisteredSports());
        sportsCenterField.setText(student.getSportsCenterCode());
    }

    /*
        Method Name : setAllFieldDisabled
        Parameter & Return : Null
        Description : Disable editing for all text fields
     */

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

    /*
       Method Name : enableEdit
       Parameter & Return : Null
       Description : Allow editing for some text fields
     */

    private void enableEdit() {
        passwordField.setEditable(true);
        ageField.setEditable(true);
        addressField.setEditable(true);
        phoneField.setEditable(true);
        emailField.setEditable(true);
        setTooltipText();
    }

    /*
        Method Name : updateStudentDetails
        Parameter & Return : Null
        Description : Update student attribute with edited information
     */

    private void updateStudentDetails () {
        student.setAge(Integer.parseInt(ageField.getText()));
        student.setAddress(addressField.getText());
        student.setContactNumber(phoneField.getText());
        student.setEmail(emailField.getText());
    }

    /*
        Method Name : setTooltipText
        Parameter & Return : Null
        Description : Set tooltip text for uneditable text fields
     */

    private void setTooltipText() {
        nameField.setToolTipText("Only admin has rights to change this field");
        studentIDField.setToolTipText("Only admin has rights to change this field");
        sportsEnrolledField.setToolTipText("Only admin has rights to change this field");
        sportsCenterField.setToolTipText("Only admin has rights to change this field");
    }


}
