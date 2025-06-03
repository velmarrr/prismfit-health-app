package com.example.prismfit.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CustomColors(
    val purpleContainer: Color,
    val greenContainer: Color
)

val LocalCustomColors = staticCompositionLocalOf {
    CustomColors(
        purpleContainer = Color.Unspecified,
        greenContainer = Color.Unspecified
    )
}

val MaterialTheme.customColors: CustomColors
    @Composable
    get() = LocalCustomColors.current