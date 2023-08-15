package com.example.sunnyweather.logic.network;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.sunnyweather.logic.Repository;
import com.example.sunnyweather.logic.model.DailyResponse;
import com.example.sunnyweather.logic.model.PlaceResponse;
import com.example.sunnyweather.logic.model.RealtimeResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SunnyWeatherNetWork {
    public static PlaceService placeService = ServiceCreator.create(PlaceService.class);
    public static WeatherService weatherService = ServiceCreator.create(WeatherService.class);
    public static MutableLiveData<PlaceResponse> searchPlaces(String query){
        final MutableLiveData<PlaceResponse> liveData = new MutableLiveData<>();
        placeService.searchPlaces(query).enqueue(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
                liveData.setValue(response.body());
                Log.d("JDB","onResponse");
                Log.d("JDB","status:"+response.body().getStatus());

            }

            @Override
            public void onFailure(Call<PlaceResponse> call, Throwable t) {
                Log.d("Error",t.toString());
            }
        });
        return liveData;
    }
    //获取realtime天气
    public static MutableLiveData<RealtimeResponse> getRealtimeWeather(String lng,String lat){
        MutableLiveData<RealtimeResponse> liveData = new MutableLiveData<>();

        weatherService.getRealtimeWeather(lng,lat).enqueue(new Callback<RealtimeResponse>() {
            @Override
            public void onResponse(Call<RealtimeResponse> call, Response<RealtimeResponse> response) {
                if(response == null)
                    Log.d("JDBNULL","realtime = null");
                Log.d("JDB","reallivechange");
                liveData.setValue(response.body());

            }

            @Override
            public void onFailure(Call<RealtimeResponse> call, Throwable t) {
                Log.d("JDBInreal",t.toString());
            }
        });
        return liveData;
    }
    //获取Daily天气
    public static MutableLiveData<DailyResponse> getDailyWeather(String lng,String lat){
        MutableLiveData<DailyResponse> liveData = new MutableLiveData<>();
        weatherService.getDailyWeather(lng,lat).enqueue(new Callback<DailyResponse>() {
            @Override
            public void onResponse(Call<DailyResponse> call, Response<DailyResponse> response) {
                Log.d("JDB","dailylivechange");
                if(response.body() == null)
                    Log.d("JDB","Danull");
                Log.d("JDB",response.body().toString());
                liveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<DailyResponse> call, Throwable t) {
                Log.d("JDBinresponse",t.toString());
            }
        });
        return liveData;
    }
}

