package com.example.smartdisasteralert.data.repository

import com.example.smartdisasteralert.data.api.ApiClient
import com.example.smartdisasteralert.data.local.MockDisasterDataSource
import com.example.smartdisasteralert.data.model.DisasterAlert
import com.example.smartdisasteralert.data.model.DisasterType
import com.example.smartdisasteralert.data.model.SeverityLevel
import com.example.smartdisasteralert.data.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DisasterRepository {

    suspend fun getDisasterAlerts(userLat: Double, userLon: Double): List<DisasterAlert> = withContext(Dispatchers.IO) {
        val resultList = mutableListOf<DisasterAlert>()

        // 1. Fetch Real Live Earthquakes from USGS GeoJSON API
        try {
            val usgsResponse = ApiClient.usgsService.getRecentEarthquakes()
            val features = usgsResponse.features ?: emptyList()

            // Take top 15 earthquakes
            features.take(15).forEach { feature ->
                val props = feature.properties
                val coords = feature.geometry?.coordinates
                val mag = props?.mag ?: 0.0

                if (props != null && coords != null && coords.size >= 2) {
                    val lon = coords[0]
                    val lat = coords[1]
                    val depth = if (coords.size >= 3) coords[2] else 10.0
                    val severity = SeverityLevel.fromMagnitude(mag)
                    val alertId = "usgs_"

                    val alert = DisasterAlert(
                        id = alertId,
                        title = "Earthquake M " + String.format("%.1f", mag),
                        type = DisasterType.EARTHQUAKE,
                        severity = severity,
                        locationName = props.place ?: "Global Seismic Activity",
                        latitude = lat,
                        longitude = lon,
                        timestamp = props.time ?: System.currentTimeMillis(),
                        description = "Earthquake of magnitude " + String.format("%.1f", mag) +
                                " registered at depth of " + String.format("%.1f", depth) + " km. Source: USGS Live Feed.",
                        safetyInstructions = "• Drop, Cover, and Hold On immediately.\n• Stay away from glass and outer walls.\n• Check for injuries once tremors cease.",
                        source = "USGS Real-time API",
                        magnitudeOrMetric = "Mag: " + String.format("%.1f", mag) + " | Depth: " + String.format("%.1f", depth) + "km",
                        affectedRadiusKm = (mag * 12).toInt().coerceAtLeast(20)
                    )
                    resultList.add(alert)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // If live network call fails, we rely on cached and fallback data
        }

        // 2. Fetch Live Weather and evaluate regional hazard via Open-Meteo API
        try {
            val weatherResponse = ApiClient.weatherService.getWeatherForecast(userLat, userLon)
            val current = weatherResponse.current
            if (current != null) {
                val rainMm = current.precipitation ?: current.rain ?: 0.0
                val windKmh = current.windSpeed10m ?: 0.0
                val code = current.weatherCode ?: 0

                // Generate regional weather hazard alert if significant
                if (rainMm > 5.0 || windKmh > 35.0 || code in listOf(95, 96, 99, 65, 75)) {
                    val weatherAlert = DisasterAlert(
                        id = "weather_live_alert",
                        title = if (windKmh > 60.0) "High Wind / Squall Warning" else if (rainMm > 15.0) "Severe Rain Alert" else "Thunderstorm Alert",
                        type = if (windKmh > 60.0) DisasterType.CYCLONE else if (rainMm > 15.0) DisasterType.RAIN else DisasterType.THUNDERSTORM,
                        severity = if (windKmh > 70.0 || rainMm > 30.0) SeverityLevel.HIGH else SeverityLevel.MEDIUM,
                        locationName = "Your Current Vicinity",
                        latitude = userLat,
                        longitude = userLon,
                        timestamp = System.currentTimeMillis(),
                        description = "Active weather alert: Precipitation " + rainMm + "mm/h, Wind gust " + windKmh + " km/h (WMO Code " + code + ").",
                        safetyInstructions = "• Stay indoors and secure loose outdoor objects.\n• Drive cautiously on wet asphalt.\n• Disconnect high-voltage home appliances.",
                        source = "Open-Meteo Live API",
                        magnitudeOrMetric = "Rain: " + rainMm + "mm | Wind: " + windKmh + "km/h",
                        affectedRadiusKm = 25
                    )
                    resultList.add(0, weatherAlert)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 3. Merge curated multi-disaster fallback events (Flood, Wildfire, Cyclone, etc.)
        val fallbacks = MockDisasterDataSource.getFallbackDisasters()
        resultList.addAll(fallbacks)

        // Deduplicate by ID and sort newest first
        resultList.distinctBy { it.id }.sortedByDescending { it.timestamp }
    }

    suspend fun getLiveWeather(userLat: Double, userLon: Double): WeatherResponse? = withContext(Dispatchers.IO) {
        try {
            ApiClient.weatherService.getWeatherForecast(userLat, userLon)
        } catch (e: Exception) {
            null
        }
    }
}
