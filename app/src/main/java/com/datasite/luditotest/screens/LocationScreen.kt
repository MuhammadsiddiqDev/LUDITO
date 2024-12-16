package com.datasite.luditotest.screens

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.datasite.luditotest.bottomSheet.AddCacheBottomSheet
import com.datasite.luditotest.bottomSheet.ResultBottomSheet
import com.datasite.luditotest.cache.AppCache
import com.datasite.luditotest.data.CacheModel
import com.datasite.luditotest.dialog.dialogCompose
import com.datasite.luditotest.screens.item.YandexMapSuggestScreen
import com.datasite.luditotest.screens.item.moveToUserLocation
import com.datasite.luditotest.viewModel.DataTransferVM
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SuggestItem
import com.yandex.mapkit.search.SuggestOptions
import com.yandex.mapkit.search.SuggestResponse
import com.yandex.mapkit.search.SuggestSession
import com.yandex.runtime.Error
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen (
    navController: NavHostController,
    modifier: Modifier
) {

    val dataTransferVM: DataTransferVM = viewModel()

    var openDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        dialogCompose(
            name = dataTransferVM.title,
            onDismissRequest = {
                dataTransferVM.title.value = ""
                dataTransferVM.subTitle.value = ""
                dataTransferVM.longitude.value = 0.0
                dataTransferVM.latitude.value = 0.0
                openDialog.value = false
            },
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
                openDialog.value = false
            }
        )
    }


    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var searchText = remember { mutableStateOf("") }
    var searchFocused = remember { mutableStateOf(false) }
    var showBottomSheet = remember { mutableStateOf(false) }
    var firstOpen = rememberSaveable { mutableStateOf(false) }
    var isLocation = rememberSaveable { mutableStateOf(false) }
    var isRandom = remember { mutableStateOf(false) }
    var initialLatitude = rememberSaveable{ mutableStateOf(41.311158) }
    var initialLongitude = rememberSaveable { mutableStateOf(69.279737) }
    val suggestions = remember { mutableStateOf(listOf<SuggestItem>()) }

    val cameraListener = remember { mutableStateOf<CameraListener?>(null) }

    YandexMapSuggestScreen(
        searchText.value,
        initialLatitude.value,
        initialLongitude.value
    ) { suggestionsList ->
        suggestions.value = suggestionsList
    }

    val searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    val suggestSession = searchManager.createSuggestSession()


    var latitude = rememberSaveable { mutableStateOf(initialLatitude.value ?: 41.311158) }
    var longitude = rememberSaveable { mutableStateOf(initialLongitude.value ?: 69.279737) }

    val mapView = remember { mutableStateOf<MapView?>(null) }
    val map = remember { mutableStateOf<Map?>(null) }

    val sheetState = rememberModalBottomSheetState()
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

    val scope = rememberCoroutineScope()

    var randomNumber = rememberSaveable { mutableStateOf(0) }

    BottomSheetScaffold(
        sheetContent = {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ){
                AddCacheBottomSheet(
                    rateCount = randomNumber,
                    onClickClose = {
                        scope.launch {
                            sheetState.hide()
                        }
                    },
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                        }
                        openDialog.value = true
                    }
                )
            }
        },
        sheetContainerColor = colorResource(id = R.color.white),
        sheetPeekHeight = 0.dp,
        sheetTonalElevation = 100.dp,
        scaffoldState = scaffoldState
    ) {
        ConstraintLayout(
            modifier = modifier.fillMaxSize()
        ) {
            val (mapViewRef, markerRef, searchTextField, addBtn, locationBtn, suggestList) = createRefs()

            if(isLocation.value){
                map.value?.let {
                    moveToUserLocation(
                        context,
                        latitude = latitude,
                        longitude = longitude,
                        fusedLocationClient,
                        it
                    )
                }

            }
            AndroidView(
                factory = { context ->
                    MapView(context).apply {
                        mapView.value = this
                        map.value = this.map
                        map.value?.move(
                            CameraPosition(
                                Point(latitude.value, longitude.value),
                                14.0f,
                                0.0f,
                                0.0f
                            )
                        )

                        if (!firstOpen.value) {

                            isRandom.value = true
                            randomNumber.value = Random.nextInt(1000, 5001)

                            isLocation.value = true

                            scope.launch {
                                sheetState.expand()
                            }
                        }

                        firstOpen.value = true

                        map.value?.isScrollGesturesEnabled = true
                        map.value?.isZoomGesturesEnabled = true

                        cameraListener.value = object : CameraListener {
                            override fun onCameraPositionChanged(
                                map: Map,
                                cameraPosition: CameraPosition,
                                cameraUpdateReason: CameraUpdateReason,
                                finished: Boolean
                            ) {

                                if (cameraUpdateReason == CameraUpdateReason.GESTURES && finished) {
                                    latitude.value = cameraPosition.target.latitude
                                    longitude.value = cameraPosition.target.longitude

                                    isRandom.value = true
                                    randomNumber.value = Random.nextInt(1000, 5001)

                                    val suggestOptions = SuggestOptions().setSuggestWords(true)
                                    val bottomLeft = Point(latitude.value , longitude.value)
                                    val topRight = Point(latitude.value, longitude.value)
                                    val boundingBox = BoundingBox(bottomLeft, topRight)

                                    suggestSession.suggest("", boundingBox, suggestOptions, object : SuggestSession.SuggestListener {
                                        override fun onResponse(suggestionsl: SuggestResponse) {
                                            val firstSuggestion = suggestionsl.items.firstOrNull()

                                            isLocation.value = false

                                            firstSuggestion?.let { suggestion ->
                                                dataTransferVM.title.value = ""
                                                dataTransferVM.subTitle.value = ""
                                                dataTransferVM.longitude.value = 0.0
                                                dataTransferVM.latitude.value = 0.0

                                                dataTransferVM.title.value = suggestion.title.text
                                                dataTransferVM.subTitle.value = suggestion.subtitle?.text.orEmpty()
                                                dataTransferVM.longitude.value = cameraPosition.target.longitude
                                                dataTransferVM.latitude.value = cameraPosition.target.latitude
                                            } ?: run {
                                                dataTransferVM.title.value = "Tashkent"
                                                dataTransferVM.subTitle.value = "Amir Temur xiyoboni"
                                                dataTransferVM.longitude.value = cameraPosition.target.longitude
                                                dataTransferVM.latitude.value = cameraPosition.target.latitude
                                            }

                                            suggestions.value = firstSuggestion?.let { listOf(it) } ?: emptyList()
                                        }

                                        override fun onError(error: Error) {
                                            Log.e("YandexSuggestError", "Error: ${error.toString()}")
                                        }
                                    })

                                    suggestions.value = emptyList()

                                    scope.launch {
                                        sheetState.expand()
                                    }

                                }
                            }
                        }

                        cameraListener.value?.let { map.value?.removeCameraListener(it) }
                        map.value?.addCameraListener(cameraListener.value!!)
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

                if (!searchFocused.value || !showBottomSheet.value) {
                    Box(
                        modifier = Modifier.padding(
                            top = 12.dp,
                            end = 16.dp,
                            start = 16.dp,
                            bottom = 4.dp,
                        )
                    ) {

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

                            Image(
                                painter = painterResource(id = R.drawable.icon_search),
                                modifier = Modifier.size(24.dp),
                                contentDescription = "search"
                            )

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

                    })
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

                    isLocation.value = true

                    scope.launch {
                        sheetState.expand()
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