package com.example.smartdisasteralert.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("current") val current: CurrentWeather?
)

data class CurrentWeather(
    @SerializedName("temperature_2m") val temperature2m: Double?,
    @SerializedName("relative_humidity_2m") val relativeHumidity2m: Int?,
    @SerializedName("precipitation") val precipitation: Double?,
    @SerializedName("rain") val rain: Double?,
    @SerializedName("weather_code") val weatherCode: Int?,
    @SerializedName("wind_speed_10m") val windSpeed10m: Double?
)
