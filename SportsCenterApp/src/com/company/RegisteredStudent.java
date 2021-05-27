package com.company;

import java.util.Comparator;

public class RegisteredStudent extends BaseStudent {
    private String name;
    private String studentID;
    private int age;
    private String address;
    private String contactNumber;
    private String email;
    private final String registeredSports;
    private final String sportsCenterCode;
    private final Coach coach;
    private boolean givenRating;

    /*
    Class       : sortByName (implements Comparator interface)
    Description : Paired with built in list sorting method to sort students by their name in alphebetical order
    */

    public static class sortByName implements Comparator<RegisteredStudent>{
        @Override
        public String toString() {
            return "Sort By Name";
        }
        @Override
        public int compare(RegisteredStudent o1, RegisteredStudent o2) {
            return o1.name.compareTo(o2.name);
        }
    }

    /*
        Method       : findMyCoach (Static method)
        Description  : Used to find student's coach given coachName and sportsCenterCode
        Parameter    : coachName (String) , sportsCentercode (String)
        Return       : Coach object / Null (if not found)
    */
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

    /*
     Method       : studentLogin (Static method)
     Description  : Used to validate student credentials before creating a student instance
     Parameter    : username(String), password(String)
     Return       : RegisteredStudent object / null if invalid credentials
 */
    public static RegisteredStudent studentLogin (String username, String password){
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
                        return new RegisteredStudent(studentInfo, RegisteredStudent.findMyCoach(studentInfo[8],studentInfo[7]));
                    }
                }
            }
        }
        return null;
    }

    public RegisteredStudent(String[] studentDetails, Coach myCoach) {
        name = studentDetails[0];
        studentID = studentDetails[1];
        age = Integer.parseInt(studentDetails[2]);
        address = studentDetails[3];
        contactNumber = studentDetails[4];
        email = studentDetails[5];
        registeredSports = studentDetails[6];
        sportsCenterCode = studentDetails[7];
        this.coach = myCoach;
        givenRating = Boolean.parseBoolean(studentDetails[9]);
    }


    @Override
    public String toString(){
        return name +"|"+ studentID +"|" + age +"|"+ address + "|" + contactNumber + "|" + email + "|"+
                registeredSports + "|" + sportsCenterCode + "|" + coach.getName() + "|" + givenRating;
    }

    /*
     Method       : updateDetails
     Description  : Used to update student details
     Parameter    : age (int), address (String), contactNumber (String), email (String)
     Return       : void
 */
    public void updateDetails (int age,String address,String contactNumber,String email){
        setAge(age);
        setAddress(address);
        setContactNumber(contactNumber);
        setEmail(email);
    }

    /*
     Method       : getAllAttributes (static)
     Description  : Used to get all attributes a student has
     Parameter    : -
     Return       : An array of strings containing all the attributes name
 */
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

    /*----------Getters and Setters----------*/

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
