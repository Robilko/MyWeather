package com.robivan.myweather.model

import com.robivan.myweather.R
import kotlin.random.Random

data class Weather(
    val city: City = getDefaultCity(),
    val temperature: Int = Random.nextInt(-35, 35),
    val feelsLike: Int = temperature - Random.nextInt(0, 5)
)

fun getDefaultCity() = City(MyWeather.appContext!!.resources.getString(R.string.default_city), 55.755826, 37.617299900000035)
