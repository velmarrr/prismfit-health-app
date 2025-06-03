package com.example.prismfit.home.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NutrientStat(
    value: String,
    unit: String,
    label: String
) {
    Row(verticalAlignment = Alignment.Bottom) {
        Text(
            text = value,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = " $unit",
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
    Text(
        text = label,
        style = MaterialTheme.typography.bodySmall
    )
}