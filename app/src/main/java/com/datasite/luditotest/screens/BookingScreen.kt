package com.datasite.luditotest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.datasite.luditotest.R
import com.datasite.luditotest.cache.AppCache
import com.datasite.luditotest.dialog.dialogDeleteCompose
import com.datasite.luditotest.screens.item.MyAddressItem

@Composable
fun BookingScreen(
    navController: NavHostController,
    modifier: Modifier) {

    var cacheModelList = remember {
        mutableStateOf(AppCache.appCache?.getCacheModelList() ?: emptyList())
    }

    var openDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {

        dialogDeleteCompose(
            onDismissRequest = {
                openDialog.value = false
            },
            onConfirm = {
                AppCache.appCache!!.clearCacheModelList()
                cacheModelList.value = AppCache.appCache!!.getCacheModelList()
                openDialog.value = false
            }
        )
    }

    Column(modifier = modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.gry3))) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(colorResource(id = R.color.white)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Text(modifier = Modifier.weight(1f),
                text = "Мои адреса",
                fontSize = 24.sp,
                color = colorResource(id = R.color.black),
                maxLines = 1,
                lineHeight = 28.8.sp,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W700
            )

            if (cacheModelList.value.isNotEmpty()){
                Icon(imageVector = Icons.Default.Delete,
                    tint = colorResource(id = R.color.black),
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            openDialog.value = true
                        },
                    contentDescription = "delete")
            }

            Spacer(modifier = Modifier.size(16.dp))
        }

        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)){

            items(
                count = cacheModelList.value.size,
                key = { index -> cacheModelList.value[index].hashCode() },
                itemContent = { index ->
                    val model = cacheModelList.value[index]
                    MyAddressItem(model)
                }
            )
        }

    }
}