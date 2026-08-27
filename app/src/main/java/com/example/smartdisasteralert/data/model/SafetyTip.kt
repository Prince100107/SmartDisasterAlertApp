package com.example.smartdisasteralert.data.model

data class SafetyTip(
    val id: String,
    val disasterType: DisasterType,
    val title: String,
    val subtitle: String,
    val beforeSteps: List<String>,
    val duringSteps: List<String>,
    val afterSteps: List<String>,
    var isExpanded: Boolean = false
)
