package com.company;

import java.io.File;
import java.util.*;

public class Schedule implements Comparable<Schedule> {
    private ArrayList<Session> sessionList = new ArrayList<>();
    private String type = "";

    /* Method : updateScheduleFile
       Description : Update changes made to session.txt to schedule.txt
     */
    public static int updateScheduleFile (String sportCenterCode) {
        HashMap<String,Schedule> scheduleHashMap = new HashMap<>();
        String[] scheduleFile = FileServer.readFile(sportCenterCode,"Schedule.txt");
        for (String line:scheduleFile){
            String[] tokens = line.split("\\|");
            scheduleHashMap.put(tokens[0], new Schedule(tokens[0]));
        }

        String[] sessionFile = FileServer.readFile(sportCenterCode,"Session.txt");
        for (String line:sessionFile){
            String[] tokens = line.split("\\|");
            Session sessionToAdd = new Session(tokens);
            if (scheduleHashMap.get(tokens[0]) == null)
                scheduleHashMap.put(tokens[0], new Schedule(tokens[0]));

            scheduleHashMap.get(tokens[0]).addSession(sessionToAdd);

            if (scheduleHashMap.get(tokens[6]) == null) 
                scheduleHashMap.put(tokens[6], new Schedule(tokens[6]));


            scheduleHashMap.get(tokens[6]).addSession(sessionToAdd);
        }
        if (FileServer.writeFile(sportCenterCode,"Schedule.txt","") == 1){return 1;}

        ArrayList<Schedule> scheduleArrayList = new ArrayList<>(scheduleHashMap.values());
        Collections.sort(scheduleArrayList);
        for (Schedule schedule : scheduleArrayList){
            if (FileServer.appendFile(sportCenterCode,"Schedule.txt",schedule.getWriteToFileString()) == 1)
                return 1;
        }
        return 0;
    }


    public Schedule (String sportsCenterCode, String[] sessionID) {
        String[] sessionFile = FileServer.readFile(sportsCenterCode,"Session.txt");
        for (String ID : sessionID){
            for (String line : sessionFile){
                String[] tokens = line.split("\\|");
                if (tokens[1].equals(ID)) {
                    sessionList.add(new Session(tokens));
                    break;
                }
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

    public String getAllSessionToString() {
        StringBuilder returnString = new StringBuilder();
        if (sessionList.size() > 0) {
            int index = 0;
            for (; index < sessionList.size() - 1; index++) {
                returnString.append(sessionList.get(index).getSessionID()).append(",");
            }
            // Append last session without comma
            returnString.append(sessionList.get(index).getSessionID());
        }
        return returnString.toString();
    }

    @Override
    public int compareTo(Schedule o) {
        if (changeDayToNum(this.type) == changeDayToNum(o.type)){
            return this.type.compareTo(o.type);
        } else
            return changeDayToNum(this.type) - changeDayToNum(o.type);
    }

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
        else if (day.equalsIgnoreCase("sunday"))
            return 0;
        else
            return 7;
    }
}
