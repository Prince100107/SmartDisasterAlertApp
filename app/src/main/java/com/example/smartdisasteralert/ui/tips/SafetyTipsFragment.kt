package com.example.smartdisasteralert.ui.tips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartdisasteralert.data.local.MockDisasterDataSource
import com.example.smartdisasteralert.databinding.FragmentSafetyTipsBinding

class SafetyTipsFragment : Fragment() {

    private var _binding: FragmentSafetyTipsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SafetyTipAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSafetyTipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tipsList = MockDisasterDataSource.getSafetyTips()
        adapter = SafetyTipAdapter(tipsList)

        binding.rvSafetyTips.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SafetyTipsFragment.adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
