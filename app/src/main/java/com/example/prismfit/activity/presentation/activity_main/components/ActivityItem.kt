package com.example.prismfit.activity.presentation.activity_main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prismfit.R
import com.example.prismfit.activity.data.model.Activity
import com.example.prismfit.activity.presentation.utils.toFormattedTime
import com.example.prismfit.core.ui.theme.AppTheme
import com.example.prismfit.core.ui.theme.ThemePreference
import java.time.Instant

@Composable
fun ActivityItem(
    activity: Activity,
    formatInstant: (Instant) -> String,
    onClick: (Activity) -> Unit
) {
    Card(
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(activity) }
                .padding(12.dp)
        ) {
            val typeLabel = when (activity.type) {
                "walking" -> stringResource(R.string.walking)
                "running" -> stringResource(R.string.running)
                else -> stringResource(R.string.cycling)
            }
            Text(
                text = typeLabel,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Text(
                text = "${formatInstant(activity.startTime)} - ${formatInstant(activity.endTime)}",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Row(
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(stringResource(R.string.duration) + ": ", fontWeight = FontWeight.SemiBold)
                Icon(Icons.Default.Timer, contentDescription = stringResource(R.string.duration))
                Text("  " + activity.durationSeconds.toFormattedTime())
            }
            Row(
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(stringResource(R.string.distance) + ": ", fontWeight = FontWeight.SemiBold)
                Icon(Icons.Default.Straighten, contentDescription = stringResource(R.string.distance))
                Text("  " + "${"%.2f".format(activity.distanceMeters / 1000.0)} "
                        + stringResource(R.string.kilometers_short))
            }
            Row(
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(stringResource(R.string.average_speed) + ": ", fontWeight = FontWeight.SemiBold)
                Icon(Icons.Default.Speed, contentDescription = stringResource(R.string.average_speed))
                Text("  " + "${"%.2f".format(activity.averageMetersPerHour / 1000.0)} "
                        + stringResource(R.string.kilometers_per_hour_short)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivityItemPreview() {
    AppTheme(themePreference = ThemePreference.SYSTEM) {
        ActivityItem(
            activity = Activity(
                id = "1",
                type = "Cycling",
                startTime = Instant.parse("2025-05-18T14:20:00Z"),
                endTime = Instant.parse("2025-05-18T14:40:00Z"),
                durationSeconds = 1200,
                distanceMeters = 5000,
                averageMetersPerHour = 25000,
                route = listOf()
            ),
            formatInstant = { it.toString() },
            onClick = {}
        )
    }
}