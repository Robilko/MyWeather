package com.robivan.myweather.model

import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    val fact: FactDTO?
)

data class FactDTO(
    val temp: Int?,
    val feels_like: Int?,
    @SerializedName("condition") val condition: Condition?,
    val icon: String?
) {
    fun getConditionText(): String {
        val contextResources = MyWeather.appContext?.resources
        return condition?.let { contextResources?.getString(it.value) } ?: "-"
    }
}


