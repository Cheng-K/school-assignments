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

    public Session (String[] details){
        sessionID = details[0];
        startTime = LocalTime.of(Integer.parseInt(details[1]),Integer.parseInt(details[2]));
        endTime = LocalTime.of(Integer.parseInt(details[3]),Integer.parseInt(details[4]));
        duration = Duration.between(startTime,endTime);
        sportName = details[5];
        coachName = details[6];
    }

    @Override
    public String toString () {
        return sessionID + "|" + startTime + "|" + endTime;
    }

}
