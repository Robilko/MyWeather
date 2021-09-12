package com.robivan.myweather.model

class RepositoryImpl : Repository {
    override fun getWeatherFromServer(): Weather = Weather()
    override fun getWeatherFromLocalStorageRus(): List<City> = getRussianCities()
    override fun getWeatherFromLocalStorageWorld(): List<City> = getWorldCities()
}