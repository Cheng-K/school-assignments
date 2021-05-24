package com.company;

import java.util.Comparator;

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
    private int rating;
    private int totalRates;
    private Sports sports; // figure this out


    public static class sortByRating implements Comparator<Coach> {
        @Override
        public int compare(Coach coach1, Coach coach2) {
            return (int)((coach1.rating-coach2.rating)*10);
        }

        @Override
        public String toString() {
            return "Sort by Rating";
        }
    }

    public static class sortByPay implements Comparator<Coach>{
        @Override
        public int compare(Coach coach1, Coach coach2) {
            return (int) ((coach1.hourlyRate-coach2.hourlyRate)*10);
        }

        @Override
        public String toString() {
            return "Sort by Hourly Rate";
        }
    }

    public static class sortByID implements Comparator<Coach>{
        @Override
        public int compare(Coach coach1, Coach coach2) {
            return coach1.coachID.compareTo(coach2.coachID);
        }

        @Override
        public String toString () {
            return "Sort by Coach ID";
        }
    }


    public Coach (String[] coachDetails){
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
        sportsCode = coachDetails[8];
        rating = Integer.parseInt(coachDetails[9]);
        totalRates = Integer.parseInt(coachDetails[10]);

        // initialize sports

    }
    public Coach (String[] coachDetails,Sports sports){
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
        sportsCode = coachDetails[8];
        rating = Integer.parseInt(coachDetails[9]);
        totalRates = Integer.parseInt(coachDetails[10]);
        this.sports = sports;
        // initialize sports

    }
    public static String[] getAllAttributes () {
        return new String[] {"Coach ID","Name","Date Joined","Date Terminated","Hourly Rate","Contact Number","Address","Sports Center ID","Sports Code","Rating"};
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public void setCoachID(String coachID) {
        this.coachID = coachID;
    }

    public int getTotalRates() {
        return totalRates;
    }

    public void setTotalRates(int totalRates) {
        this.totalRates = totalRates;
    }

    public String getCoachID() {
        return coachID;
    }

    @Override
    public String toString () {
        return coachID + "|" + name + "|" + dateJoined + "|" + dateTerminated + "|" + hourlyRate + "|"
                + phone + "|" + address + "|" + sportsCenterID + "|"+ sportsCode + "|" + rating + "|" + totalRates;
    }


}
