package com.datasite.luditotest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun RootNavigationGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Graph.MAIN_SCREEN_PAGE,
        route = Graph.ROOT
    ) {
        composable(route = Graph.MAIN_SCREEN_PAGE) {
            MainScreen()
        }
    }
}


object Graph {
    const val ROOT = "root_graph"
    const val MAIN_SCREEN_PAGE = "main_screen_graph"

}