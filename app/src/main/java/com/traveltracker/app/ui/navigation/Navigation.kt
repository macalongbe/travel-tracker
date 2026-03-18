package com.traveltracker.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.traveltracker.app.data.local.TravelRecord
import com.traveltracker.app.ui.screens.AddRecordScreen
import com.traveltracker.app.ui.screens.HomeScreen
import com.traveltracker.app.ui.screens.StatsScreen
import com.traveltracker.app.ui.viewmodel.TravelViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Add : Screen("add?recordId={recordId}")
    object Stats : Screen("stats")
    
    fun createRoute(recordId: Long? = null): String {
        return if (recordId != null) {
            "add?recordId=$recordId"
        } else {
            "add"
        }
    }
}

@Composable
fun TravelNavGraph(navController: NavHostController, viewModel: TravelViewModel = hiltViewModel()) {
    val records by viewModel.uiState.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToAdd = { navController.navigate(Screen.createRoute()) },
                onNavigateToEdit = { recordId ->
                    navController.navigate(Screen.createRoute(recordId))
                },
                onNavigateToStats = { navController.navigate(Screen.Stats.route) }
            )
        }
        composable("add?recordId={recordId}") { backStackEntry ->
            val recordId = backStackEntry.arguments?.getString("recordId")?.toLongOrNull()
            val existingRecord = records.records.find { it.id == recordId }
            AddRecordScreen(
                onNavigateBack = { navController.popBackStack() },
                existingRecord = existingRecord
            )
        }
        composable(Screen.Add.route) {
            AddRecordScreen(
                onNavigateBack = { navController.popBackStack() },
                existingRecord = null
            )
        }
        composable(Screen.Stats.route) {
            StatsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}