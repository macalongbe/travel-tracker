package com.traveltracker.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.traveltracker.app.ui.screens.AddRecordScreen
import com.traveltracker.app.ui.screens.HomeScreen
import com.traveltracker.app.ui.screens.StatsScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Add : Screen("add")
    object Stats : Screen("stats")
}

@Composable
fun TravelNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToAdd = { navController.navigate(Screen.Add.route) },
                onNavigateToStats = { navController.navigate(Screen.Stats.route) }
            )
        }
        composable(Screen.Add.route) {
            AddRecordScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Stats.route) {
            StatsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
