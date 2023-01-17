package com.application.MindSet.utils;

import android.util.Log;

import java.util.Date;
import java.util.Arrays;
import java.util.Calendar;

public class Utils {

    private static final int REQUIRED_TIME = 15;

    public static boolean isGameIn(Date gameDate, int timeNeededToMove) {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, timeNeededToMove + REQUIRED_TIME);
        Date d = new Date(c.getTimeInMillis());
        return d.after(gameDate);
    }

    public static int getDuration(String second) {
        //X mins || X hours and Y mins
        if(second.contains("hours")) {
            String[] duration = second.split("mins|hours");
            for (int i = 0; i <duration.length; i++) {
                duration[i] = duration[i].replaceAll("\\s", "");
            }

            int result = Integer.parseInt(duration[0]) * 60 + Integer.parseInt(duration[1]);

            Log.i("LocationUpdate", "Duration: " + Arrays.toString(duration));
            Log.i("LocationUpdate", "Duration: " + result);
            return result;
        } else {
            String[] duration = second.split("mins");
            for (int i = 0; i <duration.length; i++) {
                duration[i] = duration[i].replaceAll("\\s", "");
            }

            int result = Integer.parseInt(duration[0]);

            return result;
        }
    }
}
