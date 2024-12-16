package com.datasite.luditotest.screens.item

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SuggestItem
import com.yandex.mapkit.search.SuggestOptions
import com.yandex.mapkit.search.SuggestResponse
import com.yandex.mapkit.search.SuggestSession
import com.yandex.runtime.Error


@Composable
fun YandexMapSuggestScreen(
    query: String,
    latitude: Double,
    longitude: Double,
    onResultsReceived: (List<SuggestItem>) -> Unit
) {
    LaunchedEffect(query) {
        if (query.isNotEmpty()) {
            val searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
            val suggestSession = searchManager.createSuggestSession()

            val suggestOptions = SuggestOptions().setSuggestWords(true)
            val bottomLeft = Point(latitude - 0.05, longitude - 0.05)
            val topRight = Point(latitude + 0.05, longitude + 0.05)
            val boundingBox = BoundingBox(bottomLeft, topRight)

            suggestSession.suggest(query, boundingBox, suggestOptions, object : SuggestSession.SuggestListener {
                override fun onResponse(suggestions: SuggestResponse) {
                    val suggestionList = suggestions.items
                    onResultsReceived(suggestionList)
                }

                override fun onError(error: Error) {
                    Log.e("YandexSuggestError", "Error: ${error.toString()}")
                }
            })
        }
    }
}