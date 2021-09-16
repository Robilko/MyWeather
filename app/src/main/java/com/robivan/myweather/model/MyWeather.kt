package com.robivan.myweather.model

import android.app.Application
import android.content.Context

class MyWeather : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        var appContext: Context? = null
            private set
    }
}