package com.company;


import java.util.Comparator;

public class Sports {
    private final String name;
    private final String sportsID;
    private int sportFees;
    private Schedule schedule;
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
        if (schedule == null)
            schedule = new Schedule(name);
    }
    public static String[] getAllAttributes () {
        return new String[]{"Name","Sports ID","Sport Fees"};
    }
    @Override
    public String toString() {
        return name + "|" + sportsID + "|" + sportFees ;
    }

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
}
