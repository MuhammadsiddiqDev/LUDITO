package com.datasite.luditotest.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.datasite.luditotest.screens.BookingScreen
import com.datasite.luditotest.screens.LocationScreen
import com.datasite.luditotest.screens.ProfileScreen

@Composable
fun MainNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {

    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.BookingScreen.route
    ) {
        // Home Screen
        composable(route = BottomBarScreen.BookingScreen.route) {
            BookingScreen(
            modifier = modifier,
            navController = navController)
        }

        // Search Screen
        composable(route = BottomBarScreen.LocationScreen.route) {
            LocationScreen(modifier = modifier, navController = navController)
        }

        // Add Screen
        composable(
            route = BottomBarScreen.ProfileScreen.route,
        ) { backStackEntry ->
            ProfileScreen(
                navController = navController,
                modifier = modifier,
            )
        }
    }
}