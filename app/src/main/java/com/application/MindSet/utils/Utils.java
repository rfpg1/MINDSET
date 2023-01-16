package com.application.MindSet.utils;

import java.sql.Date;
import java.util.Calendar;

public class Utils {

    private static final int REQUIRED_TIME = 15;

    public static boolean isGameIn(Date gameDate, int timeNeededToMove) {

        Calendar c = Calendar.getInstance();
        Date sqlDate = new Date(c.getTimeInMillis() + (timeNeededToMove + REQUIRED_TIME) * 1000);

        return sqlDate.after(gameDate);
    }
}
