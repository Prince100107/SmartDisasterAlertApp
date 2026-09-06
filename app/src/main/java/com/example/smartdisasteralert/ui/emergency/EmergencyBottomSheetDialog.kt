package com.example.smartdisasteralert.ui.emergency

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartdisasteralert.data.local.MockDisasterDataSource
import com.example.smartdisasteralert.databinding.BottomSheetEmergencyBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EmergencyBottomSheetDialog : BottomSheetDialogFragment() {

    private var _binding: BottomSheetEmergencyBinding? = null
    private val binding get() = _binding!!

    private var latitude: Double = 23.0225
    private var longitude: Double = 72.5714

    companion object {
        const val TAG = "EmergencyBottomSheetDialog"
        private const val ARG_LAT = "ARG_LAT"
        private const val ARG_LON = "ARG_LON"

        fun newInstance(lat: Double, lon: Double): EmergencyBottomSheetDialog {
            val dialog = EmergencyBottomSheetDialog()
            val args = Bundle().apply {
                putDouble(ARG_LAT, lat)
                putDouble(ARG_LON, lon)
            }
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latitude = it.getDouble(ARG_LAT, 23.0225)
            longitude = it.getDouble(ARG_LON, 72.5714)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetEmergencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEmergencyContactsList()
        setupSosActions()
    }

    private fun setupEmergencyContactsList() {
        val contacts = MockDisasterDataSource.getEmergencyContacts()
        val adapter = EmergencyContactAdapter(contacts) { contact ->
            dialPhoneNumber(contact.number)
        }

        binding.rvEmergencyHelplines.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
    }

    private fun setupSosActions() {
        // Send SOS SMS via Implicit Intent ACTION_SENDTO
        binding.btnSosSms.setOnClickListener {
            val mapsUrl = "https://maps.google.com/?q=" + latitude + "," + longitude
            val smsBody = "EMERGENCY SOS! I am in immediate danger due to a disaster event. My live coordinates: " + mapsUrl

            val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:112")
                putExtra("sms_body", smsBody)
            }

            try {
                startActivity(smsIntent)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Unable to open SMS application", Toast.LENGTH_SHORT).show()
            }
        }

        // Share Location via Implicit Intent ACTION_SEND
        binding.btnShareLocation.setOnClickListener {
            val mapsUrl = "https://maps.google.com/?q=" + latitude + "," + longitude
            val shareText = "EMERGENCY BEACON: I need assistance! Location coordinates: " + mapsUrl

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
                putExtra(Intent.EXTRA_SUBJECT, "Urgent Disaster Emergency Location")
            }
            startActivity(Intent.createChooser(shareIntent, "Share Location Beacon via"))
        }
    }

    private fun dialPhoneNumber(phoneNumber: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:" + phoneNumber)
        }
        startActivity(dialIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
