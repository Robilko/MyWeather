package com.robivan.myweather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robivan.myweather.R
import com.robivan.myweather.model.MyWeather
import com.robivan.myweather.model.Repository
import com.robivan.myweather.model.RepositoryImpl
import java.lang.Thread.sleep
import kotlin.random.Random

class MainViewModel (private val liveDataToObserver: MutableLiveData<AppState> = MutableLiveData(),
                     private val repositoryImpl: Repository = RepositoryImpl()) : ViewModel() {
    fun getLiveData() = liveDataToObserver

    fun getWeatherFromLocalSource() = getDataFromLocalSource()

    fun getWeatherFromRemoteSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserver.value = AppState.Loading
        Thread {
            sleep(1000)
            if(Random.nextBoolean()) {
                liveDataToObserver.postValue(AppState.Success(repositoryImpl.getWeatherFromLocalStorage()))
            } else {
                liveDataToObserver.postValue(AppState.Error(Exception(MyWeather.appContext!!.resources.getString(R.string.server_error))))
            }

        }.start()
    }
}