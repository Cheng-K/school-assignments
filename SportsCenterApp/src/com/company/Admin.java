package com.company;

import jdk.swing.interop.SwingInterOpUtils;
import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

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
                return 1; //When profile with same name and sports centre already created and waiting for admins' approval
            }
        }
        fileCheck = FileServer.readFile(getSportsCenterCode(), "Student.txt");
        for (String line : fileCheck) {
            String[] token = line.split("\\|");
            if (token[0].equals(newStudent.getName())) {
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
                FileServer.appendFile("Student.txt",student.getName()+"|"+token[10]+"|"+getSportsCenterCode()+"\n");
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
            if (token[0].equals(newCoach.getName()))
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
            if (Integer.parseInt(token[1].replace("C",""))>max)
            {
                max = Integer.parseInt(token[1].replace("C",""));
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


    public int verifyCoachDetails (AdminModifyMenu form, List<String> coachDetails){
        int returnNum = 0;
        // Check for each length
        for (int index = 0; index<coachDetails.size(); index++){
            if (coachDetails.get(index).length() >= 100){
                form.setBorderRed(index,"Value is too unrealistic large/long");
                returnNum = 1;
            }
            else {
                switch (index){
                    case 0 :
                        if (!coachDetails.get(0).startsWith("C") && !onlyDigits(coachDetails.get(0).substring(1))) {
                            form.setBorderRed(index, "Coach ID should start with C and end with digits");
                            returnNum = 1;
                        }
                        break;
                    case 1:
                        break;
                    case 2:
                        if (!isDateObject(coachDetails.get(2))) {
                            form.setBorderRed(index, "Date format should be YYYY-MM-DD");
                            returnNum = 1;
                        }
                        break;
                    case 3:
                        if (!isDateObject(coachDetails.get(3)) && !coachDetails.get(3).equals("null")) {
                            form.setBorderRed(index, "Date format should be YYYY-MM-DD");
                            returnNum = 1;
                        }
                        break;
                    case 4:
                        if (!isIntegerObject(coachDetails.get(4))) {
                            form.setBorderRed(index, "Invalid integer provided");
                            returnNum = 1;
                        }
                        break;
                    case 5 :
                        if (!onlyDigits(coachDetails.get(5))) {
                            form.setBorderRed(index, "Invalid contact number, only digits are allowed");
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

    public void modCoach(List<String>newDetails,Coach coach){
        coach.setCoachID(newDetails.get(0));
        coach.setDateJoined(LocalDate.parse(newDetails.get(2)));
        try {
            coach.setDateTerminated(LocalDate.parse(newDetails.get(3)));
        }
        catch (Exception e){
            coach.setDateTerminated(null);
        }
        coach.setHourlyRate(Integer.parseInt(newDetails.get(4)));
        coach.setPhone(newDetails.get(5));
        coach.setAddress(newDetails.get(6));
        // Other continue below here
    }

    public  ArrayList<Coach> searchCoach(List<Coach>coachList,String ID){
        ArrayList<Coach>found = new ArrayList<>();
        for (Coach coach:coachList){
            if (ID.equalsIgnoreCase(coach.getCoachID()))
                found.add(coach);
        }
        return found;
    }
    public ArrayList<Coach> searchCoach(List<Coach>coachList,int rating){
        ArrayList<Coach>found = new ArrayList<>();
        for (Coach coach:coachList){
            if (rating == coach.getRating())
                found.add(coach);
        }
        return found;
    }
    public ArrayList<Coach> searchCoach(List<Coach>coachList,String ID, int rating){
        return searchCoach(searchCoach(coachList,ID),rating);
    }

    public ArrayList<Sports> searchSports(List <Sports> sportsList, String ID){
        ArrayList<Sports>found = new ArrayList<>();
        for (Sports sports : sportsList){
            if (ID.equalsIgnoreCase(sports.getSportsID()))
                found.add(sports);
        }
        return found;
    }

    public ArrayList<Student> searchStudent(List<Student>studentList,String ID){
        ArrayList<Student>found = new ArrayList<>();
        for (Student student : studentList){
            if (ID.equalsIgnoreCase(student.getStudentID()))
                found.add(student);
        }
        return found;
    }


    // Not sure where to put it
    private boolean onlyDigits (String str){
        for (int index = 0; index < str.length(); index ++){
            if (!Character.isDigit(str.charAt(index)))
                return false;
        }
        return true;
    }

    private boolean isDateObject (String str){
        try {
            LocalDate.parse(str);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    private boolean isIntegerObject (String str){
        try{
            Integer.parseInt(str);
        } catch (Exception e){
            return false;
        }
        return true;
    }
}
