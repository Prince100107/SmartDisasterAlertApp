package com.example.smartdisasteralert.ui.alerts

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartdisasteralert.data.model.DisasterAlert
import com.example.smartdisasteralert.databinding.ItemDisasterAlertBinding
import com.example.smartdisasteralert.utils.DateTimeUtils

class AlertAdapter(
    private val onItemClick: (DisasterAlert) -> Unit
) : ListAdapter<DisasterAlert, AlertAdapter.AlertViewHolder>(DiffCallback) {

    inner class AlertViewHolder(private val binding: ItemDisasterAlertBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(alert: DisasterAlert) {
            val context = binding.root.context

            binding.tvDisasterTitle.text = alert.title
            binding.tvLocation.text = alert.locationName
            binding.tvDescription.text = alert.description
            binding.tvTime.text = DateTimeUtils.formatTimeAgo(alert.timestamp)

            // Severity Badge
            binding.tvSeverityBadge.text = alert.severity.displayName
            val severityColor = ContextCompat.getColor(context, alert.severity.colorRes)
            val severityBgColor = ContextCompat.getColor(context, alert.severity.bgRes)
            binding.tvSeverityBadge.setTextColor(severityColor)
            binding.tvSeverityBadge.backgroundTintList = ColorStateList.valueOf(severityBgColor)

            // Disaster Icon
            binding.ivDisasterIcon.setImageResource(alert.type.iconRes)
            val iconTint = ContextCompat.getColor(context, alert.type.colorRes)
            binding.ivDisasterIcon.setColorFilter(iconTint)

            binding.cardDisaster.setOnClickListener {
                onItemClick(alert)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val binding = ItemDisasterAlertBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<DisasterAlert>() {
            override fun areItemsTheSame(oldItem: DisasterAlert, newItem: DisasterAlert): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DisasterAlert, newItem: DisasterAlert): Boolean {
                return oldItem == newItem
            }
        }
    }
}
