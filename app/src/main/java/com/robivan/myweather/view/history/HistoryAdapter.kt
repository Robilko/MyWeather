package com.robivan.myweather.view.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.robivan.myweather.R
import com.robivan.myweather.model.Weather
import kotlinx.android.synthetic.main.fragment_history_recycler_item.view.*

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.RecyclerItemViewHolder>() {

    private var data: List<Weather> = arrayListOf()

    fun setData(data: List<Weather>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAdapter.RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_history_recycler_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: Weather) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.history_recycler_view_item_text.text = String.format(
                    "%s \n%s %s \n%s",
                    data.city.cityName,
                    data.temperature.let { if (it > 0) "+$it°" else "$it°" },
                    data.condition,
                    data.timestamp
                )
                itemView.setOnClickListener {
                    Toast.makeText(
                        itemView.context,
                        "on click: ${data.city.cityName}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}