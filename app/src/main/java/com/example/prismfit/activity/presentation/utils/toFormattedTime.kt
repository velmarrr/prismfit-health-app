package com.example.prismfit.activity.presentation.utils

import java.util.Locale

fun Long.toFormattedTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60
    return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
}