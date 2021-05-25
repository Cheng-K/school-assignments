package com.company;

import java.util.Comparator;

public class Student extends BaseStudent {
    private String name;
    private String studentID;
    private int age;
    private String address;
    private String contactNumber;
    private String email;
    private String registeredSports;
    private String sportsCenterCode;
    private Sports sport;
    private Coach coach;
    private boolean givenRating;

    public static class sortByName implements Comparator<Student>{
        @Override
        public String toString() {
            return "Sort By Name";
        }
        @Override
        public int compare(Student o1, Student o2) {
            return o1.name.compareTo(o2.name);
        }
    }
    public static Coach findMyCoach(String coachName,String sportsCenterCode) {
        String[] coachFile = FileServer.readFile(sportsCenterCode,"Coach.txt");
        Coach foundCoach = null;
        for(String line:coachFile){
            String[] coachDetails = line.split("\\|");
            if (coachDetails[0].equals(coachName)){
                foundCoach = new Coach(coachDetails);
            }
        }
        return foundCoach;
    }

    public Student(String[] studentDetails) {
        name = studentDetails[0];
        studentID = studentDetails[1];
        age = Integer.parseInt(studentDetails[2]);
        address = studentDetails[3];
        contactNumber = studentDetails[4];
        email = studentDetails[5];
        registeredSports = studentDetails[6];
        sportsCenterCode = studentDetails[7];
        String[] coachFile = FileServer.readFile(getSportsCenterCode(),"Coach.txt");
        for(String line:coachFile){
            String[] coachDetails = line.split("\\|");
            if (coachDetails[0].equals(studentDetails[8])){
                coach = new Coach(coachDetails);
                break;
            }
        }
        givenRating = Boolean.parseBoolean(studentDetails[9]);
    }

    public Student(String[] studentDetails,Coach myCoach) {
        name = studentDetails[0];
        studentID = studentDetails[1];
        age = Integer.parseInt(studentDetails[2]);
        address = studentDetails[3];
        contactNumber = studentDetails[4];
        email = studentDetails[5];
        registeredSports = studentDetails[6];
        sportsCenterCode = studentDetails[7];
        String[] coachFile = FileServer.readFile(getSportsCenterCode(),"Coach.txt");
        this.coach = myCoach;
        givenRating = Boolean.parseBoolean(studentDetails[9]);
    }


    public static Student studentLogin (String username, String password){
        String[] studentFileContent = FileServer.readFile("Student.txt");
        for (String line:studentFileContent){
            String[] tokens = line.split("\\|");
            if (tokens[0].equals(username) && tokens[1].equals(password)) {
                String[] studentDetails = FileServer.readFile(tokens[2],"Student.txt");
                for(String lines:studentDetails)
                {
                    String[] studentInfo = lines.split("\\|");
                    if (studentInfo[0].equals(username))
                    {
                        return new Student(studentInfo,Student.findMyCoach(studentInfo[8],studentInfo[7]));
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return name +"|"+ studentID +"|" + age +"|"+ address + "|" + contactNumber + "|" + email + "|"+
                registeredSports + "|" + sportsCenterCode + "|" + coach.getName() + "|" + givenRating;
    }

    public void updateDetails (int age,String address,String contactNumber,String email){
        setAge(age);
        setAddress(address);
        setContactNumber(contactNumber);
        setEmail(email);
    }

    public static String[] getAllAttributes () {
        return new String[] {"Name","StudentID","Age","Address","Contact Number", "Email","Registered Sports",
                "Sports Center Code", "Coach", "Rating given"};
    }

    public void giveRating(int rating){
        String[] coachFileContent = FileServer.readFile(getSportsCenterCode(),"Coach.txt");
        int cnt = 0;
        for (String line:coachFileContent){
            String[] token = line.split("\\|");
            if (token[1].equals(coach.getCoachID())){
                coach.setRating(coach.getRating()+rating);
                coach.setTotalRates(coach.getTotalRates()+1);
                coachFileContent[cnt] = coach.toString();
                break;
            }
            cnt++;
        }
        FileServer.writeFile(getSportsCenterCode(),"Coach.txt","");
        for (String line:coachFileContent){
            FileServer.appendFile(getSportsCenterCode(),"Coach.txt",line+"\n");
        }
        String[] studentFile = FileServer.readFile(getSportsCenterCode(),"Student.txt");
        cnt = 0;
        setGivenRating(true);
        for (String line:studentFile){
            String[] studentDetails = line.split("\\|");
            if (getName().equals(studentDetails[0])){
                studentFile[cnt]=toString();
                break;
            }
            cnt++;
        }
        FileServer.writeFile(getSportsCenterCode(),"Student.txt","");
        for (String line:studentFile){
            FileServer.appendFile(getSportsCenterCode(),"Student.txt",line+"\n");
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegisteredSports() {
        return registeredSports;
    }

    public String getSportsCenterCode() {
        return sportsCenterCode;
    }

    public boolean getGivenRating() {
        return givenRating;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setGivenRating(boolean givenRating) {
        this.givenRating = givenRating;
    }
}
