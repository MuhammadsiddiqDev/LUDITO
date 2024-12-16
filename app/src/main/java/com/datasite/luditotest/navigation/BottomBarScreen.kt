package com.datasite.luditotest.navigation

import com.datasite.luditotest.R

sealed class BottomBarScreen(
    val route : String,
    val selectedIcon: Int,
) {
    object BookingScreen : BottomBarScreen(
        route = "BOOKING_SCREEN",
        selectedIcon = R.drawable.bxs_bookmark_alt)
    object LocationScreen : BottomBarScreen(
        route = "LOCATION_SCREEN",
        selectedIcon = R.drawable.tdesign_location_filled,
    )
    object ProfileScreen : BottomBarScreen(
        route = "PROFILE_SCREEN",
        selectedIcon = R.drawable.iconamoon_profile_fill)
}


