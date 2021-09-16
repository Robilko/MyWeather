package com.robivan.myweather.viewmodel

import com.robivan.myweather.model.City

sealed class AppState {
    data class Success(val cityData: List<City>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
