package com.example.smartdisasteralert.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.smartdisasteralert.R
import com.example.smartdisasteralert.data.local.AppPreferences
import com.example.smartdisasteralert.databinding.FragmentSettingsBinding
import com.example.smartdisasteralert.ui.main.MainViewModel
import com.example.smartdisasteralert.utils.NotificationHelper

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var preferences: AppPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = AppPreferences(requireContext())
        loadCurrentPreferences()
        setupListeners()
    }

    private fun loadCurrentPreferences() {
        binding.switchNotifications.isChecked = preferences.isNotificationsEnabled
        binding.switchLocation.isChecked = preferences.isLocationEnabled

        when (preferences.minSeverityFilter) {
            "MEDIUM_PLUS" -> binding.rbSeverityMediumPlus.isChecked = true
            "HIGH_ONLY" -> binding.rbSeverityHighOnly.isChecked = true
            else -> binding.rbSeverityAll.isChecked = true
        }
    }

    private fun setupListeners() {
        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            preferences.isNotificationsEnabled = isChecked
            val status = if (isChecked) "enabled" else "disabled"
            Toast.makeText(requireContext(), "Push notifications ", Toast.LENGTH_SHORT).show()
        }

        binding.switchLocation.setOnCheckedChangeListener { _, isChecked ->
            preferences.isLocationEnabled = isChecked
            val status = if (isChecked) "enabled" else "disabled"
            Toast.makeText(requireContext(), "GPS location tracking ", Toast.LENGTH_SHORT).show()
        }

        binding.rgSeverity.setOnCheckedChangeListener { _, checkedId ->
            val filter = when (checkedId) {
                R.id.rbSeverityMediumPlus -> "MEDIUM_PLUS"
                R.id.rbSeverityHighOnly -> "HIGH_ONLY"
                else -> "ALL"
            }
            viewModel.filterBySeverity(filter)
            Toast.makeText(requireContext(), "Severity filter updated", Toast.LENGTH_SHORT).show()
        }

        // Test Notification Button
        binding.btnTestNotification.setOnClickListener {
            if (preferences.isNotificationsEnabled) {
                NotificationHelper(requireContext()).sendDisasterNotification(
                    title = "Flash Flood Red Alert Warning",
                    message = "Water levels rising above danger mark in low-lying sectors. Evacuate to higher ground immediately.",
                    severity = "CRITICAL"
                )
                Toast.makeText(requireContext(), "Test notification dispatched to status bar!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please enable notifications first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
