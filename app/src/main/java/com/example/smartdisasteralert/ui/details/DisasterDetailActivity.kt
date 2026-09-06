package com.example.smartdisasteralert.ui.details

import android.content.Intent
import android.content.res.ColorStateList
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
        binding.tvDetailCoordinates.text = String.format("%.4f° N, %.4f° E", alert.latitude, alert.longitude)
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
            val shareText = """
                ?? DISASTER ALERT: 
                Location: 
                Severity: 
                Date/Time: 
                Coordinates: https://maps.google.com/?q=,
                
                Safety Instructions:
                
                
                Shared via Smart Disaster Alert App (MAD Project)
            """.trimIndent()

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
}
