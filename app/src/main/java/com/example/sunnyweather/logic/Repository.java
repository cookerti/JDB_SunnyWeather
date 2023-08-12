package com.example.sunnyweather.logic;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.sunnyweather.logic.model.Place;
import com.example.sunnyweather.logic.model.PlaceResponse;
import com.example.sunnyweather.logic.network.SunnyWeatherNetWork;

import java.util.ArrayList;
import java.util.Collections;
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
}
