package com.example.prismfit.settings.presentation

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.prismfit.core.session.LocalSessionManager
import com.example.prismfit.navigation.LoginGraph.LoginRoute
import com.example.prismfit.navigation.SettingsGraph.SettingsRoute

@Composable
fun SettingsScreen(
    navController: NavController
) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val context = LocalContext.current
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val theme by viewModel.themePreference.collectAsStateWithLifecycle()
    val sessionManager = LocalSessionManager.current
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val isLoggedIn by sessionManager.isLoggedIn.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            navController.navigate(LoginRoute) {
                popUpTo(SettingsRoute) { inclusive = true }
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.languageChanged.collect {
            val intent = (context as Activity).intent
            context.finish()
            context.startActivity(intent)
        }
    }
    LaunchedEffect(errorMessage) {
        errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg.asString(context),
                withDismissAction = true
            )
            viewModel.clearError()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        SettingsContent(
            currentLanguage = currentLanguage,
            onLanguageSelected = { selected ->
                if (selected != currentLanguage) {
                    viewModel.onLanguageChanged(selected)
                }
            },
            theme = theme,
            onThemeSelected = { viewModel.onThemeChanged(it) },
            onLogoutClick = { viewModel.logout() }
        )
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}
