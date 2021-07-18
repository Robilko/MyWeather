package com.robivan.myweather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robivan.myweather.model.Repository
import com.robivan.myweather.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel (private val liveDataToObserver: MutableLiveData<AppState> = MutableLiveData(),
                     private val repositoryImpl: Repository = RepositoryImpl()) : ViewModel() {
    fun getLiveData() = liveDataToObserver

    fun getWeatherFromLocalSource() = getDataFromLocalSource()

    fun getWeatherFromRemoteSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserver.value = AppState.Loading
        Thread {
            sleep(1000)
            liveDataToObserver.postValue(AppState.Success(repositoryImpl.getWeatherFromLocalStorage()))
        }.start()
    }
}