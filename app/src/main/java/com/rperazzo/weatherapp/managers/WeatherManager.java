package com.rperazzo.weatherapp.managers;

import com.rperazzo.weatherapp.model.FindResult;
import com.rperazzo.weatherapp.model.ForecastResult;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class WeatherManager {

    private static final String API_URL =
            "http://api.openweathermap.org/data/2.5/";
    public static final String API_KEY =
            "520d6b47a12735bee8f69c57737d145f";

    public interface WeatherService {
        @GET("find?units=metric")
        Call<FindResult> find(
                @Query("q") String cityName,
                @Query("appid") String apiKey
        );

        @GET("group?units=metric")
        Call<FindResult> findById(
                @Query("id") String ids,
                @Query("appid") String apiKey
        );

        @GET("forecast?units=metric")
        Call<ForecastResult> forecast(
                @Query("id") String id,
                @Query("appid") String apiKey
        );
    }

    public static WeatherService getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(WeatherService.class);
    }
}
