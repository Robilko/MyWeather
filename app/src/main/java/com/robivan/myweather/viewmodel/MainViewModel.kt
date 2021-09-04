package com.robivan.myweather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robivan.myweather.model.Repository
import com.robivan.myweather.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserver: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) : ViewModel() {

    fun getLiveData() = liveDataToObserver

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)

    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)

//    fun getWeatherFromRemoteSource() = getDataFromLocalSource(isRussian = true)

    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserver.value = AppState.Loading
        Thread {
            sleep(1000)
//            if (Random.nextBoolean()) {
            liveDataToObserver.postValue(
                AppState.Success(
                    if (isRussian) repositoryImpl.getWeatherFromLocalStorageRus()
                    else repositoryImpl.getWeatherFromLocalStorageWorld()
                )
            )
//            } else {
//                liveDataToObserver.postValue(
//                    AppState.Error(
//                        Exception(
//                            MyWeather.appContext!!.resources.getString(
//                                R.string.server_error
//                            )
//                        )
//                    )
//                )
//            }

        }.start()
    }
}