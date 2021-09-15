package com.robivan.myweather.model

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.robivan.myweather.BuildConfig
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val MAIN_SERVICE_STRING_EXTRA = "MainServiceExtra"
const val LATITUDE_EXTRA = "Latitude"
const val LONGITUDE_EXTRA = "Longitude"
const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val FACT_WEATHER_EXTRA = "FACT WEATHER EXTRA"
const val RESULT_WEATHER_EXTRA = "RESULT WEATHER EXTRA"
const val SUCCESS_RESULT = "SUCCESS_RESULT"
const val EMPTY_DATA_RESULT = "EMPTY_DATA_RESULT"



class MainService(name: String = "MainService") : IntentService(name) {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        Log.d("MainService", "onHandleIntent ${intent?.getStringExtra(MAIN_SERVICE_STRING_EXTRA)}")

        intent?.let {
            val lat = intent.getDoubleExtra(LATITUDE_EXTRA, 0.0)
            val lon = intent.getDoubleExtra(LONGITUDE_EXTRA, 0.0)

            if (lat == 0.0 && lon == 0.0) {
                onEmptyData()
            } else {
                loadWeather(lat, lon)
            }
        } ?: onEmptyIntent()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadWeather(lat: Double, lon: Double) {
        lateinit var urlConnection: HttpsURLConnection
        val uri = try {
            URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")
        } catch (e: MalformedURLException) {
            onMalformedURL()
            return
        }


            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.apply {
                    requestMethod = "GET"
                    readTimeout = 10000
                    addRequestProperty("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY)
                }
                val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                // преобразование ответа от сервера (JSON) в модель данных (WeatherDTO)
                val weatherDTO: WeatherDTO = Gson().fromJson(getLines(bufferedReader), WeatherDTO::class.java)

                onResponse(weatherDTO)
            } catch (e: Exception) {
                onErrorResponse(e.message ?: "Unknown Error")
                Log.e("", "Fail connection", e)
            } finally {
                urlConnection.disconnect()
            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    private fun onResponse(weatherDTO: WeatherDTO) {
        weatherDTO.fact?.let {
            onSuccessResponse(it)
        } ?: onEmptyResponse()
    }

    private fun onEmptyResponse() {
        TODO("Not yet implemented")
    }

    private fun onSuccessResponse(factDTO: FactDTO) {
        LocalBroadcastManager.getInstance(this)
            .sendBroadcast(Intent(DETAILS_INTENT_FILTER)
                .putExtra(RESULT_WEATHER_EXTRA, SUCCESS_RESULT)
                .putExtra(FACT_WEATHER_EXTRA, factDTO))
    }


    private fun onEmptyData() {
        LocalBroadcastManager.getInstance(this)
            .sendBroadcast(Intent(DETAILS_INTENT_FILTER)
                .putExtra(RESULT_WEATHER_EXTRA, EMPTY_DATA_RESULT)
            )
    }

    private fun onErrorResponse(s: String) {
        TODO("Not yet implemented")
    }

    private fun onMalformedURL() {
        TODO("Not yet implemented")
    }

    private fun onEmptyIntent() {
        TODO("Not yet implemented")
    }
}