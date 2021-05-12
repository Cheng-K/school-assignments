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
        name = coachDetails[0];              // I'll just remove the unused information from the text files actually
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
        rating = Float.parseFloat(coachDetails[9]);

    }

    @Override
    public String toString () {
        return name+"|"+coachID+"|"+dateJoined+"|"+dateTerminated+"|"+hourlyRate+"|"
                +phone+"|"+address+"|"+sportsCenterID+"|"+ sportsCode+"|"+rating;
    }

    public String getCoachID() {
        return coachID;
    }

    public String getName() {
        return name;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public String getDateTerminated() {
        return dateTerminated;
    }

    public int getHourlyRate() {
        return hourlyRate;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getSportsCenterID() {
        return sportsCenterID;
    }

    public String getSportsCode() {
        return sportsCode;
    }

    public float getRating() {
        return rating;
    }

    public void setCoachID(String coachID) {
        this.coachID = coachID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDateJoined(String dateJoined) {
        this.dateJoined = dateJoined;
    }

    public void setDateTerminated(String dateTerminated) {
        this.dateTerminated = dateTerminated;
    }

    public void setHourlyRate(int hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSportsCenterID(String sportsCenterID) {
        this.sportsCenterID = sportsCenterID;
    }

    public void setSportsCode(String sportsCode) {
        this.sportsCode = sportsCode;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
