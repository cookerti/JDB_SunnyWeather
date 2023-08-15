package com.example.sunnyweather.logic;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.sunnyweather.SunnyWeatherApplication;
import com.example.sunnyweather.logic.dao.PlaceDao;
import com.example.sunnyweather.logic.model.DailyResponse;
import com.example.sunnyweather.logic.model.Place;
import com.example.sunnyweather.logic.model.PlaceResponse;
import com.example.sunnyweather.logic.model.RealtimeResponse;
import com.example.sunnyweather.logic.model.Weather;
import com.example.sunnyweather.logic.network.SunnyWeatherNetWork;
import com.example.sunnyweather.logic.network.WeatherService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Repository {
    //网络渠道请求数据
    public static LiveData<List<Place>> searchPlaces(String query){

        return Transformations.switchMap(SunnyWeatherNetWork.searchPlaces(query), new Function<PlaceResponse, LiveData<List<Place>>>() {
            @Override
            public LiveData<List<Place>> apply(PlaceResponse input) {
                MutableLiveData<List<Place>> mutableLiveData = new MutableLiveData<>();
                List<Place> list = new ArrayList<>();
                if(input != null && input.getStatus().equals("ok")){
                    list = input.getPlaces();
                    mutableLiveData.setValue(list);
                    Log.d("JDB","onRe");
                }
                else{
                    Log.d("JDB","仓库层网络请求失败");
                }
                return mutableLiveData;
            }
        });
    }
    //网络渠道获取天气
    public static LiveData<Weather> getWeather(String lng,String lat){
        MediatorLiveData<Weather> mediatorLiveData = new MediatorLiveData<>();
        Weather weather = new Weather();
        mediatorLiveData.addSource(SunnyWeatherNetWork.getRealtimeWeather(lng, lat), new Observer<RealtimeResponse>() {
            @Override
            public void onChanged(RealtimeResponse realtimeResponse) {
                if(realtimeResponse == null)
                    Log.d("JDB","realnull_inRepository");
                weather.setRealtime(realtimeResponse.getResult().getRealtime());
                if(weather.getDaily()!=null){
                    mediatorLiveData.setValue(weather);
                }
            }
        });
        mediatorLiveData.addSource(SunnyWeatherNetWork.getDailyWeather(lng, lat), new Observer<DailyResponse>() {
            @Override
            public void onChanged(DailyResponse dailyResponse) {
                if(dailyResponse == null)
                    Log.d("JDB","null_inRepository");
                weather.setDaily(dailyResponse.getResult().getDaily());
                if(weather.getRealtime()!=null){
                    mediatorLiveData.setValue(weather);
                }
            }
        });

        return mediatorLiveData;
    }

    public static void savePlace(Place place){
        PlaceDao.savedPlace(place);
    }
    public static Place getSavedplace(){
        return PlaceDao.getSavedPlace();
    }
    public static boolean isPlaceSaved(){
        return PlaceDao.isSavedPlace();
    }

}
