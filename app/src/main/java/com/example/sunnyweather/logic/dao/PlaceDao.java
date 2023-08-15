package com.example.sunnyweather.logic.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sunnyweather.SunnyWeatherApplication;
import com.example.sunnyweather.logic.model.Place;
import com.example.sunnyweather.util.GlobalConstants;
import com.google.gson.Gson;

public class PlaceDao {
    public static SharedPreferences sharedPreferences(){
        SharedPreferences sharedPreferences = SunnyWeatherApplication.mcontext.getSharedPreferences("sunny_weather", Context.MODE_PRIVATE);
        return sharedPreferences;
    }
    public static void savedPlace(Place place){
        sharedPreferences().edit().putString(GlobalConstants.SELECTED_PlACE, new Gson().toJson(place)).apply();
    }
    public static Place getSavedPlace(){
        String place_string = sharedPreferences().getString(GlobalConstants.SELECTED_PlACE,"NULL");
        Place place = new Gson().fromJson(place_string, Place.class);
        return place;
    }
    public static boolean isSavedPlace(){
        return sharedPreferences().contains(GlobalConstants.SELECTED_PlACE);
    }
}
