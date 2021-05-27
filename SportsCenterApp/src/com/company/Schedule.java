package com.company;
import java.util.*;

public class Schedule implements Comparable<Schedule> {
    private ArrayList<Session> sessionList = new ArrayList<>();
    private String type = "";

    /* Method : updateScheduleFile
       Description : Update changes made to session.txt to schedule.txt
                     (Schedule.txt is derived from session.txt)

       Parameter : sportCenterCode (String) -- indicates which sport center schedule to update
       Return    : 0 for update successful
                   1 for update failed
     */
    public static int updateScheduleFile (String sportCenterCode) {
        // Use hash map structure to store schedules mapped to their type (day / sports)
        HashMap<String,Schedule> scheduleHashMap = new HashMap<>();
        String[] scheduleFile = FileServer.readFile(sportCenterCode,"Schedule.txt");

        // Only create schedule instances for sunday - saturday (first 7 lines)
        for (int line = 0;line < 7;line++){
            String[] tokens = scheduleFile[line].split("\\|");
            scheduleHashMap.put(tokens[0], new Schedule(tokens[0]));
        }

        // Initiate all sessions from session.txt & add them into respective schedule (day & sports)
        String[] sessionFile = FileServer.readFile(sportCenterCode,"Session.txt");
        for (String line:sessionFile){
            String[] tokens = line.split("\\|");
            Session sessionToAdd = new Session(tokens);
            // Check for whether the day schedule is present in hashmap, if not create a new one & add session
            if (scheduleHashMap.get(tokens[0]) == null)
                scheduleHashMap.put(tokens[0], new Schedule(tokens[0]));

            scheduleHashMap.get(tokens[0]).addSession(sessionToAdd);

            // Check for whether the sports schedule is present in hashmap, if not create a new one & add session
            if (scheduleHashMap.get(tokens[6]) == null)
                scheduleHashMap.put(tokens[6], new Schedule(tokens[6]));

            scheduleHashMap.get(tokens[6]).addSession(sessionToAdd);
        }
        // Clear schedule.txt
        if (FileServer.writeFile(sportCenterCode,"Schedule.txt","") == 1){return 1;}

        // Sort schedule from days first then sports (alphabetical)
        ArrayList<Schedule> scheduleArrayList = new ArrayList<>(scheduleHashMap.values());
        Collections.sort(scheduleArrayList);

        // Write all schedule to schedule.txt
        for (Schedule schedule : scheduleArrayList){
            if (FileServer.appendFile(sportCenterCode,"Schedule.txt",schedule.getWriteToFileString()) == 1)
                return 1;
        }
        return 0;
    }

    /*----------Class Constructor----------*/

    /*
    Constructor : Schedule
    Description : Create a schedule with sessions
    Parameter   : sportsCentercode (String) , sessionID (String Array)
    Return      : -
 */

    public Schedule (String sportsCenterCode, String type, String[] sessionID){
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
        this.type = type;
    }

    // Overloaded constructor that creates a schedule with empty session list
    public Schedule(String type){
        this.type = type;
    }

    /*
    Method      : getWriteToFileString
    Description : Get the string representation of this schedule to write to file
    Parameter   : -
    Return      : A string representation of this schedule
 */

    public String getWriteToFileString () {
        StringBuilder writeThis = new StringBuilder(type);

        for (Session session : sessionList){
            writeThis.append("|").append(session.getSessionID());
        }
        writeThis.append("|").append("\n");
        return writeThis.toString();
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

    public void addSession(Session newSession) {
        sessionList.add(newSession);
    }

    public Session getSession(int index) {
        return sessionList.get(index);
    }

    public static String[] getAllAttributes () {
        return Session.getAllAttributes();
    }

    public ArrayList<Session> getAllSession() {
        return sessionList;
    }

    public String toString() {
        return "Show schedule for " + type;
    }

    /*
        Method : compareTo
        Description : Method implemented from Comparable interface that helps to sort schedule objects
                      Schedule is sorted by type where days always come first, then sports
     */
    @Override
    public int compareTo(Schedule o) {
        if (changeDayToNum(this.type) == changeDayToNum(o.type)){
            return this.type.compareTo(o.type);
        } else
            return changeDayToNum(this.type) - changeDayToNum(o.type);
    }

    /*
        Method : changeDayToNum
        Description : Assign a number to each day to facilitate the sorting method based on days
        Parameter : day (String)
        Return    : Integer (represents the day Sunday == 1, etc...)
     */
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
