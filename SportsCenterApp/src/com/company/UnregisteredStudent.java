package com.company;

public class UnregisteredStudent extends BaseStudent{

    public int registerAccount(String[] studentDetail, String password) //ID passed in will be null
    {
        RegisteredStudent newStudent = new RegisteredStudent(studentDetail, RegisteredStudent.findMyCoach(studentDetail[8],studentDetail[7]));
        String[] fileCheck = FileServer.readFile("UnregStudent.txt");
        for (String line : fileCheck) {
            String[] token = line.split("\\|");
            if (token[0].equals(newStudent.getName())) {
                System.out.println(1);
                return 1; //When profile with same name and sports centre already created and waiting for admins' approval
            }
        }
        fileCheck = FileServer.readFile(newStudent.getSportsCenterCode(), "Student.txt");
        for (String line : fileCheck) {
            String[] token = line.split("\\|");
            if (token[0].equals(newStudent.getName())) {
                System.out.println(2);
                return 2; //Profile with same name already exists in the sports center database
            }
        }
        FileServer.appendFile("UnregStudent.txt",newStudent +"|"+password+"\n");
        System.out.println("Done");
        return 0;
    }
}
