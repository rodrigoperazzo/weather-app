package com.rperazzo.weatherapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rperazzo.weatherapp.R;
import com.rperazzo.weatherapp.managers.WeatherManager;
import com.rperazzo.weatherapp.model.City;
import com.rperazzo.weatherapp.model.ForecastItem;
import com.rperazzo.weatherapp.model.ForecastResult;
import com.rperazzo.weatherapp.utils.CalendarUtils;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private ProgressBar mLoading;
    private TextView mTitle;
    private TextView mSubtitle;

    private TextView mCurrentTemp;
    private TextView mCurrentWind;
    private TextView mCurrentClouds;
    private TextView mCurrentPressure;
    private ImageView mCurrentIcon;
    private SeekBar mSeekBar;

    private TextView[] mDay = new TextView[4];
    private TextView[] mMax = new TextView[4];
    private TextView[] mMin = new TextView[4];
    private ImageView[] mIcon = new ImageView[4];

    private City mCity;
    private ForecastResult mForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mLoading = (ProgressBar) findViewById(R.id.loading);
        mTitle = (TextView) findViewById(R.id.weatherTitle);
        mSubtitle = (TextView) findViewById(R.id.weatherSubtitle);
        mCurrentTemp = (TextView) findViewById(R.id.currentTemp);
        mCurrentWind = (TextView) findViewById(R.id.currentWind);
        mCurrentClouds = (TextView) findViewById(R.id.currentClouds);
        mCurrentPressure = (TextView) findViewById(R.id.currentPressure);
        mCurrentIcon = (ImageView) findViewById(R.id.currentWeatherIcon);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSeekBar.setMax(24);
        mSeekBar.setOnSeekBarChangeListener(this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                // basta lê o parâmetro progress
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        for (int day = 1; day <= 4; day++) {
            mDay[day-1] = (TextView) findViewById(
                    getResources().getIdentifier("day"+day,"id",getPackageName()));
            mMax[day-1] = (TextView) findViewById(
                    getResources().getIdentifier("max"+day,"id",getPackageName()));
            mMin[day-1] = (TextView) findViewById(
                    getResources().getIdentifier("min"+day,"id",getPackageName()));
            mIcon[day-1] = (ImageView) findViewById(
                    getResources().getIdentifier("weatherIcon"+day,"id",getPackageName()));
        }

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            mCity = (City) intent.getSerializableExtra("city");
            mForecast = null;
            updateViews();
            requestForecast();
        } else if (mCity != null && mForecast != null) {
            updateViews();
            loadForecast();
        }
    }

    private void updateViews() {
        mLoading.setVisibility(View.GONE);

        mTitle.setText("Weather in " + mCity.getTitle());

        Calendar now = Calendar.getInstance();
        StringBuilder builder = new StringBuilder();
        builder.append(CalendarUtils.getDayOfWeek(now));
        builder.append(", ");
        builder.append(now.get(Calendar.HOUR_OF_DAY));
        builder.append(":");
        builder.append(now.get(Calendar.MINUTE));
        builder.append(", ");
        builder.append(mCity.getDescription());
        mSubtitle.setText(builder.toString());

        mSeekBar.setProgress(now.get(Calendar.HOUR_OF_DAY));
    }

    private void requestForecast() {
        mLoading.setVisibility(View.VISIBLE);
        WeatherManager.WeatherService wService = WeatherManager.getService();
        final Call<ForecastResult> forecastCall = wService.forecast(String.valueOf(mCity.id), WeatherManager.API_KEY);
        forecastCall.enqueue(new Callback<ForecastResult>() {
            @Override
            public void onResponse(Call<ForecastResult> call, Response<ForecastResult> response) {
                mForecast = response.body();
                mLoading.setVisibility(View.GONE);
                loadForecast();
            }

            @Override
            public void onFailure(Call<ForecastResult> call, Throwable t) {
                mLoading.setVisibility(View.GONE);
            }
        });
    }

    private void loadForecast() {
        updateCurrentInfo(mSeekBar.getProgress());

        ForecastItem[] fourDays = mForecast.getNext4Days();
        for (int day = 0; day < 4; day++) {
            mDay[day].setText(fourDays[day].getDayOfWeek());
            mMax[day].setText(fourDays[day].getMaxTemperature());
            mMin[day].setText(fourDays[day].getMinTemperature());
            mIcon[day].setImageResource(fourDays[day].getIconResource(this));
        }
    }

    private void updateCurrentInfo(int hour) {
        if (mForecast != null) {
            ForecastItem current = mForecast.getNearestItem(hour);
            mCurrentIcon.setImageResource(current.getIconResource(this));
            mCurrentTemp.setText(current.getTemperature());
            mCurrentWind.setText(current.getWind());
            mCurrentClouds.setText(current.getClouds());
            mCurrentPressure.setText(current.getPressure());
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        updateCurrentInfo(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // nothing to do
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // nothing to do
    }
}
