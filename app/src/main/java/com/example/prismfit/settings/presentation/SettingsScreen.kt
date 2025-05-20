package com.example.prismfit.settings.presentation

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.prismfit.R
import com.example.prismfit.core.ui.theme.ThemePreference
import com.example.prismfit.settings.presentation.components.ThemeSwitchItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen() {
    val viewModel: SettingsViewModel = hiltViewModel()
    val context = LocalContext.current
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val theme by viewModel.themePreference.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.language), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.english))
            Switch(
                checked = currentLanguage == "en",
                onCheckedChange = {
                    if (it && currentLanguage != "en") {
                        scope.launch {
                            viewModel.onLanguageChanged("en")
                            delay(200)
                            val intent = (context as Activity).intent
                            context.finish()
                            context.startActivity(intent)
                        }
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.ukrainian))
            Switch(
                checked = currentLanguage == "uk",
                onCheckedChange = {
                    if (it && currentLanguage != "uk") {
                        scope.launch {
                            viewModel.onLanguageChanged("uk")
                            delay(200)
                            val intent = (context as Activity).intent
                            context.finish()
                            context.startActivity(intent)
                        }
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(80.dp))
        Text(stringResource(R.string.theme), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        ThemeSwitchItem(
            label = stringResource(R.string.light_theme),
            currentTheme = theme,
            switchTheme = ThemePreference.LIGHT,
            onThemeSelected = { viewModel.onThemeChanged(it) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        ThemeSwitchItem(
            label = stringResource(R.string.dark_theme),
            currentTheme = theme,
            switchTheme = ThemePreference.DARK,
            onThemeSelected = { viewModel.onThemeChanged(it) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        ThemeSwitchItem(
            label = stringResource(R.string.system_theme),
            currentTheme = theme,
            switchTheme = ThemePreference.SYSTEM,
            onThemeSelected = { viewModel.onThemeChanged(it) }
        )
    }
}
