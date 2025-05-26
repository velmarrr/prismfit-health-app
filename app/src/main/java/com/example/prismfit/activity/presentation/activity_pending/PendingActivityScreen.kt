package com.example.prismfit.activity.presentation.activity_pending

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.prismfit.R
import com.example.prismfit.activity.domain.model.ActivityType
import com.example.prismfit.activity.service.LocationService
import com.example.prismfit.activity.service.ServiceActions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun PendingActivityScreen(
    onFinish: () -> Unit,
    selectedType: ActivityType
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
