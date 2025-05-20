package com.example.prismfit.settings.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.prismfit.core.ui.theme.ThemePreference

@Composable
fun ThemeSwitchItem(
    label: String,
    currentTheme: ThemePreference,
    switchTheme: ThemePreference,
    onThemeSelected: (ThemePreference) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label)
        Switch(
            checked = currentTheme == switchTheme,
            onCheckedChange = { isChecked ->
                if (isChecked) {
                    onThemeSelected(switchTheme)
                }
            }
        )
    }
}