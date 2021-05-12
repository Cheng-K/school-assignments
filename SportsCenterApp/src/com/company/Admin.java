package com.company;

import jdk.swing.interop.SwingInterOpUtils;
import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Admin {
    private String ID;
    private String SportsCenterCode;

    public Admin(String ID, String sportsCenterCode) {
        this.ID = ID;
        SportsCenterCode = sportsCenterCode;
    }


    public static Admin adminLogin(String username, String password) {
        String[] adminFileContent = FileServer.readFile("", "Admin.txt");
        for (String line : adminFileContent) {
            String[] tokens = line.split("\\|");
            if (tokens[0].equals(username) && tokens[1].equals(password)) {
                return new Admin(tokens[2], tokens[3]);
            }
        }
        return null;
    }

    public int createAccount(String[] studentDetail, String password) //ID passed in will be null, and sports centre same with admins'
    {
        Student newStudent = new Student(studentDetail);
        String fileCheck[] = FileServer.readFile("UnregStudent.txt");
        for (String line : fileCheck) {
            String[] token = line.split("\\|");
            if (token[0].equals(newStudent.getName())) {
                System.out.println(1);
                return 1; //When profile with same name and sports centre already created and waiting for admins' approval
            }
        }
        fileCheck = FileServer.readFile(getSportsCenterCode(), "Student.txt");
        for (String line : fileCheck) {
            String[] token = line.split("\\|");
            if (token[0].equals(newStudent.getName())) {
                System.out.println(2);
                return 2; //Profile with same name already exists in the sports center database
            }
        }
        newStudent.setStudentID(newStudentID());
        FileServer.appendFile("Student.txt", newStudent.getName() + "|" + password + "|" + getSportsCenterCode() + "\n");
        FileServer.appendFile(getSportsCenterCode(), "Student.txt", newStudent.toString()+"\n");
        return 0;
    }

    public String newStudentID() {
        int max = 0;
        String newID="";
        String[] fileContent = FileServer.readFile(getSportsCenterCode(), "Student.txt");
        for (String line : fileContent) {
            String[] token = line.split("\\|");
            if (Integer.parseInt(token[1].replace("S", "")) > max) {
                max = Integer.parseInt(token[1].replace("S", ""));
            }
        }
        max++;
        if (max<10) {
            newID = "S00"+max;
        }
        else if (max<100) {
            newID = "S0"+max;
        }
        else {
            newID = "S"+max;
        }
        return newID;
    }

    public void viewAccRequest()
    {
        String[] fileContent = FileServer.readFile("UnregStudent.txt");
        //Display info from fileContent to the GUI
    }

    public void approveAccount(String name)
    {
        String[] unregStudent = FileServer.readFile("UnregStudent.txt");
        for(String line:unregStudent)
        {
            String[] token = line.split("\\|");
            if (name.equals(token[0]))
            {
                Student student = new Student(token);
                student.setStudentID(newStudentID());
                FileServer.appendFile(getSportsCenterCode(),"Student.txt",student.toString()+"\n");
                FileServer.appendFile("Student.txt",student.getName()+"|"+token[8]+"|"+getSportsCenterCode()+"\n");
                String[] fileContent = FileServer.readFile("UnregStudent.txt");
                FileServer.writeFile("UnregStudent.txt","");
                for (String newlines:fileContent)
                {
                    if(!(newlines.equals(line)))
                    {
                        FileServer.appendFile("UnregStudent.txt",newlines+"\n");
                    }
                }
            }
        }

    }

    /*  Method name : sortCoaches
        Parameter   : coachList (Array list containing coach object) , coachList (Name of column to sort by), ascending (True for ascending, False for descending)
        Return      : void
     */

    public void sortCoaches (ArrayList<Coach> coachList, Comparator<Coach> sorter, boolean ascending) {
        Collections.sort(coachList,sorter);
        if (!ascending) // Reverse the sorted coachList to produce descending order
            Collections.reverse(coachList);
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSportsCenterCode() {
        return SportsCenterCode;
    }

    public void setSportsCenterCode(String sportsCenterCode) {
        SportsCenterCode = sportsCenterCode;
    }
}
