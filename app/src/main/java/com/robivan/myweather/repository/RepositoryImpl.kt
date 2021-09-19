package com.robivan.myweather.repository

import com.robivan.myweather.model.Weather
import com.robivan.myweather.model.getRussianCities
import com.robivan.myweather.model.getWorldCities

class RepositoryImpl : Repository {
    override fun getWeatherFromServer(): Weather = Weather()
    override fun getWeatherFromLocalStorageRus(): List<Weather> = getRussianCities()
    override fun getWeatherFromLocalStorageWorld(): List<Weather> = getWorldCities()
}