package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Schedule {
    private ArrayList<Session> sessionList = new ArrayList<>();
    private String type = "";

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

    public Schedule (String sportsCenterCode, String type, String[] sessionID){
        this(sportsCenterCode,sessionID);
        this.type = type;
    }
    public Schedule(String type){
        this.type = type;
    }

    public void addSession(Session newSession) {
        sessionList.add(newSession);
    }

    public void removeSession() {
    }

    public static boolean updateScheduleFile (String sportCenterCode) {
        HashMap<String,Schedule> scheduleHashMap = new HashMap<>();
        String [] currentScheduleFile = FileServer.readFile(sportCenterCode,"Schedule.txt");
        for (String line:currentScheduleFile){
            String[] tokens = line.split("\\|");
            scheduleHashMap.put(tokens[0],new Schedule(tokens[0]));
        }
        System.out.println(scheduleHashMap.values());
        String[] sessionFile = FileServer.readFile(sportCenterCode,"Session.txt");
        for (String line:sessionFile){
            String[] tokens = line.split("\\|");
            Session sessionToAdd = new Session(tokens);
            scheduleHashMap.get(tokens[0]).addSession(sessionToAdd);
            scheduleHashMap.get(tokens[6]).addSession(sessionToAdd);
        }
        if (FileServer.writeFile(sportCenterCode,"Schedule.txt","") == 1){return false;}
        System.out.println(scheduleHashMap.values());
        for (Schedule schedule : scheduleHashMap.values()){
            if (FileServer.appendFile(sportCenterCode,"Schedule.txt",schedule.getWriteToFileString()) == 1)
                return false;
        }
        return true;
    }

    public String getWriteToFileString () {
        StringBuilder writeThis = new StringBuilder(type);

        for (Session session : sessionList){
            writeThis.append("|").append(session.getSessionID());
        }
        writeThis.append("|").append("\n");
        return writeThis.toString();
    }

    public Session getSession(int index) {
        return sessionList.get(index);
    }

    public static String[] getAllAttributes () {
        return Session.getAllAttributes();
    }

    public String toString() {
        return "Show schedule for " + type;
    }

    public ArrayList<Session> getAllSession() {
        return sessionList;
    }
}
