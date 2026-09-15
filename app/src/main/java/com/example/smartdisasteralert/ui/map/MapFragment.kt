package com.example.smartdisasteralert.ui.map

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartdisasteralert.data.model.DisasterAlert
import com.example.smartdisasteralert.databinding.FragmentMapBinding
import com.example.smartdisasteralert.ui.alerts.AlertAdapter
import com.example.smartdisasteralert.ui.details.DisasterDetailActivity
import com.example.smartdisasteralert.ui.main.MainViewModel

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var mapAlertsAdapter: AlertAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        mapAlertsAdapter = AlertAdapter { alert ->
            // Open full details with option to view coordinates in Maps
            val intent = Intent(requireContext(), DisasterDetailActivity::class.java).apply {
                putExtra(DisasterDetailActivity.EXTRA_DISASTER_ALERT, alert)
            }
            startActivity(intent)
        }

        binding.rvMapDisasters.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mapAlertsAdapter
        }
    }

    private fun setupListeners() {
        // Open user location in Google Maps app via Implicit Intent
        binding.btnOpenMyLocationMap.setOnClickListener {
            val coords = viewModel.userCoordinates.value ?: Pair(23.0225, 72.5714)
            openCoordinatesInMaps(coords.first, coords.second, "My Current Location")
        }
    }

    private fun observeViewModel() {
        // User location
        viewModel.userCoordinates.observe(viewLifecycleOwner) { coords ->
            val address = viewModel.locationName.value ?: "Detected Location"
            binding.tvUserMapCoords.text = String.format("Lat: %.4f N, Lng: %.4f E | %s", coords.first, coords.second, address)
        }

        // Active disaster zones
        viewModel.allAlerts.observe(viewLifecycleOwner) { alerts ->
            mapAlertsAdapter.submitList(alerts)
            binding.tvMapSubtitle.text = " Active Hazard Zones Plotted"
        }
    }

    private fun openCoordinatesInMaps(lat: Double, lon: Double, label: String) {
        val geoUri = Uri.parse("geo:,=,(" + Uri.encode(label) + ")")
        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
        try {
            startActivity(mapIntent)
        } catch (e: Exception) {
            // Fallback to browser Google Maps
            val webUri = Uri.parse("https://maps.google.com/?q=,")
            startActivity(Intent(Intent.ACTION_VIEW, webUri))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
