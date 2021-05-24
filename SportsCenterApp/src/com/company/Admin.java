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


    public void rejectAccount(String name)
    {
        String[] fileContent = FileServer.readFile("UnregStudent.txt");
        FileServer.writeFile("UnregStudent.txt","");
        for (String line:fileContent)
        {
            if(!(line.contains(name))){
                FileServer.appendFile("UnregStudent.txt",line+"\n");
            }
        }
    }

    public int createSports(String sportsName)
    {
        String[] fileContent = FileServer.readFile(getSportsCenterCode(),"Sports.txt");
        String sportsID="";
        int max = 0;
        for (String lines:fileContent)
        {
            String[] token = lines.split("\\|");
            if (token[0].equals(sportsName)) {
                return 1; //Sports name already exists
            }
            if (Integer.parseInt(token[1].replace("B",""))>max) {
                max = Integer.parseInt(token[1].replace("B",""));
            }
        }
        max++;
        if (max<10) {
            sportsID = "B00"+max;
        }
        else if (max<100) {
            sportsID = "B0"+max;
        }
        else {
            sportsID = "B"+max;
        }
        FileServer.appendFile(getSportsCenterCode(),"Sports.txt",sportsName+"|"+sportsID+"\n");
        return 0;
    }

    public int createCoach(String[] coachDetails) //coachID and date terminated passed will be null
    {
        Coach newCoach = new Coach(coachDetails);
        String[] fileContent = FileServer.readFile(getSportsCenterCode(),"Coach.txt");
        for (String line:fileContent)
        {
            String[] token = line.split("\\|");
            if (token[1].equals(newCoach.getName()))
            {
                return 1; //name already exists
            }
        }
        newCoach.setCoachID(newCoachID());
        FileServer.appendFile(getSportsCenterCode(),"Coach.txt",newCoach.toString()+"\n");
        return 0;
    }

    public String newCoachID()
    {
        int max = 0;
        String coachID="";
        String[] fileContent = FileServer.readFile(getSportsCenterCode(),"Coach.txt");
        for (String line:fileContent)
        {
            String[] token = line.split("\\|");
            if (Integer.parseInt(token[0].replace("C",""))>max)
            {
                max = Integer.parseInt(token[0].replace("C",""));
            }
        }
        max++;
        if (max<10) {
            coachID = "C00"+max;
        }
        else if (max<100) {
            coachID = "C0"+max;
        }
        else {
            coachID = "C"+max;
        }
        return coachID;
    }


    /*  Method name : sort (Generic Method)
        Parameter   : tArrayList (Array list containing T typed *preferably instances of Coach/Students*) ,
                      sorter (Comparator instance tells how to sort tArrayList ),
                      ascending (True for ascending, False for descending)
     */
    public <T> void sort (ArrayList<T> tArrayList, Comparator<T> sorter, boolean ascending){
        Collections.sort(tArrayList,sorter);
        if (!ascending) // reverse the sorted list if its descending
            Collections.reverse(tArrayList);
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
