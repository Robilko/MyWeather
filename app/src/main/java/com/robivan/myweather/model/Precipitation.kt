package com.robivan.myweather.model

import com.robivan.myweather.R

// как только научусь загружать картинки из интернета, этот класс не понадобится
enum class Precipitation (val icon: Int) {
    SUNNY(R.drawable.sunny),
    SUNNY_CLOUDY(R.drawable.sunny_cloudy),
    SNOWY(R.drawable.snowy),
    RAINY(R.drawable.rainy),
    RAINY_FLASH(R.drawable.rainy_flash),
    CLOUDY(R.drawable.cloudy)
}