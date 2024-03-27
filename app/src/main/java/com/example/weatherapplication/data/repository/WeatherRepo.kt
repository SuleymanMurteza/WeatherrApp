package com.example.weatherapplication.data.repository

import com.example.weatherapplication.data.entity.dailyweather.DailyWeatherData
import com.example.weatherapplication.data.entity.dailyweather.DailyWeatherResponse
import com.example.weatherapplication.data.entity.weather.WeatherResponse
import com.example.weatherapplication.data.retrofit.RetrofitInstance

class WeatherRepo() {
    var apiService=RetrofitInstance.apiService


    suspend fun getWeather(city:String) : WeatherResponse {
        return apiService.getWeather(city)
    }
    suspend fun getDailyWeather(city:String) : List<DailyWeatherData>{
        return apiService.getDailyWeather(city).list
    }


}