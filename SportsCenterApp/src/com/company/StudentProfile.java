package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/* Questions : Modifying password*/

public class StudentProfile {
    private JFrame frame;
    private JPanel rootPanel;
    private JPanel studentProfilePanel;
    private JPanel saveDetailsPanel;
    private JPanel modifyDetailsPanel;
    private JPanel changePasswordPanel;
    private JTextField nameField;
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
    private JLabel studentIDLabel;
    private JLabel ageLabel;
    private JLabel addressLabel;
    private JLabel phoneLabel;
    private JLabel emailLabel;
    private JLabel sportsEnrolledLabel;
    private JLabel sportsCenterLabel;
    private JLabel Heading;
    private JButton saveDetailsButton;
    private JPasswordField newPasswordField;
    private JPasswordField newPasswordField2;
    private JButton showButton;
    private JButton resetPasswordButton;
    private JButton changePasswordButton;
    private JLabel reenterLabel;
    private JLabel newPasswordLabel;
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
            int writeFailed = FileServer.writeFile(student.getSportsCenterCode(),"Student.txt",String.join("\n",newFileContent)+"\n");

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

    private class changePasswordListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            studentProfilePanel.setVisible(false);
            changePasswordPanel.setVisible(true);
        }
    }

    private class resetPasswordListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // check both field is matching & length of password
            if (Arrays.equals(newPasswordField.getPassword(),newPasswordField2.getPassword()) && newPasswordField.getPassword().length >=3){
                String newCredentials = student.getName() + "|" + new String(newPasswordField.getPassword()) + "|" + student.getSportsCenterCode();
                String[] credentialsFile = FileServer.readFile("Student.txt");

                // find & replace old credential
                for (int index=0;index<credentialsFile.length;index++){
                    String[] tokens = credentialsFile[index].split("\\|");
                    if (tokens[0].equals(student.getName()) && tokens[2].equals(student.getSportsCenterCode())) {
                        credentialsFile[index] = newCredentials;
                        break;
                    }
                }
                int writeFail = FileServer.writeFile("Student.txt",String.join("\n",credentialsFile)+"\n");
                if (writeFail == 0)
                    JOptionPane.showMessageDialog(frame,"Password change is successful","Success",JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(frame,"Cannot change password.Contact Admin","Error",JOptionPane.ERROR_MESSAGE);

                // change back to personal info screen
                studentProfilePanel.setVisible(true);
                changePasswordPanel.setVisible(false);
            }
            else if (newPasswordField.getPassword().length <3)
                JOptionPane.showMessageDialog(frame,"Password is too short","Error",JOptionPane.ERROR_MESSAGE);
            else
                JOptionPane.showMessageDialog(frame,"Password provided does not match","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private class hideShowPasswordListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (showButton.getText().equals ("Show")){
                newPasswordField.setEchoChar((char)0);
                newPasswordField2.setEchoChar((char)0);
                showButton.setText("Hide");
            } else {
                newPasswordField.setEchoChar('\u25cf');
                newPasswordField2.setEchoChar('\u25cf');
                showButton.setText("Show");
            }

        }
    }


    public StudentProfile (Student currStudent){
        student = currStudent;
        setAllField();
        setAllFieldDisabled();
        modifyDetailsButton.addActionListener(new modifyButtonListener());
        saveDetailsButton.addActionListener (new saveDetailsButtonListener());
        changePasswordButton.addActionListener(new changePasswordListener());
        resetPasswordButton.addActionListener(new resetPasswordListener());
        showButton.addActionListener (new hideShowPasswordListener());
        frame = new JFrame("Personal Profile");
        frame.setContentPane(rootPanel);
        saveDetailsPanel.setVisible(false);
        changePasswordPanel.setVisible(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        backToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new StudentMenu(student);
            }
        });
    }

    /*
        Method Name : setAllField
        Parameter & Return  : Null
        Description : Set all text field with student information
     */

    private void setAllField (){
        nameField.setText(student.getName());
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
        student.updateDetails(Integer.parseInt(ageField.getText()),addressField.getText(),phoneField.getText(),emailField.getText());
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
