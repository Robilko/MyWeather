package com.robivan.myweather.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.robivan.myweather.R
import com.robivan.myweather.model.Weather

class MainFragmentAdapter(
    private var onItemViewClickListener:
    MainFragment.OnItemViewClickListener?
) : RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {
    private var weatherData: List<Weather> = listOf()

    fun setWeather(data: List<Weather>) {
        weatherData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_main_recycler_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(weatherData[position])
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    fun removeListener() {
        onItemViewClickListener = null
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: Weather) {
            itemView.findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text =
                weather.city.city
            var temperature = "${(weather.temperature)}°"
            if (weather.temperature > 0) temperature = "+$temperature"
            itemView.findViewById<TextView>(R.id.mainFragmentRecyclerItemTemperatureTextView).text =
                temperature
            itemView.findViewById<ImageView>(R.id.mainFragmentRecyclerItemImageView)
                .setImageResource(weather.precipitation)
            itemView.setOnClickListener {
                onItemViewClickListener?.onItemViewClick(weather)
            }
        }

    }
}