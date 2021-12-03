package com.robivan.myweather.repository

import com.robivan.myweather.model.Weather
import com.robivan.myweather.room.HistoryDao
import com.robivan.myweather.utils.convertHistoryEntityToWeather
import com.robivan.myweather.utils.convertWeatherToEntity

class LocalRepositoryImpl(private val localDataSource: HistoryDao) : LocalRepository {
    override fun getAllHistory(): List<Weather> = convertHistoryEntityToWeather(localDataSource.all())


    override fun saveEntity(weather: Weather) { localDataSource.insert(convertWeatherToEntity(weather)) }

}