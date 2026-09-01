package com.example.smartdisasteralert.ui.alerts

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
import com.example.smartdisasteralert.data.model.DisasterType
import com.example.smartdisasteralert.databinding.FragmentAlertsBinding
import com.example.smartdisasteralert.ui.details.DisasterDetailActivity
import com.example.smartdisasteralert.ui.main.MainViewModel

class AlertsFragment : Fragment() {

    private var _binding: FragmentAlertsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var alertsAdapter: AlertAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupChips()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        alertsAdapter = AlertAdapter { alert ->
            openDetailActivity(alert)
        }
        binding.rvAlerts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = alertsAdapter
        }
    }

    private fun setupChips() {
        binding.chipGroupDisasters.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) {
                viewModel.filterByCategory(null)
                return@setOnCheckedStateChangeListener
            }
            when (checkedIds.first()) {
                R.id.chipAll -> viewModel.filterByCategory(null)
                R.id.chipEarthquake -> viewModel.filterByCategory(DisasterType.EARTHQUAKE)
                R.id.chipRain -> viewModel.filterByCategory(DisasterType.RAIN)
                R.id.chipFlood -> viewModel.filterByCategory(DisasterType.FLOOD)
                R.id.chipCyclone -> viewModel.filterByCategory(DisasterType.CYCLONE)
                R.id.chipFire -> viewModel.filterByCategory(DisasterType.FIRE)
                R.id.chipThunderstorm -> viewModel.filterByCategory(DisasterType.THUNDERSTORM)
                else -> viewModel.filterByCategory(null)
            }
        }
    }

    private fun setupListeners() {
        binding.swipeRefreshAlerts.setOnRefreshListener {
            viewModel.loadData()
        }
    }

    private fun observeViewModel() {
        viewModel.filteredAlerts.observe(viewLifecycleOwner) { alerts ->
            alertsAdapter.submitList(alerts)
            binding.layoutEmptyState.visibility = if (alerts.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.swipeRefreshAlerts.isRefreshing = loading
            binding.progressBarAlerts.visibility = if (loading && alertsAdapter.itemCount == 0) View.VISIBLE else View.GONE
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
