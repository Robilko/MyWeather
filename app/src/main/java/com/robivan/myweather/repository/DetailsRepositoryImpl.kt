package com.robivan.myweather.repository

import com.robivan.myweather.model.WeatherDTO
import retrofit2.Callback


class DetailsRepositoryImpl(private val remoteDataSource: RemoteDataSource) : DetailsRepository {

    override fun getWeatherDetailsFromServer(lat: Double, lon: Double, callback: Callback<WeatherDTO>) {
        remoteDataSource.getWeatherDetails(lat,lon , callback)
    }
}