package com.example.smartdisasteralert.data.model

import com.example.smartdisasteralert.R
import java.io.Serializable

enum class DisasterType(val displayName: String, val iconRes: Int, val colorRes: Int) : Serializable {
    EARTHQUAKE("Earthquake", R.drawable.ic_earthquake, R.color.disaster_earthquake),
    FLOOD("Flood", R.drawable.ic_flood, R.color.disaster_flood),
    CYCLONE("Cyclone", R.drawable.ic_cyclone, R.color.disaster_cyclone),
    FIRE("Wildfire", R.drawable.ic_fire, R.color.disaster_fire),
    RAIN("Heavy Rain", R.drawable.ic_rain, R.color.disaster_rain),
    THUNDERSTORM("Thunderstorm", R.drawable.ic_lightning, R.color.disaster_lightning),
    OTHER("Disaster Alert", R.drawable.ic_warning, R.color.primary);

    companion object {
        fun fromString(type: String?): DisasterType {
            return when (type?.uppercase()) {
                "EARTHQUAKE" -> EARTHQUAKE
                "FLOOD" -> FLOOD
                "CYCLONE" -> CYCLONE
                "FIRE", "WILDFIRE" -> FIRE
                "RAIN", "HEAVY RAIN" -> RAIN
                "THUNDERSTORM", "LIGHTNING" -> THUNDERSTORM
                else -> OTHER
            }
        }
    }
}
