package com.rperazzo.weatherapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rperazzo.weatherapp.R;
import com.rperazzo.weatherapp.managers.RealmManager;
import com.rperazzo.weatherapp.model.City;

import java.util.ArrayList;

public class FindItemAdapter extends ArrayAdapter<City> {

    public FindItemAdapter(Context context, ArrayList<City> cities) {
        super(context, 0, cities);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final City city = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.city_list_item, parent, false);
        }
        TextView cityName = convertView.findViewById(R.id.cityNameTxt);
        cityName.setText(city.getTitle());

        TextView description = convertView.findViewById(R.id.descriptionTxt);
        description.setText(city.getDescription());

        TextView metric = convertView.findViewById(R.id.metricTxt);
        metric.setText("ºC");

        TextView temp = convertView.findViewById(R.id.tempTxt);
        temp.setText(city.getTemperature());

        TextView wind = convertView.findViewById(R.id.windTxt);
        wind.setText(city.getWind());

        TextView clouds = convertView.findViewById(R.id.cloudsTxt);
        clouds.setText(city.getClouds());

        TextView pressure = convertView.findViewById(R.id.pressureTxt);
        pressure.setText(city.getPressure());

        int resId = getContext().getResources().getIdentifier(
                "w_"+city.weather.get(0).icon,  // nome do resource
                "drawable",                     // tipo do resource
                getContext().getPackageName()); // pacote do resource

        ImageView icon = convertView.findViewById(R.id.weatherIcon);
        icon.setImageResource(resId);

        final ImageView addIcon = convertView.findViewById(R.id.addIcon);
        updateCityStateIcon(city, addIcon);
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCityState(city);
                updateCityStateIcon(city, addIcon);
            }
        });

        return convertView;
    }

    private void updateCityState(City city) {
        if(RealmManager.isCitySaved(city)){
            RealmManager.deleteCity(city);
        } else {
            RealmManager.saveCity(city);
        }
    }

    private void updateCityStateIcon(City city, ImageView icon) {
        if(RealmManager.isCitySaved(city)){
            icon.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            icon.setImageResource(android.R.drawable.btn_star);
        }
    }
}
