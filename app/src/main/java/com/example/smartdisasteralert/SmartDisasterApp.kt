package com.example.smartdisasteralert

import android.app.Application
import com.example.smartdisasteralert.utils.NotificationHelper
import org.osmdroid.config.Configuration
import java.io.File

class SmartDisasterApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Configure OSMDroid user agent & cache path for map tiles
        Configuration.getInstance().userAgentValue = packageName
        val osmTileDir = File(cacheDir, "osmdroid")
        if (!osmTileDir.exists()) osmTileDir.mkdirs()
        Configuration.getInstance().osmdroidBasePath = osmTileDir
        Configuration.getInstance().osmdroidTileCache = osmTileDir

        // Create notification channels for disaster alerts
        NotificationHelper(this).createNotificationChannel()
    }
}
