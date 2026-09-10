package com.example.smartdisasteralert.data.api

import com.example.smartdisasteralert.data.model.UsgsResponse
import retrofit2.http.GET

interface UsgsApiService {
    @GET("earthquakes/feed/v1.0/summary/all_day.geojson")
    suspend fun getRecentEarthquakes(): UsgsResponse
}
