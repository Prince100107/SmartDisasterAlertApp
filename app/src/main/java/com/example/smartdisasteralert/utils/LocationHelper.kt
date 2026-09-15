package com.example.smartdisasteralert.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
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

    suspend fun getCurrentLocation(): Pair<Double, Double>? = withTimeoutOrNull(4000L) {
        if (!hasLocationPermission()) {
            return@withTimeoutOrNull null
        }

        try {
            // First try fast last location
            val lastLoc = suspendCancellableCoroutine<Location?> { cont ->
                try {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { loc -> cont.resume(loc) }
                        .addOnFailureListener { cont.resume(null) }
                } catch (e: Exception) {
                    cont.resume(null)
                }
            }

            if (lastLoc != null) {
                return@withTimeoutOrNull Pair(lastLoc.latitude, lastLoc.longitude)
            }

            // Fallback to system LocationManager (works offline without Google Play Services)
            val locManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            val gpsLoc = locManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val netLoc = locManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            val bestLoc = gpsLoc ?: netLoc

            if (bestLoc != null) {
                return@withTimeoutOrNull Pair(bestLoc.latitude, bestLoc.longitude)
            }

            // Otherwise try high accuracy request with timeout
            suspendCancellableCoroutine<Pair<Double, Double>?> { cont ->
                try {
                    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
                        .addOnSuccessListener { loc ->
                            if (loc != null) cont.resume(Pair(loc.latitude, loc.longitude))
                            else cont.resume(null)
                        }
                        .addOnFailureListener { cont.resume(null) }
                } catch (e: Exception) {
                    cont.resume(null)
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getAddressFromCoordinates(latitude: Double, longitude: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            @Suppress("DEPRECATION")
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val locality = address.locality ?: address.subAdminArea ?: ""
                val adminArea = address.adminArea ?: ""
                val country = address.countryName ?: ""
                listOfNotNull(locality.ifEmpty { null }, adminArea.ifEmpty { null }, country.ifEmpty { null })
                    .joinToString(", ")
                    .ifEmpty { String.format("Lat: %.4f, Lng: %.4f", latitude, longitude) }
            } else {
                String.format("Lat: %.4f, Lng: %.4f", latitude, longitude)
            }
        } catch (e: Exception) {
            String.format("Lat: %.4f, Lng: %.4f", latitude, longitude)
        }
    }
}
