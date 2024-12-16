package com.datasite.luditotest.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.datasite.luditotest.R

@Composable
fun MyBottomNavigation(
    navController: NavHostController,
    selectedItemIndex: MutableState <Int>,
    onSelectedItemChanged : (Int) -> Unit) {
    var showBottomSheet1 = remember { mutableStateOf(false) }
    var isBoolean by remember { mutableStateOf(true) }



    val screen = listOf(
        BottomBarScreen.BookingScreen,
        BottomBarScreen.LocationScreen,
        BottomBarScreen.ProfileScreen,
    )

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val matchingIndex = screen.indexOfFirst { it.route == destination.route }
            if (matchingIndex != -1) {
                selectedItemIndex.value = matchingIndex
            }
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = currentDestination?.let {
        screen.any { item -> it.route == item.route }
    } ?: false

    if (bottomBarDestination) {
        Divider(color = colorResource(id = R.color.white), thickness = 2.dp)

        NavigationBar(
            Modifier.height(48.dp),
            containerColor = colorResource(id = R.color.white)
        ) {
            screen.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == item.route } == true && selectedItemIndex.value == index,
                    onClick = {

                        if (index == 0 && isBoolean){
                            onSelectedItemChanged(index)
                            isBoolean = true
                        }else{

                            isBoolean = false
                        }
                        showBottomSheet1.value = false
                        selectedItemIndex.value = index
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorResource(id = R.color.black),
                        unselectedIconColor = colorResource(id = R.color.gry),
                        indicatorColor = colorResource(id = R.color.white)
                    ),
                    label = {},
                    icon = {
                        Icon(
                        modifier = Modifier.size(26.dp),
                        tint = if (index == selectedItemIndex.value) colorResource(id = R.color.black)
                        else colorResource(id = R.color.gry),
                        painter = painterResource(id = item.selectedIcon),
                        contentDescription = item.route
                    )},
                    interactionSource = remember { MutableInteractionSource() },
                )
            }
        }
    }
}

