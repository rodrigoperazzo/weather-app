package com.rperazzo.weatherapp.model;

import android.content.Context;

import com.rperazzo.weatherapp.utils.CalendarUtils;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ForecastItem {

    public long dt;
    public Main main;
    public List<Weather> weather;
    public Clouds clouds;
    public Wind wind;

    public class Main {
        public double temp;
        public double temp_min;
        public double temp_max;
        public double pressure;
    }

    public class Weather {
        public String description;
        public String icon;
    }

    public class Clouds {
        public int all;
    }

    public class Wind {
        public double speed;
    }

    public String getPressure() {
        return "pressure " + new DecimalFormat("#").format(this.main.pressure) + " hpa";
    }

    public String getWind() {
        return "wind " + new DecimalFormat("#.#").format(this.wind.speed) + " m/s";
    }

    public String getClouds() {
        return "clouds " + this.clouds.all + "%";
    }

    public String getTemperature() {
        return new DecimalFormat("#").format(this.main.temp);
    }

    public String getMaxTemperature() {
        return new DecimalFormat("#").format(this.main.temp_max);
    }

    public String getMinTemperature() {
        return new DecimalFormat("#").format(this.main.temp_min);
    }

    public Date getDate() {
        return new Date(dt*1000L);
    }

    public String getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate());
        return CalendarUtils.getDayOfWeek(calendar);
    }

    public int getIconResource(Context context) {
        return context.getResources().getIdentifier(
                "w_"+this.weather.get(0).icon,
                "drawable",
                context.getPackageName());
    }

}
