package com.rperazzo.weatherapp.managers;

import com.rperazzo.weatherapp.model.City;
import com.rperazzo.weatherapp.model.CityRealm;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmManager {

    public static void saveCity(City city) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        CityRealm cityRealm = realm.createObject(CityRealm.class);
        cityRealm.setId(city.id);
        cityRealm.setName(city.name);
        realm.commitTransaction();
    }

    public static void deleteCity(City city) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CityRealm> cities = findCityById(city.id);
        if (cities.size() > 0) {
            CityRealm cityRealm = cities.get(0);

            realm.beginTransaction();
            cityRealm.deleteFromRealm();
            realm.commitTransaction();
        }

    }

    public static boolean isCitySaved(City city) {
        RealmResults<CityRealm> cities = findCityById(city.id);
        return cities.size() > 0;
    }

    public static RealmResults<CityRealm> findAllCities() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(CityRealm.class).findAll();
    }

    private static RealmResults<CityRealm> findCityById(long id) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(CityRealm.class).equalTo("id", id).findAll();
    }
}
