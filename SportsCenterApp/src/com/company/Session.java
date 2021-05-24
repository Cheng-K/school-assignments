package com.company;

import java.time.Duration;
import java.time.LocalTime;

public class Session {
    private String sessionID;
    private LocalTime startTime;
    private LocalTime endTime;
    private Duration duration;
    private String sportName;
    private String coachName;
    private String day;

    public Session (String[] details){
        day = details[0];
        sessionID = details[1];
        startTime = LocalTime.of(Integer.parseInt(details[2]),Integer.parseInt(details[3]));
        endTime = LocalTime.of(Integer.parseInt(details[4]),Integer.parseInt(details[5]));
        duration = Duration.between(startTime,endTime);
        sportName = details[6];
        coachName = details[7];
    }

    public static String[] getAllAttributes (){
        return new String[] {"Day","Session ID", "Start Time", "End Time", "Duration","Sport Name", "Coach Name"};
    }
    @Override
    public String toString () {
        return day+"|"+sessionID + "|" + startTime + "|" + endTime + "|"+ duration.toHours() + "|"+ sportName + "|"+ coachName;
    }

}
