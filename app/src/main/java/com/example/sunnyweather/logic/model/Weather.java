package com.example.sunnyweather.logic.model;

public class Weather {
    private RealtimeResponse.Realtime realtime = null;
    private DailyResponse.Daily daily = null;

    public RealtimeResponse.Realtime getRealtime() {
        return realtime;
    }

    public void setRealtime(RealtimeResponse.Realtime realtime) {
        this.realtime = realtime;
    }

    public DailyResponse.Daily getDaily() {
        return daily;
    }

    public void setDaily(DailyResponse.Daily daily) {
        this.daily = daily;
    }
}
