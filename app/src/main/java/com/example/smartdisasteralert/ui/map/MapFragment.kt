package com.example.smartdisasteralert.ui.map

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.smartdisasteralert.R
import com.example.smartdisasteralert.data.model.DisasterAlert
import com.example.smartdisasteralert.databinding.FragmentMapBinding
import com.example.smartdisasteralert.ui.details.DisasterDetailActivity
import com.example.smartdisasteralert.ui.main.MainViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()
    private var selectedAlert: DisasterAlert? = null

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

        setupMap()
        setupListeners()
        observeViewModel()
    }

    private fun setupMap() {
        binding.mapView.apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(6.5)

            // Default center
            val defaultPoint = GeoPoint(23.0225, 72.5714)
            controller.setCenter(defaultPoint)
        }
    }

    private fun setupListeners() {
        // FAB to re-center on user's location
        binding.fabMyLocation.setOnClickListener {
            val coords = viewModel.userCoordinates.value ?: Pair(23.0225, 72.5714)
            val userPoint = GeoPoint(coords.first, coords.second)
            binding.mapView.controller.animateTo(userPoint, 10.0, 1000L)
        }

        // Details button on bottom preview card
        binding.btnMapPreviewDetails.setOnClickListener {
            selectedAlert?.let { alert ->
                val intent = Intent(requireContext(), DisasterDetailActivity::class.java).apply {
                    putExtra(DisasterDetailActivity.EXTRA_DISASTER_ALERT, alert)
                }
                startActivity(intent)
            }
        }
    }

    private fun observeViewModel() {
        // Observe user location
        viewModel.userCoordinates.observe(viewLifecycleOwner) { coords ->
            val userPoint = GeoPoint(coords.first, coords.second)
            addUserLocationMarker(userPoint)
        }

        // Observe disaster alerts
        viewModel.allAlerts.observe(viewLifecycleOwner) { alerts ->
            binding.progressBarMap.visibility = View.GONE
            binding.tvMapDisasterCount.text = alerts.size.toString() + " Active Disaster Zones"
            displayDisasterMarkers(alerts)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBarMap.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun addUserLocationMarker(point: GeoPoint) {
        // Find existing user marker or create new
        val userMarker = Marker(binding.mapView).apply {
            position = point
            title = "Your Location"
            snippet = "Current GPS Coordinates"
            icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_location)
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }
        binding.mapView.overlays.add(userMarker)
        binding.mapView.invalidate()
    }

    private fun displayDisasterMarkers(alerts: List<DisasterAlert>) {
        // Clear previous overlays except user location if any
        binding.mapView.overlays.clear()

        // Re-add user location
        val coords = viewModel.userCoordinates.value ?: Pair(23.0225, 72.5714)
        addUserLocationMarker(GeoPoint(coords.first, coords.second))

        for (alert in alerts) {
            val point = GeoPoint(alert.latitude, alert.longitude)
            val marker = Marker(binding.mapView).apply {
                position = point
                title = alert.title
                snippet = alert.locationName
                icon = ContextCompat.getDrawable(requireContext(), alert.type.iconRes)
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                setOnMarkerClickListener { _, _ ->
                    showBottomAlertPreview(alert)
                    binding.mapView.controller.animateTo(point)
                    true
                }
            }
            binding.mapView.overlays.add(marker)
        }

        binding.mapView.invalidate()
    }

    private fun showBottomAlertPreview(alert: DisasterAlert) {
        selectedAlert = alert
        binding.cardMapPreview.visibility = View.VISIBLE
        binding.tvMapPreviewTitle.text = alert.title
        binding.tvMapPreviewLocation.text = alert.locationName
        binding.ivMapPreviewIcon.setImageResource(alert.type.iconRes)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
