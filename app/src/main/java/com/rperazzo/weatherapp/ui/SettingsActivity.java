package com.rperazzo.weatherapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.rperazzo.weatherapp.R;
import com.rperazzo.weatherapp.utils.SettingsPreference;

public class SettingsActivity extends AppCompatActivity {

    SettingsPreference settingsPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsPreference = new SettingsPreference(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        RadioButton englishButton = (RadioButton) findViewById(R.id.englishButton);
        RadioButton portuguesButton = (RadioButton) findViewById(R.id.portuguesButton);

        englishButton.setChecked(settingsPreference.getLanguage().equals("en"));
        portuguesButton.setChecked(settingsPreference.getLanguage().equals("pt-br"));

        englishButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) settingsPreference.setLanguage("en");
            }
        });

        portuguesButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) settingsPreference.setLanguage("pt-br");
            }
        });

        RadioButton fahrenheitButton = (RadioButton) findViewById(R.id.celsiusButton);
        RadioButton celsiusButton= (RadioButton) findViewById(R.id.fahrenheitButton);

        celsiusButton.setChecked(settingsPreference.getTemperatureUnit().equals("metric"));
        fahrenheitButton.setChecked(settingsPreference.getTemperatureUnit().equals("imperial"));

        celsiusButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) settingsPreference.setTemperatureUnit("metric");
            }
        });
        fahrenheitButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) settingsPreference.setTemperatureUnit("imperial");
            }
        });
    }
}
