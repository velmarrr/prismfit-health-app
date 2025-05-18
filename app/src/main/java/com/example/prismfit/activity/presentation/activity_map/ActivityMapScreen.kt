package com.example.prismfit.activity.presentation.activity_map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.prismfit.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun ActivityMapScreen(
    route: List<LatLng>
) {
    val cameraPositionState = rememberCameraPositionState()

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
                if (route.isNotEmpty()) {
                    val boundsBuilder = LatLngBounds.builder()
                    route.forEach { boundsBuilder.include(it) }
                    val bounds = boundsBuilder.build()
                    val padding = 250
                    cameraPositionState.move(
                        CameraUpdateFactory.newLatLngBounds(bounds, padding)
                    )
                }
            }
        ) {
            if (route.isNotEmpty()) {
                Polyline(points = route, color = Color.Blue)
                Marker(
                    state = rememberMarkerState(position = route.first()),
                    title = stringResource(R.string.start_noun)
                )
                Marker(
                    state = rememberMarkerState(position = route.last()),
                    title = stringResource(R.string.end_noun)
                )
            }
        }
    }
}
