package com.robivan.myweather.model

import com.google.gson.annotations.SerializedName
import com.robivan.myweather.R

enum class Condition(val value: Int) {
    @SerializedName("clear")
    CLEAR(R.string.clear),
    @SerializedName("partly-cloudy")
    PARTY_CLOUDY(R.string.partly_cloudy),
    @SerializedName("cloudy")
    CLOUDY(R.string.cloudy),
    @SerializedName("overcast")
    OVERCAST(R.string.overcast),
    @SerializedName("drizzle")
    DRIZZLE(R.string.drizzle),
    @SerializedName("light-rain")
    LIGHT_RAIN(R.string.light_rain),
    @SerializedName("rain")
    RAIN(R.string.rain),
    @SerializedName("moderate-rain")
    MODERATE_RAIN(R.string.moderate_rain),
    @SerializedName("heavy-rain")
    HEAVY_RAIN(R.string.heavy_rain),
    @SerializedName("continuous-heavy-rain")
    CONTINUOUS_HEAVY_RAIN(R.string.continuous_heavy_rain),
    @SerializedName("showers")
    SHOWERS(R.string.showers),
    @SerializedName("wet-snow")
    WET_SNOW(R.string.wet_snow),
    @SerializedName("light-snow")
    LIGHT_SNOW(R.string.light_snow),
    @SerializedName("snow")
    SNOW(R.string.snow),
    @SerializedName("snow-showers")
    SNOW_SHOWERS(R.string.snow_showers),
    @SerializedName("hail")
    HAIL(R.string.hail),
    @SerializedName("thunderstorm")
    THUNDERSTORM(R.string.thunderstorm),
    @SerializedName("thunderstorm-with-rain")
    THUNDERSTORM_WITH_RAIN(R.string.thunderstorm_with_rain),
    @SerializedName("thunderstorm-with-hail")
    THUNDERSTORM_WITH_HAIL(R.string.thunderstorm_with_hail)
}