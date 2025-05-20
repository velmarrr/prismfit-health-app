package com.example.prismfit.activity.presentation.activity_pending

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.prismfit.R
import com.example.prismfit.activity.presentation.utils.toFormattedTime
import com.example.prismfit.activity.service.LocationService
import com.example.prismfit.activity.service.ServiceActions
import com.example.prismfit.core.ui.theme.AppTheme
import com.example.prismfit.core.ui.theme.ThemePreference
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun PendingActivityScreen(
    onFinish: () -> Unit,
    selectedType: String
) {
    val viewModel: PendingActivityViewModel = hiltViewModel()
    val context = LocalContext.current
    val path by viewModel.path.collectAsStateWithLifecycle()
    val time by viewModel.elapsedTime.collectAsStateWithLifecycle()
    val isTracking by viewModel.isTracking.collectAsStateWithLifecycle()
    val distance by viewModel.distance.collectAsStateWithLifecycle()
    val pace by viewModel.pace.collectAsStateWithLifecycle()
    val showDialog by viewModel.showBackDialog.collectAsStateWithLifecycle()
    val cameraPositionState = rememberCameraPositionState()

    BackHandler(enabled = isTracking) {
        viewModel.triggerBackDialog()
    }

    LaunchedEffect(path) {
        path.lastOrNull()?.let {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(it, 17f),
                durationMs = 1000
            )
        }
    }

    LaunchedEffect(Unit) {
        context.startService(Intent(context, LocationService::class.java).apply {
            action = ServiceActions.ACTION_START.name
        })

        LocationService.locationFlow.collect { locations ->
            viewModel.updateLocation(locations)
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissBackDialog() },
            title = { Text(stringResource(R.string.exit_confirmation)) },
            text = { Text(stringResource(R.string.activity_exit_warning)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.dismissBackDialog()
                    onFinish()
                }) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.dismissBackDialog()
                }) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }

    PendingActivityContent(
        path = path,
        time = time,
        isTracking = isTracking,
        distance = distance,
        pace = pace,
        onFinish = onFinish,
        selectedType = selectedType,
        onStartTracking = viewModel::startTracking,
        onStopTracking = viewModel::stopTracking,
        cameraPositionState = cameraPositionState
    )
}

@Composable
fun PendingActivityContent(
    path: List<LatLng>,
    time: Long,
    isTracking: Boolean,
    distance: Double,
    pace: Double,
    onFinish: () -> Unit,
    selectedType: String,
    onStartTracking: () -> Unit,
    onStopTracking: (String, () -> Unit) -> Unit,
    cameraPositionState: CameraPositionState
) {
    Column(Modifier.fillMaxSize()) {
        Text(
            text = "%.2f".format(distance / 1000.0),
            fontWeight = FontWeight.Bold,
            fontSize = 48.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp)
        )
        Text(
            text = stringResource(R.string.kilometers_short),
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally)
                .padding(top = 40.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = time.toFormattedTime(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.elapsed_time),
                    fontSize = 18.sp
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = if (pace > 0) "%.2f".format(pace) else "-",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Text(
                        if (pace > 0)
                            " " + stringResource(R.string.kilometers_per_hour_short).format(pace)
                        else
                            ""
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.average_speed),
                    fontSize = 18.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState
            ) {
                if (path.isNotEmpty()) {
                    Polyline(points = path, color = Color.Red, width = 8f)
                    Marker(
                        state = rememberMarkerState(position = path.first()),
                        title = stringResource(R.string.start_noun)
                    )
                    Marker(
                        state = rememberMarkerState(position = path.last()),
                        title = stringResource(R.string.now)
                    )
                }
            }
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                if (!isTracking) {
                    Button(
                        onClick = onStartTracking,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.height(80.dp).widthIn(min = 80.dp)
                    ) {
                        Text(stringResource(R.string.start))
                    }
                } else {
                    Button(
                        onClick = { onStopTracking(selectedType, onFinish) },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.height(80.dp).widthIn(min = 80.dp)
                    ) {
                        Text(stringResource(R.string.stop))
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PendingActivityPreview() {
    AppTheme(themePreference = ThemePreference.SYSTEM) {
        PendingActivityContent(
            path = listOf(),
            time = 2002,
            isTracking = false,
            distance = 566.0,
            pace = 5.6,
            onFinish = {},
            selectedType = "Running",
            onStartTracking = {},
            onStopTracking = { _, _ -> },
            cameraPositionState = CameraPositionState()
        )
    }
}