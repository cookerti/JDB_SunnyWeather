package com.example.sunnyweather;

import android.app.Application;
import android.content.Context;

public class SunnyWeatherApplication extends Application {
    //全局context
    public static Context mcontext;
    public static final String TOKEN = "HmqsUITH76XCPC8V";
    @Override
    public void onCreate() {
        super.onCreate();
        mcontext = getApplicationContext();
    }
}
