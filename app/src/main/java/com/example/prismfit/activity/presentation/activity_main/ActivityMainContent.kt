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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.prismfit.R
import com.example.prismfit.activity.domain.model.Activity
import com.example.prismfit.activity.domain.model.ActivityType
import com.example.prismfit.activity.presentation.activity_main.components.ActivityItem
import com.example.prismfit.activity.presentation.activity_main.components.StartTrackingButton
import java.time.Instant

@Composable
fun ActivityMainContent(
    types: List<ActivityType>,
    pagerState: PagerState,
    selectedType: ActivityType,
    activities: List<Activity>,
    isLoading: Boolean,
    onAction: (ActivityAction) -> Unit,
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
                        ActivityType.WALKING -> Icons.Default.DirectionsWalk
                        ActivityType.RUNNING -> Icons.Default.DirectionsRun
                        ActivityType.CYCLING -> Icons.Default.DirectionsBike
                    },
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
                val typeLabel = when (types[page]) {
                    ActivityType.WALKING -> stringResource(R.string.walking)
                    ActivityType.RUNNING -> stringResource(R.string.running)
                    ActivityType.CYCLING -> stringResource(R.string.cycling)
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
            onStartClick = { type -> onAction(ActivityAction.OnStart(type)) }
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
                        onClick = { onAction(ActivityAction.OnActivityClick(it)) }
                    )
                }
            }
        }
    }
}
