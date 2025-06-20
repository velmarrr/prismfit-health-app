package com.example.prismfit.core.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prismfit.core.session.LocalSessionManager
import com.example.prismfit.auth.presentation.login.LoginScreen
import com.example.prismfit.auth.presentation.registration.RegistrationScreen
import com.example.prismfit.core.network.NetworkViewModel
import com.example.prismfit.navigation.LoginGraph.LoginRoute
import com.example.prismfit.navigation.RegisterGraph.RegisterRoute

@Composable
fun PrismFitApp() {
    val sessionManager = LocalSessionManager.current
    val isLoggedIn by sessionManager.isLoggedIn.collectAsState()
    val navController = rememberNavController()
    val networkViewModel: NetworkViewModel = hiltViewModel()
    val isConnected by networkViewModel.isConnected.collectAsStateWithLifecycle()

    if (isLoggedIn) {
        PrismFitAppContent(
            navController = navController,
            isConnected = isConnected
        )
    } else {
        NavHost(
            navController = navController,
            startDestination = LoginRoute,
            modifier = Modifier.fillMaxSize()
        ) {
            composable<LoginRoute> { LoginScreen(navController) }
            composable<RegisterRoute> { RegistrationScreen(navController) }
        }
    }
}
