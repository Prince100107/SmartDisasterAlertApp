package com.example.smartdisasteralert.data.model

import com.google.gson.annotations.SerializedName

data class UsgsResponse(
    @SerializedName("type") val type: String?,
    @SerializedName("features") val features: List<UsgsFeature>?
)

data class UsgsFeature(
    @SerializedName("id") val id: String?,
    @SerializedName("properties") val properties: UsgsProperties?,
    @SerializedName("geometry") val geometry: UsgsGeometry?
)

data class UsgsProperties(
    @SerializedName("mag") val mag: Double?,
    @SerializedName("place") val place: String?,
    @SerializedName("time") val time: Long?,
    @SerializedName("title") val title: String?,
    @SerializedName("alert") val alert: String?,
    @SerializedName("tsunami") val tsunami: Int?,
    @SerializedName("felt") val felt: Int?
)

data class UsgsGeometry(
    @SerializedName("coordinates") val coordinates: List<Double>?
)
