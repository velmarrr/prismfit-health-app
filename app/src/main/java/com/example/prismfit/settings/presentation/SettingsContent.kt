package com.example.prismfit.settings.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.prismfit.R
import com.example.prismfit.core.ui.theme.ThemePreference
import com.example.prismfit.settings.presentation.components.SingleChoiceSegmentedButton

@Composable
fun SettingsContent(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    theme: ThemePreference,
    onThemeSelected: (ThemePreference) -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.language), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        SingleChoiceSegmentedButton(
            options = listOf("en", "uk"),
            selectedOption = currentLanguage,
            onOptionSelected = onLanguageSelected,
            labelMapper = { code ->
                when (code) {
                    "en" -> stringResource(R.string.english)
                    "uk" -> stringResource(R.string.ukrainian)
                    else -> code
                }
            }
        )
        Spacer(modifier = Modifier.height(60.dp))
        Text(stringResource(R.string.theme), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        SingleChoiceSegmentedButton(
            options = ThemePreference.entries.toList(),
            selectedOption = theme,
            onOptionSelected = onThemeSelected,
            labelMapper = {
                when (it) {
                    ThemePreference.LIGHT -> stringResource(R.string.light_theme)
                    ThemePreference.DARK -> stringResource(R.string.dark_theme)
                    ThemePreference.SYSTEM -> stringResource(R.string.system_theme)
                }
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onLogoutClick,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.logout))
        }
    }
}
