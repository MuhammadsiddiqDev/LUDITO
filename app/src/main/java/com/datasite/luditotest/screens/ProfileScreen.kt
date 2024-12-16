package com.datasite.luditotest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.datasite.luditotest.R

@Composable
fun ProfileScreen(
    navController: NavHostController,
    modifier: Modifier
) {

    Column(modifier = modifier.fillMaxSize()
        .background(colorResource(id = R.color.white)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "ProfileScreen",
            fontSize = 24.sp,
            color = colorResource(id = R.color.black),
            maxLines = 1,
            fontWeight = FontWeight.W700
        )
    }

}