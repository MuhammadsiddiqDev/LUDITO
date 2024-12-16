package com.datasite.luditotest.screens.item

import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.datasite.luditotest.screens.isLocationEnabled
import com.datasite.luditotest.screens.requestLocationPermission
import com.datasite.luditotest.viewModel.DataTransferVM
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SuggestItem
import com.yandex.mapkit.search.SuggestOptions
import com.yandex.mapkit.search.SuggestResponse
import com.yandex.mapkit.search.SuggestSession
import com.yandex.runtime.Error
import kotlin.random.Random
@Composable
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

    val dataTransferVM: DataTransferVM = viewModel()

    val suggestions = remember { mutableStateOf(listOf<SuggestItem>()) }

    val searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    val suggestSession = searchManager.createSuggestSession()

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
                    CameraPosition(Point(latitude.value, longitude.value), 14f, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 0.1f),
                    null
                )

                val suggestOptions = SuggestOptions().setSuggestWords(true)
                val bottomLeft = Point(latitude.value , longitude.value)
                val topRight = Point(latitude.value, longitude.value)
                val boundingBox = BoundingBox(bottomLeft, topRight)

                suggestSession.suggest("", boundingBox, suggestOptions, object : SuggestSession.SuggestListener {
                    override fun onResponse(suggestionsl: SuggestResponse) {
                        val firstSuggestion = suggestionsl.items.firstOrNull()

                        firstSuggestion?.let { suggestion ->
                            dataTransferVM.title.value = ""
                            dataTransferVM.subTitle.value = ""
                            dataTransferVM.longitude.value = 0.0
                            dataTransferVM.latitude.value = 0.0

                            dataTransferVM.title.value = suggestion.title.text
                            dataTransferVM.subTitle.value = suggestion.subtitle?.text.orEmpty()
                            dataTransferVM.longitude.value = longitude.value
                            dataTransferVM.latitude.value = latitude.value
                        } ?: run {
                            dataTransferVM.title.value = "Tashkent"
                            dataTransferVM.subTitle.value = "Amir Temur xiyoboni"
                            dataTransferVM.longitude.value = longitude.value
                            dataTransferVM.latitude.value = latitude.value
                        }

                        suggestions.value = firstSuggestion?.let { listOf(it) } ?: emptyList()
                    }

                    override fun onError(error: Error) {
                        Log.e("YandexSuggestError", "Error: ${error.toString()}")
                    }
                })
            }
        }

        val locationRequest = LocationRequest.create().apply {
            interval = 600000
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
                            CameraPosition(Point(latitude.value, longitude.value), 14f, 0.0f, 0.0f),
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
