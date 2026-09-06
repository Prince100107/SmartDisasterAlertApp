package com.example.smartdisasteralert.ui.emergency

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartdisasteralert.data.model.EmergencyContact
import com.example.smartdisasteralert.databinding.ItemEmergencyContactBinding

class EmergencyContactAdapter(
    private val contacts: List<EmergencyContact>,
    private val onDialClick: (EmergencyContact) -> Unit
) : RecyclerView.Adapter<EmergencyContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(private val binding: ItemEmergencyContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: EmergencyContact) {
            binding.tvContactName.text = contact.name
            binding.tvContactNumber.text = "Helpline: " + contact.number
            binding.ivContactIcon.setImageResource(contact.iconRes)

            binding.btnDialContact.setOnClickListener {
                onDialClick(contact)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemEmergencyContactBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int = contacts.size
}
