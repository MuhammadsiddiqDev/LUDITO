package com.datasite.luditotest.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val selectedItemIndex = rememberSaveable { mutableStateOf(0) }


    Scaffold(
        topBar = {},
        bottomBar = {
            MyBottomNavigation(
                navController = navController,
                selectedItemIndex = selectedItemIndex,
                onSelectedItemChanged = { newIndex ->
                    selectedItemIndex.value = newIndex
                }
            )
        }
    ) { paddingValue ->
        MainNavGraph(
            navController = navController,
            modifier = Modifier.padding(paddingValue))
    }
}
