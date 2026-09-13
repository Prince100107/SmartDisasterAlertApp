package com.example.smartdisasteralert.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartdisasteralert.data.local.AppPreferences
import com.example.smartdisasteralert.data.model.DisasterAlert
import com.example.smartdisasteralert.data.model.DisasterType
import com.example.smartdisasteralert.data.model.SeverityLevel
import com.example.smartdisasteralert.data.model.WeatherResponse
import com.example.smartdisasteralert.data.repository.DisasterRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: DisasterRepository,
    private val preferences: AppPreferences
) : ViewModel() {

    private val _allAlerts = MutableLiveData<List<DisasterAlert>>(emptyList())
    val allAlerts: LiveData<List<DisasterAlert>> = _allAlerts

    private val _filteredAlerts = MutableLiveData<List<DisasterAlert>>(emptyList())
    val filteredAlerts: LiveData<List<DisasterAlert>> = _filteredAlerts

    private val _weather = MutableLiveData<WeatherResponse?>()
    val weather: LiveData<WeatherResponse?> = _weather

    private val _userCoordinates = MutableLiveData<Pair<Double, Double>>(
        Pair(preferences.lastLatitude.toDouble(), preferences.lastLongitude.toDouble())
    )
    val userCoordinates: LiveData<Pair<Double, Double>> = _userCoordinates

    private val _locationName = MutableLiveData<String>(preferences.lastLocationName)
    val locationName: LiveData<String> = _locationName

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var currentCategory: DisasterType? = null

    init {
        loadData()
    }

    fun loadData() {
        val coords = _userCoordinates.value ?: Pair(23.0225, 72.5714)
        viewModelScope.launch {
            _isLoading.value = true

            // Fetch live disaster alerts
            val alerts = repository.getDisasterAlerts(coords.first, coords.second)
            _allAlerts.value = alerts
            applyFilter()

            // Fetch weather
            val weatherData = repository.getLiveWeather(coords.first, coords.second)
            _weather.value = weatherData

            _isLoading.value = false
        }
    }

    fun updateUserLocation(lat: Double, lon: Double, address: String) {
        _userCoordinates.value = Pair(lat, lon)
        _locationName.value = address

        // Persist in preferences
        preferences.lastLatitude = lat.toFloat()
        preferences.lastLongitude = lon.toFloat()
        preferences.lastLocationName = address

        // Refresh with new location
        loadData()
    }

    fun filterByCategory(category: DisasterType?) {
        currentCategory = category
        applyFilter()
    }

    fun filterBySeverity(severityFilter: String) {
        preferences.minSeverityFilter = severityFilter
        applyFilter()
    }

    private fun applyFilter() {
        val list = _allAlerts.value ?: emptyList()
        val minSeverity = preferences.minSeverityFilter

        val filtered = list.filter { alert ->
            // Category check
            val matchesCategory = currentCategory == null || alert.type == currentCategory

            // Severity check
            val matchesSeverity = when (minSeverity) {
                "MEDIUM_PLUS" -> alert.severity in listOf(SeverityLevel.MEDIUM, SeverityLevel.HIGH, SeverityLevel.CRITICAL)
                "HIGH_ONLY" -> alert.severity in listOf(SeverityLevel.HIGH, SeverityLevel.CRITICAL)
                else -> true
            }

            matchesCategory && matchesSeverity
        }
        _filteredAlerts.value = filtered
    }
}
