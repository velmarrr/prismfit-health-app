package com.example.prismfit.settings.presentation

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.prismfit.R
import com.example.prismfit.core.ui.theme.ThemePreference
import com.example.prismfit.settings.presentation.components.SingleChoiceSegmentedButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen() {
    val viewModel: SettingsViewModel = hiltViewModel()
    val context = LocalContext.current
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val theme by viewModel.themePreference.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    SettingsContent(
        currentLanguage = currentLanguage,
        onLanguageSelected = { selected ->
            if (selected != currentLanguage) {
                scope.launch {
                    viewModel.onLanguageChanged(selected)
                    delay(200)
                    val intent = (context as Activity).intent
                    context.finish()
                    context.startActivity(intent)
                }
            }
        },
        theme = theme,
        onThemeSelected = { viewModel.onThemeChanged(it) }
    )
}

@Composable
fun SettingsContent(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    theme: ThemePreference,
    onThemeSelected: (ThemePreference) -> Unit
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
    }
}
