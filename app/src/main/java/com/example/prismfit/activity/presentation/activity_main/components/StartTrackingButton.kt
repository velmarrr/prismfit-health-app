package com.example.prismfit.activity.presentation.activity_main.components

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prismfit.R
import com.example.prismfit.activity.service.utils.hasLocationPermissions

@Composable
fun StartTrackingButton(
    modifier: Modifier,
    selectedType: String,
    onStartClick: (String) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        if (granted) {
            onStartClick(selectedType)
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.location_permission_requirement),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Button(
        onClick = {
            if (hasLocationPermissions(context)) {
                onStartClick(selectedType)
            } else {
                launcher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        val startText = when (selectedType) {
            "walking" -> stringResource(R.string.start_walking)
            "running" -> stringResource(R.string.start_running)
            "cycling" -> stringResource(R.string.start_cycling)
            else -> stringResource(R.string.start) + " $selectedType"
        }
        Text(startText, fontSize = 16.sp)
    }
}