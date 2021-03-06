package com.robivan.myweather.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.robivan.myweather.R
import com.robivan.myweather.databinding.FragmentMainBinding
import com.robivan.myweather.model.Weather
import com.robivan.myweather.utils.showSnackBar
import com.robivan.myweather.view.details.DetailsFragment
import com.robivan.myweather.viewmodel.AppState
import com.robivan.myweather.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var isDataSetRus: Boolean = true
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .replace(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                    })).addToBackStack("").commitAllowingStateLoss()
            }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        adapter.removeListener()
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainFragmentRecyclerView.adapter = adapter
        mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
        with(viewModel) {
            getLiveData().observe(viewLifecycleOwner, { renderData(it) })
            getWeatherFromLocalSourceRus()
        }
    }

    private fun changeWeatherDataSet() =
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceWorld()
            mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        } else {
            viewModel.getWeatherFromLocalSourceRus()
            mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        }.also { isDataSetRus = !isDataSetRus }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)
            }
            is AppState.Loading -> {
                mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                mainFragmentLoadingLayout.visibility = View.GONE
                mainFragmentRootView.showSnackBar(
                    appState.error.message!!,
                    getString(R.string.reload),
                    { viewModel.getWeatherFromLocalSourceRus() }
                )
            }
        }
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(weather: Weather)
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}