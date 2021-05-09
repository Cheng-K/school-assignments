package com.company;

import java.io.File;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        // TEST FOR FILE SERVER
//        System.out.println(subdir.isDirectory());

//        String[] arr = FileServer.readFile("","Admin.txt");
//        System.out.println(arr.length);
//        for (String string : arr){
//            System.out.println(string);
//        }
//    }
//        FileServer.appendFile("", "Admin.txt", "D*cK\n");
//        for (String str : FileServer.readFile("","Admin.txt")){
//            System.out.println(str);
//        }

        // TEST FOR ADMIN LOGIN
//        String[] content = FileServer.readFile("","Admin.txt");
//        System.out.println(Arrays.toString(content));
//        LoginScreen login = new LoginScreen();

//        String[] coachContent = FileServer.readFile("L002","Coach.txt");
//        Coach mycoach = new Coach(coachContent[0].split("\\|"));
//        System.out.println(mycoach);

//        Student A = new Student(new String[]{"Cheng Kei","S001","19","KL","012-3345","adam@gmail.com","Badminton","L001"});
//        new StudentProfile(A);
        Admin admin = new Admin("bruh","L001");
//        String[] mylist = {"Boo","null","19","KL","012-3345","adam@gmail.com","Badminton","L001"};
//        admin.createAccount(mylist,"123");
        admin.approveAccount("CK8");
    }
}
