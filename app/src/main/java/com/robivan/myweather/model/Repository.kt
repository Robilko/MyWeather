package com.robivan.myweather.model

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorageRus(): List<City>
    fun getWeatherFromLocalStorageWorld(): List<City>
}