package com.company;

public class Coach {
    private String coachID;
    private String name;
    private String dateJoined;
    private String dateTerminated;
    private int hourlyRate;
    private String phone;
    private String address;
    private String sportsCenterID;
    private String sportsCode;
    private float rating;

    public Coach (String[] coachDetails){    // Issue : we might have store extra information in the coach.txt?
        name = coachDetails[0];
        coachID = coachDetails[1];
        dateJoined = coachDetails[2];
        if (coachDetails[3].equals("null"))
            dateTerminated = null;
        else
            dateTerminated = coachDetails[3];
        hourlyRate = Integer.parseInt(coachDetails[4]);
        phone = coachDetails[5];
        address = coachDetails[6];
        sportsCenterID = coachDetails[7];
        sportsCode = coachDetails[9];
        rating = Float.parseFloat(coachDetails[11]);

    }

    public static String[] getAllAttributes () {
        return new String[] {"Coach ID","Name","Date Joined","Date Terminated","Hourly Rate","Contact Number","Address","Sports Center ID","Sports Code","Rating"};
    }

    @Override
    public String toString () {
        return coachID + "|" + name;
    }



}
