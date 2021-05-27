package com.company.GUI;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

class FormChecker {
    // Not sure where to put it
    public boolean onlyDigits(String str) {
        if (str.isEmpty()){
            return false;
        }
        for (int index = 0; index < str.length(); index++) {
            if (!Character.isDigit(str.charAt(index)))
                return false;
        }
        return true;
    }

    public boolean isDateObject(String str) {
        try {
            LocalDate.parse(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean isIntegerObject(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean isDay(String str) {
        ArrayList<String> listOfDays = new ArrayList<String>(Arrays.asList(
                "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"));
        return listOfDays.contains(str.toLowerCase());
    }

    public boolean isTime(String str) {
        if (str.length() > 5)
            return false;
        String[] tokens = str.split(":");
        if (tokens.length == 2) {
            try {
                LocalTime.of(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
                return true;
            } catch (Exception e) {
            }
        }
        return false;
    }

    public int isLogicalDuration(String startStr, String endStr) {
        String[] startTokens = startStr.split(":");
        String[] endTokens = endStr.split(":");
        try {
            LocalTime startTime = LocalTime.of(Integer.parseInt(startTokens[0]), Integer.parseInt(startTokens[1]));
            LocalTime endTime = LocalTime.of(Integer.parseInt(endTokens[0]), Integer.parseInt(endTokens[1]));
            if (startTime.isBefore(endTime))
                return 0;
            else
                return 1;
        } catch (Exception e) {
            return 2;
        }
    }
}
