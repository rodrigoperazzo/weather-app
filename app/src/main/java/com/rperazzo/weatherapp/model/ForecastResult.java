package com.rperazzo.weatherapp.model;

import com.rperazzo.weatherapp.utils.CalendarUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ForecastResult {
    public final List<ForecastItem> list;

    public ForecastResult(List<ForecastItem> list) {
        this.list = list;
    }

    public ForecastItem getNearestItem(int hour) {
        Calendar nowCal = Calendar.getInstance();

        ForecastItem nearest = null;
        int min = 25;

        for (ForecastItem item : list) {
            if (CalendarUtils.isSameDay(nowCal.getTime(),item.getDate())) {

                Calendar itemCal = Calendar.getInstance();
                itemCal.setTime(item.getDate());

                if(Math.abs(hour-itemCal.get(Calendar.HOUR_OF_DAY)) < min) {
                    min = Math.abs(hour-itemCal.get(Calendar.HOUR_OF_DAY));
                    nearest = item;
                }
            }
        }

        return nearest;
    }

    public ForecastItem[] getNext4Days() {

        ForecastItem[] fourDays = new ForecastItem[4];

        Calendar temp = Calendar.getInstance();
        temp.add(Calendar.DATE, 1);

        int day = 0;
        for (ForecastItem item : list) {
            if (CalendarUtils.isSameDay(temp.getTime(),item.getDate())) {
                fourDays[day++] = item;
                temp.add(Calendar.DATE, 1);

                if (day == 4) {
                    break;
                }
            }
        }

        return fourDays;
    }
}
