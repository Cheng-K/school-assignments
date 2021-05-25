package com.company;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Objects;

import static java.lang.Math.round;

public class Session {
    private String sessionID;
    private LocalTime startTime;
    private LocalTime endTime;
    private Duration duration;
    private String sportName;
    private String coachName;
    private String day;
    public static class sortByDay implements Comparator<Session>{
        private int changeDayToNum (String day){
            if (day.equalsIgnoreCase("monday"))
                return 1;
            else if (day.equalsIgnoreCase("tuesday"))
                return 2;
            else if (day.equalsIgnoreCase("wednesday"))
                return 3;
            else if (day.equalsIgnoreCase("thursday"))
                return 4;
            else if (day.equalsIgnoreCase("friday"))
                return 5;
            else if (day.equalsIgnoreCase("saturday"))
                return 6;
            else
                return 0;
        }

        @Override
        public int compare(Session o1, Session o2) {
            int day1 = changeDayToNum(o1.day);
            int day2 = changeDayToNum(o2.day);
            return day1-day2;
        }
        @Override
        public String toString() {
            return "Sort by day";
        }


    }

    public static class sortByName implements Comparator<Session>{
        @Override
        public int compare(Session o1, Session o2) {
            return o1.sportName.compareTo(o2.sportName);
        }

        @Override
        public String toString() {
            return "Sort by sport name";
        }


    }
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
        return day+"|"+sessionID + "|" + startTime + "|" + endTime + "|"+ String.format("%.2f hours",duration.toMinutes()/60.0)+ "|"+ sportName + "|"+ coachName;
    }

    public String getWriteToFileString () {
        return day+"|"+sessionID + "|" + startTime.getHour() + "|" + startTime.getMinute() + "|" + endTime.getHour() + "|"+
                endTime.getMinute() + "|"+ sportName + "|"+ coachName;
    }

    public String getSessionID() {
        return sessionID;
    }
    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getSportName() {
        return sportName;
    }

    public String getCoachName() {
        return coachName;
    }

    public String getDay() {
        return day;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return getSessionID().equals(session.getSessionID()) && getStartTime().equals(session.getStartTime()) && getEndTime().equals(session.getEndTime()) && getDuration().equals(session.getDuration()) && getSportName().equals(session.getSportName()) && getCoachName().equals(session.getCoachName()) && getDay().equals(session.getDay());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSessionID(), getStartTime(), getEndTime(), getDuration(), getSportName(), getCoachName(), getDay());
    }
}
