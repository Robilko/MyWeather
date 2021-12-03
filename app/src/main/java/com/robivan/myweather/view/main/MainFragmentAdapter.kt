package com.robivan.myweather.view.main

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
        MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_main_recycler_item, parent, false) as View
        )

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) =
        holder.bind(weatherData[position])

    override fun getItemCount(): Int = weatherData.size

    fun removeListener() {
        onItemViewClickListener = null
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(weather: Weather) {
            itemView.apply {
                with(weather) {
                    findViewById<TextView>(R.id.main_fragment_recycler_item_text_view).text = this.city.cityName
                    setOnClickListener { onItemViewClickListener?.onItemViewClick(this) }
                }
            }
        }
    }
}