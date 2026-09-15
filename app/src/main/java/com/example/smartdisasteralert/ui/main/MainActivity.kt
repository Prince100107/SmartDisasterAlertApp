package com.example.smartdisasteralert.ui.main

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.smartdisasteralert.R
import com.example.smartdisasteralert.data.local.AppPreferences
import com.example.smartdisasteralert.data.repository.DisasterRepository
import com.example.smartdisasteralert.databinding.ActivityMainBinding
import com.example.smartdisasteralert.ui.alerts.AlertsFragment
import com.example.smartdisasteralert.ui.emergency.EmergencyBottomSheetDialog
import com.example.smartdisasteralert.ui.home.HomeFragment
import com.example.smartdisasteralert.ui.map.MapFragment
import com.example.smartdisasteralert.ui.settings.SettingsFragment
import com.example.smartdisasteralert.ui.tips.SafetyTipsFragment
import com.example.smartdisasteralert.utils.LocationHelper
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: AppPreferences
    private lateinit var locationHelper: LocationHelper

    private val viewModel: MainViewModel by viewModels()

    // Permission launcher for Location & Android 13+ Notifications
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            refreshUserLocation()
        } else {
            Toast.makeText(this, "Location permission denied. Using default regional location.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = AppPreferences(this)
        locationHelper = LocationHelper(this)

        setupToolbar()
        setupBottomNavigation()
        setupSosFab()
        requestAppPermissions()

        // Set default fragment
        if (savedInstanceState == null) {
            loadFragment(HomeFragment(), "Home")
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment(), getString(R.string.app_name))
                    true
                }
                R.id.nav_alerts -> {
                    loadFragment(AlertsFragment(), "Live Disaster Alerts")
                    true
                }
                R.id.nav_map -> {
                    loadFragment(MapFragment(), "Interactive Disaster Map")
                    true
                }
                R.id.nav_safety -> {
                    loadFragment(SafetyTipsFragment(), "Safety & Preparedness")
                    true
                }
                R.id.nav_settings -> {
                    loadFragment(SettingsFragment(), "Settings & Preferences")
                    true
                }
                else -> false
            }
        }
    }

    fun navigateToTab(menuItemId: Int) {
        binding.bottomNavigationView.selectedItemId = menuItemId
    }

    private fun loadFragment(fragment: Fragment, title: String) {
        binding.toolbar.title = title
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun setupSosFab() {
        binding.fabSos.setOnClickListener {
            val coords = viewModel.userCoordinates.value ?: Pair(23.0225, 72.5714)
            val dialog = EmergencyBottomSheetDialog.newInstance(coords.first, coords.second)
            dialog.show(supportFragmentManager, EmergencyBottomSheetDialog.TAG)
        }
    }

    private fun requestAppPermissions() {
        val permissionsList = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsList.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        permissionLauncher.launch(permissionsList.toTypedArray())
    }

    fun refreshUserLocation() {
        if (!preferences.isLocationEnabled) {
            Toast.makeText(this, "Location access is disabled in settings", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val location = locationHelper.getCurrentLocation()
            if (location != null) {
                val address = locationHelper.getAddressFromCoordinates(location.first, location.second)
                viewModel.updateUserLocation(location.first, location.second, address)
                Toast.makeText(this@MainActivity, "Location updated: ", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
