package com.example.sunnyweather.ui.place;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sunnyweather.MainActivity;
import com.example.sunnyweather.R;
import com.example.sunnyweather.SunnyWeatherApplication;
import com.example.sunnyweather.logic.model.Place;
import com.example.sunnyweather.ui.weather.WeatherActivity;
import com.example.sunnyweather.util.GlobalConstants;

import java.util.List;


public class PlaceFragment extends Fragment {

    public PlaceViewModel getViewModel() {
        return viewModel;
    }

    private PlaceViewModel viewModel;

    public PlaceFragment() {
    }

    public static PlaceFragment newInstance(String param1, String param2) {
        PlaceFragment fragment = new PlaceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(PlaceViewModel.class);
        if(getActivity()  instanceof MainActivity && viewModel.isPlacesaved()){
            Place place = viewModel.getSavedPlace();
            Intent intent = new Intent(getContext(), WeatherActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(GlobalConstants.CURRENT_LAT,place.getLocation().getLat());
            bundle.putString(GlobalConstants.CURRENT_LON,place.getLocation().getLng());
            bundle.putString(GlobalConstants.CURRENT_PLACE_NAME,place.getName());
            intent.putExtras(bundle);
            startActivity(intent);
            if(getActivity() != null)
                getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_place, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView iv_bg = view.findViewById(R.id.bgimageView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SunnyWeatherApplication.mcontext,LinearLayoutManager.VERTICAL,false);
        PlaceAdapter adapter = new PlaceAdapter(viewModel.placeList,this);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        EditText editText = view.findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString();
                Log.d("JDB","query"+query+"*");
                if(!query.equals("")){
                    iv_bg.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    viewModel.searchPlaces(query);
                }
                else{
                    iv_bg.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    viewModel.placeList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        viewModel.getList_LiveData().observe(getViewLifecycleOwner(), new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> places) {
                if(places != null){
                    recyclerView.setVisibility(View.VISIBLE);
                    iv_bg.setVisibility(View.GONE);
                    viewModel.placeList.clear();
                    viewModel.placeList.addAll(places);
                    adapter.notifyDataSetChanged();
                }
                else{
                    iv_bg.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"城市未查询到",Toast.LENGTH_SHORT).show();
                }

            }

        });
    }
}