package com.example.prismfit.activity.presentation.activity_pending

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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prismfit.R
import com.example.prismfit.activity.domain.model.ActivityType
import com.example.prismfit.activity.presentation.utils.toFormattedTime
import com.example.prismfit.core.ui.theme.AppTheme
import com.example.prismfit.core.ui.theme.ThemePreference
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun PendingActivityContent(
    path: List<LatLng>,
    time: Long,
    isTracking: Boolean,
    distance: Double,
    pace: Double,
    onFinish: () -> Unit,
    selectedType: ActivityType,
    onStartTracking: () -> Unit,
    onStopTracking: (ActivityType, () -> Unit) -> Unit,
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
                        modifier = Modifier
                            .height(80.dp)
                            .widthIn(min = 80.dp)
                    ) {
                        Text(stringResource(R.string.start))
                    }
                } else {
                    Button(
                        onClick = { onStopTracking(selectedType, onFinish) },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .height(80.dp)
                            .widthIn(min = 80.dp)
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
            selectedType = ActivityType.RUNNING,
            onStartTracking = {},
            onStopTracking = { _, _ -> },
            cameraPositionState = CameraPositionState()
        )
    }
}