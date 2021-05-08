package com.company;

public class Student extends BaseStudent {
    private String name;
    private String studentID;
    private int age;
    private String address;
    private String contactNumber;
    private String email;
    private String registeredSports;
    private String sportsCenterCode;


    public Student(String[] studentDetails) { // change
        name = studentDetails[0];
        studentID = studentDetails[1];
        age = Integer.parseInt(studentDetails[2]);
        address = studentDetails[3];
        contactNumber = studentDetails[4];
        email = studentDetails[5];
        registeredSports = studentDetails[6];
        sportsCenterCode = studentDetails[7];
    }

    public static Student studentLogin (String username, String password){
        String[] studentFileContent = FileServer.readFile("","Student.txt");
        for (String line:studentFileContent){
            String[] tokens = line.split("\\|");
            if (tokens[0].equals(username) && tokens[1].equals(password)) {
                return new Student(tokens);
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return name +"|"+ studentID +"|" + age +"|"+ address + "|" + contactNumber + "|" + email + "|"+ registeredSports +"|"+ sportsCenterCode;
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

    public void setRegisteredSports(String registeredSports) {
        this.registeredSports = registeredSports;
    }

    public String getSportsCenterCode() {
        return sportsCenterCode;
    }

    public void setSportsCenterCode(String sportsCenterCode) {
        this.sportsCenterCode = sportsCenterCode;
    }


}
