package com.example.smartdisasteralert.data.repository

import com.example.smartdisasteralert.data.api.ApiClient
import com.example.smartdisasteralert.data.local.MockDisasterDataSource
import com.example.smartdisasteralert.data.model.DisasterAlert
import com.example.smartdisasteralert.data.model.DisasterType
import com.example.smartdisasteralert.data.model.SeverityLevel
import com.example.smartdisasteralert.data.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

class DisasterRepository {

    suspend fun getDisasterAlerts(userLat: Double, userLon: Double): List<DisasterAlert> = withContext(Dispatchers.IO) {
        val resultList = mutableListOf<DisasterAlert>()

        // 1. Instantly load reliable multi-hazard disaster alerts (Earthquake, Flood, Cyclone, Rain, Wildfire, Storm)
        resultList.addAll(MockDisasterDataSource.getFallbackDisasters())

        // 2. Try fetching real live USGS Earthquakes with a 3-second network timeout
        try {
            withTimeoutOrNull(3000L) {
                val usgsResponse = ApiClient.usgsService.getRecentEarthquakes()
                val features = usgsResponse.features ?: emptyList()

                features.take(8).forEach { feature ->
                    val props = feature.properties
                    val coords = feature.geometry?.coordinates
                    val mag = props?.mag ?: 0.0

                    if (props != null && coords != null && coords.size >= 2) {
                        val lon = coords[0]
                        val lat = coords[1]
                        val depth = if (coords.size >= 3) coords[2] else 10.0
                        val severity = SeverityLevel.fromMagnitude(mag)
                        val alertId = "usgs_"

                        val liveAlert = DisasterAlert(
                            id = alertId,
                            title = "Earthquake M " + String.format("%.1f", mag),
                            type = DisasterType.EARTHQUAKE,
                            severity = severity,
                            locationName = props.place ?: "Seismic Activity Zone",
                            latitude = lat,
                            longitude = lon,
                            timestamp = props.time ?: System.currentTimeMillis(),
                            description = "Seismic tremor of magnitude " + String.format("%.1f", mag) +
                                    " recorded at depth " + String.format("%.1f", depth) + " km. Source: USGS Live Feed.",
                            safetyInstructions = "• DROP to hands and knees.\n• COVER head and neck under sturdy table.\n• HOLD ON until shaking stops.",
                            source = "USGS Real-Time API",
                            magnitudeOrMetric = "Magnitude " + String.format("%.1f", mag) + " | Depth " + String.format("%.1f", depth) + "km",
                            affectedRadiusKm = (mag * 12).toInt().coerceAtLeast(20)
                        )
                        resultList.add(0, liveAlert)
                    }
                }
            }
        } catch (e: Exception) {
            // Gracefully ignore network failure and rely on local data
        }

        // Deduplicate and return sorted newest first
        resultList.distinctBy { it.id }.sortedByDescending { it.timestamp }
    }

    suspend fun getLiveWeather(userLat: Double, userLon: Double): WeatherResponse? = withContext(Dispatchers.IO) {
        try {
            withTimeoutOrNull(3000L) {
                ApiClient.weatherService.getWeatherForecast(userLat, userLon)
            }
        } catch (e: Exception) {
            null
        }
    }
}
