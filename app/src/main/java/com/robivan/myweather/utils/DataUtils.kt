package com.robivan.myweather.utils

import com.robivan.myweather.model.FactDTO
import com.robivan.myweather.model.Weather
import com.robivan.myweather.model.WeatherDTO
import com.robivan.myweather.model.getDefaultCity

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    val fact: FactDTO = weatherDTO.fact!!
    return listOf(
        Weather(
            getDefaultCity(),
            fact.temp!!,
            fact.feels_like!!,
            fact.getConditionText(),
            fact.icon
        )
    )
}