package com.datasite.luditotest.screens.item


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.datasite.luditotest.R
import com.yandex.mapkit.search.SuggestItem

@Composable
fun ResultItem(
    suggestList : SuggestItem,
    onClick : () -> Unit) {

    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onClick()
        }
        .height(74.dp)
        .background(colorResource(id = R.color.white))) {

        Box(modifier = Modifier
            .padding(start = 16.dp)
            .fillMaxHeight()){
            Icon(painter = painterResource(id = R.drawable.tdesign_location_filled),
                contentDescription = "location",
                tint = colorResource(id = R.color.gry5),
                modifier = Modifier
                    .size(32.dp)
                    .fillMaxHeight()
                    .align(Alignment.Center))
        }

        Column(modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.Center,){

            Text(
                text = suggestList.displayText.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.white)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.black),
                maxLines = 1,
                lineHeight = 20.sp,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.W700
            )

            Spacer(modifier = Modifier.size(4.dp))

            suggestList.subtitle?.let {
                Text(
                    text = it.text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.white)),
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.gry2),
                    maxLines = 1,
                    lineHeight = 20.sp,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.W700
                )
            }
        }

        Box(modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 26.dp)
            .fillMaxHeight()){
            suggestList.distance?.let {
                Text(
                    text = it.text ,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(colorResource(id = R.color.white)),
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.black),
                    maxLines = 1,
                    lineHeight = 20.sp,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W700
                )
            }
        }
    }
}