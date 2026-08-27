package com.example.smartdisasteralert.data.model

import java.io.Serializable

data class DisasterAlert(
    val id: String,
    val title: String,
    val type: DisasterType,
    val severity: SeverityLevel,
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
    val description: String,
    val safetyInstructions: String,
    val source: String,
    val magnitudeOrMetric: String = "",
    val affectedRadiusKm: Int = 50
) : Serializable
