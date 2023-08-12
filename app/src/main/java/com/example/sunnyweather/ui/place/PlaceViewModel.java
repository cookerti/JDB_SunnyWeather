package com.example.sunnyweather.ui.place;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.sunnyweather.logic.Repository;
import com.example.sunnyweather.logic.model.Place;

import java.util.ArrayList;
import java.util.List;

public class PlaceViewModel extends ViewModel {
    public List<Place> placeList = new ArrayList<>();
    //值改变时，会触发switchmap
    private MutableLiveData<String> stringMutableLiveData = new MutableLiveData<>();
    //switchMap返回值为LiveData
    private LiveData<List<Place>> listMutableLiveData = Transformations.switchMap(stringMutableLiveData, new Function<String, LiveData<List<Place>>>() {
        @Override
        public LiveData<List<Place>> apply(String input) {
            return Repository.searchPlaces(input);
        }
    });

    //外部调用
    public void searchPlaces(String query){
        stringMutableLiveData.setValue(query);
    }

    public LiveData<List<Place>> getList_LiveData(){
        return listMutableLiveData;
    }

}
