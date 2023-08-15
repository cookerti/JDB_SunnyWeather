package com.example.sunnyweather.logic.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceCreator {
    public static final String BASE_URL = "https://api.caiyunapp.com/";
    private static Retrofit mretrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public static <T> T  create(Class<T> serviceClass ){
        return mretrofit.create(serviceClass);
    }
}
