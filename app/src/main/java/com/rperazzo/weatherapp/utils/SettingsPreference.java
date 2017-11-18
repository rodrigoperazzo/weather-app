package com.rperazzo.weatherapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsPreference {
    private static final String PREFERENCE_NAME = "com.rperazzo.weatherapp.shared";
    private static final String TEMPERATURE_UNIT_KEY = "TEMPERATURE_UNIT_KEY";
    private static final String LANGUAGE_KEY = "LANGUAGE_KEY";

    private SharedPreferences mSharedPref;

    public SettingsPreference(Context context) {
        mSharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void setTemperatureUnit(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(TEMPERATURE_UNIT_KEY, value);
        editor.apply();
    }

    public String getTemperatureUnit() {
        return mSharedPref.getString(TEMPERATURE_UNIT_KEY, "metric");
    }

    public void setLanguage(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(LANGUAGE_KEY, value);
        editor.apply();
    }

    public String getLanguage() {
        return mSharedPref.getString(LANGUAGE_KEY, "en");
    }
}

