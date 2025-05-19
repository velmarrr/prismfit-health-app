package com.example.prismfit.activity.presentation.activity_main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.prismfit.R
import com.example.prismfit.activity.data.model.Activity
import com.example.prismfit.activity.presentation.activity_main.components.ActivityItem
import com.example.prismfit.activity.presentation.activity_main.components.StartTrackingButton
import kotlinx.coroutines.flow.map
import java.time.Instant

@Composable
fun ActivityMainScreen(
    onStartClick: (String) -> Unit,
    onActivityClick: (Activity) -> Unit
) {
    val viewModel: ActivityMainViewModel = hiltViewModel()
    val types = listOf("walking", "running", "cycling")
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

@Composable
fun ActivityMainContent(
    types: List<String>,
    pagerState: PagerState,
    selectedType: String,
    activities: List<Activity>,
    isLoading: Boolean,
    onStartClick: (String) -> Unit,
    onActivityClick: (Activity) -> Unit,
    formatInstant: (Instant) -> String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp, top = 20.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) { page ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = when (types[page]) {
                        "walking" -> Icons.Default.DirectionsWalk
                        "running" -> Icons.Default.DirectionsRun
                        else -> Icons.Default.DirectionsBike
                    },
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
                val typeLabel = when (types[page]) {
                    "walking" -> stringResource(R.string.walking)
                    "running" -> stringResource(R.string.running)
                    "cycling" -> stringResource(R.string.cycling)
                    else -> types[page]
                }
                Text(
                    text = typeLabel,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        StartTrackingButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            selectedType = selectedType,
            onStartClick = onStartClick
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn {
                items(activities) { activity ->
                    ActivityItem(
                        activity = activity,
                        formatInstant = formatInstant,
                        onClick = { onActivityClick(activity) }
                    )
                }
            }
        }
    }
}
