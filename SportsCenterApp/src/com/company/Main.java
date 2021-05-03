package com.company;

import java.io.File;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        // TEST FOR FILE SERVER
//        File subdir = new File("textFiles","test.txt");
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
//        SportCenter admin1 = new SportCenter("Jack","bruh");
//        System.out.println(admin1.getMasterAdmin());
//        String[] content = FileServer.readFile("","Admin.txt");
//        System.out.println(Arrays.toString(content));
        LoginScreen login = new LoginScreen();

    }
}
