package com.example.sunnyweather.logic.network;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.sunnyweather.logic.Repository;
import com.example.sunnyweather.logic.model.PlaceResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SunnyWeatherNetWork {
    public static PlaceService placeService = ServiceCreator.create(PlaceService.class);
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
}
