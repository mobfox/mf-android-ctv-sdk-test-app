package com.market.ctvsampleapp.presentation.home

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.*
import com.market.ctvsampleapp.R

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * Can be used for requesting the device's location for testing the SDK feature of sending the location if available
     */
    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this, Array(1) { android.Manifest.permission.ACCESS_FINE_LOCATION },
                222
            )
        }

        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mFusedLocationClient.lastLocation
            .addOnSuccessListener(this) { location ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    Log.d("MainActivity", "Location: $location")
                } else {
                    Log.d("MainActivity", "Null location")
                }
            }
            .addOnFailureListener {
                Log.d("MainActivity", "Failed: ${it.message}")
            }

        startLocationUpdates(mFusedLocationClient)
    }

    private fun startLocationUpdates(fusedLocationClient: FusedLocationProviderClient) {
        fusedLocationClient.requestLocationUpdates(
            LocationRequest.create(),
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    for (location in locationResult.locations) {
                        Log.d("MainActivity", "Location: $location")
                    }
                }
            },
            Looper.getMainLooper()
        )
    }

}
