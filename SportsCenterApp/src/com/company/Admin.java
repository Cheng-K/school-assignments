package com.company;

import jdk.swing.interop.SwingInterOpUtils;
import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
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
        Student newStudent = new Student(studentDetail,Student.findMyCoach(studentDetail[8],studentDetail[7]));
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
                Student student = new Student(token,Student.findMyCoach(token[8],token[7]));
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

    public int createSports(String sportsName,String fees)
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
        Sports sports = new Sports(getSportsCenterCode(),new String[]{sportsName,sportsID,fees});
        FileServer.appendFile(getSportsCenterCode(),"Sports.txt", sports +"\n");
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

    public void createSession(String[] sessionDetail,String day, String sportCode) {
        int max = 0;
        String[] sessionFileContent = FileServer.readFile(getSportsCenterCode(), "Session.txt");
        for (String line : sessionFileContent) {
            String[] token = line.split("\\|");
            if (Integer.parseInt(token[1].replace("T", "")) > max) {
                max = Integer.parseInt(token[1].replace("T", ""));
            }
        }
        if (max<9){
            sessionDetail[1] = "T0" + (max+1);
        }else{
            sessionDetail[1] = "T" + (max+1);
        }
        Session newSession = new Session(sessionDetail);
        FileServer.appendFile(getSportsCenterCode(),"Session.txt",newSession.getWriteToFileString()+"\n");
        String[] scheduleFileContent = FileServer.readFile(getSportsCenterCode(),"Schedule.txt");
        int cnt = 0;
        Schedule.updateScheduleFile(getSportsCenterCode());
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


    public int modCoach(List<String>newDetails,Coach coach){
        String oldString = coach.toString();
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
        String newString = coach.toString();
        String[] coachFileContent = FileServer.readFile(this.SportsCenterCode,"Coach.txt");
        FileServer.findAndReplace(coachFileContent,oldString,newString);
        return FileServer.writeFile(this.SportsCenterCode,"Coach.txt",String.join("\n",coachFileContent)+"\n");


    }
    public int modSports (Integer newFees, Sports sports){
        String oldString = sports.toString();
        sports.setSportFees(newFees);
        String[] currentFileContent = FileServer.readFile(SportsCenterCode,"Sports.txt");
        FileServer.findAndReplace(currentFileContent,oldString,sports.toString());
        return FileServer.writeFile(SportsCenterCode,"Sports.txt",String.join("\n",currentFileContent)+"\n");
    }
    public int modSession (List<String>newDetails,Session session){
        String oldString = session.getWriteToFileString();
        // modifying attributes
        String newDay = newDetails.get(0);
        String[] newStartTime = newDetails.get(2).split(":");
        String[] newEndTime = newDetails.get(3).split(":");
        session.setDay(newDay.substring(0,1).toUpperCase() + newDay.substring(1));
        session.setStartTime(LocalTime.of(Integer.parseInt(newStartTime[0]),Integer.parseInt(newStartTime[1])));
        session.setEndTime(LocalTime.of(Integer.parseInt(newEndTime[0]),Integer.parseInt(newEndTime[1])));
        session.setDuration(Duration.between(session.getStartTime(),session.getEndTime()));
        // get new string representation
        String newString = session.getWriteToFileString();
        String[] sessionFileContent = FileServer.readFile(this.SportsCenterCode,"Session.txt");
        FileServer.findAndReplace(sessionFileContent,oldString,newString);
        if (FileServer.writeFile(this.SportsCenterCode,"Session.txt",String.join("\n",sessionFileContent)+"\n") == 1)
            return 1;
        else
            return Schedule.updateScheduleFile(this.SportsCenterCode);
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
            if (rating == (coach.getRating()/coach.getTotalRates()))
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

    public int deleteCoachRecord (List<Coach> coachList, int index){
        coachList.remove(index);
        if (FileServer.writeFile(this.SportsCenterCode,"Coach.txt","") == 1)
            return 1;
        for (Coach coach : coachList){
            if (FileServer.appendFile(this.SportsCenterCode,"Coach.txt",coach.toString()+"\n") == 1)
                return 1;
        }
        return 0;
    }

    public int deleteSportsRecord (List<Sports> sportsList, int index){
        sportsList.remove(index);
        if (FileServer.writeFile(this.SportsCenterCode,"Sports.txt","") == 1)
            return 1;
        for (Sports sports : sportsList){
            if (FileServer.appendFile(this.SportsCenterCode,"Sports.txt",sports.toString()+"\n") == 1)
                return 1;
        }
        return 0;
    }

    public int deleteStudentRecord (List<Student> studentList, int index){
        studentList.remove(index);
        if (FileServer.writeFile(this.SportsCenterCode,"Student.txt","") == 1)
            return 1;
        for (Student student : studentList){
            if (FileServer.appendFile(this.SportsCenterCode,"Student.txt",student.toString()+"\n") == 1)
                return 1;
        }
        return 0;
    }

    public int deleteSessionRecord (Session sessionToBeRemove){
        // intialize all sessions
        List<Session> allSession = new ArrayList<>();
        System.out.println(sessionToBeRemove.toString());
        String[] sessionFileContent = FileServer.readFile(this.SportsCenterCode,"Session.txt");
        for (String line : sessionFileContent){
            allSession.add(new Session(line.split("\\|")));
        }
        allSession.remove(sessionToBeRemove);
        if (FileServer.writeFile(this.SportsCenterCode,"Session.txt","") == 1)
            return 1;
        for (Session session : allSession){
            if (FileServer.appendFile(this.SportsCenterCode,"Session.txt",session.getWriteToFileString()+"\n") == 1)
                return 1;
        }
        return Schedule.updateScheduleFile(this.SportsCenterCode);
    }


}
