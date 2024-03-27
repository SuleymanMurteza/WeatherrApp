package com.example.weatherapplication.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.data.entity.dailyweather.DailyWeatherData
import com.example.weatherapplication.data.entity.dailyweather.DailyWeatherResponse
import com.example.weatherapplication.data.entity.weather.WeatherResponse
import com.example.weatherapplication.data.repository.WeatherRepo
import kotlinx.coroutines.launch

class WeatherViewModel() :ViewModel() {

    var repo=WeatherRepo()
    val response = MutableLiveData<WeatherResponse>()
    val dailyResponse = MutableLiveData<List<DailyWeatherData>>()



    fun getWeather(city:String) {
        try {
    viewModelScope.launch {
        val result = repo.getWeather(city)
        response.postValue(result)
    }

        }catch (e:Exception){

}
    }
    fun getDailyWeather(city:String){
       try {
           viewModelScope.launch {
               val result = repo.getDailyWeather(city)
               dailyResponse.postValue(result)
           }
       }catch (e:Exception){


       }
       }
}