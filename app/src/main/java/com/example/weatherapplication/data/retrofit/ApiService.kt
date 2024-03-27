package com.example.weatherapplication.data.retrofit

import com.example.weatherapplication.data.entity.dailyweather.DailyWeatherResponse
import com.example.weatherapplication.data.entity.weather.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("data/2.5/weather?&appid=a017efd8cf053cae5f282bed82cd5121")
    suspend fun getWeather(
        @Query("q")cityname:String
    ): WeatherResponse

    @GET("data/2.5/forecast?&appid=a017efd8cf053cae5f282bed82cd5121")
    suspend fun getDailyWeather(
        @Query("q")cityname:String
    ): DailyWeatherResponse


}