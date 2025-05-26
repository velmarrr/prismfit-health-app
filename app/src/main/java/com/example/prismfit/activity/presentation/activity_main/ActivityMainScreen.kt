package com.example.prismfit.activity.presentation.activity_main

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.prismfit.activity.domain.model.Activity
import com.example.prismfit.activity.domain.model.ActivityType
import kotlinx.coroutines.flow.map

@Composable
fun ActivityMainScreen(
    onStartClick: (ActivityType) -> Unit,
    onActivityClick: (Activity) -> Unit
) {
    val viewModel: ActivityMainViewModel = hiltViewModel()
    val types = ActivityType.entries
    val pagerState = rememberPagerState(
        pageCount = { types.size }
    )
    val selectedType = types[pagerState.currentPage]
    val activities by viewModel.activities
        .map { list -> list.sortedByDescending { it.startTime } }
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(pagerState.currentPage) {
        viewModel.selectType(selectedType)
    }

    LaunchedEffect(Unit) {
        viewModel.loadActivities()
    }

    ActivityMainContent(
        types = types,
        pagerState = pagerState,
        selectedType = selectedType,
        activities = activities,
        isLoading = isLoading,
        onStartClick = onStartClick,
        onActivityClick  = onActivityClick,
        formatInstant = viewModel::formatInstant
    )
}
