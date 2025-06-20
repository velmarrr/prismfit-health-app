package com.example.prismfit.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.prismfit.R

@Composable
fun NetworkStatusBanner(
    isConnected: Boolean,
    modifier: Modifier = Modifier
) {

    AnimatedVisibility(
        visible = !isConnected,
        enter = slideInVertically(initialOffsetY = { -it }, animationSpec = tween(300)),
        exit = slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(300))
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.errorContainer)
                .padding(12.dp)
        ) {
            Text(
                text = stringResource(R.string.no_internet_connection),
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
    }
}