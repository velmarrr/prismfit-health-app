package com.example.prismfit.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.prismfit.R
import com.example.prismfit.core.session.LocalSessionManager
import com.example.prismfit.core.session.SessionManager
import com.example.prismfit.navigation.LoginGraph.LoginRoute
import com.example.prismfit.navigation.ProfileGraph.ProfileRoute
import com.example.prismfit.core.ui.theme.AppTheme
import com.example.prismfit.core.ui.theme.ThemePreference

@Composable
fun ProfileScreen(
    navController: NavController,
    sessionManager: SessionManager = LocalSessionManager.current,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val isLoggedIn by sessionManager.isLoggedIn.collectAsStateWithLifecycle()
    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            navController.navigate(LoginRoute) {
                popUpTo(ProfileRoute) { inclusive = true }
            }
        }
    }

    ProfileContent(
        onLogoutClick = { viewModel.logout() }
    )
}

@Composable
fun ProfileContent(onLogoutClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 20.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
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

@Preview(showSystemUi = true)
@Composable
fun ProfilePreview() {
    AppTheme(themePreference = ThemePreference.SYSTEM) {
        ProfileContent(
            onLogoutClick = {}
        )
    }
}