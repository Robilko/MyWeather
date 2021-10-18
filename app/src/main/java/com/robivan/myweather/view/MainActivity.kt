package com.robivan.myweather.view

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.robivan.myweather.R
import com.robivan.myweather.databinding.MainActivityBinding
import com.robivan.myweather.model.City
import com.robivan.myweather.model.NetworkChangeReceiver
import com.robivan.myweather.model.Weather
import com.robivan.myweather.view.contentProvider.ContactsFragment
import com.robivan.myweather.view.contentProvider.REQUEST_CODE
import com.robivan.myweather.view.details.DetailsFragment
import com.robivan.myweather.view.googlemaps.GoogleMapsFragment
import com.robivan.myweather.view.history.HistoryFragment
import com.robivan.myweather.view.main.MainFragment
import java.io.IOException

private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private lateinit var receiver: NetworkChangeReceiver

    private val permissionGeoResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            when {
                result -> getLocation()
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showRationaleDialog()
                }
                else -> {
                    showDialog(
                        getString(R.string.dialog_title_no_gps),
                        getString(R.string.dialog_message_no_gps)
                    )
                }
            }

        }

    private val onLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            getAddressAsync(location)
        }

        //Эти три метода нужно реализовывать и убирать из них super, иначе приложение крашится
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderDisabled(provider: String) {}
        override fun onProviderEnabled(provider: String) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        receiver = NetworkChangeReceiver()

        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(binding.container.id, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .add(binding.container.id, HistoryFragment.newInstance())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
                true
            }
            R.id.menu_contacts -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .add(binding.container.id, ContactsFragment.newInstance())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
                true
            }
            R.id.menu_location -> {
                permissionGeoResult.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                true
            }
            R.id.menu_maps -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .add(binding.container.id, GoogleMapsFragment())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_rationale_title))
            .setMessage(getString(R.string.dialog_rationale_message))
            .setPositiveButton(getString(R.string.dialog_give_access)) { _, _ -> requestPermission() }
            .setNegativeButton(getString(R.string.dialog_decline_access)) { dialog, _ -> dialog.dismiss() }
            .create().show()
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
    }

    private fun showDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
            .create().show()
    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Получить менеджер геолокаций
            val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                provider?.let {
                    // Будем получать геоположение через каждые 60 секунд или каждые 100 метров
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        REFRESH_PERIOD,
                        MINIMAL_DISTANCE,
                        onLocationListener
                    )
                }
            } else {
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location == null) {
                    showDialog(
                        getString(R.string.dialog_title_gps_turned_off),
                        getString(R.string.dialog_message_last_location_unknown)
                    )
                } else {
                    getAddressAsync(location)
                    showDialog(
                        getString(R.string.dialog_title_gps_turned_off),
                        getString(R.string.dialog_message_last_known_location)
                    )
                }
            }
        } else {
            showRationaleDialog()
        }
    }

    private fun getAddressAsync(location: Location) {
        val geoCoder = Geocoder(this)
        Thread {
            try {
                val addresses = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                binding.container.post {
                    showAddressDialog(addresses[0].getAddressLine(0), location)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun showAddressDialog(address: String, location: Location) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_address_title))
            .setMessage(address)
            .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                openDetailsFragment(Weather(City(address, location.latitude, location.longitude)))
            }.setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }

    private fun openDetailsFragment(weather: Weather) {
        supportFragmentManager.apply {
            beginTransaction()
                .add(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                    putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                })).addToBackStack("").commitAllowingStateLoss()
        }

    }
}