package com.example.smartdisasteralert.ui.details

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.smartdisasteralert.data.model.DisasterAlert
import com.example.smartdisasteralert.databinding.ActivityDisasterDetailBinding
import com.example.smartdisasteralert.ui.emergency.EmergencyBottomSheetDialog
import com.example.smartdisasteralert.utils.DateTimeUtils

class DisasterDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDisasterDetailBinding
    private var disasterAlert: DisasterAlert? = null

    companion object {
        const val EXTRA_DISASTER_ALERT = "EXTRA_DISASTER_ALERT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisasterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Read alert object from Intent
        @Suppress("DEPRECATION")
        disasterAlert = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(EXTRA_DISASTER_ALERT, DisasterAlert::class.java)
        } else {
            intent.getSerializableExtra(EXTRA_DISASTER_ALERT) as? DisasterAlert
        }

        setupToolbar()
        populateDetails()
        setupActionButtons()
        setupMapNavigation()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbarDetail.setNavigationOnClickListener {
            finish()
        }
    }

    private fun populateDetails() {
        val alert = disasterAlert ?: return

        binding.tvDetailTitle.text = alert.title
        binding.tvDetailType.text = alert.type.displayName
        binding.tvDetailLocation.text = alert.locationName
        binding.tvDetailTime.text = DateTimeUtils.formatExactDateTime(alert.timestamp)
        binding.tvDetailCoordinates.text = String.format("Lat: %.4f, Lng: %.4f (Tap to Open Map)", alert.latitude, alert.longitude)
        binding.tvDetailSource.text = alert.source
        binding.tvDetailDescription.text = alert.description
        binding.tvDetailInstructions.text = alert.safetyInstructions

        // Disaster Icon
        binding.ivDetailIcon.setImageResource(alert.type.iconRes)
        val iconTint = ContextCompat.getColor(this, alert.type.colorRes)
        binding.ivDetailIcon.setColorFilter(iconTint)

        // Severity Badge
        binding.tvDetailSeverityBadge.text = alert.severity.displayName
        val severityColor = ContextCompat.getColor(this, alert.severity.colorRes)
        val severityBgColor = ContextCompat.getColor(this, alert.severity.bgRes)
        binding.tvDetailSeverityBadge.setTextColor(severityColor)
        binding.tvDetailSeverityBadge.backgroundTintList = ColorStateList.valueOf(severityBgColor)
    }

    private fun setupActionButtons() {
        val alert = disasterAlert ?: return

        // Share Alert via Implicit Intent
        binding.btnShareAlert.setOnClickListener {
            val shareText = "DISASTER ALERT: " + alert.title + "\n" +
                    "Location: " + alert.locationName + "\n" +
                    "Severity: " + alert.severity.displayName + "\n" +
                    "Time: " + DateTimeUtils.formatExactDateTime(alert.timestamp) + "\n" +
                    "Map Coordinates: https://maps.google.com/?q=" + alert.latitude + "," + alert.longitude + "\n\n" +
                    "Instructions:\n" + alert.safetyInstructions + "\n\n" +
                    "Shared via Smart Disaster Alert App (MAD Project)"

            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                putExtra(Intent.EXTRA_SUBJECT, "Disaster Alert: " + alert.title)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(sendIntent, "Share Disaster Alert via"))
        }

        // Emergency Help button opens Emergency SOS bottom sheet
        binding.btnEmergencyHelp.setOnClickListener {
            val bottomSheet = EmergencyBottomSheetDialog.newInstance(alert.latitude, alert.longitude)
            bottomSheet.show(supportFragmentManager, EmergencyBottomSheetDialog.TAG)
        }
    }

    private fun setupMapNavigation() {
        val alert = disasterAlert ?: return
        binding.tvDetailCoordinates.setOnClickListener {
            val geoUri = Uri.parse("geo:" + alert.latitude + "," + alert.longitude + "?q=" + alert.latitude + "," + alert.longitude + "(" + Uri.encode(alert.title) + ")")
            val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
            try {
                startActivity(mapIntent)
            } catch (e: Exception) {
                val webUri = Uri.parse("https://maps.google.com/?q=" + alert.latitude + "," + alert.longitude)
                startActivity(Intent(Intent.ACTION_VIEW, webUri))
            }
        }
    }
}
