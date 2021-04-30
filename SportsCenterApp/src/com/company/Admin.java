package com.company;

import jdk.swing.interop.SwingInterOpUtils;
import org.w3c.dom.ls.LSOutput;

import java.util.Arrays;

public class Admin {
    private String ID;
    private String SportsCenterCode;


    public Admin (){}

    public static Admin adminLogin (String username, String password){
        String[] adminFileContent = FileServer.readFile("","Admin.txt");
        for (String line:adminFileContent){
//            System.out.println(line);
            String[] tokens = line.split("\\|");
//            for (String a:tokens)
//                System.out.println(a);
//
//            System.out.println(tokens[0]);
            if (tokens[0].equals(username) && tokens[1].equals(password)) {
                return new Admin();
            }
        }
        return null;
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
