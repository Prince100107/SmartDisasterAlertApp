package com.example.smartdisasteralert.data.local

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "smart_disaster_alert_prefs"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val KEY_LOCATION_ENABLED = "location_enabled"
        private const val KEY_MIN_SEVERITY = "min_severity"
        private const val KEY_LAST_LATITUDE = "last_latitude"
        private const val KEY_LAST_LONGITUDE = "last_longitude"
        private const val KEY_LAST_LOCATION_NAME = "last_location_name"
    }

    var isNotificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, value).apply()

    var isLocationEnabled: Boolean
        get() = prefs.getBoolean(KEY_LOCATION_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_LOCATION_ENABLED, value).apply()

    var minSeverityFilter: String
        get() = prefs.getString(KEY_MIN_SEVERITY, "ALL") ?: "ALL"
        set(value) = prefs.edit().putString(KEY_MIN_SEVERITY, value).apply()

    var lastLatitude: Float
        get() = prefs.getFloat(KEY_LAST_LATITUDE, 23.0225f) // Default Ahmedabad, Gujarat
        set(value) = prefs.edit().putFloat(KEY_LAST_LATITUDE, value).apply()

    var lastLongitude: Float
        get() = prefs.getFloat(KEY_LAST_LONGITUDE, 72.5714f)
        set(value) = prefs.edit().putFloat(KEY_LAST_LONGITUDE, value).apply()

    var lastLocationName: String
        get() = prefs.getString(KEY_LAST_LOCATION_NAME, "Ahmedabad, Gujarat, India") ?: "Ahmedabad, Gujarat, India"
        set(value) = prefs.edit().putString(KEY_LAST_LOCATION_NAME, value).apply()
}
