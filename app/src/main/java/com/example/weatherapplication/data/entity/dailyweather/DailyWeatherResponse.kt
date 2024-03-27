package com.example.weatherapplication.data.entity.dailyweather

data class DailyWeatherResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<DailyWeatherData>,
    val message: Int
)