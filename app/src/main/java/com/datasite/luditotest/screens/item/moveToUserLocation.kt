package com.datasite.luditotest.screens.item

import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.core.app.ActivityCompat
import com.datasite.luditotest.screens.isLocationEnabled
import com.datasite.luditotest.screens.requestLocationPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map

fun moveToUserLocation(
    context: Context,
    latitude: MutableState<Double>,
    longitude: MutableState<Double>,
    fusedLocationClient: FusedLocationProviderClient,
    map : Map
) {
    if (!isLocationEnabled(context)) {
        Toast.makeText(context,"пожалуйста, включите местоположение", Toast.LENGTH_LONG).show()
        return
    }

    val currentCameraPosition = map.cameraPosition
    val currentZoom = currentCameraPosition.zoom

    if (ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                latitude.value = it.latitude
                longitude.value = it.longitude

                map.move(
                    CameraPosition(Point(latitude.value, longitude.value), currentZoom, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 1f),
                    null
                )
            }
        }

        val locationRequest = LocationRequest.create().apply {
            interval = 60000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    latitude.value = location.latitude
                    longitude.value = location.longitude

                    try {
                        map.move(
                            CameraPosition(Point(latitude.value, longitude.value), currentZoom, 0.0f, 0.0f),
                            Animation(Animation.Type.SMOOTH, 1f),
                            null
                        )

                    }catch (e:Exception){

                    }
                }
            }
        }, Looper.getMainLooper())
    } else {
        requestLocationPermission(context)
    }
}
