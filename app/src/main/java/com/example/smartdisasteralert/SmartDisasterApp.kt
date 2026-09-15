package com.example.smartdisasteralert

import android.app.Application
import com.example.smartdisasteralert.utils.NotificationHelper

class SmartDisasterApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize notification channel for disaster alerts
        NotificationHelper(this).createNotificationChannel()
    }
}
