package com.example.smartdisasteralert.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

class LocationHelper(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    suspend fun getCurrentLocation(): Pair<Double, Double>? = suspendCancellableCoroutine { cont ->
        if (!hasLocationPermission()) {
            cont.resume(null)
            return@suspendCancellableCoroutine
        }

        try {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        cont.resume(Pair(location.latitude, location.longitude))
                    } else {
                        // Fallback to last location
                        fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc ->
                            if (lastLoc != null) {
                                cont.resume(Pair(lastLoc.latitude, lastLoc.longitude))
                            } else {
                                cont.resume(null)
                            }
                        }.addOnFailureListener {
                            cont.resume(null)
                        }
                    }
                }
                .addOnFailureListener {
                    cont.resume(null)
                }
        } catch (e: SecurityException) {
            cont.resume(null)
        }
    }

    fun getAddressFromCoordinates(latitude: Double, longitude: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val locality = address.locality ?: address.subAdminArea ?: ""
                val adminArea = address.adminArea ?: ""
                val country = address.countryName ?: ""
                listOfNotNull(locality.ifEmpty { null }, adminArea.ifEmpty { null }, country.ifEmpty { null })
                    .joinToString(", ")
                    .ifEmpty { "Lat: " + String.format("%.4f", latitude) + ", Lng: " + String.format("%.4f", longitude) }
            } else {
                "Lat: " + String.format("%.4f", latitude) + ", Lng: " + String.format("%.4f", longitude)
            }
        } catch (e: Exception) {
            "Lat: " + String.format("%.4f", latitude) + ", Lng: " + String.format("%.4f", longitude)
        }
    }
}
