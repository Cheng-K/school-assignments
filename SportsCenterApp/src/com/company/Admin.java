package com.company;

public class Admin {
    private String ID;
    private String SportsCenterCode;

    public static Admin adminLogin (String email, String password){
       // String [] adminFile = read file
       // search for credentials
        return new Admin("001","001");

    }

    public Admin (String ID, String SportsCenterCode){
        this.ID = ID;
        this.SportsCenterCode = SportsCenterCode;
    }
}
