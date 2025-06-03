package com.example.prismfit.home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.prismfit.activity.presentation.activity_main.ActivityMainViewModel
import com.example.prismfit.diet.presentation.diet_main.DietViewModel
import com.example.prismfit.navigation.ActivityGraph
import com.example.prismfit.navigation.DietGraph
import com.example.prismfit.navigation.HomeGraph
import com.example.prismfit.navigation.LocalNavController
import kotlinx.coroutines.flow.map

@Composable
fun HomeScreen() {
    val dietViewModel: DietViewModel = hiltViewModel()
    val activityViewModel: ActivityMainViewModel = hiltViewModel()
    val meals by dietViewModel.mealsFlow.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val isDietLoading by dietViewModel.isLoading.collectAsStateWithLifecycle()
    val isActivityLoading by activityViewModel.isLoading.collectAsStateWithLifecycle()
    val activities by activityViewModel.activities
        .map { list -> list.sortedByDescending { it.startTime } }
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val navController = LocalNavController.current

    val onAction: (HomeAction) -> Unit = { action ->
        when (action) {
            HomeAction.DietClick -> {
                navController.navigate(DietGraph.DietRoute) {
                    popUpTo(HomeGraph.HomeRoute) { inclusive = true }
                    launchSingleTop = true
                }
            }
            HomeAction.ActivityClick -> {
                navController.navigate(ActivityGraph.ActivityMainRoute) {
                    popUpTo(HomeGraph.HomeRoute) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            dietViewModel.getAllMeals()
        }
    }

    LaunchedEffect(Unit) {
        activityViewModel.loadActivities()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isDietLoading || isActivityLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            HomeContent(
                meals = meals,
                formatNumber = dietViewModel::formatNumber,
                activities = activities,
                formatInstant = activityViewModel::formatInstant,
                onAction = onAction
            )
        }
    }
}
