package com.rperazzo.weatherapp.utils;

import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {

    private static final String daysArray[] = {"Sun","Mon","Tue", "Wed","Thu","Fri", "Sat"};
    public static String getDayOfWeek(Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return daysArray[day];
    }

    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}
