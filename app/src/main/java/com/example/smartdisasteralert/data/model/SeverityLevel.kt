package com.example.smartdisasteralert.data.model

import com.example.smartdisasteralert.R
import java.io.Serializable

enum class SeverityLevel(val displayName: String, val colorRes: Int, val bgRes: Int) : Serializable {
    LOW("LOW", R.color.severity_low, R.color.severity_low_bg),
    MEDIUM("MEDIUM", R.color.severity_medium, R.color.severity_medium_bg),
    HIGH("HIGH", R.color.severity_high, R.color.severity_high_bg),
    CRITICAL("CRITICAL", R.color.severity_high, R.color.severity_high_bg);

    companion object {
        fun fromMagnitude(mag: Double): SeverityLevel {
            return when {
                mag >= 6.5 -> CRITICAL
                mag >= 5.0 -> HIGH
                mag >= 3.5 -> MEDIUM
                else -> LOW
            }
        }
    }
}
