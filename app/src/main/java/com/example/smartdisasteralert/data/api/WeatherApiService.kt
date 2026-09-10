package com.example.smartdisasteralert.data.api

import com.example.smartdisasteralert.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getWeatherForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,precipitation,rain,weather_code,wind_speed_10m"
    ): WeatherResponse
}
