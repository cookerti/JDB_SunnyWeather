package com.example.sunnyweather.util;

import android.content.Context;
import android.widget.Toast;

import com.example.sunnyweather.SunnyWeatherApplication;

public class ToastUtil {
    public static void Show(String desc){
        Toast.makeText(SunnyWeatherApplication.mcontext,desc,Toast.LENGTH_SHORT).show();
    }
}
