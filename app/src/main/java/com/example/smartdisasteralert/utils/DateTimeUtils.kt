package com.example.smartdisasteralert.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeUtils {

    fun formatTimeAgo(timestamp: Long): String {
        val diff = System.currentTimeMillis() - timestamp
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            minutes < 1 -> "Just now"
            minutes < 60 -> " minutes ago"
            hours < 24 -> " hours ago"
            days < 7 -> " days ago"
            else -> {
                val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                sdf.format(Date(timestamp))
            }
        }
    }

    fun formatExactDateTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE, dd MMM yyyy 'at' hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
