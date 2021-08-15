package com.company;
import java.util.Comparator;

public class Sports {
    private final String name;
    private final String sportsID;
    private int sportFees;
    private Schedule schedule;

    /*----------Class Constructor----------*/

    public Sports (String sportCenterCode, String[] details){
        name = details[0];
        sportsID = details[1];
        sportFees = Integer.parseInt(details[2]);
        schedule = null;
        String[] sessionIDs = FileServer.readFile(sportCenterCode,"Schedule.txt");
        for (String line : sessionIDs){
            String[] tokens = line.split("\\|");
            if (tokens[0].equals(name)) {
                schedule = new Schedule(sportCenterCode, name, tokens);
                break;
            }
        }
        // Create schedule with empty session, due to no session has been created yet
        if (schedule == null)
            schedule = new Schedule(name);
    }

    /*----------Sorting classes----------*/

    /*
    Class       : sortByFees (implements Comparator interface)
    Description : Paired with built in list sorting method to sort sports by fees
    */

    public static class sortByFees implements Comparator<Sports> {

        @Override
        public int compare(Sports o1, Sports o2) {
            return o1.sportFees - o2.sportFees;
        }

        @Override
        public String toString() {
            return "Sort by Sport Fees";
        }
    }

    /*
    Class       : sortByName  (implements Comparator interface)
    Description : Paired with built in list sorting method to sort sports by their name
    */
    public static class sortByName implements Comparator<Sports>{

        @Override
        public int compare(Sports o1, Sports o2) {
            return o1.name.compareTo(o2.name);
        }

        @Override
        public String toString(){
            return "Sort by Sport Name";
        }
    }

    @Override
    public String toString() {
        return name + "|" + sportsID + "|" + sportFees ;
    }

    /*----------Getters and Setters----------*/

    public Schedule getSchedule() {
        return schedule;
    }

    public String getSportsID (){return sportsID;}
    public String getName() {return name;}

    public int getSportFees() {
        return sportFees;
    }
    public void setSportFees(int sportFees){
        this.sportFees = sportFees;
    }

    /*
    Method      : getAllAttributes
    Description : Return an array of strings that represents all the attributes sports class has
    Parameter   : -
    Return      : An array of strings containing all the attributes name
    */
    public static String[] getAllAttributes () {
        return new String[]{"Name","Sports ID","Sport Fees"};
    }
}
