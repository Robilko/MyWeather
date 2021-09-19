package com.robivan.myweather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.robivan.myweather.R
import com.robivan.myweather.databinding.FragmentDetailsBinding
import com.robivan.myweather.model.Weather
import com.robivan.myweather.utils.showSnackBar
import com.robivan.myweather.viewmodel.AppState
import com.robivan.myweather.viewmodel.DetailsViewModel

private const val MAIN_LINK = "https://api.weather.yandex.ru/v2/informers?"

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
        viewModel.getLiveData().observe(viewLifecycleOwner, { displayWeather(it) })
        viewModel.getWeatherFromRemoteSource(MAIN_LINK + "lat=${weatherBundle.city.lat}&lon=${weatherBundle.city.lon}")
    }

    private fun displayWeather(appState: AppState) {
        with(binding) {
            when (appState) {
                is AppState.Success -> {
                    mainView.visibility = View.VISIBLE
                    loadingLayout.visibility = View.GONE
                    setWeather(appState.weatherData[0])
                }
                is AppState.Loading -> {
                    mainView.visibility = View.GONE
                    loadingLayout.visibility = View.VISIBLE
                }
                is AppState.Error -> {
                    mainView.visibility = View.VISIBLE
                    loadingLayout.visibility = View.GONE
                    mainView.showSnackBar(
                        getString(R.string.error),
                        getString(R.string.reload),
                        {
                            viewModel.getWeatherFromRemoteSource(
                                MAIN_LINK +
                                        "lat=${weatherBundle.city.lat}&lon=${weatherBundle.city.lon}"
                            )
                        }
                    )
                }
            }
        }
    }

    private fun setWeather(weather: Weather) {
        val city = weatherBundle.city
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
            icon.setImageResource(R.drawable.sunny) //заглушка по картинке
        }

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