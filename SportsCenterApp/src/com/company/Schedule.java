package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private List<Session> sessionList = new ArrayList<Session>();

    public Schedule (String sportsCenterCode, String[] sessionID) {
        String[] sessionFile = FileServer.readFile(sportsCenterCode,"Session.txt");
        for (String ID : sessionID){
            for (String line : sessionFile){
                String[] tokens = line.split("\\|");
                if (tokens[0].equals(ID))
                    sessionList.add(new Session(tokens));
            }
        }
    }



    public void addSession(Session newSession) {

    }


    public void removeSession() {

    }


    public Session getSession() {
        return null;
    }

    public String toString() {
        return sessionList.toString();
    }
}
