package com.example.smartdisasteralert.ui.tips

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.smartdisasteralert.data.model.SafetyTip
import com.example.smartdisasteralert.databinding.ItemSafetyTipBinding

class SafetyTipAdapter(
    private var tips: List<SafetyTip>
) : RecyclerView.Adapter<SafetyTipAdapter.TipViewHolder>() {

    inner class TipViewHolder(private val binding: ItemSafetyTipBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tip: SafetyTip) {
            val context = binding.root.context

            binding.tvTipTitle.text = tip.title
            binding.tvTipSubtitle.text = tip.subtitle
            binding.ivTipIcon.setImageResource(tip.disasterType.iconRes)
            val iconTint = ContextCompat.getColor(context, tip.disasterType.colorRes)
            binding.ivTipIcon.setColorFilter(iconTint)

            // Populate Before, During, After steps
            binding.tvBeforeInstructions.text = tip.beforeSteps.joinToString("\n") { "• " }
            binding.tvDuringInstructions.text = tip.duringSteps.joinToString("\n") { "• " }
            binding.tvAfterInstructions.text = tip.afterSteps.joinToString("\n") { "• " }

            // Accordion expand/collapse state
            binding.layoutExpandedContent.visibility = if (tip.isExpanded) View.VISIBLE else View.GONE
            binding.ivExpandChevron.rotation = if (tip.isExpanded) 90f else 0f

            binding.layoutTipHeader.setOnClickListener {
                tip.isExpanded = !tip.isExpanded
                notifyItemChanged(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipViewHolder {
        val binding = ItemSafetyTipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        holder.bind(tips[position])
    }

    override fun getItemCount(): Int = tips.size

    fun updateList(newTips: List<SafetyTip>) {
        tips = newTips
        notifyDataSetChanged()
    }
}
