package com.robivan.myweather.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class WeatherDTO(
    val fact: FactDTO?
)

@Parcelize
data class FactDTO(
    val temp: Int?,
    val feels_like: Int?,
    @SerializedName("condition") val condition: Condition?,
    val icon: String?
) : Parcelable{
    fun getConditionText(): String {
        val contextResources = App.appContext?.resources
        return condition?.let { contextResources?.getString(it.value) } ?: "-"
    }
}


