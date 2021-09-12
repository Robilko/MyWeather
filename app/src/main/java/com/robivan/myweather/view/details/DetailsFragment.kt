package com.robivan.myweather.view.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.robivan.myweather.R
import com.robivan.myweather.databinding.FragmentDetailsBinding
import com.robivan.myweather.model.Weather
import com.robivan.myweather.model.WeatherDTO
import com.robivan.myweather.model.WeatherLoader

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        val loader = WeatherLoader(onLoadListener, weatherBundle.city.lat, weatherBundle.city.lon)
        loader.loadWeather()
    }

    private val onLoadListener: WeatherLoader.WeatherLoaderListener =
        object : WeatherLoader.WeatherLoaderListener {
            override fun onLoaded(weatherDTO: WeatherDTO) {
                displayWeather(weatherDTO)
            }

            override fun onFailed(throwable: Throwable) {
                //Обработка ошибки
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), throwable.localizedMessage, Toast.LENGTH_LONG).show()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .remove(this@DetailsFragment)
                        .commitNow()
                }
            }
        }

    private fun displayWeather(weatherDTO: WeatherDTO) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE
            val city = weatherBundle.city
            cityName.text = city.city
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city.lat.toString(),
                city.lon.toString()
            )
            weatherCondition.text = weatherDTO.fact?.getConditionText()
            temperatureValue.text =
                weatherDTO.fact?.temp.toString().let { if (it.toInt() > 0) "+$it°" else "$it°" }
            feelsLikeValue.text = weatherDTO.fact?.feels_like.toString()
                .let { if (it.toInt() > 0) "+$it°" else "$it°" }
//            icon.setImageURI(Uri.parse("https://yastatic.net/weather/i/icons/funky/dark/${weatherDTO.fact?.icon}.svg")) // TODO  нужно научиться загружать изображения
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