package com.datasite.luditotest.screens.item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.datasite.luditotest.R
import com.datasite.luditotest.data.CacheModel

//@Preview(showBackground = true)
@Composable
fun MyAddressItem(data : CacheModel) {

    Row(modifier = Modifier
        .padding(vertical = 6.dp)
        .fillMaxWidth()
        .height(74.dp)
        .border(
            BorderStroke(1.dp, colorResource(id = R.color.white2)),
            shape = RoundedCornerShape(16.dp),
        )
        .clip(RoundedCornerShape(16))
        .background(colorResource(id = R.color.white), shape = RoundedCornerShape(16.dp))) {

        Column(modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,){

            Text(
                text = data.title,
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

            Text(
                text = data.subTitle,
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

        Box(modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxHeight()){
            Image(painter = painterResource(id = R.drawable.location_favourite),
                contentDescription = "location",
                modifier = Modifier
                    .size(32.dp)
                    .fillMaxHeight()
                    .align(Alignment.Center))
        }

    }

}