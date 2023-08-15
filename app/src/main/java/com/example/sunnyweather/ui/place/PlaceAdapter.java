package com.example.sunnyweather.ui.place;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunnyweather.R;
import com.example.sunnyweather.SunnyWeatherApplication;
import com.example.sunnyweather.logic.model.Place;
import com.example.sunnyweather.ui.weather.WeatherActivity;
import com.example.sunnyweather.ui.weather.WeatherModel;
import com.example.sunnyweather.util.GlobalConstants;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder> {
    private List<Place> list;
    private PlaceFragment fragment;
    public PlaceAdapter(List<Place> list, PlaceFragment fragment) {
        this.list = list;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_address.setText(list.get(position).getAddress());
        holder.tv_name.setText(list.get(position).getName());
        holder.setCurPosition(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("JDB","onclick");
                int pos = holder.getCurPosition();
                Place place = list.get(pos);
                if(fragment.getActivity() instanceof WeatherActivity){
                    ((WeatherActivity) fragment.getActivity()).drawerLayout.closeDrawers();
                   WeatherModel viewModel = ((WeatherActivity) fragment.getActivity()).weatherModel;
                   viewModel.setLocation(place.getLocation());
                   viewModel.setPlaceName(place.getName());
                    ((WeatherActivity) fragment.getActivity()).refreshWeather();
                }
                else{
                    Intent intent = new Intent(SunnyWeatherApplication.mcontext, WeatherActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(GlobalConstants.CURRENT_LAT,list.get(pos).getLocation().getLat());
                    bundle.putString(GlobalConstants.CURRENT_LON,list.get(pos).getLocation().getLng());
                    bundle.putString(GlobalConstants.CURRENT_PLACE_NAME,list.get(pos).getName());
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    SunnyWeatherApplication.mcontext.startActivity(intent);
                }
                //不论是跳转还是更新城市，都需要保存
                fragment.getViewModel().savePlace(list.get(pos));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView tv_name;
        private final TextView tv_address;

        public int getCurPosition() {
            return CurPosition;
        }

        public void setCurPosition(int curPosition) {
            CurPosition = curPosition;
        }

        private int CurPosition;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.placename);
            tv_address = itemView.findViewById(R.id.placeAddress);

        }
    }
}
