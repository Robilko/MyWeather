package com.robivan.myweather.view.details

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.robivan.myweather.R
import com.robivan.myweather.databinding.FragmentDetailsBinding
import com.robivan.myweather.model.City
import com.robivan.myweather.model.Weather
import com.robivan.myweather.utils.showSnackBar
import com.robivan.myweather.viewmodel.AppState
import com.robivan.myweather.viewmodel.DetailsViewModel

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
        viewModel.detailsLiveData.observe(viewLifecycleOwner, { displayWeather(it) })
        viewModel.getWeatherFromRemoteSource(weatherBundle.city.lat, weatherBundle.city.lon)
    }

    private fun displayWeather(appState: AppState) {
        with(binding) {
            when (appState) {
                is AppState.Success -> {
                    mainView.visibility = View.VISIBLE
                    binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                    setWeather(appState.weatherData[0])
                }
                is AppState.Loading -> {
                    mainView.visibility = View.GONE
                    binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
                }
                is AppState.Error -> {
                    mainView.visibility = View.VISIBLE
                    binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                    mainView.showSnackBar(
                        getString(R.string.error),
                        getString(R.string.reload),
                        {
                            viewModel.getWeatherFromRemoteSource(weatherBundle.city.lat, weatherBundle.city.lon)
                        }
                    )
                }
            }
        }
    }

    private fun setWeather(weather: Weather) {
        val city = weatherBundle.city
        saveCity(city, weather)
        with(binding) {
            cityName.text = city.cityName
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city.lat.toString(),
                city.lon.toString()
            )
            temperatureValue.text =
                weather.temperature.toString().let { if (it.toInt() > 0) "+$it°" else "$it°" }
            feelsLikeValue.text =
                weather.feelsLike.toString().let { if (it.toInt() > 0) "+$it°" else "$it°" }
            weatherCondition.text = weather.condition
            headerIcon.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
            weather.icon.let { GlideToVectorYou.justLoadImage(activity, Uri.parse("https://yastatic.net/weather/i/icons/blueye/color/svg/${it}.svg"), icon) }
        }
    }

    private fun saveCity(city: City, weather: Weather) {
        viewModel.saveCityToDB(Weather(city, weather.temperature, weather.feelsLike, weather.condition, weather.timestamp))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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