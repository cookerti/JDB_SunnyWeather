package com.example.sunnyweather.ui.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.sunnyweather.R;
import com.example.sunnyweather.logic.model.DailyResponse;
import com.example.sunnyweather.logic.model.Location;
import com.example.sunnyweather.logic.model.RealtimeResponse;
import com.example.sunnyweather.logic.model.Sky;
import com.example.sunnyweather.logic.model.Weather;
import com.example.sunnyweather.util.GlobalConstants;
import com.example.sunnyweather.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {

    public WeatherModel weatherModel;
    private TextView tv_placeName;
    private TextView tv_currentTemp;
    private TextView tv_currentSky;
    private TextView tv_currentAQI;
    private ConstraintLayout nowLayout;
    private LinearLayout forecastLayout;
    private TextView tv_coldRisk;
    private TextView tv_dressing;
    private TextView tv_ultraviolet;
    private TextView tv_carWashing;
    private ScrollView weatherLayout;
    private SwipeRefreshLayout swipeRefresh;
    private Button navbtn;
    public DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initView();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        weatherModel = new ViewModelProvider(this).get(WeatherModel.class);
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            Location location = new Location(bundle.getString(GlobalConstants.CURRENT_LON),bundle.getString(GlobalConstants.CURRENT_LAT));
            Log.d("JDB",location.toString());
            weatherModel.setLocation(location);
            weatherModel.setPlaceName(bundle.getString(GlobalConstants.CURRENT_PLACE_NAME));
            Log.d("JDB","Name"+bundle.getString(GlobalConstants.CURRENT_PLACE_NAME));
            weatherModel.refreshWeather();
        }

        weatherModel.getWeatherLiveData().observe(this, new Observer<Weather>() {
            @Override
            public void onChanged(Weather weather) {
                Log.d("JDB","ONCHANGE");
                if(weather != null){
                    showWeatherInfo(weather);
                }
                else{
                    ToastUtil.Show("该地区天气情况无法获取！");
                }
                swipeRefresh.setRefreshing(false);//天气情况更新后，刷新状态结束
            }
        });
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //地点不变的刷新
                refreshWeather();
            }
        });

    }
    public void refreshWeather(){
        weatherModel.refreshWeather();
        swipeRefresh.setRefreshing(true);
    }

    private void showWeatherInfo(Weather weather) {
        RealtimeResponse.Realtime realtime = weather.getRealtime();
        DailyResponse.Daily daily = weather.getDaily();
        //填充now.xml数据
        tv_placeName.setText(weatherModel.getPlaceName());
        tv_placeName.bringToFront();
        tv_currentTemp.setText(realtime.getTemperature()+"℃");
        tv_currentSky.setText(Sky.getSky(realtime.getSkycon()).getInfo());
        tv_currentAQI.setText("空气指数 "+realtime.getAirQuality().getAqi().getChn());
        nowLayout.setBackgroundResource(Sky.getSky(realtime.getSkycon()).getBg());

        //填充forecast.xml数据

        forecastLayout.removeAllViews();
        int i = 0;
        for (DailyResponse.Skycon skycon : daily.getSkycon()) {
            String tempText = "";
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateInfo = view.findViewById(R.id.dateInfo);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            dateInfo.setText(simpleDateFormat.format(skycon.getDate()));

            TextView temperature = view.findViewById(R.id.temperatureInfo);
            tempText = (int)(daily.getTemperature().get(i).getMax()) + " ~ " + (int)(daily.getTemperature().get(i).getMin()) + "℃";
            temperature.setText(tempText);

            TextView skyInfo = view.findViewById(R.id.skyInfo);
            skyInfo.setText(Sky.getSky(skycon.getValue()).getInfo());

            ImageView skyIcon = view.findViewById(R.id.skyIcon);
            skyIcon.setImageResource(Sky.getSky(skycon.getValue()).getIcon());

            forecastLayout.addView(view);

            i++;

        }

        //填充life_index.xml数据
        DailyResponse.LifeIndex lifeIndex = daily.getLife_index();
        tv_coldRisk.setText(lifeIndex.getColdRisk().get(0).getDesc());
        tv_dressing.setText(lifeIndex.getDressing().get(0).getDesc());
        tv_ultraviolet.setText(lifeIndex.getUltraviolet().get(0).getDesc());
        tv_carWashing.setText(lifeIndex.getCarWashing().get(0).getDesc());

        weatherLayout.setVisibility(View.VISIBLE);
    }

    private void initView() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navbtn = findViewById(R.id.navBtn);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        tv_placeName = findViewById(R.id.placeName);
        tv_currentTemp = findViewById(R.id.currentTemp);
        tv_currentSky = findViewById(R.id.currentSky);
        tv_currentAQI = findViewById(R.id.currentAQI);
        nowLayout = findViewById(R.id.nowLayout);
        forecastLayout = findViewById(R.id.forecastLayout);
        tv_coldRisk = findViewById(R.id.coldRiskText);
        tv_dressing = findViewById(R.id.dressingText);
        tv_ultraviolet = findViewById(R.id.ultravioletText);
        tv_carWashing = findViewById(R.id.carWashingText);
        weatherLayout = findViewById(R.id.weatherLayout);
        Sky.InitSkyMap();

        navbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                 InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                 inputMethodManager.hideSoftInputFromWindow(drawerLayout.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }
}