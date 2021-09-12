package com.robivan.myweather.view.main

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.robivan.myweather.R
import com.robivan.myweather.model.City

class MainFragmentAdapter(
    private var onItemViewClickListener:
    MainFragment.OnItemViewClickListener?
) : RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {
    private var cityData: List<City> = listOf()

    fun setWeather(data: List<City>) {
        cityData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
        MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_main_recycler_item, parent, false) as View
        )

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) =
        holder.bind(cityData[position])

    override fun getItemCount(): Int = cityData.size

    fun removeListener() {
        onItemViewClickListener = null
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(city: City) {
            itemView.apply {
                with(city) {
                    findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text = cityName
                    setOnClickListener { onItemViewClickListener?.onItemViewClick(this) }
                }
            }
        }
    }
}