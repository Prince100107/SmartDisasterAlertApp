package com.example.smartdisasteralert.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartdisasteralert.R
import com.example.smartdisasteralert.data.model.DisasterAlert
import com.example.smartdisasteralert.data.model.SeverityLevel
import com.example.smartdisasteralert.databinding.FragmentHomeBinding
import com.example.smartdisasteralert.ui.alerts.AlertAdapter
import com.example.smartdisasteralert.ui.details.DisasterDetailActivity
import com.example.smartdisasteralert.ui.emergency.EmergencyBottomSheetDialog
import com.example.smartdisasteralert.ui.main.MainActivity
import com.example.smartdisasteralert.ui.main.MainViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var recentAlertsAdapter: AlertAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        recentAlertsAdapter = AlertAdapter { alert ->
            openDetailActivity(alert)
        }
        binding.rvRecentAlerts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recentAlertsAdapter
        }
    }

    private fun setupListeners() {
        binding.swipeRefreshHome.setOnRefreshListener {
            viewModel.loadData()
        }

        binding.tvViewAllAlerts.setOnClickListener {
            (activity as? MainActivity)?.navigateToTab(R.id.nav_alerts)
        }

        binding.cardEmergencySos.setOnClickListener {
            val coords = viewModel.userCoordinates.value ?: Pair(23.0225, 72.5714)
            val dialog = EmergencyBottomSheetDialog.newInstance(coords.first, coords.second)
            dialog.show(parentFragmentManager, EmergencyBottomSheetDialog.TAG)
        }

        binding.btnRefreshLocation.setOnClickListener {
            (activity as? MainActivity)?.refreshUserLocation()
        }
    }

    private fun observeViewModel() {
        // Observe alerts list
        viewModel.allAlerts.observe(viewLifecycleOwner) { alerts ->
            binding.tvTotalAlertsCount.text = alerts.size.toString()
            val criticalCount = alerts.count { it.severity == SeverityLevel.CRITICAL || it.severity == SeverityLevel.HIGH }
            binding.tvCriticalAlertsCount.text = criticalCount.toString()

            // Display top 3 recent alerts
            recentAlertsAdapter.submitList(alerts.take(3))
        }

        // Observe location
        viewModel.locationName.observe(viewLifecycleOwner) { name ->
            binding.tvUserLocation.text = name
        }

        viewModel.userCoordinates.observe(viewLifecycleOwner) { coords ->
            binding.tvCoordinates.text = String.format("Lat: %.4f, Lng: %.4f", coords.first, coords.second)
        }

        // Observe weather
        viewModel.weather.observe(viewLifecycleOwner) { weatherResponse ->
            val current = weatherResponse?.current
            if (current != null) {
                val temp = current.temperature2m ?: 28.0
                val wind = current.windSpeed10m ?: 15.0
                val rain = current.precipitation ?: current.rain ?: 0.0
                val code = current.weatherCode ?: 0

                val condition = when (code) {
                    0 -> "Clear Skies"
                    1, 2, 3 -> "Partly Cloudy"
                    51, 53, 55 -> "Light Drizzle"
                    61, 63, 65 -> "Rain Showers"
                    71, 73, 75 -> "Snow Fall"
                    80, 81, 82 -> "Heavy Rain Showers"
                    95, 96, 99 -> "Thunderstorm Alert"
                    else -> "Atmospheric Advisory"
                }

                binding.tvWeatherCondition.text = condition
                binding.tvWeatherMetrics.text = String.format("Temp: %.1f°C | Wind: %.1f km/h | Rain: %.1f mm", temp, wind, rain)

                // Select weather icon
                if (code in listOf(95, 96, 99)) {
                    binding.ivWeatherIcon.setImageResource(R.drawable.ic_lightning)
                } else if (rain > 2.0 || code in listOf(61, 63, 65, 80, 81, 82)) {
                    binding.ivWeatherIcon.setImageResource(R.drawable.ic_rain)
                } else if (wind > 40.0) {
                    binding.ivWeatherIcon.setImageResource(R.drawable.ic_cyclone)
                } else {
                    binding.ivWeatherIcon.setImageResource(R.drawable.ic_warning)
                }
            } else {
                binding.tvWeatherCondition.text = "Weather Online Monitor"
                binding.tvWeatherMetrics.text = "Real-time atmospheric monitoring active"
            }
        }

        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.swipeRefreshHome.isRefreshing = loading
            binding.progressBarHome.visibility = if (loading && recentAlertsAdapter.itemCount == 0) View.VISIBLE else View.GONE
        }
    }

    private fun openDetailActivity(alert: DisasterAlert) {
        val intent = Intent(requireContext(), DisasterDetailActivity::class.java).apply {
            putExtra(DisasterDetailActivity.EXTRA_DISASTER_ALERT, alert)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
