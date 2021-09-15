package com.robivan.myweather.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.robivan.myweather.R
import com.robivan.myweather.databinding.FragmentDetailsBinding
import com.robivan.myweather.model.*

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var cityBundle: City

    private val localBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.getStringExtra(RESULT_WEATHER_EXTRA)) {
                SUCCESS_RESULT -> {
                    intent.getParcelableExtra<FactDTO>(FACT_WEATHER_EXTRA)?.let {
                        displayWeather(it)
                    }
                }
                EMPTY_DATA_RESULT -> {
                    //TODO
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(localBroadcastReceiver, IntentFilter(DETAILS_INTENT_FILTER))
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(localBroadcastReceiver)
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cityBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: getDefaultCity()

        //Отправить координаты
        getWeather(cityBundle.lat, cityBundle.lon)
    }

    private fun getWeather(lat: Double, lon: Double) {
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        requireActivity().startService(Intent(requireContext(), MainService::class.java).apply {
            putExtra(LATITUDE_EXTRA, lat)
            putExtra(LONGITUDE_EXTRA, lon)
        })
    }

    private fun displayWeather(fact: FactDTO) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE
            val city = cityBundle
            cityName.text = city.cityName
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city.lat.toString(),
                city.lon.toString()
            )
            weatherCondition.text = fact.getConditionText()
            temperatureValue.text = fact.temp.toString().let { if (it.toInt() > 0) "+$it°" else "$it°" }
            feelsLikeValue.text = fact.feels_like.toString().let { if (it.toInt() > 0) "+$it°" else "$it°" }
            icon.setImageResource(R.drawable.sunny) //заглушка по картинке
        }
    }

    companion object {
        const val BUNDLE_EXTRA = "weather"
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}