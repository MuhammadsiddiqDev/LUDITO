package com.datasite.luditotest.screens

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.datasite.luditotest.R
import com.datasite.luditotest.bottomSheet.DetailBottomSheet
import com.datasite.luditotest.bottomSheet.ResultBottomSheet
import com.datasite.luditotest.cache.AppCache
import com.datasite.luditotest.data.CacheModel
import com.datasite.luditotest.dialog.dialogCompose
import com.datasite.luditotest.screens.item.YandexMapSuggestScreen
import com.datasite.luditotest.screens.item.moveToUserLocation
import com.datasite.luditotest.viewModel.DataTransferVM
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SuggestItem
import com.yandex.mapkit.search.SuggestOptions
import com.yandex.mapkit.search.SuggestResponse
import com.yandex.mapkit.search.SuggestSession
import com.yandex.runtime.Error

@Composable
fun LocationScreen (
    navController: NavHostController,
    modifier: Modifier
){

    val dataTransferVM : DataTransferVM = viewModel()

    var openDialog = remember { mutableStateOf(false) }

    if (openDialog.value){
        dialogCompose(
            name = dataTransferVM.title,
            onDismissRequest = {
                dataTransferVM.title.value = ""
                dataTransferVM.subTitle.value = ""
                dataTransferVM.longitude.value = 0.0
                dataTransferVM.latitude.value = 0.0
                openDialog.value = false },
            onConfirm = {

                val cacheModel = CacheModel(
                    latitude = dataTransferVM.latitude.value,
                    longitude = dataTransferVM.longitude.value,
                    title = dataTransferVM.title.value,
                    subTitle = dataTransferVM.subTitle.value
                )

                AppCache.appCache?.saveCacheModel(cacheModel)

                dataTransferVM.title.value = ""
                dataTransferVM.subTitle.value = ""
                dataTransferVM.longitude.value = 0.0
                dataTransferVM.latitude.value = 0.0
                openDialog.value = false}
        )
    }


    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var searchText = remember { mutableStateOf("") }
    var searchFocused = remember { mutableStateOf(false) }
    var showBottomSheet = remember { mutableStateOf(false) }
    var showBottomSheetDetail = remember { mutableStateOf(false) }
    var initialLatitude = remember { mutableStateOf(41.311158) }
    var initialLongitude = remember { mutableStateOf(69.279737) }
    val suggestions = remember { mutableStateOf(listOf<SuggestItem>()) } // SuggestItem ni saqlash

    YandexMapSuggestScreen(searchText.value, initialLatitude.value, initialLongitude.value) { suggestionsList ->
        suggestions.value = suggestionsList
    }

    var latitude = remember { mutableStateOf(initialLatitude.value ?: 41.311158) }
    var longitude = remember { mutableStateOf(initialLongitude.value ?: 69.279737) }

    val mapView = remember { mutableStateOf<MapView?>(null) }
    val map = remember { mutableStateOf<Map?>(null) }

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (mapViewRef, markerRef, searchTextField, addBtn, locationBtn,suggestList) = createRefs()

        // MapView in ConstraintLayout
        AndroidView(
            factory = { context ->
                MapView(context).apply {
                    mapView.value = this
                    map.value = this.map
                    map.value?.move(
                        CameraPosition(Point(latitude.value, longitude.value), 14.0f, 0.0f, 0.0f)
                    )

                    map.value?.isScrollGesturesEnabled = true
                    map.value?.isZoomGesturesEnabled = true

                    map.value?.addCameraListener(object : CameraListener {
                        override fun onCameraPositionChanged(
                            map: Map,
                            cameraPosition: CameraPosition,
                            cameraUpdateReason: CameraUpdateReason,
                            finished: Boolean
                        ) {

                            if (finished) {
                                latitude.value = cameraPosition.target.latitude
                                longitude.value = cameraPosition.target.longitude
                            }
                        }
                    })
                }
            },
            modifier = Modifier
                .constrainAs(mapViewRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        )

        Image(
            painter = painterResource(id = R.drawable.location_marker),
            contentDescription = null,
            modifier = Modifier
                .size(46.dp)
                .constrainAs(markerRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Row(modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .constrainAs(searchTextField) {
                top.linkTo(parent.top)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }) {

            if (!searchFocused.value || !showBottomSheet.value){
                Box(modifier = Modifier.padding(top = 12.dp, end = 16.dp, start = 16.dp, bottom = 4.dp, )){

                    Button(modifier = Modifier
                        .background(
                            colorResource(id = R.color.white),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .height(64.dp)
                        .border(
                            BorderStroke(8.dp, colorResource(id = R.color.white)),
                            shape = RoundedCornerShape(16.dp),
                        )
                        .fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.gry4)),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            showBottomSheet.value = true
                            searchFocused.value = true
                        }
                    ) {

                        Image(painter = painterResource(id = R.drawable.icon_search),
                            modifier = Modifier.size(24.dp),
                            contentDescription = "search")
                        
                        Spacer(modifier = Modifier.size(12.dp))

                        Text(
                            text = if (dataTransferVM.title.value == "") "Поиск" else dataTransferVM.title.value,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.black),
                            maxLines = 1,
                            lineHeight = 20.sp,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.W700
                        )
                    }

                }
            }

            if (showBottomSheet.value) showBottomSheet = ResultBottomSheet(
                suggestions = suggestions,
                map = map,
                latitude = latitude,
                longitude = longitude,
                showBottomSheet = showBottomSheet,
                onClick = {
                    showBottomSheetDetail.value = true
                })

            if (showBottomSheetDetail.value){
                showBottomSheetDetail = DetailBottomSheet(
                    showBottomSheet = showBottomSheetDetail,
                    onClickClose = {
                        showBottomSheetDetail.value = false
                        showBottomSheet.value = false
                    },
                    onClick = {
                        showBottomSheetDetail.value = false
                        showBottomSheet.value = false
                        openDialog.value = true

                    }
                )
            }
        }
        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .constrainAs(locationBtn) {
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    end.linkTo(parent.end)
                }
                .size(64.dp),
            shape = RoundedCornerShape(100.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.white)),
            onClick = {

                map.value?.let {

                    moveToUserLocation(context, latitude = latitude, longitude = longitude, fusedLocationClient, it)
                }
            }) {
            Icon(
                painter = painterResource(id = R.drawable.tabler_location_filled),
                tint = colorResource(id = R.color.black),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )

        }
    }
}

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

fun requestLocationPermission(context: Context) {
    if (ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    } else {
        Toast.makeText(context, "Permission already granted", Toast.LENGTH_SHORT).show()
    }
}

const val LOCATION_PERMISSION_REQUEST_CODE = 1001