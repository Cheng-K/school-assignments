package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private List<Session> sessionList = new ArrayList<>();
    private String day = "";
    private String sports = "";

    public Schedule (String sportsCenterCode, String[] sessionID) {
        String[] sessionFile = FileServer.readFile(sportsCenterCode,"Session.txt");
        for (String ID : sessionID){
            for (String line : sessionFile){
                String[] tokens = line.split("\\|");
                if (tokens[1].equals(ID))
                    sessionList.add(new Session(tokens));
            }
        }
    }

    public Schedule (String sportsCenterCode, String day, String[] sessionID){
        this(sportsCenterCode,sessionID);
        this.day = day;
    }



    public void addSession(Session newSession) {

    }


    public void removeSession() {

    }


    public Session getSession() {
        return null;
    }

    public static String[] getAllAttributes () {
        return Session.getAllAttributes();
    }

    public String toString() {
        return "Show schedule for " + day;
    }

    public List<Session> getAllSession() {
        return sessionList;
    }
}
