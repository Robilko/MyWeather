package com.robivan.myweather.utils

import com.robivan.myweather.model.*
import com.robivan.myweather.room.HistoryEntity
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    val fact: FactDTO = weatherDTO.fact!!
    return listOf(
        Weather(
            getDefaultCity(),
            fact.temp!!,
            fact.feels_like!!,
            fact.getConditionText(),
            fact.icon!!
        )
    )
}

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather> {
    return entityList.map {
        Weather(City(it.city, 0.0,0.0), it.temperature, 0, it.condition, it.icon, it.timestamp)
    }
}

fun convertWeatherToEntity(weather: Weather): HistoryEntity {
    return HistoryEntity(0, weather.city.cityName, weather.temperature, weather.condition, weather.icon, weather.timestamp)
}

fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy hh:mm:ss", Locale.getDefault())
    return sdf.format(Date(Timestamp(System.currentTimeMillis()).time))
}
