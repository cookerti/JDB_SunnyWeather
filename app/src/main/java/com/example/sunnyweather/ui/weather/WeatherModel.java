package com.example.sunnyweather.ui.weather;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.sunnyweather.logic.Repository;
import com.example.sunnyweather.logic.model.Location;
import com.example.sunnyweather.logic.model.RealtimeResponse;
import com.example.sunnyweather.logic.model.Weather;

public class WeatherModel extends ViewModel {
    private MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private Location location;
    private String placeName;

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    private LiveData<Weather> weatherLiveData = Transformations.switchMap(locationMutableLiveData, new Function<Location, LiveData<Weather>>() {
        @Override
        public LiveData<Weather> apply(Location input) {
            return Repository.getWeather(input.getLng(),input.getLat());
        }
    });
    public void refreshWeather(){
        locationMutableLiveData.setValue(location);
    }
    public void setLocation(Location nowlocation){
        location = nowlocation;
    }
    public LiveData<Weather> getWeatherLiveData(){
        return weatherLiveData;
    }
}
