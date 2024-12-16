package com.datasite.luditotest.bottomSheet

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.datasite.luditotest.R
import com.datasite.luditotest.myRes.MyEditText
import com.datasite.luditotest.screens.item.ResultItem
import com.datasite.luditotest.screens.item.YandexMapSuggestScreen
import com.datasite.luditotest.viewModel.DataTransferVM
import com.yandex.mapkit.Animation
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.search.SuggestItem
import kotlinx.coroutines.launch

//@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultBottomSheet(
    map : MutableState<Map?>,
    latitude : MutableState<Double>,
    longitude : MutableState<Double>,
    suggestions : MutableState<List<SuggestItem>>,
    showBottomSheet :MutableState<Boolean>,
    onClick:() -> Unit
) : MutableState<Boolean> {

    val dataTransferVM : DataTransferVM = viewModel()

    var searchText = remember { mutableStateOf("") }

    val modalBottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    YandexMapSuggestScreen(searchText.value, latitude.value, longitude.value) { suggestionsList ->
        suggestions.value = suggestionsList
    }

    ModalBottomSheet(

        onDismissRequest = {

        coroutineScope.launch {
            showBottomSheet.value = false
        }
    },
        modifier = Modifier
            .imePadding()
            .statusBarsPadding(),
        sheetState = modalBottomSheetState,
        containerColor = colorResource(id = R.color.white),
        dragHandle = { BottomSheetDefaults.DragHandle() },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.white))
            ) {

                Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)){
                    MyEditText(
                        colorBase = R.color.gry4,
                        text = searchText.value,
                        placeHolder = "Поиск",
                        isFocused = true,
                        isError = false,
                        onTextChanged = { newText ->
                            searchText.value = newText
                            showBottomSheet.value = true
                        },
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .height(168.dp)
                        .background(colorResource(id = R.color.white))
                ) {

                    items(suggestions.value.size) { item ->
                        val suggestion = suggestions.value[item]

                        Spacer(modifier = Modifier
                            .height(2.dp)
                            .background(colorResource(id = R.color.white2))
                            .fillMaxWidth())

                        item?.let {
                            Column(
                                modifier = Modifier
                            ) {
                                ResultItem(suggestions.value[item]){
                                    val point = suggestion.center
                                    if (point != null) {
                                        map.value?.move(
                                            CameraPosition(point, 17.0f, 0.0f, 0.0f),
                                            Animation(Animation.Type.SMOOTH, 1f),
                                            null
                                        )
                                    } else {
                                        Log.e(
                                            "SuggestItem",
                                            "Center is null for suggestion: ${suggestion.displayText}"
                                        )
                                    }
                                    suggestions.value = emptyList()
                                    searchText.value = ""

                                    if (point != null) {
                                        latitude.value = point!!.latitude ?: 41.311158
                                        longitude.value = point!!.longitude ?: 69.279737

                                    } else {
                                        Log.e(
                                            "SuggestItem",
                                            "Center is null for suggestion: ${suggestion.displayText}"
                                        )

                                        latitude.value = 41.311158
                                        longitude.value = 69.279737
                                    }


                                    dataTransferVM.subTitle.value = suggestion.subtitle?.text ?: ""
                                    dataTransferVM.title.value = suggestion.title.text
                                    dataTransferVM.latitude.value = latitude.value ?: 0.0
                                    dataTransferVM.longitude.value = longitude.value ?: 0.0

                                    onClick()

                                    showBottomSheet.value = false
                                }
                            }
                        }
                    }
                    item{
                        Spacer(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    )
    return showBottomSheet
}

