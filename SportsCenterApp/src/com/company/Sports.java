package com.company;

import java.util.ArrayList;

public class Sports {
    private String name;
    private String sportsID;
    private int sportFees;
    private Schedule schedule;


    public Sports (String sportCenterCode, String[] details){
        name = details[0];
        sportsID = details[1];
        sportFees = Integer.parseInt(details[2]);
        schedule = new Schedule(sportCenterCode,details[3].split(","));
    }

    public String toString() {
        return name + "|" + sportsID;
    }

    // addCoach
    // deleteCoach
    // getters & setters
}
